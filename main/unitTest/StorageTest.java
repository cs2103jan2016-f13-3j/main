package unitTest;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import Storage.LocalStorage;
import Task.Task;

public class StorageTest {

	/**
	 * Equivalence partitioning 
	 * [Valid] - any non null string
	 * [Invalid] - [null string]
	 * @throws ClassNotFoundException
	 */
	@Test
	public void testIfTaskIsAdded() throws ClassNotFoundException { //To test if task has been added to the arraylist
		Double r = Math.random();
		String s = r.toString();
		Task temp = new Task(s);
		Task temp1 = new Task(null); // invalid

		String error = "";

		LocalStorage.addToUncompletedTasks(temp);
		
		/*try {
			localStorage.addToCompletedTasks(temp1);
		} catch (IOException e) {
			result = 
		}*/

		Task isMatch = LocalStorage.getUncompletedTask(0);
		assertEquals(temp, isMatch);
	}
	/**
	 * Equivalence partitioning 
	 * [Valid] = index within arraylist size
	 * [Invalid] = index greater than arraylist size, index less than 0
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testIfTaskIsDeleted() throws ClassNotFoundException, IOException { //To test if task has been deleted from arraylist
		int deleteIndex1 = 0;
		int deleteIndex2 = 5;
		int deleteIndex3 = -1;
		
		boolean isContained = false;
		boolean result = false;
		
		String error = "";
		testIfTaskIsAdded(); //adding a test task
		try {
			Task temp = LocalStorage.deleteFromUncompletedTasks(deleteIndex1);
			ArrayList<Task> tempTasks = LocalStorage.getUncompletedTasks();
			for(Task t : tempTasks) {
				if(t.equals(temp)) {
					result = true;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			error = "index is out of bounds";
		}
		assertSame(result, isContained);
		
		result = false;
		testIfTaskIsAdded(); //adding a test task
		try {
			Task temp = LocalStorage.deleteFromUncompletedTasks(deleteIndex2);
			ArrayList<Task> tempTasks = LocalStorage.getUncompletedTasks();
			for(Task t : tempTasks) {
				if(t.equals(temp)) {
					result = true;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			error = "index is out of bounds";
		}
		assertEquals(error, "index is out of bounds");
		
		result = false;
		testIfTaskIsAdded(); //adding a test task
		try {
			Task temp = LocalStorage.deleteFromUncompletedTasks(deleteIndex3);
			ArrayList<Task> tempTasks = LocalStorage.getUncompletedTasks();
			for(Task t : tempTasks) {
				if(t.equals(temp)) {
					result = true;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			error = "index is out of bounds";
		}
		assertEquals(error, "index is out of bounds");
	}

	@Test
	public void testClearTasks() throws ClassNotFoundException, IOException { //To test if arraylist has actually been cleared
		/*Task task = new Task("Hello", "21/03/2016");
		try {
			localStorage.addToUncompletedTasks(task);
		} catch (IOException e) {
			System.out.println(e);
		}
		localStorage.clear();
		int size = localStorage.getUncompletedTasks().size();
		assertEquals(size, 0);*/
	}
	
	/**
	 * Boundary value testing
	 * Boundary values - 0, arraylist.size()
	 */
	@Test
	public void testGetUncompletedTasks() {
		Task temp1 = LocalStorage.getUncompletedTask(0);
		Task temp2 = LocalStorage.getUncompletedTask(LocalStorage.getUncompletedTasks().size());
		
		assertEquals(temp1, LocalStorage.getUncompletedTask(0));
		assertEquals(temp2, LocalStorage.getUncompletedTask(LocalStorage.getUncompletedTasks().size()));
	}
}
