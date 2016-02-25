import java.util.ArrayList;
import java.util.Calendar;

public class Task {
	private String issue;
	private ArrayList<String> label;
	private boolean isCompleted;
	private Calendar date;
	
	// Constructors
	public Task(String issue) {
		this.issue = issue;
		isCompleted = false;
		label = new ArrayList<String>();
	}
	
	public Task(String issue, String date) {
		this.issue = issue;
		isCompleted = false;
		label = new ArrayList<String>();
//		split the String date into separate integers for next line, no idea how date input format is yet
//		Calendar tempCalendar = new GregorianCalendar(year, month, day);
//		this.date = tempCalendar;
	}
	
	// Setter methods
	public void setIssue(String issue) {
		this.issue = issue;
	}
	
	public void setLabel(String label) {
		this.label.add(label);
	}
	
	public void removeLabel(String label) {
		this.label.remove(label);
	}
	
	public void setComplete() {
		isCompleted = true;
	}
	
	public void setUncomplete() {
		isCompleted = false;
	}
	
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	// Getter Methods
	public String getIssue() {
		return issue;
	}
	
	public ArrayList<String> getLabel() {
		return label;
	}
	
	public boolean getCompletedStatus() {
		return isCompleted;
	}
	
	public Calendar getDate() {
		return date;
	}
}
