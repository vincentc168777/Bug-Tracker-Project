package application;

import application.controller.ProjectPageController;
import application.controller.TicketPageController;
import application.database.CommentDBCom;
import application.database.DatabaseManager;
import application.database.ProjDBCommunicator;
import application.database.TicketDBCom;
import javafx.scene.layout.HBox;

//creates common references to the home and mainBox objects that the controllers can access
public class CommonObjs {
    private static CommonObjs singletonRef = new CommonObjs();

    //box that sets the whole window
    private HBox mainBox;
    //home class that stores list of projects
    private ProjectPageController projectController;
    private TicketPageController ticketController;
    //database classes 
    private DatabaseManager DB;
    private ProjDBCommunicator projDB;
    private TicketDBCom ticketDB;
    private CommentDBCom commentDB;
    
    private CommonObjs() {
    	//home = new Home();
    }

    public static CommonObjs getInstance() {
        return singletonRef;
    }

    public HBox getMainBox() {
        return mainBox;
    }

    public void setMainBox(HBox mainBox) {
        this.mainBox = mainBox;
    }
    public void setProjectController(ProjectPageController projectController) {
        this.projectController = projectController;
    }
    
    public ProjectPageController getProjectController() {
        return projectController;
    }
    
    public void setTicketController(TicketPageController ticketController) {
        this.ticketController = ticketController;
    }
    
    public TicketPageController getTicketController() {
        return ticketController;
    }
    
    public void setDataBase(DatabaseManager d) {
    	DB = d;
    }
    
    public DatabaseManager getDatabase() {
    	return DB;
    }
    
    public void setProjDataBase(ProjDBCommunicator p) {
    	projDB = p;
    }
    
    public ProjDBCommunicator getProjDB() {
    	return projDB;
    }

	public TicketDBCom getTicketDB() {
		return ticketDB;
	}

	public void setTicketDB(TicketDBCom ticketDB) {
		this.ticketDB = ticketDB;
	}
	
	public CommentDBCom getCommentDB() {
		return commentDB;
	}

	public void setCommentDB(CommentDBCom commentDB) {
		this.commentDB = commentDB;
	}

}