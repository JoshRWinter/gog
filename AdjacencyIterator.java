public class AdjacencyIterator{
	private final NodeWrapper[] node;
	private int nodeIndex;
	private NodeWrapper current;
	private boolean[] mark;

	public AdjacencyIterator(final NodeWrapper[] node){
		this.node = node;
		this.nodeIndex = 0;
		this.current = this.node[0].getNext();
		this.mark = new boolean[this.node.length];
		for(int i = 0; i < this.node.length; ++i)
			this.mark[i] = false;
	}

	// increment the node index and set <this.current>
	// returns true if the whole process is done
	private boolean increment(){
		// mark the node
		this.mark[this.nodeIndex] = true;
		++this.nodeIndex;
		if(this.nodeIndex >= this.node.length)
			return true; // done
		this.current = this.node[this.nodeIndex].getNext();
		return false;
	}


	public Adjacency nextAdjacency(){
		while(this.current == null){
			// done with this node list head
			if(this.increment()){
				return null; // done
			}
		}

		if(this.mark[this.current.getID()]){
			this.current = this.current.getNext();
			return this.nextAdjacency();
		}

		NodeWrapper tmp = this.current;
		this.current = this.current.getNext();
		return new Adjacency(this.node[this.nodeIndex], tmp);
	}
}
