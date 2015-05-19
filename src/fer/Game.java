/*
 * 
 */
package fer;

import fer.ai.AiPlayer;
import fer.gameplay.BattleProcessor;
import fer.graphics.Animation;
import fer.graphics.Effect;
import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;
import fer.ui.Font;
import fer.ui.Menu;
import fer.ui.MenuAction;
import fer.ui.MenuCursor;
import fer.ui.MenuElement;
import fer.ui.TextGraphic;
import fer.util.ArmorData;
import fer.util.GoalData;
import fer.util.ItemData;
import fer.util.MapData;
import fer.util.SettingsData;
import fer.util.TileData;
import fer.util.UnitClassData;
import fer.util.UnitData;
import fer.util.WeaponData;
import fer.util.XMLReader;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

// TODO: Auto-generated Javadoc
/**
 * The Class Game.
 *
 * @author Evan Stewart
 * 
 *         The main class for the game sequence. Contains the window and
 *         functions as the canvas upon which to draw graphics. Also handles the
 *         threading, looping, and running, of the game sequence, orchestrating
 *         tasks such as rendering game graphics and updating game logic.
 */
public class Game extends Canvas implements Runnable {

	/** The instance. */
	private static Game instance;
	
	/** The Constant GAME_WIDTH. */
	public static final int GAME_WIDTH = 240;
	
	/** The Constant GAME_HEIGHT. */
	public static final int GAME_HEIGHT = 160;
	
	/** The game scale. */
	public static int GAME_SCALE;
	
	/** The Constant GAME_UPS. */
	public static final int GAME_UPS = 60;
	
	/** The game res. */
	public static Dimension GAME_RES;
	
	/** The Constant GAME_TITLE. */
	public static final String GAME_TITLE = "Apotheosis";
	
	/** The weapons. */
	private static WeaponData[] weapons;
	
	/** The items. */
	private static ItemData[] items;
	
	/** The armor. */
	private static ArmorData[] armor;
	
	/** The tiles. */
	private static TileData[] tiles;
	
	/** The unit classes. */
	private static UnitClassData[] unitClasses;
	
	/** The freeplay map data. */
	private static MapData[] freeplayMapData;
	
	/** The sprite sheets. */
	private static SpriteSheet[] spriteSheets;
	
	/** The current map. */
	private static Map currentMap;
	
	/** The menu list. */
	private static CopyOnWriteArrayList<Menu> menuList;
	
	/** The effect list. */
	private static CopyOnWriteArrayList<Effect> effectList;
	
	/** The battle processor. */
	private static BattleProcessor battleProcessor;
	
	/** The title menu. */
	private static Menu titleMenu;
	
	/** The main menu. */
	private static Menu mainMenu;
	
	/** The free play menu. */
	private static Menu freePlayMenu;
	
	/** The map select menu. */
	private static Menu mapSelectMenu;
	
	/** The map info menu. */
	private static Menu mapInfoMenu;
	
	/** The player num menu. */
	private static Menu playerNumMenu;
	
	/** The player faction menu. */
	private static Menu playerFactionMenu;
	
	/** The settings menu. */
	private static Menu settingsMenu;
	
	/** The notice menu. */
	private static Menu noticeMenu;
	
	/** The current player. */
	private static int currentPlayer;
	
	/** The current faction. */
	private static int currentFaction;
	
	/** The num players. */
	private static int numPlayers;
	
	/** The hotseat. */
	private static boolean hotseat = false;
	
	/** The selected factions. */
	private static boolean[] selectedFactions;
	
	/** The player factions. */
	private static int[] playerFactions;
	
	/** The main menu overlay. */
	private static Effect mainMenuOverlay;
	
	/** The title background. */
	private static Sprite titleBackground;
	
	/** The main menu background. */
	private static Sprite mainMenuBackground;
	
	/** The on title. */
	private static boolean onTitle = true;
	
	/** The on main menu. */
	private static boolean onMainMenu = false;
	
	/** The map select position. */
	private static int mapSelectPosition = 0;
	
	/** The game running. */
	private boolean gameRunning = false;
	
	/** The updates. */
	private int updates = 0;
	
	/** The logic thread. */
	private Thread logicThread;
	
	/** The graphics thread. */
	private Thread graphicsThread;
	
	/** The control thread. */
	private Thread controlThread;
	
	/** The last enter. */
	private boolean lastEnter = false;
	
	/** The last escape. */
	private boolean lastEscape = false;
	
	/** The renderer. */
	private Renderer renderer;
	
	/** The game window. */
	private JFrame gameWindow;
	
	/** The keyboard. */
	private Keyboard keyboard;
	
	/** The cursor. */
	private static Cursor cursor;
	
	/** The menu cursor. */
	private static MenuCursor menuCursor;
	
	/** The free play maps. */
	private static Map[] freePlayMaps;
	// Global game settings
	/** The game scale. */
	private static int gameScale;
	
	/** The draw faction shadow. */
	private static boolean drawFactionShadow;
	
	/** The grid opacity. */
	private static int gridOpacity;
	// Setting defaults
	/** The Constant DRAWFACTIONSHADOWDEFAULT. */
	private static final boolean DRAWFACTIONSHADOWDEFAULT = true;
	
