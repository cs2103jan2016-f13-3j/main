//@@author Jung Kai
package Parser;

import java.util.*;

import javax.xml.ws.handler.LogicalHandler;

import java.time.YearMonth;
import Logic.Head;
import Logic.Undo;
import Storage.localStorage;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.fusesource.jansi.AnsiConsole;
import Task.Task;

public class Parser {
	private static boolean arraylistsHaveBeenModified;
	private static String startDate, date, issue, startTime, time, input, dateIn, dateIn2;
	private static Scanner sc = new Scanner(System.in);
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
	private static final String MSG_SORT = "All items are sorted in alphabetical order";
	private static final String MSG_EDIT = " is edited and saved";
	private static final String MSG_MARK = " is marked as completed";
	private static final String MSG_UNMARK = " is marked as uncompleted";
	private static final String MSG_INVALID = "Invalid inputs! Please try again";
	private static final String MSG_WRONG_DEADLINE = "Please enter deadline in dd/mm/yyyy format or enter - for no deadline";
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
	
	private static final String PROMPT_DATE = "Enter date in dd/mm/yyyy";
	private static final String PROMPT_DEADLINE = "Enter deadline in dd/mm/yyyy or enter - for no deadline";
	private static final String PROMPT_EDIT = "Insert new description and deadline for the task.";	
	private static final String PROMPT_ISSUE = "Enter keyword contained in the issue";
	private static final String PROMPT_SEARCH = "Press 1 to search by issue, 2 to search by date";

