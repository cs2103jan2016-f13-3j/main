//@@author Jung Kai
package Parser;

import java.util.*;

import javax.xml.ws.handler.LogicalHandler;

import java.time.YearMonth;
import Logic.Head;
import Logic.Undo;
import Storage.localStorage;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.fusesource.jansi.AnsiConsole;
import Task.Task;

public class parsers {

	private static int start, end, index;
	private static Command cmd;
	private static String startDate, date, issue, startTime, time, input, dateIn, dateIn2,command,description;
	private static final String[] key = { "by", "at", "during", "before", "to", "in" };
	private static boolean isRecurring = false;
	private static Command[] requiredIndex = {Command.DELETE,Command.EDIT,Command.VIEW,Command.LABEL,Command.MARK,
			Command.UNMARK,Command.PRIORITY };	

public static void processString(String s) {
	input = s;
	processInput();
	processCommand();
	processIndex();
	processIssue();
	processDate();
	checkIfRecurring();
}
	

	
	private static void setStartEndDate(String[] temp){
		if (start == -1 && end != -1) {// no start date but has end date
			startDate = "-";
			startTime = "-";
			// read date & time
			date = temp[end + 1];
			dateIn = date;
			if (hasEndTime(temp)) {// check if contain end time
				time = temp[end + 2];
				time = time.replaceAll(":", "/");
				dateIn = dateIn + "/" + time;
			} else {
				time = "-";
			}
		} else if (start != -1 && end == -1) {// has start date
			// but
			// no end date
			date = "-";
			time = "-";
			startDate = temp[start + 1];
		
			dateIn2 = startDate;

			if (hasStartTime(temp)) {
				startTime = temp[start + 2];
				startTime = startTime.replaceAll(":", "/");
				dateIn2 = dateIn2 + "/" + startTime;
			} else {
				startTime = "-";
			}
		}else { // has both start date and end date
			startDate = temp[start + 1];
			date = temp[end + 1];

			dateIn = date;
			dateIn2 = startDate;
			if (hasStartTime(temp)) {
				startTime = temp[start + 2];
				startTime = startTime.replaceAll(":", "/");
				dateIn2 = dateIn2 + "/" + startTime;
			} else {
				startTime = "-";
			}
			if (hasEndTime(temp)) {
				time = temp[end + 2];
				time = time.replaceAll(":", "/");
				dateIn = dateIn + "/" + time;

			} else {
				time = "-";
			}
		}
	}	

	/**
	 * method that return the index of first keyword present in the String[] and
	 * return -1 if no keyword is present
	 * 
	 * @param arr
	 * @return Integer
	 */
	private static int getIndexOfKey(String[] arr) {
		int idx = -1;
		for (int j = 0; j < arr.length; j++) {
			for (int i = 0; i < key.length; i++) {
				if (arr[j].equals(key[i])) {
					idx = j;
				}
			}
		}
		return idx;
	}

	/**
	 * Method that return the index of "from" from the input String[] and return
	 * -1 if no starting index is present
	 * 
	 * @param arr
	 * @return Integer
	 */
	private static int getStartingIndex(String[] arr) {
		int idx = -1;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals("from") || arr[i].equals("on")) {
				idx = i;
			}

		}
		return idx;
	}

	/**
	 * method that return a boolean value to indicate if input string[] contains
	 * starting time
	 * 
	 * @param arr
	 * @return boolean
	 */
	private static boolean hasStartTime(String[] arr) {
		boolean containTime = true;
		int start = getStartingIndex(arr);
		// if date is the last argument => no time
		if (start + 2 >= arr.length) {
			containTime = false;
		} else {
			if (!Logic.checkDate.checkTimeformat(arr[start + 2])) {
				containTime = false;
			}
		}
		return containTime;
	}

	/**
	 * method that return a boolean value to indicate if input String[] contains
	 * ending time
	 * 
	 * @param arr
	 * @return boolean
	 */
	private static boolean hasEndTime(String[] arr) {
		boolean containTime = true;
		int end = getIndexOfKey(arr);
		// if date is the last argument => no time
		if (end + 2 >= arr.length) {
			containTime = false;

		} else {
			if (!Logic.checkDate.checkTimeformat(arr[end + 2])) {
				containTime = false;
			}
		}
		return containTime;
	}

	




private static 	void checkIfRecurring() {
	isRecurring = (description.substring(description.length()-1).equals("r"));
}
	
private static void processIssue() {
	String[] temp = description.split("`");
	issue = temp[0];
}
private static void processDate() {
	String[] temp = description.split("`");
	String dateString = temp[1];
	String[] temp2 = dateString.split(" ");
	setStartEndDate(temp2);
}
private static void processIndex() {
	index = -1;
	for (int i = 0;i<requiredIndex.length;i++) {
		if (cmd.equals(requiredIndex[i])) {
			index = Integer.parseInt(description);
			break;
		}		
	} 

}

private static void processInput() {
	String[] arr = input.split(" ");
	// get command
	 command = arr[0];
	// handle lines with only the command
	if (command.length() == input.length()) {
		description = "";
	} else {
		// get description
		description = input.substring(command.length() + 1, input.length());
	}
}
private static void processCommand() {
	if (command.length() != 0) {
		if (command.equals("add")||command.equals("+")||command.equals("-")) {
			cmd = Command.ADD;
		} else if (command.equals("delete")||command.equals("-")) {
			cmd = Command.DELETE;
		} else if (command.equals("display")||command.equals("d")) {
			cmd = Command.DISPLAY;
		} else if (command.equals("view")||command.equals("v")) {
			cmd = Command.VIEW;
		} else if (command.equals("clear")|| command.equals("c")) {
			cmd = Command.CLEAR;
		} else if (command.equals("sort")) {
			cmd = Command.SORT;
		} else if (command.equals("search")||command.equals("s")) {
			cmd = Command.SEARCH;
		} else if (command.equals("mark")||command.equals("m")) {
			cmd = Command.MARK;
		} else if (command.equals("unmark")||command.equals("um")) {
			cmd = Command.UNMARK;
		} else if (command.equals("edit")||command.equals("e")) {
			cmd = Command.EDIT;
		} else if (command.equals("priority")||command.equals("p")) {
			cmd = Command.PRIORITY;
		} else if (command.equals("history")) {
			cmd = Command.HISTORY;
		} else if (command.equals("future")) {
			cmd = Command.FUTURE;
		} else if (command.equals("undo")) {
			cmd = Command.UNDO;
		} else if (command.equals("redo")) {
			cmd = Command.REDO;
		} else if (command.equals("exit")) {
			cmd = Command.EXIT;
		} else if (command.equals("help")) {
			cmd = Command.HELP;
		} else if (command.equals("dir")) {
			cmd = Command.DIR;
		} else if (command.equals("label")) {
			cmd = Command.LABEL;			
	} else {
		 cmd = Command.INVALID;
	}
}
}	
//getter method
//return existing issue processed by the parser
	public static String getIssue() {
		return issue;
	}

	// return existing date processed by the parser
	public static String getStartDate() {
		return dateIn2;
	}
	public static String getEndDate() {
		return dateIn;
	}
	public static int getIndex() {
		return index;
	}
	public static boolean getIsRecurring() {
		return isRecurring;
	}
	public static Command getCommand() {
		return cmd;
	}
}

