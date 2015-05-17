package fer.gameplay;

import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;
import fer.ui.Font;
import fer.ui.Menu;
import fer.ui.MenuAction;
import fer.ui.MenuElement;
import fer.ui.TextGraphic;
import fer.Game;
import fer.Cursor;
import fer.Unit;

public abstract class AttackerDefenderMenu {
	protected Menu defaultMenu;
	protected Menu statusMenu;
	protected Menu weaponMenu;
	protected Menu battleMenu;

	public final Menu getMenu() {
		return defaultMenu;
	}

	public final void setMenu(final Menu menu) {
		this.defaultMenu = menu;
	}

	public final Menu getStatusMenu() {
		return statusMenu;
	}

	public final void setStatusMenu(final Menu statusMenu) {
		this.statusMenu = statusMenu;
	}

	public final Menu getWeaponMenu() {
		return weaponMenu;
	}

	public final void setWeaponMenu(final Menu weaponMenu) {
		this.weaponMenu = weaponMenu;
	}

	public final Menu getBattleMenu() {
		return battleMenu;
	}

	public final void setBattleMenu(final Menu battleMenu) {
		this.battleMenu = battleMenu;
	}

	public abstract void initializeMenus(Cursor cursor);

	public final void clearMenus() {
		if (defaultMenu != null) {
			Game.getMenuList().remove(defaultMenu);
			Game.getMenuList().remove(weaponMenu);
			Game.getMenuList().remove(statusMenu);
			Game.getMenuList().remove(battleMenu);
		}
	}

	public abstract void drawMenu(Cursor cursor, Unit unit, int damage,
			float acc, float crit);

	protected final void addBattleMenuOptions(final MenuAction sa, final int damage, final float acc,
			final float crit) {
		battleMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("MT: "
				+ damage, Font.BASICFONT)).getSprite(), false, 7, 5));
		battleMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("AC: "
				+ Math.round(acc), Font.BASICFONT)).getSprite(), false, 47, 5));
		battleMenu
				.addElement(new MenuElement(sa, sa, (new TextGraphic("CR: "
						+ Math.round(crit), Font.BASICFONT)).getSprite(),
						false, 87, 5));
	}

	protected final void addDefaultMenuOptions(final Unit unit, final MenuAction sa) {
		defaultMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("HP:"
				+ unit.getCurrentHp(), Font.BASICFONT)).getSprite(), false, 7,
				5));
		defaultMenu.addElement(new MenuElement(sa, sa, new Sprite(92, 5, 1,
				12, SpriteSheet.HEALTHBAR), false, 32, 5));
		defaultMenu.addElement(new MenuElement(sa, sa, new Sprite(
				(int) ((92 * unit.getCurrentHp()) / unit.getHp()), 5, 1, 18,
				SpriteSheet.HEALTHBAR), false, 32, 5));
		defaultMenu.addElement(new MenuElement(sa, sa, new Sprite(92, 5, 1,
				24, SpriteSheet.HEALTHBAR), false, 32, 5));
	}

	protected final void addWeaponMenuOptions(final Unit unit, final MenuAction sa) {
		weaponMenu.addElement(new MenuElement(sa, sa, unit.getWeapon(0)
				.getIcon(), false, 7, 7));
		weaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("D:"
				+ unit.getWeapon(0).getDamage(), Font.BASICFONT)).getSprite(),
				false, 5, 24));
		weaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("P:"
				+ unit.getWeapon(0).getPierce(), Font.BASICFONT)).getSprite(),
				false, 5, 30));
		weaponMenu.addElement(new MenuElement(sa, sa,
				(new TextGraphic("A:" + unit.getWeapon(0).getAccuracy(),
						Font.BASICFONT)).getSprite(), false, 5, 36));
	}

	protected final void addArmorMenuOptions(final Unit unit, final MenuAction sa) {
		weaponMenu.addElement(new MenuElement(sa, sa,
				unit.getArmor().getIcon(), false, 7, 42));
		weaponMenu.addElement(new MenuElement(sa, sa,
				(new TextGraphic("R:" + unit.getArmor().getResilience(),
						Font.BASICFONT)).getSprite(), false, 5, 58));
		weaponMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("E:"
				+ unit.getArmor().getEncumberance(), Font.BASICFONT))
				.getSprite(), false, 5, 64));
	}

	protected final MenuAction getSAAction() {
		return new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				// Do nothing.
			}
		};
	}
}