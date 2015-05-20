/*
 * 
 */
package fer.graphics;

import fer.Game;

// TODO: Auto-generated Javadoc
/**
 * The Class Effect.
 *
 * @author Evan
 */
public class Effect {

	/** The animation. */
	private Animation animation;
	
	/** The sprite. */
	private Sprite sprite;

	/** The visible. */
	private boolean visible;
	
	/** The animated. */
	private boolean animated;
	
	/** The display color. */
	private boolean displayColor; // Will display the corresponding color in
	// colors[] as opposed to the pixel colors of the sprite. Transparent color
	// pixels will still be transparent.
	/** The colors. */
	private int[] colors;
	
	/** The opacities. */
	private int[] opacities;
	
	/** The current frame. */
	private int currentFrame = 0;
	
	/** The x. */
	private int x;
	
	/** The y. */
	private int y;

	/**
	 * Instantiates a new effect.
	 *
	 * @param iAnimation the i animation
	 * @param ix the ix
	 * @param iy the iy
	 */
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

	/**
	 * Instantiates a new effect.
	 *
	 * @param iAnimation the i animation
	 * @param iOpacities the i opacities
	 * @param ix the ix
	 * @param iy the iy
	 */
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

	/**
	 * Instantiates a new effect.
	 *
	 * @param iAnimation the i animation
	 * @param iOpacities the i opacities
	 * @param iColors the i colors
	 * @param ix the ix
	 * @param iy the iy
	 */
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

	/**
	 * Update animation.
	 */
	public void updateAnimation() {
		animation.updateAnimation();
		sprite = animation.getCurrentFrameSprite();
		currentFrame = animation.getCurrentFrame();
	}

	/**
	 * Gets the sprite.
	 *
	 * @return the sprite
	 */
	public Sprite getSprite() {
		return sprite;
	}

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the visible.
	 *
	 * @param visible the new visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Checks if is animated.
	 *
	 * @return true, if is animated
	 */
	public boolean isAnimated() {
		return animated;
	}

	/**
	 * Sets the animated.
	 *
	 * @param animated the new animated
	 */
	public void setAnimated(boolean animated) {
		this.animated = animated;
	}

	/**
	 * Checks if is display color.
	 *
	 * @return true, if is display color
	 */
	public boolean isDisplayColor() {
		return displayColor;
	}

	/**
	 * Sets the display color.
	 *
	 * @param displayColor the new display color
	 */
	public void setDisplayColor(boolean displayColor) {
		this.displayColor = displayColor;
	}

	/**
	 * Gets the colors.
	 *
	 * @return the colors
	 */
	public int[] getColors() {
		return colors;
	}

	/**
	 * Gets the color.
	 *
	 * @param index the index
	 * @return the color
	 */
	public int getColor(int index) {
		return colors[index];
	}

	/**
	 * Sets the colors.
	 *
	 * @param colors the new colors
	 */
	public void setColors(int[] colors) {
		this.colors = colors;
	}

	/**
	 * Sets the color.
	 *
	 * @param index the index
	 * @param iColor the i color
	 */
	public void setColor(int index, int iColor) {
		colors[index] = iColor;
	}

	/**
	 * Gets the opacities.
	 *
	 * @return the opacities
	 */
	public int[] getOpacities() {
		return opacities;
	}

	/**
	 * Gets the opacity.
	 *
	 * @param index the index
	 * @return the opacity
	 */
	public int getOpacity(int index) {
		return opacities[index];
	}

	/**
	 * Sets the opacities.
	 *
	 * @param opacities the new opacities
	 */
	public void setOpacities(int[] opacities) {
		this.opacities = opacities;
	}

	/**
	 * Sets the opacity.
	 *
	 * @param index the index
	 * @param iOpacity the i opacity
	 */
	public void setOpacity(int index, int iOpacity) {
		opacities[index] = iOpacity;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Gets the current frame.
	 *
	 * @return the current frame
	 */
	public int getCurrentFrame() {
		return currentFrame;
	}

	/**
	 * Sets the current frame.
	 *
	 * @param currentFrame the new current frame
	 */
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
		animation.setCurrentFrame(currentFrame);
	}
}
