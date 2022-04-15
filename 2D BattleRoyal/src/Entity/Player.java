package Entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Main.GamePanel;
import TileMap.*;

public class Player extends MapObject
{
	//player stuff
	private int health;
	private int maxHealth;
	private boolean dead;
	private int fire;
	private int maxFire;
	private boolean flinching;
	private long flinchTime;
	
	//bullets
	private boolean firing;
	private int fireCost;
	private int bulletBallDamage;
	private ArrayList<Bullet> bullets;
	private boolean reload;
	
	//ArrayList for bullets
	
	// scratch attack
	private boolean scratching; 
	private int scratchDamage;
	private int scratchRange;
	
	// gliding
	private boolean gliding;
	
	// animation
	ArrayList<BufferedImage[]> sprites;
	//number of frames in movement
	private final int[] numFrames = { 2, 6, 1, 2, 2, 2, 5 };

	//animations actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int FIREBALL = 4;
	private static final int GLIDING = 5;
	private static final int SCRATCHING = 6;
	
	private HashMap<String, AudioPlayer> sfx;
	
	private Point destPoint;
	
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
		
		health = maxHealth = 5;
		fire = maxFire = 25;
		
		fireCost = 1;
		bulletBallDamage = 5;
		bullets = new ArrayList<Bullet>();
		
		scratchDamage = 8;
		scratchRange = 40; // in pixels
		
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
				for( int j = 0; j< numFrames[i]; j++)
				{
					if( i == GLIDING )
					{
						bi[j] = spritesheet.getSubimage(
								j * width ,
								i * height,
								width,
								height + 15
						);
					}else {
						bi[j] = spritesheet.getSubimage(
								j * width ,
								i * height,
								width,
								height
						);
					}
					
				}
				
				sprites.add(bi);
			}
			
		}catch( Exception e)
		{
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		sfx.put("scratch", new AudioPlayer("/SFX/scratch.mp3"));
		sfx.put("fire",  new AudioPlayer("/SFX/ak74-fire.wav"));
		
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getFire() { return fire; }
	public int getMaxFire() { return maxFire; }
	
	public void setFiring(Point destPoint){ if(!reload) firing = true; this.destPoint = destPoint; }
	public void setScratching() { scratching = true; }
	public void setGliding(boolean b) { gliding = b ; }	
	
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
				if( dx < 0)
					dx = 0;
			}
			else if( dx < 0)
			{
				dx += stopSpeed;
				if(dx > 0)
					dx = 0;
			}
		}
		
		// cannot move while attacking
		
		if((currentAction == SCRATCHING || currentAction == FIREBALL) && !(jumping || falling))
			dx = 0;
		//jumping
		if(jumping && !falling)
		{
			sfx.get("jump").play();
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
	
	public void update()
	{
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//check attack has stopped
		if(currentAction == SCRATCHING)
		{
			if(animation.hasPlayedOnce()) scratching = false;
		}
		if(currentAction == FIREBALL)
			if(animation.hasPlayedOnce()) firing = false;
		
		//fireball attack
		if(fire > maxFire ) fire = maxFire;
		if(fire <= fireCost) reload = true;
		if(firing && currentAction != FIREBALL && reload != true)
				if(fire > fireCost)
				{
					sfx.get("fire").play();
					fire -= fireCost;
					Bullet bullet = new Bullet(tileMap, destPoint, facingRight );
					bullet.setPosition(x,  y);
					bullet.calcVector();
					bullets.add(bullet);
				}
		//update bullets
		
		for(int i=0; i< bullets.size(); i++)
		{
			bullets.get(i).update();
			if(bullets.get(i).shouldRemove())
			{
				bullets.remove(i);
			}
		}
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
		if(scratching)
		{
			if(currentAction != SCRATCHING) 
			{
				sfx.get("scratch").play();
				currentAction = SCRATCHING;
				animation.setFrames(sprites.get(SCRATCHING));
				animation.setDelay(50);
				width = 60;
			}
		}
		else if(firing)
		{
			if(currentAction != FIREBALL) 
			{
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(FIREBALL));
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
		if(currentAction != SCRATCHING && currentAction != FIREBALL)
		{
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
	}
	
	public void draw(Graphics2D g)
	{
		setMapPositon();
		// draw bullets
		for(int i=0; i< bullets.size(); i++)
		{
			bullets.get(i).draw(g);
		}
		
		// draw player
		if(flinching)
		{
			long elapsed = (System.nanoTime() - flinchTime) / 1000000;
			if(elapsed / 100 % 2 == 0)
			{
				return;
			}
		}
		
		if(facingRight)
		{
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2),
					(int)(y + ymap - height /2),
					null 
			);
		}else {
			if(currentAction != GLIDING)
			{
				g.drawImage(
						animation.getImage(),
						(int)(x + xmap - width / 2 + width),
						(int)(y + ymap - height / 2),
						-width,
						height,
						null 
				);
			}else {
				g.drawImage(
						animation.getImage(),
						(int)(x + xmap - width / 2 + width),
						(int)(y + ymap - height / 2),
						-width,
						height + 15,
						null 
				);
			}
			
		}
	}
	public int getXtemp(){	return (int)xtemp;	}
	public int getYtemp(){	return (int)ytemp;	}
	public void checkAttack(ArrayList<Enemy> enemies) 
	{
		// loop through enemies
		
		for(int i = 0 ; i < enemies.size(); i++)
		{
			Enemy e = enemies.get(i);
			
			//scratch attack
			
			if(scratching)
			{
				if(facingRight)
				{
					if(e.getX() > x && e.getX() < x + scratchRange && e.getY() > y - height / 2 && e.getY() < y + height / 2)
					{
						e.hit(scratchDamage);
						
					}
				}else {
					if(e.getX() < x && e.getX() > x - scratchRange && e.getY() > y - height / 2 && e.getY() < y + height / 2)
					{
						e.hit(scratchDamage);
						
					}
				}
			}
			// bullets
			for(int j = 0; j < bullets.size(); j++) {
				if(bullets.get(j).intersects(e)) {
					e.hit(bulletBallDamage);
					bullets.get(j).setHit();
					break;
				}
			}
				
			//check for enemy collision
			if(intersects(e))
			{
				hit(e.damage);
			}
			
		}
		
	}

	private void hit(int damage)
	{
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if( health == 0) dead = true;
		flinching = true;
		flinchTime = System.nanoTime();
		
	}
	
	public void isDead()
	{
		if(y<0 || y>GamePanel.HEIGHT-height || health==0)  
		{
			dead = true; 
			if(falling)
				falling = false;
		}
		
		if(dead)
		{
			setPosition(100,100);
			dead=false;
			health=maxHealth;
		}
			
	}
}
