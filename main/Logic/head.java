package Logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class head {

	private static final String WELCOME_MSG_1 = "Welcome to Agendah. ";
	private static final String WELCOME_MSG_2 = "Agendah is ready for use";
	private static final String USER_PROMPT = "command: ";
	private static final String FLAG_UNCOMPLETED = "uncompleted";
	private static final String FLAG_COMPLETED = "completed";
	private static final String FLAG_FLOATING = "floating";
	private static String storageFileNameCompletedTasks = "storageCompleted.ser";
	private static String storageFileNameUncompletedTasks = "storageUncompleted.ser";
	private static String storageFileNameFloatingTasks = "storageFloating.ser";
	private static Scanner sc = new Scanner(System.in);
	private static String lastCommand = "";

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {		
		prepareAndImportFiles();
		UI.ui.print(WELCOME_MSG_1 + WELCOME_MSG_2);
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
			Logic.sort.sortTasksChronologically();
			// save all tasks into the actual file after command is done
			saveToFile();		
		}
	}

	//getter method
	public static String getLastCommand() {
		return lastCommand;
	}

	public static void checkIfFileExists(File f) throws IOException, FileNotFoundException {
		if (!f.exists()) {
			f.createNewFile();
		}
	}

	public static void importTasksFromFiles(File file1, File file2, File file3) throws IOException {
		Logic.importTasks.importTasksFromStorage(file1, FLAG_UNCOMPLETED);
		Logic.importTasks.importTasksFromStorage(file2, FLAG_COMPLETED);
		Logic.importTasks.importTasksFromStorage(file3, FLAG_FLOATING);
	}

	public static void saveToFile() throws IOException {
		Logic.save.saveFile(storageFileNameUncompletedTasks);
		Logic.save.saveFile(storageFileNameCompletedTasks);
		Logic.save.saveFile(storageFileNameFloatingTasks);
	}

	public static void prepareAndImportFiles() throws FileNotFoundException, IOException {
		File UncompletedTasksFile = new File(storageFileNameUncompletedTasks);
		File CompletedTasksFile = new File(storageFileNameCompletedTasks);
		File FloatingTasksFile = new File(storageFileNameFloatingTasks);
		checkIfFileExists(UncompletedTasksFile);
		checkIfFileExists(CompletedTasksFile);
		checkIfFileExists(FloatingTasksFile);
		importTasksFromFiles(UncompletedTasksFile, CompletedTasksFile, FloatingTasksFile);
	}
}
