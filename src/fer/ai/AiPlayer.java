/*
 * 
 */
package fer.ai;

import fer.Cursor;
import fer.Game;
import fer.Tile;
import fer.Unit;
import fer.gameplay.BattleProcessor;
import fer.gameplay.MapGoal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class AiPlayer.
 *
 * @author Evan Stewart A class containing a set of methods that act upon a map
 *         in order to process computer player movement. Each player contains a
 *         set of parameters that determine how it will act based on certain
 *         circumstances.
 */
public class AiPlayer implements Runnable {

	/**
	 * The chance of death by engagement the player is willing to tolerate 
	 * before the engaging unit is considered a threat.
	 */
	private final double tolerableDeathChance = 0.5;
	/**
	 * The ratio of damage dealt to damage taken the player is willing to
	 * tolerate
	 * for potential engagements.
	 */
	private final double tolerableDamageRatio = 1;
	/**
	 * A coefficient that corresponds to the probability that units will stay
	 * together as a group rather than spread out. Between 0 and 1.
	 */
	private final float cohesiveness = 1;
	/**
	 * A coefficient corresponding to the tendency for units to attack
	 * enemies within their range.
	 */
	private final float aggressiveness = 1;

	/** The faction. */
	private int faction = 1;
	/** The number of tasks currently assigned to friendly units. */
	private int tasksAssigned;
	
	/** The taking turn. */
	private boolean takingTurn;
	
	/** The tasks ready. */
	private boolean tasksReady;
	
	/** The stage. */
	private int stage;
	
	/** The updates. */
	private int updates;
	
	/** The current task index. */
	private int currentTaskIndex;
	
	/** The tasks. */
	private ArrayList<AiTask> tasks;
	
	/** The ai thread. */
	private Thread aiThread;
	
	/** The destination. */
	private Tile destination;
	
	/** The arrow path. */
	private ArrayList<Tile> arrowPath;
	
	/** The current task. */
	private AiTask currentTask;
	
	/** The cursor. */
	private Cursor cursor;
	
	/** The determined attack. */
	private boolean determinedAttack;
	
	/** The can attack. */
	private boolean canAttack;
	
	/** The attack target. */
	private Unit attackTarget;

	/**
	 * Instantiates a new ai player.
	 *
	 * @param faction the faction
	 */
	public AiPlayer(final int faction) {
		this.faction = faction;
	}

	/**
	 * Start turn.
	 */
	public final void startTurn() {
		cursor = Cursor.getCursor();
		cursor.setVisible(false);
		takingTurn = true;
		stage = 0;
		updates = 0;
		currentTaskIndex = 0;
		tasks = new ArrayList<>();
		tasksReady = false;
		destination = null;
		determinedAttack = false;
		canAttack = false;
		aiThread = new Thread(this);
		aiThread.start();
	}

