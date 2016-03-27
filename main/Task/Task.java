//@@author Jie Wei
package Task;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

// modify all get date string methods to work with time!!
// get startdateString() (also enddatestring) return as "01/01/2000 12:00"

public class Task implements java.io.Serializable {
	private String issue;
	private String msg;
	private ArrayList<String> label;
	private boolean isCompleted, hasTime;
	private Calendar startDate, endDate;
	private int priority = 0;

	// Constructors

	// Constructor for dateless tasks
	public Task(String issue) {
		assert issue != null;
		this.msg = issue;
		this.issue = issue;
		isCompleted = false;
		hasTime = false;
		label = new ArrayList<String>();
		startDate = null;
		endDate = null;
	}

	// Constructor for tasks with only one date given
	public Task(String issue, String date, String msg, boolean isStartDate) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert date.contains("/");
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		label = new ArrayList<String>();
		String[] splitDates = date.split("/");
		int year = Integer.parseInt(splitDates[2]);
		int month = Integer.parseInt(splitDates[1]);
		int day = Integer.parseInt(splitDates[0]);
		int hour = 0;
		int minute = 0;
		if (splitDates.length > 3) { // given date includes time
			hour = Integer.parseInt(splitDates[3]);
			minute = Integer.parseInt(splitDates[4]);
		}
		if (isStartDate) { // the date provided is a start date
			if (splitDates.length > 3) { // has time
				hasTime = true;
				startDate = new GregorianCalendar(year, month, day, hour, minute);
			} else { // does not have time
				startDate = new GregorianCalendar(year, month, day);
			}
			endDate = null;
		} else { // the date provided is an end date
			startDate = null;
			if (splitDates.length > 3) { // has time
				hasTime = true;
				endDate = new GregorianCalendar(year, month, day, hour, minute);
			} else { // no time given
				endDate = new GregorianCalendar(year, month, day);
			}
		}
	}

	// Constructor for tasks with start and end dates given
	public Task(String issue, String startDate, String endDate, String msg) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert startDate.contains("/");
		assert endDate.contains("/");
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		label = new ArrayList<String>();
		String[] splitStartDate = startDate.split("/");
		int year = Integer.parseInt(splitStartDate[2]);
		int month = Integer.parseInt(splitStartDate[1]);
		int day = Integer.parseInt(splitStartDate[0]);
		int hour = 0;
		int minute = 0;
		if (splitStartDate.length > 3) { // given start date includes time
			hasTime = true;
			hour = Integer.parseInt(splitStartDate[3]);
			minute = Integer.parseInt(splitStartDate[4]);
			this.startDate = new GregorianCalendar(year, month, day, hour, minute);
		} else { // given start date does not have time
			this.startDate = new GregorianCalendar(year, month, day);
		}

		String[] splitEndDate = endDate.split("/");
		year = Integer.parseInt(splitEndDate[2]);
		month = Integer.parseInt(splitEndDate[1]);
		day = Integer.parseInt(splitEndDate[0]);
		if (splitEndDate.length > 3) { // given end date includes time
			hasTime = true;
			hour = Integer.parseInt(splitEndDate[3]);
			minute = Integer.parseInt(splitEndDate[4]);
			this.endDate = new GregorianCalendar(year, month, day, hour, minute);
		} else { // given end date has not time
			this.endDate = new GregorianCalendar(year, month, day);
		}
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

	public void setStartDate(String startDate) {
		if (startDate == null) {
			hasTime = false;
			this.startDate = null;
		} else {
			String[] splitStartDate = startDate.split("/");
			int year = Integer.parseInt(splitStartDate[2]);
			int month = Integer.parseInt(splitStartDate[1]);
			int day = Integer.parseInt(splitStartDate[0]);
			int hour = 0;
			int minute = 0;
			if (splitStartDate.length > 3) { // given date includes time
				hasTime = true;
				hour = Integer.parseInt(splitStartDate[3]);
				minute = Integer.parseInt(splitStartDate[4]);
				this.startDate = new GregorianCalendar(year, month, day, hour, minute);
			} else {
				hasTime = false;
				this.startDate = new GregorianCalendar(year, month, day);
			}
		}
	}

	public void setEndDate(String endDate) {
		if (endDate == null) {
			hasTime = false;
			this.endDate = null;
		} else {
			String[] splitEndDate = endDate.split("/");
			int year = Integer.parseInt(splitEndDate[2]);
			int month = Integer.parseInt(splitEndDate[1]);
			int day = Integer.parseInt(splitEndDate[0]);
			int hour = 0;
			int minute = 0;
			if (splitEndDate.length > 3) { // given date includes time
				hasTime = true;
				hour = Integer.parseInt(splitEndDate[3]);
				minute = Integer.parseInt(splitEndDate[4]);
				this.endDate = new GregorianCalendar(year, month, day, hour, minute);
			} else {
				hasTime = false;
				this.endDate = new GregorianCalendar(year, month, day);
			}
		}
	}

	// Getter Methods
	public String getIssue() {
		return issue;
	}
	public String getDescription(){
		return msg;
	}

	public ArrayList<String> getLabel() {
		return label;
	}

	public boolean getCompletedStatus() {
		return isCompleted;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public String getStartDateString() {
		if (startDate != null) {
			String result = startDate.get(Calendar.DAY_OF_MONTH) + "/";
			int month = startDate.get(Calendar.MONTH);
			if (month == 0) {
				month = 12;
			}
			result += month + "/";
			int year = startDate.get(Calendar.YEAR);
			if (month == 12) {
				year -= 1;
			}
			result += year;
			if (hasTime) {
				String hour = Integer.toString(startDate.get(Calendar.HOUR_OF_DAY));
				if (hour.length() == 1) {
					hour = "0" + hour;
				}
				result += " " + hour;
				String minute = Integer.toString(startDate.get(Calendar.MINUTE));
				if (minute.length() == 1) {
					minute = "0" + minute;
				}
				result += ":" + minute;
			}
			return result;
		} else { // return empty string if the task has no start date
			return "";
		}
	}

	public String getEndDateString() {
		if (endDate != null) {
			String result = endDate.get(Calendar.DAY_OF_MONTH) + "/";
			int month = endDate.get(Calendar.MONTH);
			if (month == 0) {
				month = 12;
			}
			result += month + "/";
			int year = endDate.get(Calendar.YEAR);
			if (month == 12) {
				year -= 1;
			}
			result += year;
			if (hasTime) {
				String hour = Integer.toString(endDate.get(Calendar.HOUR_OF_DAY));
				if (hour.length() == 1) {
					hour = "0" + hour;
				}
				result += " " + hour;
				String minute = Integer.toString(endDate.get(Calendar.MINUTE));
				if (minute.length() == 1) {
					minute = "0" + minute;
				}
				result += ":" + minute;
			}
			return result;
		} else { // return empty string if the task has no end date
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
		if (startDate == null && endDate == null) {
			return issue;
		} else {
			if (startDate == null) {
				return issue + " - " + getEndDateString();
			} else if (endDate == null) {
				return issue + " " + getStartDateString() + " -";
			}  else {
				return issue + " " + getStartDateString() + " " + getEndDateString();
			}
		}
	}
}
