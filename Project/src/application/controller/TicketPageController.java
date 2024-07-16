package application.controller;



import application.CommonObjs;

import application.Ticket;
import application.database.TicketDBCom;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;


import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class TicketPageController {
	@FXML AnchorPane ticketsAnchor;
	
	CommonObjs common = CommonObjs.getInstance();
	private HBox mainBox;
	private TicketDBCom ticketDB = common.getTicketDB();

	@FXML VBox ticketPane;
	@FXML TextField searchField;
	
	@FXML public void initialize() {
		updateTickets();
	}
	//load fxml file for ticket creation page
	@FXML public void createNewTicket() {
		URL url = getClass().getClassLoader().getResource("view/CreateTicketPage.fxml");
		mainBox = common.getMainBox();
		try {
			VBox TicketCreationPane = (VBox) FXMLLoader.load(url);
			mainBox.getChildren().add(1,TicketCreationPane);
			if(mainBox.getChildren().size()>2) {
				mainBox.getChildren().remove(mainBox.getChildren().size()-1, mainBox.getChildren().size());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//create buttons for tickets
	public void updateTickets() {
		    ticketPane.getChildren().clear();
		    for(Ticket curr : ticketDB.getTickets()) {
		        Button b = makeButton(curr.getName(),curr.getProjName());
		        ticketPane.getChildren().add(b);
		    }
		}
	
	//takes in user input in textfield and searches for ticket with the substring in ticket name or project name
	//adds buttons for those tickets
	@FXML public void searchTicket() {
		ticketPane.getChildren().clear();
	    for(Ticket curr : ticketDB.getTickets(searchField.getText())) {
	        Button b = makeButton(curr.getName(),curr.getProjName());
	        ticketPane.getChildren().add(b);
	    }
	}
	
	//creates button based on inputted ticket name and project name
	//adds ticket name above and project name below
	public Button makeButton(String ticketName, String projName) {
		Button b = new Button();
        b.setMinSize(200, 80);
        b.setPrefSize(200, 80);
        b.setMaxSize(200, 80);
        VBox labels = new VBox();
        labels.setAlignment(Pos.CENTER);
        Label tName = new Label(ticketName);
        tName.setFont(new Font(20));
        tName.maxWidth(170.0);
        tName.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
        Label pName = new Label(projName);
        labels.getChildren().addAll(tName,pName);
        b.setGraphic(labels);
        b.setOnAction(e -> {
    		URL url = getClass().getClassLoader().getResource("view/EditTicketPage.fxml");
    		mainBox = common.getMainBox();
    		try {
    			FXMLLoader loader = new FXMLLoader(url);
    			VBox ticketEditPane = (VBox) loader.load();
    			EditTicketController controller = loader.getController();
    			controller.setTicketName(ticketName);
    			controller.loadData();
    			mainBox.getChildren().add(1,ticketEditPane);
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
		ticketsAnchor.setVisible(false);
	}
	
	public void show() {
		ticketsAnchor.setVisible(true);
	}
}
