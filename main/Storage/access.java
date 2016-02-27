package Storage;

import Task.Task;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class access {

	//ArrayList to store the contents added to the file
	private static ArrayList<Task> details = new ArrayList<Task>();
	//ArrayList to store the contents added to the file in sorted order
	private static ArrayList<Task> sortedDetails = new ArrayList<Task>();
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
	 * Function to sort the contents of the file in alphabetical order
	 */
	public static void sortAlphabetically() {
		for(int i =0; i<details.size()-1; i++) {
			for(int j = i+1; j<details.size(); j++) {
				int result = details.get(i).getIssue().compareTo(details.get(j).getIssue());
				if(result > 0) {
					Task temp = details.get(i);
					details.set(i, details.get(j));
					details.set(j, temp);
				}

			}
		}
	}

	/**
	 * Function that returns the tasks which contain the given search keyword
	 * 
	 * @param searchKeyword the keyword to be searched for 
	 * 
	 * @return ArrayList searchDetails that contains the tasks which contain the given search keyword
	 */
	public static ArrayList<Task> searchStorageByIssue(String searchKeyword) {
		searchDetails = new ArrayList<Task>();
		for(int i = 0; i<details.size(); i++)
		{
			if(details.get(i).getIssue().equals(searchKeyword)) {
				searchDetails.add(details.get(i));
			}
		}
		
		return searchDetails;
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
			if(details.get(i).getDate().equals(date)) {
				searchDetails.add(details.get(i));
			}
		}
		return searchDetails;
	}
	
	/**
	 * Function that returns the tasks which are due by the given date
	 * 
	 * @param oos the outputstream object 
	 */
	public static void saveFile(ObjectOutputStream oos) throws IOException {
		oos.writeObject(details);
	}
}
