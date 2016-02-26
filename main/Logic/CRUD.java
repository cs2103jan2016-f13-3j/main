package Logic;

import Task.Task;
import Storage.access;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class CRUD {
	private static int[] leapYearDate = new int[]{31,29,31,30,31,30,31,31,30,31,30,31};
	private static int[] commonYearDate= new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	private static ArrayList<Task> temp = new ArrayList<Task>();

	/**
	 * Function to add task without time into storage
	 * 
	 */
	public static void addTask(String line) throws IOException {
		Task task=new Task(line);
		Storage.access.addToStorage(task);
	}

	/**
	 * Function to add task with time into storage
	 * 
	 */
	public static void addTask(String line,String date) throws IOException {
		Task task=new Task(line,date);

		Storage.access.addToStorage(task);
	}

	/**
	 * Function to delete task according to index in storage
	 * 
	 */
	public static void deleteTask(int index){
		Storage.access.delFromStorage(index);
	}

	/**
	 * Function to display all the tasks in the storage
	 * 
	 */
	public static void displayTasks() {

		temp = Storage.access.displayStorage();
		for(int i=0;i<temp.size();i++) {
			System.out.println(temp.get(i).getTaskString());
		}
	}

	/**
	 * Function to clear storage
	 * 
	 */
	public static void clearTasks(){
		Storage.access.clear();
	}

	/**
	 * Function to sorts tasks in storage alphabetically
	 * 
	 */
	public static void sortTasksAlphabetically(){
		Storage.access.sortAlphabetically();
	}

	/**
	 * Function to search task according to issue in the storage
	 * 
	 */
	public static void searchTasksByIssue(String keyword){
		temp = Storage.access.searchStorageByIssue(keyword);
		for(int i=0;i<temp.size();i++) {
			temp.get(i).getTaskString();
		}
	}
	
	/**
	 * Function to search task according to date in the storage
	 * 
	 */
	public static void searchTasksByDate(String keyword){
		if(checkDateformat(keyword)){

			getCalendar cal=new getCalendar(keyword);
			ArrayList<Task> temp = Storage.access.searchStorageByDate(cal);
			for(int i=0;i<temp.size();i++) {
				temp.get(i).getTaskString();
			}
		}else{
			System.out.println("Not a date");
		}

	}

	/**
	 * Function to check the format of the string if it follows the date convention
	 * 
	 */
	public static boolean checkDateformat(String msg){
		String[] msgArray=msg.split("/");
		if(msgArray.length!=3 && msg.matches("^\\d{2}/\\d{2}/\\d{4}")){
			return false;
		}else{
		int date=Integer.parseInt(msgArray[0]);
		int month=Integer.parseInt(msgArray[1]);
		int year=Integer.parseInt(msgArray[2]);
		if(year%4==0 && month>=1 && month<=12){
			if(date<=leapYearDate[month-1]){
				return true;
			}else{
				return false;
			}
		}else {
			if(date<=commonYearDate[month-1]){
				return true;
			}else{
				return false;
			}
		}
		}
	}

	/**
	 * Function to save the file into storage
	 * 
	 */
	public static void saveFile(ObjectOutputStream oos) throws IOException {
		Storage.access.saveFile(oos);
	}
	//Close the application
	public static void saveAndExit(){
		System.exit(0);
	}



}
