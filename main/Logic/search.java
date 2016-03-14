package Logic;

/**
 * @author Kowshik
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Task.Task;

public class search {
	
	private static ArrayList<Task> searchedTasks;
	
	public static ArrayList<Task> getSearchedTasks() {
		return searchedTasks;
		
	}

	/**
	 * Function to search task according to keyword in list of uncompleted tasks
	 * 
	 * @param keyword the string to be searched for in the list of tasks
	 */
	public static void searchTasksByKeyword(String keyword){
		searchedTasks = new ArrayList<Task>();
		ArrayList<Task> temp = Storage.localStorage.getUncompletedTasks();
		for(int i=0; i<temp.size(); i++) {
			if(temp.get(i).getIssue().contains(keyword) || temp.get(i).getTaskString().contains(keyword)) {
				searchedTasks.add(temp.get(i));
			}
		}

		for(int i=0; i<searchedTasks.size(); i++) {
			UI.ui.print((i+1) + ". " + searchedTasks.get(i).getTaskString());
		}
	}
	
	/**
	 * Function to search task according to keyword in list of completed tasks
	 * 
	 * @param keyword the string to be searched for in the list of tasks
	 */
	public static void searchCompletedTasksByKeyword(String keyword) {
		searchedTasks = new ArrayList<Task>();
		ArrayList<Task> temp = Storage.localStorage.getCompletedTasks();
		for(int i = 0; i<temp.size(); i++) {
			if(temp.get(i).getTaskString().contains(keyword)) {
				searchedTasks.add(temp.get(i));
			}
		}
		for(int i=0; i<searchedTasks.size(); i++) {
			UI.ui.print((i+1) + ". " + searchedTasks.get(i).getTaskString());
		}
	}

	/**
	 * Function that returns the tasks which are due by the given date
	 * 
	 * @param keyword the deadline date to be searched for 
	 *//*
	public static void searchTasksByDate(String keyword){
		if(Logic.checkDate.checkDateformat(keyword)){
			String[] dateInput = keyword.split("/");
			int day = Integer.parseInt(dateInput[0]);
			int month = Integer.parseInt(dateInput[1]);
			int year = Integer.parseInt(dateInput[2]);

			Calendar date = new GregorianCalendar(year, month, day);

			ArrayList<Task> temp = Storage.localStorage.getArrayList();
			ArrayList<Task> searchDetails = new ArrayList<Task>();
			for(int i = 0; i<temp.size(); i++) {
				Calendar task = temp.get(i).getDate();
				if(task!= null){
					if(compareCalendar(task,date)) {
						searchDetails.add(temp.get(i));
					}
				}
			}

			int index = -1;
			for(int i = 0; i<searchDetails.size(); i++) {
				index = i+1;
				UI.ui.print(index + ". " + searchDetails.get(i).getTaskString());
			}
			if(index == -1){
				UI.ui.print("No tasks found with specified date");
			}
		}
		else {
			UI.ui.print("Not a valid date");
		}

	}

	*//**
	 * Function to check if two dates are equal
	 * 
	 * @param d1 the first date
	 * @param d2 the second date
	 * 
	 * @return boolean value true or false based on whether the dates are equal or not
	 *//*
	public static boolean compareCalendar(Calendar d1,Calendar d2){
		if(d1.get(Calendar.DATE) == d2.get(Calendar.DATE)){
			if(d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH)){
				if(d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)){
					return true;
				}else{
					return false;
				}
			} else{
				return false;
			}
		} else{
			return false;
		}

	}
*/
}
