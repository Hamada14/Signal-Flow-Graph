package graph;

import java.util.ArrayList;

public class Node implements Comparable<Node> {

	private ArrayList<Edge> edges;
	private int inEdges;
	private String name;
	private int outEdges;

	public Node(final String name) {
		edges = new ArrayList<Edge>();
		this.name = name;
		this.inEdges = 0;
		this.outEdges = 0;
	}

	public boolean addEdge(Edge edge) {
		if (checkHasEdge(edge.getEndNode()))
			return false;
		edges.add(edge);
		outEdges++;
		return true;
	}

	public boolean checkHasEdge(Node node) {
		for (Edge edge : edges) {
			Node curNode = edge.getEndNode();
			if (curNode.equals(node))
				return true;
		}
		return false;
	}

	@Override
	public int compareTo(Node n) {
		return this.name.compareTo(n.getName());
	}

	public boolean deleteEdge(Node node) {
		if (!checkHasEdge(node))
			return false;
		for (int i = 0; i < outEdges; i++) {
			Node curNode = edges.get(i).getEndNode();
			if (node.equals(curNode)) {
				edges.remove(i);
				break;
			}
		}
		outEdges--;
		return true;
	}

	public boolean editEdge(Node node, double weight) {
		if (!checkHasEdge(node))
			return false;
		for (int i = 0; i < outEdges; i++) {
			Node curNode = edges.get(i).getEndNode();
			if (node.equals(curNode)) {
				edges.get(i).setWeight(weight);
				break;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object o1) {
		Node n1 = (Node) o1;
		return this.name.equals(n1.name);
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public int getInEdges() {
		return inEdges;
	}

	public String getName() {
		return name;
	}

	public int getOutEdges() {
		return outEdges;
	}

	public double getWeight(Node node) {
		for (Edge edge : edges) {
			Node curNode = edge.getEndNode();
			if (curNode.equals(node))
				return edge.getWeight();
		}
		return -1;
	}

	public void increaseInEdges() {
		++inEdges;
	}

}
