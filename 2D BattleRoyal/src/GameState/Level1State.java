package GameState;

import TileMap.*;
import TileMap.Background;
import Main.Client;
import Main.GamePanel;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import Audio.AudioPlayer;
import Entity.AimCursor;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;


public class Level1State extends GameState
{
	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Explosion> explosions;
	
	
	private HUD hud;
	
	private AudioPlayer bgMusic;
	
	private Client client;
	Thread thread;
	
	AimCursor cursor;
	
	 /**
     * Constructs a new {@code Level1State}
     * @param     gsm Game state manager
     * @param	  client socket client
     */
	Level1State(GameStateManager gsm, Client client) 
	{
		this.gsm = gsm;
		this.client = client;
		init();
	}
	
	/**
     * Function for initialization of level 1 state,textures, player, background, audio
     */
	@Override
	public void init()
	{

		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/level1TileSet.png");
		tileMap.loadMap("/Maps/level1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.07);
		
		bg = new Background("/Backgrounds/level2.png", 0.1);
		
		player = new Player(tileMap);
		try {
			player.setStartPosition(client.getStartPosition());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		System.out.println();
		
		bgMusic = new AudioPlayer("/Music/level1-1.mp3");
		bgMusic.play();
		cursor = new AimCursor();
		
		GamePanel.setDefaultCursor( cursor);
		
		client.sendHello();
		player.setID(Client.playerID);
		client.setPlayer(player);
		System.out.println(player.getID());
		client.updatePlayerOnServer();
	}

	/**
     * function for update player, server status, tile map positio, check for dead
     * @see #serverUpdate()
     */
	@Override
	public void update()
	{
		
		
		//update player
		serverUpdate();
		
		player.update();

		
		tileMap.setPosition( GamePanel.WIDTH / 2 - player.getX(),GamePanel.HEIGHT / 2 - player.getY());
		
		//set background
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
		//attack enemies
		player.checkAttack(client.playerEnemies);
		player.isDead();

		//update explosions
		
		for(int i = 0; i < explosions.size(); i++)
		{
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove())
				explosions.remove(i);
		}
	}

	/**
     * function for other players from server
     */
	public void serverUpdate()
	{
		client.setPlayer(player);
		client.updatePlayerOnServer();
		client.getPlayerFromServer(tileMap, player);
		player.setBackPlayerToGame();
		
	}
	
	/**
     * Function to draw objects from level1
     * @param g the specified frame Graphics
     */
	@Override
	public void draw(Graphics2D g) 
	{
		//draw bg
		bg.draw(g);
		
		//draw tilemap
		tileMap.draw(g);
		
		//draw player
		player.draw(g);
		//draw players
		serverDraw(g);
		
		//draw explosions
		for(int i = 0; i < explosions.size(); i++)
		{
			explosions.get(i).setMapPosition((int)tileMap.getX(), (int)tileMap.getY());
			explosions.get(i).draw(g);
		}
		
		//draw hud
		hud.draw(g);
		
		
		
	}

	/**
     *	Method for calling drawing function from client socket and draw other players from server on map
     * @param g the specified frame Graphics
     */
	public void serverDraw(Graphics2D g)
	{
		client.draw(g);
	}
	/**
     *	Method for listening the key press
     * @param k getting key cod
     */
	@Override
	public void keyPressed(int k)
	{
		if(k == KeyEvent.VK_A) player.setLeft( true );
		if(k == KeyEvent.VK_D) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_W) player.setJumping(true);
		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_R) player.setReloading();
		//if(k == KeyEvent.VK_F) player.setFiring();
	}

	/**
     *	Method for listening the key released
     * @param k getting key cod
     */
	@Override
	public void keyReleased(int k)
	{
		if(k == KeyEvent.VK_A) player.setLeft( false );
		if(k == KeyEvent.VK_D) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_W) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
	}

	/**
     *	Method for listening the mouse moved
     * @param x coordinate of mouse on window
     * @param y coordinate of mouse on window
     */
	@Override
	public void mouseMoved(int x, int y) 
	{
		cursor.setCursorPosition(
				( x / GamePanel.SCALE ) - (int)tileMap.getX(),
				( y / GamePanel.SCALE) - (int)tileMap.getY()
		);
		
	}

	/**
     *	Method for listening the mouse press
     */
	@Override
	public void mousePressed() 
	{
		player.setFiring(new Point(cursor.getX(), cursor.getY()));
	}

}
