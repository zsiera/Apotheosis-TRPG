package fer.ui;

import fer.Game;
import fer.Renderer;
import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;
import java.util.ArrayList;

/**
 * @author Evan Stewart
 */
public class Menu {

    public static final Sprite WINDOWBACKCENTER = new Sprite(16, 16, 1, 52, SpriteSheet.WINDOWSKIN);
    public static final Sprite WINDOWBACKTOPLEFT = new Sprite(16, 16, 1, 35, SpriteSheet.WINDOWSKIN);
    public static final Sprite WINDOWBACKTOPRIGHT = new Sprite(16, 16, 18, 35, SpriteSheet.WINDOWSKIN);
    public static final Sprite WINDOWBACKBOTTOMLEFT = new Sprite(16, 16, 35, 35, SpriteSheet.WINDOWSKIN);
    public static final Sprite WINDOWBACKBOTTOMRIGHT = new Sprite(16, 16, 52, 35, SpriteSheet.WINDOWSKIN);
    public static final Sprite WINDOWEDGETOPLEFT = new Sprite(16, 16, 1, 1, SpriteSheet.WINDOWSKIN);
    public static final Sprite WINDOWEDGETOPRIGHT = new Sprite(16, 16, 18, 1, SpriteSheet.WINDOWSKIN);
    public static final Sprite WINDOWEDGETOPCENTER = new Sprite(16, 16, 35, 18, SpriteSheet.WINDOWSKIN);
    public static final Sprite WINDOWEDGEBOTTOMLEFT = new Sprite(16, 16, 35, 1, SpriteSheet.WINDOWSKIN);
    public static final Sprite WINDOWEDGEBOTTOMRIGHT = new Sprite(16, 16, 52, 1, SpriteSheet.WINDOWSKIN);
    public static final Sprite WINDOWEDGEBOTTOMCENTER = new Sprite(16, 16, 52, 18, SpriteSheet.WINDOWSKIN);
    public static final Sprite WINDOWEDGELEFT = new Sprite(16, 16, 1, 18, SpriteSheet.WINDOWSKIN);
    public static final Sprite WINDOWEDGERIGHT = new Sprite(16, 16, 18, 18, SpriteSheet.WINDOWSKIN);
    public static final int MINWIDTH = 32;
    public static final int MINHEIGHT = 32;
    private ArrayList<MenuElement> elements;
    private MenuAction escapeAction = null;
    private MenuAction leftAction = null;
    private MenuAction rightAction = null;
    private int width;
    private int height;
    private int x;
    private int y;
    private boolean visible;

    public Menu(int iWidth, int iHeight, int ix, int iy) {
        width = iWidth;
        height = iHeight;
        x = ix;
        y = iy;
        Game.getMenuList().add(this);
        elements = new ArrayList();
        visible = true;
    }

    public Menu(int iWidth, int iHeight, int ix, int iy, ArrayList iElements) {
        this(iWidth, iHeight, ix, iy);
        elements = iElements;
    }

    public void removeMenu() {
        Game.getMenuList().remove(this);
    }

    public ArrayList<MenuElement> getElements() {
        return elements;
    }

    public MenuElement getElement(int index) {
        return elements.get(index);
    }

    public void setElement(int index, MenuElement iElement) {
        elements.set(index, iElement);
    }

    public void addElement(MenuElement iElement) {
        elements.add(iElement);
    }

    public void removeElement(int index) {
        elements.remove(index);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public MenuAction getEscapeAction() {
        return escapeAction;
    }

    public void setEscapeAction(MenuAction escapeAction) {
        this.escapeAction = escapeAction;
    }

    public MenuAction getLeftAction() {
        return leftAction;
    }

    public void setLeftAction(MenuAction leftAction) {
        this.leftAction = leftAction;
    }

    public MenuAction getRightAction() {
        return rightAction;
    }

    public void setRightAction(MenuAction rightAction) {
        this.rightAction = rightAction;
    }

    public static String[] wrapText(String text, int lineLength) {
        ArrayList<String> lines = new ArrayList();
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
}
