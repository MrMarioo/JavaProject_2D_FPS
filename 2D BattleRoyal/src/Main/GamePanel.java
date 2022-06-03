package Main;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import Entity.AimCursor;
import GameState.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener
{
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 3;
	
	//game thread
	private Thread thread;
	private boolean running;
	private int FPS = 70;
	private long targetTime = 1000 / FPS;
	
	//image
	private BufferedImage image;
	private Graphics2D g;
	
	//game stat manager
	private GameStateManager gsm;

	static GamePanel gp;
	
	/**
     * Constructs a new {@code GamePanel}
     */
	GamePanel()
	{
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));	
		this.setFocusable(true);
		this.requestFocus();
		gp = this;
		gsm = new GameStateManager();
	}
	
	/**
     * add listeners of keyboard and mouse to out game panel
     */
	public void addNotify()
	{
		super.addNotify();
		if( thread == null )
		{
			thread = new Thread(this);
			addKeyListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
			thread.start();
		}
	}
	
	/**
     * set new image for default cursor
     * @param aimCursor custom cursor
     */
	public static void setDefaultCursor(AimCursor aimCursor)
	{
		gp.setCursor(aimCursor.getCursor());
	}
	
	/**
     * Add generic component to our window of game panel
     * @param component generic component 
     */
	public static <Thing> void addStuff(Thing component)
	{
		gp.add((Component) component);
	}


	/**
     * function used by thread to initializa and set frame rate of window
     * create game loop
     */
	@Override
	public void run() 
	{
		init();
		
		long start;
		long elapsed;
		long wait;
		
		//game loop
		while(running)
		{
			start = System.nanoTime();
			
			
			update();
			draw();
			drawToScreen();
			
			elapsed = (System.nanoTime() - start);
			wait = targetTime - elapsed / 10000000;
			
			try
			{
				Thread.sleep(wait);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}

	/**
     * Function to initialize graphic on our program and also draw first image
     */
	private void drawToScreen() 
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE,  null);
		g2.dispose();
		
	}

	/**
     * Function to initialize image
     */
	public void init() 
	{
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		g = (Graphics2D) image.getGraphics();
		
		running = true;

	}
	
	/**
     * Function to draw game states
     */
	private void draw(){	gsm.draw(g);	}

	/**
     * Function to update game states
     */
	private void update(){	gsm.update(); 	}
	/**
     * Function to send KeyEvent to game states
     */
	@Override
	public void keyPressed(KeyEvent e) {		gsm.keyPressed(e.getKeyCode()); }

	/**
     * Function to send KeyEvent to game states
     */
	@Override
	public void keyReleased(KeyEvent e) {		gsm.keyReleased(e.getKeyCode()); }
	
	/**
     * Function to send MouseEvent to game states
     */
	@Override
	public void mouseMoved(MouseEvent e) {		gsm.mouseMoved(e.getX(), e.getY()); }
	@Override
	/**
     * Function to send MouseEvent to game states
     */
	public void mousePressed(MouseEvent e) {	gsm.mousePressed();}
	
	
	
	/**
     * Function not used in our program
     */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
     * Function not used in our program
     */
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	/**
     * Function not used in our program
     */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	/**
     * Function not used in our program
     */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	/**
     * Function not used in our program
     */
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	/**
     * Function not used in our program
     */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