	/** The Constant GRIDOPACITYDEFAULT. */
	private static final int GRIDOPACITYDEFAULT = 26;

	/**
	 * Instantiates a new game.
	 */
	public Game() {
		loadSettings();
		GAME_SCALE = gameScale;
		GAME_RES = new Dimension((GAME_WIDTH * GAME_SCALE),
				(GAME_HEIGHT * GAME_SCALE));
		setPreferredSize(GAME_RES);
		setBackground(Color.black);
		setFocusTraversalKeysEnabled(false);

		gameWindow = new JFrame(GAME_TITLE);
		keyboard = new Keyboard();
		addKeyListener(keyboard);
		menuList = new CopyOnWriteArrayList<>();
		effectList = new CopyOnWriteArrayList<>();
		battleProcessor = new BattleProcessor();
		titleBackground = new Sprite(240, 160, 0, 0, SpriteSheet.MENUBACK);
		mainMenuBackground = new Sprite(240, 160, 0, 0, SpriteSheet.MENUBACK);
		loadData();
		currentMap = null;
		cursor = Cursor.getCursor();
		menuCursor = MenuCursor.getMenuCursor();
	}

	/**
	 * The main method.
	 *
	 * @param args            the command line arguments
	 */
	public static void main(final String[] args) {
		createInstance();

		instance.startSequence();
	}

	/**
	 * Creates the instance.
	 */
	private static void createInstance() {
		instance = new Game();

		instance.gameWindow.setResizable(false);
		instance.gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		instance.gameWindow.add(instance);
		instance.gameWindow.pack();
		instance.gameWindow.setLocationRelativeTo(null);
		instance.gameWindow.setVisible(true);
	}

	/**
	 * Gets the game.
	 *
	 * @return the game
	 */
	public static Game getGame() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	/**
	 * Gets the game running.
	 *
	 * @return the game running
	 */
	public final boolean getGameRunning() {
		return gameRunning;
	}

	/**
	 * Sets the game running.
	 *
	 * @param running the new game running
	 */
	public final void setGameRunning(final boolean running) {
		gameRunning = running;
	}

	/**
	 * Gets the game window title.
	 *
	 * @return the game window title
	 */
	public final String getGameWindowTitle() {
		return gameWindow.getTitle();
	}

	/**
	 * Sets the game window title.
	 *
	 * @param title the new game window title
	 */
	public final void setGameWindowTitle(final String title) {
		gameWindow.setTitle(title);
	}

	/**
	 * Load data.
	 */
	public final void loadData() {
		// Read all data messenger objects from XML
		XMLReader reader = new XMLReader();
		weapons = reader.serializeAllWeaponData().toArray(new WeaponData[1]);
		items = reader.serializeAllItemData().toArray(new ItemData[1]);
		armor = reader.serializeAllArmorData().toArray(new ArmorData[1]);
		tiles = reader.serializeAllTileData().toArray(new TileData[1]);
		unitClasses = reader.serializeAllUnitClassData().toArray(
				new UnitClassData[1]);
		freeplayMapData = new MapData[reader.getNumFreeplayMaps()];
		for (int i = 0; i < freeplayMapData.length; i++) {
			freeplayMapData[i] = reader.serializeMapData(0, i);
		}
		// Load and store all spritesheets and corresponding indices
		ArrayList<String> sheetpaths = new ArrayList<>();
		ArrayList<Integer> widths = new ArrayList<>();
		ArrayList<Integer> heights = new ArrayList<>();
		ArrayList<Integer> colors = new ArrayList<>();
		for (int i = 0; i < weapons.length; i++) {
			if (!sheetpaths.contains(weapons[i].getSheetPath())) {
				sheetpaths.add(weapons[i].getSheetPath());
				widths.add(weapons[i].getSheetWidth());
				heights.add(weapons[i].getSheetHeight());
				colors.add(weapons[i].getSheetTransparentColor());
			}
			weapons[i].setSheetIndex(sheetpaths.indexOf(weapons[i]
					.getSheetPath()));
		}
		for (int i = 0; i < items.length; i++) {
			if (!sheetpaths.contains(items[i].getSheetPath())) {
				sheetpaths.add(items[i].getSheetPath());
				widths.add(items[i].getSheetWidth());
				heights.add(items[i].getSheetHeight());
				colors.add(items[i].getSheetTransparentColor());
			}
			items[i].setSheetIndex(sheetpaths.indexOf(items[i].getSheetPath()));
		}
		for (int i = 0; i < armor.length; i++) {
			if (!sheetpaths.contains(armor[i].getSheetPath())) {
				sheetpaths.add(armor[i].getSheetPath());
				widths.add(armor[i].getSheetWidth());
				heights.add(armor[i].getSheetHeight());
				colors.add(armor[i].getSheetTransparentColor());
			}
			armor[i].setSheetIndex(sheetpaths.indexOf(armor[i].getSheetPath()));
		}
		for (int i = 0; i < tiles.length; i++) {
			if (!sheetpaths.contains(tiles[i].getSheetPath())) {
				sheetpaths.add(tiles[i].getSheetPath());
				widths.add(tiles[i].getSheetWidth());
				heights.add(tiles[i].getSheetHeight());
				colors.add(tiles[i].getSheetTransparentColor());
			}
			tiles[i].setSheetIndex(sheetpaths.indexOf(tiles[i].getSheetPath()));
		}
		for (int i = 0; i < unitClasses.length; i++) {
			if (!sheetpaths.contains(unitClasses[i].getSheetPath())) {
				sheetpaths.add(unitClasses[i].getSheetPath());
				widths.add(unitClasses[i].getSheetWidth());
				heights.add(unitClasses[i].getSheetHeight());
				colors.add(unitClasses[i].getSheetTransparentColor());
			}
			unitClasses[i].setSheetIndex(sheetpaths.indexOf(unitClasses[i]
					.getSheetPath()));
		}
		spriteSheets = new SpriteSheet[sheetpaths.size()];
		for (int i = 0; i < spriteSheets.length; i++) {
			spriteSheets[i] = new SpriteSheet(sheetpaths.get(i), widths.get(i),
					heights.get(i), colors.get(i));
		}
	}

