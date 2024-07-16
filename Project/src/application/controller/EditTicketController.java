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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EditTicketController {
	private CommonObjs common = CommonObjs.getInstance();
	private HBox parentBox = common.getMainBox();
	private TicketDBCom ticketDB = common.getTicketDB();
	private ProjDBCommunicator projDB = common.getProjDB();
	private CommentDBCom comDB = common.getCommentDB();
    @FXML private VBox ticketCreationBox;
    @FXML private Label ticketName;
    @FXML private TextField ticketNameText;
    @FXML private ComboBox<String> projSelect;
    @FXML private TextArea ticketDescrip;
    @FXML private VBox commentBox;
    @FXML private VBox newCommentBox;
    //stores the preexisting comments' textareas and timestamps so we can reference
    private ArrayList<TextArea> commentDescriptions;
	private ArrayList<String> commentTimestamps;
	//stores the new comments' textareas and timestamps so we can reference
	private ArrayList<TextArea> newCommentDescriptions;
	private ArrayList<String> newCommentTimestamps;
	//stores deleted comments to be deleted once save is clicked
	private ArrayList<Integer> toBeDeletedComments; 
    
    @FXML public void initialize() {
    	//load projects names for dropdown and initializes the arraylists
    	fetchProj();
        commentDescriptions = new ArrayList<TextArea>();
		commentTimestamps = new ArrayList<String>();
		newCommentDescriptions = new ArrayList<TextArea>();
		newCommentTimestamps = new ArrayList<String>();
		toBeDeletedComments = new ArrayList<Integer>(); 
    }
    
    public void loadData() {
		//enters preexisting ticket data into their respective fields
        Ticket t = ticketDB.getSingleTicket(ticketName.getText());
        ticketNameText.setText(t.getName());
        projSelect.getSelectionModel().select(t.getProjName());
        ticketDescrip.setText(t.getDescription());
        
        //load every comment we have for the ticket
        for(Comment c : comDB.getComments(t.getName())) {
        	//box that holds the fields and labels
    		HBox commentEntry = new HBox();
    		commentEntry.setAlignment(Pos.CENTER_LEFT);
    		commentEntry.setSpacing(10.0);
    		//labels and fields also setting their sizes
    		Label descLabel = new Label("Comment Description");
    		TextArea desc = new TextArea();
    		desc.setPrefWidth(400);
    		desc.setPrefHeight(50);
    		desc.setUserData(c.getID());
    		desc.setText(c.getDescription());
    		Label timeLabel = new Label("Timestamp");
    		Label time = new Label(c.getTimestamp());
    		commentDescriptions.add(desc);
    		commentTimestamps.add(c.getTimestamp());
    		//create delete button
    		Button b = new Button();
	    		b.setText("Delete");
	    		b.setUserData(c.getID());
	    		b.setOnAction(e -> {
	    			//store id in arraylist to delete comment from db once save is clicked
	    			toBeDeletedComments.add((Integer)b.getUserData());
	    			//delete from reference arraylists
	    			commentDescriptions.remove(desc);
	    			commentTimestamps.remove(c.getTimestamp());
	    			//delete from box showing comments
	    			commentBox.getChildren().remove(b.getParent());
	    		});
    		//add everything to main box
    		commentEntry.getChildren().addAll(descLabel, desc, timeLabel, time, b);
    		//add that box into the old comment box inside the scrollpane
    		commentBox.getChildren().add(commentEntry);
        }
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
  		newCommentDescriptions.add(desc);
  		newCommentTimestamps.add(stamp);
  		//add everything to main box
  		commentEntry.getChildren().addAll(descLabel, desc, timeLabel, time);
  		//add that box into the new comment box in the scrollpane
  		newCommentBox.getChildren().add(commentEntry);
  	}

    @FXML
    void deleteTicket() {
    	ticketDB.deleteTicket(ticketName.getText());
    	parentBox.getChildren().remove(ticketCreationBox);
    	common.getProjectController().updateProjects();
    	common.getTicketController().updateTickets();
    }

    //when save button is clicked
    @FXML
    void editTicket() {
    	//delete to be deleted comments from db
    	for(Integer i : toBeDeletedComments) {
    		common.getCommentDB().deleteComment(i);
    	}
    	//make sure a project is selected
    			if(projSelect.getValue() != null) {
    				String name = ticketNameText.getText();
    				String projName = projSelect.getValue().toString();
    				String des = ticketDescrip.getText();
    				
    				//make sure name is filled in
    				if(!name.isEmpty()) {
    					//make sure ticket name is either the same or is something new
    					if(ticketDB.getSingleTicket(name)==null || name.equals(ticketName.getText())) {
    						Ticket t = new Ticket(name, projName, des);
        					ticketDB.updateTicket(ticketName.getText(),t);
        					
        					//edit any changed descriptions comments
        					for(int i = 0; i<commentDescriptions.size(); i++) {
        						int id = (int) commentDescriptions.get(i).getUserData();
        						String ticketName = name;
        						String description = commentDescriptions.get(i).getText();
        						String timestamp = commentTimestamps.get(i);
        						//if description has been changed then we must add a new time stamp and call to edit the db entry
        						if(!description.equals(comDB.getComment(id).getDescription())) {
        							ZonedDateTime currDate = ZonedDateTime.now(ZoneId.systemDefault());
        							timestamp = currDate.format(DateTimeFormatter.ofPattern("MM/dd/yyy hh:mm a"));
        							Comment c = new Comment(ticketName,description,timestamp);
        							comDB.editComment(id, c);
        						}
        					}
        					
        					//go through each new comment from user input and store the ones that have something in the descriptions
        					for(int i = 0; i<newCommentDescriptions.size(); i++) {
        						if(!newCommentDescriptions.get(i).getText().equals("")) {
        							String ticketName = name;
        							String description = newCommentDescriptions.get(i).getText();
        							String timestamp = newCommentTimestamps.get(i);
        							Comment c = new Comment(ticketName,description,timestamp);
        							comDB.addComment(c);
        						}
        					}
        					
        					//close window
        					parentBox.getChildren().remove(ticketCreationBox);
        					common.getTicketController().updateTickets();
    					}
    				}
    			}
    }
    
    @FXML
    void cancelTicket() {
    	parentBox.getChildren().remove(ticketCreationBox);
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
	
	public void setTicketName(String name) {
		ticketName.setText(name);
	}

}
