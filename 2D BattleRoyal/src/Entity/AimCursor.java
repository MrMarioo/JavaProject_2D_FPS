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
	
	public AimCursor()
	{
		setImage();
	}
	
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

	public int getX() { return x;}
	public int getY() { return y;}
	public Cursor getCursor() { return cursor; }
	public void setCursorPosition(int x, int y)
	{
		this.x = x;
		this.y =y;
	}
	
	public void draw(Graphics2D g)
	{
		g.drawImage(image, x, y, null);
	}
}
