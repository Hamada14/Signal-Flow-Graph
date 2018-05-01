package drawer;

import java.awt.Point;
import java.util.ArrayList;

import graph.SFG;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class GraphicsDrawer implements Observer {

	private static GraphicsDrawer instance;
	public final static int LINE_WIDTH = 2;
	public final static int PRECISION = 2;
	public final static int SHIFT = 5;
	public final static int WIDTH = 1024, HEIGHT = 650;
	public final static int WIDTH_OFFSET = 30, MAX_RADIUS = 40;

	public static GraphicsDrawer getInstance() {
		if (instance == null) {
			instance = new GraphicsDrawer();
		}
		return instance;
	}

	private Canvas canvas;

	private ArrayList<Weight> weightList;

	private GraphicsDrawer() {
		canvas = new Canvas(WIDTH, HEIGHT);
	}

	private void addLowerArrow(int second, int radius, int xAxis, GraphicsContext g) {
		g.setFill(Color.BLUE);
		double[] xPoints = new double[3];
		double[] yPoints = new double[3];
		Point curPos = getPosition(second, radius, xAxis);
		xPoints[0] = curPos.x + radius - SHIFT;
		xPoints[1] = curPos.x + 0.75 * radius - SHIFT;
		xPoints[2] = curPos.x + 1.25 * radius - SHIFT;
		yPoints[0] = curPos.y + 2 * radius;
		yPoints[1] = curPos.y + 2.25 * radius;
		yPoints[2] = yPoints[1];
		g.fillPolygon(xPoints, yPoints, 3);
	}

	private void addUpperArrow(int first, int radius, int xAxis, int xShift, GraphicsContext g) {
		g.setFill(Color.BLUE);
		double[] xPoints = new double[3];
		double[] yPoints = new double[3];
		Point curPos = getPosition(first, radius, xAxis);
		xPoints[0] = curPos.x + radius + SHIFT + xShift;
		xPoints[1] = curPos.x + 0.75 * radius + SHIFT + xShift;
		xPoints[2] = curPos.x + 1.25 * radius + SHIFT + xShift;
		yPoints[0] = curPos.y;
		yPoints[1] = curPos.y - 0.25 * radius;
		yPoints[2] = yPoints[1];
		g.fillPolygon(xPoints, yPoints, 3);
	}

	private void drawBackwardEdge(int second, int first, int radius, int xAxis, GraphicsContext g) {
		Point firstPos = getPosition(first, radius, xAxis);
		Point secondPos = getPosition(second, radius, xAxis);
		double startX = firstPos.x + radius + SHIFT;
		double endX = secondPos.x + radius - SHIFT;
		double width = endX - startX;
		double height = width / 4;
		double startY = firstPos.y - height / 2 - 0.25 * radius;
		g.setLineWidth(2);
		g.setStroke(Color.RED);
		g.strokeArc(startX, startY, width, height, 0, 180, ArcType.OPEN);
		addUpperArrow(first, radius, xAxis, 0, g);
		weightList.add(new Weight((SFG.getInstance().getWeight(second, first)), (startX + endX) / 2,
				firstPos.y - height / 2 - 2.5 * SHIFT));
	}

	private void drawDirectEdge(int first, int second, int radius, int xAxis, GraphicsContext g) {
		Point firstPos = getPosition(first, radius, xAxis);
		Point secondPos = getPosition(second, radius, xAxis);
		double startX = firstPos.x + 2.5 * radius;
		double endX = secondPos.x - 0.5 * radius;
		g.setStroke(Color.RED);
		g.setFill(Color.RED);
		g.fillRect(startX, firstPos.y + radius - LINE_WIDTH / 2, endX - startX, LINE_WIDTH);
		g.setFill(Color.BLUE);
		double[] xPoints = new double[3];
		double[] yPoints = new double[3];
		xPoints[0] = endX;
		xPoints[1] = endX;
		xPoints[2] = secondPos.x;
		yPoints[0] = firstPos.y + 0.75 * radius;
		yPoints[1] = yPoints[0] + 0.5 * radius;
		yPoints[2] = yPoints[0] + 0.25 * radius;
		g.fillPolygon(xPoints, yPoints, 3);
		weightList.add(new Weight(SFG.getInstance().getWeight(first, second), (startX + endX) / 2 - 0.5 * radius,
				xAxis + radius - LINE_WIDTH - 0.2 * radius));
	}

	private void drawEdges(int index, int nodeCount, int radius, int xAxis, GraphicsContext g) {
		if (index + 1 < nodeCount && SFG.getInstance().hasEdge(index, index + 1)) {
			drawDirectEdge(index, index + 1, radius, xAxis, g);
		}
		if (SFG.getInstance().hasEdge(index, index)) {
			drawSelfLoop(index, radius, xAxis, g);
		}
		for (int i = index + 2; i < nodeCount; i++) {
			if (SFG.getInstance().hasEdge(index, i))
				drawForwardEdge(index, i, radius, xAxis, g);
		}
		for (int i = index - 1; i >= 0; i--) {
			if (SFG.getInstance().hasEdge(index, i))
				drawBackwardEdge(index, i, radius, xAxis, g);
		}
	}
	
	private void drawForwardEdge(int first, int second, int radius, int xAxis, GraphicsContext g) {
		Point firstPos = getPosition(first, radius, xAxis);
		Point secondPos = getPosition(second, radius, xAxis);
		double startX = firstPos.x + radius + SHIFT;
		double endX = secondPos.x + radius - SHIFT;
		double width = endX - startX;
		double height = width / 4;
		double startY = firstPos.y + 2.25 * radius - height / 2;
		g.setLineWidth(2);
		g.setStroke(Color.RED);
		g.strokeArc(startX, startY, width, height, 180, 180, ArcType.OPEN);
		addLowerArrow(second, radius, xAxis, g);
		weightList.add(
				new Weight((SFG.getInstance().getWeight(first, second)), (startX + endX) / 2, startY + height - SHIFT));
	}

	private void drawNodes(int nodeCount, int radius, int xAxis, GraphicsContext g) {
		for (int i = 0; i < nodeCount; i++) {
			Point curCenter = getPosition(i, radius, xAxis);
			g.setFill(Color.BLACK);
			g.fillOval(curCenter.x, curCenter.y, radius * 2, radius * 2);
			g.setFill(Color.RED);
			g.fillText(Integer.toString(i), curCenter.x + radius - SHIFT, curCenter.y + radius + SHIFT / 2);
			drawEdges(i, nodeCount, radius, xAxis, g);
		}
		g.setFill(Color.GREEN);
		for (Weight wgh : weightList) {
			String val = wgh.getValue(PRECISION);
			g.fillText(val, wgh.getX() - 2 * val.length(), wgh.getY());
		}
	}

	private void drawSelfLoop(int index, int radius, int xAxis, GraphicsContext g){
		Point firstPos = getPosition(index, radius, xAxis);
		double startX = firstPos.x + radius - 4 * SHIFT;
		double endX = firstPos.x + radius + 4 * SHIFT;
		double width = endX - startX;
		double height = width;
		double startY = firstPos.y - height / 2 - 0.25 * radius;
		g.setLineWidth(2);
		g.setStroke(Color.RED);
		g.strokeArc(startX, startY, width, height, 0, 180, ArcType.OPEN);
		addUpperArrow(index, radius, xAxis, - 5 * SHIFT, g);
		weightList.add(new Weight((SFG.getInstance().getWeight(index, index)), (startX + endX) / 2,
				firstPos.y - height / 2 - 2.5 * SHIFT));
	}

	public Canvas getCanvas() {
		return canvas;
	}

	private Point getPosition(int index, int radius, int xAxis) {
		return new Point(WIDTH_OFFSET + radius * 6 * index, xAxis);
	}

	@Override
	public void update() {
		int nodeCount = SFG.getInstance().getNumberOfNodes();
		weightList = new ArrayList<Weight>();
		int radius = (WIDTH - 2 * WIDTH_OFFSET) / (6 * nodeCount - 4);
		radius = Math.min(radius, MAX_RADIUS);
		int xAxis = HEIGHT / 2;
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		drawNodes(nodeCount, radius, xAxis, g);
	}
}
