//@@author Kowshik
package Storage;

import java.io.IOException;
import java.util.ArrayList;

import Task.Task;

public class LocalStorage {

	// ArrayLists to store the contents added to the file
	private static ArrayList<Task> uncompletedTasks = new ArrayList<Task>();
	private static ArrayList<Task> completedTasks = new ArrayList<Task>();
	private static ArrayList<Task> floatingTasks = new ArrayList<Task>();

	// getter methods
	public static ArrayList<Task> getUncompletedTasks() {
		return uncompletedTasks;
	}

	public static ArrayList<Task> getCompletedTasks() {
		return completedTasks;
	}

	public static ArrayList<Task> getFloatingTasks() {
		return floatingTasks;
	}

	public static Task getUncompletedTask(int index) {
		Task temp = null;
		for (int i = 0; i < uncompletedTasks.size(); i++) {
			if (i == index) {
				temp = uncompletedTasks.get(i);
			}
		}
		return temp;
	}

	public static Task getCertainUncompletedTask(int index) {
		if (index >= 0 && index < uncompletedTasks.size())
			return uncompletedTasks.get(index);
		else
			return null;
	}

	public static Task getCompletedTask(int index) {
		Task temp = null;
		for (int i = 0; i < completedTasks.size(); i++) {
			if (i == index) {
				temp = completedTasks.get(i);
			}
		}
		return temp;
	}

	public static Task getFloatingTask(int index) {
		Task temp = null;
		for (int i = 0; i < floatingTasks.size(); i++) {
			if (i == index) {
				temp = floatingTasks.get(i);
			}
		}
		return temp;
	}

	// setter methods
	public static void setUncompletedTasks(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		uncompletedTasks = changedDetails;
	}

	public static void setFloatingTasks(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		floatingTasks = changedDetails;
	}

	public static void setCompletedTasks(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		completedTasks = changedDetails;
	}

	public static void setUncompletedTask(int index, Task temp) {
		uncompletedTasks.set(index, temp);
	}

	public static void setCompletedTask(int index, Task temp) {
		completedTasks.set(index, temp);
	}

	public static void setFloatingTask(int index, Task temp) {
		floatingTasks.set(index, temp);
	}

	/**
	 * Function to add a task to the list of uncompleted tasks
	 * 
	 * @param task contains the task to be added
	 */
	public static void addToUncompletedTasks(Task task) {
		uncompletedTasks.add(task);
	}

	/**
	 * Function to add a task to the list of floating tasks
	 * 
	 * @param task contains the task to be added
	 */
	public static void addToFloatingTasks(Task task) {
		floatingTasks.add(task);
	}

	/**
	 * Function to add a task to the list of completed tasks
	 * 
	 * @param task contains the task to be added
	 */
	public static void addToCompletedTasks(Task task) throws IOException, ClassNotFoundException {
		completedTasks.add(task);
	}
	
	/**
	 * Function to delete a task from the list of uncompleted tasks
	 * 
	 * @param index contains the index of the task to be deleted from uncompleted tasks
	 */
	public static Task deleteFromUncompletedTasks(int index) {
		Task temp = uncompletedTasks.remove(index);
		return temp;
	}
	
	/**
	 * Function to delete a task from the list of floating tasks
	 * 
	 * @param index contains the index of the task to be deleted from floating tasks
	 */
	public static Task deleteFromFloatingTasks(int index) {
		Task temp = floatingTasks.remove(index);
		return temp;
	}
	
	/**
	 * Function to delete a task from the list of completed tasks
	 * 
	 * @param index contains the index of the task to be deleted from completed tasks
	 */
	public static Task deleteFromCompletedTasks(int index) {
		Task temp = completedTasks.remove(index);
		return temp;
	}

	/**
	 * Function to clear the contents of the file
	 */
	public static void clearAllTasks() {
		uncompletedTasks.clear();
		completedTasks.clear();
		floatingTasks.clear();

	}

	// @@author Jie Wei
	/**
	 * Function to replace the current tasks arraylists with the given arraylists, to "undo" to the previous state
	 * 
	 * @param previousCompleted
	 * @param previousUncompleted
	 * @param previousFloating
	 */
	public static void revertToPreviousState(ArrayList<Task> previousCompleted, ArrayList<Task> previousUncompleted,
			                                 ArrayList<Task> previousFloating) {
		completedTasks = previousCompleted;
		uncompletedTasks = previousUncompleted;
		floatingTasks = previousFloating;

	}
}
