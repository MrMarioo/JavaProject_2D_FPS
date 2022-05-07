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
import java.util.concurrent.Semaphore;

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
	
	private Semaphore sem;
	
	public ClientHandler(Socket socket, DataInputStream dataIn, DataOutputStream dataOut, ObjectInputStream objIn, ObjectOutputStream objOut, Semaphore sem)
	{
		this.socket = socket;
		this.dataIn = dataIn;
		this.dataOut = dataOut;
		this.objIn = objIn;
		this.objOut = objOut;
		this.sem = sem;
	}

	public void update() throws IOException
	{
		try 
		{
			this.player = (Player) objIn.readUnshared();
			sem.acquire();
			
			Server.players.set(player.getID()-1, player);
			if(Server.players.size() == 1)
			{
				
				objOut.writeObject(Server.players.get(0));
				objOut.reset();
			}else
			{
				objOut.writeObject(Server.players.get(1));
				objOut.reset();
			}
				
			
			sem.release();
			
			
			
			
		} catch (ClassNotFoundException | IOException | InterruptedException e) {

			
			if( e.getMessage() == "Connection reset")
			{
				socket.close();
				System.out.println("Client  disconnected");
			}
			else
			{
				System.out.println(e.getMessage());
			}
				
		}
	}
	@Override
	public void run()
	{
		try 
		{
			nick = (String) objIn.readObject();
			objOut.writeObject("Hello: "+nick);
			objOut.reset();
			objOut.writeObject("true");
			objOut.reset();
			sem.acquire();
			
			Server.numOfPlayers++;
			objOut.writeObject(Server.numOfPlayers);
			objOut.reset();
			this.player = (Player) objIn.readUnshared();
			Server.players.add(player);
			
			sem.release();
			while(true)
			{
				if(!socket.isClosed())
					update();
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
}

