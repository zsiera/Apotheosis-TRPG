/*
 * 
 */
package fer.ai;

// TODO: Auto-generated Javadoc
/**
 * The Class Exclusions.
 */
public class Exclusions {
	
	/** The exclude x. */
	private int excludeX = -1;
	
	/** The exclude y. */
	private int excludeY = -1;

	/**
	 * Gets the exclude x.
	 *
	 * @return the exclude x
	 */
	public final int getExcludeX() {
		return excludeX;
	}

	/**
	 * Sets the exclude x.
	 *
	 * @param excludeX the new exclude x
	 */
	public final void setExcludeX(final int excludeX) {
		this.excludeX = excludeX;
	}

	/**
	 * Gets the exclude y.
	 *
	 * @return the exclude y
	 */
	public final int getExcludeY() {
		return excludeY;
	}

	/**
	 * Sets the exclude y.
	 *
	 * @param excludeY the new exclude y
	 */
	public final void setExcludeY(final int excludeY) {
		this.excludeY = excludeY;
	}

	/**
	 * Sets the exclude collision.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public final void setExcludeCollision(final int x, final int y) {
		excludeX = x;
		excludeY = y;
	}

}