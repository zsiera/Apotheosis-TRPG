package fer.util;

/**
 * @author Evan Stewart
 */
public class SettingsData {
    
    private int gameScale, gridOpacity;
    private boolean factionShadows;

    public int getGameScale() {
        return gameScale;
    }

    public void setGameScale(int gameScale) {
        this.gameScale = gameScale;
    }

    public int getGridOpacity() {
        return gridOpacity;
    }

    public void setGridOpacity(int gridOpacity) {
        this.gridOpacity = gridOpacity;
    }

    public boolean isFactionShadows() {
        return factionShadows;
    }

    public void setFactionShadows(boolean factionShadows) {
        this.factionShadows = factionShadows;
    }
}
