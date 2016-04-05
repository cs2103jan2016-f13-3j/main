//@@author Kowshik
package Logic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import Task.Task;

public class Sort {

	/**
	 * Function to sorts tasks in storage alphabetically
	 * 
	 */
	public static void sortTasksAlphabetically(){
		ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();

		for(int i = 0; i<tempUncompletedTasks.size()-1; i++) {
			for(int j = i+1; j<tempUncompletedTasks.size(); j++) {
				int result = tempUncompletedTasks.get(i).getIssue().compareTo(tempUncompletedTasks.get(j).getIssue());
				if(result > 0) {
					Task setTask = tempUncompletedTasks.get(i);
					tempUncompletedTasks.set(i, tempUncompletedTasks.get(j));
					tempUncompletedTasks.set(j, setTask);
				}
			}
		}
		try {
			Storage.localStorage.setUncompletedTasks(tempUncompletedTasks);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Task> tempFloatingTasks = Storage.localStorage.getFloatingTasks();

		for(int i = 0; i<tempFloatingTasks.size()-1; i++) {
			for(int j = i+1; j<tempFloatingTasks.size(); j++) {
				int result = tempFloatingTasks.get(i).getIssue().compareTo(tempFloatingTasks.get(j).getIssue());
				if(result > 0) {
					Task setTask = tempFloatingTasks.get(i);
					tempFloatingTasks.set(i, tempFloatingTasks.get(j));
					tempFloatingTasks.set(j, setTask);
				}
			}
		}
		try {
			Storage.localStorage.setFloatingTasks(tempFloatingTasks);
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
				Calendar startDate1 = tempTasks.get(i).getStartDate();
				Calendar startDate2 = tempTasks.get(j).getStartDate();
				Calendar endDate1 = tempTasks.get(i).getEndDate();
				Calendar endDate2 = tempTasks.get(j).getEndDate();

				if(endDate1 == null && endDate2 == null) { //both end dates are null
					if(startDate1.compareTo(startDate2) > 0) {
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}
				else if(endDate1 != null && endDate2 != null) { //both end dates are not null
					if(endDate1.compareTo(endDate2) > 0) {
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}
				
				else if(endDate1 == null && endDate2 != null) { //one end date is null
					if(startDate1.compareTo(endDate2) > 0) {
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}
				else {
					if(endDate1.compareTo(startDate2) > 0) { //one end date is null
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}
			}
		}

		try {
			Storage.localStorage.setUncompletedTasks(tempTasks);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Function to sort tasks according to priority
	 */
	public static void sortTasksPriority() {
		sortTasksChronologically();
		ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();
		
		for(int i = 0; i<tempUncompletedTasks.size(); i++) {
			for(int j = i+1; j<tempUncompletedTasks.size(); j++) {
				if(tempUncompletedTasks.get(i).getPriority().equals("low") && tempUncompletedTasks.get(j).getPriority().equals("high")) {
					Task temp = tempUncompletedTasks.get(i);
					tempUncompletedTasks.set(i, tempUncompletedTasks.get(j));
					tempUncompletedTasks.set(j, temp);
				}
				
				else if(tempUncompletedTasks.get(i).getPriority().equals("medium") && tempUncompletedTasks.get(j).getPriority().equals("high")) {
					Task temp = tempUncompletedTasks.get(i);
					tempUncompletedTasks.set(i, tempUncompletedTasks.get(j));
					tempUncompletedTasks.set(j, temp);
				}
				
				else if(tempUncompletedTasks.get(i).getPriority().equals("low") && tempUncompletedTasks.get(j).getPriority().equals("medium")) {
					Task temp = tempUncompletedTasks.get(i);
					tempUncompletedTasks.set(i, tempUncompletedTasks.get(j));
					tempUncompletedTasks.set(j, temp);
				}
			}
		}
		
		try {
			Storage.localStorage.setUncompletedTasks(tempUncompletedTasks);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Task> tempFloatingTasks = Storage.localStorage.getFloatingTasks();
		
		for(int i = 0; i<tempFloatingTasks.size(); i++) {
			for(int j = i+1; j<tempFloatingTasks.size(); j++) {
				if(tempFloatingTasks.get(i).getPriority().equals("low") && tempFloatingTasks.get(j).getPriority().equals("high")) {
					Task temp = tempFloatingTasks.get(i);
					tempFloatingTasks.set(i, tempFloatingTasks.get(j));
					tempFloatingTasks.set(j, temp);
				}
				
				else if(tempFloatingTasks.get(i).getPriority().equals("medium") && tempFloatingTasks.get(j).getPriority().equals("high")) {
					Task temp = tempFloatingTasks.get(i);
					tempFloatingTasks.set(i, tempFloatingTasks.get(j));
					tempFloatingTasks.set(j, temp);
				}
				
				else if(tempFloatingTasks.get(i).getPriority().equals("low") && tempFloatingTasks.get(j).getPriority().equals("medium")) {
					Task temp = tempFloatingTasks.get(i);
					tempFloatingTasks.set(i, tempFloatingTasks.get(j));
					tempFloatingTasks.set(j, temp);
				}
			}
		}
		
		try {
			Storage.localStorage.setFloatingTasks(tempFloatingTasks);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


