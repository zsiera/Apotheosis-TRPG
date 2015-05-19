/*
 * 
 */
package fer.graphics;

import fer.Tile;

// TODO: Auto-generated Javadoc
/**
 * The Class Sprite.
 *
 * @author Evan
 * 
 *         Stores the pixels of a sprite pulled from a spritesheet object based
 *         upon given coordinates and dimensions.
 */
public class Sprite {

	/** The height. */
	private final int WIDTH, HEIGHT;
	
	/** The y. */
	private int x, y;
	
	/** The pixels. */
	private int[] pixels;
	
	/** The transparent color. */
	private int transparentColor;
	
	/** The spritesheet. */
	private SpriteSheet spritesheet;

	/**
	 * Instantiates a new sprite.
	 *
	 * @param iWidth the i width
	 * @param iHeight the i height
	 * @param ix the ix
	 * @param iy the iy
	 * @param parentSheet the parent sheet
	 */
	public Sprite(int iWidth, int iHeight, int ix, int iy,
			SpriteSheet parentSheet) {
		this.WIDTH = iWidth;
		this.HEIGHT = iHeight;
		this.x = ix;
		this.y = iy;
		spritesheet = parentSheet;
		pixels = new int[WIDTH * HEIGHT];
		loadSprite();
	}

	/**
	 * Instantiates a new sprite.
	 *
	 * @param iWidth the i width
	 * @param iHeight the i height
	 * @param ix the ix
	 * @param iy the iy
	 * @param parentSheet the parent sheet
	 * @param flippedHorizantal the flipped horizantal
	 */
	public Sprite(int iWidth, int iHeight, int ix, int iy,
			SpriteSheet parentSheet, boolean flippedHorizantal) {
		this(iWidth, iHeight, ix, iy, parentSheet);
		if (flippedHorizantal) {
			flipSpriteHorizantal();
		}
	}

	/**
	 * Instantiates a new sprite.
	 *
	 * @param iWidth the i width
	 * @param iHeight the i height
	 * @param ix the ix
	 * @param iy the iy
	 * @param parentSheet the parent sheet
	 * @param flippedHorizantal the flipped horizantal
	 * @param flippedVertical the flipped vertical
	 */
	public Sprite(int iWidth, int iHeight, int ix, int iy,
			SpriteSheet parentSheet, boolean flippedHorizantal,
			boolean flippedVertical) {
		this(iWidth, iHeight, ix, iy, parentSheet, flippedHorizantal);
		if (flippedVertical) {
			flipSpriteVertical();
		}
	}

	/**
	 * Instantiates a new sprite.
	 *
	 * @param iWidth the i width
	 * @param iHeight the i height
	 * @param iColor the i color
	 * @param iTransparentColor the i transparent color
	 */
	public Sprite(int iWidth, int iHeight, int iColor, int iTransparentColor) {
		WIDTH = iWidth;
		HEIGHT = iHeight;
		pixels = new int[WIDTH * HEIGHT];
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = iColor;
		}
		transparentColor = iTransparentColor;
	}

	/**
	 * Instantiates a new sprite.
	 *
	 * @param iWidth the i width
	 * @param iHeight the i height
	 * @param iPixels the i pixels
	 * @param iTransparentColor the i transparent color
	 */
	public Sprite(int iWidth, int iHeight, int[] iPixels, int iTransparentColor) {
		WIDTH = iWidth;
		HEIGHT = iHeight;
		pixels = iPixels;
		transparentColor = iTransparentColor;
	}

	/**
	 * Load sprite.
	 */
	private void loadSprite() {
		transparentColor = spritesheet.getTransparentColor();
		for (int xi = 0; xi < WIDTH; xi++) {
			for (int yi = 0; yi < HEIGHT; yi++) {
				pixels[xi + yi * WIDTH] = spritesheet.getPixel((xi + x)
						+ ((yi + y) * spritesheet.getWidth()));
			}
		}
	}

	/**
	 * Flip sprite horizantal.
	 */
	public void flipSpriteHorizantal() {
		int bottomIndex = 0;
		int topIndex = WIDTH - 1;
		int temp = 0;
		for (int y = 0; y < HEIGHT; y++) {
			while (bottomIndex < topIndex) {
				temp = pixels[bottomIndex + y * WIDTH];
				pixels[bottomIndex + y * WIDTH] = pixels[topIndex + y * WIDTH];
				pixels[topIndex + y * WIDTH] = temp;
				bottomIndex++;
				topIndex--;
			}
			bottomIndex = 0;
			topIndex = WIDTH - 1;
		}
	}

	/**
	 * Flip sprite vertical.
	 */
	public void flipSpriteVertical() {
		int bottomIndex = 0;
		int topIndex = HEIGHT - 1;
		int temp = 0;
		for (int y = 0; y < HEIGHT; y++) {
			while (bottomIndex < topIndex) {
				temp = pixels[(bottomIndex * HEIGHT) + y];
				pixels[(bottomIndex * HEIGHT) + y] = pixels[(topIndex * HEIGHT)
						+ y];
				pixels[(topIndex * HEIGHT) + y] = temp;
				bottomIndex++;
				topIndex--;
			}
			bottomIndex = 0;
			topIndex = HEIGHT - 1;
		}
	}

	/**
	 * Gets the pixel.
	 *
	 * @param index the index
	 * @return the pixel
	 */
	public int getPixel(int index) {
		return pixels[index];
	}

	/**
	 * Sets the pixel.
	 *
	 * @param index the index
	 * @param iColor the i color
	 */
	public void setPixel(int index, int iColor) {
		pixels[index] = iColor;
	}

	/**
	 * Gets the transparent color.
	 *
	 * @return the transparent color
	 */
	public int getTransparentColor() {
		return transparentColor;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return WIDTH;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return HEIGHT;
	}
}
