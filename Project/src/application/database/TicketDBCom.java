package application.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.CommonObjs;
import application.Ticket;

public class TicketDBCom {
	private CommonObjs common = CommonObjs.getInstance();
	private DatabaseManager DB;
	private Connection conn;
	
	public TicketDBCom() {
		DB = common.getDatabase();
		conn = DB.getConnection();
	}
	//add ticket to database
	public void addTicket(Ticket t) {
		try (Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO Tickets (ticket_name, project_name, description) VALUES ('"
                    + t.getName() + "', '"
                    + t.getProjName() + "', '"  
                    + t.getDescription() + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	//fetches tickets from database and places them into an arraylist
	public ArrayList<Ticket> getTickets(){
		ArrayList<Ticket> ticketArray = new ArrayList<Ticket>();
		try (Statement stmt = conn.createStatement()){
			ResultSet rs = stmt.executeQuery("SELECT * FROM Tickets");
			while(rs.next()) {
				String tName = rs.getString("ticket_name");
				String projName = rs.getString("project_name");
				String des = rs.getString("description");
				ticketArray.add(new Ticket(tName, projName, des));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return ticketArray;
	}
	
	//grabs all the tickets that have name as a substring in the ticket_name or project_name
	public ArrayList<Ticket> getTickets(String name){
		ArrayList<Ticket> ticketArray = new ArrayList<Ticket>();
		try (Statement stmt = conn.createStatement()){
			ResultSet rs = stmt.executeQuery("SELECT * FROM Tickets WHERE ticket_name LIKE '%"+name+"%' OR project_name LIKE '%"+name+"%'");
			while(rs.next()) {
				String tName = rs.getString("ticket_name");
				String projName = rs.getString("project_name");
				String des = rs.getString("description");
				ticketArray.add(new Ticket(tName, projName, des));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return ticketArray;
	}
	
	//gets ticket with exact matching ticket name
		public Ticket getSingleTicket(String ticketName) {
	        try (Statement stmt = conn.createStatement()) {
	            ResultSet rs = stmt.executeQuery("SELECT * FROM Tickets WHERE ticket_name = '"+ticketName+"'");
	            
	            String tName = rs.getString("ticket_name");
				String projName = rs.getString("project_name");
				String des = rs.getString("description");
				// after fetching the tname, desc, etc, if the tname is null, it means that the ticket was not found in sqlLite
				if (tName == null) {
	            	return null;
	            }
				
	            Ticket ticket = new Ticket(tName, projName, des);
	            return ticket;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        //should never hit this but it wont compile unless you return something
	        return new Ticket(null, null, null);
		}
		
		//delete ticket with exact matching ticket name
		public void deleteTicket(String name) {
			try (Statement stmt = conn.createStatement()) {
	            String sql = "DELETE FROM Tickets WHERE ticket_name = '"+name+"'";
	            stmt.executeUpdate(sql);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
		
		//updates the ticket_name, project_name, and description for ticket that matches oldName
		public void updateTicket(String oldName, Ticket ticket) {
			try (Statement stmt = conn.createStatement()) {
	            String sql = "UPDATE Tickets SET ticket_name = '"
	                    + ticket.getName() + "', project_name = '"
	                    + ticket.getProjName() + "', description = '"
	                    + ticket.getDescription() + "' WHERE ticket_name = '"
	                    + oldName +"'";
	            stmt.executeUpdate(sql);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		}
		
}
