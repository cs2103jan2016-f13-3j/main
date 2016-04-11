//@@author Jie Wei
package unitTest;

import static org.junit.Assert.*;

import org.junit.Test;

import Task.Task;

public class TaskTest {

	/**
	 * Equivalence partitions:
	 * Valid - [any non-null string]
	 * Invalid - [null] 
	 */
	@Test
	public void testTaskNoDate() {
		String result = "";
		Task testTask1 = new Task("test sample 1"); // valid input.
		Task testTask2 = new Task(""); // boundary case for non-null string.
		try {
			Task testTask3 = new Task(null); // invalid input and boundary case for [null] string, assertion error expected.
		} catch (AssertionError e) {
			result = "";
		}
		assertEquals(result, "");
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
		testTask1.setIssue(""); // boundary case for non-null string.
		assertEquals("", testTask1.getIssue());
		String result = "";
		try {
			testTask1.setIssue(null); // invalid input and boundary case for [null] string, assertion error expected.
		} catch (AssertionError e) {
			result = "null assertion";
		}
		assertEquals(result, "");
	}
}
