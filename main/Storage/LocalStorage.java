//@@author Kowshik
package Storage;

import java.io.IOException;
import java.util.ArrayList;

import Task.Task;

public class LocalStorage {

	private static LocalStorage localStorage;

	private ArrayList<Task> uncompletedTasks;
	private ArrayList<Task> floatingTasks;
	private ArrayList<Task> completedTasks;
	
	// Private constructor, following the singleton pattern.
	private LocalStorage() {
		uncompletedTasks = new ArrayList<Task>();
		floatingTasks = new ArrayList<Task>();
		completedTasks = new ArrayList<Task>();
	}

	/**
	 * Method to access this class, following the singleton pattern. Invokes
	 * constructor if LocalStorage has not been initialised.
	 * 
	 * @return The LocalStorage object.
	 */
	public static LocalStorage getInstance() {
		if (localStorage == null) {
			localStorage = new LocalStorage();
		}
		return localStorage;
	}

	// Getter methods.
	public ArrayList<Task> getUncompletedTasks() {
		return uncompletedTasks;
	}

	public ArrayList<Task> getFloatingTasks() {
		return floatingTasks;
	}
	
	public ArrayList<Task> getCompletedTasks() {
		return completedTasks;
	}

	public Task getUncompletedTask(int index) {
		Task temp = null;
		for (int i = 0; i < uncompletedTasks.size(); i++) {
			if (i == index) {
				temp = uncompletedTasks.get(i);
			}
		}
		return temp;
	}

	public Task getCertainUncompletedTask(int index) {
		if (index >= 0 && index < uncompletedTasks.size())
			return uncompletedTasks.get(index);
		else
			return null;
	}
	
	public Task getFloatingTask(int index) {
		Task temp = null;
		for (int i = 0; i < floatingTasks.size(); i++) {
			if (i == index) {
				temp = floatingTasks.get(i);
			}
		}
		return temp;
	}
	
	public Task getCompletedTask(int index) {
		Task temp = null;
		for (int i = 0; i < completedTasks.size(); i++) {
			if (i == index) {
				temp = completedTasks.get(i);
			}
		}
		return temp;
	}


	// Setter methods.
	public void setUncompletedTasks(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		uncompletedTasks = changedDetails;
	}

	public void setFloatingTasks(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		floatingTasks = changedDetails;
	}

	public void setCompletedTasks(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		completedTasks = changedDetails;
	}

	public void setUncompletedTask(int index, Task temp) {
		uncompletedTasks.set(index, temp);
	}

	public void setCompletedTask(int index, Task temp) {
		completedTasks.set(index, temp);
	}

	public void setFloatingTask(int index, Task temp) {
		floatingTasks.set(index, temp);
	}

	/**
	 * Function to add a task to the list of uncompleted tasks
	 * 
	 * @param task contains the task to be added to list of uncompleted tasks
	 */
	public void addToUncompletedTasks(Task task) {
		uncompletedTasks.add(task);
	}

	/**
	 * Function to add a task to the list of floating tasks
	 * 
	 * @param task contains the task to be added to list of floating tasks
	 */
	public void addToFloatingTasks(Task task) {
		floatingTasks.add(task);
	}

	/**
	 * Function to add a task to the list of completed tasks
	 * 
	 * @param task contains the task to be added to list of completed tasks
	 */
	public void addToCompletedTasks(Task task) throws IOException, ClassNotFoundException {
		completedTasks.add(task);
	}

	/**
	 * Function to delete a task from the list of uncompleted tasks
	 * 
	 * @param index contains the index of the task to be deleted from list of uncompleted tasks
	 */
	public Task deleteFromUncompletedTasks(int index) {
		Task temp = uncompletedTasks.remove(index);
		return temp;
	}

	/**
	 * Function to delete a task from the list of floating tasks
	 * 
	 * @param index  contains the index of the task to be deleted from list of floating tasks
	 */
	public Task deleteFromFloatingTasks(int index) {
		Task temp = floatingTasks.remove(index);
		return temp;
	}

	/**
	 * Function to delete a task from the list of completed tasks
	 * 
	 * @param index contains the index of the task to be deleted from list of completed tasks
	 */
	public Task deleteFromCompletedTasks(int index) {
		Task temp = completedTasks.remove(index);
		return temp;
	}

	/**
	 * Function to clear the contents of the file
	 */
	public void clearAllTasks() {
		uncompletedTasks.clear();
		floatingTasks.clear();
		completedTasks.clear();
	}

	// @@author Jie Wei
	/**
	 * Function to replace the current tasks arraylists with the given arraylists, to "undo" to the previous state
	 * 
	 * @param previousCompleted   the old list of completed tasks
	 * @param previousUnCompleted the old list of uncompleted tasks
	 * @param previousFloating    the old list of floating tasks
	 */
	public void revertToPreviousState(ArrayList<Task> previousCompleted, ArrayList<Task> previousUncompleted,
			                          ArrayList<Task> previousFloating) {
		completedTasks = previousCompleted;
		uncompletedTasks = previousUncompleted;
		floatingTasks = previousFloating;
	}
}
