import java.io.*;  
import java.net.*;  
		
public class MainServ {

	public static void main(String[] args) throws Throwable 
	{
		Server server = new Server("Lobby",5000);
		server.getData();
		server.finalize();
	}

}
