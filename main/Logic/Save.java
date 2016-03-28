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

	/**
	 * Function to save the uncompleted tasks arraylist into the storage file
	 * 
	 */
	public static void saveUncompletedTasksToFile(String fileName) throws IOException {
		saveArrayToFile(Storage.localStorage.getUncompletedTasks(), fileName);
	}

	/**
	 * Function to save the completed tasks arraylist into the storage file
	 * 
	 */
	public static void saveCompletedTasksToFile(String fileName) throws IOException {
		saveArrayToFile(Storage.localStorage.getCompletedTasks(), fileName);
	}

	/**
	 * Function to save the floating tasks arraylist into the storage file
	 * 
	 */
	public static void saveFloatingTasksToFile(String fileName) throws IOException {
		saveArrayToFile(Storage.localStorage.getFloatingTasks(), fileName);
	}

	private static void saveArrayToFile(ArrayList<Task> sourceArray, String destinationFileName) throws IOException {
		Writer writer = new FileWriter(destinationFileName);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(sourceArray, writer);
		writer.close();
	}
}
