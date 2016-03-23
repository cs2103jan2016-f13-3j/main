package Storage;
/**
 * @author Kowshik
 */
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Task.Task;

public class fileStorage {

	/**
	 * Function that returns the tasks which are due by the given date <<<<<<<<<<<< !!!!
	 * 
	 * @param oos the outputstream object 
	 */
	public static void saveFile(String fileName, ArrayList<Task> details) throws IOException {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			for (Task t : details) {
				oos.writeObject(t);
			}
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
