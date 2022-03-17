import java.awt.Color;
import javax.swing.*;

public class MainFrame extends JFrame{
    
	// labels buttons
	private JButton playButton;
	private JTextField nickText;
	private JPanel panel;
	private JLabel nickLabel;
	MainFrame()
	{
		this.setTitle("2D BATTLEROYALE");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(420,420);
		
		ImageIcon image = new ImageIcon("pngegg.png");
		this.setIconImage(image.getImage());
		this.getContentPane().setBackground(new Color(0x344a8c));
		nickLabel = new JLabel("Put your nick");
		nickLabel.setVerticalTextPosition(JLabel.TOP);
		nickLabel.setHorizontalTextPosition(JLabel.CENTER);
		playButton = new JButton("Play");
		nickText = new JTextField(16);
		panel = new JPanel();
		panel.setBounds(0,0,420,200);
		panel.add(nickText);
		panel.add(playButton);
		panel.add(nickLabel);
		panel.setVisible(true);
		
		this.add(panel);
		this.setVisible(true);
	}
}
