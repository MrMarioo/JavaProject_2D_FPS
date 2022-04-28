package Entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public class Bullet extends MapObject
{
	private boolean hit;
	private boolean remove;
	transient private BufferedImage[] sprites;
	transient private BufferedImage[] hitSprites;
	
	private double angle;
	private double xVelocity;
	private double yVelocity;
	private Point destPoint;
	
	public Bullet(TileMap tm, Point destPoint,boolean right)
	{
		super(tm);
		this.destPoint = destPoint;
		

		facingRight = right;
		moveSpeed = 4.5;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;
		
		//load sprites
		try {
			
			BufferedImage spriteSheet = ImageIO.read(
					getClass().getResource(
						"/Sprites/Player/fireball.png"
					)	
			);
			
			sprites = new BufferedImage[4];
			for(int i=0; i< sprites.length; i++)
				sprites[i] = spriteSheet.getSubimage(
						i * width,
						0,
						width,
						height
				);
			hitSprites = new BufferedImage[3];
			for(int i=0; i<hitSprites.length; i++)
				hitSprites[i] = spriteSheet.getSubimage(
						i * width,
						height,
						width,
						height
				);
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setHit()
	{
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update()
	{
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		if(dx == 0 && !hit) setHit();
		
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) remove = true;
		//delete if over or under map
		if((y<0 || y>GamePanel.HEIGHT-cheight) && !hit) { remove = true; setHit(); }
		//delete if hits the ground
		if((bottomLeft || bottomRight) && !hit) { remove = true; setHit(); }
		
	}
	public void draw(Graphics2D g)
	{
		setMapPositon();
		if(facingRight)
		{
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2),
					(int)(y + ymap - height /2),
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

	public void calcVector() 
	{
		
		angle = Math.atan2(destPoint.getX() - x, destPoint.getY() - y);
		xVelocity =  ( (moveSpeed) * Math.sin( 1 * angle));
        yVelocity =  ((moveSpeed) * Math.cos(-1 * angle));
        setVector(xVelocity, yVelocity);
	}
}
