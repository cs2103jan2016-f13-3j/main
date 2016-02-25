package Logic;

import java.util.ArrayList;

public class CRUD {

	public static void addLine(String line) {
		Storage.access.addToStorage(line);
	}
	
	public static void delete(int index){
		Storage.access.delFromStorage(index);
	}
	
	public static void display(){
		Storage.access.displayStorage();
	}
	
	public static void clear(){
		Storage.access.clear();
	}
	
	public static void sort(){
		Storage.access.sortStorage();
	}
	public static void search(String keyword){
		Storage.access.searchStorage(keyword);
	}
	public static void saveAndExit(){
		System.exit();
	}

	public static void displayContents() {
		ArrayList<String> temp = new ArrayList<String>();
		temp = Storage.access.displayStorage();
		for(String s : temp) {
			System.out.println(s);
		}
	}

}
