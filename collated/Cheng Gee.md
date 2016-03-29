# Cheng Gee
###### main\Logic\checkDate.java
``` java
package Logic;
public class checkDate {

	private static int[] leapYearDate = new int[]{31,29,31,30,31,30,31,31,30,31,30,31};
	private static int[] commonYearDate= new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	
	/**
	 * Function to check the format of the string if it follows the date convention
	 * 
	 */
	public static boolean checkDateformat(String msg){
		String[] msgArray=msg.split("/");
		if(msgArray.length != 3 && !msg.matches("^\\d{2}/\\d{2}/\\d{4}")) {
			return false;
		} else {
			int date=Integer.parseInt(msgArray[0]);
			int month=Integer.parseInt(msgArray[1]);
			int year=Integer.parseInt(msgArray[2]);
			if(month>=1 && month<=12){
				if((year % 4) == 0 ) {
					if(date <= leapYearDate[month-1]) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				if(date <= commonYearDate[month-1]) {
					return true;
				}
				else {
					return false;
				}
			}
			}else{
				return false;
			}
		}
	}
	  public static boolean checkTimeformat(String msg){
		    String[] msgArray=msg.split(":");
		    if(msgArray.length != 2 && !msg.matches("^\\d{2}:\\d{2}")) {
		     return false;
		    } else if(msg.endsWith("am")||msg.endsWith("pm")){
		     msg=msg.substring(0, msg.length()-2);
		     int hour=Integer.parseInt(msgArray[0]);
		      int minute=Integer.parseInt(msgArray[1]);
		      if(minute>=0&&minute<60&&hour>=0&&hour<=23){
		        return true;
		     }return false;
		    }else {
		     int hour=Integer.parseInt(msgArray[0]);
		     int minute=Integer.parseInt(msgArray[1]);
		     if(minute>=0&&minute<60&&hour>=0&&hour<=23){
		       return true;
		    }return false;
		   }
		   }
}
```
###### main\Logic\crud.java
``` java
	 /**
	  * Function to edit task without editing date according to index in storage
	  * 
	  * @param line the updated task description
	  * @param index the index of the task to be edited
	  * @throws IOException
	  */
	 public static void copyTask(int index){
		 Task edit = Storage.localStorage.getUncompletedTask(index-1);
		 if(edit != null){
			 String copy = edit.getDescription();
			 StringSelection selec = new StringSelection(copy);
			 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			 clipboard.setContents(selec, selec);
		 }
	 }
	 public static void copyEditingTask(int index){
		 ArrayList<Task> task1=Storage.localStorage.getUncompletedTasks();
		 
		 int size=task1.size();
		 if(index<=size){
		 Task edit = Storage.localStorage.getUncompletedTask(index-1);
		 if(edit != null){
			 String copy = edit.getDescription();
			 StringSelection selec = new StringSelection(copy);
			 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			 clipboard.setContents(selec, selec);
		 }
		 }else{
			 Task edit = Storage.localStorage.getFloatingTask(index-size-1);
			 if(edit != null){
				 String copy = edit.getIssue();
				 StringSelection selec = new StringSelection(copy);
				 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				 clipboard.setContents(selec, selec);
			 }
		 }
	 }
	 
	 
	 /**
	  * Function to edit task (edited task has no date)
	  * @param line
	  * @param date
	  * @param msg
	  * @param index
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 public static void editTaskWithNoDate(String line, String msg, int index) throws IOException, ClassNotFoundException {
		 int uncompleteList=Storage.localStorage.getUncompletedTasks().size();
		 if(index<uncompleteList){
			 Task temp = Storage.localStorage.getUncompletedTask(index);
			 deleteTask(index,1);
			 temp.setStartDate(null);
			 temp.setEndDate(null);
			 temp.setIssue(line);
			 addTask(msg);
		 }else{
			 Task temp = Storage.localStorage.getFloatingTask(index - uncompleteList);
			 temp.setStartDate(null);
			 temp.setEndDate(null);
			 temp.setIssue(line);
			 Storage.localStorage.setFloatingTask(index - uncompleteList, temp);
		 }
		 
	 }
	 /**
	  * Function to edit task (edited task has only start date)
	  * @param line
	  * @param date
	  * @param msg
	  * @param index
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 public static void editTaskWithStartDate(String line, String date, String msg, int index) throws IOException, ClassNotFoundException {		 
		 int uncompleteList=Storage.localStorage.getUncompletedTasks().size();
		 if(index<uncompleteList){
			 Task temp = Storage.localStorage.getUncompletedTask(index);
			 temp.setIssue(line);
			 temp.setEndDate(null);
			 temp.setStartDate(date);
			 Storage.localStorage.setUncompletedTask(index, temp);
		 }else{
			 Task temp = Storage.localStorage.getFloatingTask(index-uncompleteList);
			 temp.setIssue(line);
			 temp.setEndDate(null);
			 temp.setStartDate(date);
			 Storage.localStorage.setUncompletedTask(index, temp);
		 }
	 }
	 
	 /**
	  * Function to edit task (edited task has only end date)
	  * @param index
	  * @param line
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 public static void editTaskWithEndDate(String line, String date, String msg, int index) throws IOException, ClassNotFoundException {
		 int uncompleteList=Storage.localStorage.getUncompletedTasks().size();
		 if(index<uncompleteList){
		 
		 Task temp = Storage.localStorage.getUncompletedTask(index);
		 temp.setIssue(line);
		 temp.setStartDate(null);
		 temp.setEndDate(date);
		 Storage.localStorage.setUncompletedTask(index, temp);
		 }else{
			 Task temp = Storage.localStorage.getFloatingTask(index-uncompleteList);
			 deleteTask(index,1);
			 temp.setIssue(line);
			 temp.setStartDate(null);
			 temp.setEndDate(date);
			 addTaskWithStartDate(line, date, msg);
			 
		 }
	 }
	 
	 /**
	  * Function to edit task (edited task has both start and end dates)
	  * @param index
	  * @param line
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 public static void editTaskWithBothDates(String line, String startDate, String endDate, String msg, int index) throws IOException, ClassNotFoundException {
		 Task temp = Storage.localStorage.getUncompletedTask(index);
		 temp.setIssue(line);
		 temp.setStartDate(startDate);
		 temp.setEndDate(endDate);
		 Storage.localStorage.setUncompletedTask(index, temp);
	 }

```
