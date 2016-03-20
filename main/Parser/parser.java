package Parser;

import java.util.*;

import Logic.crud;

import java.io.*;
import Task.Task;

public class parser {
	private static String date, issue;
	private static Scanner sc = new Scanner(System.in);
	private static final String[] key = { "by", "at", "in", "on", "during","before"};
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
	 * 
	 */
	public static boolean run(String cmd, String description) throws IOException {
		// process commands
		boolean valid = parseCommands(cmd, description);
		return valid;

	}

	/**
	 * methods that take in command and the body as the argument and process
	 * them to to meet the requests of the user.
	 * 
	 * @param option
	 * @param s
	 * @throws IOException
	 */
	public static boolean parseCommands(String option, String s) throws IOException {
		Boolean valid = true;
		if (option.equals("add") || option.equals("a") || option.equals("+")) {
			// get index of key
			String[] temp = s.split(" ");
			int index = getIndexOfKey(temp);
			date = "-";
			boolean isAdded;
			if (index != -1) {// if s contains date
				// read date
				date = temp[index + 1];
				if (!Logic.checkDate.checkDateformat(date)) {
					UI.ui.print(WRONG_DATE_MSG);

				} else {
					// get issue
					issue = getIssue(temp, index);
					isAdded = Logic.crud.addTask(issue, date);
					if(isAdded) {
						UI.ui.print("\"" + issue + "\" " + ADD_MSG);
					}
					else {
						UI.ui.print(DUPLICATE_ADD_MSG);
					}
				}
			} else {
				isAdded = Logic.crud.addTask(s);
				if(isAdded) {
					UI.ui.print("\"" + s + "\" " + ADD_MSG);
				}
				else {
					UI.ui.print(DUPLICATE_ADD_MSG);
				}
			}

		}

		else if (option.equals("delete") || option.equals("-")) {
			if((Logic.head.getLastCommand().equals("dc") || Logic.head.getLastCommand().equals("displaycompleted"))!= true) {
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
			}
			else if((Logic.head.getLastCommand().equals("search")|| Logic.head.getLastCommand().equals("s"))) {
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
			}
			else {
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
			}
		}

		else if (option.equals("display") || option.equals("d")) {
			Logic.crud.displayUncompletedTasks();
		}

		else if (option.equals("displaycompleted") || option.equals("dc")) {
			Logic.crud.displayCompletedTasks();
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

		else if (option.equals("edit") || option.equals("e")) {
			int num = Integer.parseInt(s);
			Logic.crud.copyTask(num);
			UI.ui.print("Enter edited task:");
			issue = sc.nextLine();
			Logic.crud.copyTaskDate(num);
			UI.ui.print("Enter the edited date:");
			while (true) { // check if the user want to add date
				date = sc.nextLine();
				if (date.equals("-")) {
					break;
				}
				if (Logic.checkDate.checkDateformat(date)) {
					break;
				}
				UI.ui.print(WRONG_DEADLINE_MSG);
			}
			if (date.equals("-")) {
				Logic.crud.editTask(num - 1, issue);
			} else {
				Logic.crud.editTask(num - 1, issue, date);
			}
			UI.ui.print("Task number " + s + EDIT_MSG);
		}

		else if (option.equals("p")) {
			int num = Integer.parseInt(s);
			UI.ui.print("Enter priority");
			String priority = sc.nextLine();
			Logic.mark.setPriority(num - 1, priority);
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
		} return valid;
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
	 * method that return the description of task from the String[] and position
	 * of keyword derived from the user's input
	 * 
	 * @param arr
	 * @param pos
	 * @return String
	 */
	public static String getIssue(String[] arr, int pos) {
		int size = arr.length - 2;
		String[] temp = new String[size];
		int i;
		// copy of arr to temp without the command,keyword and date
		for (i = 0; i < size; i++) {
			if (i >= pos) {
				temp[i] = arr[i + 2];

			} else {
				temp[i] = arr[i];
			}
		}
		return arrayToString(temp);

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
					break;
				}
			}
		}
		return idx;
	}
	public static String getIssue() {
		return issue;
	}
	public static String getDate() {
		return date;
	}
}