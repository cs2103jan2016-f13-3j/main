//@@author Cheng Gee
package Logic;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.fusesource.jansi.AnsiConsole;
public class Head {

	private static String lastDisplay = "";
	private static String lastDisplayArg = "";

	private static final String LOGO_LINE_1 = "********   ********   ********  **       **   *****       ********   **    **";
	private static final String LOGO_LINE_2 = "********   ********   ********  ***      **   **   **     ********   **    **";
	private static final String LOGO_LINE_3 = "**    **   **         **        ****     **   **    **    **    **   **    **";
	private static final String LOGO_LINE_4 = "**    **   **         ******    *** **   **   **     **   **    **   ********";
	private static final String LOGO_LINE_5 = "********   **   ***   ******    ***  **  **   **     **   ********   ********";
	private static final String LOGO_LINE_6 = "********   **    **   **        ***   ** **   **     **   ********   **    **";
	private static final String LOGO_LINE_7 = "**    **   ********   ********  ***    ****   **    **    **    **   **    **";
	private static final String LOGO_LINE_8 = "**    **   ********   ********  ***     ***   *******     **    **   **    **";
	private static final String USER_PROMPT = "command: ";
	private static final String WELCOME_HELP = "Enter \"help\" for instructions.\n";
	private static final String WELCOME_MSG_1 = "Welcome to Agendah. ";
	private static final String WELCOME_MSG_2 = "Agendah is ready for use";

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {		
		Logic.ImportTasks.prepareAndImportFiles();
		AnsiConsole.systemInstall();

		printLogo();

		UI.ui.printYellow(WELCOME_MSG_1 + WELCOME_MSG_2);

		UI.ui.print("\n");
		UI.ui.printYellow(WELCOME_HELP);
		Logic.Notification.welcomeReminder();
		runProgram();
	}

	/**
	 * Method to print the welcome logo of Agendah.
	 */
	private static void printLogo() {
		UI.ui.eraseScreen();
		UI.ui.printGreen(LOGO_LINE_1);
		UI.ui.printGreen(LOGO_LINE_2);
		UI.ui.printGreen(LOGO_LINE_3);
		UI.ui.printGreen(LOGO_LINE_4);
		UI.ui.printGreen(LOGO_LINE_5);
		UI.ui.printGreen(LOGO_LINE_6);
		UI.ui.printGreen(LOGO_LINE_7);
		UI.ui.printGreen(LOGO_LINE_8);
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
			UI.ui.printRed(USER_PROMPT);
			Logic.Core.acceptCommand();

			lastDisplay = Parser.Parser.getCommand();
			lastDisplayArg = Parser.Parser.getDescription();

			Logic.Sort.sortTasksPriority();

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
