package Logic;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class Head {

	 private static final String WELCOME_MSG_1 = "Welcome to Agendah. ";
	 private static final String WELCOME_MSG_2 = "Agendah is ready for use";
	 private static final String USER_PROMPT = "command: ";
	 private static Scanner sc = new Scanner(System.in);
	 private static String lastCommand = "";
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
//		 System.out.print(ansi().eraseScreen().fgBright(RED));
		 UI.ui.print(logo1);
		 UI.ui.print(logo2);
		 UI.ui.print(logo3);
		 UI.ui.print(logo4);
		 UI.ui.print(logo5);
		 UI.ui.print(logo6);
		 UI.ui.print(logo7);
		 UI.ui.print(logo8);

		 UI.ui.print(WELCOME_MSG_1 + WELCOME_MSG_2);
//		 System.out.print(ansi().reset());
		 UI.ui.print("\n");
		 UI.ui.print("Enter \"help\" for instructions.");
		 Logic.crud.displayUncompletedAndFloatingTasks();
		 runProgram();
	 }

	 public static void runProgram() throws IOException, ClassNotFoundException {
		 while (true) {
			 UI.ui.print(USER_PROMPT);
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
			 if(lastCommand.equals("sortp") || lastCommand.equals("sp") != true) {
				 Logic.Sort.sortTasksChronologically();
			 }
			 // save all tasks into the actual file after command is done
			 Logic.Save.saveToFile();		
		 }
	 }

	 //getter method
	 public static String getLastCommand() {
		 return lastCommand;
	 }
 }
