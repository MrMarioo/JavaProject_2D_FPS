package Entity;

import java.awt.Rectangle;
import java.io.Serializable;

import TileMap.Tile;
import TileMap.TileMap;

@SuppressWarnings("serial")
public abstract class MapObject implements Serializable
{
	//tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	// position and vector
	
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	//dimensions
	
	protected int width;
	protected int height;
	
	//collision box
	protected int cwidth;
	protected int cheight;
	
	//collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	//animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	//movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	//movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	 /**
     * Constructs a new {@code MapObject}
     * @param     tm TileMap of Object
     */
	public MapObject(TileMap tm)
	{
		this.tileMap = tm;
		this.tileSize = tm.getTileSize();
	}
	
	/**
     * Setter
     * set TileMap to object
     * @param     tm TileMap of Object
     */
	public void setTM(TileMap tm)
	{
		this.tileMap = tm;
	}
	
	/**
     * Determines whether or not this {@code Rectangle} and the specified
     * {@code Rectangle} intersect. Two rectangles intersect if
     * their intersection is nonempty.
     *
     * @param o the specified {@code MapObject}
     * @return    {@code true} if the specified {@code Rectangle}
     *            and this {@code Rectangle} intersect;
     *            {@code false} otherwise.
     */
	public boolean intersects(MapObject o)
	{
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	/**
     * Getter
     * get {@code Rectangle} of object
     * @return {@code Rectangle} new rectangle of object
     */
	public Rectangle getRectangle()
	{
		return new Rectangle(
					(int)x - width,
					(int)y - height,
					width,
					height
				);
	}
	
	/**
     * calculateCorners for block player on the wall etc.
     * @param x the {@code x}
     * @param y the {@code y}
     */
	public void calculateCorners(double x, double y)
	{
		int leftTile = (int) (x - cwidth / 2) / tileSize;
		int rightTile = (int) (x + cwidth /2 -1 ) / tileSize;
		int topTile = (int) (y - cheight / 2) / tileSize;
		int bottomTile = (int) (y +  cheight /2 -1) / tileSize;
		
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
	}
	
	/**
     * check if player is in wall or on floor, and prevent from falling
     */
	public void checkTileMapCollision()
	{
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		
		
		calculateCorners(x, ydest);
		if(dy < 0)
			checkYVectorTopMapCollision();
			
		if(dy > 0)
			checkYVectorBottomMapCollision();
		
		
		calculateCorners(xdest, y);
		
		if(dx < 0)
			checkXVectorTopMapCollision();
		if(dx > 0)
			checkXVectorBottomMapCollision();
		
		
		if(!falling)
			checkFallingMapCollision();
			
	}
	
	/**
     * prevent falling from map
     */
	private void checkFallingMapCollision()
	{
		calculateCorners(x, ydest + 1);
		if(!bottomLeft && ! bottomRight)
			falling = true;		
	}

	/**
     * prevent entry in the wall or floor
     */
	private void checkXVectorTopMapCollision()
	{
		if(topLeft || bottomLeft)
		{
			dx = 0;
			xtemp = currCol * tileSize + cwidth / 2;
		}else
			xtemp += dx;
		
	}

	/**
     * prevent entry in the wall or floor
     */
	private void checkXVectorBottomMapCollision()
	{
		if(topRight || bottomRight)
		{
			dx = 0;
			xtemp = (currCol + 1) * tileSize - cwidth / 2;
		}else
			xtemp += dx;
		
	}

	/**
     * prevent entry in the wall or floors
     */
	private void checkYVectorBottomMapCollision() 
	{
		if(bottomLeft || bottomRight)
		{
			dy = 0;
			falling = false;
			ytemp = (currRow + 1) * tileSize - cheight / 2;
		}else
			ytemp += dy;

		
	}

	/**
     * prevent entry in the wall or floor
     */
	private void checkYVectorTopMapCollision()
	{
		if(topLeft || topRight)
		{
			dy = 0;
			ytemp = currRow * tileSize + cheight / 2;
			
		}else
			ytemp +=dy;
		
	}

	/**
     * Getter for x coordinate of object
     * @return {@code x} 
     */
	public int getX() { return (int)x; }
	/**
     * Getter for y coordinate of object
     * @return {@code y} 
     */
	public int getY() { return (int)y; }
	/**
     * Getter width of object
     * @return {@code width} 
     */
	public int getWidth() { return width;  }
	/**
     * Getter height of object
     * @return {@code height} 
     */
	public int getHeight() { return height;  }
	/**
     * Getter cwidth of object
     * @return {@code cwidth} 
     */
	public int getCWidth() { return cwidth;  }
	/**
     * Getter cheight of object
     * @return {@code cheight} 
     */
	public int getCHeight() { return cheight;  }
	/**
     * Getter x coordinate of tilemap
     * @return {@code x} 
     */
	public double getXTileMap() { return tileMap.getX();}
	
	
	/**
     * Setter x,y coordinate for object
     * @param {@code x}
     * @param {@code y}
     */
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	/**
     * Setter x,y temp coordinate of object
     * @param {@code x}
     * @param {@code y}
     */
	public void setTempPosition(double x, double y)
	{
		this.xtemp = x;
		this.ytemp = y;
	}
	/**
     * Setter x,y moving vector of object
     * @param {@code x}
     * @param {@code y}
     */
	public void setVector( double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	/**
     * Setter map position of object
     */ 
	public void setMapPositon()
	{
		xmap = tileMap.getX();
		ymap = tileMap.getY();
	}
	
	
	/**
     * Setter for left key 
     * @param {{@code boolean}
     */ 
	public void setLeft(boolean b) {this.left = b; }
	/**
     * Setter for right key 
     * @param {{@code boolean}
     */ 
	public void setRight(boolean b) {this.right= b; }
	/**
     * Setter for up key 
     * @param {{@code boolean}
     */ 
	public void setUp(boolean b) {this.up = b; }
	/**
     * Setter for down key 
     * @param {{@code boolean}
     */ 
	public void setDown(boolean b) {this.down = b; }
	/**
     * Setter for jumping 
     * @param {{@code boolean}
     */ 
	public void setJumping(boolean b) { this.jumping = b;}
	
	
	
}
