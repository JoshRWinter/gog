import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main extends JFrame implements ActionListener{
	private GamePanel gamePanel;
	private JPanel topbar;
	private JLabel welcomeLabel;
	private JButton resetButton;

	public Main(){
		setSize(850,600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Game of Graphs by Josh Winter");
		setResizable(false);

		this.gamePanel = new GamePanel(this);
		this.topbar = new JPanel(new BorderLayout());

		this.welcomeLabel = new JLabel("Welcome to Game of Graphs by Josh Winter");
		this.resetButton = new JButton("Reset");
		this.resetButton.addActionListener(this);

		this.topbar.add(this.welcomeLabel, BorderLayout.WEST);
		this.topbar.add(this.resetButton, BorderLayout.EAST);
		this.add(this.topbar,BorderLayout.NORTH);
		this.add(this.gamePanel, BorderLayout.CENTER);

		setVisible(true);
		//JOptionPane.showMessageDialog(null, "width=" + this.gamePanel.getWidth() + ", height=" + this.gamePanel.getHeight());

		this.gamePanel.reset();
	}

	public static void main(String[] args){
		new Main();
	}

	public void actionPerformed(ActionEvent e){
		this.gamePanel.reset();
	}
}
