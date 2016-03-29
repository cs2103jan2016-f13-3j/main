# Kowshik
###### main\Logic\crud.java
``` java
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

```
###### main\Logic\crud.java
``` java
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
		 boolean isEmptyUn = false;
		 temp = Storage.localStorage.getUncompletedTasks();
		 String dt="";
		 for(int i=0; i<temp.size(); i++) {
			 UI.ui.print((i+1) + ".\t" + temp.get(i).getStartDateString() + "\t" + temp.get(i).getEndDateString() + "\t" + temp.get(i).getIssue());
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
```
###### main\Logic\Help.java
``` java
package Logic;
public class Help {

	public static void printHelpMenu() {
		String addCommand = "Type \"add/+/a\" followed by task description. Press Enter. Now enter the date or -.";
		String editCommand = "Type \"edit/e\" followed by edited task description and edited date.";
		String deleteCommand = "Enter \"delete/-\" followed by the task number that you want to delete.";
		String markCommand = "Enter \"mark/m\" to mark a task as completed or uncompleted";
		String exitCommand = "Enter \"exit\" to quit Agendah";
		String clearCommand = "Enter \"clear/c\" to delete all the tasks.";
		String sortCommand = "Enter \"sort\" to sort the tasks alphabetically";
		String searchCommand = "Enter \"search/s\" followed by the word you want to search for to display all tasks containing that word";
		String displayUncompletedCommand = "Enter \"display/d\" to display the uncompleted tasks";
		String displayCompletedCommand = "Enter \"displaycompleted/dc\" to display the completed tasks";
		
		UI.ui.print("1. " + addCommand);
		UI.ui.print("2. " + editCommand);
		UI.ui.print("3. " + deleteCommand);
		UI.ui.print("4. " + displayUncompletedCommand);
		UI.ui.print("5. " + displayCompletedCommand);
		UI.ui.print("6. " + sortCommand);
		UI.ui.print("7. " + searchCommand);
		UI.ui.print("8. " + clearCommand);
		UI.ui.print("9. " + markCommand);
		UI.ui.print("10. " + exitCommand);
	}
}
```
###### main\Logic\mark.java
``` java
package Logic;
import java.io.IOException;
import java.util.ArrayList;

import Storage.localStorage;
import Task.Task;

public class Mark {

	/**
	 * Function to mark tasks as completed
	 * 
	 * @param index the index of the task to be marked as completed
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void markTaskAsCompleted(int index) throws IOException, ClassNotFoundException {
		ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();

		if(index < getSize.size()) {
			Task temp = Storage.localStorage.getUncompletedTask(index);
			temp.setComplete();
			Storage.localStorage.delFromUncompletedTasks(index);
			Storage.localStorage.addToCompletedTasks(temp);
		}
		else {
			Task temp = Storage.localStorage.getFloatingTask(index - getSize.size());
			temp.setComplete();
			Storage.localStorage.delFromFloatingTasks(index - getSize.size());
			Storage.localStorage.addToCompletedTasks(temp);
		}
	}

	/**
	 * Function to mark a task as uncompleted
	 * 
	 * @param index the index of the task to be marked as uncompleted
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void markTaskAsUncompleted(int index) throws IOException, ClassNotFoundException {
		ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();

		if(index < getSize.size()) {
			Task temp = Storage.localStorage.getCompletedTask(index);
			temp.setUncomplete();
			Storage.localStorage.delFromCompletedTasks(index);
			Storage.localStorage.addToUncompletedTasks(temp);
		}
		else {
			Task temp = Storage.localStorage.getCompletedTask(index);
			temp.setUncomplete();
			Storage.localStorage.delFromCompletedTasks(index);
			Storage.localStorage.addToFloatingTasks(temp);
		}
	}

	/**
	 * Function to set the priority for a task
	 * 
	 * @param index the index of the task to be updated
	 * @param priority the priority to be set for the task
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void setPriority(int index, String priority) throws ClassNotFoundException, IOException {
		//		localStorage.copyCurrentState();
		ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();

		if(index < getSize.size()) {
			Task temp = Storage.localStorage.getUncompletedTask(index);
			temp.setPriority(priority);
			Storage.localStorage.setUncompletedTask(index, temp);
		}
		else {
			Task temp = Storage.localStorage.getFloatingTask(index - getSize.size());
			temp.setPriority(priority);
			Storage.localStorage.setFloatingTask(index - getSize.size(), temp);
		}
	}
}
```
###### main\Logic\search.java
``` java
package Logic;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Task.Task;

public class Search {
	
	private static ArrayList<Task> searchedTasks;
	
	public static ArrayList<Task> getSearchedTasks() {
		return searchedTasks;
		
	}

	/**
	 * Function to search task according to keyword in list of uncompleted tasks
	 * 
	 * @param keyword the string to be searched for in the list of tasks
	 */
	public static void searchTasksByKeyword(String keyword){
		searchedTasks = new ArrayList<Task>();
		int counter = 0;
		ArrayList<Task> temp = Storage.localStorage.getUncompletedTasks();
		for(int i=0; i<temp.size(); i++) {
			if(temp.get(i).getIssue().contains(keyword) || temp.get(i).getTaskString().contains(keyword)) {
				searchedTasks.add(temp.get(i));
			}
		}
		
		UI.ui.print("UNCOMPLETED TASKS");
		for(int i=0; i<searchedTasks.size(); i++) {
			UI.ui.print((i+1) + ". " + searchedTasks.get(i).getTaskString());
			counter++;
		}
		temp = Storage.localStorage.getFloatingTasks();
		UI.ui.print("________________________________________");
		UI.ui.print("\n");
		UI.ui.print("FLOATING TASKS");
		for(int i=0; i<temp.size(); i++) {
			if(temp.get(i).getIssue().contains(keyword) || temp.get(i).getTaskString().contains(keyword)) {
				searchedTasks.add(temp.get(i));
			}
		}

		for(int i = counter; i<searchedTasks.size(); i++) {
			UI.ui.print((i+1) + ". " + searchedTasks.get(i).getTaskString());
			counter++;
		}
		
		temp = Storage.localStorage.getCompletedTasks();
		UI.ui.print("________________________________________");
		UI.ui.print("\n");
		UI.ui.print("COMPLETED TASKS");
		for(int i=0; i<temp.size(); i++) {
			if(temp.get(i).getIssue().contains(keyword) || temp.get(i).getTaskString().contains(keyword)) {
				searchedTasks.add(temp.get(i));
			}
		}

		for(int i = counter; i<searchedTasks.size(); i++) {
			UI.ui.print((i+1) + ". " + searchedTasks.get(i).getTaskString());
			
		}
	}
}
```
###### main\Logic\sort.java
``` java
package Logic;
import java.io.IOException;
import java.util.ArrayList;
import Task.Task;

public class Sort {

	/**
	 * Function to sorts tasks in storage alphabetically
	 * 
	 */
	public static void sortTasksAlphabetically(){
		ArrayList<Task> temp = Storage.localStorage.getUncompletedTasks();

		for(int i = 0; i<temp.size()-1; i++) {
			for(int j = i+1; j<temp.size(); j++) {
				int result = temp.get(i).getIssue().compareTo(temp.get(j).getIssue());
				if(result > 0) {
					Task setTask = temp.get(i);
					temp.set(i, temp.get(j));
					temp.set(j, setTask);
				}
			}
		}
		try {
			Storage.localStorage.setArrayList(temp);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Function to sort tasks in chronological order
	 */
	public static void sortTasksChronologically() {
		ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();	
		for(int i = 0; i<tempTasks.size(); i++) {
			for(int j = i+1; j<tempTasks.size(); j++) {
				if(tempTasks.get(i).getDate().compareTo(tempTasks.get(j).getDate()) > 0) {
					Task temp = tempTasks.get(i);
					tempTasks.set(i, tempTasks.get(j));
					tempTasks.set(j, temp);
				}
			}
		}
		try {
			Storage.localStorage.setArrayList(tempTasks);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Function to sort tasks according to priority
	 */
	public static void sortTasksPriority() {
		ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		
		for(int i = 0; i<tempTasks.size(); i++) {
			for(int j = i+1; j<tempTasks.size(); j++) {
				if(tempTasks.get(i).getPriority().equals("high") && tempTasks.get(j).getPriority().equals("low")) {
					Task temp = tempTasks.get(i);
					tempTasks.set(i, tempTasks.get(j));
					tempTasks.set(j, temp);
				}
			}
		}
	}
}


```
###### main\Parser\Parser.java
``` java
		else if (option.equals("delete") || option.equals("-")) {
			if ((Logic.Head.getLastCommand().equals("d") || Logic.Head.getLastCommand().equals("display")) == true) {
				// delete from uncompleted tasks
				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
					ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
					if (list.size() + list2.size() == 0) {
						UI.ui.print(EMPTY_MSG);
					} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
						// handle indexOutofBoundException
						UI.ui.print(DNE_MSG);
					} else {
						if (num < list.size()) {
							Task deleted = list.get(num - 1);
							issue = deleted.getIssue();
							Logic.crud.deleteTask(num - 1, 1);
							UI.ui.print("\"" + issue + "\" " + DELETE_MSG);
							arraylistsHaveBeenModified = true;
						} else {
							Task deleted = list2.get(num - list.size() - 1);
							issue = deleted.getIssue();
							Logic.crud.deleteTask(num - 1, 1);
							UI.ui.print("\"" + issue + "\" " + DELETE_MSG);
							arraylistsHaveBeenModified = true;
						}
					}
				} catch (Exception e) {
				}
			} else if ((Logic.Head.getLastCommand().equals("search") || Logic.Head.getLastCommand().equals("s"))) {
				// delete from search results
				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Logic.Search.getSearchedTasks();
					if (list.size() == 0) {
						UI.ui.print(EMPTY_MSG);
					} else if (list.size() < num || num - 1 < 0) {
						// handle indexOutofBoundException
						UI.ui.print(DNE_MSG);
					} else {
						Task deleted = list.get(num - 1);
						issue = deleted.getIssue();
						Logic.crud.deleteTask(num - 1, 3);
						UI.ui.print("\"" + issue + "\" " + DELETE_MSG);
						arraylistsHaveBeenModified = true;
					}
				} catch (Exception e) {
				}
			} else if ((Logic.Head.getLastCommand().equals("dc")
					|| (Logic.Head.getLastCommand().equals("displaycompleted") == true))) {
				// delete from completed tasks
				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Storage.localStorage.getCompletedTasks();
					if (list.size() == 0) {
						UI.ui.print(EMPTY_MSG);
					} else if (list.size() < num || num - 1 < 0) {
						// handle indexOutofBoundException
						UI.ui.print(DNE_MSG);
					} else {
						Task deleted = list.get(num - 1);
						issue = deleted.getIssue();
						Logic.crud.deleteTask(num - 1, 2);
						UI.ui.print("\"" + issue + "\" " + DELETE_MSG);
						arraylistsHaveBeenModified = true;
					}
				} catch (Exception e) {
				}
			}

			else {
				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
					ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
					if (list.size() + list2.size() == 0) {
						UI.ui.print(EMPTY_MSG);
					} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
						// handle indexOutofBoundException
						UI.ui.print(DNE_MSG);
					} else {
						if (num < list.size()) {
							Task deleted = list.get(num - 1);
							issue = deleted.getIssue();
							Logic.crud.deleteTask(num - 1, 1);
							UI.ui.print("\"" + issue + "\" " + DELETE_MSG);
							arraylistsHaveBeenModified = true;
						} else {
							Task deleted = list2.get(num - list.size() - 1);
							issue = deleted.getIssue();
							Logic.crud.deleteTask(num - 1, 1);
							UI.ui.print("\"" + issue + "\" " + DELETE_MSG);
							arraylistsHaveBeenModified = true;
						}
					}
				} catch (Exception e) {
				}
			}
		} else if (option.equals("display") || option.equals("d")) {
			UI.ui.print("UNCOMPLETED TASKS");
			Logic.crud.displayUncompletedAndFloatingTasks();
		}

		else if (option.equals("displaycompleted") || option.equals("dc")) {
			Logic.crud.displayCompletedTasks();
		}

		else if (option.equals("view") || option.equals("v")) {
			try {
				int num = Integer.parseInt(s);
				Logic.crud.viewIndividualTask(num - 1);
			} catch (Exception e) {
			}
		}

		else if (option.equals("clear") || option.equals("c")) {
			Logic.crud.clearTasks();
			UI.ui.print(CLEAR_MSG);
			arraylistsHaveBeenModified = true;
		}

		else if (option.equals("sort")) { // by alphabetical order
			Logic.Sort.sortTasksAlphabetically();
			UI.ui.print(SORT_MSG);
			arraylistsHaveBeenModified = true;
		}

		else if (option.equals("search") || option.equals("s")) {
			Logic.Search.searchTasksByKeyword(s);
		}

		else if (option.equals("mark") || option.equals("m")) {
			try {
				int num = Integer.parseInt(s);
				// check if user input integer is valid. If it is valid, mark
				// should
				// work
				ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
				ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					UI.ui.print(EMPTY_MSG);
				} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
					UI.ui.print(MARK_FAIL_MSG);
				} else {
					Logic.Mark.markTaskAsCompleted(num - 1);
					UI.ui.print(s + MARK_MSG);
					arraylistsHaveBeenModified = true;
				}
			} catch (Exception e) {

			}
		} else if (option.equals("unmark") || option.equals("um")) {
			try {
				int num = Integer.parseInt(s);
				// check if user input integer is valid. If it is valid, unmark
				// should
				// work
				ArrayList<Task> list = Storage.localStorage.getCompletedTasks();
				if (list.size() == 0) {
					UI.ui.print(NoCompleted_MSG);
				} else if (list.size() < num || num - 1 < 0) {
					UI.ui.print(UNMARK_FAIL_MSG);
				} else {
					Logic.Mark.markTaskAsUncompleted(num - 1);
					UI.ui.print(s + UNMARK_MSG);
					arraylistsHaveBeenModified = true;
				}
			} catch (Exception e) {

			}
		} 
```
###### main\Parser\Parser.java
``` java
		else if (option.equals("p")) {
			try {
				int num = Integer.parseInt(s);
				// check if user input integer is valid. If it is valid, edit
				// should
				// work
				ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
				ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					UI.ui.print(EMPTY_MSG);
				} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
					UI.ui.print(PRIORITY_FAIL_MSG);
				} else {
					UI.ui.print("Enter priority");
					String priority = sc.nextLine();
					Logic.Mark.setPriority(num - 1, priority);
					arraylistsHaveBeenModified = true;
				}
			} catch (Exception e) {
			}
		} else if (option.equals("sortp") || option.equals("sp")) {

			Logic.Sort.sortTasksPriority();
			Logic.crud.displayUncompletedAndFloatingTasks();
		}

		else if (option.equals("history")) {
			String pastCommands = Undo.getInstance().viewPastCommands();
			UI.ui.print(pastCommands);
		}

		else if (option.equals("undo")) {
			String outcome = Undo.getInstance().undo();
			UI.ui.print(outcome);
		}

		else if (option.equals("exit")) {
			Logic.crud.exit();
		}

		else if (option.equals("help")) {
			Logic.Help.printHelpMenu();
		}

		else {
			UI.ui.print(INVALID_MSG);
		}
		return arraylistsHaveBeenModified;
	}

```
###### main\Storage\fileStorage.java
``` java
package Storage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Task.Task;

public class fileStorage {

	/**
	 * Function that saves the given arraylist into a file named as the String fileName
	 * 
	 */
	public static void saveFile(String fileName, ArrayList<Task> details) throws IOException {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			for (Task t : details) {
				oos.writeObject(t);
			}
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
```
###### main\Storage\localStorage.java
``` java
package Storage;
import Task.Task;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class localStorage {

	//ArrayList to store the contents added to the file
	private static ArrayList<Task> uncompletedTasks = new ArrayList<Task>();
	private static ArrayList<Task> completedTasks = new ArrayList<Task>();
	private static ArrayList<Task> floatingTasks = new ArrayList<Task>();

	/**
	 * Function to return the ArrayList details
	 * @return the ArrayList details
	 */
	public static ArrayList<Task> getUncompletedTasks() {
		return uncompletedTasks;
	}

	public static ArrayList<Task> getCompletedTasks() {
		return completedTasks;
	}
	
	public static ArrayList<Task> getFloatingTasks() {
		return floatingTasks;
	}

	/**
	 * Function to get a particular task from the list of tasks
	 * 
	 * @param index the index of the task that is required
	 * 
	 * @return Task task of specified index
	 */
	public static Task getUncompletedTask(int index) {
		Task temp = null;
		for(int i = 0; i<uncompletedTasks.size(); i++) {
			if(i == index) {
				temp = uncompletedTasks.get(i);
			}
		}
		return temp;
	}

	public static Task getCertainUncompletedTask(int index) {
		Task temp = null;
		if(index >= 0&& index < uncompletedTasks.size())
			return uncompletedTasks.get(index);
		else
			return temp;
	}

	public static Task getCompletedTask(int index) {
		Task temp = null;
		for(int i = 0; i<completedTasks.size(); i++) {
			if(i == index) {
				temp = completedTasks.get(i);
			}
		}
		return temp;
	}
	
	public static Task getFloatingTask(int index) {
		Task temp = null;
		for(int i = 0; i<floatingTasks.size(); i++) {
			if(i == index) {
				temp = floatingTasks.get(i);
			}
		}
		return temp;
	}

	/**
	 * Function to set a task to a particular index
	 * @param index
	 * @param temp
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void setUncompletedTask(int index, Task temp) {
		uncompletedTasks.set(index, temp);
	}

	public static void setCompletedTask(int index, Task temp) {
		completedTasks.set(index, temp);
	}
	
	public static void setFloatingTask(int index, Task temp) {
		floatingTasks.set(index, temp);
	}

	/**
	 * Function to assign details array list to given array list
	 * @param changedDetails the arraylist to be assigned to details
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void setArrayList(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		uncompletedTasks = changedDetails;
	}

	/**
	 * Function to add a task to the uncompleted task list
	 * 
	 * @param task contains the task to be added
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void addToUncompletedTasks(Task task) {
		uncompletedTasks.add(task);
	}

	/**
	 * Function to add a task to the list of completed tasks
	 * 
	 * @param task the task to be added
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void addToCompletedTasks(Task task) throws IOException, ClassNotFoundException {
		completedTasks.add(task);
	}
	
	public static void addToFloatingTasks(Task task) {
		floatingTasks.add(task);
	}

	/**
	 * Function to delete a task from the file
	 * 
	 * @param index contains the index of the task to be deleted
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static Task delFromUncompletedTasks(int index) {
		Task temp = uncompletedTasks.remove(index);
		return temp;
	}

	/**
	 * Function to delete a task from the list of completed tasks
	 * 
	 * @param index contains the index of the task to be deleted
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static Task delFromCompletedTasks(int index) {
		Task temp = completedTasks.remove(index);
		return temp;
	}
	
	public static Task delFromFloatingTasks(int index) {
		Task temp = floatingTasks.remove(index);
		return temp;
	}

	/**
	 * Function to clear the contents of the file
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void clear() {
		uncompletedTasks.clear();
		completedTasks.clear();
		floatingTasks.clear();
	}

	// replace the current tasks arraylists with the given arraylists, to "undo" to the previous state
	public static void revertToPreviousState(ArrayList<Task> previousCompleted, 
											 ArrayList<Task> previousUncompleted,
											 ArrayList<Task> previousFloating) {
		completedTasks = previousCompleted;
		uncompletedTasks = previousUncompleted;
		floatingTasks = previousFloating;
	}
}
```
###### main\UI\ui.java
``` java
package UI;

import java.util.Scanner;
//import static org.fusesource.jansi.Ansi.*;
//import static org.fusesource.jansi.Ansi.Color.*;
public class ui {

	private static Scanner sc = new Scanner(System.in);

	/**
	 * Function to print the incoming string
	 * @param temp the string to be printed
	 */
	public static void print(String temp) {
		//System.out.print( ansi().fg(GREEN).a(temp).fg(WHITE) );
		System.out.print(temp);
		if(temp.equals("command: ") != true) {
			System.out.println();
		}
	}
	
	/**
	 * Function to accept the command from the user
	 * @return returns the string entered by the user
	 */
	public static String acceptCommand() {
		String command = sc.nextLine();
		return command;
	}

}
```
