/*
 * 
 */
package fer.ui;

import fer.graphics.Sprite;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuElement.
 *
 * @author Evan Stewart
 * 
 *         The class that stores the information for each object within a menu,
 *         including its graphical sprite, its position within the window,
 *         whether or not it is selectable by the cursor, and the actions to be
 *         performed both when the element (if it is selectable) is selected
 *         (highlighted with the cursor) and when it is pressed (when the
 *         affirmative key, likely enter, is pressed while the element is
 *         selected).
 */
public class MenuElement {

	/** The selected action. */
	private MenuAction selectedAction;
	
	/** The pressed action. */
	private MenuAction pressedAction;
	
	/** The graphic. */
	private Sprite graphic;

	/** The selectable. */
	private boolean selectable;
	
	/** The x. */
	private int x;
	
	/** The y. */
	private int y;

	/**
	 * Instantiates a new menu element.
	 *
	 * @param iSelectAction the i select action
	 * @param iPressAction the i press action
	 * @param iGraphic the i graphic
	 * @param iSelectable the i selectable
	 * @param ix the ix
	 * @param iy the iy
	 */
	public MenuElement(MenuAction iSelectAction, MenuAction iPressAction,
			Sprite iGraphic, boolean iSelectable, int ix, int iy) {
		selectedAction = iSelectAction;
		pressedAction = iPressAction;
		graphic = iGraphic;
		selectable = iSelectable;
		x = ix;
		y = iy;
	}

	/**
	 * Gets the selected action.
	 *
	 * @return the selected action
	 */
	public MenuAction getSelectedAction() {
		return selectedAction;
	}

	/**
	 * Sets the selected action.
	 *
	 * @param selectedAction the new selected action
	 */
	public void setSelectedAction(MenuAction selectedAction) {
		this.selectedAction = selectedAction;
	}

	/**
	 * Gets the pressed action.
	 *
	 * @return the pressed action
	 */
	public MenuAction getPressedAction() {
		return pressedAction;
	}

	/**
	 * Sets the pressed action.
	 *
	 * @param pressedAction the new pressed action
	 */
	public void setPressedAction(MenuAction pressedAction) {
		this.pressedAction = pressedAction;
	}

	/**
	 * Gets the graphic.
	 *
	 * @return the graphic
	 */
	public Sprite getGraphic() {
		return graphic;
	}

	/**
	 * Sets the graphic.
	 *
	 * @param graphic the new graphic
	 */
	public void setGraphic(Sprite graphic) {
		this.graphic = graphic;
	}

	/**
	 * Checks if is selectable.
	 *
	 * @return true, if is selectable
	 */
	public boolean isSelectable() {
		return selectable;
	}

	/**
	 * Sets the selectable.
	 *
	 * @param selectable the new selectable
	 */
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(int y) {
		this.y = y;
	}
}
