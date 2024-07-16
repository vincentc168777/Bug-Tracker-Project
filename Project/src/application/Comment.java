package application;


public class Comment {
	private int id;
	private String ticketName;
	private String description;
	private String timestamp;

	//no input id means creating a comment that we have never stored in the db before
	public Comment(String ticketName,String des,String timestamp) {
		this.id = 0;
		this.ticketName = ticketName;
		this.description = des;
		this.timestamp = timestamp;
	}

	//creating a comment that we have stored in before
	public Comment(int id,String ticketName,String des,String timestamp) {
		this.id = id;
		this.ticketName = ticketName;
		this.description = des;
		this.timestamp = timestamp;
	}
	
	//id doesn't need setter since it should only be set by the db
	public int getID() {
		return id;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getTimestamp() {
		return timestamp;
	}

	public String getTicketName() {
		return ticketName;
	}

	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
