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

		// cant add yourself as an adjacency
		if(this.node == nodeWrapper.node)
			return;

		// cant add duplicate adjacencies
		if(this.isAdjacent(nodeWrapper))
			return;

		// add the node to the current list head,
		NodeWrapper nw = new NodeWrapper(nodeWrapper.getID(), n, next, this);
		if(this.next != null)
			this.next.prev = nw;
		this.next = nw;
	}

	public void removeAdjacent(NodeWrapper nodeWrapper){
		if(this.prev != null){
			System.err.println(this.toString() + " is not the list head");
			return;
		}

		NodeWrapper current = this.next;
		while(current != null){
			if(current.node == nodeWrapper.node){
				current.remove();
				return;
			}
			current = current.getNext();
		}
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

	// retrieve the number of nodes adjacent to this one
	public int adjacencyCount(){
		int count = 0;
		NodeWrapper current = this.getNext();
		while(current != null){
			++count;
			current = current.getNext();
		}
		return count;
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
		if(this.prev == null){
			System.err.println("can't remove list head!");
			return;
		}
		
		this.prev.next = this.next;
		
		if(this.next != null)
			this.next.prev = this.prev;

		this.prev = null;
		this.next = null;
		this.node = null;
	}
}
