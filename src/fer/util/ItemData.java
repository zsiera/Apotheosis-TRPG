package fer.util;

/**
 * @author Evan Stewart A data messenger class to be deserialized from user
 *         inputted xml and used to pass data to constructed Item instances.
 */
public class ItemData {

	private int index;
	private String name, description;
	private String actionName;
	private boolean consumable;
	private int price, uses, heal;

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

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public boolean getConsumable() {
		return consumable;
	}

	public void setConsumable(boolean consumable) {
		this.consumable = consumable;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getUses() {
		return uses;
	}

	public void setUses(int uses) {
		this.uses = uses;
	}

	public int getHeal() {
		return heal;
	}

	public void setHeal(int heal) {
		this.heal = heal;
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
