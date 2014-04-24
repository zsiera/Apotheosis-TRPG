package fer.ai;

import fer.Cursor;
import fer.Game;
import fer.Tile;
import fer.Unit;
import fer.gameplay.BattleProcessor;
import fer.gameplay.MapGoal;
import fer.gameplay.Weapon;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Evan Stewart A class containing a set of methods that act upon a map
 * in order to process computer player movement. Each player contains a set of
 * parameters that determine how it will act based on certain circumstances.
 */
public class AIPlayer implements Runnable {

    private int faction = 1;
    //A coefficient that corresponds to the probabiliy that units will stay 
    //together as a group rather than spread out.  Between 0 and 1.
    private float cohesiveness = 1;
    //A coefficient corresponding to the tendency for units to attack 
    //enemies within their range.
    private float aggressiveness = 1;
    //The chance of death by engagement the player is willing to tolerate before
    //the engaging unit is considered a threat
    private double tolerableDeathChance = 0.5;
    //The ratio of damage dealt to damage taken the player is willing to tolerate
    //for potential engagements
    private double tolerableDamageRatio = 1;
    //The number of tasks currently assigned to friendly units
    private int tasksAssigned = 0;
    private boolean takingTurn = false;
    private boolean tasksReady;
    private int stage;
    private int updates;
    private int currentTaskIndex;
    private ArrayList<AITask> tasks;
    private Thread aiThread;
    private Tile destination;
    private AITask currentTask;
    private Cursor cursor;
    private boolean determinedAttack;
    private boolean canAttack;
    private Unit attackTarget;

    public AIPlayer(int faction) {
        this.faction = faction;
    }

    public void startTurn() {
        cursor = Cursor.getCursor();
        cursor.setVisible(false);
        takingTurn = true;
        stage = 0;
        updates = 0;
        currentTaskIndex = 0;
        tasks = new ArrayList();
        tasksReady = false;
        destination = null;
        determinedAttack = false;
        canAttack = false;
        aiThread = new Thread(this);
        aiThread.start();
    }

    public void update() {
        switch (stage) {
            case 0: //Wait for the thread to finish task generation
                if (tasksReady) {
                    stage++;
                }
                break;
            case 1: //Select and center a unit
                if (tasks.get(currentTaskIndex).getAssignedIndex() == -1) {
                    stage = 6; //TODO: Check
                } else {
                    aiThread = new Thread(this);
                    aiThread.start();
                    currentTask = tasks.get(currentTaskIndex);
                    cursor.setMapLocation(Game.getCurrentMap().getUnit(currentTask.
                        getAssignedIndex()).getMapx(),Game.getCurrentMap().
                        getUnit(currentTask.getAssignedIndex()).getMapy());
                    cursor.centerCursor();
                    Game.getCurrentMap().getUnit(currentTask.getAssignedIndex()).
                            resetAnimation(1);
                    Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
                            .setActiveMapAnimation(1);
                    updates = 0;
                    stage++;
                }
                break;
            case 2: //Animate the selected unit and wait for the ai thread
                if (updates >= Game.getCurrentMap().getUnit(currentTask.
                        getAssignedIndex()).getActiveAnimationFrames() * 2
                        && destination != null) {
                    updates = 0;
                    stage++;
                }
                break;
            case 3: //Move the unit to the destination tile
                Game.getCurrentMap().getUnit(currentTask.getAssignedIndex()).
                        setMapx(destination.getMapX());
                Game.getCurrentMap().getUnit(currentTask.getAssignedIndex()).
                        setMapy(destination.getMapY());
                cursor.setMapLocation(Game.getCurrentMap().getUnit(currentTask.
                        getAssignedIndex()).getMapx(),Game.getCurrentMap().
                        getUnit(currentTask.getAssignedIndex()).getMapy());
                cursor.centerCursor();
                Game.getCurrentMap().getUnit(currentTask.getAssignedIndex()).
                        resetAnimation(3);
                Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
                        .setActiveMapAnimation(3);
                updates = 0;
                stage++;
            case 4: //Determine and take further action
                if (currentTask.getType() == AITask.TaskType.ATTACK_UNIT) {
                    aiThread = new Thread(this);
                    aiThread.start();
                    stage = 5;
                } else {
                    stage = 7;
                }
                break;
            case 5: //Check if can attack and start battle
                if (determinedAttack) {
                    if (canAttack) {
                        Game.getBattleProcessor().startBattle(Game.
                                getCurrentMap().getUnit(currentTask.
                                getAssignedIndex()), attackTarget);
                    }
                    stage++;
                }
                break;
            case 6: //Wait for battle processor
                if (!Game.getBattleProcessor().isInCombat()) {
                    stage++;
                }
                break;
            case 7: //Reset values and loop if more tasks
                Game.getCurrentMap().getUnit(currentTask.getAssignedIndex()).
                        resetAnimation(0);
                Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
                        .setActiveMapAnimation(0);
                Game.getCurrentMap().getUnit(currentTask.getAssignedIndex()).
                        setMoved(true);
                destination = null;
                determinedAttack = false;
                canAttack = false;
                currentTaskIndex++;
                System.out.println("TASK SIZE: " + tasks.size());
                if (currentTaskIndex >= tasks.size()) {
                    stage++;
                } else {
                    stage = 1;
                }
                break;
            case 8: //End the turn
                endTurn();
                break;
        }
        updates++;
    }

