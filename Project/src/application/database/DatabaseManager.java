package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:projectDB.sqlite"; // Name of the database file.
    private Connection conn;

    public DatabaseManager() {
        try {
        	SQLiteConfig config = new SQLiteConfig();
        	//foreign keys off by default
        	config.enforceForeignKeys(true);
            conn = DriverManager.getConnection(URL,config.toProperties());
            createTables();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        try (Statement stmt = conn.createStatement()) {
            String createProjectsTable = "CREATE TABLE IF NOT EXISTS Projects (" +
                "project_name TEXT NOT NULL PRIMARY KEY," +
                "start_date TEXT NOT NULL," +
                "description TEXT" +
                ")";
            stmt.execute(createProjectsTable);

            //foreign key project_name which will delete or update if deleted or updated  from Projects
            String createTicketsTable = "CREATE TABLE IF NOT EXISTS Tickets (" +
                "ticket_name TEXT PRIMARY KEY	," +
                "project_name TEXT NOT NULL," +
                "description TEXT" +
                "comment TEXT," +
                "FOREIGN KEY(project_name) REFERENCES Projects(project_name) ON UPDATE CASCADE ON DELETE CASCADE" +
                ")";
            stmt.execute(createTicketsTable);

            //primary key id so we can differentiate between comments even if their description has been changed
            //foreign key ticket_name which will delete or update if deleted or updated from Tickets
            String createCommentsTable = "CREATE TABLE IF NOT EXISTS Comments (" +
            	"id INTEGER PRIMARY KEY," +
                "ticket_name TEXT," +
                "description TEXT," +
                "timestamp TEXT," +
                "FOREIGN KEY(ticket_name) REFERENCES Tickets(ticket_name) ON UPDATE CASCADE ON DELETE CASCADE" +
                ")";
            stmt.execute(createCommentsTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }
}
