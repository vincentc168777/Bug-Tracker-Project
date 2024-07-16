package application.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import application.CommonObjs;
import application.Project;

// any project creation methods that involve the database go here
public class ProjDBCommunicator {
	
	private CommonObjs common = CommonObjs.getInstance();
	private DatabaseManager dbRef;
	private Connection conn;
	
	public ProjDBCommunicator() {
		dbRef = common.getDatabase();
		conn = dbRef.getConnection();
	}
	
	public void addProj(Project newProj) {
		try (Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO Projects (project_name, start_date, description) VALUES ('"
                    + newProj.getName() + "', '"
                    + newProj.getDate().getTime() + "', '"  // Assuming you're storing date as a timestamp
                    + newProj.getDescription() + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	//get all projects in db
	public ArrayList<Project> getProjects(){
        ArrayList<Project> projects = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Projects");
            while (rs.next()) {
                String name = rs.getString("project_name");
                Date date = new Date(rs.getLong("start_date"));  // Assuming date was stored as a timestamp
                String description = rs.getString("description");
                projects.add(new Project(name, date, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
	
	//grabs all the projects that have projName as a substring in the name
	public ArrayList<Project> getProjects(String projName){
        ArrayList<Project> projects = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Projects WHERE project_name LIKE '%"+projName+"%'");
            while (rs.next()) {
                String name = rs.getString("project_name");
                Date date = new Date(rs.getLong("start_date"));  // Assuming date was stored as a timestamp
                String description = rs.getString("description");
                projects.add(new Project(name, date, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
	
	//gets project with exact matching project name
	public Project getSingleProject(String projName) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Projects WHERE project_name = '"+projName+"'");
            
            String name = rs.getString("project_name");
            Date date = new Date(rs.getLong("start_date"));  // Assuming date was stored as a timestamp
            String description = rs.getString("description");
            // after fetching the name, desc, etc, if the name is null, it means that the proj was not found in sqlLite
            if(name == null) {
            	return null;
            }
            
            Project project = new Project(name, date, description);
            return project;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //should never hit this but it wont compile unless you return something
        return new Project(null, new Date(), null);
	}
	
	//delete project with exact matching project name
	public void deleteProject(String name) {
		try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM Projects WHERE project_name = '"+name+"'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	//updates the project_name, starting_date, and description for project that matches oldName
	public void updateProject(String oldName, Project proj) {
		try (Statement stmt = conn.createStatement()) {
            String sql = "UPDATE Projects SET project_name = '"
                    + proj.getName() + "', start_date = '"
                    + proj.getDate().getTime() + "', description = '"  // Assuming you're storing date as a timestamp
                    + proj.getDescription() + "' WHERE project_name = '"
                    + oldName +"'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}
