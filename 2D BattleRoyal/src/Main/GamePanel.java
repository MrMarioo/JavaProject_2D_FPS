package Main;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import GameState.GameStateManager;


public class GamePanel extends JPanel implements Runnable, KeyListener
{
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 3;
	
	//game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	//image
	private BufferedImage image;
	private Graphics2D g;
	
	//game stat manager
	private GameStateManager gsm;
	
	GamePanel()
	{
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));	
		this.setFocusable(true);
		this.requestFocus();
	}
	
	public void addNotify()
	{
		super.addNotify();
		if( thread == null )
		{
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}




	


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

	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT * SCALE,  null);
		g2.dispose();
		
	}

	private void draw(){	gsm.draw(g);	}

	private void update(){	gsm.update(); 	}
	@Override
	public void keyPressed(KeyEvent e) {	gsm.keyPressed(e.getKeyCode()); }

	@Override
	public void keyReleased(KeyEvent e){	gsm.keyReleased(e.getKeyCode()); }

	public void init() 
	{
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		g = (Graphics2D) image.getGraphics();
		
		running = true;
		
		gsm = new GameStateManager();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
