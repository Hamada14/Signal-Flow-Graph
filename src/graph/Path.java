package graph;

import java.util.ArrayList;

public class Path {

	private double gain;
	private ArrayList<Node> path;

	public Path(ArrayList<Node> path, double gain) {
		this.path = path;
		this.gain = gain;
	}

	public double getGain() {
		return gain;
	}

	public ArrayList<Node> getPath() {
		return path;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(path.get(0).getName());
		for(int i = 1; i < path.size(); i++){
			sb.append(" -> " + path.get(i).getName());
		}
		return sb.toString();
	}
}
