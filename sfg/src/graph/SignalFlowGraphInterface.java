package graph;

import java.util.ArrayList;

public interface SignalFlowGraphInterface {

	public ArrayList<Path> getForwardPaths(int from, int to);

	public double getLoopDelta();

	public ArrayList<Path> getLoops();

	public ArrayList<Double> getLoopsGain();

	public ArrayList<ArrayList<ArrayList<Path>>> getNoneTouching(ArrayList<Path> loops);

	public ArrayList<Double> getPathsDelta(int from, int to);

	public ArrayList<Double> getPathsGain(int from, int to);

	public double GetTransferFunction(int to);

}
