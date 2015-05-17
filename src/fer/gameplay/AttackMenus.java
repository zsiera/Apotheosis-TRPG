package fer.gameplay;

import fer.ui.Font;
import fer.ui.Menu;
import fer.ui.MenuAction;
import fer.ui.MenuElement;
import fer.ui.TextGraphic;
import fer.Map;
import fer.Cursor;
import fer.Unit;

public class AttackMenus extends AttackerDefenderMenu {

	public final void initializeMenus(final Cursor cursor) {
		if (cursor.getMapX() - cursor.getMapScrollx() > Map.MIN_MAP_WIDTH / 2) {
			defaultMenu = new Menu(46, 52, 0, 0);
			weaponMenu = new Menu(30, 76, 0, 52);
			statusMenu = new Menu(131, 16, 46, 0);
			battleMenu = new Menu(131, 16, 46, 16);
		} else {
			defaultMenu = new Menu(46, 52, 194, 0);
			weaponMenu = new Menu(30, 76, 210, 52);
			statusMenu = new Menu(131, 16, 63, 0);
			battleMenu = new Menu(131, 16, 63, 16);
		}
	}

	public final void drawMenu(final Cursor cursor, final Unit attacker, final int attackerDamage,
			final float accAttacker, final float critAttacker) {
		clearMenus();
		MenuAction sa = getSAAction();
		initializeMenus(cursor);

		defaultMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(
				attacker.getName(), Font.BASICFONT)).getSprite(), false, 7, 7));
		if (attacker.getMapFaceSprite() != null) {
			new MenuElement(sa, sa, attacker.getMapFaceSprite(), false, 7, 13);
		}
		if (attacker.getWeapon(0) != null) {
			addWeaponMenuOptions(attacker, sa);
		}
		if (attacker.getArmor() != null) {
			addArmorMenuOptions(attacker, sa);
		}
		addDefaultMenuOptions(attacker, sa);
		addBattleMenuOptions(sa, attackerDamage, accAttacker, critAttacker);
	}
}