//@@author Jung Kai
package Parser;
import java.util.*;

import Logic.Undo;
import Logic.crud;

import java.io.*;
import java.security.Timestamp;

import Task.Task;

public class Parser {
	private static boolean arraylistsHaveBeenModified;
	private static String startDate, date, issue, startTime, time, input, dateIn, dateIn2;
	private static Scanner sc = new Scanner(System.in);
	private static final String[] key = { "by", "at", "in", "on", "during", "before", "to" };
	private static final String EMPTY_MSG = "Storage is empty. Press \"add\" to add task.";
	private static final String CLEAR_MSG = "All content deleted";
	private static final String ADD_MSG = "is added to the task list.";
	private static final String EDIT_PROMPT = "Insert new description and deadline for the task.";
	private static final String EDIT_FAIL_MSG = "Fail to edit. Please insert a valid task number.";
	private static final String MARK_FAIL_MSG = "Fail to mark task as completed. Please insert a valid task number.";
	private static final String UNMARK_FAIL_MSG = "Fail to mark task as uncompleted. Please insert a valid task number.";
	private static final String NoCompleted_MSG = "No task has been completed yet.";
	private static final String DUPLICATE_ADD_MSG = "Duplicate task detected.";
	private static final String DELETE_MSG = "is deleted from the task list.";
	private static final String SORT_MSG = "All items are sorted in alphabetical order";
	private static final String EDIT_MSG = " is edited and saved";
	private static final String MARK_MSG = " is marked as completed";
	private static final String UNMARK_MSG = " is marked as uncompleted";
	private static final String INVALID_MSG = "Invalid inputs! Please try again";
	private static final String DEADLINE_PROMPT = "Enter deadline in dd/mm/yyyy or enter - for no deadline";
	private static final String WRONG_DEADLINE_MSG = "Please enter deadline in dd/mm/yyyy format or enter - for no deadline";
	private static final String SEARCH_PROMPT = "Press 1 to search by issue, 2 to search by date";
	private static final String ISSUE_PROMPT = "Enter keyword contained in the issue";
	private static final String DATE_PROMPT = "Enter date in dd/mm/yyyy";
	private static final String WRONG_DATE_MSG = "Please enter date in dd/mm/yyyy format";
	private static final String DNE_MSG = "Task does not exists";
	private static final String PRIORITY_FAIL_MSG = "Fail to set priority. Please insert a valid task number.";

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
			// get index of key
			String[] temp = s.split(" ");
			int start = getStartingIndex(temp); // start has value of -1 if it
			// has no start date
			int end = getIndexOfKey(temp); // end has value of -1 if it has no
			// end date
			boolean isAdded;
			if (start == -1 && end != -1) {// no start date but has end date
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
					UI.ui.print(WRONG_DATE_MSG);
				} else {
					// get issue
					issue = getIssue(temp, start, end, hasStartTime(temp), hasEndTime(temp));
					// isAdded
					// =Logic.crud.addTask(issue,startDate,startTime,endDate,endTime)
					// (to be implemented)
					isAdded = Logic.crud.addTaskWithEndDate(issue, dateIn, s);
					if (isAdded) {
						UI.ui.print("\"" + issue + "\" " + ADD_MSG);
						arraylistsHaveBeenModified = true;
					} else {
						UI.ui.print(DUPLICATE_ADD_MSG);
					}
				}

			} else if (start == -1 && end == -1) {// no end date and no start
				// date
				isAdded = Logic.crud.addTask(s);
				if (isAdded) {
					UI.ui.print("\"" + s + "\" " + ADD_MSG);
					arraylistsHaveBeenModified = true;
				} else {
					UI.ui.print(DUPLICATE_ADD_MSG);
				}

			} else if (start != -1 && end == -1) {// has start date but no end
				// date
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
					UI.ui.print(WRONG_DATE_MSG);
				} else {
					// get issue
					issue = getIssue(temp, start, end, hasStartTime(temp), hasEndTime(temp));
					// isAdded =
					// Logic.crud.addTask(issue,startDate,startTime,endDate,endTime);
					isAdded = Logic.crud.addTaskWithStartDate(issue, dateIn2, s);
					if (isAdded) {
						UI.ui.print("\"" + issue + "\" " + ADD_MSG);
						arraylistsHaveBeenModified = true;
					} else {
						UI.ui.print(DUPLICATE_ADD_MSG);
					}
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
					UI.ui.print(WRONG_DATE_MSG);
				} else {
					// get issue
					issue = getIssue(temp, start, end, hasEndTime(temp), hasEndTime(temp));

					// isAdded =
					// Logic.crud.addTask(issue,startDate,startTime,endDate,endTime);
					isAdded = Logic.crud.addTaskWithBothDates(issue, dateIn2, dateIn, s);
					if (isAdded) {
						UI.ui.print("\"" + issue + "\" " + ADD_MSG);
						arraylistsHaveBeenModified = true;
					} else {
						UI.ui.print(DUPLICATE_ADD_MSG);
					}
				}
			}
		} 
		//@@author Kowshik
		else if (option.equals("delete") || option.equals("-")) {
			if ((Logic.Head.getLastCommand().equals("d") || Logic.Head.getLastCommand().equals("display")) == true) {
				// delete from uncompleted tasks
				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
					ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
					if (list.size() + list2.size() == 0) {
						UI.ui.print(EMPTY_MSG);
					} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
						// handle indexOutofBoundException
						UI.ui.print(DNE_MSG);
					} else {
						if (num < list.size()) {
							Task deleted = list.get(num - 1);
							issue = deleted.getIssue();
							Logic.crud.deleteTask(num - 1, 1);
							UI.ui.print("\"" + issue + "\" " + DELETE_MSG);
							arraylistsHaveBeenModified = true;
						} else {
							Task deleted = list2.get(num - list.size() - 1);
							issue = deleted.getIssue();
							Logic.crud.deleteTask(num - 1, 1);
							UI.ui.print("\"" + issue + "\" " + DELETE_MSG);
							arraylistsHaveBeenModified = true;
						}
					}
				} catch (Exception e) {
				}
			} else if ((Logic.Head.getLastCommand().equals("search") || Logic.Head.getLastCommand().equals("s"))) {
				// delete from search results
				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Logic.search.getSearchedTasks();
					if (list.size() == 0) {
						UI.ui.print(EMPTY_MSG);
					} else if (list.size() < num || num - 1 < 0) {
						// handle indexOutofBoundException
						UI.ui.print(DNE_MSG);
					} else {
						Task deleted = list.get(num - 1);
						issue = deleted.getIssue();
						Logic.crud.deleteTask(num - 1, 3);
						UI.ui.print("\"" + issue + "\" " + DELETE_MSG);
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
						UI.ui.print(EMPTY_MSG);
					} else if (list.size() < num || num - 1 < 0) {
						// handle indexOutofBoundException
						UI.ui.print(DNE_MSG);
					} else {
						Task deleted = list.get(num - 1);
						issue = deleted.getIssue();
						Logic.crud.deleteTask(num - 1, 2);
						UI.ui.print("\"" + issue + "\" " + DELETE_MSG);
						arraylistsHaveBeenModified = true;
					}
				} catch (Exception e) {
				}
			}

			else {
				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Storage.localStorage.getFloatingTasks();
					if (list.size() == 0) {
						UI.ui.print(EMPTY_MSG);
					} else if (list.size() < num || num - 1 < 0) {
						// handle indexOutofBoundException
						UI.ui.print(DNE_MSG);
					} else {
						Task deleted = list.get(num - 1);
						issue = deleted.getIssue();
						Logic.crud.deleteTask(num - 1, 4);
						UI.ui.print("\"" + issue + "\" " + DELETE_MSG);
						arraylistsHaveBeenModified = true;
					}
				} catch (Exception e) {
				}
			}
		} else if (option.equals("display") || option.equals("d")) {
			UI.ui.print("UNCOMPLETED TASKS");
			Logic.crud.displayUncompletedAndFloatingTasks();
		}

		else if (option.equals("displaycompleted") || option.equals("dc")) {
			Logic.crud.displayCompletedTasks();
		}

		else if (option.equals("view") || option.equals("v")) {
			try {
				int num = Integer.parseInt(s);
				Logic.crud.viewIndividualTask(num - 1);
			} catch (Exception e) {
			}
		}

		else if (option.equals("clear") || option.equals("c")) {
			Logic.crud.clearTasks();
			UI.ui.print(CLEAR_MSG);
			arraylistsHaveBeenModified = true;
		}

		else if (option.equals("sort")) { // by alphabetical order
			Logic.sort.sortTasksAlphabetically();
			UI.ui.print(SORT_MSG);
			arraylistsHaveBeenModified = true;
		}

		else if (option.equals("search") || option.equals("s")) {
			Logic.search.searchTasksByKeyword(s);
		}

		else if (option.equals("mark") || option.equals("m")) {
			try {
				int num = Integer.parseInt(s);
				// check if user input integer is valid. If it is valid, mark
				// should
				// work
				ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
				ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					UI.ui.print(EMPTY_MSG);
				} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
					UI.ui.print(MARK_FAIL_MSG);
				} else {
					Logic.mark.markTaskAsCompleted(num - 1);
					UI.ui.print(s + MARK_MSG);
					arraylistsHaveBeenModified = true;
				}
			} catch (Exception e) {

			}
		} else if (option.equals("unmark") || option.equals("um")) {
			try {
				int num = Integer.parseInt(s);
				// check if user input integer is valid. If it is valid, unmark
				// should
				// work
				ArrayList<Task> list = Storage.localStorage.getCompletedTasks();
				if (list.size() == 0) {
					UI.ui.print(NoCompleted_MSG);
				} else if (list.size() < num || num - 1 < 0) {
					UI.ui.print(UNMARK_FAIL_MSG);
				} else {
					Logic.mark.markTaskAsUncompleted(num - 1);
					UI.ui.print(s + UNMARK_MSG);
					arraylistsHaveBeenModified = true;
				}
			} catch (Exception e) {

			}
		} 
		//@@author Jung Kai
		else if (option.equals("edit") || option.equals("e")) {
			try {
				int num = Integer.parseInt(s);
				// check if user input integer is valid. If it is valid, edit
				// should
				// work
				ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
				ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					UI.ui.print(EMPTY_MSG);
				} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
					UI.ui.print(EDIT_FAIL_MSG);
				} else {
					UI.ui.print(EDIT_PROMPT);
					Logic.crud.copyEditingTask(num);
					input = sc.nextLine();
					String[] temp = input.split(" ");
					int start = getStartingIndex(temp); // start has value of -1
					// if it has no start
					// date
					int end = getIndexOfKey(temp); // end has value of -1 if it
					// has no end date
					if (start == -1 && end != -1) {// no start date but has end
						// date
						startDate = "-";
						startTime = "-";
						// read date & time
						date = temp[end + 1];
						dateIn = date;

						if (hasEndTime(temp)) {// check if contain end time
							time = temp[end + 2];
							time.replaceAll(":", "/");
							dateIn = dateIn + "/" + time;
						} else {
							time = "-";
						}
						if (!Logic.checkDate.checkDateformat(date)) {
							UI.ui.print(WRONG_DATE_MSG);
						} else {
							// get issue

							issue = getIssue(temp, start, end, hasStartTime(temp), hasEndTime(temp));
							// Logic.crud.editTask(num-1,issue,startDate,startTime,endDate,endTime,input)
							// (to be implemented)
							Logic.crud.editTaskWithEndDate(issue, dateIn, input, num - 1);
							UI.ui.print("Task number " + num + EDIT_MSG);
							arraylistsHaveBeenModified = true;
						}
					} else if (start == -1 && end == -1) {// no end date and no
						// start date
						Logic.crud.editTaskWithNoDate(input, input, num - 1);
						UI.ui.print("Task number " + num + EDIT_MSG);
						arraylistsHaveBeenModified = true;
					} else if (start != -1 && end == -1) {// has start date but
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
							UI.ui.print(WRONG_DATE_MSG);
						} else {
							// get issue
							issue = getIssue(temp, start, end, hasStartTime(temp), hasStartTime(temp));
							// Logic.crud.editTask(issue,startDate,startTime,endDate,endTime,input);
							Logic.crud.editTaskWithStartDate(issue, dateIn2, input, num - 1);
							UI.ui.print("Task number " + num + EDIT_MSG);
							arraylistsHaveBeenModified = true;
						}
					} else { // has both start date and end date
						startDate = temp[start + 1];
						date = temp[end + 1];
						dateIn = date;
						dateIn2 = startDate;
						if (hasStartTime(temp)) {
							startTime = temp[start + 2];
							startTime.replaceAll(":", "/");
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
							UI.ui.print(WRONG_DATE_MSG);
						} else {
							// get issue
							issue = getIssue(temp, start, end, hasStartTime(temp), hasEndTime(temp));
							// Logic.crud.addTask(issue,startDate,startTime,endDate,endTime);
							Logic.crud.editTaskWithBothDates(issue, dateIn2, dateIn, input, num - 1);
							UI.ui.print("Task number " + num + EDIT_MSG);
							arraylistsHaveBeenModified = true;
						}
					}
				}
			} catch (Exception e) {
			}
		} 
		//@@author Kowshik
		else if (option.equals("p")) {
			try {
				int num = Integer.parseInt(s);
				// check if user input integer is valid. If it is valid, edit
				// should
				// work
				ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
				ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					UI.ui.print(EMPTY_MSG);
				} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
					UI.ui.print(PRIORITY_FAIL_MSG);
				} else {
					UI.ui.print("Enter priority");
					String priority = sc.nextLine();
					Logic.mark.setPriority(num - 1, priority);
					arraylistsHaveBeenModified = true;
				}
			} catch (Exception e) {
			}
		} else if (option.equals("sortp") || option.equals("sp")) {

			Logic.sort.sortTasksPriority();
			Logic.crud.displayUncompletedAndFloatingTasks();
		}

		else if (option.equals("history")) {
			String pastCommands = Undo.getInstance().viewPastCommands();
			UI.ui.print(pastCommands);
		}

		else if (option.equals("undo")) {
			String outcome = Undo.getInstance().undo();
			UI.ui.print(outcome);
		}

		else if (option.equals("exit")) {
			Logic.crud.exit();
		}

		else if (option.equals("help")) {
			Logic.Help.printHelpMenu();
		}

		else {
			UI.ui.print(INVALID_MSG);
		}
		return arraylistsHaveBeenModified;
	}

	//@@author Jung Kai
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
		for (int i = 0; i < key.length; i++) {
			for (int j = 1; j < arr.length; j++) {
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
			if (arr[i].equals("from")) {
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
}