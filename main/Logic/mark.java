package Logic;

/**
 * @author Kowshik
 */
import java.io.IOException;
import java.util.ArrayList;

import Storage.localStorage;
import Task.Task;

public class mark {

	/**
	 * Function to mark tasks as completed
	 * 
	 * @param index the index of the task to be marked as completed
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void markTaskAsCompleted(int index) throws IOException, ClassNotFoundException {
		ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();

		if(index < getSize.size()) {
			Task temp = Storage.localStorage.getUncompletedTask(index);
			temp.setComplete();
			Storage.localStorage.delFromUncompletedTasks(index);
			Storage.localStorage.addToCompletedTasks(temp);
		}
		else {
			Task temp = Storage.localStorage.getFloatingTask(index - getSize.size());
			temp.setComplete();
			Storage.localStorage.delFromFloatingTasks(index - getSize.size());
			Storage.localStorage.addToCompletedTasks(temp);
		}
	}

	/**
	 * Function to mark a task as uncompleted
	 * 
	 * @param index the index of the task to be marked as uncompleted
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void markTaskAsUncompleted(int index) throws IOException, ClassNotFoundException {
		Task temp = Storage.localStorage.getUncompletedTask(index);
		temp.setUncomplete();
		Storage.localStorage.delFromCompletedTasks(index);
		Storage.localStorage.addToUncompletedTasks(temp);
	}

	/**
	 * Function to set the priority for a task
	 * 
	 * @param index the index of the task to be updated
	 * @param priority the priority to be set for the task
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void setPriority(int index, String priority) throws ClassNotFoundException, IOException {
		//		localStorage.copyCurrentState();
		Task temp = Storage.localStorage.getUncompletedTask(index);
		temp.setPriority(priority);
		Storage.localStorage.setUncompletedTask(index, temp);
	}
}
