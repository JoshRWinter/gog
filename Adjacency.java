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

	public static boolean nodeInCommon(Adjacency a, Adjacency b){
		return a.from.getNode() == b.from.getNode() || a.from.getNode() == b.to.getNode() || a.to.getNode() == b.from.getNode() || a.to.getNode() == b.to.getNode();
	}
}
