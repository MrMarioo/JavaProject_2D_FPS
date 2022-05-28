package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Explosion 
{
	private int x;
	private int y;
	private int xmap;
	private int ymap;
	
	private int width;
	private int height;
	
	private Animation animation;
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	/**
     * Constructs a new {@code Explosion}
     * @param x
     * @param y
     */
	public Explosion(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		width = 30;
		height = 30;
		
		try {
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream("/Sprites/Enemies/explosion.gif")
			);
			loadSprites(spritesheet);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(70);
	}
	/**
     * Setter for whole texture and sprites for HUD
     */
	private void loadSprites(BufferedImage spritesheet)
	{
		sprites = new BufferedImage[6];
		for(int i=0; i  < sprites.length; i++)
		{
			sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
			);
		}
		
	}

	/**
     * Update animation of explosion
     */
	public void update()
	{
		animation.update();
		if(animation.hasPlayedOnce())
			remove = true;
	}
	
	/**
     * Getter
     * @return {@code remove} statement if animation end 
     */
	public boolean shouldRemove() { return remove; }
	
	/**
     * Setter
     * @param {@code x} set explosion on map
     * @param {@code y} set explosion on map
     */
	public void setMapPosition(int x, int y)
	{
		xmap = x;
		ymap = y;
	}
	
	/**
     * Function to draw explosion
     * @param g the specified frame Graphics
     */
	public void draw(Graphics2D g)
	{
		g.drawImage(
				animation.getImage(),
				x + xmap - width / 2,
				y + ymap - height / 2,
				null
		);
	}
}
