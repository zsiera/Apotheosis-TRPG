package fer.gameplay;

import fer.Game;
import fer.graphics.Sprite;
import fer.ui.Font;
import fer.ui.Menu;
import fer.ui.MenuAction;
import fer.ui.MenuCursor;
import fer.ui.MenuElement;
import fer.ui.TextGraphic;
import fer.util.WeaponData;

/**
 * @author Evan
 */
public class Weapon {

    private Sprite icon;
    private String name, description;
    private boolean melee;
    private int range, damage, pierce, critical, accuracy, weight, uses, maxUses,
            price;
    //For ranged weapons, weight is replaced (in the UI) with rate of fire.
    private Menu infoMenu;

    /*public Weapon(int typeindex) {
     switch (typeindex) {
     case 0: //ACR M01
     name = "ACR M01";
     icon = new Sprite(16, 16, 1, 1, SpriteSheet.WEAPONICONSET);
                
     melee = false;
     range = 3;
     damage = 10;
     pierce = 2;
     critical = 0;
     accuracy = 90;
     weight = 3;
                
     maxUses = 45;
     uses = maxUses;
     }
     }*/
    public Weapon(int typeindex) {
        WeaponData data = Game.getWeaponData(typeindex);
        name = data.getName();
        description = data.getDescription();
        melee = data.getMelee();
        range = data.getRange();
        damage = data.getDamage();
        pierce = data.getPierce();
        critical = data.getCritical();
        accuracy = data.getAccuracy();
        weight = data.getWeight();
        maxUses = data.getUses();
        uses = maxUses;
        price = data.getPrice();

        icon = new Sprite(data.getSpriteWidth(), data.getSpriteHeight(),
                data.getX(), data.getY(), Game.getSpriteSheet(data.
                getSheetIndex()));

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

    public boolean isMelee() {
        return melee;
    }

    public void setMelee(boolean melee) {
        this.melee = melee;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getPierce() {
        return pierce;
    }

    public void setPierce(int pierce) {
        this.pierce = pierce;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public void drawInfoMenu(int x, int y, Menu callingMenu, boolean active) {
        infoMenu = new Menu(200, 56, x, y);

        MenuAction nil = new MenuAction() {
            @Override
            public void execute(MenuElement caller) { //Do nothing
            }
        };
        
        infoMenu.addElement(new MenuElement(nil, nil, icon, false, 7, 7));
        infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic(name, Font.BASICFONT).getSprite(), false, 23, 7)); 
        String[] lines = Menu.wrapText(description, 34);
        for (int i = 0; i < Math.min(3, lines.length); i++) {
            infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic(lines[i], Font.BASICFONT).getSprite(), false, 23, 13 + (6* i)));
        }
        infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("RNG: " + range, Font.BASICFONT).getSprite(), false, 7, 31));
        infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("DMG: " + damage, Font.BASICFONT).getSprite(), false, 47, 31));
        infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("PRC: " + pierce, Font.BASICFONT).getSprite(), false, 87, 31));
        infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("CRT: " + critical, Font.BASICFONT).getSprite(), false, 127, 31));
        infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("ACC: " + accuracy, Font.BASICFONT).getSprite(), false, 7, 37));
        infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("WGT: " + weight, Font.BASICFONT).getSprite(), false, 47, 37));
        infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("VALUE: " + price, Font.BASICFONT).getSprite(), false, 87, 37));
        infoMenu.addElement(new MenuElement(nil, nil, new TextGraphic("USES: " + uses + "/" + maxUses, Font.BASICFONT).getSprite(), false, 7, 43));
        
        
        if (active) {
            MenuCursor.getMenuCursor().setElementIndex(0);
            MenuCursor.setActiveMenu(infoMenu);
        }
    }
    
    public boolean infoMenuDrawn() {
        return infoMenu != null;
    }
    
    public void clearInfoMenu() {
        infoMenu.removeMenu();
        infoMenu = null;
    }
}
