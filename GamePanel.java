import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.JOptionPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.Timer;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener{
	public static final int DEFAULT_NODE_COUNT = 20;
	public int nodeCount;
	private Main owner;
	private JLabel timerLabel;
	private NodeWrapper[] node; // array of list heads
	private Node mouseFocus; // the node the mouse is dragging
	private int mouseOffsetX;
	private int mouseOffsetY;
	private Timer timer;
	private int elapsed; // elapsed seconds
	private boolean allset; // a semaphore of sorts, dont allow painting if false
	private boolean mouseLock; // is the mouse allowed to move the nodes?
	Random rand;

	public GamePanel(Main owner, JLabel timerLabel, int seed){
		super(new BorderLayout());
		this.owner = owner;
		this.timerLabel = timerLabel;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.allset(true, false);
		this.timer = new Timer(1000, this);
		this.nodeCount = GamePanel.DEFAULT_NODE_COUNT;
		if(seed == -1)
			rand = new Random();
		else
			rand = new Random(seed);
	}

	// called by the timer object
	public void actionPerformed(ActionEvent e){
		++elapsed;
		int minutes = 0;
		int seconds = elapsed;
		while(seconds > 59){
			seconds -= 60;
			++minutes;
		}
		this.timerLabel.setText(String.format("%02d:%02d",minutes, seconds));
	}

	public synchronized void reset(){
		this.allset(true, false);
		this.mouseFocus = null;
		this.mouseLock = false;
		this.timer.stop();
		this.elapsed = 0;
		this.timerLabel.setText("00:00");

		// generate the nodes
		this.node = new NodeWrapper[this.nodeCount];
		int xoff = 0;
		int yoff = 30;
		// arrange <GamePanel.NODE_COUNT> nodes into a straight line
		for(int i = 0; i < this.nodeCount; ++i){
			this.node[i] = new NodeWrapper(i, new Node(xoff, yoff), null, null);
			xoff += Node.SIZE;
			if(xoff + Node.SIZE > this.getWidth()){
				yoff += Node.SIZE;
				xoff = 0;
			}
		}
		// add initial adjacencies
		for(int i = 0; i < this.nodeCount - 1; ++i){
			this.node[i].addAdjacent(this.node[i + 1]);
			this.node[i + 1].addAdjacent(this.node[i]);
		}
		this.shuffleY();
		// add some more adjacencies
		for(int i = 0; i < this.nodeCount; ++i){
			for(int j = 0; j < this.nodeCount; ++j){
				if(i == j)
					continue;
				// random chance to skip this <j> node
				if(this.rand.nextInt(3) != 0)
					continue;

				// make an adjacency between <i> and <j>, if the
				// new adjacency compromises the "planarity" of
				// the graph, then remove it and loop again.
				if(this.node[i].isAdjacent(this.node[j]))
					continue;
				this.node[i].addAdjacent(this.node[j]);
				this.node[j].addAdjacent(this.node[i]);
				if(!this.checkPlanar()){
					// remove it
					this.node[i].removeAdjacent(this.node[j]);
					this.node[j].removeAdjacent(this.node[i]);
				}
			}
		}

		// jumble 'em up
		this.shuffle();

		this.allset(true,true);
		// refresh the screen
		this.repaint();
	}

	// full shuffle, jumble up the nodes
	private void shuffle(){
		for(int i = 0; i < this.nodeCount; ++i){
			this.node[i].getNode().x = this.rand.nextInt(this.getWidth() - Node.SIZE);
			this.node[i].getNode().y = this.rand.nextInt(this.getHeight() - Node.SIZE);
		}
	}

	// jumble up only on the y axis
	private void shuffleY(){
		for(int i = 0; i < this.nodeCount; ++i){
			this.node[i].getNode().y = this.rand.nextInt(this.getHeight() - Node.SIZE);
		}
	}

	private boolean checkPlanar(){
		AdjacencyIterator ai = new AdjacencyIterator(this.node);
		Adjacency a = ai.nextAdjacency();
		while(a != null){
			AdjacencyIterator ai2 = new AdjacencyIterator(this.node);
			Adjacency b = ai2.nextAdjacency();
			while(b != null){
				// if same adjacency, skip
				if(a.equals(b)){
					b = ai2.nextAdjacency();
					continue;
				}

				// if 2 adjacencies have a node in common, skip
				if(Adjacency.nodeInCommon(a, b)){
					b = ai2.nextAdjacency();
					continue;
				}

				// check if intersecting
				if(GamePanel.checkIntersecting(a, b)){
					return false;
				}

				b = ai2.nextAdjacency();
			}
			a = ai.nextAdjacency();
		}
		return true;
	}

	private static boolean checkIntersecting(Adjacency a, Adjacency b){
		Node a1 = a.from.getNode();
		Node a2 = a.to.getNode();
		Node b1 = b.from.getNode();
		Node b2 = b.to.getNode();

		// first adjacency
		Point p1 = new Point(a1.x + (Node.SIZE/2), a1.y + (Node.SIZE/2));
		Point p2 = new Point(a2.x + (Node.SIZE/2), a2.y + (Node.SIZE/2));
		// second adjacency
		Point p3 = new Point(b1.x + (Node.SIZE/2), b1.y + (Node.SIZE/2));
		Point p4 = new Point(b2.x + (Node.SIZE/2), b2.y + (Node.SIZE/2));

    	boolean p1side = (p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x) > 0;
    	boolean p2side = (p4.x - p3.x) * (p2.y - p3.y) - (p4.y - p3.y) * (p2.x - p3.x) > 0;
    	boolean p3side = (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x) > 0;
    	boolean p4side = (p2.x - p1.x) * (p4.y - p1.y) - (p2.y - p1.y) * (p4.x - p1.x) > 0;
    	return p1side != p2side && p3side != p4side;
	}

	// draw the scene
	// NOTE: all adjacencies are drawn first, then all nodes are drawn
	// this is so line segments appear "underneath" the nodes
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(!this.allset(false,false))
			return;

		// iterate over all adjacencies
		AdjacencyIterator ai = new AdjacencyIterator(this.node);
		Adjacency a = ai.nextAdjacency();
		while(a != null){
			Node to = a.to.getNode();
			Node from = a.from.getNode();
			g.drawLine(from.x + (Node.SIZE/2), from.y + (Node.SIZE/2), to.x + (Node.SIZE/2), to.y + (Node.SIZE/2));
			a = ai.nextAdjacency();
		}

		g.setColor(new Color(0.6f, 0.0f, 0.6f));
		// traverse over each node
		for(int i = 0; i < this.nodeCount; ++i){
			Node n = this.node[i].getNode();

			// draw the node
			g.fillOval(n.x, n.y, Node.SIZE, Node.SIZE);
		}
	}

	// MouseListener mandatory methods
	public void mouseClicked(MouseEvent e){
	}

	public void mouseExited(MouseEvent e){
	}

	public void mouseEntered(MouseEvent e){
	}

	public void mouseReleased(MouseEvent e){
		if(this.mouseLock)
			return;
		this.mouseFocus = null;
		if(this.checkPlanar()){ // win condition
			this.timer.stop();
			long seconds = elapsed;
			long minutes = 0;
			while(seconds > 59){
				seconds -= 60;
				++minutes;
			}
			this.mouseLock = true;

			JOptionPane.showMessageDialog(this, 
				"Congratulations! You Win!\n\n" +
				"You corrected the graph in " + String.format("%02d:%02d\n", minutes, seconds) +
				"Press the RESET button in the top right to play again."
				);
		}
	}

	public void mousePressed(MouseEvent e){
		if(this.mouseLock)
			return;
		if(!this.timer.isRunning())
			this.timer.start();

		Point p = e.getPoint();
		for(int i = 0; i < this.nodeCount; ++i){
			Node n = this.node[i].getNode();
			if(p.x > n.x && p.x < n.x + Node.SIZE && p.y > n.y && p.y < n.y + Node.SIZE){
				this.mouseFocus = n;
				this.mouseOffsetX = p.x - n.x;
				this.mouseOffsetY = p.y - n.y;
			}
		}
	}

	// MouseMotionListener mandatory methods
	public void mouseDragged(MouseEvent e){
		if(this.mouseFocus == null)
			return;

		Point p = e.getPoint();
		this.mouseFocus.x = p.x - this.mouseOffsetX;
		this.mouseFocus.y = p.y - this.mouseOffsetY;

		// don't let it move offscreen
		if(this.mouseFocus.x + Node.SIZE > this.getWidth())
			this.mouseFocus.x = this.getWidth() - Node.SIZE;
		else if(this.mouseFocus.x < 0)
			this.mouseFocus.x = 0;

		if(this.mouseFocus.y + Node.SIZE > this.getHeight())
			this.mouseFocus.y = this.getHeight() - Node.SIZE;
		else if(this.mouseFocus.y < 0)
			this.mouseFocus.y = 0;

		this.repaint();
	}

	public void mouseMoved(MouseEvent e){
	}

	private synchronized boolean allset(boolean set, boolean v){
		if(set){
			this.allset = v;
			return v;
		}
		else{
			return this.allset;
		}
	}
}
