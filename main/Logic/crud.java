package Logic;

import Task.Task;
import Storage.localStorage;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
/*import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
 */public class crud {

	 private static ArrayList<Task> temp = new ArrayList<Task>();
	 private static Task temp1;
	 private static final String FLAG_UNCOMPLETED = "uncompleted";
	 private static final String FLAG_COMPLETED = "completed";
	 private static final String FLAG_FLOATING = "floating";
	 private static final String FLAG_RECURRING = "recurring";
	 private static boolean noDuplicate;

	 //@@author Kowshik
	 /**
	  * Function to add task without time into storage
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static boolean addTask(String line) throws IOException, ClassNotFoundException {
		 Task task = new Task(line);
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 boolean noDuplicate = true;
		 for(Task temp : tempTasks) {
			 if(temp.getTaskString().equals(task.getTaskString())) {
				 noDuplicate = false;
			 }
		 }
		 if(noDuplicate) {
			 Storage.localStorage.addToFloatingTasks(task);
			 return true;
		 }
		 else {
			 return false;
		 }
	 }

	 public static ArrayList<Task> getTemp(){
		 return temp;
	 }

	 /**
	  * Function to add task with only start date into storage
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static boolean addTaskWithStartDate(String line,String date, String msg) throws IOException, ClassNotFoundException {
		 Task task = new Task(line, date, msg, true);

		 boolean noDuplicate = true;
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 for(Task temp : tempTasks) {
			 if(temp.getTaskString().equals(task.getTaskString())) {
				 System.out.println(temp.getTaskString());
				 noDuplicate = false;
			 }
		 }
		 if(noDuplicate) {
			 Storage.localStorage.addToUncompletedTasks(task);
			 return true;
		 }
		 else {
			 return false;
		 }
	 }

	 /**
	  * Function to add task with only end date into storage
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static boolean addTaskWithEndDate(String line, String date, String msg) throws IOException, ClassNotFoundException {
		 Task task = new Task(line, date, msg, false);

		 boolean noDuplicate = true;
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 for(Task temp : tempTasks) {
			 if(temp.getTaskString().equals(task.getTaskString())) {
				 System.out.println(temp.getTaskString());
				 noDuplicate = false;
			 }
		 }
		 if(noDuplicate) {
			 Storage.localStorage.addToUncompletedTasks(task);
			 return true;
		 }
		 else {
			 return false;
		 }
	 }

	 /**
	  * Function to add task with both start and end date into storage
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static boolean addTaskWithBothDates(String line,String startDate, String endDate, String msg) throws IOException, ClassNotFoundException {
		 Task task = new Task(line, startDate, endDate, msg);

		 boolean noDuplicate = true;
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 for(Task temp : tempTasks) {
			 if(temp.getTaskString().equals(task.getTaskString())) {
				 System.out.println(temp.getTaskString());
				 noDuplicate = false;
			 }
		 }
		 if(noDuplicate) {
			 Storage.localStorage.addToUncompletedTasks(task);
			 return true;
		 }
		 else {
			 return false;
		 }
	 }

	 //@@author Jie Wei
	 /**
	  * Function to import the tasks from the storage file
	  * @param task the tasks to be added to the arraylist storage
	  * @throws IOException 
	  * @throws ClassNotFoundException 
	  */
	 public static void addTaskViaImport(Task task, String flag) throws IOException, ClassNotFoundException {
		 if (flag.equals(FLAG_UNCOMPLETED)) {
			 noDuplicate = checkForDuplicateTasks(task, Storage.localStorage.getUncompletedTasks());
			 if (noDuplicate) {
				 Storage.localStorage.addToUncompletedTasks(task);
			 }
		 } else if (flag.equals(FLAG_COMPLETED)) {
			 noDuplicate = checkForDuplicateTasks(task, Storage.localStorage.getCompletedTasks());
			 if (noDuplicate) {
				 Storage.localStorage.addToCompletedTasks(task);
			 }
		 } else if (flag.equals(FLAG_FLOATING)) {
			 noDuplicate = checkForDuplicateTasks(task, Storage.localStorage.getFloatingTasks());
			 if (noDuplicate) {
				 Storage.localStorage.addToFloatingTasks(task);
			 }
		 } else if (flag.equals(FLAG_RECURRING)) {
			 noDuplicate = checkForDuplicateTasks(task, Storage.localStorage.getRecurringTasks());
			 if (noDuplicate) {
				 Storage.localStorage.addToRecurringTasks(task);
			 }
		 }
	 }

	 private static boolean checkForDuplicateTasks(Task task, ArrayList<Task> destination) {
		 boolean noDuplicate = true;
		 for(Task temp : destination) {
			 if(temp.getTaskString().equals(task.getTaskString())) {
				 noDuplicate = false;
				 break;
			 }
		 }
		 return noDuplicate;
	 }

	 //@@author Cheng Gee
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

	 //@@author Kowshik
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
		 int uncompleteList = Storage.localStorage.getUncompletedTasks().size();

		 if(index < uncompleteList){
			 Task temp = Storage.localStorage.getUncompletedTask(index);
			 deleteTask(index,1);
			 temp.setStartDate(null);
			 temp.setEndDate(null);
			 temp.setIssue(line);
			 addTask(msg);
		 } else {
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
		 int uncompleteList = Storage.localStorage.getUncompletedTasks().size();

		 if(index < uncompleteList){
			 Task temp = Storage.localStorage.getUncompletedTask(index);
			 temp.setIssue(line);
			 temp.setEndDate(null);
			 temp.setStartDate(date);
			 Storage.localStorage.setUncompletedTask(index, temp);
		 } else {
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

		 if(index < uncompleteList){
			 Task temp = Storage.localStorage.getUncompletedTask(index);
			 temp.setIssue(line);
			 temp.setStartDate(null);
			 temp.setEndDate(date);
			 Storage.localStorage.setUncompletedTask(index, temp);
		 } else {
			 Task temp = Storage.localStorage.getFloatingTask(index - uncompleteList);
			 deleteTask(index, 1);
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

	 /**
	  * Function to delete task according to index in storage
	  * @throws IOException 
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static void deleteTask(int index, int listOfTasks) throws ClassNotFoundException, IOException{
		 if(listOfTasks == 1) { //delete from uncompleted tasks
			 ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();
			 if(index < getSize.size()) {
				 Storage.localStorage.delFromUncompletedTasks(index);
			 }
			 else {
				 Storage.localStorage.delFromFloatingTasks(index - getSize.size());
			 }
		 }
		 else if(listOfTasks == 2) { //delete from completed tasks
			 Storage.localStorage.delFromCompletedTasks(index);
		 }
		 else if(listOfTasks == 3) { //delete from search completed tasks view
			 ArrayList<Task> searchTemp = Search.getSearchedTasks();
			 Task taskToBeDeleted = searchTemp.get(index);
			 ArrayList<Task> uncompletedTemp = Storage.localStorage.getUncompletedTasks();
			 for(int i = 0; i<uncompletedTemp.size(); i++) {
				 if(uncompletedTemp.get(i).equals(taskToBeDeleted)) {
					 uncompletedTemp.remove(i);
					 break;
				 }
			 }
		 }
		 else if(listOfTasks == 4) { //delete from floating tasks view
			 Storage.localStorage.delFromFloatingTasks(index);
		 }
	 }

	 /**
	  * Function to display all the uncompleted tasks in the storage
	  * 
	  */
	 public static void displayUncompletedAndFloatingTasks() {
		 //System.out.print(ansi().eraseScreen().fgBright(YELLOW));
		 
		 UI.ui.print("UNCOMPLETED TASKS");
		 UI.ui.print("Index\tTask");
		 boolean isEmptyUn = false;
		 temp = Storage.localStorage.getUncompletedTasks();
		 String dt="";
		 for(int i=0; i<temp.size(); i++) {
			 UI.ui.print((i+1) + ".\t" + temp.get(i).getTaskString());
		 }
		 if (temp.isEmpty()) {
			 isEmptyUn = true;
		 }
		 UI.ui.print("________________________________________________________________");
		 UI.ui.print("FLOATING TASKS");
		 UI.ui.print("Index\tTask");
		 boolean isEmptyF = false;
		 temp = Storage.localStorage.getFloatingTasks();
		 ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();
		 for(int i=0; i<temp.size(); i++) {
			 UI.ui.print((getSize.size() + i+1) + ".\t" + temp.get(i).getIssue());
		 }
		 if (temp.isEmpty()) {
			 isEmptyF = true;
		 }

		 if(isEmptyUn && isEmptyF) {
			 UI.ui.print("There are no tasks to show.");
		 }
		 //System.out.print(ansi().reset());
	 }

	 /**
	  * Function to display the details of an individual task
	  * 
	  * @param index the index of the task to be displayed
	  */
	 public static void viewIndividualTask(int index) {
		 ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();
		 if(index < getSize.size()) {
			 temp1 = Storage.localStorage.getUncompletedTask(index);
		 }
		 else {
			 temp1 = Storage.localStorage.getFloatingTask(index - getSize.size());
		 }
		 boolean isCompleted = temp1.getCompletedStatus();
		 String completed = "Not completed";
		 if(isCompleted) {
			 completed = "Completed";
		 }

		 UI.ui.print(temp1.getTaskString());
		 UI.ui.print("Status: " + completed);
		 UI.ui.print("Priority: " + temp1.getPriority());
		 UI.ui.print("Labels:");
		 for(String label : temp1.getLabel()) {
			 UI.ui.print(label);
		 }
	 }

	 /**
	  * Function to display all the completed tasks in the storage
	  * 
	  */
	 public static void displayCompletedTasks() {
		 temp = Storage.localStorage.getCompletedTasks();
		 for(int i=0; i<temp.size(); i++) {
			 UI.ui.print((i+1) + ".\t" + temp.get(i).getStartDateString() + "\t" + temp.get(i).getEndDateString() + "\t" + temp.get(i).getIssue());
		 }
		 if (temp.isEmpty()) {
			 UI.ui.print("There is no stored task to display");
		 }
	 }

	 public static void displayScheduleForADay(String inputDate) {
		 /* String[] splitEndDate = inputDate.split("/");
		 Calendar date;
		 int year = Integer.parseInt(splitEndDate[2]);
		 int month = Integer.parseInt(splitEndDate[1]);
		 int day = Integer.parseInt(splitEndDate[0]);

		 if (splitEndDate.length > 3) { // given end date includes time
			 int hour = Integer.parseInt(splitEndDate[3]);
			 int minute = Integer.parseInt(splitEndDate[4]);
			 date = new GregorianCalendar(year, month, day, hour, minute);
		 } else { // given end date has not time
			 date = new GregorianCalendar(year, month, day);
		 }*/

		 //run through all the tasks and find which have same date
		 ArrayList<Task> tempTasks = new ArrayList<Task>();
		 ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();

		 for(Task temp : tempUncompletedTasks) {
			 if(temp.getEndDateString().contains(inputDate) || temp.getStartDateString().contains(inputDate)) {
				 tempTasks.add(temp);
			 }
		 }

		 if (tempTasks.isEmpty()) {
			 UI.ui.print("There is no stored task to display");
		 }
		 else {
			 UI.ui.print("Index\tTask");
			 for(int i = 0; i<tempTasks.size(); i++) {
				 UI.ui.print((i+1) + ".\t" + tempTasks.get(i).getTaskString());
			 }
		 }
	 }

	 /**
	  * Function to clear storage
	  * @throws IOException 
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static void clearTasks() throws ClassNotFoundException, IOException{
		 Storage.localStorage.clear();
	 }

	 /**
	  * Function to exit the application when user enters exit command
	  */
	 public static void exit(){
		 System.exit(0);
	 }
	 public static boolean addTaskToRecurring(String line, String date, String msg) {
			Task task = new Task(line, date, msg, false);

			System.out.println("end date of task : "+task.getEndDateString());

			Storage.localStorage.addToRecurringTasks(task);
			return true;
			}
			
		

		public static boolean addByTask(Task task) {
			localStorage.addToUncompletedTasks(task);
			return true;
		}

 }
