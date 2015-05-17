package fer.graphics;

import fer.Game;

/**
 * @author Evan
 */
public class Effect {

	private Animation animation;
	private Sprite sprite;

	private boolean visible;
	private boolean animated;
	private boolean displayColor; // Will display the corresponding color in
	// colors[] as opposed to the pixel colors of the sprite. Transparent color
	// pixels will still be transparent.
	private int[] colors;
	private int[] opacities;
	private int currentFrame = 0;
	private int x;
	private int y;

	public Effect(Animation iAnimation, int ix, int iy) {
		animation = iAnimation;
		sprite = iAnimation.getCurrentFrameSprite();
		opacities = new int[animation.getFrames()];
		for (int i = 0; i < opacities.length; i++) {
			opacities[i] = 255;
		}
		visible = true;
		animated = true;
		displayColor = false;
		x = ix;
		y = iy;
		Game.getEffectList().add(this);
	}

	public Effect(Animation iAnimation, int[] iOpacities, int ix, int iy) {
		animation = iAnimation;
		sprite = iAnimation.getCurrentFrameSprite();
		visible = true;
		animated = true;
		displayColor = false;
		opacities = iOpacities;
		x = ix;
		y = iy;
		Game.getEffectList().add(this);
	}

	public Effect(Animation iAnimation, int[] iOpacities, int[] iColors,
			int ix, int iy) {
		animation = iAnimation;
		sprite = iAnimation.getCurrentFrameSprite();
		visible = true;
		animated = true;
		opacities = iOpacities;
		colors = iColors;
		displayColor = true;
		x = ix;
		y = iy;
		Game.getEffectList().add(this);
	}

	public void updateAnimation() {
		animation.updateAnimation();
		sprite = animation.getCurrentFrameSprite();
		currentFrame = animation.getCurrentFrame();
	}

	public Sprite getSprite() {
		return sprite;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isAnimated() {
		return animated;
	}

	public void setAnimated(boolean animated) {
		this.animated = animated;
	}

	public boolean isDisplayColor() {
		return displayColor;
	}

	public void setDisplayColor(boolean displayColor) {
		this.displayColor = displayColor;
	}

	public int[] getColors() {
		return colors;
	}

	public int getColor(int index) {
		return colors[index];
	}

	public void setColors(int[] colors) {
		this.colors = colors;
	}

	public void setColor(int index, int iColor) {
		colors[index] = iColor;
	}

	public int[] getOpacities() {
		return opacities;
	}

	public int getOpacity(int index) {
		return opacities[index];
	}

	public void setOpacities(int[] opacities) {
		this.opacities = opacities;
	}

	public void setOpacity(int index, int iOpacity) {
		opacities[index] = iOpacity;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
		animation.setCurrentFrame(currentFrame);
	}
}
