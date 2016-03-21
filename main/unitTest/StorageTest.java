package unitTest;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import Storage.localStorage;
import Task.Task;

public class StorageTest {

	@Test
	public void testClearTasks() { //To test if arraylist has actually been cleared
		Task task = new Task("Hello", "21/03/2016");
		try {
			localStorage.addToUncompletedTasks(task);
		} catch (IOException e) {
			System.out.println(e);
		}
		localStorage.clear();
		int size = localStorage.getUncompletedTasks().size();
		assertEquals(size, 0);
	}
	
	@Test
	public void testIfTaskIsAdded() { //To test if task has been added to the arraylist
		Task temp = new Task("Testing", "21/03/2016");
		try {
			localStorage.addToUncompletedTasks(temp);
		} catch (IOException e) {
			System.out.println(e);
		}
		Task isMatch = localStorage.getUncompletedTask(0);
		assertEquals(temp, isMatch);
	}
	
	@Test
	public void testIfTaskIsDeleted() { //To test if task has been deleted from arraylist
		int deleteIndex = 0;
		boolean isContained = false;
		
		testIfTaskIsAdded(); //adding a test task
		
		Task temp = localStorage.delFromUncompletedTasks(deleteIndex);
		ArrayList<Task> tempTasks = localStorage.getUncompletedTasks();
		boolean result = false;
		for(Task t : tempTasks) {
			if(t.equals(temp)) {
				result = true;
			}
		}
		assertSame(result, isContained);
		
	}
}
