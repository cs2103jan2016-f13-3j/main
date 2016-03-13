package Parser;

import java.util.*;

import Logic.crud;

import java.io.*;
import Task.Task;

public class parser {
	private static String date;

	private static Scanner sc = new Scanner(System.in);

	private static final String EMPTY_MSG = " is empty";
	private static final String CLEAR_MSG = "All content has been cleared.";
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
	public static void parseCommands(String option, String s) throws IOException {
		if(option.equals("add") || option.equals("a") || option.equals("+")) {
			UI.ui.print(DEADLINE_MSG);
			while (true) { //check if the user want to add date
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
				Logic.crud.addTask(s);
			} else {
				Logic.crud.addTask(s, date);
			}
			UI.ui.print( "\"" + s + "\" " + "is added to the task list.");
		}

		else if(option.equals("delete") ||option.equals("-")) {
			int num = Integer.parseInt(s);
			Logic.crud.deleteTask(num - 1);
			UI.ui.print( "\"" + s + "\" " + "is deleted from the task list.");
		}

		else if(option.equals("display") || option.equals("d")) {
			Logic.crud.displayUncompletedTasks();
		}
		
		else if(option.equals("displaycompleted") || option.equals("dc")) {
			Logic.crud.displayCompletedTasks();
		}
		
		else if(option.equals("view") || option.equals("v")) {
			int num = Integer.parseInt(s);
			Logic.crud.viewIndividualTask(num - 1);
		}

		else if(option.equals("clear") || option.equals("c")) {
			Logic.crud.clearTasks();
			UI.ui.print(CLEAR_MSG);
		}

		else if(option.equals("sort")) { // by alphabetical order
			Logic.sort.sortTasksAlphabetically();
			UI.ui.print(SORT_MSG);
		}

		else if(option.equals("search") || option.equals("s")) {
			Logic.search.searchTasksByKeyword(s);	
		}

		else if (option.equals("mark") || option.equals("m")) {
			int num = Integer.parseInt(s);
			Logic.mark.markTaskAsCompleted(num - 1);
			UI.ui.print(s + MARK_MSG);
		}

		else if(option.equals("edit") ||  option.equals("e")) {
			int num = Integer.parseInt(s);
			UI.ui.print("Enter edited task:");
			String taskDescription = sc.nextLine();

			UI.ui.print("Enter the edited date:");
			while (true) { //check if the user want to add date
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
				Logic.crud.editTask(num - 1, taskDescription);
			} else {
				Logic.crud.editTask(num - 1, taskDescription, date);
			}			
			UI.ui.print("Task number " + s + EDIT_MSG);
		}
		
		else if(option.equals("p")) {
			int num = Integer.parseInt(s);
			UI.ui.print("Enter priority");
			String priority = sc.nextLine();
			Logic.mark.setPriority(num - 1, priority);
		}

		else if(option.equals("exit")) {
			Logic.crud.exit();
		}
		
		else if(option.equals("help")) {
			Logic.help.printHelpMenu();
		}

		else {
			UI.ui.print(INVALID_MSG);
		}
	}


}