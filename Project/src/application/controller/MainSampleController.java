package application.controller;

import java.io.IOException;
import java.net.URL;

import application.CommonObjs;
import application.database.CommentDBCom;
import application.database.DatabaseManager;
import application.database.ProjDBCommunicator;
import application.database.TicketDBCom;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class MainSampleController {

	@FXML HBox mainBox;
	@FXML AnchorPane projTicketViewer;
	@FXML AnchorPane rightWindow;
	private CommonObjs common = CommonObjs.getInstance();
	
	
	//loads the project and ticket page on startup and hides them
	@FXML
    public void initialize() {
		// set up databases before loading project and ticket page
		DatabaseManager d = new DatabaseManager();
		common.setDataBase(d);
		
		ProjDBCommunicator projDB = new ProjDBCommunicator();
		common.setProjDataBase(projDB);
		
		TicketDBCom ticketDB = new TicketDBCom();
		common.setTicketDB(ticketDB);
		
		CommentDBCom commentDB = new CommentDBCom();
		common.setCommentDB(commentDB);
		
		/*
		 * Url gets file
		 * Then reads file and converts to anchor pane
		 * sets the converted file as child of projTicketViwer(the left window with proj and ticket buttons)
		 */
		try {
			URL url = getClass().getClassLoader().getResource("view/ProjectsPage.fxml");
			FXMLLoader loader = new FXMLLoader(url);
			AnchorPane projectPane = (AnchorPane) loader.load();
			common.setProjectController(loader.getController());
			
			
			AnchorPane.setBottomAnchor(projectPane, 0.0);
			projTicketViewer.getChildren().add(projectPane);
			
			url = getClass().getClassLoader().getResource("view/TicketPage.fxml");
			loader = new FXMLLoader(url);
			AnchorPane ticketPane = (AnchorPane) loader.load();
			common.setTicketController(loader.getController());
			
			
			AnchorPane.setBottomAnchor(ticketPane, 0.0);
			projTicketViewer.getChildren().add(ticketPane);
			ticketPane.setVisible(false);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	//when we hit the button we unhide either the project or ticket page
	@FXML public void showProjects() {
		common.getTicketController().hide();
		common.getProjectController().show();
	}

	@FXML public void showTickets() {
		common.getProjectController().hide();
		common.getTicketController().show();
	} 
	
}
