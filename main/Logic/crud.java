package Logic;

import Task.Task;
import unitTest.StorageTest;
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

	 private static ArrayList<Task> tempTasks = new ArrayList<Task>();
	 private static Task tempTask;
	 private static final String FLAG_UNCOMPLETED = "uncompleted";
	 private static final String FLAG_COMPLETED = "completed";
	 private static final String FLAG_FLOATING = "floating";
	 private static final String FLAG_RECURRING = "recurring";
	 private static boolean noDuplicate;

	 //@@author Kowshik
	 public static ArrayList<Task> getTemp(){
		 return tempTasks;
	 }
	 
	 public static Task getTempTask(int index) {
		 return tempTasks.get(index);
	 }
	 
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
				 UI.ui.printRed(temp.getTaskString());
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
				 UI.ui.printRed(temp.getTaskString());
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
				 UI.ui.printRed(temp.getTaskString());
				 noDuplicate = false;
			 }

			 if(temp.getStartDate() != null && temp.getEndDate() != null) {
				 if(temp.getStartDateString().equals(task.getStartDateString()) && temp.getEndDateString().equals(task.getEndDateString())) {
					 UI.ui.printRed("CLASH IN TIMING DETECTED WITH - ");
					 UI.ui.printRed(temp.getTaskString());
				 }
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
	  * Function to find the index of Task with Start Date
	  * 
	  * @param line the updated task description
	  * @param index the index of the task to be edited
	  * @throws IOException
	  */
	 public static int uncompletedTaskIndexWithStartDate(String line,String date, String msg){
		 Task task = new Task(line, date, msg, true);
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 for(int i=0;i<tempTasks.size();i++){
			 if(tempTasks.get(i).getTaskString().equals(task.getTaskString())) {
				 return i;
			 }
		 }return -1;
	 }
	 /**
	  * Function to find the index of Task with End Date
	  * @param line
	  * @param date
	  * @param msg
	  * @return
	  */
	 public static int uncompletedTaskIndexWithEndDate(String line,String date, String msg){
		 Task task = new Task(line, date, msg, false);
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 for(int i=0;i<tempTasks.size();i++){
			 if(tempTasks.get(i).getTaskString().equals(task.getTaskString())) {
				 return i;
			 }
		 }return -1;
	 }
	 /**
	  * Function to find the index of Task with Both Dates
	  * @param line
	  * @param startDate
	  * @param endDate
	  * @param msg
	  * @return
	  */

	 public static int uncompletedTaskIndexWithBothDates(String line,String startDate, String endDate, String msg){
		 Task task = new Task(line, startDate, endDate, msg);
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 for(int i=0;i<tempTasks.size();i++){
			 if(tempTasks.get(i).getTaskString().equals(task.getTaskString())) {
				 return i;
			 }
		 }return -1;
	 }
	 
	 /**
	  * Function to find the index of Task with No Date
	  * @param line
	  * @return
	  */
	 public static int uncompletedTaskIndexWithNoDate(String line){
		 Task task = new Task(line);
		 ArrayList<Task> tempTasks = Storage.localStorage.getFloatingTasks();
		 for(int i=0;i<tempTasks.size();i++){
			 if(tempTasks.get(i).getTaskString().equals(task.getTaskString())) {
				 return i;
			 }
		 }return -1;
	 }
	 /**
	  * Function to display the Nearest 5 Completed Task with the newly 
	  * mark Completed Task
	  * @param t
	  */
	 public static void displayNearestFiveCompletedTaskList(Task t){
		 int index=-1;
		 ArrayList<Task> tempTasks = Storage.localStorage.getCompletedTasks();
		 int size=tempTasks.size();
		 for(int i=0;i<size;i++){
			 Task temp=tempTasks.get(i);
			 if(t.getMsg().equals(temp.getMsg())){
				 index=i;
				 break;
			 }
		 }
		 int head=index-2;
		 int tail = index+3;
		 if(head<0){
			 head=0;
		 }
		 if(tail>=size){
			 tail=size;
		 }
		 for(int i=head;i<tail;i++){
			 Task temp=tempTasks.get(i);
			 if(index==i){
				 UI.ui.printTaskAdded1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());

			 }else{
				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());
			 }

		 }

	 }
	 /**
	  * Function to display the nearest 5 task from uncompleted task list 
	  * or floating task list including the unmark task
	  * @param t
	  */
	 public static void displayNearestFiveUnmarkCompleteTaskList(Task t){
		 ArrayList<Task> tempTasks;
		 if(t.getEndDate() != null || t.getStartDate() != null){

			 tempTasks = Storage.localStorage.getUncompletedTasks();
		 }else{
			 tempTasks = Storage.localStorage.getFloatingTasks();
		 }
		 int size = tempTasks.size();
		 int index=-1;
		 for(int i=0;i<size;i++){
			 Task temp=tempTasks.get(i);
			 if(t.getMsg().equals(temp.getMsg())){
				 index=i;
				 break;
			 }
		 }
		 int head=index-2;
		 int tail = index+3;
		 if(head<0){
			 head=0;
		 }
		 if(tail>=size){
			 tail=size;
		 }
		 for(int i=head;i<tail;i++){
			 Task temp=tempTasks.get(i);
			 if(index==i){
				 UI.ui.printTaskAdded1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());

			 }else{
				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());

			 }

		 }
	 }

	 /**
	  * Function to Display the surrounding uncomplete task list of 
	  * the deleted task
	  * @param index
	  */
	 public static void displayNearestFiveDeleteUncompleteTaskList(int index){
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 int size = tempTasks.size();
		 if(size==0){
			 UI.ui.printGreen("Uncompleted Task List is empty");
		 }else if(index<=tempTasks.size()){

			 int head = index-2;
			 int tail = index+3;
			 if(head<0){
				 head=0;
			 }
			 if(tail>=size){
				 tail=size;
			 }

			 for(int i=head;i<tail;i++){
				 Task temp=tempTasks.get(i);
				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());


			 }
		 }
	 }
	 /**
	  * Function to display the surrounding floating task list of
	  * delete task 
	  * @param index
	  */
	 public static void displayNearestFiveDeleteFloatingTask(int index){
		 ArrayList<Task> tempTasks = Storage.localStorage.getFloatingTasks();
		 int size = Storage.localStorage.getUncompletedTasks().size();
		 int size2=tempTasks.size();
		 index-=size;
		 if(size2==0){
			 UI.ui.printGreen("Floating Task List is Empty");
		 }else if(index<size2){
			 int head = index-2;
			 int tail = index+3;
			 if(head<0){
				 head=0;
			 }
			 if(tail>size2){
				 tail=size2;
			 }
			 for(int i=head;i<tail;i++){
				 Task temp=tempTasks.get(i);
				 UI.ui.printFloating(i, temp.getIssue());
				 

			 }
		 }
	 }
	 /**
	  * Function to display the nearest 5 uncompleted task including the new
	  * added task
	  * @param index
	  */
	 public static void displayNearestFiveUncompleted(int index){
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 int size = tempTasks.size();
		 int head = index-2;
		 int tail = index+3;
		 if(head<0){
			 head=0;
		 }
		 if(tail>=size){
			 tail=size;
		 }

		 for(int i=head;i<tail;i++){
			 Task temp=tempTasks.get(i);
			 if(i==index){
				 UI.ui.printTaskAdded1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());

			 }else{
				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());

			 }
		 }

	 }
	 /**
	  * Function to display the nearest 5 floating tasks including the 
	  * newly added floating task
	  * @param index
	  */
	 public static void displayNearestFiveFloating(int index){
		 ArrayList<Task> tempTasks = Storage.localStorage.getFloatingTasks();
		 int unSize = Storage.localStorage.getUncompletedTasks().size();
		 int size = tempTasks.size();
		 int head = index-2;
		 int tail = index+3;
		 if(head<0){
			 head=0;
		 }
		 if(tail>=size){
			 tail=size;
		 }

		 for(int i=head;i<tail;i++){
			 Task temp=tempTasks.get(i);
			 if(i==index){
				 UI.ui.printFloatingBackground(unSize+i,temp.getIssue());


			 }else{
				 UI.ui.printFloating(unSize+i,temp.getIssue());


			 }
		 }

	 }
	 /**
	  * Function to copy the Recurring Task Description
	  * @param t
	  */
	 public static void copyRecurringTask(Task t){
		 if(t!=null){
			 String copy = t.getDescription();
			 StringSelection selec = new StringSelection(copy);
			 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			 clipboard.setContents(selec, selec);
		 }
	 }
	 /**
	  * Function to copy the uncompleted Task Description
	  * @param index
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
	 /**
	  * Function to copy the editing Task Description
	  * @param index
	  */
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

			 addTask(msg);
		 } else {
			 Task temp = Storage.localStorage.getFloatingTask(index - uncompleteList);
			 temp.setStartDate(null);
			 temp.setEndDate(null);
			 temp.setDescription(msg);
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
			 if (!temp.getIssue().equals(line)) {
				 temp.resetID();
			 }
			 temp.setIssue(line);
			 temp.setDescription(msg);
			 temp.setEndDate(null);
			 temp.setStartDate(date);
			 Storage.localStorage.setUncompletedTask(index, temp);
		 } else {
			 Task temp = Storage.localStorage.getFloatingTask(index-uncompleteList);
			 temp.setIssue(line);
			 temp.setDescription(msg);
			 temp.setEndDate(null);
			 temp.setStartDate(date);
			 Storage.localStorage.delFromFloatingTasks(index-uncompleteList);
			 Storage.localStorage.addToUncompletedTasks(temp);
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
			 if (!temp.getIssue().equals(line)) {
				 temp.resetID();
			 }
			 temp.setIssue(line);
			 temp.setStartDate(null);
			 temp.setEndDate(date);
			 temp.setDescription(msg);
		//	 deleteTask(index,1);
		//	 addTaskWithEndDate(line, date, msg);
			 //Storage.localStorage.addToUncompletedTasks(temp);
			 Storage.localStorage.setUncompletedTask(index, temp);
		 } else {
			 Task temp = Storage.localStorage.getFloatingTask(index - uncompleteList);
			 deleteTask(index, 1);
			 temp.setDescription(msg);
			 temp.setIssue(line);
			 temp.setStartDate(null);
			 temp.setEndDate(date);
			 addTaskWithEndDate(line, date, msg);

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
		 int uncompleteList = Storage.localStorage.getUncompletedTasks().size();
		 if(index < uncompleteList){
			 Task temp = Storage.localStorage.getUncompletedTask(index);
			 if (!temp.getIssue().equals(line)) {
				 temp.resetID();
			 }
			 temp.setIssue(line);
			 temp.setDescription(msg);
			 temp.setStartDate(startDate);
			 temp.setEndDate(endDate);
			 Storage.localStorage.setUncompletedTask(index, temp);
		 }else{
			 Task temp = Storage.localStorage.getFloatingTask(index - uncompleteList);
			 deleteTask(index, 1);
			 temp.setDescription(msg);
			 temp.setIssue(line);
			 temp.setStartDate(startDate);
			 temp.setEndDate(endDate);
			 addTaskWithBothDates(line,startDate,endDate,msg);
		 }
	 }
	 /**
	  * Function to get a task from the list in Uncompleted Task List
	  * @param index
	  * @return
	  */
	 public static Task getUncompletedTask(int index){

		 int size1=Storage.localStorage.getUncompletedTasks().size();
		 if(index<size1){
		 return Storage.localStorage.getUncompletedTask(index);
		 }else{
		 
			 return Storage.localStorage.getFloatingTask(index-size1);
		 }
	 }
	 public static Task getCompletedTask(int index){
		 return Storage.localStorage.getCompletedTask(index);
	 }

	 /**
	  * Function to delete task according to index in storage
	  * @throws IOException 
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static void deleteTask(int index, int listOfTasks) throws ClassNotFoundException, IOException{
		 if(listOfTasks == 1) { //delete from "display all" view
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
		 else if(listOfTasks == 5) { //delete from "display" view
			 Task temp = tempTasks.get(index);
			 ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();
			 for(int i = 0; i<tempUncompletedTasks.size(); i++) {
				 if(tempUncompletedTasks.get(i).getTaskString().equals(temp.getTaskString())) {
					 tempUncompletedTasks.remove(i);
					 break;
				 }
			 }
			 Storage.localStorage.setUncompletedTasks(tempUncompletedTasks);
		 }
	 }

	 /**
	  * Function to display all the uncompleted tasks in the storage
	  * 
	  */
	 public static void displayUncompletedAndFloatingTasks() {

		 UI.ui.eraseScreen();
		 boolean isEmptyUn = false;
		 tempTasks = Storage.localStorage.getUncompletedTasks();
		 String dt="";

		 if (tempTasks.isEmpty()) {
			 isEmptyUn = true;
		 } else {
			 printUncompletedTask(tempTasks);
		 }


		 boolean isEmptyF = false;
		 tempTasks = Storage.localStorage.getFloatingTasks();
		 if (tempTasks.isEmpty()) {
			 isEmptyF = true;
		 }
		 else {
			 UI.ui.printGreen("FLOATING TASKS");
			 UI.ui.printGreen("Index\tTask");
			 ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();
			 for(int i=0; i<tempTasks.size(); i++) {
				 Task temp=tempTasks.get(i);
				 UI.ui.printYellow((getSize.size() + i+1) + ".\t" +temp.getShortPriority()+ temp.getIssue());
			 }
		 }
		 if(isEmptyUn && isEmptyF) {
			 UI.ui.printGreen("There are no tasks to show.");
		 }
	 }
	 public static void printUncompletedTask(ArrayList<Task> tempTask){
		 UI.ui.printGreen("UNCOMPLETED TASKS");
		 UI.ui.printGreen("Index\tStart Date\tEnd Date\tTask");

		 for(int i=0; i<tempTask.size(); i++) {
			 Task temp = tempTask.get(i);

			 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getShortPriority()+temp.getIssue(),temp.getRecurFrequency());
		 }
		 UI.ui.print("________________________________________________________________");
		 
	 }
	 public static void printCompletedTask(ArrayList<Task> tempTask){
		 UI.ui.eraseScreen();
		 UI.ui.printGreen("COMPLETED TASKS");
		 UI.ui.printGreen("Index\tStart Date\tEnd Date\tTask");
		 for(int i=0; i<tempTasks.size(); i++) {
			 Task temp=tempTasks.get(i);
			 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());
		 }
	 }
	 /**
	  * function to display all floating task in storage
	  * 
	  */
	 public static void displayFloatingTasks() {
		 UI.ui.eraseScreen();
		 UI.ui.printGreen("FLOATING TASKS");
		 UI.ui.printGreen("Index\tTask");
		 boolean isEmptyF = false;
		 tempTasks = Storage.localStorage.getFloatingTasks();
		 ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();
		 for(int i=0; i<tempTasks.size(); i++) {
			 Task temp=tempTasks.get(i);
			 UI.ui.printYellow((getSize.size() + i+1) + ".\t" +temp.getShortPriority()+ temp.getIssue());
		 }
		 if (tempTasks.isEmpty()) {
			 isEmptyF = true;
		 }  if(isEmptyF) {
			 UI.ui.printGreen("There are no floating tasks to show.");
		 }
	 }
	 /**
	  * Function to display the details of an individual task
	  * 
	  * @param index the index of the task to be displayed
	  */
	 public static void viewIndividualTask(int index) {
		 UI.ui.eraseScreen();
		 ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();
		 if(index < getSize.size()) {
			 tempTask = Storage.localStorage.getUncompletedTask(index);
		 }
		 else {
			 tempTask = Storage.localStorage.getFloatingTask(index - getSize.size());
		 }
		 boolean isCompleted = tempTask.getCompletedStatus();
		 String completed = "Not completed";
		 if(isCompleted) {
			 completed = "Completed";
		 }

		 UI.ui.printYellow(tempTask.getTaskString());
		 UI.ui.print("Status: " + completed);
		 UI.ui.print("Priority: " + tempTask.getPriority());
		 UI.ui.print("Labels:");
		 for(String label : tempTask.getLabel()) {
			 UI.ui.print(label);
		 }
	 }

	 /**
	  * Function to display all the completed tasks in the storage
	  * 
	  */
	 public static void displayCompletedTasks() {
		 tempTasks = Storage.localStorage.getCompletedTasks();
		 printCompletedTask(tempTasks);
		 if (tempTasks.isEmpty()) {
			 UI.ui.printGreen("There is no stored task to display");
		 }
	 }

	 public static void displayScheduleForADay(String inputDate) {
		 inputDate = inputDate.replace("/0", "/");
		 if(inputDate.startsWith("0")) {
			 inputDate = inputDate.replaceFirst("0", "");
		 }
		 System.out.println(inputDate);
		 String[] splitDate = inputDate.split("/");
		 //run through all the tasks and find which have same date
		 tempTasks = new ArrayList<Task>();
		 ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();

		 for(Task temp : tempUncompletedTasks) {
			 if(temp.getStartDate() != null) {
				 String startDay = "" + temp.getStartDate().get(Calendar.DAY_OF_MONTH);
				 String startMonth = "" + (temp.getStartDate().get(Calendar.MONTH) + 1);
				 String startYear = "" + temp.getStartDate().get(Calendar.YEAR);
				 if(checkIfDateIsContained(splitDate, startDay, startMonth, startYear)) {
					 tempTasks.add(temp);
					 continue;
				 }
			 } else {
				 String endDay = "" + temp.getEndDate().get(Calendar.DAY_OF_MONTH);
				 String endMonth = "" + (temp.getEndDate().get(Calendar.MONTH) + 1);
				 String endYear = "" + temp.getEndDate().get(Calendar.YEAR);
				 if(checkIfDateIsContained(splitDate, endDay, endMonth, endYear)) {
					 tempTasks.add(temp);
				 }

			 }
		 }

		 if (tempTasks.isEmpty()) {
			 UI.ui.printGreen("There is no stored task to display");
		 }
		 else {
			 UI.ui.eraseScreen();
			 UI.ui.print("Index\tTask");
			 for(int i = 0; i<tempTasks.size(); i++) {
				 Task temp=tempTasks.get(i);
				 UI.ui.printTask(i,temp.getStartDateString(),temp.getEndDateString(),temp.getIssue());
			 }
		 }
	 }

	 public static boolean checkIfDateIsContained(String[] splitDate, String day, String month, String year) {
		 if(day.contains(splitDate[0]) && month.contains(splitDate[1]) && year.contains(splitDate[2])) {
			 return true;
		 }
		 return false;
	 }

	 public static void displayByLabel(String s) {
		 UI.ui.eraseScreen();
		 tempTasks = new ArrayList<Task>();
		 ArrayList<Task> displayResults = Storage.localStorage.getUncompletedTasks();

		 for(Task temp : displayResults) {
			 if(temp.getLabel().contains(s)) {
				 tempTasks.add(temp);
			 }
		 }

		 if(tempTasks.size() > 0) {
			 printUncompletedTask(tempTasks);
		 }

		 tempTasks = new ArrayList<Task>();
		 displayResults = Storage.localStorage.getFloatingTasks();
		 for(Task temp : displayResults) {
			 if(temp.getLabel().contains(s)) {
				 tempTasks.add(temp);
			 }
		 }

		 if(tempTasks.size() > 0) {
			 UI.ui.printGreen("FLOATING TASKS");
			 UI.ui.printGreen("Index \t Task");
			 ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();
			 for(int i = 0; i<tempTasks.size(); i++) {
				 Task temp=tempTasks.get(i);
				 UI.ui.printYellow((getSize.size() + i+1) + ".\t" +temp.getShortPriority() +temp.getIssue());
			 }
			 UI.ui.print("________________________________");
		 }

		 tempTasks = new ArrayList<Task>();
		 displayResults = Storage.localStorage.getCompletedTasks();
		 for(Task temp : displayResults) {
			 if(temp.getLabel().contains(s)) {
				 tempTasks.add(temp);
			 }
		 }
		 if(tempTasks.size() > 0) {
			 printCompletedTask(tempTasks);
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



	 public static void addLabelToTask(int index, String label) {
		 int sizeOfUncompletedTasks = Storage.localStorage.getUncompletedTasks().size();
		 if(index < sizeOfUncompletedTasks) {
			 Task temp = Storage.localStorage.getUncompletedTask(index);
			 temp.setLabel(label);
			 Storage.localStorage.setUncompletedTask(index, temp);
		 } else {
			 Task temp = Storage.localStorage.getFloatingTask(index);
			 temp.setLabel(label);
			 Storage.localStorage.setFloatingTask(index, temp);
		 }
	 }

	 public static void displayTasksForThisWeek() {
		 UI.ui.eraseScreen();
		 UI.ui.printGreen("Upcoming tasks this week - ");
		 ArrayList<Task> tasksToBeDisplayed = new ArrayList<Task>();
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();

		 //getting today and seven days later
		 /*Calendar d1 = Calendar.getInstance();
		 int todayDay = d1.get(Calendar.DAY_OF_MONTH);
		 int todayMonth = d1.get(Calendar.MONTH);
		 int todayYear = d1.get(Calendar.YEAR);
		 Date today = new Date(todayYear, todayMonth, todayDay);

		 Calendar d2 = Calendar.getInstance();
		 d2.add(Calendar.DAY_OF_MONTH, 7);
		 int futureDay = d2.get(Calendar.DAY_OF_MONTH);
		 int futureMonth = d2.get(Calendar.MONTH);
		 int futureYear = d2.get(Calendar.YEAR);
		 Date future = new Date(futureYear, futureMonth, futureDay);*/

		 Calendar date = Calendar.getInstance();
		 int thisWeek = date.get(Calendar.WEEK_OF_YEAR);

		 //finding tasks which has dates in current week
		 for(Task temp : tempTasks) {
			 if(temp.getEndDate() != null) {
				 if(temp.getEndDate().get(Calendar.WEEK_OF_YEAR) == thisWeek) {
					 tasksToBeDisplayed.add(temp);
					 continue;
				 }
			 }
			 if(temp.getStartDate() != null) {
				 if(temp.getStartDate().get(Calendar.WEEK_OF_YEAR) == thisWeek) {
					 tasksToBeDisplayed.add(temp);
				 }
			 }
		 }

		 if(tasksToBeDisplayed.size() > 0) {
			 printUncompletedTask(tasksToBeDisplayed);
			 
		 }
		 else {
			 UI.ui.printRed("No tasks this week");
		 }
	 }

	 public static void displayTasksForNextWeek() {
		 UI.ui.eraseScreen();
		 UI.ui.printGreen("Upcoming tasks next week - ");
		 ArrayList<Task> tasksToBeDisplayed = new ArrayList<Task>();
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();

		 Calendar date = Calendar.getInstance();
		 date.add(Calendar.WEEK_OF_YEAR, 1);
		 int nextWeek = date.get(Calendar.WEEK_OF_YEAR);

		 //finding tasks which has dates in the next week
		 for(Task temp : tempTasks) {
			 if(temp.getEndDate() != null) {
				 if(temp.getEndDate().get(Calendar.WEEK_OF_YEAR) == nextWeek) {
					 tasksToBeDisplayed.add(temp);
					 continue;
				 }
			 }
			 if(temp.getStartDate() != null) {
				 if(temp.getStartDate().get(Calendar.WEEK_OF_YEAR) == nextWeek) {
					 tasksToBeDisplayed.add(temp);
				 }
			 }
		 }

		 if(tasksToBeDisplayed.size() > 0) {
			 printUncompletedTask(tasksToBeDisplayed);
			 
		 }
		 else {
			 UI.ui.printRed("No tasks next week");
		 }

	 }

	 public static void displayTaksForTwoWeeksLater() {
		 UI.ui.eraseScreen();
		 UI.ui.printGreen("Upcoming tasks for two weeks later - ");
		 ArrayList<Task> tasksToBeDisplayed = new ArrayList<Task>();
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();

		 Calendar date = Calendar.getInstance();
		 date.add(Calendar.WEEK_OF_YEAR, 2);
		 int twoWeeksLater = date.get(Calendar.WEEK_OF_YEAR);

		 //finding tasks which has dates in two weeks time
		 for(Task temp : tempTasks) {
			 if(temp.getEndDate() != null) {
				 if(temp.getEndDate().get(Calendar.WEEK_OF_YEAR) == twoWeeksLater) {
					 tasksToBeDisplayed.add(temp);
					 continue;
				 }
			 }
			 if(temp.getStartDate() != null) {
				 if(temp.getStartDate().get(Calendar.WEEK_OF_YEAR) == twoWeeksLater) {
					 tasksToBeDisplayed.add(temp);
				 }
			 }
		 }

		 if(tasksToBeDisplayed.size() > 0) {
			 printUncompletedTask(tasksToBeDisplayed);
			 
		 }
		 else {
			 UI.ui.printRed("No tasks for two weeks later");
		 }

	 }

	 public static void displayTasksForLastWeek() {
		 UI.ui.eraseScreen();
		 UI.ui.printGreen("Tasks uncompleted from last week - ");
		 ArrayList<Task> tasksToBeDisplayed = new ArrayList<Task>();
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();

		 Calendar date = Calendar.getInstance();
		 date.add(Calendar.WEEK_OF_YEAR, -1);
		 int lastWeek = date.get(Calendar.WEEK_OF_YEAR);

		 //finding tasks which has dates in two weeks time
		 for(Task temp : tempTasks) {
			 if(temp.getEndDate() != null) {
				 if(temp.getEndDate().get(Calendar.WEEK_OF_YEAR) == lastWeek) {
					 tasksToBeDisplayed.add(temp);
					 continue;
				 }
			 }
			 if(temp.getStartDate() != null) {
				 if(temp.getStartDate().get(Calendar.WEEK_OF_YEAR) == lastWeek) {
					 tasksToBeDisplayed.add(temp);
				 }
			 }
		 }

		 if(tasksToBeDisplayed.size() > 0) {
			 printUncompletedTask(tasksToBeDisplayed);
			 
		 }
		 else {
			 UI.ui.printRed("No tasks left from last week");
		 }

	 }


	 public static void displayUpcomingTasks() {
		 UI.ui.eraseScreen();
		 ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();
		 //today
		 Calendar d1 = Calendar.getInstance();
		 d1.add(Calendar.DAY_OF_MONTH, -1);

		 //7 days in advance
		 Calendar d2 = Calendar.getInstance();
		 d2.add(Calendar.DAY_OF_MONTH, 7);

		 tempTasks = new ArrayList<Task>();

		 for(Task temp : tempUncompletedTasks) {
			 if(temp.getEndDate() != null) {
				 if(temp.getEndDate().compareTo(d1) >= 0 && temp.getEndDate().compareTo(d2) <=0) {
					 tempTasks.add(temp);
					 continue;
				 }
			 }
			 else if(temp.getStartDate() != null) {
				 if((temp.getStartDate().compareTo(d1) >= 0) && (temp.getStartDate().compareTo(d2) <= 0)) {
					 tempTasks.add(temp);
				 }
			 }
		 }
		 
		 if(tempTasks.size() > 0) {
			 printUncompletedTask(tempTasks);
			 
			 }
		 }
	 }
 

