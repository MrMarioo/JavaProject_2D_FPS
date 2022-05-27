package ServerMessage;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import Entity.Bullet;
import TileMap.TileMap;

public class UpdatePlayer implements Serializable
{
	private TileMap tm;
	private Point cordPlayer;
	private boolean facingRight;
	private int currentAction;
	private ArrayList<Bullet> bullets;
	
	public UpdatePlayer(TileMap tm, Point cordPlayer, boolean facingRight, int currentAction, ArrayList<Bullet> bullets)
	{
		this.tm = tm;
		this.cordPlayer = cordPlayer;
		this.facingRight = facingRight;
		this.currentAction = currentAction;
		this.bullets = bullets;
	}
	
	public TileMap getTM() { return tm; }
	public Point getCord() { return cordPlayer; }
	public boolean getFacing() { return facingRight; }
	public int getCurrentAction() { return currentAction; }
	public ArrayList<Bullet> getBullets() { return bullets; }
}
