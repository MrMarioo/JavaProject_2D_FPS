package Main;
import java.net.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.*;
import java.util.*;

import Entity.Player;
import ServState.StartPoint;
import TileMap.TileMap;

@SuppressWarnings("serial")
public class Client implements Serializable
{
	private String nick;
	
	private InetAddress ipv4;
	private Socket socket;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	private int[] teamsLifes;
	private StartPoint teamStart;
	private boolean[] teamWin;
	
	static public boolean isAlive;
	static public boolean isConnected;
	static public int playerID;
	
	
	transient protected Player player;
	transient public ArrayList<Player>playerEnemies;
	transient protected Player enemyPlayer;
	
	/**
     * Constructs a new {@code LobbyState}
     * @param     nick nick of player
     * @param     ipString ip of server
     * @param     team team of player
     */
	public Client( String nick, String ipString, String team)
	{
		this.nick = nick;
		this.teamWin = new boolean[2];
		playerEnemies = new ArrayList<Player>();
		
		try 
		{
			ipv4 = InetAddress.getByName(ipString);
			socket = new Socket(ipv4, 5050);
			socket.setTcpNoDelay(true);
			objOut = new ObjectOutputStream(socket.getOutputStream());
			objIn = new ObjectInputStream(socket.getInputStream());
			
			sendTeamInfo(team);
			
			isConnected = true;
			
		} catch (IOException e) 
		{
			isConnected = false;
			System.out.println(e.getMessage());
		}
		
		
	}
	
	/**
     * Constructs a new {@code LobbyState}
     * @param     team send a name of team to server
     */
	private void sendTeamInfo(String team) throws IOException 
	{
		if(team.equals("B"))
		{
			objOut.writeObject(1);
			objOut.flush();
			objOut.reset();
			return;
		}
		objOut.writeObject(0);
		objOut.flush();
		objOut.reset();
	}

	/**
     * Set player to local copy of player
     * @param     player player from local game
     */
	public void setPlayer(Player player) { this.player = player; }
	/**
	 * Getter
     * Get a boolean variable that check if client is connected to server
     * return     {@code true} if socket is connected to server
     * 			{@code false} is socket is not connected to server
     */
	public boolean getConnect() {return socket.isConnected(); }
	
	/**
	 * Function to get Point with start point for current team and get team name
     */
	public StartPoint getStartPosition() throws ClassNotFoundException, IOException
	{ 
		teamStart = (StartPoint) objIn.readObject(); 
		return teamStart;
	}
	/**
	 * Send first message to server to check correct connection with server
     */
	public void sendHello()
	{
		isAlive = true;
		try 
		{

			objOut.writeObject(nick);
			objOut.flush();
			objOut.reset();
			System.out.println(objIn.readObject());
			System.out.println(objIn.readObject());
			playerID = (int) objIn.readUnshared();
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Update our player on server array of players
     */
	public void updatePlayerOnServer()
	{
		try 
		{
			objOut.writeObject( this.player);
			objOut.flush();
			objOut.reset();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * One of the most important function
	 * get new player from server
	 * or only update the most important things with local srray of players
	 * @see #checkVictory(int[])
	 * @see #getPlayerFromServer(TileMap, Player)
	 * @see #updatePlayersFromServer(int, TileMap)
     */
	public void getPlayerFromServer(TileMap tm, Player p) 
	{
		try 
		{
			teamsLifes = (int[]) objIn.readObject();
			checkVictory(teamsLifes);
			
			int size = (int) objIn.readObject();
			
			if( size != playerEnemies.size() )
			{
				getPlayersFromServer(size);
				return;
			}
			updatePlayersFromServer(size, tm);
			
			
		} catch (ClassNotFoundException | IOException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Update arraylist of enemy players
	 * set and update most important things of player class
     */
	private void updatePlayersFromServer(int size, TileMap tm) throws ClassNotFoundException, IOException 
	{
		objOut.writeObject(true);
		objOut.flush();
		objOut.reset();

		for(int i = 0; i < size; i++)
		{
			enemyPlayer = (Player) objIn.readObject();
			if(playerEnemies.get(i).getID() != player.getID())
			{
				playerEnemies.get(i).updateFromServer(enemyPlayer, tm );
			}
			
		}
		
	}

	/**
	 * If new player appears on server create new array of enemy players
	 * for new players load local textures
     */
	private void getPlayersFromServer(int size) throws IOException, ClassNotFoundException 
	{
		objOut.writeObject(false);
		objOut.flush();
		objOut.reset();
		
		playerEnemies = new ArrayList<Player>();
		
		for(int i = 0; i < size; i++)
		{
			enemyPlayer = (Player) objIn.readObject();
			playerEnemies.add(enemyPlayer);
			playerEnemies.get(i).setImage();
			playerEnemies.get(i).setAnimation();
		}
		
	}

	/**
	 * Check if one of team has no Team lifes
	 * if one of team has zero live draw appropriate inscription on the frame
     */
	private void checkVictory(int[] tf) 
	{
		if(tf[1] == 0 &&  tf[0] > 0)
		{
			teamWin[0] = true;
			teamWin[1] = false;
			return;
		}
		if(tf[0] == 0  && tf[1] > 0)
		{
			teamWin[1] = true;
			teamWin[0] = false;
		}
			
	}

	/**
     * Function to draw enemy players on local player window
     * @param g the specified frame Graphics
     */
	public void draw(Graphics2D g)
	{
		for(int i = 0; i < playerEnemies.size(); i++)
		{
			if(playerEnemies.get(i).getID() != player.getID())
			{
				playerEnemies.get(i).draw(g);
				System.out.println(playerEnemies.get(i).getX());
			}
			
		}
		
		g.setFont(
				new Font("Arial",
						Font.PLAIN,
						10)
				);
		g.setColor(Color.blue);
		g.drawString(teamsLifes[0]+" / ", GamePanel.WIDTH/2 - 15, 15);
		g.setColor(Color.red);
		g.drawString(teamsLifes[1]+"", GamePanel.WIDTH/2 + 8, 15);
		
		if(teamWin[0] || teamWin[1])
			drawWin(g);
	}

	/**
     * if one of team win draw appropriate inscription
     * @param g the specified frame Graphics
     */
	private void drawWin(Graphics2D g)
	{
		g.setFont(
				new Font("Arial",
						Font.PLAIN,
						30)
				);
		if(teamWin[0])
			g.setColor(Color.blue);
		if(teamWin[1])
			g.setColor(Color.red);
		
		if(teamWin[0])
		{
			g.drawString("TEAM A WIN", GamePanel.WIDTH/2 - 100, GamePanel.HEIGHT/2 );
			return;
		}
		g.drawString("TEAM B WIN", GamePanel.WIDTH/2 - 100, GamePanel.HEIGHT/2 );
		return;
		
	}
	
}
