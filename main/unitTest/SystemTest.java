package unitTest;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

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
	
	@Test
	public void testIfTasksAreCleared() {
		try {
			Parser.Parser.run("clear", "");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(0, Storage.localStorage.getUncompletedTasks().size() + Storage.localStorage.getFloatingTasks().size());
	}
	
	@Test
	public void testIfSearchResultsAreCorrect() {
		Task temp = new Task("Testing", "31/03/2016", "01/04/2016", "Testing from 31/03/2016 to 01/04/2016");
		Task temp1 = new Task("Testing", "02/04/2016", "03/04/2016", "Testing from 02/04/2016 to 03/04/2016");
		Task temp2 = new Task("Testing", "05/03/2016", "06/04/2016", "Testing from 05/03/2016 to 06/04/2016");
		
		Storage.localStorage.addToUncompletedTasks(temp);
		Storage.localStorage.addToUncompletedTasks(temp1);
		Storage.localStorage.addToUncompletedTasks(temp2);
		
		ArrayList<Task> searchResults = new ArrayList<Task>();
		
		try {
			Parser.Parser.run("search", "Testing");
			searchResults = Logic.Search.getSearchedTasks();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(searchResults, Storage.localStorage.getUncompletedTasks());
	}
	
}
