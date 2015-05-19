/*
 * 
 */
package fer.gameplay;

import fer.ui.Menu;
import fer.ui.MenuAction;
import fer.Unit;
import fer.ui.MenuCursor;
import fer.ui.MenuElement;
import fer.ui.TextGraphic;
import fer.ui.Font;

// TODO: Auto-generated Javadoc
/**
 * The Class ExpMenus.
 */
public class ExpMenus {
	
	/** The attacker exp menu. */
	private Menu attackerExpMenu;
	
	/** The defender exp menu. */
	private Menu defenderExpMenu;
	
	/** The defender exp open. */
	private boolean defenderExpOpen;
	
	/** The attacker exp open. */
	private boolean attackerExpOpen;

	/**
	 * Gets the attacker exp menu.
	 *
	 * @return the attacker exp menu
	 */
	public final Menu getAttackerExpMenu() {
		return attackerExpMenu;
	}

	/**
	 * Sets the attacker exp menu.
	 *
	 * @param attackerExpMenu the new attacker exp menu
	 */
	public final void setAttackerExpMenu(final Menu attackerExpMenu) {
		this.attackerExpMenu = attackerExpMenu;
	}

	/**
	 * Gets the defender exp menu.
	 *
	 * @return the defender exp menu
	 */
	public final Menu getDefenderExpMenu() {
		return defenderExpMenu;
	}

	/**
	 * Sets the defender exp menu.
	 *
	 * @param defenderExpMenu the new defender exp menu
	 */
	public final void setDefenderExpMenu(final Menu defenderExpMenu) {
		this.defenderExpMenu = defenderExpMenu;
	}

	/**
	 * Gets the defender exp open.
	 *
	 * @return the defender exp open
	 */
	public final boolean getDefenderExpOpen() {
		return defenderExpOpen;
	}

	/**
	 * Sets the defender exp open.
	 *
	 * @param defenderExpOpen the new defender exp open
	 */
	public final void setDefenderExpOpen(final boolean defenderExpOpen) {
		this.defenderExpOpen = defenderExpOpen;
	}

	/**
	 * Gets the attacker exp open.
	 *
	 * @return the attacker exp open
	 */
	public final boolean getAttackerExpOpen() {
		return attackerExpOpen;
	}

	/**
	 * Sets the attacker exp open.
	 *
	 * @param attackerExpOpen the new attacker exp open
	 */
	public final void setAttackerExpOpen(final boolean attackerExpOpen) {
		this.attackerExpOpen = attackerExpOpen;
	}

	/**
	 * Creates the attacker menu.
	 *
	 * @param gain the gain
	 * @param attacker the attacker
	 */
	private void createAttackerMenu(final int gain, final Unit attacker) {
		attackerExpMenu = new Menu(114, 50, 63, 30);
		MenuAction sa = getSAAction();
		MenuAction close = getCloseAttackerAction();
		attackerExpMenu.setEscapeAction(close);
		int newExp = (attacker.getExp() + gain) % Unit.EXP_CAP;
		int levelGain = (int) (attacker.getExp() + gain) / Unit.EXP_CAP;
		attacker.addElementsToAttackerMenu(gain, sa, close, newExp, levelGain,
				attackerExpMenu);
	}

	/**
	 * Draw attacker exp menu.
	 *
	 * @param gain the gain
	 * @param attacker the attacker
	 */
	public final void drawAttackerExpMenu(final int gain, final Unit attacker) {
		attackerExpOpen = true;
		if (attackerExpMenu != null) {
			attackerExpMenu.removeMenu();
		}
		createAttackerMenu(gain, attacker);
		MenuCursor.getMenuCursor().setElementIndex(0);
		MenuCursor.setActiveMenu(attackerExpMenu);
	}

	/**
	 * Creates the defender menu.
	 *
	 * @param gain the gain
	 * @param defender the defender
	 */
	private void createDefenderMenu(final int gain, final Unit defender) {
		defenderExpMenu = new Menu(114, 50, 63, 30);
		MenuAction sa = getSAAction();
		MenuAction close = getCloseDefenderAction();
		defenderExpMenu.setEscapeAction(close);
		int newExp = (defender.getExp() + gain) % Unit.EXP_CAP;
		int levelGain = (int) (defender.getExp() + gain) / Unit.EXP_CAP;
		defenderExpMenu.addElementsToDefenderMenu(gain, sa, newExp, levelGain,
				defender);
	}

	/**
	 * Draw defender exp menu.
	 *
	 * @param gain the gain
	 * @param defender the defender
	 */
	public final void drawDefenderExpMenu(final int gain, final Unit defender) {
		defenderExpOpen = true;
		if (defenderExpMenu != null) {
			defenderExpMenu.removeMenu();
		}
		createDefenderMenu(gain, defender);
		MenuCursor.getMenuCursor().setElementIndex(0);
		MenuCursor.setActiveMenu(defenderExpMenu);
	}

	/**
	 * Gets the close attacker action.
	 *
	 * @return the close attacker action
	 */
	private MenuAction getCloseAttackerAction() {
		return new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				attackerExpMenu.removeMenu();
				attackerExpOpen = false;
				if (defenderExpMenu != null) {
					defenderExpMenu.removeMenu();
				}
				defenderExpOpen = false;
			}
		};
	}

	/**
	 * Gets the close defender action.
	 *
	 * @return the close defender action
	 */
	private MenuAction getCloseDefenderAction() {
		return new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				defenderExpMenu.removeMenu();
				defenderExpOpen = false;
				if (attackerExpMenu != null) {
					attackerExpMenu.removeMenu();
				}
				attackerExpOpen = false;
			}
		};
	}

	/**
	 * Gets the SA action.
	 *
	 * @return the SA action
	 */
	private MenuAction getSAAction() {
		return new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				// Do nothing.
			}
		};
	}
}