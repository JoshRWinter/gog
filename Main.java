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
	private JButton nodeButton;

	public Main(int seed){
		setSize(1150,750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Game of Graphs by Josh Winter");

		this.timerLabel = new JLabel("00:00");
		this.gamePanel = new GamePanel(this, this.timerLabel, seed);
		this.topbar = new JPanel(new BorderLayout());


		this.welcomeLabel = new JLabel(" Welcome to Game of Graphs by Josh Winter");
		this.resetButton = new JButton("RESET");
		this.resetButton.addActionListener(this);
		this.nodeButton = new JButton("Node Count");
		this.nodeButton.addActionListener(this);

		this.topbar.add(this.welcomeLabel, BorderLayout.CENTER);
		this.topbar.add(this.resetButton, BorderLayout.EAST);
		this.topbar.add(this.nodeButton, BorderLayout.WEST);
		this.add(this.topbar,BorderLayout.NORTH);
		this.add(this.gamePanel, BorderLayout.CENTER);
		this.add(this.timerLabel, BorderLayout.SOUTH);

		setVisible(true);

		this.gamePanel.reset();
	}

	public static void main(String[] args){
		int seed = -1;
		if(args.length > 0 && args[0].contains("seed=")){
			try{
				seed = Integer.parseInt(args[0].substring(5));
			}catch(Exception e){
				System.err.println("Error: could not parse \"" + args[0].substring(5) + "\" as an integer.");
				return;
			}
		}

		JFrame jf = new Main(seed);

		JOptionPane.showMessageDialog(jf,
			"Welcome to Game of Graphs by Josh Winter!\n\n" +
			"- You are shown here a connected, planar graph of " + GamePanel.DEFAULT_NODE_COUNT + " vertices.\n\n" +
			"- The graph is not represented in a true planar fashion, with line\n" +
			"segments intersecting everywhere.\n\n" +
			"- Your job is to drag the nodes around using your mouse until the graph\n" +
			"is represented in a true planar fashion (no line segment intersections).\n\n" +
			"- Press the \"Node Count\" button to change the number of generated\n" +
			"nodes."
			);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == this.resetButton)
			this.gamePanel.reset();
		else{
			String nodeCount = JOptionPane.showInputDialog("How many nodes do you want?");
			int count;
			try{
				count = Integer.parseInt(nodeCount);
			}catch(Exception ex){
				return;
			}
			if(count < 2)
				return;
			this.gamePanel.nodeCount = count;
			this.gamePanel.reset();
		}
	}
}
