package Main;
import javax.swing.*;


public class Game extends JFrame
{
	Game()
	{
		this.setTitle("Multi-Soldat");
		this.setContentPane(new GamePanel());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE);
		this.setResizable(false);
		
		this.setVisible(true);
	}
}
