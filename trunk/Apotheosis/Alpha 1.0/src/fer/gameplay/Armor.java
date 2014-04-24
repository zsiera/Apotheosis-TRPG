package fer.gameplay;

import fer.Game;
import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;
import fer.util.ArmorData;

/**
 * @author Evan
 */
public class Armor {
    
    private Sprite icon;
    private String name;
    
    private int resilience, encumberance, price;
    
    /*public Armor(int typeindex) {
        switch(typeindex) {
            case 0: //Leather Bodyarmor
                icon = new Sprite(16, 16, 1, 1, SpriteSheet.ARMORICONSET);
                name = "Leather BA";
                
                resilience = 5;
                encumberance = 2;
        }
    }*/
    
    public Armor(int typeindex) {
        ArmorData data = Game.getArmorData(typeindex);
        name = data.getName();
        resilience = data.getResilience();
        encumberance = data.getEncumberance();
        price = data.getPrice();
        
        icon = new Sprite(data.getSpriteWidth(), data.getSpriteHeight(), data.
                getX(), data.getY(), Game.getSpriteSheet(data.getSheetIndex()));
    }

    public Sprite getIcon() {
        return icon;
    }

    public void setIcon(Sprite icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResilience() {
        return resilience;
    }

    public void setResilience(int resilience) {
        this.resilience = resilience;
    }

    public int getEncumberance() {
        return encumberance;
    }

    public void setEncumberance(int encumberance) {
        this.encumberance = encumberance;
    }
}
