package ServState;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.io.*;

import Entity.*;
import Main.Game;

public class Server 
{
	private ServerSocket servSocket; 
	private Socket socket;
	private InetAddress ipv4Addr;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	private Semaphore sem;
	
	static int numOfPlayers;
	
	public static transient ArrayList<Player> players;
	
	public Server()
	{
		try
		{
			players = new ArrayList<Player>();
			ipv4Addr = InetAddress.getByName("127.0.0.1");
			servSocket = new ServerSocket(5050, 50, ipv4Addr);
			sem = new Semaphore(1);
			System.out.println("Server started");
		} catch (IOException e) 
		{
			e.printStackTrace();
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
				
				Thread thread = new ClientHandler(socket, dataIn, dataOut, objIn, objOut, sem);
				
				thread.setDaemon(true);
				thread.start();
				
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			
		}
		

		
	}
	
}
