package Logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Task.Task;

public class search {

	/**
	 * Function to search task according to issue in the storage
	 * 
	 * @param keyword the string to be searched for in the list of tasks
	 */
	public static void searchTasksByIssue(String keyword){
		ArrayList<Task> temp = Storage.localStorage.getArrayList();
		ArrayList<Task> searchedTasks = new ArrayList<Task>();
		
		for(int i=0; i<temp.size(); i++) {
			if(temp.get(i).getIssue().contains(keyword)) {
				searchedTasks.add(temp.get(i));
			}
		}
		
		for(int i=0; i<searchedTasks.size(); i++) {
			UI.ui.print((i+1) + ". " + searchedTasks.get(i).getTaskString());
			UI.ui.printEmptyLine();
		}
	}

	/**
	 * Function to search task according to date in the storage
	 * 
	 */
	public static void searchTasksByDate(String keyword){
		if(Logic.checkDate.checkDateformat(keyword)){
			String[] dateInput = keyword.split("/");
			int day = Integer.parseInt(dateInput[0]);
			int month = Integer.parseInt(dateInput[1]);
			int year = Integer.parseInt(dateInput[2]);
			
			Calendar date = new GregorianCalendar(year, month, day);
			ArrayList<Task> temp = Storage.localStorage.searchStorageByDate(date);
			for(int i=0; i<temp.size(); i++) {
				temp.get(i).getTaskString();
			}
		}
		else {
			System.out.println("Not a date");
		}

	}

	
}
