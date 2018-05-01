package graph;

import java.util.ArrayList;
import java.util.TreeSet;

public class GraphParameters implements SignalFlowGraphInterface {

	private ForwardPath forwardPath;

	private Graph graph;
	private Loop loop;

	public GraphParameters(Graph graph) {
		this.graph = graph;
	}

	private boolean checkPairs(ArrayList<Path> pairs) {
		int n = pairs.size();
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				if (isTouching(pairs.get(i), pairs.get(j)))
					return false;
			}
		}
		return true;
	}

	private ArrayList<ArrayList<ArrayList<Path>>> combinations(ArrayList<Path> paths) {
		ArrayList<ArrayList<ArrayList<Path>>> noneTouching = new ArrayList<ArrayList<ArrayList<Path>>>();
		ArrayList<Path> pairs = new ArrayList<Path>();
		int n = paths.size(), c = 0;
		for (int i = 0; i < n - 1; i++) {
			noneTouching.add(new ArrayList<ArrayList<Path>>());
		}
		for (int i = 0; i < (1 << n); i++) {
			c = 0;
			for (int j = 0; j < 32; ++j) {
				if ((i & (1 << j)) != 0)
					c++;
			}
			if (c > 1 && c <= n) {
				pairs.clear();
				for (int j = 0; j < 32; ++j) {
					if ((i & (1 << j)) != 0) {
						pairs.add(paths.get(j));
					}
				}
				if (checkPairs(pairs)) {
					noneTouching.get(c - 2).add(new ArrayList<Path>(pairs));
				}
			}
		}
		while (noneTouching.remove(new ArrayList<ArrayList<Path>>()))
			;
		return noneTouching;
	}

	private double deleteNoneTouching(double delta, ArrayList<ArrayList<ArrayList<Path>>> noneTouching) {
		int sign = 1;
		for (ArrayList<ArrayList<Path>> noneTouchingDegree : noneTouching) {
			for (ArrayList<Path> pairs : noneTouchingDegree) {
				double value = 1;
				for (Path loop : pairs) {
					value *= loop.getGain();
				}
				delta += (sign * value);
			}
			sign *= -1;
		}
		return delta;
	}

	public ArrayList<Path> getForwardPaths(int from, int to) {
		if (!graph.isValidNode(from) || !graph.isValidNode(to)) {
			throw new RuntimeException("Invalid node.");
		}
		forwardPath = new ForwardPath(graph);
		return forwardPath.getPaths(graph.getGraphNodes().get(from), graph.getGraphNodes().get(to));
	}

	public double getLoopDelta() {
		ArrayList<Path> loops = getLoops();
		ArrayList<ArrayList<ArrayList<Path>>> noneTouching = getNoneTouching(loops);
		double delta = 1;
		for (Path loop : loops) {
			delta -= loop.getGain();
		}
		return deleteNoneTouching(delta, noneTouching);
	}

	public ArrayList<Path> getLoops() {
		loop = new Loop(graph);
		return loop.getLoops();
	}

	public ArrayList<Double> getLoopsGain() {
		ArrayList<Double> gain = new ArrayList<Double>();
		ArrayList<Path> loops = getLoops();
		for (Path path : loops) {
			gain.add(path.getGain());
		}
		return gain;
	}

	public ArrayList<ArrayList<ArrayList<Path>>> getNoneTouching(ArrayList<Path> loops) {
		return combinations(loops);
	}

	public ArrayList<Double> getPathsDelta(int from, int to) {
		ArrayList<Double> delta = new ArrayList<Double>();
		ArrayList<Path> paths = getForwardPaths(from, to);
		ArrayList<Path> loops = getLoops();
		for (Path path : paths) {
			delta.add(pathDelta(path, loops));
		}
		return delta;
	}

	public ArrayList<Double> getPathsGain(int from, int to) {
		ArrayList<Double> gain = new ArrayList<Double>();
		ArrayList<Path> paths = getForwardPaths(from, to);
		for (Path path : paths) {
			gain.add(path.getGain());
		}
		return gain;
	}

	public double GetTransferFunction(int to) {
		double TF = 0;
		ArrayList<Double> pathGain = getPathsGain(0, to);
		ArrayList<Double> pathDelta = getPathsDelta(0, to);
		for (int i = 0; i < pathGain.size(); i++) {
			TF += (pathGain.get(i) * pathDelta.get(i));
		}
		TF /= getLoopDelta();
		return TF;
	}

	private boolean isTouching(Path first, Path second) {
		TreeSet<Node> set = new TreeSet<Node>();
		for (Node node : first.getPath()) {
			set.add(node);
		}
		for (Node node : second.getPath()) {
			if (set.contains(node))
				return true;
		}
		return false;
	}

	private double pathDelta(Path path, ArrayList<Path> loops) {
		double delta = 1;
		ArrayList<Path> pathLoops = new ArrayList<Path>();
		for (Path loop : loops) {
			if (!isTouching(path, loop)) {
				pathLoops.add(loop);
				delta -= loop.getGain();
			}
		}
		ArrayList<ArrayList<ArrayList<Path>>> noneTouching = getNoneTouching(pathLoops);
		return deleteNoneTouching(delta, noneTouching);
	}
}
