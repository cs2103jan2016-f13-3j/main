package Logic;

import java.io.IOException;

import Task.Task;

public class mark {

	/**
	 * Function to mark tasks as completed
	 * @throws IOException 
	 */
	public static void markTaskAsCompleted(int index) throws IOException {
		Task temp = Storage.localStorage.getTask(index);
		temp.setComplete();
		Storage.localStorage.setTask(index, temp);
		switchCompletedTaskToCompletedList(index);
	}
	
	/**
	 * Function to remove an uncompleted task and put it in completed tasks list
	 *  
	 * @param index the index of the task to be switched
	 * @throws IOException
	 */
	public static void switchCompletedTaskToCompletedList(int index) throws IOException {
		Task temp = Storage.localStorage.delFromUncompletedTasks(index);
		Storage.localStorage.addToCompletedTasks(temp);
	}
}
