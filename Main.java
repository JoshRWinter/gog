import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;

public class Main extends JFrame{
	private JPanel infoPanel;
	private JPanel gamePanel;
	private JLabel welcomeLabel;

	public Main(){
		setSize(850,600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Game of Graphs by Josh Winter");
		setResizable(false);

		this.infoPanel = new JPanel(new BorderLayout());
		this.gamePanel = new JPanel();
		this.welcomeLabel = new JLabel("Welcome to Game of Graphs by Josh Winter");
		this.infoPanel.add(this.welcomeLabel, BorderLayout.CENTER);
		this.add(this.infoPanel,BorderLayout.NORTH);
		this.add(this.gamePanel, BorderLayout.CENTER);

		setVisible(true);
	}

	public static void main(String[] args){
		new Main();
	}
}
