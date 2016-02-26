package Logic;
import Task.Task;

import java.io.IOException;
import java.util.ArrayList;

public class CRUD {
private static int[] leapYearDate = new int[]{31,29,31,30,31,30,31,31,30,31,30,31};
private static int[] commonYearDate= new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
private static ArrayList<Task> temp = new ArrayList<Task>();

//add the msg without date into storage
public static void addTask(String line) throws IOException {
	Task task=new Task(line);
	Storage.access.addToStorage(task);
}

//add the msg with date into storage
public static void addTask(String line,String date) throws IOException {
	Task task=new Task(line,date);
	
	Storage.access.addToStorage(task);
}

//delete the msg according to its index
public static void deleteTask(int index){
		Storage.access.delFromStorage(index);
}

//display all the msg 
public static void displayTasks() {
		
		temp = Storage.access.displayStorage();
		for(int i=0;i<temp.size();i++) {
			temp.get(i).printTask();
		}
	}
	
//Clear all the added msg in the storage
public static void clearTasks(){
		Storage.access.clear();
}

//Sort the msgs in the Storage
public static void sortTasksAlphabetically(){
	Storage.access.sortAlphabetically();
}

//Search the msg according to its keyword
public static void searchTasksByIssue(String keyword){
		temp = Storage.access.searchStorageByIssue(keyword);
		for(int i=0;i<temp.size();i++) {
			temp.get(i).printTask();
			}
}

//Search the msg according to its Date
	public static void searchTasksByDate(String keyword){
		Calendar date=new Calendar(keyword);
		ArrayList<Task> temp = Storage.access.searchStorageByDate(date);
		for(int i=0;i<temp.size();i++) {
			temp.get(i).printTask();
			}
}
	
//Check the formate of the date
	public static boolean checkDateformat(String msg){
		String[] msgArray=msg.split("/");
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
	//Close the application
	public static void saveAndExit(){
		System.exit(0);
	}

	

}
