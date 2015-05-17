package fer.ai;

public class Exclusions {
	private int excludeX = -1;
	private int excludeY = -1;

	public int getExcludeX() {
		return excludeX;
	}

	public void setExcludeX(int excludeX) {
		this.excludeX = excludeX;
	}

	public int getExcludeY() {
		return excludeY;
	}

	public void setExcludeY(int excludeY) {
		this.excludeY = excludeY;
	}

	public void setExcludeCollision(int x, int y) {
		excludeX = x;
		excludeY = y;
	}

}