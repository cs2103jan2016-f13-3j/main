package Logic;

import java.util.ArrayList;

public class CRUD {

	public static void addTask(String line) {
		Storage.access.addToStorage(line);
	}
	
	public static void deleteTask(int index){
		Storage.access.delFromStorage(index);
	}
	
	public static void displayTasks() {
		ArrayList<String> temp = new ArrayList<String>();
		temp = Storage.access.displayStorage();
		for(String s : temp) {
			System.out.println(s);
		}
	}
	
	public static void clearTasks(){
		Storage.access.clear();
	}
	
	public static void sortTasksAlphabetically(){
		Storage.access.sortAlphabetically();
	}
	
	public static void searchTasks(String keyword){
		ArrayList<String> temp = Storage.access.searchStorage(keyword);
		for(String s : temp) {
			System.out.println(s);
		}
	}
	
	public static void saveAndExit(){
		System.exit(0);
	}

	

}
