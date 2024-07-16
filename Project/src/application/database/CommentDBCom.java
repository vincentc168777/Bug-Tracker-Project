package application.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.Comment;
import application.CommonObjs;

public class CommentDBCom {
	private CommonObjs common = CommonObjs.getInstance();
	private DatabaseManager DB;
	private Connection conn;
	
	public CommentDBCom() {
		DB = common.getDatabase();
		conn = DB.getConnection();
	}
	
	//we take the instance variables of the comment and store it to the database
	public void addComment(Comment c) {
		try (Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO Comments (ticket_name, description, timestamp) VALUES ('"
                    + c.getTicketName() + "', '"
                    + c.getDescription() + "', '"  
                    + c.getTimestamp() + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	//returns all comments with matching ticket_name
	public ArrayList<Comment> getComments(String ticketName){
        ArrayList<Comment> comments = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Comments WHERE ticket_name LIKE '%"+ticketName+"%'");
            while (rs.next()) {
            	int id = rs.getInt("id");
                String ticket_name = rs.getString("ticket_name");
                String description = rs.getString("description");
                String timestamp = rs.getString("timestamp");
                comments.add(new Comment(id, ticket_name, description, timestamp));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
	
	//returns comment with exact matching id
		public Comment getComment(int id){
	        Comment comment;
	        try (Statement stmt = conn.createStatement()) {
	            ResultSet rs = stmt.executeQuery("SELECT * FROM Comments WHERE id = "+id);
	            while (rs.next()) {
	                String ticket_name = rs.getString("ticket_name");
	                String description = rs.getString("description");
	                String timestamp = rs.getString("timestamp");
	                comment = new Comment(id, ticket_name, description, timestamp);
	                return comment;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        //should never hit this but it wont compile unless you return something
	        return new Comment(0,null,null,null);
	    }
	
	//delete comment with exact matching id
		public void deleteComment(int id) {
			try (Statement stmt = conn.createStatement()) {
	            String sql = "DELETE FROM Comments WHERE id = "+id;
	            stmt.executeUpdate(sql);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
		
	//updates the ticket_name, description, and timestamp for comment that exactly matches id
		public void editComment(int id, Comment comment) {
			try (Statement stmt = conn.createStatement()) {
	            String sql = "UPDATE Comments SET ticket_name = '"
	                    + comment.getTicketName() + "', description = '"
	                    + comment.getDescription() + "', timestamp = '"
	                    + comment.getTimestamp() + "' WHERE id = "
	                    + id;
	            stmt.executeUpdate(sql);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
}
