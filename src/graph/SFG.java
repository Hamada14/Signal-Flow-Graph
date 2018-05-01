package graph;

import java.util.ArrayList;

import drawer.GraphicsDrawer;

public class SFG implements Observable {

	private static SFG instance;
	public static final int MAX_NODES = 16;

	public static SFG getInstance() {
		if (instance == null) {
			instance = new SFG();
		}
		return instance;
	}

	private Graph graph;
	private GraphParameters parameters;

	private SFG() {
	}

	public void addEdge(int start, int end, double wgh) {
		if (!hasGraph())
			throw new IllegalStateException();
		graph.addEdge(start, end, wgh);
		getLoops();
		notifyGraphics();
	}

	public void deleteEdge(int start, int end) {
		if (!hasGraph())
			throw new IllegalStateException();
		graph.deleteEdge(start, end);
		getLoops();
		notifyGraphics();
	}

	public void editEdge(int start, int end, double wgh) {
		if (!hasGraph())
			throw new IllegalStateException();
		graph.editEdge(start, end, wgh);
		getLoops();
		notifyGraphics();
	}

	public DataEntry getLoops() {
		if (!hasGraph())
			throw new IllegalStateException();
		ArrayList<Double> fpg = parameters.getLoopsGain();
		StringBuilder sb = new StringBuilder();
		ArrayList<Path> loops = parameters.getLoops();
		int i = 0;
		for (Path loop : loops) {
			sb.append(Integer.toString(i + 1) + ") " + loop.toString());
			sb.append(".\n");
			String strVal = String.format("%." + 3 + "f", fpg.get(i));
			sb.append("The gain is " + strVal);
			sb.append(".\n");
			i++;
		}
		return new DataEntry(loops.size(), sb.toString());
	}

	public int getNumberOfNodes() {
		if (!hasGraph())
			throw new IllegalStateException();
		return graph.getNumberOfNodes();
	}

	public DataEntry getOverallFunction(int start, int end) {
		double ans = transferFunction(start, end);
		return new DataEntry(0, String.format("%." + 4 + "f", ans));
	}

	public DataEntry getProcessPaths(int start, int end) {
		ArrayList<Path> fp = parameters.getForwardPaths(start, end);
		ArrayList<Double> fpg = parameters.getPathsGain(start, end);
		ArrayList<Double> fpd = parameters.getPathsDelta(start, end);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Path path : fp) {
			sb.append(Integer.toString(i + 1) + ") " + path.toString());
			sb.append(".\n");
			String strVal = String.format("%." + 3 + "f", fpd.get(i));
			sb.append("The Delta for this Path is " + strVal);
			sb.append("\n");
			strVal = String.format("%." + 3 + "f", fpg.get(i));
			sb.append("The gain is " + strVal);
			sb.append(".\n");
			i++;
		}
		return new DataEntry(fp.size(), sb.toString());
	}

	public double getWeight(int first, int second) {
		if (!hasGraph())
			throw new IllegalStateException();
		return graph.getWeight(first, second);
	}

	public boolean hasEdge(int start, int end) {
		if (!hasGraph())
			throw new IllegalStateException();
		return graph.hasEdge(start, end);
	}

	public boolean hasGraph() {
		return graph != null;
	}

	public boolean hasNode(int node) {
		if (!hasGraph())
			throw new IllegalStateException();
		return graph.isValidNode(node);
	}

	public boolean isInput(int end) {
		return end == 0;
	}

	public boolean isOutput(int start) {
		return start == graph.getNumberOfNodes() - 1;
	}

	public void newGraph(int numOfNodes) {
		graph = new Graph(numOfNodes);
		parameters = new GraphParameters(graph);
		notifyGraphics();
	}

	@Override
	public void notifyGraphics() {
		GraphicsDrawer.getInstance().update();
	}

	public double transferFunction(int from, int to) {
		double overall;
		overall = parameters.GetTransferFunction(to);
		if (from != 0)
			overall /= parameters.GetTransferFunction(from);
		return overall;
	}
}
