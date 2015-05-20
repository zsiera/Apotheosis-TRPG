/*
 * 
 */
package fer.ui;

// TODO: Auto-generated Javadoc
/**
 * The Interface MenuAction.
 *
 * @author Evan Stewart
 * 
 *         A simple interface that covers the actions carried out when a menu
 *         element is selected or pressed. When a menu element is activated, the
 *         execute method of its corresponding menu action is called.
 */
public interface MenuAction {

	/**
	 * Execute.
	 *
	 * @param caller the caller
	 */
	public void execute(MenuElement caller);

}
