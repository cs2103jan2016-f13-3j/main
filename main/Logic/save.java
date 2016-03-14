package Logic;

/**
 * @author Kowshik
 */
import java.io.IOException;

public class save {

	/**
	 * Function to save the file into storage.ser
	 * 
	 */
	public static void saveFile(String fileName) throws IOException {
		Storage.fileStorage.saveFile(fileName, Storage.localStorage.getUncompletedTasks());
	}
	
}
