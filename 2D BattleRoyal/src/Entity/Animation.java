package Entity;

import java.awt.image.BufferedImage;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Animation implements Serializable
{
	transient private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime;
	private long delay;
	
	private boolean playedOnce;
	
	/**
     * Constructs a new {@code Animation}
     */
	public Animation()
	{
		playedOnce = false;
	}
	
	/**
	 * Setter
     * set sprite for animation
     * @param sprites of object
     */
	public void setFrames(BufferedImage[] frames)
	{
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}
	
	/**
	 * Setter
     * set delay for time between animation
     * @param g time of delay
     */
	public void setDelay( long g) {this.delay = g; }
	/**
	 * Setter
     * set of current sprite to animation
     * @param i number of sprite
     */
	public void setFrame(int i) { this.currentFrame = i;}
	
	/**
	 * Getter
     * Return int value of current animation
     * @return {@code currentFrame}
     */
	public int getFrame() { return currentFrame; }
	/**
	 * Getter
     * Return image value of current animation
     * @return {@code frames[currentFrame]} image of animation
     */
	public BufferedImage getImage() { return frames[currentFrame]; }
	/**
	 * Getter
     * Return value of delay
     * @return {@code delay} return value of delay
     */
	public long getDelay() { return delay; }
	/**
     * Return statement if animation was started
     * @return {@code playedOnce} true if animation was started or false if not
     */
	public boolean hasPlayedOnce() { return playedOnce; }
	
	
	/**
     * Update animation, set next animation if delay between animation was completed
     */
	public void update()
	{
		if(delay == -1) return;
		
		long elapsed = (System.nanoTime() - startTime ) / 1000000;
		if(elapsed > delay)
		{
			currentFrame++;
			startTime = System.nanoTime() ;
		}
		if(currentFrame == frames.length)
		{
			currentFrame = 0;
			playedOnce = true;
		}
		
	}
	
	
}
