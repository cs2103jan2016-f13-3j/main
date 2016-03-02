package Logic;

import Task.Task;
import Storage.localStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CRUD {
	
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


	
	
	public static void importTasks(String fileName) throws IOException {
		FileInputStream fis = new FileInputStream(fileName);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				Task task = (Task)ois.readObject();
				Logic.CRUD.addTaskViaImport(task);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				break;
			}
		}
		fis.close();
	}	

	/**
	 * Function to save the file into storage.ser
	 * 
	 */
	public static void saveFile(String fileName) throws IOException {
		Storage.fileStorage.saveFile(fileName, Storage.localStorage.getArrayList());
	}
	
	//Close the application
	public static void exit(){
		System.exit(0);
	}



}
