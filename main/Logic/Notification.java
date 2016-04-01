package Logic;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Storage.localStorage;
import Task.Task;

public class Notification {

	/**
	 * Function that prints the upcoming uncompleted tasks in the next three days
	 */
	public static void welcomeReminder() {
		//Today
		UI.ui.printRed("DEADLINES APPROACHING - ");
		Calendar d1 = Calendar.getInstance();
		int todayDay = d1.get(Calendar.DAY_OF_MONTH);
		int todayMonth = d1.get(Calendar.MONTH);
		int todayYear = d1.get(Calendar.YEAR);
		Date today = new Date(todayYear, todayMonth, todayDay);
		
		//Three days later
		Calendar d2 = Calendar.getInstance();
		d2.add(Calendar.DAY_OF_MONTH, 3);
		int futureDay = d2.get(Calendar.DAY_OF_MONTH);
		int futureMonth = d2.get(Calendar.MONTH);
		int futureYear = d2.get(Calendar.YEAR);
		Date future = new Date(futureYear, futureMonth, futureDay);
		
		ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		ArrayList<Task> tasksToBeDisplayed = new ArrayList<Task>();
		
		for(Task temp : tempTasks) {
			 if(temp.getEndDate() != null) {
				 if(!(temp.getEndDate().before(today)) && !(temp.getEndDate().after(future))) {
					 tasksToBeDisplayed.add(temp);
					 System.out.println("Yes");
					 continue;
				 }
			 }
			 else if(temp.getStartDate() != null) {
				 if(!(temp.getStartDate().before(today)) && !(temp.getStartDate().after(future))) {
					 tasksToBeDisplayed.add(temp);
				 }
			 }
		 }
		if(tasksToBeDisplayed.size() > 0) {
			 UI.ui.printGreen("UNCOMPLETED TASKS");
			 for(int i = 0; i<tasksToBeDisplayed.size(); i++) {
				 Task temp = tasksToBeDisplayed.get(i);
				 UI.ui.printTask(i, temp.getStartDateString(), temp.getEndDateString(), temp.getIssue());
			 }
		 }
	}
}
