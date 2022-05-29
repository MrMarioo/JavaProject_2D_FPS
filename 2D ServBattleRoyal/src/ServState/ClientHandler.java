package ServState;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Semaphore;

import Entity.*;

@SuppressWarnings("serial")
public class ClientHandler extends Thread implements Serializable
{	
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	private final Socket socket;
	private StartPoint teamStart;
	
	private String nick;
	Player player;
	
	
	private Semaphore sem;
	
	public ClientHandler(Socket socket, ObjectInputStream objIn, ObjectOutputStream objOut, Semaphore sem, StartPoint st) throws SocketException
	{
		this.socket = socket;
		this.socket.setTcpNoDelay(true);
		this.objIn = objIn;
		this.objOut = objOut;
		this.sem = sem;
		this.teamStart = st;
	}

	public void update() throws IOException, InterruptedException
	{
		try 
		{
			
			
			this.player = (Player) objIn.readObject();
			//sem.acquire();
			
			
			
			if(player.getLosePointTeam())
			{
				if(teamStart.getName().equals("A"))
					Server.lifeOfTeam[0]--;
				else
					Server.lifeOfTeam[1]--;
				System.out.println(Server.lifeOfTeam[0]);
			}
			
			objOut.writeObject( Server.lifeOfTeam);	
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
				//sem.release();
				return;
			}	
			
		

			for(int i = 0; i < Server.players.size(); i++ )
			{
				objOut.writeObject(Server.players.get(i));
				objOut.flush();
				objOut.reset();
				
			}
			//sem.release();

			
			
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
			objOut.writeObject(teamStart);
			nick = (String) objIn.readObject();
			objOut.flush();
			objOut.reset();
			objOut.writeObject("Hello: "+nick);
			objOut.flush();
			objOut.reset();
			objOut.writeObject("true");
			objOut.flush();
			objOut.reset();
			
			//sem.acquire(); 	
			
			Server.numOfPlayers++;
			objOut.writeObject(Server.numOfPlayers);
			objOut.flush();
			objOut.reset();
			this.player = (Player) objIn.readObject();
			player.setTextured(false);
			Server.players.add(player);
			
			//sem.release();
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

