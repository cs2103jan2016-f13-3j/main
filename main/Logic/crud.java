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
		Task task = new Task(line);
		Storage.localStorage.addToUncompletedTasks(task);
	}

	/**
	 * Function to add task with time into storage
	 * 
	 */
	public static void addTask(String line,String date) throws IOException {
		Task task = new Task(line,date);
		Storage.localStorage.addToUncompletedTasks(task);
	}

	/**
	 * Function to import the tasks from the storage file
	 * @param task the tasks to be added to the arraylist storage
	 * @throws IOException 
	 */
	public static void addTaskViaImport(Task task) throws IOException {
		Storage.localStorage.addToUncompletedTasks(task);
	}

	/**
	 * Function to edit task without editing date according to index in storage
	 * 
	 * @param line the updated task description
	 * @param index the index of the task to be edited
	 * @throws IOException
	 */
	public static void editTask(int index, String line) throws IOException {
		Task editedTask = new Task(line);
		Storage.localStorage.setUncompletedTask(index, editedTask);
	}
	
	/**
	 * Function to edit task along with date according to index in storage
	 * 
	 * @param index the index of the task to be edited
	 * @param line the updated task description
	 * @param date the updated task date
	 */
	public static void editTask(int index, String line, String date) {
		Task editedTask = new Task(line,date);
		Storage.localStorage.setUncompletedTask(index, editedTask);
	}
	/**
	 * Function to delete task according to index in storage
	 * 
	 */
	public static void deleteTask(int index){
		Storage.localStorage.delFromUncompletedTasks(index);
	}

	/**
	 * Function to display all the uncompleted tasks in the storage
	 * 
	 */
	public static void displayUncompletedTasks() {
		temp = Storage.localStorage.displayUncompletedTasks();
		for(int i=0; i<temp.size(); i++) {
			UI.ui.print((i+1) + ". " + temp.get(i).getTaskString());
		}
		if (temp.isEmpty()) {
			UI.ui.print("There is no stored task to display");
		}
	}
	
	/**
	 * Function to display all the completed tasks in the storage
	 * 
	 */
	public static void displayCompletedTasks() {
		temp = Storage.localStorage.displayCompletedTasks();
		for(int i=0; i<temp.size(); i++) {
			UI.ui.print((i+1) + ". " + temp.get(i).getTaskString());
		}
		if (temp.isEmpty()) {
			UI.ui.print("There is no stored task to display");
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
