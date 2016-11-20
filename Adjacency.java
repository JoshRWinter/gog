// instances of this class are returned by AdjacencyIterator.nextAdjacency()
public class Adjacency{
	public NodeWrapper to;
	public NodeWrapper from;

	public Adjacency(NodeWrapper to, NodeWrapper from){
		this.to = to;
		this.from = from;
	}

	public boolean equals(Adjacency a){
		return (this.to == a.to && this.from == a.from) || (this.to == a.from && this.from == a.to);
	}
}