    public void endTurn() {
        takingTurn = false;
        cursor.setVisible(true);
        cursor.endTurn();
    }

    public ArrayList<AITask> generateAITasks() {
        ArrayList<AITask> tasks = new ArrayList();
        //Generate attack unit tasks for every enemy unit on the map
        for (int i = 0; i < Game.getCurrentMap().getNumUnits(); i++) {
            if (Game.getCurrentMap().getUnit(i).getFaction() != faction && !Game
                    .getCurrentMap().getUnit(i).isDead()) {
                tasks.add(new AITask(i));
            }
        }
        //Generate go to tile tasks for any tiles of interest
        if (Game.getCurrentMap().getFactionGoals()[faction].getType() == MapGoal.GoalType.REACH_TILE) {
            //Add a task to reach the goal tile
            tasks.add(new AITask(Game.getCurrentMap().getFactionGoals()[faction].
                    getTileX(), Game.getCurrentMap().getFactionGoals()[faction].
                    getTileY()));
        }
        System.out.println("Num Tasks: " + tasks.size());
        return tasks;
    }

    public void prioritizeAITasks(ArrayList<AITask> tasks) {
        BattleProcessor bp = Game.getBattleProcessor();
        for (int i = 0; i < tasks.size(); i++) {
            int priority = 1;
            if (tasks.get(i).getType() == AITask.TaskType.ATTACK_UNIT) {
                ArrayList<Unit> attackableUnits = getAttackableUnits(Game.getCurrentMap().getUnit(tasks.get(i).getTargetIndex()));
                //Add priority if the unit is part of the faction goal
                if (Game.getCurrentMap().getFactionGoals()[faction].getType() == MapGoal.GoalType.ELIMINATE_TARGETS) {
                    for (int j = 0; j < Game.getCurrentMap().getFactionGoals()[faction].getTargets().length; j++) {
                        if (tasks.get(i).getTargetIndex() == Game.getCurrentMap().getFactionGoals()[faction].getTargets()[j]) {
                            priority += 10;
                            break;
                        }
                    }
                }
                //Add priority for at-risk friendly units
                for (int j = 0; j < attackableUnits.size(); j++) {
                    priority++;
                    //If the unit is part of the enemy faction's goal, units that put it at risk are prioritized targets
                    //Units that are capable of killing friendly units within a turn also gain priority
                    if (Game.getCurrentMap().getFactionGoals()[Game.getCurrentMap().getUnit(tasks.get(i).getTargetIndex()).getFaction()].getType() == MapGoal.GoalType.ELIMINATE_TARGETS) {
                        boolean goalTarget = true;
                        for (int k = 0; k < Game.getCurrentMap().getFactionGoals()[Game.getCurrentMap().getUnit(tasks.get(i).getTargetIndex()).getFaction()].getTargets().length; k++) {
                            goalTarget &= (attackableUnits.get(j).getMapIndex() == Game.getCurrentMap().getFactionGoals()[Game.getCurrentMap().getUnit(tasks.get(i).getTargetIndex()).getFaction()].getTargets()[k]);
                        }
                        if (goalTarget) {
                            priority += 2;
                            if (bp.calculateDeathChance(attackableUnits.get(j), Game.getCurrentMap().getUnit(tasks.get(i).getTargetIndex()), false) > tolerableDeathChance) {
                                priority += 6;
                            }
                        } else {
                            if (bp.calculateDeathChance(attackableUnits.get(j), Game.getCurrentMap().getUnit(tasks.get(i).getTargetIndex()), false) > tolerableDeathChance) {
                                priority += 2;
                            }
                        }
                    } else {
                        if (bp.calculateDeathChance(attackableUnits.get(j), Game.getCurrentMap().getUnit(tasks.get(i).getTargetIndex()), false) > tolerableDeathChance) {
                            priority += 2;
                        }
                    }
                }
            } else if (tasks.get(i).getType() == AITask.TaskType.GO_TO_TILE) {
                //Check if the target tile is a map goal for this faction
                for (int j = 0; j < Game.getCurrentMap().getFactionGoals().length; j++) {
                    if (Game.getCurrentMap().getFactionGoals()[j].getFaction()
                            == faction && Game.getCurrentMap().
                            getFactionGoals()[j].getType() == MapGoal.GoalType.REACH_TILE) {
                        if (Game.getCurrentMap().getFactionGoals()[j].getTileX()
                                == tasks.get(i).getMapx() && Game.
                                getCurrentMap().getFactionGoals()[j].getTileY()
                                == tasks.get(i).getMapx()) {
                            priority += 10;
                        }
                    }
                }
            }
            tasks.get(i).setPriority(priority);
        }
    }

