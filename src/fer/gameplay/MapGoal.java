package fer.gameplay;

import fer.Map;
import fer.util.GoalData;

/**
 * @author Evan Stewart
 */
public class MapGoal {

	/**
	 * An enumeration listing all of the basic types of map goals. Goals are
	 * divided into type categories with parameters in order to simplify parsing
	 * from XML.
	 */
	public enum GoalType {
		ELIMINATE_TARGETS("KILL TARGET"), ROUTE_ENEMY("ROUTE ENEMY"), DEFEND_TARGETS(
				"DEFEND TARGET"), REACH_TILE("REACH TILE");

		private String name;

		GoalType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	// The goal's basic type, used to determine what parameters are checked for
	// completion
	private GoalType type;
	// The index of the faction holding this goal
	private int faction;
	// An array of unit indexes holding either the units to be eliminated, the
	// units to be defended, or the units that must reach the specified tile
	private int[] targets;
	// The number of turns to survive for or the turn limit for the goal
	private int turns;
	// The x-coordinate of the tile to be reached
	private int tileX;
	// The y-coordinate of the tile to be reached
	private int tileY;

	public MapGoal(GoalType type, int faction) {
		this.type = type;
		this.faction = faction;
	}

	public MapGoal(GoalData data) {
		switch (data.getType()) {
		case ("REACH TILE"):
			type = GoalType.REACH_TILE;
			tileX = data.getMapx();
			tileY = data.getMapy();
			break;
		case ("KILL TARGET"):
			type = GoalType.ELIMINATE_TARGETS;
			targets = data.getTargets();
			break;
		case ("DEFEND TARGET"):
			type = GoalType.DEFEND_TARGETS;
			targets = data.getTargets();
			turns = data.getTurns();
			break;
		default: // Route enemy
			type = GoalType.ROUTE_ENEMY;
			break;
		}
		faction = data.getIndex();
	}

	public boolean isMet(Map map) { // TODO: Update to check turns
		if (type == GoalType.ELIMINATE_TARGETS) {
			boolean completed = true;
			for (int i = 0; i < targets.length; i++) {
				completed &= (map.getUnit(targets[i]).isDead());
			}
			return completed;
		} else if (type == GoalType.DEFEND_TARGETS) {
			boolean completed = true;
			for (int i = 0; i < targets.length; i++) {
				completed &= (!map.getUnit(targets[i]).isDead());
			}
			return completed;
		} else if (type == GoalType.ROUTE_ENEMY) {
			boolean completed = true;
			for (int i = 0; i < map.getUnits().length; i++) {
				if (map.getUnit(i).getFaction() != faction) {
					completed &= map.getUnit(i).isDead();
				}
			}
			return completed;
		} else { // GoalType.REACH_TILE
			boolean completed = true;
			for (int i = 0; i < targets.length; i++) {
				completed &= ((map.getUnit(targets[i]).getMapx() == tileX) && (map
						.getUnit(targets[i]).getMapy() == tileY));
			}
			return completed;
		}
	}

	public int getFaction() {
		return faction;
	}

	public void setFaction(int faction) {
		this.faction = faction;
	}

	public int[] getTargets() {
		return targets;
	}

	public void setTargets(int[] targets) {
		this.targets = targets;
	}

	public int getTurns() {
		return turns;
	}

	public void setTurns(int turns) {
		this.turns = turns;
	}

	public GoalType getType() {
		return type;
	}

	public void setType(GoalType type) {
		this.type = type;
	}

	public int getTileX() {
		return tileX;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}
}
