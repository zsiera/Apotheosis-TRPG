/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fer.util;

// TODO: Auto-generated Javadoc
/**
 * The Class WeaponData.
 *
 * @author Evan Stewart A middleman class that is used to store information from
 *         deserialized XML documents that will be used to initialize weapon
 *         instances.
 */
public class WeaponData {

	/** The index. */
	private int index;
	
	/** The description. */
	private String name, description;
	
	/** The melee. */
	private boolean melee;
	
	/** The price. */
	private int damage, pierce, critical, accuracy, weight, range, uses, price;

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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the melee.
	 *
	 * @return the melee
	 */
	public boolean getMelee() {
		return melee;
	}

	/**
	 * Sets the melee.
	 *
	 * @param melee the new melee
	 */
	public void setMelee(boolean melee) {
		this.melee = melee;
	}

	/**
	 * Gets the damage.
	 *
	 * @return the damage
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Sets the damage.
	 *
	 * @param damage the new damage
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * Gets the pierce.
	 *
	 * @return the pierce
	 */
	public int getPierce() {
		return pierce;
	}

	/**
	 * Sets the pierce.
	 *
	 * @param pierce the new pierce
	 */
	public void setPierce(int pierce) {
		this.pierce = pierce;
	}

	/**
	 * Gets the accuracy.
	 *
	 * @return the accuracy
	 */
	public int getAccuracy() {
		return accuracy;
	}

	/**
	 * Sets the accuracy.
	 *
	 * @param accuracy the new accuracy
	 */
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Sets the weight.
	 *
	 * @param weight the new weight
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * Gets the range.
	 *
	 * @return the range
	 */
	public int getRange() {
		return range;
	}

	/**
	 * Sets the range.
	 *
	 * @param range the new range
	 */
	public void setRange(int range) {
		this.range = range;
	}

	/**
	 * Gets the uses.
	 *
	 * @return the uses
	 */
	public int getUses() {
		return uses;
	}

	/**
	 * Sets the uses.
	 *
	 * @param uses the new uses
	 */
	public void setUses(int uses) {
		this.uses = uses;
	}

	/**
	 * Gets the critical.
	 *
	 * @return the critical
	 */
	public int getCritical() {
		return critical;
	}

	/**
	 * Sets the critical.
	 *
	 * @param critical the new critical
	 */
	public void setCritical(int critical) {
		this.critical = critical;
	}

	/**
	 * Gets the price.
	 *
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Sets the price.
	 *
	 * @param price the new price
	 */
	public void setPrice(int price) {
		this.price = price;
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
