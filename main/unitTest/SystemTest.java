package unitTest;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import Task.Task;

public class SystemTest {

	@Test
	public void testIfTaskIsAdded() {
		//Temporary task
		Task temp = new Task("Testing", "31/03/2016", "01/04/2016", "Testing from 31/03/2016 to 01/04/2016");
		String testString = temp.getTaskString();
		//Checking if add command works
		try {
			Parser.Parser.run("add", "Testing from 31/03/2016 to 01/04/2016");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Task taskAdded = Storage.localStorage.getUncompletedTask(0);
		String checkString = taskAdded.getTaskString();
		assertEquals(testString, checkString); //to check if the tast is added correctly
		assertEquals(1, Storage.localStorage.getUncompletedTasks().size()); //to check size of array list
	}

	@Test
	public void testIfTaskIsDeleted() {
		//adding task first
		Task temp = new Task("Testing", "31/03/2016", "01/04/2016", "Testing from 31/03/2016 to 01/04/2016");
		Storage.localStorage.addToUncompletedTasks(temp);
		assertEquals(2, Storage.localStorage.getUncompletedTasks().size());

		//deleting task
		try {
			Parser.Parser.run("delete", "1");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(1, Storage.localStorage.getUncompletedTasks().size());
	}
	
}
