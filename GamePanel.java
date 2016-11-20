import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.JOptionPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;
import java.awt.Color;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener{
	private static final int NODE_COUNT = 20;
	private Main owner;
	private NodeWrapper[] node;
	private Node mouseFocus;
	private int mouseOffsetX;
	private int mouseOffsetY;

	public GamePanel(Main owner){
		super(new BorderLayout());
		this.owner = owner;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public void reset(){
		this.mouseFocus = null;

		// generate the nodes
		this.node = new NodeWrapper[GamePanel.NODE_COUNT];
		for(int i = 0; i < GamePanel.NODE_COUNT; ++i)
			this.node[i] = new NodeWrapper(i, new Node((int)(Math.random() * (this.getWidth() - Node.SIZE)), (int)(Math.random() * (this.getHeight() - Node.SIZE))), null, null);
		this.repaint();

		// some adjacencies
		/*for(int i = 0; i < GamePanel.NODE_COUNT; ++i){
			for(int j = 0; j < GamePanel.NODE_COUNT; ++j){
				this.node[i].addAdjacent(this.node[j]);
				this.node[j].addAdjacent(this.node[i]);
			}
		}*/
		this.node[6].addAdjacent(this.node[14]);
		this.node[14].addAdjacent(this.node[6]);

		this.node[14].addAdjacent(this.node[13]);
		this.node[13].addAdjacent(this.node[14]);

		this.node[3].addAdjacent(this.node[11]);
		this.node[11].addAdjacent(this.node[3]);

		this.node[19].addAdjacent(this.node[0]);
		this.node[0].addAdjacent(this.node[19]);

		// a simple test
		AdjacencyIterator ai = new AdjacencyIterator(this.node);
		Adjacency a = null;
		int count = 0;
		do{
			a = ai.nextAdjacency();
			if(a != null) ++count;
		}while(a != null);
		System.err.println("count=" + count);
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

		g.drawString(this.checkPlanar()?"planar":"not planar",10, 10);

		// set up the "mark" array for traversal
		boolean[] mark = new boolean[GamePanel.NODE_COUNT];
		for(int i = 0; i < GamePanel.NODE_COUNT; ++i)
			mark[i] = false;

		// traverse over each adjacency
		for(int i = 0; i < GamePanel.NODE_COUNT; ++i){
			Node n = this.node[i].getNode();

			// for each node, iterate over its adjacency list
			AdjacencyIterator ai = new AdjacencyIterator(this.node);
			Adjacency a = ai.nextAdjacency();
			while(a != null){
				Node to = a.to.getNode();
				Node from = a.from.getNode();
				g.drawLine(from.x + (Node.SIZE/2), from.y + (Node.SIZE/2), to.x + (Node.SIZE/2), to.y + (Node.SIZE/2));
				a = ai.nextAdjacency();
			}
		}

		// traverse over each node
		for(int i = 0; i < GamePanel.NODE_COUNT; ++i){
			Node n = this.node[i].getNode();

			// draw the node
			g.setColor(new Color(0.6f, 0.0f, 0.6f));
			g.fillOval(n.x, n.y, Node.SIZE, Node.SIZE);
			g.setColor(new Color(0.0f,0.0f,0.0f));
			g.drawString(i + "", n.x, n.y);
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
		this.mouseFocus = null;
		if(this.checkPlanar() && false){ // win condition
			JOptionPane.showMessageDialog(this, "Congratulations! You Win!");
			this.reset();
		}
	}

	public void mousePressed(MouseEvent e){
		Point p = e.getPoint();
		for(int i = 0; i < GamePanel.NODE_COUNT; ++i){
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

}
