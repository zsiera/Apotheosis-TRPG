package fer;

import fer.ai.AIPlayer;
import fer.ai.PathFinder;
import fer.gameplay.MapGoal;
import fer.gameplay.Weapon;
import fer.graphics.SpriteSheet;
import fer.graphics.Sprite;
import fer.ui.Font;
import fer.ui.Menu;
import fer.ui.MenuAction;
import fer.ui.MenuCursor;
import fer.ui.MenuElement;
import fer.ui.TextGraphic;
import java.util.ArrayList;

/**
 * @author Evan Stewart
 *
 * A singleton class that stores the cursor location on maps and menus, as well
 * as other properties, resources, and methods related to the cursor.
 */
public class Cursor {

    /*Number of tiles away from the edge that the cursor will scroll the map
     at.*/
    public static final int SCROLL_AREA = 3;
    public static final int CURSOR_COLOR = 0xFFFF00;
    private static Cursor instance;
    private MenuCursor menuCursor;
    private Sprite cursorSprite;
    private Sprite moveOverlaySprite;
    private Sprite attackOverlaySprite;
    private Unit selectedUnit;
    private Menu unitMenu;
    private Menu terrainMenu;
    private Menu turnMenu;
    private int mapx = 0;
    private int mapy = 0;
    private int mapScrollx = 0;
    private int mapScrolly = 0;
    private boolean movingUnit;
    private boolean attacking;
    private boolean showingMoveArrow = false;
    private PathFinder pathFinder;
    private ArrayList<Tile> moveableTiles;
    private ArrayList<Tile> attackableTiles;
    private ArrayList<Tile> arrowPath;
    private boolean active;
    private boolean visible;
    private int currentFactionTurn;
    private int goalMenuFaction = 0;

