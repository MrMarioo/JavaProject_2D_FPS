package Entity;

import java.awt.image.BufferedImage;

public class Animation 
{
	private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime;
	private long delay;
	
	private boolean playedOnce;
	
	public void Animation()
	{
		playedOnce = false;
	}
	
	public void setFrames(BufferedImage[] frames)
	{
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}
	
	public void setDelay( long g) {this.delay = g; }
	public void setFrame(int i) { this.currentFrame = i;}
	public int getFrame() { return currentFrame; }
	public BufferedImage getImage() { return frames[currentFrame]; }
	public boolean hasPlayedOnse() { return playedOnce; }
	
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