	/**
	 * Load settings.
	 */
	public final void loadSettings() {
		XMLReader reader = new XMLReader();
		SettingsData data = reader.serializeSettingsData();
		gameScale = data.getGameScale();
		drawFactionShadow = data.isFactionShadows();
		gridOpacity = data.getGridOpacity();
	}

	/**
	 * Start sequence.
	 */
	public final synchronized void startSequence() {
		// loadData();
		gameRunning = true;

		renderer = new Renderer();
		logicThread = new Thread(this, "Logic Thread");
		graphicsThread = new Thread(renderer, "Graphics Thread");
		controlThread = new Thread(keyboard, "Control Thread");
		drawTitleMenu();

		logicThread.start();
		graphicsThread.start();
		controlThread.start();
	}

	/**
	 * Stop sequence.
	 */
	public final synchronized void stopSequence() {
		gameRunning = false;
		try {
			// logicThread.join();
			graphicsThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gameWindow.dispose();
	}

	/**
	 * Update.
	 */
	public final void update() {
		int xQueue = 0, yQueue = 0;
		boolean enter, escape;
		// keyboard.update();
		if (updates % 5 == 0) {
			if (keyboard.isUp()) {
				yQueue--;
			}
			if (keyboard.isDown()) {
				yQueue++;
			}
			if (keyboard.isLeft()) {
				xQueue--;
			}
			if (keyboard.isRight()) {
				xQueue++;
			}
			enter = keyboard.isEnter() && !lastEnter;
			escape = keyboard.isEscape() && !lastEscape;
			if (cursor.isActive() && currentMap != null) {
				cursor.update(currentMap, xQueue, yQueue, enter, escape,
						keyboard.isTab());
			} else if (menuCursor.isActive()) {
				menuCursor.update(xQueue, yQueue, enter, escape);
			}
			lastEnter = keyboard.isEnter();
			lastEscape = keyboard.isEscape();
		}
		if (currentMap != null) {
			currentMap.updateUnitAnimations();
			if (battleProcessor.isInCombat()) {
				battleProcessor.update();
			}
		}
		for (int i = 0; i < effectList.size(); i++) {
			if (effectList.get(i).isAnimated()) {
				effectList.get(i).updateAnimation();
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public final void run() {
		long previousTime = System.nanoTime();
		long currentTime;
		long timer = System.currentTimeMillis();
		double iterationLength = 1000000000 / GAME_UPS;
		double timeElapsed = 0;
		requestFocus();
		while (gameRunning) {
			currentTime = System.nanoTime();
			timeElapsed += (currentTime - previousTime) / iterationLength;
			previousTime = currentTime;
			while (timeElapsed >= 1) {
				update();
				updates++;
				timeElapsed--;
			}
			if ((System.currentTimeMillis() - timer) > 1000) {
				timer += 1000;
				System.out.println(updates + " UPS");
				updates = 0;
			}
			// System.out.println("Logic thread updated.");
			try {
				Thread.sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		}
	}

	/**
	 * Prepare freeplay.
	 */
	public static void prepareFreeplay() {
		freePlayMaps = new Map[freeplayMapData.length];
		for (int i = 0; i < freePlayMaps.length; i++) {
			freePlayMaps[i] = new Map(freeplayMapData[i]);
		}
	}

	/**
	 * Prepare map.
	 *
	 * @param map the map
	 * @param index the index
	 * @param selectedFactions the selected factions
	 */
	public static void prepareMap(final Map map, final int index, final boolean[] selectedFactions) {
		// Read corresponding map XML data
		XMLReader reader = new XMLReader();
		GoalData[] goals = reader.serializeAllGoalData(0, index).toArray(
				new GoalData[1]);
		UnitData[] units = reader.serializeAllUnitData(0, index).toArray(
				new UnitData[1]);
		for (int i = 0; i < selectedFactions.length; i++) {
			if (!selectedFactions[i]) {
				map.setPlayer(i, new AiPlayer(i));
			}
		}
		// Initialize the map
		map.init(goals, units);
		// Clear the temporary array of maps
		freePlayMaps = null;
		// Set the current map
		cursor.resetCursor();
		currentMap = map;
	}

	/**
	 * Gets the current map.
	 *
	 * @return the current map
	 */
	public static Map getCurrentMap() {
		return currentMap;
	}

	/**
	 * Sets the current map.
	 *
	 * @param newMap the new current map
	 */
	public static void setCurrentMap(final Map newMap) {
		currentMap = newMap;
	}

	/**
	 * Gets the menu list.
	 *
	 * @return the menu list
	 */
	public static CopyOnWriteArrayList<Menu> getMenuList() {
		return menuList;
	}

	/**
	 * Gets the effect list.
	 *
	 * @return the effect list
	 */
	public static CopyOnWriteArrayList<Effect> getEffectList() {
		return effectList;
	}

	/**
	 * Gets the weapon data.
	 *
	 * @param index the index
	 * @return the weapon data
	 */
	public static WeaponData getWeaponData(final int index) {
		return weapons[index];
	}

	/**
	 * Gets the item data.
	 *
	 * @param index the index
	 * @return the item data
	 */
	public static ItemData getItemData(final int index) {
		return items[index];
	}

	/**
	 * Gets the armor data.
	 *
	 * @param index the index
	 * @return the armor data
	 */
	public static ArmorData getArmorData(final int index) {
		return armor[index];
	}

	/**
	 * Gets the tile data.
	 *
	 * @param index the index
	 * @return the tile data
	 */
	public static TileData getTileData(final int index) {
		return tiles[index];
	}

	/**
	 * Gets the unit class data.
	 *
	 * @param index the index
	 * @return the unit class data
	 */
	public static UnitClassData getUnitClassData(final int index) {
		return unitClasses[index];
	}

	/**
	 * Gets the sprite sheet.
	 *
	 * @param index the index
	 * @return the sprite sheet
	 */
	public static SpriteSheet getSpriteSheet(final int index) {
		return spriteSheets[index];
	}

	/**
	 * Gets the battle processor.
	 *
	 * @return the battle processor
	 */
	public static BattleProcessor getBattleProcessor() {
		return battleProcessor;
	}

	/**
	 * Checks if is on title.
	 *
	 * @return true, if is on title
	 */
	public static boolean isOnTitle() {
		return onTitle;
	}

	/**
	 * Sets the on title.
	 *
	 * @param iOnTitle the new on title
	 */
	public static void setOnTitle(final boolean iOnTitle) {
		onTitle = iOnTitle;
	}

	/**
	 * Checks if is on main menu.
	 *
	 * @return true, if is on main menu
	 */
	public static boolean isOnMainMenu() {
		return onMainMenu;
	}

	/**
	 * Sets the on main menu.
	 *
	 * @param iOnMainMenu the new on main menu
	 */
	public static void setOnMainMenu(final boolean iOnMainMenu) {
		onMainMenu = iOnMainMenu;
	}

	/**
	 * Gets the title background.
	 *
	 * @return the title background
	 */
	public static Sprite getTitleBackground() {
		return titleBackground;
	}

	/**
	 * Gets the main menu background.
	 *
	 * @return the main menu background
	 */
	public static Sprite getMainMenuBackground() {
		return mainMenuBackground;
	}

	/**
	 * Draw title menu.
	 */
	public static void drawTitleMenu() {
		titleMenu = new Menu(69, 31, 85, 100);

		MenuAction sa = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				// Do nothing.
			}
		};

		MenuAction start = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				onTitle = false;
				onMainMenu = true;
				menuList.remove(titleMenu);
				drawMainMenu();
			}
		};

		titleMenu.addElement(new MenuElement(sa, start, (new TextGraphic(
				"PRESS ENTER", Font.BASICFONT).getSprite()), true, 7, 13));
		MenuCursor.setActiveMenu(titleMenu);
	}

	/**
	 * Draw main menu.
	 */
	public static void drawMainMenu() {
		mainMenu = new Menu(110, 140, 120, 10);

		MenuAction sa = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				// Do nothing.
			}
		};