	/**
	 * Update.
	 */
	public final void update() {
		switch (stage) {
		case 0: // Wait for the thread to finish task generation
			if (tasksReady) {
				stage++;
			}
			break;
		case 1: // Select and center a unit
			if (tasks.get(currentTaskIndex).getAssignedIndex() == -1) {
				stage = 7; // TODO: Check
			} else {
				currentTask = tasks.get(currentTaskIndex);
				aiThread = new Thread(this);
				aiThread.start();
				System.out.println("Starting unit "
						+ currentTask.getAssignedIndex());
				cursor.setMapLocation(
						Game.getCurrentMap()
								.getUnit(currentTask.getAssignedIndex())
								.getMapx(),
						Game.getCurrentMap()
								.getUnit(currentTask.getAssignedIndex())
								.getMapy());
				cursor.centerCursor();
				Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
						.resetAnimation(1);
				Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
						.setActiveMapAnimation(1);
				updates = 0;
				stage++;
			}
			break;
		case 2: // Animate the selected unit and wait for the ai thread
			if (updates >= Game.getCurrentMap()
					.getUnit(currentTask.getAssignedIndex())
					.getActiveAnimationFrames() * 2
					&& destination != null) {
				System.out.println("Path created for unit "
						+ currentTask.getAssignedIndex());
				cursor.setArrowPath(arrowPath);
				cursor.setShowingMoveArrow(true);
				updates = 0;
				stage++;
			}
			break;
		case 3: // Move the unit to the destination tile
			Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
					.setMapx(destination.getMapX());
			Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
					.setMapy(destination.getMapY());
			cursor.setMapLocation(
					Game.getCurrentMap()
							.getUnit(currentTask.getAssignedIndex()).getMapx(),
					Game.getCurrentMap()
							.getUnit(currentTask.getAssignedIndex()).getMapy());
			cursor.centerCursor();
			Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
					.resetAnimation(3);
			Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
					.setActiveMapAnimation(3);
			cursor.setShowingMoveArrow(false);
			updates = 0;
			stage++;
		case 4: // Determine and take further action
			if (currentTask.getType() == AiTask.TaskType.ATTACK_UNIT) {
				aiThread = new Thread(this);
				aiThread.start();
				stage = 5;
			} else {
				stage = 7;
			}
			break;
		case 5: // Check if can attack and start battle
			if (determinedAttack) {
				if (canAttack) {
					Game.getBattleProcessor().startBattle(
							Game.getCurrentMap().getUnit(
									currentTask.getAssignedIndex()),
							attackTarget);
				}
				stage++;
			}
			break;
		case 6: // Wait for battle processor
			if (!Game.getBattleProcessor().isInCombat()) {
				stage++;
			}
			break;
		case 7: // Reset values and loop if more tasks
			if (currentTask.getAssignedIndex() != -1) {
				Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
						.resetAnimation(0);
				Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
						.setActiveMapAnimation(0);
				Game.getCurrentMap().getUnit(currentTask.getAssignedIndex())
						.setMoved(true);
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
		case 8: // End the turn
			endTurn();
			break;
		default:
			break;
		}
		updates++;
	}

	/**
	 * End turn.
	 */
	public final void endTurn() {
		takingTurn = false;
		cursor.setVisible(true);
		cursor.endTurn();
	}

	/**
	 * Generate ai tasks.
	 *
	 * @return the array list
	 */
	public final ArrayList<AiTask> generateAITasks() {
		ArrayList<AiTask> tasks = new ArrayList<>();
		// Generate attack unit tasks for every enemy unit on the map
		for (int i = 0; i < Game.getCurrentMap().getNumUnits(); i++) {
			if (Game.getCurrentMap().getUnit(i).getFaction() != faction
					&& !Game.getCurrentMap().getUnit(i).isDead()) {
				tasks.add(new AiTask(i));
			}
		}
		// Generate go to tile tasks for any tiles of interest
		if (Game.getCurrentMap().getFactionGoals()[faction].getType() == MapGoal.GoalType.REACH_TILE) {
			// Add a task to reach the goal tile
			tasks.add(new AiTask(
					Game.getCurrentMap().getFactionGoals()[faction].getTileX(),
					Game.getCurrentMap().getFactionGoals()[faction].getTileY()));
		}
		System.out.println("Num Tasks: " + tasks.size());
		return tasks;
	}

	/**
	 * Prioritize ai tasks.
	 *
	 * @param tasks the tasks
	 */
	public final void prioritizeAITasks(final ArrayList<AiTask> tasks) {
		BattleProcessor bp = Game.getBattleProcessor();
		for (int i = 0; i < tasks.size(); i++) {
			int priority = addPriority(tasks, bp, i);
			tasks.get(i).setPriority(priority);
		}
	}

	/**
	 * Adds the priority.
	 *
	 * @param tasks the tasks
	 * @param bp the bp
	 * @param i the i
	 * @return the int
	 */
	private int addPriority(final ArrayList<AiTask> tasks, final BattleProcessor bp, final int i) {
		int priority = 1;
		if (tasks.get(i).getType() == AiTask.TaskType.ATTACK_UNIT) {
			ArrayList<Unit> attackableUnits = getAttackableUnits(Game
					.getCurrentMap().getUnit(tasks.get(i).getTargetIndex()));
			// Add priority if the unit is part of the faction goal
			if (Game.getCurrentMap().getFactionGoals()[faction].getType() == MapGoal.GoalType.ELIMINATE_TARGETS) {
				for (int j = 0; j < Game.getCurrentMap().getFactionGoals()[faction]
						.getTargets().length; j++) {
					if (tasks.get(i).getTargetIndex() == Game.getCurrentMap()
							.getFactionGoals()[faction].getTargets()[j]) {
						priority += 10;
						break;
					}
				}
			}
			// Add priority for at-risk friendly units
			for (int j = 0; j < attackableUnits.size(); j++) {
				priority++;
				// If the unit is part of the enemy faction's goal, units that
				// put it at risk are prioritized targets
				// Units that are capable of killing friendly units within a
				// turn also gain priority
				if (Game.getCurrentMap().getFactionGoals()[Game.getCurrentMap()
						.getUnit(tasks.get(i).getTargetIndex()).getFaction()]
						.getType() == MapGoal.GoalType.ELIMINATE_TARGETS) {
					boolean goalTarget = true;
					for (int k = 0; k < Game.getCurrentMap().getFactionGoals()[Game
							.getCurrentMap()
							.getUnit(tasks.get(i).getTargetIndex())
							.getFaction()].getTargets().length; k++) {
						goalTarget &= attackableUnits.get(j).getMapIndex() == Game
								.getCurrentMap().getFactionGoals()[Game
								.getCurrentMap()
								.getUnit(tasks.get(i).getTargetIndex())
								.getFaction()].getTargets()[k];
					}
					if (goalTarget) {
						priority += 2;
						if (bp.calculateDeathChance(
								attackableUnits.get(j),
								Game.getCurrentMap().getUnit(
										tasks.get(i).getTargetIndex()), false) > tolerableDeathChance) {
							priority += 6;
						}
					} else if (bp.calculateDeathChance(
							attackableUnits.get(j),
							Game.getCurrentMap().getUnit(
									tasks.get(i).getTargetIndex()), false) > tolerableDeathChance) {
						priority += 2;
					}
				} else if (bp.calculateDeathChance(
						attackableUnits.get(j),
						Game.getCurrentMap().getUnit(
								tasks.get(i).getTargetIndex()), false) > tolerableDeathChance) {
					priority += 2;
				}
			}
		} else if (tasks.get(i).getType() == AiTask.TaskType.GO_TO_TILE) {
			// Check if the target tile is a map goal for this faction
			for (int j = 0; j < Game.getCurrentMap().getFactionGoals().length; j++) {
				if (Game.getCurrentMap().getFactionGoals()[j].getFaction() == faction
						&& Game.getCurrentMap().getFactionGoals()[j].getType() == MapGoal.GoalType.REACH_TILE && Game.getCurrentMap().getFactionGoals()[j].getTileX() == tasks
						.get(i).getMapx()
						&& Game.getCurrentMap().getFactionGoals()[j]
								.getTileY() == tasks.get(i).getMapx()) {
priority += 10;
}
			}
		}
		return priority;
	}

	/**
	 * Assign tasks.
	 *
	 * @param tasks the tasks
	 */
	public final void assignTasks(final ArrayList<AiTask> tasks) {
		// Cycle through each task and find the most suitable unit. End when
		// all units have been assigned a task or when tasks have been exhausted
		int numUnits = 0;
		ArrayList<Integer> assignedNumbers = new ArrayList<>();
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
						if (!Game.getCurrentMap().getUnit(j).isDead()
								&& Game.getCurrentMap().getUnit(j).getFaction() == faction) {
							System.out.println("Faction: "
									+ Game.getCurrentMap().getUnit(j)
											.getFaction());
							int score = tasks.get(i).determineUnitSuitability(
									Game.getCurrentMap().getUnit(j), secondRun,
									tolerableDamageRatio, tolerableDeathChance,
									this);
							System.out.println("Done prob");
							if (score > bestScore
									&& !assignedNumbers.contains(j)) {
								bestScore = score;
								bestScoreIndex = j;
								System.out.println("Suitability: " + bestScore
										+ " Index: " + bestScoreIndex
										+ " Target: "
										+ tasks.get(i).getTargetIndex());
							}
						}
					}
					if (secondRun) {
						AiTask taskCopy;
						taskCopy = tasks.get(i).getType() == AiTask.TaskType.ATTACK_UNIT ? new AiTask(tasks.get(i).getTargetIndex())
								: new AiTask(tasks.get(i).getMapx(), tasks
										.get(i).getMapy());
						taskCopy.setPriority(tasks.get(i).getPriority());
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

	/**
	 * Determine units.
	 *
	 * @param numUnits the num units
	 * @return the int
	 */
	private int determineUnits(int numUnits) {
		for (int i = 0; i < Game.getCurrentMap().getNumUnits(); i++) {
			if (!Game.getCurrentMap().getUnit(i).isDead()
					&& Game.getCurrentMap().getUnit(i).getFaction() == faction) {
				numUnits++;
			}
		}
		return numUnits;
	}

	/**
	 * Take turn.
	 */
	public final void takeTurn() {
		System.out.println("Beginning turn");
		ArrayList<AiTask> tasks = generateAITasks();
		System.out.println("Prioritizing tasks");
		prioritizeAITasks(tasks);
		// Sort tasks
		System.out.println("Sorting tasks");
		Comparator comp = new Comparator() {
			@Override
			public int compare(final Object o1, final Object o2) {
				AiTask t1 = (AiTask) o1;
				AiTask t2 = (AiTask) o2;
				if (t1.getPriority() < t2.getPriority()) {
					return -1;
				} else
					return t1.getPriority() == t2.getPriority() ? 0 : 1;
			}
		};
		PriorityQueue<AiTask> sortedTasks = new PriorityQueue(tasks.size(),
				comp);
		sortedTasks.addAll(tasks);
		for (int i = 0; i < sortedTasks.size(); i++) {
			tasks.add(sortedTasks.poll());
		}
		System.out.println("Assigning tasks");
		// Assign tasks
		assignTasks(tasks);
		// Move units
		System.out.println("Moving units");
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getAssignedIndex() != -1) {
				tasks.get(i).moveUnit(this);
				System.out.println("Unit moved");
			}
		}
	}

