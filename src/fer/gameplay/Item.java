/*
 * 
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class Item.
 *
 * @author Evan
 */
public class Item {

	/** The icon. */
	private Sprite icon;
	
	/** The type. */
	private String name, description, type;
	
	/** The used. */
	private MenuAction used;
	
	/** The cursor. */
	private Cursor cursor = Cursor.getCursor();
	
	/** The magnitude. */
	private int uses, maxUses, magnitude;
	
	/** The consumable. */
	private boolean consumable;
	
	/** The info menu. */
	private Menu infoMenu;

	/**
	 * Instantiates a new item.
	 *
	 * @param iName the i name
	 * @param iIcon the i icon
	 * @param iAction the i action
	 */
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
	/**
 * Instantiates a new item.
 *
 * @param typeindex the typeindex
 */
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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public final void setDescription(final String description) {
		this.description = description;
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
	 * Gets the used action.
	 *
	 * @return the used action
	 */
	public final MenuAction getUsedAction() {
		return used;
	}

	/**
	 * Sets the used action.
	 *
	 * @param used the new used action
	 */
	public final void setUsedAction(final MenuAction used) {
		this.used = used;
	}

	/**
	 * Checks if is consumable.
	 *
	 * @return true, if is consumable
	 */
	public final boolean isConsumable() {
		return consumable;
	}

	/**
	 * Sets the consumable.
	 *
	 * @param consumable the new consumable
	 */
	public final void setConsumable(final boolean consumable) {
		this.consumable = consumable;
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

	/**
	 * Gets the nil menu.
	 *
	 * @return the nil menu
	 */
	private MenuAction getNilMenu() {
		return new MenuAction() {
			@Override
			public void execute(final MenuElement caller) { // Do nothing
			}
		};
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
