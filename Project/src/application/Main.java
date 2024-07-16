package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	/*
	 * create base window for our application. Everything is held inside.
	 */
	public void start(Stage primaryStage) {
		try {
			HBox mainBox = (HBox)FXMLLoader.load(getClass().getClassLoader().getResource("view/Main.fxml"));
			Scene scene = new Scene(mainBox);
			scene.getStylesheets().add(getClass().getClassLoader().getResource("css/application.css").toExternalForm());
			primaryStage.setTitle("Bug Tracker");
			primaryStage.setScene(scene);
			primaryStage.show();
			/*
			 * gets the singleton object and adds the mainBox we just made
			 * to it for ease of access
			 */
			CommonObjs common  = CommonObjs.getInstance();
			common.setMainBox(mainBox);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
