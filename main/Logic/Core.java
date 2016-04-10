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
import Storage.LocalStorage;
import Task.Task;

public class Core {
	private static boolean arraylistsHaveBeenModified;
	private static CheckDate checkDateObject = new CheckDate();
	private static int INVALID_TASK_INDEX = -1;	
	private static LocalStorage localStorageObject = LocalStorage.getInstance();
	private static String startDate, date, issue, startTime, time, input, dateIn, dateIn2;

	private static final String[] week = { "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday" };
	private static final String[] key = { "by", "at", "during", "before", "to", "in" };
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

	private static Help helpObject = new Help();
	private static Scanner sc = new Scanner(System.in);

	/**
	 * method that simulate command line interface that will responds to user's
	 * inputs
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 *             return boolean
	 */
	public static void acceptCommand() throws ClassNotFoundException, IOException {
		// take "snapshots" of current storage state
		Undo.getInstance().copyCurrentTasksState();

		String command = sc.nextLine();
		
		UI.UI.eraseScreen();
		UI.UI.printRed("command: ");
		UI.UI.print(command);
	
		String[] splitCommand = command.split(" ");
		if (splitCommand[0].equals("add") || splitCommand[0].equals("a") || splitCommand[0].equals("+")) {
			// only use natty if add command is detected
			command = Natty.getInstance().parseString(command);
		}
		Parser.Parser.parse(command);

		// take "snapshots" of current storage state
		Undo.getInstance().copyCurrentTasksState();
		boolean modificationsWereMade = parseCommands();
		if (modificationsWereMade) {
			// store the "snapshots" into Undo class if arraylists have been modified
			Undo.getInstance().storePreviousState(command);
			Undo.getInstance().clearRedoCommands(); // if valid command executed and arraylists modified, remove all stored redo commands
		}
	}

	/**
	 * methods that take in command and the body as the argument and process
	 * them to to meet the requests of the user.
	 * 
	 * @param option
	 * @param s
	 * @throws IOException
	 * @throws ClassNotFoundException
	 *             return boolean
	 */
	public static boolean parseCommands() throws IOException, ClassNotFoundException {
		arraylistsHaveBeenModified = false;
		String option = Parser.Parser.getCommand();
		if (option.equals("add") || option.equals("a") || option.equals("+")) {
			addCommand();
		} else if (option.equals("delete") || option.equals("-")) {
			deleteCommand();
		} else if (option.equals("display") || option.equals("d")) {
			displayCommand();
		} else if (option.equals("view") || option.equals("v")) {
			viewCommand();
		} else if (option.equals("clear") || option.equals("c")) {
			clearCommand();
		} else if (option.equals("search") || option.equals("s")) {
			Logic.Search.searchTasksByKeyword(Parser.Parser.getDescription());
		} else if (option.equals("mark") || option.equals("m")) {
			markCommand();
		} else if (option.equals("unmark") || option.equals("um")) {
			unmarkCommand();
		} else if (option.equals("edit") || option.equals("e")) {
			editCommand();
		} else if (option.equals("priority") || option.equals("p")) {
			String s=Parser.Parser.getIssueM();
			if(s.contains("all")) {
				setAllRecurringTasksPriorityCommand();
			} else {
				setPriorityCommand();
			}
		} else if (option.equals("history")) {
			String pastCommands = Undo.getInstance().viewPastCommands();
			UI.UI.printYellow(pastCommands);
		} else if (option.equals("future")) {
			String possibleRedoCommands = Undo.getInstance().viewRedoCommands();
			UI.UI.printYellow(possibleRedoCommands);
		} else if (option.equals("undo")) {
			undoCommand();
		} else if (option.equals("redo")) {
			redoCommand();
		} else if (option.equals("exit")) {
			UI.UI.printGreen("Bye!");
			AnsiConsole.systemUninstall();
			Logic.Crud.exit();
		} else if (option.equals("help")) {
			helpObject.printHelpMenu();
		} else if (option.equals("dir")) {
			changeDirectoryCommand();
		} else if (option.equals("label")) {
			setLabelCommand();
		} else {
			UI.UI.printRed(MSG_INVALID);
		}
		return arraylistsHaveBeenModified;
	}
	private static void setAllRecurringTasksPriorityCommand() {
		String description=Parser.Parser.getDescription();
		String [] tmp = description.split(" ");
		String idx = tmp[1];
		int num = Integer.parseInt(idx);


		ArrayList<Task> list = localStorageObject.getUncompletedTasks();

		if (list.size() == 0) {
			UI.UI.printRed(MSG_EMPTY);
		} else if (list.size()  < num || num - 1 < 0) {
			UI.UI.printRed(MSG_PRIORITY_FAIL);
		} else {
			UI.UI.printYellow("Enter priority");
			String priority = sc.nextLine();
			Logic.Mark.setRecurringTasksPriority(num - 1, priority);
			arraylistsHaveBeenModified = true;
		}

	}


