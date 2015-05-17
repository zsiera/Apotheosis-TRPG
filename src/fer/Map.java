package fer;

import fer.ai.AiPlayer;
import fer.gameplay.MapGoal;
import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;
import fer.util.GoalData;
import fer.util.MapData;
import fer.util.UnitData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Evan
 *
 *         Stores the array of tiles that make up a map, along with properties
 *         and methods related to interfacing said map. Maps cannot be smaller
 *         than the amount of tiles required to fill the screen (15 by 10
 *         tiles).
 */
public class Map {

	public static final int MIN_MAP_WIDTH = 15;
	public static final int MIN_MAP_HEIGHT = 10;
	public static final int[] FACTION_COLORS = new int[] { 0xff0000, 0x0000ff,
			0x00ff00, 0xffff00, 0x00ffff, 0xff00ff, 0xffffff, 0x000000 };
	public static final Sprite FACTION_SHADOW = new Sprite(16, 16, 1, 1,
			SpriteSheet.FACTIONSHADOW);
	private Cursor cursor;
	private int width, height;
	private Tile[] tiles;
	private Unit[] units;
	private Comparator displayCompare = new Comparator() {
		@Override
		public int compare(Object o1, Object o2) {
			Unit u1 = (Unit) o1;
			Unit u2 = (Unit) o2;
			if (u1.getMapy() > u2.getMapy()) {
				return 1;
			} else if (u1.getMapy() < u2.getMapy()) {
				return -1;
			} else {
				if (u1.getMapx() > u2.getMapx()) {
					return 1;
				} else if (u1.getMapx() < u2.getMapx()) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	};
	private PriorityQueue displayUnitsQueue;
	private CopyOnWriteArrayList<Unit> displayUnits;
	private int numFactions;
	private MapGoal[] factionGoals;
	private AiPlayer[] players;
	private String name;
	private int currentTurn = 0;

	public Map(int mapWidth, int mapHeight, String mapName, int[] tileTypes,
			Unit[] units, MapGoal[] goals) {
		cursor = Cursor.getCursor();

		if (mapWidth < MIN_MAP_WIDTH) {
			width = MIN_MAP_WIDTH;
		} else {
			width = mapWidth;
		}
		if (mapHeight < MIN_MAP_HEIGHT) {
			height = MIN_MAP_HEIGHT;
		} else {
			height = mapHeight;
		}
		name = mapName;
		tiles = new Tile[width * height];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new Tile(0);
		}
		for (int i = 0; i < tileTypes.length; i++) {
			try {
				tiles[i] = new Tile(tileTypes[i], i % width, i / width);
			} catch (ArrayIndexOutOfBoundsException err) {
				break;
			}
		}
		// Default an uninitialized tiles to 0.
		if (tiles.length > tileTypes.length) {
			for (int i = tileTypes.length; i < tiles.length; i++) {
				tiles[i] = new Tile(0, i % width, i / width);
			}
		}
		this.units = units;
		factionGoals = goals;
		ArrayList<Integer> factions = new ArrayList<>();
		numFactions = 0;
		for (int i = 0; i < units.length; i++) {
			if (!factions.contains(units[i].getFaction())) {
				numFactions++;
				factions.add(i);
			}
		}
	}

	public Map(MapData data) {
		cursor = Cursor.getCursor();
		name = data.getName();
		width = data.getWidth();
		height = data.getHeight();
		tiles = new Tile[data.getTiles().length];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new Tile(data.getTiles()[i], i % width, i / width);
		}
		units = new Unit[data.getNumUnits()];
		numFactions = data.getNumFactions();
		players = new AiPlayer[numFactions];
	}

	/**
	 * To be run after a map is selected in the menu, this method pulls all of
	 * the relevant data from the map's corresponding GoalData and UnitData
	 * messenger classes in order to initialize the map's faction map goals and
	 * units.
	 *
	 * @param goalData
	 *            The array of GoalData messenger objects for the map
	 * @param unitData
	 *            The array of UnitData messenger objects for the map
	 */
	public void init(GoalData[] goalData, UnitData[] unitData) {
		factionGoals = new MapGoal[goalData.length];
		for (int i = 0; i < factionGoals.length; i++) {
			factionGoals[i] = new MapGoal(goalData[i]);
		}
		units = new Unit[unitData.length];
		for (int i = 0; i < units.length; i++) {
			units[i] = new Unit(unitData[i]);
		}
		displayUnits = new CopyOnWriteArrayList<>();
		updateUnitDisplay();
	}

