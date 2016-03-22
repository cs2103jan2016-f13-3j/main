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
	private static ArrayList<Task> formerCompletedTasks, formerUncompletedTasks;
	private static boolean changesWereMade = false;
	
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
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void setUncompletedTask(int index, Task temp) throws ClassNotFoundException, IOException {
//		copyCurrentState();
		uncompletedTasks.set(index, temp);
		changesWereMade = true;
	}
	
	public static void setCompletedTask(int index, Task temp) throws ClassNotFoundException, IOException {
//		copyCurrentState();
		completedTasks.set(index, temp);
		changesWereMade = true;
	}

	/**
	 * Function to assign details array list to given array list
	 * @param changedDetails the arraylist to be assigned to details
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void setArrayList(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
//		copyCurrentState();
		uncompletedTasks = changedDetails;
		changesWereMade = true;
	}

	/**
	 * Function to add a task to the uncompleted task list
	 * 
	 * @param task contains the task to be added
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void addToUncompletedTasks(Task task) throws IOException, ClassNotFoundException {
		copyCurrentState();
		uncompletedTasks.add(task);
		changesWereMade = true;
	}
	
	/**
	 * Function to add a task to the list of completed tasks
	 * 
	 * @param task the task to be added
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void addToCompletedTasks(Task task) throws IOException, ClassNotFoundException {
		copyCurrentState();
		completedTasks.add(task);
		changesWereMade = true;
	}

	/**
	 * Function to delete a task from the file
	 * 
	 * @param index contains the index of the task to be deleted
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static Task delFromUncompletedTasks(int index) throws ClassNotFoundException, IOException {
		copyCurrentState();
		Task temp = uncompletedTasks.remove(index);
		changesWereMade = true;
		return temp;
	}

	/**
	 * Function to delete a task from the list of completed tasks
	 * 
	 * @param index contains the index of the task to be deleted
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static Task delFromCompletedTasks(int index) throws ClassNotFoundException, IOException {
		copyCurrentState();
		Task temp = completedTasks.remove(index);
		changesWereMade = true;
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
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void clear() throws ClassNotFoundException, IOException {
		copyCurrentState();
		uncompletedTasks.clear();
		completedTasks.clear();
		changesWereMade = true;
	}
	
	public static void copyCurrentState() throws IOException, ClassNotFoundException {
		copyCurrentCompletedTasks();
		copyCurrentUncompletedTasks();
	}
	
	public static ArrayList<Task> getFormerCompletedTasks() {
		return formerCompletedTasks;
	}
	
	public static ArrayList<Task> getFormerUncompletedTasks() {
		return formerUncompletedTasks;
	}
	
	public static void copyCurrentCompletedTasks() throws IOException, ClassNotFoundException {
		// copies completed tasks for storing into undo's history
				ArrayList<Task> completedCopy = new ArrayList<Task>(completedTasks.size());
				for (Task t : completedTasks) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(bos);
					oos.writeObject(t);
					oos.flush();
					oos.close();
					bos.close();
					byte[] byteData = bos.toByteArray();
					
					// restore
					ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
					Task tempTask = (Task) new ObjectInputStream(bais).readObject();
					completedCopy.add(tempTask);
				}
				formerCompletedTasks = completedCopy;
	}
	
	public static void copyCurrentUncompletedTasks() throws IOException, ClassNotFoundException {
		// copies uncompleted tasks for storing into undo's history
				ArrayList<Task> uncompletedCopy = new ArrayList<Task>(uncompletedTasks.size());
				for (Task t : uncompletedTasks) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(bos);
					oos.writeObject(t);
					oos.flush();
					oos.close();
					bos.close();
					byte[] byteData = bos.toByteArray();
					
					// restore
					ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
					Task tempTask = (Task) new ObjectInputStream(bais).readObject();
					uncompletedCopy.add(tempTask);
				}
				formerUncompletedTasks = uncompletedCopy;
	}
	
	public static void revertToPreviousState(ArrayList<Task> previousCompleted, ArrayList<Task> previousUncompleted) {
		completedTasks = previousCompleted;
		uncompletedTasks = previousUncompleted;
	}
	
	public static boolean hasBeenChanged() {
		return changesWereMade;
	}
	
	public static void setUnchanged() {
		changesWereMade = false;
	}
}
