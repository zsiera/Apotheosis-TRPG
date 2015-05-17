package fer.ui;

/**
 * @author Evan Stewart
 */
public class MenuCursor {

	public static final int CURSOR_COLOR = 0xFFFF00;
	private static MenuCursor instance;
	private static Menu activeMenu;
	private int elementIndex;
	private boolean active;

	public MenuCursor() {
		active = true;
	}

	public static MenuCursor getMenuCursor() {
		if (instance == null) {
			instance = new MenuCursor();
		}

		return instance;
	}

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

	public static Menu getActiveMenu() {
		return activeMenu;
	}

	public static void setActiveMenu(Menu activeMenu) {
		MenuCursor.activeMenu = activeMenu;
	}

	public int getElementIndex() {
		return elementIndex;
	}

	public void setElementIndex(int elementIndex) {
		this.elementIndex = elementIndex;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean iActive) {
		active = iActive;
	}
}
