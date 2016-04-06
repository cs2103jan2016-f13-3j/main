//@@author Jie Wei
package Task;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Parser.Natty;

public class Task implements java.io.Serializable {
	private String issue;
	private String msg;
	private ArrayList<String> label;
	private boolean isCompleted, hasTime;
	private Calendar startDate, endDate;
	private String priority;
	private  String lastDate = "-";
	private  int frequency = -1;
	private  int dayBefore = -1;
	private static final String[] NAMES_OF_MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	private static int counter = 0;
	private String dateCompare;
	private String fixedStartDate;
	private String id;
	private static final String MSG_RECURSE_FREQUENCY = "(Recurs every ";
	private static final String MSG_DAYS = " day(s))";

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
		priority = "low";
	}

	// Constructor for tasks with only one date given
	public Task(String issue, String date, String msg, boolean isStartDate) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert date.contains("/");
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		label = new ArrayList<String>();
		priority = "low";
		String[] splitDates = date.split("/");
		int year = Integer.parseInt(splitDates[2]);
		int month = Integer.parseInt(splitDates[1])-1;
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
		priority = "low";
		String[] splitStartDate = startDate.split("/");
		int year = Integer.parseInt(splitStartDate[2]);
		int month = Integer.parseInt(splitStartDate[1])-1;
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
		month = Integer.parseInt(splitEndDate[1])-1;
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
	// Constructor for recurring tasks with only one date given
	public Task(String issue, String date, String msg, boolean isStartDate,int f,int d,String last) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert date.contains("/");
		id = "0000-0000-000" + counter;
		counter++;
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		frequency = f;
		dayBefore = d;
		lastDate = last;
		dateCompare = date;
		label = new ArrayList<String>();
		priority = "low";
		String[] splitDates = date.split("/");
		int year = Integer.parseInt(splitDates[2]);
		int month = Integer.parseInt(splitDates[1])-1;
		int day = Integer.parseInt(splitDates[0]);
		int hour = 0;
		int minute = 0;
		if (splitDates.length > 3) { // given date includes time
			hour = Integer.parseInt(splitDates[3]);
			minute = Integer.parseInt(splitDates[4]);

		}
		if (isStartDate) { // the date provided is a start date
			fixedStartDate  = date;
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
	public Task(String issue, String date, String msg, boolean isStartDate,int f,int d,String last,String identity) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert date.contains("/");
		id = identity;
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		frequency = f;
		dayBefore = d;
		lastDate = last;
		dateCompare = date;
		label = new ArrayList<String>();
		priority = "low";
		String[] splitDates = date.split("/");
		int year = Integer.parseInt(splitDates[2]);
		int month = Integer.parseInt(splitDates[1])-1;
		int day = Integer.parseInt(splitDates[0]);
		int hour = 0;
		int minute = 0;
		if (splitDates.length > 3) { // given date includes time
			hour = Integer.parseInt(splitDates[3]);
			minute = Integer.parseInt(splitDates[4]);

		}
		if (isStartDate) { // the date provided is a start date
			fixedStartDate  = date;
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
	// Constructor for recurring tasks with start and end dates given
	public Task(String issue, String startDate, String endDate, String msg,int f, int d, String last) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert startDate.contains("/");
		assert endDate.contains("/");
		id = "0000-0000-000" + counter;
		counter++;
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		frequency = f;
		dayBefore = d;
		lastDate = last;
		dateCompare = endDate;
		fixedStartDate  = startDate;
		label = new ArrayList<String>();
		priority = "low";
		String[] splitStartDate = startDate.split("/");
		int year = Integer.parseInt(splitStartDate[2]);
		int month = Integer.parseInt(splitStartDate[1])-1;
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
		month = Integer.parseInt(splitEndDate[1])-1;
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
	public Task(String issue, String startDate, String endDate, String msg,int f, int d, String last,String identity) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert startDate.contains("/");
		assert endDate.contains("/");
		this.msg=msg;
		id = identity;
		this.issue = issue;
		isCompleted = false;
		frequency = f;
		dayBefore = d;
		lastDate = last;
		dateCompare = endDate;
		fixedStartDate  = startDate;
		label = new ArrayList<String>();
		priority = "low";
		String[] splitStartDate = startDate.split("/");
		int year = Integer.parseInt(splitStartDate[2]);
		int month = Integer.parseInt(splitStartDate[1])-1;
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
		month = Integer.parseInt(splitEndDate[1])-1;
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
	public void setDescription(String msg){
		this.msg = msg;
	}
	public void setLabel(String label) {
		assert label != null;
		if (!this.label.contains(label)) { // prevent duplicate tag from being added
			this.label.add(label);
		}
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
	public void setLastDate(String s) {
		lastDate = s;
	}
	public void setFrequency(int n) {
		frequency = n;
	}
	public void setdayBefore(int n) {
		dayBefore = n;
	}

	public void setStartDate(String startDate) {
		if (startDate == null) {
			hasTime = false;
			this.startDate = null;
		} else {
			String[] splitStartDate = startDate.split("/");
			int year = Integer.parseInt(splitStartDate[2]);
			int month = Integer.parseInt(splitStartDate[1])-1;
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
			int month = Integer.parseInt(splitEndDate[1])-1;
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
	public String getLastDate() {
		return lastDate;
	}
	public int getFrequency() {
		return frequency;
	}
	public int getDayBefore() {
		return dayBefore;
	}
	public String getMsg() {
		return msg;
	}
	public String getStartDateString() {
		if (startDate != null) {

			String result = startDate.get(Calendar.DAY_OF_MONTH) + " ";
			if (result.length() == 2) { // adds 0 to single digit dates
				result = "0" + result;
			}
			int month = startDate.get(Calendar.MONTH);
			result += NAMES_OF_MONTHS[month] + " ";

			int year = startDate.get(Calendar.YEAR);
			result += year;			

			String dayName = Natty.getInstance().getDayName(result); // get the name of the day, eg Sun, Mon
			result = Natty.getInstance().tryChangeTodayOrTomorrow(result); // check if is today or tomorrow and change accordingly

			result += " " + dayName;

			if (hasTime) {
				int hour =startDate.get(Calendar.HOUR_OF_DAY);
				if (hour > 12) {
					hour -= 12; // 24hr to 12hr format
				}

				if (hour < 10) {
					result += " " + "0" + hour;
				} else {
					result += " " + hour;
				}

				String minute = Integer.toString(startDate.get(Calendar.MINUTE));
				if (minute.length() == 1) {
					minute = "0" + minute;
				}
				if (startDate.get(Calendar.HOUR_OF_DAY) < 12) {
					result += ":" + minute + "AM" + "\t";
				} else {
					result += ":" + minute + "PM" + "\t";
				}
			}else{
				result += "\t";
			}
			return result;

		} else { // return empty string if the task has no start date
			return "\t\t";
		}
	}

	public String getEndDateString() {
		if (endDate != null) {
			String day = Integer.toString(endDate.get(Calendar.DAY_OF_MONTH));

			String result = day + " ";
			if (result.length() == 2) { // adds 0 to single digit dates
				result = "0" + result;
			}
			int month = endDate.get(Calendar.MONTH);
			result += NAMES_OF_MONTHS[month] + " ";

			int year = endDate.get(Calendar.YEAR);
			result += year;

			String dayName = Natty.getInstance().getDayName(result); // get the name of the day, eg Sun, Mon
			result = Natty.getInstance().tryChangeTodayOrTomorrow(result); // check if is today or tomorrow and change accordingly

			result += " " + dayName;

			if (hasTime) {
				int hour = endDate.get(Calendar.HOUR_OF_DAY);
				if (hour > 12) {
					hour -= 12; // 24hr to 12hr format
				}

				if (hour < 10) {
					result += " " + "0" + hour;
				} else {
					result += " " + hour;
				}

				String minute = Integer.toString(endDate.get(Calendar.MINUTE));
				if (minute.length() == 1) {
					minute = "0" + minute;
				}
				if (endDate.get(Calendar.HOUR_OF_DAY) < 12) {
					result += ":" + minute + "AM" + "\t";
				} else {
					result += ":" + minute + "PM" + "\t";
				}
				//				result += ":" + minute + "\t";
			}else{
				result += "\t";
			}
			return result;
		} else { // return empty string if the task has no end date
			return "\t\t";
		}
	}

	public String getDateCompare() {
		return dateCompare;
	}
	public String getFixedStartDateString() {
		return fixedStartDate;
	}
	public String getId() {
		return id;
	}
	public String getPriority(){
		return priority;
	}


	public void setPriority(String priority){
		this.priority = priority;
	}
	// Returns date in string format of DD/MM/YYYY
	public String getTaskString() {
		return getStartDateString() +  getEndDateString() +issue;
		/*if (startDate == null && endDate == null) {
			return issue;
		} else {
			if (startDate == null) {
				return " - " + getEndDateString() + " " + issue;
			} else if (endDate == null) {
				return getStartDateString() + " -" + " " + issue  ;
			}  else {
				return getStartDateString() + " " + getEndDateString() + " " + issue;
			}
		}*/
	}

	public String getStartDateLineOne() { // Method to return DD MTH YYYY
		if (startDate == null) {
			return null;
		} else {
			String result = startDate.get(Calendar.DAY_OF_MONTH) + " ";
			if (result.length() == 2) { // adds 0 to single digit dates
				result = "0" + result;
			}
			int month = startDate.get(Calendar.MONTH);
			result += NAMES_OF_MONTHS[month] + " ";

			int year = startDate.get(Calendar.YEAR);
			result += year;	
			result = Natty.getInstance().tryChangeTodayOrTomorrow(result); // check if is today or tomorrow and change accordingly
			return result;
		}
	}

	public String getStartDateLineTwo() { // Method to return DAY or DAY HH:MMam
		if (startDate == null) {
			return null;
		} else {
			String result = getStartDateLineOne();
			result = Natty.getInstance().getDayName(result); // get the name of the day, eg Sun, Mon

			if (hasTime) { // if start date has time
				int hour = startDate.get(Calendar.HOUR_OF_DAY);
				if (hour > 12) {
					hour -= 12; // 24hr to 12hr format
				}

				if (hour < 10) {
					result += " " + "0" + hour;
				} else {
					result += " " + hour;
				}

				String minute = Integer.toString(startDate.get(Calendar.MINUTE));
				if (minute.length() == 1) {
					minute = "0" + minute;
				}
				if (startDate.get(Calendar.HOUR_OF_DAY) < 12) {
					result += ":" + minute + "AM" + "\t";
				} else {
					result += ":" + minute + "PM" + "\t";
				}
			} else {
				result += "\t";
			}
			return result;
		}
	}
	
	public String getEndDateLineOne() { // Method to return DD MTH YYYY
		if (endDate == null) {
			return null;
		} else {
			String result = endDate.get(Calendar.DAY_OF_MONTH) + " ";
			if (result.length() == 2) { // adds 0 to single digit dates
				result = "0" + result;
			}
			int month = endDate.get(Calendar.MONTH);
			result += NAMES_OF_MONTHS[month] + " ";

			int year = endDate.get(Calendar.YEAR);
			result += year;	
			result = Natty.getInstance().tryChangeTodayOrTomorrow(result); // check if is today or tomorrow and change accordingly
			return result;
		}
	}

	public String getEndDateLineTwo() { // Method to return DAY or DAY HH:MMam
		if (endDate == null) {
			return null;
		} else {
			String result = getEndDateLineOne();
			result = Natty.getInstance().getDayName(result); // get the name of the day, eg Sun, Mon

			if (hasTime) { // if start date has time
				int hour = endDate.get(Calendar.HOUR_OF_DAY);
				if (hour > 12) {
					hour -= 12; // 24hr to 12hr format
				}

				if (hour < 10) {
					result += " " + "0" + hour;
				} else {
					result += " " + hour;
				}

				String minute = Integer.toString(endDate.get(Calendar.MINUTE));
				if (minute.length() == 1) {
					minute = "0" + minute;
				}
				if (endDate.get(Calendar.HOUR_OF_DAY) < 12) {
					result += ":" + minute + "AM" + "\t";
				} else {
					result += ":" + minute + "PM" + "\t";
				}
			} else {
				result += "\t";
			}
			return result;
		}
	}
	
	public String getRecurFrequency() {
		if (frequency < 1) { // if not recurring task
			return "";
		}
		return MSG_RECURSE_FREQUENCY + frequency + MSG_DAYS;
	}
}
