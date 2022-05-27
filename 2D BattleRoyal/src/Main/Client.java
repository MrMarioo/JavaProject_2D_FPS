package Main;
import java.net.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.*;
import java.util.*;

import Entity.Player;
import ServState.Server;
import ServState.StartPoint;
import TileMap.TileMap;
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
	
	private void sendTeamInfo(String team) throws IOException 
	{
		if(team.equals("B"))
		{
			objOut.writeObject(1);
			return;
		}
		objOut.writeObject(0);
	}

	public void setPlayer(Player player) { this.player = player; }
	
	public boolean getConnect() {return socket.isConnected(); }
	

	
	public void update()
	{
			/*for(int i = 0; i < playerEnemies.size(); i++)
			{
				if(player.getID() != playerEnemies.get(i).getID())
					playerEnemies.get(i).update();
			}*/
	}
	
	public StartPoint getStartPosition() throws ClassNotFoundException, IOException
	{ 
		teamStart = (StartPoint) objIn.readObject(); 
		return teamStart;
	}
	public void sendHello()
	{
		isAlive = true;
		try 
		{

			objOut.writeObject(nick);
			System.out.println(objIn.readObject());
			System.out.println(objIn.readObject());
			playerID = (int) objIn.readUnshared();
			objOut.flush();
			objOut.reset();
		} catch (IOException e) 
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void updatePlayerOnServer()
	{
		try 
		{
			objOut.flush();
			objOut.reset();
			objOut.writeObject( this.player);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Player getEnemy() { return enemyPlayer; }
	public void getPlayerFromServer(TileMap tm, Player p) 
	{
		try 
		{
			teamsLifes = (int[]) objIn.readObject();
			checkVictory(teamsLifes);
			
			int size = (int) objIn.readObject();
			if( size != playerEnemies.size() )
			{
				objOut.writeObject(false);
				
				
				playerEnemies = new ArrayList<Player>();
				
				for(int i = 0; i < size; i++)
				{
					enemyPlayer = (Player) objIn.readObject();
					playerEnemies.add(enemyPlayer);
					playerEnemies.get(i).setImage();
					playerEnemies.get(i).setAnimation();
				}
				return;
			}
			
			objOut.writeObject(true);

			for(int i = 0; i < size; i++)
			{
				enemyPlayer = (Player) objIn.readObject();
				if(playerEnemies.get(i).getID() != player.getID())
				{
					playerEnemies.get(i).updateFromServer(enemyPlayer, tm);
				}
				
			}
			
		} catch (ClassNotFoundException | IOException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
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

	public void draw(Graphics2D g)
	{
		for(int i = 0; i < playerEnemies.size(); i++)
		{
			if(playerEnemies.get(i).getID() != player.getID())
			{
				playerEnemies.get(i).draw(g);

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
