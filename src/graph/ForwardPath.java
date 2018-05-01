package graph;

import java.util.ArrayList;

public class ForwardPath {
	private ArrayList<Node> currentPath;
	private ArrayList<Path> paths;
	private Node targetNode;
	private boolean[] visited;

	public ForwardPath(Graph graph) {
		visited = new boolean[graph.getGraphNodes().size()];
		currentPath = new ArrayList<Node>();
		paths = new ArrayList<Path>();
	}

	public void DFS(Node node, double gain) {
		if (node.equals(targetNode)) {
			currentPath.add(node);
			Path path = new Path(new ArrayList<Node>(currentPath), gain);
			paths.add(path);
			currentPath.remove(node);
			return;
		}
		visited[toInt(node.getName())] = true;
		currentPath.add(node);
		for (Edge edge : node.getEdges()) {
			Node neighbour = edge.getEndNode();
			if (!visited[toInt(neighbour.getName())]) {
				gain *= edge.getWeight();
				DFS(neighbour, gain);
				gain /= edge.getWeight();
			}
		}
		visited[toInt(node.getName())] = false;
		currentPath.remove(node);
	}

	public ArrayList<Path> getPaths(Node startNode, Node endNode) {
		currentPath.clear();
		paths.clear();
		this.targetNode = endNode;
		DFS(startNode, 1);
		return paths;
	}

	private int toInt(String number) {
		return Integer.valueOf(number);
	}

}