    public int determineUnitSuitability(Unit unit, AITask task, boolean secondRun) {
        int suitability = 1;
        BattleProcessor bp = Game.getBattleProcessor();
        if (task.getType() == AITask.TaskType.ATTACK_UNIT) {
            //Check this unit's manhattan distance from the target
            suitability += (int) ((Math.abs(unit.getMapx() - Game.getCurrentMap()
                    .getUnit(task.getTargetIndex()).getMapx()) / Game.
                    getCurrentMap().getWidth()) * 10);
            suitability += (int) ((Math.abs(unit.getMapy() - Game.getCurrentMap()
                    .getUnit(task.getTargetIndex()).getMapy()) / Game.
                    getCurrentMap().getHeight()) * 10);
            System.out.println("Dist suit");
            //Check if this unit can kill the target unit or damage it enough to be worth the risk
            //TODO: Improve to take speed, hit chance, etc. into account
            if (canAttack(unit, Game.getCurrentMap().getUnit(task.getTargetIndex()))) {
                System.out.println("Can attack");
                suitability += 10;
                System.out.println("Attack damage attacker: " + (bp.calculateAttackDamage(unit, Game.getCurrentMap().
                        getUnit(task.getTargetIndex()))));
                System.out.println("Attack damage defender: " + (bp.
                        calculateAttackDamage(Game.getCurrentMap().
                        getUnit(task.getTargetIndex()), unit)));
                System.out.println("DAMAGE RATIO: " + (float) ((float) bp.calculateAttackDamage(unit, Game.getCurrentMap().
                        getUnit(task.getTargetIndex())) / (float) bp.
                        calculateAttackDamage(Game.getCurrentMap().
                        getUnit(task.getTargetIndex()), unit)));
                if ((float) ((float) bp.calculateAttackDamage(unit, Game.getCurrentMap().
                        getUnit(task.getTargetIndex())) / (float) bp.
                        calculateAttackDamage(Game.getCurrentMap().
                        getUnit(task.getTargetIndex()), unit))
                        >= tolerableDamageRatio) {
                    System.out.println("Tolerable damage ratio");
                    suitability += 10;
                }
                System.out.println("DEATH CHANCE: " + bp.calculateDeathChance(unit, Game.getCurrentMap().getUnit(task.getTargetIndex()), true));
                if (bp.calculateDeathChance(unit, Game.getCurrentMap().getUnit(task.getTargetIndex()), true) < tolerableDeathChance) {
                    System.out.println("Tolerable death chance");
                    suitability += 10;
                } else if (!secondRun) {
                    suitability = 0;
                }
            }
        } else if (task.getType() == AITask.TaskType.GO_TO_TILE) {
            //Check this unit's manhattan distance from the target
            suitability += (int) ((Math.abs(unit.getMapx() - task.getMapx())
                    / Game.getCurrentMap().getWidth()) * 100);
            suitability += (int) ((Math.abs(unit.getMapy() - task.getMapy())
                    / Game.getCurrentMap().getHeight()) * 100);
        }
        System.out.println("Suitability: " + suitability);
        return suitability;
    }

