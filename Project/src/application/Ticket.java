package application;

public class Ticket {
	private String name;
	private String projName;
	private String description;
	
	public Ticket(String na, String pName, String des) {
		this.name = na;
		this.projName = pName;
		this.description = des;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public String getName() {
		return name;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
