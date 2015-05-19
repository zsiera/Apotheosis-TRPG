/*
 * 
 */
package fer.util;

// TODO: Auto-generated Javadoc
/**
 * The Class UnitData.
 *
 * @author Evan Stewart
 */
public class UnitData {

	/** The index. */
	private int index;
	
	/** The name. */
	private String name;
	
	/** The unit class. */
	private int unitClass;
	
	/** The exp. */
	private int level, exp;
	
	/** The auto level. */
	private boolean autoLevel;
	
	/** The con. */
	private int hp, str, skl, spd, def, res, mov, con;
	
	/** The weapons. */
	private int[] weapons;
	
	/** The items. */
	private int[] items;
	
	/** The armor. */
	private int armor;

	/** The mapx. */
	private int mapx;
	
	/** The mapy. */
	private int mapy;
	
	/** The faction. */
	private int faction;

	/** The has face sprite. */
	private boolean hasFaceSprite;
	
	/** The sheet path. */
	private String sheetPath;
	
	/** The sheet transparent color. */
	private int sheetWidth, sheetHeight, sheetTransparentColor;
	
	/** The sprite height. */
	private int x, y, spriteWidth, spriteHeight;

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
	 * Gets the unit class.
	 *
	 * @return the unit class
	 */
	public int getUnitClass() {
		return unitClass;
	}

	/**
	 * Sets the unit class.
	 *
	 * @param unitClass the new unit class
	 */
	public void setUnitClass(int unitClass) {
		this.unitClass = unitClass;
	}

	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the level.
	 *
	 * @param level the new level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Gets the exp.
	 *
	 * @return the exp
	 */
	public int getExp() {
		return exp;
	}

	/**
	 * Sets the exp.
	 *
	 * @param exp the new exp
	 */
	public void setExp(int exp) {
		this.exp = exp;
	}

	/**
	 * Checks if is auto level.
	 *
	 * @return true, if is auto level
	 */
	public boolean isAutoLevel() {
		return autoLevel;
	}

	/**
	 * Sets the auto level.
	 *
	 * @param autoLevel the new auto level
	 */
	public void setAutoLevel(boolean autoLevel) {
		this.autoLevel = autoLevel;
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
	 * Gets the weapons.
	 *
	 * @return the weapons
	 */
	public int[] getWeapons() {
		return weapons;
	}

	/**
	 * Sets the weapons.
	 *
	 * @param weapons the new weapons
	 */
	public void setWeapons(int[] weapons) {
		this.weapons = weapons;
	}

	/**
	 * Gets the items.
	 *
	 * @return the items
	 */
	public int[] getItems() {
		return items;
	}

	/**
	 * Sets the items.
	 *
	 * @param items the new items
	 */
	public void setItems(int[] items) {
		this.items = items;
	}

	/**
	 * Gets the armor.
	 *
	 * @return the armor
	 */
	public int getArmor() {
		return armor;
	}

	/**
	 * Sets the armor.
	 *
	 * @param armor the new armor
	 */
	public void setArmor(int armor) {
		this.armor = armor;
	}

	/**
	 * Gets the mapx.
	 *
	 * @return the mapx
	 */
	public int getMapx() {
		return mapx;
	}

	/**
	 * Sets the mapx.
	 *
	 * @param mapx the new mapx
	 */
	public void setMapx(int mapx) {
		this.mapx = mapx;
	}

	/**
	 * Gets the mapy.
	 *
	 * @return the mapy
	 */
	public int getMapy() {
		return mapy;
	}

	/**
	 * Sets the mapy.
	 *
	 * @param mapy the new mapy
	 */
	public void setMapy(int mapy) {
		this.mapy = mapy;
	}

	/**
	 * Gets the faction.
	 *
	 * @return the faction
	 */
	public int getFaction() {
		return faction;
	}

	/**
	 * Sets the faction.
	 *
	 * @param faction the new faction
	 */
	public void setFaction(int faction) {
		this.faction = faction;
	}

	/**
	 * Checks if is checks for face sprite.
	 *
	 * @return true, if is checks for face sprite
	 */
	public boolean isHasFaceSprite() {
		return hasFaceSprite;
	}

	/**
	 * Sets the checks for face sprite.
	 *
	 * @param hasFaceSprite the new checks for face sprite
	 */
	public void setHasFaceSprite(boolean hasFaceSprite) {
		this.hasFaceSprite = hasFaceSprite;
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
}
