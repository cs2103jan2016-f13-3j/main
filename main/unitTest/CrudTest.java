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

import org.junit.Test;

import Logic.checkDate;
import Logic.crud;
import Task.Task;

public class crudTest {
	@Test
	public void testuncompletedTaskIndexWithNoDate() throws ClassNotFoundException, IOException{
		int test =Logic.crud.uncompletedTaskIndexWithNoDate("test");
		//final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		//System.setOut(new PrintStream(outContent));
		//Logic.Head.runProgram();
		
		
		assertEquals(test,-1);
		//ByteArrayInputStream in = new ByteArrayInputStream("exit".getBytes());
		//System.setIn(in);
		//assertEquals("test",1);
		// System.out.println("test"+outContent.toString());

		 
	}
	@Test
	public void testCopyTask() throws HeadlessException, UnsupportedFlavorException, IOException{
		Task test = new Task("test");
		Logic.crud.copyTask(test);
		String data = (String) Toolkit.getDefaultToolkit()
                .getSystemClipboard().getData(DataFlavor.stringFlavor);
		assertEquals("test",data);
	}
	/*
	@Test
	public void testAddTaskString() throws ClassNotFoundException {
		boolean test=false;
		try {
			test = Logic.crud.addTask("testing");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true,test);
	}*/
	/*
	@Test
	public void testTaskAdded() throws IOException, ClassNotFoundException{
		Logic.crud.clearTasks();
		boolean test= Logic.crud.addTask("testing");
		Logic.crud.displayUncompletedTasks();
		int a =Logic.crud.getTemp().size();
		assertEquals(a,1);
		
	}	
	@Test
	public void testEditTask() throws IOException, ClassNotFoundException{
		
		boolean test= Logic.crud.addTask("testing");
		Logic.crud.editTask(0,"lalala");
		Logic.crud.displayUncompletedTasks();
		ArrayList<Task> test1=Logic.crud.getTemp();
		String msg=test1.get(0).getIssue();
		assertEquals(msg,"lalala");
		
	}*/
	/*
	@Test
	public void testCheckDateformat() {
		final Logger logger= Logger.getLogger( Class.class.getName() );
		try{
		String date="12/11/1212";
		boolean check =Logic.checkDate.checkDateformat(date);
		
		}catch(ArrayIndexOutOfBoundsException e){
			logger.log( Level.SEVERE, e.toString(), e );
			
		}
	}*/

}
