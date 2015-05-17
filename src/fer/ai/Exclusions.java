package fer.ai;

public class Exclusions {
	private int excludeX = -1;
	private int excludeY = -1;

	public final int getExcludeX() {
		return excludeX;
	}

	public final void setExcludeX(final int excludeX) {
		this.excludeX = excludeX;
	}

	public final int getExcludeY() {
		return excludeY;
	}

	public final void setExcludeY(final int excludeY) {
		this.excludeY = excludeY;
	}

	public final void setExcludeCollision(final int x, final int y) {
		excludeX = x;
		excludeY = y;
	}

}