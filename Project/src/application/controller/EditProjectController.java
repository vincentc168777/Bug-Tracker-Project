package application.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import application.CommonObjs;
import application.Project;
import application.database.ProjDBCommunicator;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EditProjectController {
	private CommonObjs common = CommonObjs.getInstance();
	private ProjDBCommunicator projDB = common.getProjDB();
	private HBox parentBox = common.getMainBox();
    @FXML VBox mainBox;
    @FXML Label projName;
    @FXML TextField nameField;
    @FXML DatePicker date;
    @FXML TextArea descriptionField;
    
    //load the original name, date and descriptions
    @FXML public void loadData() {
        Project p = projDB.getSingleProject(projName.getText());
        nameField.setText(p.getName());
        date.setValue(p.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        descriptionField.setText(p.getDescription());
    }

    //removes project and updates the project and ticket panes
    @FXML public void deleteProject() {
    	projDB.deleteProject(projName.getText());
    	parentBox.getChildren().remove(mainBox);
    	common.getProjectController().updateProjects();
    	common.getTicketController().updateTickets();
    }

    //adjusts the project based on anything that the user changes
    @FXML public void editProject() {
    	LocalDate localDate = date.getValue();
	    String name = nameField.getText();
	    String description = descriptionField.getText();

	    //checks to make sure name is not empty
	    if(!name.isEmpty() && localDate!=null) {
	    	//we search to see if the project name already exists
	    	//if we get a project back that has a null name that means the query couldn't find a match and we don't have a duplicate name
	    	//we also check to see if the name was changed at all since that will show up as a duplicate name
	    	//if the name was unchanged then we can continue
	    	if(projDB.getSingleProject(name)==null || name.equals(projName.getText())) {
	    		Date dateObj = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		        Project newProj = new Project(name, dateObj, description);
		        projDB.updateProject(projName.getText(),newProj);
		        parentBox.getChildren().remove(mainBox);
		        common.getProjectController().updateProjects();
		        common.getTicketController().updateTickets();
	    	}
	    }
    }
    
    //closes the window
    @FXML public void cancelProject() {
    	parentBox.getChildren().remove(mainBox);
    }
    
    //sets label containing old name
    public void setProjName(String name) {
    	projName.setText(name);
    }
}
