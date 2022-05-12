package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

public class Slugger extends Enemy 
{

	private BufferedImage[] sprites;
	
	public Slugger(TileMap tm)
	{
		super(tm);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		health = maxHealth = 2;
		damage = 1;
		
		//loadsprites
		
		try {
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
					"/Sprites/Enemies/slugger.gif"		
					)
			);
			
			sprites = new BufferedImage[3];
			
			for(int i=0; i< sprites.length;i++)
			{
				sprites[i] = spritesheet.getSubimage(
						i * width,
						0,
						width,
						height
				);
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		right = true;
		facingRight = true;
	}
	
	
	
	private void getNextPosition()
	{
		if(left)
		{
			dx -= moveSpeed;
			if(dx < - maxSpeed)
				dx = -maxSpeed;
		}
		else if(right)
		{
			dx += moveSpeed;
			if(dx > maxSpeed)
				dx = maxSpeed;
		}
		
		if(falling)
		{
			dy += fallSpeed;
		}
	}
	
	public void update()
	{
		
		//update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//check flinching

		if(flinching)
		{
			long elapsed = (System.nanoTime() - flinchTime) / 1000000;
			if(elapsed > 400)
				flinching = false;
		}
		
		//if hits the wall go other direction
		if(right && dx == 0)
		{
			right = false;
			left = true;
			facingRight = false;
		}
		else if( left && dx == 0)
		{
			right = true;
			left = false;
			facingRight = true;
		}
		animation.update();
	}
	
	public void draw(Graphics2D g)
	{
		//if(notOnScreen()) return;
		
		
		setMapPositon();
		
		if(facingRight)
		{
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2),
					(int)(y + ymap - height / 2),
					null 
			);
		}else {

			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2 + width),
					(int)(y + ymap - height / 2),
					-width,
					height,
					null 
			);
			
			
		}
	}

}
