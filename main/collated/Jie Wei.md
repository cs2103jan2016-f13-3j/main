# Jie Wei
###### main\Logic\Core.java
``` java
	/**
	 * Function to change the location of where tasks are stored on the computer.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void changeDirectoryCommand() throws IOException, ClassNotFoundException {
		String description = parserObject.getDescription();
		ImportTasks importTasksObject = new ImportTasks();
		if (description.isEmpty()) { // only "dir" was typed, this will display the current storage folder directory in use.
			String currentStorageDirectory = importTasksObject.getFolderDirectory();
			if (currentStorageDirectory.isEmpty()) { // indicates source.
				// folder is in use
				uiObject.printGreen(MSG_DIRECTORY_USED + MSG_DEFAULT_DIRECTORY);
			} else {
				uiObject.printGreen(MSG_DIRECTORY_USED + currentStorageDirectory);
			}
		} else { // "dir <path>" was entered
			String feedback = importTasksObject.changeStorageDestination(description);
			uiObject.print(feedback);
		}
	}

	/**
	 * Function to redo a command entered by the user. 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void redoCommand() throws ClassNotFoundException, IOException {
		String s = parserObject.getDescription();
		if (s.isEmpty()) { // only "redo" was typed.
			String outcome = Undo.getInstance().redo();
			uiObject.printGreen(outcome);
		} else if (s.equals("all")) {
			int redoCount = Undo.getInstance().getRedoCount();
			if (redoCount == 0) { // if no commands to redo.
				uiObject.printRed(MSG_NO_REDO_COMMAND);
			} else {
				for (int i = 0; i < redoCount; i++) { // do redo for all stored commands.
					String outcome = Undo.getInstance().redo();
					uiObject.printGreen(outcome);
				}
				uiObject.printGreen(MSG_ALL_COMMANDS_REDONE);
			}
		} else { // e.g. "redo 2" will redo the latest 2 commands.
			try {
				int count = Integer.parseInt(s);
				if (count < 1 || count > Undo.getInstance().getRedoCount()) { // if entered count is outside valid bounds.
					uiObject.printRed(MSG_INVALID_REDO_COUNT);
				} else {
					for (int i = 0; i < count; i++) { // redo the number of commands specified.
						if (Undo.getInstance().getRedoCount() == 0) { // all
							// commands have been redone but user used a higher int.
							uiObject.printRed(MSG_NO_REDO_COMMAND);
							break;
						}
						String outcome = Undo.getInstance().redo();
						uiObject.printGreen(outcome);
					}
				}
			} catch (NumberFormatException e) { // if non-number was entered, e.g. "redo hello".
				uiObject.printRed(MSG_INVALID_REDO_COUNT);
			}
		}
	}

	/**
	 * Function to undo a command entered by the user.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void undoCommand() throws ClassNotFoundException, IOException {
		String s = parserObject.getDescription();
		if (s.isEmpty()) { // only "undo" was typed.
			String outcome = Undo.getInstance().undo();
			uiObject.printGreen(outcome);
		} else if (s.equals("all")) {
			int historyCount = Undo.getInstance().getHistoryCount();
			if (historyCount == 0) { // if no commands to undo.
				uiObject.printRed(MSG_NO_PAST_COMMAND);
			} else {
				for (int i = 0; i < historyCount; i++) { // do undo for all stored commands.
					String outcome = Undo.getInstance().undo();
					uiObject.printGreen(outcome);
				}
				uiObject.printGreen(MSG_ALL_COMMANDS_UNDONE);
			}
		} else { // e.g. "undo 2" will undo the latest 2 commands.
			try {
				int count = Integer.parseInt(s);
				if (count < 1 || count > Undo.getInstance().getHistoryCount()) { // if entered count is outside valid bounds.
					uiObject.printRed(MSG_INVALID_UNDO_COUNT);
				} else {
					for (int i = 0; i < count; i++) { // undo the number of commands specified.
						if (Undo.getInstance().getHistoryCount() == 0) { 
							// all commands have been undone but user used a higher int
							uiObject.printRed(MSG_NO_PAST_COMMAND);
							break;
						}
						String outcome = Undo.getInstance().undo();
						uiObject.printGreen(outcome);
					}
				}
			} catch (NumberFormatException e) { // if non-number was entered, e.g. "undo hello".
				uiObject.printRed(MSG_INVALID_UNDO_COUNT);
			}
		}
	}

```
###### main\Logic\Crud.java
``` java
	/**
	 * Function to import the tasks from the storage file.
	 * 
	 * @param task                    the tasks to be added to the arraylist storage.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void addTaskViaImport(Task task, String flag) throws IOException, ClassNotFoundException {
		if (flag.equals(FLAG_UNCOMPLETED)) {
			noDuplicate = checkForDuplicateTasks(task, localStorageObject.getUncompletedTasks());
			if (noDuplicate) {
				localStorageObject.addToUncompletedTasks(task);
			}
		} else if (flag.equals(FLAG_COMPLETED)) {
			noDuplicate = checkForDuplicateTasks(task, localStorageObject.getCompletedTasks());
			if (noDuplicate) {
				localStorageObject.addToCompletedTasks(task);
			}
		} else if (flag.equals(FLAG_FLOATING)) {
			noDuplicate = checkForDuplicateTasks(task, localStorageObject.getFloatingTasks());
			if (noDuplicate) {
				localStorageObject.addToFloatingTasks(task);
			}
		}
	}

```
###### main\Logic\Help.java
``` java
package Logic;

import java.util.InputMismatchException;
import java.util.Scanner;

import UI.UI;

public class Help {
	
	private static Scanner sc;
	
	private static final String EXAMPLE_HEADER = "\nEXAMPLES\n";
	
	private static final String ADD_HEADER = "1.  ADDING TASKS\n";
	private static final String ADD_1 = "Type \"add\", \"+\" or \"a\" followed by task description.";
	private static final String ADD_2 = "To include date(s), type \"`\" (the key above TAB) after the description," + "\n"
	                                    + "and then start typing your date(s).";
	private static final String ADD_3 = "Use \"from\" or \"on\" for start dates, use \"to\" or \"by\" for end dates.\n";
	private static final String ADD_4 = "To add a recurring task, follow the steps above,\n"
	                                    + "and type \"r\" at the end of the command. Press Enter.";
	private static final String ADD_5 = "Now enter the frequency (number of days between each occurrence)\n" 
			                            + "and the date which you want the task to recur until."; 
	private static final String ADD_SAMPLE_1 = "add Think of proposal idea\n"
			                                   + "(Adds a task without a date)\n";
	private static final String ADD_SAMPLE_2 = "add Submit report ` by tomorrow 5pm\n" 
			                                   + "(Adds a task with deadline of tomorrow 5pm)\n";
	private static final String ADD_SAMPLE_3 = "add Meeting ` from today 3pm to today 5pm\n" 
			                                   + "(Adds a task for today from 3pm to 5pm)\n";
	private static final String ADD_SAMPLE_4 = "add Pay bills ` today r --followed by--> 30 31/12/2016\n"
			                                   + "(Adds a task starting today, that recurs every 30 days until 31/12/2016)";
	
	private static final String DELETE_HEADER = "2.  DELETING TASKS\n";
	private static final String DELETE_1 = "Type \"delete\" or \"-\" followed by the task number you wish to delete.";
	private static final String DELETE_2 = "The task number is based on the latest display view used.\n";
	private static final String DELETE_3 = "To delete all occurrences of the same recurring task, type \"delete\"\n" 
	                                       + "or \"-\" followed by \"all\" and the task number.\n";
	private static final String DELETE_4 = "To delete all tasks, type \"clear\" or \"c\".";
	private static final String DELETE_SAMPLE_1 = "delete 3\n" 
	                                              + "(Deletes task number 3)\n";
	private static final String DELETE_SAMPLE_2 = "delete all 2\n" 
			                                      + "(Deletes task 2 & all of its related occurrences if it is a recurring one)\n";
	private static final String DELETE_SAMPLE_3 = "clear\n"
			                                      + "(Deletes all tasks currently stored)";
			
	private static final String EDIT_HEADER = "3.  EDITING TASKS\n";	
	private static final String EDIT_1 = "Type \"edit\" or \"e\" followed by the task number you wish to edit. Press Enter.\n";
	private static final String EDIT_2 = "Now type the new task description and/or date(s).\n";
	private static final String EDIT_3 = "Remember to use \"`\" (the key above TAB) if you wish to have start/end date(s).\n";
	private static final String EDIT_4 = "To edit all occurrences of the same recurring task, type \"edit\"\n"
			                             + "or \"e\" followed by \"all\" and the task task number.\n";
	private static final String EDIT_5 = "For convenience, you may press \"Ctrl + V\"\n"
			                             + "to paste the original description (and dates)."; 
	private static final String EDIT_SAMPLE_1 = "edit 1 --followed by--> Buy groceries\n"
			                                    + "(Edits task number 1 to a floating task (no date) with the new description)\n";
	private static final String EDIT_SAMPLE_2 = "edit 2 --followed by--> Submit report ` by tomorrow 5pm\n"
			                                    + "(Edits task number 2 to a task due tomorrow 5pm)";
	
	private static final String DISPLAY_HEADER = "4.  DISPLAYING TASKS\n";
	private static final String DISPLAY_1 = "Type \"display\" or \"d\" to show all tasks up till 7 days from today.\n"
			                                + "(excludes floating & completed tasks)\n";
	private static final String DISPLAY_2 = "You may also add the following keywords to specify a certain display scope:\n";
	private static final String DISPLAY_3 = "all              shows all stored tasks (except completed tasks)";
	private static final String DISPLAY_4 = "today            shows all tasks with today as their start/end date(s)";
	private static final String DISPLAY_5 = "tomorrow         shows all tasks with tomorrow as their start/end date(s)";
	private static final String DISPLAY_6 = "last week        shows all tasks from last week";
	private static final String DISPLAY_7 = "next week        shows all tasks from the nearest Monday to the Sunday after it";
	private static final String DISPLAY_8 = "two weeks later  shows all tasks scheduled for two weeks from now";
	private static final String DISPLAY_9 = "floating or f    shows all floating tasks";
	private static final String DISPLAY_10 = "completed or c   shows all completed tasks\n";	
	private static final String DISPLAY_11 = "Note: Any command involving task number (such as delete 1)\n"
			                                 + "      will be based on the last display scope used.";
	private static final String DISPLAY_SAMPLE_1 = "display floating\n";
	private static final String DISPLAY_SAMPLE_2 = "display tomorrow";
	
	private static final String MARK_HEADER = "5.  MARKING TASKS AS COMPLETED / UNCOMPLETED\n"; 
	private static final String MARK_1 = "Type \"mark\" or \"m\" followed by a task number to mark that task as complete.\n";
	private static final String MARK_2 = "Type \"unmark\" or \"um\" followed by a task number to mark that task as uncomplete.";
	private static final String MARK_SAMPLE_1 = "mark 3";
	
	private static final String PRIORITY_HEADER = "6.  SETTING PRIORITY TO A TASK\n";
	private static final String PRIORITY_1 = "Tasks with higher priority will appear higher\n"
			                                 + "on the list when you display your tasks.\n";
	private static final String PRIORITY_2 = "Type \"priority\" or \"p\" followed by a task number to change its priority level.\n"
			                                 + "Press Enter.\n";
	private static final String PRIORITY_3 = "Now type \"high\", \"medium\" or \"low\" to set the respective priority to the task.\n";
	private static final String PRIORITY_4 = "To do the same for all occurrences of the same recurring task,\n"
			                                 + "type \"priority\" or \"p\" followed by \"all\" and the task number.";
	private static final String PRIORITY_SAMPLE_1 = "priority 1 --followed by--> high\n"
			                                        + "(sets task number priority to high)\n";
	private static final String PRIORITY_SAMPLE_2 = "priority all 2 --followed by--> medium\n"
			                                        + "(sets medium priority to task 2 & all of its related occurrences if it is a recurring one)";

	private static final String LABEL_HEADER = "7.  LABELLING TASKS\n";
	private static final String LABEL_1 = "Type \"label\' followed by a task number. Press Enter.";
	private static final String LABEL_2 = "Now type the label you want to add to that task.\n\n"
			                              + "(Tasks can be searched using their labels)";
	private static final String LABEL_SAMPLE_1 = "label 3 --followed by--> Japan trip\n"
			                                     + "(adds a label called Japan trip to that task)";
	
	private static final String VIEW_HEADER = "8.  VIEWING DETAILS OF A TASK\n";
	private static final String VIEW_1 = "Type \"view\" or \"v\" followed by a task number to view the details of that task.\n";
	private static final String VIEW_2 = "You will be able to see start & end dates/time (if any), issue description,\n"
			                             + "completetion status, priority level and labels (if any) of the task.";
	private static final String VIEW_SAMPLE_1 = "view 3";
	
	private static final String SEARCH_HEADER = "9.  SEARCHING FOR TASKS\n";
	private static final String SEARCH_1 = "Type \"search\" or \"s\" followed the word(s) that you wish to search for.";
	private static final String SEARCH_2 = "Search results will be based on task descriptions.\n";
	private static final String SEARCH_3 = "If you search using more than 1 word, the ordering of the words\n"
			                               + "does not matter as long as all words are present in a task.";
	private static final String SEARCH_SAMPLE_1 = "search japan trip\n"
			                                      + "(searches for task(s) whose issue(s) contain the words \"japan\" and \"trip\")";
	private static final String SEARCH_SAMPLE_2 = "A task with issue \"pack luggage for trip to japan\" will also be a match.";
	
	private static final String UNDO_REDO_HEADER = "10. UNDOING & REDOING COMMANDS\n";
	private static final String UNDO_REDO_1 = "Type \"history\" to see all the commands you may undo.\n"
                                              + "Commands will be undone from the top of the list.\n";
	private static final String UNDO_REDO_2 = "Type \"future\" to see all the commands you may redo.\n"
                                              + "Commands will be redone from the top of the list.";
	private static final String UNDO_REDO_3 = "Type \"undo\" to roll back the last command that was executed (if any).";
	private static final String UNDO_REDO_4 = "Type \"redo\" to re-execute the last command that was undone (if any).\n";
	private static final String UNDO_REDO_5 = "You may also add a number to undo/redo a specific number of commands (if any).";
	private static final String UNDO_REDO_6 = "Or add \"all\" to undo/redo all commands (if any).\n";
	private static final String UNDO_REDO_SAMPLE_1 = "undo\n" 
			                                         + "(undo the last command executed)\n";
	private static final String UNDO_REDO_SAMPLE_2 = "redo 3\n"
			                                         + "(redo the last 3 commands that were undone)";
	
	private static final String DIRECTORY_HEADER = "11. CHANGING SAVE DIRECTORY\n";
	private static final String DIRECTORY_1 = "Type \"dir\" to see the current folder directory that is used to store your tasks.";
	private static final String DIRECTORY_2 = "Default directory refers to where the executable file of Agendah is located.\n"; 
	private static final String DIRECTORY_3 = "Type \"dir\" followed by a folder path to use that folder for storage.";
	private static final String DIRECTORY_4 = "Please include \"\\\" at the end of the folder path (if not already present).\n";
	private static final String DIRECTORY_5 = "Using \"default\" as the folder path will revert to saving at the default directory.\n";
	private static final String DIRECTORY_6 = "If an invalid folder path was used, the default directory will be used instead.";
	private static final String DIRECTORY_SAMPLE_1 = "dir C:\\Program Files\\My Folder\\\n"
			                                         + "(sets the storage location to the folder named \"My Folder\")";
	
	private static final String EXIT_HEADER = "12. EXITING AGENDAH\n";
	private static final String EXIT_1 = "Type \"exit\" to quit Agendah";
	private static final String EXIT_2 = "Have a nice day!";
	
	private static final String HELP_MENU = "\nHELP CONTENTS:\n\n" + ADD_HEADER + DELETE_HEADER + EDIT_HEADER + DISPLAY_HEADER
			                                + MARK_HEADER + PRIORITY_HEADER + LABEL_HEADER + VIEW_HEADER + SEARCH_HEADER
			                                + UNDO_REDO_HEADER + DIRECTORY_HEADER + EXIT_HEADER;
	private static final String HELP_PROMPT = "Enter the number of the topic you need help with";
	
	private static final String MSG_INVALID_INPUT = "Please enter a valid number";
	
	private UI uiObject;

	public Help() {
		uiObject = new UI();
	}
	
	/**
	 * Method to print the list of help topics, and the specific topic chosen by the user.
	 */
	public void printHelpMenu() {
		sc = new Scanner(System.in);
		uiObject.printYellow(HELP_MENU);
		uiObject.printGreen(HELP_PROMPT);
		
		int topicNumber = 0;
		try {
			topicNumber = sc.nextInt();
		} catch (InputMismatchException e) {
			uiObject.printRed(MSG_INVALID_INPUT);
			return;
		}

		uiObject.eraseScreen();
		printHelpTopic(topicNumber);
	}
	
	/**
	 * Method to print a help topic according to the input topic number.
	 * 
	 * @param topicNumber The topic number to be printed.
	 */
	private void printHelpTopic(int topicNumber) {
		if (topicNumber == 1) {
			printAddHelp();
		} else if (topicNumber == 2) {
			printDeleteHelp();
		} else if (topicNumber == 3) {
			printEditHelp();
		} else if (topicNumber == 4) {
			printDisplayHelp();
		} else if (topicNumber == 5) {
			printMarkHelp();
		} else if (topicNumber == 6) {
			printPriorityHelp();
		} else if (topicNumber == 7) {
			printLabelHelp();
		} else if (topicNumber == 8) {
			printViewHelp();
		} else if (topicNumber == 9) {
			printSearcHelp();
		} else if (topicNumber == 10) {
			printUndoRedoHelp();
		} else if (topicNumber == 11) {
			printDirectoryHelp();
		} else if (topicNumber == 12) {
			printExitHelp();
		} else {
			// topicNumber < 1 or > 12
			uiObject.printRed(MSG_INVALID_INPUT);
			return;
		}
	}

	// Method to print the strings related to Add function.
	private void printAddHelp() {
		uiObject.printGreen(ADD_HEADER);
		uiObject.printYellow(ADD_1);
		uiObject.printYellow(ADD_2);
		uiObject.printYellow(ADD_3);
		uiObject.printYellow(ADD_4);
		uiObject.printYellow(ADD_5);
		uiObject.printGreen(EXAMPLE_HEADER);
		uiObject.printCyan(ADD_SAMPLE_1);
		uiObject.printCyan(ADD_SAMPLE_2);
		uiObject.printCyan(ADD_SAMPLE_3);
		uiObject.printCyan(ADD_SAMPLE_4);
	}

	// Method to print the strings related to Delete function.
	private void printDeleteHelp() {
		uiObject.printGreen(DELETE_HEADER);
		uiObject.printYellow(DELETE_1);
		uiObject.printYellow(DELETE_2);
		uiObject.printYellow(DELETE_3);
		uiObject.printYellow(DELETE_4);
		uiObject.printGreen(EXAMPLE_HEADER);
		uiObject.printCyan(DELETE_SAMPLE_1);
		uiObject.printCyan(DELETE_SAMPLE_2);
		uiObject.printCyan(DELETE_SAMPLE_3);
	}

	// Method to print the strings related to Edit function.
	private void printEditHelp() {
		uiObject.printGreen(EDIT_HEADER);
		uiObject.printYellow(EDIT_1);
		uiObject.printYellow(EDIT_2);
		uiObject.printYellow(EDIT_3);
		uiObject.printYellow(EDIT_4);
		uiObject.printYellow(EDIT_5);
		uiObject.printGreen(EXAMPLE_HEADER);
		uiObject.printCyan(EDIT_SAMPLE_1);
		uiObject.printCyan(EDIT_SAMPLE_2);
	}

	// Method to print the strings related to Display function.
	private void printDisplayHelp() {
		uiObject.printGreen(DISPLAY_HEADER);
		uiObject.printYellow(DISPLAY_1);
		uiObject.printYellow(DISPLAY_2);
		uiObject.printYellow(DISPLAY_3);
		uiObject.printYellow(DISPLAY_4);
		uiObject.printYellow(DISPLAY_5);
		uiObject.printYellow(DISPLAY_6);
		uiObject.printYellow(DISPLAY_7);
		uiObject.printYellow(DISPLAY_8);
		uiObject.printYellow(DISPLAY_9);
		uiObject.printYellow(DISPLAY_10);
		uiObject.printYellow(DISPLAY_11);
		uiObject.printGreen(EXAMPLE_HEADER);
		uiObject.printCyan(DISPLAY_SAMPLE_1);
		uiObject.printCyan(DISPLAY_SAMPLE_2);
	}

	// Method to print the strings related to Mark function.
	private void printMarkHelp() {
		uiObject.printGreen(MARK_HEADER);
		uiObject.printYellow(MARK_1);
		uiObject.printYellow(MARK_2);
		uiObject.printGreen(EXAMPLE_HEADER);
		uiObject.printCyan(MARK_SAMPLE_1);
	}

	// Method to print the strings related to Priority function.
	private void printPriorityHelp() {
		uiObject.printGreen(PRIORITY_HEADER);
		uiObject.printYellow(PRIORITY_1);
		uiObject.printYellow(PRIORITY_2);
		uiObject.printYellow(PRIORITY_3);
		uiObject.printYellow(PRIORITY_4);
		uiObject.printGreen(EXAMPLE_HEADER);
		uiObject.printCyan(PRIORITY_SAMPLE_1);
		uiObject.printCyan(PRIORITY_SAMPLE_2);
	}

	// Method to print the strings related to Label function.
	private void printLabelHelp() {
		uiObject.printGreen(LABEL_HEADER);
		uiObject.printYellow(LABEL_1);
		uiObject.printYellow(LABEL_2);
		uiObject.printGreen(EXAMPLE_HEADER);
		uiObject.printCyan(LABEL_SAMPLE_1);
	}

	// Method to print the strings related to View function.
	private void printViewHelp() {
		uiObject.printGreen(VIEW_HEADER);
		uiObject.printYellow(VIEW_1);
		uiObject.printYellow(VIEW_2);
		uiObject.printGreen(EXAMPLE_HEADER);
		uiObject.printCyan(VIEW_SAMPLE_1);
	}

	// Method to print the strings related to Search function.
	private void printSearcHelp() {
		uiObject.printGreen(SEARCH_HEADER);
		uiObject.printYellow(SEARCH_1);
		uiObject.printYellow(SEARCH_2);
		uiObject.printYellow(SEARCH_3);
		uiObject.printGreen(EXAMPLE_HEADER);
		uiObject.printCyan(SEARCH_SAMPLE_1);
		uiObject.printCyan(SEARCH_SAMPLE_2);
	}

	// Method to print the strings related to Undo function.
	private void printUndoRedoHelp() {
		uiObject.printGreen(UNDO_REDO_HEADER);
		uiObject.printYellow(UNDO_REDO_1);
		uiObject.printYellow(UNDO_REDO_2);
		uiObject.printYellow(UNDO_REDO_3);
		uiObject.printYellow(UNDO_REDO_4);
		uiObject.printYellow(UNDO_REDO_5);
		uiObject.printYellow(UNDO_REDO_6);
		uiObject.printGreen(EXAMPLE_HEADER);
		uiObject.printCyan(UNDO_REDO_SAMPLE_1);
		uiObject.printCyan(UNDO_REDO_SAMPLE_2);
	}

	// Method to print the strings related to Directory function.
	private void printDirectoryHelp() {		
		uiObject.printGreen(DIRECTORY_HEADER);
		uiObject.printYellow(DIRECTORY_1);
		uiObject.printYellow(DIRECTORY_2);
		uiObject.printYellow(DIRECTORY_3);
		uiObject.printYellow(DIRECTORY_4);
		uiObject.printYellow(DIRECTORY_5);
		uiObject.printYellow(DIRECTORY_6);
		uiObject.printGreen(EXAMPLE_HEADER);
		uiObject.printCyan(DIRECTORY_SAMPLE_1);
	}

	// Method to print the strings related to Exit function.
	private void printExitHelp() {
		uiObject.printGreen(EXIT_HEADER);
		uiObject.printYellow(EXIT_1);
		uiObject.printYellow(EXIT_2);
	}
}
```
###### main\Logic\ImportTasks.java
``` java
package Logic;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import Task.Task;