	/**
	 * Shortest path greater.
	 *
	 * @param shortestPath the shortest path
	 * @param range the range
	 * @return true, if successful
	 */
	public final boolean shortestPathGreater(final ArrayList<Tile> shortestPath, final int range) {
		return shortestPath.size() >= range;
	}

	/**
	 * Determine average x.
	 *
	 * @param faction the faction
	 * @return the int
	 */
	public final int determineAverageX(final int faction) {
		float xSum = 0;
		float numUnits = 0;
		for (int i = 0; i < Game.getCurrentMap().getNumUnits(); i++) {
			if (Game.getCurrentMap().getUnit(i).getFaction() == faction) {
				xSum += Game.getCurrentMap().getUnit(i).getMapx();
				numUnits++;
			}
		}
		return Math.round(xSum / numUnits);
	}

	/**
	 * Determine average y.
	 *
	 * @param faction the faction
	 * @return the int
	 */
	public final int determineAverageY(final int faction) {
		float ySum = 0;
		float numUnits = 0;
		for (int i = 0; i < Game.getCurrentMap().getNumUnits(); i++) {
			if (Game.getCurrentMap().getUnit(i).getFaction() == faction) {
				ySum += Game.getCurrentMap().getUnit(i).getMapx();
				numUnits++;
			}
		}
		return Math.round(ySum / numUnits);
	}

