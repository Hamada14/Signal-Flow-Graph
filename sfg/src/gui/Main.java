package gui;

import drawer.GraphicsDrawer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class Main extends Application {

	public final static String TITLE = "Signal Flow Graph";

	public static void main(String[] args) {
		launch(args);
	}

	void setStageProperties(Stage primaryStage, Scene sfgScene) {
		primaryStage.setTitle(TITLE);
		primaryStage.setScene(sfgScene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		SplitPane layout = new SFGLayout();
		Scene sfgScene = new Scene(layout, GraphicsDrawer.WIDTH, GraphicsDrawer.HEIGHT);
		setStageProperties(primaryStage, sfgScene);
	}

}