public class ImportTasks {
	
	private static String storageFolderDirectory;
	
	private static final String FEEDBACK_CHANGE_DIRECTORY_DEFAULT = "The default folder is now in use as the storage folder directory";
	private static final String FEEDBACK_CHANGE_DIRECTORY_ERROR = "The target directory is invalid. The default folder is now in use";
	private static final String FEEDBACK_CHANGE_DIRECTORY_SUCCESS = "The storage folder directory has been changed to ";
	private static final String FLAG_COMPLETED = "completed";
	private static final String FLAG_FLOATING = "floating";
	private static final String FLAG_UNCOMPLETED = "uncompleted";
	private static final String STORAGE_FILENAME_COMPLETED_TASKS = "TasksCompleted.txt";
	private static final String STORAGE_FILENAME_FLOATING_TASKS = "TasksFloating.txt";
	private static final String STORAGE_FILENAME_UNCOMPLETED_TASKS = "TasksUncompleted.txt";
	private static final String STORAGE_FOLDER_TEXT_FILE = "StorageFolderPath.txt";
	private static final String STRING_BACKSLASH = "\\";
	private static final String STRING_DEFAULT = "default";
	private static final String STRING_EMPTY = "";

	private static final Logger logger = Logger.getLogger(Class.class.getName());
	
