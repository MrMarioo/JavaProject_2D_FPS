package Entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import EntityProperties.ObjectTileStuff;
import ServState.StartPoint;
import TileMap.*;

@SuppressWarnings("serial")
public class Player extends MapObject implements Serializable
{	
	private int id;
	//player stuff
	private int health;
	private int maxHealth;
	private boolean dead;
	private boolean loseOneTeamPoint;
	private int fire;
	private int maxFire;
	private boolean flinching;
	private long flinchTime;
	private StartPoint startPosition;
	private boolean isTextured = false;
	//bullets
	private boolean firing;
	private int fireCost;
	private  ArrayList<Bullet> bullets;
	private boolean reload;
	private boolean reloading;
	private Point destPoint;
	
	// gliding
	private boolean gliding;
	
	// animation
	transient private ArrayList<BufferedImage[]> sprites;
	
	//number of frames in movement
	private int[] numFrames = { 2, 6, 1, 2, 2, 3, 3 };

	//animations actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int FIRING = 4;
	private static final int RELOADING = 5;
	private static final int GLIDING = 6;
	
	
	 /**
     * Constructs a new {@code Player}
     * @param     tm TileMap of player
     */
	public Player(TileMap tm)
	{
		
		super(tm);
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 30;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;	
		reload = false;
		loseOneTeamPoint = false;
		health = maxHealth = 5;
		fire = maxFire = 10;
		
		fireCost = 1;
		bullets = new ArrayList<Bullet>();
		
		setImage();
		isTextured = true;
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
	}
	
	/**
     * Setter for whole texture and sprites for player
     */
	public void setImage()
	{
		//load sprite
		try
		{
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResource(
							"/Sprites/Player/player.png"
					) 
			);
			
			sprites = new ArrayList<BufferedImage[]>();
			
			for(int i = 0; i < 7; i++)
			{
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for( int j = 0; j < numFrames[i]; j++)
				{
					if( i != GLIDING )
					{
						bi[j] = spritesheet.getSubimage(
								j * width ,
								i * height,
								width,
								height
						);
						continue;
					}
					bi[j] = spritesheet.getSubimage(
							j * width ,
							i * height,
							width,
							height + 15
					);

					
				}
				
				sprites.add(bi);
			}
			
		}catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
     * Setter for animation player
     */
	public void setAnimation()
	{
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(animation.getDelay());
		
	}
	/**
     * Getter for current health of player
     * @return {@code health}
     */
	public int getHealth() { return health; }
	/**
     * Getter for max health of player
     * @return {@code maxHealth}
     */
	public int getMaxHealth() { return maxHealth; }
	/**
     * Getter for avilable ammo of player
     * @return {@code fire}
     */
	public int getFire() { return fire; }
	/**
     * Getter for max ammo of player
     * @return {@code maxFire}
     */
	public int getMaxFire() { return maxFire; }
	/**
     * Getter for ID of player
     * @return {@code id}
     */
	public int getID() { return id; }
	/**
     * Getter for Texture statement
     * @return {@code isTextured}
     */
	public boolean getTextured() { return isTextured; }
	/**
     * Getter for starting point of player
     * @return {@code startPosition}
     */
	public StartPoint getStartPoint() { return startPosition; }
	/**
     * Getter for loseOneTeamPoint statement
     * @return {@code loseOneTeamPoint}
     */
	public boolean getLosePointTeam() { return loseOneTeamPoint; }
	/**
     * Getter for TileMap of player
     * @return {@code tileMap}
     */
	public TileMap getTM() { return tileMapStuff.getTileMap() ; }
	/**
     * Getter for direction of player
     * @return {@code facingRight}
     */
	public boolean getFacingRight() {	return facingRight;	}
	/**
     * Getter for current animation
     * @return {@code currentAction}
     */
	public int getCurrentAction() { return currentAction; }
	/**
     * Getter for bullet of player
     * @return {@code bullets}
     */
	public ArrayList<Bullet> getPlayerBullet() { return bullets; }
	
