/*
 * 
 */
package fer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class Keyboard.
 *
 * @author Evan Stewart
 * 
 *         Gets input from the keyboard, identifies the key providing said
 *         input, and passes it for use in logic classes.
 */
public class Keyboard implements KeyListener, Runnable {

	/** The keys. */
	private boolean[] keys = new boolean[120];
	
	/** The tab. */
	private boolean up, down, left, right, enter, escape, tab;

	/**
	 * Update.
	 */
	public void update() {
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		enter = keys[KeyEvent.VK_ENTER];
		escape = keys[KeyEvent.VK_ESCAPE];
		tab = keys[KeyEvent.VK_TAB];
		/*
		 * for (int i = 0; i < keys.length; i++) { if (keys[i]) {
		 * System.out.println(i); } }
		 */
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing. TODO: Do something if I find a use for keyTyped.
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		try {
			keys[e.getKeyCode()] = true;
		} catch (ArrayIndexOutOfBoundsException err) {
			System.err.println("The pressed key is not supported.  Key: "
					+ e.getKeyCode());
			err.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		try {
			keys[e.getKeyCode()] = false;
		} catch (ArrayIndexOutOfBoundsException err) {
			System.err.println("The released key is not supported.  Key: "
					+ e.getKeyCode());
			err.printStackTrace();
		}
	}

	/**
	 * Checks if is up.
	 *
	 * @return true, if is up
	 */
	public boolean isUp() {
		return up;
	}

	/**
	 * Checks if is down.
	 *
	 * @return true, if is down
	 */
	public boolean isDown() {
		return down;
	}

	/**
	 * Checks if is left.
	 *
	 * @return true, if is left
	 */
	public boolean isLeft() {
		return left;
	}

	/**
	 * Checks if is right.
	 *
	 * @return true, if is right
	 */
	public boolean isRight() {
		return right;
	}

	/**
	 * Checks if is enter.
	 *
	 * @return true, if is enter
	 */
	public boolean isEnter() {
		return enter;
	}

	/**
	 * Checks if is escape.
	 *
	 * @return true, if is escape
	 */
	public boolean isEscape() {
		return escape;
	}

	/**
	 * Checks if is tab.
	 *
	 * @return true, if is tab
	 */
	public boolean isTab() {
		return tab;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (Game.getGame().getGameRunning()) {
			update();
			try {
				Thread.sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
	}
}
