package Storage;
/**
 * @author Kowshik
 */
import Task.Task;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class localStorage {

	//ArrayList to store the contents added to the file
	private static ArrayList<Task> uncompletedTasks = new ArrayList<Task>();
	private static ArrayList<Task> completedTasks = new ArrayList<Task>();
	private static ArrayList<Task> floatingTasks = new ArrayList<Task>();

	/**
	 * Function to return the ArrayList details
	 * @return the ArrayList details
	 */
	public static ArrayList<Task> getUncompletedTasks() {
		return uncompletedTasks;
	}

	public static ArrayList<Task> getCompletedTasks() {
		return completedTasks;
	}
	
	public static ArrayList<Task> getFloatingTasks() {
		return floatingTasks;
	}

	/**
	 * Function to get a particular task from the list of tasks
	 * 
	 * @param index the index of the task that is required
	 * 
	 * @return Task task of specified index
	 */
	public static Task getUncompletedTask(int index) {
		Task temp = null;
		for(int i = 0; i<uncompletedTasks.size(); i++) {
			if(i == index) {
				temp = uncompletedTasks.get(i);
			}
		}
		return temp;
	}

	public static Task getCertainUncompletedTask(int index) {
		Task temp = null;
		if(index >= 0&& index < uncompletedTasks.size())
			return uncompletedTasks.get(index);
		else
			return temp;
	}

	public static Task getCompletedTask(int index) {
		Task temp = null;
		for(int i = 0; i<completedTasks.size(); i++) {
			if(i == index) {
				temp = completedTasks.get(i);
			}
		}
		return temp;
	}
	
	public static Task getFloatingTask(int index) {
		Task temp = null;
		for(int i = 0; i<floatingTasks.size(); i++) {
			if(i == index) {
				temp = floatingTasks.get(i);
			}
		}
		return temp;
	}

	/**
	 * Function to set a task to a particular index
	 * @param index
	 * @param temp
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
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
	 * Function to assign details array list to given array list
	 * @param changedDetails the arraylist to be assigned to details
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void setArrayList(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		uncompletedTasks = changedDetails;
	}

	/**
	 * Function to add a task to the uncompleted task list
	 * 
	 * @param task contains the task to be added
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void addToUncompletedTasks(Task task) {
		uncompletedTasks.add(task);
	}

	/**
	 * Function to add a task to the list of completed tasks
	 * 
	 * @param task the task to be added
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void addToCompletedTasks(Task task) throws IOException, ClassNotFoundException {
		completedTasks.add(task);
	}
	
	public static void addToFloatingTasks(Task task) {
		floatingTasks.add(task);
	}

	/**
	 * Function to delete a task from the file
	 * 
	 * @param index contains the index of the task to be deleted
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static Task delFromUncompletedTasks(int index) {
		Task temp = uncompletedTasks.remove(index);
		return temp;
	}

	/**
	 * Function to delete a task from the list of completed tasks
	 * 
	 * @param index contains the index of the task to be deleted
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static Task delFromCompletedTasks(int index) {
		Task temp = completedTasks.remove(index);
		return temp;
	}
	
	public static Task delFromFloatingTasks(int index) {
		Task temp = floatingTasks.remove(index);
		return temp;
	}

	/**
	 * Function to clear the contents of the file
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void clear() {
		uncompletedTasks.clear();
		completedTasks.clear();
		floatingTasks.clear();
	}

	// replace the current tasks arraylists with the given arraylists, to "undo" to the previous state
	public static void revertToPreviousState(ArrayList<Task> previousCompleted, 
											 ArrayList<Task> previousUncompleted,
											 ArrayList<Task> previousFloating) {
		completedTasks = previousCompleted;
		uncompletedTasks = previousUncompleted;
		floatingTasks = previousFloating;
	}
}
