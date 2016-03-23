package Parser;

import java.util.*;

import Logic.Undo;
import Logic.crud;

import java.io.*;
import Task.Task;

public class Parser {
	private static String startDate, date, issue, startTime, time;
	private static Scanner sc = new Scanner(System.in);
	private static final String[] key = { "by", "at", "in", "on", "during", "before" };
	private static final String EMPTY_MSG = " Unable to delete from empty task list";
	private static final String CLEAR_MSG = "All content deleted";
	private static final String ADD_MSG = "is added to the task list.";
	private static final String DUPLICATE_ADD_MSG = "Duplicate task detected.";
	private static final String DELETE_MSG = "is deleted from the task list.";
	private static final String SORT_MSG = "All items are sorted in alphabetical order";
	private static final String EDIT_MSG = " is edited and saved";
	private static final String MARK_MSG = " is marked as completed";
	private static final String INVALID_MSG = "Invalid inputs! Please try again";
	private static final String DEADLINE_PROMPT = "Enter deadline in dd/mm/yyyy or enter - for no deadline";
	private static final String WRONG_DEADLINE_MSG = "Please enter deadline in dd/mm/yyyy format or enter - for no deadline";
	private static final String SEARCH_PROMPT = "Press 1 to search by issue, 2 to search by date";
	private static final String ISSUE_PROMPT = "Enter keyword contained in the issue";
	private static final String DATE_PROMPT = "Enter date in dd/mm/yyyy";
	private static final String WRONG_DATE_MSG = "Please enter date in dd/mm/yyyy format";
	private static final String DNE_MSG = "Task does not exists";

	/**
	 * method that simulate command line interface that will responds to user's
	 * inputs
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * return boolean
	 */
	public static boolean run(String cmd, String description) throws IOException, ClassNotFoundException {
		// process commands
		boolean valid = parseCommands(cmd, description);
		if (!description.equals("")) {
			cmd += " " + description;
		}
		Undo.getInstance().storePreviousStateIfNeeded(cmd);
		return valid;
	}

