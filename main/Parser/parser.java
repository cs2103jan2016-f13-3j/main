package Parser;

import Logic.CRUD;
import java.util.*;
import java.io.*;

public class parser {

	private static Scanner sc = new Scanner(System.in);
	private static String fileName_;
	private static final String WELCOME_MSG_1 = "Welcome to TextBuddy. ";
	private static final String WELCOME_MSG_2 = " is ready for use";
	private static final String EMPTY_MSG = " is empty";
	private static final String CLEAR_MSG = "all content deleted from ";
	private static final String SORT_MSG = "all items are sorted in alphabetical order";
	private static final String EDIT_MSG = " is edited and saved";
	private static final String MARK_MSG = " is marked as completed";
	private static final String INVALID_MSG = "Invalid inputs! Please try again";
	private static final String DATE_MSG = "enter deadline in dd/mm/yyyy or enter - for no deadline";
	private static final String WRONG_DATE_MSG = "please enter deadline in dd/mm/yyyy format or enter - for no deadline";

	public static void main(String[] args) {
		fileName_ = args[0];
		sc = new Scanner(System.in);
		System.out.println(WELCOME_MSG_1 + fileName_ + WELCOME_MSG_2);
		// run to simulate command line interactions
		run();
	}

	/**
	 * method that simulate command line interface that will responds to user's
	 * inputs
	 * 
	 */
	public static void run() {
		while (true) {
			System.out.print("command: ");
			String input = sc.nextLine();
			String[] arr = input.split(" ");
			// get command
			String cmd = arr[0];
			String description;
			// handle lines with only the command
			if (cmd.length() == input.length()) {
				description = "";
			} else {
				// get description
				description = input.substring(cmd.length() + 1, input.length());
			}
			String date;
			if (cmd.equals("add")) {
				System.out.println(DATE_MSG);
				while (true) {
					date = sc.nextLine();
					if (date.equals("-")) {
						break;
					}
					if (Logic.CRUD.checkDateformat(date)) {
						break;
					}
					System.out.println(WRONG_DATE_MSG);
				}
				if (cmd.equals("search")) {
					if (Logic.CRUD.checkDateformat(description)) {
						date = description;
					} else {
						date = "-";
					}

				}
			}
		}

		// process commands
		parseCommands(cmd, description, date);
		// terminate the program if exit command is processed
		if (cmd.equals("exit")) {
			break;
		}
	}

	}

	/**
	 * methods that take in command and the body as the argument and process
	 * them to to meet the requests of the user.
	 * 
	 * @param option
	 * @param s
	 */
	public static void parseCommands(String option, String s, String d) {
		switch (option) {
		case "add":
			if (d.equals("-")) {
				Logic.CRUD.addTask(s);
			} else {
				Logic.CRUD.addTask(s, d);
			}
			System.out.println("added to " + fileName_ + ":\"" + s + "\" ");
			break;
		case "delete":
			int num = Integer.parseInt(s);
			Logic.CRUD.deleteTask(num - 1);
			break;
		case "display":

			Logic.CRUD.displayTasks();
			break;
		case "clear":
			Logic.CRUD.clearTasks();
			System.out.println(CLEAR_MSG + fileName_);
			break;
		case "sort": // by alphabetical order
			Logic.CRUD.sortTasksAlphabetically();
			System.out.println(SORT_MSG);
			break;
		case "search":
			if (d.equals("-")) {
				Logic.CRUD.searchTasks(s);
			} else {
				Logic.CRUD.searchTask(d);
			}
			break;
		case "mark":
			// Logic.CRUD.mark(s);
			System.out.println(s + MARK_MSG);
			break;
		case "edit":
			// Logic.CRUD.edit(s,d);
			System.out.println(s + EDIT_MSG);
		case "exit":
			Logic.CRUD.saveAndExit();
			break;

		default:
			System.out.println(INVALID_MSG);
			break;
		}

	}
	/*
	 * public static boolean checkDateformat(String s) {
	 * 
	 * Boolean isDate = true; String[] temp = s.split("/"); try { int day =
	 * Integer.parseInt(temp[0]); int month = Integer.parseInt(temp[1]); int
	 * year = Integer.parseInt(temp[2]); } catch (Exception e) { isDate = false;
	 * }
	 * 
	 * return isDate; }
	 */

}