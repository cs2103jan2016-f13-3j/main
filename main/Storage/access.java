package Storage;

import java.util.ArrayList;

public class access {

	private static ArrayList<String> details = new ArrayList<String>();
	
	public static void addToStorage(String line) {
		details.add(line);
	}
	
	public static ArrayList<String> displayStorage() {
		return details;
	}
}