	/**
	 * Can attack.
	 *
	 * @param attacker the attacker
	 * @param defender the defender
	 * @return true, if successful
	 */
	public final boolean canAttack(final Unit attacker, final Unit defender) {
		return attacker.canAttack(defender, this);
	}

	/**
	 * Attacker can traverse.
	 *
	 * @param attacker the attacker
	 * @param path the path
	 * @return the int
	 */
	public final int attackerCanTraverse(final Unit attacker, final ArrayList<Tile> path) {
		int pathCost = 0;
		for (int i = path.size() - 1; i >= 0; i--) {
			pathCost += attacker.getUnitClass().getMoveCost(
					path.get(i).getTerrain());
		}
		return pathCost;
	}

	/**
	 * Find longest range.
	 *
	 * @param attacker the attacker
	 * @return the int
	 */
	public final int findLongestRange(final Unit attacker) {
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
	 * @param attacker
	 *            The unit to determine attackable units for
	 * @return An arraylist of all the units the attacker may attack within a
	 *         single turn
	 */
	public final ArrayList<Unit> getAttackableUnits(final Unit attacker) {
		PathFinder pf = new PathFinder();
		ArrayList<Unit> attackableUnits = new ArrayList<>();
		// Get all tiles that the attacker can reach within a turn
		ArrayList<Tile> moveableTiles = pf.getMovableTiles(attacker,
				Game.getCurrentMap());
		// Create a dummy unit with which to test each reachable tile and equip
		// it with its longest range weapon
		Unit testUnit = attacker;
		int longest = 0;
		for (int i = 0; i < testUnit.getWeapons().length; i++) {
			// Note: The equipped weapon is tested against itself as well to
			// assure it is not null
			longest = testUnit.getLongestRange(longest, i);
		}
		if (longest > 0) { // Equip the weapon
			testUnit.equipWeapon(longest);
		}
		// Test each tile for units within attack range
		for (int i = 0; i < moveableTiles.size(); i++) {
			ArrayList<Tile> attackableTiles = pf.getAttackableTiles(testUnit,
					moveableTiles.get(i).getMapX(), moveableTiles.get(i)
							.getMapY(), Game.getCurrentMap(), 0);
			for (int j = 0; j < attackableTiles.size(); j++) {
				if (Game.getCurrentMap().getUnitTile(
						attackableTiles.get(j).getMapX()
								+ attackableTiles.get(j).getMapY()
								* Game.getCurrentMap().getWidth()) != null && !attackableUnits.contains(Game.getCurrentMap()
						.getUnitTile(
								attackableTiles.get(j).getMapX()
										+ attackableTiles.get(j).getMapY()
										* Game.getCurrentMap().getWidth()))) {
attackableUnits.add(Game.getCurrentMap().getUnitTile(
attackableTiles.get(j).getMapX()
+ attackableTiles.get(j).getMapY()
* Game.getCurrentMap().getWidth()));
}
			}
		}
		// Re-equip the original weapon
		if (longest > 0) {
			testUnit.equipWeapon(longest);
		}

		return attackableUnits;
	}

	/**
	 * Checks if is taking turn.
	 *
	 * @return true, if is taking turn
	 */
	public final boolean isTakingTurn() {
		return takingTurn;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public final void run() {
		if (stage == 0) {
			System.out.println("Beginning turn");
			tasks = generateAITasks();
			System.out.println("Prioritizing tasks");
			prioritizeAITasks(tasks);
			// Sort tasks
			System.out.println("Sorting tasks");
			Comparator comp = new Comparator() {
				@Override
				public int compare(final Object o1, final Object o2) {
					AiTask t1 = (AiTask) o1;
					AiTask t2 = (AiTask) o2;
					if (t1.getPriority() < t2.getPriority()) {
						return -1;
					} else
						return t1.getPriority() == t2.getPriority() ? 0 : 1;
				}
			};
			PriorityQueue<AiTask> sortedTasks = new PriorityQueue(tasks.size(),
					comp);
			sortedTasks.addAll(tasks);
			for (int i = 0; i < sortedTasks.size(); i++) {
				tasks.add(sortedTasks.poll());
			}
			System.out.println("Assigning tasks.  Size: " + tasks.size());
			// Assign tasks
			assignTasks(tasks);
			System.out.println("Size: " + tasks.size());
			tasksReady = true;
		} else if (stage == 1 || stage == 2) {
			System.out.println("Starting pathfinding thread.");
			PathFinder pf = new PathFinder();
			Unit unit = Game.getCurrentMap().getUnit(
					currentTask.getAssignedIndex());
			if (currentTask.getType() == AiTask.TaskType.ATTACK_UNIT) {
				Unit target = Game.getCurrentMap().getUnit(
						currentTask.getTargetIndex());
				pf.setExcludeCollision(target.getMapx(), target.getMapy());
				ArrayList<Tile> shortestPath = pf.getShortestPathAStar(
						Game.getCurrentMap(),
						unit,
						Game.getCurrentMap().getTile(
								unit.getMapx() + unit.getMapy()
										* Game.getCurrentMap().getWidth()),
						Game.getCurrentMap().getTile(
								target.getMapx() + target.getMapy()
										* Game.getCurrentMap().getWidth()));
				int range = unit.findLongestRange();
				if (shortestPathGreater(shortestPath, range)) {
					System.out.println("Pathsize: " + shortestPath.size());
					System.out.println("Range: " + range);
					// Likely outside of range, move the unit
					int cost = 0;
					int tile = shortestPath.size() - 1;
					for (int i = shortestPath.size() - 2; i >= 0; i--) {
						if (cost
								+ unit.getUnitClass().getMoveCost(
										shortestPath.get(i).getTerrain()) <= unit
									.getMov()) {
							cost += unit.getUnitClass().getMoveCost(
									shortestPath.get(i).getTerrain());
							if (cost == unit.getMov()) {
								tile = i;
								break;
							}
						} else {
							tile = i + 1;
							break;
						}
						if (i == 0 && tile == shortestPath.size() - 1) {
							tile = Game.getCurrentMap().getUnitTile(
									shortestPath.get(0).getMapX()
											+ shortestPath.get(0).getMapY()
											* Game.getCurrentMap().getWidth()) != null ? 1 : 0;
						}
					}
					tile = addTiles(unit, shortestPath, tile);
					System.out.println("COST: " + cost);
					System.out.println("TILE: " + tile);
					arrowPath = new ArrayList<>();
					for (int i = tile; i < shortestPath.size(); i++) {
						arrowPath.add(shortestPath.get(i));
					}
					destination = shortestPath.get(tile);
				} else {
					arrowPath = new ArrayList<>();
					arrowPath.add(Game.getCurrentMap().getTile(
							unit.getMapx() + unit.getMapy()
									* Game.getCurrentMap().getWidth()));
					destination = Game.getCurrentMap().getTile(
							unit.getMapx() + unit.getMapy()
									* Game.getCurrentMap().getWidth());
				}
			}
		} else if (stage >= 3) {
			PathFinder pf = new PathFinder();
			Unit unit = Game.getCurrentMap().getUnit(
					currentTask.getAssignedIndex());
			Unit target = Game.getCurrentMap().getUnit(
					currentTask.getTargetIndex());
			pf.setExcludeCollision(target.getMapx(), target.getMapy());
			for (int i = 0; i < unit.getWeapons().length; i++) {
				// Test each weapon's range
				if (unit.getWeapon(i) != null) {
					ArrayList<Tile> attackableTiles = pf.getAttackableTiles(
							unit, unit.getMapx(), unit.getMapy(),
							Game.getCurrentMap(), i);
					if (attackableTiles.contains(Game.getCurrentMap().getTile(
							target.getMapx() + target.getMapy()
									* Game.getCurrentMap().getWidth()))
							&& !target.isDead()) {
						// Target is in firing range, attack
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
				// If task target is not a valid target, search for
				// others in range
				for (int i = 0; i < unit.getWeapons().length; i++) {
					// Test each weapon's range
					if (unit.getWeapon(i) != null) {
						ArrayList<Tile> attackableTiles = pf
								.getAttackableTiles(unit, unit.getMapx(),
										unit.getMapy(), Game.getCurrentMap(), i);
						for (int j = 0; j < attackableTiles.size(); j++) {
							if (Game.getCurrentMap().getUnitTile(
									attackableTiles.get(j).getMapX()
											+ attackableTiles.get(j).getMapY()
											* Game.getCurrentMap().getWidth()) != null
									&& Game.getCurrentMap()
											.getUnitTile(
													attackableTiles.get(j)
															.getMapX()
															+ attackableTiles
																	.get(j)
																	.getMapY()
															* Game.getCurrentMap()
																	.getWidth())
											.getFaction() != faction
									&& !Game.getCurrentMap()
											.getUnitTile(
													attackableTiles.get(j)
															.getMapX()
															+ attackableTiles
																	.get(j)
																	.getMapY()
															* Game.getCurrentMap()
																	.getWidth())
											.isDead()) {
								if (i != 0) {
									unit.equipWeapon(i);
								}
								attackTarget = Game.getCurrentMap()
										.getUnitTile(
												attackableTiles.get(j)
														.getMapX()
														+ attackableTiles
																.get(j)
																.getMapY()
														* Game.getCurrentMap()
																.getWidth());
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
			Logger.getLogger(AiPlayer.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	/**
	 * Adds the tiles.
	 *
	 * @param unit the unit
	 * @param shortestPath the shortest path
	 * @param tile the tile
	 * @return the int
	 */
	private int addTiles(final Unit unit, final ArrayList<Tile> shortestPath, int tile) {
		while (Game.getCurrentMap().getUnitTile(
				shortestPath.get(tile).getMapX()
						+ shortestPath.get(tile).getMapY()
						* Game.getCurrentMap().getWidth()) != null
				&& Game.getCurrentMap().getUnitTile(
						shortestPath.get(tile).getMapX()
								+ shortestPath.get(tile).getMapY()
								* Game.getCurrentMap().getWidth()) != unit) {
			tile++;
		}
		return tile;
	}
}
