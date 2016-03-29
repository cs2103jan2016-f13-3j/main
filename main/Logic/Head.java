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
	 private static final String FLAG_UNCOMPLETED = "uncompleted";
	 private static final String FLAG_COMPLETED = "completed";
	 private static final String FLAG_FLOATING = "floating";
	 private static String storageFileNameCompletedTasks = "TasksCompleted.txt";
	 private static String storageFileNameUncompletedTasks = "TasksUncompleted.txt";
	 private static String storageFileNameFloatingTasks = "TasksFloating.txt";
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
		 prepareAndImportFiles();
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
			 /*if(lastCommand.equals("sortp") || lastCommand.equals("sp") != true) {
				 Logic.sort.sortTasksChronologically();
			 }*/
			 // save all tasks into the actual file after command is done
			 saveToFile();		
		 }
	 }

	 //getter method
	 public static String getLastCommand() {
		 return lastCommand;
	 }

	 // creates the necessary storage files if they do not exist
	 // and import tasks from they, if any
	 public static void prepareAndImportFiles() throws FileNotFoundException, IOException, ClassNotFoundException {
		 File UncompletedTasksFile = new File(storageFileNameUncompletedTasks);
		 File CompletedTasksFile = new File(storageFileNameCompletedTasks);
		 File FloatingTasksFile = new File(storageFileNameFloatingTasks);
		 checkIfFileExists(UncompletedTasksFile);
		 checkIfFileExists(CompletedTasksFile);
		 checkIfFileExists(FloatingTasksFile);
		 importTasksFromFiles(storageFileNameUncompletedTasks, storageFileNameCompletedTasks, storageFileNameFloatingTasks);
	 }

	 public static void checkIfFileExists(File f) throws IOException, FileNotFoundException {
		 if (!f.exists()) {
			 f.createNewFile();
		 }
	 }

	 public static void importTasksFromFiles(String fileName1, String fileName2, String fileName3) throws IOException, ClassNotFoundException {
		 Logic.ImportTasks.importTasksFromStorage(fileName1, FLAG_UNCOMPLETED);
		 Logic.ImportTasks.importTasksFromStorage(fileName2, FLAG_COMPLETED);
		 Logic.ImportTasks.importTasksFromStorage(fileName3, FLAG_FLOATING);
	 }

	 public static void saveToFile() throws IOException {
		 Logic.Save.saveUncompletedTasksToFile(storageFileNameUncompletedTasks);
		 Logic.Save.saveCompletedTasksToFile(storageFileNameCompletedTasks);
		 Logic.Save.saveFloatingTasksToFile(storageFileNameFloatingTasks);
	 }
 }