	/**
	 * method that simulate command line interface that will responds to user's
	 * inputs
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 *             return boolean
	 */
	public static boolean run(String cmd, String description) throws IOException, ClassNotFoundException {
		// process commands

		// take "snapshots" of current storage state
		Undo.getInstance().copyCurrentTasksState();
		boolean modificationsWereMade = parseCommands(cmd, description);
		if (!description.equals("")) {
			cmd += " " + description;
		}
		if (modificationsWereMade) {
			// store the "snapshots" into Undo class if arraylists have been
			// modified
			Undo.getInstance().storePreviousState(cmd);
			Undo.getInstance().clearRedoCommands(); // if valid command executed
			// and arraylists modified,
			// remove all stored redo
			// commands
		}
		return modificationsWereMade;
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
	public static boolean parseCommands(String option, String s) throws IOException, ClassNotFoundException {
		arraylistsHaveBeenModified = false;
		if (option.equals("add") || option.equals("a") || option.equals("+")) {
			addCommand(s);
		} else if (option.equals("delete") || option.equals("-")) {
			deleteCommand(s);
		} else if (option.equals("display") || option.equals("d")) {
			displayCommand(s);
		} else if (option.equals("view") || option.equals("v")) {
			viewCommand(s);
		} else if (option.equals("clear") || option.equals("c")) {
			clearCommand();
		} else if (option.equals("sort")) {
			sortCommand(s);
		} else if (option.equals("search") || option.equals("s")) {
			Logic.Search.searchTasksByKeyword(s);
		} else if (option.equals("mark") || option.equals("m")) {
			markCommand(s);
		} else if (option.equals("unmark") || option.equals("um")) {
			unmarkCommand(s);
		} else if (option.equals("edit") || option.equals("e")) {
			editCommand(s);
		} else if (option.equals("priority") || option.equals("p")) {
			if(s.contains("all")) {
				setAllRecurringTasksPriorityCommand(s);
			} else {
				setPriorityCommand(s);
			}
		} else if (option.equals("history")) {
			String pastCommands = Undo.getInstance().viewPastCommands();
			UI.ui.printYellow(pastCommands);
		} else if (option.equals("future")) {
			String possibleRedoCommands = Undo.getInstance().viewRedoCommands();
			UI.ui.printYellow(possibleRedoCommands);
		} else if (option.equals("undo")) {
			undoCommand(s);
		} else if (option.equals("redo")) {
			redoCommand(s);
		} else if (option.equals("exit")) {
			UI.ui.printGreen("Bye!");
			AnsiConsole.systemUninstall();
			Logic.crud.exit();
		} else if (option.equals("help")) {
			Logic.Help.printHelpMenu();
		} else if (option.equals("dir")) {
			changeDirectoryCommand(s);
		} else if (option.equals("label")) {
			setLabelCommand(s);
		} else {
			UI.ui.printRed(MSG_INVALID);
		}
		return arraylistsHaveBeenModified;
	}

	private static void setAllRecurringTasksPriorityCommand(String s) {
		String [] tmp = s.split(" ");
		String idx = tmp[1];
		int num = Integer.parseInt(idx);


		ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();

		if (list.size() == 0) {
			UI.ui.printRed(MSG_EMPTY);
		} else if (list.size()  < num || num - 1 < 0) {
			UI.ui.printRed(MSG_PRIORITY_FAIL);
		} else {
			UI.ui.printYellow("Enter priority");
			String priority = sc.nextLine();
			Logic.Mark.setRecurringTasksPriority(num - 1, priority);
			arraylistsHaveBeenModified = true;
		}

	}


	public static void 	addCommand(String s) throws IOException, ClassNotFoundException {
		boolean isAdded;
		if (s.equals("")) {
		} else {
			// get index of key
			if (s.contains("`")) {
				int ind = s.indexOf("`");
				String[] s2 = s.split(" ` ");
				if (s2.length == 2) {
					String[] temp = s2[1].split(" ");
					String r = s.substring(0, ind) + s.substring(ind + 2);
					int start = getStartingIndex(temp); // start has value of -1
					// if
					// it
					// has no start date
					int end = getIndexOfKey(temp); // end has value of -1 if it
					// has
					// no end date
					if (end < start) {// { "by", "at", "on", "during", "before",
						// "to" } is before "from"
						end = -1;// no end date
					}

					boolean toRecurred = (temp[temp.length - 1].equals("r")); // return
					if (!toRecurred) {
						if (start == -1 && end != -1) {// no start date but has end date
							startDate = "-";
							startTime = "-";
							// read date & time
							date = temp[end + 1];
							int idx = getIndexOfWeek(date);
							if (idx != -1) {
								date = matchDate(idx);
							}
							dateIn = date;
							if (hasEndTime(temp)) {// check if contain end time
								time = temp[end + 2];
								time = time.replaceAll(":", "/");
								dateIn = dateIn + "/" + time;
							} else {
								time = "-";
							}

							if (!Logic.checkDate.checkDateformat(date)) {
								UI.ui.printRed(MSG_WRONG_DATE);
							} else {
								// get issue
								issue = s2[0];
								isAdded = Logic.crud.addTaskWithEndDate(issue, dateIn, s);
								if (isAdded) {
									Logic.Sort.sortTasksChronologically();
									int index = Logic.crud.uncompletedTaskIndexWithEndDate(issue, dateIn, s);
									UI.ui.printGreen("\"" + issue + "\" " + MSG_ADD);
									arraylistsHaveBeenModified = true;
									Logic.crud.displayNearestFiveUncompleted(index);
								} else {
									UI.ui.printRed(MSG_DUPLICATE_ADD);
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
							if (!Logic.checkDate.checkDateformat(startDate)) {
								UI.ui.printRed(MSG_WRONG_DATE);
							} else {
								// get issue
								issue = s2[0];
								// isAdded =
								// Logic.crud.addTask(issue,startDate,startTime,endDate,endTime);
								isAdded = Logic.crud.addTaskWithStartDate(issue, dateIn2, s);
								if (isAdded) {
									Logic.Sort.sortTasksChronologically();
									int index = Logic.crud.uncompletedTaskIndexWithStartDate(issue, dateIn2, s);
									UI.ui.printGreen("\"" + issue + "\" " + MSG_ADD);
									Logic.crud.displayNearestFiveUncompleted(index);
									arraylistsHaveBeenModified = true;
								} else {
									UI.ui.printRed(MSG_DUPLICATE_ADD);
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
							if (!Logic.checkDate.checkDateformat(startDate) && !Logic.checkDate.checkDateformat(date)) {
								UI.ui.printRed(MSG_WRONG_DATE);
							} else {
								// get issue
								issue = s2[0];

								// isAdded =
								// Logic.crud.addTask(issue,startDate,startTime,endDate,endTime);
								System.out.println(dateIn);
								isAdded = Logic.crud.addTaskWithBothDates(issue, dateIn2, dateIn, s);
								if (isAdded) {
									Logic.Sort.sortTasksChronologically();
									int index = Logic.crud.uncompletedTaskIndexWithBothDates(issue, dateIn2, dateIn, s);
									UI.ui.printGreen("\"" + issue + "\" " + MSG_ADD);
									Logic.crud.displayNearestFiveUncompleted(index);
									arraylistsHaveBeenModified = true;
								} else {
									UI.ui.printRed(MSG_DUPLICATE_ADD);
								}
							}
						}
					} else { // for recurring tasks
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

							if (!Logic.checkDate.checkDateformat(date)) {
								UI.ui.printRed(MSG_WRONG_DATE);
							} else {
								UI.ui.printRed("Enter \"<frequency> <last date>\"");
								String in = sc.nextLine();
								String[] tmp = in.split(" ");
								String freq = tmp[0];
								String last = tmp[1];
								//	String before = tmp[2];

								int frequency = Integer.parseInt(freq);						

								Task task = new Task(issue,dateIn,s,true,frequency,last);
								checkDateAndAdd(task);
								 UI.ui.printGreen("\"" + task.getIssue() + "\"" +  " is added to the task list. (recurs every " + freq + " days)");
								arraylistsHaveBeenModified = true;

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
							if (!Logic.checkDate.checkDateformat(startDate)) {
								UI.ui.printRed(MSG_WRONG_DATE);
							} else {
								UI.ui.printRed("Enter \"<frequency> <last date>\"");
								String in = sc.nextLine();
								String[] tmp = in.split(" ");
								String freq = tmp[0];
								String last = tmp[1];
								//	String before = tmp[2];

								int frequency = Integer.parseInt(freq);

								Task task = new Task(issue,dateIn2,s,true,frequency,last);
								checkDateAndAdd(task);
								 UI.ui.printGreen("\"" + task.getIssue() + "\"" +  " is added to the task list. (recurs every " + freq + " days)");
								arraylistsHaveBeenModified = true;

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
							if (!Logic.checkDate.checkDateformat(startDate) && !Logic.checkDate.checkDateformat(date)) {
								UI.ui.printRed(MSG_WRONG_DATE);
							} else {
								UI.ui.printRed("Enter \"<frequency> <last date>\"");
								String in = sc.nextLine();
								String[] tmp = in.split(" ");
								String freq = tmp[0];
								String last = tmp[1];
								//	String before = tmp[2];

								int frequency = Integer.parseInt(freq);
			

								Task task = new Task(issue,dateIn2,dateIn,s,frequency,last);
								 UI.ui.printGreen("\"" + task.getIssue() + "\"" +  " is added to the task list. (recurs every " + freq + " days)");
								checkDateAndAdd(task);
								arraylistsHaveBeenModified = true;

							}
						}
					}

				}

			} else {
				isAdded = Logic.crud.addTask(s);
				if (isAdded) {
					Logic.Sort.sortTasksChronologically();
					int index = Logic.crud.uncompletedTaskIndexWithNoDate(s);
					UI.ui.printGreen("\"" + s + "\" " + MSG_ADD);
					Logic.crud.displayNearestFiveFloating(index);
					arraylistsHaveBeenModified = true;
				} else {
					UI.ui.printRed(MSG_DUPLICATE_ADD);
				}

			}

		}
	}

	// @@author Kowshik
	public static void setLabelCommand(String s) {
		try {
			int num = Integer.parseInt(s);

			ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
			ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();

			if (list.size() + list2.size() == 0) {
				UI.ui.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				UI.ui.printRed(MSG_PRIORITY_FAIL);
			} else {
				UI.ui.print("Enter label");
				String label = sc.nextLine();
				Logic.crud.addLabelToTask(num - 1, label);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {

		}
	}

	// @@author Jie Wei
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

	// @@author Kowshik
	public static void setPriorityCommand(String s) {
		try {
			int num = Integer.parseInt(s);
			// check if user input integer is valid. If it is valid, edit
			// should
			// work
			ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
			ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
			if (list.size() + list2.size() == 0) {
				UI.ui.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				UI.ui.printRed(MSG_PRIORITY_FAIL);
			} else {
				UI.ui.printYellow("Enter priority");
				String priority = sc.nextLine();
				Logic.Mark.setPriority(num - 1, priority);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
		}
	}

	// @@author Jung Kai
	public static void editCommand(String s) {
		try {
			String[] tmp = s.split(" ");
			if (s.contains("all")) {
				int num = Integer.parseInt(tmp[1]);
				editRecurringTask(num - 1);
			} else {
				int num = Integer.parseInt(s);
				// check if user input integer is valid. If it is valid, edit
				// should
				// work
				ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
				ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					UI.ui.printRed(MSG_EMPTY);
				} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
					UI.ui.printRed(MSG_EDIT_FAIL);
				} else {
					UI.ui.printGreen(PROMPT_EDIT);
					Logic.crud.copyEditingTask(num);
					input = sc.nextLine();
					input = Natty.getInstance().parseEditString(input);
					if (input.contains("`")) {
						int ind = input.indexOf("`");
						String[] s2 = input.split(" ` ");

						if (s2.length == 2) {
							String[] temp = s2[1].split(" ");
							String r = input.substring(0, ind) + input.substring(ind + 2);
							int start = getStartingIndex(temp);
							int end = getIndexOfKey(temp); // end has value of
							// -1 if it has
							// no end date
							if (end < start) {// { "by", "at", "on", "during",
								// "before", "to" } is before
								// "from"
								end = -1;// no end date
							}
							if (start == -1 && end != -1) {// no start date but
								// has end
								// date
								startDate = "-";
								startTime = "-";
								// read date & time
								date = temp[end + 1];
								dateIn = date;

								if (hasEndTime(temp)) {// check if contain end
									// time
									// something is wrong here
									time = temp[end + 2];
									time = time.replaceAll(":", "/");
									dateIn = dateIn + "/" + time;

								} else {
									time = "-";
								}
								if (!Logic.checkDate.checkDateformat(date)) {
									UI.ui.printRed(MSG_WRONG_DATE);
								} else {
									// get issue

									issue = s2[0];
									System.out.println(issue);
									// Logic.crud.editTask(num-1,issue,startDate,startTime,endDate,endTime,input)
									// (to be implemented)
									Logic.crud.editTaskWithEndDate(issue, dateIn, input, num - 1);
									Logic.Sort.sortTasksChronologically();
									UI.ui.eraseScreen();
									int index = Logic.crud.uncompletedTaskIndexWithEndDate(issue, dateIn, input);
									UI.ui.printGreen("Task number " + num + MSG_EDIT);
									Logic.crud.displayNearestFiveUncompleted(index);
									arraylistsHaveBeenModified = true;
								}
							} else if (start != -1 && end == -1) {// has start
								// date but
								// no end date
								date = "-";
								time = "-";
								startDate = temp[start + 1];
								dateIn2 = startDate;
								if (hasStartTime(temp)) {
									startTime = temp[start + 2];
									startTime = startTime.replaceAll(":", "/");
									dateIn2 = dateIn2 + "/" + startTime;
								} else {
									startTime = "-";
								}
								if (!Logic.checkDate.checkDateformat(startDate)) {
									UI.ui.printRed(MSG_WRONG_DATE);
								} else {
									// get issue
									issue = s2[0];
									// Logic.crud.editTask(issue,startDate,startTime,endDate,endTime,input);
									Logic.crud.editTaskWithStartDate(issue, dateIn2, input, num - 1);
									Logic.Sort.sortTasksChronologically();
									int index = Logic.crud.uncompletedTaskIndexWithStartDate(issue, dateIn2, input);
									UI.ui.eraseScreen();
									UI.ui.printGreen("Task number " + num + MSG_EDIT);
									Logic.crud.displayNearestFiveUncompleted(index);
									arraylistsHaveBeenModified = true;
								}
							} else { // has both start date and end date
								startDate = temp[start + 1];
								date = temp[end + 1];
								dateIn = date;
								dateIn2 = startDate;

								if (hasStartTime(temp)) {
									startTime = temp[start + 2];
									startTime = startTime.replaceAll(":", "/");
									dateIn2 = dateIn2 + "/" + startTime;
								}
								if (hasEndTime(temp)) {
									time = temp[end + 2];
									time = time.replaceAll(":", "/");
									dateIn = dateIn + "/" + time;

								}
								if (!Logic.checkDate.checkDateformat(startDate)
										&& !Logic.checkDate.checkDateformat(date)) {
									UI.ui.printRed(MSG_WRONG_DATE);
								} else {
									// get issue
									issue = s2[0];
									// Logic.crud.addTask(issue,startDate,startTime,endDate,endTime);
									Logic.crud.editTaskWithBothDates(issue, dateIn2, dateIn, input, num - 1);
									UI.ui.eraseScreen();
									UI.ui.printGreen("Task number " + num + MSG_EDIT);
									Logic.Sort.sortTasksChronologically();
									int index = Logic.crud.uncompletedTaskIndexWithBothDates(issue, dateIn2, dateIn,
											input);
									Logic.crud.displayNearestFiveUncompleted(index);
									arraylistsHaveBeenModified = true;
								}
							}
						}
					} else {// no end date and no
						// start date

						Logic.crud.editTaskWithNoDate(input, input, num - 1);
						int index = Logic.crud.uncompletedTaskIndexWithNoDate(input);
						UI.ui.printGreen("Task number " + num + MSG_EDIT);
						Logic.crud.displayNearestFiveFloating(index);
						arraylistsHaveBeenModified = true;
					}
				}
			}
		} catch (Exception e) {

		}
	}

	// @@author Kowshik
	public static void unmarkCommand(String s) {
		try {
			int num = Integer.parseInt(s);
			// check if user input integer is valid. If it is valid, unmark
			// should
			// work
			ArrayList<Task> list = Storage.localStorage.getCompletedTasks();
			if (list.size() == 0) {
				UI.ui.printRed(MSG_NO_COMPLETED_TASKS);
			} else if (list.size() < num || num - 1 < 0) {
				UI.ui.printRed(MSG_UNMARK_FAIL);
			} else {
				Task temp = Logic.crud.getCompletedTask(num - 1);
				Logic.Mark.markTaskAsUncompleted(num - 1);
				UI.ui.printGreen(s + MSG_UNMARK);
				Logic.crud.displayNearestFiveUnmarkCompleteTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {

		}
	}

	public static void markCommand(String s) {
		try {
			int num = Integer.parseInt(s);
			// check if user input integer is valid. If it is valid, mark
			// should
			// work
			ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
			ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
			if (list.size() + list2.size() == 0) {
				UI.ui.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				UI.ui.printRed(MSG_MARK_FAIL);
			} else {
				Task temp = Logic.crud.getUncompletedTask(num - 1);
				Logic.Mark.markTaskAsCompleted(num - 1);
				UI.ui.eraseScreen();
				UI.ui.printGreen(s + MSG_MARK);
				Logic.crud.displayNearestFiveCompletedTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {

		}
	}

	public static void sortCommand(String s) {
		if (s.equals("p") || s.equals("priority")) {
			Logic.Sort.sortTasksPriority();
			Logic.crud.displayUncompletedAndFloatingTasks();
		} else {
			Logic.Sort.sortTasksAlphabetically();
			UI.ui.printGreen(MSG_SORT);
			arraylistsHaveBeenModified = true;
		}
	}

	public static void clearCommand() throws ClassNotFoundException, IOException {
		Logic.crud.clearTasks();
		UI.ui.printGreen(MSG_CLEAR);
		arraylistsHaveBeenModified = true;
	}

	public static void viewCommand(String s) {
		try {
			int num = Integer.parseInt(s);
			Logic.crud.viewIndividualTask(num - 1);
		} catch (Exception e) {
		}
	}

	public static void displayCommand(String s) {
		if (s.equals("completed") || s.equals("c")) {
			Logic.crud.displayCompletedTasks();
		} else if (s.equals("floating") || s.equals("f")) {
			Logic.crud.displayFloatingTasks();
		} else if (Logic.checkDate.checkDateformat(s)) {
			Logic.crud.displayScheduleForADay(s);
		} else if (s.equals("all")) {
			Logic.crud.displayUncompletedAndFloatingTasks();
		} else if (s.equals("")){
			Logic.crud.displayUpcomingTasks();
		} else if (s.equals("today")) {
			Calendar today = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String todayString = df.format(today.getTime());
			Logic.crud.displayScheduleForADay(todayString);
		} else if (s.equals("tomorrow")) {
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.add(Calendar.DAY_OF_MONTH, 1);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String tomorrowString = df.format(tomorrow.getTime());

			Logic.crud.displayScheduleForADay(tomorrowString);
		} else if (s.equals("week") || s.equals("this week")) {
			Logic.crud.displayTasksForThisWeek();
		} else if (s.equals("next week") || s.equals("w+1")) {
			Logic.crud.displayTasksForNextWeek();
		} else if (s.equals("two weeks later") || s.equals("w+2")) {
			Logic.crud.displayTaksForTwoWeeksLater();
		} else if (s.equals("last week") || s.equals("w -1")) {
			Logic.crud.displayTasksForLastWeek();
		}  else {
			Logic.crud.displayByLabel(s);
		}
	}

	public static void deleteCommand(String s) {
		if ((Logic.Head.getLastCommand().equals("d") || Logic.Head.getLastCommand().equals("display")) == true) {
			// delete from uncompleted tasks
			try {
				String[] tmp = s.split(" ");
				if (s.contains("all")) {
					int num = Integer.parseInt(tmp[1]);
					Task t = delAllRecurringTask(num - 1);
					UI.ui.printGreen("All recurring tasks with issue " + t.getIssue() + " have been deleted");
					arraylistsHaveBeenModified = true;
				} else {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
					ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
					if (list.size() + list2.size() == 0) {
						UI.ui.printRed(MSG_EMPTY);
					} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
						// handle indexOutofBoundException
						UI.ui.printRed(MSG_TASK_DES_NOT_EXIST);
					} else {
						if ((num - 1) < list.size()) {
							Task deleted = list.get(num - 1);
							issue = deleted.getIssue();
							UI.ui.eraseScreen();
							Logic.crud.deleteTask(num - 1, 1);
							UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
							Logic.crud.displayNearestFiveDeleteUncompleteTaskList(num - 1);
							arraylistsHaveBeenModified = true;
						} else {
							Task deleted = list2.get(num - list.size() - 1);
							issue = deleted.getIssue();
							Logic.crud.deleteTask(num - 1, 1);
							UI.ui.eraseScreen();
							UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
							Logic.crud.displayNearestFiveDeleteFloatingTask(num - 1);
							arraylistsHaveBeenModified = true;
						}
					}
				}
			} catch (Exception e) {
			}
		} else if ((Logic.Head.getLastCommand().equals("search") || Logic.Head.getLastCommand().equals("s"))) {
			// delete from search results
			try {
				int num = Integer.parseInt(s);
				ArrayList<Task> list = Logic.Search.getSearchedTasks();
				if (list.size() == 0) {
					UI.ui.printRed(MSG_EMPTY);
				} else if (list.size() < num || num - 1 < 0) {
					// handle indexOutofBoundException
					UI.ui.printRed(MSG_TASK_DES_NOT_EXIST);
				} else {
					Task deleted = list.get(num - 1);
					issue = deleted.getIssue();
					Logic.crud.deleteTask(num - 1, 3);
					UI.ui.eraseScreen();
					UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
					arraylistsHaveBeenModified = true;
				}
			} catch (Exception e) {
			}
		} else if ((Logic.Head.getLastCommand().equals("dc")
				|| (Logic.Head.getLastCommand().equals("displaycompleted") == true))) {
			// delete from completed tasks
			try {
				int num = Integer.parseInt(s);
				ArrayList<Task> list = Storage.localStorage.getCompletedTasks();
				if (list.size() == 0) {
					UI.ui.printRed(MSG_EMPTY);
				} else if (list.size() < num || num - 1 < 0) {
					// handle indexOutofBoundException
					UI.ui.printRed(MSG_TASK_DES_NOT_EXIST);
				} else {
					Task deleted = list.get(num - 1);
					issue = deleted.getIssue();
					Logic.crud.deleteTask(num - 1, 2);
					UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
					arraylistsHaveBeenModified = true;
				}
			} catch (Exception e) {
			}
		}

		else {
			try {
				int num = Integer.parseInt(s);
				ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
				ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					UI.ui.printRed(MSG_EMPTY);
				} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
					// handle indexOutofBoundException
					UI.ui.printRed(MSG_TASK_DES_NOT_EXIST);
				} else {
					if ((num - 1) < list.size()) {
						Task deleted = list.get(num - 1);
						issue = deleted.getIssue();
						Logic.crud.deleteTask(num - 1, 1);
						UI.ui.eraseScreen();
						UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
						Logic.crud.displayNearestFiveDeleteUncompleteTaskList(num - 1);
						arraylistsHaveBeenModified = true;
					} else {
						Task deleted = list2.get(num - list.size() - 1);
						issue = deleted.getIssue();
						Logic.crud.deleteTask(num - 1, 1);
						UI.ui.eraseScreen();
						UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
						Logic.crud.displayNearestFiveDeleteFloatingTask(num - 1);
						arraylistsHaveBeenModified = true;
					}
				}
			} catch (Exception e) {
			}
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
	}

	/**
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
			if (!Logic.checkDate.checkTimeformat(arr[start + 2])) {
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
			if (!Logic.checkDate.checkTimeformat(arr[end + 2])) {
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

	public static void display(String s) {
		System.out.println(s);
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
	public static Task delAllRecurringTask(int n) throws ClassNotFoundException, IOException {
		ArrayList<Task> list = localStorage.getUncompletedTasks();
		Task deleted = list.get(n);
		String id = deleted.getId();
		for (int i = 0; i < list.size(); i++) {// delete from uncompleted tasks
			Task task = list.get(i);
			if (id.equals(task.getId())) {
				Logic.crud.deleteTask(i, 1);
				i = -1;// loop again
			}
		}
		return deleted;
	}

	/** 
	 * methods that support the editing of recurring task at index n
	 *  
	 * @param n
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */

	public static void editRecurringTask(int n) throws ClassNotFoundException, IOException {
		Task replaced = localStorage.getUncompletedTask(n);
		
		if (replaced.getId().equals("")) { // if the task at the user-entered index is not a recurring task. stop & inform user
			UI.ui.printRed(MSG_EDIT_NOT_RECURRING_TASK_HEAD + (n + 1) + MSG_EDIT_NOT_RECURRING_TASK_TAIL);
			return;
		}
		
		Logic.crud.copyRecurringTask(replaced);
		UI.ui.printRed("Enter new description and deadline of recurring tasks");
		String in = sc.nextLine();
		in = Natty.getInstance().parseEditString(in);
		// get index of key
		int ind= in.indexOf("`");
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

				if (!Logic.checkDate.checkDateformat(date)) {
					UI.ui.printRed(MSG_WRONG_DATE);
				} else {
					UI.ui.printRed("Enter \"<frequency> <last date>\"");
					String in2 = sc.nextLine();
					String[] tmp = in2.split(" ");
					String freq = tmp[0];
					String last = tmp[1];
					//	String before = tmp[2];

					int frequency = Integer.parseInt(freq);
					//int be4 = Integer.parseInt(before);
					int be4 = 0;

					Task task = new Task(issue,dateIn2,in,false,frequency,last);
					checkDateAndAdd(task);
					arraylistsHaveBeenModified = true;

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
				if (!Logic.checkDate.checkDateformat(startDate)) {
					UI.ui.printRed(MSG_WRONG_DATE);
				} else {
					UI.ui.printRed("Enter \"<frequency> <last date>\"");
					String in2 = sc.nextLine();
					String[] tmp = in2.split(" ");
					String freq = tmp[0];
					String last = tmp[1];
					//	String before = tmp[2];

					int frequency = Integer.parseInt(freq);

					Task task = new Task(issue,dateIn2,in,true,frequency,last);
					checkDateAndAdd(task);
					arraylistsHaveBeenModified = true;

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
				if (!Logic.checkDate.checkDateformat(startDate) && !Logic.checkDate.checkDateformat(date)) {
					UI.ui.printRed(MSG_WRONG_DATE);
				} else {
					UI.ui.printRed("Enter \"<frequency> <last date>\"");
					String in2 = sc.nextLine();
					String[] tmp = in2.split(" ");
					String freq = tmp[0];
					String last = tmp[1];
					//	String before = tmp[2];

					int frequency = Integer.parseInt(freq);
					Task task = new Task(issue,dateIn2,dateIn,in,frequency,last);
					checkDateAndAdd(task);
					arraylistsHaveBeenModified = true;

				}
			}
		} 
		delAllRecurringTask(n);
		UI.ui.printGreen("All instances of Task " + (n+1) +" has been edited and saved");
		
	}
	public static void checkDateAndAdd(Task task) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String today = dateFormat.format(date);

		try {

					String ed = task.getDateCompare();

					
					boolean expired = isExpired(ed, task.getLastDate());

						while (true) {
							if (expired) {//If task is not within display time frame or when task expired									//expired
								break;
							}
							localStorage.addToUncompletedTasks(task);
							String newED = processDate(ed, task.getFrequency());
							if (task.getStartDate() == null) {//no start date
								task = new Task(task.getIssue(), newED, task.getMsg(), false, task.getFrequency(),
										task.getLastDate(),task.getId());

							} else if (task.getEndDate() == null) {
								task = new Task(task.getIssue(), newED, task.getMsg(), true, task.getFrequency(),
										 task.getLastDate(),task.getId());
							}
							else {// has start date and end date
								task = new Task(task.getIssue(),task.getFixedStartDateString(),newED,task.getMsg(),task.getFrequency()
										,task.getLastDate(),task.getId());								
							}

							ed = task.getDateCompare();

							expired = (isExpired(ed,task.getLastDate()));
					

						}
				


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean isExpired(String date1, String date2) {
		String[] temp = date1.split("/");
		String[] temp2 = date2.split("/");
		if (Integer.parseInt(temp2[2]) < Integer.parseInt(temp[2])) {
			return true;
		}	else {	
			if (Integer.parseInt(temp2[1]) > Integer.parseInt(temp[1]) ) {//year or month < today 
				return false;
			} 	if (Integer.parseInt(temp2[1]) < Integer.parseInt(temp[1]) ) {//year or month < today 
				return true;
			} else {
				if (Integer.parseInt(temp2[0]) >= Integer.parseInt(temp[0])) {// day
					return false;
				} else {
					return true;
				}
			}
		}
	}
}