	private Crud crudObject;
	
	public ImportTasks(){
		crudObject = Crud.getInstance();
	}
	
	/**
	 * Method to check (and create) storage files, and import tasks from them into program instance.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void prepareAndImportFiles() throws FileNotFoundException, IOException, ClassNotFoundException {
		File StorageFolderPathFile = new File(STORAGE_FOLDER_TEXT_FILE);
		if (!StorageFolderPathFile.exists()) {
			StorageFolderPathFile.createNewFile();
		}
		
		Scanner sc = new Scanner(StorageFolderPathFile);
		try {
			storageFolderDirectory = sc.nextLine();
		} catch (NoSuchElementException e) { // empty file (no directory specified)
			storageFolderDirectory = STRING_EMPTY;
		}
		sc.close();
		
		if (storageFolderDirectory.equals(STRING_DEFAULT)) {
			storageFolderDirectory = STRING_EMPTY;
		}
		
		checkIfFileExists(storageFolderDirectory, STORAGE_FILENAME_UNCOMPLETED_TASKS);
		checkIfFileExists(storageFolderDirectory, STORAGE_FILENAME_COMPLETED_TASKS);
		checkIfFileExists(storageFolderDirectory, STORAGE_FILENAME_FLOATING_TASKS);
		
		importTasksFromStorage(storageFolderDirectory + STORAGE_FILENAME_UNCOMPLETED_TASKS, FLAG_UNCOMPLETED);
		importTasksFromStorage(storageFolderDirectory + STORAGE_FILENAME_COMPLETED_TASKS, FLAG_COMPLETED);
		importTasksFromStorage(storageFolderDirectory + STORAGE_FILENAME_FLOATING_TASKS, FLAG_FLOATING);
	}

	/**
	 * Method to check whether the storage file exists. If not, creates one.
	 * 
	 * @param folderDirectory The destination storage folder.
	 * @param fileName        The destination storage file.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void checkIfFileExists(String folderDirectory, String fileName) throws IOException, FileNotFoundException {
		File file = new File(folderDirectory + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) { // occurs when the specified folder directory does not exist
				file = new File(fileName);
				file.createNewFile();
				storageFolderDirectory = STRING_EMPTY; // replace the folder directory to the source folder
			}
		}
	}

	/**
	 * Function to import tasks from a given storage file
	 * 
	 * @param fileName String that contains the name of the storage file.
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	private void importTasksFromStorage(String fileName, String flag) throws IOException, ClassNotFoundException {
		//		LoggerTry.startLog();
		JsonReader reader = new JsonReader(new FileReader(fileName));
		ArrayList<Task> GsonObjects = new ArrayList<Task>();
		try {
			reader.beginArray();
			while (reader.hasNext()) {
				Task obj = (new Gson()).fromJson(reader, Task.class);
				GsonObjects.add(obj);
			}
			reader.endArray();
			reader.close();
			for (Task t : GsonObjects) {
				crudObject.addTaskViaImport(t, flag);
			}
		} catch (EOFException e) {
			return;
		}
	}
	
	// Getter methods
	public String getUncompletedTasksStorageFileName() {
		return storageFolderDirectory + STORAGE_FILENAME_UNCOMPLETED_TASKS;
	}
	
	public String getCompletedTasksStorageFileName() {
		return storageFolderDirectory + STORAGE_FILENAME_COMPLETED_TASKS;
	}
	
	public String getFloatingTasksStorageFileName() {
		return storageFolderDirectory + STORAGE_FILENAME_FLOATING_TASKS;
	}
	
	public String getFolderDirectory() {
		return storageFolderDirectory;
	}
	
	/**
	 * Method to change storage folder directory. Uses source folder if invalid directory detected.
	 * 
	 * @param destination The folder path entered by the user.
	 * @return            Feedback regarding outcome of directory change.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public String changeStorageDestination(String destination) throws IOException, ClassNotFoundException {
		storageFolderDirectory = checkAndSetDirectoryName(destination);
		
		checkIfFileExists(storageFolderDirectory, STORAGE_FILENAME_UNCOMPLETED_TASKS);
		checkIfFileExists(storageFolderDirectory, STORAGE_FILENAME_COMPLETED_TASKS);
		checkIfFileExists(storageFolderDirectory, STORAGE_FILENAME_FLOATING_TASKS);
		
		// if the new storage directory already contains the storage files,
		// all tasks (unless duplicate) in these files will be imported into the current program instance
		importTasksFromStorage(storageFolderDirectory + STORAGE_FILENAME_UNCOMPLETED_TASKS, FLAG_UNCOMPLETED);
		importTasksFromStorage(storageFolderDirectory + STORAGE_FILENAME_COMPLETED_TASKS, FLAG_COMPLETED);
		importTasksFromStorage(storageFolderDirectory + STORAGE_FILENAME_FLOATING_TASKS, FLAG_FLOATING);
		
		if (destination.equals(STRING_DEFAULT)) { // default directory was chosen
			writeNewDirectory(destination);
			return FEEDBACK_CHANGE_DIRECTORY_DEFAULT;
		}
		
		if (storageFolderDirectory.equals(STRING_EMPTY)) { // empty directory was given
			writeNewDirectory(STRING_DEFAULT);
			return FEEDBACK_CHANGE_DIRECTORY_ERROR;
		}
		
		if (!destination.contains(STRING_BACKSLASH)) { // does not contain \, thus not folder in Windows environment
			writeNewDirectory(STRING_DEFAULT);
			return FEEDBACK_CHANGE_DIRECTORY_ERROR;
		}
		
		writeNewDirectory(destination);
		return FEEDBACK_CHANGE_DIRECTORY_SUCCESS + storageFolderDirectory; // directory successfully changed
	}
	
	/**
	 * Method to check the given folder directory and assign it appropriately.
	 * 
	 * @param destination The directory to be checked.
	 * @return            The directory to be used.
	 */
	private String checkAndSetDirectoryName(String destination) {
		if (destination.equals(STRING_DEFAULT)) {
			return STRING_EMPTY;
		} else if (!destination.contains(STRING_BACKSLASH)) { // does not contain \, thus not folder on Windows
			return STRING_EMPTY;
		} else {
			return destination;
		}
	}
	
