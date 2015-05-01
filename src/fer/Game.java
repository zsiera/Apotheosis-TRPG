package fer;

import fer.ai.AIPlayer;
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

/**
 * @author Evan Stewart
 *
 * The main class for the game sequence. Contains the window and functions as
 * the canvas upon which to draw graphics. Also handles the threading, looping,
 * and running, of the game sequence, orchestrating tasks such as rendering game
 * graphics and updating game logic.
 *
 */
public class Game extends Canvas implements Runnable {

    private static Game instance;
    public static final int GAME_WIDTH = 240;
    public static final int GAME_HEIGHT = 160;
    public static int GAME_SCALE;
    public static final int GAME_UPS = 60;
    public static Dimension GAME_RES;
    public static final String GAME_TITLE = "Apotheosis";
    private static WeaponData[] weapons;
    private static ItemData[] items;
    private static ArmorData[] armor;
    private static TileData[] tiles;
    private static UnitClassData[] unitClasses;
    private static MapData[] freeplayMapData;
    private static SpriteSheet[] spriteSheets;
    private static Map currentMap;
    private static CopyOnWriteArrayList<Menu> menuList;
    private static CopyOnWriteArrayList<Effect> effectList;
    private static BattleProcessor battleProcessor;
    private static Menu titleMenu;
    private static Menu mainMenu;
    private static Menu freePlayMenu;
    private static Menu mapSelectMenu;
    private static Menu mapInfoMenu;
    private static Menu playerNumMenu;
    private static Menu playerFactionMenu;
    private static Menu settingsMenu;
    private static Menu noticeMenu;
    private static int currentPlayer;
    private static int currentFaction;
    private static int numPlayers;
    private static boolean hotseat = false;
    private static boolean[] selectedFactions;
    private static int[] playerFactions;
    private static Effect mainMenuOverlay;
    private static Sprite titleBackground;
    private static Sprite mainMenuBackground;
    private static boolean onTitle = true;
    private static boolean onMainMenu = false;
    private static int mapSelectPosition = 0;
    private boolean gameRunning = false;
    private int updates = 0;
    private Thread logicThread;
    private Thread graphicsThread;
    private Thread controlThread;
    private boolean lastEnter = false;
    private boolean lastEscape = false;
    private Renderer renderer;
    private JFrame gameWindow;
    private Keyboard keyboard;
    private static Cursor cursor;
    private static MenuCursor menuCursor;
    private static Map[] freePlayMaps;
    //Global game settings
    private static int gameScale;
    private static boolean drawFactionShadow;
    private static int gridOpacity;
    //Setting defaults
    private static final boolean DRAWFACTIONSHADOWDEFAULT = true;
    private static final int GRIDOPACITYDEFAULT = 26;

    public Game() {
        loadSettings();
        GAME_SCALE = gameScale;
        GAME_RES = new Dimension((GAME_WIDTH * GAME_SCALE), (GAME_HEIGHT * GAME_SCALE));
        setPreferredSize(GAME_RES);
        setBackground(Color.black);
        setFocusTraversalKeysEnabled(false);

        gameWindow = new JFrame(GAME_TITLE);
        keyboard = new Keyboard();
        addKeyListener(keyboard);
        menuList = new CopyOnWriteArrayList();
        effectList = new CopyOnWriteArrayList();
        battleProcessor = new BattleProcessor();
        titleBackground = new Sprite(240, 160, 0, 0, SpriteSheet.MENUBACK);
        mainMenuBackground = new Sprite(240, 160, 0, 0, SpriteSheet.MENUBACK);
        loadData();
        currentMap = null;
        cursor = Cursor.getCursor();
        menuCursor = MenuCursor.getMenuCursor();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        createInstance();

        instance.startSequence();
    }

    private static void createInstance() {
        instance = new Game();

        instance.gameWindow.setResizable(false);
        instance.gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instance.gameWindow.add(instance);
        instance.gameWindow.pack();
        instance.gameWindow.setLocationRelativeTo(null);
        instance.gameWindow.setVisible(true);
    }

