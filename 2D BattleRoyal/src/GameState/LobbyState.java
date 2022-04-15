package GameState;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import Main.Game;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class LobbyState extends GameState
{

	private Background bg;
	private int currentChoice = 0;
	private String[] options = { "Start",
			"Back" };
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	private JTextField lobbyTextFields;
	
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
						g.setColor(Color.red);
					g.drawString(options[i], 145, 140 + i * 15);
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
			gsm.setState(GameStateManager.LEVEL1STATE);
			break;
		case 1:
			gsm.setState(GameStateManager.MENUSTATE);
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
