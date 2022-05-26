package ServState;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.awt.Point;
import java.io.*;

import Entity.*;
import Main.Game;

public class Server 
{
	private ServerSocket servSocket; 
	private Socket socket;
	private InetAddress ipv4Addr;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	private Semaphore sem;
	protected ArrayList<StartPoint> startingPoints;
	
	static int numOfPlayers;
	static int[] lifeOfTeam = { 1, 1 };
	
	public static transient ArrayList<Player> players;
	
	public Server() throws ClassNotFoundException
	{
		try
		{
			createTeam();
			players = new ArrayList<Player>();
			ipv4Addr = InetAddress.getByName("127.0.0.1");
			servSocket = new ServerSocket(5050, 50, ipv4Addr);
			sem = new Semaphore(1);
			System.out.println("Server started");
		} catch (IOException e) 
		{
			System.out.println(e.getMessage());
		}
		while(true)
		{
			try 
			{
				socket = servSocket.accept();
				objOut = new ObjectOutputStream(socket.getOutputStream());
				objOut.flush();
				objIn = new ObjectInputStream(socket.getInputStream());

				System.out.println("Client connected");
				
				Thread thread = new ClientHandler(socket,
						objIn, 
						objOut,
						sem,
						getStart(
								 (int) objIn.readObject()
								)
						);
				
				thread.setDaemon(true);
				thread.start();
				
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			
		}
	}
	
	private void createTeam() 
	{
		StartPoint a = new StartPoint(new Point(100, 100), "A");
		StartPoint b = new StartPoint(new Point(700, 100), "B");
		startingPoints = new ArrayList<StartPoint>();
		startingPoints.add(a);
		startingPoints.add(b);
		
	}

	public StartPoint getStart(int choose)
	{
		return	startingPoints.get(choose);
	}
}