	/**
	 * methods that take in command and the body as the argument and process
	 * them to to meet the requests of the user.
	 * 
	 * @param option
	 * @param s
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * return boolean
	 */
	public static boolean parseCommands(String option, String s) throws IOException, ClassNotFoundException {
		Boolean valid = true;
		if (option.equals("add") || option.equals("a") || option.equals("+")) {
			// get index of key
			String[] temp = s.split(" ");
			int start = getStartingIndex(temp); // start has value of -1 if it has no start date
			int end = getIndexOfKey(temp); // end has value of -1 if it has no end date
			boolean isAdded;
			if (start == -1 && end != -1) {// no start date but has end date
				startDate = "-";
				startTime = "-";
				// read date & time
				date = temp[end + 1];

				if (hasEndTime(temp)) {// check if contain end time
					time = temp[end + 2];
				} else {
					time = "-";
				}
				if (!Logic.checkDate.checkDateformat(date)) {
					UI.ui.print(WRONG_DATE_MSG);

				} else {
					// get issue
					issue = getIssue(temp, start, end, hasStartTime(temp), hasEndTime(temp));
					// isAdded =Logic.crud.addTask(issue,startDate,startTime,endDate,endTime) (to be implemented)
					isAdded = Logic.crud.addTask(issue, date);
					if (isAdded) {
						UI.ui.print("\"" + issue + "\" " + ADD_MSG);
					} else {
						UI.ui.print(DUPLICATE_ADD_MSG);
					}
				}
			} else if (start == -1 && end == -1) {// no end date and no start date
				isAdded = Logic.crud.addTask(s);
				if (isAdded) {
					UI.ui.print("\"" + s + "\" " + ADD_MSG);
				} else {
					UI.ui.print(DUPLICATE_ADD_MSG);
				}

			} else if (start != -1 && end == -1) {// has start date but no end date
				date = "-";
				time = "-";
				startDate = temp[start + 1];

				if (hasStartTime(temp)) {
					startTime = temp[start + 2];
				} else {
					startTime = "-";
				}
				if (!Logic.checkDate.checkDateformat(startDate)) {
					UI.ui.print(WRONG_DATE_MSG);

				} else {
					// get issue
					issue = getIssue(temp, start, end, hasStartTime(temp), hasEndTime(temp));
					// isAdded = Logic.crud.addTask(issue,startDate,startTime,endDate,endTime);
					isAdded = Logic.crud.addTask(issue, startDate);
					if (isAdded) {
						UI.ui.print("\"" + issue + "\" " + ADD_MSG);
					} else {
						UI.ui.print(DUPLICATE_ADD_MSG);
					}
				}
			} else { // has both start date and end date
				startDate = temp[start + 1];
				date = temp[end + 1];
				if (hasStartTime(temp)) {
					startTime = temp[start + 2];
				} else {
					startTime = "-";
				}
				if (hasEndTime(temp)) {
					time = temp[end + 2];

				} else {
					time = "-";
				}

				if (!Logic.checkDate.checkDateformat(startDate) && !Logic.checkDate.checkDateformat(date)) {
					UI.ui.print(WRONG_DATE_MSG);
				} else {
					// get issue
					issue = getIssue(temp, start, end, hasEndTime(temp), hasEndTime(temp));
					// isAdded = Logic.crud.addTask(issue,startDate,startTime,endDate,endTime);
					isAdded = Logic.crud.addTask(issue, date);
					if (isAdded) {
						UI.ui.print("\"" + issue + "\" " + ADD_MSG);
					} else {
						UI.ui.print(DUPLICATE_ADD_MSG);
					}
				}

			}
		} else if (option.equals("delete") || option.equals("-")) {
			if ((Logic.head.getLastCommand().equals("d") || Logic.head.getLastCommand().equals("display")) == true) {
				// delete from uncompleted tasks
				int num = Integer.parseInt(s);
				ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
				if (list.size() == 0) {
					UI.ui.print(EMPTY_MSG);
				} else if (list.size() < num || num - 1 < 0) {
					// handle indexOutofBoundException
					UI.ui.print(DNE_MSG);

				} else {
					Task deleted = list.get(num - 1);
					issue = deleted.getIssue();
					Logic.crud.deleteTask(num - 1, 1);
					UI.ui.print("\"" + issue + "\" " + DELETE_MSG);
				}
			} else if ((Logic.head.getLastCommand().equals("search") || Logic.head.getLastCommand().equals("s"))) {
				// delete from search results
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
				}
			} else if ((Logic.head.getLastCommand().equals("dc")
					|| (Logic.head.getLastCommand().equals("displaycompleted") == true))) {
				// delete from completed tasks
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
				}
			} else {
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
				}
			}
		}

		else if (option.equals("display") || option.equals("d")) {
			Logic.crud.displayUncompletedTasks();
		}

		else if (option.equals("displaycompleted") || option.equals("dc")) {
			Logic.crud.displayCompletedTasks();
		}

		else if (option.equals("displayfloating") || option.equals("df")) {
			Logic.crud.displayFloatingTasks();
		}

		else if (option.equals("view") || option.equals("v")) {
			int num = Integer.parseInt(s);
			Logic.crud.viewIndividualTask(num - 1);
		}

		else if (option.equals("clear") || option.equals("c")) {
			Logic.crud.clearTasks();
			UI.ui.print(CLEAR_MSG);
		}

		else if (option.equals("sort")) { // by alphabetical order
			Logic.sort.sortTasksAlphabetically();
			UI.ui.print(SORT_MSG);
		}

		else if (option.equals("search") || option.equals("s")) {
			Logic.search.searchTasksByKeyword(s);
		}

		else if (option.equals("searchcompleted") || option.equals("sc")) {
			Logic.search.searchCompletedTasksByKeyword(s);
		}

		else if (option.equals("mark") || option.equals("m")) {
			int num = Integer.parseInt(s);
			Logic.mark.markTaskAsCompleted(num - 1);
			UI.ui.print(s + MARK_MSG);
		}

		// edit <task number>|<issue>|<date>
		else if (option.equals("edit") || option.equals("e")) {
			String[] temp = s.split("\\|");
			int num = Integer.parseInt(temp[0]);
			issue = temp[1];
			date = temp[2];
			Logic.crud.copyTask(num);
			Logic.crud.copyTaskDate(num);
			if (!Logic.checkDate.checkDateformat(date)) {
				Logic.crud.editTask(num - 1, issue);
			} else {
				Logic.crud.editTask(num - 1, issue, date);
			}
			UI.ui.print("Task number " + num + EDIT_MSG);
		}

		else if (option.equals("p")) {
			int num = Integer.parseInt(s);
			UI.ui.print("Enter priority");
			String priority = sc.nextLine();
			Logic.mark.setPriority(num - 1, priority);
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
			Logic.help.printHelpMenu();
		}

		else {
			UI.ui.print(INVALID_MSG);
			valid = false;
		}
		return valid;
	}

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
			if (startTime && endTime) { // has start time and end time
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
			} else if (startTime = true && endTime == false) {// has only start
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
			if (!checkTimeFormat(arr[start + 2])) {
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
			if (!checkTimeFormat(arr[end + 2])) {
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

	// to be implemented
	public static boolean checkTimeFormat(String s) {// assume no time
		return true;
	}
}
