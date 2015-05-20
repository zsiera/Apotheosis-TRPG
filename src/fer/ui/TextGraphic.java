/*
 * 
 */
package fer.ui;

import fer.graphics.Sprite;

// TODO: Auto-generated Javadoc
/**
 * The Class TextGraphic.
 *
 * @author Evan Stewart
 * 
 *         A class that takes a string of ASCII characters and a font as an
 *         input and builds a sprite containing graphical representations of
 *         each of these characters. Essentially a wrapper for a single Sprite
 *         instance, its main usefulness lies in its constructor, which
 *         retrieves each of the relevant character sprites from the specified
 *         font and merges them into a single sprite. It is worth noting that
 *         each particular TextGraphic encompasses only one line of texts, so
 *         line breaks must be created using multiple TextGraphic instances.
 */
public class TextGraphic {

	/** The graphic. */
	private Sprite graphic;

	/**
	 * Instantiates a new text graphic.
	 *
	 * @param text the text
	 * @param font the font
	 */
	public TextGraphic(String text, Font font) {
		char[] characters = text.toCharArray();
		Sprite[] sprites = new Sprite[characters.length];
		int tgWidth = 0;
		int tgHeight = 0;
		for (int i = 0; i < characters.length; i++) {
			sprites[i] = font.getCharacter(characters[i]);
			tgWidth += sprites[i].getWidth();
			if (sprites[i].getHeight() > tgHeight) {
				tgHeight = sprites[i].getHeight();
			}
		}
		graphic = new Sprite(tgWidth, tgHeight,
				sprites[0].getTransparentColor(),
				sprites[0].getTransparentColor());
		int drawOffset = 0;
		for (int i = 0; i < sprites.length; i++) {
			for (int x = 0; x < sprites[i].getWidth(); x++) {
				for (int y = 0; y < sprites[i].getHeight(); y++) {
					graphic.setPixel((drawOffset + x) + y * graphic.getWidth(),
							sprites[i].getPixel(x + y * sprites[i].getWidth()));
				}
			}
			drawOffset += sprites[i].getWidth();
		}
	}

	/**
	 * Gets the sprite.
	 *
	 * @return the sprite
	 */
	public Sprite getSprite() {
		return graphic;
	}

}
