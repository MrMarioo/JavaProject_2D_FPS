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

	public void update() throws IOException, InterruptedException
	{
		try 
		{
			
			
			this.player = (Player) objIn.readObject();
			
			sem.acquire();
			objOut.flush();
			objOut.reset();
			
			
			Server.players.set(player.getID()-1, player);
		
			objOut.writeObject(Server.players.size());
			objOut.flush();
			objOut.reset();
			
			if( !((boolean) objIn.readObject()) )
			{

				
				for(int i = 0; i < Server.players.size(); i++ )
				{
					objOut.writeObject(Server.players.get(i));
					objOut.flush();
					objOut.reset();
				}
				sem.release();
				return;
			}	
			
		

			for(int i = 0; i < Server.players.size(); i++ )
			{
				objOut.writeObject(Server.players.get(i));
				objOut.flush();
				objOut.reset();
				
			}
			sem.release();

			
			
		} catch (ClassNotFoundException | IOException  e) {

			
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
			objOut.flush();
			objOut.reset();
			objOut.writeObject("Hello: "+nick);
			objOut.writeObject("true");
			
			sem.acquire(); 	
			
			Server.numOfPlayers++;
			objOut.writeObject(Server.numOfPlayers);
			objOut.reset();
			this.player = (Player) objIn.readObject();
			player.setTextured(false);
			Server.players.add(player);
			
			sem.release();
			while(true)
			{
				
				if(!socket.isClosed())
					update();
				
				Thread.sleep(0, 10);
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

