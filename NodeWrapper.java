// represents one entry in the adjacency list
public class NodeWrapper{
	private Node node;
	private NodeWrapper next;
	private NodeWrapper prev;

	public NodeWrapper(Node n, NodeWrapper next, NodeWrapper prev){
		this.node = n;
		this.next = next;
		this.prev = prev;
	}

	public void setAdjacent(Node n){
		NodeWrapper nw = new NodeWrapper(n, next, this);
		this.next = nw;
	}

	public Node getNode(){
		return this.node;
	}

	public void remove(){
		if(this.prev != null)
			this.prev.next = this.next;
		
		if(this.next != null)
			this.next.prev = this.prev;

		this.prev = null;
		this.next = null;
		this.node = null;
	}
}
