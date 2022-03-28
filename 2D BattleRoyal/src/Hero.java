import java.awt.*;
import java.awt.event.*;

public class Hero extends Rectangle 
{
	private int SizeX=40;
	private int SizeY=80;
	private int speed=10;
	private long jumpingTime=1000;
	private Boolean crouch=false;
	public Boolean jump=false;
	MyThread thread1 = new MyThread();
	Hero(int x, int y){
		this.x=x;
		this.y=y;
		this.width=SizeX;
		this.height=SizeY;
	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(!jump){
				this.jump=true;
				new Thread(new MyThread()).start();
			}	
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(!crouch){
				this.height=SizeY/2;
				this.y=this.y+SizeY/2;
				this.crouch=true;
				this.speed=5;
			}
			else{
				this.height=SizeY;
				this.y=this.y-SizeY/2;
				this.crouch=false;
				this.speed=10;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.x=x-speed;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.x=x+speed;
		}
	}
	public void draw(Graphics g) {
		if(this.jump)
			this.y-=5;
		if(this.y<300 && !this.jump)
			this.y+=5;
		g.setColor(Color.blue);
		g.fillRect(this.x, this.y, this.width, this.height);
	}
		
	public class MyThread extends Thread{
		@Override
		public void run() {
			try {
				System.out.println("Poczatek skoku");
				Thread.sleep(jumpingTime);
				jump=false;
				System.out.println("Koniec skoku");
			} catch (InterruptedException e) {
				e.printStackTrace();
				new Thread(this).start();
			}
		}
	}
}