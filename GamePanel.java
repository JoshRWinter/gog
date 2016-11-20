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
		for(int i = 0; i < GamePanel.NODE_COUNT; ++i){
			for(int j = 0; j < GamePanel.NODE_COUNT; ++j){
				this.node[i].addAdjacent(this.node[j]);
				this.node[j].addAdjacent(this.node[i]);
			}
		}
	}

	private boolean checkPlanar(){
		return false;
	}

	// draw the scene
	// NOTE: all adjacencies are drawn first, then all nodes are drawn
	// this is so line segments appear "underneath" the nodes
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		// set up the "mark" array for traversal
		boolean[] mark = new boolean[GamePanel.NODE_COUNT];
		for(int i = 0; i < GamePanel.NODE_COUNT; ++i)
			mark[i] = false;

		// traverse over each adjacency
		for(int i = 0; i < GamePanel.NODE_COUNT; ++i){
			Node n = this.node[i].getNode();

			// for each node, iterate over its adjacency list
			NodeWrapper current = this.node[i].getNext();
			while(current != null){
				// if already processed, skip it
				if(mark[current.getID()] == true){
					current = current.getNext();
					continue;
				}
				Node cn = current.getNode();
				g.drawLine(n.x + (Node.SIZE/2), n.y + (Node.SIZE/2), cn.x + (Node.SIZE/2), cn.y + (Node.SIZE/2));
				current = current.getNext();

				// mark this node
				mark[i] = true;
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
		if(this.checkPlanar()){ // win condition
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
