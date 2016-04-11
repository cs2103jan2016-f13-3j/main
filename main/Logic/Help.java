//@@author Jie Wei
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
