package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class HUD 
{
	private Player player;
	private BufferedImage image;
	private Font font;
	
	public HUD(Player p)
	{
		this.player = p;
		
		try {
			image = ImageIO.read(
					getClass().getResourceAsStream(
							"/HUD/hud.gif"
					)
			);
			font = new Font("Arial",Font.PLAIN, 14); 
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void draw( Graphics2D g)
	{
		g.drawImage(image, 0, 20, null);
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 35);
		g.drawString(player.getFire() / 100 + "/" + player.getMaxFire() / 100, 30, 55);
	}
}