    public void assignTasks(ArrayList<AITask> tasks) {
        //Cycle through each task and find the most suitable unit.  End when
        //all units have been assigned a task or when tasks have been exhausted   
        int numUnits = 0;
        ArrayList<Integer> assignedNumbers = new ArrayList();
        for (int i = 0; i < Game.getCurrentMap().getNumUnits(); i++) {
            if (!Game.getCurrentMap().getUnit(i).isDead() && Game.
                    getCurrentMap().getUnit(i).getFaction() == faction) {
                numUnits++;
            }
        }
        System.out.println("Num units: " + numUnits);
        boolean secondRun = false;
        while (tasksAssigned < numUnits) {
            for (int i = 0; i < tasks.size(); i++) {
                if (tasksAssigned >= numUnits) {
                    break;
                }
                if (tasks.get(i).getAssignedIndex() == -1 || secondRun) {
                    int bestScore = 0;
                    int bestScoreIndex = -1;
                    for (int j = 0; j < Game.getCurrentMap().getNumUnits(); j++) {
                        if ((!Game.getCurrentMap().getUnit(j).isDead()) && Game.
                                getCurrentMap().getUnit(j).getFaction() == faction) {
                            System.out.println("Faction: " + Game.getCurrentMap().getUnit(j).getFaction());
                            int score = determineUnitSuitability(Game.
                                    getCurrentMap().getUnit(j), tasks.get(i), secondRun);
                            System.out.println("Done prob");
                            if (score > bestScore && !assignedNumbers.contains(j)) {
                                bestScore = score;
                                bestScoreIndex = j;
                                System.out.println("Suitability: " + bestScore + " Index: " + bestScoreIndex + " Target: " + tasks.get(i).getTargetIndex());
                            }
                        }
                    }
                    if (secondRun) {
                        AITask taskCopy;
                        if (tasks.get(i).getType() == AITask.TaskType.ATTACK_UNIT) {
                            taskCopy = new AITask(tasks.get(i).getTargetIndex());
                            taskCopy.setPriority(tasks.get(i).getPriority());
                        } else { //GO_TO_TILE
                            taskCopy = new AITask(tasks.get(i).getMapx(), tasks.get(i).getMapy());
                            taskCopy.setPriority(tasks.get(i).getPriority());
                        }
                        taskCopy.setAssignedIndex(bestScoreIndex);
                        tasks.add(taskCopy);
                    } else {
                        tasks.get(i).setAssignedIndex(bestScoreIndex);
                    }
                    if (bestScoreIndex != -1) {
                        assignedNumbers.add(bestScoreIndex);
                        tasksAssigned++;
                    }
                }
            }
            secondRun = true;
        }

        System.out.println("Tasks assigned: " + tasksAssigned);
        tasksAssigned = 0;
    }

