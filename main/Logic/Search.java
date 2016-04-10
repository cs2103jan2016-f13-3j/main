//@@author Kowshik
package Logic;

import java.util.ArrayList;

import Storage.LocalStorage;
import Task.Task;
import UI.UI;

public class Search {

	private static Search search;

	private static ArrayList<Task> searchedTasks;
	private static boolean isFloating;
	private static LocalStorage localStorageObject = LocalStorage.getInstance();
	private static UI uiObject = new UI();

	// Private constructor, following the singleton pattern.
	private Search() {
		searchedTasks = new ArrayList<Task>();
		isFloating = false;
	}

	/**
	 * Method to access this class, following the singleton pattern. 
	 * Invokes constructor if Search has not been initialised.
	 * 
	 * @return The Search object.
	 */
	public static Search getInstance() {
		if (search == null) {
			search = new Search();
		}
		return search;
	}

	// Getter methods.
	public ArrayList<Task> getSearchedTasks() {
		return searchedTasks;

	}

	public Task getSearchedTask(int index) {
		return searchedTasks.get(index);

	}

	/**
	 * Function to search task according to keyword in list of uncompleted tasks.
	 * 
	 * @param keyword the string to be searched for in the list of tasks.
	 */
	public void searchTasksByKeyword(String keyword) {
		searchedTasks = new ArrayList<Task>();
		String[] searchKeywords = keyword.split(" ");

		int counter = 0;
		ArrayList<Task> temp = new ArrayList<Task>();

		if ((searchKeywords.length == 1) && (searchKeywords[0].length() == 1)) {
			searchSingleLetter(searchKeywords, counter, temp);
		} else {
			searchPhrase(searchKeywords, counter, temp);
		}
	}

	/**
	 * Function to search tasks that have the single letter keyword entered by user.
	 * 
	 * @param searchKeywords the keyword entered by the user.
	 * @param counter        the index of the tasks.
	 * @param temp           the arraylist containing the tasks.
	 */
	public void searchSingleLetter(String[] searchKeywords, int counter, ArrayList<Task> temp) {
		String search = searchKeywords[0];

		temp = localStorageObject.getUncompletedTasks();
		isFloating = false;
		counter = checkIfSingleLetterMatches(counter, temp, search, isFloating);

		temp = localStorageObject.getFloatingTasks();
		isFloating = true;
		counter = checkIfSingleLetterMatches(counter, temp, search, isFloating);

		if (searchedTasks.size() == 0) {
			uiObject.printRed("NO TASKS FOUND");
		}
	}

	/**
	 * Function to search if the single letter entered by the user matches a word in the task list.
	 * 
	 * @param counter    the index of the tasks.
	 * @param temp       the arraylist containing the tasks.
	 * @param search     the letter to be searched for.
	 * @param isFloating to indicate if the arraylist is floating task arraylist or not.
	 * 
	 * @return           true if match found, false otherwise.
	 */
	public int checkIfSingleLetterMatches(int counter, ArrayList<Task> temp, String search, boolean isFloating) {
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
			counter = printSearchesFromUncompletedTasks(counter, isFloating);
		}
		return counter;
	}

	/**
	 * Function to print the search results.
	 * 
	 * @param counter    the index of the tasks.
	 * @param isFloating to indicate whether searched results are from floating tasks or not.
	 * 
	 * @return           the index of the tasks.
	 */
	public int printSearchesFromUncompletedTasks(int counter, boolean isFloating) {
		if (!isFloating) {
			if (searchedTasks.size() > counter) {
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
			return counter;
		} else {
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
			return counter;
		}
	}

	/**
	 * Function to search tasks that have matching phrase / keyword entered by the user.
	 * 
	 * @param searchKeywords the phrase/keyword to be searched for.
	 * @param counter        the index of the task list.
	 * @param temp           the arraylist containing the tasks.
	 */
	public void searchPhrase(String[] searchKeywords, int counter, ArrayList<Task> temp) {
		isFloating = false;
		temp = localStorageObject.getUncompletedTasks();
		counter = checkIfKeywordMatches(searchKeywords, counter, temp, isFloating);

		temp = localStorageObject.getFloatingTasks();
		isFloating = true;
		counter = checkIfKeywordMatches(searchKeywords, counter, temp, isFloating);

		if (searchedTasks.size() == 0) {
			uiObject.printRed("NO TASKS FOUND");
		}
	}

	/**
	 * Function to check if search keyword matches a word in task.
	 * 
	 * @param searchKeywords the keyword/phrase to be searched for.
	 * @param counter        the index of the tasks.
	 * @param temp           the arraylist containing the tasks.
	 * @param isFloating     to indicate if arraylist is floating task arraylist or not.
	 * @return
	 */
	public int checkIfKeywordMatches(String[] searchKeywords, int counter, ArrayList<Task> temp, boolean isFloating) {
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
			counter = printSearchesFromUncompletedTasks(counter, isFloating);
		}
		return counter;
	}

	/**
	 * Function to check if the task contains the given search keyword.
	 * 
	 * @param searchKeywords the keyword to be searched, for entered by the user.
	 * @param temp           the array list of tasks.
	 * @param i              the arraylist task index.
	 * @param j              the array index.
	 * 
	 * @return true if match is found, false otherwise.
	 */
	public boolean isContainsKeyword(String[] searchKeywords, ArrayList<Task> temp, int i, int j) {
		if (temp.get(i).getIssue().toLowerCase().contains(searchKeywords[j].toLowerCase())
			|| temp.get(i).getTaskString().toLowerCase().contains(searchKeywords[j].toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
}
