//@@author Jie Wei

package Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class Natty {
	
	private static Natty natty;
	
	private static ArrayList<String> monthNamesList;
	private static boolean hasTime;
	private static Parser nattyParser; // This Parser is the one included in Natty's jar file, not our project Parser class.
	
	private static final String DATE_INDICATOR = " ` ";
	private static final String MSG_START_DATE_KEYWORD = "on";
	private static final String MSG_TODAY = "Today";
	private static final String MSG_TOMORROW = "Tomorrow";
	private static final String[] NAMES_OF_MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

	private Natty() {
		nattyParser = new Parser();
		monthNamesList = new ArrayList<String>(Arrays.asList(NAMES_OF_MONTHS));
		hasTime = false;
	}

	public static Natty getInstance() {
		if (natty == null) {
			natty = new Natty();
		}
		return natty;
	}

	/**
	 * Method to convert a date to "Today", "Tomorrow" if applicable.
	 * 
	 * @param source The date to convert.
	 * @return       The converted date.
	 */
	public String tryChangeTodayOrTomorrow(String source) {
		String result = parseDay(source);
		return result;
	}

	/**
	 * Method to check if a given date is today, tomorrow or neither.
	 * If is today or tomorrow, returns "Today" or "Tomorrow" instead.
	 * 
	 * @param sourceDay The date to check.
	 * @return          The checked result.
	 */
	private String parseDay(String sourceDay) {
		Calendar today = GregorianCalendar.getInstance(); // create today's Calendar object and then its string form
		String todayString = today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.MONTH) + "/" + today.get(Calendar.YEAR);
		
		String[] splitDates = sourceDay.split(" "); // split the input date to create a string
		int year = Integer.parseInt(splitDates[2]);
		int month = monthNamesList.indexOf(splitDates[1]);
		int day = Integer.parseInt(splitDates[0]);
		Calendar input = new GregorianCalendar(year, month, day); // create a Calendar object of the input date
		String inputString = input.get(Calendar.DAY_OF_MONTH) + "/" + input.get(Calendar.MONTH) + "/" + input.get(Calendar.YEAR);
		
		if (inputString.equals(todayString)) { // input date matches with today
			return MSG_TODAY;
		}

		today.add(Calendar.DAY_OF_MONTH, 1); // add 1 day to get tomorrow's Calendar object and then its string form
		String tomorrowString = today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.MONTH) + "/" + today.get(Calendar.YEAR);
		
		if (inputString.equals(tomorrowString)) { // input date matches with tomorrow
			return MSG_TOMORROW;
		}
		return sourceDay; // input is neither today nor tomorrow
	}

	/**
	 * Method to check a user entered command during add for date(s) and convert it to a format that our Parser recognises.
	 * 
	 * @param source The user-entered command.
	 * @return       The converted command.
	 */
	public String parseString(String source) {
		if (!source.contains(" ` ")) {
			// no date indicator was given, thus no date to parse, return as is
			return source;
		}

		int indexOfIndicator = source.lastIndexOf(" ` "); // last index in case user uses same sequence earlier as issue
		String stringBeforeIndicator = source.substring(0, indexOfIndicator);
		String stringAfterIndicator = source.substring(indexOfIndicator + 3); // string containing date information
		stringAfterIndicator = convertToAmericanFormat(stringAfterIndicator); // if user enter DD/MM/YYYY, need to convert for natty
		List<DateGroup> dateGroups = nattyParser.parse(stringAfterIndicator);

		if (dateGroups.isEmpty()) { // if no date was found by natty
			// return the string without the indicator, treat as floating task etc
			return source.substring(0, indexOfIndicator)+ " " + source.substring(indexOfIndicator + 3);
		}

		hasTime = inputIncludesTime(dateGroups.get(0)); // keep track of whether user entered date with time

		String result = convertDateGroupToString(dateGroups); // get a DD/MM/YYYY or DD/MM/YYYY HrHr:MinMin representation of the string

		if (stringAfterIndicator.split(" ").length == 1) {
			// if input was only 04/05/2016, terminate early. treat it as start date
			return stringBeforeIndicator + DATE_INDICATOR + MSG_START_DATE_KEYWORD + " " + result;
		}

		String[] splitArray = stringAfterIndicator.split(" ");
		if (splitArray[splitArray.length - 1].equalsIgnoreCase("r")) {
			// if input command ends with "r" by itself, indicates recurring
			result += " r"; // add "r" to the result string for Parser recognition
		}

		// gets "on" from "buy book on Friday" etc.
		String keyword = getFirstKeywordOfDate(dateGroups.get(0), splitArray);

		result = keyword + " " + result;
		return stringBeforeIndicator + DATE_INDICATOR + result; // adds the issue description, date indicator and parsed date together
	}
	
	/**
	 * Method to check a user entered command during edit for date(s) and convert it to a format that our Parser recognises.
	 * 
	 * @param input The user-entered command.
	 * @return      The converted command.
	 */
	public String parseEditString(String input) {
		if (!input.contains(" ` ")) {
			// no date indicator was given, no date to parse, return as is
			return input;
		}
		
		int indexOfIndicator = input.lastIndexOf(" ` ");
		String issueDescription =  input.substring(0, indexOfIndicator);
		String stringAfterIndicator = input.substring(indexOfIndicator + 3); // string containing date information
		stringAfterIndicator = convertToAmericanFormat(stringAfterIndicator); // if user enter DD/MM/YYYY, need to convert for natty
		List<DateGroup> dateGroups = nattyParser.parse(stringAfterIndicator);

		if (dateGroups.isEmpty()) { // if no date was found by natty
			// return the string without the indicator, treat as floating task etc
			return issueDescription + " " + stringAfterIndicator;
		}

		hasTime = inputIncludesTime(dateGroups.get(0)); // keep track of whether user entered date with time

		String result = convertDateGroupToString(dateGroups); // get a DD/MM/YYYY or DD/MM/YYYY HrHr:MinMin representation of the string

		if (stringAfterIndicator.split(" ").length == 1) {
			// if input was only 1 word, terminate early. treat as start date
			return issueDescription + DATE_INDICATOR + MSG_START_DATE_KEYWORD + " " + result;
		}

		String[] splitArray = stringAfterIndicator.split(" ");
		if (splitArray[splitArray.length - 1].equalsIgnoreCase("r")) {
			// if input command ends with "r" by itself, indicates recurring
			result += " r"; // add "r" to the result string for Parser recognition
		}

		// gets "on" from "buy book on Friday" etc.
		String keyword = getFirstKeywordOfDate(dateGroups.get(0), splitArray);

		result = keyword + " " + result;
		return issueDescription + DATE_INDICATOR + result; // adds the issue description, date indicator and parsed date together
	}

	/**
	 * Method to convert a list of DateGroups to a string representation.
	 * 
	 * @param groups The list containing DateGroup objects (if any).
	 * @return       String representation of the date(s) found.
	 */
	private String convertDateGroupToString(List<DateGroup> groups) {
		String result = "";
		if (groups.isEmpty()) { // Natty found no dates
			return result;
		}
		List<Date> dates = groups.get(0).getDates();
		String originalString = dates.toString();

		if (dates.size() == 1) { // only 1 date was detected
			result = changeOneDateStringFormat(originalString);
		} else if (dates.size() == 2) { // 2 dates were detected
			result = changeTwoDatesStringFormat(originalString);
		}
		return result;
	}

	/**
	 * Method to convert a string of 1 date to DD/MM/YYYY or DD/MM/YYYY HrHr:MinMin (if time is included).
	 * 
	 * @param date The string containing the date.
	 * @return     A string of the converted date.
	 */
	private String changeOneDateStringFormat(String date) {
		String result = date.substring(1, date.length() - 1); // removes the brackets at start & end
		String[] splitDates = result.split(" ");

		String time;
		if (hasTime) { // if input contained time, convert to our format
			// converts 18:10:00 -> 18:10 for Task class to read
			time = splitDates[3];
			time = " " + time.substring(0, 2) + ":" + time.substring(3, 5);
		} else { // if input had no time, make sure nothing is added to the result string
			time = "";
		}

		String month = splitDates[1];
		month = Integer.toString(monthNamesList.indexOf(month) + 1); // converts Apr -> 4

		if (month.length() == 1) {
			month = "0" + month; // adds 0 to single digit months
		}
		
		return splitDates[2] + "/" + month + "/" + splitDates[5] + time;
	}

	/**
	 * Method to convert a string of 2 dates to DD/MM/YYYY or DD/MM/YYYY HrHr:MinMin (if time is included).
	 * 
	 * @param date The string containing 2 dates.
	 * @return     A string of the converted dates separated by "to".
	 */
	private String changeTwoDatesStringFormat(String date) {
		String startDate = date.substring(0, date.indexOf(",")); // extracts original string of the first date
		startDate = startDate + "]";
		String result = changeOneDateStringFormat(startDate);

		String endDate = date.substring(date.indexOf(",") + 2);	// extracts original string of the second date
		endDate = "[" + endDate;
		result = result + " to " + changeOneDateStringFormat(endDate);

		return result;
	}

	/**
	 * Method to check whether a date detected by natty contains a specified time.
	 * 
	 * @param dateGroup The detected date to be checked.
	 * @return          Whether time is found.
	 */
	private boolean inputIncludesTime(DateGroup dateGroup) {
		String syntaxTree = dateGroup.getSyntaxTree().toStringTree();
		return syntaxTree.contains("HOURS");
	}

	/**
	 * Method to change a DD/MM/YYYY to MM/DD/YYYY (American format) if present in a string.
	 * Natty recognises date in the American format.
	 * 
	 * @param input The date string to be checked.
	 * @return      The changed date string.
	 */
	private static String convertToAmericanFormat(String input) {
		String[] strings = input.split(" ");
		return checkForDateFormatAndChange(strings);
	}
	
	/**
	 * Method to check each word in a string array for DD/MM/YYYY format.
	 * If found, change it to MM/DD/YYYY format.
	 * 
	 * @param input The string array to check from.
	 * @return      A string of all the words combined.
	 */
	private static String checkForDateFormatAndChange(String[] input) {
		String result = "";
		for (String s : input) {
			if (s.contains("/")) { // e.g. 03/04/2016
				String[] split = s.split("/");
				boolean isValidNumber = checkForValidNumber(split);	
				
				if (!isValidNumber) {
					// if not number, eg "him/her", treat this as non-date and continue
					result = addToString(result, s);
					continue;
				}

				String temp = switchDayAndMonth(split);
				result = addToString(result, temp);
			} else {
				// current string is not DD/MM/YYYY, add it back as it is
				result = addToString(result, s);
			}
		}
		return result;
	}
	
	/**
	 * Method to add a string to a destination string with proper spacing.
	 * 
	 * @param destination The string to add to.
	 * @param input       The string to be added.
	 * @return            The combined string.
	 */
	private static String addToString(String destination, String input) {
		if (destination.isEmpty()) {
			destination = input;
		} else {
			
			destination += " " + input;
		}
		return destination;
	}

	/**
	 * Method to switch positions of month and day in a date.
	 * 
	 * @param input A string array of original date.
	 * @return      The date with month and day swapped.
	 */
	private static String switchDayAndMonth(String[] input) {
		String output = input[1] + "/" + input[0];
		if (input.length == 3) {
			output += "/" + input[2]; // adds /YYYY if present
		}
		return output;
	}

	/**
	 * Method to check whether a date of DD/MM/YYYY format only contains numbers (other than the /).
	 * 
	 * @param input The date to verify from. Date has already been split by /.
	 * @return      Whether the date is only numerical.
	 */
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

	/**
	 * Method to return a keyword indicating start or end date from user-entered dateGroup
	 * 
	 * @param dateGroup  Object containing information on user-entered date.
	 * @param splitArray Array of entire user-entered date string that is split by whitespace.
	 * @return           Keyword representing start or end date.
	 */
	private static String getFirstKeywordOfDate(DateGroup dateGroup, String[] splitArray) {
		String detectedKeyword = dateGroup.getText();
		detectedKeyword = detectedKeyword.split(" ")[0]; // get only the first word if multiple keywords found (eg Monday to Friday)
		
		int position = getPositionOfKeyword(detectedKeyword, splitArray);
		if (position == 0) { // means user enter something like "add have lunch ` today", without from/on/by/to
			return MSG_START_DATE_KEYWORD;
		}
		return splitArray[position - 1];
	}
	
	/**
	 * Method to return the position of a keyword within a string split by whitespace.
	 * 
	 * @param keyword     The keyword to look for.
	 * @param stringArray The string split by whitespace to search from.
	 * @return            Position of the keyword.
	 */
	private static int getPositionOfKeyword(String keyword, String[] stringArray) {
		int count = -1;
		for (String s : stringArray) {
			count++;
			if (s.equals(keyword)) {
				break;
			}			
		}
		return count;
	}

	/**
	 * Method to retrieve day name of a date, such as Sun, Mon, Tue etc.
	 * 
	 * @param source The input date to get day name from.
	 * @return       Day name of a date.
	 */
	public String getDayName(String source) {
		List<DateGroup> dateGroups = nattyParser.parse(source);
		List<Date> dates = dateGroups.get(0).getDates();
		String originalString = dates.toString(); // At this point, String is "[Mon Apr 03..."
		return originalString.substring(1,4);
	}
}
