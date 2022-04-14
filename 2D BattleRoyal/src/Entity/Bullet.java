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
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	
	private double angle;
	private double xVelocity;
	private double yVelocity;
	private Point destPoint;
	
	public Bullet(TileMap tm, Point destPoint,boolean right)
	{
		super(tm);
		this.destPoint = destPoint;
		

		facingRight = right;
		moveSpeed = 3.8;
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
		//trzeba zmienic to 230 bo to chyba map height ale po skali
		if(y<0 || y>GamePanel.HEIGHT-cheight) remove = true;
		
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
		xVelocity =  ( (1.0) * Math.sin( 1 * angle));
        yVelocity =  ((1.0) * Math.cos(-1 * angle));
        setVector(xVelocity, yVelocity);
		//System.out.println(xVelocity+"   "+yVelocity);
	}
}