	/**
     * Set realoading statement
     */
	public void setReloading() { reloading = true;	}
	/**
     * Set bullet point to travel 
     * @param destPoint point of bullet aim
     */
	public void setFiring(Point destPoint){ if(!reload) firing = true; this.destPoint = destPoint; }
	/**
     * Set gliding statement
     * @param b set state for gliding animation 
     */
	public void setGliding(boolean b) { gliding = b ; }	
	/**
     * Set unic ID to player
     * @param id ID that player get from server
     */
	public void setID(int id) { this.id = id; } 
	/**
     * Set statement of textured player
     * @param is check if player was textured once
     */
	public void setTextured(boolean is) { isTextured = is; }
	/**
     * set start position to player,depending on the team
     * @param p StartPoint is class of starting position of team and also has team name
     */
	public void setStartPosition(StartPoint p) { this.startPosition = p; setPosition(p.getPoint().x , p.getPoint().y); }
	/**
     * If player was respawn cannot still lose team points
     */
	public void setBackPlayerToGame() { this.loseOneTeamPoint = false; }
	
	
	/**
     * Set position to player
     * set vector to player of maxspeed
     * set possible move to player, and prevent from unexpected move
     */
	private void getNextPosition()
	{
		// movement
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
		}else {
			if(dx > 0)
			{
				dx -= stopSpeed;
				if( dx < 0 )
					dx = 0;
			}
			else if( dx < 0 )
			{
				dx += stopSpeed;
				if( dx > 0 )
					dx = 0;
			}
		}
		
		// cannot move while attacking
		
		if(( currentAction == FIRING) && !(jumping || falling))
			dx = 0;
		//jumping
		if(jumping && !falling)
		{
			dy = jumpStart;
			falling = true;
		}
		
