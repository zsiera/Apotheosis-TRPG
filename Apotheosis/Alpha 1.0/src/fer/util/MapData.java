package fer.util;

/**
 * @author Evan Stewart
 */
public class MapData {
    
    private int index;
    private String name;
    private int width, height, numUnits, numFactions;
    private int[] tiles;

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

    public int getNumUnits() {
        return numUnits;
    }

    public void setNumUnits(int numUnits) {
        this.numUnits = numUnits;
    }

    public int getNumFactions() {
        return numFactions;
    }

    public void setNumFactions(int numFactions) {
        this.numFactions = numFactions;
    }

    public int[] getTiles() {
        return tiles;
    }

    public void setTiles(int[] tiles) {
        this.tiles = tiles;
    }
}
