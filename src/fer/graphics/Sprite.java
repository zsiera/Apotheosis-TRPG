package fer.graphics;

import fer.Tile;

/**
 * @author Evan
 * 
 * Stores the pixels of a sprite pulled from a spritesheet object based upon
 * given coordinates and dimensions.
 */
public class Sprite {
    
    private final int WIDTH, HEIGHT;
    private int x, y;
    private int[] pixels;
    private int transparentColor;
    private SpriteSheet spritesheet;
    
    public Sprite (int iWidth, int iHeight, int ix, int iy,
            SpriteSheet parentSheet) {
        this.WIDTH = iWidth;
        this.HEIGHT = iHeight;
        this.x = ix;
        this.y = iy;
        spritesheet = parentSheet;
        pixels = new int[WIDTH * HEIGHT];
        loadSprite();
    }
    
    public Sprite (int iWidth, int iHeight, int ix, int iy,
            SpriteSheet parentSheet, boolean flippedHorizantal) {
        this(iWidth, iHeight, ix, iy, parentSheet);
        if (flippedHorizantal) {
            flipSpriteHorizantal();
        }
    }
    
    public Sprite (int iWidth, int iHeight, int ix, int iy,
            SpriteSheet parentSheet, boolean flippedHorizantal,
            boolean flippedVertical) {
        this(iWidth, iHeight, ix, iy, parentSheet, flippedHorizantal);
        if (flippedVertical) {
            flipSpriteVertical();
        }
    }
    
    public Sprite (int iWidth, int iHeight, int iColor, int iTransparentColor) {
        WIDTH = iWidth;
        HEIGHT = iHeight;
        pixels = new int[WIDTH * HEIGHT];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = iColor;
        }
        transparentColor = iTransparentColor;
    }
    
    public Sprite (int iWidth, int iHeight, int[] iPixels, 
            int iTransparentColor) {
        WIDTH = iWidth;
        HEIGHT = iHeight;
        pixels = iPixels;
        transparentColor = iTransparentColor;
    }
    
    private void loadSprite() {
        transparentColor = spritesheet.getTransparentColor();
        for (int xi = 0; xi < WIDTH; xi++) {
            for (int yi = 0; yi < HEIGHT; yi++) {
                pixels[xi + yi * WIDTH] = spritesheet.getPixel((xi + x) + 
                        ((yi + y) * spritesheet.getWidth()));
            }
        }
    }
    
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
    
    public void flipSpriteVertical() {
        int bottomIndex = 0;
        int topIndex = HEIGHT - 1;
        int temp = 0;
        for (int y = 0; y < HEIGHT; y++) {
            while (bottomIndex < topIndex) {
                temp = pixels[(bottomIndex * HEIGHT) + y];
                pixels[(bottomIndex * HEIGHT) + y] = pixels[(topIndex * HEIGHT) + y];
                pixels[(topIndex * HEIGHT) + y] = temp;
                bottomIndex++;
                topIndex--;
            }
            bottomIndex = 0;
            topIndex = HEIGHT - 1;
        }
    }
    
    public int getPixel(int index) {
        return pixels[index];
    }
    
    public void setPixel(int index, int iColor) {
        pixels[index] = iColor;
    }
    
    public int getTransparentColor() {
        return transparentColor;
    }
    
    public int getWidth() {
        return WIDTH;
    }
    
    public int getHeight() {
        return HEIGHT;
    }
}
