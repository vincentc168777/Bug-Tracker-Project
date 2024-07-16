package application.controller;

import java.io.IOException;
import java.net.URL;

import application.CommonObjs;
import application.Project;
import application.database.ProjDBCommunicator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ProjectPageController {
	private CommonObjs common = CommonObjs.getInstance();
	private ProjDBCommunicator projDB = common.getProjDB();
	@FXML HBox mainBox;
	@FXML VBox projectsPane;
	@FXML AnchorPane projectsAnchor;
	@FXML TextField searchField;
	
	//update the projects on startup so that we can show everything right away
	@FXML public void initialize() {
		updateProjects();
		
	}
	
	//when newproj button is pressed we open the create proj page
	@FXML public void createNewProject() {
		URL url = getClass().getClassLoader().getResource("view/CreateProjectPage.fxml");
		mainBox = common.getMainBox();
		try {
			VBox projectCreationPane = (VBox) FXMLLoader.load(url);
			mainBox.getChildren().add(1,projectCreationPane);
			if(mainBox.getChildren().size()>2) {
				mainBox.getChildren().remove(mainBox.getChildren().size()-1, mainBox.getChildren().size());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//deletes buttons in the scroll pane
	//then iterates through the list of projects and adds them as new buttons
	public void updateProjects() {
		
	    projectsPane.getChildren().clear();
	    for(Project curr : projDB.getProjects()) {
	    	Button b = makeButton(curr.getName());
	        projectsPane.getChildren().add(b);
	    }
	}
	
	//takes in user input in the textfield and searches for projects that have that substring in the name
	//adds buttons for those projects
	@FXML public void searchProject() {
		projectsPane.getChildren().clear();
	    for(Project curr : projDB.getProjects(searchField.getText())) {
	        Button b = makeButton(curr.getName());
	        projectsPane.getChildren().add(b);
	    }
	}
	
	public Button makeButton(String name) {
		Button b = new Button();
        b.setMinSize(200, 80);
        b.setPrefSize(200, 80);
        b.setMaxSize(200, 80);
        b.setText(name);
        b.setFont(new Font(20));
        //when the button is pressed we load the edit page and set the label with the name
        b.setOnAction(e -> {
        		URL url = getClass().getClassLoader().getResource("view/EditProjectPage.fxml");
        		mainBox = common.getMainBox();
        		try {
        			FXMLLoader loader = new FXMLLoader(url);
        			VBox projectEditPane = (VBox) loader.load();
        			EditProjectController controller = loader.getController();
        			controller.setProjName(name);
        			controller.loadData();
        			mainBox.getChildren().add(1,projectEditPane);
        			if(mainBox.getChildren().size()>2) {
        				mainBox.getChildren().remove(mainBox.getChildren().size()-1, mainBox.getChildren().size());
        			}
        		} catch (IOException ex) {
        			// TODO Auto-generated catch block
        			ex.printStackTrace();
        		}
        	});
        return b;
	}

	
	public void hide() {
		projectsAnchor.setVisible(false);
	}
	
	public void show() {
		projectsAnchor.setVisible(true);
	}
}
