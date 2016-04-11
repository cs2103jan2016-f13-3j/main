//@@author Kowshik
package unitTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import Logic.Crud;
import Storage.LocalStorage;
import Task.Task;
import Logic.Mark;
public class MarkTest {
	
	private Crud crud = Crud.getInstance();
	private Mark mark = new Mark();
	private String issue;
	private ArrayList<Task> uncompleted,completed; 
	private LocalStorage storage = LocalStorage.getInstance();
	
	/**
	 * To test if mark works.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testMark() throws ClassNotFoundException, IOException {
		issue = "task";
		crud.clearTasks();
		crud.addTaskWithBothDates("task", "11/04/2016", "12/04/2016", "add task ` from 11/04/2016 to 12/04/2016");
		uncompleted = storage.getUncompletedTasks();
		completed = storage.getCompletedTasks();
		Task task = uncompleted.get(0);
		assertEquals(issue,task.getIssue());
		assertEquals(0,completed.size());
		mark.markTaskAsCompleted(0);
		assertEquals(0,uncompleted.size());
		task = completed.get(0);
		assertEquals(issue,task.getIssue());										
		
		
	}
	
	/**
	 * To test if unmark works.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testUnMark() throws ClassNotFoundException, IOException {
		issue = "task";
		crud.clearTasks();
		crud.addTaskWithBothDates("task", "11/04/2016", "12/04/2016", "add task ` from 11/04/2016 to 12/04/2016");
		uncompleted = storage.getUncompletedTasks();
		completed = storage.getCompletedTasks();
		Task task = uncompleted.get(0);
		assertEquals(issue,task.getIssue());
		assertEquals(0,completed.size());
		mark.markTaskAsCompleted(0);
		assertEquals(0,uncompleted.size());
		task = completed.get(0);
		assertEquals(issue,task.getIssue());
		mark.markTaskAsUncompleted(0);
		task = uncompleted.get(0);
		assertEquals(issue,task.getIssue());
		assertEquals(0,completed.size());		
		
	}
	
	@Test
	public void testPriority() throws ClassNotFoundException, IOException {
		issue = "task";
		crud.clearTasks();
		crud.addTaskWithBothDates("task", "11/04/2016", "12/04/2016", "add task ` from 11/04/2016 to 12/04/2016");
		uncompleted = storage.getUncompletedTasks();
		Task task = uncompleted.get(0);
		assertEquals(issue,task.getIssue());
		mark.setPriority(0,"high");
		task = uncompleted.get(0);
		assertEquals(issue,task.getIssue());
		assertEquals("high",task.getPriority());
	}

}
