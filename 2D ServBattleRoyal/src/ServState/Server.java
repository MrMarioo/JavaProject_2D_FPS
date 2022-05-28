package ServState;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.awt.Point;
import java.io.*;

import Entity.*;
public class Server 
{
	private ServerSocket servSocket; 
	private Socket socket;
	private InetAddress ipv4Addr;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	private Semaphore sem;
	protected ArrayList<StartPoint> startingPoints;
	private String userIP;
	
	static int numOfPlayers;
	static int[] lifeOfTeam = { 1, 1 };

	
	public static transient ArrayList<Player> players;
	
	public Server() throws ClassNotFoundException
	{
		try
		{
			getIP();
			createTeam();
			players = new ArrayList<Player>();
			ipv4Addr = InetAddress.getByName(userIP);
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
				socket.setTcpNoDelay(true);
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
				System.out.println(e.getMessage());
			}
			
			
		}
	}
	
	private void getIP() 
	{
		System.out.println("Wprowadz ip: ");
		try (Scanner in = new Scanner(System.in)) {
			userIP = in.next();
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
