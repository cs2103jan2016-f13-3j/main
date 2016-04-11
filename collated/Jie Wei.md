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
			 noDuplicate = checkForDuplicateTasks(task, Storage.localStorage.getUncompletedTasks());
			 if (noDuplicate) {
				 Storage.localStorage.addToUncompletedTasks(task);
			 }
		 } else if (flag.equals(FLAG_COMPLETED)) {
			 noDuplicate = checkForDuplicateTasks(task, Storage.localStorage.getCompletedTasks());
			 if (noDuplicate) {
				 Storage.localStorage.addToCompletedTasks(task);
			 }
		 } else if (flag.equals(FLAG_FLOATING)) {
			 noDuplicate = checkForDuplicateTasks(task, Storage.localStorage.getFloatingTasks());
			 if (noDuplicate) {
				 Storage.localStorage.addToFloatingTasks(task);
			 }
		 } 
		 
	 }

	 private static boolean checkForDuplicateTasks(Task task, ArrayList<Task> destination) {
		 boolean noDuplicate = true;
		 for(Task temp : destination) {
			 if(temp.getTaskString().equals(task.getTaskString())) {
				 noDuplicate = false;
				 break;
			 }
		 }
		 return noDuplicate;
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import Task.Task;

public class ImportTasks {
	private static String storageFileNameCompletedTasks = "TasksCompleted.txt";
	private static String storageFileNameUncompletedTasks = "TasksUncompleted.txt";
	private static String storageFileNameFloatingTasks = "TasksFloating.txt";
	private static final String FEEDBACK_CHANGE_DIRECTORY_DEFAULT = "The default folder is now in use as the storage folder directory";
	private static final String FEEDBACK_CHANGE_DIRECTORY_ERROR = "The target directory is invalid. The default folder is now in use";
	private static final String FEEDBACK_CHANGE_DIRECTORY_SUCCESS = "The storage folder directory has been changed to ";
	private static final String FLAG_UNCOMPLETED = "uncompleted";
	private static final String FLAG_COMPLETED = "completed";
	private static final String FLAG_FLOATING = "floating";
	private static String storageFolderDirectory;

	private static final Logger logger = Logger.getLogger(Class.class.getName()); 

	
	
	// creates the necessary storage files if they do not exist
	// and import tasks from they, if any
	public static void prepareAndImportFiles() throws FileNotFoundException, IOException, ClassNotFoundException {
		File StorageFolderPathFile = new File("StorageFolderPath.txt");
		if (!StorageFolderPathFile.exists()) {
			StorageFolderPathFile.createNewFile();
		}
		Scanner sc = new Scanner(StorageFolderPathFile);
		try {
			storageFolderDirectory = sc.nextLine();
		} catch (NoSuchElementException e) { // empty file (no directory specified)
			storageFolderDirectory = "";
		}
		sc.close();
		if (storageFolderDirectory.equals("default")) {
			storageFolderDirectory = "";
		}
		
		checkIfFileExists(storageFolderDirectory, storageFileNameUncompletedTasks);
		checkIfFileExists(storageFolderDirectory, storageFileNameCompletedTasks);
		checkIfFileExists(storageFolderDirectory, storageFileNameFloatingTasks);

		
		importTasksFromStorage(storageFolderDirectory + storageFileNameUncompletedTasks, FLAG_UNCOMPLETED);
		importTasksFromStorage(storageFolderDirectory + storageFileNameCompletedTasks, FLAG_COMPLETED);
		importTasksFromStorage(storageFolderDirectory + storageFileNameFloatingTasks, FLAG_FLOATING);
	}

	public static void checkIfFileExists(String folderDirectory, String fileName) throws IOException, FileNotFoundException {
		File file = new File(folderDirectory + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) { // occurs when the specified folder directory does not exist
				file = new File(fileName);
				file.createNewFile();
				storageFolderDirectory = ""; // replace the folder directory to the source folder
			}
		}
	}

	/**
	 * Function to import tasks from a given storage file
	 * 
	 * @param fileName String that contains the name of the storage file
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */

	private static void importTasksFromStorage(String fileName, String flag) throws IOException, ClassNotFoundException {
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
	
	public static String getUncompletedTasksStorageFileName() {
		return storageFolderDirectory + storageFileNameUncompletedTasks;
	}
	
	public static String getCompletedTasksStorageFileName() {
		return storageFolderDirectory + storageFileNameCompletedTasks;
	}
	
	public static String getFloatingTasksStorageFileName() {
		return storageFolderDirectory + storageFileNameFloatingTasks;
	}
	
	public static String getFolderDirectory() {
		return storageFolderDirectory;
	}
	
	public static String changeStorageDestination(String destination) throws IOException, ClassNotFoundException {
		File StorageFolderPathFile = new File("StorageFolderPath.txt");
		FileWriter writer = new FileWriter(StorageFolderPathFile);
		writer.write(destination);
		writer.close();
		if (destination.equals("default")) {
			storageFolderDirectory = "";
		} else {
			storageFolderDirectory = destination;
		}
		checkIfFileExists(storageFolderDirectory, storageFileNameUncompletedTasks);
		checkIfFileExists(storageFolderDirectory, storageFileNameCompletedTasks);
		checkIfFileExists(storageFolderDirectory, storageFileNameFloatingTasks);

		
		// if the new storage directory already contains the storage files,
		//all tasks (unless duplicate) in these files will be imported into the current program instance
		importTasksFromStorage(storageFolderDirectory + storageFileNameUncompletedTasks, FLAG_UNCOMPLETED);
		importTasksFromStorage(storageFolderDirectory + storageFileNameCompletedTasks, FLAG_COMPLETED);
		importTasksFromStorage(storageFolderDirectory + storageFileNameFloatingTasks, FLAG_FLOATING);
		
		if (destination.equals("default")) { // default directory was chosen
			return FEEDBACK_CHANGE_DIRECTORY_DEFAULT;
		}
		if (storageFolderDirectory.equals("")) { // invalid directory was given
			return FEEDBACK_CHANGE_DIRECTORY_ERROR;
		}
		return FEEDBACK_CHANGE_DIRECTORY_SUCCESS + storageFolderDirectory; // directory successfully changed
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
	
	public static void saveToFile() throws IOException {
		 saveUncompletedTasksToFile(Logic.ImportTasks.getUncompletedTasksStorageFileName());
		 saveCompletedTasksToFile(Logic.ImportTasks.getCompletedTasksStorageFileName());
		 saveFloatingTasksToFile(Logic.ImportTasks.getFloatingTasksStorageFileName());
	 }

	/**
	 * Function to save the uncompleted tasks arraylist into the storage file
	 * 
	 */
	private static void saveUncompletedTasksToFile(String fileName) throws IOException {
		saveArrayToFile(Storage.localStorage.getUncompletedTasks(), fileName);
	}

	/**
	 * Function to save the completed tasks arraylist into the storage file
	 * 
	 */
	private static void saveCompletedTasksToFile(String fileName) throws IOException {
		saveArrayToFile(Storage.localStorage.getCompletedTasks(), fileName);
	}

	/**
	 * Function to save the floating tasks arraylist into the storage file
	 * 
	 */
	private static void saveFloatingTasksToFile(String fileName) throws IOException {
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
	private static final String NO_REDO_COMMAND = "There are no remaining commands that can be redone";
	private static final String UNDO_CONFIRMATION = " has been undone";
	private static final String UNDO_HISTORY_HEADER = "Here are the commands you can undo, starting from the top: \n";
	private static final String REDO_HISTORY_HEADER = "Here are the commands you can redo, starting from the top: \n";
	private static final String REDO_CONFIRMATION = " has been redone";
	private static Undo undo;
	private Stack<ArrayList<Task>> completedStack, uncompletedStack, floatingStack, recurringStack, completedRedoStack, uncompletedRedoStack, floatingRedoStack;
	private ArrayList<String> undoCommands, redoCommands;
	private ArrayList<Task> uncompletedTasksSnapshot, completedTasksSnapshot, floatingTasksSnapshot;

	private Undo() { //constructor
		completedStack = new Stack<ArrayList<Task>>();
		uncompletedStack = new Stack<ArrayList<Task>>();
		floatingStack = new Stack<ArrayList<Task>>();
		recurringStack = new Stack<ArrayList<Task>>();
		undoCommands = new ArrayList<String>();

		completedRedoStack = new Stack<ArrayList<Task>>();
		uncompletedRedoStack = new Stack<ArrayList<Task>>();
		floatingRedoStack = new Stack<ArrayList<Task>>();
		redoCommands = new ArrayList<String>();
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
		if (undoCommands.isEmpty()) {
			return NO_PAST_COMMAND;
		}
		int count = 0;
		result += UNDO_HISTORY_HEADER;
		for (int i = undoCommands.size() - 1; i >= 0; i--) {
			count++;		
			result += count + ". " + undoCommands.get(i) + "\n";
		}
		return result.substring(0, result.length() - 1);
	}

	// returns string containing a list of redo-able commands, if any
	public String viewRedoCommands() {
		String result = "";
		if (redoCommands.isEmpty()) {
			return NO_REDO_COMMAND;
		}
		int count = 0;
		result += REDO_HISTORY_HEADER;
		for (int i = redoCommands.size() - 1; i >= 0; i--) {
			count++;
			result += count + ". " + redoCommands.get(i) + "\n";
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

	public ArrayList<Task> getLastRecurringState() {
		return recurringStack.pop();
	}

	public ArrayList<Task> getLastRedoCompletedState() {
		return completedRedoStack.pop();
	}

	public ArrayList<Task> getLastRedoUnompletedState() {
		return uncompletedRedoStack.pop();
	}

	public ArrayList<Task> getLastRedoFloatingState() {
		return floatingRedoStack.pop();
	}


	public String getLastCommand() {
		return undoCommands.remove(undoCommands.size() - 1);
	}

	public String getRedoneCommand() {
		return redoCommands.remove(redoCommands.size() - 1);
	}

	public int getHistoryCount() {
		return undoCommands.size();
	}

	public int getRedoCount() {
		return redoCommands.size();
	}

	// replaces current state in localStorage to a "snapshot" taken previously
	public String undo() throws ClassNotFoundException, IOException {
		if (undoCommands.isEmpty()) {
			return NO_PAST_COMMAND;
		}
		String lastCommand = getLastCommand();

		copyCurrentTasksState();
		storeCurrentStateForRedo(lastCommand); // store current snapshots for redo purposes

		localStorage.revertToPreviousState(getLastCompletedState(), getLastUnompletedState(), getLastFloatingState());
		return "\"" + lastCommand + "\"" + UNDO_CONFIRMATION;
	}

	// replaces current state in localStorage to a "snapshot" taken previously
	public String redo() throws ClassNotFoundException, IOException {
		if (redoCommands.isEmpty()) {
			return NO_REDO_COMMAND;
		}
		String redoneCommand = getRedoneCommand();

		copyCurrentTasksState();
		storePreviousState(redoneCommand); // store current snapshots for undo purposes

		localStorage.revertToPreviousState(getLastRedoCompletedState(), getLastRedoUnompletedState(), getLastRedoFloatingState());
		return "\"" + redoneCommand + "\"" + REDO_CONFIRMATION;
	}

	// copy all 3 task arraylists as "snapshots" of the current program state
	public void copyCurrentTasksState() throws ClassNotFoundException, IOException {
		uncompletedTasksSnapshot = copyArrayList(Storage.localStorage.getUncompletedTasks());
		completedTasksSnapshot = copyArrayList(Storage.localStorage.getCompletedTasks());
		floatingTasksSnapshot = copyArrayList(Storage.localStorage.getFloatingTasks());
		
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
		completedStack.push(completedTasksSnapshot);
		uncompletedStack.push(uncompletedTasksSnapshot);
		floatingStack.push(floatingTasksSnapshot);
		undoCommands.add(command);
	}

	public void storeCurrentStateForRedo(String command) {
		completedRedoStack.push(completedTasksSnapshot);
		uncompletedRedoStack.push(uncompletedTasksSnapshot);
		floatingRedoStack.push(floatingTasksSnapshot);
		redoCommands.add(command);
	}

	public void clearRedoCommands() {
		redoCommands.clear();
	}
}
```
###### main\Parser\Natty.java
``` java

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class Natty {
	private static Natty natty;
	private static Parser parser;
	private static final String DATE_INDICATOR = " ` ";
	private static final String MSG_START_DATE_INDICATOR = "on";
	private static final String MSG_TODAY = "Today";
	private static final String MSG_TOMORROW = "Tomorrow";
	private static final String[] NAMES_OF_MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	private static final String[] END_DATE_KEYWORDS = {"by", "on", "before"};
	private static ArrayList<String> monthNamesList, endDateKeywordsList;
	private static boolean hasTime, hasTwoDates;

	private Natty() {
		parser = new Parser();
		monthNamesList = new ArrayList<String>(Arrays.asList(NAMES_OF_MONTHS));
		endDateKeywordsList = new ArrayList<String>(Arrays.asList(END_DATE_KEYWORDS));
		hasTime = false;
		hasTwoDates = false;
	}

	public static Natty getInstance() {
		if (natty == null) {
			natty = new Natty();
		}
		return natty;
	}

	// Method that checks whether input date string matches today's or tomorrow's date
	// Returns "Today" if matches today, "Tomorrow" if matches tomorrow
	// Otherwise return original string
	public String tryChangeTodayOrTomorrow(String source) {
		String result = parseDay(source);
		return result;
	}

	private String parseDay(String sourceDay) {
		Calendar today = GregorianCalendar.getInstance(); // create today's Calendar and then its string form
		String todayString = today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.MONTH) + "/" + today.get(Calendar.YEAR);
		
		String[] splitDates = sourceDay.split(" "); // split the input date to create a string
		int year = Integer.parseInt(splitDates[2]);
		int month = monthNamesList.indexOf(splitDates[1]);
		int day = Integer.parseInt(splitDates[0]);
		Calendar input = new GregorianCalendar(year, month, day);
		String inputString = input.get(Calendar.DAY_OF_MONTH) + "/" + input.get(Calendar.MONTH) + "/" + input.get(Calendar.YEAR);
		
		if (inputString.equals(todayString)) { // input string matches today's string
			return MSG_TODAY;
		}

		today.add(Calendar.DAY_OF_MONTH, 1); // add 1 day to get tomorrow's Calendar and then its string form
		String tomorrowString = today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.MONTH) + "/" + today.get(Calendar.YEAR);
		
		if (inputString.equals(tomorrowString)) { // input string matches tomorrow's string
			return MSG_TOMORROW;
		}
		return sourceDay; // input is neither today nor tomorrow
	}

	public String parseString(String source) {
		if (!source.contains(" ` ")) { // no date indicator was given, thus no date to parse, return as is
			return source;
		}

		int indexOfIndicator = source.lastIndexOf(" ` "); // last index in case user uses same sequence earlier as issue
		String stringBeforeIndicator = source.substring(0, indexOfIndicator);
		String stringAfterIndicator = source.substring(indexOfIndicator + 3); // e.g. add buy egg ` <by tomorrow>
		stringAfterIndicator = convertToAmericanFormat(stringAfterIndicator); // if user enter DD/MM/YYYY, neede to convert for natty
		List<DateGroup> dateGroups = parser.parse(stringAfterIndicator);

		if (dateGroups.isEmpty()) { // if no date was found by natty
			// return the string without the indicator, treat as floating task etc
			return source.substring(0, indexOfIndicator)+ " " + source.substring(indexOfIndicator + 3);
		}

		if (inputIncludesTime(dateGroups.get(0))) { // keep track of whether user entered date with time
			hasTime = true;
		} else {
			hasTime = false;
		}

		String result = convertDateGroupToString(dateGroups); // get a DD/MM/YYYY or DD/MM/YYYY HrHr:MinMin representation of the string

		if (stringAfterIndicator.split(" ").length == 1) { // if input was only 04/05/2016, terminate early. treat it as start date
			return stringBeforeIndicator + DATE_INDICATOR + MSG_START_DATE_INDICATOR + " " + result;
		}

		String[] splitArray = stringAfterIndicator.split(" ");
		if (splitArray[splitArray.length - 1].equalsIgnoreCase("r")) { // if input command ends with "r" by itself, indicates recurring
			result += " r"; // add "r" to the result string for Parser recognition
		}

		// gets "on" from "buy book on Friday" etc.
		String keyword = getLastWordOfIssue(dateGroups.get(0), splitArray);

		result = keyword + " " + result;
		return stringBeforeIndicator + DATE_INDICATOR + result; // adds the issue description, date indicator, and parsed date together
	}
	
	public String parseEditString(String input) {
		if (!input.contains(" ` ")) { // no date indicator was given, thus no date to parse, return as is
			return input;
		}
		int indexOfIndicator = input.lastIndexOf(" ` ");
		String issueDescription =  input.substring(0, indexOfIndicator);
		String stringAfterIndicator = input.substring(indexOfIndicator + 3); // e.g. add buy egg ` <by tomorrow>
		stringAfterIndicator = convertToAmericanFormat(stringAfterIndicator); // if user enter DD/MM/YYYY, neede to convert for natty
		List<DateGroup> dateGroups = parser.parse(stringAfterIndicator);

		if (dateGroups.isEmpty()) { // if no date was found by natty
			// return the string without the indicator, treat as floating task etc
			return issueDescription + " " + stringAfterIndicator;
		}

		if (inputIncludesTime(dateGroups.get(0))) { // keep track of whether user entered date with time
			hasTime = true;
		} else {
			hasTime = false;
		}

		String result = convertDateGroupToString(dateGroups); // get a DD/MM/YYYY or DD/MM/YYYY HrHr:MinMin representation of the string

		if (stringAfterIndicator.split(" ").length == 1) { // if input was only 1 date, terminate early. treat it as start date
			return issueDescription + DATE_INDICATOR + MSG_START_DATE_INDICATOR + " " + result;
		}

		String[] splitArray = stringAfterIndicator.split(" ");
		if (splitArray[splitArray.length - 1].equalsIgnoreCase("r")) { // if input command ends with "r" by itself, indicates recurring
			result += " r"; // add "r" to the result string for Parser recognition
		}

		// gets "on" from "buy book on Friday" etc.
		String keyword = getLastWordOfIssue(dateGroups.get(0), splitArray);

		result = keyword + " " + result;
		return issueDescription + DATE_INDICATOR + result; // adds the issue description, date indicator, and parsed date together
	}

	private String convertDateGroupToString(List<DateGroup> group) {
		String result = "";
		if (group.isEmpty()) { // Natty found no dates
			return result;
		}
		List<Date> dates = group.get(0).getDates();
		String originalString = dates.toString();

		if (dates.size() == 1) { // only 1 date was parsed
			result = changeOneDateStringFormat(originalString);
			hasTwoDates = false;
		} else if (dates.size() == 2) { // 2 dates were parsed
			result = changeTwoDatesStringFormat(originalString);
			hasTwoDates = true;
		}
		return result;
	}

	// Method to change one date from [Sun Apr 03 18:35:50 SGT 2016] to 03/04/201/18/35
	private String changeOneDateStringFormat(String date) {
		String result;
		result = date.substring(1, date.length() - 1); // removes the brackets at start & end
		String[] splitDates = result.split(" ");

		String time;
		if (hasTime) { // if input contained time, convert to our format
			time = splitDates[3];
			time = " " + time.substring(0, 2) + ":" + time.substring(3, 5); // converts 18:10:00 -> 18:10 for Task class to read
		} else { // if input had no time, make sure nothing is added to the result string
			time = "";
		}

		String month = splitDates[1];
		month = Integer.toString(monthNamesList.indexOf(month) + 1); // converts Apr -> 4

		if (month.length() == 1) {
			month = "0" + month; // appends 0 to single digit months
		}
		result = splitDates[2] + "/" + month + "/" + splitDates[5] + time;

		return result;
	}


	// Method to change two dates
	private String changeTwoDatesStringFormat(String date) {
		String result;
		String startDate = date.substring(0, date.indexOf(",")); // extracts original string of the first date
		startDate = startDate + "]";
		result = changeOneDateStringFormat(startDate);

		String endDate = date.substring(date.indexOf(",") + 2);	// extracts original string of the second date
		endDate = "[" + endDate;
		result = result + " to " + changeOneDateStringFormat(endDate);

		return result;
	}

	// Method to check whether the string parsed by natty contains time
	private boolean inputIncludesTime(DateGroup dateGroup) {
		String syntaxTree = dateGroup.getSyntaxTree().toStringTree();
		return syntaxTree.contains("HOURS");
	}

	// Method to change DD/MM/YYYY to MM/DD/YYYY because natty recognises the American format
	private static String convertToAmericanFormat(String input) {
		String result = "";
		String[] strings = input.split(" ");
		for (String str : strings) {
			if (str.contains("/")) { // eg 03/04/2016 or 03/04?? BUT THIS WILL INCLUDE ALL, eg him/her, maybe split & chk if can parseInt?
				String[] split = str.split("/");

				boolean isValidNumber = checkForValidNumber(split);		
				if (!isValidNumber) {  // if not numbers, eg "him/her", treat this as non-date, and continue with the for loop
					result = addToString(result, str);
					continue;
				}

				String temp = switchDayAndMonth(split);
				result = addToString(result, temp);
			} else {
				result = addToString(result, str);
			}
		}
		return result;
	}

	// Adds the input string to a destination string.
	// Will add a blank space in front of the input string if it is not the first word
	private static String addToString(String destination, String input) {
		if (destination.isEmpty()) {
			destination = input;
		} else {
			destination += " " + input;
		}
		return destination;
	}

	// Method to change 03/04 to 04/03
	private static String switchDayAndMonth(String[] input) {
		String output = input[1] + "/" + input[0];
		if (input.length == 3) {
			output += "/" + input[2]; // adds /YYYY if present
		}
		return output;
	}

	// checks whether a string array (split from a string containing "/") is of numberic format
	private static boolean checkForValidNumber(String[] input) {
		boolean isValid = true;
		for (String string : input) { // go through each split part, eg 03, 04 and 2016
			try{
				Integer.parseInt(string); // check if they are numbers
			} catch (NumberFormatException e) { // catch non-numerical strings
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	// Returns index where the detected date keyword(s) begin
//	private static int getIndexOfDetectedDate(DateGroup dateGroup) {
//		return dateGroup.getPosition();
//	}

	// Returns the last word before the detected date keyword(s) begin
	private static String getLastWordOfIssue(DateGroup dateGroup, String[] splitArray) {
		String detectedKeyword = dateGroup.getText();
		detectedKeyword = detectedKeyword.split(" ")[0]; // get only the first word if multiple keywords found (eg Monday to Friday)
		int count = -1;
		for (String s : splitArray) {
			count++;
			if (s.equals(detectedKeyword)) {
				break;
			}			
		}
		if (count == 0) { // means user enter something like "add have lunch ` "today", without "from"/"on"/"by"/"to"
			return MSG_START_DATE_INDICATOR;
		}
		return splitArray[count - 1]; // count is where detectedKeyword is, return the word before it
	}

	public String getDayName(String source) {
		List<DateGroup> dateGroups = parser.parse(source);
		List<Date> dates = dateGroups.get(0).getDates();
		String originalString = dates.toString(); // At this point, String is "[Mon Apr 03......"
		return originalString.substring(1,4);
	}
}
```
###### main\Parser\Parser.java
``` java
	public static void changeDirectoryCommand(String s) throws IOException, ClassNotFoundException {
		if (s.isEmpty()) { // only "dir" was typed, this will display the
			// current storage folder directory in use
			String currentStorageDirectory = Logic.ImportTasks.getFolderDirectory();
			if (currentStorageDirectory.isEmpty()) { // indicates source
				// folder is in use
				UI.ui.printGreen(MSG_DIRECTORY_USED + MSG_DEFAULT_DIRECTORY);
			} else {
				UI.ui.printGreen(MSG_DIRECTORY_USED + currentStorageDirectory);
			}
		} else { // "dir <path>" was entered
			String feedback = Logic.ImportTasks.changeStorageDestination(s);
			UI.ui.print(feedback);
		}
	}

	public static void redoCommand(String s) throws ClassNotFoundException, IOException {
		if (s.isEmpty()) { // only "redo" was typed
			String outcome = Undo.getInstance().redo();
			UI.ui.printGreen(outcome);
		} else if (s.equals("all")) {
			int redoCount = Undo.getInstance().getRedoCount();
			if (redoCount == 0) { // if no commands to redo
				UI.ui.printRed(MSG_NO_REDO_COMMAND);
			} else {
				for (int i = 0; i < redoCount; i++) { // do redo for all stored
					// commands
					String outcome = Undo.getInstance().redo();
					UI.ui.printGreen(outcome);
				}
				UI.ui.printGreen(MSG_ALL_COMMANDS_REDONE);
			}
		} else { // e.g. "redo 2" will redo the latest 2 commands
			try {
				int count = Integer.parseInt(s);
				if (count < 1 || count > Undo.getInstance().getRedoCount()) { // if
					// entered
					// count
					// is
					// outside
					// valid
					// bounds
					UI.ui.printRed(MSG_INVALID_REDO_COUNT);
				} else {
					for (int i = 0; i < count; i++) { // redo the number of
						// commands specified
						if (Undo.getInstance().getRedoCount() == 0) { // all
							// commands
							// have
							// been
							// redone
							// but
							// user
							// used
							// a
							// higher
							// int
							UI.ui.printRed(MSG_NO_REDO_COMMAND);
							break;
						}
						String outcome = Undo.getInstance().redo();
						UI.ui.printGreen(outcome);
					}
				}
			} catch (NumberFormatException e) { // if non-number was entered,
				// e.g. "redo hello"
				UI.ui.printRed(MSG_INVALID_REDO_COUNT);
			}
		}
	}

	public static void undoCommand(String s) throws ClassNotFoundException, IOException {
		if (s.isEmpty()) { // only "undo" was typed
			String outcome = Undo.getInstance().undo();
			UI.ui.printGreen(outcome);
		} else if (s.equals("all")) {
			int historyCount = Undo.getInstance().getHistoryCount();
			if (historyCount == 0) { // if no commands to undo
				UI.ui.printRed(MSG_NO_PAST_COMMAND);
			} else {
				for (int i = 0; i < historyCount; i++) { // do undo for all
					// stored commands
					String outcome = Undo.getInstance().undo();
					UI.ui.printGreen(outcome);
				}
				UI.ui.printGreen(MSG_ALL_COMMANDS_UNDONE);
			}
		} else { // e.g. "undo 2" will undo the latest 2 commands
			try {
				int count = Integer.parseInt(s);
				if (count < 1 || count > Undo.getInstance().getHistoryCount()) { // if
					// entered
					// count
					// is
					// outside
					// valid
					// bounds
					UI.ui.printRed(MSG_INVALID_UNDO_COUNT);
				} else {
					for (int i = 0; i < count; i++) { // undo the number of
						// commands specified
						if (Undo.getInstance().getHistoryCount() == 0) { // all commands have been undone but user used a higher int
							UI.ui.printRed(MSG_NO_PAST_COMMAND);
							break;
						}
						String outcome = Undo.getInstance().undo();
						UI.ui.printGreen(outcome);
					}
				}
			} catch (NumberFormatException e) { // if non-number was entered,
				// e.g. "undo hello"
				UI.ui.printRed(MSG_INVALID_UNDO_COUNT);
			}
		}
	}



```
###### main\Storage\localStorage.java
``` java
	// replace the current tasks arraylists with the given arraylists, to "undo" to the previous state
	public static void revertToPreviousState(ArrayList<Task> previousCompleted, 
			ArrayList<Task> previousUncompleted,	ArrayList<Task> previousFloating) {
		completedTasks = previousCompleted;
		uncompletedTasks = previousUncompleted;
		floatingTasks = previousFloating;

	}
}
```
###### main\Task\Task.java
``` java
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
	private static final String[] NAMES_OF_MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	private String dateCompare;
	private String fixedStartDate;
	private String id ="";
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
	public Task(String issue, String date, String msg, boolean isStartDate,int f,String last) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert date.contains("/");
		Calendar CalendarID = Calendar.getInstance();
		id = String.valueOf(CalendarID.getTimeInMillis());
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		frequency = f;
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
	public Task(String issue, String date, String msg, boolean isStartDate,int f,String last,String identity) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert date.contains("/");
		id = identity;
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		frequency = f;
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
	public Task(String issue, String startDate, String endDate, String msg,int f, String last) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert startDate.contains("/");
		assert endDate.contains("/");
		Calendar CalendarID = Calendar.getInstance();
		id = String.valueOf(CalendarID.getTimeInMillis());
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		frequency = f;
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
	public Task(String issue, String startDate, String endDate, String msg,int f, String last,String identity) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert startDate.contains("/");
		assert endDate.contains("/");
		this.msg=msg;
		id = identity;
		this.issue = issue;
		isCompleted = false;
		frequency = f;
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

	public void resetID() {
		id = "";
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
```