    public static Game getGame() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    public boolean getGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean running) {
        gameRunning = running;
    }

    public String getGameWindowTitle() {
        return gameWindow.getTitle();
    }

    public void setGameWindowTitle(String title) {
        gameWindow.setTitle(title);
    }

    public void loadData() {
        //Read all data messenger objects from XML
        XMLReader reader = new XMLReader();
        weapons = reader.serializeAllWeaponData().toArray(new WeaponData[1]);
        items = reader.serializeAllItemData().toArray(new ItemData[1]);
        armor = reader.serializeAllArmorData().toArray(new ArmorData[1]);
        tiles = reader.serializeAllTileData().toArray(new TileData[1]);
        unitClasses = reader.serializeAllUnitClassData().toArray(new UnitClassData[1]);
        freeplayMapData = new MapData[reader.getNumFreeplayMaps()];
        for (int i = 0; i < freeplayMapData.length; i++) {
            freeplayMapData[i] = reader.serializeMapData(0, i);
        }
        //Load and store all spritesheets and corresponding indices
        ArrayList<String> sheetpaths = new ArrayList();
        ArrayList<Integer> widths = new ArrayList();
        ArrayList<Integer> heights = new ArrayList();
        ArrayList<Integer> colors = new ArrayList();
        for (int i = 0; i < weapons.length; i++) {
            if (!sheetpaths.contains(weapons[i].getSheetPath())) {
                sheetpaths.add(weapons[i].getSheetPath());
                widths.add(weapons[i].getSheetWidth());
                heights.add(weapons[i].getSheetHeight());
                colors.add(weapons[i].getSheetTransparentColor());
            }
            weapons[i].setSheetIndex(sheetpaths.indexOf(weapons[i].getSheetPath()));
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
            unitClasses[i].setSheetIndex(sheetpaths.indexOf(unitClasses[i].getSheetPath()));
        }
        spriteSheets = new SpriteSheet[sheetpaths.size()];
        for (int i = 0; i < spriteSheets.length; i++) {
            spriteSheets[i] = new SpriteSheet(sheetpaths.get(i), widths.get(i),
                    heights.get(i), colors.get(i));
        }
    }

    public void loadSettings() {
        XMLReader reader = new XMLReader();
        SettingsData data = reader.serializeSettingsData();
        gameScale = data.getGameScale();
        drawFactionShadow = data.isFactionShadows();
        gridOpacity = data.getGridOpacity();
    }

    public synchronized void startSequence() {
        //loadData();
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

    public synchronized void stopSequence() {
        gameRunning = false;
        try {
            //logicThread.join();
            graphicsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameWindow.dispose();
    }

    public void update() {
        int xQueue = 0, yQueue = 0;
        boolean enter, escape;
        //keyboard.update();
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
                cursor.update(currentMap, xQueue, yQueue, enter,
                        escape, keyboard.isTab());
            } else if (menuCursor.isActive()) {
                menuCursor.update(xQueue, yQueue, enter,
                        escape);
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

    public void run() {
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
            //System.out.println("Logic thread updated.");
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void prepareFreeplay() {
        freePlayMaps = new Map[freeplayMapData.length];
        for (int i = 0; i < freePlayMaps.length; i++) {
            freePlayMaps[i] = new Map(freeplayMapData[i]);
        }
    }

    public static void prepareMap(Map map, int index, boolean[] selectedFactions) {
        //Read corresponding map XML data
        XMLReader reader = new XMLReader();
        GoalData[] goals = reader.serializeAllGoalData(0, index).toArray(new GoalData[1]);
        UnitData[] units = reader.serializeAllUnitData(0, index).toArray(new UnitData[1]);
        for (int i = 0; i < selectedFactions.length; i++) {
            if (!selectedFactions[i]) {
                map.setPlayer(i, new AIPlayer(i));
            }
        }
        //Initialize the map
        map.init(goals, units);
        //Clear the temporary array of maps
        freePlayMaps = null;
        //Set the current map
        cursor.resetCursor();
        currentMap = map;
    }

    public static Map getCurrentMap() {
        return currentMap;
    }

    public static void setCurrentMap(Map newMap) {
        currentMap = newMap;
    }

    public static CopyOnWriteArrayList<Menu> getMenuList() {
        return menuList;
    }

    public static CopyOnWriteArrayList<Effect> getEffectList() {
        return effectList;
    }

    public static WeaponData getWeaponData(int index) {
        return weapons[index];
    }

    public static ItemData getItemData(int index) {
        return items[index];
    }

    public static ArmorData getArmorData(int index) {
        return armor[index];
    }

    public static TileData getTileData(int index) {
        return tiles[index];
    }

    public static UnitClassData getUnitClassData(int index) {
        return unitClasses[index];
    }

    public static SpriteSheet getSpriteSheet(int index) {
        return spriteSheets[index];
    }

    public static BattleProcessor getBattleProcessor() {
        return battleProcessor;
    }

    public static boolean isOnTitle() {
        return onTitle;
    }

    public static void setOnTitle(boolean iOnTitle) {
        onTitle = iOnTitle;
    }

    public static boolean isOnMainMenu() {
        return onMainMenu;
    }

    public static void setOnMainMenu(boolean iOnMainMenu) {
        onMainMenu = iOnMainMenu;
    }

    public static Sprite getTitleBackground() {
        return titleBackground;
    }

    public static Sprite getMainMenuBackground() {
        return mainMenuBackground;
    }

    public static void drawTitleMenu() {
        titleMenu = new Menu(69, 31, 85, 100);

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };

        MenuAction start = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                onTitle = false;
                onMainMenu = true;
                menuList.remove(titleMenu);
                drawMainMenu();
            }
        };

        titleMenu.addElement(new MenuElement(sa, start, (new TextGraphic("PRESS ENTER", Font.BASICFONT).getSprite()), true, 7, 13));
        MenuCursor.setActiveMenu(titleMenu);
    }

    public static void drawMainMenu() {
        mainMenu = new Menu(110, 140, 120, 10);

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };
        
        MenuAction campaign = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                drawNoticeMenu("This mode is not yet available.  Please wait for a future update.", mainMenu, menuCursor.getElementIndex());
            }
        };

        MenuAction freePlay = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                prepareFreeplay();
                drawFreePlayMenu();
                MenuCursor.getMenuCursor().setElementIndex(0);
                mainMenuOverlay = new Effect(new Animation(1, new Sprite[]{new Sprite(110, 140, 0, 0xff0000)}), new int[]{128}, new int[]{0}, 120, 10);
            }
        };
        
        MenuAction editors = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                drawNoticeMenu("This mode is not yet available.  Please wait for a future update.", mainMenu, menuCursor.getElementIndex());
            }
        };

        MenuAction settings = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                drawSettingsMenu(mainMenu, menuCursor.getElementIndex(), "");
            }
        };
        
        MenuAction exit = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                getGame().stopSequence();
            }
        };

        mainMenu.addElement(new MenuElement(sa, campaign, (new TextGraphic("CAMPAIGN", Font.BASICFONT).getSprite()), true, 7, 7));
        mainMenu.addElement(new MenuElement(sa, freePlay, (new TextGraphic("FREEPLAY", Font.BASICFONT).getSprite()), true, 7, 17));
        mainMenu.addElement(new MenuElement(sa, editors, (new TextGraphic("EDITORS", Font.BASICFONT).getSprite()), true, 7, 27));
        mainMenu.addElement(new MenuElement(sa, settings, (new TextGraphic("SETTINGS", Font.BASICFONT).getSprite()), true, 7, 37));
        mainMenu.addElement(new MenuElement(sa, exit, (new TextGraphic("EXIT GAME", Font.BASICFONT).getSprite()), true, 49, 127));
        MenuCursor.setActiveMenu(mainMenu);
    }

    public static void drawFreePlayMenu() {
        freePlayMenu = new Menu(100, 70, 10, 10);

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };

        MenuAction singlePlayer = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                hotseat = false;
                menuList.remove(mainMenu);
                menuList.remove(freePlayMenu);
                effectList.remove(mainMenuOverlay);
                drawMapSelectMenu();
            }
        };

        MenuAction hotSeat = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                hotseat = true;
                menuList.remove(mainMenu);
                menuList.remove(freePlayMenu);
                effectList.remove(mainMenuOverlay);
                drawMapSelectMenu();
            }
        };

        MenuAction back = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                menuList.remove(freePlayMenu);
                effectList.remove(mainMenuOverlay);
                MenuCursor.setActiveMenu(mainMenu);
            }
        };

        freePlayMenu.setEscapeAction(back);

        freePlayMenu.addElement(new MenuElement(sa, singlePlayer, (new TextGraphic("SINGLE PLAYER", Font.BASICFONT).getSprite()), true, 7, 7));
        freePlayMenu.addElement(new MenuElement(sa, hotSeat, (new TextGraphic("HOTSEAT", Font.BASICFONT).getSprite()), true, 7, 17));
        freePlayMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("LOCAL AREA", Font.BASICFONT).getSprite()), true, 7, 27));
        freePlayMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("SERVER", Font.BASICFONT).getSprite()), true, 7, 37));
        freePlayMenu.addElement(new MenuElement(sa, back, (new TextGraphic("BACK", Font.BASICFONT).getSprite()), true, 73, 57));

        MenuCursor.getMenuCursor().setElementIndex(0);
        MenuCursor.setActiveMenu(freePlayMenu);
    }

    public static void drawMapSelectMenu() {
        mapSelectMenu = new Menu(110, 140, 10, 10);

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing
            }
        };

        MenuAction scrollUp = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                if (mapSelectPosition == 0) {
                    mapSelectPosition = ((int) Math.floor(freePlayMaps.length / 16)) * 16;
                } else {
                    mapSelectPosition -= 16;
                }
                menuList.remove(mapSelectMenu);
                MenuCursor.getMenuCursor().setElementIndex(0);
                drawMapSelectMenu();
            }
        };

        MenuAction select = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                menuList.remove(mapInfoMenu);
                drawMapInfoMenu(freePlayMaps[(MenuCursor.getMenuCursor().getElementIndex() - 1) + mapSelectPosition]);
            }
        };

        MenuAction chooseMap = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                Sprite overlay = new Sprite(220, 140, 0x000000, 0xff0000);
                numPlayers = 1;
                currentPlayer = 1;
                currentFaction = 0;
                selectedFactions = new boolean[freePlayMaps[(MenuCursor.getMenuCursor().getElementIndex() - 1) + mapSelectPosition].getNumFactions()];
                playerFactions = new int[freePlayMaps[(MenuCursor.getMenuCursor().getElementIndex() - 1) + mapSelectPosition].getNumFactions()];
                if (hotseat) {
                    drawPlayerNumMenu(freePlayMaps[(MenuCursor.getMenuCursor().getElementIndex() - 1) + mapSelectPosition], (MenuCursor.getMenuCursor().getElementIndex() - 1) + mapSelectPosition);
                    for (int x = 0; x < playerNumMenu.getWidth(); x++) {
                        for (int y = 0; y < playerNumMenu.getHeight(); y++) {
                            overlay.setPixel((x + playerNumMenu.getX() - 10)
                                    + (y + playerNumMenu.getY() - 10)
                                    * overlay.getWidth(), overlay.
                                    getTransparentColor());
                        }
                    }
                } else {
                    drawPlayerFactionMenu(freePlayMaps[(MenuCursor.getMenuCursor().getElementIndex() - 1) + mapSelectPosition], (MenuCursor.getMenuCursor().getElementIndex() - 1) + mapSelectPosition, false);
                    for (int x = 0; x < playerFactionMenu.getWidth(); x++) {
                        for (int y = 0; y < playerFactionMenu.getHeight(); y++) {
                            overlay.setPixel((x + playerFactionMenu.getX() - 10)
                                    + (y + playerFactionMenu.getY() - 10)
                                    * overlay.getWidth(), overlay.
                                    getTransparentColor());
                        }
                    }
                }
                mainMenuOverlay = new Effect(new Animation(1, new Sprite[]{overlay}), new int[]{128}, 10, 10);
            }
        };

        MenuAction scrollDown = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                if (mapSelectPosition >= ((int) Math.floor(freePlayMaps.length / 16))) {
                    mapSelectPosition = 0;
                } else {
                    mapSelectPosition += 16;
                }
                menuList.remove(mapSelectMenu);
                MenuCursor.getMenuCursor().setElementIndex(0);
                drawMapSelectMenu();
            }
        };

        MenuAction back = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                menuList.remove(mapSelectMenu);
                drawMainMenu();
                drawFreePlayMenu();
                mainMenuOverlay = new Effect(new Animation(1, new Sprite[]{new Sprite(110, 140, 0, 0xff0000)}), new int[]{128}, new int[]{0}, 120, 10);
            }
        };

        mapSelectMenu.setEscapeAction(back);

        mapSelectMenu.addElement(new MenuElement(sa, scrollUp, new TextGraphic("^", Font.BASICFONT).getSprite(), true, 55, 7));
        for (int i = 0; i < 16; i++) {
            if (i + mapSelectPosition < freePlayMaps.length) {
                mapSelectMenu.addElement(new MenuElement(select, chooseMap, new TextGraphic(freePlayMaps[i + mapSelectPosition].
                        getName(), Font.BASICFONT).getSprite(), true, 7, 13
                        + (i * 6)));
            }
        }
        mapSelectMenu.addElement(new MenuElement(sa, scrollDown, new TextGraphic("v", Font.BASICFONT).getSprite(), true, 55, 116));
        mapSelectMenu.addElement(new MenuElement(sa, back, new TextGraphic("BACK", Font.BASICFONT).getSprite(), true, 7, 127));

        MenuCursor.getMenuCursor().setElementIndex(1);
        MenuCursor.setActiveMenu(mapSelectMenu);
    }

    public static void drawMapInfoMenu(Map map) {
        mapInfoMenu = new Menu(110, 140, 120, 10);

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing
            }
        };

        mapInfoMenu.addElement(new MenuElement(sa, sa, map.getMapSprite(96, 63), false, 7, 7));
        mapInfoMenu.addElement(new MenuElement(sa, sa, new TextGraphic("Width: " + map.getWidth(), Font.BASICFONT).getSprite(), false, 7, 75));
        mapInfoMenu.addElement(new MenuElement(sa, sa, new TextGraphic("Height: " + map.getHeight(), Font.BASICFONT).getSprite(), false, 7, 81));
        mapInfoMenu.addElement(new MenuElement(sa, sa, new TextGraphic("Factions: " + map.getNumFactions(), Font.BASICFONT).getSprite(), false, 7, 87));
        mapInfoMenu.addElement(new MenuElement(sa, sa, new TextGraphic("Units: " + map.getNumUnits(), Font.BASICFONT).getSprite(), false, 7, 93));
    }

    public static void drawPlayerNumMenu(Map map, int mapIndex) {
        playerNumMenu = new Menu(64, 20, 88, 70);

        final Map inputMap = map;
        final int index = mapIndex;
        final int numFactions = map.getNumFactions();

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing
            }
        };

        MenuAction down = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
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
            public void execute(MenuElement caller) {
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
            public void execute(MenuElement caller) {
                playerNumMenu.removeMenu();
                effectList.remove(mainMenuOverlay);
                drawPlayerFactionMenu(inputMap, index, false);
                Sprite overlay = new Sprite(220, 140, 0x000000, 0xff0000);
                for (int x = 0; x < playerFactionMenu.getWidth(); x++) {
                    for (int y = 0; y < playerFactionMenu.getHeight(); y++) {
                        overlay.setPixel((x + playerFactionMenu.getX() - 10)
                                + (y + playerFactionMenu.getY() - 10)
                                * overlay.getWidth(), overlay.
                                getTransparentColor());
                    }
                }
                mainMenuOverlay = new Effect(new Animation(1, new Sprite[]{overlay}), new int[]{128}, 10, 10);

            }
        };

        MenuAction back = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                playerNumMenu.removeMenu();
                effectList.remove(mainMenuOverlay);
                MenuCursor.getMenuCursor().setElementIndex(1);
                MenuCursor.setActiveMenu(mapSelectMenu);
            }
        };

        playerNumMenu.setLeftAction(down);
        playerNumMenu.setRightAction(up);
        playerNumMenu.setEscapeAction(back);


        playerNumMenu.addElement(new MenuElement(sa, ok, new TextGraphic(numPlayers + " Players", Font.BASICFONT).getSprite(), true, 7, 7));

        MenuCursor.getMenuCursor().setElementIndex(0);
        MenuCursor.setActiveMenu(playerNumMenu);
    }

    public static void drawPlayerFactionMenu(Map map, int mapIndex, boolean message) {
        playerFactionMenu = new Menu(114, 26, 68, 67);

        final Map inputMap = map;
        final int index = mapIndex;
        final int numFactions = map.getNumFactions();

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing
            }
        };

        MenuAction down = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
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
            public void execute(MenuElement caller) {
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
            public void execute(MenuElement caller) {
                if (selectedFactions[currentFaction]) {
                    playerFactionMenu.removeMenu();
                    drawPlayerFactionMenu(inputMap, index, true);
                } else {
                    selectedFactions[currentFaction] = true;
                    playerFactions[currentFaction] = currentPlayer;
                    currentPlayer++;
                    if (currentPlayer > numPlayers) {
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
                    } else {
                        playerFactionMenu.removeMenu();
                        drawPlayerFactionMenu(inputMap, index, false);
                    }
                }
            }
        };

        MenuAction back = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                playerFactionMenu.removeMenu();
                if (hotseat) {
                    currentPlayer = 1;
                    effectList.remove(mainMenuOverlay);
                    drawPlayerNumMenu(inputMap, index);
                    Sprite overlay = new Sprite(220, 140, 0x000000, 0xff0000);
                    for (int x = 0; x < playerNumMenu.getWidth(); x++) {
                        for (int y = 0; y < playerNumMenu.getHeight(); y++) {
                            overlay.setPixel((x + playerNumMenu.getX() - 10)
                                    + (y + playerNumMenu.getY() - 10)
                                    * overlay.getWidth(), overlay.
                                    getTransparentColor());
                        }
                    }
                    mainMenuOverlay = new Effect(new Animation(1, new Sprite[]{overlay}), new int[]{128}, 10, 10);
                } else {
                    effectList.remove(mainMenuOverlay);
                    MenuCursor.getMenuCursor().setElementIndex(1);
                    MenuCursor.setActiveMenu(mapSelectMenu);
                }
            }
        };

        playerFactionMenu.setLeftAction(down);
        playerFactionMenu.setRightAction(up);
        playerFactionMenu.setEscapeAction(back);


        playerFactionMenu.addElement(new MenuElement(sa, ok, new TextGraphic("Player " + currentPlayer + ": Faction " + currentFaction, Font.BASICFONT).getSprite(), true, 7, 7));
        if (message) {
            playerFactionMenu.addElement(new MenuElement(sa, sa, new TextGraphic("Faction in use.", Font.BASICFONT).getSprite(), false, 7, 13));
        }

        MenuCursor.getMenuCursor().setElementIndex(0);
        MenuCursor.setActiveMenu(playerFactionMenu);
    }

    public static void drawSettingsMenu(Menu lastMenu, int lastIndex, String message) {
        if (settingsMenu != null) {
            settingsMenu.removeMenu();
        }
        settingsMenu = new Menu(220, 140, 10, 10);

        final Menu backMenu = lastMenu;
        final int backIndex = lastIndex;

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing
            }
        };

        MenuAction left = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
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
                }
            }
        };
        
        MenuAction right = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
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
                }
            }
        };

        MenuAction save = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
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
            public void execute(MenuElement caller) {
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

        settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic("GAME SCALE*", Font.BASICFONT).getSprite(), true, 7, 7));
        settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic("" + gameScale, Font.BASICFONT).getSprite(), false, 188, 7));
        settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic("DRAW UNIT FACTION SHADOW", Font.BASICFONT).getSprite(), true, 7, 13));
        settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic(drawFactionShadow ? "TRUE" : "FALSE", Font.BASICFONT).getSprite(), false, 188, 13));
        settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic("GRID OPACITY", Font.BASICFONT).getSprite(), true, 7, 19));
        settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic("" + gridOpacity, Font.BASICFONT).getSprite(), false, 188, 19));

        settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic("*SETTINGS WILL NOT BE APPLIED UNTIL THE", Font.BASICFONT).getSprite(), false, 7, 115));
        settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic(" GAME HAS BEEN RESET.", Font.BASICFONT).getSprite(), false, 7, 121));
        settingsMenu.addElement(new MenuElement(sa, save, new TextGraphic("SAVE", Font.BASICFONT).getSprite(), true, 7, 127));
        settingsMenu.addElement(new MenuElement(sa, back, new TextGraphic("BACK", Font.BASICFONT).getSprite(), true, 32, 127));
        settingsMenu.addElement(new MenuElement(sa, sa, new TextGraphic("| " + message, Font.BASICFONT).getSprite(), false, 57, 127));

        menuCursor.setElementIndex(0);
        MenuCursor.setActiveMenu(settingsMenu);
    }
    
    public static void drawNoticeMenu(String message, Menu lastMenu, int lastIndex) {
        if (noticeMenu != null) {
            noticeMenu.removeMenu();
        }
        String[] lines = Menu.wrapText(message, 20);
        noticeMenu = new Menu(114, Math.min(GAME_HEIGHT, (6 * (lines.length + 1)) + 14), 63, Math.max(0, (GAME_HEIGHT - ((6 * (lines.length + 1)) + 14)) / 2));
        
        final Menu backMenu = lastMenu;
        final int backIndex = lastIndex;
        
        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing
            }
        };
        
        MenuAction back = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
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
            noticeMenu.addElement(new MenuElement(sa, sa, new TextGraphic(lines[i], Font.BASICFONT).getSprite(), false, 7, 7 + (6 * i)));
        }
        noticeMenu.addElement(new MenuElement(sa, back, new TextGraphic("CLOSE", Font.BASICFONT).getSprite(), true, 42, 13 + (6 * lines.length)));
        
        menuCursor.setElementIndex(lines.length);
        MenuCursor.setActiveMenu(noticeMenu);
    }

    public Renderer getRenderer() {
        return renderer;
    }
    
    public static int getGameScale() {
        return gameScale;
    }

    public static boolean isDrawFactionShadow() {
        return drawFactionShadow;
    }

    public static int getGridOpacity() {
        return gridOpacity;
    }
}
