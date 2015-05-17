/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fer.util;

/**
 * @author Evan Stewart A middleman class that is used to store information from
 *         deserialized XML documents that will be used to initialize weapon
 *         instances.
 */
public class WeaponData {

	private int index;
	private String name, description;
	private boolean melee;
	private int damage, pierce, critical, accuracy, weight, range, uses, price;

	private String sheetPath;
	private int sheetWidth, sheetHeight, sheetTransparentColor;
	private int x, y, spriteWidth, spriteHeight;

	// To be used during game initialization, not set through XML
	private int sheetIndex;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getMelee() {
		return melee;
	}

	public void setMelee(boolean melee) {
		this.melee = melee;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getPierce() {
		return pierce;
	}

	public void setPierce(int pierce) {
		this.pierce = pierce;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getUses() {
		return uses;
	}

	public void setUses(int uses) {
		this.uses = uses;
	}

	public int getCritical() {
		return critical;
	}

	public void setCritical(int critical) {
		this.critical = critical;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getSheetPath() {
		return sheetPath;
	}

	public void setSheetPath(String sheetPath) {
		this.sheetPath = sheetPath;
	}

	public int getSheetWidth() {
		return sheetWidth;
	}

	public void setSheetWidth(int sheetWidth) {
		this.sheetWidth = sheetWidth;
	}

	public int getSheetHeight() {
		return sheetHeight;
	}

	public void setSheetHeight(int sheetHeight) {
		this.sheetHeight = sheetHeight;
	}

	public int getSheetTransparentColor() {
		return sheetTransparentColor;
	}

	public void setSheetTransparentColor(int sheetTransparentColor) {
		this.sheetTransparentColor = sheetTransparentColor;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public void setSpriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
	}

	public int getSpriteHeight() {
		return spriteHeight;
	}

	public void setSpriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}
}