	/**
	 * Method to write the given directory path to the StorageFolderPath.txt file.
	 * 
	 * @param directory The target directory to be written.
	 * @throws IOException
	 */
	private void writeNewDirectory(String directory) throws IOException {
		File StorageFolderPathFile = new File(STORAGE_FOLDER_TEXT_FILE);
		FileWriter writer = new FileWriter(StorageFolderPathFile);
		writer.write(directory);
		writer.close();
	}
}

class LoggerTry {
	private final static Logger logger = Logger.getLogger(Class.class.getName());
	private static FileHandler fileHandler = null;

	public static void startLog() throws SecurityException, IOException {
		fileHandler = new FileHandler("loggerFile.log", true);
		Logger logger = Logger.getLogger("");
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
		logger.setLevel(Level.CONFIG);
	}
}
```
###### main\Logic\Save.java
``` java

package Logic;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Storage.LocalStorage;
import Task.Task;

public class Save {
	
	private ImportTasks importTasksObject;
	private LocalStorage localStorageObject;

	public Save() {
		importTasksObject = new ImportTasks();
		localStorageObject = LocalStorage.getInstance();
	}

	/**
	 * Method to save all 3 storage arraylists into their respective save files.
	 * 
	 * @throws IOException
	 */
	public void saveToFile() throws IOException {
		// Save uncompleted tasks
		String uncompletedTasksStorageFileName = importTasksObject.getUncompletedTasksStorageFileName();
		saveArrayToFile(localStorageObject.getUncompletedTasks(), uncompletedTasksStorageFileName);

		// Save completed tasks
		String completedTasksStorageFileName = importTasksObject.getCompletedTasksStorageFileName();
		saveArrayToFile(localStorageObject.getCompletedTasks(), completedTasksStorageFileName);

		// Save floating tasks
		String floatingTasksStorageFileName = importTasksObject.getFloatingTasksStorageFileName();
		saveArrayToFile(localStorageObject.getFloatingTasks(), floatingTasksStorageFileName);
	}

