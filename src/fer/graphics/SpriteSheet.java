package fer.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * @author Evan
 * 
 *         A class that handles the loading and caching of spritesheets, among
 *         other things.
 */
public class SpriteSheet {

	private final int WIDTH, HEIGHT;
	private String path;
	private int[] pixels;
	private int transparentColor;

	/*
	 * public static final SpriteSheet TILESET = new SpriteSheet
	 * //("/res/images/map/Tileset.png", 426, 681, 0);
	 * ("/res/images/map/CustomTileset.png", 171, 18, 0);
	 */
	public static final SpriteSheet CURSOR = new SpriteSheet(
			"/res/images/map/MapCursor.png", 16, 16, -65536);
	public static final SpriteSheet MAPOVERLAY = new SpriteSheet(
			"/res/images/map/MapMovementOverlay.png", 35, 18, -65281);
	public static final SpriteSheet MAPARROW = new SpriteSheet(
			"/res/images/map/MapMovementArrow.png", 35, 69, -16776961);
	public static final SpriteSheet FACTIONSHADOW = new SpriteSheet(
			"/res/images/units/map/FactionShadow.png", 18, 18, -16469967);
	/*
	 * public static final SpriteSheet FARCHERMAP = new SpriteSheet
	 * ("/res/images/units/map/FArcherMap.png", 101, 126, -16469967); public
	 * static final SpriteSheet KAELLMAP = new SpriteSheet
	 * ("/res/images/units/map/KaellMapSprite.png", 176, 751, -16469967);
	 */
	public static final SpriteSheet WINDOWSKIN = new SpriteSheet(
			"/res/images/ui/Windowskin.png", 69, 69, -16776961);
	public static final SpriteSheet MENUBACK = new SpriteSheet(
			"/res/images/ui/FractalMenuBack.png", 240, 160, -16776961);
	public static final SpriteSheet BASICFONT = new SpriteSheet(
			"/res/images/fonts/BasicFont.png", 61, 61, -16776961);
	/*
	 * public static final SpriteSheet WEAPONICONSET = new SpriteSheet
	 * ("/res/images/icons/WeaponIconset.png", 18, 18,-16776961); public static
	 * final SpriteSheet ITEMICONSET = new SpriteSheet
	 * ("/res/images/icons/ItemIconset.png", 18, 18,-16776961); public static
	 * final SpriteSheet ARMORICONSET = new SpriteSheet
	 * ("/res/images/icons/ArmorIconset.png", 18, 18,-16776961); public static
	 * final SpriteSheet KAELLMAPFACE = new SpriteSheet
	 * ("/res/images/faces/KaellMapFace.png", 32, 32, -16469967);
	 */
	public static final SpriteSheet HEALTHBAR = new SpriteSheet(
			"/res/images/ui/HealthBar.png", 94, 30, -16776961);
	public static final SpriteSheet STATSBAR = new SpriteSheet(
			"/res/images/ui/StatsBar.png", 83, 17, -16776961);

	public SpriteSheet(String pathName, int width, int height,
			int transparentColor) {
		path = pathName;
		this.WIDTH = width;
		this.HEIGHT = height;
		this.transparentColor = transparentColor;
		pixels = new int[WIDTH * HEIGHT];
		loadImage();
	}

	private void loadImage() {
		String separator = System.getProperty("file.separator");
		String absPath = null;
		absPath = System.getProperty("user.dir");
		if (absPath.endsWith(separator + "dist" + separator + "Apotheosis.jar")) {
			absPath = absPath
					.substring(
							0,
							absPath.length()
									- (separator + "dist" + separator + "Apotheosis.jar")
											.length());
		} else if (absPath.endsWith(separator + "build" + separator + "classes"
				+ separator)) {
			absPath = absPath
					.substring(0,
							absPath.length()
									- (separator + "build" + separator
											+ "classes" + separator).length());
		} else if (absPath.endsWith(separator + "bin" + separator)) { // added
																		// for
																		// Eclipse
																		// compatibility
			absPath = absPath.substring(0, absPath.length()
					- (separator + "bin" + separator).length());
		}
		absPath += path;
		try {
			BufferedImage image = ImageIO.read(new File(absPath));
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0,
					image.getWidth());
		} catch (IOException ex) {
			Logger.getLogger(SpriteSheet.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		/*
		 * try { BufferedImage image = ImageIO.read(SpriteSheet.class.
		 * getResource(path)); image.getRGB(0, 0, image.getWidth(),
		 * image.getHeight(), pixels, 0, image.getWidth()); } catch (IOException
		 * ex) { Logger.getLogger(SpriteSheet.class.getName()).log(Level.SEVERE,
		 * null, ex); }
		 */
	}

	public int getPixel(int index) {
		return pixels[index];
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public int getTransparentColor() {
		return transparentColor;
	}

}
