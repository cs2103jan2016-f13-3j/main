//@@author Jie Wei
package Logic;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import Task.Task;

public class ImportTasks {
	private static String storageFileNameCompletedTasks = "TasksCompleted.txt";
	private static String storageFileNameUncompletedTasks = "TasksUncompleted.txt";
	private static String storageFileNameFloatingTasks = "TasksFloating.txt";
	private static final String FEEDBACK_CHANGE_DIRECTORY_DEFAULT = "The default folder is now in use as the storage folder directory";
	private static final String FEEDBACK_CHANGE_DIRECTORY_ERROR = "The target directory is invalid. The default folder is now in use";
	private static final String FEEDBACK_CHANGE_DIRECTORY_SUCCESS = "The storage folder directory has been changed to ";
	private static final String FLAG_UNCOMPLETED = "uncompleted";
	private static final String FLAG_COMPLETED = "completed";
	private static final String FLAG_FLOATING = "floating";
	private static String storageFolderDirectory;

	private static final Logger logger = Logger.getLogger(Class.class.getName()); 

	
	
	// creates the necessary storage files if they do not exist
	// and import tasks from they, if any
	public static void prepareAndImportFiles() throws FileNotFoundException, IOException, ClassNotFoundException {
		File StorageFolderPathFile = new File("StorageFolderPath.txt");
		if (!StorageFolderPathFile.exists()) {
			StorageFolderPathFile.createNewFile();
		}
		Scanner sc = new Scanner(StorageFolderPathFile);
		try {
			storageFolderDirectory = sc.nextLine();
		} catch (NoSuchElementException e) { // empty file (no directory specified)
			storageFolderDirectory = "";
		}
		sc.close();
		if (storageFolderDirectory.equals("default")) {
			storageFolderDirectory = "";
		}
		
		checkIfFileExists(storageFolderDirectory, storageFileNameUncompletedTasks);
		checkIfFileExists(storageFolderDirectory, storageFileNameCompletedTasks);
		checkIfFileExists(storageFolderDirectory, storageFileNameFloatingTasks);

		
		importTasksFromStorage(storageFolderDirectory + storageFileNameUncompletedTasks, FLAG_UNCOMPLETED);
		importTasksFromStorage(storageFolderDirectory + storageFileNameCompletedTasks, FLAG_COMPLETED);
		importTasksFromStorage(storageFolderDirectory + storageFileNameFloatingTasks, FLAG_FLOATING);
	}

	public static void checkIfFileExists(String folderDirectory, String fileName) throws IOException, FileNotFoundException {
		File file = new File(folderDirectory + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) { // occurs when the specified folder directory does not exist
				file = new File(fileName);
				file.createNewFile();
				storageFolderDirectory = ""; // replace the folder directory to the source folder
			}
		}
	}

	/**
	 * Function to import tasks from a given storage file
	 * 
	 * @param fileName String that contains the name of the storage file
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */

	private static void importTasksFromStorage(String fileName, String flag) throws IOException, ClassNotFoundException {
		//		LoggerTry.startLog();
		JsonReader reader = new JsonReader(new FileReader(fileName));
		ArrayList<Task> GsonObjects = new ArrayList<Task>();
		try {
			reader.beginArray();
			while (reader.hasNext()) {
				Task obj = (new Gson()).fromJson(reader, Task.class);
				GsonObjects.add(obj);
			}
			reader.endArray();
			reader.close();
			for (Task t : GsonObjects) {
				Logic.crud.addTaskViaImport(t, flag);
			}
		} catch (EOFException e) {
			return;
		}
	}
	
	public static String getUncompletedTasksStorageFileName() {
		return storageFolderDirectory + storageFileNameUncompletedTasks;
	}
	
	public static String getCompletedTasksStorageFileName() {
		return storageFolderDirectory + storageFileNameCompletedTasks;
	}
	
	public static String getFloatingTasksStorageFileName() {
		return storageFolderDirectory + storageFileNameFloatingTasks;
	}
	
	public static String getFolderDirectory() {
		return storageFolderDirectory;
	}
	
	public static String changeStorageDestination(String destination) throws IOException, ClassNotFoundException {
		File StorageFolderPathFile = new File("StorageFolderPath.txt");
		FileWriter writer = new FileWriter(StorageFolderPathFile);
		writer.write(destination);
		writer.close();
		if (destination.equals("default")) {
			storageFolderDirectory = "";
		} else {
			storageFolderDirectory = destination;
		}
		checkIfFileExists(storageFolderDirectory, storageFileNameUncompletedTasks);
		checkIfFileExists(storageFolderDirectory, storageFileNameCompletedTasks);
		checkIfFileExists(storageFolderDirectory, storageFileNameFloatingTasks);

		
		// if the new storage directory already contains the storage files,
		//all tasks (unless duplicate) in these files will be imported into the current program instance
		importTasksFromStorage(storageFolderDirectory + storageFileNameUncompletedTasks, FLAG_UNCOMPLETED);
		importTasksFromStorage(storageFolderDirectory + storageFileNameCompletedTasks, FLAG_COMPLETED);
		importTasksFromStorage(storageFolderDirectory + storageFileNameFloatingTasks, FLAG_FLOATING);
		
		if (destination.equals("default")) { // default directory was chosen
			return FEEDBACK_CHANGE_DIRECTORY_DEFAULT;
		}
		if (storageFolderDirectory.equals("")) { // invalid directory was given
			return FEEDBACK_CHANGE_DIRECTORY_ERROR;
		}
		return FEEDBACK_CHANGE_DIRECTORY_SUCCESS + storageFolderDirectory; // directory successfully changed
	}
}

class LoggerTry {
	private final static Logger logger = Logger.getLogger(Class.class.getName());
	private static FileHandler fileHandler = null;

	public static void startLog() throws SecurityException, IOException {
		fileHandler = new FileHandler("loggerFile.log", true);
		Logger logger = Logger.getLogger("");
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
		logger.setLevel(Level.CONFIG);
	}
}
