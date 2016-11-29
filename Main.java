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
	private JLabel timerLabel;

	public Main(){
		setSize(1150,750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Game of Graphs by Josh Winter");

		this.timerLabel = new JLabel("00:00");
		this.gamePanel = new GamePanel(this, this.timerLabel);
		this.topbar = new JPanel(new BorderLayout());

		this.welcomeLabel = new JLabel("Welcome to Game of Graphs by Josh Winter");
		this.resetButton = new JButton("Reset");
		this.resetButton.addActionListener(this);

		this.topbar.add(this.welcomeLabel, BorderLayout.WEST);
		this.topbar.add(this.resetButton, BorderLayout.EAST);
		this.add(this.topbar,BorderLayout.NORTH);
		this.add(this.gamePanel, BorderLayout.CENTER);
		this.add(this.timerLabel, BorderLayout.SOUTH);

		setVisible(true);

		this.gamePanel.reset();
	}

	public static void main(String[] args){
		JFrame jf = new Main();
		JOptionPane.showMessageDialog(jf,
			"Welcome to Game of Graphs by Josh Winter!\n\n" +
			"- You are shown here a connected, planar graph of " + GamePanel.NODE_COUNT + " vertices.\n\n" +
			"- The graph is not represented in a true planar fashion, with line\n" +
			"segments intersecting everywhere.\n\n" +
			"- Your job is to drag the nodes around using your mouse until the graph\n" +
			"is represented in a true planar fashion (no line segment intersections)."
			);
	}

	public void actionPerformed(ActionEvent e){
		this.gamePanel.reset();
	}
}
