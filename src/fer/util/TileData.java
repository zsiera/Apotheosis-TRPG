/*
 * 
 */
package fer.util;

// TODO: Auto-generated Javadoc
/**
 * The Class TileData.
 *
 * @author Evan Stewart
 */
public class TileData {

	/** The index. */
	private int index;
	
	/** The name. */
	private String name;
	
	/** The terrain type. */
	private int terrainType;
	
	/** The attackable. */
	private boolean attackable;
	
	/** The avo. */
	private int def, avo;

	/** The sheet path. */
	private String sheetPath;
	
	/** The sheet transparent color. */
	private int sheetWidth, sheetHeight, sheetTransparentColor;
	
	/** The sprite height. */
	private int x, y, spriteWidth, spriteHeight;

	// To be used during game initialization, not set through XML
	/** The sheet index. */
	private int sheetIndex;

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index.
	 *
	 * @param index the new index
	 */
	public void setIndex(int index) {
		this.index = index;
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
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the terrain type.
	 *
	 * @return the terrain type
	 */
	public int getTerrainType() {
		return terrainType;
	}

	/**
	 * Sets the terrain type.
	 *
	 * @param terrainType the new terrain type
	 */
	public void setTerrainType(int terrainType) {
		this.terrainType = terrainType;
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
	 * Sets the attackable.
	 *
	 * @param attackable the new attackable
	 */
	public void setAttackable(boolean attackable) {
		this.attackable = attackable;
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
	 * Gets the sheet path.
	 *
	 * @return the sheet path
	 */
	public String getSheetPath() {
		return sheetPath;
	}

	/**
	 * Sets the sheet path.
	 *
	 * @param sheetPath the new sheet path
	 */
	public void setSheetPath(String sheetPath) {
		this.sheetPath = sheetPath;
	}

	/**
	 * Gets the sheet width.
	 *
	 * @return the sheet width
	 */
	public int getSheetWidth() {
		return sheetWidth;
	}

	/**
	 * Sets the sheet width.
	 *
	 * @param sheetWidth the new sheet width
	 */
	public void setSheetWidth(int sheetWidth) {
		this.sheetWidth = sheetWidth;
	}

	/**
	 * Gets the sheet height.
	 *
	 * @return the sheet height
	 */
	public int getSheetHeight() {
		return sheetHeight;
	}

	/**
	 * Sets the sheet height.
	 *
	 * @param sheetHeight the new sheet height
	 */
	public void setSheetHeight(int sheetHeight) {
		this.sheetHeight = sheetHeight;
	}

	/**
	 * Gets the sheet transparent color.
	 *
	 * @return the sheet transparent color
	 */
	public int getSheetTransparentColor() {
		return sheetTransparentColor;
	}

	/**
	 * Sets the sheet transparent color.
	 *
	 * @param sheetTransparentColor the new sheet transparent color
	 */
	public void setSheetTransparentColor(int sheetTransparentColor) {
		this.sheetTransparentColor = sheetTransparentColor;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the sprite width.
	 *
	 * @return the sprite width
	 */
	public int getSpriteWidth() {
		return spriteWidth;
	}

	/**
	 * Sets the sprite width.
	 *
	 * @param spriteWidth the new sprite width
	 */
	public void setSpriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
	}

	/**
	 * Gets the sprite height.
	 *
	 * @return the sprite height
	 */
	public int getSpriteHeight() {
		return spriteHeight;
	}

	/**
	 * Sets the sprite height.
	 *
	 * @param spriteHeight the new sprite height
	 */
	public void setSpriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
	}

	/**
	 * Gets the sheet index.
	 *
	 * @return the sheet index
	 */
	public int getSheetIndex() {
		return sheetIndex;
	}

	/**
	 * Sets the sheet index.
	 *
	 * @param sheetIndex the new sheet index
	 */
	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}
}
