import java.awt.*;
import java.awt.event.*;

public class Hero extends Rectangle 
{
	private int SizeX=40;
	private int SizeY=80;
	private int speed=10;
	private long jumpingTime=1000;
	private Boolean crouch=false;
	private Boolean jump=false;
	MyThread thread = new MyThread();
	Hero(int x, int y){
		this.x=x;
		this.y=y;
		this.width=SizeX;
		this.height=SizeY;
	}
	public void keyPressed(KeyEvent e) throws InterruptedException {
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(!jump){
				jump=true;
				jump=thread.jumping(jumpingTime);//czeka 1s
			}	
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(!crouch){
				this.height=SizeY/2;
				this.y=this.y+SizeY/2;
				crouch=true;
				speed=5;
			}
			else{
				this.height=SizeY;
				this.y=this.y-SizeY/2;
				crouch=false;
				speed=10;
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
		g.setColor(Color.blue);
		g.fillRect(this.x, this.y, this.width, this.height);
	}
	public void jumping() {
		if(jump)
			this.y=this.y-5;
	}
}