    public void takeTurn() {
        System.out.println("Beginning turn");
        ArrayList<AITask> tasks = generateAITasks();
        System.out.println("Prioritizing tasks");
        prioritizeAITasks(tasks);
        //Sort tasks
        System.out.println("Sorting tasks");
        Comparator comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                AITask t1 = (AITask) o1;
                AITask t2 = (AITask) o2;
                if (t1.getPriority() < t2.getPriority()) {
                    return -1;
                } else if (t1.getPriority() == t2.getPriority()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };
        PriorityQueue<AITask> sortedTasks = new PriorityQueue(tasks.size(), comp);
        for (int i = 0; i < sortedTasks.size(); i++) {
            sortedTasks.add(tasks.get(i));
        }
        for (int i = 0; i < sortedTasks.size(); i++) {
            tasks.add(sortedTasks.poll());
        }
        System.out.println("Assigning tasks");
        //Assign tasks
        assignTasks(tasks);
        //Move units
        System.out.println("Moving units");
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getAssignedIndex() != -1) {
                moveUnit(tasks.get(i));
                System.out.println("Unit moved");
            }
        }
    }

    public void moveUnit(AITask task) {
        //Determine the best course of action for the unit to take and execute
        //it
        System.out.println("Moving unit");
        PathFinder pf = new PathFinder();
        pf.setUnitCollision(false);
        BattleProcessor bp = Game.getBattleProcessor();
        Unit unit = Game.getCurrentMap().getUnit(task.getAssignedIndex());
        if (task.getType() == AITask.TaskType.ATTACK_UNIT) {
            Unit target = Game.getCurrentMap().getUnit(task.getTargetIndex());
            ArrayList<Tile> shortestPath = pf.getShortestPathAStar(Game.
                    getCurrentMap(), unit, Game.getCurrentMap().getTile(unit.
                    getMapx() + unit.getMapy() * Game.getCurrentMap().
                    getWidth()), Game.getCurrentMap().getTile(target.getMapx()
                    + target.getMapy() * Game.getCurrentMap().getWidth()));
            for (Tile t : shortestPath) {
                System.out.println("X: " + t.getMapX());
                System.out.println("Y: " + t.getMapY());
            }
            int range = 0;
            for (int i = 0; i < unit.getWeapons().length; i++) {
                if (unit.getWeapon(i) != null) {
                    if (unit.getWeapon(i).getRange() > range) {
                        range = unit.getWeapon(i).getRange();
                    }
                }
            }
            Cursor.getCursor().setMapLocation(unit.getMapx(), unit.getMapy());
            Cursor.getCursor().centerCursor();
            long time = System.currentTimeMillis();
            while (System.currentTimeMillis() - time < 1000) {
            }
            if (shortestPath.size() >= range) {
                System.out.println("Pathsize: " + shortestPath.size());
                System.out.println("Range: " + range);
                //Likely outside of range, move the unit
                int cost = 0;
                int tile = shortestPath.size() - 1;
                for (int i = shortestPath.size() - 2; i >= 0; i--) {
                    if (cost + unit.getUnitClass().getMoveCost(shortestPath.get(i).
                            getTerrain()) <= unit.getMov()) {
                        cost += unit.getUnitClass().getMoveCost(shortestPath.get(i).
                                getTerrain());
                        if (cost == unit.getMov()) {
                            tile = i;
                            break;
                        }
                    } else {
                        tile = i + 1;
                        break;
                    }
                }
                unit.setMapx(shortestPath.get(tile).getMapX());
                unit.setMapy(shortestPath.get(tile).getMapY());
                Cursor.getCursor().setMapLocation(unit.getMapx(), unit.getMapy());
                Cursor.getCursor().centerCursor();
                time = System.currentTimeMillis();
                while (System.currentTimeMillis() - time < 1000) {
                }
            }
            for (int i = 0; i < unit.getWeapons().length; i++) {
                //Test each weapon's range
                if (unit.getWeapon(i) != null) {
                    ArrayList<Tile> attackableTiles = pf.getAttackableTiles(unit, unit.
                            getMapx(), unit.getMapy(), Game.getCurrentMap(), i);
                    if (attackableTiles.contains(Game.getCurrentMap().
                            getTile(target.getMapx() + target.getMapy() * Game.
                            getCurrentMap().getWidth()))) {
                        //Target is in firing range, attack
                        if (i != 0) {
                            //Equip the weapon if it is not already
                            Weapon temp = unit.getWeapon(0);
                            unit.setWeapon(0, unit.getWeapon(i));
                            unit.setWeapon(i, temp);
                        }
                        bp.startBattle(unit, target);
                        break;
                    }
                }
            }
            unit.setMoved(true);
        } else if (task.getType() == AITask.TaskType.GO_TO_TILE) {
            //TODO: DO
        }
    }

    public int determineAverageX(int faction) {
        int xSum = 0;
        int numUnits = 0;
        for (int i = 0; i < Game.getCurrentMap().getNumUnits(); i++) {
            if (Game.getCurrentMap().getUnit(i).getFaction() == faction) {
                xSum += Game.getCurrentMap().getUnit(i).getMapx();
                numUnits++;
            }
        }
        return Math.round(xSum / numUnits);
    }

    public int determineAverageY(int faction) {
        int ySum = 0;
        int numUnits = 0;
        for (int i = 0; i < Game.getCurrentMap().getNumUnits(); i++) {
            if (Game.getCurrentMap().getUnit(i).getFaction() == faction) {
                ySum += Game.getCurrentMap().getUnit(i).getMapx();
                numUnits++;
            }
        }
        return Math.round(ySum / numUnits);
    }

    public boolean canAttack(Unit attacker, Unit defender) {
        PathFinder pf = new PathFinder();
        pf.setUnitCollision(false);
        System.out.println("Getting path...");
        ArrayList<Tile> path = pf.getShortestPathAStar(Game.getCurrentMap(),
                attacker, Game.getCurrentMap().getTile(attacker.getMapx()
                + attacker.getMapy() * Game.getCurrentMap().getWidth()), Game.
                getCurrentMap().getTile(defender.getMapx() + defender.getMapy()
                * Game.getCurrentMap().getWidth()));
        System.out.println("Path got.");
        //Find the longest possible range for the attacker
        int range = 0;
        for (int i = 0; i < attacker.getWeapons().length; i++) {
            if (attacker.getWeapon(i) != null) {
                if (attacker.getWeapon(i).getRange() > range) {
                    range = attacker.getWeapon(i).getRange();
                }
            }
        }
        //Remove tiles corresponding to the range of the weapon
        for (int i = 0; i < range; i++) {
            if (path.size() > 0) {
                path.remove(0);
            }
        }
        //Determine if the path is short enough for the attacker to traverse
        int pathCost = 0;
        for (int i = path.size() - 1; i >= 0; i--) {
            pathCost += attacker.getUnitClass().getMoveCost(path.get(i).getTerrain());
        }
        return attacker.getMov() <= pathCost;
    }

    /**
     * Generates a list of all units that the specified attacker may attack
     * within a single turn. Runs a PathFinder to get the attackable tiles
     * available for the unit for each of the tiles it may move to during a turn
     * and scans these tiles for non-duplicate units with range of the
     * attacker's longest-range weapon. Due to frequent use of the PathFinder,
     * this method may cause lag.
     *
     * @param attacker The unit to determine attackable units for
     * @return An arraylist of all the units the attacker may attack within a
     * single turn
     */
    public ArrayList<Unit> getAttackableUnits(Unit attacker) {
        PathFinder pf = new PathFinder();
        ArrayList<Unit> attackableUnits = new ArrayList();
        //Get all tiles that the attacker can reach within a turn
        ArrayList<Tile> moveableTiles = pf.getMovableTiles(attacker,
                Game.getCurrentMap());
        //Create a dummy unit with which to test each reachable tile and equip
        //it with its longest range weapon
        Unit testUnit = attacker;
        int longest = 0;
        for (int i = 0; i < testUnit.getWeapons().length; i++) {
            //Note: The equipped weapon is tested against itself as well to
            //assure it is not null
            if (testUnit.getWeapon(i) != null) {
                if (testUnit.getWeapon(i).getRange() > testUnit.getWeapon(longest).
                        getRange()) {
                    longest = i;
                }
            }
        }
        if (longest > 0) { //Equip the weapon
            Weapon temp = testUnit.getWeapon(0);
            testUnit.setWeapon(0, testUnit.getWeapon(longest));
            testUnit.setWeapon(longest, temp);
        }
        //Test each tile for units within attack range
        for (int i = 0; i < moveableTiles.size(); i++) {
            ArrayList<Tile> attackableTiles = pf.getAttackableTiles(testUnit,
                    moveableTiles.get(i).getMapX(), moveableTiles.get(i).
                    getMapY(), Game.getCurrentMap(), 0);
            for (int j = 0; j < attackableTiles.size(); j++) {
                if (Game.getCurrentMap().getUnitTile(attackableTiles.get(j).
                        getMapX() + attackableTiles.get(j).getMapY() * Game.
                        getCurrentMap().getWidth()) != null) {
                    //Prevent duplicates
                    if (!attackableUnits.contains(Game.getCurrentMap().
                            getUnitTile(attackableTiles.get(j).getMapX()
                            + attackableTiles.get(j).getMapY() * Game.
                            getCurrentMap().getWidth()))) {
                        attackableUnits.add(Game.getCurrentMap().
                                getUnitTile(attackableTiles.get(j).getMapX()
                                + attackableTiles.get(j).getMapY() * Game.
                                getCurrentMap().getWidth()));
                    }
                }
            }
        }
        //Re-equip the original weapon
        if (longest > 0) {
            Weapon temp = testUnit.getWeapon(0);
            testUnit.setWeapon(0, testUnit.getWeapon(longest));
            testUnit.setWeapon(longest, temp);
        }

        return attackableUnits;
    }

    public boolean isTakingTurn() {
        return takingTurn;
    }

    @Override
    public void run() {
        if (stage == 0) {
            System.out.println("Beginning turn");
            tasks = generateAITasks();
            System.out.println("Prioritizing tasks");
            prioritizeAITasks(tasks);
            //Sort tasks
            System.out.println("Sorting tasks");
            Comparator comp = new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    AITask t1 = (AITask) o1;
                    AITask t2 = (AITask) o2;
                    if (t1.getPriority() < t2.getPriority()) {
                        return -1;
                    } else if (t1.getPriority() == t2.getPriority()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            };
            PriorityQueue<AITask> sortedTasks = new PriorityQueue(tasks.size(), comp);
            for (int i = 0; i < sortedTasks.size(); i++) {
                sortedTasks.add(tasks.get(i));
            }
            for (int i = 0; i < sortedTasks.size(); i++) {
                tasks.add(sortedTasks.poll());
            }
            System.out.println("Assigning tasks.  Size: " + tasks.size());
            //Assign tasks
            assignTasks(tasks);
            System.out.println("Size: " + tasks.size());
            tasksReady = true;
        } else if (stage == 1 || stage == 2) {
            PathFinder pf = new PathFinder();
            Unit unit = Game.getCurrentMap().getUnit(currentTask.getAssignedIndex());
            if (currentTask.getType() == AITask.TaskType.ATTACK_UNIT) {
                Unit target = Game.getCurrentMap().getUnit(currentTask.getTargetIndex());
                pf.setExcludeCollision(target.getMapx(), target.getMapy());
                ArrayList<Tile> shortestPath = pf.getShortestPathAStar(Game.
                        getCurrentMap(), unit, Game.getCurrentMap().getTile(unit.
                        getMapx() + unit.getMapy() * Game.getCurrentMap().
                        getWidth()), Game.getCurrentMap().getTile(target.getMapx()
                        + target.getMapy() * Game.getCurrentMap().getWidth()));
                int range = 0;
                for (int i = 0; i < unit.getWeapons().length; i++) {
                    if (unit.getWeapon(i) != null) {
                        if (unit.getWeapon(i).getRange() > range) {
                            range = unit.getWeapon(i).getRange();
                        }
                    }
                }
                if (shortestPath.size() >= range) {
                    System.out.println("Pathsize: " + shortestPath.size());
                    System.out.println("Range: " + range);
                    //Likely outside of range, move the unit
                    int cost = 0;
                    int tile = shortestPath.size() - 1;
                    for (int i = shortestPath.size() - 2; i >= 0; i--) {
                        if (cost + unit.getUnitClass().getMoveCost(shortestPath.get(i).
                                getTerrain()) <= unit.getMov()) {
                            cost += unit.getUnitClass().getMoveCost(shortestPath.get(i).
                                    getTerrain());
                            if (cost == unit.getMov()) {
                                tile = i;
                                break;
                            }
                        } else {
                            tile = i + 1;
                            break;
                        }
                        if ((i == 0) && (tile == shortestPath.size() - 1)) {
                            if (Game.getCurrentMap().getUnitTile(shortestPath.get(0).getMapX() + shortestPath.get(0).getMapY() * Game.getCurrentMap().getWidth()) == null) {
                                tile = 0;
                            } else {
                                tile = 1;
                            }
                        }
                    }
                    while (Game.getCurrentMap().getUnitTile(shortestPath.
                            get(tile).getMapX() + shortestPath.get(tile).
                            getMapY() * Game.getCurrentMap().getWidth())
                            != null && Game.getCurrentMap().
                            getUnitTile(shortestPath.get(tile).getMapX()
                            + shortestPath.get(tile).getMapY() * Game.
                            getCurrentMap().getWidth()) != unit) {
                        tile++;
                    }
                    System.out.println("COST: " + cost);
                    System.out.println("TILE: " + tile);
                    destination = shortestPath.get(tile);
                } else {
                    destination = Game.getCurrentMap().getTile(unit.getMapx()
                            + unit.getMapy() * Game.getCurrentMap().getWidth());
                }
            }
        } else if (stage >= 3) {
            PathFinder pf = new PathFinder(); 
            Unit unit = Game.getCurrentMap().getUnit(currentTask.getAssignedIndex());
            Unit target = Game.getCurrentMap().getUnit(currentTask.getTargetIndex());
            pf.setExcludeCollision(target.getMapx(), target.getMapy());
            for (int i = 0; i < unit.getWeapons().length; i++) {
                //Test each weapon's range
                if (unit.getWeapon(i) != null) {
                    ArrayList<Tile> attackableTiles = pf.getAttackableTiles(unit, unit.
                            getMapx(), unit.getMapy(), Game.getCurrentMap(), i);
                    if (attackableTiles.contains(Game.getCurrentMap().
                            getTile(target.getMapx() + target.getMapy() * Game.
                            getCurrentMap().getWidth())) && !target.isDead()) {
                        //Target is in firing range, attack
                        if (i != 0) {
                            //Equip the weapon if it is not already
                            Weapon temp = unit.getWeapon(0);
                            unit.setWeapon(0, unit.getWeapon(i));
                            unit.setWeapon(i, temp);
                        }
                        attackTarget = target;
                        canAttack = true;
                        break;
                    }
                }
            }
            if (!canAttack) {
                //If task target is not a valid target, search for
                //others in range
                for (int i = 0; i < unit.getWeapons().length; i++) {
                    //Test each weapon's range
                    if (unit.getWeapon(i) != null) {
                        ArrayList<Tile> attackableTiles = pf.getAttackableTiles(unit, unit.
                                getMapx(), unit.getMapy(), Game.getCurrentMap(), i);
                        for (int j = 0; j < attackableTiles.size(); j++) {
                            if (Game.getCurrentMap().getUnitTile(attackableTiles.
                                    get(j).getMapX() + attackableTiles.get(j).
                                    getMapY() * Game.getCurrentMap().getWidth()) != null
                                    && Game.getCurrentMap().getUnitTile(attackableTiles.
                                    get(j).getMapX() + attackableTiles.get(j).
                                    getMapY() * Game.getCurrentMap().getWidth()).
                                    getFaction() != faction && !Game.getCurrentMap().
                                    getUnitTile(attackableTiles.get(j).getMapX()
                                    + attackableTiles.get(j).getMapY() * Game.
                                    getCurrentMap().getWidth()).isDead()) {
                                if (i != 0) {
                                    //Equip the weapon if it is not already
                                    Weapon temp = unit.getWeapon(0);
                                    unit.setWeapon(0, unit.getWeapon(i));
                                    unit.setWeapon(i, temp);
                                }
                                attackTarget = Game.getCurrentMap().
                                        getUnitTile(attackableTiles.get(j).getMapX()
                                        + attackableTiles.get(j).getMapY() * Game.
                                        getCurrentMap().getWidth());
                                canAttack = true;
                                break;
                            }
                        }
                    }
                }
            }
            determinedAttack = true;
        }
        try {
            aiThread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(AIPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