		//falling
		if(falling)
		{
			if(dy > 0 && gliding) dy += fallSpeed * 0.1;
			else dy +=fallSpeed;
			
			if(dy > 0 ) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		
	}
	
	
	/**
     * Update player
     * set player position, calc vector, check for collision with map object
     * Also using for update animation
     * @see #getNextPosition()
     * @see #checkTileMapCollision()
     * @see #setPosition(double x, double y)
     * @see #updateBullets()
     */
	public void update()
	{
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//check reloading has stopped
		if(currentAction == RELOADING)
		{
			if(animation.hasPlayedOnce()) reloading = false;
			fire = maxFire;
			reload = false;
		}
		//check firing has stopped
		if(currentAction == FIRING)
			if(animation.hasPlayedOnce()) firing = false;
		
		//Bullet shoot and attack
		if(fire >= maxFire ) fire = maxFire;
		if(fire < fireCost) reload = true;
		if(firing && currentAction != FIRING && reload != true && currentAction != RELOADING)
			if(fire >= fireCost)
			{
				fire -= fireCost;
				Bullet bullet = new Bullet(tileMapStuff.getTileMap(), destPoint, facingRight );
				bullet.setPosition(x,  y);
				bullet.calcVector();
				bullets.add(bullet);
			}
		
		//update bullets
		updateBullets();
		
		//check done flinching
		if(flinching)
		{
			long elapsed = (System.nanoTime() - flinchTime) / 1000000;
			if(elapsed > 1000)
			{
				flinching = false;
			}
		}
		
		//set animation
		if(reloading)
		{
			if(currentAction != RELOADING) 
			{
				currentAction = RELOADING;
				animation.setFrames(sprites.get(RELOADING));
				animation.setDelay(50);
				width = 30;
			}
		}
		else if(firing)
		{
			if(currentAction != FIRING) 
			{
				currentAction = FIRING;
				animation.setFrames(sprites.get(FIRING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if( dy > 0 )
		{
			if(gliding)
			{
				if(currentAction != GLIDING)
				{
					currentAction = GLIDING;
					animation.setFrames(sprites.get(GLIDING));
					animation.setDelay(100);
					width = 30;
				}
			}
			else if(currentAction != FALLING)
			{
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}
		}
		else if( dy < 0)
		{
			if(currentAction != JUMPING)
			{
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else if( left || right)
		{
			if(currentAction != WALKING)
			{
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		}else {
			if(currentAction != IDLE)
			{
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 30;
			}
		}
		
		animation.update();
		
		//set direction
		if(currentAction != RELOADING && currentAction != FIRING)
		{
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
	}

	
	
	/**
     * Update others players on map current player
     * @see #setPosition(double x, double y)
     * @param p the player from package of ServerPlayers
     * @param tm TileMap of current player
     */
	public void updateFromServer(Player p, TileMap tm)
	{
		tileMapStuff.setTM(tm);
		setPosition(p.getX(), p.getY());
		this.facingRight = p.facingRight;
		animation.setFrames(sprites.get(p.currentAction));
		animation.setDelay(animation.getDelay());
		updateBullets(p, tm);
	}
	
	/**
     * Function to update bullets of other players on map
     * @param p the other player
     * @param tm TileMap of current player
     */
	private void updateBullets(Player p, TileMap tm)
	{
		if( bullets.size() != p.bullets.size() )
		{
			bullets = p.bullets;
			for(int j = 0; j < bullets.size(); j++)
			{
				bullets.get(j).loadSprites();
				bullets.get(j).setTM(tm);
			}
			return;
		}

		for(int i = 0; i < bullets.size(); i++)
			bullets.get(i).updateFromServer(tm, p.bullets.get(i));
	}
	/**
     * Update player bullets
     * update bullet position, animation, check for collision
     * delete if was hited
     */
	private void updateBullets()
	{
		for(int i=0; i< bullets.size(); i++)
		{
			bullets.get(i).update();
			if(bullets.get(i).shouldRemove())
			{
				bullets.remove(i);
			}
		}
	}
	

	/**
     * Function to draw players
     * Also draw bullets
     * If player got hit and is flinching check for elapsed time
     * Checking for facing Right or Left, and then draw on screen
     * @param g the specified frame Graphics
     */
	public void draw(Graphics2D g)
	{
		setMapPositon();
		// draw bullets
		for(int i=0; i <bullets.size(); i++)
		{
			bullets.get(i).draw(g);
		}


		if(flinchingDone())
			return;
		
		// draw player
		if(!facingRight)
		{
			if(currentAction != GLIDING)
			{
				g.drawImage(
						animation.getImage(),
						(int)(x + tileMapStuff.getXmap() - width / 2 + width),
						(int)(y + tileMapStuff.getYmap() - height / 2),
						-width,
						height,
						null 
				);
				return;
			}
			
			g.drawImage(
					animation.getImage(),
					(int)(x + tileMapStuff.getXmap() - width / 2 + width),
					(int)(y + tileMapStuff.getYmap() - height / 2),
					-width,
					height + 15,
					null 
			);
			
			return;
		}
		g.drawImage(
				animation.getImage(),
				(int)(x + tileMapStuff.getXmap() - width / 2 ),
				(int)(y + tileMapStuff.getYmap() - height /2),
				null 
		);
			
	}
	
	/**
     * return state of flinching
     * @return    {@code true} if flinch time is done
     * 			  {@code false} if flinch still is on
     */
	private boolean flinchingDone() 
	 {
		 if(flinching)
			{
				long elapsed = (System.nanoTime() - flinchTime) / 1000000;
				if(elapsed / 100 % 2 == 0)
				{
					return true;
				}
			}
		 return false;
	 }
	
	/**
     * Return temporary x of player
     * @return    {@code (xtemp)}
     */
	public int getXtemp(){	return (int)xtemp;	}
	/**
     * Return temporary y of player
     * @return    {@code (ytemp)}
     */
	public int getYtemp(){	return (int)ytemp;	}
	

	/**
     * Check for attack from enemy players
     * Loop through all players on server
     * If player is in the same team, skip one iteration
     * if enemy bullet intersects to player set hit
     * if enemy player intersect to player also set hit
     * 
     * @see #intersects(MapObject o)
     * @see #hit(int damage)
     * @param Enemy players from server {@code enemyPlayers}
     */
	public void checkAttack(ArrayList<Player> enemyPlayers)
	{
		for(int i = 0 ; i < enemyPlayers.size(); i++)
		{
			Player p = enemyPlayers.get(i);

			if( ( p.getID() == id ) || ( p.getStartPoint().getName().equals( startPosition.getName() ) ) )
				continue;
			for(int j = 0; j < p.bullets.size() ; j++)
			{
				if(intersects(p.bullets.get(i)) )
				{
					hit(1);
					p.bullets.get(i).setHit();
					break;
				}
					
			}
			if(intersects(p))
			{
				hit(1);
			}
		}
	}

	/**
     * Minus damage health from player lifes
     * If player was hited and still flinching return
     * When player lifes is 0 set dead
     * @param how much damage will minus from player lifes {@code damage}
     */
	private void hit(int damage)
	{
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if( health == 0) dead = true;
		flinching = true;
		flinchTime = System.nanoTime();
		
	}
	
	/**
     * Checking if player is dead
     * when dead subtracts one team point and respawn in starting point
     */
	public void isDead()
	{
		if(y<0 || health==0)  
		{
			dead = true; 
			loseOneTeamPoint = true;
			if(falling)
				falling = false;
			
		}
		
		if(dead)
		{
			setPosition(startPosition.getPoint().x,
					startPosition.getPoint().y);
			dead=false;
			health=maxHealth;
		}
			
	}
	
	/**
	 * Own method to readObject
     * Method to receive chosen data from player
     */
	/*@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream objIn) throws ClassNotFoundException, IOException 
	  {  

	    setPosition( (double) objIn.readObject() , (double)objIn.readObject());
	    this.facingRight = (boolean) objIn.readObject();
	    this.currentAction = (int) objIn.readObject();
	    this.bullets = (ArrayList<Bullet>) objIn.readObject();
	    this.id = (int) objIn.readObject();
	    this.numFrames = (int[]) objIn.readObject();
	    this.width = (int) objIn.readObject();
	    this.height = (int) objIn.readObject();
	    this.cwidth = (int) objIn.readObject();
	    this.cheight = (int) objIn.readObject();
	    this.xtemp = (double) objIn.readObject();
	    this.ytemp = (double) objIn.readObject();
	    this.health = (int) objIn.readObject();
	    this.tileMapStuff = (ObjectTileStuff) objIn.readObject();
	    
	    this.topLeft = (boolean) objIn.readObject();
		this.topRight = (boolean) objIn.readObject();
		this.bottomLeft = (boolean) objIn.readObject();
		this.bottomRight = (boolean) objIn.readObject();
		
		//this.currCol = (int) objIn.readObject();
		//this.currRow = (int) objIn.readObject();
	    
	  }*/
	 
	/**
	 * Own method to writeObject
     * Method to send chosen data from player to server
     */
	  /*private void writeObject(ObjectOutputStream objOut) throws IOException 
	  {
		  objOut.writeObject(x);
		  objOut.writeObject(y);
		  objOut.writeObject(facingRight);
		  objOut.writeObject(currentAction);
		  objOut.writeObject(bullets);
		  objOut.writeObject(id);
		  objOut.writeObject(numFrames);
		  objOut.writeObject(width);
		  objOut.writeObject(height);
		  objOut.writeObject(cwidth);
		  objOut.writeObject(cheight);
		  objOut.writeObject(xtemp);
		  objOut.writeObject(ytemp);
		  objOut.writeObject(health);
		  objOut.writeObject(tileMapStuff);
		  
		  objOut.writeObject(topLeft);
		  objOut.writeObject(topRight);
		  objOut.writeObject(bottomLeft);
		  objOut.writeObject(bottomRight);
		  
		 // objOut.writeObject(currCol);
		 // objOut.writeObject(currRow);
	  } */

}