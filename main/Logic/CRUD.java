package Logic;

import Task.Task;
import Storage.localStorage;
import java.io.IOException;
import java.util.ArrayList;

public class crud {

	private static ArrayList<Task> temp = new ArrayList<Task>();

	/**
	 * Function to add task without time into storage
	 * 
	 */
	public static void addTask(String line) throws IOException {
		Task task=new Task(line);
		Storage.localStorage.addToStorage(task);
	}

	/**
	 * Function to add task with time into storage
	 * 
	 */
	public static void addTask(String line,String date) throws IOException {
		Task task=new Task(line,date);
		Storage.localStorage.addToStorage(task);
	}

	/**
	 * Function to import the tasks from the storage file
	 * @param task the tasks to be added to the arraylist storage
	 * @throws IOException 
	 */
	public static void addTaskViaImport(Task task) throws IOException {
		Storage.localStorage.addToStorage(task);
	}

	/**
	 * Function to delete task according to index in storage
	 * 
	 */
	public static void deleteTask(int index){
		Storage.localStorage.delFromStorage(index);
	}

	/**
	 * Function to display all the tasks in the storage
	 * 
	 */
	public static void displayTasks() {
		temp = Storage.localStorage.displayStorage();
		for(int i=0; i<temp.size(); i++) {
			System.out.println((i+1) + ". " + temp.get(i).getTaskString());
		}
		if (temp.isEmpty()) {
			System.out.println("There is no stored task to display");
		}
	}

	/**
	 * Function to clear storage
	 * 
	 */
	public static void clearTasks(){
		Storage.localStorage.clear();
	}

	/**
	 * Function to exit the application when user enters exit command
	 */
	public static void exit(){
		System.exit(0);
	}



}
