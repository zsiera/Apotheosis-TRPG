/*
 * 
 */
package fer.ui;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuCursor.
 *
 * @author Evan Stewart
 */
public class MenuCursor {

	/** The Constant CURSOR_COLOR. */
	public static final int CURSOR_COLOR = 0xFFFF00;
	
	/** The instance. */
	private static MenuCursor instance;
	
	/** The active menu. */
	private static Menu activeMenu;
	
	/** The element index. */
	private int elementIndex;
	
	/** The active. */
	private boolean active;

	/**
	 * Instantiates a new menu cursor.
	 */
	public MenuCursor() {
		active = true;
	}

	/**
	 * Gets the menu cursor.
	 *
	 * @return the menu cursor
	 */
	public static MenuCursor getMenuCursor() {
		if (instance == null) {
			instance = new MenuCursor();
		}

		return instance;
	}

	/**
	 * Update.
	 *
	 * @param xScrollQueue the x scroll queue
	 * @param yScrollQueue the y scroll queue
	 * @param enter the enter
	 * @param escape the escape
	 */
	public void update(int xScrollQueue, int yScrollQueue, boolean enter,
			boolean escape) {
		// Update the active element based upon the scroll queue
		do {
			if (elementIndex + yScrollQueue < 0) {
				elementIndex = activeMenu.getElements().size()
						+ (elementIndex + yScrollQueue);
			} else if (elementIndex + yScrollQueue >= activeMenu.getElements()
					.size()) {
				elementIndex = (elementIndex + yScrollQueue)
						- activeMenu.getElements().size();
			} else {
				elementIndex += yScrollQueue;
			}
			if (yScrollQueue > 0) {
				yScrollQueue = 1;
			} else if (yScrollQueue < 0) {
				yScrollQueue = -1;
			}
		} while (!activeMenu.getElement(elementIndex).isSelectable());
		// If enter is pressed, run the active element's pressedAction
		if (enter) {
			activeMenu.getElement(elementIndex).getPressedAction()
					.execute(activeMenu.getElement(elementIndex));
		} else if (escape && activeMenu.getEscapeAction() != null) {
			activeMenu.getEscapeAction().execute(null);
		} else if (xScrollQueue < 0 && activeMenu.getLeftAction() != null) {
			activeMenu.getLeftAction().execute(null);
		} else if (xScrollQueue > 0 && activeMenu.getRightAction() != null) {
			activeMenu.getRightAction().execute(null);
		} else { // Otherwise, run the active element's selectedAction
			activeMenu.getElement(elementIndex).getSelectedAction()
					.execute(activeMenu.getElement(elementIndex));
		}
	}

	/**
	 * Gets the active menu.
	 *
	 * @return the active menu
	 */
	public static Menu getActiveMenu() {
		return activeMenu;
	}

	/**
	 * Sets the active menu.
	 *
	 * @param activeMenu the new active menu
	 */
	public static void setActiveMenu(Menu activeMenu) {
		MenuCursor.activeMenu = activeMenu;
	}

	/**
	 * Gets the element index.
	 *
	 * @return the element index
	 */
	public int getElementIndex() {
		return elementIndex;
	}

	/**
	 * Sets the element index.
	 *
	 * @param elementIndex the new element index
	 */
	public void setElementIndex(int elementIndex) {
		this.elementIndex = elementIndex;
	}

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param iActive the new active
	 */
	public void setActive(boolean iActive) {
		active = iActive;
	}
}
