package TileMap;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.awt.*;

import Main.GamePanel;
public class Background
{
	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	/**
     * Constructs a new {@code GamePanel}
     * @param s name of file to background
     * @param ms how fast will move background
     */
	public Background(String s, double ms)
	{
		try
		{
			image = ImageIO.read(getClass().getResourceAsStream(s));
			moveScale = ms;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
     * set position to out image
     * @param x cord on window
     * @param y cord on window
     */
	public void setPosition(double x, double y)
	{
		this.x = (x *moveScale) % GamePanel.WIDTH;
		this.y = (y *moveScale) % GamePanel.HEIGHT;
	}
	
	/**
     * set vector to our image
     * @param dx x vector to image
     * @param dy y vector to image
     */
	public void setVector(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	/**
     * update positon, move the background image
     */
	public void update()
	{
		x += dx;
		y += dy;
	}
	
	/**
     * Function to draw out background image
     * @param g the specified frame Graphics
     */
	public void draw(Graphics2D g)
	{
		g.drawImage(image, (int)x, (int)y, null);
		if(x<0)
			g.drawImage(image,
					(int)x + GamePanel.WIDTH,
					(int)y, 
					null);
		if(x > 0)
			g.drawImage(image,
					(int)x - GamePanel.WIDTH,
					(int)y, 
					null);
	}
}
