package Parser;

import java.util.*;
import java.io.*;
import Task.Task;

public class parser {
	private static String date;

	private static Scanner sc = new Scanner(System.in);

	private static final String EMPTY_MSG = " is empty";
	private static final String CLEAR_MSG = "All content deleted from ";
	private static final String SORT_MSG = "All items are sorted in alphabetical order";
	private static final String EDIT_MSG = " is edited and saved";
	private static final String MARK_MSG = " is marked as completed";
	private static final String INVALID_MSG = "Invalid inputs! Please try again";
	private static final String DEADLINE_MSG = "Enter deadline in dd/mm/yyyy or enter - for no deadline";
	private static final String WRONG_DEADLINE_MSG = "Please enter deadline in dd/mm/yyyy format or enter - for no deadline";
	private static final String SEARCH_MSG = "Press 1 to search by issue, 2 to search by date";
	private static final String DATE_MSG = "Enter date in dd/mm/yyyy";
	private static final String WRONG_DATE_MSG = "Please enter date in dd/mm/yyyy format";

	/**
	 * method that simulate command line interface that will responds to user's
	 * inputs
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
	public static void parseCommands(String option, String s ) throws IOException {
		if(option.equals("add")) {
			System.out.println(DEADLINE_MSG);
			while (true) { //check if the user want to add date
				date = sc.nextLine();
				if (date.equals("-")) {
					break;
				}
				if (Logic.checkDate.checkDateformat(date)) {
					break;
				}
				System.out.println(WRONG_DEADLINE_MSG);
			}
			if (date.equals("-")) {
				Logic.crud.addTask(s);
			} else {
				Logic.crud.addTask(s, date);
			}
			System.out.println( "\"" + s + "\" " + "is added to the task list.");
		}

		else if(option.equals("delete")) {
			int num = Integer.parseInt(s);
			Logic.crud.deleteTask(num - 1);
			System.out.println( "\"" + s + "\" " + "is deleted from the task list.");
		}

		else if(option.equals("display")) {
			Logic.crud.displayTasks();
		}

		else if(option.equals("clear")) {
			Logic.crud.clearTasks();
			System.out.println(CLEAR_MSG);
		}

		else if(option.equals("sort")) { // by alphabetical order
			Logic.sort.sortTasksAlphabetically();
			System.out.println(SORT_MSG);
		}

		else if(option.equals("search")) {
			System.out.println(SEARCH_MSG);
			String temp = sc.nextLine();
			if (temp.equals("1"))  {
				Logic.search.searchTasksByIssue(s);
			} else {
				System.out.println(DATE_MSG);
				while (true) {
					//date = sc.nextLine();		
					//System.out.println(date);
					if (Logic.checkDate.checkDateformat(s)) {
						Logic.search.searchTasksByDate(s);
						break;
					} else {
						System.out.println(WRONG_DATE_MSG);
					}
				}
			}		
		}

		else if (option.equals("mark")) {
			// Logic.crud.mark(s);
			System.out.println(s + MARK_MSG);
		}

		else if(option.equals("edit")) {
			// Logic.crud.edit(s,d);
			System.out.println(s + EDIT_MSG);
		}

		else if(option.equals("exit")) {
			Logic.crud.exit();
		}

		else {
			System.out.println(INVALID_MSG);
		}
	}


}