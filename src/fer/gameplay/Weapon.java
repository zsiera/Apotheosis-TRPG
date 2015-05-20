/*
 * 
 */
package fer.gameplay;

import fer.Game;
import fer.graphics.Sprite;
import fer.ui.Font;
import fer.ui.Menu;
import fer.ui.MenuAction;
import fer.ui.MenuCursor;
import fer.ui.MenuElement;
import fer.ui.TextGraphic;
import fer.util.WeaponData;

// TODO: Auto-generated Javadoc
/**
 * The Class Weapon.
 *
 * @author Evan
 */
public class Weapon {

	/** The icon. */
	private Sprite icon;
	
	/** The description. */
	private String name, description;
	
	/** The melee. */
	private boolean melee;
	
	/** The price. */
	private int range, damage, pierce, critical, accuracy, weight, uses,
			maxUses, price;
	/** For ranged weapons, weight is replaced (in the UI) with rate of fire. */
	private Menu infoMenu;

	// Public Weapon(int typeindex) { switch (typeindex) { case 0: //ACR M01
	// name = "ACR M01"; icon = new Sprite(16, 16, 1, 1,
	// SpriteSheet.WEAPONICONSET);

	// melee = false; range = 3; damage = 10; pierce = 2; critical = 0; accuracy
	// = 90; weight = 3;
	//
	// maxUses = 45; uses = maxUses; } }
	 
	/**
	 * Instantiates a new weapon.
	 *
	 * @param typeindex the typeindex
	 */
	public Weapon(final int typeindex) {
		WeaponData data = Game.getWeaponData(typeindex);
		name = data.getName();
		description = data.getDescription();
		melee = data.getMelee();
		range = data.getRange();
		damage = data.getDamage();
		pierce = data.getPierce();
		critical = data.getCritical();
		accuracy = data.getAccuracy();
		weight = data.getWeight();
		maxUses = data.getUses();
		uses = maxUses;
		price = data.getPrice();

		icon = new Sprite(data.getSpriteWidth(), data.getSpriteHeight(),
				data.getX(), data.getY(), Game.getSpriteSheet(data
						.getSheetIndex()));

	}

	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public final Sprite getIcon() {
		return icon;
	}

	/**
	 * Sets the icon.
	 *
	 * @param icon the new icon
	 */
	public final void setIcon(final Sprite icon) {
		this.icon = icon;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * Checks if is melee.
	 *
	 * @return true, if is melee
	 */
	public final boolean isMelee() {
		return melee;
	}

	/**
	 * Sets the melee.
	 *
	 * @param melee the new melee
	 */
	public final void setMelee(final boolean melee) {
		this.melee = melee;
	}

	/**
	 * Gets the range.
	 *
	 * @return the range
	 */
	public final int getRange() {
		return range;
	}

	/**
	 * Sets the range.
	 *
	 * @param range the new range
	 */
	public final void setRange(final int range) {
		this.range = range;
	}

	/**
	 * Gets the damage.
	 *
	 * @return the damage
	 */
	public final int getDamage() {
		return damage;
	}

	/**
	 * Sets the damage.
	 *
	 * @param damage the new damage
	 */
	public final void setDamage(final int damage) {
		this.damage = damage;
	}

	/**
	 * Gets the pierce.
	 *
	 * @return the pierce
	 */
	public final int getPierce() {
		return pierce;
	}

	/**
	 * Sets the pierce.
	 *
	 * @param pierce the new pierce
	 */
	public final void setPierce(final int pierce) {
		this.pierce = pierce;
	}

	/**
	 * Gets the critical.
	 *
	 * @return the critical
	 */
	public final int getCritical() {
		return critical;
	}

	/**
	 * Sets the critical.
	 *
	 * @param critical the new critical
	 */
	public final void setCritical(final int critical) {
		this.critical = critical;
	}

	/**
	 * Gets the accuracy.
	 *
	 * @return the accuracy
	 */
	public final int getAccuracy() {
		return accuracy;
	}

	/**
	 * Sets the accuracy.
	 *
	 * @param accuracy the new accuracy
	 */
	public final void setAccuracy(final int accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public final int getWeight() {
		return weight;
	}

	/**
	 * Sets the weight.
	 *
	 * @param weight the new weight
	 */
	public final void setWeight(final int weight) {
		this.weight = weight;
	}

	/**
	 * Gets the uses.
	 *
	 * @return the uses
	 */
	public final int getUses() {
		return uses;
	}

	/**
	 * Sets the uses.
	 *
	 * @param uses the new uses
	 */
	public final void setUses(final int uses) {
		this.uses = uses;
	}

	/**
	 * Gets the max uses.
	 *
	 * @return the max uses
	 */
	public final int getMaxUses() {
		return maxUses;
	}

	/**
	 * Sets the max uses.
	 *
	 * @param maxUses the new max uses
	 */
	public final void setMaxUses(final int maxUses) {
		this.maxUses = maxUses;
	}

	/**
	 * Draw info menu.
	 *
	 * @param x the x
	 * @param y the y
	 * @param callingMenu the calling menu
	 * @param active the active
	 */
	public final void drawInfoMenu(final int x, final int y, final Menu callingMenu, final boolean active) {
		infoMenu = new Menu(200, 56, x, y);
		MenuAction nil = getNilAction();
		addElements(nil);
		if (active) {
			MenuCursor.getMenuCursor().setElementIndex(0);
			MenuCursor.setActiveMenu(infoMenu);
		}
	}

	/**
	 * Gets the nil action.
	 *
	 * @return the nil action
	 */
	private MenuAction getNilAction() {
		return new MenuAction() {
			@Override
			public void execute(final MenuElement caller) { // Do nothing
			}
		};
	}

	/**
	 * Adds the elements.
	 *
	 * @param nil the nil
	 */
	private void addElements(final MenuAction nil) {
		infoMenu.addElement(new MenuElement(nil, nil, icon, false, 7, 7));
		infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic(name,
				Font.BASICFONT).getSprite(), false, 23, 7));
		String[] lines = Menu.wrapText(description, 34);
		for (int i = 0; i < Math.min(3, lines.length); i++) {
			infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic(
					lines[i], Font.BASICFONT).getSprite(), false, 23,
					13 + 6 * i));
		}
		infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("RNG: "
				+ range, Font.BASICFONT).getSprite(), false, 7, 31));
		infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("DMG: "
				+ damage, Font.BASICFONT).getSprite(), false, 47, 31));
		infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("PRC: "
				+ pierce, Font.BASICFONT).getSprite(), false, 87, 31));
		infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("CRT: "
				+ critical, Font.BASICFONT).getSprite(), false, 127, 31));
		infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("ACC: "
				+ accuracy, Font.BASICFONT).getSprite(), false, 7, 37));
		infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("WGT: "
				+ weight, Font.BASICFONT).getSprite(), false, 47, 37));
		infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("VALUE: "
				+ price, Font.BASICFONT).getSprite(), false, 87, 37));
		infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("USES: "
				+ uses + "/" + maxUses, Font.BASICFONT).getSprite(), false, 7,
				43));
	}

	/**
	 * Info menu drawn.
	 *
	 * @return true, if successful
	 */
	public final boolean infoMenuDrawn() {
		return infoMenu != null;
	}

	/**
	 * Clear info menu.
	 */
	public final void clearInfoMenu() {
		infoMenu.removeMenu();
		infoMenu = null;
	}
}
