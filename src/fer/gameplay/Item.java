package fer.gameplay;

import fer.Cursor;
import fer.Game;
import fer.graphics.Sprite;
import fer.ui.Font;
import fer.ui.Menu;
import fer.ui.MenuAction;
import fer.ui.MenuCursor;
import fer.ui.MenuElement;
import fer.ui.TextGraphic;
import fer.util.ItemData;

/**
 * @author Evan
 */
public class Item {

	private Sprite icon;
	private String name, description, type;
	private MenuAction used;
	private Cursor cursor = Cursor.getCursor();
	private int uses, maxUses, magnitude;
	private boolean consumable;
	private Menu infoMenu;

	public Item(String iName, Sprite iIcon, MenuAction iAction) {
		icon = iIcon;
		name = iName;
		used = iAction;
	}

	/*
	 * public Item(int typeindex) { switch (typeindex) { case 0: //Morphine Hypo
	 * icon = new Sprite(16, 16, 1, 1, SpriteSheet.ITEMICONSET); name =
	 * "MORPHINE H"; used = new MenuAction() {
	 * 
	 * @Override public void execute(MenuElement caller) { if
	 * (cursor.getSelectedUnit().getCurrentHp() >= 5) {
	 * cursor.getSelectedUnit().
	 * setCurrentHp(cursor.getSelectedUnit().getCurrentHp() - 5); } else {
	 * cursor.getSelectedUnit().setCurrentHp(0); } } }; maxUses = 1; uses =
	 * maxUses; consumable = true; } }
	 */
	public Item(int typeindex) {
		ItemData data = Game.getItemData(typeindex);
		name = data.getName();
		description = data.getDescription();
		consumable = data.getConsumable();
		maxUses = data.getUses();
		uses = maxUses;
		type = data.getActionName();
		switch (type) {
		case ("HEAL"):
			magnitude = data.getHeal();
			used = new MenuAction() {
				@Override
				public void execute(MenuElement caller) {
					cursor.getSelectedUnit().setCurrentHp(
							Math.max(
									Math.min(0, cursor.getSelectedUnit()
											.getCurrentHp() + magnitude),
									cursor.getSelectedUnit().getHp()));
				}
			};
			break;
		default:
			used = new MenuAction() {
				@Override
				public void execute(MenuElement caller) {
					// Do nothing
				}
			};
			break;
		}

		icon = new Sprite(data.getSpriteWidth(), data.getSpriteHeight(),
				data.getX(), data.getY(), Game.getSpriteSheet(data
						.getSheetIndex()));
	}

	public Sprite getIcon() {
		return icon;
	}

	public void setIcon(Sprite icon) {
		this.icon = icon;
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

	public int getUses() {
		return uses;
	}

	public void setUses(int uses) {
		this.uses = uses;
	}

	public int getMaxUses() {
		return maxUses;
	}

	public void setMaxUses(int maxUses) {
		this.maxUses = maxUses;
	}

	public MenuAction getUsedAction() {
		return used;
	}

	public void setUsedAction(MenuAction used) {
		this.used = used;
	}

	public boolean isConsumable() {
		return consumable;
	}

	public void setConsumable(boolean consumable) {
		this.consumable = consumable;
	}

	public void drawInfoMenu(int x, int y, Menu callingMenu, boolean active) {
		infoMenu = new Menu(200, 50, x, y);

		MenuAction nil = getNilMenu();

		infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic(name,
				Font.BASICFONT).getSprite(), false, 23, 7));
		infoMenu.addElement(new MenuElement(nil, nil, icon, false, 7, 7));
		infoMenu.addElementsForDescription(nil, description);

		if (type.equalsIgnoreCase("HEAL"))
			infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic(
					"-Heals for " + magnitude + " points.", Font.BASICFONT)
					.getSprite(), false, 23, 37));

		if (active) {
			MenuCursor.getMenuCursor().setElementIndex(0);
			MenuCursor.setActiveMenu(infoMenu);
		}
	}

	private MenuAction getNilMenu() {
		MenuAction nil = new MenuAction() {
			@Override
			public void execute(MenuElement caller) { // Do nothing
			}
		};
		return nil;
	}

	public boolean infoMenuDrawn() {
		return infoMenu != null;
	}

	public void clearInfoMenu() {
		infoMenu.removeMenu();
		infoMenu = null;
	}
}
