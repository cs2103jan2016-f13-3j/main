package unitTest;

import static org.junit.Assert.*;

import org.junit.Test;

import Task.Task;

public class taskTest {


	//	Task testTask3 = new Task("");
	//	Task testTask3 = new Task(null);

	/**
	 * Equivalence partitions:
	 * Valid - [any non-null string]
	 * Invalid - [null] 
	 */
	@Test
	public void testTaskNoDate() {
		String result = "";
		Task testTask1 = new Task("test sample 1"); // valid input
		Task testTask2 = new Task(""); // boundary case for non-null string
		try {
			Task testTask3 = new Task(null); // invalid input and boundary case for [null] string, assertion error expected
		} catch (AssertionError e) {
			result = "null assertion";
		}
		assertEquals(result, "null assertion");
	}

	/**
	 * Equivalence partitions:
	 * Valid - [non-null string for issue, date string in DD/MM/YYYY format]
	 * Invalid - [null],[date not in DD/MM/YYYY format]
	 */
	@Test
	public void testTaskWithDate() {
		/*Task testTask1 = new Task("test sample 1", "12/12/2011");
		assertEquals("12/12/2011", testTask1.getDateString());
		String result = "";
		try {
			Task testTask2 = new Task(null); // invalid input and boundary case for [null] string, assertion error expected
		} catch (AssertionError e) {
			result = "null assertion";
		}
		assertEquals(result, "null assertion");
		try {
			Task testTask3 = new Task("test issue 2", "01-02-1999"); // invalid input and boundary case for [non-DD/MM/YYYY format] string, assertion error expected
		} catch (AssertionError e) {
			result = "null assertion";
		}
		assertEquals(result, "null assertion");*/
	}

	/**
	 * Equivalence partitions:
	 * Valid - [any non-null string]
	 * Invalid - [null] 
	 * 
	 */
	@Test
	public void testSetIssue() {
		Task testTask1 = new Task("test sample 1");
		assertEquals("test sample 1", testTask1.getIssue());
		testTask1.setIssue("new issue");
		assertEquals("new issue", testTask1.getIssue());
		testTask1.setIssue(""); // boundary case for non-null string
		assertEquals("", testTask1.getIssue());
		String result = "";
		try {
			testTask1.setIssue(null); // invalid input and boundary case for [null] string, assertion error expected
		} catch (AssertionError e) {
			result = "null assertion";
		}
		assertEquals(result, "null assertion");
	}

	@Test
	public void testGetTaskString() {
		/*
		Task testTask1 = new Task("test sample 1");
		Task testTask2 = new Task("test sample 2", "12/12/2011");
		assertEquals("test sample 1", testTask1.getTaskString());
		assertEquals("test sample 2 12/12/2011", testTask2.getTaskString());
		//		assertEquals("", testTask3.getTaskString());
		//		System.out.println(testTask3.getTaskString());
		 
		 */
	}

}
