package fer.util;

import java.net.URL;

/**
 * @author Evan Stewart
 */
public class UnitClassData {

    private int index;
    private String name;
    private boolean female;
    private int hp, str, skl, spd, def, res, mov, con;
    //private int sword, axe, lance, bow, anima, light, dark, staff;
    private int[] movetype;
    
    private String sheetPath;
    private int sheetWidth, sheetHeight, sheetSpacing, sheetTransparentColor;
    private int[] spriteWidths, spriteHeights, numFrames, keyFrames;
    private int[][] frameDurations;
    
    //To be used during game initialization, not set through XML
    private int sheetIndex;

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

    public boolean isFemale() {
        return female;
    }

    public void setFemale(boolean female) {
        this.female = female;
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

    public int[] getMovetype() {
        return movetype;
    }

    public void setMovetype(int[] movetype) {
        this.movetype = movetype;
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

    public int getSheetSpacing() {
        return sheetSpacing;
    }

    public void setSheetSpacing(int sheetSpacing) {
        this.sheetSpacing = sheetSpacing;
    }

    public int getSheetTransparentColor() {
        return sheetTransparentColor;
    }

    public void setSheetTransparentColor(int sheetTransparentColor) {
        this.sheetTransparentColor = sheetTransparentColor;
    }

    public int[] getSpriteWidths() {
        return spriteWidths;
    }

    public void setSpriteWidths(int[] spriteWidths) {
        this.spriteWidths = spriteWidths;
    }

    public int[] getSpriteHeights() {
        return spriteHeights;
    }

    public void setSpriteHeights(int[] spriteHeights) {
        this.spriteHeights = spriteHeights;
    }

    public int[] getNumFrames() {
        return numFrames;
    }

    public void setNumFrames(int[] numFrames) {
        this.numFrames = numFrames;
    }

    public int[] getKeyFrames() {
        return keyFrames;
    }

    public void setKeyFrames(int[] keyFrames) {
        this.keyFrames = keyFrames;
    }

    public int[][] getFrameDurations() {
        return frameDurations;
    }

    public void setFrameDurations(int[][] frameDurations) {
        this.frameDurations = frameDurations;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }
}