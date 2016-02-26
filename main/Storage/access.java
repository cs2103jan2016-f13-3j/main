package Storage;

import java.util.ArrayList;
import java.util.Collections;

public class access {

	//ArrayList to store the contents added to the file
	private static ArrayList<String> details = new ArrayList<String>();
	//ArrayList to store the contents added to the file in sorted order
	private static ArrayList<String> sortedDetails = new ArrayList<String>();
	//ArrayList to store the lines in the file that contain the given search keyword 
	private static ArrayList<String> searchDetails;
	
	/**
	 * Function to return the ArrayList details
	 * @return the ArrayList details
	 */
	public static ArrayList<String> getArrayList() {
		return details;
	}
	
	/**
	 * Function to add a task to the file
	 * 
	 * @param line contains the task to be added
	 */
	public static void addToStorage(String line) {
		details.add(line);
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
	public static ArrayList<String> displayStorage() {
		return details;
	}
	
	/**
	 * Function to clear the contents of the file
	 */
	public static void clear() {
		details.clear();
	}
	
	/**
	 * Function to sort the contents of the file in alphabetical order
	 */
	public static void sortAlphabetically() {
		sortedDetails = details;
		Collections.sort(sortedDetails);
	}
	
	/**
	 * Function that returns the tasks which contain the given search keyword
	 * 
	 * @param searchKeyword the keyword to be searched for 
	 * 
	 * @return ArrayList searchDetails that contains the tasks which contain the given search keyword
	 */
	public static ArrayList<String> searchStorage(String searchKeyword) {
		searchDetails = new ArrayList<String>();
		for(String s : details) {
			if(s.contains(searchKeyword)) {
				searchDetails.add(s);
			}
		}
		return searchDetails;
	}
}
