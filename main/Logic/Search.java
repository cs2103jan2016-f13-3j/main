//@@author Kowshik
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
		UI.ui.eraseScreen();
		searchedTasks = new ArrayList<Task>();
		String[] searchKeywords = keyword.split(" ");
		int counter = 0;
		ArrayList<Task> temp = Storage.localStorage.getUncompletedTasks();

		if(temp.size() > 0) {
			for(int i = 0; i<temp.size(); i++) {
				for(int j = 0; j<searchKeywords.length; j++) {
					if(temp.get(i).getIssue().toLowerCase().contains(searchKeywords[j].toLowerCase()) || 
							temp.get(i).getTaskString().toLowerCase().contains(searchKeywords[j].toLowerCase())) {
						searchedTasks.add(temp.get(i));
						break;
					}
				}
			}

			UI.ui.printGreen("UNCOMPLETED TASKS");
			UI.ui.printGreen("Index\tStart Date\tEnd Date\tTask");
			for(int i = 0; i<searchedTasks.size(); i++) {
				Task temp1 = searchedTasks.get(i);
				UI.ui.printTask(i, temp1.getStartDateString(), temp1.getEndDateString(), temp1.getIssue());

				counter++;
			}
			
			UI.ui.print("________________________________________________________________");
			UI.ui.print("\n");
		}

		temp = Storage.localStorage.getFloatingTasks();
		if(temp.size() > 0) {

			UI.ui.printGreen("FLOATING TASKS");
			UI.ui.printGreen("Index\tTask");
			for(int i = 0; i<temp.size(); i++) {
				for(int j = 0; j<searchKeywords.length; j++) {
					if(temp.get(i).getIssue().toLowerCase().contains(searchKeywords[j].toLowerCase()) || 
							temp.get(i).getTaskString().toLowerCase().contains(searchKeywords[j].toLowerCase())) {
						searchedTasks.add(temp.get(i));
						break;
					}
				}
			}
		}

		for(int i = counter; i<searchedTasks.size(); i++) {
			Task temp1 = searchedTasks.get(i);
			UI.ui.printTask(i, temp1.getStartDateString(), temp1.getEndDateString(), temp1.getIssue());
			counter++;
		}

		temp = Storage.localStorage.getCompletedTasks();
		if(temp.size() > 0) {
			UI.ui.print("________________________________________________________________");
			UI.ui.print("\n");
			UI.ui.printGreen("COMPLETED TASKS");
			UI.ui.printGreen("Index\tTask");
			for(int i = 0; i<temp.size(); i++) {
				for(int j = 0; j<searchKeywords.length; j++) {
					if(temp.get(i).getIssue().toLowerCase().contains(searchKeywords[j].toLowerCase()) || 
							temp.get(i).getTaskString().toLowerCase().contains(searchKeywords[j].toLowerCase())) {
						searchedTasks.add(temp.get(i));
						break;
					}
				}
			}

			for(int i = counter; i<searchedTasks.size(); i++) {
				Task temp1 = searchedTasks.get(i);
				UI.ui.printTask(i, temp1.getStartDateString(), temp1.getEndDateString(), temp1.getIssue());

			}
		}
	}
}
