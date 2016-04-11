package unitTest;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import Logic.Core;
import Logic.Search;
import Logic.Undo;
import Parser.Parser;
import Storage.LocalStorage;
import Task.Task;

public class SystemTest {

	@Test
	public void testIfTaskIsAdded() {
		//Temporary task
		Task temp = new Task("Testing", "31/03/2016", "01/04/2016", "Testing from 31/03/2016 to 01/04/2016");
		String testString = temp.getTaskString();
		Core test;
		Parser parse;
		LocalStorage storage = LocalStorage.getInstance();
		//Checking if add command works
		try {
			test = Core.getInstance();
			parse = Parser.getInstance();
			parse.parse("add Testing ` from 31/03/2016 to 01/04/2016");
			test.parseCommands();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		Task taskAdded = storage.getUncompletedTask(0);
		String checkString = taskAdded.getTaskString();
		assertEquals(testString, checkString); //to check if the tast is added correctly
		assertEquals(1, storage.getUncompletedTasks().size()); //to check size of array list
	}

	@Test
	public void testIfTaskIsDeleted() throws ClassNotFoundException, IOException {
		//adding task first
		Task temp = new Task("Testing", "31/03/2016", "01/04/2016", "Testing ` from 31/03/2016 to 01/04/2016");
		Core test;
		Parser parse;
		LocalStorage storage = LocalStorage.getInstance();;
		storage.clearAllTasks();
		storage.addToUncompletedTasks(temp);
		assertEquals(1,storage.getUncompletedTasks().size());
		test = Core.getInstance();
		parse = Parser.getInstance();
		parse.parse("delete 1");
		test.parseCommands();
		//deleting task
		

		assertEquals(1, storage.getUncompletedTasks().size());
	}

	@Test
	public void testIfTasksAreCleared() {
		Core test = Core.getInstance();;
		Parser parse = Parser.getInstance();
		LocalStorage storage = LocalStorage.getInstance();
		storage.clearAllTasks();
		Task temp = new Task("Testing", "31/03/2016", "01/04/2016", "Testing ` from 31/03/2016 to 01/04/2016");
		storage.addToUncompletedTasks(temp);
		assertEquals(1, storage.getUncompletedTasks().size());
		try {
			parse.parse("clear");
			test.parseCommands();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(0, storage.getUncompletedTasks().size() + storage.getFloatingTasks().size());
	}
	
	@Test
	public void testIfSearchResultsAreCorrect() {
		Task temp = new Task("Testing", "31/03/2016", "01/04/2016", "Testing ` from 31/03/2016 to 01/04/2016");
		Task temp1 = new Task("Testing", "02/04/2016", "03/04/2016", "Testing ` from 02/04/2016 to 03/04/2016");
		Task temp2 = new Task("Testing", "05/03/2016", "06/04/2016", "Testing ` from 05/03/2016 to 06/04/2016");
		
		Core test = Core.getInstance();;
		Parser parse = Parser.getInstance();
		LocalStorage storage = LocalStorage.getInstance();
		storage.clearAllTasks();
		storage.addToUncompletedTasks(temp);
		storage.addToUncompletedTasks(temp1);
		storage.addToUncompletedTasks(temp2);

		ArrayList<Task> searchResults = new ArrayList<Task>();
		Search search = Search.getInstance();
		try {
			parse.parse("search Testing");
			test.parseCommands();
			
			searchResults = search.getSearchedTasks();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(searchResults, storage.getUncompletedTasks());
	}
	

	@Test
	public void testIfUndoWorks() throws ClassNotFoundException, IOException {
		// check the initial amount off floating task
		Core test = Core.getInstance();;
		Parser parse = Parser.getInstance();
		LocalStorage storage = LocalStorage.getInstance();
		storage.clearAllTasks();
		int initialFloatingNum = storage.getFloatingTasks().size();
		System.out.println("Current amount of floating tasks: " + initialFloatingNum);

		//Add a sample floating task
		try {
			parse.parse("add Test Floating");
			test.parseCommands();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Ensure that task has been added
		System.out.println("Current amount of floating tasks (after adding): " + storage.getFloatingTasks().size());
		assertEquals(initialFloatingNum + 1, storage.getFloatingTasks().size());

		//Undo the addition of floating task
		parse.parse("undo");
		test.parseCommands();

		// Ensure the added task has been removed (undone)
		System.out.println("Current amount of floating tasks (after undo): " + storage.getFloatingTasks().size());
		assertEquals(initialFloatingNum + 1, storage.getFloatingTasks().size());

		// Clear all tasks for clean testing state
		storage.clearAllTasks(); 
		initialFloatingNum = storage.getFloatingTasks().size();

		//Add 2 sample floating tasks
		try {
			parse.parse("add Test Floating 1");
			test.parseCommands();
			parse.parse("add Test Floating 2");
			test.parseCommands();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Ensure that task has been added
		System.out.println("Current amount of floating tasks (after adding): " + storage.getFloatingTasks().size());
		assertEquals(initialFloatingNum + 2, storage.getFloatingTasks().size());

		// Remove all tasks
		parse.parse("clear");
		test.parseCommands();

		// Ensure that all tasks has been deleted
		System.out.println("Current amount of floating tasks (after clearing): " + storage.getFloatingTasks().size());
		assertEquals(0, storage.getFloatingTasks().size());

		//Undo the clear command
		parse.parse("undo");
		test.parseCommands();

		// Ensure the removed tasks has been restored (undone)
		System.out.println("Current amount of floating tasks (after undo): " + storage.getFloatingTasks().size());
		assertEquals(initialFloatingNum , storage.getFloatingTasks().size());

	}
	private static Undo undoObject = Undo.getInstance();

	@Test
	public void testInitialEmptyUndoAndRedo() {
		// Wipe redo stack in case any other test methods were run before this
		undoObject.clearRedoCommands();

		// Check for zero initial undo-able commands
		assertEquals(0, undoObject.getHistoryCount());

		// Check for zero initial redo-able commands
		System.out.println(undoObject.getRedoCount());
		assertEquals(0, undoObject.getRedoCount());
	}
	@Test
	public void testRedo() throws ClassNotFoundException, IOException {
		// Helper method to simulate undo (and thus adding of command to redo stack)
		undoObject.testFunction();

		// Check for correct retrieval of the undo command name
		String redoneCommand = undoObject.getRedoneCommand();
		assertEquals("Add Buy eggs ` on Friday", redoneCommand);

		// Check for one redo-able commands after pseudo-undo
		undoObject.testFunction();
		assertEquals(1, undoObject.getRedoCount());
	}


	
}
