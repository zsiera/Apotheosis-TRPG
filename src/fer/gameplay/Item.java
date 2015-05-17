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

	public Item(final String iName, final Sprite iIcon, final MenuAction iAction) {
		icon = iIcon;
		name = iName;
		used = iAction;
	}

//	/**
//	 * Public Item(int typeindex) { switch (typeindex) { case 0: //Morphine Hypo
//	 * icon = new Sprite(16, 16, 1, 1, SpriteSheet.ITEMICONSET); name =
//	 * "MORPHINE H"; used = new MenuAction() {
//	 * 
//	 * @Override public void execute(MenuElement caller) { if
//	 * (cursor.getSelectedUnit().getCurrentHp() >= 5) {
//	 * cursor.getSelectedUnit().
//	 * setCurrentHp(cursor.getSelectedUnit().getCurrentHp() - 5); } else {
//	 * cursor.getSelectedUnit().setCurrentHp(0); } } }; maxUses = 1; uses =
//	 * maxUses; consumable = true; } }
//	 */
	public Item(final int typeindex) {
		ItemData data = Game.getItemData(typeindex);
		name = data.getName();
		description = data.getDescription();
		consumable = data.getConsumable();
		maxUses = data.getUses();
		uses = maxUses;
		type = data.getActionName();
		switch (type) {
		case "HEAL":
			magnitude = data.getHeal();
			used = new MenuAction() {
				@Override
				public void execute(final MenuElement caller) {
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
				public void execute(final MenuElement caller) {
					// Do nothing
				}
			};
			break;
		}

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

	public final String getDescription() {
		return description;
	}

	public final void setDescription(final String description) {
		this.description = description;
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

	public final MenuAction getUsedAction() {
		return used;
	}

	public final void setUsedAction(final MenuAction used) {
		this.used = used;
	}

	public final boolean isConsumable() {
		return consumable;
	}

	public final void setConsumable(final boolean consumable) {
		this.consumable = consumable;
	}

	public final void drawInfoMenu(final int x, final int y, final Menu callingMenu, final boolean active) {
		infoMenu = new Menu(200, 50, x, y);

		MenuAction nil = getNilMenu();

		infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic(name,
				Font.BASICFONT).getSprite(), false, 23, 7));
		infoMenu.addElement(new MenuElement(nil, nil, icon, false, 7, 7));
		infoMenu.addElementsForDescription(nil, description);

		if ("HEAL".equalsIgnoreCase(type)) {
			infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic(
					"-Heals for " + magnitude + " points.", Font.BASICFONT)
					.getSprite(), false, 23, 37));
		}

		if (active) {
			MenuCursor.getMenuCursor().setElementIndex(0);
			MenuCursor.setActiveMenu(infoMenu);
		}
	}

	private MenuAction getNilMenu() {
		return new MenuAction() {
			@Override
			public void execute(final MenuElement caller) { // Do nothing
			}
		};
	}

	public final boolean infoMenuDrawn() {
		return infoMenu != null;
	}

	public final void clearInfoMenu() {
		infoMenu.removeMenu();
		infoMenu = null;
	}
}
