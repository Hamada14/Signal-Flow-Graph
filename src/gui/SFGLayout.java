package gui;

import java.util.ArrayList;
import java.util.Iterator;

import drawer.GraphicsDrawer;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.StackPane;

public class SFGLayout extends SplitPane {

	private StackPane graphicsPane, toolbarPane;
	private ToolBar toolbar;
	private ArrayList<Node> toolbarNodes = new ArrayList<Node>();

	public SFGLayout() {
		initialiseToolbarPane();
		initialiseGraphicsPane();
		getItems().addAll(toolbarPane, graphicsPane);
		setOrientation(Orientation.VERTICAL);
		setDividerPositions(0.1f, 0.6f);
	}

	private void initialiseGraphicsPane() {
		graphicsPane = new StackPane();
		graphicsPane.getChildren().add(GraphicsDrawer.getInstance().getCanvas());
	}

	private void initialiseToolbarPane() {
		toolbarPane = new StackPane();
		toolbar = new ToolBar();
		Iterator<Node> nodeIter = ButtonFactory.getInstance().getIterator();
		while (nodeIter.hasNext()) {
			toolbarNodes.add(nodeIter.next());
		}
		toolbar.getItems().addAll(toolbarNodes);
		toolbarPane.getChildren().add(toolbar);
	}
}
