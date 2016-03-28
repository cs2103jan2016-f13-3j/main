//@@author Jie Wei
package Logic;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import Task.Task;

public class ImportTasks {

	private static final Logger logger = Logger.getLogger(Class.class.getName()); 

	/**
	 * Function to import tasks from a given storage file
	 * 
	 * @param fileName String that contains the name of the storage file
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */

	public static void importTasksFromStorage(String fileName, String flag) throws IOException, ClassNotFoundException {
		//		LoggerTry.startLog();
		JsonReader reader = new JsonReader(new FileReader(fileName));
		ArrayList<Task> GsonObjects = new ArrayList<Task>();
		try {
			reader.beginArray();
			while (reader.hasNext()) {
				Task obj = (new Gson()).fromJson(reader, Task.class);
				GsonObjects.add(obj);
			}
			reader.endArray();
			reader.close();
			for (Task t : GsonObjects) {
				Logic.crud.addTaskViaImport(t, flag);
			}
		} catch (EOFException e) {
			return;
		}
	}
}

class LoggerTry {
	private final static Logger logger = Logger.getLogger(Class.class.getName());
	private static FileHandler fileHandler = null;

	public static void startLog() throws SecurityException, IOException {
		fileHandler = new FileHandler("loggerFile.log", true);
		Logger logger = Logger.getLogger("");
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
		logger.setLevel(Level.CONFIG);
	}
}
