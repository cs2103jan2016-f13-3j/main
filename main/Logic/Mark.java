//@@author Kowshik
package Logic;
import java.io.IOException;
import java.util.ArrayList;

import Storage.LocalStorage;
import Task.Task;

public class Mark {

	/**
	 * Function to mark tasks as completed
	 * 
	 * @param index the index of the task to be marked as completed
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void markTaskAsCompleted(int index) throws IOException, ClassNotFoundException {
		ArrayList<Task> getSize = Storage.LocalStorage.getUncompletedTasks();

		if(index < getSize.size()) {
			Task temp = Storage.LocalStorage.getUncompletedTask(index);
			temp.setComplete();
			Storage.LocalStorage.deleteFromUncompletedTasks(index);
			Storage.LocalStorage.addToCompletedTasks(temp);
		}
		else {
			Task temp = Storage.LocalStorage.getFloatingTask(index - getSize.size());
			temp.setComplete();
			Storage.LocalStorage.deleteFromFloatingTasks(index - getSize.size());
			Storage.LocalStorage.addToCompletedTasks(temp);
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
		Task temp = Storage.LocalStorage.getCompletedTask(index);

		if(temp.getEndDate() != null || temp.getStartDate() != null) {
			Storage.LocalStorage.addToUncompletedTasks(temp);
			Storage.LocalStorage.deleteFromCompletedTasks(index);
		} else {
			Storage.LocalStorage.addToFloatingTasks(temp);
			Storage.LocalStorage.deleteFromCompletedTasks(index);
		}
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
		ArrayList<Task> getSize = Storage.LocalStorage.getUncompletedTasks();

		if(index < getSize.size()) {
			Task temp = Storage.LocalStorage.getUncompletedTask(index);
			temp.setPriority(priority);
			Storage.LocalStorage.setUncompletedTask(index, temp);
		}
		else {
			Task temp = Storage.LocalStorage.getFloatingTask(index - getSize.size());
			temp.setPriority(priority);
			Storage.LocalStorage.setFloatingTask(index - getSize.size(), temp);
		}
	}

	public static void setRecurringTasksPriority(int index, String priority) {
		ArrayList<Task> tempTasks = Storage.LocalStorage.getUncompletedTasks();


		Task temp = Storage.LocalStorage.getUncompletedTask(index);
		String idOfTask = temp.getId();

	if (!idOfTask.equals("")) {
			for(int i = 0; i<tempTasks.size(); i++) {
				if(!tempTasks.get(i).getId().equals("")) {
					if(tempTasks.get(i).getId().equals(idOfTask)) {
						tempTasks.get(i).setPriority(priority);
					}
				}
			}

	}		

			try {
				Storage.LocalStorage.setUncompletedTasks(tempTasks);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	} 
}
