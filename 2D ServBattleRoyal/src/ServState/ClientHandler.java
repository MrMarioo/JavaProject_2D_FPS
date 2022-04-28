package ServState;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;

import Entity.*;

public class ClientHandler extends Thread implements Serializable
{
	private DateFormat fordate;
	private DateFormat fortime;
	
	private final DataInputStream dataIn;
	private final DataOutputStream dataOut;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	private final Socket socket;
	
	private String nick;
	private String msgBack;
	Player player;
	
	public ClientHandler(Socket socket, DataInputStream dataIn, DataOutputStream dataOut, ObjectInputStream objIn, ObjectOutputStream objOut)
	{
		this.socket = socket;
		this.dataIn = dataIn;
		this.dataOut = dataOut;
		this.objIn = objIn;
		this.objOut = objOut;
	}

	public void update()
	{
		try 
		{
			this.player = (Player) objIn.readUnshared();
			System.out.println(  this.player.getX());
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run()
	{
		try 
		{
			nick = (String) objIn.readObject();
			objOut.writeObject("Hello: "+nick);
			objOut.writeObject("true");
			while(true)
			{
				update();
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
}

