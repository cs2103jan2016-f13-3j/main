package Parser;
import Logic.CRUD;
import java.util.*;
import java.io.*;
public class parser {

	private static Scanner sc;
	private static String fileName_;
	private static Date date;
	private static final String WELCOME_MSG_1 = "Welcome to TextBuddy. ";
	private static final String WELCOME_MSG_2 = " is ready for use";
	private static final String EMPTY_MSG = " is empty";
	private static final String CLEAR_MSG = "all content deleted from ";
	private static final String SORT_MSG = "all items are sorted in alphabetical order";
	private static final String EDIT_MSG = " is edited and saved";
	private static final String MARK_MSG = " is marked as completed";
	private static final String INVALID_MSG = "Invalid inputs! Please try again";


/*	public parser(String name) {
		sc = new Scanner(System.in);
		fileName_ = name;
		date = new Date();
	}
*/
	public static void main(String[] args) {
		// Vector is used to store the words in sequence
		 fileName_ = args[0];
			sc = new Scanner(System.in);
			date = new Date();
	
		System.out.println(WELCOME_MSG_1 + fileName_ + WELCOME_MSG_2);
	//	parser a = new parser(fileName);
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
			String content;
			// handle lines with only the command
			if (cmd.length() == input.length()) {
				content = "";
			} else {
				// get string that is not part of the command
				content = input.substring(cmd.length() + 1, input.length());
			}

			// process commands
			parseCommands(cmd, content);
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
	public static void parseCommands(String option, String s) {	
		switch (option) {
		case "add":
			String time = date.toString();
			//add(s.time);
			System.out.println("added to " + fileName_ + ":\"" + s + "\" ");
			break;
		case "delete":
			int num = Integer.parseInt(s);
			//delete(num - 1);
			//	System.out.println("deleted from " + fileName_ + ": \"" + items.get(num-1) + "\"");
			break;
		case "display":
			//display();
			break;
		case "clear":
			//clear();
			System.out.println(CLEAR_MSG + fileName_);
			break;
		case "sort": //by alphabetical order
			//sort();
			System.out.println(SORT_MSG);
			break;
		case "search": 
			//search(s);
			break;
		case "mark":
			//mark(s);
			System.out.println(s + MARK_MSG);
			break;
		case "edit":
			//edit(s);
			System.out.println(s + EDIT_MSG);
		case "exit":
			//exit
			break;


		default:
			System.out.println(INVALID_MSG);
			break;
		}

	}
}