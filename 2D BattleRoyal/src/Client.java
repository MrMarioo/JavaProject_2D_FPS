import java.net.*;
import java.io.*;
import java.util.*;
public class Client 
{
	 // initialize socket and input output streams
    private Socket socket            = null;
    private Scanner  clientIn   = null;
    private DataInputStream input = null;
    private DataOutputStream out     = null;
 
    // constructor to put ip address and port
    @SuppressWarnings("deprecation")
	public Client(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");
 
            // takes input from terminal
            input  = new DataInputStream(System.in);
 
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
 
        // string to read message from input
        String line = "";
 
        // keep reading until "Over" is input
        while (!line.equals("Over"))
        {
            try
            {
                line = input.readLine();
                out.writeUTF(line);
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        }
 
        // close the connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
}
