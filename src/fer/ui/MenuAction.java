package fer.ui;

/**
 * @author Evan Stewart
 * 
 *         A simple interface that covers the actions carried out when a menu
 *         element is selected or pressed. When a menu element is activated, the
 *         execute method of its corresponding menu action is called.
 */
public interface MenuAction {

	public void execute(MenuElement caller);

}
