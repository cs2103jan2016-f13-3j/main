package unitTest;

import static org.junit.Assert.*;

import org.junit.Test;

import Parser.parser;

public class parserTest {

	@Test
	public void splitTest() {
		try {
			String s = "task 1 before 20/03/2016";
			String cmd = "add";
		
			String testDescription = "task 1";
			String testDate = "20/03/2016";
			assertTrue(parser.run(cmd,s));
			String description = parser.getIssue();
			String date = parser.getDate();
			System.out.println("***"+ date+ "****");
			assertEquals(testDescription, description);
			assertEquals(testDate, date);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteTest() {

		try {
			String s = "1";
			String cmd = "delete";
	
			assertTrue(parser.run(cmd,s));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void markTest() {
		try {
			parser.run("add","task 2 by 20/03/2016");
			parser.run("add" ,"task 3 by 19/03/2016");
			String cmd = "mark";
			String s = "1";
		
			assertTrue(parser.run(cmd,s));
			parser.run("clear"," ");
			parser.run("exit"," ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
