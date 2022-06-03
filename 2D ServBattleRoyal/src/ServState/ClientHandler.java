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
	private Player player;
	
	@SuppressWarnings("unused")
	private Semaphore sem;
	/**
     * Constructs a new {@code ClientHandler}
     * @param     socket to connect with client
     * @param	  objIn to get data from client
     * @param	  objOut to send data to client
     * @param	  sem to synchronize threads with server
     * @param	  st start points and name teams
     */
	public ClientHandler(Socket socket, ObjectInputStream objIn, ObjectOutputStream objOut, Semaphore sem, StartPoint st) throws SocketException
	{
		this.socket = socket;
		this.socket.setTcpNoDelay(true);
		this.objIn = objIn;
		this.objOut = objOut;
		this.sem = sem;
		this.teamStart = st;
	}

	/**
     * update server
     * send and receive data from client and synchronize with data on server
     * @see #checkPoinTeam()
     * @see #sendNewPlayersToClient()
     * @see #updatePlayerOnClient()
     */
	public void update() throws IOException, InterruptedException
	{
		try 
		{
			
			
			this.player = (Player) objIn.readObject();
			
			
			checkPoinTeam();
			
			Server.players.set(player.getID()-1, player);
			
			objOut.writeObject(Server.players.size());
			
			if( !((boolean) objIn.readObject()) )
			{
				sendNewPlayersToClient();
				
				return;
			}	
			updatePlayerOnClient();
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
	/**
     * Send whole array of players from server to client
     */
	private void updatePlayerOnClient() throws IOException 
	{
		for(int i = 0; i < Server.players.size(); i++ )
			objOut.writeObject(Server.players.get(i));
			
		
	}
	/**
     * Send whole array of old and new players from server to client
     */
	private void sendNewPlayersToClient() throws IOException
	{
		for(int i = 0; i < Server.players.size(); i++ )
			objOut.writeObject(Server.players.get(i));
		
	}

	/**
     * Check point team, if one player was dead decrement one point
     */
	private void checkPoinTeam() throws IOException 
	{
		if(player.getLosePointTeam())
		{
			if(teamStart.getName().equals("A"))
				Server.lifeOfTeam[0]--;
			else
				Server.lifeOfTeam[1]--;
			System.out.println(Server.lifeOfTeam[0]);
		}
		
		objOut.writeObject( Server.lifeOfTeam);	
	}

	/**
     * Default function with thread work, initializa connection with client
     * create loop for receiving and sending data
     */
	@Override
	public void run()
	{
		try 
		{
			stabilizationConnection();
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

	/**
     * Function to check connection with player
     */
	private void stabilizationConnection() throws IOException, ClassNotFoundException
	{
		objOut.writeObject(teamStart);
		nick = (String) objIn.readObject();
		objOut.writeObject("Hello: "+nick);
		objOut.writeObject("true");
		
		//sem.acquire(); 	
		
		Server.numOfPlayers++;
		objOut.writeObject(Server.numOfPlayers);
		objOut.flush();
		objOut.reset();
		this.player = (Player) objIn.readObject();
		player.setTextured(false);
		Server.players.add(player);
		
	}
}

