//@@author Jie Wei

package Logic;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Storage.LocalStorage;
import Task.Task;

public class Save {
	
	private ImportTasks importTasksObject;
	private LocalStorage localStorageObject;

	public Save() {
		importTasksObject = new ImportTasks();
		localStorageObject = LocalStorage.getInstance();
	}

	/**
	 * Method to save all 3 storage arraylists into their respective save files.
	 * 
	 * @throws IOException
	 */
	public void saveToFile() throws IOException {
		// Save uncompleted tasks
		String UncompletedTasksStorageFIleName = importTasksObject.getUncompletedTasksStorageFileName();
		saveArrayToFile(localStorageObject.getUncompletedTasks(), UncompletedTasksStorageFIleName);

		// Save completed tasks
		String CompletedTasksStorageFileName = importTasksObject.getCompletedTasksStorageFileName();
		saveArrayToFile(localStorageObject.getCompletedTasks(), CompletedTasksStorageFileName);

		// Save floating tasks
		String FloatingTasksStorageFileName = importTasksObject.getFloatingTasksStorageFileName();
		saveArrayToFile(localStorageObject.getFloatingTasks(), FloatingTasksStorageFileName);
	}

	/**
	 * Method to save the contents of an arraylist into a Gson text file.
	 * 
	 * @param sourceArray         The arraylist to be saved.
	 * @param destinationFileName The name to save the file as.
	 * @throws IOException
	 */
	private void saveArrayToFile(ArrayList<Task> sourceArray, String destinationFileName) throws IOException {
		Writer writer = new FileWriter(destinationFileName);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(sourceArray, writer);
		writer.close();
	}
}