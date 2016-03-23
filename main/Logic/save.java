package Logic;

/**
 * @author Kowshik
 */
import java.io.IOException;

public class save {

	private static String storageFileNameCompletedTasks = "storageCompleted.ser";
	private static String storageFileNameUncompletedTasks = "storageUncompleted.ser";
	private static String storageFileNameFloatingTasks = "storageFloating.ser";

	/**
	 * Function to save the file into storage.ser
	 * 
	 */
	public static void saveFile(String fileName) throws IOException {
		if (fileName.equals(storageFileNameUncompletedTasks)) {
			Storage.fileStorage.saveFile(fileName, Storage.localStorage.getUncompletedTasks());
		} else if (fileName.equals(storageFileNameCompletedTasks)) {
			Storage.fileStorage.saveFile(fileName, Storage.localStorage.getCompletedTasks());
		} else if (fileName.equals(storageFileNameFloatingTasks)) {
			Storage.fileStorage.saveFile(fileName, Storage.localStorage.getFloatingTasks());
		}
	}
}
