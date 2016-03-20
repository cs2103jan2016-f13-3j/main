package unitTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import Logic.checkDate;
import Logic.crud;
import Task.Task;

public class crudTest {

	@Test
	public void testAddTaskString() {
		boolean test=false;
		try {
			test = Logic.crud.addTask("testing");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true,test);
	}
	@Test
	public void testTaskAdded() throws IOException{
		Logic.crud.clearTasks();
		boolean test= Logic.crud.addTask("testing");
		Logic.crud.displayUncompletedTasks();
		int a =Logic.crud.getTemp().size();
		assertEquals(a,1);
		
	}	
	@Test
	public void testEditTask() throws IOException{
		
		boolean test= Logic.crud.addTask("testing");
		Logic.crud.editTask(0,"lalala");
		Logic.crud.displayUncompletedTasks();
		ArrayList<Task> test1=Logic.crud.getTemp();
		String msg=test1.get(0).getIssue();
		assertEquals(msg,"lalala");
		
	}
	@Test
	public void testCheckDateformat() {
		final Logger logger= Logger.getLogger( Class.class.getName() );
		try{
		String date="12/11/1212";
		boolean check =Logic.checkDate.checkDateformat(date);
		
		}catch(ArrayIndexOutOfBoundsException e){
			logger.log( Level.SEVERE, e.toString(), e );
			
		}
	}

}
