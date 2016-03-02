package Logic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import Task.Task;

public class importTasks {

	/**
	 * Function to import tasks from the storage file "storage.ser"
	 * 
	 * @param fileName String that contains the name of the storage file
	 * @throws IOException
	 */
	
	public static void importTasksFromStorage(String fileName) throws IOException {
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

}
