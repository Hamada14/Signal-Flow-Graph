package drawer;

public class Weight {

	private double value, x, y;

	public Weight(double value, double x, double y) {
		this.value = value;
		this.x = x;
		this.y = y;
	}

	public String getValue(int precision) {
		return String.format("%." + precision + "f", value);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
