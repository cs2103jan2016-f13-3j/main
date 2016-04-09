package Logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.fusesource.jansi.AnsiConsole;
public class Head {
	//@@author Cheng Gee
	private static final String WELCOME_MSG_1 = "Welcome to Agendah. ";
	private static final String WELCOME_MSG_2 = "Agendah is ready for use";
	private static final String USER_PROMPT = "command: ";
	private static Scanner sc = new Scanner(System.in);
	private static String lastDisplay = "";
	private static String lastDisplayArg = "";
	private static String logo1="********   ********   ********  **       **   *****       ********   **    **";
	private static String logo2="********   ********   ********  ***      **   **   **     ********   **    **";
	private static String logo3="**    **   **         **        ****     **   **    **    **    **   **    **";
	private static String logo4="**    **   **         ******    *** **   **   **     **   **    **   ********";
	private static String logo5="********   **   ***   ******    ***  **  **   **     **   ********   ********";
	private static String logo6="********   **    **   **        ***   ** **   **     **   ********   **    **";
	private static String logo7="**    **   ********   ********  ***    ****   **    **    **    **   **    **";
	private static String logo8="**    **   ********   ********  ***     ***   *******     **    **   **    **";

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {		
		Logic.ImportTasks.prepareAndImportFiles();
		AnsiConsole.systemInstall();

		//		 System.out.print(ansi().eraseScreen().fgBright(RED));
		//	checkDateAndAdd();
		UI.ui.eraseScreen();
		UI.ui.printGreen(logo1);
		UI.ui.printGreen(logo2);
		UI.ui.printGreen(logo3);
		UI.ui.printGreen(logo4);
		UI.ui.printGreen(logo5);
		UI.ui.printGreen(logo6);
		UI.ui.printGreen(logo7);
		UI.ui.printGreen(logo8);

		UI.ui.printYellow(WELCOME_MSG_1 + WELCOME_MSG_2);

		UI.ui.print("\n");
		UI.ui.printYellow("Enter \"help\" for instructions.");
		Logic.Notification.welcomeReminder();
		runProgram();
	}

	public static void runProgram() throws IOException, ClassNotFoundException {
		while (true) {
			UI.ui.printRed(USER_PROMPT);
			Logic.Core.acceptCommand();
			lastDisplay = Parser.Parser.getCommand();
			lastDisplayArg = Parser.Parser.getDescription();
			


			/*if((lastCommand.equals("sort") && (lastArg.equals("c") || lastArg.equals("chrono")))!= true) {

			 }*/
			Logic.Sort.sortTasksPriority();
			// save all tasks into the actual file after command is done
			Logic.Save.saveToFile();		
		}
	}


	//getter method
	public static String getLastDisplay() {
		return lastDisplay;
	}

	public static String getLastDisplayArg() {
		return lastDisplayArg;
	}
}
