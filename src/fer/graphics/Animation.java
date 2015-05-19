/*
 * 
 */
package fer.graphics;

// TODO: Auto-generated Javadoc
/**
 * The Class Animation.
 *
 * @author Evan Stewart
 * 
 *         A class containing a set of frames for an animation along with each
 *         frame's corresponding sprite, duration, sound effects, etc.
 * 
 *         TODO: Read from XML.
 */
public class Animation {

	/** The num frames. */
	private int numFrames;
	
	/** The frame durations. */
	private int[] frameDurations;
	
	/** The sprites. */
	private Sprite[] sprites;
	
	/** The current frame. */
	private int currentFrame = 0;
	
	/** The current frame updates. */
	private int currentFrameUpdates = 0;
	
	/** The key frame. */
	private int keyFrame = 0;

	/**
	 * Instantiates a new animation.
	 *
	 * @param iNumFrames the i num frames
	 */
	public Animation(int iNumFrames) {
		this.numFrames = iNumFrames;
		sprites = new Sprite[numFrames];
		frameDurations = new int[numFrames];
		// Avoid null points for frameDurations. For the moment, uninitialized
		// sprites shall remain that way.
		for (int i = 0; i < frameDurations.length; i++) {
			frameDurations[i] = 0;
		}
	}

	/**
	 * Instantiates a new animation.
	 *
	 * @param iNumFrames the i num frames
	 * @param spritesIn the sprites in
	 */
	public Animation(int iNumFrames, Sprite[] spritesIn) {
		this(iNumFrames);
		if (spritesIn.length < numFrames) {
			for (int i = 0; i < spritesIn.length; i++) {
				sprites[i] = spritesIn[i];
			}
			// Sprites past this point remain uninitialized.
		} else {
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesIn[i];
			}
		}
	}

	/**
	 * Instantiates a new animation.
	 *
	 * @param iNumFrames the i num frames
	 * @param frameDurationsIn the frame durations in
	 */
	public Animation(int iNumFrames, int[] frameDurationsIn) {
		this(iNumFrames);
		if (!(frameDurationsIn.length < numFrames)) {
			for (int i = 0; i < frameDurations.length; i++) {
				frameDurations[i] = frameDurations[i];
			}
		} else {
			for (int i = 0; i < frameDurationsIn.length; i++) {
				frameDurations[i] = frameDurationsIn[i];
			}
			// Elements past this point are initialized to zero.
			for (int i = frameDurationsIn.length; i < frameDurations.length; i++) {
				frameDurations[i] = 0;
			}
		}
	}

	/**
	 * Instantiates a new animation.
	 *
	 * @param iNumFrames the i num frames
	 * @param spritesIn the sprites in
	 * @param frameDurationsIn the frame durations in
	 */
	public Animation(int iNumFrames, Sprite[] spritesIn, int[] frameDurationsIn) {
		this(iNumFrames, spritesIn);
		if (!(frameDurationsIn.length < numFrames)) {
			for (int i = 0; i < frameDurations.length; i++) {
				frameDurations[i] = frameDurationsIn[i];
			}
		} else {
			for (int i = 0; i < frameDurationsIn.length; i++) {
				frameDurations[i] = frameDurationsIn[i];
			}
			// Elements past this point are initialized to zero.
			for (int i = frameDurationsIn.length; i < frameDurations.length; i++) {
				frameDurations[i] = 0;
			}
		}
	}

	/**
	 * Returns the sprite corresponding to the given frame.
	 * 
	 * @param frame
	 *            : The frame corresponding to the desired sprite.
	 * @return The sprite corresponding to the given frame.
	 */
	public Sprite getSprite(int frame) {
		return sprites[frame];
	}

	/**
	 * Sets the sprite.
	 *
	 * @param frame the frame
	 * @param sprite the sprite
	 */
	public void setSprite(int frame, Sprite sprite) {
		sprites[frame] = sprite;
	}

	/**
	 * Sets the frame duration.
	 *
	 * @param frame the frame
	 * @param duration the duration
	 */
	public void setFrameDuration(int frame, int duration) {
		frameDurations[frame] = duration;
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
	 * @param frame the new current frame
	 */
	public void setCurrentFrame(int frame) {
		currentFrame = frame;
	}

	/**
	 * Gets the current frame updates.
	 *
	 * @return the current frame updates
	 */
	public int getCurrentFrameUpdates() {
		return currentFrameUpdates;
	}

	/**
	 * Sets the current frame updates.
	 *
	 * @param updates the new current frame updates
	 */
	public void setCurrentFrameUpdates(int updates) {
		currentFrameUpdates = updates;
	}

	/**
	 * A method to be called on every update for an active animation. Increments
	 * the amount of updates that have occurred during the current frame, and
	 * increments the current frame when those exceed the frame duration.
	 **/
	public void updateAnimation() {
		currentFrameUpdates++;
		if (currentFrameUpdates > frameDurations[currentFrame]) {
			currentFrame++;
			if (currentFrame >= numFrames) {
				currentFrame = 0;
			}
			currentFrameUpdates = 0;
		}
	}

	/**
	 * Reset animation.
	 */
	public void resetAnimation() {
		currentFrame = 0;
		currentFrameUpdates = 0;
	}

	/**
	 * Returns the sprite corresponding to the currently active frame.
	 * 
	 * @return The sprite corresponding to the current frame.
	 */
	public Sprite getCurrentFrameSprite() {
		return sprites[currentFrame];
	}

	/**
	 * Gets the frames.
	 *
	 * @return the frames
	 */
	public int getFrames() {
		return sprites.length;
	}

	/**
	 * Gets the updates.
	 *
	 * @param frameCount the frame count
	 * @return the updates
	 */
	public int getUpdates(int frameCount) {
		int updates = 0;
		for (int i = 0; i < frameCount; i++) {
			updates += frameDurations[i];
		}
		return updates;
	}

	/**
	 * Gets the key frame.
	 *
	 * @return the key frame
	 */
	public int getKeyFrame() {
		return keyFrame;
	}

	/**
	 * Sets the key frame.
	 *
	 * @param keyFrame the new key frame
	 */
	public void setKeyFrame(int keyFrame) {
		this.keyFrame = keyFrame;
	}
}
