package graph;

import java.util.ArrayList;

public class Graph {

	private ArrayList<Node> graphNodes;
	private int numberOfNodes;

	public Graph(int numberOfNodes) {
		graphNodes = new ArrayList<Node>();
		this.numberOfNodes = numberOfNodes;
		for (int i = 0; i < numberOfNodes; i++) {
			graphNodes.add(new Node(Integer.toString(i)));
		}
		addEdge(0, 1, 1);
		addEdge(numberOfNodes - 2, numberOfNodes - 1, 1);
	}

	public void addEdge(int from, int to, double weight) {
		if (!isValidNode(from) || !isValidNode(to)) {
			throw new RuntimeException("Invalid node.");
		}
		Edge edge = new Edge(weight, graphNodes.get(to));
		if (!graphNodes.get(from).addEdge(edge)) {
			throw new RuntimeException("This edge already exists.");
		}
	}

	public void deleteEdge(int from, int to) {
		if (!isValidNode(from) || !isValidNode(to)) {
			throw new RuntimeException("Invalid node.");
		}
		if (!graphNodes.get(from).deleteEdge(graphNodes.get(to))) {
			throw new RuntimeException("This edge already exists.");
		}
	}

	public void editEdge(int from, int to, double weight) {
		if (!isValidNode(from) || !isValidNode(to)) {
			throw new RuntimeException("Invalid node.");
		}
		if (!graphNodes.get(from).editEdge(graphNodes.get(to), weight)) {
			throw new RuntimeException("This edge already exists.");
		}
	}

	public ArrayList<Node> getGraphNodes() {
		return graphNodes;
	}

	public int getNumberOfNodes() {
		return numberOfNodes;
	}

	public double getWeight(int first, int second) {
		return graphNodes.get(first).getWeight(graphNodes.get(second));
	}

	public boolean hasEdge(int from, int to) {
		if (!isValidNode(from) || !isValidNode(to)) {
			throw new RuntimeException("Invalid node.");
		}
		return graphNodes.get(from).checkHasEdge(graphNodes.get(to));
	}

	public boolean isValidNode(int node) {
		return (node >= 0 && node <= numberOfNodes + 1);
	}

}
