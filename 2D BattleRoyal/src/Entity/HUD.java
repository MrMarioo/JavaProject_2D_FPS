package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class HUD 
{
	private Player player;
	private BufferedImage image;
	private Font font;
	
	 /**
     * Constructs a new {@code HUD}
     * @param p set HUD to a player frame    
     */
	public HUD(Player p)
	{
		this.player = p;
		
		loadSprites();
		
	}
	
	/**
     * Setter for whole texture and sprites for HUD
     */
	private void loadSprites() 
	{
		try {
			image = ImageIO.read(
					getClass().getResourceAsStream(
							"/HUD/hud.png"
					)
			);
			font = new Font("Arial",Font.PLAIN, 10); 
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
     * Function to draw HUD
     * @param g the specified frame Graphics
     */
	public void draw( Graphics2D g)
	{
		g.drawImage(image, GamePanel.WIDTH - 30, 1, null);
		g.setFont(font);
		g.setColor(Color.red);
		g.drawString(player.getHealth() + "/" + player.getMaxHealth(), GamePanel.WIDTH - 50, 13);
		g.setColor(new Color(0x948335));
		g.drawString(player.getFire() + "/" + player.getMaxFire() , GamePanel.WIDTH - 60, 30);
	}
}
