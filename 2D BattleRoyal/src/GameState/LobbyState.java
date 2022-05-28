package GameState;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Main.Client;
import Main.GamePanel;
import TileMap.Background;

public class LobbyState extends GameState
{

	private Background bg;
	private int currentChoice = 0;
	private String[] options = { 
			"Connect",
			"Start",
			"Back",
			"Nick",
			"IP",
			"Team"
	};
	private String nick = "Mario";
	private String ip = "127.0.0.1";
	public String team = "A";
	
	public String servStuff[] = {
			nick,
			ip,
			team
	};
	
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	private JTextField lobbyTextFields;
	
	private Client client;
	
	
	public LobbyState(GameStateManager gsm)
	{
		this.gsm = gsm;
		try
		{
			lobbyTextFields = new JTextField();
			lobbyTextFields.setPreferredSize(new Dimension(60, 60));
			lobbyTextFields.setFont(new Font("Arial", Font.PLAIN, 25));
			lobbyTextFields.setBounds(60, 60, 40, 60);
			lobbyTextFields.setText("username");
			lobbyTextFields.setBackground(new Color(0x000));
			lobbyTextFields.setCaretColor(Color.white);
			GamePanel.addStuff(lobbyTextFields);

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		
		try
		{
			bg = new Background("/Backgrounds/lobby.png", 1);
			bg.setVector(-0.1, 0);
			
			titleColor = new Color(0xffffff);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			
			font = new Font("Arial", Font.PLAIN, 12);
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(){	bg.update(); }

	@Override
	public void draw(Graphics2D g)
	{
		//draw bg
				bg.draw(g);
				
				//draw title
				g.setColor(titleColor);
				g.setFont(titleFont);
				g.drawString("LOBBY", 10 , 30);
				
				//draw menu
				g.setFont(font);
				for(int i=0; i<options.length; i++)
				{
					if( i == currentChoice)
						g.setColor(Color.white);
					else
						g.setColor(new Color(0x6e6a69));
					if(i < 3)
						g.drawString(options[i], 145, 140 + i * 15);
					else
					{
						g.drawString(options[i], 10, 20 + i * 15);
					}
						
				}
				
				g.setColor(new Color(0x6e6a69));
				
				for(int i=0; i<servStuff.length; i++)
				{	
					g.drawString(servStuff[i], 45, 65 + i * 15);
				}
		
	}

	@Override
	public void keyPressed(int k) 
	{
		if( k == KeyEvent.VK_ENTER)
			select();
		if( k == KeyEvent.VK_UP)
		{
			currentChoice --;
			if(currentChoice == -1)
				currentChoice = options.length -1;
		}
		if( k == KeyEvent.VK_DOWN)
		{
			currentChoice++;
			if(currentChoice == options.length)
				currentChoice = 0;
		}
		
	}

	private void select() 
	{
		switch(currentChoice)
		{
		case 0:
			if( !Client.isAlive )
			{
				client = new Client(servStuff[0], servStuff[1], servStuff[2]);
			}
			break;
		case 1:
			if(Client.isConnected)
			{
				gsm.setClient(client);
				gsm.setState(GameStateManager.LEVEL1STATE);
			}
			
			break;
		case 2:
			gsm.setState(GameStateManager.MENUSTATE);
			break;
		case 3:
			servStuff[0] = JOptionPane.showInputDialog("Enter your nick");
			break;
		case 4:
			servStuff[1] = JOptionPane.showInputDialog("Enter ip adress of your serv");
			break;
		case 5:
			servStuff[2] = JOptionPane.showInputDialog("Choose a team: A or B");
			break;
		}
		
	}


	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed() {
		// TODO Auto-generated method stub
		
	}

}
