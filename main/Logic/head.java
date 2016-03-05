package Logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class head {

	private static final String WELCOME_MSG_1 = "Welcome to Agendah. ";
	private static final String WELCOME_MSG_2 = "Agendah is ready for use";
	private static final String USER_PROMPT = "command: ";
	private static String storageFileName = "storage.ser";
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File file = new File(storageFileName);
		checkIfFileExistsAndImportIfExists(file);
		UI.ui.print(WELCOME_MSG_1 +"/n"+ WELCOME_MSG_2);
		runProgram();
	}

	public static void runProgram() throws IOException {
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
			
			Parser.parser.run(cmd, description);
			// save all tasks into the actual file after command is done
			Logic.save.saveFile(storageFileName);
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
		Logic.importTasks.importTasksFromStorage(storageFileName);
	}
}
