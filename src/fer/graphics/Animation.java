package fer.graphics;

/**
 * @author Evan Stewart
 * 
 *         A class containing a set of frames for an animation along with each
 *         frame's corresponding sprite, duration, sound effects, etc.
 * 
 *         TODO: Read from XML.
 */
public class Animation {

	private int numFrames;
	private int[] frameDurations;
	private Sprite[] sprites;
	private int currentFrame = 0;
	private int currentFrameUpdates = 0;
	private int keyFrame = 0;

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

	public void setSprite(int frame, Sprite sprite) {
		sprites[frame] = sprite;
	}

	public void setFrameDuration(int frame, int duration) {
		frameDurations[frame] = duration;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int frame) {
		currentFrame = frame;
	}

	public int getCurrentFrameUpdates() {
		return currentFrameUpdates;
	}

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

	public int getFrames() {
		return sprites.length;
	}

	public int getUpdates(int frameCount) {
		int updates = 0;
		for (int i = 0; i < frameCount; i++) {
			updates += frameDurations[i];
		}
		return updates;
	}

	public int getKeyFrame() {
		return keyFrame;
	}

	public void setKeyFrame(int keyFrame) {
		this.keyFrame = keyFrame;
	}
}
