package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import Main.GamePanel;
import TileMap.Background;


public class MenuState extends GameState 
{
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = { "Start",
			"Help",
			"Quit" };
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	/**
     * Constructs a new {@code LobbyState}
     * @param     gsm Game state manager
     */
	public MenuState(GameStateManager gsm) 
	{
		this.gsm = gsm;
		
		try
		{
			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(-0.1, 0);
			
			titleColor = new Color(128,0,0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			
			font = new Font("Arial", Font.PLAIN, 12);
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
     * Function for initialization of level 1 state,textures, player, background, audio
     */
	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}
	/**
     * function for update player, server status, tile map positio, check for dead
     * @see #serverUpdate()
     */
	@Override
	public void update(){	bg.update(); }
	
	/**
     * Function to draw objects from level1
     * @param g the specified frame Graphics
     */
	@Override
	public void draw(Graphics2D g)
	{
		//draw bg
		bg.draw(g);
		
		//draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("SOLDAT", GamePanel.WIDTH/2 - 60 , 70);
		
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
	/**
     *	Function for select option in Lobby
     */
	private void select()
	{
		switch(currentChoice)
		{
		case 0:
			gsm.setState(GameStateManager.LOBBYSTATE);
			break;
		case 1:
			JOptionPane.showMessageDialog(null, "HELPING INFO", "INFO", JOptionPane.PLAIN_MESSAGE);
			break;
			
		case 2:
			System.exit(0);
			break;
		}
	}

	/**
     *	Method for listening the key press
     * @param k getting key cod
     */
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
	/**
     *	Method for listening the mouse moved
     * @param x coordinate of mouse on window
     * @param y coordinate of mouse on window
     */
	@Override
	public void mouseMoved(int x, int y) 
	{
		// TODO Auto-generated method stub
		
	}

	
	/**
     *	Method for listening the key released
     * @param k getting key cod
     */
	@Override
	public void keyReleased(int k) 
	{
		// TODO Auto-generated method stub

	}
	/**
     *	Method for listening the mouse press
     */
	@Override
	public void mousePressed() {
		// TODO Auto-generated method stub
		
	}


	
}
