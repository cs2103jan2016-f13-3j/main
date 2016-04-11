//@@author Jung Kai
package unitTest;

import static org.junit.Assert.*;
import Parser.Parser;
import Parser.Natty;
import Task.Task;
import java.util.ArrayList;
import org.junit.Test;

public class ParserTest {
	private static final String input1 = "add floating task";
	private static final String input2 = "add task ` from 11/04/2016";
	private static final String input3 = "add task ` from 11/04/2016 12am";
	private static final String input4 = "add task ` by today";
	private static final String input5 = "add task ` by tomorrow 23:59";
	private static final String input6 = "add task ` from today 1am to tuesday 23:59";
	private static final String input7 = "edit 1";
	private static final String input8 = "mark 1";
	private static final String input9 = "display all";
	private static final String input10 = "priority 1";
	private static final String input11 = "view 1";
	private static final String input12 = "delete 1";
	private static final String input13 = "add recurring task ` by 11/04/2016 r";
	private static final String input14 = "search task";
	private static final String input15 = "clear";
	private static final String input16 = "undo";

	private String issue, startDate, endDate, description, processed;
	private boolean isRecurring;
	protected Parser parse;
	protected Natty natty;

	/**
	 * To set up this test
	 */
	protected void initParser() {
		parse = Parser.getInstance();
	}

	protected void initNatty() {
		natty = Natty.getInstance();
	}

	@Test
	public void testAdd() {
		initParser();
		initNatty();
		issue = "floating task";
		processed = natty.parseString(input1);
		parse.parse(processed);
		assertEquals("add", parse.getCommand());
		assertEquals(issue,parse.getIssueM());

		issue = "task";
		startDate = "11/04/2016";
		processed = natty.parseString(input2);
		parse.parse(processed);
		assertEquals("add",parse.getCommand());
		assertEquals(issue,parse.getIssueM());
		assertEquals(startDate,parse.getStartDate());

		startDate = "11/04/2016/00/00";
		processed = natty.parseString(input3);
		parse.parse(processed);
		assertEquals("add",parse.getCommand());
		assertEquals(issue,parse.getIssueM());
		assertEquals(startDate,parse.getStartDateWithTime());

		endDate = "11/04/2016";
		processed = natty.parseString(input4);
		parse.parse(processed);
		assertEquals("add",parse.getCommand());
		assertEquals(issue,parse.getIssueM());
		assertEquals(endDate,parse.getEndDate());

		endDate = "12/04/2016/23/59";
		processed = natty.parseString(input5);
		parse.parse(processed);
		assertEquals("add",parse.getCommand());
		assertEquals(issue,parse.getIssueM());
		assertEquals(endDate,parse.getEndDateWithTime());

		startDate = "11/04/2016/01/00";
		processed = natty.parseString(input6);
		parse.parse(processed);
		assertEquals( "add",parse.getCommand());
		assertEquals(issue,parse.getIssueM());
		assertEquals(startDate,parse.getStartDateWithTime());
		assertEquals(endDate,parse.getEndDateWithTime());

		endDate = "11/04/2016";
		issue = "recurring task";
		processed = natty.parseString(input13);
		parse.parse(processed);
		assertEquals("add",parse.getCommand());
		assertEquals(issue,parse.getIssueM());
		assertEquals(endDate,parse.getEndDate());
		assertTrue(parse.getRecurrence());

	}

	@Test
	public void testCommandWithIndex() {
		initParser();
		description = "1";
		parse.parse(input7);
		assertEquals("edit",parse.getCommand());
		assertEquals(description,parse.getDescription());
		
		parse.parse(input8);
		assertEquals("mark",parse.getCommand());
		assertEquals(description,parse.getDescription());

		parse.parse(input10);
		assertEquals("priority",parse.getCommand());
		assertEquals(description,parse.getDescription());

		
		parse.parse(input11);
		assertEquals("view",parse.getCommand());
		assertEquals(description,parse.getDescription());

		parse.parse(input12);
		assertEquals("delete",parse.getCommand());
		assertEquals(description,parse.getDescription());
	}

	@Test
	public void testCommandWithNoIndex() {
		initParser();
		description = "all";
		parse.parse(input9);
		assertEquals("display",parse.getCommand());
		assertEquals(description,parse.getDescription());
		
		description = "task";
		parse.parse(input14);
		assertEquals("search",parse.getCommand());
		assertEquals(description,parse.getDescription());

		description = "";
		parse.parse(input15);
		assertEquals("clear",parse.getCommand());
		assertEquals(description,parse.getDescription());

		description = "";
		parse.parse(input16);
		assertEquals("undo",parse.getCommand());
		assertEquals(description,parse.getDescription());
	}

}
