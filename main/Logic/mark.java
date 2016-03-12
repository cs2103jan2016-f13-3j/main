package Logic;

import Task.Task;

public class mark {

	/**
	 * Function to mark tasks as completed
	 */
	public static void markTaskAsCompleted(int index) {
		Task temp = Storage.localStorage.getTask(index);
		temp.setComplete();
		Storage.localStorage.setTask(index, temp);
		switchCompletedTaskToCompletedList(index);
	}
	
	public static void switchCompletedTaskToCompletedList(int index) {
		Task temp = Storage.localStorage.remove(index);
		Storage.localStorage.addToCompletedTasks(temp);
	}
}
