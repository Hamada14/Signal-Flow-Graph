package graph;


public class DataEntry {

	private int count;
	private String data;
	
	public DataEntry(int size, String data) {
		this.count = size;
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public String getData() {
		return data;
	}
}
