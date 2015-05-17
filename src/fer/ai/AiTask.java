package fer.ai;

import fer.Cursor;
import fer.Game;
import fer.Tile;
import fer.Unit;
import fer.gameplay.Attack;
import fer.gameplay.BattleProcessor;
import java.util.ArrayList;

/**
 * @author Evan Stewart A struct class that stores the parameters surrounding a
 *         potential action for and AI player to take, the the unit to which it
 *         is assigned, and its priority, which is evaluated based on its type.
 */
public class AiTask {

	public enum TaskType {
		ATTACK_UNIT, GO_TO_TILE
	}

	private TaskType type;
	private int priority;
	private int mapx, mapy, assignedIndex = -1, targetIndex = -1;

	/**
	 * Creates a new AITask of type AITask.TaskType.GO_TO_TILE
	 * 
	 * @param mapx
	 *            The x-coordinate of the destination tile
	 * @param mapy
	 *            The y-coordinate of the destination tile
	 */
	public AiTask(int mapx, int mapy) {
		type = TaskType.GO_TO_TILE;
		this.mapx = mapx;
		this.mapy = mapy;
	}

	/**
	 * Creates a new AITask of type AITask.TaskType.ATTACK_UNIT
	 * 
	 * @param targetIndex
	 *            The index on the current map of the target unit
	 */
	public AiTask(int targetIndex) {
		type = TaskType.ATTACK_UNIT;
		this.targetIndex = targetIndex;
	}

	public TaskType getType() {
		return type;
	}

	public void setType(TaskType type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getMapx() {
		return mapx;
	}

	public void setMapx(int mapx) {
		this.mapx = mapx;
	}

	public int getMapy() {
		return mapy;
	}

	public void setMapy(int mapy) {
		this.mapy = mapy;
	}

	public int getAssignedIndex() {
		return assignedIndex;
	}

	public void setAssignedIndex(int assignedIndex) {
		this.assignedIndex = assignedIndex;
	}

	public int getTargetIndex() {
		return targetIndex;
	}

	public void setTargetIndex(int targetIndex) {
		this.targetIndex = targetIndex;
	}

	public int determineUnitSuitability(Unit unit, boolean secondRun,
			double tolerableDamageRatio, double tolerableDeathChance,
			AiPlayer aIPlayer) {
		int suitability = 1;
		BattleProcessor bp = Game.getBattleProcessor();
		if (getType() == AiTask.TaskType.ATTACK_UNIT) {
			suitability += (Math.abs(unit.getMapx()
					- Game.getCurrentMap().getUnit(getTargetIndex()).getMapx()) / Game
					.getCurrentMap().getWidth()) * 10;
			suitability += (Math.abs(unit.getMapy()
					- Game.getCurrentMap().getUnit(getTargetIndex()).getMapy()) / Game
					.getCurrentMap().getHeight()) * 10;
			System.out.println("Dist suit");
			if (aIPlayer.canAttack(unit,
					Game.getCurrentMap().getUnit(getTargetIndex()))) {
				System.out.println("Can attack");
				suitability += 10;
				System.out.println("Attack damage attacker: "
						+ bp.calculateAttackDamage(unit, Game.getCurrentMap()
								.getUnit(getTargetIndex())));
				System.out.println("Attack damage defender: "
						+ bp.calculateAttackDamage(Game.getCurrentMap()
								.getUnit(getTargetIndex()), unit));
				System.out
						.println("DAMAGE RATIO: "
								+ (float) bp.calculateAttackDamage(
										unit,
										Game.getCurrentMap().getUnit(
												getTargetIndex())) / bp
										.calculateAttackDamage(
												Game.getCurrentMap().getUnit(
														getTargetIndex()), unit));
				if ((float) bp.calculateAttackDamage(unit, Game
						.getCurrentMap().getUnit(getTargetIndex())) / bp
						.calculateAttackDamage(
								Game.getCurrentMap().getUnit(getTargetIndex()),
								unit) >= tolerableDamageRatio) {
					System.out.println("Tolerable damage ratio");
					suitability += 10;
				}
				System.out.println("DEATH CHANCE: "
						+ bp.calculateDeathChance(unit, Game.getCurrentMap()
								.getUnit(getTargetIndex()), true));
				if (bp.calculateDeathChance(unit,
						Game.getCurrentMap().getUnit(getTargetIndex()), true) < tolerableDeathChance) {
					System.out.println("Tolerable death chance");
					suitability += 10;
				} else if (!secondRun) {
					suitability = 0;
				}
			}
		} else if (getType() == AiTask.TaskType.GO_TO_TILE) {
			suitability += (Math.abs(unit.getMapx() - getMapx()) / Game
					.getCurrentMap().getWidth()) * 100;
			suitability += (Math.abs(unit.getMapy() - getMapy()) / Game
					.getCurrentMap().getHeight()) * 100;
		}
		System.out.println("Suitability: " + suitability);
		return suitability;
	}

	public void moveUnit(AiPlayer aIPlayer) {
		System.out.println("Moving unit");
		PathFinder pf = new PathFinder();
		pf.setUnitCollision(false);
		BattleProcessor bp = Game.getBattleProcessor();
		Unit unit = Game.getCurrentMap().getUnit(getAssignedIndex());
		if (getType() == AiTask.TaskType.ATTACK_UNIT) {
			Unit target = Game.getCurrentMap().getUnit(getTargetIndex());
			ArrayList<Tile> shortestPath = pf.getShortestPathAStar(
					Game.getCurrentMap(),
					unit,
					Game.getCurrentMap().getTile(
							unit.getMapx() + unit.getMapy()
									* Game.getCurrentMap().getWidth()),
					Game.getCurrentMap().getTile(
							target.getMapx() + target.getMapy()
									* Game.getCurrentMap().getWidth()));
			for (Tile t : shortestPath) {
				System.out.println("X: " + t.getMapX());
				System.out.println("Y: " + t.getMapY());
			}
			int range = aIPlayer.findLongestRange(unit);
			Cursor.getCursor().setMapLocation(unit.getMapx(), unit.getMapy());
			Cursor.getCursor().centerCursor();
			long time = System.currentTimeMillis();
			while (System.currentTimeMillis() - time < 1000) {
			}
			if (aIPlayer.shortestPathGreater(shortestPath, range)) {
				System.out.println("Pathsize: " + shortestPath.size());
				System.out.println("Range: " + range);
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
				}
				unit.setMapx(shortestPath.get(tile).getMapX());
				unit.setMapy(shortestPath.get(tile).getMapY());
				Cursor.getCursor().setMapLocation(unit.getMapx(),
						unit.getMapy());
				Cursor.getCursor().centerCursor();
				time = System.currentTimeMillis();
				while (System.currentTimeMillis() - time < 1000) {
				}
			}
			Attack.attackWithWeaponInRange(pf, bp, unit, target);
			unit.setMoved(true);
		} else if (getType() == AiTask.TaskType.GO_TO_TILE) {
		}
	}
}