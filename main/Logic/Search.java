//@@author Kowshik
package Logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Storage.LocalStorage;
import Task.Task;
import UI.UI;

public class Search {

	private static ArrayList<Task> searchedTasks;
	private static LocalStorage localStorageObject = LocalStorage.getInstance();
	private static UI uiObject = new UI();

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
		uiObject.eraseScreen();
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
				uiObject.printGreen("UNCOMPLETED TASKS");
				uiObject.printGreen("Index\tStart Date\tEnd Date\tTask");
				for (int i = 0; i < searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					uiObject.printTask1(i, temp1.getStartDateLineOne(), temp1.getStartDateLineTwo(),
							temp1.getEndDateLineOne(), temp1.getEndDateLineTwo(), temp1.getIssue(),
							temp1.getRecurFrequency());

					counter++;
				}

				uiObject.print("________________________________________________________________");
				uiObject.print("\n");
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
				uiObject.printGreen("FLOATING TASKS");
				uiObject.printGreen("Index\tTask");
				for (int i = counter; i < searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					uiObject.printYellow((i + 1) + ".\t" + temp1.getIssue());
					counter++;
				}

				uiObject.print("________________________________________________________________");
				uiObject.print("\n");
			}
		}

		/*
		 * temp = localStorageObject.getCompletedTasks(); if(temp.size() > 0) {
		 * for(int i = 0; i<temp.size(); i++) { String[] taskParts =
		 * temp.get(i).getIssue().split(" "); for(int j = 0; j<taskParts.length;
		 * j++) { if(taskParts[j].trim().equals(search)) {
		 * searchedTasks.add(temp.get(i)); break; } } }
		 * 
		 * if(searchedTasks.size() > counter) { uiObject.printGreen(
		 * "COMPLETED TASKS"); uiObject.printGreen("Index\tTask"); for(int i =
		 * counter; i<searchedTasks.size(); i++) { Task temp1 =
		 * searchedTasks.get(i);
		 * uiObject.printTask1(i,temp1.getStartDateLineOne(),temp1.
		 * getStartDateLineTwo(),
		 * temp1.getEndDateLineOne(),temp1.getEndDateLineTwo(),temp1.getIssue(),
		 * temp1.getRecurFrequency());
		 * 
		 * } } }
		 */
		if (searchedTasks.size() == 0) {
			uiObject.printRed("NO TASKS FOUND");
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
				uiObject.printGreen("UNCOMPLETED TASKS");
				uiObject.printGreen("Index\tStart Date\tEnd Date\tTask");
				for (int i = 0; i < searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					uiObject.printTask1(i, temp1.getStartDateLineOne(), temp1.getStartDateLineTwo(),
							temp1.getEndDateLineOne(), temp1.getEndDateLineTwo(), temp1.getIssue(),
							temp1.getRecurFrequency());
					counter++;
				}

				uiObject.print("________________________________________________________________");
				uiObject.print("\n");
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
				uiObject.printGreen("FLOATING TASKS");
				uiObject.printGreen("Index\tTask");
				for (int i = counter; i < searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					uiObject.printYellow((i + 1) + ".\t" + temp1.getIssue());
					counter++;
				}

				uiObject.print("________________________________________________________________");
				uiObject.print("\n");
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
				uiObject.printGreen("COMPLETED TASKS");
				uiObject.printGreen("Index\tTask");
				for (int i = counter; i < searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					uiObject.printTask(i, temp1.getStartDateString(), temp1.getEndDateString(), temp1.getIssue());

				}
			}
		}

		if (searchedTasks.size() == 0) {
			uiObject.printRed("NO TASKS FOUND");
		}
	}

	public static boolean isContainsKeyword(String[] searchKeywords, ArrayList<Task> temp, int i, int j) {
		return temp.get(i).getIssue().toLowerCase().contains(searchKeywords[j].toLowerCase())
				|| temp.get(i).getTaskString().toLowerCase().contains(searchKeywords[j].toLowerCase());
	}
}
