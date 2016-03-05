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

		for(int i =0; i<temp.size()-1; i++) {
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
}
