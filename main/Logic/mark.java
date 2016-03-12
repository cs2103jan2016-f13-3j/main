package Logic;

import java.io.IOException;

import Task.Task;

public class mark {

	/**
	 * Function to mark tasks as completed
	 * @throws IOException 
	 */
	public static void markTaskAsCompleted(int index) throws IOException {
		Task temp = Storage.localStorage.getUncompletedTask(index);
		temp.setComplete();
		Storage.localStorage.setUncompletedTask(index, temp);
		switchUncompletedTaskToCompletedList(index);
	}
	
	/**
	 * Function to remove an uncompleted task and put it in completed tasks list
	 *  
	 * @param index the index of the task to be switched
	 * @throws IOException
	 */
	public static void switchUncompletedTaskToCompletedList(int index) throws IOException {
		Task temp = Storage.localStorage.delFromUncompletedTasks(index);
		Storage.localStorage.addToCompletedTasks(temp);
	}
	
	public static void markTaskAsUncompleted(int index) throws IOException {
		Task temp = Storage.localStorage.getUncompletedTask(index);
		temp.setUncomplete();
		Storage.localStorage.setCompletedTask(index, temp);		
	}
	
	public static void switchCompletedTaskToUncompletedList(int index) throws IOException {
		Task temp = Storage.localStorage.delFromCompletedTasks(index);
		Storage.localStorage.addToUncompletedTasks(temp);
	}
}
