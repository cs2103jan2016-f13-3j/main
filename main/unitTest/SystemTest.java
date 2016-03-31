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

	@Test
	public void testIfUndoWorks() throws ClassNotFoundException, IOException {
		// check the initial amount off floating task
		int initialFloatingNum = Storage.localStorage.getFloatingTasks().size();
		System.out.println("Current amount of floating tasks: " + initialFloatingNum);

		//Add a sample floating task
		try {
			Parser.Parser.run("add", "Test Floating");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Ensure that task has been added
		System.out.println("Current amount of floating tasks (after adding): " + Storage.localStorage.getFloatingTasks().size());
		assertEquals(initialFloatingNum + 1, Storage.localStorage.getFloatingTasks().size());

		//Undo the addition of floating task
		Parser.Parser.run("undo", "");

		// Ensure the added task has been removed (undone)
		System.out.println("Current amount of floating tasks (after undo): " + Storage.localStorage.getFloatingTasks().size());
		assertEquals(initialFloatingNum, Storage.localStorage.getFloatingTasks().size());

		// Clear all tasks for clean testing state
		Storage.localStorage.clear(); 
		initialFloatingNum = Storage.localStorage.getFloatingTasks().size();

		//Add 2 sample floating tasks
		try {
			Parser.Parser.run("add", "Test Floating 1");
			Parser.Parser.run("add", "Test Floating 2");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Ensure that task has been added
		System.out.println("Current amount of floating tasks (after adding): " + Storage.localStorage.getFloatingTasks().size());
		assertEquals(initialFloatingNum + 2, Storage.localStorage.getFloatingTasks().size());

		// Remove all tasks
		Parser.Parser.run("clear", "");

		// Ensure that all tasks has been deleted
		System.out.println("Current amount of floating tasks (after clearing): " + Storage.localStorage.getFloatingTasks().size());
		assertEquals(0, Storage.localStorage.getFloatingTasks().size());

		//Undo the clear command
		Parser.Parser.run("undo", "");

		// Ensure the removed tasks has been restored (undone)
		System.out.println("Current amount of floating tasks (after undo): " + Storage.localStorage.getFloatingTasks().size());
		assertEquals(initialFloatingNum + 2, Storage.localStorage.getFloatingTasks().size());
		
		//Ensure the restored tasks have the same issues
		String task1Issue = Storage.localStorage.getFloatingTask(0).getIssue();
		assertEquals("Test Floating 1", task1Issue);
		
		String task2Issue = Storage.localStorage.getFloatingTask(1).getIssue();
		assertEquals("Test Floating 2", task2Issue);
	}
}
