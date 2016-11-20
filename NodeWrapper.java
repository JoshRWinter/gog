// represents one entry in the adjacency list
public class NodeWrapper{
	private int id; // index into leftmost column of the original adjacencylist
	private Node node;
	private NodeWrapper next;
	private NodeWrapper prev;

	public NodeWrapper(int id, Node n, NodeWrapper next, NodeWrapper prev){
		this.id = id;
		this.node = n;
		this.next = next;
		this.prev = prev;
	}

	public void addAdjacent(NodeWrapper nodeWrapper){
		Node n = nodeWrapper.node;
		// if <this> isn't the list head, doesn't make much sense to call this function
		if(this.prev != null){
			System.err.println(this.toString() + " is not the list head");
			return;
		}

		// first add the node to the current list head,
		NodeWrapper nw = new NodeWrapper(nodeWrapper.getID(), n, next, this);
		this.next = nw;
	}

	public boolean isAdjacent(NodeWrapper n){
		// if <this> isn't the list head, it doesn't make much sense to call this function
		if(prev != null){
			System.err.println(this.toString() + " is not the list head");
			return false;
		}

		NodeWrapper current = this.next;
		while(current != null){
			if(current.node == n.node)
				return true;
			current = current.next;
		}

		return false;
	}

	public int getID(){
		return this.id;
	}

	public NodeWrapper getNext(){
		return this.next;
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