	/**
	 * Method to save the contents of an arraylist into a Gson text file.
	 * 
	 * @param sourceArray         The arraylist to be saved.
	 * @param destinationFileName The name to save the file as.
	 * @throws IOException
	 */
	private void saveArrayToFile(ArrayList<Task> sourceArray, String destinationFileName) throws IOException {
		Writer writer = new FileWriter(destinationFileName);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(sourceArray, writer);
		writer.close();
	}
}
```
###### main\Logic\Undo.java
``` java
package Logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Stack;

import Storage.LocalStorage;
import Task.Task;

public class Undo {
	
	private static Undo undo;
	
	private static final String CONFIRMATION_REDO = " has been redone";
	private static final String CONFIRMATION_UNDO = " has been undone";
	private static final String HEADER_UNDO_HISTORY = "Here are the commands you can undo, starting from the top: \n\n";
	private static final String HEADER_REDO_HISTORY = "Here are the commands you can redo, starting from the top: \n\n";
	private static final String MSG_NO_UNDO_COMMAND = "There are no remaining commands that can be undone";
	private static final String MSG_NO_REDO_COMMAND = "There are no remaining commands that can be redone";
		
	private ArrayList<String> undoCommands, redoCommands;
	private ArrayList<Task> uncompletedTasksSnapshot, completedTasksSnapshot, floatingTasksSnapshot;
	private LocalStorage localStorageObject; 
	private Stack<ArrayList<Task>> completedStack, uncompletedStack, floatingStack,
								   completedRedoStack, uncompletedRedoStack, floatingRedoStack;

	// Private constructor, following the singleton pattern.
	private Undo() {
		localStorageObject = LocalStorage.getInstance();
		
		completedStack = new Stack<ArrayList<Task>>();
		uncompletedStack = new Stack<ArrayList<Task>>();
		floatingStack = new Stack<ArrayList<Task>>();
		undoCommands = new ArrayList<String>();

		completedRedoStack = new Stack<ArrayList<Task>>();
		uncompletedRedoStack = new Stack<ArrayList<Task>>();
		floatingRedoStack = new Stack<ArrayList<Task>>();
		redoCommands = new ArrayList<String>();
	}

	/**
	 * Method to access this class, following the singleton pattern.
	 * Invokes constructor if Undo has not been initialised.
	 * 
	 * @return The Undo object.
	 */
	public static Undo getInstance() { //singleton
		if (undo == null) {
			undo = new Undo();
		}
		return undo;
	}

	/**
	 * Show a list of commands that can be undone.
	 * The first command displayed is the latest command that was done.
	 * 
	 * @return A list of all possible undo-able commands, if any. 
	 */
	public String viewPastCommands() {
		if (undoCommands.isEmpty()) {
			return MSG_NO_UNDO_COMMAND;
		}

		String result = HEADER_UNDO_HISTORY;
		result = getListOfCommands(undoCommands, result);
		return result.substring(0, result.length() - 1);
	}

	/**
	 * Show a list of commands that can be redone.
	 * The first command displayed is the latest command that was undone.
	 * 
	 * @return A list of all possible redo-able commands, if any. 
	 */
	public String viewRedoCommands() {
		if (redoCommands.isEmpty()) {
			return MSG_NO_REDO_COMMAND;
		}

		String result = HEADER_REDO_HISTORY;
		result = getListOfCommands(redoCommands, result);
		return result.substring(0, result.length() - 1);
	}

	/**
	 * Appends a numbered list of commands from an arraylist to a header string.
	 * 
	 * @param sourceArrayList The arraylist from which to get commands from.
	 * @param inputString     A header string where the list of commands will be added to.
	 * @return                The updated input string now containing the list of commands.
	 */
	private String getListOfCommands(ArrayList<String> sourceArrayList, String inputString) {
		int count = 0;
		for (int i = sourceArrayList.size() - 1; i >= 0; i--) {
			count++;
			inputString += count + ". " + sourceArrayList.get(i) + "\n";
		}
		return inputString;
	}

	/**
	 * Get the completed tasks arraylist copied from the previous program state.
	 * 
	 * @return Arraylist containing completed tasks.
	 */
	public ArrayList<Task> getLastCompletedState() {
		return completedStack.pop();
	}

	/**
	 * Get the uncompleted tasks arraylist copied from the previous program state.
	 * 
	 * @return Arraylist containing uncompleted tasks.
	 */
	public ArrayList<Task> getLastUnompletedState() {
		return uncompletedStack.pop();
	}

	/**
	 * Get the floating tasks arraylist copied from the previous program state.
	 * 
	 * @return Arraylist containing floating tasks.
	 */
	public ArrayList<Task> getLastFloatingState() {
		return floatingStack.pop();
	}

	/**
	 * Get the completed tasks arraylist copied from program state before the latest undo was performed.
	 * 
	 * @return Arraylist containing completed tasks.
	 */
	public ArrayList<Task> getLastRedoCompletedState() {
		return completedRedoStack.pop();
	}

	/**
	 * Get the uncompleted tasks arraylist copied from program state before the latest undo was performed.
	 * 
	 * @return Arraylist containing uncompleted tasks.
	 */
	public ArrayList<Task> getLastRedoUnompletedState() {
		return uncompletedRedoStack.pop();
	}

	/**
	 * Get the floating tasks arraylist copied from program state before the latest undo was performed.
	 * 
	 * @return Arraylist containing floating tasks.
	 */
	public ArrayList<Task> getLastRedoFloatingState() {
		return floatingRedoStack.pop();
	}

	/**
	 * Get the entire user-entered command of the action that is going to be undone.
	 * 
	 * @return String of the entire command.
	 */
	public String getLastCommand() {
		return undoCommands.remove(undoCommands.size() - 1);
	}

	/**
	 * Get the entire user-entered command of the action that is going to be redone.
	 * 
	 * @return String of the entire command.
	 */
	public String getRedoneCommand() {
		return redoCommands.remove(redoCommands.size() - 1);
	}

	/**
	 * Get the number of stored commands that can be undone.
	 * 
	 * @return Number of undo-able commands.
	 */
	public int getHistoryCount() {
		return undoCommands.size();
	}


	/**
	 * Get the number of stored commands that can be redone.
	 * 
	 * @return Number of redo-able commands.
	 */
	public int getRedoCount() {
		return redoCommands.size();
	}

