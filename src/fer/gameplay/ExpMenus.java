package fer.gameplay;

import fer.ui.Menu;
import fer.ui.MenuAction;
import fer.Unit;
import fer.ui.MenuCursor;
import fer.ui.MenuElement;
import fer.ui.TextGraphic;
import fer.ui.Font;

public class ExpMenus {
	private Menu attackerExpMenu;
	private Menu defenderExpMenu;
	private boolean defenderExpOpen;
	private boolean attackerExpOpen;

	public Menu getAttackerExpMenu() {
		return attackerExpMenu;
	}

	public void setAttackerExpMenu(Menu attackerExpMenu) {
		this.attackerExpMenu = attackerExpMenu;
	}

	public Menu getDefenderExpMenu() {
		return defenderExpMenu;
	}

	public void setDefenderExpMenu(Menu defenderExpMenu) {
		this.defenderExpMenu = defenderExpMenu;
	}

	public boolean getDefenderExpOpen() {
		return defenderExpOpen;
	}

	public void setDefenderExpOpen(boolean defenderExpOpen) {
		this.defenderExpOpen = defenderExpOpen;
	}

	public boolean getAttackerExpOpen() {
		return attackerExpOpen;
	}

	public void setAttackerExpOpen(boolean attackerExpOpen) {
		this.attackerExpOpen = attackerExpOpen;
	}

	private void createAttackerMenu(int gain, Unit attacker) {
		attackerExpMenu = new Menu(114, 50, 63, 30);
		MenuAction sa = getSAAction();
		MenuAction close = getCloseAttackerAction();
		attackerExpMenu.setEscapeAction(close);
		int newExp = (attacker.getExp() + gain) % Unit.EXP_CAP;
		int levelGain = (int) (attacker.getExp() + gain) / Unit.EXP_CAP;
		attacker.addElementsToAttackerMenu(gain, sa, close, newExp, levelGain,
				attackerExpMenu);
	}

	public void drawAttackerExpMenu(int gain, Unit attacker) {
		attackerExpOpen = true;
		if (attackerExpMenu != null) {
			attackerExpMenu.removeMenu();
		}
		createAttackerMenu(gain, attacker);
		MenuCursor.getMenuCursor().setElementIndex(0);
		MenuCursor.setActiveMenu(attackerExpMenu);
	}

	private void createDefenderMenu(int gain, Unit defender) {
		defenderExpMenu = new Menu(114, 50, 63, 30);
		MenuAction sa = getSAAction();
		MenuAction close = getCloseDefenderAction();
		defenderExpMenu.setEscapeAction(close);
		int newExp = (defender.getExp() + gain) % Unit.EXP_CAP;
		int levelGain = (int) (defender.getExp() + gain) / Unit.EXP_CAP;
		defenderExpMenu.addElementsToDefenderMenu(gain, sa, newExp, levelGain,
				defender);
	}

	public void drawDefenderExpMenu(int gain, Unit defender) {
		defenderExpOpen = true;
		if (defenderExpMenu != null) {
			defenderExpMenu.removeMenu();
		}
		createDefenderMenu(gain, defender);
		MenuCursor.getMenuCursor().setElementIndex(0);
		MenuCursor.setActiveMenu(defenderExpMenu);
	}

	private MenuAction getCloseAttackerAction() {
		return new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				attackerExpMenu.removeMenu();
				attackerExpOpen = false;
				if (defenderExpMenu != null) {
					defenderExpMenu.removeMenu();
				}
				defenderExpOpen = false;
			}
		};
	}

	private MenuAction getCloseDefenderAction() {
		return new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				defenderExpMenu.removeMenu();
				defenderExpOpen = false;
				if (attackerExpMenu != null) {
					attackerExpMenu.removeMenu();
				}
				attackerExpOpen = false;
			}
		};
	}

	private MenuAction getSAAction() {
		return new MenuAction() {
			@Override
			public void execute(MenuElement caller) {
				// Do nothing.
			}
		};
	}
}