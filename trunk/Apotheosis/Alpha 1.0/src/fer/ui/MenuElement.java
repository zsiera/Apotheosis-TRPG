package fer.ui;

import fer.graphics.Sprite;

/**
 * @author Evan Stewart
 * 
 * The class that stores the information for each object within a menu, 
 * including its graphical sprite, its position within the window, whether
 * or not it is selectable by the cursor, and the actions to be performed
 * both when the element (if it is selectable) is selected (highlighted with
 * the cursor) and when it is pressed (when the affirmative key, likely enter,
 * is pressed while the element is selected).
 */
public class MenuElement {
    
    private MenuAction selectedAction;
    private MenuAction pressedAction;
    private Sprite graphic;
    
    private boolean selectable;
    private int x;
    private int y;
    
    public MenuElement(MenuAction iSelectAction, MenuAction iPressAction, 
            Sprite iGraphic, boolean iSelectable, int ix, int iy) {
        selectedAction = iSelectAction;
        pressedAction = iPressAction;
        graphic = iGraphic;
        selectable = iSelectable;
        x = ix;
        y = iy;
    }

    public MenuAction getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(MenuAction selectedAction) {
        this.selectedAction = selectedAction;
    }

    public MenuAction getPressedAction() {
        return pressedAction;
    }

    public void setPressedAction(MenuAction pressedAction) {
        this.pressedAction = pressedAction;
    }

    public Sprite getGraphic() {
        return graphic;
    }

    public void setGraphic(Sprite graphic) {
        this.graphic = graphic;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
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
}
