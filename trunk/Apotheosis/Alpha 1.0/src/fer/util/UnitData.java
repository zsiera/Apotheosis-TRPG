package fer.util;

/**
 * @author Evan Stewart
 */
public class UnitData {
    
    private int index;
    private String name;
    private int unitClass;
    private int level, exp;
    private boolean autoLevel;
    private int hp, str, skl, spd, def, res, mov, con;
    private int[] weapons;
    private int[] items;
    private int armor;
    
    private int mapx;
    private int mapy;
    private int faction;
    
    private boolean hasFaceSprite;
    private String sheetPath;
    private int sheetWidth, sheetHeight, sheetTransparentColor;
    private int x, y, spriteWidth, spriteHeight;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnitClass() {
        return unitClass;
    }

    public void setUnitClass(int unitClass) {
        this.unitClass = unitClass;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public boolean isAutoLevel() {
        return autoLevel;
    }

    public void setAutoLevel(boolean autoLevel) {
        this.autoLevel = autoLevel;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getSkl() {
        return skl;
    }

    public void setSkl(int skl) {
        this.skl = skl;
    }

    public int getSpd() {
        return spd;
    }

    public void setSpd(int spd) {
        this.spd = spd;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getMov() {
        return mov;
    }

    public void setMov(int mov) {
        this.mov = mov;
    }

    public int getCon() {
        return con;
    }

    public void setCon(int con) {
        this.con = con;
    }

    public int[] getWeapons() {
        return weapons;
    }

    public void setWeapons(int[] weapons) {
        this.weapons = weapons;
    }

    public int[] getItems() {
        return items;
    }

    public void setItems(int[] items) {
        this.items = items;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getMapx() {
        return mapx;
    }

    public void setMapx(int mapx) {
        this.mapx = mapx;
    }

    public int getMapy() {
        return mapy;
    }

    public void setMapy(int mapy) {
        this.mapy = mapy;
    }

    public int getFaction() {
        return faction;
    }

    public void setFaction(int faction) {
        this.faction = faction;
    }

    public boolean isHasFaceSprite() {
        return hasFaceSprite;
    }

    public void setHasFaceSprite(boolean hasFaceSprite) {
        this.hasFaceSprite = hasFaceSprite;
    }

    public String getSheetPath() {
        return sheetPath;
    }

    public void setSheetPath(String sheetPath) {
        this.sheetPath = sheetPath;
    }

    public int getSheetWidth() {
        return sheetWidth;
    }

    public void setSheetWidth(int sheetWidth) {
        this.sheetWidth = sheetWidth;
    }

    public int getSheetHeight() {
        return sheetHeight;
    }

    public void setSheetHeight(int sheetHeight) {
        this.sheetHeight = sheetHeight;
    }

    public int getSheetTransparentColor() {
        return sheetTransparentColor;
    }

    public void setSheetTransparentColor(int sheetTransparentColor) {
        this.sheetTransparentColor = sheetTransparentColor;
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

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public void setSpriteWidth(int spriteWidth) {
        this.spriteWidth = spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public void setSpriteHeight(int spriteHeight) {
        this.spriteHeight = spriteHeight;
    } 
}