	public static void 	addCommand() throws IOException, ClassNotFoundException {
		String description = Parser.Parser.getDescription();
		String startDate = Parser.Parser.getStartDate();
		String startDateWithTime = Parser.Parser.getStartDateWithTime();
		String endDate = Parser.Parser.getEndDate();
		String endDateWithTime = Parser.Parser.getEndDateWithTime();
		String issue = Parser.Parser.getIssueM();
		boolean recurrence = Parser.Parser.getRecurrence();
		boolean containDate = Parser.Parser.getContainDate();
		boolean isAdded;

		if (description.equals("")) {
			UI.UI.printRed(MSG_INVALID);
		} else {
			// get index of key
			if(containDate){
				if (!recurrence) {
					if (startDate.equals("-") && !endDate.equals("-")) { // no start date but has end date
						if (!checkDateObject.checkDateformat(endDate)) {
							UI.UI.printRed(MSG_WRONG_DATE);
						} else {
							isAdded = Logic.Crud.addTaskWithEndDate(issue, endDateWithTime, description);
							if (isAdded) {
								Logic.Sort.sortTasksChronologically();
								int index = Logic.Crud.uncompletedTaskIndexWithEndDate(issue, endDateWithTime, description);
								UI.UI.printGreen("\"" + issue + "\" " + MSG_ADD);
								arraylistsHaveBeenModified = true;
								Logic.Crud.displayNearestFiveUncompleted(index);
							} else {
								UI.UI.printRed(MSG_DUPLICATE_ADD);
							}
						}

					} else if ((!startDate.equals("-")) && endDate.equals("-")) { // has start date

						if (!checkDateObject.checkDateformat(startDate)) {
							UI.UI.printRed(MSG_WRONG_DATE);
						} else {
							isAdded = Logic.Crud.addTaskWithStartDate(issue, startDateWithTime, description);
							if (isAdded) {
								Logic.Sort.sortTasksChronologically();
								int index = Logic.Crud.uncompletedTaskIndexWithStartDate(issue, startDateWithTime, description);
								UI.UI.printGreen("\"" + issue + "\" " + MSG_ADD);
								Logic.Crud.displayNearestFiveUncompleted(index);
								arraylistsHaveBeenModified = true;
							} else {
								UI.UI.printRed(MSG_DUPLICATE_ADD);
							}
						}
					} else { // has both start date and end date

						if (!checkDateObject.checkDateformat(startDate) && !checkDateObject.checkDateformat(endDate)) {
							UI.UI.printRed(MSG_WRONG_DATE);
						} else {
							isAdded = Logic.Crud.addTaskWithBothDates(issue, startDateWithTime, endDateWithTime, description);
							if (isAdded) {
								Logic.Sort.sortTasksChronologically();
								int index = Logic.Crud.uncompletedTaskIndexWithBothDates(issue, startDateWithTime, endDateWithTime, description);
								UI.UI.printGreen("\"" + issue + "\" " + MSG_ADD);
								Logic.Crud.displayNearestFiveUncompleted(index);
								arraylistsHaveBeenModified = true;
							} else {
								UI.UI.printRed(MSG_DUPLICATE_ADD);
							}
						}
					}
				} else { // for recurring tasks

					if (startDate.equals("-") && !endDate.equals("-")) {// no start date but has
						if (!checkDateObject.checkDateformat(endDate)) {
							UI.UI.printRed(MSG_WRONG_DATE);
						} else {
							UI.UI.printRed(PROMPT_RECURRING);
							try {
								String in = sc.nextLine();
								String[] tmp = in.split(" ");
								String freq = tmp[0];
								String last = tmp[1];

								int frequency = Integer.parseInt(freq);						

								Task task = new Task(issue,endDateWithTime,description,true,frequency,last);
								checkDateAndAdd(task);
								UI.UI.printGreen("\"" + task.getIssue() + "\"" +  " is added to the task list. (recurs every " + freq + " days)");
								arraylistsHaveBeenModified = true;
							} catch (Exception e) {
								UI.UI.printRed(MSG_INVALID);
								arraylistsHaveBeenModified = false;
							}

						}
					} else if ((!startDate.equals("-")) && endDate.equals("-")) {// has start date

						if (!checkDateObject.checkDateformat(startDate)) {
							UI.UI.printRed(MSG_WRONG_DATE);
						} else {
							UI.UI.printRed(PROMPT_RECURRING);
							try {
								String in = sc.nextLine();
								String[] tmp = in.split(" ");
								String freq = tmp[0];
								String last = tmp[1];

								int frequency = Integer.parseInt(freq);

								Task task = new Task(issue,startDateWithTime,description,true,frequency,last);
								checkDateAndAdd(task);
								UI.UI.printGreen("\"" + task.getIssue() + "\"" +  " is added to the task list. (recurs every " + freq + " days)");
								arraylistsHaveBeenModified = true;
							} catch (Exception e) {
								UI.UI.printRed(MSG_INVALID);
								arraylistsHaveBeenModified = false;
							}

						}
					} else { // has both start date and end date

						if (!checkDateObject.checkDateformat(startDate) && !checkDateObject.checkDateformat(date)) {
							UI.UI.printRed(MSG_WRONG_DATE);
						} else {
							UI.UI.printRed(PROMPT_RECURRING);
							try {
								String in = sc.nextLine();
								String[] tmp = in.split(" ");
								String freq = tmp[0];
								String last = tmp[1];
								//	String before = tmp[2];

								int frequency = Integer.parseInt(freq);


								Task task = new Task(issue,startDateWithTime,endDateWithTime,description,frequency,last);
								UI.UI.printGreen("\"" + task.getIssue() + "\"" +  " is added to the task list. (recurs every " + freq + " days)");
								checkDateAndAdd(task);
								arraylistsHaveBeenModified = true;
							} catch (Exception e) {
								UI.UI.printRed(MSG_INVALID);
								arraylistsHaveBeenModified = false;
							}
						}
					}
				}
			} else {
				isAdded = Logic.Crud.addTask(description);
				if (isAdded) {
					Logic.Sort.sortTasksChronologically();
					int index = Logic.Crud.uncompletedTaskIndexWithNoDate(description);
					UI.UI.printGreen("\"" + description + "\" " + MSG_ADD);
					Logic.Crud.displayNearestFiveFloating(index);
					arraylistsHaveBeenModified = true;
				} else {
					UI.UI.printRed(MSG_DUPLICATE_ADD);
				}
			}
		}
	}




	// @@author Kowshik
	public static void setLabelCommand() {
		String description = Parser.Parser.getDescription();
		try {
			int num = Integer.parseInt(description);

			ArrayList<Task> list = localStorageObject.getUncompletedTasks();
			ArrayList<Task> list2 = localStorageObject.getFloatingTasks();

			if (list.size() + list2.size() == 0) {
				UI.UI.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				UI.UI.printRed(MSG_PRIORITY_FAIL);
			} else {
				UI.UI.print("Enter label");
				String label = sc.nextLine();
				Logic.Crud.addLabelToTask(num - 1, label);
				arraylistsHaveBeenModified = true;
				Task temp = localStorageObject.getUncompletedTask(num-1);
				String issue = temp.getIssue();
				UI.UI.printGreen("Task "+issue+" has been labelled "+label);
			}
		} catch (Exception e) {

		}
	}

// @@author Jie Wei
	public static void changeDirectoryCommand() throws IOException, ClassNotFoundException {
		String description = Parser.Parser.getDescription();
		ImportTasks importTasksObject = new ImportTasks();
		if (description.isEmpty()) { // only "dir" was typed, this will display the
			// current storage folder directory in use
			String currentStorageDirectory = importTasksObject.getFolderDirectory();
			if (currentStorageDirectory.isEmpty()) { // indicates source
				// folder is in use
				UI.UI.printGreen(MSG_DIRECTORY_USED + MSG_DEFAULT_DIRECTORY);
			} else {
				UI.UI.printGreen(MSG_DIRECTORY_USED + currentStorageDirectory);
			}
		} else { // "dir <path>" was entered
			String feedback = importTasksObject.changeStorageDestination(description);
			UI.UI.print(feedback);
		}
	}

	public static void redoCommand() throws ClassNotFoundException, IOException {
		String s = Parser.Parser.getDescription();
		if (s.isEmpty()) { // only "redo" was typed
			String outcome = Undo.getInstance().redo();
			UI.UI.printGreen(outcome);
		} else if (s.equals("all")) {
			int redoCount = Undo.getInstance().getRedoCount();
			if (redoCount == 0) { // if no commands to redo
				UI.UI.printRed(MSG_NO_REDO_COMMAND);
			} else {
				for (int i = 0; i < redoCount; i++) { // do redo for all stored
					// commands
					String outcome = Undo.getInstance().redo();
					UI.UI.printGreen(outcome);
				}
				UI.UI.printGreen(MSG_ALL_COMMANDS_REDONE);
			}
		} else { // e.g. "redo 2" will redo the latest 2 commands
			try {
				int count = Integer.parseInt(s);
				if (count < 1 || count > Undo.getInstance().getRedoCount()) { // if
					// entered count is outside valid bounds
					UI.UI.printRed(MSG_INVALID_REDO_COUNT);
				} else {
					for (int i = 0; i < count; i++) { // redo the number of
						// commands specified
						if (Undo.getInstance().getRedoCount() == 0) { // all
							// commands have been redone but user used a higher int
							UI.UI.printRed(MSG_NO_REDO_COMMAND);
							break;
						}
						String outcome = Undo.getInstance().redo();
						UI.UI.printGreen(outcome);
					}
				}
			} catch (NumberFormatException e) { // if non-number was entered,
				// e.g. "redo hello"
				UI.UI.printRed(MSG_INVALID_REDO_COUNT);
			}
		}
	}

