import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server 
{
	//initialize socket and input stream
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private BufferedReader br = null;
    private PrintStream dataToClient = null;
    
    // list of players and name lobby server
    private ArrayList<String> players = null;
    private String lobby;
    public Server(String lobby, int port)
    {
        // starts server and waits for a connection
        try
        {
        	this.lobby = lobby;
            server = new ServerSocket(port);
            System.out.println("Server started");
 
            System.out.println("Waiting for a client ...");
            
            // connect it to client socket
            socket = server.accept();
            
            System.out.println("Client accepted");
            
            // to send data to client
            dataToClient = new PrintStream(socket.getOutputStream());
           
            // to read data coming from the client  
            br = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
            //initialize ArrayList
            players = new ArrayList<String>();
            

        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    
    public void getData()
    {
    	
    	try {
	    		String cLobby;
	    		System.out.println("Waiting for data");
				cLobby = br.readLine();
				if(cLobby.equals(lobby))
			    {
					dataToClient.println("T");
			    	String login= br.readLine();
			    	boolean isExist = false;
			    	if(players.isEmpty())
			    		players.add(login);
			    	else
			    	{
			    		for( String x : players)
				    		if(login == x)
				    			isExist=true;
			    	}
			    	
			    	if(!isExist)
			    	{
			    		players.add(login);             
			            System.out.println("Client accepted");
			            System.out.println("witaj: "+login);
			    	}
			    	else
			    	{
			    		System.out.println("Client denied");
			     		socket.close();
			    	}		
			    }
				else
				{
					dataToClient.print("F");
					finalize();
				}
		} 
    	catch (IOException e) 
    	{
    		System.out.println(e);
		}
    	catch (Throwable e)
    	{
    		System.out.println(e);
    	}
    }
    
    protected void finalize() throws Throwable
   	{	
    	System.out.println("clossing connection");
   		 try
            {
	   			 socket.close();
	             System.out.println("connection closed");
            } catch(IOException i)
            {
                System.out.println(i);
            }
   	}
	
}
