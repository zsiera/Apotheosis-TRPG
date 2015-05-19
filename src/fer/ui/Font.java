/*
 * 
 */
package fer.ui;

import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;

// TODO: Auto-generated Javadoc
/**
 * The Class Font.
 *
 * @author Evan Stewart
 * 
 *         A class that stores a set of sprites corresponding to various
 *         characters from the ASCII character set to be referenced by classes
 *         that display text, such as the TextGraphic class.
 */
public class Font {

	/** The basicfont. */
	public static Font BASICFONT = new Font(SpriteSheet.BASICFONT, 5, 5, 1, 10,
			10, 32, 32, 1, 1);
	
	/** The statfont. */
	public static Font STATFONT = new Font(SpriteSheet.STATSBAR, 5, 9, 1, 1,
			11, 10, 48, 1, 7);
	
	/** The characters. */
	private Sprite[] characters;
	
	/** The null character. */
	private Sprite nullCharacter;

	/**
	 * Instantiates a new font.
	 *
	 * @param iCharacters the i characters
	 */
	public Font(Sprite[] iCharacters) {
		characters = iCharacters;
		try {
			nullCharacter = characters[32];
		} catch (NullPointerException e) {
			nullCharacter = new Sprite(5, 5, 0, 0);
		}
	}

	/**
	 * Instantiates a new font.
	 *
	 * @param iCharacters the i characters
	 * @param iNullChar the i null char
	 */
	public Font(Sprite[] iCharacters, Sprite iNullChar) {
		this(iCharacters);
		nullCharacter = iNullChar;
	}

	/**
	 * Instantiates a new font.
	 *
	 * @param spriteSheet the sprite sheet
	 * @param cWidth the c width
	 * @param cHeight the c height
	 * @param ssOffset the ss offset
	 * @param ssRows the ss rows
	 * @param ssColumns the ss columns
	 * @param nullChar the null char
	 * @param charStart the char start
	 * @param xStart the x start
	 * @param yStart the y start
	 */
	public Font(SpriteSheet spriteSheet, int cWidth, int cHeight, int ssOffset,
			int ssRows, int ssColumns, int nullChar, int charStart, int xStart,
			int yStart) {
		characters = new Sprite[charStart + (ssRows * ssColumns)];
		for (int i = 0; i < characters.length - charStart; i++) {
			characters[i + charStart] = new Sprite(cWidth, cHeight, xStart
					+ (((i % (ssColumns)) * cWidth))
					+ ((i % (ssColumns)) * ssOffset), yStart
					+ ((i / (ssColumns)) * cHeight)
					+ ((i / (ssColumns)) * ssOffset), spriteSheet);
		}
		nullCharacter = (characters[nullChar + charStart]);
	}

	/**
	 * Gets the character.
	 *
	 * @param c the c
	 * @return the character
	 */
	public Sprite getCharacter(char c) {
		try {
			return !(characters[(int) c] != null) ? nullCharacter : characters[(int) c];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("The requested character does not exist within "
					+ "the specified font.");
			return nullCharacter;
		}
	}

	/**
	 * Sets the character.
	 *
	 * @param c the c
	 * @param newCharacter the new character
	 */
	public void setCharacter(char c, Sprite newCharacter) {
		try {
			characters[(int) c] = newCharacter;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("The requested character does not exist within "
					+ "the specified font.");
		}
	}
}
