package Main;
import java.net.*;
import java.io.*;
import java.util.*;
public class Client 
{
	 // initialize socket and input output streams
    private Socket socket            = null;
    private DataOutputStream out     = null;
    private BufferedReader keyBoard = null;
    private BufferedReader servData = null;
    
    // var to login and nameLobby
    private String login;
    private String nameLobby;
 
    // constructor to put ip address and port
    @SuppressWarnings("deprecation")
	public Client(String address, int port) throws IOException
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");
 
 
            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
            
            // to read data from the sever
            servData = new BufferedReader(
                    	new InputStreamReader(
                            socket.getInputStream()));
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
        
        // to read data from the keyboard
        //  keyBoard = new BufferedReader( new InputStreamReader(System.in));
        // string to read message from input
        //String str;
 
        // keep reading until "exit" is input
       /* do
        {
        	str = keyBoard.readLine();
        	// send to the server
        	(str + "\n");
        }
        while (!(str.equals("exit")));*/
       
    }
    
    //setters
    public void setLog(String login)
    {
    	this.login = login;
    }
    public void setServerName(String nameLobby)
    {
    	this.nameLobby = nameLobby;
    }
    
    //send data to server
    public void sendToServer()
    {
    	String sData;
    	try
    	{
			out.writeBytes(nameLobby + "\n");
			sData = servData.readLine();
			System.out.println("Odebrano: "+sData);
			if(sData.equals("T"))
				out.writeBytes(login+"\n");
			else
				finalize();
			out.writeBytes("exit");
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
    
    //finalize and close the connection
    protected void finalize() throws Throwable
	{	
		 try
         {
         	keyBoard.close();
             out.close();
             socket.close();
             System.out.println("zakoonczono polaczenie");
         }
         catch(IOException i)
         {
             System.out.println(i);
         }
	}
}
