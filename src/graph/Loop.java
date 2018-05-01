package graph;

import java.util.ArrayList;
import java.util.TreeSet;

public class Loop {

	private ArrayList<Node> currentPath;
	private Graph graph;
	private ArrayList<Path> paths;
	private Node targetNode;
	private boolean[] visited;

	public Loop(Graph graph) {
		this.graph = graph;
		visited = new boolean[graph.getGraphNodes().size()];
		currentPath = new ArrayList<Node>();
		paths = new ArrayList<Path>();
	}

	private boolean checkIfExist(Path path) {
		for (Path loop : paths) {
			if (loop.getPath().size() >= path.getPath().size() && isSame(loop, path))
				return true;
			else if ((loop.getPath().size() < path.getPath().size() && isSame(path, loop)))
				return true;
		}
		return false;
	}

	public void DFS(Node node, double gain) {
		if (node.equals(targetNode) && visited[toInt(node.getName())]) {
			currentPath.add(node);
			Path path = new Path(new ArrayList<Node>(currentPath), gain);
			if (!checkIfExist(path))
				paths.add(path);
			currentPath.remove(currentPath.size() - 1);
			return;
		}
		visited[toInt(node.getName())] = true;
		currentPath.add(node);
		for (Edge edge : node.getEdges()) {
			Node neighbour = edge.getEndNode();
			if (!visited[toInt(neighbour.getName())] || neighbour.equals(targetNode)) {
				gain *= edge.getWeight();
				DFS(neighbour, gain);
				gain /= edge.getWeight();
			}
		}
		visited[toInt(node.getName())] = false;
		currentPath.remove(currentPath.size() - 1);
	}

	public ArrayList<Path> getLoops() {
		paths.clear();
		for (Node node : graph.getGraphNodes()) {
			this.targetNode = node;
			DFS(node, 1);
		}
		return paths;
	}

	private boolean isSame(Path path1, Path path2) {
		TreeSet<Node> set = new TreeSet<Node>();
		for (Node node : path1.getPath()) {
			set.add(node);
		}
		int same = -1;
		for (Node node : path2.getPath()) {
			if (set.contains(node))
				same++;
		}
		return same == set.size();
	}

	private int toInt(String number) {
		return Integer.valueOf(number);
	}
}
