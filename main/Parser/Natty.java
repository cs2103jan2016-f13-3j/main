package Parser;

//@@author Jie Wei

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class Natty {
	private static Natty natty;
	private static Parser parser;
	private static final String DATE_INDICATOR = " ` ";
	private static final String MSG_START_DATE_INDICATOR = "on";
	private static final String MSG_TODAY = "Today";
	private static final String MSG_TOMORROW = "Tomorrow";
	private static final String[] NAMES_OF_MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	private static final String[] END_DATE_KEYWORDS = {"by", "on", "before"};
	private static ArrayList<String> monthNamesList, endDateKeywordsList;
	private static boolean hasTime, hasTwoDates;

	private Natty() {
		parser = new Parser();
		monthNamesList = new ArrayList<String>(Arrays.asList(NAMES_OF_MONTHS));
		endDateKeywordsList = new ArrayList<String>(Arrays.asList(END_DATE_KEYWORDS));
		hasTime = false;
		hasTwoDates = false;
	}

	public static Natty getInstance() {
		if (natty == null) {
			natty = new Natty();
		}
		return natty;
	}

	// Method that checks whether input date string matches today's or tomorrow's date
	// Returns "Today" if matches today, "Tomorrow" if matches tomorrow
	// Otherwise return original string
	public String tryChangeTodayOrTomorrow(String source) {
		String[] split = source.split(" ");
		String convertedString = (monthNamesList.indexOf(split[1]) + 1) + "/" + split[0] + "/" + split[2]; //04 Apr 2016 to 04/04/2016
		convertedString = convertToAmericanFormat(convertedString);

		String convertedSource = parseString(convertedString);
		String convertedToday = parseString(MSG_TODAY);
		String convertedTomorrow = parseString(MSG_TOMORROW);

		if (convertedSource.equals(convertedToday)) { // input date matches today's date
			return MSG_TODAY;
		} else if (convertedSource.equals(convertedTomorrow)) { // input date matches tomorrow's date
			return MSG_TOMORROW;
		} else {
			return source; // not today or tomorrow, return original date string
		}
	}

	public String parseString(String source) {
		if (!source.contains(" ` ")) { // no date indicator was given, thus no date to parse, return as is
			return source;
		}
		
		int indexOfIndicator = source.lastIndexOf(" ` "); // last index in case user uses same sequence earlier as issue
		String stringBeforeIndicator = source.substring(0, indexOfIndicator);
		String stringAfterIndicator = source.substring(indexOfIndicator + 3); // e.g. add buy egg ` <by tomorrow>
		stringAfterIndicator = convertToAmericanFormat(stringAfterIndicator); // if user enter DD/MM/YYYY, neede to convert for natty
		List<DateGroup> dateGroups = parser.parse(stringAfterIndicator);

		if (dateGroups.isEmpty()) { // if no date was found by natty
			// return the string without the indicator, treat as floating task etc
			return source.substring(0, indexOfIndicator)+ " " + source.substring(indexOfIndicator + 3);
		}

		if (inputIncludesTime(dateGroups.get(0))) { // keep track of whether user entered date with time
			hasTime = true;
		} else {
			hasTime = false;
		}
		
		String matchingValue = dateGroups.get(0).getText();
		System.out.println("matchingvalue is " + matchingValue);
		
//		int indexOfDate = getIndexOfDetectedDate(dateGroups.get(0)); // gets index of where the issue description ends
		String result = convertDateGroupToString(dateGroups); // get a DD/MM/YYYY or DD/MM/YYYY HrHr:MinMin representation of the string

		if (stringAfterIndicator.split(" ").length == 1) { // if input was only 04/05/2016, terminate early. treat it as start date
			return stringBeforeIndicator + DATE_INDICATOR + MSG_START_DATE_INDICATOR + " " + result;
		}
		
		String[] splitArray = stringAfterIndicator.split(" ");
		if (splitArray[splitArray.length - 1].equalsIgnoreCase("r")) { // if input command ends with "r" by itself, indicates recurring
			result += " r"; // add "r" to the result string for Parser recognition
		}

		// gets "room" from "clean room tomorrow", "on" from "buy book on Friday" etc...
		String keyword = getLastWordOfIssue(dateGroups.get(0), splitArray);
		System.out.println("keyword is " + keyword);

//		if (hasTwoDates) { // if task has 2 dates detected
//			if (!keyword.equalsIgnoreCase("from")) { // if user did not type "from", we append it for Parser to recognise
//				result = "from " + result;
//			}
//		} else { // task has only 1 date detected
//			if (!endDateKeywordsList.contains(keyword) && !keyword.equals("from")) {
				// if user did not enter a end date keyword such as "by" or "before" nor "from"
//				result = "from " + result; // we append "from" to inform Parser this is a start date
			result = keyword + " " + result;
//			}
		

//		result = source.substring(0, indexOfDate) + result; // adds the issue description to the converted date string
		return stringBeforeIndicator + DATE_INDICATOR + result;
	}

	private String convertDateGroupToString(List<DateGroup> group) {
		String result = "";
		if (group.isEmpty()) { // Natty found no dates
			return result;
		}
		List<Date> dates = group.get(0).getDates();
		String originalString = dates.toString();

		if (dates.size() == 1) { // only 1 date was parsed
			result = changeOneDateStringFormat(originalString);
			hasTwoDates = false;
		} else if (dates.size() == 2) { // 2 dates were parsed
			result = changeTwoDatesStringFormat(originalString);
			hasTwoDates = true;
		}
		return result;
	}

	// Method to change one date from [Sun Apr 03 18:35:50 SGT 2016] to 03/04/201/18/35
	private String changeOneDateStringFormat(String date) {
		String result;
		result = date.substring(1, date.length() - 1); // removes the brackets at start & end
		String[] splitDates = result.split(" ");

		String time;
		if (hasTime) { // if input contained time, convert to our format
			time = splitDates[3];
			time = " " + time.substring(0, 2) + ":" + time.substring(3, 5); // converts 18:10:00 -> 18:10 for Task class to read
		} else { // if input had no time, make sure nothing is added to the result string
			time = "";
		}

		String month = splitDates[1];
		month = Integer.toString(monthNamesList.indexOf(month) + 1); // converts Apr -> 4

		if (month.length() == 1) {
			month = "0" + month; // appends 0 to single digit months
		}
		result = splitDates[2] + "/" + month + "/" + splitDates[5] + time;

		return result;
	}


	// Method to change two dates
	private String changeTwoDatesStringFormat(String date) {
		String result;
		String startDate = date.substring(0, date.indexOf(",")); // extracts original string of the first date
		startDate = startDate + "]";
		result = changeOneDateStringFormat(startDate);

		String endDate = date.substring(date.indexOf(",") + 2);	// extracts original string of the second date
		endDate = "[" + endDate;
		result = result + " to " + changeOneDateStringFormat(endDate);

		return result;
	}

	// Method to check whether the string parsed by natty contains time
	private boolean inputIncludesTime(DateGroup dateGroup) {
		String syntaxTree = dateGroup.getSyntaxTree().toStringTree();
		return syntaxTree.contains("HOURS");
	}

	// Method to change DD/MM/YYYY to MM/DD/YYYY because natty recognises the American format
	private static String convertToAmericanFormat(String input) {
		String result = "";
		String[] strings = input.split(" ");
		for (String str : strings) {
			if (str.contains("/")) { // eg 03/04/2016 or 03/04?? BUT THIS WILL INCLUDE ALL, eg him/her, maybe split & chk if can parseInt?
				String[] split = str.split("/");

				boolean isValidNumber = checkForValidNumber(split);		
				if (!isValidNumber) {  // if not numbers, eg "him/her", treat this as non-date, and continue with the for loop
					result = addToString(result, str);
					continue;
				}

				String temp = switchDayAndMonth(split);
				result = addToString(result, temp);
			} else {
				result = addToString(result, str);
			}
		}
		return result;
	}

	// Adds the input string to a destination string.
	// Will add a blank space in front of the input string if it is not the first word
	private static String addToString(String destination, String input) {
		if (destination.isEmpty()) {
			destination = input;
		} else {
			destination += " " + input;
		}
		return destination;
	}

	// Method to change 03/04 to 04/03
	private static String switchDayAndMonth(String[] input) {
		String output = input[1] + "/" + input[0];
		if (input.length == 3) {
			output += "/" + input[2]; // adds /YYYY if present
		}
		return output;
	}

	// checks whether a string array (split from a string containing "/") is of numberic format
	private static boolean checkForValidNumber(String[] input) {
		boolean isValid = true;
		for (String string : input) { // go through each split part, eg 03, 04 and 2016
			try{
				Integer.parseInt(string); // check if they are numbers
			} catch (NumberFormatException e) { // catch non-numerical strings
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	// Returns index where the detected date keyword(s) begin
	private static int getIndexOfDetectedDate(DateGroup dateGroup) {
		return dateGroup.getPosition();
	}

	// Returns the last word before the detected date keyword(s) begin
	private static String getLastWordOfIssue(DateGroup dateGroup, String[] splitArray) {
		String detectedKeyword = dateGroup.getText();
		detectedKeyword = detectedKeyword.split(" ")[0]; // get only the first word if multiple keywords found (eg Monday to Friday)
		int count = -1;
		for (String s : splitArray) {
			count++;
			if (s.equals(detectedKeyword)) {
				break;
			}			
		}
		if (count == 0) { // means user enter something like "add have lunch ` "today", without "from"/"on"/"by"/"to"
			return MSG_START_DATE_INDICATOR;
		}
		return splitArray[count - 1]; // count is where detectedKeyword is, return the word before it
	}
	
	public String getDayName(String source) {
//		System.out.println("source is " + source);
		List<DateGroup> dateGroups = parser.parse(source);
		List<Date> dates = dateGroups.get(0).getDates();
		String originalString = dates.toString(); // At this point, String is "[Mon Apr 03......"
		return originalString.substring(1,4);
	}
}
