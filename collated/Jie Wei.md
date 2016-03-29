# Jie Wei
###### main\Logic\crud.java
``` java
	 /**
	  * Function to import the tasks from the storage file
	  * @param task the tasks to be added to the arraylist storage
	  * @throws IOException 
	  * @throws ClassNotFoundException 
	  */
	 public static void addTaskViaImport(Task task, String flag) throws IOException, ClassNotFoundException {
		 if (flag.equals(FLAG_UNCOMPLETED)) {
			 Storage.localStorage.addToUncompletedTasks(task);
		 } else if (flag.equals(FLAG_COMPLETED)) {
			 Storage.localStorage.addToCompletedTasks(task);
		 } else if (flag.equals(FLAG_FLOATING)) {
			 Storage.localStorage.addToFloatingTasks(task);
		 }
	 }

```
###### main\Logic\ImportTasks.java
``` java
package Logic;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import Task.Task;

public class ImportTasks {

	private static final Logger logger = Logger.getLogger(Class.class.getName()); 

	/**
	 * Function to import tasks from a given storage file
	 * 
	 * @param fileName String that contains the name of the storage file
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */

	public static void importTasksFromStorage(String fileName, String flag) throws IOException, ClassNotFoundException {
		//		LoggerTry.startLog();
		JsonReader reader = new JsonReader(new FileReader(fileName));
		ArrayList<Task> GsonObjects = new ArrayList<Task>();
		try {
			reader.beginArray();
			while (reader.hasNext()) {
				Task obj = (new Gson()).fromJson(reader, Task.class);
				GsonObjects.add(obj);
			}
			reader.endArray();
			reader.close();
			for (Task t : GsonObjects) {
				Logic.crud.addTaskViaImport(t, flag);
			}
		} catch (EOFException e) {
			return;
		}
	}
}

class LoggerTry {
	private final static Logger logger = Logger.getLogger(Class.class.getName());
	private static FileHandler fileHandler = null;

	public static void startLog() throws SecurityException, IOException {
		fileHandler = new FileHandler("loggerFile.log", true);
		Logger logger = Logger.getLogger("");
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
		logger.setLevel(Level.CONFIG);
	}
}
```
###### main\Logic\Save.java
``` java
package Logic;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Task.Task;

public class Save {

	/**
	 * Function to save the uncompleted tasks arraylist into the storage file
	 * 
	 */
	public static void saveUncompletedTasksToFile(String fileName) throws IOException {
		saveArrayToFile(Storage.localStorage.getUncompletedTasks(), fileName);
	}

	/**
	 * Function to save the completed tasks arraylist into the storage file
	 * 
	 */
	public static void saveCompletedTasksToFile(String fileName) throws IOException {
		saveArrayToFile(Storage.localStorage.getCompletedTasks(), fileName);
	}

	/**
	 * Function to save the floating tasks arraylist into the storage file
	 * 
	 */
	public static void saveFloatingTasksToFile(String fileName) throws IOException {
		saveArrayToFile(Storage.localStorage.getFloatingTasks(), fileName);
	}

	private static void saveArrayToFile(ArrayList<Task> sourceArray, String destinationFileName) throws IOException {
		Writer writer = new FileWriter(destinationFileName);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(sourceArray, writer);
		writer.close();
	}
}
```
###### main\Logic\Undo.java
``` java
package Logic;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Stack;

import Storage.localStorage;
import Task.Task;

public class Undo {
	private static final String NO_PAST_COMMAND = "There are no remaining commands that can be undone";
	private static Undo undo;
	private Stack<ArrayList<Task>> completedStack, uncompletedStack, floatingStack;
	private ArrayList<String> pastCommands;
	private ArrayList<Task> UncompletedTasksSnapshot, CompletedTasksSnapshot, FloatingTasksSnapshot;

	private Undo() { //constructor
		completedStack = new Stack<ArrayList<Task>>();
		uncompletedStack = new Stack<ArrayList<Task>>();
		floatingStack = new Stack<ArrayList<Task>>();
		pastCommands = new ArrayList<String>();
	}

	public static Undo getInstance() { //singleton
		if (undo == null) {
			undo = new Undo();
		}
		return undo;
	}

	// returns string containing a list of undo-able commands, if any
	public String viewPastCommands() {
		String result = "";
		if (pastCommands.isEmpty()) {
			return NO_PAST_COMMAND;
		}
		int count = 0;
		for (String s : pastCommands) {
			count++;
			result += count + ". " + s + "\n";
		}
		return result.substring(0, result.length() - 1);
	}

	public ArrayList<Task> getLastCompletedState() {
		return completedStack.pop();
	}

	public ArrayList<Task> getLastUnompletedState() {
		return uncompletedStack.pop();
	}
	
	public ArrayList<Task> getLastFloatingState() {
		return floatingStack.pop();
	}

	public String getLastCommand() {
		return pastCommands.remove(pastCommands.size() - 1);
	}

	// replaces current state in localStorage to a "snapshot" taken previously
	public String undo() {
		if (pastCommands.isEmpty()) {
			return NO_PAST_COMMAND;
		}
		localStorage.revertToPreviousState(getLastCompletedState(), getLastUnompletedState(), getLastFloatingState());
		return getLastCommand() + " has been undone";
	}

	// copy all 3 task arraylists as "snapshots" of the current program state
	public void copyCurrentTasksState() throws ClassNotFoundException, IOException {
		UncompletedTasksSnapshot = copyArrayList(Storage.localStorage.getUncompletedTasks());
		CompletedTasksSnapshot = copyArrayList(Storage.localStorage.getCompletedTasks());
		FloatingTasksSnapshot = copyArrayList(Storage.localStorage.getFloatingTasks());
	}
	
	// make a copy of an arraylist
	// input & output streams are used to ensure no shared references
	private static ArrayList<Task> copyArrayList(ArrayList<Task> sourceArray) throws IOException, ClassNotFoundException {	
		ArrayList<Task> CopyOfArraylist = new ArrayList<Task>(sourceArray.size());
		for (Task t : sourceArray) {
			
			// convert tasks to bytes
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(t);
			oos.flush();
			oos.close();
			bos.close();
			byte[] byteData = bos.toByteArray();

			// restore task from bytes and add to the copied arraylist
			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			Task tempTask = (Task) new ObjectInputStream(bais).readObject();
			CopyOfArraylist.add(tempTask);
		}
		return CopyOfArraylist;
	}

	// adds the "snapshots" of the arraylists into stacks for undo purposes. The command executed is also stored.
	public void storePreviousState(String command) {
		completedStack.push(CompletedTasksSnapshot);
		uncompletedStack.push(UncompletedTasksSnapshot);
		floatingStack.push(FloatingTasksSnapshot);
		pastCommands.add(command);
	}
	
	
}
```
###### main\Task\Task.java
``` java
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
```
