package Parser;

import java.util.*;
import java.io.*;
import Task.Task;

public class parser {
	private static String date;
	private static String storageFileName = "storage.ser";

	private static Scanner sc = new Scanner(System.in);

	private static final String WELCOME_MSG_1 = "Welcome to Agendah. ";
	private static final String WELCOME_MSG_2 = "Agendah is ready for use";
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

	public static void main(String[] args) throws IOException {

		File file = new File(storageFileName);
		checkIfFileExistsAndImportIfExists(file);

		System.out.println(WELCOME_MSG_1 + WELCOME_MSG_2);

		// run to simulate command line interactions
		run();
	}

	/**
	 * method that simulate command line interface that will responds to user's
	 * inputs
	 * @throws IOException 
	 * 
	 */
	public static void run() throws IOException {
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

			// process commands
			parseCommands(cmd, description);
			// save all tasks into the actual file after command is done
			Logic.CRUD.saveFile(storageFileName);
		}
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
				if (Logic.CRUD.checkDateformat(date)) {
					break;
				}
				System.out.println(WRONG_DEADLINE_MSG);
			}

			if (date.equals("-")) {
				Logic.CRUD.addTask(s);
			} else {
				Logic.CRUD.addTask(s, date);
			}
			System.out.println( "\"" + s + "\" " + "is added to the task list.");
		}

		else if(option.equals("delete")) {
			int num = Integer.parseInt(s);
			Logic.CRUD.deleteTask(num - 1);
			System.out.println( "\"" + s + "\" " + "is deleted from the task list.");
		}

		else if(option.equals("display")) {
			Logic.CRUD.displayTasks();
		}

		else if(option.equals("clear")) {
			Logic.CRUD.clearTasks();
			System.out.println(CLEAR_MSG);
		}

		else if(option.equals("sort")) { // by alphabetical order
			Logic.CRUD.sortTasksAlphabetically();
			System.out.println(SORT_MSG);
		}

		else if(option.equals("search")) {
			System.out.println(SEARCH_MSG);
			int temp = sc.nextInt();
			if (temp == 1) {
				Logic.CRUD.searchTasksByIssue(s);
			} else {
				System.out.println(DATE_MSG);
				while (true) {
					//date = sc.nextLine();		
					//System.out.println(date);
					if (Logic.CRUD.checkDateformat(s)) {
						Logic.CRUD.searchTasksByDate(s);
						break;
					} else {
						System.out.println(WRONG_DATE_MSG);
					}
				}
			}		
		}

		else if (option.equals("mark")) {
			// Logic.CRUD.mark(s);
			System.out.println(s + MARK_MSG);
		}

		else if(option.equals("edit")) {
			// Logic.CRUD.edit(s,d);
			System.out.println(s + EDIT_MSG);
		}

		else if(option.equals("exit")) {
			Logic.CRUD.exit();
		}

		else {
			System.out.println(INVALID_MSG);
		}
	}

	public static void checkIfFileExistsAndImportIfExists(File f) throws IOException, FileNotFoundException {
		if (!f.exists()) {
			f.createNewFile();
		} else {
			importTasksFromFile();
		}
	}

	public static void importTasksFromFile() throws IOException {
		Logic.CRUD.importTasks(storageFileName);
	}
}