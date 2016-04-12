# Cheng Gee
###### main\Logic\CheckDate.java
``` java
package Logic;

public class CheckDate {

	private static final int[] LEAP_YEAR_DATE = new int[]{31,29,31,30,31,30,31,31,30,31,30,31};
	private static final int[] COMMON_YEAR_DATE = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	
	public CheckDate() {
	}

	/**
	 * Function to check the format of the string if it follows the date convention
	 * 
	 * @param msg The string to be checked.
	 * @return    Whether the string is a correct DD/MM/YYYY format date.
	 */
	public boolean checkDateformat(String msg) {
		String[] msgArray = msg.split("/");
		if (msgArray.length != 3 && !msg.matches("^\\d{2}/\\d{2}/\\d{4}")) {
			return false;
		} else {
			int date = Integer.parseInt(msgArray[0]);
			int month = Integer.parseInt(msgArray[1]);
			int year = Integer.parseInt(msgArray[2]);
			if (month >= 1 && month <= 12) {
				if ((year % 4) == 0) {
					if (date <= LEAP_YEAR_DATE[month - 1]) {
						return true;
					} else {
						return false;
					}
				} else {
					if (date <= COMMON_YEAR_DATE[month - 1]) {
						return true;
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		}
	}

	/**
	 * Method to check a string is of a correct time format.
	 * 
	 * @param msg The string to be checked.
	 * @return    Whether the string is a valid time.
	 */
	public boolean checkTimeformat(String msg) {
		String[] msgArray = msg.split(":");
		if (msgArray.length != 2 && !msg.matches("^\\d{2}:\\d{2}")) {
			return false;
		} else if (msg.endsWith("am") || msg.endsWith("pm")) {
			msg = msg.substring(0, msg.length() - 2);
			int hour = Integer.parseInt(msgArray[0]);
			int minute = Integer.parseInt(msgArray[1]);
			if (minute >= 0 && minute < 60 && hour >= 0 && hour <= 23) {
				return true;
			}
			return false;
		} else {
			int hour = Integer.parseInt(msgArray[0]);
			int minute = Integer.parseInt(msgArray[1]);
			if (minute >= 0 && minute < 60 && hour >= 0 && hour <= 23) {
				return true;
			}
			return false;
		}
	}
}
```
###### main\Logic\Core.java
``` java
package Logic;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.fusesource.jansi.AnsiConsole;

import Parser.Natty;
import Parser.Parser;
import Storage.LocalStorage;
import Task.Task;
import UI.UI;

public class Core {

	private static Core core;

	private static Crud crudObject;
	private static Mark markObject;
	private static Notification notificationObject;
	private static Search searchObject;
	private static Sort sortObject;

	private static final int INVALID_TASK_INDEX = -1;
	private static final String[] WEEK = { "monday", "tuesday", "wednesday", "thursday", "friday", "saturday",
			                               "sunday" };
	private static final String[] KEY = { "by", "at", "during", "before", "to", "in" };
	private static final String MSG_EMPTY = "Storage is empty. Press \"add\" to add task.";
	private static final String MSG_CLEAR = "All content deleted";
	private static final String MSG_ADD = "is added to the task list.";
	private static final String MSG_EDIT_FAIL = "Fail to edit. Please insert a valid task number.";
	private static final String MSG_EDIT_NOT_RECURRING_TASK_HEAD = "The task at index ";
	private static final String MSG_EDIT_NOT_RECURRING_TASK_TAIL = " is not a recurring task. Please try again.";
	private static final String MSG_MARK_FAIL = "Fail to mark task as completed. Please insert a valid task number.";
	private static final String MSG_UNMARK_FAIL = "Fail to mark task as uncompleted. Please insert a valid task number.";
	private static final String MSG_NO_COMPLETED_TASKS = "No task has been completed yet.";
	private static final String MSG_DUPLICATE_ADD = "Duplicate task detected.";
	private static final String MSG_DELETE = "is deleted from the task list.";
	private static final String MSG_EDIT = " is edited and saved";
	private static final String MSG_MARK = " is marked as completed";
	private static final String MSG_UNMARK = " is marked as uncompleted";
	private static final String MSG_INVALID = "Invalid inputs! Please try again";
	private static final String MSG_WRONG_DATE = "Please enter date in dd/mm/yyyy format";
	private static final String MSG_TASK_DES_NOT_EXIST = "Task does not exists";
	private static final String MSG_PRIORITY_FAIL = "Fail to set priority. Please insert a valid task number.";
	private static final String MSG_DIRECTORY_USED = "The storage directory currently in use is ";
	private static final String MSG_DEFAULT_DIRECTORY = "the default source folder";
	private static final String MSG_ALL_COMMANDS_UNDONE = "All commands have been undone";
	private static final String MSG_ALL_COMMANDS_REDONE = "All commands have been redone";
	private static final String MSG_NO_PAST_COMMAND = "There are no remaining commands that can be undone";
	private static final String MSG_NO_REDO_COMMAND = "There are no remaining commands that can be redone";
	private static final String MSG_INVALID_UNDO_COUNT = "Please enter a valid number of commands you wish to undo";
	private static final String MSG_INVALID_REDO_COUNT = "Please enter a valid number of commands you wish to redo";
	private static final String PROMPT_EDIT = "Insert new description and deadline for the task.";
	private static final String PROMPT_RECURRING = "Enter <frequency> <date in dd/mm/yyyy>. e.g \"4 01/01/2016\"";

	private boolean arraylistsHaveBeenModified;
	private CheckDate checkDateObject;
	private LocalStorage localStorageObject;
	private String input, issue, endDate, startDate, startTime, endTime, endDateWithTime, startDateWithTime;
	private UI uiObject;
	private Help helpObject;
	private Parser parserObject;
	private Scanner sc;

	// Private constructor, following the singleton pattern.
	private Core() {
		arraylistsHaveBeenModified = false;

		input = "";
		issue = "";
		endDate = "";
		startDate = "";
		startTime = "";
		endTime = "";
		endDateWithTime = "";
		startDateWithTime = "";

		checkDateObject = new CheckDate();
		crudObject = Crud.getInstance();
		helpObject = new Help();
		localStorageObject = LocalStorage.getInstance();
		notificationObject = new Notification();
		markObject = new Mark();
		parserObject = Parser.getInstance();
		sc = new Scanner(System.in);
		searchObject = Search.getInstance();
		sortObject = new Sort();
		uiObject = new UI();
	}

	/**
	 * Method to access this class, following the singleton pattern. 
	 * Invokes constructor if Core has not been initialised.
	 * 
	 * @return The Core object.
	 */
	public static Core getInstance() {
		if (core == null) {
			core = new Core();
		}
		return core;
	}

	/**
	 * Function that simulate command line interface that will responds to user's inputs.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void acceptCommand() throws ClassNotFoundException, IOException {
		// take "snapshots" of current storage state
		Undo.getInstance().copyCurrentTasksState();

		String command = sc.nextLine();

		uiObject.eraseScreen();
		uiObject.printRed("command: ");
		uiObject.print(command);

		String[] splitCommand = command.split(" ");
		if (splitCommand[0].equals("add") || splitCommand[0].equals("a") || splitCommand[0].equals("+")) {
			// only use natty if add command is detected.
			command = Natty.getInstance().parseString(command);
		}

		parserObject.parse(command);

		boolean modificationsWereMade = parseCommands();
		if (modificationsWereMade) {
			// store the "snapshots" into Undo class if arraylists have been
			// modified.
			Undo.getInstance().storePreviousState(command);

			// if valid command executed and arraylists modified, remove all
			// stored redo commands.
			Undo.getInstance().clearRedoCommands();
		}
	}

```
###### main\Logic\Crud.java
``` java
	/**
	 * Function to display the nearest 5 floating tasks including the newly added floating task.
	 * 
	 * @param index the index of task added.
	 */
	public void displayNearestFiveFloating(int index) {
		ArrayList<Task> tempTasks = localStorageObject.getFloatingTasks();
		int unSize = localStorageObject.getUncompletedTasks().size();
		int size = tempTasks.size();
		int head = index - 2;
		int tail = index + 3;
		if (head < 0) {
			head = 0;
		}
		if (tail >= size) {
			tail = size;
		}
		uiObject.printGreen("FLOATING TASKS");
		uiObject.printGreen("Index\tTask");

		for (int i = head; i < tail; i++) {
			Task temp = tempTasks.get(i);
			if (i == index) {
				uiObject.printFloatingBackground(unSize + i, temp.getIssue());
			} else {
				uiObject.printFloating(unSize + i, temp.getIssue());
			}
		}
	}

	/**
	 * Function to display the Nearest 5 Completed Task with the newly mark completed tasks.
	 * 
	 * @param t the task completed.
	 */
	public void displayNearestFiveCompletedTaskList(Task t) {
		int index = -1;
		ArrayList<Task> tempTasks = localStorageObject.getCompletedTasks();
		int size = tempTasks.size();
		for (int i = 0; i < size; i++) {
			Task temp = tempTasks.get(i);
			if (t.getMsg().equals(temp.getMsg())) {
				index = i;
				break;
			}
		}
		int head = index - 2;
		int tail = index + 3;
		if (head < 0) {
			head = 0;
		}
		if (tail >= size) {
			tail = size;
		}
		uiObject.printGreen("COMPLETED TASKS");
		uiObject.printGreen("Index\tStart Date\tEnd Date\tTask");
		for (int i = head; i < tail; i++) {
			Task temp = tempTasks.get(i);
			if (index == i) {
				uiObject.printTaskAdded1(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(),
						                 temp.getEndDateLineOne(), temp.getEndDateLineTwo(), temp.getIssue(), 
						                 temp.getRecurFrequency());
			} else {
				uiObject.printTask1(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(), temp.getEndDateLineOne(),
						            temp.getEndDateLineTwo(), temp.getIssue(), temp.getRecurFrequency());
			}
		}
	}

	/**
	 * Function to display the nearest 5 task from uncompleted task list or floating task list including the unmarked task.
	 * 
	 * @param t the task unmarked.
	 */
	public void displayNearestFiveUnmarkCompleteTaskList(Task t) {
		ArrayList<Task> tempTasks;
		
		if (t.getEndDate() != null || t.getStartDate() != null) {
			uiObject.printGreen("UNCOMPLETED TASKS");
			uiObject.printGreen("Index\tStart Date\tEnd Date\tTask");
			tempTasks = localStorageObject.getUncompletedTasks();
		} else {
			uiObject.printGreen("FLOATING TASKS");
			uiObject.printGreen("Index\tTask");
			tempTasks = localStorageObject.getFloatingTasks();
		}
		int size = tempTasks.size();
		int index = -1;
		for (int i = 0; i < size; i++) {
			Task temp = tempTasks.get(i);
			if (t.getMsg().equals(temp.getMsg())) {
				index = i;
				break;
			}
		}
		int head = index - 2;
		int tail = index + 3;
		if (head < 0) {
			head = 0;
		}
		if (tail >= size) {
			tail = size;
		}
		for (int i = head; i < tail; i++) {
			Task temp = tempTasks.get(i);
			if (index == i) {
				uiObject.printTaskAdded1(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(),
						                 temp.getEndDateLineOne(), temp.getEndDateLineTwo(), temp.getIssue(), 
						                 temp.getRecurFrequency());
			} else {
				uiObject.printTask1(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(), temp.getEndDateLineOne(),
						            temp.getEndDateLineTwo(), temp.getIssue(), temp.getRecurFrequency());
			}
		}
	}

	/**
	 * Function to display the surrounding uncompleted task list of the deleted task.
	 * 
	 * @param index the index of the task deleted.
	 */
	public void displayNearestFiveDeleteUncompleteTaskList(int index) {
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();
		int size = tempTasks.size();
		
		if (size == 0) {
			uiObject.printGreen("Uncompleted Task List is empty");
		} else if (index <= tempTasks.size()) {
			int head = index - 2;
			int tail = index + 3;
			if (head < 0) {
				head = 0;
			}
			if (tail >= size) {
				tail = size;
			}
			uiObject.printGreen("UNCOMPLETED TASKS");
			uiObject.printGreen("Index\tStart Date\tEnd Date\tTask");
			for (int i = head; i < tail; i++) {
				Task temp = tempTasks.get(i);
				uiObject.printTask1(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(), temp.getEndDateLineOne(),
						            temp.getEndDateLineTwo(), temp.getIssue(), temp.getRecurFrequency());
			}
		}
	}

	/**
	 * Function to display the surrounding floating task list of delete task.
	 * 
	 * @param index the index of the task deleted.
	 */
	public void displayNearestFiveDeleteFloatingTask(int index) {
		ArrayList<Task> tempTasks = localStorageObject.getFloatingTasks();
		int size = localStorageObject.getUncompletedTasks().size();
		int size2 = tempTasks.size();
		index -= size;
		if (size2 == 0) {
			uiObject.printGreen("Floating Task List is Empty");
		} else if (index < size2) {
			int head = index - 2;
			int tail = index + 3;
			if (head < 0) {
				head = 0;
			}
			if (tail > size2) {
				tail = size2;
			}
			uiObject.printGreen("FLOATING TASKS");
			uiObject.printGreen("Index\tTask");
			for (int i = head; i < tail; i++) {
				Task temp = tempTasks.get(i);
				uiObject.printFloating(i, temp.getIssue());
			}
		}
	}

	/**
	 * Function to display the nearest 5 uncompleted task including the new added task.
	 * 
	 * @param index the index of the task added.
	 */
	public void displayNearestFiveUncompleted(int index) {
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();
		int size = tempTasks.size();
		int head = index - 2;
		int tail = index + 3;
		if (head < 0) {
			head = 0;
		}
		if (tail >= size) {
			tail = size;
		}
		uiObject.printGreen("UNCOMPLETED TASKS");
		uiObject.printGreen("Index\tStart Date\tEnd Date\tTask");

		for (int i = head; i < tail; i++) {
			Task temp = tempTasks.get(i);
			if (i == index) {
				uiObject.printTaskAdded1(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(),
						                 temp.getEndDateLineOne(), temp.getEndDateLineTwo(), temp.getIssue(), 
						                 temp.getRecurFrequency());
			} else {
				uiObject.printTask1(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(), temp.getEndDateLineOne(),
						            temp.getEndDateLineTwo(), temp.getIssue(), temp.getRecurFrequency());
			}
		}
	}

```
###### main\Logic\Crud.java
``` java
	/**
	 * Function to find the index of task with given start date.
	 * 
	 * @param line         the updated task description.
	 * @param index        the index of the task to be edited.
	 * @param msg          the updated task description with date.
	 * @throws IOException
	 */
	public int uncompletedTaskIndexWithStartDate(String line, String date, String msg) {
		Task task = new Task(line, date, msg, true);
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();
		for (int i = 0; i < tempTasks.size(); i++) {
			if (tempTasks.get(i).getTaskString().equals(task.getTaskString())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Function to find the index of task with given end date.
	 * 
	 * @param line         the updated task description.
	 * @param index        the index of the task to be edited.
	 * @param msg          the updated task description with date.
	 * @throws IOException
	 */
	public int uncompletedTaskIndexWithEndDate(String line, String date, String msg) {
		Task task = new Task(line, date, msg, false);
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();
		for (int i = 0; i < tempTasks.size(); i++) {
			if (tempTasks.get(i).getTaskString().equals(task.getTaskString())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Function to find the index of task with given start and end dates.
	 * 
	 * @param line         the updated task description.
	 * @param index        the index of the task to be edited.
	 * @param msg          the updated task description with date.
	 * @throws IOException
	 */
	public int uncompletedTaskIndexWithBothDates(String line, String startDate, String endDate, String msg) {
		Task task = new Task(line, startDate, endDate, msg);
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();
		for (int i = 0; i < tempTasks.size(); i++) {
			if (tempTasks.get(i).getTaskString().equals(task.getTaskString())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Function to find the index of task with no date.
	 * 
	 * @param line         the updated task description.
     *
	 * @throws IOException
	 */
	public int uncompletedTaskIndexWithNoDate(String line) {
		Task task = new Task(line);
		ArrayList<Task> tempTasks = localStorageObject.getFloatingTasks();
		for (int i = 0; i < tempTasks.size(); i++) {
			if (tempTasks.get(i).getTaskString().equals(task.getTaskString())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Function to copy the task description.
	 * 
	 * @param temp the task to be copied.
	 */
	public void copyTask(Task temp) {
		if (temp != null) {
			String copy = temp.getDescription();
			StringSelection selec = new StringSelection(copy);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selec, selec);
		}
	}

	/**
	 * Function to copy the uncompleted task description by index of arraylist.
	 * 
	 * @param index the index of the task to be copied.
	 */
	public void copyTask(int index) {
		Task edit = localStorageObject.getUncompletedTask(index - 1);
		if (edit != null) {
			String copy = edit.getDescription();
			StringSelection selec = new StringSelection(copy);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selec, selec);
		}
	}

	/**
	 * Function to copy the editing task description.
	 * 
	 * @param index the index of the task to be copied.
	 */
	public void copyEditingTask(int index) {
		ArrayList<Task> task1 = localStorageObject.getUncompletedTasks();

		int size = task1.size();
		if (index <= size) {
			Task edit = localStorageObject.getUncompletedTask(index - 1);
			if (edit != null) {
				String copy = edit.getDescription();
				StringSelection selec = new StringSelection(copy);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selec, selec);
			}
		} else {
			Task edit = localStorageObject.getFloatingTask(index - size - 1);
			if (edit != null) {
				String copy = edit.getIssue();
				StringSelection selec = new StringSelection(copy);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selec, selec);
			}
		}
	}

```
###### main\Logic\Head.java
``` java
package Logic;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.fusesource.jansi.AnsiConsole;

import Parser.Parser;
import UI.UI;

public class Head {

	private static String lastDisplay = "";
	private static String lastDisplayArg = "";
	private static UI uiObject = new UI();
	private static Parser parserObject = Parser.getInstance();
	private static Core coreObject = Core.getInstance();
	private static Sort sortObject = new Sort();
	private static Notification notificationObject = new Notification();

	private static final String USER_PROMPT = "command: ";
	private static final String WELCOME_HELP = "Enter \"help\" for instructions.\n";
	private static final String WELCOME_MSG_1 = "Welcome to Agendah. ";
	private static final String WELCOME_MSG_2 = "Agendah is ready for use";

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		ImportTasks importTaskObject = new ImportTasks();
		importTaskObject.prepareAndImportFiles();

		AnsiConsole.systemInstall();

		uiObject.printLogo();

		uiObject.printYellow(WELCOME_MSG_1 + WELCOME_MSG_2);

		uiObject.print("\n");
		uiObject.printYellow(WELCOME_HELP);

		notificationObject.welcomeReminder();

		runProgram();
	}

	/**
	 * Main program loop.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void runProgram() throws IOException, ClassNotFoundException {
		Save saveObject = new Save();

		while (true) {
			uiObject.printRed(USER_PROMPT);
			coreObject.acceptCommand();

			lastDisplay = parserObject.getCommand();
			lastDisplayArg = parserObject.getDescription();

			sortObject.sortTasksPriority();

			// save all arraylists (and their tasks) into respective files after each command is done
			saveObject.saveToFile();		
		}
	}

	/**
	 * Method to get first word of the latest executed command for keeping track of the last display view.
	 * 
	 * @return First word of the latest executed command.
	 */
	public static String getLastDisplay() {
		return lastDisplay;
	}

	/**
	 * Method to get arguments (if any) of the latest executed command for keeping track of the last display view.
	 * 
	 * @return Second word onwards (if any) of the latest executed command.
	 */
	public static String getLastDisplayArg() {
		return lastDisplayArg;
	}
}
```
###### main\UI\UI.java
``` java
	
	private static final String LOGO_LINE_1 = "********   ********   ********  **       **   *****       ********   **    **";
	private static final String LOGO_LINE_2 = "********   ********   ********  ***      **   **   **     ********   **    **";
	private static final String LOGO_LINE_3 = "**    **   **         **        ****     **   **    **    **    **   **    **";
	private static final String LOGO_LINE_4 = "**    **   **         ******    *** **   **   **     **   **    **   ********";
	private static final String LOGO_LINE_5 = "********   **   ***   ******    ***  **  **   **     **   ********   ********";
	private static final String LOGO_LINE_6 = "********   **    **   **        ***   ** **   **     **   ********   **    **";
	private static final String LOGO_LINE_7 = "**    **   ********   ********  ***    ****   **    **    **    **   **    **";
	private static final String LOGO_LINE_8 = "**    **   ********   ********  ***     ***   *******     **    **   **    **";
	
	public static final String SANE = "\u001B[0m";

	public static final String HIGH_INTENSITY = "\u001B[1m";
	public static final String LOW_INTENSITY = "\u001B[2m";

	public static final String ITALIC = "\u001B[3m";
	public static final String UNDERLINE = "\u001B[4m";
	public static final String BLINK = "\u001B[5m";
	public static final String RAPID_BLINK = "\u001B[6m";
	public static final String REVERSE_VIDEO = "\u001B[7m";
	public static final String INVISIBLE_TEXT = "\u001B[8m";

	public static final String BLACK = "\u001B[30m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String MAGENTA = "\u001B[35m";
	public static final String CYAN = "\u001B[36m";
	public static final String WHITE = "\u001B[37m";

	public static final String ANSI_CLS = "\u001b[2J";
	public static final String ANSI_HOME = "\u001b[H";
	public static final String ANSI_BOLD = "\u001b[1m";
	public static final String ANSI_AT55 = "\u001b[10;10H";
	public static final String ANSI_REVERSEON = "\u001b[7m";
	public static final String ANSI_NORMAL = "\u001b[0m";
	public static final String ANSI_WHITEONBLUE = "\u001b[37;44m";
	
	public static final String BACKGROUND_BLACK	= "\u001B[40m";
	public static final String BACKGROUND_RED = "\u001B[41m";
	public static final String BACKGROUND_GREEN	= "\u001B[42m";
	public static final String BACKGROUND_YELLOW = "\u001B[43m";
	public static final String BACKGROUND_BLUE = "\u001B[44m";
	public static final String BACKGROUND_MAGENTA = "\u001B[45m";
	public static final String BACKGROUND_CYAN = "\u001B[46m";
	public static final String BACKGROUND_WHITE	= "\u001B[47m";

	public UI() {
	}
	
	/**
	 * Method to print the welcome logo of Agendah.
	 */
	public void printLogo() {
		eraseScreen();
		printGreen(LOGO_LINE_1);
		printGreen(LOGO_LINE_2);
		printGreen(LOGO_LINE_3);
		printGreen(LOGO_LINE_4);
		printGreen(LOGO_LINE_5);
		printGreen(LOGO_LINE_6);
		printGreen(LOGO_LINE_7);
		printGreen(LOGO_LINE_8);
	}
	
	/**
	 * Function to print the incoming string.
	 * 
	 * @param temp The string to be printed.
	 */
	public void print(String temp) {
		System.out.print(temp);
		if(temp.equals("command: ") != true) {
			System.out.println();
		}
	}

	/**
	 * Method to clear the screen in CLI.
	 */
	public void eraseScreen() {
		AnsiConsole.systemUninstall();
		System.out.print(ansi().eraseScreen());
		AnsiConsole.systemInstall();
		System.out.println(ANSI_CLS);
		System.out.println(ANSI_CLS);
		System.out.println(ANSI_CLS);
	}
	
	/**
	 * Method to print a task with its details, with tabs added for alignment.
	 * 
	 * @param i     A counter to print a numbered list.
	 * @param sdate Start date and time (if any) of the task.
	 * @param edate End date and time (if any) of the task.
	 * @param msg   Issue description of the task.
	 */
	public void printTask(int i, String sdate, String edate, String msg) {
		i = i + 1;
		System.out.println(HIGH_INTENSITY + YELLOW + i + ".\t" + CYAN + sdate
				           + " " + edate + " " + YELLOW + msg + ansi().reset());
	}
	
	/**
	 * Method to a task with its details in two lines, with tabs added for alignment.
	 * 
	 * @param i     A counter to print a numbered list.
	 * @param sdate Start date (if any) of the task.
	 * @param stime Start day and time (if any) of the task.
	 * @param edate End date (if any) of the task.
	 * @param etime End day and time (if any) of the task.
	 * @param msg   Issue description of the task.
	 * @param rec   Recurrence interval of the task (if any).
	 */
	public void printTask1(int i, String sdate, String stime,
			                      String edate,String etime,
			                      String msg, String rec) {
		i = i + 1;
		if (sdate == null) {
			sdate = "\t";
		} else if (sdate.length() < 8) {
			sdate += "\t";
		}
		if (stime == null) {
			stime = "\t\t";
		} else if (stime.length() < 8) {
			stime += "\t";
		}
		if (edate == null) {
			edate = "\t\t";
		} else if (edate.length() < 8) {
			edate += "\t\t";
		} else if (edate.length() < 12) {
			edate += "\t";
		}
		if (etime == null) {
			etime = "\t\t";
		} else if (etime.length() < 8) {
			etime += "\t";
		}
		for (int j = rec.length(); j < msg.length(); j++) {
			rec += " ";
		}
		System.out.println(HIGH_INTENSITY + YELLOW + i + ".\t" + CYAN + sdate
				           + "\t" + edate + YELLOW + msg + ansi().reset());
		System.out.println(HIGH_INTENSITY + YELLOW + "\t" + CYAN + stime
				           + etime + rec + ansi().reset());
	}

	/**
	 * Method to a task with its details in two lines during the welcome screen, with tabs added for alignment.
	 * Includes information on whether the task is expired or due today.
	 * 
	 * @param i     A counter to print a numbered list.
	 * @param sdate Start date (if any) of the task.
	 * @param stime Start day and time (if any) of the task.
	 * @param edate End date (if any) of the task.
	 * @param etime End day and time (if any) of the task.
	 * @param msg   Issue description of the task.
	 * @param rec   Information regarding expiry/deadline (if any) of task.
	 */
	public void printTask2(int i, String sdate, String stime,
			                      String edate, String etime,
			                      String msg, String rec) {
		i = i + 1;
		if (sdate == null) {
			sdate = "\t";
		} else if (sdate.length() < 8) {
			sdate += "\t";
		}
		if (stime == null) {
			stime = "\t\t";
		} else if (stime.length() < 8) {
			stime += "\t";
		}
		if (edate == null) {
			edate = "\t\t";
		} else if (edate.length() < 8) {
			edate += "\t\t";
		} else if (edate.length() < 12) {
			edate += "\t";
		}
		if (etime == null) {
			etime = "\t\t";
		} else if (etime.length() < 8) {
			etime += "\t";
		}

		for (int j = rec.length(); j < msg.length(); j++) {
			rec += " ";
		}

		System.out.println(HIGH_INTENSITY + YELLOW + i + ".\t" + CYAN + sdate
				           + "\t" + edate + YELLOW + msg + ansi().reset());
		System.out.println(HIGH_INTENSITY + YELLOW + "\t" + CYAN + stime
				           + etime + RED + rec + ansi().reset());
	}

	/**
	 * Method to print feedback of the task details after it has been added.
	 * 
	 * @param i     A counter to print a numbered list.
	 * @param sdate Start date and time (if any) of the task.
	 * @param edate End date and time (if any) of the task.
	 * @param msg   Issue description of the task.
	 */
	public void printTaskAdded(int i, String sdate, String edate, String msg) {
		i = i + 1;
		System.out.println(BACKGROUND_BLUE + HIGH_INTENSITY + YELLOW + i + ".\t"
		                   + CYAN + sdate + edate + YELLOW + msg + ansi().reset());
	}

	/**
	 * Method to print feedback of the task details in 2 lines after it has been added.
	 * 
	 * @param i     A counter to print a numbered list.
	 * @param sdate Start date (if any) of the task.
	 * @param stime Start day and time (if any) of the task.
	 * @param edate End date (if any) of the task.
	 * @param etime End day and time (if any) of the task.
	 * @param msg   Issue description of the task.
	 * @param rec   Information regarding expiry/deadline (if any) of task.
	 */
	public void printTaskAdded1(int i, String sdate, String stime,
			                           String edate, String etime,
			                           String msg, String rec) {
		i = i + 1;
		if (sdate == null) {
			sdate = "\t";
		} else if (sdate.length() < 8) {
			sdate += "\t";
		}
		if (stime == null) {
			stime = "\t\t";
		} else if (stime.length() < 8) {
			stime += "\t";
		}
		if (edate == null) {
			edate = "\t\t";
		} else if (edate.length() < 8) {
			edate += "\t\t";
		} else if (edate.length() < 12) {
			edate += "\t";
		}
		if (etime == null) {
			etime = "\t\t";
		} else if (etime.length() < 8) {
			etime += "\t";
		}
		for (int j = rec.length(); j < msg.length(); j++) {
			rec += " ";

		}
		System.out.println(BACKGROUND_BLUE + HIGH_INTENSITY + HIGH_INTENSITY + YELLOW + i+ ".\t"
		                  + CYAN + sdate + "\t" + edate + YELLOW + msg + ansi().reset());
		System.out.println(BACKGROUND_BLUE + HIGH_INTENSITY + HIGH_INTENSITY + YELLOW + "\t"
		                  + CYAN + stime + etime + rec + ansi().reset());
	}

	/**
	 * Method to print a floating task.
	 * 
	 * @param i   A counter to print a numbered list.
	 * @param msg Issue description of the task.
	 */
	public void printFloating(int i, String msg) {
		i = i + 1;
		System.out.println(HIGH_INTENSITY + YELLOW + i + ".\t" + msg + ansi().reset());

	}

	/**
	 * Method to print a floating task with blue background.
	 * 
	 * @param i   A counter to print a numbered list.
	 * @param msg Issue description of the task.
	 */
	public void printFloatingBackground(int i, String msg) {
		i = i + 1;
		System.out.println(BACKGROUND_BLUE + HIGH_INTENSITY + YELLOW + i + ".\t" + msg + ansi().reset());

	}

	// The following methods print the given string in the stated colour.
	public void printRed(String temp) {

		System.out.print(HIGH_INTENSITY + RED + temp + ansi().reset());
		if (temp.equals("command: ") != true) {
			System.out.println();
		}
	}

	public void printBlue(String temp) {
		System.out.println(HIGH_INTENSITY + BLUE + temp + ansi().reset());
	}

	public void printGreen(String temp) {
		System.out.println(HIGH_INTENSITY + GREEN + temp + ansi().reset());
	}

	public void printYellow(String temp) {
		System.out.println(HIGH_INTENSITY + YELLOW + temp + ansi().reset());
	}

	public void printCyan(String temp) {
		System.out.println(HIGH_INTENSITY + CYAN + temp + ansi().reset());
	}
}
```
###### main\unitTest\CrudTest.java
``` java
package unitTest;

import static org.junit.Assert.*;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.fusesource.jansi.Ansi.ansi;

import org.fusesource.jansi.AnsiConsole;
import org.junit.Test;


import Logic.CheckDate;
import Logic.Crud;
import Storage.LocalStorage;
import Task.Task;

public class CrudTest {
	@Test
	public void testuncompletedTaskIndexWithNoDate() throws ClassNotFoundException, IOException{
		Logic.Crud test = Crud.getInstance();
		int output =test.uncompletedTaskIndexWithNoDate("test");
		assertEquals(output,-1); 
	}
	@Test
	public void testcopyTask() throws HeadlessException, UnsupportedFlavorException, IOException{
		Task output = new Task("test");
		Logic.Crud test = Crud.getInstance();
		test.copyTask(output);
		String data = (String) Toolkit.getDefaultToolkit()
                .getSystemClipboard().getData(DataFlavor.stringFlavor);
		assertEquals("test",data);
	}
	@Test
	public void testaddTask() throws ClassNotFoundException, IOException{
		Logic.Crud test = Crud.getInstance();
		boolean output = test.addTask("test");
		assertEquals(output,true);
		
	}
	
	@Test
	public void testaddTaskWithStartDate() throws ClassNotFoundException, IOException{
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		Logic.Crud test = Crud.getInstance();
		boolean output = test.addTaskWithStartDate("test","11/4/2016","test ` 11/4/2016");
		assertEquals(output,true);
	}
	
	@Test
	public void testaddTaskWithEndDate() throws ClassNotFoundException, IOException{
		Logic.Crud test = Crud.getInstance();
		boolean output = test.addTaskWithEndDate("test","11/4/2016","test ` 11/4/2016");
		assertEquals(output,true);
	}
	
	@Test
	public void testaddTaskWithBothDate() throws ClassNotFoundException, IOException{
		Logic.Crud test = Crud.getInstance();
		boolean output = test.addTaskWithBothDates("test","11/4/2016","20/4/2016", "test ` from 11/4/2016 to 20/4/2016");
		assertEquals(output,true);
	}
	
	@Test
	public void testaddLabeltoTask() throws ClassNotFoundException, IOException{
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		
		Logic.Crud test = Crud.getInstance();
		test.addTask("test");
		test.addLabelToTask(0, "first");
		
		Task temp= temp1.getFloatingTask(0);
		String output = temp.getLabel().get(0);
		assertEquals(output,"first");
	}
	@Test
	public void testeditTaskWithNoDate() throws ClassNotFoundException, IOException{
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		
		Logic.Crud test = Crud.getInstance();
		test.addTask("test");
		test.editTaskWithNoDate("Hahaha", "Hahaha", 0);
		Task temp= temp1.getFloatingTask(0);
		String output = temp.getIssue();
		assertEquals(output,"Hahaha");
	}
	
	@Test
	public void testeditTaskWithStartDate() throws ClassNotFoundException, IOException{
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		
		Logic.Crud test = Crud.getInstance();
		test.addTaskWithStartDate("test","11/4/2016","test ` 11/4/2016");
		Task temp= temp1.getUncompletedTask(0);
		String output = temp.getDescription();
		assertEquals(output,"test ` 11/4/2016");
	}
	@Test
	public void testeditTaskWithEndDate() throws ClassNotFoundException, IOException{
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		
		Logic.Crud test = Crud.getInstance();
		test.addTaskWithEndDate("test","11/4/2016","test ` 11/4/2016");
		Task temp= temp1.getUncompletedTask(0);
		String output = temp.getDescription();
		assertEquals(output,"test ` 11/4/2016");
	}
	public void testeditTaskBothEndDates() throws ClassNotFoundException, IOException{
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		
		Logic.Crud test = Crud.getInstance();
		test.addTaskWithBothDates("test","11/4/2016","20/4/2016", "test ` from 11/4/2016 to 20/4/2016");
		Task temp= temp1.getUncompletedTask(0);
		String output = temp.getDescription();
		assertEquals(output,"test ` from 11/4/2016 to 20/4/2016");
	}
	@Test
	public void testdisplayCompletedList(){
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		Logic.Crud test = Crud.getInstance();
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
				test.displayCompletedTasks();
				String output = "\u001B[1m\u001B[32mThere is no stored task to display"+ansi().reset();
				assertEquals(output.trim(),outContent.toString().trim());
				System.out.println("test"+outContent.toString());
	}
}
```
