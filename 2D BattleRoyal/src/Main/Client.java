package Main;
import java.net.*;
import java.io.*;
import java.util.*;

import Entity.Player;
public class Client implements Serializable
{
	private String ipString;
	private String nick;
	
	private String test;
	
	private InetAddress ipv4;
	private Socket socket;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	
	static public boolean isAlive;
	static public boolean isConnected;
	
	transient protected Player player;
	transient protected ArrayList<Player>playerEnemies;
	public Client( String nick, String ipString)
	{
		playerEnemies = new ArrayList<Player>();
		this.ipString = ipString;
		this.nick = nick;
		

		
		try 
		{
			ipv4 = InetAddress.getByName(ipString);
			socket = new Socket(ipv4, 5056);
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
	public void test() { System.out.println(player.getX()) ; }
	
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
			System.out.println(this.player.getX());
			objOut.reset();
			objOut.writeUnshared( this.player);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
