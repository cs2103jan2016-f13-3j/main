//@@author Kowshik
package Logic;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Task.Task;

public class Notification {
	private static ArrayList<Task> tasksToBeDisplayed;
	private static int daysInAdvance = 3;
	
	public static ArrayList<Task> getTasksToBeDisplayed() {
		return tasksToBeDisplayed;
	}

	public static Task getSpecificTask(int index) throws IndexOutOfBoundsException {
			return tasksToBeDisplayed.get(index);
	}

	/**
	 * Function that prints the upcoming uncompleted tasks in the next three days
	 */
	public static void welcomeReminder() {
		// before daysInAdvance
		UI.ui.printRed("DEADLINES APPROACHING - ");
		Calendar d1 = Calendar.getInstance();
		d1.add(Calendar.DAY_OF_MONTH, -daysInAdvance);
		int pastDay = d1.get(Calendar.DAY_OF_MONTH);
		int pastMonth = d1.get(Calendar.MONTH);
		int pastYear = d1.get(Calendar.YEAR);
		Date past = new Date(pastYear, pastMonth, pastDay);

		//after daysInAdvance
		Calendar d2 = Calendar.getInstance();
		d2.add(Calendar.DAY_OF_MONTH, 3);
		int futureDay = d2.get(Calendar.DAY_OF_MONTH);
		int futureMonth = d2.get(Calendar.MONTH);
		int futureYear = d2.get(Calendar.YEAR);
		Date future = new Date(futureYear, futureMonth, futureDay);

		//today
		Calendar d3 = Calendar.getInstance();
		int todayDay = d2.get(Calendar.DAY_OF_MONTH);
		int todayMonth = d2.get(Calendar.MONTH);
		int todayYear = d2.get(Calendar.YEAR);
		Date today = new Date(todayYear, todayMonth, todayDay);


		ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		tasksToBeDisplayed = new ArrayList<Task>();

		for(Task temp : tempTasks) {
			if(temp.getEndDate() != null) {
				if((temp.getEndDate().compareTo(d1) > 0) && (temp.getEndDate().compareTo(d2) <= 0)) {
					tasksToBeDisplayed.add(temp);
					continue;
				}
			}
			else if(temp.getStartDate() != null) {
				if((temp.getStartDate().compareTo(d1) > 0) && (temp.getStartDate().compareTo(d2) <= 0)) {
					tasksToBeDisplayed.add(temp);
				}
			}
		}

		if(tasksToBeDisplayed.size() > 0) {
			UI.ui.printGreen("UNCOMPLETED TASKS");
			UI.ui.printGreen("Index\tStart Date\tEnd Date\tTask");
			for(int i = 0; i<tasksToBeDisplayed.size(); i++) {
				Task temp = tasksToBeDisplayed.get(i);

				if(temp.getEndDate() != null) {
					int result = temp.getEndDate().get(Calendar.DAY_OF_YEAR) - d3.get(Calendar.DAY_OF_YEAR);
					String message = "";
					if(result < 0) {
						message = "overdue by " + Math.abs(result) + " days";
					} else if (result == 0) {
						message = "deadline today";
					}

					UI.ui.printTask2(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),
							temp.getEndDateLineOne(), temp.getEndDateLineTwo(), temp.getIssue(), message);
				}
				else if(temp.getStartDate() != null) {
					int result = temp.getStartDate().get(Calendar.DAY_OF_YEAR) - d3.get(Calendar.DAY_OF_YEAR);
					String message = "";
					if(result < 0) {
						message = "started " + Math.abs(result) + " days ago";
					} else if(result == 0) {
						message = "starts today";
					}
					UI.ui.printTask2(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),
							temp.getEndDateLineOne(), temp.getEndDateLineTwo(), temp.getIssue(), message);
				}
				else {
					String message = "";
					UI.ui.printTask2(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),
							temp.getEndDateLineOne(), temp.getEndDateLineTwo(), temp.getIssue(), message);
				}
			}
		}
	}

	public static void changeDaysInAdvance(int change) {
		daysInAdvance = change;
	}
}
