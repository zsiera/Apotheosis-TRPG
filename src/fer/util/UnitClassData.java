/*
 * 
 */
package fer.util;

import java.net.URL;

// TODO: Auto-generated Javadoc
/**
 * The Class UnitClassData.
 *
 * @author Evan Stewart
 */
public class UnitClassData {

	/** The index. */
	private int index;
	
	/** The name. */
	private String name;
	
	/** The female. */
	private boolean female;
	
	/** The con. */
	private int hp, str, skl, spd, def, res, mov, con;
	// private int sword, axe, lance, bow, anima, light, dark, staff;
	/** The movetype. */
	private int[] movetype;

	/** The sheet path. */
	private String sheetPath;
	
	/** The sheet transparent color. */
	private int sheetWidth, sheetHeight, sheetSpacing, sheetTransparentColor;
	
	/** The key frames. */
	private int[] spriteWidths, spriteHeights, numFrames, keyFrames;
	
	/** The frame durations. */
	private int[][] frameDurations;

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
	 * Checks if is female.
	 *
	 * @return true, if is female
	 */
	public boolean isFemale() {
		return female;
	}

	/**
	 * Sets the female.
	 *
	 * @param female the new female
	 */
	public void setFemale(boolean female) {
		this.female = female;
	}

	/**
	 * Gets the hp.
	 *
	 * @return the hp
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * Sets the hp.
	 *
	 * @param hp the new hp
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}

	/**
	 * Gets the str.
	 *
	 * @return the str
	 */
	public int getStr() {
		return str;
	}

	/**
	 * Sets the str.
	 *
	 * @param str the new str
	 */
	public void setStr(int str) {
		this.str = str;
	}

	/**
	 * Gets the skl.
	 *
	 * @return the skl
	 */
	public int getSkl() {
		return skl;
	}

	/**
	 * Sets the skl.
	 *
	 * @param skl the new skl
	 */
	public void setSkl(int skl) {
		this.skl = skl;
	}

	/**
	 * Gets the spd.
	 *
	 * @return the spd
	 */
	public int getSpd() {
		return spd;
	}

	/**
	 * Sets the spd.
	 *
	 * @param spd the new spd
	 */
	public void setSpd(int spd) {
		this.spd = spd;
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
	 * Gets the res.
	 *
	 * @return the res
	 */
	public int getRes() {
		return res;
	}

	/**
	 * Sets the res.
	 *
	 * @param res the new res
	 */
	public void setRes(int res) {
		this.res = res;
	}

	/**
	 * Gets the mov.
	 *
	 * @return the mov
	 */
	public int getMov() {
		return mov;
	}

	/**
	 * Sets the mov.
	 *
	 * @param mov the new mov
	 */
	public void setMov(int mov) {
		this.mov = mov;
	}

	/**
	 * Gets the con.
	 *
	 * @return the con
	 */
	public int getCon() {
		return con;
	}

	/**
	 * Sets the con.
	 *
	 * @param con the new con
	 */
	public void setCon(int con) {
		this.con = con;
	}

	/**
	 * Gets the movetype.
	 *
	 * @return the movetype
	 */
	public int[] getMovetype() {
		return movetype;
	}

	/**
	 * Sets the movetype.
	 *
	 * @param movetype the new movetype
	 */
	public void setMovetype(int[] movetype) {
		this.movetype = movetype;
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
	 * Gets the sheet spacing.
	 *
	 * @return the sheet spacing
	 */
	public int getSheetSpacing() {
		return sheetSpacing;
	}

	/**
	 * Sets the sheet spacing.
	 *
	 * @param sheetSpacing the new sheet spacing
	 */
	public void setSheetSpacing(int sheetSpacing) {
		this.sheetSpacing = sheetSpacing;
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
	 * Gets the sprite widths.
	 *
	 * @return the sprite widths
	 */
	public int[] getSpriteWidths() {
		return spriteWidths;
	}

	/**
	 * Sets the sprite widths.
	 *
	 * @param spriteWidths the new sprite widths
	 */
	public void setSpriteWidths(int[] spriteWidths) {
		this.spriteWidths = spriteWidths;
	}

	/**
	 * Gets the sprite heights.
	 *
	 * @return the sprite heights
	 */
	public int[] getSpriteHeights() {
		return spriteHeights;
	}

	/**
	 * Sets the sprite heights.
	 *
	 * @param spriteHeights the new sprite heights
	 */
	public void setSpriteHeights(int[] spriteHeights) {
		this.spriteHeights = spriteHeights;
	}

	/**
	 * Gets the num frames.
	 *
	 * @return the num frames
	 */
	public int[] getNumFrames() {
		return numFrames;
	}

	/**
	 * Sets the num frames.
	 *
	 * @param numFrames the new num frames
	 */
	public void setNumFrames(int[] numFrames) {
		this.numFrames = numFrames;
	}

	/**
	 * Gets the key frames.
	 *
	 * @return the key frames
	 */
	public int[] getKeyFrames() {
		return keyFrames;
	}

	/**
	 * Sets the key frames.
	 *
	 * @param keyFrames the new key frames
	 */
	public void setKeyFrames(int[] keyFrames) {
		this.keyFrames = keyFrames;
	}

	/**
	 * Gets the frame durations.
	 *
	 * @return the frame durations
	 */
	public int[][] getFrameDurations() {
		return frameDurations;
	}

	/**
	 * Sets the frame durations.
	 *
	 * @param frameDurations the new frame durations
	 */
	public void setFrameDurations(int[][] frameDurations) {
		this.frameDurations = frameDurations;
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