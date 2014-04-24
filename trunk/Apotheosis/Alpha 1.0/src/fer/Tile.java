package fer;

import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;
import fer.util.TileData;

/**
 * @author Evan Stewart
 *
 * Primarily a storage class, each tile is assigned a type index directing it to
 * it's properties within the corresponding XML document, which it can then pass
 * on to other parts of the program.
 */
public class Tile {

    public static final int TILE_WIDTH = 16;
    public static final int TILE_HEIGHT = 16;
    public static final int IMPASSIBLE = 60;
    private int typeIndex, terrainType, color = 0;
    private int mapx, mapy, def, avo;
    private boolean attackable;
    private Sprite sprite;
    private String name;

    /*public Tile(int type) {
     typeIndex = type;
     sprite = Sprite.getTileSprite(typeIndex);
     terrainType = getTileTerrainType(type);
     name = getTileName(type);
     attackable = getTileAttackability(typeIndex);
     }
    
     public Tile(int type, int mapx, int mapy) {
     this(type);
     this.mapx = mapx;
     this.mapy = mapy;
     }*/
    public Tile(int typeindex) {
        typeIndex = typeindex;
        TileData data = Game.getTileData(typeindex);
        name = data.getName();
        terrainType = data.getTerrainType();
        attackable = data.isAttackable();
        def = data.getDef();
        avo = data.getAvo();

        sprite = new Sprite(data.getSpriteWidth(), data.getSpriteHeight(),
                data.getX(), data.getY(), Game.getSpriteSheet(data.
                getSheetIndex()));
    }

    public Tile(int typeindex, int mapx, int mapy) {
        this(typeindex);
        this.mapx = mapx;
        this.mapy = mapy;
    }

    public void setColor(int newColor) {
        color = newColor;
    }

    public int getColor() {
        return color;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public static int getTileTerrainType(int typeIndex) {
        switch (typeIndex) {
            case 0:
                return 0;
            case 1:
                return 0;
            case 2:
                return 0;
            case 3:
                return 0;
            case 4:
                return 0;
            case 5:
                return 1;
            default:
                return 0;
        }
    }

    public static boolean getTileAttackability(int typeIndex) {
        switch (typeIndex) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
            case 4:
                return true;
            case 5:
                return true;
            default:
                return true;
        }
    }

    public static String getTileName(int typeIndex) {
        switch (typeIndex) {
            case 0:
                return "GRASSLAND";
            case 1:
                return "PLAINS";
            case 2:
                return "DIRT";
            case 3:
                return "GRANITE";
            case 4:
                return "GRAVEL";
            case 5:
                return "HOUSE";
            default:
                return "TERRAIN";
        }
    }

    public int getMapX() {
        return mapx;
    }

    public int getMapY() {
        return mapy;
    }

    public int getTerrain() {
        return terrainType;
    }

    public String getName() {
        return name;
    }

    public boolean isAttackable() {
        return attackable;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getAvo() {
        return avo;
    }

    public void setAvo(int avo) {
        this.avo = avo;
    }

    public void setAttackable(boolean attackable) {
        this.attackable = attackable;
    }

    public int getTypeIndex() {
        return typeIndex;
    }
}
