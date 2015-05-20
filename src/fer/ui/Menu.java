/*
 * 
 */
package fer.ui;

import fer.Game;
import fer.Unit;
import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Menu.
 *
 * @author Evan Stewart
 */
public class Menu {

	/** The Constant WINDOWBACKCENTER. */
	public static final Sprite WINDOWBACKCENTER = new Sprite(16, 16, 1, 52,
			SpriteSheet.WINDOWSKIN);
	
	/** The Constant WINDOWBACKTOPLEFT. */
	public static final Sprite WINDOWBACKTOPLEFT = new Sprite(16, 16, 1, 35,
			SpriteSheet.WINDOWSKIN);
	
	/** The Constant WINDOWBACKTOPRIGHT. */
	public static final Sprite WINDOWBACKTOPRIGHT = new Sprite(16, 16, 18, 35,
			SpriteSheet.WINDOWSKIN);
	
	/** The Constant WINDOWBACKBOTTOMLEFT. */
	public static final Sprite WINDOWBACKBOTTOMLEFT = new Sprite(16, 16, 35,
			35, SpriteSheet.WINDOWSKIN);
	
	/** The Constant WINDOWBACKBOTTOMRIGHT. */
	public static final Sprite WINDOWBACKBOTTOMRIGHT = new Sprite(16, 16, 52,
			35, SpriteSheet.WINDOWSKIN);
	
	/** The Constant WINDOWEDGETOPLEFT. */
	public static final Sprite WINDOWEDGETOPLEFT = new Sprite(16, 16, 1, 1,
			SpriteSheet.WINDOWSKIN);
	
	/** The Constant WINDOWEDGETOPRIGHT. */
	public static final Sprite WINDOWEDGETOPRIGHT = new Sprite(16, 16, 18, 1,
			SpriteSheet.WINDOWSKIN);
	
	/** The Constant WINDOWEDGETOPCENTER. */
	public static final Sprite WINDOWEDGETOPCENTER = new Sprite(16, 16, 35, 18,
			SpriteSheet.WINDOWSKIN);
	
	/** The Constant WINDOWEDGEBOTTOMLEFT. */
	public static final Sprite WINDOWEDGEBOTTOMLEFT = new Sprite(16, 16, 35, 1,
			SpriteSheet.WINDOWSKIN);
	
	/** The Constant WINDOWEDGEBOTTOMRIGHT. */
	public static final Sprite WINDOWEDGEBOTTOMRIGHT = new Sprite(16, 16, 52,
			1, SpriteSheet.WINDOWSKIN);
	
	/** The Constant WINDOWEDGEBOTTOMCENTER. */
	public static final Sprite WINDOWEDGEBOTTOMCENTER = new Sprite(16, 16, 52,
			18, SpriteSheet.WINDOWSKIN);
	
	/** The Constant WINDOWEDGELEFT. */
	public static final Sprite WINDOWEDGELEFT = new Sprite(16, 16, 1, 18,
			SpriteSheet.WINDOWSKIN);
	
	/** The Constant WINDOWEDGERIGHT. */
	public static final Sprite WINDOWEDGERIGHT = new Sprite(16, 16, 18, 18,
			SpriteSheet.WINDOWSKIN);
	
	/** The Constant MINWIDTH. */
	public static final int MINWIDTH = 32;
	
	/** The Constant MINHEIGHT. */
	public static final int MINHEIGHT = 32;
	
	/** The elements. */
	private ArrayList<MenuElement> elements;
	
	/** The escape action. */
	private MenuAction escapeAction = null;
	
	/** The left action. */
	private MenuAction leftAction = null;
	
	/** The right action. */
	private MenuAction rightAction = null;
	
	/** The width. */
	private int width;
	
	/** The height. */
	private int height;
	
	/** The x. */
	private int x;
	
	/** The y. */
	private int y;
	
	/** The visible. */
	private boolean visible;

	/**
	 * Instantiates a new menu.
	 *
	 * @param iWidth the i width
	 * @param iHeight the i height
	 * @param ix the ix
	 * @param iy the iy
	 */
	public Menu(int iWidth, int iHeight, int ix, int iy) {
		width = iWidth;
		height = iHeight;
		x = ix;
		y = iy;
		Game.getMenuList().add(this);
		elements = new ArrayList<>();
		visible = true;
	}

	/**
	 * Instantiates a new menu.
	 *
	 * @param iWidth the i width
	 * @param iHeight the i height
	 * @param ix the ix
	 * @param iy the iy
	 * @param iElements the i elements
	 */
	public Menu(int iWidth, int iHeight, int ix, int iy,
			ArrayList<MenuElement> iElements) {
		this(iWidth, iHeight, ix, iy);
		elements = iElements;
	}

