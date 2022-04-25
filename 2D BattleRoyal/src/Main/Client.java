package Main;
import java.net.*;
import java.io.*;
import java.util.*;
public class Client 
{
	private String ipString;
	private String nick;
	
	private InetAddress ipv4;
	private Socket socket;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	

	public Client( String nick, String ipString)
	{
		this.ipString = ipString;
		this.nick = nick;
		
		try 
		{
			ipv4 = InetAddress.getByName(ipString);
			socket = new Socket(ipv4, 5056);
			dataIn = new DataInputStream(socket.getInputStream());
			dataOut = new DataOutputStream(socket.getOutputStream());
			
			
			dataOut.writeUTF(nick);
			System.out.println(dataIn.readUTF());
			System.out.println(dataIn.readUTF());
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
	}
}
