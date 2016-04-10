//@@author Kowshik
package Logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Storage.LocalStorage;
import Task.Task;

public class Search {

	private static ArrayList<Task> searchedTasks;
	private static LocalStorage localStorageObject = LocalStorage.getInstance();

	public static ArrayList<Task> getSearchedTasks() {
		return searchedTasks;

	}

	public static Task getSearchedTask(int index) {
		return searchedTasks.get(index);

	}

	/**
	 * Function to search task according to keyword in list of uncompleted tasks
	 * 
	 * @param keyword
	 *            the string to be searched for in the list of tasks
	 */
	public static void searchTasksByKeyword(String keyword) {
		UI.UI.eraseScreen();
		searchedTasks = new ArrayList<Task>();
		String[] searchKeywords = keyword.split(" ");

		int counter = 0;
		ArrayList<Task> temp = localStorageObject.getUncompletedTasks();

		if ((searchKeywords.length == 1) && (searchKeywords[0].length() == 1)) {
			searchSingleLetter(searchKeywords, counter, temp);
		} else {
			searchPhrase(searchKeywords, counter, temp);
		}
	}

	public static void searchSingleLetter(String[] searchKeywords, int counter, ArrayList<Task> temp) {
		String search = searchKeywords[0];
		if (temp.size() > 0) {
			for (int i = 0; i < temp.size(); i++) {
				String[] taskParts = temp.get(i).getIssue().split(" ");
				for (int j = 0; j < taskParts.length; j++) {
					if (taskParts[j].trim().equals(search)) {
						searchedTasks.add(temp.get(i));
						break;
					}
				}
			}

			if (searchedTasks.size() > 0) {
				UI.UI.printGreen("UNCOMPLETED TASKS");
				UI.UI.printGreen("Index\tStart Date\tEnd Date\tTask");
				for (int i = 0; i < searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					UI.UI.printTask1(i, temp1.getStartDateLineOne(), temp1.getStartDateLineTwo(),
							temp1.getEndDateLineOne(), temp1.getEndDateLineTwo(), temp1.getIssue(),
							temp1.getRecurFrequency());

					counter++;
				}

				UI.UI.print("________________________________________________________________");
				UI.UI.print("\n");
			}
		}

		temp = localStorageObject.getFloatingTasks();
		if (temp.size() > 0) {
			for (int i = 0; i < temp.size(); i++) {
				String[] taskParts = temp.get(i).getIssue().split(" ");
				for (int j = 0; j < taskParts.length; j++) {
					if (taskParts[j].trim().equals(search)) {
						searchedTasks.add(temp.get(i));
						break;
					}
				}
			}

			if (searchedTasks.size() > counter) {
				UI.UI.printGreen("FLOATING TASKS");
				UI.UI.printGreen("Index\tTask");
				for (int i = counter; i < searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					UI.UI.printYellow((i + 1) + ".\t" + temp1.getIssue());
					counter++;
				}

				UI.UI.print("________________________________________________________________");
				UI.UI.print("\n");
			}
		}

		/*
		 * temp = localStorageObject.getCompletedTasks(); if(temp.size() > 0) {
		 * for(int i = 0; i<temp.size(); i++) { String[] taskParts =
		 * temp.get(i).getIssue().split(" "); for(int j = 0; j<taskParts.length;
		 * j++) { if(taskParts[j].trim().equals(search)) {
		 * searchedTasks.add(temp.get(i)); break; } } }
		 * 
		 * if(searchedTasks.size() > counter) { UI.ui.printGreen(
		 * "COMPLETED TASKS"); UI.ui.printGreen("Index\tTask"); for(int i =
		 * counter; i<searchedTasks.size(); i++) { Task temp1 =
		 * searchedTasks.get(i);
		 * UI.ui.printTask1(i,temp1.getStartDateLineOne(),temp1.
		 * getStartDateLineTwo(),
		 * temp1.getEndDateLineOne(),temp1.getEndDateLineTwo(),temp1.getIssue(),
		 * temp1.getRecurFrequency());
		 * 
		 * } } }
		 */
		if (searchedTasks.size() == 0) {
			UI.UI.printRed("NO TASKS FOUND");
		}
	}

	public static void searchPhrase(String[] searchKeywords, int counter, ArrayList<Task> temp) {

		if (temp.size() > 0) {
			for (int i = 0; i < temp.size(); i++) {
				boolean isSuccess = true;
				for (int j = 0; j < searchKeywords.length; j++) {
					if (isContainsKeyword(searchKeywords, temp, i, j)) {

					} else {
						isSuccess = false;
						break;
					}
				}
				if (isSuccess) {
					searchedTasks.add(temp.get(i));
				}
			}

			if (searchedTasks.size() > 0) {
				UI.UI.printGreen("UNCOMPLETED TASKS");
				UI.UI.printGreen("Index\tStart Date\tEnd Date\tTask");
				for (int i = 0; i < searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					UI.UI.printTask1(i, temp1.getStartDateLineOne(), temp1.getStartDateLineTwo(),
							temp1.getEndDateLineOne(), temp1.getEndDateLineTwo(), temp1.getIssue(),
							temp1.getRecurFrequency());
					counter++;
				}

				UI.UI.print("________________________________________________________________");
				UI.UI.print("\n");
			}
		}

		temp = localStorageObject.getFloatingTasks();
		if (temp.size() > 0) {
			for (int i = 0; i < temp.size(); i++) {
				boolean isSuccess = true;
				for (int j = 0; j < searchKeywords.length; j++) {
					if (isContainsKeyword(searchKeywords, temp, i, j)) {
					} else {
						isSuccess = false;
						break;
					}
				}
				if (isSuccess) {
					searchedTasks.add(temp.get(i));
				}
			}

			if (searchedTasks.size() > counter) {
				UI.UI.printGreen("FLOATING TASKS");
				UI.UI.printGreen("Index\tTask");
				for (int i = counter; i < searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					UI.UI.printYellow((i + 1) + ".\t" + temp1.getIssue());
					counter++;
				}

				UI.UI.print("________________________________________________________________");
				UI.UI.print("\n");
			}
		}

		temp = localStorageObject.getCompletedTasks();
		if (temp.size() > 0) {
			for (int i = 0; i < temp.size(); i++) {
				boolean isSuccess = true;
				for (int j = 0; j < searchKeywords.length; j++) {
					if (isContainsKeyword(searchKeywords, temp, i, j)) {

					} else {
						isSuccess = false;
						break;
					}
				}
				if (isSuccess) {
					searchedTasks.add(temp.get(i));
				}
			}

			if (searchedTasks.size() > counter) {
				UI.UI.printGreen("COMPLETED TASKS");
				UI.UI.printGreen("Index\tTask");
				for (int i = counter; i < searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					UI.UI.printTask(i, temp1.getStartDateString(), temp1.getEndDateString(), temp1.getIssue());

				}
			}
		}

		if (searchedTasks.size() == 0) {
			UI.UI.printRed("NO TASKS FOUND");
		}
	}

	public static boolean isContainsKeyword(String[] searchKeywords, ArrayList<Task> temp, int i, int j) {
		return temp.get(i).getIssue().toLowerCase().contains(searchKeywords[j].toLowerCase())
				|| temp.get(i).getTaskString().toLowerCase().contains(searchKeywords[j].toLowerCase());
	}
}