	/**
	 * Undo a command by replacing the current storage arraylists with the arraylists copied from the previous program state.
	 * It will first copy and store current program state and the command input into the redo stack before proceeding. 
	 * Does not proceed if there are no commands to undo.
	 * 
	 * @return Feedback on method outcome.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String undo() throws ClassNotFoundException, IOException {
		if (undoCommands.isEmpty()) {
			return MSG_NO_UNDO_COMMAND;
		}
		
		String lastCommand = getLastCommand();

		copyCurrentTasksState();	
		storeCurrentStateForRedo(lastCommand);

		localStorageObject.revertToPreviousState(getLastCompletedState(), getLastUnompletedState(), getLastFloatingState());
		return "\"" + lastCommand + "\"" + CONFIRMATION_UNDO;
	}

	/**
	 * Redo a command by replacing the current storage arraylists with the arraylists copied from the previous program state.
	 * It will first copy and store current program state and the command input into the undo stack before proceeding. 
	 * Does not proceed if there are no commands to redo.
	 * 
	 * @return Feedback on method outcome.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String redo() throws ClassNotFoundException, IOException {
		if (redoCommands.isEmpty()) {
			return MSG_NO_REDO_COMMAND;
		}
		
		String redoneCommand = getRedoneCommand();

		copyCurrentTasksState();
		storePreviousState(redoneCommand); // store current "snapshots" for undo purposes

		localStorageObject.revertToPreviousState(getLastRedoCompletedState(), getLastRedoUnompletedState(), getLastRedoFloatingState());
		return "\"" + redoneCommand + "\"" + CONFIRMATION_REDO;
	}

	/**
	 * Create fresh copies of all the storage arraylists.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void copyCurrentTasksState() throws ClassNotFoundException, IOException {
		uncompletedTasksSnapshot = copyArrayList(localStorageObject.getUncompletedTasks());
		completedTasksSnapshot = copyArrayList(localStorageObject.getCompletedTasks());
		floatingTasksSnapshot = copyArrayList(localStorageObject.getFloatingTasks());
	}

	/**
	 * Creates a fresh copy of an arraylist and its task contents such that both of them do not share any references.
	 * 
	 * @param sourceArray The arraylist to be copied from.
	 * @return            The copied arraylist.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static ArrayList<Task> copyArrayList(ArrayList<Task> sourceArray) throws IOException, ClassNotFoundException {	
		ArrayList<Task> CopyOfArraylist = new ArrayList<Task>(sourceArray.size());

		for (Task t : sourceArray) {
			// convert each task to bytes
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(t);
			oos.flush();
			oos.close();
			bos.close();
			byte[] byteData = bos.toByteArray();

			// restore task from bytes and add to the new arraylist
			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			Task tempTask = (Task) new ObjectInputStream(bais).readObject();
			CopyOfArraylist.add(tempTask);
		}
		return CopyOfArraylist;
	}

	/**
	 * Store all current copied storage arraylists into undo stack to be used for undoing the current command.
	 * Also stores the current command string to be used as feedback when undo is performed.
	 * 
	 * @param command The entire command input entered by the user.
	 */
	public void storePreviousState(String command) {
		completedStack.push(completedTasksSnapshot);
		uncompletedStack.push(uncompletedTasksSnapshot);
		floatingStack.push(floatingTasksSnapshot);
		undoCommands.add(command);
	}

	/**
	 * Store all current copied storage arraylists into redo stack to be used for redoing the current command.
	 * Also stores the current command string to be used as feedback when redo is performed.
	 * 
	 * @param command The entire command input that is currently being undone.
	 */
	private void storeCurrentStateForRedo(String command) {
		completedRedoStack.push(completedTasksSnapshot);
		uncompletedRedoStack.push(uncompletedTasksSnapshot);
		floatingRedoStack.push(floatingTasksSnapshot);
		redoCommands.add(command);
	}

	/**
	 * Empties the redo command arraylist
	 */
	public void clearRedoCommands() {
		redoCommands.clear();
	}
	
	// Helper method for testing
	public void testFunction() throws ClassNotFoundException, IOException {
		copyCurrentTasksState();	
		storeCurrentStateForRedo("Add Buy eggs ` on Friday");
	}
}
```
###### main\Parser\Natty.java
``` java

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
```
###### main\Storage\LocalStorage.java
``` java
	/**
	 * Function to replace the current tasks arraylists with the given arraylists, to "undo" to the previous state.
	 * 
	 * @param previousCompleted   the old list of completed tasks.
	 * @param previousUnCompleted the old list of uncompleted tasks.
	 * @param previousFloating    the old list of floating tasks.
	 */
	public void revertToPreviousState(ArrayList<Task> previousCompleted, ArrayList<Task> previousUncompleted,
			                          ArrayList<Task> previousFloating) {
		completedTasks = previousCompleted;
		uncompletedTasks = previousUncompleted;
		floatingTasks = previousFloating;
	}
}
```
###### main\Task\Task.java
``` java
package Task;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Parser.Natty;

public class Task implements java.io.Serializable {
	private String issue;
	private String msg;
	private ArrayList<String> label;
	private boolean isCompleted, hasTime;
	private Calendar startDate, endDate;
	private String priority;
	private  String lastDate = "-";
	private  int frequency = -1;
	private static final String[] NAMES_OF_MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	private String dateCompare;
	private String fixedStartDate;
	private String id ="";
	private static final String MSG_RECURSE_FREQUENCY = "(Recurs every ";
	private static final String MSG_DAYS = " day(s))";
	private static final String PRIORITY_HIGH = "high"; 
	private static final String PRIORITY_HIGH_SHORT = "(H)";
	private static final String PRIORITY_MEDIUM = "medium";
	private static final String PRIORITY_MEDIUM_SHORT = "(M)";
//	private static final String PRIORITY_LOW = "low";
	private static final String PRIORITY_LOW_SHORT = "(L)";

	// Constructors

	// Constructor for dateless tasks
	public Task(String issue) {
		assert issue != null;
		this.msg = issue;
		this.issue = issue;
		isCompleted = false;
		hasTime = false;
		label = new ArrayList<String>();
		startDate = null;
		endDate = null;
		priority = "low";
	}

