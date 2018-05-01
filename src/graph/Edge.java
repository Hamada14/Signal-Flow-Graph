package graph;

public class Edge {

	private Node endNode;
	private double weight;

	public Edge(double weight, Node endNode) {
		this.weight = weight;
		this.endNode = endNode;
		this.endNode.increaseInEdges();
	}

	public Node getEndNode() {
		return endNode;
	}

	public double getWeight() {
		return weight;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

}
