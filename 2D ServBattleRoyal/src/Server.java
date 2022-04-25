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
	
	public Server()
	{
		try
		{
			ipv4Addr = InetAddress.getByName("127.0.0.1");
			servSocket = new ServerSocket(5056, 50, ipv4Addr);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		while(true)
		{
			try 
			{
				socket = servSocket.accept();
				dataIn = new DataInputStream(socket.getInputStream());
				dataOut = new DataOutputStream(socket.getOutputStream());
				
				System.out.println("Client connected");
				
				Thread thread = new ClientHandler(socket, dataIn, dataOut);
						
				thread.start();
				
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			
		}
		
	}
	
}
