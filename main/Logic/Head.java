package Logic;

import static org.fusesource.jansi.Ansi.ansi;

import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
import java.time.YearMonth;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import Storage.localStorage;
import Task.Task;
import org.fusesource.jansi.AnsiConsole;
public class Head {
	//@@author Cheng Gee
	private static final String WELCOME_MSG_1 = "Welcome to Agendah. ";
	private static final String WELCOME_MSG_2 = "Agendah is ready for use";
	private static final String USER_PROMPT = "command: ";
	private static Scanner sc = new Scanner(System.in);
	private static String lastCommand = "";
	private static String lastArg = "";
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
			String input = UI.ui.acceptCommand();
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

			Parser.Parser.run(cmd, description);
			lastCommand = cmd;
			lastArg = description;
			/*if((lastCommand.equals("sort") && (lastArg.equals("c") || lastArg.equals("chrono")))!= true) {

			 }*/
			Logic.Sort.sortTasksPriority();
			// save all tasks into the actual file after command is done
			Logic.Save.saveToFile();		
		}
	}


	//getter method
	public static String getLastCommand() {
		return lastCommand;
	}
	
	public static String getLastArg() {
		return lastArg;
	}
}
