//@@author Jie Wei

package Logic;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Task.Task;

public class Save {
	
	private ImportTasks importTasksObject;

	public Save() {
		importTasksObject = new ImportTasks();
	}

	/**
	 * Method to save all 3 storage arraylists into their respective save files.
	 * 
	 * @throws IOException
	 */
	public void saveToFile() throws IOException {
		// Save uncompleted tasks
		String UncompletedTasksStorageFIleName = importTasksObject.getUncompletedTasksStorageFileName();
		saveArrayToFile(Storage.LocalStorage.getUncompletedTasks(), UncompletedTasksStorageFIleName);

		// Save completed tasks
		String CompletedTasksStorageFileName = importTasksObject.getCompletedTasksStorageFileName();
		saveArrayToFile(Storage.LocalStorage.getCompletedTasks(), CompletedTasksStorageFileName);

		// Save floating tasks
		String FloatingTasksStorageFileName = importTasksObject.getFloatingTasksStorageFileName();
		saveArrayToFile(Storage.LocalStorage.getFloatingTasks(), FloatingTasksStorageFileName);
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