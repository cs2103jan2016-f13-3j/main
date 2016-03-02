package Logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Task.Task;

public class search {

	private static ArrayList<Task> temp = new ArrayList<Task>();
	
	/**
	 * Function to search task according to issue in the storage
	 * 
	 */
	public static void searchTasksByIssue(String keyword){
		temp = Storage.localStorage.searchStorageByIssue(keyword);
		for(int i=0;i<temp.size();i++) {
			System.out.println((i+1) + ". " + temp.get(i).getTaskString());
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
			
			Calendar date = new GregorianCalendar(day, month, year);
			ArrayList<Task> temp = Storage.localStorage.searchStorageByDate(date);
			for(int i=0;i<temp.size();i++) {
				temp.get(i).getTaskString();
			}
		}
		else {
			System.out.println("Not a date");
		}

	}

	
}
