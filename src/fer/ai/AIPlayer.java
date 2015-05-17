package fer.ai;

import fer.Cursor;
import fer.Game;
import fer.Tile;
import fer.Unit;
import fer.gameplay.Attack;
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

    //The chance of death by engagement the player is willing to tolerate before
    //the engaging unit is considered a threat
    private final double tolerableDeathChance = 0.5;
    //The ratio of damage dealt to damage taken the player is willing to tolerate
    //for potential engagements
    private final double tolerableDamageRatio = 1;
    //A coefficient that corresponds to the probabiliy that units will stay 
    //together as a group rather than spread out.  Between 0 and 1.
    private final float cohesiveness = 1;
    //A coefficient corresponding to the tendency for units to attack 
    //enemies within their range.
    private final float aggressiveness = 1;
	
    private int faction = 1;
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
    private ArrayList<Tile> arrowPath;
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
                    stage = 7; //TODO: Check
                } else {   
                    currentTask = tasks.get(currentTaskIndex);
                    aiThread = new Thread(this);
                    aiThread.start();
                    System.out.println("Starting unit " + currentTask.getAssignedIndex());
                    cursor.setMapLocation(Game.getCurrentMap().getUnit(currentTask.
                            getAssignedIndex()).getMapx(), Game.getCurrentMap().
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
                    System.out.println("Path created for unit "  + currentTask.getAssignedIndex());
                    cursor.setArrowPath(arrowPath);
                    cursor.setShowingMoveArrow(true);
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
                        getAssignedIndex()).getMapx(), Game.getCurrentMap().
                        getUnit(currentTask.getAssignedIndex()).getMapy());
                cursor.centerCursor();
                Game.getCurrentMap().getUnit(currentTask.getAssignedIndex()).
                        resetAnimation(3);
                Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
                        .setActiveMapAnimation(3);
                cursor.setShowingMoveArrow(false);
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
                if (currentTask.getAssignedIndex() != -1) {
                    Game.getCurrentMap().getUnit(currentTask.getAssignedIndex()).
                            resetAnimation(0);
                    Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
                            .setActiveMapAnimation(0);
                    Game.getCurrentMap().getUnit(currentTask.getAssignedIndex()).
                            setMoved(true);
                }
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
            int priority = addPriority(tasks, bp, i);
            tasks.get(i).setPriority(priority);
        }
    }

	private int addPriority(ArrayList<AITask> tasks, BattleProcessor bp, int i) {
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
		return priority;
	}

    public void assignTasks(ArrayList<AITask> tasks) {
        //Cycle through each task and find the most suitable unit.  End when
        //all units have been assigned a task or when tasks have been exhausted   
        int numUnits = 0;
        ArrayList<Integer> assignedNumbers = new ArrayList();
        numUnits = determineUnits(numUnits);
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
                            int score = tasks.get(i).determineUnitSuitability(Game.
                                    getCurrentMap().getUnit(j), secondRun, tolerableDamageRatio, tolerableDeathChance, this);
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

	private int determineUnits(int numUnits) {
		for (int i = 0; i < Game.getCurrentMap().getNumUnits(); i++) {
            if (!Game.getCurrentMap().getUnit(i).isDead() && Game.
                    getCurrentMap().getUnit(i).getFaction() == faction) {
                numUnits++;
            }
        }
		return numUnits;
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
                tasks.get(i).moveUnit(this);
                System.out.println("Unit moved");
            }
        }
    }

    public boolean shortestPathGreater(ArrayList<Tile> shortestPath, int range) {
		return shortestPath.size() >= range;
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
        return attacker.canAttack(defender, this);
    }

	public int attackerCanTraverse(Unit attacker, ArrayList<Tile> path) {
		int pathCost = 0;
        for (int i = path.size() - 1; i >= 0; i--) {
            pathCost += attacker.getUnitClass().getMoveCost(path.get(i).getTerrain());
        }
		return pathCost;
	}

	public int findLongestRange(Unit attacker) {
		return attacker.findLongestRange();
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
            longest = testUnit.getLongestRange(longest, i);
        }
        if (longest > 0) { //Equip the weapon
            testUnit.equipWeapon(longest);
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
            testUnit.equipWeapon(longest);
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
            System.out.println("Starting pathfinding thread.");
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
                int range = unit.findLongestRange();
                if (shortestPathGreater(shortestPath, range)) {
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
                    tile = addTiles(unit, shortestPath, tile);
                    System.out.println("COST: " + cost);
                    System.out.println("TILE: " + tile);
                    arrowPath = new ArrayList();
                    for (int i = tile; i < shortestPath.size(); i++) {
                        arrowPath.add(shortestPath.get(i));
                    }
                    destination = shortestPath.get(tile);
                } else {
                    arrowPath = new ArrayList();
                    arrowPath.add(Game.getCurrentMap().getTile(unit.getMapx()
                            + unit.getMapy() * Game.getCurrentMap().getWidth()));
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
                            unit.equipWeapon(i);
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
                                    unit.equipWeapon(i);
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

	private int addTiles(Unit unit, ArrayList<Tile> shortestPath, int tile) {
		while (Game.getCurrentMap().getUnitTile(shortestPath.
		        get(tile).getMapX() + shortestPath.get(tile).
		        getMapY() * Game.getCurrentMap().getWidth())
		        != null && Game.getCurrentMap().
		        getUnitTile(shortestPath.get(tile).getMapX()
		        + shortestPath.get(tile).getMapY() * Game.
		        getCurrentMap().getWidth()) != unit) {
		    tile++;
		}
		return tile;
	}
}
