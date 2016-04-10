package unitTest;

import static org.junit.Assert.*;
import Parser.Parser;
import Task.Task;
import java.util.ArrayList;
import org.junit.Test;

public class parserTest {

	@Test
	public void splitTest() {
		try {
			String s = "task 1 before 20/03/2016";
			String cmd = "add";
		
			String testDescription = "task 1";
			String testDate = "20/03/2016";
			assertTrue(Parser.run(cmd,s));
			String description = Parser.getIssue();
			String date = Parser.getDate();
			assertEquals(testDescription, description);
			assertEquals(testDate, date);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Test
	public void SearchTest() {
		try {
			Parser.run("add" ,"mission 3");
			String cmd = "search";
			String s = "mission";
			Parser.run("display","");
			assertTrue(Parser.run(cmd,s));
			ArrayList<Task> arr = Logic.search.getSearchedTasks();
			assertEquals(arr.get(0).getTaskString(),"mission 3");
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	@Test
	public void deleteTest() {

		try {
			String s = "1";
			String cmd = "delete";
			ArrayList<Task> list = Storage.LocalStorage.getUncompletedTasks();
			Task deleted = list.get(0);
			assertTrue(Parser.run(cmd,s));			
			assertEquals(deleted.getIssue(),"task 1");
			Parser.run("clear"," ");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
