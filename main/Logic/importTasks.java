package Logic;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import Task.Task;

public class importTasks {
	
	private static final Logger logger = Logger.getLogger(Class.class.getName()); 
	private static final String FLAG_UNCOMPLETED = "uncompleted";
	private static final String FLAG_COMPLETED = "completed";
	private static final String FLAG_FLOATING = "floating";

	/**
	 * Function to import tasks from the storage file "storage.ser"
	 * 
	 * @param fileName String that contains the name of the storage file
	 * @throws IOException
	 */
	
	public static void importTasksFromStorage(File file, String flag) throws IOException {
//		LoggerTry.startLog();
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(file);		
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
				Logic.crud.addTaskViaImport(task, flag);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (EOFException e) {
				//logger.log(Level.WARNING, e.toString(), e);
				break;
			} catch (IOException e) {
				//logger.log(Level.SEVERE, e.toString(), e);
				break;
			} catch (NullPointerException e) {
				break;
			}
		}
		fis.close();
	}
//	////////////////////////// REMOVE THIS CHUNK!!!////
//	public static void importCompletedTasksFromStorage(String fileName) throws IOException {
//		LoggerTry.startLog();
//		FileInputStream fis = null;
//		ObjectInputStream ois = null;
//		try {
//			fis = new FileInputStream(fileName);		
//			ois = new ObjectInputStream(fis);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		} catch (EOFException e) {
//			fis.close();
//			return;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		while (true) {
//			try {
//				Task task = (Task)ois.readObject();
//				Logic.crud.addTaskViaImport(task, FLAG_COMPLETED);
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (EOFException e) {
//				//logger.log(Level.WARNING, e.toString(), e);
//				break;
//			} catch (IOException e) {
//				//logger.log(Level.SEVERE, e.toString(), e);
//				break;
//			} catch (NullPointerException e) {
//				break;
//			}
//		}
//		fis.close();
//	}	
//	
//	public static void importFloatingTasksFromStorage(String fileName) throws IOException {
//		LoggerTry.startLog();
//		FileInputStream fis = null;
//		ObjectInputStream ois = null;
//		try {
//			fis = new FileInputStream(fileName);		
//			ois = new ObjectInputStream(fis);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		} catch (EOFException e) {
//			fis.close();
//			return;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		while (true) {
//			try {
//				Task task = (Task)ois.readObject();
//				Logic.crud.addTaskViaImport(task, FLAG_FLOATING);
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (EOFException e) {
//				//logger.log(Level.WARNING, e.toString(), e);
//				break;
//			} catch (IOException e) {
//				//logger.log(Level.SEVERE, e.toString(), e);
//				break;
//			} catch (NullPointerException e) {
//				break;
//			}
//		}
//		fis.close();
//	}	
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
