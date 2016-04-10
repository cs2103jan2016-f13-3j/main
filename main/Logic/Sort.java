//@@author Kowshik

package Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import Storage.LocalStorage;
import Task.Task;

public class Sort {

	private LocalStorage localStorageObject;

	public Sort() {
		localStorageObject = LocalStorage.getInstance();
	}

	/**
	 * Function to sort tasks according to priority
	 */
	public void sortTasksPriority() {
		sortTasksChronologically();
		ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
		ArrayList<Task> changedTasks = getArrayListSortedInPriority(tempUncompletedTasks, false);

		try {
			localStorageObject.setUncompletedTasks(changedTasks);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();
		changedTasks = getArrayListSortedInPriority(tempFloatingTasks, true);

		try {
			localStorageObject.setFloatingTasks(changedTasks);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to sort given arraylist according to priority
	 * 
	 * @param tempTasks  the arraylist to be sorted
 	 * @param isFloating to indicate if arraylist is floating task arraylist or not
 	 * 
	 * @return           arraylist sorted according to priority  
	 */
	public ArrayList<Task> getArrayListSortedInPriority(ArrayList<Task> tempTasks, boolean isFloating) {
		ArrayList<Task> highPriorityTasks = new ArrayList<Task>();
		ArrayList<Task> mediumPriorityTasks = new ArrayList<Task>();
		ArrayList<Task> lowPriorityTasks = new ArrayList<Task>();

		for (Task t : tempTasks) {
			if (t.getPriority().equals("high")) {
				highPriorityTasks.add(t);
			} else if (t.getPriority().equals("medium")) {
				mediumPriorityTasks.add(t);
			} else if (t.getPriority().equals("low")) {
				lowPriorityTasks.add(t);
			}
		}

		if (!isFloating) {
			highPriorityTasks = sortArrayListInChronologicalOrder(highPriorityTasks);
			mediumPriorityTasks = sortArrayListInChronologicalOrder(mediumPriorityTasks);
			lowPriorityTasks = sortArrayListInChronologicalOrder(lowPriorityTasks);
		}

		ArrayList<Task> changedTasks = new ArrayList<Task>();
		for (Task t : highPriorityTasks) {
			changedTasks.add(t);
		}
		for (Task t : mediumPriorityTasks) {
			changedTasks.add(t);
		}
		for (Task t : lowPriorityTasks) {
			changedTasks.add(t);
		}
		return changedTasks;
	}

	/**
	 * Function to sort tasks in chronological order
	 */
	public void sortTasksChronologically() {
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();

		for (int i = 0; i < tempTasks.size(); i++) {
			for (int j = i + 1; j < tempTasks.size(); j++) {
				Calendar startDate1 = tempTasks.get(i).getStartDate();
				Calendar startDate2 = tempTasks.get(j).getStartDate();
				Calendar endDate1 = tempTasks.get(i).getEndDate();
				Calendar endDate2 = tempTasks.get(j).getEndDate();

				compareDates(tempTasks, i, j, startDate1, startDate2, endDate1, endDate2);
			}
		}

		try {
			localStorageObject.setUncompletedTasks(tempTasks);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to compare the different dates of uncompleted tasks and sort  them accordingly
	 * 
	 * @param tempTasks  arraylist of tasks to be sorted
	 * @param i          index of first Task
	 * @param j          index of second Task
	 * @param startDate1 start date of first Calendar object
	 * @param startDate2 start date of second Calendar object
	 * @param endDate1   end date of first Calendar object
	 * @param endDate2   end date of second Calendar object
	 */
	public void compareDates(ArrayList<Task> tempTasks, int i, int j, Calendar startDate1, Calendar startDate2,
			                 Calendar endDate1, Calendar endDate2) {
		if (endDate1 == null && endDate2 == null) { // both end dates are null
			if (startDate1.compareTo(startDate2) > 0) {
				Task temp = tempTasks.get(i);
				tempTasks.set(i, tempTasks.get(j));
				tempTasks.set(j, temp);
			}
		} else if (endDate1 != null && endDate2 != null) { // both end dates are  not null
			if (endDate1.compareTo(endDate2) > 0) { // endDate1 is greater than endDate2
				Task temp = tempTasks.get(i);
				tempTasks.set(i, tempTasks.get(j));
				tempTasks.set(j, temp);
			} else if (endDate1.compareTo(endDate2) == 0) { // end dates are equal
				if (startDate1 != null) {
					if (startDate2 != null) { // both start dates are not null
						if (startDate1.compareTo(startDate2) > 0) { // startDate1 is greater than startDate2
							Task temp = tempTasks.get(i);
							tempTasks.set(i, tempTasks.get(j));
							tempTasks.set(j, temp);
						}
					}
				} else { // start date 1 is null
					Task temp = tempTasks.get(i);
					tempTasks.set(i, tempTasks.get(j));
					tempTasks.set(j, temp);
				}
			}
		}

		else if (endDate1 == null && endDate2 != null) { // endDate1 is null
			if (startDate1.compareTo(endDate2) > 0) {
				Task temp = tempTasks.get(i);
				tempTasks.set(i, tempTasks.get(j));
				tempTasks.set(j, temp);
			} else if (startDate1.compareTo(endDate2) == 0) {
				Task temp = tempTasks.get(i);
				tempTasks.set(i, tempTasks.get(j));
				tempTasks.set(j, temp);
			}
		} else {
			if (endDate1.compareTo(startDate2) > 0) { // endDate2 is null
				Task temp = tempTasks.get(i);
				tempTasks.set(i, tempTasks.get(j));
				tempTasks.set(j, temp);
			}
		}
	}

	/**
	 * Function to sort a given arraylist of tasks in chronological order and return the sorted list
	 * 
	 * @param tempTasks arraylist of tasks to be sorted in chronological order
	 * 
	 * @return          arraylist sortedChronologically
	 */
	public ArrayList<Task> sortArrayListInChronologicalOrder(ArrayList<Task> tempTasks) {
		for (int i = 0; i < tempTasks.size(); i++) {
			for (int j = i + 1; j < tempTasks.size(); j++) {
				Calendar startDate1 = tempTasks.get(i).getStartDate();
				Calendar startDate2 = tempTasks.get(j).getStartDate();
				Calendar endDate1 = tempTasks.get(i).getEndDate();
				Calendar endDate2 = tempTasks.get(j).getEndDate();

				compareDates(tempTasks, i, j, startDate1, startDate2, endDate1, endDate2);
			}
		}
		return tempTasks;
	}

}
