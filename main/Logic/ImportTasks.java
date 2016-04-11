//@@author Jie Wei
package Logic;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	
	private static String storageFolderDirectory;
	
	private static final String FEEDBACK_CHANGE_DIRECTORY_DEFAULT = "The default folder is now in use as the storage folder directory";
	private static final String FEEDBACK_CHANGE_DIRECTORY_ERROR = "The target directory is invalid. The default folder is now in use";
	private static final String FEEDBACK_CHANGE_DIRECTORY_SUCCESS = "The storage folder directory has been changed to ";
	private static final String FLAG_COMPLETED = "completed";
	private static final String FLAG_FLOATING = "floating";
	private static final String FLAG_UNCOMPLETED = "uncompleted";
	private static final String STORAGE_FILENAME_COMPLETED_TASKS = "TasksCompleted.txt";
	private static final String STORAGE_FILENAME_FLOATING_TASKS = "TasksFloating.txt";
	private static final String STORAGE_FILENAME_UNCOMPLETED_TASKS = "TasksUncompleted.txt";
	private static final String STORAGE_FOLDER_TEXT_FILE = "StorageFolderPath.txt";
	private static final String STRING_BACKSLASH = "\\";
	private static final String STRING_DEFAULT = "default";
	private static final String STRING_EMPTY = "";
	
	private Crud crudObject;
	
	public ImportTasks(){
		crudObject = Crud.getInstance();
	}
	
	/**
	 * Method to check (and create) storage files, and import tasks from them into program instance.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void prepareAndImportFiles() throws FileNotFoundException, IOException, ClassNotFoundException {
		File StorageFolderPathFile = new File(STORAGE_FOLDER_TEXT_FILE);
		if (!StorageFolderPathFile.exists()) {
			StorageFolderPathFile.createNewFile();
		}
		
		Scanner sc = new Scanner(StorageFolderPathFile);
		try {
			storageFolderDirectory = sc.nextLine();
		} catch (NoSuchElementException e) { // empty file (no directory specified)
			storageFolderDirectory = STRING_EMPTY;
		}
		sc.close();
		
		if (storageFolderDirectory.equals(STRING_DEFAULT)) {
			storageFolderDirectory = STRING_EMPTY;
		}
		
		checkIfFileExists(storageFolderDirectory, STORAGE_FILENAME_UNCOMPLETED_TASKS);
		checkIfFileExists(storageFolderDirectory, STORAGE_FILENAME_COMPLETED_TASKS);
		checkIfFileExists(storageFolderDirectory, STORAGE_FILENAME_FLOATING_TASKS);
		
		importTasksFromStorage(storageFolderDirectory + STORAGE_FILENAME_UNCOMPLETED_TASKS, FLAG_UNCOMPLETED);
		importTasksFromStorage(storageFolderDirectory + STORAGE_FILENAME_COMPLETED_TASKS, FLAG_COMPLETED);
		importTasksFromStorage(storageFolderDirectory + STORAGE_FILENAME_FLOATING_TASKS, FLAG_FLOATING);
	}

	/**
	 * Method to check whether the storage file exists. If not, creates one.
	 * 
	 * @param folderDirectory The destination storage folder.
	 * @param fileName        The destination storage file.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void checkIfFileExists(String folderDirectory, String fileName) throws IOException, FileNotFoundException {
		File file = new File(folderDirectory + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) { // occurs when the specified folder directory does not exist
				file = new File(fileName);
				file.createNewFile();
				storageFolderDirectory = STRING_EMPTY; // replace the folder directory to the source folder
			}
		}
	}

	/**
	 * Function to import tasks from a given storage file
	 * 
	 * @param fileName String that contains the name of the storage file.
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	private void importTasksFromStorage(String fileName, String flag) throws IOException, ClassNotFoundException {
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
				crudObject.addTaskViaImport(t, flag);
			}
		} catch (EOFException e) {
			return;
		}
	}
	
	// Getter methods
	public String getUncompletedTasksStorageFileName() {
		return storageFolderDirectory + STORAGE_FILENAME_UNCOMPLETED_TASKS;
	}
	
	public String getCompletedTasksStorageFileName() {
		return storageFolderDirectory + STORAGE_FILENAME_COMPLETED_TASKS;
	}
	
	public String getFloatingTasksStorageFileName() {
		return storageFolderDirectory + STORAGE_FILENAME_FLOATING_TASKS;
	}
	
	public String getFolderDirectory() {
		return storageFolderDirectory;
	}
	
	/**
	 * Method to change storage folder directory. Uses source folder if invalid directory detected.
	 * 
	 * @param destination The folder path entered by the user.
	 * @return            Feedback regarding outcome of directory change.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public String changeStorageDestination(String destination) throws IOException, ClassNotFoundException {
		storageFolderDirectory = checkAndSetDirectoryName(destination);
		
		checkIfFileExists(storageFolderDirectory, STORAGE_FILENAME_UNCOMPLETED_TASKS);
		checkIfFileExists(storageFolderDirectory, STORAGE_FILENAME_COMPLETED_TASKS);
		checkIfFileExists(storageFolderDirectory, STORAGE_FILENAME_FLOATING_TASKS);
		
		// if the new storage directory already contains the storage files,
		// all tasks (unless duplicate) in these files will be imported into the current program instance
		importTasksFromStorage(storageFolderDirectory + STORAGE_FILENAME_UNCOMPLETED_TASKS, FLAG_UNCOMPLETED);
		importTasksFromStorage(storageFolderDirectory + STORAGE_FILENAME_COMPLETED_TASKS, FLAG_COMPLETED);
		importTasksFromStorage(storageFolderDirectory + STORAGE_FILENAME_FLOATING_TASKS, FLAG_FLOATING);
		
		if (destination.equals(STRING_DEFAULT)) { // default directory was chosen
			writeNewDirectory(destination);
			return FEEDBACK_CHANGE_DIRECTORY_DEFAULT;
		}
		
		if (storageFolderDirectory.equals(STRING_EMPTY)) { // empty directory was given
			writeNewDirectory(STRING_DEFAULT);
			return FEEDBACK_CHANGE_DIRECTORY_ERROR;
		}
		
		if (!destination.contains(STRING_BACKSLASH)) { // does not contain \, thus not folder in Windows environment
			writeNewDirectory(STRING_DEFAULT);
			return FEEDBACK_CHANGE_DIRECTORY_ERROR;
		}
		
		writeNewDirectory(destination);
		return FEEDBACK_CHANGE_DIRECTORY_SUCCESS + storageFolderDirectory; // directory successfully changed
	}
	
	/**
	 * Method to check the given folder directory and assign it appropriately.
	 * 
	 * @param destination The directory to be checked.
	 * @return            The directory to be used.
	 */
	private String checkAndSetDirectoryName(String destination) {
		if (destination.equals(STRING_DEFAULT)) {
			return STRING_EMPTY;
		} else if (!destination.contains(STRING_BACKSLASH)) { // does not contain \, thus not folder on Windows
			return STRING_EMPTY;
		} else {
			return destination;
		}
	}
	
	/**
	 * Method to write the given directory path to the StorageFolderPath.txt file.
	 * 
	 * @param directory The target directory to be written.
	 * @throws IOException
	 */
	private void writeNewDirectory(String directory) throws IOException {
		File StorageFolderPathFile = new File(STORAGE_FOLDER_TEXT_FILE);
		FileWriter writer = new FileWriter(StorageFolderPathFile);
		writer.write(directory);
		writer.close();
	}
}

class LoggerTry {
	private static FileHandler fileHandler = null;

	public static void startLog() throws SecurityException, IOException {
		fileHandler = new FileHandler("loggerFile.log", true);
		Logger logger = Logger.getLogger("");
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
		logger.setLevel(Level.CONFIG);
	}
}
