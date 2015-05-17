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
	private boolean defenderExpOpen = false;
	private boolean attackerExpOpen = false;

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
		addElementsToAttackerMenu(gain, sa, close, newExp, levelGain, attacker);
	}

	private void addElementsToAttackerMenu(int gain, MenuAction sa,
			MenuAction close, int newExp, int levelGain, Unit attacker) {
		attackerExpMenu.addElement(new MenuElement(sa, close, new TextGraphic(
				attacker.getName(), Font.BASICFONT).getSprite(), true, 7, 7));
		attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"OLD EXP: " + attacker.getExp(), Font.BASICFONT).getSprite(),
				false, 7, 13));
		attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"        +" + gain, Font.BASICFONT).getSprite(), false, 7, 19));
		attackerExpMenu
				.addElement(new MenuElement(sa, sa, new TextGraphic("NEW EXP: "
						+ newExp, Font.BASICFONT).getSprite(), false, 7, 25));
		attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"LVL: " + attacker.getLevel() + " -> "
						+ (attacker.getLevel() + levelGain), Font.BASICFONT)
				.getSprite(), false, 7, 31));
		attackerExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"TO NEXT: " + (Unit.EXP_CAP - newExp), Font.BASICFONT)
				.getSprite(), false, 7, 37));
	}
	
	public void drawAttackerExpMenu(int gain, Unit attacker){
		attackerExpOpen = true;
		if (attackerExpMenu != null) {
			attackerExpMenu.removeMenu();
		}
		createAttackerMenu(gain, attacker);
		MenuCursor.getMenuCursor().setElementIndex(0);
		MenuCursor.setActiveMenu(attackerExpMenu);
	}
	
	private void createDefenderMenu(int gain, Unit defender){
		defenderExpMenu = new Menu(114, 50, 63, 30);
		MenuAction sa = getSAAction();
		MenuAction close = getCloseDefenderAction();
		defenderExpMenu.setEscapeAction(close);
		int newExp = (defender.getExp() + gain) % Unit.EXP_CAP;
		int levelGain = (int) (defender.getExp() + gain) / Unit.EXP_CAP;
		addElementsToDefenderMenu(gain, sa, newExp, levelGain, defender);
	}
	
	private void addElementsToDefenderMenu(int gain, MenuAction sa, int newExp, int levelGain, Unit defender) {
		defenderExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("NEW EXP: " + newExp, Font.BASICFONT).getSprite(), false, 7, 25));		         defenderExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("NEW EXP: " + newExp, Font.BASICFONT).getSprite(), false, 7, 25));
        defenderExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("LVL: " + defender.getLevel() + " -> " + (defender.getLevel() + levelGain), Font.BASICFONT).getSprite(), false, 7, 31));		         defenderExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("LVL: " + defender.getLevel() + " -> " + (defender.getLevel() + levelGain), Font.BASICFONT).getSprite(), false, 7, 31));
        defenderExpMenu.addElement(new MenuElement(sa, sa, new TextGraphic("TO NEXT: " + (Unit.EXP_CAP - newExp), Font.BASICFONT).getSprite(), false, 7, 37));
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
		MenuAction close = new MenuAction() {
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
		return close;
	}
	
	private MenuAction getCloseDefenderAction() {
		MenuAction close = new MenuAction() {
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
		return close;
	}
	
    private MenuAction getSAAction() {
		MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };
		return sa;
	}
}