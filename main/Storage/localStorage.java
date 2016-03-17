package Storage;
/**
 * @author Kowshik
 */
import Task.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class localStorage {

	//ArrayList to store the contents added to the file
	private static ArrayList<Task> uncompletedTasks = new ArrayList<Task>();
	private static ArrayList<Task> completedTasks = new ArrayList<Task>();
	
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
		if(index>=0&&index<uncompletedTasks.size())
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
	
	/**
	 * Function to set a task to a particular index
	 * @param index
	 * @param temp
	 */
	public static void setUncompletedTask(int index, Task temp) {
		uncompletedTasks.set(index, temp);
	}
	
	public static void setCompletedTask(int index, Task temp) {
		completedTasks.set(index, temp);
	}

	/**
	 * Function to assign details array list to given array list
	 * @param changedDetails the arraylist to be assigned to details
	 */
	public static void setArrayList(ArrayList<Task> changedDetails) {
		uncompletedTasks = changedDetails;
	}

	/**
	 * Function to add a task to the uncompleted task list
	 * 
	 * @param task contains the task to be added
	 * @throws IOException 
	 */
	public static void addToUncompletedTasks(Task task) throws IOException {
		uncompletedTasks.add(task);
	}
	
	/**
	 * Function to add a task to the list of completed tasks
	 * 
	 * @param task the task to be added
	 * @throws IOException
	 */
	public static void addToCompletedTasks(Task task) throws IOException {
		completedTasks.add(task);
	}

	/**
	 * Function to delete a task from the file
	 * 
	 * @param index contains the index of the task to be deleted
	 */
	public static Task delFromUncompletedTasks(int index) {
		Task temp = uncompletedTasks.remove(index);
		return temp;
	}

	/**
	 * Function to delete a task from the list of completed tasks
	 * 
	 * @param index contains the index of the task to be deleted
	 */
	public static Task delFromCompletedTasks(int index) {
		Task temp = completedTasks.remove(index);
		return temp;
	}

	/**
	 * Function to return the uncompleted list of tasks to Logic
	 * 
	 * @return ArrayList details that contains the list of uncompleted tasks
	 */
	public static ArrayList<Task> displayUncompletedTasks() {
		return uncompletedTasks;
	}
	
	/**
	 * Function to return the completed list of tasks to Logic
	 * 
	 * @return ArrayList details that contains the list of completed tasks
	 */
	public static ArrayList<Task> displayCompletedTasks() {
		return completedTasks;
	}

	/**
	 * Function to clear the contents of the file
	 */
	public static void clear() {
		uncompletedTasks.clear();
		completedTasks.clear();
	}

	
}
