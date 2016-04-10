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
		assertEquals(parse.getCommand(), "add");
		assertEquals(parse.getIssueM(), issue);

		issue = "task";
		startDate = "11/04/2016";
		processed = natty.parseString(input2);
		parse.parse(processed);
		assertEquals(parse.getCommand(), "add");
		assertEquals(parse.getIssueM(), issue);
		assertEquals(parse.getStartDate(), startDate);

		startDate = "11/04/2016/00/00";
		processed = natty.parseString(input3);
		parse.parse(processed);
		assertEquals(parse.getCommand(), "add");
		assertEquals(parse.getIssueM(), issue);
		assertEquals(parse.getStartDateWithTime(), startDate);

		endDate = "11/04/2016";
		processed = natty.parseString(input4);
		parse.parse(processed);
		assertEquals(parse.getCommand(), "add");
		assertEquals(parse.getIssueM(), issue);
		assertEquals(parse.getEndDate(), endDate);

		endDate = "12/04/2016/23/59";
		processed = natty.parseString(input5);
		parse.parse(processed);
		assertEquals(parse.getCommand(), "add");
		assertEquals(parse.getIssueM(), issue);
		assertEquals(parse.getEndDateWithTime(), endDate);

		startDate = "11/04/2016/01/00";
		processed = natty.parseString(input6);
		parse.parse(processed);
		assertEquals(parse.getCommand(), "add");
		assertEquals(parse.getIssueM(), issue);
		assertEquals(parse.getStartDateWithTime(), startDate);
		assertEquals(parse.getEndDateWithTime(), endDate);

		endDate = "11/04/2016";
		issue = "recurring task";
		processed = natty.parseString(input13);
		parse.parse(processed);
		assertEquals(parse.getCommand(), "add");
		assertEquals(parse.getIssueM(), issue);
		assertEquals(parse.getEndDate(), endDate);
		assertTrue(parse.getRecurrence());

	}

	@Test
	public void testCommandWithIndex() {
		initParser();
		description = "1";
		parse.parse(input7);
		assertEquals(parse.getCommand(), "edit");
		assertEquals(parse.getDescription(), description);

		
		parse.parse(input8);
		assertEquals(parse.getCommand(), "mark");
		assertEquals(parse.getDescription(), description);

		description = "all";
		parse.parse(input9);
		assertEquals(parse.getCommand(), "display");
		assertEquals(parse.getDescription(), description);

		description = "1";
		parse.parse(input10);
		assertEquals(parse.getCommand(), "priority");
		assertEquals(parse.getDescription(), description);

		
		parse.parse(input11);
		assertEquals(parse.getCommand(), "view");
		assertEquals(parse.getDescription(), description);

		parse.parse(input12);
		assertEquals(parse.getCommand(), "delete");
		assertEquals(parse.getDescription(), description);
	}

	@Test
	public void testCommandWithNoIndex() {
		initParser();
		description = "task";
		parse.parse(input14);
		assertEquals(parse.getCommand(), "search");
		assertEquals(parse.getDescription(), description);

		description = "";
		parse.parse(input15);
		assertEquals(parse.getCommand(), "clear");
		assertEquals(parse.getDescription(), description);

		description = "";
		parse.parse(input16);
		assertEquals(parse.getCommand(), "undo");
		assertEquals(parse.getDescription(), description);
	}

}
