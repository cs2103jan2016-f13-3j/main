//@@author Kowshik
package Logic;

import java.io.IOException;
import java.util.ArrayList;

import Storage.LocalStorage;
import Task.Task;

public class Mark {

	private LocalStorage localStorageObject;

	//Constructor.
	public Mark() {
		localStorageObject = LocalStorage.getInstance();
	}

	/**
	 * Function to mark tasks as completed.
	 * 
	 * @param index                  the index of the task to be marked as completed.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void markTaskAsCompleted(int index) throws IOException, ClassNotFoundException {
		ArrayList<Task> getSize = localStorageObject.getUncompletedTasks();

		if (index < getSize.size()) {
			Task temp = localStorageObject.getUncompletedTask(index);
			temp.setComplete();
			localStorageObject.deleteFromUncompletedTasks(index);
			localStorageObject.addToCompletedTasks(temp);
		} else {
			Task temp = localStorageObject.getFloatingTask(index - getSize.size());
			temp.setComplete();
			localStorageObject.deleteFromFloatingTasks(index - getSize.size());
			localStorageObject.addToCompletedTasks(temp);
		}
	}

	/**
	 * Function to mark a task as uncompleted.
	 * 
	 * @param index                   the index of the task to be marked as uncompleted.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void markTaskAsUncompleted(int index) throws IOException, ClassNotFoundException {
		Task temp = localStorageObject.getCompletedTask(index);

		if (temp.getEndDate() != null || temp.getStartDate() != null) {
			localStorageObject.addToUncompletedTasks(temp);
			localStorageObject.deleteFromCompletedTasks(index);
		} else {
			localStorageObject.addToFloatingTasks(temp);
			localStorageObject.deleteFromCompletedTasks(index);
		}
	}

	/**
	 * Function to set the priority for a task.
	 * 
	 * @param index                   the index of the task to be updated.
	 * @param priority                the priority to be set for the task.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void setPriority(int index, String priority) throws ClassNotFoundException, IOException {
		ArrayList<Task> getSize = localStorageObject.getUncompletedTasks();

		if (index < getSize.size()) {
			Task temp = localStorageObject.getUncompletedTask(index);
			temp.setPriority(priority);
			localStorageObject.setUncompletedTask(index, temp);
		} else {
			Task temp = localStorageObject.getFloatingTask(index - getSize.size());
			temp.setPriority(priority);
			localStorageObject.setFloatingTask(index - getSize.size(), temp);
		}
	}

	/**
	 * Function to set priority for all instances of a recurring task.
	 * 
	 * @param index    the index of the tasks.
	 * @param priority the priority to be set for the task.
	 */
	public void setRecurringTasksPriority(int index, String priority) {
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();

		Task temp = localStorageObject.getUncompletedTask(index);
		String idOfTask = temp.getId();

		checkIfIdMatches(priority, tempTasks, idOfTask);
		
		try {
			localStorageObject.setUncompletedTasks(tempTasks);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to check if the id of the task matches recurring task id.
	 * 
	 * @param priority  the priority to be set for the task.
	 * @param tempTasks the arraylist of tasks.
	 * @param idOfTask  the id of the task.
	 */
	public void checkIfIdMatches(String priority, ArrayList<Task> tempTasks, String idOfTask) {
		if (!idOfTask.equals("")) {
			for (int i = 0; i < tempTasks.size(); i++) {
				if (!tempTasks.get(i).getId().equals("")) {
					if (tempTasks.get(i).getId().equals(idOfTask)) {
						tempTasks.get(i).setPriority(priority);
					}
				}
			}
		}
	}
}