    public Cursor() {
        cursorSprite = new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 0, 0,
                SpriteSheet.CURSOR);
        moveOverlaySprite = new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 1,
                SpriteSheet.MAPOVERLAY);
        attackOverlaySprite = new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18,
                1, SpriteSheet.MAPOVERLAY);
        pathFinder = new PathFinder();
        menuCursor = MenuCursor.getMenuCursor();
        active = false;
        visible = true;
    }

    public static Cursor getCursor() {
        if (instance == null) {
            instance = new Cursor();
        }
        return instance;
    }

    public void update(Map currentMap, int xQueue, int yQueue, boolean enter,
            boolean escape, boolean tab) {
        if (Game.getCurrentMap().getPlayer(currentFactionTurn) == null) {
            if (unitMenu != null) {
                Game.getMenuList().remove(unitMenu);
            }
            if (terrainMenu != null) {
                Game.getMenuList().remove(terrainMenu);
            }
            if (turnMenu != null) {
                Game.getMenuList().remove(turnMenu);
            }
            mapx += xQueue;
            mapy += yQueue;
            //Make sure new coordinates are legal.
            if (mapx >= currentMap.getWidth()) {
                mapx = currentMap.getWidth() - 1;
            } else if (mapx < 0) {
                mapx = 0;
            }
            if (mapy >= currentMap.getHeight()) {
                mapy = currentMap.getHeight() - 1;
            } else if (mapy < 0) {
                mapy = 0;
            }
            //Update the map scroll if the cursor is near the edge of the map.
            int rightEdge = mapScrollx + (Map.MIN_MAP_WIDTH - 1);
            int bottomEdge = mapScrolly + (Map.MIN_MAP_HEIGHT - 1);
            if ((mapx < (mapScrollx + SCROLL_AREA)) && (mapx >= 0 + SCROLL_AREA)) {
                mapScrollx--;
            } else if ((mapx > (rightEdge - SCROLL_AREA))
                    && (mapx < (currentMap.getWidth() - (SCROLL_AREA)))) {
                mapScrollx++;
            }
            if ((mapy < (mapScrolly + SCROLL_AREA)) && (mapy >= 0 + SCROLL_AREA)) {
                mapScrolly--;
            } else if ((mapy > (bottomEdge - SCROLL_AREA))
                    && (mapy < (currentMap.getHeight() - (SCROLL_AREA)))) {
                mapScrolly++;
            }
            if (!movingUnit && !attacking) {
                drawTurnMenu();
                Unit newSelectedUnit = Game.getCurrentMap().getSelectedUnit();
                if (newSelectedUnit != selectedUnit && selectedUnit != null) {
                    selectedUnit.setActiveMapAnimation(0);
                    selectedUnit.resetAnimation(1);
                }
                if (newSelectedUnit != null) {
                    if (newSelectedUnit.getFaction() == currentFactionTurn && !newSelectedUnit.hasMoved()) {
                        newSelectedUnit.setActiveMapAnimation(1);
                    }
                    drawUnitMenu(newSelectedUnit);
                }
                selectedUnit = newSelectedUnit;
                drawTerrainMenu(currentMap.getTile(mapx + mapy * currentMap.getWidth()));
                if (enter) {
                    if (selectedUnit != null && !selectedUnit.isDead() && !selectedUnit.hasMoved()) {
                        if (selectedUnit.getFaction() == currentFactionTurn) {
                            selectedUnit.setActiveMapAnimation(3);
                            moveableTiles = pathFinder.getMovableTiles(selectedUnit, currentMap);
                            movingUnit = true;
                        }
                    } else {
                        drawMapMenu();
                    }
                } else if (tab) {
                    if (selectedUnit != null) {
                        menuCursor.setActive(true);
                        this.setActive(false);
                        selectedUnit.drawStatusMenu(null, false);
                    }
                }
            } else if (attacking) {
                if (escape) {
                    attacking = false;
                    MenuCursor.getActiveMenu().setVisible(true);
                    menuCursor.setActive(true);
                    setActive(false);
                } else if (enter) {
                    if (attackableTiles != null) {
                        if (attackableTiles.contains(Game.getCurrentMap().getTile(
                                mapx + mapy * Game.getCurrentMap().getWidth()))
                                && Game.getCurrentMap().getUnitTile(mapx + mapy * Game.
                                getCurrentMap().getWidth()) != null) {
                            if (Game.getCurrentMap().getUnitTile(mapx + mapy * Game.
                                    getCurrentMap().getWidth()).getFaction()
                                    != selectedUnit.getFaction()) {
                                int oldx = mapx;
                                int oldy = mapy;
                                mapx -= (oldx - selectedUnit.getMapx()) / 2;
                                mapy -= (oldy - selectedUnit.getMapy()) / 2;
                                centerCursor();
                                MenuCursor.getActiveMenu().removeMenu();
                                attacking = false;
                                Game.getBattleProcessor().startBattle(selectedUnit,
                                        Game.getCurrentMap().getUnitTile(oldx + oldy
                                        * Game.getCurrentMap().getWidth()));
                            }
                        }
                    }
                }
            } else {
                if (escape) {
                    movingUnit = false;
                    showingMoveArrow = false;
                    selectedUnit.setActiveMapAnimation(0);
                } else if (enter) {
                    if (moveableTiles.contains(
                            Game.getCurrentMap().getTile(mapx + mapy * Game.
                            getCurrentMap().getWidth())) && (Game.getCurrentMap().
                            getUnitTile(mapx + mapy * Game.getCurrentMap().getWidth())
                            == null) || Game.getCurrentMap().getUnitTile(mapx + mapy
                            * Game.getCurrentMap().getWidth()) == selectedUnit) {
                        if (Game.getCurrentMap().getUnitTile(mapx + mapy * Game.getCurrentMap().getWidth()) == selectedUnit) {
                            movingUnit = false;
                            showingMoveArrow = false;
                            drawActionMenu(mapx, mapy);
                        } else {
                            drawMovementMenu();
                        }
                    }
                } else if ((mapx != selectedUnit.getMapx() || mapy
                        != selectedUnit.getMapy()) && moveableTiles.contains(currentMap
                        .getSelectedTile())) {
                    arrowPath = pathFinder.getShortestPathAStar(currentMap,
                            selectedUnit, currentMap.getTile(selectedUnit.getMapx()
                            + selectedUnit.getMapy() * currentMap.getWidth()),
                            currentMap.getSelectedTile());
                    showingMoveArrow = true;
                } else {
                    showingMoveArrow = false;
                }
            }
        } else {
            if (unitMenu != null) {
                unitMenu.removeMenu();
                unitMenu = null;
            }
            if (terrainMenu != null) {
                terrainMenu.removeMenu();
                terrainMenu = null;
            }
            if (!Game.getBattleProcessor().isInCombat()) {
                Game.getCurrentMap().getPlayer(currentFactionTurn).update();
            }
        }
    }

    public void centerCursor() {
        int xdif = (mapx - mapScrollx) - 8;
        int ydif = (mapy - mapScrolly) - 5;
        if (xdif < 0) {
            mapScrollx = Math.max((mapScrollx + xdif), 0);
        } else {
            mapScrollx = Math.min((mapScrollx + xdif), Game.getCurrentMap().getWidth() - Map.MIN_MAP_WIDTH);
        }
        if (ydif < 0) {
            mapScrolly = Math.max((mapScrolly + ydif), 0);
        } else {
            mapScrolly = Math.min((mapScrolly + ydif), Game.getCurrentMap().getHeight() - Map.MIN_MAP_HEIGHT);
        }
    }

    /**
     * Updates the current active faction and resets unit movement. If the
     * current faction is the last faction on the map, a new turn is begun.
     */
    public void endTurn() {
        System.out.println(Game.getCurrentMap().getNumFactions());
        currentFactionTurn++;
        if (currentFactionTurn >= Game.getCurrentMap().getNumFactions()) {
            currentFactionTurn = 0;
            Game.getCurrentMap().setCurrentTurn(Game.getCurrentMap().getCurrentTurn() + 1);
        }
        Game.getCurrentMap().resetUnitMovement();
        if (Game.getCurrentMap().getPlayer(currentFactionTurn) != null) {
            Game.getCurrentMap().getPlayer(currentFactionTurn).startTurn();
        }
    }

    /**
     * Calls the map to check if any faction has met their map goal, then calls
     * the victory menu and ends map processing. TODO: Add functionality for
     * other gamemodes, such as victory dialogue for campaign.
     */
    public void processVictory() {
        int faction = Game.getCurrentMap().checkVictory();
        if (faction != -1) {
            drawVictoryScreen(faction);
            setActive(false);
            menuCursor.setActive(true);
        }
    }

    public int getMapX() {
        return mapx;
    }

    public int getMapY() {
        return mapy;
    }

    public void setMapLocation(int x, int y) {
        mapx = x;
        mapy = y;
    }

    public int getMapScrollx() {
        return mapScrollx;
    }

    public int getMapScrolly() {
        return mapScrolly;
    }

    public void setMapScroll(int x, int y) {
        mapScrollx = x;
        mapScrolly = y;
    }

    public Sprite getCursorSprite() {
        return cursorSprite;
    }

    public Sprite getMoveOverlaySprite() {
        return moveOverlaySprite;
    }

    public Sprite getAttackOverlaySprite() {
        return attackOverlaySprite;
    }

    public void resetCursor() {
        mapx = 0;
        mapy = 0;
        mapScrollx = 0;
        mapScrolly = 0;
        currentFactionTurn = 0;
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public boolean movingUnit() {
        return movingUnit;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public ArrayList<Tile> getMoveableTiles() {
        return moveableTiles;
    }

    public ArrayList<Tile> getAttackableTiles() {
        return attackableTiles;
    }

    public boolean showingMoveArrow() {
        return showingMoveArrow;
    }

    public void setShowingMoveArrow(boolean showing) {
        showingMoveArrow = showing;
    }

    public void setArrowPath(ArrayList<Tile> arrowPath) {
        this.arrowPath = arrowPath;
    }

    public ArrayList<Tile> getArrowPath() {
        return arrowPath;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean iActive) {
        active = iActive;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void drawMovementMenu() {
        Menu movementMenu;
        if (mapy < 2 && mapx > Game.getCurrentMap().getWidth() - 4) {
            movementMenu = new Menu(42, 26, (mapx - mapScrollx) * 16 - 42,
                    (mapy - mapScrolly) * 16);
        } else if (mapy < 2) {
            movementMenu = new Menu(42, 26, (mapx - mapScrollx) * 16 + 16,
                    (mapy - mapScrolly) * 16);
        } else if (mapx > Game.getCurrentMap().getWidth() - 4) {
            movementMenu = new Menu(42, 26, (mapx - mapScrollx) * 16 - 42,
                    (mapy - mapScrolly) * 16 - 26);
        } else {
            movementMenu = new Menu(42, 26, (mapx - mapScrollx) * 16 + 16,
                    (mapy - mapScrolly) * 16 - 26);
        }
        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };
        MenuAction move = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                int oldx = selectedUnit.getMapx();
                int oldy = selectedUnit.getMapy();
                selectedUnit.setMapx(mapx);
                selectedUnit.setMapy(mapy);
                movingUnit = false;
                showingMoveArrow = false;
                //selectedUnit.setActiveMapAnimation(0);
                MenuCursor.getActiveMenu().removeMenu();
                //menuCursor.setActive(false);
                //setActive(true);
                drawActionMenu(oldx, oldy);
            }
        };
        MenuAction cancel = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                MenuCursor.getActiveMenu().removeMenu();
                menuCursor.setActive(false);
                setActive(true);
            }
        };
        movementMenu.setEscapeAction(cancel);
        movementMenu.addElement(new MenuElement(sa, move, (new TextGraphic("MOVE", Font.BASICFONT)).getSprite(), true, 6, 7));
        movementMenu.addElement(new MenuElement(sa, cancel, (new TextGraphic("CANCEL", Font.BASICFONT)).getSprite(), true, 6, 13));
        setActive(false);
        menuCursor.setActive(true);
        MenuCursor.setActiveMenu(movementMenu);
        menuCursor.setElementIndex(0);
    }

    public void drawActionMenu(int oldx, int oldy) {
        Menu actionMenu;
        final int fOldx = oldx;
        final int fOldy = oldy;
        if (mapy < 2 && mapx > Game.getCurrentMap().getWidth() - 4) {
            actionMenu = new Menu(42, 38, (mapx - mapScrollx) * 16 - 42,
                    (mapy - mapScrolly) * 16);
        } else if (mapy < 2) {
            actionMenu = new Menu(42, 38, (mapx - mapScrollx) * 16 + 16,
                    (mapy - mapScrolly) * 16);
        } else if (mapx > Game.getCurrentMap().getWidth() - 4) {
            actionMenu = new Menu(42, 38, (mapx - mapScrollx) * 16 - 42,
                    (mapy - mapScrolly) * 16 - 26);
        } else {
            actionMenu = new Menu(42, 38, (mapx - mapScrollx) * 16 + 16,
                    (mapy - mapScrolly) * 16 - 26);
        }
        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };
        MenuAction attack = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                MenuCursor.getActiveMenu().setVisible(false);
                menuCursor.setActive(false);
                setActive(true);

                attacking = true;
                attackableTiles = pathFinder.getAttackableTiles(selectedUnit, selectedUnit.getMapx(), selectedUnit.getMapy(), Game.getCurrentMap(), 0);
            }
        };
        MenuAction items = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                MenuCursor.getActiveMenu().removeMenu();
                drawItemsMenu(fOldx, fOldy);
            }
        };
        MenuAction wait = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                selectedUnit.setMoved(true);
                selectedUnit.setActiveMapAnimation(0);
                MenuCursor.getActiveMenu().removeMenu();
                menuCursor.setActive(false);
                setActive(true);
            }
        };
        MenuAction back = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                if (selectedUnit.getMapx() != fOldx && selectedUnit.getMapy()
                        != fOldy) {
                    showingMoveArrow = true;
                }
                selectedUnit.setMapx(fOldx);
                selectedUnit.setMapy(fOldy);
                movingUnit = true;

                MenuCursor.getActiveMenu().removeMenu();
                menuCursor.setActive(false);
                setActive(true);
            }
        };
        actionMenu.setEscapeAction(back);
        actionMenu.addElement(new MenuElement(sa, attack, (new TextGraphic("ATTACK", Font.BASICFONT)).getSprite(), true, 6, 7));
        actionMenu.addElement(new MenuElement(sa, items, (new TextGraphic("ITEMS", Font.BASICFONT)).getSprite(), true, 6, 13));
        actionMenu.addElement(new MenuElement(sa, wait, (new TextGraphic("WAIT", Font.BASICFONT)).getSprite(), true, 6, 19));
        actionMenu.addElement(new MenuElement(sa, back, (new TextGraphic("BACK", Font.BASICFONT)).getSprite(), true, 6, 25));
        setActive(false);
        menuCursor.setActive(true);
        MenuCursor.setActiveMenu(actionMenu);
        menuCursor.setElementIndex(0);
    }

    public void drawMapMenu() {
        Menu mapMenu;
        if (mapy < 2 && mapx > Game.getCurrentMap().getWidth() - 4) {
            mapMenu = new Menu(54, 38, (mapx - mapScrollx) * 16 - 47,
                    (mapy - mapScrolly) * 16);
        } else if (mapy < 2) {
            mapMenu = new Menu(54, 38, (mapx - mapScrollx) * 16 + 16,
                    (mapy - mapScrolly) * 16);
        } else if (mapx > Game.getCurrentMap().getWidth() - 5) {
            mapMenu = new Menu(54, 38, (mapx - mapScrollx) * 16 - 47,
                    (mapy - mapScrolly) * 16 - 26);
        } else {
            mapMenu = new Menu(54, 38, (mapx - mapScrollx) * 16 + 16,
                    (mapy - mapScrolly) * 16 - 26);
        }
        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };
        MenuAction goals = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                MenuCursor.getActiveMenu().removeMenu();
                drawGoalMenu();
            }
        };
        MenuAction endTurn = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                endTurn();
                MenuCursor.getActiveMenu().removeMenu();
                menuCursor.setActive(false);
                setActive(true);
            }
        };
        MenuAction options = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                MenuCursor.getActiveMenu().removeMenu();
                drawOptionsMenu();
            }
        };
        MenuAction cancel = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                MenuCursor.getActiveMenu().removeMenu();
                menuCursor.setActive(false);
                setActive(true);
            }
        };
        mapMenu.setEscapeAction(cancel);
        mapMenu.addElement(new MenuElement(sa, endTurn, (new TextGraphic("END TURN", Font.BASICFONT)).getSprite(), true, 6, 7));
        mapMenu.addElement(new MenuElement(sa, goals, (new TextGraphic("GOALS", Font.BASICFONT)).getSprite(), true, 6, 13));
        mapMenu.addElement(new MenuElement(sa, options, (new TextGraphic("OPTIONS", Font.BASICFONT)).getSprite(), true, 6, 19));
        mapMenu.addElement(new MenuElement(sa, cancel, (new TextGraphic("CANCEL", Font.BASICFONT)).getSprite(), true, 6, 25));
        setActive(false);
        menuCursor.setActive(true);
        MenuCursor.setActiveMenu(mapMenu);
        menuCursor.setElementIndex(0);
    }

    public void drawOptionsMenu() {
        final Menu optionsMenu;
        if (mapy < 2 && mapx > Game.getCurrentMap().getWidth() - 5) {
            optionsMenu = new Menu(57, 38, (mapx - mapScrollx) * 16 - 57,
                    (mapy - mapScrolly) * 16);
        } else if (mapy < 2) {
            optionsMenu = new Menu(57, 38, (mapx - mapScrollx) * 16 + 16,
                    (mapy - mapScrolly) * 16);
        } else if (mapx > Game.getCurrentMap().getWidth() - 5) {
            optionsMenu = new Menu(57, 38, (mapx - mapScrollx) * 16 - 57,
                    (mapy - mapScrolly) * 16 - 26);
        } else {
            optionsMenu = new Menu(57, 38, (mapx - mapScrollx) * 16 + 16,
                    (mapy - mapScrolly) * 16 - 26);
        }
        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };
        MenuAction settings = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                Game.drawSettingsMenu(optionsMenu, menuCursor.getElementIndex(), "");
            }
        };
        MenuAction toTitle = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                MenuCursor.getActiveMenu().removeMenu();
                terrainMenu.removeMenu();
                turnMenu.removeMenu();
                Game.setOnTitle(true);
                Game.drawTitleMenu();
                active = false;
                setMapLocation(0, 0);
                setMapScroll(0, 0);
                menuCursor.setActive(true);
                while (Game.getGame().getRenderer().isDrawingMap()) {
                }
                Game.setCurrentMap(null);
            }
        };
        MenuAction exitGame = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                Game.getGame().stopSequence();
            }
        };
        MenuAction cancel = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                MenuCursor.getActiveMenu().removeMenu();
                drawMapMenu();
            }
        };
        optionsMenu.setEscapeAction(cancel);
        optionsMenu.addElement(new MenuElement(sa, settings, (new TextGraphic("SETTINGS", Font.BASICFONT)).getSprite(), true, 6, 7));
        optionsMenu.addElement(new MenuElement(sa, toTitle, (new TextGraphic("TO TITLE", Font.BASICFONT)).getSprite(), true, 6, 13));
        optionsMenu.addElement(new MenuElement(sa, exitGame, (new TextGraphic("EXIT GAME", Font.BASICFONT)).getSprite(), true, 6, 19));
        optionsMenu.addElement(new MenuElement(sa, cancel, (new TextGraphic("CANCEL", Font.BASICFONT)).getSprite(), true, 6, 25));
        setActive(false);
        menuCursor.setActive(true);
        MenuCursor.setActiveMenu(optionsMenu);
        menuCursor.setElementIndex(0);
    }

    public void drawUnitMenu(Unit unit) {
        if (mapx - mapScrollx > Map.MIN_MAP_WIDTH / 2) {
            unitMenu = new Menu(74, 46, 0, 0);
        } else {
            unitMenu = new Menu(74, 46, 166, 0);
        }
        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };
        unitMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(unit.getName(), Font.BASICFONT)).getSprite(), false, 38, 7));
        unitMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("LVL:" + unit.getLevel(), Font.BASICFONT)).getSprite(), false, 38, 13));
        unitMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("HP:", Font.BASICFONT)).getSprite(), false, 38, 23));
        unitMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("" + unit.getCurrentHp(), Font.BASICFONT)).getSprite(), false, 58, 23));
        unitMenu.addElement(new MenuElement(sa, sa, (new Sprite(30, 10, 1, 1, SpriteSheet.HEALTHBAR)), false, 38, 29));
        unitMenu.addElement(new MenuElement(sa, sa, (new Sprite((int) ((30 * unit.getCurrentHp()) / unit.getHp()), 10, 32, 1, SpriteSheet.HEALTHBAR)), false, 38, 29));
        unitMenu.addElement(new MenuElement(sa, sa, (new Sprite(30, 10, 63, 1, SpriteSheet.HEALTHBAR)), false, 38, 29));
        if (unit.getMapFaceSprite() != null) {
            unitMenu.addElement(new MenuElement(sa, sa, unit.getMapFaceSprite(), false, 6, 7));
        }
    }

    public void drawTerrainMenu(Tile tile) {
        if (mapx - mapScrollx > Map.MIN_MAP_WIDTH / 2) {
            terrainMenu = new Menu(62, 42, 0, 118);
        } else {
            terrainMenu = new Menu(62, 42, 178, 118);
        }
        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };
        terrainMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(tile.getName(), Font.BASICFONT)).getSprite(), false, 6, 7));
        terrainMenu.addElement(new MenuElement(sa, sa, tile.getSprite(), false, 39, 19));
        terrainMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("X:" + tile.getMapX(), Font.BASICFONT)).getSprite(), false, 6, 13));
        terrainMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("Y:" + tile.getMapY(), Font.BASICFONT)).getSprite(), false, 31, 13));
        terrainMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("DEF:" + (tile.isAttackable() ? tile.getDef() : "--"), Font.BASICFONT)).getSprite(), false, 6, 19));
        terrainMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("AVO:" + (tile.isAttackable() ? tile.getAvo() : "--"), Font.BASICFONT)).getSprite(), false, 6, 25));
    }

    public void drawTurnMenu() {
        if (mapy - mapScrolly > Map.MIN_MAP_HEIGHT / 2) {
            turnMenu = new Menu(80, 26, 80, 0);
        } else {
            turnMenu = new Menu(80, 26, 80, 134);
        }
        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };
        turnMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("TRN:" + Game.getCurrentMap().getCurrentTurn(), Font.BASICFONT)).getSprite(), false, 6, 7));
        turnMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("FCT:" + currentFactionTurn, Font.BASICFONT)).getSprite(), false, 41, 7));
        turnMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(Game.getCurrentMap().getFactionGoals()[currentFactionTurn].getType().getName(), Font.BASICFONT)).getSprite(), false, 6, 13));
    }

    public void drawItemsMenu(int oldx, int oldy) {
        Menu itemsMenu = new Menu(183, 100, 28, 30);
        final int fOldx = oldx;
        final int fOldy = oldy;
        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing.
            }
        };
        MenuAction back = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                MenuCursor.getActiveMenu().removeMenu();
                drawActionMenu(fOldx, fOldy);
            }
        };
        MenuAction equipWeapon = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                Weapon temp = selectedUnit.getWeapon((caller.getY() - 7) / 16);
                selectedUnit.setWeapon((caller.getY() - 7) / 16, selectedUnit.getWeapon(0));
                selectedUnit.setWeapon(0, temp);
                MenuCursor.getActiveMenu().removeMenu();
                drawItemsMenu(fOldx, fOldy);
            }
        };
        MenuAction useItem = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                selectedUnit.getItem((caller.getY() - 7) / 16).getUsedAction().execute(caller);
                if (selectedUnit.getItem((caller.getY() - 7) / 16).isConsumable()) {
                    selectedUnit.getItem((caller.getY() - 7) / 16).
                            setUses(selectedUnit.getItem((caller.getY() - 7)
                            / 16).getUses() - 1);
                    if (selectedUnit.getItem((caller.getY() - 7) / 16).getUses() <= 0) {
                        selectedUnit.setItem((caller.getY() - 7) / 16, null);
                        MenuCursor.getActiveMenu().removeMenu();
                        drawItemsMenu(fOldx, fOldy);
                    }
                }
                selectedUnit.setMoved(true);
                selectedUnit.setActiveMapAnimation(0);
                MenuCursor.getActiveMenu().removeMenu();
                menuCursor.setActive(false);
                setActive(true);
            }
        };
        itemsMenu.setEscapeAction(back);
        try {
            itemsMenu.addElement(new MenuElement(sa, sa, selectedUnit.getWeapon(0).getIcon(), false, 7, 7));
            itemsMenu.addElement(new MenuElement(sa, equipWeapon, (new TextGraphic(
                    selectedUnit.getWeapon(0).getName(), Font.BASICFONT)).
                    getSprite(), true, 23, 12));
            itemsMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(""
                    + selectedUnit.getWeapon(0).getUses(), Font.BASICFONT)).
                    getSprite(), false, 78, 12));
        } catch (NullPointerException e) {
        }
        try {
            itemsMenu.addElement(new MenuElement(sa, sa, selectedUnit.getWeapon(1).getIcon(), false, 7, 23));
            itemsMenu.addElement(new MenuElement(sa, equipWeapon, (new TextGraphic(
                    selectedUnit.getWeapon(1).getName(), Font.BASICFONT)).
                    getSprite(), true, 23, 28));
            itemsMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(""
                    + selectedUnit.getWeapon(1).getUses(), Font.BASICFONT)).
                    getSprite(), false, 78, 28));
        } catch (NullPointerException e) {
        }
        try {
            itemsMenu.addElement(new MenuElement(sa, sa, selectedUnit.getWeapon(2).getIcon(), false, 7, 39));
            itemsMenu.addElement(new MenuElement(sa, equipWeapon, (new TextGraphic(
                    selectedUnit.getWeapon(2).getName(), Font.BASICFONT)).
                    getSprite(), true, 23, 44));
            itemsMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(""
                    + selectedUnit.getWeapon(2).getUses(), Font.BASICFONT)).
                    getSprite(), false, 78, 44));
        } catch (NullPointerException e) {
        }
        try {
            itemsMenu.addElement(new MenuElement(sa, sa, selectedUnit.getWeapon(3).getIcon(), false, 7, 55));
            itemsMenu.addElement(new MenuElement(sa, equipWeapon, (new TextGraphic(
                    selectedUnit.getWeapon(3).getName(), Font.BASICFONT)).
                    getSprite(), true, 23, 60));
            itemsMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(""
                    + selectedUnit.getWeapon(3).getUses(), Font.BASICFONT)).
                    getSprite(), false, 78, 60));
        } catch (NullPointerException e) {
        }
        try {
            itemsMenu.addElement(new MenuElement(sa, sa, selectedUnit.getWeapon(4).getIcon(), false, 7, 71));
            itemsMenu.addElement(new MenuElement(sa, equipWeapon, (new TextGraphic(
                    selectedUnit.getWeapon(4).getName(), Font.BASICFONT)).
                    getSprite(), true, 23, 76));
            itemsMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(""
                    + selectedUnit.getWeapon(4).getUses(), Font.BASICFONT)).
                    getSprite(), false, 78, 76));
        } catch (NullPointerException e) {
        }
        try {
            itemsMenu.addElement(new MenuElement(sa, sa, selectedUnit.getItem(0).getIcon(), false, 99, 7));
            itemsMenu.addElement(new MenuElement(sa, useItem, (new TextGraphic(
                    selectedUnit.getItem(0).getName(), Font.BASICFONT)).
                    getSprite(), true, 115, 12));
            itemsMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(""
                    + selectedUnit.getItem(0).getUses(), Font.BASICFONT)).
                    getSprite(), false, 170, 12));
        } catch (NullPointerException e) {
        }
        try {
            itemsMenu.addElement(new MenuElement(sa, sa, selectedUnit.getItem(1).getIcon(), false, 99, 23));
            itemsMenu.addElement(new MenuElement(sa, useItem, (new TextGraphic(
                    selectedUnit.getItem(1).getName(), Font.BASICFONT)).
                    getSprite(), true, 115, 28));
            itemsMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(""
                    + selectedUnit.getItem(0).getUses(), Font.BASICFONT)).
                    getSprite(), false, 170, 28));
        } catch (NullPointerException e) {
        }
        try {
            itemsMenu.addElement(new MenuElement(sa, sa, selectedUnit.getItem(2).getIcon(), false, 99, 39));
            itemsMenu.addElement(new MenuElement(sa, useItem, (new TextGraphic(
                    selectedUnit.getItem(2).getName(), Font.BASICFONT)).
                    getSprite(), true, 115, 44));
            itemsMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(""
                    + selectedUnit.getItem(0).getUses(), Font.BASICFONT)).
                    getSprite(), false, 170, 44));
        } catch (NullPointerException e) {
        }
        try {
            itemsMenu.addElement(new MenuElement(sa, sa, selectedUnit.getItem(3).getIcon(), false, 99, 55));
            itemsMenu.addElement(new MenuElement(sa, useItem, (new TextGraphic(
                    selectedUnit.getItem(3).getName(), Font.BASICFONT)).
                    getSprite(), true, 115, 60));
            itemsMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(""
                    + selectedUnit.getItem(0).getUses(), Font.BASICFONT)).
                    getSprite(), false, 170, 60));
        } catch (NullPointerException e) {
        }
        try {
            itemsMenu.addElement(new MenuElement(sa, sa, selectedUnit.getItem(4).getIcon(), false, 99, 71));
            itemsMenu.addElement(new MenuElement(sa, useItem, (new TextGraphic(
                    selectedUnit.getItem(4).getName(), Font.BASICFONT)).
                    getSprite(), true, 115, 76));
            itemsMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(""
                    + selectedUnit.getItem(4).getUses(), Font.BASICFONT)).
                    getSprite(), false, 170, 76));
        } catch (NullPointerException e) {
        }
        itemsMenu.addElement(new MenuElement(sa, sa, new Sprite(1, 80, 0x333324, 0), false, 91, 7));
        itemsMenu.addElement(new MenuElement(sa, back, (new TextGraphic("BACK", Font.BASICFONT)).getSprite(), true, 146, 91));
        setActive(false);
        menuCursor.setActive(true);
        MenuCursor.setActiveMenu(itemsMenu);
        menuCursor.setElementIndex(1);
    }

    public void drawStatusWindowStats() {
        Menu statusMenuStats = new Menu(220, 140, 10, 10);

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing
            }
        };
        MenuAction exit = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                MenuCursor.getActiveMenu().removeMenu();
            }
        };
        statusMenuStats.setEscapeAction(exit);

        MenuCursor.setActiveMenu(statusMenuStats);
    }

    public void drawStatusWindowInventory() {
    }

    public void drawGoalMenu() {
        Menu goalMenu = new Menu(100, 100, 70, 30);

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing
            }
        };
        MenuAction exit = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                goalMenuFaction = 0;
                MenuCursor.getActiveMenu().removeMenu();
                menuCursor.setActive(false);
                setActive(true);
            }
        };
        MenuAction left = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                goalMenuFaction--;
                if (goalMenuFaction < 0) {
                    goalMenuFaction = Game.getCurrentMap().getNumFactions() - 1;
                }
                MenuCursor.getActiveMenu().removeMenu();
                drawGoalMenu();
            }
        };
        MenuAction right = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                goalMenuFaction++;
                if (goalMenuFaction >= Game.getCurrentMap().getNumFactions()) {
                    goalMenuFaction = 0;
                }
                MenuCursor.getActiveMenu().removeMenu();
                drawGoalMenu();
            }
        };
        goalMenu.setLeftAction(left);
        goalMenu.setRightAction(right);
        goalMenu.setEscapeAction(exit);

        goalMenu.addElement(new MenuElement(sa, exit, (new TextGraphic("BACK", Font.BASICFONT)).getSprite(), true, 6, 87));
        goalMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("FACTION " + goalMenuFaction + ":", Font.BASICFONT)).getSprite(), false, 6, 7));
        goalMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(Game.getCurrentMap().getFactionGoals()[currentFactionTurn].getType().getName(), Font.BASICFONT)).getSprite(), false, 6, 13));
        if (Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getType() == MapGoal.GoalType.DEFEND_TARGETS) {
            goalMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("REMAINING TURNS: " + Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTurns(), Font.BASICFONT)).getSprite(), false, 6, 7));
            for (int i = 0; i < Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTargets().length; i++) {
                goalMenu.addElement(new MenuElement(sa, sa, new TextGraphic(Game.getCurrentMap().getUnit(Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTargets()[i]).getName(), Font.BASICFONT).getSprite(), false, 6, 25 + 6 * i));
            }
        } else if (Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getType() == MapGoal.GoalType.ELIMINATE_TARGETS) {
            for (int i = 0; i < Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTargets().length; i++) {
                goalMenu.addElement(new MenuElement(sa, sa, new TextGraphic(Game.getCurrentMap().getUnit(Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTargets()[i]).getName(), Font.BASICFONT).getSprite(), false, 6, 19 + 6 * i));
            }

        } else if (Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getType() == MapGoal.GoalType.REACH_TILE) {
            goalMenu.addElement(new MenuElement(sa, sa, Game.getCurrentMap().getTile(Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTileX() + Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTileY() * Game.getCurrentMap().getWidth()).getSprite(), false, 78, 19));
            goalMenu.addElement(new MenuElement(sa, sa, (new TextGraphic(Game.getCurrentMap().getTile(Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTileX() + Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTileY() * Game.getCurrentMap().getWidth()).getName(), Font.BASICFONT)).getSprite(), false, 6, 19));
            goalMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("X " + Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTileX(), Font.BASICFONT)).getSprite(), false, 6, 25));
            goalMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("Y " + Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTileY(), Font.BASICFONT)).getSprite(), false, 31, 25));
            for (int i = 0; i < Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTargets().length; i++) {
                goalMenu.addElement(new MenuElement(sa, sa, new TextGraphic(Game.getCurrentMap().getUnit(Game.getCurrentMap().getFactionGoals()[goalMenuFaction].getTargets()[i]).getName(), Font.BASICFONT).getSprite(), false, 6, 36 + 6 * i));
            }
        } else { //Route enemy
            int remaining = 0;
            for (int i = 0; i < Game.getCurrentMap().getUnits().length; i++) {
                if (!Game.getCurrentMap().getUnit(i).isDead() && Game.getCurrentMap().getUnit(i).getFaction() != goalMenuFaction) {
                    remaining++;
                }
            }
            goalMenu.addElement(new MenuElement(sa, sa, (new TextGraphic("REMAINING: " + remaining, Font.BASICFONT)).getSprite(), false, 6, 19));
        }

        MenuCursor.setActiveMenu(goalMenu);
        menuCursor.setElementIndex(0);
    }

    public void drawVictoryScreen(int faction) {
        Menu victoryMenu = new Menu(220, 140, 10, 10);

        MenuAction sa = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                //Do nothing
            }
        };
        MenuAction toTitle = new MenuAction() {
            @Override
            public void execute(MenuElement caller) {
                MenuCursor.getActiveMenu().removeMenu();
                if (terrainMenu != null) {
                    terrainMenu.removeMenu();
                }
                if (turnMenu != null) {
                    turnMenu.removeMenu();
                }
                Game.setOnTitle(true);
                Game.drawTitleMenu();
                active = false;
                menuCursor.setActive(true);
                while (Game.getGame().getRenderer().isDrawingMap()) {
                }
                Game.setCurrentMap(null);
            }
        };

        victoryMenu.addElement(new MenuElement(sa, toTitle, new TextGraphic("EXIT", Font.BASICFONT).getSprite(), true, 110, 128));

        victoryMenu.addElement(new MenuElement(sa, sa, new TextGraphic("BATTLE RESULTS", Font.BASICFONT).getSprite(), false, 75, 7));
        victoryMenu.addElement(new MenuElement(sa, sa, new TextGraphic("WINNING FACTION: " + faction, Font.BASICFONT).getSprite(), false, 65, 13));
        victoryMenu.addElement(new MenuElement(sa, sa, new TextGraphic("GOAL: " + Game.getCurrentMap().getFactionGoals()[faction].getType().getName(), Font.BASICFONT).getSprite(), false, 6, 19));
        victoryMenu.addElement(new MenuElement(sa, sa, new TextGraphic("TURNS: " + Game.getCurrentMap().getCurrentTurn(), Font.BASICFONT).getSprite(), false, 144, 19));

        MenuCursor.setActiveMenu(victoryMenu);
    }
}
