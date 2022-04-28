package ServState;
import java.net.*;
import java.util.ArrayList;
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
	
	public ArrayList<Player> players;
	
	public Server()
	{
		try
		{
			players = new ArrayList<Player>();
			ipv4Addr = InetAddress.getByName("127.0.0.1");
			servSocket = new ServerSocket(5056, 50, ipv4Addr);
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
				objIn = new ObjectInputStream(socket.getInputStream());
				
				
				System.out.println("Client connected");
				
				Thread thread = new ClientHandler(socket, dataIn, dataOut, objIn, objOut);
						
				thread.start();
				
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			
		}
		
	}
	
}