	/**
	 * Removes the menu.
	 */
	public void removeMenu() {
		Game.getMenuList().remove(this);
	}

	/**
	 * Gets the elements.
	 *
	 * @return the elements
	 */
	public ArrayList<MenuElement> getElements() {
		return elements;
	}

	/**
	 * Gets the element.
	 *
	 * @param index the index
	 * @return the element
	 */
	public MenuElement getElement(int index) {
		return elements.get(index);
	}

	/**
	 * Sets the element.
	 *
	 * @param index the index
	 * @param iElement the i element
	 */
	public void setElement(int index, MenuElement iElement) {
		elements.set(index, iElement);
	}

	/**
	 * Adds the element.
	 *
	 * @param iElement the i element
	 */
	public void addElement(MenuElement iElement) {
		elements.add(iElement);
	}

	/**
	 * Removes the element.
	 *
	 * @param index the index
	 */
	public void removeElement(int index) {
		elements.remove(index);
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(int height) {
		this.height = height;
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

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the visible.
	 *
	 * @param visible the new visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Gets the escape action.
	 *
	 * @return the escape action
	 */
	public MenuAction getEscapeAction() {
		return escapeAction;
	}

	/**
	 * Sets the escape action.
	 *
	 * @param escapeAction the new escape action
	 */
	public void setEscapeAction(MenuAction escapeAction) {
		this.escapeAction = escapeAction;
	}

	/**
	 * Gets the left action.
	 *
	 * @return the left action
	 */
	public MenuAction getLeftAction() {
		return leftAction;
	}

	/**
	 * Sets the left action.
	 *
	 * @param leftAction the new left action
	 */
	public void setLeftAction(MenuAction leftAction) {
		this.leftAction = leftAction;
	}

	/**
	 * Gets the right action.
	 *
	 * @return the right action
	 */
	public MenuAction getRightAction() {
		return rightAction;
	}

	/**
	 * Sets the right action.
	 *
	 * @param rightAction the new right action
	 */
	public void setRightAction(MenuAction rightAction) {
		this.rightAction = rightAction;
	}

	/**
	 * Wrap text.
	 *
	 * @param text the text
	 * @param lineLength the line length
	 * @return the string[]
	 */
	public static String[] wrapText(String text, int lineLength) {
		ArrayList<String> lines = new ArrayList<>();
		boolean wrapped = false;
		while (!wrapped) {
			int i;
			for (i = lineLength - 1; i >= 0; i--) {
				if (text.toCharArray()[i] == 32) {
					break;
				}
			}
			if (i == 0) {
				i = lineLength - 1;
			}
			lines.add(text.substring(0, i));
			text = text.substring(i + 1);
			if (text.length() <= lineLength) {
				lines.add(text);
				wrapped = true;
			}
		}

		String[] strings = new String[lines.size()];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = lines.get(i);
		}
		return strings;
	}

	/**
	 * Adds the elements to defender menu.
	 *
	 * @param gain the gain
	 * @param sa the sa
	 * @param newExp the new exp
	 * @param levelGain the level gain
	 * @param defender the defender
	 */
	public void addElementsToDefenderMenu(int gain, MenuAction sa, int newExp,
			int levelGain, Unit defender) {
		addElement(new MenuElement(sa, sa, new TextGraphic(
				"NEW EXP: " + newExp, Font.BASICFONT).getSprite(), false, 7, 25));
		addElement(new MenuElement(sa, sa, new TextGraphic(
				"NEW EXP: " + newExp, Font.BASICFONT).getSprite(), false, 7, 25));
		addElement(new MenuElement(sa, sa,
				new TextGraphic("LVL: " + defender.getLevel() + " -> "
						+ (defender.getLevel() + levelGain), Font.BASICFONT)
						.getSprite(), false, 7, 31));
		addElement(new MenuElement(sa, sa,
				new TextGraphic("LVL: " + defender.getLevel() + " -> "
						+ (defender.getLevel() + levelGain), Font.BASICFONT)
						.getSprite(), false, 7, 31));
		addElement(new MenuElement(sa, sa, new TextGraphic("TO NEXT: "
				+ (Unit.EXP_CAP - newExp), Font.BASICFONT).getSprite(), false,
				7, 37));
	}

	/**
	 * Adds the elements for description.
	 *
	 * @param nil the nil
	 * @param description the description
	 */
	public void addElementsForDescription(MenuAction nil, String description) {
		String[] lines = Menu.wrapText(description, 34);
		for (int i = 0; i < Math.min(4, lines.length); i++) {
			addElement(new MenuElement(nil, nil, new TextGraphic(lines[i],
					Font.BASICFONT).getSprite(), false, 23, 13 + (6 * i)));
		}
	}
}