	// Constructor for tasks with only one date given
	public Task(String issue, String date, String msg, boolean isStartDate) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert date.contains("/");
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		label = new ArrayList<String>();
		priority = "low";
		String[] splitDates = date.split("/");
		int year = Integer.parseInt(splitDates[2]);
		int month = Integer.parseInt(splitDates[1])-1;
		int day = Integer.parseInt(splitDates[0]);
		int hour = 0;
		int minute = 0;
		if (splitDates.length > 3) { // given date includes time
			hour = Integer.parseInt(splitDates[3]);
			minute = Integer.parseInt(splitDates[4]);
		}
		if (isStartDate) { // the date provided is a start date
			if (splitDates.length > 3) { // has time
				hasTime = true;
				startDate = new GregorianCalendar(year, month, day, hour, minute);
			} else { // does not have time
				startDate = new GregorianCalendar(year, month, day);
			}
			endDate = null;
		} else { // the date provided is an end date
			startDate = null;
			if (splitDates.length > 3) { // has time
				hasTime = true;
				endDate = new GregorianCalendar(year, month, day, hour, minute);
			} else { // no time given
				endDate = new GregorianCalendar(year, month, day);
			}
		}
	}

	// Constructor for tasks with start and end dates given
	public Task(String issue, String startDate, String endDate, String msg) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert startDate.contains("/");
		assert endDate.contains("/");
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		label = new ArrayList<String>();
		priority = "low";
		String[] splitStartDate = startDate.split("/");
		int year = Integer.parseInt(splitStartDate[2]);
		int month = Integer.parseInt(splitStartDate[1])-1;
		int day = Integer.parseInt(splitStartDate[0]);
		int hour = 0;
		int minute = 0;
		if (splitStartDate.length > 3) { // given start date includes time
			hasTime = true;
			hour = Integer.parseInt(splitStartDate[3]);
			minute = Integer.parseInt(splitStartDate[4]);
			this.startDate = new GregorianCalendar(year, month, day, hour, minute);
		} else { // given start date does not have time
			this.startDate = new GregorianCalendar(year, month, day);
		}

		String[] splitEndDate = endDate.split("/");
		year = Integer.parseInt(splitEndDate[2]);
		month = Integer.parseInt(splitEndDate[1])-1;
		day = Integer.parseInt(splitEndDate[0]);
		if (splitEndDate.length > 3) { // given end date includes time
			hasTime = true;
			hour = Integer.parseInt(splitEndDate[3]);
			minute = Integer.parseInt(splitEndDate[4]);
			this.endDate = new GregorianCalendar(year, month, day, hour, minute);
		} else { // given end date has not time
			this.endDate = new GregorianCalendar(year, month, day);
		}
	}
	// Constructor for recurring tasks with only one date given
	public Task(String issue, String date, String msg, boolean isStartDate,int f,String last) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert date.contains("/");
		Calendar CalendarID = Calendar.getInstance();
		id = String.valueOf(CalendarID.getTimeInMillis());
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		frequency = f;
		lastDate = last;
		dateCompare = date;
		label = new ArrayList<String>();
		priority = "low";
		String[] splitDates = date.split("/");
		int year = Integer.parseInt(splitDates[2]);
		int month = Integer.parseInt(splitDates[1])-1;
		int day = Integer.parseInt(splitDates[0]);
		int hour = 0;
		int minute = 0;
		if (splitDates.length > 3) { // given date includes time
			hour = Integer.parseInt(splitDates[3]);
			minute = Integer.parseInt(splitDates[4]);

		}
		if (isStartDate) { // the date provided is a start date
			fixedStartDate  = date;
			if (splitDates.length > 3) { // has time
				hasTime = true;
				startDate = new GregorianCalendar(year, month, day, hour, minute);
			} else { // does not have time
				startDate = new GregorianCalendar(year, month, day);
			}
			endDate = null;
		} else { // the date provided is an end date
			startDate = null;
			if (splitDates.length > 3) { // has time
				hasTime = true;
				endDate = new GregorianCalendar(year, month, day, hour, minute);
			} else { // no time given
				endDate = new GregorianCalendar(year, month, day);
			}
		}
	}
	// Constructor for recurring tasks with start and end dates given
	public Task(String issue, String startDate, String endDate, String msg,int f, String last) { // assuming String date provided is of the format DD/MM/YYYY
		assert issue != null;
		assert startDate.contains("/");
		assert endDate.contains("/");
		Calendar CalendarID = Calendar.getInstance();
		id = String.valueOf(CalendarID.getTimeInMillis());
		this.msg=msg;
		this.issue = issue;
		isCompleted = false;
		frequency = f;
		lastDate = last;
		dateCompare = endDate;
		fixedStartDate  = startDate;
		label = new ArrayList<String>();
		priority = "low";
		String[] splitStartDate = startDate.split("/");
		int year = Integer.parseInt(splitStartDate[2]);
		int month = Integer.parseInt(splitStartDate[1])-1;
		int day = Integer.parseInt(splitStartDate[0]);
		int hour = 0;
		int minute = 0;
		if (splitStartDate.length > 3) { // given start date includes time
			hasTime = true;
			hour = Integer.parseInt(splitStartDate[3]);
			minute = Integer.parseInt(splitStartDate[4]);

			this.startDate = new GregorianCalendar(year, month, day, hour, minute);
		} else { // given start date does not have time
			this.startDate = new GregorianCalendar(year, month, day);
		}

		String[] splitEndDate = endDate.split("/");
		year = Integer.parseInt(splitEndDate[2]);
		month = Integer.parseInt(splitEndDate[1])-1;
		day = Integer.parseInt(splitEndDate[0]);
		if (splitEndDate.length > 3) { // given end date includes time
			hasTime = true;
			hour = Integer.parseInt(splitEndDate[3]);
			minute = Integer.parseInt(splitEndDate[4]);
			this.endDate = new GregorianCalendar(year, month, day, hour, minute);
		} else { // given end date has not time
			this.endDate = new GregorianCalendar(year, month, day);
		}
	}
	
	// Setter Methods
	public void setIssue(String issue) {
		assert issue != null;
		this.issue = issue;
	}
	public void setDescription(String msg){
		this.msg = msg;
	}
	public void setLabel(String label) {
		assert label != null;
		if (!this.label.contains(label)) { // prevent duplicate tag from being added
			this.label.add(label);
		}
	}

	public void removeLabel(String label) {
		assert label != null;
		this.label.remove(label);
	}

	public void setComplete() {
		isCompleted = true;
	}

	public void setUncomplete() {
		isCompleted = false;
	}
	public void setLastDate(String s) {
		lastDate = s;
	}
	public void setFrequency(int n) {
		frequency = n;
	}
	public void setID(String s) {
		id = s;
	}

	public void resetID() {
		id = "";
	}

	public void setStartDate(String startDate) {
		if (startDate == null) {
			hasTime = false;
			this.startDate = null;
		} else {
			String[] splitStartDate = startDate.split("/");
			int year = Integer.parseInt(splitStartDate[2]);
			int month = Integer.parseInt(splitStartDate[1])-1;
			int day = Integer.parseInt(splitStartDate[0]);
			int hour = 0;
			int minute = 0;
			if (splitStartDate.length > 3) { // given date includes time
				hasTime = true;
				hour = Integer.parseInt(splitStartDate[3]);
				minute = Integer.parseInt(splitStartDate[4]);
				this.startDate = new GregorianCalendar(year, month, day, hour, minute);
			} else {
				hasTime = false;
				this.startDate = new GregorianCalendar(year, month, day);
			}
		}
	}

	public void setEndDate(String endDate) {
		if (endDate == null) {
			hasTime = false;
			this.endDate = null;
		} else {
			String[] splitEndDate = endDate.split("/");
			int year = Integer.parseInt(splitEndDate[2]);
			int month = Integer.parseInt(splitEndDate[1])-1;
			int day = Integer.parseInt(splitEndDate[0]);
			int hour = 0;
			int minute = 0;
			if (splitEndDate.length > 3) { // given date includes time
				hasTime = true;
				hour = Integer.parseInt(splitEndDate[3]);
				minute = Integer.parseInt(splitEndDate[4]);
				this.endDate = new GregorianCalendar(year, month, day, hour, minute);
			} else {
				hasTime = false;
				this.endDate = new GregorianCalendar(year, month, day);
			}
		}
	}

	// Getter Methods
	public String getIssue() {
		return issue;
	}
	public String getDescription(){
		return msg;
	}

	public ArrayList<String> getLabel() {
		return label;
	}

	public boolean getCompletedStatus() {
		return isCompleted;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}
	public String getLastDate() {
		return lastDate;
	}
	public int getFrequency() {
		return frequency;
	}

	public String getMsg() {
		return msg;
	}
	public String getStartDateString() {
		if (startDate != null) {

			String result = startDate.get(Calendar.DAY_OF_MONTH) + " ";
			if (result.length() == 2) { // adds 0 to single digit dates
				result = "0" + result;
			}
			int month = startDate.get(Calendar.MONTH);
			result += NAMES_OF_MONTHS[month] + " ";

			int year = startDate.get(Calendar.YEAR);
			result += year;			

			String dayName = Natty.getInstance().getDayName(result); // get the name of the day, eg Sun, Mon
			result = Natty.getInstance().tryChangeTodayOrTomorrow(result); // check if is today or tomorrow and change accordingly

			result += " " + dayName;

			if (hasTime) {
				int hour =startDate.get(Calendar.HOUR_OF_DAY);
				if (hour > 12) {
					hour -= 12; // 24hr to 12hr format
				}

				if (hour < 10) {
					result += " " + "0" + hour;
				} else {
					result += " " + hour;
				}

				String minute = Integer.toString(startDate.get(Calendar.MINUTE));
				if (minute.length() == 1) {
					minute = "0" + minute;
				}
				if (startDate.get(Calendar.HOUR_OF_DAY) < 12) {
					result += ":" + minute + "AM" + "\t";
				} else {
					result += ":" + minute + "PM" + "\t";
				}
			}else{
				result += "\t";
			}
			return result;

		} else { // return empty string if the task has no start date
			return "\t\t";
		}
	}

	public String getEndDateString() {
		if (endDate != null) {
			String day = Integer.toString(endDate.get(Calendar.DAY_OF_MONTH));

			String result = day + " ";
			if (result.length() == 2) { // adds 0 to single digit dates
				result = "0" + result;
			}
			int month = endDate.get(Calendar.MONTH);
			result += NAMES_OF_MONTHS[month] + " ";

			int year = endDate.get(Calendar.YEAR);
			result += year;

			String dayName = Natty.getInstance().getDayName(result); // get the name of the day, eg Sun, Mon
			result = Natty.getInstance().tryChangeTodayOrTomorrow(result); // check if is today or tomorrow and change accordingly

			result += " " + dayName;

			if (hasTime) {
				int hour = endDate.get(Calendar.HOUR_OF_DAY);
				if (hour > 12) {
					hour -= 12; // 24hr to 12hr format
				}

				if (hour < 10) {
					result += " " + "0" + hour;
				} else {
					result += " " + hour;
				}

				String minute = Integer.toString(endDate.get(Calendar.MINUTE));
				if (minute.length() == 1) {
					minute = "0" + minute;
				}
				if (endDate.get(Calendar.HOUR_OF_DAY) < 12) {
					result += ":" + minute + "AM" + "\t";
				} else {
					result += ":" + minute + "PM" + "\t";
				}
				//				result += ":" + minute + "\t";
			}else{
				result += "\t";
			}
			return result;
		} else { // return empty string if the task has no end date
			return "\t\t";
		}
	}

	public String getDateCompare() {
		return dateCompare;
	}
	public String getFixedStartDateString() {
		return fixedStartDate;
	}
	public String getId() {
		return id;
	}
	public String getPriority(){
		return priority;
	}
	
	// Method to get (H) for task with high priority, (M) for medium priority, (L) for low priority
	public String getShortPriority() {
		if (priority.equals(PRIORITY_HIGH)) {
			return PRIORITY_HIGH_SHORT;
		} else if (priority.equals(PRIORITY_MEDIUM)) {
			return PRIORITY_MEDIUM_SHORT;
		} else {
			return PRIORITY_LOW_SHORT;
		}
	}

	public void setPriority(String priority){
		this.priority = priority;
	}
	// Returns date in string format of DD/MM/YYYY
	public String getTaskString() {
		return getStartDateString() +  getEndDateString() +issue;
		/*if (startDate == null && endDate == null) {
			return issue;
		} else {
			if (startDate == null) {
				return " - " + getEndDateString() + " " + issue;
			} else if (endDate == null) {
				return getStartDateString() + " -" + " " + issue  ;
			}  else {
				return getStartDateString() + " " + getEndDateString() + " " + issue;
			}
		}*/
	}

	public String getStartDateLineOne() { // Method to return DD MTH YYYY
		if (startDate == null) {
			return null;
		} else {
			String result = startDate.get(Calendar.DAY_OF_MONTH) + " ";
			if (result.length() == 2) { // adds 0 to single digit dates
				result = "0" + result;
			}
			int month = startDate.get(Calendar.MONTH);
			result += NAMES_OF_MONTHS[month] + " ";

			int year = startDate.get(Calendar.YEAR);
			result += year;	
			result = Natty.getInstance().tryChangeTodayOrTomorrow(result); // check if is today or tomorrow and change accordingly
			return result;
		}
	}

	public String getStartDateLineTwo() { // Method to return DAY or DAY HH:MMam
		if (startDate == null) {
			return null;
		} else {
			String result = getStartDateLineOne();
			result = Natty.getInstance().getDayName(result); // get the name of the day, eg Sun, Mon

			if (hasTime) { // if start date has time
				int hour = startDate.get(Calendar.HOUR_OF_DAY);
				if (hour > 12) {
					hour -= 12; // 24hr to 12hr format
				}

				if (hour < 10) {
					result += " " + "0" + hour;
				} else {
					result += " " + hour;
				}

				String minute = Integer.toString(startDate.get(Calendar.MINUTE));
				if (minute.length() == 1) {
					minute = "0" + minute;
				}
				if (startDate.get(Calendar.HOUR_OF_DAY) < 12) {
					result += ":" + minute + "AM" + "\t";
				} else {
					result += ":" + minute + "PM" + "\t";
				}
			} else {
				result += "\t";
			}
			return result;
		}
	}
	
	public String getEndDateLineOne() { // Method to return DD MTH YYYY
		if (endDate == null) {
			return null;
		} else {
			String result = endDate.get(Calendar.DAY_OF_MONTH) + " ";
			if (result.length() == 2) { // adds 0 to single digit dates
				result = "0" + result;
			}
			int month = endDate.get(Calendar.MONTH);
			result += NAMES_OF_MONTHS[month] + " ";

			int year = endDate.get(Calendar.YEAR);
			result += year;	
			result = Natty.getInstance().tryChangeTodayOrTomorrow(result); // check if is today or tomorrow and change accordingly
			return result;
		}
	}

	public String getEndDateLineTwo() { // Method to return DAY or DAY HH:MMam
		if (endDate == null) {
			return null;
		} else {
			String result = getEndDateLineOne();
			result = Natty.getInstance().getDayName(result); // get the name of the day, eg Sun, Mon

			if (hasTime) { // if start date has time
				int hour = endDate.get(Calendar.HOUR_OF_DAY);
				if (hour > 12) {
					hour -= 12; // 24hr to 12hr format
				}

				if (hour < 10) {
					result += " " + "0" + hour;
				} else {
					result += " " + hour;
				}

				String minute = Integer.toString(endDate.get(Calendar.MINUTE));
				if (minute.length() == 1) {
					minute = "0" + minute;
				}
				if (endDate.get(Calendar.HOUR_OF_DAY) < 12) {
					result += ":" + minute + "AM" + "\t";
				} else {
					result += ":" + minute + "PM" + "\t";
				}
			} else {
				result += "\t";
			}
			return result;
		}
	}
	
	public String getRecurFrequency() {
		if (frequency < 1) { // if not recurring task
			return "";
		}
		return MSG_RECURSE_FREQUENCY + frequency + MSG_DAYS;
	}
}
```
###### main\unitTest\TaskTest.java
``` java
package unitTest;

