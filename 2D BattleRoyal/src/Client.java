import java.net.*;
import java.io.*;
import java.util.*;
public class Client 
{
	 // initialize socket and input output streams
    private Socket socket            = null;
    private DataOutputStream out     = null;
    private BufferedReader keyBoard = null;
 
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
            out    = new DataOutputStream(socket.getOutputStream());
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
        keyBoard = new BufferedReader( new InputStreamReader(System.in));
        // string to read message from input
        String str;
 
        // keep reading until "exit" is input
        do
        {
        	str = keyBoard.readLine();
        	// send to the server
        	out.writeBytes(str + "\n");
        }
        while (!(str.equals("exit")));
 
        // close the connection
        try
        {
        	keyBoard.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
}
