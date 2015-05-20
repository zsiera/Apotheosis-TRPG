/*
 * 
 */
package fer.gameplay;

import fer.Map;
import fer.util.GoalData;

// TODO: Auto-generated Javadoc
/**
 * The Class MapGoal.
 *
 * @author Evan Stewart
 */
public class MapGoal {

	/**
	 * An enumeration listing all of the basic types of map goals. Goals are
	 * divided into type categories with parameters in order to simplify parsing
	 * from XML.
	 */
	public enum GoalType {
		
		/** The eliminate targets. */
		ELIMINATE_TARGETS("KILL TARGET"), 
 /** The route enemy. */
 ROUTE_ENEMY("ROUTE ENEMY"), 
 /** The defend targets. */
 DEFEND_TARGETS(
				"DEFEND TARGET"), 
 /** The reach tile. */
 REACH_TILE("REACH TILE");

		/** The name. */
		private String name;

		/**
		 * Instantiates a new goal type.
		 *
		 * @param name the name
		 */
		GoalType(final String name) {
			this.name = name;
		}

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}
	}

	/**
	 * The goal's basic type, used to determine what parameters are checked for
	 * completion.
	 */
	private GoalType type;
	/** The index of the faction holding this goal. */
	private int faction;
	/**
	 * An array of unit indexes holding either the units to be eliminated, the
	 * units to be defended, or the units that must reach the specified tile.
	 */
	private int[] targets;
	/** The number of turns to survive for or the turn limit for the goal. */
	private int turns;
	/** The x-coordinate of the tile to be reached. */
	private int tileX;
	/** The y-coordinate of the tile to be reached. */
	private int tileY;

	/**
	 * Instantiates a new map goal.
	 *
	 * @param type the type
	 * @param faction the faction
	 */
	public MapGoal(final GoalType type, final int faction) {
		this.type = type;
		this.faction = faction;
	}

	/**
	 * Instantiates a new map goal.
	 *
	 * @param data the data
	 */
	public MapGoal(final GoalData data) {
		switch (data.getType()) {
		case "REACH TILE":
			type = GoalType.REACH_TILE;
			tileX = data.getMapx();
			tileY = data.getMapy();
			break;
		case "KILL TARGET":
			type = GoalType.ELIMINATE_TARGETS;
			targets = data.getTargets();
			break;
		case "DEFEND TARGET":
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

	/**
	 * Checks if is met.
	 *
	 * @param map the map
	 * @return true, if is met
	 */
	public final boolean isMet(final Map map) { // TODO: Update to check turns
		boolean completed = true;
		if (type == GoalType.ELIMINATE_TARGETS) {
			for (int i = 0; i < targets.length; i++) {
				completed &= map.getUnit(targets[i]).isDead();
			}
		} else if (type == GoalType.DEFEND_TARGETS) {
			for (int i = 0; i < targets.length; i++) {
				completed &= !map.getUnit(targets[i]).isDead();
			}
		} else if (type == GoalType.ROUTE_ENEMY) {
			for (int i = 0; i < map.getUnits().length; i++) {
				if (map.getUnit(i).getFaction() != faction) {
					completed &= map.getUnit(i).isDead();
				}
			}
		} else { // GoalType.REACH_TILE
			for (int i = 0; i < targets.length; i++) {
				completed &= map.getUnit(targets[i]).getMapx() == tileX && map
						.getUnit(targets[i]).getMapy() == tileY;
			}
		}
		return completed;
	}

	/**
	 * Gets the faction.
	 *
	 * @return the faction
	 */
	public final int getFaction() {
		return faction;
	}

	/**
	 * Sets the faction.
	 *
	 * @param faction the new faction
	 */
	public final void setFaction(final int faction) {
		this.faction = faction;
	}

	/**
	 * Gets the targets.
	 *
	 * @return the targets
	 */
	public final int[] getTargets() {
		return targets;
	}

	/**
	 * Sets the targets.
	 *
	 * @param targets the new targets
	 */
	public final void setTargets(final int[] targets) {
		this.targets = targets;
	}

	/**
	 * Gets the turns.
	 *
	 * @return the turns
	 */
	public final int getTurns() {
		return turns;
	}

	/**
	 * Sets the turns.
	 *
	 * @param turns the new turns
	 */
	public final void setTurns(final int turns) {
		this.turns = turns;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public final GoalType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public final void setType(final GoalType type) {
		this.type = type;
	}

	/**
	 * Gets the tile x.
	 *
	 * @return the tile x
	 */
	public final int getTileX() {
		return tileX;
	}

	/**
	 * Sets the tile x.
	 *
	 * @param tileX the new tile x
	 */
	public final void setTileX(final int tileX) {
		this.tileX = tileX;
	}

	/**
	 * Gets the tile y.
	 *
	 * @return the tile y
	 */
	public final int getTileY() {
		return tileY;
	}

	/**
	 * Sets the tile y.
	 *
	 * @param tileY the new tile y
	 */
	public final void setTileY(final int tileY) {
		this.tileY = tileY;
	}
}