import static org.junit.Assert.*;

import org.junit.Test;

import Task.Task;

public class TaskTest {

	/**
	 * Equivalence partitions:
	 * Valid - [any non-null string]
	 * Invalid - [null] 
	 */
	@Test
	public void testTaskNoDate() {
		String result = "";
		Task testTask1 = new Task("test sample 1"); // valid input.
		Task testTask2 = new Task(""); // boundary case for non-null string.
		try {
			Task testTask3 = new Task(null); // invalid input and boundary case for [null] string, assertion error expected.
		} catch (AssertionError e) {
			result = "";
		}
		assertEquals(result, "");
	}

	/**
	 * Equivalence partitions:
	 * Valid - [any non-null string]
	 * Invalid - [null] 
	 * 
	 */
	@Test
	public void testSetIssue() {
		Task testTask1 = new Task("test sample 1");
		assertEquals("test sample 1", testTask1.getIssue());
		testTask1.setIssue("new issue");
		assertEquals("new issue", testTask1.getIssue());
		testTask1.setIssue(""); // boundary case for non-null string.
		assertEquals("", testTask1.getIssue());
		String result = "";
		try {
			testTask1.setIssue(null); // invalid input and boundary case for [null] string, assertion error expected.
		} catch (AssertionError e) {
			result = "null assertion";
		}
		assertEquals(result, "");
	}
}
```
###### main\unitTest\UndoTest.java
``` java
package unitTest;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import Logic.Undo;

public class UndoTest {

	private static Undo undoObject = Undo.getInstance();

	@Test
	public void testInitialEmptyUndoAndRedo() {
		// Wipe redo stack in case any other test methods were run before this
		undoObject.clearRedoCommands();

		// Check for zero initial undo-able commands
		assertEquals(0, undoObject.getHistoryCount());

		// Check for zero initial redo-able commands
		System.out.println(undoObject.getRedoCount());
		assertEquals(0, undoObject.getRedoCount());
	}

	@Test
	public void testAddingToUndoStack() throws ClassNotFoundException, IOException {
		// Simulate running an add task command
		undoObject.storePreviousState("Add Buy eggs ` on Friday");

		// Check for one undo-able commands that was just added
		assertEquals(1, undoObject.getHistoryCount());
	}

	@Test
	public void testUndo() throws ClassNotFoundException, IOException {
		// Check for correct retrieval of the undo command name
		String undoneCommand = undoObject.getLastCommand();
		assertEquals("Add Buy eggs ` on Friday", undoneCommand);

		// Check for no undo-able commands after pseudo-undo
		assertEquals(0, undoObject.getHistoryCount());
	}

	@Test
	public void testRedo() throws ClassNotFoundException, IOException {
		// Helper method to simulate undo (and thus adding of command to redo stack)
		undoObject.testFunction();

		// Check for correct retrieval of the undo command name
		String redoneCommand = undoObject.getRedoneCommand();
		assertEquals("Add Buy eggs ` on Friday", redoneCommand);

		// Check for one redo-able commands after pseudo-undo
		undoObject.testFunction();
		assertEquals(1, undoObject.getRedoCount());
	}
}
```
