//@@author Cheng Gee
package unitTest;

import static org.junit.Assert.*;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.fusesource.jansi.Ansi.ansi;

import org.fusesource.jansi.AnsiConsole;
import org.junit.Test;


import Logic.CheckDate;
import Logic.Crud;
import Storage.LocalStorage;
import Task.Task;

public class CrudTest {
	@Test
	public void testuncompletedTaskIndexWithNoDate() throws ClassNotFoundException, IOException{
		Logic.Crud test = Crud.getInstance();
		int output =test.uncompletedTaskIndexWithNoDate("test");
		assertEquals(output,-1); 
	}
	@Test
	public void testcopyTask() throws HeadlessException, UnsupportedFlavorException, IOException{
		Task output = new Task("test");
		Logic.Crud test = Crud.getInstance();
		test.copyTask(output);
		String data = (String) Toolkit.getDefaultToolkit()
                .getSystemClipboard().getData(DataFlavor.stringFlavor);
		assertEquals("test",data);
	}
	@Test
	public void testaddTask() throws ClassNotFoundException, IOException{
		Logic.Crud test = Crud.getInstance();
		boolean output = test.addTask("test");
		assertEquals(output,true);
		
	}
	
	@Test
	public void testaddTaskWithStartDate() throws ClassNotFoundException, IOException{
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		Logic.Crud test = Crud.getInstance();
		boolean output = test.addTaskWithStartDate("test","11/4/2016","test ` 11/4/2016");
		assertEquals(output,true);
	}
	
	@Test
	public void testaddTaskWithEndDate() throws ClassNotFoundException, IOException{
		Logic.Crud test = Crud.getInstance();
		boolean output = test.addTaskWithEndDate("test","11/4/2016","test ` 11/4/2016");
		assertEquals(output,true);
	}
	
	@Test
	public void testaddTaskWithBothDate() throws ClassNotFoundException, IOException{
		Logic.Crud test = Crud.getInstance();
		boolean output = test.addTaskWithBothDates("test","11/4/2016","20/4/2016", "test ` from 11/4/2016 to 20/4/2016");
		assertEquals(output,true);
	}
	
	@Test
	public void testaddLabeltoTask() throws ClassNotFoundException, IOException{
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		
		Logic.Crud test = Crud.getInstance();
		test.addTask("test");
		test.addLabelToTask(0, "first");
		
		Task temp= temp1.getFloatingTask(0);
		String output = temp.getLabel().get(0);
		assertEquals(output,"first");
	}
	@Test
	public void testeditTaskWithNoDate() throws ClassNotFoundException, IOException{
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		
		Logic.Crud test = Crud.getInstance();
		test.addTask("test");
		test.editTaskWithNoDate("Hahaha", "Hahaha", 0);
		Task temp= temp1.getFloatingTask(0);
		String output = temp.getIssue();
		assertEquals(output,"Hahaha");
	}
	
	@Test
	public void testeditTaskWithStartDate() throws ClassNotFoundException, IOException{
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		
		Logic.Crud test = Crud.getInstance();
		test.addTaskWithStartDate("test","11/4/2016","test ` 11/4/2016");
		Task temp= temp1.getUncompletedTask(0);
		String output = temp.getDescription();
		assertEquals(output,"test ` 11/4/2016");
	}
	@Test
	public void testeditTaskWithEndDate() throws ClassNotFoundException, IOException{
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		
		Logic.Crud test = Crud.getInstance();
		test.addTaskWithEndDate("test","11/4/2016","test ` 11/4/2016");
		Task temp= temp1.getUncompletedTask(0);
		String output = temp.getDescription();
		assertEquals(output,"test ` 11/4/2016");
	}
	public void testeditTaskBothEndDates() throws ClassNotFoundException, IOException{
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		
		Logic.Crud test = Crud.getInstance();
		test.addTaskWithBothDates("test","11/4/2016","20/4/2016", "test ` from 11/4/2016 to 20/4/2016");
		Task temp= temp1.getUncompletedTask(0);
		String output = temp.getDescription();
		assertEquals(output,"test ` from 11/4/2016 to 20/4/2016");
	}
	@Test
	public void testdisplayCompletedList(){
		LocalStorage temp1 = LocalStorage.getInstance();
		temp1.clearAllTasks();
		Logic.Crud test = Crud.getInstance();
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
				test.displayCompletedTasks();
				String output = "\u001B[1m\u001B[32mThere is no stored task to display"+ansi().reset();
				assertEquals(output.trim(),outContent.toString().trim());
				System.out.println("test"+outContent.toString());
	}
}
