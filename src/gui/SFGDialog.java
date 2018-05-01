package gui;

import java.util.Optional;

import graph.DataEntry;
import graph.SFG;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class SFGDialog {

	private static SFGDialog instance;

	public static SFGDialog getInstance() {
		if (instance == null) {
			instance = new SFGDialog();
		}
		return instance;
	}

	private SFGDialog() {

	}

	public void addEdge() {
		if (!SFG.getInstance().hasGraph()) {
			popErrorDialog("There's no Graph, Please create one first.");
			return;
		}
		try {
			int start = Integer.parseInt(ButtonFactory.FROM_AREA.getText());
			int end = Integer.parseInt(ButtonFactory.TO_AREA.getText());
			double wgh = Double.parseDouble(ButtonFactory.WEIGHT_AREA.getText());
			if ((!SFG.getInstance().hasNode(start)) || (!SFG.getInstance().hasNode(end)))
				popErrorDialog("Such Nodes don't exist in the graph.");
			else if (SFG.getInstance().hasEdge(start, end))
				popErrorDialog("Such Edge already exists in the graph");
			else if (SFG.getInstance().isInput(end) || SFG.getInstance().isOutput(start))
				popErrorDialog("Invalid Edge");
			else
				SFG.getInstance().addEdge(start, end, wgh);
		} catch (NumberFormatException e) {
			popErrorDialog("Please use only numeric values");
		}
	}

	public void editEdge() {
		if (!SFG.getInstance().hasGraph()) {
			popErrorDialog("There's no Graph, Please create one first.");
			return;
		}
		try {
			int start = Integer.parseInt(ButtonFactory.FROM_AREA.getText());
			int end = Integer.parseInt(ButtonFactory.TO_AREA.getText());
			double wgh = Double.parseDouble(ButtonFactory.WEIGHT_AREA.getText());
			if ((!SFG.getInstance().hasNode(start)) || (!SFG.getInstance().hasNode(end)))
				popErrorDialog("Such Nodes don't exist in the graph.");
			else if (!SFG.getInstance().hasEdge(start, end))
				popErrorDialog("Such Edge doesn't exist in the graph");
			else
				SFG.getInstance().editEdge(start, end, wgh);
		} catch (NumberFormatException e) {
			popErrorDialog("Please use only numeric values");
		}
	}

	public void newSFG() {
		TextInputDialog dialog = new TextInputDialog("Number of Nodes");
		dialog.setTitle("New Signal Flow Graph");
		dialog.setHeaderText("SFG Required Data");
		dialog.setContentText("Enter number of Nodes:");
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> {
			try {
				int numOfNodes = Integer.parseInt(name);
				if (numOfNodes <= 2 || numOfNodes >= SFG.MAX_NODES)
					popErrorDialog("Please make sure to enter a number between 2 and " + SFG.MAX_NODES + "exclusive.");
				else
					SFG.getInstance().newGraph(numOfNodes);
			} catch (NumberFormatException e) {
				popErrorDialog("Please Ensure the value entered was a valid integer");
			}
		});
	}

	public void popData(String title, String header, String data) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		TextArea textArea = new TextArea(data);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(textArea, 0, 1);
		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();
	}

	public void popErrorDialog(String errorMessage) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error has Occured");
		alert.setHeaderText("Error Description:");
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}

	public void processFunction() {
		if (!SFG.getInstance().hasGraph()) {
			popErrorDialog("There's no Graph, Please create one first.");
			return;
		}
		try {
			int start = Integer.parseInt(ButtonFactory.FROM_AREA.getText());
			int end = Integer.parseInt(ButtonFactory.TO_AREA.getText());
			if ((!SFG.getInstance().hasNode(start)) || (!SFG.getInstance().hasNode(end)))
				popErrorDialog("Such Nodes don't exist in the graph.");
			else {
				DataEntry de = SFG.getInstance().getOverallFunction(start, end);
				popData("System Overall Function", "System Overall Function", 
						"The System overall Transfer Function from Node " + start
						+ " to the node " + end + " Is equal to = " + de.getData());
			}
		} catch (NumberFormatException e) {
			popErrorDialog("Please use only numeric values");
		}
	}

	public void processLoops() {
		if (!SFG.getInstance().hasGraph()) {
			popErrorDialog("There's no Graph, Please create one first.");
			return;
		}
		DataEntry de = SFG.getInstance().getLoops();
		popData("System Loops", "There's " + de.getCount() + " Loop" + (de.getCount() != 1 ? "s " : " "), de.getData());
	}

	public void processPaths() {
		if (!SFG.getInstance().hasGraph()) {
			popErrorDialog("There's no Graph, Please create one first.");
			return;
		}
		try {
			int start = Integer.parseInt(ButtonFactory.FROM_AREA.getText());
			int end = Integer.parseInt(ButtonFactory.TO_AREA.getText());
			if ((!SFG.getInstance().hasNode(start)) || (!SFG.getInstance().hasNode(end)))
				popErrorDialog("Such Nodes don't exist in the graph.");
			else {
				DataEntry de = SFG.getInstance().getProcessPaths(start, end);
				popData("System Forward Paths", "There's " + de.getCount() + " path" + (de.getCount() != 1 ? "s " : " ")
						+ "From Node y" + start + " to Node y" + end, de.getData());
			}
		} catch (NumberFormatException e) {
			popErrorDialog("Please use only numeric values");
		}
	}

	public void removeEdge() {
		if (!SFG.getInstance().hasGraph()) {
			popErrorDialog("There's no Graph, Please create one first.");
			return;
		}
		try {
			int start = Integer.parseInt(ButtonFactory.FROM_AREA.getText());
			int end = Integer.parseInt(ButtonFactory.TO_AREA.getText());
			if ((!SFG.getInstance().hasNode(start)) || (!SFG.getInstance().hasNode(end)))
				popErrorDialog("Such Nodes don't exist in the graph.");
			else if (!SFG.getInstance().hasEdge(start, end))
				popErrorDialog("Such Edge doesn't exist in the graph");
			else
				SFG.getInstance().deleteEdge(start, end);
		} catch (NumberFormatException e) {
			popErrorDialog("Please use only numeric values");
		}
	}
}
