package Logic;

import java.util.ArrayList;

public class CRUD {

	public static void addLine(String line) {
		Storage.access.addToStorage(line);
	}
	
	public static void displayContents() {
		ArrayList<String> temp = new ArrayList<String>();
		temp = Storage.access.displayStorage();
		for(String s : temp) {
			System.out.println(s);
		}
	}

}
