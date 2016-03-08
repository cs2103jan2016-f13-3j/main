package Parser;

import java.util.*;

import Logic.CRUD;

import java.io.*;
import Task.Task;

public class parser {
	private static String date, issue, keyword;

	private static Scanner sc = new Scanner(System.in);

	private static final String EMPTY_MSG = " Unable to delete from empty task list";
	private static final String CLEAR_MSG = "All content deleted";
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
	public static void run(String cmd, String description) throws IOException {
		// process commands
		parseCommands(cmd, description);

	}

	/**
	 * methods that take in command and the body as the argument and process
	 * them to to meet the requests of the user.
	 * 
	 * @param option
	 * @param s
	 * @throws IOException
	 */
	public static void parseCommands(String option, String s) throws IOException {
		if (option.equals("add")) {
			UI.ui.print(DEADLINE_PROMPT);
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
				Logic.CRUD.addTask(s);
			} else {
				Logic.CRUD.addTask(s, date);
			}
			UI.ui.print("\"" + s + "\" " + "is added to the task list.");
		}

		else if (option.equals("delete")) {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = Storage.localStorage.displayStorage();
			if (list.size() == 0) {
				UI.ui.print(EMPTY_MSG);
			} else if (list.size() < num || num - 1 < 0) {
				// handle indexOutofBoundException
				UI.ui.print(DNE_MSG);

			} else {
				Task deleted = list.get(num - 1);
				issue = deleted.getIssue();
				Logic.CRUD.deleteTask(num - 1);
				UI.ui.print("\"" + issue + "\" " + "is deleted from the task list.");
			}
		}

		else if (option.equals("display")) {
			Logic.CRUD.displayTasks();
		}

		else if (option.equals("clear")) {
			Logic.CRUD.clearTasks();
			UI.ui.print(CLEAR_MSG);
		}

		else if (option.equals("sort")) { // by alphabetical order
			Logic.Sort.sortTasksAlphabetically();
			UI.ui.print(SORT_MSG);
		}

		else if (option.equals("search")) {

			String temp = "";
			UI.ui.print(SEARCH_PROMPT);
			while ((!temp.equals("1")) || (!temp.equals("2"))) {
				if (temp.equals("1") || (temp.equals("2"))) {
					break;
				}
				temp = sc.nextLine();
				if (temp.equals("1")) {
					UI.ui.print(ISSUE_PROMPT);
					keyword = sc.nextLine();
					Logic.Search.searchTasksByIssue(keyword);
				} else if (temp.equals("2")) {
					UI.ui.print(DATE_PROMPT);
					while (true) {
						date = sc.nextLine();

						if (Logic.checkDate.checkDateformat(date)) {
							Logic.Search.searchTasksByDate(date);
							break;
						} else {
							UI.ui.print(WRONG_DATE_MSG);
						}
					}
				} else {
					UI.ui.print(SEARCH_PROMPT);
				}
			}
		}

		else if (option.equals("mark")) {// Assume issue is the argument
			// Logic.crud.completeTask(s);
			UI.ui.print(s + MARK_MSG);
		}

		else if (option.equals("edit")) {// Assume issue is the argument
			// Logic.crud.edit(s);
			UI.ui.print(s + EDIT_MSG);
		}

		else if (option.equals("exit")) {
			Logic.CRUD.exit();
		}

		else {
			UI.ui.print(INVALID_MSG);
		}
	}

}