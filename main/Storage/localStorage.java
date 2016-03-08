package Storage;

import Task.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class localStorage {

	//ArrayList to store the contents added to the file
	private static ArrayList<Task> details = new ArrayList<Task>();

	/**
	 * Function to return the ArrayList details
	 * @return the ArrayList details
	 */
	public static ArrayList<Task> getArrayList() {
		return details;
	}
	
	/**
	 * Function to get a particular task from the list of tasks
	 * 
	 * @param index the index of the task that is required
	 * 
	 * @return Task task of specified index
	 */
	public static Task getTask(int index) {
		Task temp = null;
		for(int i = 0; i<details.size(); i++) {
			if(i == index) {
				temp = details.get(i);
			}
		}
		return temp;
	}
	
	/**
	 * Function to set a task to a particular index
	 * @param index
	 * @param temp
	 */
	public static void setTask(int index, Task temp) {
		details.set(index, temp);
	}

	/**
	 * Function to assign details array list to given array list
	 * @param changedDetails the arraylist to be assigned to details
	 */
	public static void setArrayList(ArrayList<Task> changedDetails) {
		details = changedDetails;
	}

	/**
	 * Function to add a task to the file
	 * 
	 * @param line contains the task to be added
	 * @throws IOException 
	 */
	public static void addToStorage(Task task) throws IOException {
		details.add(task);
	}

	/**
	 * Function to delete a task from the file
	 * 
	 * @param index contains the index of the task to be deleted
	 */
	public static void delFromStorage(int index) {
		details.remove(index);
	}

	/**
	 * Function to return the contents of the file to Logic
	 * 
	 * @return ArrayList details that contains the file contents
	 */
	public static ArrayList<Task> displayStorage() {
		return details;
	}

	/**
	 * Function to clear the contents of the file
	 */
	public static void clear() {
		details.clear();
	}
}