		MenuAction campaign = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				drawNoticeMenu(
						"This mode is not yet available.  Please wait for a future update.",
						mainMenu, menuCursor.getElementIndex());
			}
		};

		MenuAction freePlay = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				prepareFreeplay();
				drawFreePlayMenu();
				MenuCursor.getMenuCursor().setElementIndex(0);
				mainMenuOverlay = new Effect(new Animation(1,
						new Sprite[] { new Sprite(110, 140, 0, 0xff0000) }),
						new int[] { 128 }, new int[] { 0 }, 120, 10);
			}
		};

		MenuAction editors = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				drawNoticeMenu(
						"This mode is not yet available.  Please wait for a future update.",
						mainMenu, menuCursor.getElementIndex());
			}
		};

		MenuAction settings = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				drawSettingsMenu(mainMenu, menuCursor.getElementIndex(), "");
			}
		};

		MenuAction exit = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				getGame().stopSequence();
			}
		};

		mainMenu.addElement(new MenuElement(sa, campaign, (new TextGraphic(
				"CAMPAIGN", Font.BASICFONT).getSprite()), true, 7, 7));
		mainMenu.addElement(new MenuElement(sa, freePlay, (new TextGraphic(
				"FREEPLAY", Font.BASICFONT).getSprite()), true, 7, 17));
		mainMenu.addElement(new MenuElement(sa, editors, (new TextGraphic(
				"EDITORS", Font.BASICFONT).getSprite()), true, 7, 27));
		mainMenu.addElement(new MenuElement(sa, settings, (new TextGraphic(
				"SETTINGS", Font.BASICFONT).getSprite()), true, 7, 37));
		mainMenu.addElement(new MenuElement(sa, exit, (new TextGraphic(
				"EXIT GAME", Font.BASICFONT).getSprite()), true, 49, 127));
		MenuCursor.setActiveMenu(mainMenu);
	}

	/**
	 * Draw free play menu.
	 */
	public static void drawFreePlayMenu() {
		freePlayMenu = new Menu(100, 70, 10, 10);

		MenuAction sa = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				// Do nothing.
			}
		};

		MenuAction singlePlayer = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				hotseat = false;
				menuList.remove(mainMenu);
				menuList.remove(freePlayMenu);
				effectList.remove(mainMenuOverlay);
				drawMapSelectMenu();
			}
		};

		MenuAction hotSeat = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				hotseat = true;
				menuList.remove(mainMenu);
				menuList.remove(freePlayMenu);
				effectList.remove(mainMenuOverlay);
				drawMapSelectMenu();
			}
		};

		MenuAction back = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				menuList.remove(freePlayMenu);
				effectList.remove(mainMenuOverlay);
				MenuCursor.setActiveMenu(mainMenu);
			}
		};

		freePlayMenu.setEscapeAction(back);

		freePlayMenu.addElement(new MenuElement(sa, singlePlayer,
				(new TextGraphic("SINGLE PLAYER", Font.BASICFONT).getSprite()),
				true, 7, 7));
		freePlayMenu.addElement(new MenuElement(sa, hotSeat, (new TextGraphic(
				"HOTSEAT", Font.BASICFONT).getSprite()), true, 7, 17));
		freePlayMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(
				"LOCAL AREA", Font.BASICFONT).getSprite()), true, 7, 27));
		freePlayMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(
				"SERVER", Font.BASICFONT).getSprite()), true, 7, 37));
		freePlayMenu.addElement(new MenuElement(sa, back, (new TextGraphic(
				"BACK", Font.BASICFONT).getSprite()), true, 73, 57));

		MenuCursor.getMenuCursor().setElementIndex(0);
		MenuCursor.setActiveMenu(freePlayMenu);
	}

	/**
	 * Draw map select menu.
	 */
	public static void drawMapSelectMenu() {
		mapSelectMenu = new Menu(110, 140, 10, 10);

		MenuAction sa = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				// Do nothing
			}
		};

		MenuAction scrollUp = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				mapSelectPosition = !(mapSelectPosition == 0) ? 16 : ((int) Math
						.floor(freePlayMaps.length / 16)) * 16;
				menuList.remove(mapSelectMenu);
				MenuCursor.getMenuCursor().setElementIndex(0);
				drawMapSelectMenu();
			}
		};

		MenuAction select = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				menuList.remove(mapInfoMenu);
				drawMapInfoMenu(freePlayMaps[(MenuCursor.getMenuCursor()
						.getElementIndex() - 1) + mapSelectPosition]);
			}
		};

		MenuAction chooseMap = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				Sprite overlay = new Sprite(220, 140, 0x000000, 0xff0000);
				numPlayers = 1;
				currentPlayer = 1;
				currentFaction = 0;
				selectedFactions = new boolean[freePlayMaps[(MenuCursor
						.getMenuCursor().getElementIndex() - 1)
						+ mapSelectPosition].getNumFactions()];
				playerFactions = new int[freePlayMaps[(MenuCursor
						.getMenuCursor().getElementIndex() - 1)
						+ mapSelectPosition].getNumFactions()];
				if (hotseat) {
					drawPlayerNumMenu(freePlayMaps[(MenuCursor.getMenuCursor()
							.getElementIndex() - 1) + mapSelectPosition],
							(MenuCursor.getMenuCursor().getElementIndex() - 1)
									+ mapSelectPosition);
					for (int x = 0; x < playerNumMenu.getWidth(); x++) {
						for (int y = 0; y < playerNumMenu.getHeight(); y++) {
							overlay.setPixel(
									(x + playerNumMenu.getX() - 10)
											+ (y + playerNumMenu.getY() - 10)
											* overlay.getWidth(),
									overlay.getTransparentColor());
						}
					}
				} else {
					drawPlayerFactionMenu(freePlayMaps[(MenuCursor
							.getMenuCursor().getElementIndex() - 1)
							+ mapSelectPosition], (MenuCursor.getMenuCursor()
							.getElementIndex() - 1) + mapSelectPosition, false);
					for (int x = 0; x < playerFactionMenu.getWidth(); x++) {
						for (int y = 0; y < playerFactionMenu.getHeight(); y++) {
							overlay.setPixel(
									(x + playerFactionMenu.getX() - 10)
											+ (y + playerFactionMenu.getY() - 10)
											* overlay.getWidth(),
									overlay.getTransparentColor());
						}
					}
				}
				mainMenuOverlay = new Effect(new Animation(1,
						new Sprite[] { overlay }), new int[] { 128 }, 10, 10);
			}
		};

		MenuAction scrollDown = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				mapSelectPosition = mapSelectPosition >= ((int) Math
						.floor(freePlayMaps.length / 16)) ? 0 : 16;
				menuList.remove(mapSelectMenu);
				MenuCursor.getMenuCursor().setElementIndex(0);
				drawMapSelectMenu();
			}
		};

		MenuAction back = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				menuList.remove(mapSelectMenu);
				drawMainMenu();
				drawFreePlayMenu();
				mainMenuOverlay = new Effect(new Animation(1,
						new Sprite[] { new Sprite(110, 140, 0, 0xff0000) }),
						new int[] { 128 }, new int[] { 0 }, 120, 10);
			}
		};

		mapSelectMenu.setEscapeAction(back);

		mapSelectMenu.addElement(new MenuElement(sa, scrollUp, new TextGraphic(
				"^", Font.BASICFONT).getSprite(), true, 55, 7));
		for (int i = 0; i < 16; i++) {
			if (i + mapSelectPosition < freePlayMaps.length) {
				mapSelectMenu.addElement(new MenuElement(select, chooseMap,
						new TextGraphic(freePlayMaps[i + mapSelectPosition]
								.getName(), Font.BASICFONT).getSprite(), true,
						7, 13 + (i * 6)));
			}
		}
		mapSelectMenu
				.addElement(new MenuElement(sa, scrollDown, new TextGraphic(
						"v", Font.BASICFONT).getSprite(), true, 55, 116));
		mapSelectMenu.addElement(new MenuElement(sa, back, new TextGraphic(
				"BACK", Font.BASICFONT).getSprite(), true, 7, 127));

		MenuCursor.getMenuCursor().setElementIndex(1);
		MenuCursor.setActiveMenu(mapSelectMenu);
	}

	/**
	 * Draw map info menu.
	 *
	 * @param map the map
	 */
	public static void drawMapInfoMenu(final Map map) {
		mapInfoMenu = new Menu(110, 140, 120, 10);

		MenuAction sa = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				// Do nothing
			}
		};

		mapInfoMenu.addElement(new MenuElement(sa, sa,
				map.getMapSprite(96, 63), false, 7, 7));
		mapInfoMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"Width: " + map.getWidth(), Font.BASICFONT).getSprite(), false,
				7, 75));
		mapInfoMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"Height: " + map.getHeight(), Font.BASICFONT).getSprite(),
				false, 7, 81));
		mapInfoMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"Factions: " + map.getNumFactions(), Font.BASICFONT)
				.getSprite(), false, 7, 87));
		mapInfoMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"Units: " + map.getNumUnits(), Font.BASICFONT).getSprite(),
				false, 7, 93));
	}

	/**
	 * Draw player num menu.
	 *
	 * @param map the map
	 * @param mapIndex the map index
	 */
	public static void drawPlayerNumMenu(final Map map, final int mapIndex) {
		playerNumMenu = new Menu(64, 20, 88, 70);

		final Map inputMap = map;
		final int index = mapIndex;
		final int numFactions = map.getNumFactions();

		MenuAction sa = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				// Do nothing
			}
		};

		MenuAction down = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				numPlayers--;
				if (numPlayers <= 0) {
					numPlayers = 1;
				}
				playerNumMenu.removeMenu();
				drawPlayerNumMenu(inputMap, index);
			}
		};

		MenuAction up = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				numPlayers++;
				if (numPlayers > numFactions) {
					numPlayers = numFactions;
				}
				playerNumMenu.removeMenu();
				drawPlayerNumMenu(inputMap, index);
			}
		};

		MenuAction ok = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				playerNumMenu.removeMenu();
				effectList.remove(mainMenuOverlay);
				drawPlayerFactionMenu(inputMap, index, false);
				Sprite overlay = new Sprite(220, 140, 0x000000, 0xff0000);
				for (int x = 0; x < playerFactionMenu.getWidth(); x++) {
					for (int y = 0; y < playerFactionMenu.getHeight(); y++) {
						overlay.setPixel(
								(x + playerFactionMenu.getX() - 10)
										+ (y + playerFactionMenu.getY() - 10)
										* overlay.getWidth(),
								overlay.getTransparentColor());
					}
				}
				mainMenuOverlay = new Effect(new Animation(1,
						new Sprite[] { overlay }), new int[] { 128 }, 10, 10);

			}
		};

		MenuAction back = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				playerNumMenu.removeMenu();
				effectList.remove(mainMenuOverlay);
				MenuCursor.getMenuCursor().setElementIndex(1);
				MenuCursor.setActiveMenu(mapSelectMenu);
			}
		};

		playerNumMenu.setLeftAction(down);
		playerNumMenu.setRightAction(up);
		playerNumMenu.setEscapeAction(back);

		playerNumMenu.addElement(new MenuElement(sa, ok, new TextGraphic(
				numPlayers + " Players", Font.BASICFONT).getSprite(), true, 7,
				7));

		MenuCursor.getMenuCursor().setElementIndex(0);
		MenuCursor.setActiveMenu(playerNumMenu);
	}

	/**
	 * Draw player faction menu.
	 *
	 * @param map the map
	 * @param mapIndex the map index
	 * @param message the message
	 */
	public static void drawPlayerFactionMenu(final Map map, final int mapIndex,
			final boolean message) {
		playerFactionMenu = new Menu(114, 26, 68, 67);

		final Map inputMap = map;
		final int index = mapIndex;
		final int numFactions = map.getNumFactions();

		MenuAction sa = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				// Do nothing
			}
		};

		MenuAction down = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				currentFaction--;
				if (currentFaction < 0) {
					currentFaction = 0;
				}
				playerFactionMenu.removeMenu();
				drawPlayerFactionMenu(inputMap, index, false);
			}
		};

		MenuAction up = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				currentFaction++;
				if (currentFaction >= numFactions) {
					currentFaction = numFactions - 1;
				}
				playerFactionMenu.removeMenu();
				drawPlayerFactionMenu(inputMap, index, false);

			}
		};

		MenuAction ok = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				if (selectedFactions[currentFaction]) {
					playerFactionMenu.removeMenu();
					drawPlayerFactionMenu(inputMap, index, true);
				} else {
					selectedFactions[currentFaction] = true;
					playerFactions[currentFaction] = currentPlayer;
					currentPlayer++;
					if (!(currentPlayer > numPlayers)) {
						playerFactionMenu.removeMenu();
						drawPlayerFactionMenu(inputMap, index, false);
					} else {
						onMainMenu = false;
						prepareMap(inputMap, index, selectedFactions);
						menuCursor.setActive(false);
						MenuCursor.setActiveMenu(null);
						mapSelectMenu.removeMenu();
						if (mapInfoMenu != null) {
							mapInfoMenu.removeMenu();
						}
						playerFactionMenu.removeMenu();
						effectList.remove(mainMenuOverlay);
						cursor.setActive(true);
						if (Game.getCurrentMap().getPlayer(0) != null) {
							Game.getCurrentMap().getPlayer(0).startTurn();
						}
					}
				}
			}
		};

		MenuAction back = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				playerFactionMenu.removeMenu();
				if (!(hotseat)) {
					effectList.remove(mainMenuOverlay);
					MenuCursor.getMenuCursor().setElementIndex(1);
					MenuCursor.setActiveMenu(mapSelectMenu);
				} else {
					currentPlayer = 1;
					effectList.remove(mainMenuOverlay);
					drawPlayerNumMenu(inputMap, index);
					Sprite overlay = new Sprite(220, 140, 0x000000, 0xff0000);
					for (int x = 0; x < playerNumMenu.getWidth(); x++) {
						for (int y = 0; y < playerNumMenu.getHeight(); y++) {
							overlay.setPixel(
									(x + playerNumMenu.getX() - 10)
											+ (y + playerNumMenu.getY() - 10)
											* overlay.getWidth(),
									overlay.getTransparentColor());
						}
					}
					mainMenuOverlay = new Effect(new Animation(1,
							new Sprite[] { overlay }), new int[] { 128 }, 10,
							10);
				}
			}
		};

		playerFactionMenu.setLeftAction(down);
		playerFactionMenu.setRightAction(up);
		playerFactionMenu.setEscapeAction(back);

		playerFactionMenu.addElement(new MenuElement(sa, ok, new TextGraphic(
				"Player " + currentPlayer + ": Faction " + currentFaction,
				Font.BASICFONT).getSprite(), true, 7, 7));
		if (message) {
			playerFactionMenu.addElement(new MenuElement(sa, sa,
					new TextGraphic("Faction in use.", Font.BASICFONT)
							.getSprite(), false, 7, 13));
		}

		MenuCursor.getMenuCursor().setElementIndex(0);
		MenuCursor.setActiveMenu(playerFactionMenu);
	}

	/**
	 * Draw settings menu.
	 *
	 * @param lastMenu the last menu
	 * @param lastIndex the last index
	 * @param message the message
	 */
	public static void drawSettingsMenu(final Menu lastMenu, final int lastIndex,
			final String message) {
		if (settingsMenu != null) {
			settingsMenu.removeMenu();
		}
		settingsMenu = new Menu(220, 140, 10, 10);

		final Menu backMenu = lastMenu;
		final int backIndex = lastIndex;

		MenuAction sa = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				// Do nothing
			}
		};

		MenuAction left = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				int curIndex = menuCursor.getElementIndex();
				switch (curIndex) {
				case 0:
					gameScale = Math.max(1, gameScale - 1);
					settingsMenu.removeMenu();
					drawSettingsMenu(backMenu, backIndex, "");
					menuCursor.setElementIndex(curIndex);
					break;
				case 2:
					drawFactionShadow = !drawFactionShadow;
					settingsMenu.removeMenu();
					drawSettingsMenu(backMenu, backIndex, "");
					menuCursor.setElementIndex(curIndex);
					break;
				case 4:
					gridOpacity = Math.max(0, gridOpacity - 1);
					settingsMenu.removeMenu();
					drawSettingsMenu(backMenu, backIndex, "");
					menuCursor.setElementIndex(curIndex);
					break;
				default:
					break;
				}
			}
		};

		MenuAction right = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				int curIndex = menuCursor.getElementIndex();
				switch (curIndex) {
				case 0:
					gameScale = Math.min(6, gameScale + 1);
					settingsMenu.removeMenu();
					drawSettingsMenu(backMenu, backIndex, "");
					menuCursor.setElementIndex(curIndex);
					break;
				case 2:
					drawFactionShadow = !drawFactionShadow;
					settingsMenu.removeMenu();
					drawSettingsMenu(backMenu, backIndex, "");
					menuCursor.setElementIndex(curIndex);
					break;
				case 4:
					gridOpacity = Math.min(256, gridOpacity + 1);
					settingsMenu.removeMenu();
					drawSettingsMenu(backMenu, backIndex, "");
					menuCursor.setElementIndex(curIndex);
					break;
				default:
					break;
				}
			}
		};

		MenuAction save = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				XMLReader reader = new XMLReader();
				reader.writeSettings();
				int curIndex = menuCursor.getElementIndex();
				settingsMenu.removeMenu();
				drawSettingsMenu(backMenu, backIndex, "DATA HAS BEEN SAVED.");
				menuCursor.setElementIndex(curIndex);
			}
		};

		MenuAction back = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				settingsMenu.removeMenu();
				if (backMenu != null) {
					menuCursor.setElementIndex(backIndex);
					MenuCursor.setActiveMenu(backMenu);
				} else {
					menuCursor.setActive(false);
					cursor.setActive(true);
				}
			}
		};

		settingsMenu.setEscapeAction(back);
		settingsMenu.setLeftAction(left);
		settingsMenu.setRightAction(right);

		settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"GAME SCALE*", Font.BASICFONT).getSprite(), true, 7, 7));
		settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic(""
				+ gameScale, Font.BASICFONT).getSprite(), false, 188, 7));
		settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"DRAW UNIT FACTION SHADOW", Font.BASICFONT).getSprite(), true,
				7, 13));
		settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				drawFactionShadow ? "TRUE" : "FALSE", Font.BASICFONT)
				.getSprite(), false, 188, 13));
		settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"GRID OPACITY", Font.BASICFONT).getSprite(), true, 7, 19));
		settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic(""
				+ gridOpacity, Font.BASICFONT).getSprite(), false, 188, 19));

		settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				"*SETTINGS WILL NOT BE APPLIED UNTIL THE", Font.BASICFONT)
				.getSprite(), false, 7, 115));
		settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
				" GAME HAS BEEN RESET.", Font.BASICFONT).getSprite(), false, 7,
				121));
		settingsMenu.addElement(new MenuElement(sa, save, new TextGraphic(
				"SAVE", Font.BASICFONT).getSprite(), true, 7, 127));
		settingsMenu.addElement(new MenuElement(sa, back, new TextGraphic(
				"BACK", Font.BASICFONT).getSprite(), true, 32, 127));
		settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic("| "
				+ message, Font.BASICFONT).getSprite(), false, 57, 127));

		menuCursor.setElementIndex(0);
		MenuCursor.setActiveMenu(settingsMenu);
	}

	/**
	 * Draw notice menu.
	 *
	 * @param message the message
	 * @param lastMenu the last menu
	 * @param lastIndex the last index
	 */
	public static void drawNoticeMenu(final String message, final Menu lastMenu,
			final int lastIndex) {
		if (noticeMenu != null) {
			noticeMenu.removeMenu();
		}
		String[] lines = Menu.wrapText(message, 20);
		noticeMenu = new Menu(114, Math.min(GAME_HEIGHT,
				(6 * (lines.length + 1)) + 14), 63, Math.max(0,
				(GAME_HEIGHT - ((6 * (lines.length + 1)) + 14)) / 2));

		final Menu backMenu = lastMenu;
		final int backIndex = lastIndex;

		MenuAction sa = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				// Do nothing
			}
		};

		MenuAction back = new MenuAction() {
			@Override
			public void execute(final MenuElement caller) {
				noticeMenu.removeMenu();
				if (backMenu != null) {
					menuCursor.setElementIndex(backIndex);
					MenuCursor.setActiveMenu(backMenu);
				} else {
					menuCursor.setActive(false);
					cursor.setActive(true);
				}
			}
		};

		noticeMenu.setEscapeAction(back);

		for (int i = 0; i < lines.length; i++) {
			noticeMenu.addElement(new MenuElement(sa, sa, new TextGraphic(
					lines[i], Font.BASICFONT).getSprite(), false, 7,
					7 + (6 * i)));
		}
		noticeMenu.addElement(new MenuElement(sa, back, new TextGraphic(
				"CLOSE", Font.BASICFONT).getSprite(), true, 42,
				13 + (6 * lines.length)));

		menuCursor.setElementIndex(lines.length);
		MenuCursor.setActiveMenu(noticeMenu);
	}

	/**
	 * Gets the renderer.
	 *
	 * @return the renderer
	 */
	public final Renderer getRenderer() {
		return renderer;
	}

	/**
	 * Gets the game scale.
	 *
	 * @return the game scale
	 */
	public static int getGameScale() {
		return gameScale;
	}

	/**
	 * Checks if is draw faction shadow.
	 *
	 * @return true, if is draw faction shadow
	 */
	public static boolean isDrawFactionShadow() {
		return drawFactionShadow;
	}

	/**
	 * Gets the grid opacity.
	 *
	 * @return the grid opacity
	 */
	public static int getGridOpacity() {
		return gridOpacity;
	}
}
