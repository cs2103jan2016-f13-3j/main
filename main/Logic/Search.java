package Logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Task.Task;

public class Search {

	private static int[] leapYearDate = new int[]{31,29,31,30,31,30,31,31,30,31,30,31};
	private static int[] commonYearDate= new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
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
		if(checkDateformat(keyword)){
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

	/**
	 * Function to check the format of the string if it follows the date convention
	 * 
	 */
	public static boolean checkDateformat(String msg){
		String[] msgArray=msg.split("/");
		if(msgArray.length!=3 && msg.matches("^\\d{2}/\\d{2}/\\d{4}")) {
			return false;
		} else {
			int date=Integer.parseInt(msgArray[0]);
			int month=Integer.parseInt(msgArray[1]);
			int year=Integer.parseInt(msgArray[2]);

			if(year%4==0 && month>=1 && month<=12) {
				if(date<=leapYearDate[month-1]) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				if(date<=commonYearDate[month-1]) {
					return true;
				}
				else {
					return false;
				}
			}
		}
	}
}