	/**
	 * A method to be run after every in game action that cycles through each
	 * faction's goal to see if it is met. If a faction has met their goal, the
	 * index of said faction is returned. If no faction has met their goal, -1
	 * is returned.
	 *
	 * @return The index of the first faction to complete their goal
	 */
	public int checkVictory() {
		for (int i = 0; i < numFactions; i++) {
			if (factionGoals[i].isMet(this)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Sets all units on the map to a moveable state
	 */
	public void resetUnitMovement() {
		for (int i = 0; i < units.length; i++) {
			units[i].setMoved(false);
		}
	}

	public Tile getTile(int index) {
		return tiles[index];
	}

	public Tile getSelectedTile() {
		return tiles[cursor.getMapX() + cursor.getMapY() * width];
	}

	public int getNumTiles() {
		return tiles.length;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Unit getUnit(int index) {
		return units[index];
	}

	public Unit getUnitTile(int tileIndex) {
		int x = tileIndex % width;
		int y = tileIndex / width;
		for (int i = 0; i < units.length; i++) {
			if (units[i].getMapx() == x) {
				if (units[i].getMapy() == y) {
					if (!units[i].isDead()) {
						return units[i];
					}
				}
			}
		}
		return null;
	}

	public Unit getSelectedUnit() {
		return getUnitTile(cursor.getMapX() + cursor.getMapY() * width);
	}

	public int getNumUnits() {
		return units.length;
	}

	public Unit[] getUnits() {
		return units;
	}

	public void updateUnitAnimations() {
		for (int i = 0; i < units.length; i++) {
			units[i].updateAnimation();
		}
	}

	public Sprite getMapSprite(int width, int height) {
		Sprite mapSprite = new Sprite(width, height, 0, 0);
		int tilePixelsX = (int) Math.floor(width / this.width);
		int tilePixelsY = (int) Math.floor(height / this.height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (((int) Math.floor(x / tilePixelsX)) < this.width
						&& ((int) Math.floor(y / tilePixelsY)) < this.height) {
					mapSprite
							.setPixel(
									x + y * width,
									tiles[((int) Math.floor(x / tilePixelsX))
											+ ((int) Math
													.floor(y / tilePixelsY))
											* this.width]
											.getSprite()
											.getPixel(
													((16 / tilePixelsX) * (x % tilePixelsX))
															+ ((16 / tilePixelsY) * (y % tilePixelsY))
															* Tile.TILE_WIDTH));
				}
			}
		}
		return mapSprite;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getTileTypes() {
		int[] tileTypes = new int[tiles.length];
		for (int i = 0; i < tileTypes.length; i++) {
			tileTypes[i] = tiles[i].getTypeIndex();
		}
		return tileTypes;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public int getNumFactions() {
		return numFactions;
	}

	public void setNumFactions(int numFactions) {
		this.numFactions = numFactions;
	}

	public MapGoal[] getFactionGoals() {
		return factionGoals;
	}

	public void setFactionGoals(MapGoal[] factionGoals) {
		this.factionGoals = factionGoals;
	}

	public AiPlayer getPlayer(int index) {
		return players[index];
	}

	public void setPlayer(int index, AiPlayer player) {
		players[index] = player;
	}

	public void updateUnitDisplay() {
		displayUnitsQueue = new PriorityQueue(units.length, displayCompare);
		for (int i = 0; i < units.length; i++) {
			displayUnitsQueue.add(units[i]);
		}
		System.out.println(displayUnitsQueue.size());
		displayUnits.removeAll(displayUnits);
		int size = displayUnitsQueue.size();
		for (int i = 0; i < size; i++) {
			displayUnits.add((Unit) displayUnitsQueue.poll());
		}
		System.out.println(displayUnits.size());
	}

	public CopyOnWriteArrayList<Unit> getUnitDisplay() {
		return displayUnits;
	}
}
