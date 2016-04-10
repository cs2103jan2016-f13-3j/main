//@@author Cheng Gee
package Logic;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.fusesource.jansi.AnsiConsole;

import Parser.Parser;
import UI.UI;

public class Head {

	private static String lastDisplay = "";
	private static String lastDisplayArg = "";
	private static UI uiObject = new UI();
	private static Parser parserObject = Parser.getInstance();
	private static Core coreObject = Core.getInstance();
	private static Sort sortObject = new Sort();

	private static final String USER_PROMPT = "command: ";
	private static final String WELCOME_HELP = "Enter \"help\" for instructions.\n";
	private static final String WELCOME_MSG_1 = "Welcome to Agendah. ";
	private static final String WELCOME_MSG_2 = "Agendah is ready for use";

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		ImportTasks importTaskObject = new ImportTasks();
		importTaskObject.prepareAndImportFiles();

		AnsiConsole.systemInstall();

		uiObject.printLogo();

		uiObject.printYellow(WELCOME_MSG_1 + WELCOME_MSG_2);

		uiObject.print("\n");
		uiObject.printYellow(WELCOME_HELP);

		Logic.Notification.welcomeReminder();

		runProgram();
	}

	/**
	 * Main program loop.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void runProgram() throws IOException, ClassNotFoundException {
		Save arrayListsSaver = new Save();

		while (true) {
			uiObject.printRed(USER_PROMPT);
			coreObject.acceptCommand();

			lastDisplay = parserObject.getCommand();
			lastDisplayArg = parserObject.getDescription();

			sortObject.sortTasksPriority();

			// save all arraylists (and their tasks) into respective files after each command is done
			arrayListsSaver.saveToFile();		
		}
	}

	/**
	 * Method to get first word of the latest executed command for keeping track of the last display view.
	 * 
	 * @return First word of the latest executed command.
	 */
	public static String getLastDisplay() {
		return lastDisplay;
	}

	/**
	 * Method to get arguments (if any) of the latest executed command for keeping track of the last display view.
	 * 
	 * @return Second word onwards (if any) of the latest executed command.
	 */
	public static String getLastDisplayArg() {
		return lastDisplayArg;
	}
}
