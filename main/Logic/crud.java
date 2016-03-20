package Logic;

import Task.Task;
import Storage.localStorage;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;

public class crud {

	private static ArrayList<Task> temp = new ArrayList<Task>();
	private static Task temp1;
	/**
	 * Function to add task without time into storage
	 * 
	 */
	public static boolean addTask(String line) throws IOException {
		Task task = new Task(line);
		ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		boolean noDuplicate = true;
		for(Task temp : tempTasks) {
			if(temp.getTaskString().equals(task.getTaskString())) {
				noDuplicate = false;
			}
		}
		if(noDuplicate) {
			Storage.localStorage.addToUncompletedTasks(task);
			return true;
		}
		else {
			return false;
		}
	}
	public static ArrayList<Task> getTemp(){
		return temp;
	}

	/**
	 * Function to add task with time into storage
	 * 
	 */
	public static boolean addTask(String line,String date) throws IOException {
		Task task = new Task(line,date);
		
		boolean noDuplicate = true;
		ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		for(Task temp : tempTasks) {
			if(temp.getTaskString().equals(task.getTaskString())) {
				System.out.println(temp.getTaskString());
				noDuplicate = false;
			}
		}
		if(noDuplicate) {
			Storage.localStorage.addToUncompletedTasks(task);
			return true;
		}
		else {
			return false;
		}
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
	public static void copyTask(int index){
		Task edit=Storage.localStorage.getUncompletedTask(index-1);
		if(edit!=null){
		String copy=edit.getIssue();
		StringSelection selec= new StringSelection(copy);
		   Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		   clipboard.setContents(selec, selec);
		}
	}
	public static void copyTaskDate(int index){
		Task edit=Storage.localStorage.getUncompletedTask(index-1);
		if(edit!=null){
		String copy=edit.getDateString();
		StringSelection selec= new StringSelection(copy);
		   Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		   clipboard.setContents(selec, selec);
		}
	}
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
	public static void deleteTask(int index, int listOfTasks){
		if(listOfTasks == 1) { //delete from uncompleted tasks
			Storage.localStorage.delFromUncompletedTasks(index);
		}
		else if(listOfTasks == 2) { //delete from completed tasks
			Storage.localStorage.delFromCompletedTasks(index);
		}
		else if(listOfTasks == 3) { //delete from search completed tasks view
			ArrayList<Task> searchTemp = search.getSearchedTasks();
			Task taskToBeDeleted = searchTemp.get(index);
			ArrayList<Task> uncompletedTemp = Storage.localStorage.getUncompletedTasks();
			for(int i = 0; i<uncompletedTemp.size(); i++) {
				if(uncompletedTemp.get(i).equals(taskToBeDeleted)) {
					uncompletedTemp.remove(i);
					break;
				}
			}
		}
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
	 * Function to display the details of an individual task
	 * 
	 * @param index the index of the task to be displayed
	 */
	public static void viewIndividualTask(int index) {
		temp1 = Storage.localStorage.getUncompletedTask(index);

		boolean isCompleted = temp1.getCompletedStatus();
		String completed = "Not completed";
		if(isCompleted) {
			completed = "Completed";
		}

		UI.ui.print(temp1.getTaskString());
		UI.ui.print("Status: " + completed);
		UI.ui.print("Priority: " + temp1.getPriority());
		UI.ui.print("Labels:");
		for(String label : temp1.getLabel()) {
			UI.ui.print(label);
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
