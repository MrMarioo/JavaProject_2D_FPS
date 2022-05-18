package GameState;

import TileMap.*;
import TileMap.Background;
import Main.Client;
import Main.GamePanel;
import ServState.ClientHandler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import org.w3c.dom.events.MouseEvent;

import Audio.AudioPlayer;
import Entity.AimCursor;
import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.Enemies.Slugger;


public class Level1State extends GameState
{
	
	private int updateTicks = 0;
	private int drawTicks = 0;
	private final int MAX_TICKS = 1;
	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	//private ArrayList<Player> players;
	
	
	private HUD hud;
	
	private AudioPlayer bgMusic;
	
	private Client client;
	Thread thread;
	
	AimCursor cursor;
	
	Level1State(GameStateManager gsm, Client client) 
	{
		this.gsm = gsm;
		this.client = client;
		init();
	}
	
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
		player.setPosition(80, 80);
		
		
		//populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		System.out.println();
		
		//bgMusic = new AudioPlayer("/Music/level1-1.mp3");
		//bgMusic.play();
		cursor = new AimCursor();
		
		GamePanel.setDefaultCursor( cursor);
		
		client.sendHello();
		player.setID(Client.playerID);
		client.setPlayer(player);
		System.out.println(player.getID());
		client.updatePlayerOnServer();
	}

	@Override
	public void update()
	{
		//update player
		serverUpdate();
		
		player.update();
		
		client.update();
		
		
		tileMap.setPosition( GamePanel.WIDTH / 2 - player.getX(),GamePanel.HEIGHT / 2 - player.getY());
		
		//set background
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
		//attack enemies
		player.checkAttack(client.playerEnemies);
		//if(client.getEnemy().getID() != player.getID())
		//	player.checkAttack(client.getEnemy());
		player.isDead();
		
		
		
		
		
		
		//update enemies
		
		/*for(int i=0 ; i < enemies.size(); i++)
		{
			if(i == 0 )
				System.out.println("ENEMY: " +enemies.get(i).getPosition());
			enemies.get(i).update();
			if(enemies.get(i).isDead())
			{
				explosions.add(new Explosion(enemies.get(i).getX(), enemies.get(i).getY()));
				enemies.remove(i);
				i--;
				
			}
		}*/
		
		//update explosions
		
		for(int i = 0; i < explosions.size(); i++)
		{
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove())
				explosions.remove(i);
		}
	}

	public void serverUpdate()
	{
		if(updateTicks != MAX_TICKS)
		{
			updateTicks++;
			return;
		}
		updateTicks = 0;
		client.setPlayer(player);
		client.updatePlayerOnServer();
		client.getPlayerFromServer(tileMap, player);
		
	}
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
	
		
		
		//draw enemies
		/*for(int i=0 ; i < enemies.size(); i++)
		{
			enemies.get(i).draw(g);
		}*/
		
		//draw explosions
		for(int i = 0; i < explosions.size(); i++)
		{
			explosions.get(i).setMapPosition((int)tileMap.getX(), (int)tileMap.getY());
			explosions.get(i).draw(g);
		}
		
		//draw hud
		hud.draw(g);
		
		
		
	}

	public void serverDraw(Graphics2D g)
	{
		client.draw(g);
	}
	@Override
	public void keyPressed(int k)
	{
		if(k == KeyEvent.VK_LEFT) player.setLeft( true );
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_W) player.setJumping(true);
		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_R) player.setScratching();
		//if(k == KeyEvent.VK_F) player.setFiring();
	}

	@Override
	public void keyReleased(int k)
	{
		if(k == KeyEvent.VK_LEFT) player.setLeft( false );
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_W) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
	}

	@Override
	public void mouseMoved(int x, int y) 
	{
		cursor.setCursorPosition(
				( x / GamePanel.SCALE ) - (int)tileMap.getX(),
				( y / GamePanel.SCALE) - (int)tileMap.getY()
		);
		
	}

	@Override
	public void mousePressed() 
	{
		player.setFiring(new Point(cursor.getX(), cursor.getY()));
	}

}
