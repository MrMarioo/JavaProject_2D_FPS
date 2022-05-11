package Main;
import java.net.*;
import java.awt.Graphics2D;
import java.io.*;
import java.util.*;

import Entity.Player;
import ServState.Server;
public class Client implements Serializable
{
	private String ipString;
	private String nick;
	
	private String test;
	
	private InetAddress ipv4;
	private Socket socket;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	
	static public boolean isAlive;
	static public boolean isConnected;
	static public int playerID;
	
	
	transient protected Player player;
	transient public ArrayList<Player>playerEnemies;
	transient protected Player enemyPlayer;
	
	public Client( String nick, String ipString)
	{
		
		this.ipString = ipString;
		this.nick = nick;
		
		playerEnemies = new ArrayList<Player>();
		
		try 
		{
			ipv4 = InetAddress.getByName(ipString);
			socket = new Socket(ipv4, 5050);
			objOut = new ObjectOutputStream(socket.getOutputStream());
			objIn = new ObjectInputStream(socket.getInputStream());
			
			isConnected = true;
			
		} catch (IOException e) 
		{
			isConnected = false;
			System.out.println(e.getMessage());
		}
		
		
	}
	
	public void setPlayer(Player player) { this.player = player; }
	
	public boolean getConnect() {return socket.isConnected(); }
	

	
	public void update()
	{
		try {
			objOut.writeObject(player);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
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
	public void getPlayerFromServer() 
	{
		try 
		{
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
					System.out.println("siema");
					System.out.println("LOCAL X: " +playerEnemies.get(i).getX()+ " Serv X: "+ enemyPlayer.getX());
					playerEnemies.get(i).updateFromServer(enemyPlayer);
				}
				
			}
			
		} catch (ClassNotFoundException | IOException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void draw(Graphics2D g)
	{
		for(int i = 0; i < playerEnemies.size(); i++)
		{
			if(playerEnemies.get(i).getID() != player.getID())
			{
				playerEnemies.get(i).draw(g,1);

			}
			
		}
	}

	

	
}
