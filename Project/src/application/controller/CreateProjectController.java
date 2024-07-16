package application.controller;



import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import application.CommonObjs;
import application.Project;
import application.database.ProjDBCommunicator;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateProjectController {
	private CommonObjs common = CommonObjs.getInstance();
	private ProjDBCommunicator projDB = common.getProjDB();
	@FXML VBox mainBox;
	@FXML HBox parentBox = common.getMainBox();
	@FXML TextField nameField;
	@FXML DatePicker date;
	@FXML TextArea descriptionField;
	
	
	//fills the datepicker with the current date by default
	@FXML
    public void initialize() {
        date.setValue(LocalDate.now());
    }
	
	//when the create button is pressed we take the entries from the fields
	//check to make sure name is not empty
	//create new project object and add it to the list in projDBCom class
	@FXML public void createProject() {
	    LocalDate localDate = date.getValue();
	    String name = nameField.getText();
	    String description = descriptionField.getText();

	    if(!name.isEmpty() && localDate!=null) {
	    	//we search to see if the project name already exists
	    	//if we get a project back that has a null name that means the query couldn't find a match and we don't have a duplicate name
	    	if(projDB.getSingleProject(name)==null) {
		        Date dateObj = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		        Project newProj = new Project(name, dateObj, description);
		        projDB.addProj(newProj);  // This now adds the project to the database as well.
		        parentBox.getChildren().remove(mainBox);
		        common.getProjectController().updateProjects();
	    	}
	    }
	}


	//if user hits cancel button window closes
	@FXML public void cancelProject() {
		parentBox.getChildren().remove(mainBox);
	}

}
