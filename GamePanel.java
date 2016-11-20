import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.JOptionPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;

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
			this.node[i] = new NodeWrapper(new Node((int)(Math.random() * (this.getWidth() - Node.SIZE)), (int)(Math.random() * (this.getHeight() - Node.SIZE))), null, null);
		this.repaint();
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		// draw the nodes
		for(int i = 0; i < GamePanel.NODE_COUNT; ++i){
			Node n = this.node[i].getNode();
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
		this.mouseFocus = null;
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
