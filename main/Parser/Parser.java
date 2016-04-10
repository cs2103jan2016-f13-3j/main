//@@author Jung Kai
package Parser;


import java.util.Arrays;

import Logic.CheckDate;


public class Parser {

	private static boolean rec,containDate;
	private static CheckDate checkDateObject = new CheckDate();
	private static String command,sd,stime,sdWithTime,ed,etime,edWithTime,originalMsg,issueM;
	
	private static final String[] KEY = { "by", "at", "during", "before", "to", "in" };

	public static void parse(String description){
		String[] splitCommand = description.split(" ");
		command = splitCommand[0];
		if(splitCommand.length==1){
			originalMsg="";
			issueM=originalMsg;
			sd="-";
			stime="-";
			ed="-";
			etime="-";
			sdWithTime="";
			edWithTime="";
			rec=false;
		}else{
			rec=false;
			containDate=false;
			if(splitCommand[splitCommand.length-1].equals("r")){
				rec=true;
			}
			int i=description.indexOf(" ");
			originalMsg=description.substring(i+1);
			issueM=originalMsg;
			sd="-";
			stime="-";
			ed="-";
			etime="-";
			sdWithTime="";
			edWithTime="";
			if(originalMsg.contains(" ` ")){
				containDate=true;
				String[] splitDate= originalMsg.split(" ` ");
				issueM=splitDate[0];
				String[] temp=splitDate[1].split(" ");
				int start = getStartingIndex(temp); // start has value of -1
				int end = getIndexOfKey(temp);
				// end has value of -1 if it
				if(end<start){
					end=-1;
				}
				if(start!=-1 ){
					sd=temp[start+1];
					if(hasStartTime(temp)){
						stime=temp[start+2];
						stime = stime.replaceAll(":", "/");
						sdWithTime = sd + "/" + stime;
					}else{
						sdWithTime = sd;
					}
				}

				if(end!=-1){
					ed=temp[end+1];
					if(hasEndTime(temp)){
						etime=temp[end+2];
						etime = etime.replaceAll(":", "/");
						edWithTime = ed + "/" + etime;
					}else{
						edWithTime=ed;
					}
				}
			}
		}
	}

	public static String getStartDateWithTime(){
		return sdWithTime;
	}

	public static String getEndDateWithTime(){
		return edWithTime;
	}

	public static boolean getContainDate(){
		return containDate;
	}

	public static boolean getRecurrence(){
		return rec;
	}

	public static String getCommand(){
		return command;
	}

	public static String getStartDate(){
		return sd;
	}

	public static String getEndDate(){
		return ed;
	}

	public static String getStartTime(){
		return stime;
	}

	public static String getEndTime(){
		return etime;
	}

	public static String getDescription(){
		return originalMsg;
	}

	public static String getIssueM(){
		return issueM;
	}


	// @@author Jung Kai
	/**
	 * method that convert String[] to String
	 * 
	 * @param arr
	 * @return String
	 */
	public static String arrayToString(String[] arr) {
		String temp = Arrays.toString(arr);
		temp = temp.substring(1, temp.length() - 1).replaceAll(", ", " ");
		return temp;
	}

	/**
	 * method that return the index of first keyword present in the String[] and
	 * return -1 if no keyword is present
	 * 
	 * @param arr
	 * @return Integer
	 */
	public static int getIndexOfKey(String[] arr) {
		int idx = -1;
		for (int j = 0; j < arr.length; j++) {
			for (int i = 0; i < KEY.length; i++) {
				if (arr[j].equals(KEY[i])) {
					idx = j;
				}
			}
		}
		return idx;
	}

	/**
	 * Method that return the index of "from" or "on" from the input String[] and return
	 * -1 if no starting index is present
	 * 
	 * @param arr
	 * @return Integer
	 */
	public static int getStartingIndex(String[] arr) {
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
	 * @return 
	 */
	public static boolean hasStartTime(String[] arr) {
		boolean containTime = true;
		int start = getStartingIndex(arr);
		// if date is the last argument => no time
		if (start + 2 >= arr.length) {
			containTime = false;
		} else {
			if (!checkDateObject.checkTimeformat(arr[start + 2])) {
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
	public static boolean hasEndTime(String[] arr) {
		boolean containTime = true;
		int end = getIndexOfKey(arr);
		// if date is the last argument => no time
		if (end + 2 >= arr.length) {
			containTime = false;

		} else {
			if (!checkDateObject.checkTimeformat(arr[end + 2])) {
				containTime = false;
			}
		}
		return containTime;
	}
}
