package Entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import TileMap.TileMap;

@SuppressWarnings("serial")
public class Bullet extends MapObject
{
	private boolean hit;
	private boolean remove;
	transient private BufferedImage[] sprites;
	transient private BufferedImage[] hitSprites;
	private long id;
	
	private double angle;
	private double xVelocity;
	private double yVelocity;
	private Point destPoint;
	
	
	/**
     * Constructs a new {@code Bullet}
     * @param     tm TileMap of Bullet
     * @param	  destPoint to where bullet will go
     * @param	  right statement of facing right or left
     */
	public Bullet(TileMap tm, Point destPoint,boolean right)
	{
		super(tm);
		this.destPoint = destPoint;
		id++;

		facingRight = right;
		moveSpeed = 5;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;
		loadSprites();
	}
	
	
	/**
     * Load whole texture and sprites for bullet
     */
	public void loadSprites()
	{
		//load sprites
		try {
			
			BufferedImage spriteSheet = ImageIO.read(
					getClass().getResource(
						"/Sprites/Player/fireball.png"
					)	
			);
			
			loadBulletSprite(spriteSheet);
			loadHitSprite(spriteSheet);
			
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
     * Load default sprites for bullet
     * @param	spriteSheet image with all sprites
     */
	private void loadHitSprite(BufferedImage spriteSheet)
	{
		hitSprites = new BufferedImage[3];
		for(int i=0; i<hitSprites.length; i++)
			hitSprites[i] = spriteSheet.getSubimage(
					i * width,
					height,
					width,
					height
			);
		
	}
	/**
     * Load hit sprites for bullet
     * @param	spriteSheet image with all hit sprites
     */
	private void loadBulletSprite(BufferedImage spriteSheet)
	{
		sprites = new BufferedImage[4];
		for(int i=0; i< sprites.length; i++)
			sprites[i] = spriteSheet.getSubimage(
					i * width,
					0,
					width,
					height
			);
		
	}

	/**
     * Setter for Tile Map of bullet
     * @param	tm image TileMap 
     */
	public void setTM(TileMap tm) { tileMapStuff.setTM(tm); }
	/**
     * Getter 
     * return bullet id
     * @return	{@code id} id number of bullet
     */
	public long getID( ) { return id; }
	
	
	/** 
     * Set hit and change default sprites to hit sprites
     */
	public void setHit()
	{
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
		dy = 0;
		xVelocity = 0;
		yVelocity = 0;
	}
	
	/** 
     * Return value of remove statement who define bullet is alive
     * @return {@code remove} true if bullet will remove or false if still alive
     */
	public boolean shouldRemove() { return remove; }
	
	/** 
     * Update movement, sprites, check collision of current bullet
     * @see #checkTileMapCollision()
     * @see #setPosition(double x, double y)
     */
	public void update()
	{
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		if(dx == 0 && !hit) setHit();
		
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) remove = true;
		//delete if over or under map
		if((y < 0 ) && !hit) { remove = true; setHit(); }
		//delete if hits the ground
		if((bottomLeft || bottomRight) && !hit) { remove = true; setHit(); }
		
	}
	
	/** 
     * Update movement, tilemap, of bullet from server
     * @param tm TileMap of current map
     * @param b Bullet from server
     */
	public void updateFromServer( TileMap tm, Bullet b)
	{
		tileMapStuff.setTM(tm);
		setPosition(b.getX(), b.getY());
		this.facingRight = b.facingRight;
		animation.setFrames(sprites);
		animation.setDelay(animation.getDelay());
	}
	
	/** 
     * Update movement, tilemap, of bullet from server
     * @param g the specified frame Graphics
     * @see	#setMapPositon()
     */
	public void draw(Graphics2D g)
	{
		setMapPositon();
		if(facingRight)
		{
			
			g.drawImage(
					animation.getImage(),
					(int)(x + tileMapStuff.getXmap() - width / 2),
					(int)(y + tileMapStuff.getYmap() - height /2),
					null 
			);
			return;
		}
		g.drawImage(
				animation.getImage(),
				(int)(x + tileMapStuff.getXmap() - width / 2 + width),
				(int)(y + tileMapStuff.getYmap() - height / 2),
				-width,
				height,
				null 
		);	
	}
	
	/** 
     * Calc vector for bullet
     */
	public void calcVector() 
	{
		angle = Math.atan2(destPoint.getX() - x, destPoint.getY() - y);
		xVelocity =  ( (moveSpeed) * Math.sin( 1 * angle) );
        yVelocity =  ((moveSpeed) * Math.cos(-1 * angle) );
        setVector(xVelocity, yVelocity);
	}
	

	/*private void readObject(ObjectInputStream objIn) throws ClassNotFoundException, IOException 
	  {  
		int xt, yt;
	    xt = (int) objIn.readObject();
	    yt = (int) objIn.readObject();
	    setPosition(xt, yt);
	    this.facingRight = (boolean) objIn.readObject();
	    this.currentAction = (int) objIn.readObject();
	    this.id = (long) objIn.readObject();
	    this.width = (int) objIn.readObject();
	    this.height = (int) objIn.readObject();
	  }
	 
	  private void writeObject(ObjectOutputStream objOut) throws IOException 
	  {
		  objOut.writeObject(getX());
		  objOut.writeObject(getY());
		  objOut.writeObject(facingRight);
		  objOut.writeObject(currentAction);
		  objOut.writeObject(id);
		  objOut.writeObject(width);
		  objOut.writeObject(height);
	  }*/

}