	public static void undoCommand() throws ClassNotFoundException, IOException {
		String s = Parser.Parser.getDescription();
		if (s.isEmpty()) { // only "undo" was typed
			String outcome = Undo.getInstance().undo();
			UI.UI.printGreen(outcome);
		} else if (s.equals("all")) {
			int historyCount = Undo.getInstance().getHistoryCount();
			if (historyCount == 0) { // if no commands to undo
				UI.UI.printRed(MSG_NO_PAST_COMMAND);
			} else {
				for (int i = 0; i < historyCount; i++) { // do undo for all
					// stored commands
					String outcome = Undo.getInstance().undo();
					UI.UI.printGreen(outcome);
				}
				UI.UI.printGreen(MSG_ALL_COMMANDS_UNDONE);
			}
		} else { // e.g. "undo 2" will undo the latest 2 commands
			try {
				int count = Integer.parseInt(s);
				if (count < 1 || count > Undo.getInstance().getHistoryCount()) { // if
					// entered count is outside valid bounds
					UI.UI.printRed(MSG_INVALID_UNDO_COUNT);
				} else {
					for (int i = 0; i < count; i++) { // undo the number of
						// commands specified
						if (Undo.getInstance().getHistoryCount() == 0) { // all commands have been undone but user used a higher int
							UI.UI.printRed(MSG_NO_PAST_COMMAND);
							break;
						}
						String outcome = Undo.getInstance().undo();
						UI.UI.printGreen(outcome);
					}
				}
			} catch (NumberFormatException e) { // if non-number was entered,
				// e.g. "undo hello"
				UI.UI.printRed(MSG_INVALID_UNDO_COUNT);
			}
		}
	}



