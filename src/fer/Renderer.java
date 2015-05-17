package fer;

import fer.graphics.Effect;
import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;
import fer.ui.Menu;
import fer.ui.MenuCursor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Evan
 *
 *         Designed to do much of the graphical heavy lifting, the renderer
 *         class is given the task of sequentially drawing all of the necessary
 *         pixels onto the screen.
 */
public class Renderer implements Runnable {

	// Singleton References
	private Game game;
	private Cursor cursor;
	private MenuCursor menuCursor;
	// Static constants
	public static final int NUM_BUFFERS = 3;
	// Runtime objects
	private BufferStrategy bufferStrat;
	private BufferedImage bufImage;
	private Random random = new Random();
	// Runtime values
	private int[] pixels;
	private boolean drawingMap = false;
	private boolean drawingMenus = false;

	public Renderer() {
		game = Game.getGame();
		cursor = Cursor.getCursor();
		menuCursor = MenuCursor.getMenuCursor();

		if (game.getBufferStrategy() == null) {
			game.createBufferStrategy(NUM_BUFFERS);
		}
		bufferStrat = game.getBufferStrategy();
		bufImage = new BufferedImage(Game.GAME_WIDTH, Game.GAME_HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) bufImage.getRaster().getDataBuffer())
				.getData();

		/*
		 * for (int x = 0; x < game.getCurrentMap().getWidth(); x++) { for (int
		 * y = 0; y < game.getCurrentMap().getHeight(); y++) { if ((x + y) % 2
		 * == 0) { game.getCurrentMap().getTile(x + y
		 * game.getCurrentMap().getWidth()).setColor(0xdeface); } else {
		 * game.getCurrentMap().getTile(x + y
		 * game.getCurrentMap().getWidth()).setColor(0xfacade); } } }
		 */
	}

	public void run() {
		int frames = 0;
		long timer = System.currentTimeMillis();
		while (game.getGameRunning()) {
			render();
			frames++;
			if ((System.currentTimeMillis() - timer) > 1000) {
				timer += 1000;
				game.setGameWindowTitle(Game.GAME_TITLE + "  |  " + frames
						+ " FPS");
				frames = 0;
			}
			// System.out.println("Graphics thread updated.");
			try {
				Thread.sleep(1);
			} catch (InterruptedException ex) {
				Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
	}

	public void render() {
		clearPixels();
		if (Game.isOnTitle()) {
			drawTitleBackground();
		}
		if (Game.isOnMainMenu()) {
			drawMainMenuBackground();
		}
		if (Game.getCurrentMap() != null) {
			drawingMap = true;
			drawTiles();
			if (cursor.movingUnit()) {
				drawMoveTileOverlay();
			}
			if (cursor.showingMoveArrow()) {
				drawMoveArrow();
			}
			if (cursor.isAttacking() && cursor.getAttackableTiles() != null) {
				drawAttackTileOverlay();
			}
			drawUnits();
			drawingMap = false;
		}
		if (Game.getMenuList() != null) {
			drawingMenus = true;
			for (Menu m : Game.getMenuList()) {
				if (m != null) {
					if (m.isVisible()) {
						drawMenu(m);
					}
				}
			}
			drawingMenus = false;
		}
		for (Effect e : Game.getEffectList()) {
			if (e.isVisible()) {
				drawEffect(e);
			}
		}

		Graphics g = bufferStrat.getDrawGraphics();
		g.clearRect(0, 0, Game.GAME_WIDTH * Game.GAME_SCALE, Game.GAME_HEIGHT
				* Game.GAME_SCALE);
		g.setColor(Color.red);
		g.drawImage(bufImage, 0, 0, Game.GAME_WIDTH * Game.GAME_SCALE,
				Game.GAME_HEIGHT * Game.GAME_SCALE, null);
		g.dispose();

		bufferStrat.show();
	}

	public void drawTitleBackground() {
		if (Game.getTitleBackground() != null) {
			for (int x = 0; x < Game.GAME_WIDTH; x++) {
				for (int y = 0; y < Game.GAME_HEIGHT; y++) {
					pixels[x + y * Game.GAME_WIDTH] = Game.getTitleBackground()
							.getPixel(x + y * Game.GAME_WIDTH);
				}
			}
		}
	}

	public void drawMainMenuBackground() {
		if (Game.getMainMenuBackground() != null) {
			for (int x = 0; x < Game.GAME_WIDTH; x++) {
				for (int y = 0; y < Game.GAME_HEIGHT; y++) {
					pixels[x + y * Game.GAME_WIDTH] = Game
							.getMainMenuBackground().getPixel(
									x + y * Game.GAME_WIDTH);
				}
			}
		}
	}

	public void drawTiles() {
		for (int x = 0; x < Game.GAME_WIDTH; x++) {
			for (int y = 0; y < Game.GAME_HEIGHT; y++) {
				int tileIndex = ((x >> 4) + cursor.getMapScrollx())
						+ ((y >> 4) + cursor.getMapScrolly())
						* Game.getCurrentMap().getWidth();
				if (tileIndex == cursor.getMapX() + cursor.getMapY()
						* Game.getCurrentMap().getWidth()
						&& (cursor.getCursorSprite().getPixel(
								(x & 15) + (y & 15) * Tile.TILE_WIDTH) != cursor
								.getCursorSprite().getTransparentColor())
						&& cursor.isVisible()) {
					pixels[(x + (y * Game.GAME_WIDTH))] = cursor
							.getCursorSprite().getPixel(
									(x & 15) + (y & 15) * Tile.TILE_WIDTH);
				} else {
					pixels[(x + (y * Game.GAME_WIDTH))] = Game.getCurrentMap()
							.getTile(tileIndex).getSprite()
							.getPixel((x & 15) + (y & 15) * Tile.TILE_WIDTH);
				}
				if (((x % Tile.TILE_WIDTH) == 0 || (x % Tile.TILE_WIDTH) == (Tile.TILE_WIDTH - 1))
						|| ((y % Tile.TILE_HEIGHT) == 0 || (y % Tile.TILE_HEIGHT) == (Tile.TILE_HEIGHT - 1))) {
					pixels[(x + (y * Game.GAME_WIDTH))] = blendColors(0x000000,
							pixels[(x + (y * Game.GAME_WIDTH))],
							Game.getGridOpacity());
				}
			}
		}
	}

	public void drawUnits() {
		for (Unit unit : Game.getCurrentMap().getUnitDisplay()) {
			if (!unit.isDead()) {
				if (unit.getMapx() >= cursor.getMapScrollx()
						&& unit.getMapx() < (cursor.getMapScrollx() + (Game.GAME_WIDTH / Tile.TILE_WIDTH))) {
					if (unit.getMapy() >= cursor.getMapScrolly()
							&& unit.getMapy() < (cursor.getMapScrolly() + (Game.GAME_HEIGHT / Tile.TILE_HEIGHT))) {
						int tileStartingX = (unit.getMapx() * Tile.TILE_WIDTH)
								- (cursor.getMapScrollx() * Tile.TILE_WIDTH);
						int tileStartingY = (unit.getMapy() * Tile.TILE_HEIGHT)
								- (cursor.getMapScrolly() * Tile.TILE_HEIGHT);
						if (Game.isDrawFactionShadow()) {
							for (int x = 0; x < Map.FACTION_SHADOW.getWidth(); x++) {
								for (int y = 0; y < Map.FACTION_SHADOW
										.getHeight(); y++) {
									if (Map.FACTION_SHADOW.getPixel(x + y
											* Map.FACTION_SHADOW.getWidth()) != Map.FACTION_SHADOW
											.getTransparentColor()) {
										pixels[(x + tileStartingX)
												+ (y + tileStartingY)
												* Game.GAME_WIDTH] = blendColors(
												blendColors(
														Map.FACTION_COLORS[unit
																.getFaction()],
														Map.FACTION_SHADOW.getPixel(x
																+ y
																* Map.FACTION_SHADOW
																		.getWidth()),
														192),
												pixels[(x + tileStartingX)
														+ (y + tileStartingY)
														* Game.GAME_WIDTH], 128);
									}
								}
							}
						}
						int drawXOffset = (unit.getMapSprite().getWidth() - Tile.TILE_WIDTH) / 2;
						int drawYOffset = (unit.getMapSprite().getHeight() - Tile.TILE_HEIGHT);
						int unitStartingX = tileStartingX - drawXOffset
								+ unit.getxOffset();
						int unitStartingY = tileStartingY - drawYOffset
								+ unit.getyOffset();
						if (tileStartingX - drawXOffset + unit.getxOffset() < 0) {
							for (int x = drawXOffset; x < unit.getMapSprite()
									.getWidth(); x++) {
								for (int y = 0; y < unit.getMapSprite()
										.getHeight(); y++) {
									try {
										if (unit.getMapSprite().getPixel(
												x
														+ y
														* unit.getMapSprite()
																.getWidth()) != unit
												.getMapSprite()
												.getTransparentColor()) {
											if (!(unit.hasMoved())) {
												pixels[(unitStartingX + x)
														+ (unitStartingY + y)
														* Game.GAME_WIDTH] = unit
														.getMapSprite()
														.getPixel(
																x
																		+ y
																		* (unit.getMapSprite()
																				.getWidth()));
											} else {
												pixels[(unitStartingX + x)
														+ (unitStartingY + y)
														* Game.GAME_WIDTH] = grayscaleColor(unit
														.getMapSprite()
														.getPixel(
																x
																		+ y
																		* (unit.getMapSprite()
																				.getWidth())));
											}
										}
									} catch (ArrayIndexOutOfBoundsException e) {
										// Do nothing.
									}
								}
							}
						} else {
							for (int x = 0; x < unit.getMapSprite().getWidth(); x++) {
								for (int y = 0; y < unit.getMapSprite()
										.getHeight(); y++) {
									try {
										if (unit.getMapSprite().getPixel(
												x
														+ y
														* unit.getMapSprite()
																.getWidth()) != unit
												.getMapSprite()
												.getTransparentColor()) {
											if (!(unit.hasMoved())) {
												pixels[(unitStartingX + x)
														+ (unitStartingY + y)
														* Game.GAME_WIDTH] = unit
														.getMapSprite()
														.getPixel(
																x
																		+ y
																		* (unit.getMapSprite()
																				.getWidth()));
											} else {
												pixels[(unitStartingX + x)
														+ (unitStartingY + y)
														* Game.GAME_WIDTH] = grayscaleColor(unit
														.getMapSprite()
														.getPixel(
																x
																		+ y
																		* (unit.getMapSprite()
																				.getWidth())));
											}
										}
									} catch (ArrayIndexOutOfBoundsException e) {
										// Do nothing.
									}
								}
							}
						}
					}
				}
			}
		}

	}

	public void drawMoveTileOverlay() {
		for (int x = 0; x < Game.GAME_WIDTH; x++) {
			for (int y = 0; y < Game.GAME_HEIGHT; y++) {
				int tileIndex = ((x >> 4) + cursor.getMapScrollx())
						+ ((y >> 4) + cursor.getMapScrolly())
						* Game.getCurrentMap().getWidth();
				int xDif = Math.abs((x >> 4)
						- (cursor.getSelectedUnit().getMapx() - cursor
								.getMapScrollx()));
				int yDif = Math.abs((y >> 4)
						- (cursor.getSelectedUnit().getMapy() - cursor
								.getMapScrolly()));
				/*
				 * if ((xDif + yDif) <= cursor.getSelectedUnit().getMov()) { if
				 * (cursor.getMoveOverlaySprite().getPixel((x & 15) + (y & 15) *
				 * cursor.getMoveOverlaySprite().getWidth()) !=
				 * cursor.getMoveOverlaySprite() .getTransparentColor()) {
				 * pixels[x + y * Game.GAME_WIDTH] = blendColors(cursor
				 * .getMoveOverlaySprite().getPixel((x & 15) + (y & 15)
				 * cursor.getMoveOverlaySprite().getWidth()), pixels[x + y *
				 * Game.GAME_WIDTH], 128); } }
				 */
				if (cursor.getMoveableTiles().contains(
						Game.getCurrentMap().getTile(tileIndex))) {
					if (cursor.getMoveOverlaySprite().getPixel(
							(x & 15) + (y & 15)
									* cursor.getMoveOverlaySprite().getWidth()) != cursor
							.getMoveOverlaySprite().getTransparentColor()) {
						pixels[x + y * Game.GAME_WIDTH] = blendColors(
								cursor.getMoveOverlaySprite().getPixel(
										(x & 15)
												+ (y & 15)
												* cursor.getMoveOverlaySprite()
														.getWidth()), pixels[x
										+ y * Game.GAME_WIDTH], 128);
					}
				}
			}
		}
	}

	public void drawAttackTileOverlay() {
		for (int x = 0; x < Game.GAME_WIDTH; x++) {
			for (int y = 0; y < Game.GAME_HEIGHT; y++) {
				int tileIndex = ((x >> 4) + cursor.getMapScrollx())
						+ ((y >> 4) + cursor.getMapScrolly())
						* Game.getCurrentMap().getWidth();
				int xDif = Math.abs((x >> 4)
						- (cursor.getSelectedUnit().getMapx() - cursor
								.getMapScrollx()));
				int yDif = Math.abs((y >> 4)
						- (cursor.getSelectedUnit().getMapy() - cursor
								.getMapScrolly()));
				/*
				 * if ((xDif + yDif) <= cursor.getSelectedUnit().getMov()) { if
				 * (cursor.getMoveOverlaySprite().getPixel((x & 15) + (y & 15) *
				 * cursor.getMoveOverlaySprite().getWidth()) !=
				 * cursor.getMoveOverlaySprite() .getTransparentColor()) {
				 * pixels[x + y * Game.GAME_WIDTH] = blendColors(cursor
				 * .getMoveOverlaySprite().getPixel((x & 15) + (y & 15)
				 * cursor.getMoveOverlaySprite().getWidth()), pixels[x + y *
				 * Game.GAME_WIDTH], 128); } }
				 */
				if (cursor.getAttackableTiles().contains(
						Game.getCurrentMap().getTile(tileIndex))) {
					if (cursor.getMoveOverlaySprite().getPixel(
							(x & 15)
									+ (y & 15)
									* cursor.getAttackOverlaySprite()
											.getWidth()) != cursor
							.getAttackOverlaySprite().getTransparentColor()) {
						pixels[x + y * Game.GAME_WIDTH] = blendColors(
								cursor.getAttackOverlaySprite()
										.getPixel(
												(x & 15)
														+ (y & 15)
														* cursor.getAttackOverlaySprite()
																.getWidth()),
								pixels[x + y * Game.GAME_WIDTH], 128);
					}
				}
			}
		}
	}

	public void drawMoveArrow() {
		ArrayList<Tile> tiles = cursor.getArrowPath();
		for (int i = 0; i < tiles.size(); i++) {
			// If the tile is onscreen
			if (tiles.get(i).getMapX() >= cursor.getMapScrollx()
					&& tiles.get(i).getMapX() < cursor.getMapScrollx() + 16
					&& tiles.get(i).getMapY() >= cursor.getMapScrolly()
					&& tiles.get(i).getMapY() < cursor.getMapScrolly() + 16) {
				Tile nextTile = null;
				Tile lastTile = null;
				if (i != 0) {
					nextTile = tiles.get(i - 1);
				}
				if (i < (tiles.size() - 1)) {
					lastTile = tiles.get(i + 1);
				}
				Sprite arrowSeg = getArrowSegment(lastTile, tiles.get(i),
						nextTile);
				int drawStartX = (tiles.get(i).getMapX() - cursor
						.getMapScrollx()) * Tile.TILE_WIDTH;
				int drawStartY = (tiles.get(i).getMapY() - cursor
						.getMapScrolly()) * Tile.TILE_HEIGHT;
				for (int x = drawStartX; x < drawStartX + Tile.TILE_WIDTH; x++) {
					for (int y = drawStartY; y < drawStartY + Tile.TILE_HEIGHT; y++) {
						if (arrowSeg.getPixel((x & 15) + (y & 15)
								* Tile.TILE_WIDTH) != arrowSeg
								.getTransparentColor()) {
							try {
								pixels[x + y * Game.GAME_WIDTH] = arrowSeg
										.getPixel((x & 15) + (y & 15)
												* Tile.TILE_WIDTH);
							} catch (ArrayIndexOutOfBoundsException e) {
								// Do nothing
							}
						}
					}
				}
			}
		}
	}

	public void drawMenu(Menu menu) {
		// Draw background filler
		for (int x = menu.getX(); x < menu.getX() + menu.getWidth(); x++) {
			for (int y = menu.getY(); y < menu.getY() + menu.getHeight(); y++) {
				if (Menu.WINDOWBACKCENTER.getPixel((x & 15) + (y & 15)
						* Menu.WINDOWBACKCENTER.getWidth()) != Menu.WINDOWBACKCENTER
						.getTransparentColor()) {
					pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWBACKCENTER
							.getPixel((x & 15) + (y & 15)
									* Menu.WINDOWBACKCENTER.getWidth());
				}
			}
		}
		// Draw the background corners
		// Top left background
		for (int x = menu.getX(); x < menu.getX()
				+ Menu.WINDOWBACKTOPLEFT.getWidth(); x++) {
			for (int y = menu.getY(); y < menu.getY()
					+ Menu.WINDOWBACKTOPLEFT.getHeight(); y++) {
				if (Menu.WINDOWBACKTOPLEFT
						.getPixel((x - menu.getX()) + (y - menu.getY())
								* Menu.WINDOWBACKTOPLEFT.getWidth()) != Menu.WINDOWBACKTOPLEFT
						.getTransparentColor()) {
					pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWBACKTOPLEFT
							.getPixel((x - menu.getX()) + (y - menu.getY())
									* Menu.WINDOWBACKTOPLEFT.getWidth());
				}
			}
		}
		// Top right background
		for (int x = (menu.getX() + menu.getWidth())
				- Menu.WINDOWBACKTOPRIGHT.getWidth(); x < menu.getX()
				+ menu.getWidth(); x++) {
			for (int y = menu.getY(); y < menu.getY()
					+ Menu.WINDOWBACKTOPRIGHT.getHeight(); y++) {
				if (Menu.WINDOWBACKTOPRIGHT
						.getPixel((x - (menu.getX() + menu.getWidth() - Menu.WINDOWBACKTOPRIGHT
								.getWidth()))
								+ (y - menu.getY())
								* Menu.WINDOWBACKTOPRIGHT.getWidth()) != Menu.WINDOWBACKTOPRIGHT
						.getTransparentColor()) {
					pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWBACKTOPRIGHT
							.getPixel((x - (menu.getX() + menu.getWidth() - Menu.WINDOWBACKTOPRIGHT
									.getWidth()))
									+ (y - menu.getY())
									* Menu.WINDOWBACKTOPRIGHT.getWidth());
				}
			}
		}
		// Bottom left background
		for (int x = menu.getX(); x < menu.getX()
				+ Menu.WINDOWBACKBOTTOMLEFT.getWidth(); x++) {
			for (int y = (menu.getY() + menu.getHeight())
					- Menu.WINDOWBACKBOTTOMLEFT.getHeight(); y < menu.getY()
					+ menu.getHeight(); y++) {
				if (Menu.WINDOWBACKBOTTOMLEFT
						.getPixel((x - menu.getX())
								+ (y - ((menu.getY() + menu.getHeight()) - Menu.WINDOWBACKBOTTOMLEFT
										.getHeight()))
								* Menu.WINDOWBACKBOTTOMLEFT.getWidth()) != Menu.WINDOWBACKBOTTOMLEFT
						.getTransparentColor()) {
					pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWBACKBOTTOMLEFT
							.getPixel((x - menu.getX())
									+ (y - ((menu.getY() + menu.getHeight()) - Menu.WINDOWBACKBOTTOMLEFT
											.getHeight()))
									* Menu.WINDOWBACKBOTTOMLEFT.getWidth());
				}
			}
		}
		// Bottom right background
		for (int x = (menu.getX() + menu.getWidth())
				- Menu.WINDOWBACKBOTTOMRIGHT.getWidth(); x < menu.getX()
				+ menu.getWidth(); x++) {
			for (int y = (menu.getY() + menu.getHeight())
					- Menu.WINDOWBACKBOTTOMRIGHT.getHeight(); y < menu.getY()
					+ menu.getHeight(); y++) {
				if (Menu.WINDOWBACKBOTTOMRIGHT
						.getPixel((x - (menu.getX() + menu.getWidth() - Menu.WINDOWBACKBOTTOMRIGHT
								.getWidth()))
								+ (y - ((menu.getY() + menu.getHeight()) - Menu.WINDOWBACKBOTTOMRIGHT
										.getHeight()))
								* Menu.WINDOWBACKBOTTOMRIGHT.getWidth()) != Menu.WINDOWBACKBOTTOMRIGHT
						.getTransparentColor()) {
					pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWBACKBOTTOMRIGHT
							.getPixel((x - (menu.getX() + menu.getWidth() - Menu.WINDOWBACKBOTTOMRIGHT
									.getWidth()))
									+ (y - ((menu.getY() + menu.getHeight()) - Menu.WINDOWBACKBOTTOMRIGHT
											.getHeight()))
									* Menu.WINDOWBACKBOTTOMRIGHT.getWidth());
				}
			}
		}
		// Draw the border corners
		// Top left border
		for (int x = menu.getX(); x < menu.getX()
				+ Menu.WINDOWEDGETOPLEFT.getWidth(); x++) {
			for (int y = menu.getY(); y < menu.getY()
					+ Menu.WINDOWEDGETOPLEFT.getHeight(); y++) {
				if (Menu.WINDOWEDGETOPLEFT
						.getPixel((x - menu.getX()) + (y - menu.getY())
								* Menu.WINDOWEDGETOPLEFT.getWidth()) != Menu.WINDOWEDGETOPLEFT
						.getTransparentColor()) {
					pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWEDGETOPLEFT
							.getPixel((x - menu.getX()) + (y - menu.getY())
									* Menu.WINDOWEDGETOPLEFT.getWidth());
				}
			}
		}
		// Top right border
		for (int x = (menu.getX() + menu.getWidth())
				- Menu.WINDOWEDGETOPRIGHT.getWidth(); x < menu.getX()
				+ menu.getWidth(); x++) {
			for (int y = menu.getY(); y < menu.getY()
					+ Menu.WINDOWEDGETOPRIGHT.getHeight(); y++) {
				if (Menu.WINDOWEDGETOPRIGHT.getPixel((x - ((menu.getX() + menu
						.getWidth()) - Menu.WINDOWEDGETOPRIGHT.getWidth()))
						+ (y - menu.getY())
						* Menu.WINDOWEDGETOPRIGHT.getWidth()) != Menu.WINDOWEDGETOPRIGHT
						.getTransparentColor()) {
					pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWEDGETOPRIGHT
							.getPixel((x - ((menu.getX() + menu.getWidth()) - Menu.WINDOWEDGETOPRIGHT
									.getWidth()))
									+ (y - menu.getY())
									* Menu.WINDOWEDGETOPRIGHT.getWidth());
				}
			}
		}
		// Bottom left border
		for (int x = menu.getX(); x < menu.getX()
				+ Menu.WINDOWEDGEBOTTOMLEFT.getWidth(); x++) {
			for (int y = (menu.getY() + menu.getHeight())
					- Menu.WINDOWEDGEBOTTOMLEFT.getHeight(); y < menu.getY()
					+ menu.getHeight(); y++) {
				if (Menu.WINDOWEDGEBOTTOMLEFT
						.getPixel((x - menu.getX())
								+ (y - ((menu.getY() + menu.getHeight()) - Menu.WINDOWEDGEBOTTOMLEFT
										.getHeight()))
								* Menu.WINDOWEDGEBOTTOMLEFT.getWidth()) != Menu.WINDOWEDGEBOTTOMLEFT
						.getTransparentColor()) {
					pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWEDGEBOTTOMLEFT
							.getPixel((x - menu.getX())
									+ (y - ((menu.getY() + menu.getHeight()) - Menu.WINDOWEDGEBOTTOMLEFT
											.getHeight()))
									* Menu.WINDOWEDGEBOTTOMLEFT.getWidth());
				}
			}
		}
		// Bottom right corner
		for (int x = (menu.getX() + menu.getWidth())
				- Menu.WINDOWEDGEBOTTOMRIGHT.getWidth(); x < menu.getX()
				+ menu.getWidth(); x++) {
			for (int y = (menu.getY() + menu.getHeight())
					- Menu.WINDOWEDGEBOTTOMRIGHT.getHeight(); y < menu.getY()
					+ menu.getHeight(); y++) {
				if (Menu.WINDOWEDGEBOTTOMRIGHT
						.getPixel((x - ((menu.getX() + menu.getWidth()) - Menu.WINDOWEDGEBOTTOMRIGHT
								.getWidth()))
								+ (y - ((menu.getY() + menu.getHeight()) - Menu.WINDOWEDGEBOTTOMRIGHT
										.getHeight()))
								* Menu.WINDOWEDGEBOTTOMRIGHT.getWidth()) != Menu.WINDOWEDGEBOTTOMRIGHT
						.getTransparentColor()) {
					pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWEDGEBOTTOMRIGHT
							.getPixel((x - ((menu.getX() + menu.getWidth()) - Menu.WINDOWEDGEBOTTOMRIGHT
									.getWidth()))
									+ (y - ((menu.getY() + menu.getHeight()) - Menu.WINDOWEDGEBOTTOMRIGHT
											.getHeight()))
									* Menu.WINDOWEDGEBOTTOMRIGHT.getWidth());
				}
			}
		}
		// Draw the border connectors
		for (int x = menu.getX(); x < menu.getX() + menu.getWidth(); x++) {
			for (int y = menu.getY(); y < menu.getY() + menu.getHeight(); y++) {
				// Top border connectors
				if (x >= menu.getX() + Menu.WINDOWEDGETOPLEFT.getWidth()
						&& x <= ((menu.getX() + menu.getWidth()) - Menu.WINDOWEDGETOPRIGHT
								.getWidth())) {
					if (y - menu.getY() < Menu.WINDOWEDGETOPCENTER.getHeight()) {
						if (Menu.WINDOWEDGETOPCENTER
								.getPixel(((x - menu.getX()) & 15)
										+ ((y - menu.getY()) & 15)
										* Menu.WINDOWEDGETOPCENTER.getWidth()) != Menu.WINDOWEDGETOPCENTER
								.getTransparentColor()) {
							pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWEDGETOPCENTER
									.getPixel(((x - menu.getX()) & 15)
											+ ((y - menu.getY()) & 15)
											* Menu.WINDOWEDGETOPCENTER
													.getWidth());
						}
					}
				}
				// Bottom border connectors
				if (x >= menu.getX() + Menu.WINDOWEDGEBOTTOMLEFT.getWidth()
						&& x <= ((menu.getX() + menu.getWidth()) - Menu.WINDOWEDGEBOTTOMRIGHT
								.getWidth())) {
					if (y > ((menu.getY() + menu.getHeight()) - Menu.WINDOWEDGEBOTTOMCENTER
							.getHeight())) {
						if (Menu.WINDOWEDGEBOTTOMCENTER
								.getPixel(((x - menu.getX()) & 15)
										+ (y - ((menu.getY() + menu.getHeight()) - Menu.WINDOWEDGEBOTTOMCENTER
												.getHeight()))
										* Menu.WINDOWEDGEBOTTOMCENTER
												.getWidth()) != Menu.WINDOWEDGEBOTTOMCENTER
								.getTransparentColor()) {
							pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWEDGEBOTTOMCENTER
									.getPixel(((x - menu.getX()) & 15)
											+ (y - ((menu.getY() + menu
													.getHeight()) - Menu.WINDOWEDGEBOTTOMCENTER
													.getHeight()))
											* Menu.WINDOWEDGEBOTTOMCENTER
													.getWidth());
						}
					}
				}
				// Left border connector
				if (y >= menu.getY() + Menu.WINDOWEDGETOPLEFT.getHeight()
						&& y <= ((menu.getY() + menu.getHeight()) - Menu.WINDOWEDGEBOTTOMLEFT
								.getHeight())) {
					if (x - menu.getX() < Menu.WINDOWEDGELEFT.getWidth()) {
						if (Menu.WINDOWEDGELEFT
								.getPixel(((x - menu.getX()) & 15)
										+ ((y - menu.getY()) & 15)
										* Menu.WINDOWEDGELEFT.getWidth()) != Menu.WINDOWEDGELEFT
								.getTransparentColor()) {
							pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWEDGELEFT
									.getPixel(((x - menu.getX()) & 15)
											+ ((y - menu.getY()) & 15)
											* Menu.WINDOWEDGELEFT.getWidth());
						}
					}
				}
				// Right border connectors
				if (y >= menu.getY() + Menu.WINDOWEDGETOPRIGHT.getHeight()
						&& y <= ((menu.getY() + menu.getHeight()) - Menu.WINDOWEDGEBOTTOMRIGHT
								.getHeight())) {
					if (x > ((menu.getX() + menu.getWidth()) - Menu.WINDOWEDGERIGHT
							.getWidth())) {
						if (Menu.WINDOWEDGERIGHT
								.getPixel(((x - ((menu.getX() + menu.getWidth()) - Menu.WINDOWEDGERIGHT
										.getWidth())))
										+ ((y - menu.getY()) & 15)
										* Menu.WINDOWEDGERIGHT.getWidth()) != Menu.WINDOWEDGERIGHT
								.getTransparentColor()) {
							pixels[x + y * Game.GAME_WIDTH] = Menu.WINDOWEDGERIGHT
									.getPixel(((x - ((menu.getX() + menu
											.getWidth()) - Menu.WINDOWEDGERIGHT
											.getWidth())))
											+ ((y - menu.getY()) & 15)
											* Menu.WINDOWEDGERIGHT.getWidth());
						}
					}
				}
			}
		}
		// Draw the elements
		for (int i = 0; i < menu.getElements().size(); i++) {
			for (int x = menu.getElement(i).getX(); x < menu.getElement(i)
					.getX() + menu.getElement(i).getGraphic().getWidth(); x++) {
				for (int y = menu.getElement(i).getY(); y < menu.getElement(i)
						.getY() + menu.getElement(i).getGraphic().getHeight(); y++) {
					if (x < menu.getWidth() && y < menu.getHeight()) {
						if (menu.getElement(i)
								.getGraphic()
								.getPixel(
										(x - menu.getElement(i).getX())
												+ (y - menu.getElement(i)
														.getY())
												* menu.getElement(i)
														.getGraphic()
														.getWidth()) != menu
								.getElement(i).getGraphic()
								.getTransparentColor()) {
							pixels[(x + menu.getX()) + (y + menu.getY())
									* Game.GAME_WIDTH] = menu
									.getElement(i)
									.getGraphic()
									.getPixel(
											(x - menu.getElement(i).getX())
													+ (y - menu.getElement(i)
															.getY())
													* menu.getElement(i)
															.getGraphic()
															.getWidth());
						}
					}
				}
			}
		}
		// If the menu is active, draw the cursor
		if (menu == MenuCursor.getActiveMenu()) {
			try {
				for (int x = menu.getX()
						+ menu.getElement(menuCursor.getElementIndex()).getX(); x < menu
						.getX()
						+ menu.getElement(menuCursor.getElementIndex()).getX()
						+ menu.getElement(menuCursor.getElementIndex())
								.getGraphic().getWidth(); x++) {
					for (int y = menu.getY()
							+ menu.getElement(menuCursor.getElementIndex())
									.getY(); y < menu.getY()
							+ menu.getElement(menuCursor.getElementIndex())
									.getY()
							+ menu.getElement(menuCursor.getElementIndex())
									.getGraphic().getHeight(); y++) {
						pixels[x + y * Game.GAME_WIDTH] = blendColors(
								MenuCursor.CURSOR_COLOR, pixels[x + y
										* Game.GAME_WIDTH], 96);
					}
				}
			} catch (IndexOutOfBoundsException e) {
				// Do nothing
			}
		}
	}

	public void drawEffect(Effect effect) {
		for (int x = effect.getX(); x < effect.getX()
				+ effect.getSprite().getWidth(); x++) {
			for (int y = effect.getY(); y < effect.getY()
					+ effect.getSprite().getHeight(); y++) {
				if (x >= 0
						&& x < Game.GAME_WIDTH
						&& y >= 0
						&& y < Game.GAME_HEIGHT
						&& effect.getSprite().getPixel(
								(x - effect.getX()) + (y - effect.getY())
										* effect.getSprite().getWidth()) != effect
								.getSprite().getTransparentColor()) {
					if (effect.isDisplayColor()) {
						pixels[x + y * Game.GAME_WIDTH] = blendColors(
								effect.getColor(effect.getCurrentFrame()),
								pixels[x + y * Game.GAME_WIDTH],
								effect.getOpacity(effect.getCurrentFrame()));
					} else {
						pixels[x + y * Game.GAME_WIDTH] = blendColors(
								effect.getSprite()
										.getPixel(
												(x - effect.getX())
														+ (y - effect.getY())
														* effect.getSprite()
																.getWidth()),
								pixels[x + y * Game.GAME_WIDTH],
								effect.getOpacity(effect.getCurrentFrame()));
					}
				}
			}
		}
	}

	public void drawCursor() {
	}

	public void clearPixels() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}

	/**
	 * Blends together one given RGB color with another, using an alpha value to
	 * determine the opacity of the added color.
	 *
	 * @param a_rrggbb
	 *            : The color to be blended with the initial color.
	 * @param b_rrggbb
	 *            : The initial color.
	 * @param alpha
	 *            : The alpha value (out of 256) that determines the opacity of
	 *            the added color.
	 * @return The result RGB color of the blending operation.
	 */
	public int blendColors(int a_rrggbb, int b_rrggbb, int alpha) {
		int a_rr00bb = a_rrggbb & 0xff00ff;
		int a_gg00 = a_rrggbb & 0xff00;
		int b_rr00bb = b_rrggbb & 0xff00ff;
		int b_gg00 = b_rrggbb & 0xff00;

		int blend_rr00bb00 = (a_rr00bb * alpha + b_rr00bb * (256 - alpha)) & 0xff00ff00;
		int blend_gg0000 = (a_gg00 * alpha + b_gg00 * (256 - alpha)) & 0xff0000;
		return (blend_rr00bb00 | blend_gg0000) >>> 8;
	}

	/**
	 * A very basic RGB desaturation algorithm that finds the highest color
	 * value in the speciied color (R, G, or B) and sets the two remaining
	 * fields to that value in order to create a grayscale color. Credit to
	 * stackexchange user cubic1271 for a simplified byte extracting algorithm.
	 *
	 * @param rrggbb
	 *            The color to be transformed to grayscale
	 * @return The grayscale equivalent of the given color
	 */
	public int grayscaleColor(int rrggbb) {
		int r = ((rrggbb >> 16) & 0xFF);
		int g = ((rrggbb >> 8) & 0xFF);
		int b = (rrggbb & 0xFF);

		int greatest = 0;
		if (r > greatest) {
			greatest = r;
		}
		if (g > greatest) {
			greatest = g;
		}
		if (b > greatest) {
			greatest = b;
		}

		return greatest + (greatest << 8) + (greatest << 16);
	}

	/**
	 * Returns the sprite corresponding to the proper movement arrow segment
	 * given a sequence of three adjacent tiles. The sprite corresponds to the
	 * segment for the central tile in the sequence. Either of the other two
	 * tiles may be null, but the central tile may not, and no more than one
	 * tile may be null. If the central tile is null, two or more tiles are
	 * null, or if the given tiles are not adjacent, or if any of the tiles are
	 * the same, the returned segment will likely not be the desired result.
	 *
	 * @param lastTile
	 *            : The tile before the currentTile in the sequence. May be
	 *            null.
	 * @param currentTile
	 *            : The current tile in the sequence. May not be null.
	 * @param nextTile
	 *            : The tile after the current tile in the sequence. May be
	 *            null.
	 * @return: The sprite for the arrow segment corresponding to the current or
	 *          central tile in the sequence.
	 */
	public Sprite getArrowSegment(Tile lastTile, Tile currentTile, Tile nextTile) {
		if (currentTile == null) {
			return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 18,
					SpriteSheet.MAPARROW);
		} else if (lastTile == null && nextTile == null) {
			return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 18,
					SpriteSheet.MAPARROW);
		} else if (lastTile == null) {
			if (nextTile.getMapX() > currentTile.getMapX()) {
				return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 1,
						SpriteSheet.MAPARROW);
			} else if (nextTile.getMapX() < currentTile.getMapX()) {
				return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 1,
						SpriteSheet.MAPARROW, true);
			} else if (nextTile.getMapY() > currentTile.getMapY()) {
				return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18, 1,
						SpriteSheet.MAPARROW, false, true);
			} else if (nextTile.getMapY() < currentTile.getMapY()) {
				return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18, 1,
						SpriteSheet.MAPARROW);
			} else {
				return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 1,
						SpriteSheet.MAPARROW);
			}
		} else if (nextTile == null) {
			if (lastTile.getMapX() > currentTile.getMapX()) {
				return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 52,
						SpriteSheet.MAPARROW, true);
			} else if (lastTile.getMapX() < currentTile.getMapX()) {
				return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 52,
						SpriteSheet.MAPARROW);
			} else if (lastTile.getMapY() > currentTile.getMapY()) {
				return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18, 52,
						SpriteSheet.MAPARROW);
			} else if (!(lastTile.getMapY() < currentTile.getMapY())) {
				return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 52,
						SpriteSheet.MAPARROW);
			} else {
				return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18, 52,
						SpriteSheet.MAPARROW, false, true);
			}
		} else {
			if (lastTile.getMapX() > currentTile.getMapX()) {
				if (nextTile.getMapX() < currentTile.getMapX()) {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 18,
							SpriteSheet.MAPARROW);
				} else if (nextTile.getMapY() > currentTile.getMapY()) {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18,
							35, SpriteSheet.MAPARROW, true);
				} else if (!(nextTile.getMapY() < currentTile.getMapY())) {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 1,
							SpriteSheet.MAPARROW);
				} else {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 35,
							SpriteSheet.MAPARROW, true);
				}
			} else if (lastTile.getMapX() < currentTile.getMapX()) {
				if (nextTile.getMapX() > currentTile.getMapX()) {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 18,
							SpriteSheet.MAPARROW);
				} else if (nextTile.getMapY() > currentTile.getMapY()) {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18,
							35, SpriteSheet.MAPARROW);
				} else if (nextTile.getMapY() < currentTile.getMapY()) {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 35,
							SpriteSheet.MAPARROW);
				} else {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 1,
							SpriteSheet.MAPARROW, true);
				}
			} else if (lastTile.getMapY() > currentTile.getMapY()) {
				if (nextTile.getMapX() > currentTile.getMapX()) {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18,
							35, SpriteSheet.MAPARROW, true);
				} else if (nextTile.getMapX() < currentTile.getMapX()) {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18,
							35, SpriteSheet.MAPARROW);
				} else if (nextTile.getMapY() < currentTile.getMapY()) {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18,
							18, SpriteSheet.MAPARROW);
				} else {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18, 1,
							SpriteSheet.MAPARROW, false, true);
				}
			} else if (!(lastTile.getMapY() < currentTile.getMapY())) {
				return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 18,
						SpriteSheet.MAPARROW);
			} else {
				if (nextTile.getMapX() > currentTile.getMapX()) {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 35,
							SpriteSheet.MAPARROW, true);
				} else if (nextTile.getMapX() < currentTile.getMapX()) {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 1, 35,
							SpriteSheet.MAPARROW);
				} else if (nextTile.getMapY() > currentTile.getMapY()) {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18,
							18, SpriteSheet.MAPARROW);
				} else {
					return new Sprite(Tile.TILE_WIDTH, Tile.TILE_HEIGHT, 18, 1,
							SpriteSheet.MAPARROW);
				}
			}
		}
	}

	public boolean isDrawingMap() {
		return drawingMap;
	}

	public boolean isDrawingMenus() {
		return drawingMenus;
	}
}
