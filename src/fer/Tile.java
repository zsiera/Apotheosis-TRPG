/*
 * 
 */
package fer;

import fer.ai.Exclusions;
import fer.graphics.Sprite;
import fer.util.TileData;

// TODO: Auto-generated Javadoc
/**
 * The Class Tile.
 *
 * @author Evan Stewart
 * 
 *         Primarily a storage class, each tile is assigned a type index
 *         directing it to it's properties within the corresponding XML
 *         document, which it can then pass on to other parts of the program.
 */
public class Tile {

	/** The Constant TILE_WIDTH. */
	public static final int TILE_WIDTH = 16;
	
	/** The Constant TILE_HEIGHT. */
	public static final int TILE_HEIGHT = 16;
	
	/** The Constant IMPASSIBLE. */
	public static final int IMPASSIBLE = 60;
	
	/** The color. */
	private int typeIndex, terrainType, color = 0;
	
	/** The avo. */
	private int mapx, mapy, def, avo;
	
	/** The attackable. */
	private boolean attackable;
	
	/** The sprite. */
	private Sprite sprite;
	
	/** The name. */
	private String name;

	/*
	 * public Tile(int type) { typeIndex = type; sprite =
	 * Sprite.getTileSprite(typeIndex); terrainType = getTileTerrainType(type);
	 * name = getTileName(type); attackable = getTileAttackability(typeIndex); }
	 * 
	 * public Tile(int type, int mapx, int mapy) { this(type); this.mapx = mapx;
	 * this.mapy = mapy; }
	 */
	/**
	 * Instantiates a new tile.
	 *
	 * @param typeindex the typeindex
	 */
	public Tile(int typeindex) {
		typeIndex = typeindex;
		TileData data = Game.getTileData(typeindex);
		name = data.getName();
		terrainType = data.getTerrainType();
		attackable = data.isAttackable();
		def = data.getDef();
		avo = data.getAvo();

		sprite = new Sprite(data.getSpriteWidth(), data.getSpriteHeight(),
				data.getX(), data.getY(), Game.getSpriteSheet(data
						.getSheetIndex()));
	}

	/**
	 * Instantiates a new tile.
	 *
	 * @param typeindex the typeindex
	 * @param mapx the mapx
	 * @param mapy the mapy
	 */
	public Tile(int typeindex, int mapx, int mapy) {
		this(typeindex);
		this.mapx = mapx;
		this.mapy = mapy;
	}

	/**
	 * Sets the color.
	 *
	 * @param newColor the new color
	 */
	public void setColor(int newColor) {
		color = newColor;
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public int getColor() {
		return color;
	}

	/**
	 * Gets the sprite.
	 *
	 * @return the sprite
	 */
	public Sprite getSprite() {
		return sprite;
	}

	/**
	 * Gets the tile terrain type.
	 *
	 * @param typeIndex the type index
	 * @return the tile terrain type
	 */
	public static int getTileTerrainType(int typeIndex) {
		switch (typeIndex) {
		case 0:
			return 0;
		case 1:
			return 0;
		case 2:
			return 0;
		case 3:
			return 0;
		case 4:
			return 0;
		case 5:
			return 1;
		default:
			return 0;
		}
	}

	/**
	 * Gets the tile attackability.
	 *
	 * @param typeIndex the type index
	 * @return the tile attackability
	 */
	public static boolean getTileAttackability(int typeIndex) {
		switch (typeIndex) {
		case 0:
			return true;
		case 1:
			return true;
		case 2:
			return true;
		case 3:
			return true;
		case 4:
			return true;
		case 5:
			return true;
		default:
			return true;
		}
	}

	/**
	 * Gets the tile name.
	 *
	 * @param typeIndex the type index
	 * @return the tile name
	 */
	public static String getTileName(int typeIndex) {
		switch (typeIndex) {
		case 0:
			return "GRASSLAND";
		case 1:
			return "PLAINS";
		case 2:
			return "DIRT";
		case 3:
			return "GRANITE";
		case 4:
			return "GRAVEL";
		case 5:
			return "HOUSE";
		default:
			return "TERRAIN";
		}
	}

	/**
	 * Gets the map x.
	 *
	 * @return the map x
	 */
	public int getMapX() {
		return mapx;
	}

	/**
	 * Gets the map y.
	 *
	 * @return the map y
	 */
	public int getMapY() {
		return mapy;
	}

	/**
	 * Gets the terrain.
	 *
	 * @return the terrain
	 */
	public int getTerrain() {
		return terrainType;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Checks if is attackable.
	 *
	 * @return true, if is attackable
	 */
	public boolean isAttackable() {
		return attackable;
	}

	/**
	 * Gets the def.
	 *
	 * @return the def
	 */
	public int getDef() {
		return def;
	}

	/**
	 * Sets the def.
	 *
	 * @param def the new def
	 */
	public void setDef(int def) {
		this.def = def;
	}

	/**
	 * Gets the avo.
	 *
	 * @return the avo
	 */
	public int getAvo() {
		return avo;
	}

	/**
	 * Sets the avo.
	 *
	 * @param avo the new avo
	 */
	public void setAvo(int avo) {
		this.avo = avo;
	}

	/**
	 * Sets the attackable.
	 *
	 * @param attackable the new attackable
	 */
	public void setAttackable(boolean attackable) {
		this.attackable = attackable;
	}

	/**
	 * Gets the type index.
	 *
	 * @return the type index
	 */
	public int getTypeIndex() {
		return typeIndex;
	}

	/**
	 * Gets the movement cost.
	 *
	 * @param unit the unit
	 * @param unitCollision the unit collision
	 * @param exclusions the exclusions
	 * @return the movement cost
	 */
	public int getMovementCost(Unit unit, boolean unitCollision,
			Exclusions exclusions) {
		if (Game.getCurrentMap().getUnitTile(
				getMapX() + getMapY() * Game.getCurrentMap().getWidth()) != null) {
			if (Game.getCurrentMap()
					.getUnitTile(
							getMapX() + getMapY()
									* Game.getCurrentMap().getWidth())
					.getFaction() != unit.getFaction()
					&& unitCollision
					&& (getMapX() != exclusions.getExcludeX() || getMapY() != exclusions
							.getExcludeY())) {
				return Tile.IMPASSIBLE;
			}
		}
		return unit.getUnitClass().getMoveCost(getTerrain());
	}

	/**
	 * Gets the manhattan heuristic.
	 *
	 * @param target the target
	 * @return the manhattan heuristic
	 */
	public int getManhattanHeuristic(Tile target) {
		return (Math.abs(getMapX() - target.getMapX()) + Math.abs(getMapY()
				- target.getMapY()));
	}

	/**
	 * Gets the cost.
	 *
	 * @param unit the unit
	 * @param target the target
	 * @param unitCollision the unit collision
	 * @param exclusions the exclusions
	 * @return the cost
	 */
	public int getCost(Unit unit, Tile target, boolean unitCollision,
			Exclusions exclusions) {
		return getManhattanHeuristic(target)
				+ getMovementCost(unit, unitCollision, exclusions);
	}
}
