package Storage;

import Task.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class localStorage {

	//ArrayList to store the contents added to the file
	private static ArrayList<Task> details = new ArrayList<Task>();
	//ArrayList to store the lines in the file that contain the given search keyword 
	private static ArrayList<Task> searchDetails;

	/**
	 * Function to return the ArrayList details
	 * @return the ArrayList details
	 */
	public static ArrayList<Task> getArrayList() {
		return details;
	}
	
	/**
	 * Function to assign details array list to given array list
	 * @param changedDetails the arraylist to be assigned to details
	 */
	public static void setArrayList(ArrayList<Task> changedDetails) {
		details = changedDetails;
	}

	/**
	 * Function to add a task to the file
	 * 
	 * @param line contains the task to be added
	 * @throws IOException 
	 */
	public static void addToStorage(Task task) throws IOException {
		details.add(task);
	}

	/**
	 * Function to delete a task from the file
	 * 
	 * @param index contains the index of the task to be deleted
	 */
	public static void delFromStorage(int index) {
		details.remove(index);
	}

	/**
	 * Function to return the contents of the file to Logic
	 * 
	 * @return ArrayList details that contains the file contents
	 */
	public static ArrayList<Task> displayStorage() {
		return details;
	}

	/**
	 * Function to clear the contents of the file
	 */
	public static void clear() {
		details.clear();
	}

	/**
	 * Function that returns the tasks which are due by the given date
	 * 
	 * @param date the deadline date to be searched for 
	 * 
	 * @return ArrayList searchDetails that contains the tasks which contain the given search keyword
	 */
	public static ArrayList<Task> searchStorageByDate(Calendar date) {
		searchDetails = new ArrayList<Task>();
		for(int i = 0; i<details.size(); i++) {
			Calendar task = details.get(i).getDate();
			if(task!= null){
				if(compareCalendar(task,date)) {
					searchDetails.add(details.get(i));
				}
			}
		}
		return searchDetails;
	}
	public static boolean compareCalendar(Calendar d1,Calendar d2){
		if(d1.get(Calendar.DATE)==d2.get(Calendar.DATE)){
			if(d1.get(Calendar.MONTH)==d2.get(Calendar.MONTH)){
				if(d1.get(Calendar.YEAR)==d2.get(Calendar.YEAR)){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
		
	}

}
