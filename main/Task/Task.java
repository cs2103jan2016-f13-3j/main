package Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Task implements java.io.Serializable {
	private String issue;
	private ArrayList<String> label;
	private boolean isCompleted;
	private Calendar date;
	private int priority = 0;

	// Constructors

	// Constructor for dateless tasks
	public Task(String issue) {
		assert issue != null;
		this.issue = issue;
		isCompleted = false;
		label = new ArrayList<String>();
		date = null;
	}

	// Constructor for tasks with date given
	public Task(String issue, String date) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert date.contains("/");
		this.issue = issue;
		isCompleted = false;
		label = new ArrayList<String>();
		String[] splitDates = date.split("/");
		int year = Integer.parseInt(splitDates[2]);
		int month = Integer.parseInt(splitDates[1]);
		int day = Integer.parseInt(splitDates[0]);
		this.date = new GregorianCalendar(year, month, day);
	}

	// Setter Methods
	public void setIssue(String issue) {
		assert issue != null;
		this.issue = issue;
	}

	public void setLabel(String label) {
		assert label != null;
		this.label.add(label);
	}

	public void removeLabel(String label) {
		assert label != null;
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
	public String getDateString(){
		if(date!=null){
		String a=date.get(Calendar.DAY_OF_MONTH)+"/";
		int n=date.get(Calendar.MONTH);
		if(n == 0) {
			n=12;
		}
		a+=n+"/";
		int year = date.get(Calendar.YEAR);
		if (n == 12) {
			year -= 1;
		}
		a+=year;
		return a;
		}else{
			return "";
		}
	}

	public String getPriority(){
		if(priority == 1){
			return "high";

		} else {
			return "low";
		}
	}

	public void setPriority(String pri){
		if(pri.equalsIgnoreCase("high")){
			priority = 1;
		}
	}
	// Returns date in string format of DD/MM/YYYY
	public String getTaskString() {
		if (date == null) {
			return issue;
		} else {
			String dateString = "";
			String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
			if (day.length() == 1) {
				day = "0" + day;
			}
			dateString += day;
			String month = String.valueOf(date.get(Calendar.MONTH));
			if (month.equals("0")) {
				month = "12";
			}
			if (month.length() == 1) {
				month = "0" + month;
			}
			dateString += "/" + month;
			int year = date.get(Calendar.YEAR);
			if (month.equals("12")) {
				year -= 1;
			}
			dateString += "/" + year;
			return issue + " " + dateString;
		}
	}


	//	public String getDateString() { //not in use
	//		String dateString = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
	//		dateString += "/" + String.valueOf(date.get(Calendar.MONTH));
	//		dateString += "/" + String.valueOf(date.get(Calendar.YEAR));
	//		return dateString;
	//	}
}
