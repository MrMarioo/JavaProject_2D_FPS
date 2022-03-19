import java.net.*;
import java.io.*;

public class Server 
{
	//initialize socket and input stream
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream in       =  null;
    @SuppressWarnings("deprecation")
	public Server(int port)
    {
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");
 
            System.out.println("Waiting for a client ...");
 
            socket = server.accept();
            System.out.println("Client accepted");
 
            // takes input from the client socket
            in = new DataInputStream(
            		new BufferedInputStream(socket.getInputStream()));
            
            // to read data coming from the client
            BufferedReader br
            = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
 
            String str;
            
            // reads message from client until "Over" is sent
            while (!(str = br.readLine()).equals("exit")) 
            {
	                System.out.println(str);

	        }
            System.out.println("Closing connection");
            // close connection
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
	
}
