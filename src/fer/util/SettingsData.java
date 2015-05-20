/*
 * 
 */
package fer.util;

// TODO: Auto-generated Javadoc
/**
 * The Class SettingsData.
 *
 * @author Evan Stewart
 */
public class SettingsData {

	/** The grid opacity. */
	private int gameScale, gridOpacity;
	
	/** The faction shadows. */
	private boolean factionShadows;

	/**
	 * Gets the game scale.
	 *
	 * @return the game scale
	 */
	public int getGameScale() {
		return gameScale;
	}

	/**
	 * Sets the game scale.
	 *
	 * @param gameScale the new game scale
	 */
	public void setGameScale(int gameScale) {
		this.gameScale = gameScale;
	}

	/**
	 * Gets the grid opacity.
	 *
	 * @return the grid opacity
	 */
	public int getGridOpacity() {
		return gridOpacity;
	}

	/**
	 * Sets the grid opacity.
	 *
	 * @param gridOpacity the new grid opacity
	 */
	public void setGridOpacity(int gridOpacity) {
		this.gridOpacity = gridOpacity;
	}

	/**
	 * Checks if is faction shadows.
	 *
	 * @return true, if is faction shadows
	 */
	public boolean isFactionShadows() {
		return factionShadows;
	}

	/**
	 * Sets the faction shadows.
	 *
	 * @param factionShadows the new faction shadows
	 */
	public void setFactionShadows(boolean factionShadows) {
		this.factionShadows = factionShadows;
	}
}