	// @@author Kowshik
	public static void editCommand() {
		int num;
		String description = Parser.Parser.getDescription();
		if(description.contains("all")) {
			String[] tmp = description.split(" ");
			num = Integer.parseInt(tmp[1]);
		} else {
			num = Integer.parseInt(description);
		}
		if(Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if(Logic.Head.getLastDisplayArg().equals("floating") || Logic.Head.getLastDisplayArg().equals("all")) {
				if (description.contains("all")) {
					String[] tmp = description.split(" ");
					num = Integer.parseInt(tmp[1]);
				} else {
					num = Integer.parseInt(description);
				}
			} else {
				num = getCorrectIndexFromDisplayAll(num);
			}
		}
		else if(Logic.Head.getLastDisplay().equals("search") ||  Logic.Head.getLastDisplay().equals("s")) {
			num = getCorrectIndexFromSearchView(num);
		}
		else if(Logic.Head.getLastDisplay().equals("")) {
			num = getCorrectIndexWelcomeView(num - 1);
		}

		//@@author Jung Kai
		try {
			if(num<0){
				UI.UI.printRed(MSG_INVALID);
			}else if(description.contains("all")) {
				editRecurringTask(num - 1);
			} else {
				// check if user input integer is valid. If it is valid, edit should work
				ArrayList<Task> list = localStorageObject.getUncompletedTasks();
				ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					UI.UI.printRed(MSG_EMPTY);
				} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
					UI.UI.printRed(MSG_EDIT_FAIL);
				} else {
					UI.UI.printGreen(PROMPT_EDIT);
					Logic.Crud.copyEditingTask(num);
					input = sc.nextLine();
					input = Natty.getInstance().parseEditString(input);
					input ="edit "+input;
					Parser.Parser.parse(input);
					description = Parser.Parser.getDescription();
					String startDate = Parser.Parser.getStartDate();
					String startDateWithTime = Parser.Parser.getStartDateWithTime();
					String endDate = Parser.Parser.getEndDate();
					String endDateWithTime = Parser.Parser.getEndDateWithTime();
					String issue = Parser.Parser.getIssueM();
					boolean rec = Parser.Parser.getRecurrence();
					boolean containDate = Parser.Parser.getContainDate();
					if (description.equals("")) {
						UI.UI.printRed(MSG_INVALID);
					}else{
						if (containDate) {
							if (!rec) {
								if (startDate.equals("-") && !endDate.equals("-")) {
									if (!checkDateObject.checkDateformat(endDate)) {
										UI.UI.printRed(MSG_WRONG_DATE);
									} else {
										// Logic.crud.editTask(num-1,issue,startDate,startTime,endDate,endTime,input)
										// (to be implemented)
										Logic.Crud.editTaskWithEndDate(issue, endDateWithTime, description, num - 1);
										Logic.Sort.sortTasksChronologically();
										
										int index = Logic.Crud.uncompletedTaskIndexWithEndDate(issue, endDateWithTime, description);
										UI.UI.printGreen("Task number " + num + MSG_EDIT);
										Logic.Crud.displayNearestFiveUncompleted(index);
										arraylistsHaveBeenModified = true;
									}
								} else if ((!startDate.equals("-")) && endDate.equals("-")) {// has start date

									if (!checkDateObject.checkDateformat(startDate)) {
										UI.UI.printRed(MSG_WRONG_DATE);
									} else {

										// Logic.crud.editTask(issue,startDate,startTime,endDate,endTime,input);
										Logic.Crud.editTaskWithStartDate(issue, startDateWithTime, description, num - 1);
										Logic.Sort.sortTasksChronologically();
										int index = Logic.Crud.uncompletedTaskIndexWithStartDate(issue, startDateWithTime, description);
										
										UI.UI.printGreen("Task number " + num + MSG_EDIT);
										Logic.Crud.displayNearestFiveUncompleted(index);
										arraylistsHaveBeenModified = true;
									}
								} else { // has both start date and end date
									if (!checkDateObject.checkDateformat(startDate)	&& !checkDateObject.checkDateformat(endDate)) {
										UI.UI.printRed(MSG_WRONG_DATE);
									} else {
										// get issue
										// Logic.crud.addTask(issue,startDate,startTime,endDate,endTime);
										Logic.Crud.editTaskWithBothDates(issue, startDateWithTime, endDateWithTime, description, num - 1);
										
										UI.UI.printGreen("Task number " + num + MSG_EDIT);
										Logic.Sort.sortTasksChronologically();
										int index = Logic.Crud.uncompletedTaskIndexWithBothDates(issue, startDateWithTime, endDateWithTime,
												input);
										Logic.Crud.displayNearestFiveUncompleted(index);
										arraylistsHaveBeenModified = true;
									}
								}
							}
						} else {// no end date and no
							// start date

							Logic.Crud.editTaskWithNoDate(input, input, num - 1);
							int index = Logic.Crud.uncompletedTaskIndexWithNoDate(description);
							UI.UI.printGreen("Task number " + num + MSG_EDIT);
							Logic.Crud.displayNearestFiveFloating(index);
							arraylistsHaveBeenModified = true;
						}
					}
				}

			}
		}catch (Exception e) {

		}
	}

	//@@author Kowshik
	public static int getCorrectIndexFromSearchView(int num) {
		Task temp = Logic.Search.getSearchedTask(num - 1);
		ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
		ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();

		int counter = 1;
		for(Task t : tempUncompletedTasks) {
			if(t.getTaskString().equals(temp.getTaskString())) {
				num = counter;
				break;
			}
			counter++;
		}

		counter++;
		for(Task t : tempFloatingTasks) {
			if(t.getTaskString().equals(temp.getTaskString())) {
				num = counter;
				break;
			}
			counter++;
		}
		return num;
	}

	public static int getCorrectIndexFromDisplayAll(int num) {
		try{
			Task temp = Logic.Crud.getTempTask(num - 1);

			ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
			ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();

			int counter = 1;
			for(Task t : tempUncompletedTasks) {
				if(t.getTaskString().equals(temp.getTaskString())) {
					num = counter;
					break;
				}
				counter++;
			}

			counter++;
			for(Task t : tempFloatingTasks) {
				if(t.getTaskString().equals(temp.getTaskString())) {
					num = counter;
					break;
				}
				counter++;
			}
			return num;
		}catch(Exception e){
			return -1;
		}
	}

	public static int getCorrectIndexWelcomeView(int num) {
		Task temp;
		try {
			temp = Logic.Notification.getSpecificTask(num);
		} catch (IndexOutOfBoundsException e) {
			return INVALID_TASK_INDEX;
		}
		
		ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
		ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();

		int counter = 1;
		for(Task t : tempUncompletedTasks) {
			if(t.getTaskString().equals(temp.getTaskString())) {
				num = counter;
				break;
			}
			counter++;
		}

		counter++;
		for(Task t : tempFloatingTasks) {
			if(t.getTaskString().equals(temp.getTaskString())) {
				num = counter;
				break;
			}
			counter++;
		}
		return num;
	}


	public static void setPriorityCommand() {
		if(Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if(Logic.Head.getLastDisplayArg().equals("all") || Logic.Head.getLastDisplayArg().equals("floating")) {
				setPriorityFromDisplayAllView();
			}
			else {
				setPriorityFromDisplayView();
			}
		} else if(Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s")) {
			setPriorityFromSearchTaskView();
		} else if(Logic.Head.getLastDisplay().equals("")) {
			setPriorityFromDisplayAllView();
		}
		else {
			setPriorityFromDisplayAllView();
		}
	}

	public static void setPriorityFromDisplayView() {
		String s = Parser.Parser.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = Logic.Crud.getTemp();
			if (list.size() == 0) {
				UI.UI.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				UI.UI.printRed(MSG_PRIORITY_FAIL);
			} else {
				num = getCorrectIndexFromDisplayAll(num);
				UI.UI.printYellow("Enter priority");
				String priority = sc.nextLine();
				while((priority.equals("high") != true) && (priority.equals("medium") != true) &&
						priority.equals("low") != true) {
					UI.UI.printRed("Invalid priority entered. Please enter high, medium or low.");
					priority = sc.nextLine();
				}
				UI.UI.printGreen("Issue "+num+" has been set to "+priority);
				Logic.Mark.setPriority(num - 1, priority);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
		}
	}

	public static void setPriorityFromSearchTaskView() {
		try {
			String s = Parser.Parser.getDescription();
			int num = Integer.parseInt(s);
			ArrayList<Task> list = Logic.Search.getSearchedTasks();
			if (list.size() == 0) {
				UI.UI.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				UI.UI.printRed(MSG_PRIORITY_FAIL);
			} else {
				UI.UI.printYellow("Enter priority");
				String priority = sc.nextLine();
				while((priority.equals("high") != true) && (priority.equals("medium") != true) &&
						priority.equals("low") != true) {
					UI.UI.printRed("Invalid priority entered. Please enter high, medium or low.");
					priority = sc.nextLine();
				}
				UI.UI.printGreen("Issue "+num+" has been set to "+priority);
				Task temp = list.get(num - 1);
				ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
				ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();
				int counter = 0;
				for(Task t : tempUncompletedTasks) {
					if(t.getTaskString().equals(temp.getTaskString())) {
						num = counter;
						break;
					}
					counter++;
				}
				counter++;
				for(Task t : tempFloatingTasks) {
					if(t.getTaskString().equals(temp.getTaskString())) {
						num = counter;
						break;
					}
					counter++;
				}
				Logic.Mark.setPriority(num - 1, priority);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
		}
	}

	public static void setPriorityFromDisplayAllView() {
		try {
			String s = Parser.Parser.getDescription();
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getUncompletedTasks();
			ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
			if (list.size() + list2.size() == 0) {
				UI.UI.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				UI.UI.printRed(MSG_PRIORITY_FAIL);
			} else {
				UI.UI.printYellow("Enter priority");
				String priority = sc.nextLine();
				while((priority.equals("high") != true) && (priority.equals("medium") != true) &&
						priority.equals("low") != true) {
					UI.UI.printRed("Invalid priority entered. Please enter high, medium or low.");
					priority = sc.nextLine();
				}
				UI.UI.printGreen("Issue "+num+" has been set to "+priority);
				Logic.Mark.setPriority(num - 1, priority);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
		}
	}

	public static void unmarkCommand() {
		try {
			String s = Parser.Parser.getDescription();
			int num = Integer.parseInt(s);
			// check if user input integer is valid. If it is valid, unmark
			// should
			// work
			ArrayList<Task> list = localStorageObject.getCompletedTasks();
			if (list.size() == 0) {
				UI.UI.printRed(MSG_NO_COMPLETED_TASKS);
			} else if (list.size() < num || num - 1 < 0) {
				UI.UI.printRed(MSG_UNMARK_FAIL);
			} else {
				Task temp = Logic.Crud.getCompletedTask(num - 1);
				Logic.Mark.markTaskAsUncompleted(num - 1);
				UI.UI.printGreen("\""+temp.getIssue()+"\"" + MSG_UNMARK);
				Logic.Crud.displayNearestFiveUnmarkCompleteTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {

		}
	}

	public static void markCommand() {
		String s = Parser.Parser.getDescription();
		if(Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if(Logic.Head.getLastDisplayArg().equals("all") || Logic.Head.getLastDisplayArg().equals("floating")) { // marking from "display" view

				markFromDisplayAllView(s);
			} else {
				markFromDisplayView();
			}
		} else if(Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s")) {
			markFromSearchView();
		} else if(Logic.Head.getLastDisplay().equals("")) {
			int num = getCorrectIndexWelcomeView(Integer.parseInt(s) - 1);
			String index = "" + num;
			markFromDisplayAllView(index);
		}
		else {
			markFromDisplayAllView(s);
		}
	}

	public static void markFromSearchView() {
		String s = Parser.Parser.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = Logic.Search.getSearchedTasks();
			if (list.size() == 0) {
				UI.UI.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				UI.UI.printRed(MSG_MARK_FAIL);
			} else {
				Task temp = list.get(num -1);
				ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
				ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();
				int counter = 0;
				for(Task t : tempUncompletedTasks) {
					if(t.getTaskString().equals(temp.getTaskString())) {
						Logic.Mark.markTaskAsCompleted(counter);
						break;
					}
					counter++;
				}
				for(Task t : tempFloatingTasks) {
					if(t.getTaskString().equals(temp.getTaskString())) {
						Logic.Mark.markTaskAsCompleted(counter);
						break;
					}
					counter++;
				}

				
				UI.UI.printGreen(s + MSG_MARK);
				Logic.Crud.displayNearestFiveCompletedTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {

		}
	}

	public static void markFromDisplayAllView(String s) {

		try { 
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getUncompletedTasks();
			ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
			if (list.size() + list2.size() == 0) {
				UI.UI.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				UI.UI.printRed(MSG_MARK_FAIL);
			} else {
				Task temp = Logic.Crud.getUncompletedTask(num - 1);
				Logic.Mark.markTaskAsCompleted(num - 1);
				
				UI.UI.printGreen("\""+temp.getIssue()+"\"" + MSG_MARK);
				Logic.Crud.displayNearestFiveCompletedTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {

		}
	}

	public static void markFromDisplayView() {
		String s = Parser.Parser.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = Logic.Crud.getTemp();
			if (list.size() == 0) {
				UI.UI.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				UI.UI.printRed(MSG_MARK_FAIL);
			} else {

				Task temp = Logic.Crud.getTempTask(num - 1);
				num = getCorrectIndexFromDisplayAll(num);
				Logic.Mark.markTaskAsCompleted(num - 1);
				
				UI.UI.printGreen(s + MSG_MARK);
				Logic.Crud.displayNearestFiveCompletedTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		}
		catch (Exception e) {
		}
	}

	public static void clearCommand() throws ClassNotFoundException, IOException {
		Logic.Crud.clearTasks();
		UI.UI.printGreen(MSG_CLEAR);
		arraylistsHaveBeenModified = true;
	}

	public static void viewCommand() {
		String s = Parser.Parser.getDescription();
		if(Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if(Logic.Head.getLastDisplayArg().equals("all") || Logic.Head.getLastDisplayArg().equals("floating")) {
				int num = Integer.parseInt(s);
				Logic.Crud.viewIndividualTask(num - 1);
			}
			else {
				viewFromDisplayView();
			}
		} else if(Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s")) {
			viewFromSearchView();
		}
		else if(Logic.Head.getLastDisplay().equals("")) {
			int num = getCorrectIndexWelcomeView(Integer.parseInt(s) - 1);
			Logic.Crud.viewIndividualTask(num - 1);
		}
		else {
			try {
				int num = Integer.parseInt(s);
				Logic.Crud.viewIndividualTask(num - 1);
			} catch (Exception e) {
			}
		}
	}

	public static void viewFromSearchView() {
		String s = Parser.Parser.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = Logic.Search.getSearchedTasks();
			if (list.size() == 0) {
				UI.UI.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				UI.UI.printRed("Invalid index entered");
			} else {
				Task temp = list.get(num - 1);
				ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
				ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();
				int counter = 0;
				for(Task t : tempUncompletedTasks) {
					if(t.getTaskString().equals(temp.getTaskString())) {
						
						Logic.Crud.viewIndividualTask(counter);
						arraylistsHaveBeenModified = true;
						break;
					}
					counter++;
				}

				for(Task t : tempFloatingTasks) {
					if(t.getTaskString().equals(temp.getTaskString())) {
						
						Logic.Crud.viewIndividualTask(counter);
						arraylistsHaveBeenModified = true;
						break;
					}
					counter++;
				}
			}

		}
		catch(Exception e) {
		}
	}

	public static void viewFromDisplayView() {
		String s = Parser.Parser.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = Logic.Crud.getTemp();
			if (list.size() == 0) {
				UI.UI.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				UI.UI.printRed("Wrong index entered");
			} else {
				num = getCorrectIndexFromDisplayAll(num);
				
				Logic.Crud.viewIndividualTask(num - 1);
				arraylistsHaveBeenModified = true;
			}
		}
		catch (Exception e) {

		}
	}

	public static void displayCommand() {
		String s = Parser.Parser.getDescription();
		if (s.equals("completed") || s.equals("c")) {
			Logic.Crud.displayCompletedTasks();
		} else if (s.equals("floating") || s.equals("f")) {
			Logic.Crud.displayFloatingTasks();
		} else if (checkDateObject.checkDateformat(s)) {
			Logic.Crud.displayScheduleForADay(s);
		} else if (s.equals("all")) {
			Logic.Crud.displayUncompletedAndFloatingTasks();
		} else if (s.equals("")){
			Logic.Crud.displayUpcomingTasks();
		} else if (s.equals("today")) {
			Calendar today = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String todayString = df.format(today.getTime());
			Logic.Crud.displayScheduleForADay(todayString);
		} else if (s.equals("tomorrow")) {
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.add(Calendar.DAY_OF_MONTH, 1);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String tomorrowString = df.format(tomorrow.getTime());
			Logic.Crud.displayScheduleForADay(tomorrowString);
		} else if (s.equals("next week") || s.equals("w+1")) {
			Logic.Crud.displayTasksForNextWeek();
		} else if (s.equals("two weeks later") || s.equals("w+2")) {
			Logic.Crud.displayTaksForTwoWeeksLater();
		} else if (s.equals("last week") || s.equals("w -1")) {
			Logic.Crud.displayTasksForLastWeek();
		}  else {
			Logic.Crud.displayByLabel(s);
		}
	}

	public static void deleteCommand() {
		String s = Parser.Parser.getDescription();
		if ((Logic.Head.getLastDisplay().equals("d") == true || Logic.Head.getLastDisplay().equals("display")) == true) {
			if(Logic.Head.getLastDisplayArg().equals("all") || Logic.Head.getLastDisplayArg().equals("floating")) {
				if(s.contains("all") != true) {
					deleteFromDisplayAllView(s);
				}
				else {
					deleteAllRecurringTasks();
				}
			}
			else if(Logic.Head.getLastDisplayArg().equals("completed") || Logic.Head.getLastDisplayArg().equals("c")) {
				if(s.contains("all")!= true) {
					deleteFromDisplayCompletedView();
				}
				else {
					deleteAllRecurringTasks();
				}
			} else { // this is "display" only
				if(s.contains("all") != true) {
					deleteFromDisplayView();
				}else{
					deleteAllRecurringTasks();
				}
			}
		} else if ((Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s"))) {
			// delete from search results
			if(s.contains("all")!=true) {
				deleteFromSearchView();
			}
			else {
				deleteAllRecurringTasks();
			}
		}
		else if (Logic.Head.getLastDisplay().equals("")) {
			if(s.contains("all") != true) {
				int num = getCorrectIndexWelcomeView(Integer.parseInt(s) - 1);
				
				if (num == -1) { // -1 when storage is empty, and user tries to delete immediately after launch
					UI.UI.printRed(MSG_EMPTY);
					return;
				}
				
				String index = "" + num;
				deleteFromDisplayAllView(index);
			}else{
				deleteAllRecurringTasks();
			}
		}
		else {
			deleteFromDisplayAllView(s);
		}
	}

	public static void deleteFromDisplayView() {
		String s = Parser.Parser.getDescription();
		String[] splitInput = s.split(" ");
		try{
			int num;
			if (splitInput.length == 2) {
				num = Integer.parseInt(splitInput[1]);
			} else {
				num = Integer.parseInt(splitInput[0]);
			}

			ArrayList<Task> list = Logic.Crud.getTemp();
			Task deleted = list.get(num - 1);
			issue = deleted.getIssue();
			
			try {
				Logic.Crud.deleteTask(num - 1, 5);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			UI.UI.printGreen("\"" + issue + "\" " + MSG_DELETE);
			Logic.Crud.displayNearestFiveDeleteUncompleteTaskList(num - 1);
			arraylistsHaveBeenModified = true;
		}catch(Exception e){
			UI.UI.printRed(MSG_INVALID);
		}
	}

	public static void deleteFromDisplayCompletedView() {
		String s = Parser.Parser.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getCompletedTasks();
			if (list.size() == 0) {
				UI.UI.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				// handle indexOutofBoundException
				UI.UI.printRed(MSG_TASK_DES_NOT_EXIST);
			} else {
				Task deleted = list.get(num - 1);
				issue = deleted.getIssue();
				Logic.Crud.deleteTask(num - 1, 2);
				UI.UI.printGreen("\"" + issue + "\" " + MSG_DELETE);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			UI.UI.printRed(MSG_INVALID);
		}
	}

	public static void deleteFromSearchView() {
		String s = Parser.Parser.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = Logic.Search.getSearchedTasks();
			if (list.size() == 0) {
				UI.UI.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				// handle indexOutofBoundException
				UI.UI.printRed(MSG_TASK_DES_NOT_EXIST);
			} else {
				Task deleted = list.get(num - 1);
				issue = deleted.getIssue();
				Logic.Crud.deleteTask(num - 1, 3);
				
				UI.UI.printGreen("\"" + issue + "\" " + MSG_DELETE);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			UI.UI.printRed(MSG_INVALID);
		}
	}

	public static void deleteFromDisplayAllView(String s) {
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getUncompletedTasks();
			ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
			if (list.size() + list2.size() == 0) {
				UI.UI.printRed(MSG_EMPTY);
			} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
				// handle indexOutofBoundException
				UI.UI.printRed(MSG_TASK_DES_NOT_EXIST);
			} else {
				if ((num - 1) < list.size()) {
					Task deleted = list.get(num - 1);
					issue = deleted.getIssue();
					Logic.Crud.deleteTask(num - 1, 1);
					
					UI.UI.printGreen("\"" + issue + "\" " + MSG_DELETE);
					Logic.Crud.displayNearestFiveDeleteUncompleteTaskList(num - 1);
					arraylistsHaveBeenModified = true;
				} else {
					Task deleted = list2.get(num - list.size() - 1);
					issue = deleted.getIssue();
					Logic.Crud.deleteTask(num - 1, 1);
					
					UI.UI.printGreen("\"" + issue + "\" " + MSG_DELETE);
					Logic.Crud.displayNearestFiveDeleteFloatingTask(num - 1);
					arraylistsHaveBeenModified = true;
				}
			}
		} catch (Exception e) {
			UI.UI.printRed(MSG_INVALID);
		}
	}

	public static void deleteAllRecurringTasks() {
		String s = Parser.Parser.getDescription();
		try {
			String[] tmp = s.split(" ");
			if (s.contains("all")) {
				int num = Integer.parseInt(tmp[1]);
				boolean isDeleted  = delAllRecurringTask(num - 1);
				if (isDeleted) {
					UI.UI.printGreen("All instances of Task " + num +  " have been deleted");
				} else {
					UI.UI.printRed("Not a recurring tasks. Enter delete/d followed by index to delete this task");
				}
				arraylistsHaveBeenModified = isDeleted;
			} else {
				int num = Integer.parseInt(s);
				ArrayList<Task> list = localStorageObject.getUncompletedTasks();
				ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					UI.UI.printRed(MSG_EMPTY);
				} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
					// handle indexOutofBoundException
					UI.UI.printRed(MSG_TASK_DES_NOT_EXIST);
				} else {
					if ((num - 1) < list.size()) {
						Task deleted = list.get(num - 1);
						issue = deleted.getIssue();
						
						Logic.Crud.deleteTask(num - 1, 1);
						UI.UI.printGreen("\"" + issue + "\" " + MSG_DELETE);
						Logic.Crud.displayNearestFiveDeleteUncompleteTaskList(num - 1);
						arraylistsHaveBeenModified = true;
					} else {
						Task deleted = list2.get(num - list.size() - 1);
						issue = deleted.getIssue();
						Logic.Crud.deleteTask(num - 1, 1);
						
						UI.UI.printGreen("\"" + issue + "\" " + MSG_DELETE);
						Logic.Crud.displayNearestFiveDeleteFloatingTask(num - 1);
						arraylistsHaveBeenModified = true;
					}
				}
			}
		} catch (Exception e) {
			UI.UI.printRed(MSG_INVALID);
		}
	}


	// @@author Jung Kai
	/**
	 * method that convert String[] to String
	 * 
	 * @param arr
	 * @return String
	 */
	public static String arrayToString(String[] arr) {
		String temp = Arrays.toString(arr);
		temp = temp.substring(1, temp.length() - 1).replaceAll(", ", " ");
		return temp;
	}

	/**
	 * method that return the description of task from the String[],position of
	 * start keyword , position of end keyword,boolean value of startTime and
	 * boolean value of endTime
	 * 
	 * @param arr
	 * @param start
	 * @param end
	 * @param startTime
	 * @param endTime
	 * @return String
	 */
	public static String getIssue(String[] arr, int start, int end, boolean startTime, boolean endTime) {
		if (start == -1) {// no start date
			if (endTime) { // has end time
				int size = arr.length - 3;
				String[] temp = new String[size];
				int i;

				for (i = 0; i < size; i++) {
					if (i >= end) {
						temp[i] = arr[i + 3];

					} else {
						temp[i] = arr[i];
					}
				}
				return arrayToString(temp);
			} else {// no end time
				int size = arr.length - 2;
				String[] temp = new String[size];
				int i;
				for (i = 0; i < size; i++) {
					if (i >= end) {
						temp[i] = arr[i + 2];

					} else {
						temp[i] = arr[i];
					}
				}
				return arrayToString(temp);
			}

		} else if (end == -1) { // no end date
			if (startTime) {// has start time
				int size = arr.length - 3;
				String[] temp = new String[size];
				int i;
				for (i = 0; i < size; i++) {
					if (i >= start) {
						temp[i] = arr[i + 3];

					} else {
						temp[i] = arr[i];
					}
				}
				return arrayToString(temp);
			} else {// no start time
				int size = arr.length - 2;
				String[] temp = new String[size];
				int i;
				for (i = 0; i < size; i++) {
					if (i >= start) {
						temp[i] = arr[i + 2];

					} else {
						temp[i] = arr[i];
					}
				}
				return arrayToString(temp);
			}

		} else {// has both start date and end date
			if (startTime == true && endTime == true) { // has start time and
				// end time
				int size = arr.length - 6;

				String[] temp = new String[size];
				int i;
				for (i = 0; i < size; i++) {
					if (i >= start && i < end) {
						temp[i] = arr[i + 3];
					} else if (i >= end) {
						temp[i] = arr[i + 3];

					} else {
						temp[i] = arr[i];
					}
				}
				return arrayToString(temp);
			} else if (startTime == false && endTime == true) { // has only end
				// time
				int size = arr.length - 5;
				String[] temp = new String[size];
				int i;
				for (i = 0; i < size; i++) {
					if (i >= start && i < end) {
						temp[i] = arr[i + 2];
					} else if (i >= end) {
						temp[i] = arr[i + 3];

					} else {
						temp[i] = arr[i];
					}
				}
				return arrayToString(temp);
			} else if (startTime == true && endTime == false) {// has only start
				// time
				int size = arr.length - 5;
				String[] temp = new String[size];
				int i;
				for (i = 0; i < size; i++) {
					if (i >= start && i < end) {
						temp[i] = arr[i + 3];
					} else if (i >= end) {
						temp[i] = arr[i + 2];

					} else {
						temp[i] = arr[i];
					}
				}
				return arrayToString(temp);
			} else { // has no start time and no end time

				int size = arr.length - 4;
				String[] temp = new String[size];
				int i;
				for (i = 0; i < size; i++) {
					if (i >= start && i < end) {
						temp[i] = arr[i + 2];
					} else if (i >= end) {
						temp[i] = arr[i + 2];

					} else {
						temp[i] = arr[i];
					}
				}
				return arrayToString(temp);
			}
		}
	}/**
	 * method that return the index of first keyword present in the String[] and
	 * return -1 if no keyword is present
	 * 
	 * @param arr
	 * @return Integer
	 */
	public static int getIndexOfKey(String[] arr) {
		int idx = -1;
		for (int j = 0; j < arr.length; j++) {
			for (int i = 0; i < key.length; i++) {
				if (arr[j].equals(key[i])) {
					idx = j;
				}
			}
		}
		return idx;
	}

	/**
	 * Method that return the index of "from" from the input String[] and return
	 * -1 if no starting index is present
	 * 
	 * @param arr
	 * @return Integer
	 */
	public static int getStartingIndex(String[] arr) {
		int idx = -1;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals("from") || arr[i].equals("on")) {
				idx = i;
			}

		}
		return idx;
	}

	/**
	 * method that return a boolean value to indicate if input string[] contains
	 * starting time
	 * 
	 * @param arr
	 * @return boolean
	 */
	public static boolean hasStartTime(String[] arr) {
		boolean containTime = true;
		int start = getStartingIndex(arr);
		// if date is the last argument => no time
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
	 * method that return a boolean value to indicate if input String[] contains
	 * ending time
	 * 
	 * @param arr
	 * @return boolean
	 */
	public static boolean hasEndTime(String[] arr) {
		boolean containTime = true;
		int end = getIndexOfKey(arr);
		// if date is the last argument => no time
		if (end + 2 >= arr.length) {
			containTime = false;

		} else {
			if (!checkDateObject.checkTimeformat(arr[end + 2])) {
				containTime = false;
			}
		}
		return containTime;
	}

	// return existing issue processed by the parser
	public static String getIssue() {
		return issue;
	}

	// return existing date processed by the parser
	public static String getDate() {
		return date;
	}


	/**
	 * method that return index of week, if it is not a week day, -1 will be
	 * returned
	 * 
	 * @param s
	 * @return int
	 */
	public static int getIndexOfWeek(String s) {
		int idx = -1;
		for (int i = 0; i < 7; i++) {
			if (s.equals(week[i])) {
				idx = i;
				break;
			}
		}
		return idx;
	}

	/**
	 * method that match a weekday to its date using the index
	 * 
	 * @param n
	 * @return String
	 */
	public static String matchDate(int n) {
		String output;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String today = dateFormat.format(date);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		int diff;
		if (n + 1 == day) {
			output = today;
		} else if (n + 1 > day) {

			diff = n + 1 - day;
			output = processDate(today, diff);
		} else {
			int remaining = 7 - day;
			diff = remaining + 1 + (n + 1);
			output = processDate(today, diff);
		}
		return output;

	}

	/**
	 * method that process Date for recurring tasks based on the date and number
	 * of recurring tasks calculated
	 * 
	 * @param s
	 * @param n
	 * @return
	 */
	public static String processDate(String s, int n) {
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
	 * method that delete all recurring task at index n from recurring tasks and
	 * uncompleted tasks in storage
	 * 
	 * @param n
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static boolean delAllRecurringTask(int n) throws ClassNotFoundException, IOException {
		ArrayList<Task> list = localStorageObject.getUncompletedTasks();
		Task deleted = list.get(n);

		String id = deleted.getId();
		if (id.equals("")) {// if the task is not recurring task
			return false;
		} else {
			for (int i = 0; i < list.size(); i++) {// delete from uncompleted tasks
				Task task = list.get(i);
				if (id.equals(task.getId())) {
					Logic.Crud.deleteTask(i, 1);
					i = -1;// loop again
				}
			}
			return true;
		}
	}

	/** 
	 * methods that support the editing of recurring task at index n
	 *  
	 * @param n
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */

	public static void editRecurringTask(int n) throws ClassNotFoundException, IOException {
		Task replaced = localStorageObject.getUncompletedTask(n);

		if (replaced.getId().equals("")) { // if the task at the user-entered index is not a recurring task. stop & inform user
			UI.UI.printRed(MSG_EDIT_NOT_RECURRING_TASK_HEAD + (n + 1) + MSG_EDIT_NOT_RECURRING_TASK_TAIL);
			return;
		}

		Logic.Crud.copyTask(replaced);
		UI.UI.printRed("Enter new description and deadline of recurring tasks");
		String in = sc.nextLine();
		in = Natty.getInstance().parseEditString(in);
		// get index of key
		String[] s2= in.split("`");

		if(s2.length==2){			
			String[] temp=s2[1].split(" ");
			int start = getStartingIndex(temp); // start has value of -1
			// if
			// it
			// has no start date
			int end = getIndexOfKey(temp);

			issue = s2[0];
			if (start == -1 && end != -1) {// no start date but has
				// end
				// date
				startDate = "-";
				startTime = "-";
				// read date & time
				date = temp[end + 1];
				dateIn = date;
				if (hasEndTime(temp)) {// check if contain end time
					time = temp[end + 2];
					time = time.replaceAll(":", "/");
					dateIn = dateIn + "/" + time;
				} else {
					time = "-";
				}

				if (!checkDateObject.checkDateformat(date)) {
					UI.UI.printRed(MSG_WRONG_DATE);
				} else {
					UI.UI.printRed(PROMPT_RECURRING);
					try {
						String in2 = sc.nextLine();
						String[] tmp = in2.split(" ");
						String freq = tmp[0];
						String last = tmp[1];

						int frequency = Integer.parseInt(freq);

						Task task = new Task(issue,dateIn,in,false,frequency,last);
						checkDateAndAdd(task);
						arraylistsHaveBeenModified = true;
						delAllRecurringTask(n);
						UI.UI.printGreen("All instances of Task " + (n+1) +" has been edited and saved");
					} catch (Exception e) {
						UI.UI.printRed(MSG_INVALID);
						arraylistsHaveBeenModified = false;
					}
				}
			} else if (start != -1 && end == -1) {// has start date
				// but
				// no end date
				date = "-";
				time = "-";
				startDate = temp[start + 1];
				int idx = getIndexOfWeek(startDate);
				if (idx != -1) {
					startDate = matchDate(idx);
				}
				dateIn2 = startDate;

				if (hasStartTime(temp)) {
					startTime = temp[start + 2];
					startTime = startTime.replaceAll(":", "/");
					dateIn2 = dateIn2 + "/" + startTime;
				} else {
					startTime = "-";
				}
				if (!checkDateObject.checkDateformat(startDate)) {
					UI.UI.printRed(MSG_WRONG_DATE);
				} else {
					UI.UI.printRed(PROMPT_RECURRING);
					try {
						String in2 = sc.nextLine();
						String[] tmp = in2.split(" ");
						String freq = tmp[0];
						String last = tmp[1];

						int frequency = Integer.parseInt(freq);

						Task task = new Task(issue,dateIn,in,true,frequency,last);
						checkDateAndAdd(task);
						arraylistsHaveBeenModified = true;
						delAllRecurringTask(n);
						UI.UI.printGreen("All instances of Task " + (n+1) +" has been edited and saved");

					} catch (Exception e) {
						UI.UI.printRed(MSG_INVALID);
						arraylistsHaveBeenModified = false;
					}

				}
			} else { // has both start date and end date
				startDate = temp[start + 1];
				date = temp[end + 1];
				int idx = getIndexOfWeek(startDate);
				int idx2 = getIndexOfWeek(date);
				if (idx != -1) {
					startDate = matchDate(idx);
				}
				if (idx2 != -1) {
					date = matchDate(idx2);
				}
				dateIn = date;
				dateIn2 = startDate;
				if (hasStartTime(temp)) {
					startTime = temp[start + 2];
					startTime = startTime.replaceAll(":", "/");
					dateIn2 = dateIn2 + "/" + startTime;
				} else {
					startTime = "-";
				}
				if (hasEndTime(temp)) {
					time = temp[end + 2];
					time = time.replaceAll(":", "/");
					dateIn = dateIn + "/" + time;

				} else {
					time = "-";
				}
				if (!checkDateObject.checkDateformat(startDate) && !checkDateObject.checkDateformat(date)) {
					UI.UI.printRed(MSG_WRONG_DATE);
				} else {
					UI.UI.printRed(PROMPT_RECURRING);
					try {
						String in2 = sc.nextLine();
						String[] tmp = in2.split(" ");
						String freq = tmp[0];
						String last = tmp[1];

						int frequency = Integer.parseInt(freq);
						Task task = new Task(issue,dateIn2,dateIn,in,frequency,last);
						checkDateAndAdd(task);
						arraylistsHaveBeenModified = true;
						delAllRecurringTask(n);
						UI.UI.printGreen("All instances of Task " + (n+1) +" has been edited and saved");

					} catch (Exception e) {
						UI.UI.printRed(MSG_INVALID);
						arraylistsHaveBeenModified = false;
					}
				}
			}
		}
	}
	public static void checkDateAndAdd(Task task) {
		try {
			String id = task.getId();
			String ed = task.getDateCompare();

			boolean expired = isExpired(ed, task.getLastDate());

			while (true) {
				if (expired) {//If task is not within display time frame or when task expired
					break;
				}
				localStorageObject.addToUncompletedTasks(task);
				String newED = processDate(ed, task.getFrequency());
				if (task.getStartDate() == null) {//no start date
					
					task = new Task(task.getIssue(), newED, task.getMsg(), false, task.getFrequency(),
							task.getLastDate());
					task.setID(id);

				} else if (task.getEndDate() == null) {
					task = new Task(task.getIssue(), newED, task.getMsg(), true, task.getFrequency(),
							task.getLastDate());
					task.setID(id);
				}
				else {// has start date and end date
					task = new Task(task.getIssue(),task.getFixedStartDateString(),newED,task.getMsg(),task.getFrequency()
							,task.getLastDate());
					task.setID(id);
				}

				ed = task.getDateCompare();

				expired = (isExpired(ed,task.getLastDate()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isExpired(String currentRecurDate, String recurDeadline) {
		String[] splitCurrentRecurDate = currentRecurDate.split("/");
		String[] splitRecurDeadline = recurDeadline.split("/");
		if (Integer.parseInt(splitRecurDeadline[2]) < Integer.parseInt(splitCurrentRecurDate[2])) { // end year < current year
			return true;
		} else if (Integer.parseInt(splitRecurDeadline[2]) > Integer.parseInt(splitCurrentRecurDate[2])) { // end year > current year
			return false;
		} else {	// end year == current year
			if (Integer.parseInt(splitRecurDeadline[1]) > Integer.parseInt(splitCurrentRecurDate[1]) ) { // end month > current month 
				return false;
			} 	if (Integer.parseInt(splitRecurDeadline[1]) < Integer.parseInt(splitCurrentRecurDate[1]) ) { // end month < today 
				return true;
			} else { // same year & month
				if (Integer.parseInt(splitRecurDeadline[0]) >= Integer.parseInt(splitCurrentRecurDate[0])) { // valid date
					return false;
				} else {
					return true;
				}
			}
		}
	}
}