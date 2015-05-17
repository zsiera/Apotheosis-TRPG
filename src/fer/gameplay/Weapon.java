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

/**
 * @author Evan
 */
public class Weapon {

	private Sprite icon;
	private String name, description;
	private boolean melee;
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

	public final Sprite getIcon() {
		return icon;
	}

	public final void setIcon(final Sprite icon) {
		this.icon = icon;
	}

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final boolean isMelee() {
		return melee;
	}

	public final void setMelee(final boolean melee) {
		this.melee = melee;
	}

	public final int getRange() {
		return range;
	}

	public final void setRange(final int range) {
		this.range = range;
	}

	public final int getDamage() {
		return damage;
	}

	public final void setDamage(final int damage) {
		this.damage = damage;
	}

	public final int getPierce() {
		return pierce;
	}

	public final void setPierce(final int pierce) {
		this.pierce = pierce;
	}

	public final int getCritical() {
		return critical;
	}

	public final void setCritical(final int critical) {
		this.critical = critical;
	}

	public final int getAccuracy() {
		return accuracy;
	}

	public final void setAccuracy(final int accuracy) {
		this.accuracy = accuracy;
	}

	public final int getWeight() {
		return weight;
	}

	public final void setWeight(final int weight) {
		this.weight = weight;
	}

	public final int getUses() {
		return uses;
	}

	public final void setUses(final int uses) {
		this.uses = uses;
	}

	public final int getMaxUses() {
		return maxUses;
	}

	public final void setMaxUses(final int maxUses) {
		this.maxUses = maxUses;
	}

	public final void drawInfoMenu(final int x, final int y, final Menu callingMenu, final boolean active) {
		infoMenu = new Menu(200, 56, x, y);
		MenuAction nil = getNilAction();
		addElements(nil);
		if (active) {
			MenuCursor.getMenuCursor().setElementIndex(0);
			MenuCursor.setActiveMenu(infoMenu);
		}
	}

	private MenuAction getNilAction() {
		return new MenuAction() {
			@Override
			public void execute(final MenuElement caller) { // Do nothing
			}
		};
	}

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

	public final boolean infoMenuDrawn() {
		return infoMenu != null;
	}

	public final void clearInfoMenu() {
		infoMenu.removeMenu();
		infoMenu = null;
	}
}
