/*
 * 
 */
package fer.gameplay;

import fer.ui.Font;
import fer.ui.Menu;
import fer.ui.MenuAction;
import fer.ui.MenuElement;
import fer.ui.TextGraphic;
import fer.Map;
import fer.Cursor;
import fer.Unit;

// TODO: Auto-generated Javadoc
/**
 * The Class DefenderMenus.
 */
public class DefenderMenus extends AttackerDefenderMenu {

	/* (non-Javadoc)
	 * @see fer.gameplay.AttackerDefenderMenu#initializeMenus(fer.Cursor)
	 */
	public final void initializeMenus(final Cursor cursor) {
		if (cursor.getMapX() - cursor.getMapScrollx() > Map.MIN_MAP_WIDTH / 2) {
			defaultMenu = new Menu(46, 52, 194, 108);
			weaponMenu = new Menu(30, 76, 210, 32);
			statusMenu = new Menu(131, 16, 63, 144);
			battleMenu = new Menu(131, 16, 63, 128);
		} else {
			defaultMenu = new Menu(46, 52, 0, 108);
			weaponMenu = new Menu(30, 76, 0, 32);
			statusMenu = new Menu(131, 16, 46, 144);
			battleMenu = new Menu(131, 16, 46, 128);
		}
	}

	/* (non-Javadoc)
	 * @see fer.gameplay.AttackerDefenderMenu#drawMenu(fer.Cursor, fer.Unit, int, float, float)
	 */
	public final void drawMenu(final Cursor cursor, final Unit defender, final int defenderDamage,
			final float accDefender, final float critDefender) {
		clearMenus();
		MenuAction sa = getSAAction();
		initializeMenus(cursor);

		defaultMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(
				defender.getName(), Font.BASICFONT)).getSprite(), false, 7, 7));
		if (defender.getMapFaceSprite() != null) {
			defaultMenu.addElement(new MenuElement(sa, sa, defender
					.getMapFaceSprite(), false, 7, 13));
		}
		if (defender.getWeapon(0) != null) {
			addWeaponMenuOptions(defender, sa);
		}
		if (defender.getArmor() != null) {
			addArmorMenuOptions(defender, sa);
		}
		addDefaultMenuOptions(defender, sa);
		addBattleMenuOptions(sa, defenderDamage, accDefender, critDefender);
	}
}