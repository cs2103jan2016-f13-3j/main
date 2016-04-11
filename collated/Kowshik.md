# Kowshik
###### main\Logic\crud.java
``` java
	 public static ArrayList<Task> getTemp(){
		 return tempTasks;
	 }
	 
	 public static Task getTempTask(int index) {
		 return tempTasks.get(index);
	 }
	 
	 /**
	  * Function to add task without time into storage
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static boolean addTask(String line) throws IOException, ClassNotFoundException {
		 Task task = new Task(line);
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 boolean noDuplicate = true;
		 for(Task temp : tempTasks) {
			 if(temp.getTaskString().equals(task.getTaskString())) {
				 noDuplicate = false;
			 }
		 }
		 if(noDuplicate) {
			 Storage.localStorage.addToFloatingTasks(task);
			 return true;
		 }
		 else {
			 return false;
		 }
	 }

	 

	 /**
	  * Function to add task with only start date into storage
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static boolean addTaskWithStartDate(String line,String date, String msg) throws IOException, ClassNotFoundException {
		 Task task = new Task(line, date, msg, true);

		 boolean noDuplicate = true;
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 for(Task temp : tempTasks) {
			 if(temp.getTaskString().equals(task.getTaskString())) {
				 UI.ui.printRed(temp.getTaskString());
				 noDuplicate = false;
			 }
		 }
		 if(noDuplicate) {
			 Storage.localStorage.addToUncompletedTasks(task);
			 return true;
		 }
		 else {
			 return false;
		 }
	 }

	 /**
	  * Function to add task with only end date into storage
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static boolean addTaskWithEndDate(String line, String date, String msg) throws IOException, ClassNotFoundException {
		 Task task = new Task(line, date, msg, false);

		 boolean noDuplicate = true;
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 for(Task temp : tempTasks) {
			 if(temp.getTaskString().equals(task.getTaskString())) {
				 UI.ui.printRed(temp.getTaskString());
				 noDuplicate = false;
			 }
		 }
		 if(noDuplicate) {
			 Storage.localStorage.addToUncompletedTasks(task);
			 return true;
		 }
		 else {
			 return false;
		 }
	 }

	 /**
	  * Function to add task with both start and end date into storage
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static boolean addTaskWithBothDates(String line,String startDate, String endDate, String msg) throws IOException, ClassNotFoundException {
		 Task task = new Task(line, startDate, endDate, msg);

		 boolean noDuplicate = true;
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 for(Task temp : tempTasks) {
			 if(temp.getTaskString().equals(task.getTaskString())) {
				 UI.ui.printRed(temp.getTaskString());
				 noDuplicate = false;
			 }

			 if(temp.getStartDate() != null && temp.getEndDate() != null) {
				 if(temp.getStartDateString().equals(task.getStartDateString()) && temp.getEndDateString().equals(task.getEndDateString())) {
					 UI.ui.printRed("CLASH IN TIMING DETECTED WITH - ");
					 UI.ui.printRed(temp.getTaskString());
				 }
			 }
		 }
		 if(noDuplicate) {
			 Storage.localStorage.addToUncompletedTasks(task);
			 return true;
		 }
		 else {
			 return false;
		 }
	 }

```
###### main\Logic\crud.java
``` java
	 /**
	  * Function to edit task (edited task has no date)
	  * @param line
	  * @param date
	  * @param msg
	  * @param index
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 public static void editTaskWithNoDate(String line, String msg, int index) throws IOException, ClassNotFoundException {
		 int uncompleteList = Storage.localStorage.getUncompletedTasks().size();

		 if(index < uncompleteList){
			 Task temp = Storage.localStorage.getUncompletedTask(index);
			 deleteTask(index,1);

			 addTask(msg);
		 } else {
			 Task temp = Storage.localStorage.getFloatingTask(index - uncompleteList);
			 temp.setStartDate(null);
			 temp.setEndDate(null);
			 temp.setDescription(msg);
			 temp.setIssue(line);
			 Storage.localStorage.setFloatingTask(index - uncompleteList, temp);
		 }

	 }

	 /**
	  * Function to edit task (edited task has only start date)
	  * @param line
	  * @param date
	  * @param msg
	  * @param index
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 public static void editTaskWithStartDate(String line, String date, String msg, int index) throws IOException, ClassNotFoundException {		 
		 int uncompleteList = Storage.localStorage.getUncompletedTasks().size();

		 if(index < uncompleteList){
			 Task temp = Storage.localStorage.getUncompletedTask(index);
			 temp.setIssue(line);
			 temp.setDescription(msg);
			 temp.setEndDate(null);
			 temp.setStartDate(date);
			 temp.resetID();
			 Storage.localStorage.setUncompletedTask(index, temp);
		 } else {
			 Task temp = Storage.localStorage.getFloatingTask(index-uncompleteList);
			 temp.setIssue(line);
			 temp.setDescription(msg);
			 temp.setEndDate(null);
			 temp.setStartDate(date);
			 Storage.localStorage.delFromFloatingTasks(index-uncompleteList);
			 Storage.localStorage.addToUncompletedTasks(temp);
		 }
	 }

	 /**
	  * Function to edit task (edited task has only end date)
	  * @param index
	  * @param line
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 public static void editTaskWithEndDate(String line, String date, String msg, int index) throws IOException, ClassNotFoundException {
		 int uncompleteList=Storage.localStorage.getUncompletedTasks().size();

		 if(index < uncompleteList){
			 Task temp = Storage.localStorage.getUncompletedTask(index);
			 temp.setIssue(line);
			 temp.setStartDate(null);
			 temp.setEndDate(date);
			 temp.setDescription(msg);
			 deleteTask(index,1);
			 addTaskWithEndDate(line, date, msg);
			 //Storage.localStorage.addToUncompletedTasks(temp);
			 //Storage.localStorage.setUncompletedTask(index, temp);
		 } else {
			 Task temp = Storage.localStorage.getFloatingTask(index - uncompleteList);
			 deleteTask(index, 1);
			 temp.setDescription(msg);
			 temp.setIssue(line);
			 temp.setStartDate(null);
			 temp.setEndDate(date);
			 addTaskWithEndDate(line, date, msg);

		 }
	 }

	 /**
	  * Function to edit task (edited task has both start and end dates)
	  * @param index
	  * @param line
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 public static void editTaskWithBothDates(String line, String startDate, String endDate, String msg, int index) throws IOException, ClassNotFoundException {
		 int uncompleteList = Storage.localStorage.getUncompletedTasks().size();
		 if(index < uncompleteList){
			 Task temp = Storage.localStorage.getUncompletedTask(index);
			 temp.setIssue(line);
			 temp.setDescription(msg);
			 temp.setStartDate(startDate);
			 temp.setEndDate(endDate);
			 temp.resetID();
			 Storage.localStorage.setUncompletedTask(index, temp);
		 }else{
			 Task temp = Storage.localStorage.getFloatingTask(index - uncompleteList);
			 deleteTask(index, 1);
			 temp.setDescription(msg);
			 temp.setIssue(line);
			 temp.setStartDate(startDate);
			 temp.setEndDate(endDate);
			 addTaskWithBothDates(line,startDate,endDate,msg);
		 }
	 }
	 /**
	  * Function to get a task from the list in Uncompleted Task List
	  * @param index
	  * @return
	  */
	 public static Task getUncompletedTask(int index){

		 int size1=Storage.localStorage.getUncompletedTasks().size();
		 if(index<size1){
		 return Storage.localStorage.getUncompletedTask(index);
		 }else{
		 
			 return Storage.localStorage.getFloatingTask(index-size1);
		 }
	 }
	 public static Task getCompletedTask(int index){
		 return Storage.localStorage.getCompletedTask(index);
	 }

	 /**
	  * Function to delete task according to index in storage
	  * @throws IOException 
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static void deleteTask(int index, int listOfTasks) throws ClassNotFoundException, IOException{
		 if(listOfTasks == 1) { //delete from "display all" view
			 ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();
			 if(index < getSize.size()) {
				 Storage.localStorage.delFromUncompletedTasks(index);
			 }
			 else {
				 Storage.localStorage.delFromFloatingTasks(index - getSize.size());
			 }
		 }
		 else if(listOfTasks == 2) { //delete from completed tasks
			 Storage.localStorage.delFromCompletedTasks(index);
		 }
		 else if(listOfTasks == 3) { //delete from search completed tasks view
			 ArrayList<Task> searchTemp = Search.getSearchedTasks();
			 Task taskToBeDeleted = searchTemp.get(index);
			 ArrayList<Task> uncompletedTemp = Storage.localStorage.getUncompletedTasks();
			 for(int i = 0; i<uncompletedTemp.size(); i++) {
				 if(uncompletedTemp.get(i).equals(taskToBeDeleted)) {
					 uncompletedTemp.remove(i);
					 break;
				 }
			 }
		 }
		 else if(listOfTasks == 4) { //delete from floating tasks view
			 Storage.localStorage.delFromFloatingTasks(index);
		 }
		 else if(listOfTasks == 5) { //delete from "display" view
			 Task temp = tempTasks.get(index);
			 ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();
			 for(int i = 0; i<tempUncompletedTasks.size(); i++) {
				 if(tempUncompletedTasks.get(i).getTaskString().equals(temp.getTaskString())) {
					 tempUncompletedTasks.remove(i);
					 break;
				 }
			 }
			 Storage.localStorage.setUncompletedTasks(tempUncompletedTasks);
		 }
	 }

	 /**
	  * Function to display all the uncompleted tasks in the storage
	  * 
	  */
	 public static void displayUncompletedAndFloatingTasks() {

		 UI.ui.eraseScreen();
		 boolean isEmptyUn = false;
		 tempTasks = Storage.localStorage.getUncompletedTasks();
		 String dt="";

		 if (tempTasks.isEmpty()) {
			 isEmptyUn = true;
		 } else {
			 UI.ui.printGreen("UNCOMPLETED TASKS");
			 UI.ui.printGreen("Index\tStart Date\tEnd Date\tTask");

			 for(int i=0; i<tempTasks.size(); i++) {
				 Task temp = tempTasks.get(i);

				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());
			 }
			 UI.ui.print("________________________________________________________________");
		 }


		 boolean isEmptyF = false;
		 tempTasks = Storage.localStorage.getFloatingTasks();
		 if (tempTasks.isEmpty()) {
			 isEmptyF = true;
		 }
		 else {
			 UI.ui.printGreen("FLOATING TASKS");
			 UI.ui.printGreen("Index\tTask");
			 ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();
			 for(int i=0; i<tempTasks.size(); i++) {
				 UI.ui.printYellow((getSize.size() + i+1) + ".\t" + tempTasks.get(i).getIssue());
			 }
		 }
		 if(isEmptyUn && isEmptyF) {
			 UI.ui.printGreen("There are no tasks to show.");
		 }
	 }
	 /**
	  * function to display all floating task in storage
	  * 
	  */
	 public static void displayFloatingTasks() {
		 UI.ui.eraseScreen();
		 UI.ui.printGreen("FLOATING TASKS");
		 UI.ui.printGreen("Index\tTask");
		 boolean isEmptyF = false;
		 tempTasks = Storage.localStorage.getFloatingTasks();
		 ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();
		 for(int i=0; i<tempTasks.size(); i++) {
			 UI.ui.printYellow((getSize.size() + i+1) + ".\t" + tempTasks.get(i).getIssue());
		 }
		 if (tempTasks.isEmpty()) {
			 isEmptyF = true;
		 }  if(isEmptyF) {
			 UI.ui.printGreen("There are no floating tasks to show.");
		 }
	 }
	 /**
	  * Function to display the details of an individual task
	  * 
	  * @param index the index of the task to be displayed
	  */
	 public static void viewIndividualTask(int index) {
		 UI.ui.eraseScreen();
		 ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();
		 if(index < getSize.size()) {
			 tempTask = Storage.localStorage.getUncompletedTask(index);
		 }
		 else {
			 tempTask = Storage.localStorage.getFloatingTask(index - getSize.size());
		 }
		 boolean isCompleted = tempTask.getCompletedStatus();
		 String completed = "Not completed";
		 if(isCompleted) {
			 completed = "Completed";
		 }

		 UI.ui.printYellow(tempTask.getTaskString());
		 UI.ui.print("Status: " + completed);
		 UI.ui.print("Priority: " + tempTask.getPriority());
		 UI.ui.print("Labels:");
		 for(String label : tempTask.getLabel()) {
			 UI.ui.print(label);
		 }
	 }

	 /**
	  * Function to display all the completed tasks in the storage
	  * 
	  */
	 public static void displayCompletedTasks() {
		 UI.ui.eraseScreen();
		 UI.ui.printGreen("COMPLETED TASKS");
		 UI.ui.printGreen("Index\tStart Date\tEnd Date\tTask");
		 tempTasks = Storage.localStorage.getCompletedTasks();
		 for(int i=0; i<tempTasks.size(); i++) {
			 Task temp=tempTasks.get(i);
			 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());
		 }
		 if (tempTasks.isEmpty()) {
			 UI.ui.printGreen("There is no stored task to display");
		 }
	 }

	 public static void displayScheduleForADay(String inputDate) {
		 inputDate = inputDate.replace("/0", "/");
		 if(inputDate.startsWith("0")) {
			 inputDate = inputDate.replaceFirst("0", "");
		 }
		 System.out.println(inputDate);
		 String[] splitDate = inputDate.split("/");
		 //run through all the tasks and find which have same date
		 tempTasks = new ArrayList<Task>();
		 ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();

		 for(Task temp : tempUncompletedTasks) {
			 if(temp.getStartDate() != null) {
				 String startDay = "" + temp.getStartDate().get(Calendar.DAY_OF_MONTH);
				 String startMonth = "" + (temp.getStartDate().get(Calendar.MONTH) + 1);
				 String startYear = "" + temp.getStartDate().get(Calendar.YEAR);
				 if(checkIfDateIsContained(splitDate, startDay, startMonth, startYear)) {
					 tempTasks.add(temp);
					 continue;
				 }
			 } else {
				 String endDay = "" + temp.getEndDate().get(Calendar.DAY_OF_MONTH);
				 String endMonth = "" + (temp.getEndDate().get(Calendar.MONTH) + 1);
				 String endYear = "" + temp.getEndDate().get(Calendar.YEAR);
				 if(checkIfDateIsContained(splitDate, endDay, endMonth, endYear)) {
					 tempTasks.add(temp);
				 }

			 }
		 }

		 if (tempTasks.isEmpty()) {
			 UI.ui.printGreen("There is no stored task to display");
		 }
		 else {
			 UI.ui.eraseScreen();
			 UI.ui.print("Index\tTask");
			 for(int i = 0; i<tempTasks.size(); i++) {
				 Task temp=tempTasks.get(i);
				 UI.ui.printTask(i,temp.getStartDateString(),temp.getEndDateString(),temp.getIssue());
			 }
		 }
	 }

	 public static boolean checkIfDateIsContained(String[] splitDate, String day, String month, String year) {
		 if(day.contains(splitDate[0]) && month.contains(splitDate[1]) && year.contains(splitDate[2])) {
			 return true;
		 }
		 return false;
	 }

	 public static void displayByLabel(String s) {
		 UI.ui.eraseScreen();
		 tempTasks = new ArrayList<Task>();
		 ArrayList<Task> displayResults = Storage.localStorage.getUncompletedTasks();

		 for(Task temp : displayResults) {
			 if(temp.getLabel().contains(s)) {
				 tempTasks.add(temp);
			 }
		 }

		 if(tempTasks.size() > 0) {
			 UI.ui.printGreen("UNCOMPLETED TASKS");
			 UI.ui.printGreen("Index \t Task");
			 for(int i = 0; i<tempTasks.size(); i++) {
				 UI.ui.printYellow((i+1) + ".\t" + tempTasks.get(i).getTaskString());
			 }
			 UI.ui.print("________________________________");
		 }

		 tempTasks = new ArrayList<Task>();
		 displayResults = Storage.localStorage.getFloatingTasks();
		 for(Task temp : displayResults) {
			 if(temp.getLabel().contains(s)) {
				 tempTasks.add(temp);
			 }
		 }

		 if(tempTasks.size() > 0) {
			 UI.ui.printGreen("FLOATING TASKS");
			 UI.ui.printGreen("Index \t Task");
			 for(int i = 0; i<tempTasks.size(); i++) {
				 UI.ui.printYellow((i+1) + ".\t" + tempTasks.get(i).getTaskString());
			 }
			 UI.ui.print("________________________________");
		 }

		 tempTasks = new ArrayList<Task>();
		 displayResults = Storage.localStorage.getCompletedTasks();
		 for(Task temp : displayResults) {
			 if(temp.getLabel().contains(s)) {
				 tempTasks.add(temp);
			 }
		 }
		 if(tempTasks.size() > 0) {
			 UI.ui.printGreen("COMPLETED TASKS");
			 UI.ui.printYellow("Index \t Task");
			 for(int i = 0; i<tempTasks.size(); i++) {
				 UI.ui.printYellow((i+1) + ".\t" + tempTasks.get(i).getTaskString());
			 }
		 }
	 }


	 /**
	  * Function to clear storage
	  * @throws IOException 
	  * @throws ClassNotFoundException 
	  * 
	  */
	 public static void clearTasks() throws ClassNotFoundException, IOException{
		 Storage.localStorage.clear();
	 }

	 /**
	  * Function to exit the application when user enters exit command
	  */
	 public static void exit(){
		 System.exit(0);
	 }



	 public static void addLabelToTask(int index, String label) {
		 int sizeOfUncompletedTasks = Storage.localStorage.getUncompletedTasks().size();
		 if(index < sizeOfUncompletedTasks) {
			 Task temp = Storage.localStorage.getUncompletedTask(index);
			 temp.setLabel(label);
			 Storage.localStorage.setUncompletedTask(index, temp);
		 } else {
			 Task temp = Storage.localStorage.getFloatingTask(index);
			 temp.setLabel(label);
			 Storage.localStorage.setFloatingTask(index, temp);
		 }
	 }

	 public static void displayTasksForThisWeek() {
		 UI.ui.eraseScreen();
		 UI.ui.printGreen("Upcoming tasks this week - ");
		 ArrayList<Task> tasksToBeDisplayed = new ArrayList<Task>();
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();

		 //getting today and seven days later
		 /*Calendar d1 = Calendar.getInstance();
		 int todayDay = d1.get(Calendar.DAY_OF_MONTH);
		 int todayMonth = d1.get(Calendar.MONTH);
		 int todayYear = d1.get(Calendar.YEAR);
		 Date today = new Date(todayYear, todayMonth, todayDay);

		 Calendar d2 = Calendar.getInstance();
		 d2.add(Calendar.DAY_OF_MONTH, 7);
		 int futureDay = d2.get(Calendar.DAY_OF_MONTH);
		 int futureMonth = d2.get(Calendar.MONTH);
		 int futureYear = d2.get(Calendar.YEAR);
		 Date future = new Date(futureYear, futureMonth, futureDay);*/

		 Calendar date = Calendar.getInstance();
		 int thisWeek = date.get(Calendar.WEEK_OF_YEAR);

		 //finding tasks which has dates in current week
		 for(Task temp : tempTasks) {
			 if(temp.getEndDate() != null) {
				 if(temp.getEndDate().get(Calendar.WEEK_OF_YEAR) == thisWeek) {
					 tasksToBeDisplayed.add(temp);
					 continue;
				 }
			 }
			 if(temp.getStartDate() != null) {
				 if(temp.getStartDate().get(Calendar.WEEK_OF_YEAR) == thisWeek) {
					 tasksToBeDisplayed.add(temp);
				 }
			 }
		 }

		 if(tasksToBeDisplayed.size() > 0) {
			 UI.ui.printGreen("UNCOMPLETED TASKS");
			 for(int i = 0; i<tasksToBeDisplayed.size(); i++) {
				 Task temp = tasksToBeDisplayed.get(i);
				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());
			 }
		 }
		 else {
			 UI.ui.printRed("No tasks this week");
		 }
	 }

	 public static void displayTasksForNextWeek() {
		 UI.ui.eraseScreen();
		 UI.ui.printGreen("Upcoming tasks next week - ");
		 ArrayList<Task> tasksToBeDisplayed = new ArrayList<Task>();
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();

		 Calendar date = Calendar.getInstance();
		 date.add(Calendar.WEEK_OF_YEAR, 1);
		 int nextWeek = date.get(Calendar.WEEK_OF_YEAR);

		 //finding tasks which has dates in the next week
		 for(Task temp : tempTasks) {
			 if(temp.getEndDate() != null) {
				 if(temp.getEndDate().get(Calendar.WEEK_OF_YEAR) == nextWeek) {
					 tasksToBeDisplayed.add(temp);
					 continue;
				 }
			 }
			 if(temp.getStartDate() != null) {
				 if(temp.getStartDate().get(Calendar.WEEK_OF_YEAR) == nextWeek) {
					 tasksToBeDisplayed.add(temp);
				 }
			 }
		 }

		 if(tasksToBeDisplayed.size() > 0) {
			 UI.ui.printGreen("UNCOMPLETED TASKS");
			 for(int i = 0; i<tasksToBeDisplayed.size(); i++) {
				 Task temp = tasksToBeDisplayed.get(i);
				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());
			 }
		 }
		 else {
			 UI.ui.printRed("No tasks next week");
		 }

	 }

	 public static void displayTaksForTwoWeeksLater() {
		 UI.ui.eraseScreen();
		 UI.ui.printGreen("Upcoming tasks for two weeks later - ");
		 ArrayList<Task> tasksToBeDisplayed = new ArrayList<Task>();
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();

		 Calendar date = Calendar.getInstance();
		 date.add(Calendar.WEEK_OF_YEAR, 2);
		 int twoWeeksLater = date.get(Calendar.WEEK_OF_YEAR);

		 //finding tasks which has dates in two weeks time
		 for(Task temp : tempTasks) {
			 if(temp.getEndDate() != null) {
				 if(temp.getEndDate().get(Calendar.WEEK_OF_YEAR) == twoWeeksLater) {
					 tasksToBeDisplayed.add(temp);
					 continue;
				 }
			 }
			 if(temp.getStartDate() != null) {
				 if(temp.getStartDate().get(Calendar.WEEK_OF_YEAR) == twoWeeksLater) {
					 tasksToBeDisplayed.add(temp);
				 }
			 }
		 }

		 if(tasksToBeDisplayed.size() > 0) {
			 UI.ui.printGreen("UNCOMPLETED TASKS");
			 for(int i = 0; i<tasksToBeDisplayed.size(); i++) {
				 Task temp = tasksToBeDisplayed.get(i);
				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());
			 }
		 }
		 else {
			 UI.ui.printRed("No tasks for two weeks later");
		 }

	 }

	 public static void displayTasksForLastWeek() {
		 UI.ui.eraseScreen();
		 UI.ui.printGreen("Tasks uncompleted from last week - ");
		 ArrayList<Task> tasksToBeDisplayed = new ArrayList<Task>();
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();

		 Calendar date = Calendar.getInstance();
		 date.add(Calendar.WEEK_OF_YEAR, -1);
		 int lastWeek = date.get(Calendar.WEEK_OF_YEAR);

		 //finding tasks which has dates in two weeks time
		 for(Task temp : tempTasks) {
			 if(temp.getEndDate() != null) {
				 if(temp.getEndDate().get(Calendar.WEEK_OF_YEAR) == lastWeek) {
					 tasksToBeDisplayed.add(temp);
					 continue;
				 }
			 }
			 if(temp.getStartDate() != null) {
				 if(temp.getStartDate().get(Calendar.WEEK_OF_YEAR) == lastWeek) {
					 tasksToBeDisplayed.add(temp);
				 }
			 }
		 }

		 if(tasksToBeDisplayed.size() > 0) {
			 UI.ui.printGreen("UNCOMPLETED TASKS");
			 for(int i = 0; i<tasksToBeDisplayed.size(); i++) {
				 Task temp = tasksToBeDisplayed.get(i);
				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());
			 }
		 }
		 else {
			 UI.ui.printRed("No tasks left from last week");
		 }

	 }


	 public static void displayUpcomingTasks() {
		 UI.ui.eraseScreen();
		 ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();
		 //today
		 Calendar d1 = Calendar.getInstance();
		 d1.add(Calendar.DAY_OF_MONTH, -1);

		 //7 days in advance
		 Calendar d2 = Calendar.getInstance();
		 d2.add(Calendar.DAY_OF_MONTH, 7);

		 tempTasks = new ArrayList<Task>();

		 for(Task temp : tempUncompletedTasks) {
			 if(temp.getEndDate() != null) {
				 if(temp.getEndDate().compareTo(d1) >= 0 && temp.getEndDate().compareTo(d2) <=0) {
					 tempTasks.add(temp);
					 continue;
				 }
			 }
			 else if(temp.getStartDate() != null) {
				 if((temp.getStartDate().compareTo(d1) >= 0) && (temp.getStartDate().compareTo(d2) <= 0)) {
					 tempTasks.add(temp);
				 }
			 }
		 }
		 
		 if(tempTasks.size() > 0) {
			 UI.ui.printGreen("UNCOMPLETED TASKS");
			 UI.ui.printGreen("Index\tStart Date\tEnd Date\tTask");
			 for(int i = 0; i<tempTasks.size(); i++) {
				 Task temp = tempTasks.get(i);
				 UI.ui.printTask2(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());

			 }
		 }
	 }
 }

```
###### main\Logic\Help.java
``` java
package Logic;
public class Help {

	public static void printHelpMenu() {
		String addCommand = "Type \"add/+/a\" followed by task description. Press Enter. Now enter the date or -.";
		String editCommand = "Type \"edit/e\" followed by edited task description and edited date.";
		String deleteCommand = "Enter \"delete/-\" followed by the task number that you want to delete.";
		String markCommand = "Enter \"mark/m\" to mark a task as completed or uncompleted";
		String exitCommand = "Enter \"exit\" to quit Agendah";
		String clearCommand = "Enter \"clear/c\" to delete all the tasks.";
		String sortCommand = "Enter \"sort\" to sort the tasks alphabetically";
		String searchCommand = "Enter \"search/s\" followed by the word you want to search for to display all tasks containing that word";
		String displayUncompletedCommand = "Enter \"display/d\" to display the uncompleted tasks";
		String displayCompletedCommand = "Enter \"displaycompleted/dc\" to display the completed tasks";
		
		UI.ui.print("1. " + addCommand);
		UI.ui.print("2. " + editCommand);
		UI.ui.print("3. " + deleteCommand);
		UI.ui.print("4. " + displayUncompletedCommand);
		UI.ui.print("5. " + displayCompletedCommand);
		UI.ui.print("6. " + sortCommand);
		UI.ui.print("7. " + searchCommand);
		UI.ui.print("8. " + clearCommand);
		UI.ui.print("9. " + markCommand);
		UI.ui.print("10. " + exitCommand);
	}
}
```
###### main\Logic\Mark.java
``` java
package Logic;
import java.io.IOException;
import java.util.ArrayList;

import Storage.localStorage;
import Task.Task;

public class Mark {

	/**
	 * Function to mark tasks as completed
	 * 
	 * @param index the index of the task to be marked as completed
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void markTaskAsCompleted(int index) throws IOException, ClassNotFoundException {
		ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();

		if(index < getSize.size()) {
			Task temp = Storage.localStorage.getUncompletedTask(index);
			temp.setComplete();
			Storage.localStorage.delFromUncompletedTasks(index);
			Storage.localStorage.addToCompletedTasks(temp);
		}
		else {
			Task temp = Storage.localStorage.getFloatingTask(index - getSize.size());
			temp.setComplete();
			Storage.localStorage.delFromFloatingTasks(index - getSize.size());
			Storage.localStorage.addToCompletedTasks(temp);
		}
	}

	/**
	 * Function to mark a task as uncompleted
	 * 
	 * @param index the index of the task to be marked as uncompleted
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void markTaskAsUncompleted(int index) throws IOException, ClassNotFoundException {
		Task temp = Storage.localStorage.getCompletedTask(index);

		if(temp.getEndDate() != null || temp.getStartDate() != null) {
			Storage.localStorage.addToUncompletedTasks(temp);
			Storage.localStorage.delFromCompletedTasks(index);
		} else {
			Storage.localStorage.addToFloatingTasks(temp);
			Storage.localStorage.delFromCompletedTasks(index);
		}
	}

	/**
	 * Function to set the priority for a task
	 * 
	 * @param index the index of the task to be updated
	 * @param priority the priority to be set for the task
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void setPriority(int index, String priority) throws ClassNotFoundException, IOException {
		//		localStorage.copyCurrentState();
		ArrayList<Task> getSize = Storage.localStorage.getUncompletedTasks();

		if(index < getSize.size()) {
			Task temp = Storage.localStorage.getUncompletedTask(index);
			temp.setPriority(priority);
			Storage.localStorage.setUncompletedTask(index, temp);
		}
		else {
			Task temp = Storage.localStorage.getFloatingTask(index - getSize.size());
			temp.setPriority(priority);
			Storage.localStorage.setFloatingTask(index - getSize.size(), temp);
		}
	}

	public static void setRecurringTasksPriority(int index, String priority) {
		ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();


		Task temp = Storage.localStorage.getUncompletedTask(index);
		String idOfTask = temp.getId();

	if (!idOfTask.equals("")) {
			for(int i = 0; i<tempTasks.size(); i++) {
				if(!tempTasks.get(i).getId().equals("")) {
					if(tempTasks.get(i).getId().equals(idOfTask)) {
						tempTasks.get(i).setPriority(priority);
					}
				}
			}

	}		

			try {
				Storage.localStorage.setUncompletedTasks(tempTasks);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	} 
}
```
###### main\Logic\Notification.java
``` java
package Logic;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Storage.localStorage;
import Task.Task;

public class Notification {
	private static int daysInAdvance = 3;
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
		ArrayList<Task> tasksToBeDisplayed = new ArrayList<Task>();

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
					if(temp.getEndDate().get(Calendar.DAY_OF_YEAR) < d3.get(Calendar.DAY_OF_YEAR)) {
						int overdue = d3.get(Calendar.DAY_OF_MONTH) - temp.getEndDate().get(Calendar.DAY_OF_MONTH);
						String message = "";
						if(overdue != 1) {
							message = "overdue by " + overdue + " days";
						} else {
							message = "overdue by " + overdue + " day";
						}
						UI.ui.printTask2(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),message);

					} else if(temp.getEndDate().get(Calendar.DAY_OF_YEAR) == d3.get(Calendar.DAY_OF_YEAR)) {
						String message = "deadline today";
						UI.ui.printTask2(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),message);

					} 
					else {
						UI.ui.printTask2(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());

					}
				}
			}
		}
	}

	public static void changeDaysInAdvance(int change) {
		daysInAdvance = change;
	}
}
```
###### main\Logic\Search.java
``` java
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

		if((searchKeywords.length == 1) && (searchKeywords[0].length() == 1)) {
			searchSingleLetter(searchKeywords, counter, temp);
		}
		else {
			searchPhrase(searchKeywords, counter, temp);
		}
	}

	public static void searchSingleLetter(String[] searchKeywords, int counter, ArrayList<Task> temp) {
		String search = searchKeywords[0];
		if(temp.size() > 0) {
			for(int i = 0; i<temp.size(); i++) {
				String[] taskParts = temp.get(i).getIssue().split(" ");
				for(int j = 0; j<taskParts.length; j++) {
					if(taskParts[j].trim().equals(search)) {
						searchedTasks.add(temp.get(i));
						break;
					}
				}
			}

			if(searchedTasks.size() > 0) {
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
		}
		
		temp = Storage.localStorage.getFloatingTasks();
		if(temp.size() > 0) {
			for(int i = 0; i<temp.size(); i++) {
				String[] taskParts = temp.get(i).getIssue().split(" ");
				for(int j = 0; j<taskParts.length; j++) {
					if(taskParts[j].trim().equals(search)) {
						searchedTasks.add(temp.get(i));
						break;
					}
				}
			}

			if(searchedTasks.size() > counter) {
				UI.ui.printGreen("FLOATING TASKS");
				UI.ui.printGreen("Index\tTask");
				for(int i = counter; i<searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					UI.ui.printTask(i, temp1.getStartDateString(), temp1.getEndDateString(), temp1.getIssue());
					counter++;
				}

				UI.ui.print("________________________________________________________________");
				UI.ui.print("\n");
			}
		}
		
		temp = Storage.localStorage.getCompletedTasks();
		if(temp.size() > 0) {
			for(int i = 0; i<temp.size(); i++) {
				String[] taskParts = temp.get(i).getIssue().split(" ");
				for(int j = 0; j<taskParts.length; j++) {
					if(taskParts[j].trim().equals(search)) {
						searchedTasks.add(temp.get(i));
						break;
					}
				}
			}

			if(searchedTasks.size() > counter) {
				UI.ui.printGreen("COMPLETED TASKS");
				UI.ui.printGreen("Index\tTask");
				for(int i = counter; i<searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					UI.ui.printTask(i, temp1.getStartDateString(), temp1.getEndDateString(), temp1.getIssue());

				}
			}
		}
		
		if(searchedTasks.size() == 0) {
			UI.ui.printRed("NO TASKS FOUND");
		}
	}

	public static void searchPhrase(String[] searchKeywords, int counter, ArrayList<Task> temp) {
		
		if(temp.size() > 0) {
			for(int i = 0; i<temp.size(); i++) {
				boolean isSuccess = true;
				for(int j = 0; j<searchKeywords.length; j++) {
					if(isContainsKeyword(searchKeywords, temp, i, j)) {
						
					}
					else {
						isSuccess = false;
						break;
					}
				}
				if(isSuccess) {
					searchedTasks.add(temp.get(i));
				}
			}

			if(searchedTasks.size() > 0) {
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
		}

		temp = Storage.localStorage.getFloatingTasks();
		if(temp.size() > 0) {
			for(int i = 0; i<temp.size(); i++) {
				boolean isSuccess = true;
				for(int j = 0; j<searchKeywords.length; j++) {
					if(isContainsKeyword(searchKeywords, temp, i, j)) {
						
					}
					else {
						isSuccess = false;
						break;
					}
				}
				if(isSuccess) {
					searchedTasks.add(temp.get(i));
				}
			}

			if(searchedTasks.size() > counter) {
				UI.ui.printGreen("FLOATING TASKS");
				UI.ui.printGreen("Index\tTask");
				for(int i = counter; i<searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					UI.ui.printTask(i, temp1.getStartDateString(), temp1.getEndDateString(), temp1.getIssue());
					counter++;
				}

				UI.ui.print("________________________________________________________________");
				UI.ui.print("\n");
			}
		}

		temp = Storage.localStorage.getCompletedTasks();
		if(temp.size() > 0) {
			for(int i = 0; i<temp.size(); i++) {
				boolean isSuccess = true;
				for(int j = 0; j<searchKeywords.length; j++) {
					if(isContainsKeyword(searchKeywords, temp, i, j)) {
						
					}
					else {
						isSuccess = false;
						break;
					}
				}
				if(isSuccess) {
					searchedTasks.add(temp.get(i));
				}
			}

			if(searchedTasks.size() > counter) {
				UI.ui.printGreen("COMPLETED TASKS");
				UI.ui.printGreen("Index\tTask");
				for(int i = counter; i<searchedTasks.size(); i++) {
					Task temp1 = searchedTasks.get(i);
					UI.ui.printTask(i, temp1.getStartDateString(), temp1.getEndDateString(), temp1.getIssue());

				}
			}
		}
		
		if(searchedTasks.size() == 0) {
			UI.ui.printRed("NO TASKS FOUND");
		}
	}

	public static boolean isContainsKeyword(String[] searchKeywords, ArrayList<Task> temp, int i, int j) {
		return temp.get(i).getIssue().toLowerCase().contains(searchKeywords[j].toLowerCase()) || 
				temp.get(i).getTaskString().toLowerCase().contains(searchKeywords[j].toLowerCase());
	}
}
```
###### main\Logic\Sort.java
``` java
package Logic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import Task.Task;

public class Sort {

	/**
	 * Function to sorts tasks in storage alphabetically
	 * 
	 */
	public static void sortTasksAlphabetically(){
		ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();

		for(int i = 0; i<tempUncompletedTasks.size()-1; i++) {
			for(int j = i+1; j<tempUncompletedTasks.size(); j++) {
				int result = tempUncompletedTasks.get(i).getIssue().compareTo(tempUncompletedTasks.get(j).getIssue());
				if(result > 0) {
					Task setTask = tempUncompletedTasks.get(i);
					tempUncompletedTasks.set(i, tempUncompletedTasks.get(j));
					tempUncompletedTasks.set(j, setTask);
				}
			}
		}
		try {
			Storage.localStorage.setUncompletedTasks(tempUncompletedTasks);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<Task> tempFloatingTasks = Storage.localStorage.getFloatingTasks();

		for(int i = 0; i<tempFloatingTasks.size()-1; i++) {
			for(int j = i+1; j<tempFloatingTasks.size(); j++) {
				int result = tempFloatingTasks.get(i).getIssue().compareTo(tempFloatingTasks.get(j).getIssue());
				if(result > 0) {
					Task setTask = tempFloatingTasks.get(i);
					tempFloatingTasks.set(i, tempFloatingTasks.get(j));
					tempFloatingTasks.set(j, setTask);
				}
			}
		}
		try {
			Storage.localStorage.setFloatingTasks(tempFloatingTasks);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Function to sort tasks in chronological order
	 */
	public static void sortTasksChronologically() {
		ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		for(int i = 0; i<tempTasks.size(); i++) {
			for(int j = i+1; j<tempTasks.size(); j++) {
				Calendar startDate1 = tempTasks.get(i).getStartDate();
				Calendar startDate2 = tempTasks.get(j).getStartDate();
				Calendar endDate1 = tempTasks.get(i).getEndDate();
				Calendar endDate2 = tempTasks.get(j).getEndDate();

				if(endDate1 == null && endDate2 == null) { //both end dates are null
					if(startDate1.compareTo(startDate2) > 0) {
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}
				else if(endDate1 != null && endDate2 != null) { //both end dates are not null
					if(endDate1.compareTo(endDate2) > 0) {
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}

				else if(endDate1 == null && endDate2 != null) { //one end date is null
					if(startDate1.compareTo(endDate2) > 0) {
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}
				else {
					if(endDate1.compareTo(startDate2) > 0) { //one end date is null
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}
			}
		}

		try {
			Storage.localStorage.setUncompletedTasks(tempTasks);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<Task> sortArrayListInChronologicalOrder(ArrayList<Task> tempTasks) {
		for(int i = 0; i<tempTasks.size(); i++) {
			for(int j = i+1; j<tempTasks.size(); j++) {
				Calendar startDate1 = tempTasks.get(i).getStartDate();
				Calendar startDate2 = tempTasks.get(j).getStartDate();
				Calendar endDate1 = tempTasks.get(i).getEndDate();
				Calendar endDate2 = tempTasks.get(j).getEndDate();

				if(endDate1 == null && endDate2 == null) { //both end dates are null
					if(startDate1.compareTo(startDate2) > 0) {
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}
				else if(endDate1 != null && endDate2 != null) { //both end dates are not null
					if(endDate1.compareTo(endDate2) > 0) {
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}

				else if(endDate1 == null && endDate2 != null) { //one end date is null
					if(startDate1.compareTo(endDate2) > 0) {
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}
				else {
					if(endDate1.compareTo(startDate2) > 0) { //one end date is null
						Task temp = tempTasks.get(i);
						tempTasks.set(i, tempTasks.get(j));
						tempTasks.set(j, temp);
					}
				}
			}
		}
		return tempTasks;
	}

	/**
	 * Function to sort tasks according to priority
	 */
	public static void sortTasksPriority() {
		sortTasksChronologically();
		ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();

		for(int i = 0; i<tempUncompletedTasks.size(); i++) {
			for(int j = i+1; j<tempUncompletedTasks.size(); j++) {
				if(tempUncompletedTasks.get(i).getPriority().equals("low") && tempUncompletedTasks.get(j).getPriority().equals("high")) {
					Task temp = tempUncompletedTasks.get(i);
					tempUncompletedTasks.set(i, tempUncompletedTasks.get(j));
					tempUncompletedTasks.set(j, temp);
				}

				else if(tempUncompletedTasks.get(i).getPriority().equals("medium") && tempUncompletedTasks.get(j).getPriority().equals("high")) {
					Task temp = tempUncompletedTasks.get(i);
					tempUncompletedTasks.set(i, tempUncompletedTasks.get(j));
					tempUncompletedTasks.set(j, temp);
				}

				else if(tempUncompletedTasks.get(i).getPriority().equals("low") && tempUncompletedTasks.get(j).getPriority().equals("medium")) {
					Task temp = tempUncompletedTasks.get(i);
					tempUncompletedTasks.set(i, tempUncompletedTasks.get(j));
					tempUncompletedTasks.set(j, temp);
				}
			}
		}
		ArrayList<Task> highPriorityTasks = new ArrayList<Task>();
		ArrayList<Task> mediumPriorityTasks = new ArrayList<Task>();
		ArrayList<Task> lowPriorityTasks = new ArrayList<Task>();

		for(Task t : tempUncompletedTasks) {
			if(t.getPriority().equals("high")) {
				highPriorityTasks.add(t);
			}
			else if(t.getPriority().equals("medium")) {
				mediumPriorityTasks.add(t);
			}
			else if(t.getPriority().equals("low")) {
				lowPriorityTasks.add(t);
			}
		}

		highPriorityTasks = sortArrayListInChronologicalOrder(highPriorityTasks);
		mediumPriorityTasks = sortArrayListInChronologicalOrder(mediumPriorityTasks);
		lowPriorityTasks = sortArrayListInChronologicalOrder(lowPriorityTasks);

		ArrayList<Task> changedTasks = new ArrayList<Task>();
		for(Task t : highPriorityTasks) {
			changedTasks.add(t);
		}
		for(Task t : mediumPriorityTasks) {
			changedTasks.add(t);
		}
		for(Task t : lowPriorityTasks) {
			changedTasks.add(t);
		}
		
		try {
			Storage.localStorage.setUncompletedTasks(changedTasks);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<Task> tempFloatingTasks = Storage.localStorage.getFloatingTasks();

		/*for(int i = 0; i<tempFloatingTasks.size(); i++) {
			for(int j = i+1; j<tempFloatingTasks.size(); j++) {
				if(tempFloatingTasks.get(i).getPriority().equals("low") && tempFloatingTasks.get(j).getPriority().equals("high")) {
					Task temp = tempFloatingTasks.get(i);
					tempFloatingTasks.set(i, tempFloatingTasks.get(j));
					tempFloatingTasks.set(j, temp);
				}

				else if(tempFloatingTasks.get(i).getPriority().equals("medium") && tempFloatingTasks.get(j).getPriority().equals("high")) {
					Task temp = tempFloatingTasks.get(i);
					tempFloatingTasks.set(i, tempFloatingTasks.get(j));
					tempFloatingTasks.set(j, temp);
				}

				else if(tempFloatingTasks.get(i).getPriority().equals("low") && tempFloatingTasks.get(j).getPriority().equals("medium")) {
					Task temp = tempFloatingTasks.get(i);
					tempFloatingTasks.set(i, tempFloatingTasks.get(j));
					tempFloatingTasks.set(j, temp);
				}
			}
		}*/
		
		highPriorityTasks = new ArrayList<Task>();
		 mediumPriorityTasks = new ArrayList<Task>();
		lowPriorityTasks = new ArrayList<Task>();

		for(Task t : tempFloatingTasks) {
			if(t.getPriority().equals("high")) {
				highPriorityTasks.add(t);
			}
			else if(t.getPriority().equals("medium")) {
				mediumPriorityTasks.add(t);
			}
			else if(t.getPriority().equals("low")) {
				lowPriorityTasks.add(t);
			}
		}

		changedTasks = new ArrayList<Task>();
		for(Task t : highPriorityTasks) {
			changedTasks.add(t);
		}
		for(Task t : mediumPriorityTasks) {
			changedTasks.add(t);
		}
		for(Task t : lowPriorityTasks) {
			changedTasks.add(t);
		}
		
		try {
			Storage.localStorage.setFloatingTasks(changedTasks);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


```
###### main\Parser\Parser.java
``` java
	public static void setLabelCommand(String s) {
		try {
			int num = Integer.parseInt(s);

			ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
			ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();

			if (list.size() + list2.size() == 0) {
				UI.ui.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				UI.ui.printRed(MSG_PRIORITY_FAIL);
			} else {
				UI.ui.print("Enter label");
				String label = sc.nextLine();
				Logic.crud.addLabelToTask(num - 1, label);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {

		}
	}

```
###### main\Parser\Parser.java
``` java
	public static void setPriorityCommand(String s) {
		if(Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if(Logic.Head.getLastDisplayArg().equals("")) { //set priority from "display" view
				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Logic.crud.getTemp();
					if (list.size() == 0) {
						UI.ui.printRed(MSG_EMPTY);
					} else if (list.size() < num || num - 1 < 0) {
						UI.ui.printRed(MSG_PRIORITY_FAIL);
					} else {
						UI.ui.printYellow("Enter priority");
						String priority = sc.nextLine();

						Task temp = list.get(num - 1);
						ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();
						int counter = 1;
						for(Task t : tempUncompletedTasks) {
							if(t.getTaskString().equals(temp.getTaskString())) {
								num = counter;
								break;
							}
							counter++;
						}
						Logic.Mark.setPriority(num - 1, priority);
						arraylistsHaveBeenModified = true;
					}
				} catch (Exception e) {
				}
			}
			else if(Logic.Head.getLastDisplayArg().equals("all")) {
				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
					ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
					if (list.size() + list2.size() == 0) {
						UI.ui.printRed(MSG_EMPTY);
					} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
						UI.ui.printRed(MSG_PRIORITY_FAIL);
					} else {
						UI.ui.printYellow("Enter priority");
						String priority = sc.nextLine();
						Logic.Mark.setPriority(num - 1, priority);
						arraylistsHaveBeenModified = true;
					}
				} catch (Exception e) {
				}
			}
		} else {
			try {
				int num = Integer.parseInt(s);
				ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
				ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					UI.ui.printRed(MSG_EMPTY);
				} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
					UI.ui.printRed(MSG_PRIORITY_FAIL);
				} else {
					UI.ui.printYellow("Enter priority");
					String priority = sc.nextLine();
					Logic.Mark.setPriority(num - 1, priority);
					arraylistsHaveBeenModified = true;
				}
			} catch (Exception e) {
			}
		}
	}

```
###### main\Parser\Parser.java
``` java
	public static void unmarkCommand(String s) {
		try {
			int num = Integer.parseInt(s);
			// check if user input integer is valid. If it is valid, unmark
			// should
			// work
			ArrayList<Task> list = Storage.localStorage.getCompletedTasks();
			if (list.size() == 0) {
				UI.ui.printRed(MSG_NO_COMPLETED_TASKS);
			} else if (list.size() < num || num - 1 < 0) {
				UI.ui.printRed(MSG_UNMARK_FAIL);
			} else {
				Task temp = Logic.crud.getCompletedTask(num - 1);
				Logic.Mark.markTaskAsUncompleted(num - 1);
				UI.ui.printGreen(s + MSG_UNMARK);
				Logic.crud.displayNearestFiveUnmarkCompleteTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {

		}
	}

	public static void markCommand(String s) {
		if(Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if(Logic.Head.getLastDisplayArg().equals("")) { // marking from "display" view

				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Logic.crud.getTemp();
					if (list.size() == 0) {
						UI.ui.printRed(MSG_EMPTY);
					} else if (list.size() < num || num - 1 < 0) {
						UI.ui.printRed(MSG_MARK_FAIL);
					} else {

						Task temp = Logic.crud.getTempTask(num - 1);
						ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();
						int counter = 0;
						for(Task t : tempUncompletedTasks) {
							if(t.getTaskString().equals(temp.getTaskString())) {
								Logic.Mark.markTaskAsCompleted(counter);
								UI.ui.eraseScreen();
								UI.ui.printGreen(s + MSG_MARK);
								Logic.crud.displayNearestFiveCompletedTaskList(temp);
								arraylistsHaveBeenModified = true;
								break;
							}
							counter++;
						}
					}
				} catch (Exception e) {

				}
			}
			else if(Logic.Head.getLastDisplayArg().equals("all")) {
				try { 
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
					ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
					if (list.size() + list2.size() == 0) {
						UI.ui.printRed(MSG_EMPTY);
					} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
						UI.ui.printRed(MSG_MARK_FAIL);
					} else {
						Task temp = Logic.crud.getUncompletedTask(num - 1);
						Logic.Mark.markTaskAsCompleted(num - 1);
						UI.ui.eraseScreen();
						UI.ui.printGreen(s + MSG_MARK);
						Logic.crud.displayNearestFiveCompletedTaskList(temp);
						arraylistsHaveBeenModified = true;
					}
				} catch (Exception e) {

				}
			}
			else if(Logic.Head.getLastDisplayArg().equals("floating") || Logic.Head.getLastDisplayArg().equals("f")) {
				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
					ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
					if (list.size() + list2.size() == 0) {
						UI.ui.printRed(MSG_EMPTY);
					} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
						UI.ui.printRed(MSG_MARK_FAIL);
					} else {
						int i = list.size();
						Task temp = Logic.crud.getUncompletedTask(num -1);
						Logic.Mark.markTaskAsCompleted(num - 1);
						UI.ui.eraseScreen();
						UI.ui.printGreen(s + MSG_MARK);
						Logic.crud.displayNearestFiveCompletedTaskList(temp);
						arraylistsHaveBeenModified = true;
					}
				} catch (Exception e) {

				}

			}
		}
		else {
			try { 
				int num = Integer.parseInt(s);
				ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
				ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					UI.ui.printRed(MSG_EMPTY);
				} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
					UI.ui.printRed(MSG_MARK_FAIL);
				} else {
					Task temp = Logic.crud.getUncompletedTask(num - 1);
					Logic.Mark.markTaskAsCompleted(num - 1);
					UI.ui.eraseScreen();
					UI.ui.printGreen(s + MSG_MARK);
					Logic.crud.displayNearestFiveCompletedTaskList(temp);
					arraylistsHaveBeenModified = true;
				}
			} catch (Exception e) {

			}
		}
	}


	public static void sortCommand(String s) {
		if (s.equals("p") || s.equals("priority")) {
			Logic.Sort.sortTasksPriority();
			Logic.crud.displayUncompletedAndFloatingTasks();
		} else {
			Logic.Sort.sortTasksAlphabetically();
			UI.ui.printGreen(MSG_SORT);
			arraylistsHaveBeenModified = true;
		}
	}

	public static void clearCommand() throws ClassNotFoundException, IOException {
		Logic.crud.clearTasks();
		UI.ui.printGreen(MSG_CLEAR);
		arraylistsHaveBeenModified = true;
	}

	public static void viewCommand(String s) {
		if(Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if(Logic.Head.getLastDisplayArg().equals("")) {
				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Logic.crud.getTemp();
					Task temp = Logic.crud.getTempTask(num - 1);
					ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();
					int counter = 0;
					for(Task t : tempUncompletedTasks) {
						if(t.getTaskString().equals(temp.getTaskString())) {
							UI.ui.eraseScreen();
							Logic.crud.viewIndividualTask(counter);
							arraylistsHaveBeenModified = true;
							break;
						}
						counter++;
					}
				}
				catch (Exception e) {

				}

			}
			else {
				try {
					int num = Integer.parseInt(s);
					Logic.crud.viewIndividualTask(num - 1);
				} catch (Exception e) {
				}
			}
		} /*else if(Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplayArg().equals("s")) {

			try {
				int num = Integer.parseInt(s);
				Task temp = Logic.crud.getTempTask(num - 1);
				System.out.println("dsadas");
				ArrayList<Task> tempUncompletedTasks = Storage.localStorage.getUncompletedTasks();
				ArrayList<Task> tempFloatingTasks = Storage.localStorage.getFloatingTasks();
				int counter = 0;
				for(Task t : tempUncompletedTasks) {
					if(t.getTaskString().equals(temp.getTaskString())) {
						UI.ui.eraseScreen();
						Logic.crud.viewIndividualTask(counter);
						arraylistsHaveBeenModified = true;
						break;
					}
					counter++;
				}

				for(Task t : tempFloatingTasks) {
					if(t.getTaskString().equals(temp.getTaskString())) {
						UI.ui.eraseScreen();
						Logic.crud.viewIndividualTask(counter);
						arraylistsHaveBeenModified = true;
						break;
					}
					counter++;
				}
			}
			catch(Exception e) {
			}
		}
		 */	}


	public static void displayCommand(String s) {
		if (s.equals("completed") || s.equals("c")) {
			Logic.crud.displayCompletedTasks();
		} else if (s.equals("floating") || s.equals("f")) {
			Logic.crud.displayFloatingTasks();
		} else if (Logic.checkDate.checkDateformat(s)) {
			Logic.crud.displayScheduleForADay(s);
		} else if (s.equals("all")) {
			Logic.crud.displayUncompletedAndFloatingTasks();
		} else if (s.equals("")){
			Logic.crud.displayUpcomingTasks();
		} else if (s.equals("today")) {
			Calendar today = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String todayString = df.format(today.getTime());
			Logic.crud.displayScheduleForADay(todayString);
		} else if (s.equals("tomorrow")) {
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.add(Calendar.DAY_OF_MONTH, 1);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String tomorrowString = df.format(tomorrow.getTime());

			Logic.crud.displayScheduleForADay(tomorrowString);
		} else if (s.equals("week") || s.equals("this week")) {
			Logic.crud.displayTasksForThisWeek();
		} else if (s.equals("next week") || s.equals("w+1")) {
			Logic.crud.displayTasksForNextWeek();
		} else if (s.equals("two weeks later") || s.equals("w+2")) {
			Logic.crud.displayTaksForTwoWeeksLater();
		} else if (s.equals("last week") || s.equals("w -1")) {
			Logic.crud.displayTasksForLastWeek();
		}  else {
			Logic.crud.displayByLabel(s);
		}
	}

	public static void deleteCommand(String s) {
		if ((Logic.Head.getLastDisplay().equals("d") == true || Logic.Head.getLastDisplay().equals("display")) == true) {
			if(Logic.Head.getLastDisplayArg().equals("")) {
				if(s.contains("all")!= true) { 
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Logic.crud.getTemp();
					Task deleted = list.get(num - 1);
					issue = deleted.getIssue();
					UI.ui.eraseScreen();
					try {
						Logic.crud.deleteTask(num - 1, 5);
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
					Logic.crud.displayNearestFiveDeleteUncompleteTaskList(num - 1);
					arraylistsHaveBeenModified = true;
				}
				else {
					try {
						String[] tmp = s.split(" ");
						if (s.contains("all")) {
							int num = Integer.parseInt(tmp[1]);
							Task t = delAllRecurringTask(num - 1);
							UI.ui.printGreen("All recurring tasks with issue " + t.getIssue() + " have been deleted");
							arraylistsHaveBeenModified = true;
						} else {
							int num = Integer.parseInt(s);
							ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
							ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
							if (list.size() + list2.size() == 0) {
								UI.ui.printRed(MSG_EMPTY);
							} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
								// handle indexOutofBoundException
								UI.ui.printRed(MSG_TASK_DES_NOT_EXIST);
							} else {
								if ((num - 1) < list.size()) {
									Task deleted = list.get(num - 1);
									issue = deleted.getIssue();
									UI.ui.eraseScreen();
									Logic.crud.deleteTask(num - 1, 1);
									UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
									Logic.crud.displayNearestFiveDeleteUncompleteTaskList(num - 1);
									arraylistsHaveBeenModified = true;
								} else {
									Task deleted = list2.get(num - list.size() - 1);
									issue = deleted.getIssue();
									Logic.crud.deleteTask(num - 1, 1);
									UI.ui.eraseScreen();
									UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
									Logic.crud.displayNearestFiveDeleteFloatingTask(num - 1);
									arraylistsHaveBeenModified = true;
								}
							}
						}
					} catch (Exception e) {
					}
				} 
			} else if(Logic.Head.getLastDisplayArg().equals("completed") || Logic.Head.getLastDisplayArg().equals("c")) {
				if(s.contains("all")!= true) {
					try {
						int num = Integer.parseInt(s);
						ArrayList<Task> list = Storage.localStorage.getCompletedTasks();
						if (list.size() == 0) {
							UI.ui.printRed(MSG_EMPTY);
						} else if (list.size() < num || num - 1 < 0) {
							// handle indexOutofBoundException
							UI.ui.printRed(MSG_TASK_DES_NOT_EXIST);
						} else {
							Task deleted = list.get(num - 1);
							issue = deleted.getIssue();
							Logic.crud.deleteTask(num - 1, 2);
							UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
							arraylistsHaveBeenModified = true;
						}
					} catch (Exception e) {
					}
				}
				else {
					try {
						String[] tmp = s.split(" ");
						if (s.contains("all")) {
							int num = Integer.parseInt(tmp[1]);
							Task t = delAllRecurringTask(num - 1);
							UI.ui.printGreen("All recurring tasks with issue " + t.getIssue() + " have been deleted");
							arraylistsHaveBeenModified = true;
						} else {
							int num = Integer.parseInt(s);
							ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
							ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
							if (list.size() + list2.size() == 0) {
								UI.ui.printRed(MSG_EMPTY);
							} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
								// handle indexOutofBoundException
								UI.ui.printRed(MSG_TASK_DES_NOT_EXIST);
							} else {
								if ((num - 1) < list.size()) {
									Task deleted = list.get(num - 1);
									issue = deleted.getIssue();
									UI.ui.eraseScreen();
									Logic.crud.deleteTask(num - 1, 1);
									UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
									Logic.crud.displayNearestFiveDeleteUncompleteTaskList(num - 1);
									arraylistsHaveBeenModified = true;
								} else {
									Task deleted = list2.get(num - list.size() - 1);
									issue = deleted.getIssue();
									Logic.crud.deleteTask(num - 1, 1);
									UI.ui.eraseScreen();
									UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
									Logic.crud.displayNearestFiveDeleteFloatingTask(num - 1);
									arraylistsHaveBeenModified = true;
								}
							}
						}
					} catch (Exception e) {
					}
				}
			}
			else if(Logic.Head.getLastDisplayArg().equals("all")) {
				if(s.contains("all") != true) {
					try {
						int num = Integer.parseInt(s);
						ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
						ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
						if (list.size() + list2.size() == 0) {
							UI.ui.printRed(MSG_EMPTY);
						} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
							// handle indexOutofBoundException
							UI.ui.printRed(MSG_TASK_DES_NOT_EXIST);
						} else {
							if ((num - 1) < list.size()) {
								Task deleted = list.get(num - 1);
								issue = deleted.getIssue();
								Logic.crud.deleteTask(num - 1, 1);
								UI.ui.eraseScreen();
								UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
								Logic.crud.displayNearestFiveDeleteUncompleteTaskList(num - 1);
								arraylistsHaveBeenModified = true;
							} else {
								Task deleted = list2.get(num - list.size() - 1);
								issue = deleted.getIssue();
								Logic.crud.deleteTask(num - 1, 1);
								UI.ui.eraseScreen();
								UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
								Logic.crud.displayNearestFiveDeleteFloatingTask(num - 1);
								arraylistsHaveBeenModified = true;
							}
						}
					} catch (Exception e) {
					}
				}
				else {
					try {
						String[] tmp = s.split(" ");
						if (s.contains("all")) {
							int num = Integer.parseInt(tmp[1]);
							Task t = delAllRecurringTask(num - 1);
							UI.ui.printGreen("All recurring tasks with issue " + t.getIssue() + " have been deleted");
							arraylistsHaveBeenModified = true;
						} else {
							int num = Integer.parseInt(s);
							ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
							ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
							if (list.size() + list2.size() == 0) {
								UI.ui.printRed(MSG_EMPTY);
							} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
								// handle indexOutofBoundException
								UI.ui.printRed(MSG_TASK_DES_NOT_EXIST);
							} else {
								if ((num - 1) < list.size()) {
									Task deleted = list.get(num - 1);
									issue = deleted.getIssue();
									UI.ui.eraseScreen();
									Logic.crud.deleteTask(num - 1, 1);
									UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
									Logic.crud.displayNearestFiveDeleteUncompleteTaskList(num - 1);
									arraylistsHaveBeenModified = true;
								} else {
									Task deleted = list2.get(num - list.size() - 1);
									issue = deleted.getIssue();
									Logic.crud.deleteTask(num - 1, 1);
									UI.ui.eraseScreen();
									UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
									Logic.crud.displayNearestFiveDeleteFloatingTask(num - 1);
									arraylistsHaveBeenModified = true;
								}
							}
						}
					} catch (Exception e) {
					}
				}
			}
		} else if ((Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s"))) {
			// delete from search results
			if(s.contains("all")!=true) {
				try {
					int num = Integer.parseInt(s);
					ArrayList<Task> list = Logic.Search.getSearchedTasks();
					if (list.size() == 0) {
						UI.ui.printRed(MSG_EMPTY);
					} else if (list.size() < num || num - 1 < 0) {
						// handle indexOutofBoundException
						UI.ui.printRed(MSG_TASK_DES_NOT_EXIST);
					} else {
						Task deleted = list.get(num - 1);
						issue = deleted.getIssue();
						Logic.crud.deleteTask(num - 1, 3);
						UI.ui.eraseScreen();
						UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
						arraylistsHaveBeenModified = true;
					}
				} catch (Exception e) {
				}
			}
			else {
				try {
					String[] tmp = s.split(" ");
					if (s.contains("all")) {
						int num = Integer.parseInt(tmp[1]);
						Task t = delAllRecurringTask(num - 1);
						UI.ui.printGreen("All recurring tasks with issue " + t.getIssue() + " have been deleted");
						arraylistsHaveBeenModified = true;
					} else {
						int num = Integer.parseInt(s);
						ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
						ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
						if (list.size() + list2.size() == 0) {
							UI.ui.printRed(MSG_EMPTY);
						} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
							// handle indexOutofBoundException
							UI.ui.printRed(MSG_TASK_DES_NOT_EXIST);
						} else {
							if ((num - 1) < list.size()) {
								Task deleted = list.get(num - 1);
								issue = deleted.getIssue();
								UI.ui.eraseScreen();
								Logic.crud.deleteTask(num - 1, 1);
								UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
								Logic.crud.displayNearestFiveDeleteUncompleteTaskList(num - 1);
								arraylistsHaveBeenModified = true;
							} else {
								Task deleted = list2.get(num - list.size() - 1);
								issue = deleted.getIssue();
								Logic.crud.deleteTask(num - 1, 1);
								UI.ui.eraseScreen();
								UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
								Logic.crud.displayNearestFiveDeleteFloatingTask(num - 1);
								arraylistsHaveBeenModified = true;
							}
						}
					}
				} catch (Exception e) {
				}

			}
		}
	}

	/*else {
			try {
				int num = Integer.parseInt(s);
				ArrayList<Task> list = Storage.localStorage.getUncompletedTasks();
				ArrayList<Task> list2 = Storage.localStorage.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					UI.ui.printRed(MSG_EMPTY);
				} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
					// handle indexOutofBoundException
					UI.ui.printRed(MSG_TASK_DES_NOT_EXIST);
				} else {
					if ((num - 1) < list.size()) {
						Task deleted = list.get(num - 1);
						issue = deleted.getIssue();
						Logic.crud.deleteTask(num - 1, 1);
						UI.ui.eraseScreen();
						UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
						Logic.crud.displayNearestFiveDeleteUncompleteTaskList(num - 1);
						arraylistsHaveBeenModified = true;
					} else {
						Task deleted = list2.get(num - list.size() - 1);
						issue = deleted.getIssue();
						Logic.crud.deleteTask(num - 1, 1);
						UI.ui.eraseScreen();
						UI.ui.printGreen("\"" + issue + "\" " + MSG_DELETE);
						Logic.crud.displayNearestFiveDeleteFloatingTask(num - 1);
						arraylistsHaveBeenModified = true;
					}
				}
			} catch (Exception e) {
			}
		}*/


```
###### main\Storage\fileStorage.java
``` java
package Storage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Task.Task;

public class fileStorage {

	/**
	 * Function that saves the given arraylist into a file named as the String fileName
	 * 
	 */
	public static void saveFile(String fileName, ArrayList<Task> details) throws IOException {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			for (Task t : details) {
				oos.writeObject(t);
			}
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
```
###### main\Storage\localStorage.java
``` java
package Storage;
import Task.Task;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class localStorage {

	//ArrayLists to store the contents added to the file
	private static ArrayList<Task> uncompletedTasks = new ArrayList<Task>();
	private static ArrayList<Task> completedTasks = new ArrayList<Task>();
	private static ArrayList<Task> floatingTasks = new ArrayList<Task>();


	//getter methods
	public static ArrayList<Task> getUncompletedTasks() {
		return uncompletedTasks;
	}

	public static ArrayList<Task> getCompletedTasks() {
		return completedTasks;
	}

	public static ArrayList<Task> getFloatingTasks() {
		return floatingTasks;
	}
	

	public static Task getUncompletedTask(int index) {
		Task temp = null;
		for(int i = 0; i<uncompletedTasks.size(); i++) {
			if(i == index) {
				temp = uncompletedTasks.get(i);
			}
		}
		return temp;
	}

	public static Task getCertainUncompletedTask(int index) {
		Task temp = null;
		if(index >= 0&& index < uncompletedTasks.size())
			return uncompletedTasks.get(index);
		else
			return temp;
	}

	public static Task getCompletedTask(int index) {
		Task temp = null;
		for(int i = 0; i<completedTasks.size(); i++) {
			if(i == index) {
				temp = completedTasks.get(i);
			}
		}
		return temp;
	}

	public static Task getFloatingTask(int index) {
		Task temp = null;
		for(int i = 0; i<floatingTasks.size(); i++) {
			if(i == index) {
				temp = floatingTasks.get(i);
			}
		}
		return temp;
	}

	//setter methods
	public static void setUncompletedTasks(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		uncompletedTasks = changedDetails;
	}

	public static void setFloatingTasks(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		floatingTasks = changedDetails;
	}

	public static void setCompletedTasks(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		completedTasks = changedDetails;
	}


	/**
	 * Function to set a task to a particular index
	 * @param index
	 * @param temp
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void setUncompletedTask(int index, Task temp) {
		uncompletedTasks.set(index, temp);
	}

	public static void setCompletedTask(int index, Task temp) {
		completedTasks.set(index, temp);
	}

	public static void setFloatingTask(int index, Task temp) {
		floatingTasks.set(index, temp);
	}

	/**
	 * Function to add a task to the uncompleted task list
	 * 
	 * @param task contains the task to be added
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void addToUncompletedTasks(Task task) {
		uncompletedTasks.add(task);
	}

	/**
	 * Function to add a task to the list of completed tasks
	 * 
	 * @param task the task to be added
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void addToCompletedTasks(Task task) throws IOException, ClassNotFoundException {
		completedTasks.add(task);
	}

	public static void addToFloatingTasks(Task task) {
		floatingTasks.add(task);
	}
	
	/**
	 * Function to delete a task from the file
	 * 
	 * @param index contains the index of the task to be deleted
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static Task delFromUncompletedTasks(int index) {
		Task temp = uncompletedTasks.remove(index);
		return temp;
	}

	/**
	 * Function to delete a task from the list of completed tasks
	 * 
	 * @param index contains the index of the task to be deleted
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static Task delFromCompletedTasks(int index) {
		Task temp = completedTasks.remove(index);
		return temp;
	}

	public static Task delFromFloatingTasks(int index) {
		Task temp = floatingTasks.remove(index);
		return temp;
	}
	/**
	 * Function to clear the contents of the file
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void clear() {
		uncompletedTasks.clear();
		completedTasks.clear();
		floatingTasks.clear();

	}

```
###### main\UI\ui.java
``` java
package UI;

import java.util.Scanner;

import org.fusesource.jansi.AnsiConsole;

import Parser.Natty;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
public class ui {
```
