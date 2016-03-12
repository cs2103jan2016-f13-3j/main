package Logic;

import java.util.ArrayList;

import Task.Task;

public class sort {

	/**
	 * Function to sorts tasks in storage alphabetically
	 * 
	 */
	public static void sortTasksAlphabetically(){
		ArrayList<Task> temp = Storage.localStorage.getArrayList();

		for(int i = 0; i<temp.size()-1; i++) {
			for(int j = i+1; j<temp.size(); j++) {
				int result = temp.get(i).getIssue().compareTo(temp.get(j).getIssue());
				if(result > 0) {
					Task setTask = temp.get(i);
					temp.set(i, temp.get(j));
					temp.set(j, setTask);
				}
			}
		}
		Storage.localStorage.setArrayList(temp);
	}

	/**
	 * Function to sort tasks in chronological order
	 */
	public static void sortTasksChronologically() {
		ArrayList<Task> tempTasks = Storage.localStorage.getArrayList();

		for(int i = 0; i<tempTasks.size() - 1; i++) {
			for(int j = 1; j<tempTasks.size(); j++) {
				if((tempTasks.get(i).getDate() != null) && (tempTasks.get(j).getDate() != null)) {
					int result = tempTasks.get(i).getDate().compareTo(tempTasks.get(j).getDate());
					if(result > 0) {
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}
			}
		}
		Storage.localStorage.setArrayList(tempTasks);
	}
}
