package Entity;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class AimCursor 
{
	private int x;
	private int y;
	private BufferedImage image;
	
	private Cursor cursor;
	
	/**
     * Constructs a new {@code AimCursor}
     */
	public AimCursor()
	{
		setImage();
	}
	
	/**
     * Set texture for cursor
     */
	private void setImage() 
	{
		try {
			image = ImageIO.read(
					getClass().getResourceAsStream(
							"/Cursor/cursor.png"
					)
			);
			
			cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "aimCursor");
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	/**
     * Getter
     * @return {@code x} coordinate of cursor
     */
	public int getX() { return x;}
	/**
     * Getter
     * @return {@code y} coordinate of cursor
     */
	public int getY() { return y;}
	/**
     * Returns a cursor object
     * @return the specified cursor
     */
	public Cursor getCursor() { return cursor; }
	
	/**
     * Setter
     * set cursor coordinate of map
     * @param x coordinate of cursor
     * @param y coordinate of cursor
     */
	public void setCursorPosition(int x, int y)
	{
		this.x = x;
		this.y =y;
	}
	
	/** 
     * Draw cursor on screen
     * @param g the specified frame Graphics
     */
	public void draw(Graphics2D g)
	{
		g.drawImage(image, x, y, null);
	}
}
