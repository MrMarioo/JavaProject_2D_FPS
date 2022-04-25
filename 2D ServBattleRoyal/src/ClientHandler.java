import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;

public class ClientHandler extends Thread
{
	private DateFormat fordate;
	private DateFormat fortime;
	
	private final DataInputStream dataIn;
	private final DataOutputStream dataOut;
	private final Socket socket;
	
	private String nick;
	private String msgBack;
	
	public ClientHandler(Socket socket, DataInputStream dataIn, DataOutputStream dataOut)
	{
		this.socket = socket;
		this.dataIn = dataIn;
		this.dataOut = dataOut;
	}
	
	@Override
	public void run()
	{

		try 
		{
			nick = dataIn.readUTF();
			dataOut.writeUTF("Hello: "+nick);
			dataOut.writeUTF("true");
			dataIn.close();
			dataOut.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
	}
}

