//@@author Kowshik
package Logic;

import java.util.ArrayList;
import java.util.Calendar;

import Storage.LocalStorage;
import Task.Task;
import UI.UI;

public class Notification {
	private static final int DAYS_WINDOW = 3;

	private ArrayList<Task> tasksToBeDisplayed;
	private LocalStorage localStorageObject;
	private UI uiObject;

	// Constructor.
	public Notification() {
		localStorageObject = LocalStorage.getInstance();
		tasksToBeDisplayed = new ArrayList<Task>();
		uiObject = new UI();
	}

	// Getter methods.
	public ArrayList<Task> getTasksToBeDisplayed() {
		return tasksToBeDisplayed;
	}

	public Task getSpecificTask(int index) throws IndexOutOfBoundsException {
		return tasksToBeDisplayed.get(index);
	}

	/**
	 * Function that prints the upcoming uncompleted tasks in the next three days.
	 */
	public void welcomeReminder() {
		// before daysInAdvance
		uiObject.printRed("DEADLINES APPROACHING - ");
		Calendar d1 = Calendar.getInstance();
		d1.add(Calendar.DAY_OF_MONTH, -DAYS_WINDOW);

		// after daysInAdvance
		Calendar d2 = Calendar.getInstance();
		d2.add(Calendar.DAY_OF_MONTH, 3);

		// today
		Calendar d3 = Calendar.getInstance();

		findRelevantTasks(d1, d2);
		printRelevantTasks(d3);
	}

	/**
	 * Function to find those tasks within the give window.
	 * 
	 * @param d1 the Calendar object of 3 days before current day.
	 * @param d2 the Calendar object of 3 days after current day.
	 */
	public void findRelevantTasks(Calendar d1, Calendar d2) {
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();
		for (Task temp : tempTasks) {
			if (temp.getEndDate() != null) {
				if ((temp.getEndDate().compareTo(d1) > 0) && (temp.getEndDate().compareTo(d2) <= 0)) {
					tasksToBeDisplayed.add(temp);
					continue;
				}
			} else if (temp.getStartDate() != null) {
				if ((temp.getStartDate().compareTo(d1) > 0) && (temp.getStartDate().compareTo(d2) <= 0)) {
					tasksToBeDisplayed.add(temp);
				}
			}
		}
	}
	
	/**
	 * Function to print the relevant tasks.
	 * 
	 * @param d3 the Calendar object of the current day.
	 */
	public void printRelevantTasks(Calendar d3) {
		if (tasksToBeDisplayed.size() > 0) {
			uiObject.printGreen("UNCOMPLETED TASKS");
			uiObject.printGreen("Index\tStart Date\tEnd Date\tTask");
			
			for (int i = 0; i < tasksToBeDisplayed.size(); i++) {
				Task temp = tasksToBeDisplayed.get(i);

				if (temp.getEndDate() != null) {
					String message = "";
					
					int result = temp.getEndDate().get(Calendar.DAY_OF_YEAR) - d3.get(Calendar.DAY_OF_YEAR);
					if (result < 0) {
						message = "overdue by " + Math.abs(result) + " days";
					} else if (result == 0) {
						message = "deadline today";
					}

					uiObject.printTask2(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(),
							            temp.getEndDateLineOne(), temp.getEndDateLineTwo(),
							            temp.getShortPriority() + temp.getIssue(), message);
				} else if (temp.getStartDate() != null) {
					String message = "";
					
					int result = temp.getStartDate().get(Calendar.DAY_OF_YEAR) - d3.get(Calendar.DAY_OF_YEAR);
					if (result < 0) {
						message = "started " + Math.abs(result) + " days ago";
					} else if (result == 0) {
						message = "starts today";
					}
					
					uiObject.printTask2(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(),
							            temp.getEndDateLineOne(), temp.getEndDateLineTwo(),
							            temp.getShortPriority() + temp.getIssue(), message);
				} else {
					String message = "";
					
					uiObject.printTask2(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(),
							            temp.getEndDateLineOne(), temp.getEndDateLineTwo(),
							            temp.getShortPriority() + temp.getIssue(), message);
				}
			}
		}
	}
}
