package Logic;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(fileName);		
			ois = new ObjectInputStream(fis);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (EOFException e) {
			fis.close();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (true) {
			try {
				Task task = (Task)ois.readObject();
				Logic.crud.addTaskViaImport(task);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (EOFException e) {
				break;
			} catch (IOException e) {
				break;
			} catch (NullPointerException e) {
				break;
			}
		}
		fis.close();
	}	

}
