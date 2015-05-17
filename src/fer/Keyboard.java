package fer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Evan Stewart
 *
 *         Gets input from the keyboard, identifies the key providing said
 *         input, and passes it for use in logic classes.
 */
public class Keyboard implements KeyListener, Runnable {

	private boolean[] keys = new boolean[120];
	private boolean up, down, left, right, enter, escape, tab;

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

	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing. TODO: Do something if I find a use for keyTyped.
	}

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

	public boolean isUp() {
		return up;
	}

	public boolean isDown() {
		return down;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}

	public boolean isEnter() {
		return enter;
	}

	public boolean isEscape() {
		return escape;
	}

	public boolean isTab() {
		return tab;
	}

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
