import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Task {
	private String issue;
	private ArrayList<String> label;
	private boolean isCompleted;
	private Calendar date;
	
	// Constructors
	
	// Constructor for dateless tasks
	public Task(String issue) {
		this.issue = issue;
		isCompleted = false;
		label = new ArrayList<String>();
		date = null;
	}
	
	// Constructor for tasks with date given
	public Task(String issue, String date) { // assuming String date provided is of the format DD/MM/YYYY
		this.issue = issue;
		isCompleted = false;
		label = new ArrayList<String>();
		String[] splitDates = date.split("/");
		int year = Integer.parseInt(splitDates[2]);
		int month = Integer.parseInt(splitDates[1]);
		int day = Integer.parseInt(splitDates[0]);
		this.date = new GregorianCalendar(year, month, day);
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
	
	// Returns date in string format of DD/MM/YYYY
	public String printTask() {
		String dateString = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
		dateString += "/" + String.valueOf(date.get(Calendar.MONTH));
		dateString += "/" + String.valueOf(date.get(Calendar.YEAR));
		return issue + dateString;
	}
}
