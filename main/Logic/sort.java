package Logic;
import java.io.IOException;

/**
 * @author Kowshik
 */

import java.util.ArrayList;
import Task.Task;

public class sort {

	/**
	 * Function to sorts tasks in storage alphabetically
	 * 
	 */
	public static void sortTasksAlphabetically(){
		ArrayList<Task> temp = Storage.localStorage.getUncompletedTasks();

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
		try {
			Storage.localStorage.setArrayList(temp);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Function to sort tasks in chronological order
	 */
	public static void sortTasksChronologically() {
		ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();	
		for(int i = 0; i<tempTasks.size(); i++) {
			for(int j = i+1; j<tempTasks.size(); j++) {
				if(tempTasks.get(i).getDate().compareTo(tempTasks.get(j).getDate()) > 0) {
					Task temp = tempTasks.get(i);
					tempTasks.set(i, tempTasks.get(j));
					tempTasks.set(j, temp);
				}
			}
		}
		try {
			Storage.localStorage.setArrayList(tempTasks);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Function to sort tasks according to priority
	 */
	public static void sortTasksPriority() {
		ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		
		for(int i = 0; i<tempTasks.size(); i++) {
			for(int j = i+1; j<tempTasks.size(); j++) {
				if(tempTasks.get(i).getPriority().equals("high") && tempTasks.get(j).getPriority().equals("low")) {
					Task temp = tempTasks.get(i);
					tempTasks.set(i, tempTasks.get(j));
					tempTasks.set(j, temp);
				}
			}
		}
	}
}


