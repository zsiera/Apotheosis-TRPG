package fer.ui;

import fer.graphics.Sprite;
import fer.graphics.SpriteSheet;

/**
 * @author Evan Stewart
 *
 * A class that stores a set of sprites corresponding to various characters from
 * the ASCII character set to be referenced by classes that display text, such
 * as the TextGraphic class.
 */
public class Font {

    public static Font BASICFONT = new Font(SpriteSheet.BASICFONT, 5, 5, 1, 10,
            10, 32, 32, 1, 1);
    public static Font STATFONT = new Font(SpriteSheet.STATSBAR, 5, 9, 1, 1, 11, 
            10, 48, 1, 7);
    private Sprite[] characters;
    private Sprite nullCharacter;

    public Font(Sprite[] iCharacters) {
        characters = iCharacters;
        try {
            nullCharacter = characters[32];
        } catch (NullPointerException e) {
            nullCharacter = new Sprite(5, 5, 0, 0);
        }
    }

    public Font(Sprite[] iCharacters, Sprite iNullChar) {
        this(iCharacters);
        nullCharacter = iNullChar;
    }

    public Font(SpriteSheet spriteSheet, int cWidth, int cHeight, int ssOffset,
            int ssRows, int ssColumns, int nullChar, int charStart, int xStart, 
            int yStart) {
        characters = new Sprite[charStart + (ssRows * ssColumns)];
        for (int i = 0; i < characters.length - charStart; i++) {
            characters[i + charStart] = new Sprite(cWidth, cHeight, xStart + (((i % (ssColumns)) * cWidth))
                    + ((i % (ssColumns)) * ssOffset), yStart + ((i / (ssColumns)) * cHeight) + ((i / (ssColumns)) * ssOffset), spriteSheet);
        }
        nullCharacter = (characters[nullChar + charStart]);
    }

    public Sprite getCharacter(char c) {
        try {
            if (characters[(int) c] != null) {
                return characters[(int) c];
            } else {
                return nullCharacter;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("The requested character does not exist within "
                    + "the specified font.");
            return nullCharacter;
        }
    }

    public void setCharacter(char c, Sprite newCharacter) {
        try {
            characters[(int) c] = newCharacter;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("The requested character does not exist within "
                    + "the specified font.");
        }
    }
}
