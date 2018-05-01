package gui;

import java.util.Iterator;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;

public class ButtonFactory {

	private class ButtonIterator implements Iterator<Node> {

		int index;

		private Node createNode() {
			if (isSeparator(index)) {
				return new Separator();
			}
			return createButton(index);
		}

		public boolean hasNext() {
			if (index < btnName.length) {
				return true;
			}
			return false;
		}

		@Override
		public Node next() {
			if (this.hasNext()) {
				Node curNode = createNode();
				index++;
				return curNode;
			}
			return null;
		}
	}

	private static final String[] btnName = new String[] { "New Graph", null, "Add", "Edit", "Remove", null, "", "", "",
			null, "Paths", "Loops", "Overal Function", null, "exit" };

	public static TextField FROM_AREA, TO_AREA, WEIGHT_AREA;

	public static final int INPUT_WIDTH = 60, INPUT_HEIGHT = 10;

	private static ButtonFactory instance;

	private static final int NEW = 0, ADD_EDGE = 2, EDIT_EDGE = 3, REMOVE_EDGE = 4, FROM_TEXT = 6, TO_TEXT = 7,
			WEIGHT_TEXT = 8, PROCESS_PATHS = 10, PROCESS_LOOPS = 11, PROCESS_FUNCTION = 12, EXIT = 14;

	public static ButtonFactory getInstance() {
		if (instance == null) {
			instance = new ButtonFactory();
		}
		return instance;
	}

	private ButtonFactory() {

	}

	private Node createButton(int index) {
		Button curBtn = new Button(btnName[index]);
		switch (index) {
		case NEW:
			curBtn.setOnAction(e -> SFGDialog.getInstance().newSFG());
			break;
		case ADD_EDGE:
			curBtn.setOnAction(e -> SFGDialog.getInstance().addEdge());
			break;
		case EDIT_EDGE:
			curBtn.setOnAction(e -> SFGDialog.getInstance().editEdge());
			break;
		case REMOVE_EDGE:
			curBtn.setOnAction(e -> SFGDialog.getInstance().removeEdge());
			break;
		case FROM_TEXT:
			FROM_AREA = new TextField("Start");
			FROM_AREA.setMaxHeight(INPUT_HEIGHT);
			FROM_AREA.setMaxWidth(INPUT_WIDTH);
			return FROM_AREA;
		case TO_TEXT:
			TO_AREA = new TextField("End");
			TO_AREA.setMaxHeight(INPUT_HEIGHT);
			TO_AREA.setMaxWidth(INPUT_WIDTH);
			return TO_AREA;
		case WEIGHT_TEXT:
			WEIGHT_AREA = new TextField("Weight");
			WEIGHT_AREA.setMaxHeight(INPUT_HEIGHT);
			WEIGHT_AREA.setMaxWidth(INPUT_WIDTH);
			return WEIGHT_AREA;
		case PROCESS_PATHS:
			curBtn.setOnAction(e -> SFGDialog.getInstance().processPaths());
			break;
		case PROCESS_LOOPS:
			curBtn.setOnAction(e -> SFGDialog.getInstance().processLoops());
			break;
		case PROCESS_FUNCTION:
			curBtn.setOnAction(e -> SFGDialog.getInstance().processFunction());
			break;
		case EXIT:
			curBtn.setOnAction(e -> System.exit(0));
			break;
		default:
			return null;
		}
		return curBtn;
	}

	public Iterator<Node> getIterator() {
		return new ButtonIterator();
	}

	private boolean isSeparator(int index) {
		return btnName[index] == null;
	}
}
