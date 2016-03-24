package Logic;

import Task.Task;
import Storage.localStorage;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;
/*import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
 */public class crud {

	 private static ArrayList<Task> temp = new ArrayList<Task>();
	 private static Task temp1;
	 private static final String FLAG_UNCOMPLETED = "uncompleted";
	 private static final String FLAG_COMPLETED = "completed";
	 private static final String FLAG_FLOATING = "floating";
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
	  * Function to add task with time into storage
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static boolean addTask(String line,String date, String msg) throws IOException, ClassNotFoundException {
		 Task task = new Task(line,date,msg);

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
	  * Function to import the tasks from the storage file
	  * @param task the tasks to be added to the arraylist storage
	  * @throws IOException 
	  * @throws ClassNotFoundException 
	  */
	 public static void addTaskViaImport(Task task, String flag) throws IOException, ClassNotFoundException {
		 if (flag.equals(FLAG_UNCOMPLETED)) {
			 Storage.localStorage.addToUncompletedTasks(task);
		 } else if (flag.equals(FLAG_COMPLETED)) {
			 Storage.localStorage.addToCompletedTasks(task);
		 } else if (flag.equals(FLAG_FLOATING)) {
			 Storage.localStorage.addToFloatingTasks(task);
		 }
	 }

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
			 String copy = edit.getIssue();
			 StringSelection selec = new StringSelection(copy);
			 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			 clipboard.setContents(selec, selec);
		 }
	 }
	 public static void copyDescription(int index){
		 Task edit = Storage.localStorage.getUncompletedTask(index-1);
		 if(edit != null){
			 String copy = edit.getDescription();
			 StringSelection selec = new StringSelection(copy);
			 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			 clipboard.setContents(selec, selec);
		 }
	 }

	 public static void copyTaskDate(int index){
		 Task edit = Storage.localStorage.getUncompletedTask(index-1);
		 if(edit != null){
			 String copy = edit.getDateString();
			 StringSelection selec = new StringSelection(copy);
			 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			 clipboard.setContents(selec, selec);
		 }
	 }
	 public static void editTask(int index, String line) throws IOException, ClassNotFoundException {
		 Task editedTask = new Task(line);
		 ArrayList<Task> getSize = Storage.localStorage.getFloatingTasks();
		 if(index < getSize.size()) {
			 Storage.localStorage.setUncompletedTask(index, editedTask);
		 }
		 else {
			 Storage.localStorage.setFloatingTask(index - getSize.size(), editedTask);
		 }
	 }

	 /**
	  * Function to edit task along with date according to index in storage
	  * 
	  * @param index the index of the task to be edited
	  * @param line the updated task description
	  * @param date the updated task date
	  * @throws IOException 
	  * @throws ClassNotFoundException 
	  */
	 public static void editTask(int index, String line, String date,String msg) throws ClassNotFoundException, IOException {
		 Task editedTask = new Task(line, date, msg);
		 Storage.localStorage.setUncompletedTask(index, editedTask);
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
			 ArrayList<Task> searchTemp = search.getSearchedTasks();
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
		 boolean isEmptyUn = false;
		 temp = Storage.localStorage.getUncompletedTasks();
		 for(int i=0; i<temp.size(); i++) {
			 UI.ui.print((i+1) + ". " + temp.get(i).getDateString() + "\t" + temp.get(i).getIssue());
			 UI.ui.print("________________________________________________________________");
			 UI.ui.print("\n");

		 }
		 if (temp.isEmpty()) {
			 isEmptyUn = true;
		 }

		 UI.ui.print("FLOATING TASKS");
		 boolean isEmptyF = false;
		 temp = Storage.localStorage.getFloatingTasks();
		 ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();
		 for(int i=0; i<temp.size(); i++) {
			 UI.ui.print((getSize.size() + i+1) + ". " + temp.get(i).getIssue());
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
			 UI.ui.print((i+1) + ". " + temp.get(i).getDateString() + "\t" + temp.get(i).getIssue());
		 }
		 if (temp.isEmpty()) {
			 UI.ui.print("There is no stored task to display");
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
 }
