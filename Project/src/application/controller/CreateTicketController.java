package application.controller;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import application.Comment;
import application.CommonObjs;
import application.Project;
import application.Ticket;
import application.database.CommentDBCom;
import application.database.ProjDBCommunicator;
import application.database.TicketDBCom;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class CreateTicketController {
	
	@FXML VBox ticketCreationBox;
	@FXML TextField ticketNameText;
	@FXML ComboBox<String> projSelect;
	@FXML TextArea ticketDescrip;
	@FXML VBox commentBox;
	
	CommonObjs common = CommonObjs.getInstance();
	private HBox parent = common.getMainBox();
	private TicketDBCom ticketDB = common.getTicketDB();
	private ProjDBCommunicator projDB = common.getProjDB();
	private CommentDBCom commentDB = common.getCommentDB();
	private ArrayList<TextArea> commentDescriptions;
	private ArrayList<String> commentTimestamps;
	
	
	@FXML public void initialize() {
		fetchProj();
		commentDescriptions = new ArrayList<TextArea>();
		commentTimestamps = new ArrayList<String>();
	}
	
	//creates a ticket object and comments based on user input
	@FXML public void createTicket() {
		//make sure a project is selected
		if(projSelect.getValue() != null) {
			String name = ticketNameText.getText();
			String projName = projSelect.getValue().toString();
			String des = ticketDescrip.getText();
			
			//make sure name is filled in
			if(!name.isEmpty()) {
				//check to see if the ticket name already exists
				if(ticketDB.getSingleTicket(name)==null) {
					Ticket t = new Ticket(name, projName, des);
					//add ticket to database
					ticketDB.addTicket(t);
					//go through each comment from user input and store the ones that have descriptions
					for(int i = 0; i<commentDescriptions.size(); i++) {
						if(!commentDescriptions.get(i).getText().equals("")) {
							String ticketName = name;
							String description = commentDescriptions.get(i).getText();
							String timestamp = commentTimestamps.get(i);
							Comment c = new Comment(ticketName,description,timestamp);
							commentDB.addComment(c);
						}
					}
					
					//update the list of tickets
					parent.getChildren().remove(ticketCreationBox);
					common.getTicketController().updateTickets();
				}
			}
		}
	}
	
	@FXML public void cancelTicket() {
		parent.getChildren().remove(ticketCreationBox);
	}
	
	//adds an entry each time new comment button is pressed
	@FXML public void createNewComment() {
		//box that holds the fields and labels
		HBox commentEntry = new HBox();
		commentEntry.setAlignment(Pos.CENTER_LEFT);
		commentEntry.setSpacing(10.0);
		//labels and fields also setting their sizes
		Label descLabel = new Label("Comment Description");
		TextArea desc = new TextArea();
		desc.setPrefWidth(400);
		desc.setPrefHeight(50);
		//we set timestamp and format it automatically to current time and date
		Label timeLabel = new Label("Timestamp");
		ZonedDateTime currDate = ZonedDateTime.now(ZoneId.systemDefault());
		String stamp = currDate.format(DateTimeFormatter.ofPattern("MM/dd/yyy hh:mm a"));
		Label time = new Label(stamp);
		commentDescriptions.add(desc);
		commentTimestamps.add(stamp);
		//add everything to main box
		commentEntry.getChildren().addAll(descLabel, desc, timeLabel, time);
		//add that box into the scroll pane
		commentBox.getChildren().add(commentEntry);
	}
	
	
	/*
	 * fetches projects from database to put in the drop down list
	 * for the ticket creation page. project names are sorted alphabetically ascending
	 */
	public void fetchProj() {
		ArrayList<Project> fetchedList = projDB.getProjects();
		ArrayList<String> projNames = new ArrayList<>();
		for (Project p: fetchedList) {
			projNames.add(p.getName());
		}
		Collections.sort(projNames);
		for (String s: projNames) {
			projSelect.getItems().add(s);
		}
		
	}
	
	
	
}
