import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener{
    
	// labels buttons
	private JButton playButton;
	private JTextField nickText;
	private JTextField serverText;
	private JPanel panel;
	private JLabel nickLabel;
	private JLabel serverLabel;
	private JLabel imageLabel;
	private String playerNick;
	private String server;
	MainFrame()
	{
		this.setTitle("2D BATTLEROYALE");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(420,420);
		
		ImageIcon image = new ImageIcon("pngegg.png");
		ImageIcon rifles = new ImageIcon("rifles.png");
		this.setIconImage(image.getImage());
		this.getContentPane().setBackground(new Color(0x344a8c));
		nickLabel = new JLabel("Put your nick");
		nickLabel.setVerticalTextPosition(JLabel.TOP);
		nickLabel.setHorizontalTextPosition(JLabel.CENTER);
		serverLabel = new JLabel("Put your server");
		serverLabel.setVerticalTextPosition(JLabel.TOP);
		serverLabel.setHorizontalTextPosition(JLabel.CENTER);
		imageLabel = new JLabel();
		imageLabel.setIcon(rifles);
		playButton = new JButton("Play");
		playButton.addActionListener(this);
		
		nickText = new JTextField(16);
		serverText = new JTextField(16);
		panel = new JPanel();
		panel.setBounds(0,0,420,200);
		panel.add(nickText);
		panel.add(nickLabel);
		panel.add(serverText);
		panel.add(serverLabel);
		panel.add(playButton);
		panel.add(imageLabel);
		panel.setVisible(true);
		
		this.add(panel);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==playButton) {
			playerNick=nickText.getText();
			server=serverText.getText();
			System.out.println(playerNick);
			System.out.println(server);
			nickLabel.setVisible(true);
		}	
	}
	public String getNick()
	{
		return playerNick;
	}
	public String getServer()
	{
		return server;
	}
	
}
