//@@author Cheng Gee
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

	// @@author Kowshik
	/**
	 * Function that take in command and the body as the argument and process them to to meet the requests of the user.
	 * 
	 * @return true                   if storage has been modified, false otherwise.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean parseCommands() throws IOException, ClassNotFoundException {
		arraylistsHaveBeenModified = false;
		String option = parserObject.getCommand();

		if (option.equals("add") || option.equals("a") || option.equals("+")) {
			addCommand();
		} else if (option.equals("display") || option.equals("d")) {
			displayCommand();
		} else if (option.equals("edit") || option.equals("e")) {
			editCommand();
		} else if (option.equals("delete") || option.equals("-")) {
			deleteCommand();
		} else if (option.equals("view") || option.equals("v")) {
			viewCommand();
		} else if (option.equals("search") || option.equals("s")) {
			searchObject.searchTasksByKeyword(parserObject.getDescription());
		} else if (option.equals("mark") || option.equals("m")) {
			markCommand();
		} else if (option.equals("unmark") || option.equals("um")) {
			unmarkCommand();
		} else if (option.equals("priority") || option.equals("p")) {
			String s = parserObject.getIssueM();
			if (s.contains("all")) {
				setAllRecurringTasksPriorityCommand();
			} else {
				setPriorityCommand();
			}
		} else if (option.equals("history")) {
			String pastCommands = Undo.getInstance().viewPastCommands();
			uiObject.printYellow(pastCommands);
		} else if (option.equals("future")) {
			String possibleRedoCommands = Undo.getInstance().viewRedoCommands();
			uiObject.printYellow(possibleRedoCommands);
		} else if (option.equals("undo")) {
			undoCommand();
		} else if (option.equals("redo")) {
			redoCommand();
		} else if (option.equals("help")) {
			helpObject.printHelpMenu();
		} else if (option.equals("dir")) {
			changeDirectoryCommand();
		} else if (option.equals("label")) {
			setLabelCommand();
		} else if (option.equals("clear") || option.equals("c")) {
			clearCommand();
		} else if (option.equals("exit")) {
			uiObject.printGreen("Bye!");
			AnsiConsole.systemUninstall();
			crudObject.exit();
		} else {
			uiObject.printRed(MSG_INVALID);
		}
		return arraylistsHaveBeenModified;
	}

	//@@author Jung Kai
	/**
	 * Function to decipher if adding a normal or recurring task.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void addCommand() throws IOException, ClassNotFoundException {
		String description = parserObject.getDescription();
		String startDate = parserObject.getStartDate();
		String startDateWithTime = parserObject.getStartDateWithTime();
		String endDate = parserObject.getEndDate();
		String endDateWithTime = parserObject.getEndDateWithTime();
		String issue = parserObject.getIssueM();
		boolean recurrence = parserObject.getRecurrence();
		boolean containDate = parserObject.getContainDate();
		boolean isAdded;

		if (description.equals("")) {
			uiObject.printRed(MSG_INVALID);
		} else {
			// get index of key.
			if (containDate) {
				if (!recurrence) {
					addNormalTask(description, startDate, startDateWithTime, endDate, endDateWithTime, issue);
				} else { // for recurring tasks.
					addRecurringTask(description, startDate, startDateWithTime, endDate, endDateWithTime, issue);
				}
			} else {
				isAdded = crudObject.addTask(description);
				if (isAdded) {
					sortObject.sortTasksChronologically();
					int index = crudObject.uncompletedTaskIndexWithNoDate(description);
					uiObject.printGreen("\"" + description + "\" " + MSG_ADD);
					crudObject.displayNearestFiveFloating(index);
					arraylistsHaveBeenModified = true;
				} else {
					uiObject.printRed(MSG_DUPLICATE_ADD);
				}
			}
		}
	}

	/**
	 * Function to add a recurring task. 
	 * 
	 * @param description       the task description.
	 * @param startDate         the start date.
	 * @param endDate           the end date.
	 * @param startDateWithTime the start date along with the time.
	 * @param endDateWithTime   the end date along with the time.
	 * @param issue             the task description.
	 */
	public void addRecurringTask(String description, String startDate, String startDateWithTime, String endDate,
			String endDateWithTime, String issue) {
		if (startDate.equals("-") && !endDate.equals("-")) {// no start date but has end date.
			if (!checkDateObject.checkDateformat(endDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				uiObject.printRed(PROMPT_RECURRING);
				try {
					String in = sc.nextLine();
					String[] tmp = in.split(" ");
					String freq = tmp[0];
					String last = tmp[1];
					int frequency = Integer.parseInt(freq);

					Task task = new Task(issue, endDateWithTime, description, true, frequency, last);
					checkDateAndAdd(task);
					uiObject.printGreen("\"" + task.getIssue() + "\"" + " is added to the task list. (recurs every " 
					                    + freq + " days)");
					arraylistsHaveBeenModified = true;
				} catch (Exception e) {
					uiObject.printRed(MSG_INVALID);
					arraylistsHaveBeenModified = false;
				}
			}
		} else if ((!startDate.equals("-")) && endDate.equals("-")) {// has start date.
			if (!checkDateObject.checkDateformat(startDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				uiObject.printRed(PROMPT_RECURRING);
				try {
					String in = sc.nextLine();
					String[] tmp = in.split(" ");
					String freq = tmp[0];
					String last = tmp[1];
					int frequency = Integer.parseInt(freq);

					Task task = new Task(issue, startDateWithTime, description, true, frequency, last);
					checkDateAndAdd(task);
					uiObject.printGreen("\"" + task.getIssue() + "\"" + " is added to the task list. (recurs every " 
							            + freq + " days)");
					arraylistsHaveBeenModified = true;
				} catch (Exception e) {
					uiObject.printRed(MSG_INVALID);
					arraylistsHaveBeenModified = false;
				}
			}
		} else { // has both start date and end date.
			if (!checkDateObject.checkDateformat(startDate) && !checkDateObject.checkDateformat(endDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				uiObject.printRed(PROMPT_RECURRING);
				try {
					String in = sc.nextLine();
					String[] tmp = in.split(" ");
					String freq = tmp[0];
					String last = tmp[1];
					int frequency = Integer.parseInt(freq);

					Task task = new Task(issue, startDateWithTime, endDateWithTime, description, frequency,
							             last);
					uiObject.printGreen("\"" + task.getIssue() + "\"" + " is added to the task list. (recurs every " 
							            + freq + " days)");
					checkDateAndAdd(task);
					arraylistsHaveBeenModified = true;
				} catch (Exception e) {
					uiObject.printRed(MSG_INVALID);
					arraylistsHaveBeenModified = false;
				}
			}
		}
	}

	/**
	 * Function to add a normal task. 
	 * 
	 * @param description       the task description.
	 * @param startDate         the start date.
	 * @param endDate           the end date.
	 * @param startDateWithTime the start date along with the time.
	 * @param endDateWithTime   the end date along with the time.
	 * @param issue             the task description.
	 */
	public void addNormalTask(String description, String startDate, String startDateWithTime, String endDate,
			String endDateWithTime, String issue) throws IOException, ClassNotFoundException {
		boolean isAdded;
		if (startDate.equals("-") && !endDate.equals("-")) { // no start date but has end date.
			if (!checkDateObject.checkDateformat(endDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				isAdded = crudObject.addTaskWithEndDate(issue, endDateWithTime, description);
				if (isAdded) {
					sortObject.sortTasksChronologically();
					int index = crudObject.uncompletedTaskIndexWithEndDate(issue, endDateWithTime, description);
					uiObject.printGreen("\"" + issue + "\" " + MSG_ADD);
					arraylistsHaveBeenModified = true;
					crudObject.displayNearestFiveUncompleted(index);
				} else {
					uiObject.printRed(MSG_DUPLICATE_ADD);
				}
			}
		} else if ((!startDate.equals("-")) && endDate.equals("-")) { // has start date.
			if (!checkDateObject.checkDateformat(startDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				isAdded = crudObject.addTaskWithStartDate(issue, startDateWithTime, description);
				if (isAdded) {
					sortObject.sortTasksChronologically();
					int index = crudObject.uncompletedTaskIndexWithStartDate(issue, startDateWithTime, description);
					uiObject.printGreen("\"" + issue + "\" " + MSG_ADD);
					crudObject.displayNearestFiveUncompleted(index);
					arraylistsHaveBeenModified = true;
				} else {
					uiObject.printRed(MSG_DUPLICATE_ADD);
				}
			}
		} else { // has both start date and end date.
			if (!checkDateObject.checkDateformat(startDate) && !checkDateObject.checkDateformat(endDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				isAdded = crudObject.addTaskWithBothDates(issue, startDateWithTime, endDateWithTime, description);
				if (isAdded) {
					sortObject.sortTasksChronologically();
					int index = crudObject.uncompletedTaskIndexWithBothDates(issue, startDateWithTime,
							                                                 endDateWithTime, description);
					uiObject.printGreen("\"" + issue + "\" " + MSG_ADD);
					crudObject.displayNearestFiveUncompleted(index);
					arraylistsHaveBeenModified = true;
				} else {
					uiObject.printRed(MSG_DUPLICATE_ADD);
				}
			}
		}
	}
	
	/**
	 * Function to set priority for all instances of a recurring task.
	 */
	private void setAllRecurringTasksPriorityCommand() {
		String description = parserObject.getDescription();
		String[] tmp = description.split(" ");
		String idx = tmp[1];
		int num = Integer.parseInt(idx);

		ArrayList<Task> list = localStorageObject.getUncompletedTasks();

		if (list.size() == 0) {
			uiObject.printRed(MSG_EMPTY);
		} else if (list.size() < num || num - 1 < 0) {
			uiObject.printRed(MSG_PRIORITY_FAIL);
		} else {
			uiObject.printYellow("Enter priority");
			String priority = sc.nextLine();
			markObject.setRecurringTasksPriority(num - 1, priority);
			arraylistsHaveBeenModified = true;
		}
	}

	// @@author Jie Wei
	/**
	 * Function to change the location of where tasks are stored on the computer.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void changeDirectoryCommand() throws IOException, ClassNotFoundException {
		String description = parserObject.getDescription();
		ImportTasks importTasksObject = new ImportTasks();
		if (description.isEmpty()) { // only "dir" was typed, this will display the current storage folder directory in use.
			String currentStorageDirectory = importTasksObject.getFolderDirectory();
			if (currentStorageDirectory.isEmpty()) { // indicates source.
				// folder is in use
				uiObject.printGreen(MSG_DIRECTORY_USED + MSG_DEFAULT_DIRECTORY);
			} else {
				uiObject.printGreen(MSG_DIRECTORY_USED + currentStorageDirectory);
			}
		} else { // "dir <path>" was entered
			String feedback = importTasksObject.changeStorageDestination(description);
			uiObject.print(feedback);
		}
	}

	/**
	 * Function to redo a command entered by the user. 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void redoCommand() throws ClassNotFoundException, IOException {
		String s = parserObject.getDescription();
		if (s.isEmpty()) { // only "redo" was typed.
			String outcome = Undo.getInstance().redo();
			uiObject.printGreen(outcome);
		} else if (s.equals("all")) {
			int redoCount = Undo.getInstance().getRedoCount();
			if (redoCount == 0) { // if no commands to redo.
				uiObject.printRed(MSG_NO_REDO_COMMAND);
			} else {
				for (int i = 0; i < redoCount; i++) { // do redo for all stored commands.
					String outcome = Undo.getInstance().redo();
					uiObject.printGreen(outcome);
				}
				uiObject.printGreen(MSG_ALL_COMMANDS_REDONE);
			}
		} else { // e.g. "redo 2" will redo the latest 2 commands.
			try {
				int count = Integer.parseInt(s);
				if (count < 1 || count > Undo.getInstance().getRedoCount()) { // if entered count is outside valid bounds.
					uiObject.printRed(MSG_INVALID_REDO_COUNT);
				} else {
					for (int i = 0; i < count; i++) { // redo the number of commands specified.
						if (Undo.getInstance().getRedoCount() == 0) { // all
							// commands have been redone but user used a higher int.
							uiObject.printRed(MSG_NO_REDO_COMMAND);
							break;
						}
						String outcome = Undo.getInstance().redo();
						uiObject.printGreen(outcome);
					}
				}
			} catch (NumberFormatException e) { // if non-number was entered, e.g. "redo hello".
				uiObject.printRed(MSG_INVALID_REDO_COUNT);
			}
		}
	}

	/**
	 * Function to undo a command entered by the user.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void undoCommand() throws ClassNotFoundException, IOException {
		String s = parserObject.getDescription();
		if (s.isEmpty()) { // only "undo" was typed.
			String outcome = Undo.getInstance().undo();
			uiObject.printGreen(outcome);
		} else if (s.equals("all")) {
			int historyCount = Undo.getInstance().getHistoryCount();
			if (historyCount == 0) { // if no commands to undo.
				uiObject.printRed(MSG_NO_PAST_COMMAND);
			} else {
				for (int i = 0; i < historyCount; i++) { // do undo for all stored commands.
					String outcome = Undo.getInstance().undo();
					uiObject.printGreen(outcome);
				}
				uiObject.printGreen(MSG_ALL_COMMANDS_UNDONE);
			}
		} else { // e.g. "undo 2" will undo the latest 2 commands.
			try {
				int count = Integer.parseInt(s);
				if (count < 1 || count > Undo.getInstance().getHistoryCount()) { // if entered count is outside valid bounds.
					uiObject.printRed(MSG_INVALID_UNDO_COUNT);
				} else {
					for (int i = 0; i < count; i++) { // undo the number of commands specified.
						if (Undo.getInstance().getHistoryCount() == 0) { 
							// all commands have been undone but user used a higher int
							uiObject.printRed(MSG_NO_PAST_COMMAND);
							break;
						}
						String outcome = Undo.getInstance().undo();
						uiObject.printGreen(outcome);
					}
				}
			} catch (NumberFormatException e) { // if non-number was entered, e.g. "undo hello".
				uiObject.printRed(MSG_INVALID_UNDO_COUNT);
			}
		}
	}

	// @@author Kowshik
	/**
	 * Function to add a label to a task.
	 */
	public void setLabelCommand() {
		String description = parserObject.getDescription();
		try {
			int num = Integer.parseInt(description);
			ArrayList<Task> list = localStorageObject.getUncompletedTasks();
			ArrayList<Task> list2 = localStorageObject.getFloatingTasks();

			if (list.size() + list2.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				uiObject.printRed(MSG_PRIORITY_FAIL);
			} else {
				uiObject.print("Enter label");
				String label = sc.nextLine();
				crudObject.addLabelToTask(num - 1, label);
				arraylistsHaveBeenModified = true;
				Task temp = localStorageObject.getUncompletedTask(num - 1);
				String issue = temp.getIssue();
				uiObject.printGreen("Task " + issue + " has been labelled " + label);
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}
	
	/**
	 * Function to edit a task.
	 */
	public void editCommand() {
		int num;
		String description = parserObject.getDescription();
		num = findTheLastDisplay(description);

		// @@author Jung Kai
		try {
			if (num < 0) {
				uiObject.printRed(MSG_INVALID);
			} else if (description.contains("all")) {
				editRecurringTask(num - 1);
			} else {
				// check if user input integer is valid. If it is valid, edit
				// should work
				ArrayList<Task> list = localStorageObject.getUncompletedTasks();
				ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					uiObject.printRed(MSG_EMPTY);
				} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
					uiObject.printRed(MSG_EDIT_FAIL);
				} else {
					uiObject.printGreen(PROMPT_EDIT);
					crudObject.copyEditingTask(num);
					input = sc.nextLine();
					input = Natty.getInstance().parseEditString(input);
					input = "edit " + input;
					parserObject.parse(input);
					description = parserObject.getDescription();
					String startDate = parserObject.getStartDate();
					String startDateWithTime = parserObject.getStartDateWithTime();
					String endDate = parserObject.getEndDate();
					String endDateWithTime = parserObject.getEndDateWithTime();
					String issue = parserObject.getIssueM();
					boolean rec = parserObject.getRecurrence();
					boolean containDate = parserObject.getContainDate();
					if (description.equals("")) {
						uiObject.printRed(MSG_INVALID);
					} else {
						if (containDate) {
							if (!rec) { // has a date.
								editATaskWithDate(num, description, startDate, startDateWithTime, endDate,
										          endDateWithTime, issue);
							}
						} else {// no end date and no start date.
							editTaskWithNoDate(num, description);
						}
					}
				}
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to edit a task with no date.
	 * 
	 * @param num                     the index of the task to be edited.
	 * @param description             the updated description.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void editTaskWithNoDate(int num, String description) throws IOException, ClassNotFoundException {
		crudObject.editTaskWithNoDate(input, input, num - 1);
		int index = crudObject.uncompletedTaskIndexWithNoDate(description);
		uiObject.printGreen("Task number " + num + MSG_EDIT);
		crudObject.displayNearestFiveFloating(index);
		arraylistsHaveBeenModified = true;
	}

	/**
	 * Function to edit a task with a date.
	 * 
	 * @param num                     the index of the task to be edited.
	 * @param description             the updated description.
	 * @param startDate               the updated start date.
	 * @param startDateWithTime       the updated start date with time.
	 * @param endDate                 the update end date.
	 * @param endDateWithTime         the update end date with time.
	 * @param issue                   the updated task description.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void editATaskWithDate(int num, String description, String startDate, String startDateWithTime,
			                      String endDate, String endDateWithTime, String issue) 
			                      throws IOException, ClassNotFoundException {
		if (startDate.equals("-") && !endDate.equals("-")) {
			if (!checkDateObject.checkDateformat(endDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				crudObject.editTaskWithEndDate(issue, endDateWithTime, description, num - 1);
				sortObject.sortTasksChronologically();

				int index = crudObject.uncompletedTaskIndexWithEndDate(issue, endDateWithTime,
						description);
				uiObject.printGreen("Task number " + num + MSG_EDIT);
				crudObject.displayNearestFiveUncompleted(index);
				arraylistsHaveBeenModified = true;
			}
		} else if ((!startDate.equals("-")) && endDate.equals("-")) {// has start date.
			if (!checkDateObject.checkDateformat(startDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				crudObject.editTaskWithStartDate(issue, startDateWithTime, description,
						num - 1);
				sortObject.sortTasksChronologically();
				int index = crudObject.uncompletedTaskIndexWithStartDate(issue,
						startDateWithTime, description);

				uiObject.printGreen("Task number " + num + MSG_EDIT);
				crudObject.displayNearestFiveUncompleted(index);
				arraylistsHaveBeenModified = true;
			}
		} else { // has both start date and end date.
			if (!checkDateObject.checkDateformat(startDate)
					&& !checkDateObject.checkDateformat(endDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				crudObject.editTaskWithBothDates(issue, startDateWithTime, endDateWithTime,
						description, num - 1);
				uiObject.printGreen("Task number " + num + MSG_EDIT);
				sortObject.sortTasksChronologically();
				int index = crudObject.uncompletedTaskIndexWithBothDates(issue,
						startDateWithTime, endDateWithTime, input);
				crudObject.displayNearestFiveUncompleted(index);
				arraylistsHaveBeenModified = true;
			}
		}
	}

	//@@author Kowshik
	/**
	 * Function to find the last display.
	 * 
	 * @param description the command entered by the user.
	 * 
	 * @return            the index of the task to be edited.
	 */
	public int findTheLastDisplay(String description) {
		int num;
		if (description.contains("all")) {
			String[] tmp = description.split(" ");
			num = Integer.parseInt(tmp[1]);
		} else {
			num = Integer.parseInt(description);
		}
		if (Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if (Logic.Head.getLastDisplayArg().equals("floating") || Logic.Head.getLastDisplayArg().equals("all")) {
				if (description.contains("all")) {
					String[] tmp = description.split(" ");
					num = Integer.parseInt(tmp[1]);
				} else {
					num = Integer.parseInt(description);
				}
			} else {
				num = getCorrectIndexFromDisplayAll(num);
			}
		} else if (Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s")) {
			num = getCorrectIndexFromSearchView(num);
		} else if (Logic.Head.getLastDisplay().equals("")) {
			num = getCorrectIndexWelcomeView(num - 1);
		}
		return num;
	}

	/**
	 * Function to get the index of the last from search "view".
	 * 
	 * @param num the current index of the task.
	 * 
	 * @return    the actual index of the task from the storage arraylist.
	 */
	public int getCorrectIndexFromSearchView(int num) {
		Task temp = searchObject.getSearchedTask(num - 1);
		ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
		ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();

		num = getCorrectIndexFromStorage(num, temp, tempUncompletedTasks, tempFloatingTasks);
		return num;
	}

	/**
	 * Function to get the actual index of the task from storage arraylist.
	 * 
	 * @param num                   the current index of the task.
	 * @param temp                  the task.
	 * @param tempUncompletedTasks  the arraylist containing the uncompleted tasks.
	 * @param tempFloatingTasks     the arraylist containing the floating tasks.
	 * 
	 * @return                      the actual index of the task.
	 */
	public int getCorrectIndexFromStorage(int num, Task temp, ArrayList<Task> tempUncompletedTasks,
			                              ArrayList<Task> tempFloatingTasks) {
		int counter = 1;
		for (Task t : tempUncompletedTasks) {
			if (t.getTaskString().equals(temp.getTaskString())) {
				num = counter;
				break;
			}
			counter++;
		}
		counter++;
		
		for (Task t : tempFloatingTasks) {
			if (t.getTaskString().equals(temp.getTaskString())) {
				num = counter;
				break;
			}
			counter++;
		}
		return num;
	}

	/**
	 * Function to get the index of the last from display all "view".
	 * 
	 * @param num the current index of the task.
	 * 
	 * @return    the actual index of the task from the storage arraylist.
	 */
	public int getCorrectIndexFromDisplayAll(int num) {
		try {
			Task temp = crudObject.getTempTask(num - 1);

			ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
			ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();

			num = getCorrectIndexFromStorage(num, temp, tempUncompletedTasks, tempFloatingTasks);
			return num;
			
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Function to get the index of the last from welcome "view".
	 * 
	 * @param num the current index of the task.
	 * 
	 * @return    the actual index of the task from the storage arraylist.
	 */
	public int getCorrectIndexWelcomeView(int num) {
		Task temp;
		try {
			temp = notificationObject.getSpecificTask(num);
		} catch (IndexOutOfBoundsException e) {
			return INVALID_TASK_INDEX;
		}

		ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
		ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();

		num = getCorrectIndexFromStorage(num, temp, tempUncompletedTasks, tempFloatingTasks);
		return num;
	}

	/**
	 * Function to determine which "view" to set priority from.
	 */
	public void setPriorityCommand() {
		if (Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if (Logic.Head.getLastDisplayArg().equals("all") || Logic.Head.getLastDisplayArg().equals("floating")) {
				setPriorityFromDisplayAllView();
			} else {
				setPriorityFromDisplayView();
			}
		} else if (Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s")) {
			setPriorityFromSearchTaskView();
		} else if (Logic.Head.getLastDisplay().equals("")) {
			setPriorityFromDisplayAllView();
		} else {
			setPriorityFromDisplayAllView();
		}
	}

	/**
	 * Function to set priority to task from display "view".
	 */
	public void setPriorityFromDisplayView() {
		ArrayList<Task> list = crudObject.getTemp();
		try {
			String s = parserObject.getDescription();
			int num = Integer.parseInt(s);
			int oldNum = num;
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_PRIORITY_FAIL);
			} else {
				String priority = acceptPriorityFromUser();
				uiObject.printGreen(crudObject.getTempTask(oldNum - 1).getIssue() + " has been set to " + priority 
						            + " priority");
				num = getCorrectIndexFromDisplayAll(num);
				markObject.setPriority(num - 1, priority);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to set priority from search "view".
	 */
	public void setPriorityFromSearchTaskView() {
		try {
			String s = parserObject.getDescription();
			int num = Integer.parseInt(s);
			int oldNum = num;
			ArrayList<Task> list = searchObject.getSearchedTasks();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_PRIORITY_FAIL);
			} else {
				String priority = acceptPriorityFromUser();
				uiObject.printGreen(searchObject.getSearchedTask(oldNum - 1).getIssue() + " has been set to " + priority
						            + " priority");
				num = getCorrectIndexFromSearchView(num);
				markObject.setPriority(num - 1, priority);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to set priority from the display all "view".
	 */
	public void setPriorityFromDisplayAllView() {
		try {
			String s = parserObject.getDescription();
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getUncompletedTasks();
			ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
			if (list.size() + list2.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				uiObject.printRed(MSG_PRIORITY_FAIL);
			} else {
				String priority = acceptPriorityFromUser();

				int sizeOfUncompletedTasksList = localStorageObject.getUncompletedTasks().size();
				if (num <= localStorageObject.getUncompletedTasks().size()) {
					uiObject.printGreen(localStorageObject.getUncompletedTask(num - 1).getIssue() + " has been set to "
							            + priority + " priority");
				} else {
					uiObject.printGreen(
							localStorageObject.getFloatingTask(num - sizeOfUncompletedTasksList - 1).getIssue()
									                           + " has been set to " + priority + " priority");
				}
				markObject.setPriority(num - 1, priority);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}
	
	/**
	 * Function to accept priority from user.
	 * 
	 * @return the priority entered by the user.
	 */
	public String acceptPriorityFromUser() {
		uiObject.printYellow("Enter priority");
		String priority = sc.nextLine();
		while ((priority.equals("high") != true) && (priority.equals("medium") != true)
				&& priority.equals("low") != true) {
			uiObject.printRed("Invalid priority entered. Please enter high, medium or low.");
			priority = sc.nextLine();
		}
		return priority;
	}

	/**
	 * Function to unmark a completed task.
	 */
	public void unmarkCommand() {
		try {
			String s = parserObject.getDescription();
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getCompletedTasks();
			
			if (list.size() == 0) {
				uiObject.printRed(MSG_NO_COMPLETED_TASKS);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_UNMARK_FAIL);
			} else {
				Task temp = crudObject.getCompletedTask(num - 1);
				markObject.markTaskAsUncompleted(num - 1);
				uiObject.printGreen("\"" + temp.getIssue() + "\"" + MSG_UNMARK);
				crudObject.displayNearestFiveUnmarkCompleteTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to determine which "view" to mark a task from.
	 */
	public void markCommand() {
		String s = parserObject.getDescription();
		if (Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if (Logic.Head.getLastDisplayArg().equals("all") || Logic.Head.getLastDisplayArg().equals("floating")) {
				markFromDisplayAllView(s);
			} else {
				markFromDisplayView();
			}
		} else if (Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s")) {
			markFromSearchView();
		} else if (Logic.Head.getLastDisplay().equals("")) {
			int num = getCorrectIndexWelcomeView(Integer.parseInt(s) - 1);
			String index = "" + num;
			markFromDisplayAllView(index);
		} else {
			markFromDisplayAllView(s);
		}
	}

	/**
	 * Function to mark a task from search "view".
	 */
	public void markFromSearchView() {
		String s = parserObject.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = searchObject.getSearchedTasks();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_MARK_FAIL);
			} else {
				Task temp = list.get(num - 1);
				ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
				ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();
				int counter = 0;
				for (Task t : tempUncompletedTasks) {
					if (t.getTaskString().equals(temp.getTaskString())) {
						markObject.markTaskAsCompleted(counter);
						break;
					}
					counter++;
				}
				for (Task t : tempFloatingTasks) {
					if (t.getTaskString().equals(temp.getTaskString())) {
						markObject.markTaskAsCompleted(counter);
						break;
					}
					counter++;
				}
				uiObject.printGreen("\"" + temp.getIssue() + "\"" + MSG_MARK);
				crudObject.displayNearestFiveCompletedTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to mark a task from display all "view".
	 * @param s
	 */
	public void markFromDisplayAllView(String s) {
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getUncompletedTasks();
			ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
			if (list.size() + list2.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				uiObject.printRed(MSG_MARK_FAIL);
			} else {
				Task temp = crudObject.getUncompletedTask(num - 1);
				markObject.markTaskAsCompleted(num - 1);
				uiObject.printGreen("\"" + temp.getIssue() + "\"" + MSG_MARK);
				crudObject.displayNearestFiveCompletedTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to mark a task from welcome "view".
	 */
	public void markFromDisplayView() {
		String s = parserObject.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = crudObject.getTemp();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_MARK_FAIL);
			} else {
				Task temp = crudObject.getTempTask(num - 1);
				num = getCorrectIndexFromDisplayAll(num);
				markObject.markTaskAsCompleted(num - 1);
				uiObject.printGreen("\"" + temp.getIssue() + "\"" + MSG_MARK);
				crudObject.displayNearestFiveCompletedTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to clear all the tasks in the storage.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void clearCommand() throws ClassNotFoundException, IOException {
		crudObject.clearTasks();
		uiObject.printGreen(MSG_CLEAR);
		arraylistsHaveBeenModified = true;
	}

	/**
	 * Function to determine which "view" should the details of the task be shown from.
	 */
	public void viewCommand() {
		String s = parserObject.getDescription();
		if (Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if (Logic.Head.getLastDisplayArg().equals("all") || Logic.Head.getLastDisplayArg().equals("floating")) {
				int num = Integer.parseInt(s);
				crudObject.viewIndividualTask(num - 1);
			} else {
				viewFromDisplayView();
			}
		} else if (Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s")) {
			viewFromSearchView();
		} else if (Logic.Head.getLastDisplay().equals("")) {
			int num = getCorrectIndexWelcomeView(Integer.parseInt(s) - 1);
			crudObject.viewIndividualTask(num - 1);
		} else {
			try {
				int num = Integer.parseInt(s);
				crudObject.viewIndividualTask(num - 1);
			} catch (Exception e) {
				uiObject.printRed(MSG_INVALID);
			}
		}
	}

	/**
	 * Function to view a task from search "view".
	 */
	public void viewFromSearchView() {
		String s = parserObject.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = searchObject.getSearchedTasks();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed("Invalid index entered");
			} else {
				Task temp = list.get(num - 1);
				ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
				ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();
				int counter = 0;
				for (Task t : tempUncompletedTasks) {
					if (t.getTaskString().equals(temp.getTaskString())) {
						crudObject.viewIndividualTask(counter);
						arraylistsHaveBeenModified = true;
						break;
					}
					counter++;
				}
				for (Task t : tempFloatingTasks) {
					if (t.getTaskString().equals(temp.getTaskString())) {
						crudObject.viewIndividualTask(counter);
						arraylistsHaveBeenModified = true;
						break;
					}
					counter++;
				}
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to view a task from display "view".
	 */
	public void viewFromDisplayView() {
		String s = parserObject.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = crudObject.getTemp();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed("Wrong index entered");
			} else {
				num = getCorrectIndexFromDisplayAll(num);
				crudObject.viewIndividualTask(num - 1);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to read which display the user has entered.
	 */
	public void displayCommand() {
		String s = parserObject.getDescription();
		if (s.equals("completed") || s.equals("c")) {
			crudObject.displayCompletedTasks();
		} else if (s.equals("floating") || s.equals("f")) {
			crudObject.displayFloatingTasks();
		} else if (checkDateObject.checkDateformat(s)) {
			crudObject.displayScheduleForADay(s);
		} else if (s.equals("all")) {
			crudObject.displayUncompletedAndFloatingTasks();
		} else if (s.equals("")) {
			crudObject.displayUpcomingTasks();
		} else if (s.equals("today")) {
			Calendar today = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String todayString = df.format(today.getTime());
			crudObject.displayScheduleForADay(todayString);
		} else if (s.equals("tomorrow")) {
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.add(Calendar.DAY_OF_MONTH, 1);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String tomorrowString = df.format(tomorrow.getTime());
			crudObject.displayScheduleForADay(tomorrowString);
		} else if (s.equals("next week") || s.equals("w+1")) {
			crudObject.displayTasksForNextWeek();
		} else if (s.equals("two weeks later") || s.equals("w+2")) {
			crudObject.displayTaksForTwoWeeksLater();
		} else if (s.equals("last week") || s.equals("w -1")) {
			crudObject.displayTasksForLastWeek();
		} else {
			crudObject.displayByLabel(s);
		}
	}

	/**
	 * Function to determine from which "view" task should be deleted.
	 */
	public void deleteCommand() {
		String s = parserObject.getDescription();
		if ((Logic.Head.getLastDisplay().equals("d") == true || Logic.Head.getLastDisplay().equals("display")) == true) {
			if (Logic.Head.getLastDisplayArg().equals("all") || Logic.Head.getLastDisplayArg().equals("floating")) {
				if (s.contains("all") != true) {
					deleteFromDisplayAllView(s);
				} else {
					deleteAllRecurringTasks();
				}
			} else if (Logic.Head.getLastDisplayArg().equals("completed")|| Logic.Head.getLastDisplayArg().equals("c")) {
				if (s.contains("all") != true) {
					deleteFromDisplayCompletedView();
				} else {
					deleteAllRecurringTasks();
				}
			} else { // this is "display" only.
				if (s.contains("all") != true) {
					deleteFromDisplayView();
				} else {
					deleteAllRecurringTasks();
				}
			}
		} else if ((Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s"))) {
			// delete from search results.
			if (s.contains("all") != true) {
				deleteFromSearchView();
			} else {
				deleteAllRecurringTasks();
			}
		} else if (Logic.Head.getLastDisplay().equals("")) {
			if (s.contains("all") != true) {
				int num = getCorrectIndexWelcomeView(Integer.parseInt(s) - 1);
				if (num == -1) { // -1 when storage is empty, and user tries to delete immediately after launch.
					uiObject.printRed(MSG_EMPTY);
					return;
				}

				String index = "" + num;
				deleteFromDisplayAllView(index);
			} else {
				deleteAllRecurringTasks();
			}
		} else {
			deleteFromDisplayAllView(s);
		}
	}

	/**
	 * Function to delete a task from display "view".
	 */
	public void deleteFromDisplayView() {
		String s = parserObject.getDescription();
		String[] splitInput = s.split(" ");
		try {
			int num;
			if (splitInput.length == 2) {
				num = Integer.parseInt(splitInput[1]);
			} else {
				num = Integer.parseInt(splitInput[0]);
			}

			ArrayList<Task> list = crudObject.getTemp();
			Task deleted = list.get(num - 1);
			issue = deleted.getIssue();
			try {
				crudObject.deleteTask(num - 1, 5);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			uiObject.printGreen("\"" + issue + "\" " + MSG_DELETE);
			crudObject.displayNearestFiveDeleteUncompleteTaskList(num - 1);
			arraylistsHaveBeenModified = true;
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to delete a task from display completed "view".
	 */
	public void deleteFromDisplayCompletedView() {
		String s = parserObject.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getCompletedTasks();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_TASK_DES_NOT_EXIST);
			} else {
				Task deleted = list.get(num - 1);
				issue = deleted.getIssue();
				crudObject.deleteTask(num - 1, 2);
				uiObject.printGreen("\"" + issue + "\" " + MSG_DELETE);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to delete a task from display completed "view".
	 */
	public void deleteFromSearchView() {
		String s = parserObject.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = searchObject.getSearchedTasks();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_TASK_DES_NOT_EXIST);
			} else {
				Task deleted = list.get(num - 1);
				issue = deleted.getIssue();
				crudObject.deleteTask(num - 1, 3);

				uiObject.printGreen("\"" + issue + "\" " + MSG_DELETE);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to delete a task from display all "view".
	 * 
	 * @param s the index of the task to be deleted.
	 */
	public void deleteFromDisplayAllView(String s) {
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getUncompletedTasks();
			ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
			if (list.size() + list2.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
				uiObject.printRed(MSG_TASK_DES_NOT_EXIST);
			} else {
				if ((num - 1) < list.size()) {
					Task deleted = list.get(num - 1);
					issue = deleted.getIssue();
					crudObject.deleteTask(num - 1, 1);

					uiObject.printGreen("\"" + issue + "\" " + MSG_DELETE);
					crudObject.displayNearestFiveDeleteUncompleteTaskList(num - 1);
					arraylistsHaveBeenModified = true;
				} else {
					Task deleted = list2.get(num - list.size() - 1);
					issue = deleted.getIssue();
					crudObject.deleteTask(num - 1, 1);

					uiObject.printGreen("\"" + issue + "\" " + MSG_DELETE);
					crudObject.displayNearestFiveDeleteFloatingTask(num - 1);
					arraylistsHaveBeenModified = true;
				}
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	//@@author Jung Kai
	/**
	 * Function to delete all instances of a recurring task from the list of tasks.
	 */
	public void deleteAllRecurringTasks() {
		String s = parserObject.getDescription();
		try {
			String[] tmp = s.split(" ");
			if (s.contains("all")) {
				int num = Integer.parseInt(tmp[1]);
				boolean isDeleted = delAllRecurringTask(num - 1);
				if (isDeleted) {
					uiObject.printGreen("All instances of Task " + num + " have been deleted");
				} else {
					uiObject.printRed("Not a recurring tasks. Enter delete/d followed by index to delete this task");
				}
				arraylistsHaveBeenModified = isDeleted;
			} else {
				int num = Integer.parseInt(s);
				ArrayList<Task> list = localStorageObject.getUncompletedTasks();
				ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					uiObject.printRed(MSG_EMPTY);
				} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
					uiObject.printRed(MSG_TASK_DES_NOT_EXIST);
				} else {
					if ((num - 1) < list.size()) {
						Task deleted = list.get(num - 1);
						issue = deleted.getIssue();

						crudObject.deleteTask(num - 1, 1);
						uiObject.printGreen("\"" + issue + "\" " + MSG_DELETE);
						crudObject.displayNearestFiveDeleteUncompleteTaskList(num - 1);
						arraylistsHaveBeenModified = true;
					} else {
						Task deleted = list2.get(num - list.size() - 1);
						issue = deleted.getIssue();
						crudObject.deleteTask(num - 1, 1);

						uiObject.printGreen("\"" + issue + "\" " + MSG_DELETE);
						crudObject.displayNearestFiveDeleteFloatingTask(num - 1);
						arraylistsHaveBeenModified = true;
					}
				}
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to convert an array to a string.
	 * 
	 * @param arr
	 * @return String
	 */
	public String arrayToString(String[] arr) {
		String temp = Arrays.toString(arr);
		temp = temp.substring(1, temp.length() - 1).replaceAll(", ", " ");
		return temp;
	}

	

	/**
	 * Function that returns the index of first keyword present in the String[] and
	 * return -1 if no keyword is present.
	 * 
	 * @param arr      the user input.
	 *   
	 * @return integer the index of the first keyword.
	 */
	public int getIndexOfKey(String[] arr) {
		int idx = -1;
		for (int j = 0; j < arr.length; j++) {
			for (int i = 0; i < KEY.length; i++) {
				if (arr[j].equals(KEY[i])) {
					idx = j;
				}
			}
		}
		return idx;
	}

	/**
	 * Function that returns the index of "from" from the input String[] and return
	 * -1 if no starting index is present.
	 * 
	 * @param arr      the user date input.
	 * 
	 * @return integer the index of the first occurrence of 'from' in the user date input.
	 */
	public int getStartingIndex(String[] arr) {
		int idx = -1;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals("from") || arr[i].equals("on")) {
				idx = i;
			}
		}
		return idx;
	}

	/**
	 * Function that returns a boolean value to indicate if input string[] contains starting time.
	 * 
	 * @param arr      the user date input.
	 * 
	 * @return boolean true if the input contains a starting time, false otherwise.
	 */
	public boolean hasStartTime(String[] arr) {
		boolean containTime = true;
		int start = getStartingIndex(arr);
		// if date is the last argument => no time.
		if (start + 2 >= arr.length) {
			containTime = false;
		} else {
			if (!checkDateObject.checkTimeformat(arr[start + 2])) {
				containTime = false;
			}
		}
		return containTime;
	}

	/**
	 * Function that returns a boolean value to indicate if input String[] contains ending time.
	 * 
	 * @param arr      the user date input.
	 * 
	 * @return boolean true if the input contains a starting time, false otherwise.
	 */
	public boolean hasEndTime(String[] arr) {
		boolean containTime = true;
		int end = getIndexOfKey(arr);
		// if date is the last argument => no time.
		if (end + 2 >= arr.length) {
			containTime = false;

		} else {
			if (!checkDateObject.checkTimeformat(arr[end + 2])) {
				containTime = false;
			}
		}
		return containTime;
	}


	/**
	 * Function that processes Date for recurring tasks based on the date and number
	 * of recurring tasks calculated.
	 * 
	 * @param s  the input date entered by the user.
	 * @param n  the number of recurring tasks.
	 * 
	 * @return   the processed date.
	 */
	public String processDate(String s, int n) {
		String[] temp = s.split("/");
		temp[0] = String.valueOf(Integer.parseInt(temp[0]) + n);
		YearMonth yearMonthObject;
		yearMonthObject = YearMonth.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[1]));
		int daysInMonth = yearMonthObject.lengthOfMonth();
		if (Integer.parseInt(temp[0]) > daysInMonth) {
			temp[0] = String.valueOf(Integer.parseInt(temp[0]) - daysInMonth);
			temp[1] = String.valueOf(Integer.parseInt(temp[1]) + 1);
		}
		if (temp[0].length() == 1) {
			temp[0] = "0" + temp[0];
		}
		if (temp[1].length() == 1) {
			temp[1] = "0" + temp[1];
		}

		String tmp = arrayToString(temp);
		tmp = tmp.replaceAll(" ", "/");

		return tmp;

	}

	/**
	 * Function that deletes all recurring task at index n from recurring tasks and
	 * uncompleted tasks in storage.
	 * 
	 * @param n                       the index of the recurring task to be deleted.
	 * 
	 * @return                        true if the edit is successful.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public boolean delAllRecurringTask(int n) throws ClassNotFoundException, IOException {
		ArrayList<Task> list = localStorageObject.getUncompletedTasks();
		Task deleted = list.get(n);

		String id = deleted.getId();
		if (id.equals("")) {// if the task is not recurring task.
			return false;
		} else {
			for (int i = 0; i < list.size(); i++) {// delete from uncompleted tasks.
				Task task = list.get(i);
				if (id.equals(task.getId())) {
					crudObject.deleteTask(i, 1);
					i = -1;
				}
			}
			return true;
		}
	}

	/**
	 * Function that supports the editing of recurring task at index n.
	 * 
	 * @param n                       the index of the recurring task to be edited.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void editRecurringTask(int n) throws ClassNotFoundException, IOException {
		Task replaced = localStorageObject.getUncompletedTask(n);
		if (replaced.getId().equals("")) { // if task is not a recurring task, stop & inform user.
			uiObject.printRed(MSG_EDIT_NOT_RECURRING_TASK_HEAD + (n + 1) + MSG_EDIT_NOT_RECURRING_TASK_TAIL);
			return;
		}
		
		crudObject.copyTask(replaced);
		uiObject.printRed("Enter new description and deadline of recurring tasks");
		String in = sc.nextLine();
		in = Natty.getInstance().parseEditString(in); // get index of key.
		String[] s2 = in.split("`");

		if (s2.length == 2) {
			String[] temp = s2[1].split(" ");
			int start = getStartingIndex(temp); // start has value of -1.
			int end = getIndexOfKey(temp);
			issue = s2[0];
			
			if (start == -1 && end != -1) {// no start date but has end date.
				startDate = "-";
				startTime = "-";
				endDate = temp[end + 1]; // read date & time.
				endDateWithTime = endDate;
				if (hasEndTime(temp)) {// check if contain end time.
					endTime = temp[end + 2];
					endTime = endTime.replaceAll(":", "/");
					endDateWithTime = endDateWithTime + "/" + endTime;
				} else {
					endTime = "-";
				}

				if (!checkDateObject.checkDateformat(endDate)) {
					uiObject.printRed(MSG_WRONG_DATE);
				} else {
					uiObject.printRed(PROMPT_RECURRING);
					try {
						String in2 = sc.nextLine();
						String[] tmp = in2.split(" ");
						String freq = tmp[0];
						String last = tmp[1];
						int frequency = Integer.parseInt(freq);

						Task task = new Task(issue, endDateWithTime, in, false, frequency, last);
						checkDateAndAdd(task);
						arraylistsHaveBeenModified = true;
						delAllRecurringTask(n);
						uiObject.printGreen("All instances of Task " + (n + 1) + " has been edited and saved");
					} catch (Exception e) {
						uiObject.printRed(MSG_INVALID);
						arraylistsHaveBeenModified = false;
					}
				}
			} else if (start != -1 && end == -1) {// has start date but no end date.
				endDate = "-";
				endTime = "-";
				startDate = temp[start + 1];
		
				startDateWithTime = startDate;

				if (hasStartTime(temp)) {
					startTime = temp[start + 2];
					startTime = startTime.replaceAll(":", "/");
					startDateWithTime = startDateWithTime + "/" + startTime;
				} else {
					startTime = "-";
				}
				if (!checkDateObject.checkDateformat(startDate)) {
					uiObject.printRed(MSG_WRONG_DATE);
				} else {
					uiObject.printRed(PROMPT_RECURRING);
					try {
						String in2 = sc.nextLine();
						String[] tmp = in2.split(" ");
						String freq = tmp[0];
						String last = tmp[1];
						int frequency = Integer.parseInt(freq);

						Task task = new Task(issue, endDateWithTime, in, true, frequency, last);
						checkDateAndAdd(task);
						arraylistsHaveBeenModified = true;
						delAllRecurringTask(n);
						uiObject.printGreen("All instances of Task " + (n + 1) + " has been edited and saved");
					} catch (Exception e) {
						uiObject.printRed(MSG_INVALID);
						arraylistsHaveBeenModified = false;
					}
				}
			} else { // has both start date and end date.
				startDate = temp[start + 1];
				endDate = temp[end + 1];
		
				endDateWithTime = endDate;
				startDateWithTime = startDate;
				if (hasStartTime(temp)) {
					startTime = temp[start + 2];
					startTime = startTime.replaceAll(":", "/");
					startDateWithTime = startDateWithTime + "/" + startTime;
				} else {
					startTime = "-";
				}
				if (hasEndTime(temp)) {
					endTime = temp[end + 2];
					endTime = endTime.replaceAll(":", "/");
					endDateWithTime = endDateWithTime + "/" + endTime;
				} else {
					endTime = "-";
				}
				if (!checkDateObject.checkDateformat(startDate) && !checkDateObject.checkDateformat(endDate)) {
					uiObject.printRed(MSG_WRONG_DATE);
				} else {
					uiObject.printRed(PROMPT_RECURRING);
					try {
						String in2 = sc.nextLine();
						String[] tmp = in2.split(" ");
						String freq = tmp[0];
						String last = tmp[1];
						int frequency = Integer.parseInt(freq);
						
						Task task = new Task(issue, startDateWithTime, endDateWithTime, in, frequency, last);
						checkDateAndAdd(task);
						arraylistsHaveBeenModified = true;
						delAllRecurringTask(n);
						uiObject.printGreen("All instances of Task " + (n + 1) + " has been edited and saved");
					} catch (Exception e) {
						uiObject.printRed(MSG_INVALID);
						arraylistsHaveBeenModified = false;
					}
				}
			}
		}
	}

	/**
	 * Function to check if date is valid.
	 * 
	 * @param task the task for which the date is checked.
	 */
	public void checkDateAndAdd(Task task) {
		try {
			String id = task.getId();
			String ed = task.getDateCompare();
			boolean expired = isExpired(ed, task.getLastDate());

			while (true) {
				if (expired) {// If task is not within display time frame or when task expired.
					break;
				}
				localStorageObject.addToUncompletedTasks(task);
				String newED = processDate(ed, task.getFrequency());
				if (task.getStartDate() == null) {// no start date.
					task = new Task(task.getIssue(), newED, task.getMsg(), false, task.getFrequency(),
							task.getLastDate());
					task.setID(id);
				} else if (task.getEndDate() == null) {
					task = new Task(task.getIssue(), newED, task.getMsg(), true, task.getFrequency(),
							task.getLastDate());
					task.setID(id);
				} else {// has start date and end date.
					task = new Task(task.getIssue(), task.getFixedStartDateString(), newED, task.getMsg(),
							task.getFrequency(), task.getLastDate());
					task.setID(id);
				}
				ed = task.getDateCompare();
				expired = (isExpired(ed, task.getLastDate()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to check if recurring task is expired.
	 * 
	 * @param currentRecurDate the date of the particular instance of the recurring task.
	 * @param recurDeadline    the date of the last instance of the recurring task.
	 * 
	 * @return                 true if date has expired, false otherwise.
	 */
	public boolean isExpired(String currentRecurDate, String recurDeadline) {
		String[] splitCurrentRecurDate = currentRecurDate.split("/");
		String[] splitRecurDeadline = recurDeadline.split("/");
		if (Integer.parseInt(splitRecurDeadline[2]) < Integer.parseInt(splitCurrentRecurDate[2])) { // end year < current year.
			return true;
		} else if (Integer.parseInt(splitRecurDeadline[2]) > Integer.parseInt(splitCurrentRecurDate[2])) { // end year>current year
			return false;
		} else { // end year == current year
			if (Integer.parseInt(splitRecurDeadline[1]) > Integer.parseInt(splitCurrentRecurDate[1])) { // end month> current month
				return false;
			}
			if (Integer.parseInt(splitRecurDeadline[1]) < Integer.parseInt(splitCurrentRecurDate[1])) { // end month < today.
				return true;
			} else { // same year & month.
				if (Integer.parseInt(splitRecurDeadline[0]) >= Integer.parseInt(splitCurrentRecurDate[0])) { // valid date.
					return false;
				} else {
					return true;
				}
			}
		}
	}
}