# Jung Kai
###### main\Logic\Core.java
``` java
	/**
	 * Function to decipher if adding a normal or recurring task.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void addCommand() throws IOException, ClassNotFoundException {
		String description = parserObject.getDescription();
		String startDate = parserObject.getStartDate();
		String startDateWithTime = parserObject.getStartDateWithTime();
		String endDate = parserObject.getEndDate();
		String endDateWithTime = parserObject.getEndDateWithTime();
		String issue = parserObject.getIssueM();
		boolean recurrence = parserObject.getRecurrence();
		boolean containDate = parserObject.getContainDate();
		boolean isAdded;

		if (description.equals("")) {
			uiObject.printRed(MSG_INVALID);
		} else {
			// get index of key.
			if (containDate) {
				if (!recurrence) {
					addNormalTask(description, startDate, startDateWithTime, endDate, endDateWithTime, issue);
				} else { // for recurring tasks.
					addRecurringTask(description, startDate, startDateWithTime, endDate, endDateWithTime, issue);
				}
			} else {
				isAdded = crudObject.addTask(description);
				if (isAdded) {
					sortObject.sortTasksChronologically();
					int index = crudObject.uncompletedTaskIndexWithNoDate(description);
					uiObject.printGreen("\"" + description + "\" " + MSG_ADD);
					crudObject.displayNearestFiveFloating(index);
					arraylistsHaveBeenModified = true;
				} else {
					uiObject.printRed(MSG_DUPLICATE_ADD);
				}
			}
		}
	}

	/**
	 * Function to add a recurring task. 
	 * 
	 * @param description       the task description.
	 * @param startDate         the start date.
	 * @param endDate           the end date.
	 * @param startDateWithTime the start date along with the time.
	 * @param endDateWithTime   the end date along with the time.
	 * @param issue             the task description.
	 */
	public void addRecurringTask(String description, String startDate, String startDateWithTime, String endDate,
			String endDateWithTime, String issue) {
		if (startDate.equals("-") && !endDate.equals("-")) {// no start date but has end date.
			if (!checkDateObject.checkDateformat(endDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				uiObject.printRed(PROMPT_RECURRING);
				try {
					String in = sc.nextLine();
					String[] tmp = in.split(" ");
					String freq = tmp[0];
					String last = tmp[1];
					int frequency = Integer.parseInt(freq);

					Task task = new Task(issue, endDateWithTime, description, true, frequency, last);
					checkDateAndAdd(task);
					uiObject.printGreen("\"" + task.getIssue() + "\"" + " is added to the task list. (recurs every " 
					                    + freq + " days)");
					arraylistsHaveBeenModified = true;
				} catch (Exception e) {
					uiObject.printRed(MSG_INVALID);
					arraylistsHaveBeenModified = false;
				}
			}
		} else if ((!startDate.equals("-")) && endDate.equals("-")) {// has start date.
			if (!checkDateObject.checkDateformat(startDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				uiObject.printRed(PROMPT_RECURRING);
				try {
					String in = sc.nextLine();
					String[] tmp = in.split(" ");
					String freq = tmp[0];
					String last = tmp[1];
					int frequency = Integer.parseInt(freq);

					Task task = new Task(issue, startDateWithTime, description, true, frequency, last);
					checkDateAndAdd(task);
					uiObject.printGreen("\"" + task.getIssue() + "\"" + " is added to the task list. (recurs every " 
							            + freq + " days)");
					arraylistsHaveBeenModified = true;
				} catch (Exception e) {
					uiObject.printRed(MSG_INVALID);
					arraylistsHaveBeenModified = false;
				}
			}
		} else { // has both start date and end date.
			if (!checkDateObject.checkDateformat(startDate) && !checkDateObject.checkDateformat(endDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				uiObject.printRed(PROMPT_RECURRING);
				try {
					String in = sc.nextLine();
					String[] tmp = in.split(" ");
					String freq = tmp[0];
					String last = tmp[1];
					int frequency = Integer.parseInt(freq);

					Task task = new Task(issue, startDateWithTime, endDateWithTime, description, frequency,
							             last);
					uiObject.printGreen("\"" + task.getIssue() + "\"" + " is added to the task list. (recurs every " 
							            + freq + " days)");
					checkDateAndAdd(task);
					arraylistsHaveBeenModified = true;
				} catch (Exception e) {
					uiObject.printRed(MSG_INVALID);
					arraylistsHaveBeenModified = false;
				}
			}
		}
	}

	/**
	 * Function to add a normal task. 
	 * 
	 * @param description       the task description.
	 * @param startDate         the start date.
	 * @param endDate           the end date.
	 * @param startDateWithTime the start date along with the time.
	 * @param endDateWithTime   the end date along with the time.
	 * @param issue             the task description.
	 */
	public void addNormalTask(String description, String startDate, String startDateWithTime, String endDate,
			String endDateWithTime, String issue) throws IOException, ClassNotFoundException {
		boolean isAdded;
		if (startDate.equals("-") && !endDate.equals("-")) { // no start date but has end date.
			if (!checkDateObject.checkDateformat(endDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				isAdded = crudObject.addTaskWithEndDate(issue, endDateWithTime, description);
				if (isAdded) {
					sortObject.sortTasksChronologically();
					int index = crudObject.uncompletedTaskIndexWithEndDate(issue, endDateWithTime, description);
					uiObject.printGreen("\"" + issue + "\" " + MSG_ADD);
					arraylistsHaveBeenModified = true;
					crudObject.displayNearestFiveUncompleted(index);
				} else {
					uiObject.printRed(MSG_DUPLICATE_ADD);
				}
			}
		} else if ((!startDate.equals("-")) && endDate.equals("-")) { // has start date.
			if (!checkDateObject.checkDateformat(startDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				isAdded = crudObject.addTaskWithStartDate(issue, startDateWithTime, description);
				if (isAdded) {
					sortObject.sortTasksChronologically();
					int index = crudObject.uncompletedTaskIndexWithStartDate(issue, startDateWithTime, description);
					uiObject.printGreen("\"" + issue + "\" " + MSG_ADD);
					crudObject.displayNearestFiveUncompleted(index);
					arraylistsHaveBeenModified = true;
				} else {
					uiObject.printRed(MSG_DUPLICATE_ADD);
				}
			}
		} else { // has both start date and end date.
			if (!checkDateObject.checkDateformat(startDate) && !checkDateObject.checkDateformat(endDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				isAdded = crudObject.addTaskWithBothDates(issue, startDateWithTime, endDateWithTime, description);
				if (isAdded) {
					sortObject.sortTasksChronologically();
					int index = crudObject.uncompletedTaskIndexWithBothDates(issue, startDateWithTime,
							                                                 endDateWithTime, description);
					uiObject.printGreen("\"" + issue + "\" " + MSG_ADD);
					crudObject.displayNearestFiveUncompleted(index);
					arraylistsHaveBeenModified = true;
				} else {
					uiObject.printRed(MSG_DUPLICATE_ADD);
				}
			}
		}
	}
	
	/**
	 * Function to set priority for all instances of a recurring task.
	 */
	private void setAllRecurringTasksPriorityCommand() {
		String description = parserObject.getDescription();
		String[] tmp = description.split(" ");
		String idx = tmp[1];
		int num = Integer.parseInt(idx);

		ArrayList<Task> list = localStorageObject.getUncompletedTasks();

		if (list.size() == 0) {
			uiObject.printRed(MSG_EMPTY);
		} else if (list.size() < num || num - 1 < 0) {
			uiObject.printRed(MSG_PRIORITY_FAIL);
		} else {
			uiObject.printYellow("Enter priority");
			String priority = sc.nextLine();
			markObject.setRecurringTasksPriority(num - 1, priority);
			arraylistsHaveBeenModified = true;
		}
	}

```
###### main\Logic\Core.java
``` java
		try {
			if (num < 0) {
				uiObject.printRed(MSG_INVALID);
			} else if (description.contains("all")) {
				editRecurringTask(num - 1);
			} else {
				// check if user input integer is valid. If it is valid, edit
				// should work
				ArrayList<Task> list = localStorageObject.getUncompletedTasks();
				ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					uiObject.printRed(MSG_EMPTY);
				} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
					uiObject.printRed(MSG_EDIT_FAIL);
				} else {
					uiObject.printGreen(PROMPT_EDIT);
					crudObject.copyEditingTask(num);
					input = sc.nextLine();
					input = Natty.getInstance().parseEditString(input);
					input = "edit " + input;
					parserObject.parse(input);
					description = parserObject.getDescription();
					String startDate = parserObject.getStartDate();
					String startDateWithTime = parserObject.getStartDateWithTime();
					String endDate = parserObject.getEndDate();
					String endDateWithTime = parserObject.getEndDateWithTime();
					String issue = parserObject.getIssueM();
					boolean rec = parserObject.getRecurrence();
					boolean containDate = parserObject.getContainDate();
					if (description.equals("")) {
						uiObject.printRed(MSG_INVALID);
					} else {
						if (containDate) {
							if (!rec) { // has a date.
								editATaskWithDate(num, description, startDate, startDateWithTime, endDate,
										          endDateWithTime, issue);
							}
						} else {// no end date and no start date.
							editTaskWithNoDate(num, description);
						}
					}
				}
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to edit a task with no date.
	 * 
	 * @param num                     the index of the task to be edited.
	 * @param description             the updated description.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void editTaskWithNoDate(int num, String description) throws IOException, ClassNotFoundException {
		crudObject.editTaskWithNoDate(input, input, num - 1);
		int index = crudObject.uncompletedTaskIndexWithNoDate(description);
		uiObject.printGreen("Task number " + num + MSG_EDIT);
		crudObject.displayNearestFiveFloating(index);
		arraylistsHaveBeenModified = true;
	}

	/**
	 * Function to edit a task with a date.
	 * 
	 * @param num                     the index of the task to be edited.
	 * @param description             the updated description.
	 * @param startDate               the updated start date.
	 * @param startDateWithTime       the updated start date with time.
	 * @param endDate                 the update end date.
	 * @param endDateWithTime         the update end date with time.
	 * @param issue                   the updated task description.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void editATaskWithDate(int num, String description, String startDate, String startDateWithTime,
			                      String endDate, String endDateWithTime, String issue) 
			                      throws IOException, ClassNotFoundException {
		if (startDate.equals("-") && !endDate.equals("-")) {
			if (!checkDateObject.checkDateformat(endDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				crudObject.editTaskWithEndDate(issue, endDateWithTime, description, num - 1);
				sortObject.sortTasksChronologically();

				int index = crudObject.uncompletedTaskIndexWithEndDate(issue, endDateWithTime,
						description);
				uiObject.printGreen("Task number " + num + MSG_EDIT);
				crudObject.displayNearestFiveUncompleted(index);
				arraylistsHaveBeenModified = true;
			}
		} else if ((!startDate.equals("-")) && endDate.equals("-")) {// has start date.
			if (!checkDateObject.checkDateformat(startDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				crudObject.editTaskWithStartDate(issue, startDateWithTime, description,
						num - 1);
				sortObject.sortTasksChronologically();
				int index = crudObject.uncompletedTaskIndexWithStartDate(issue,
						startDateWithTime, description);

				uiObject.printGreen("Task number " + num + MSG_EDIT);
				crudObject.displayNearestFiveUncompleted(index);
				arraylistsHaveBeenModified = true;
			}
		} else { // has both start date and end date.
			if (!checkDateObject.checkDateformat(startDate)
					&& !checkDateObject.checkDateformat(endDate)) {
				uiObject.printRed(MSG_WRONG_DATE);
			} else {
				crudObject.editTaskWithBothDates(issue, startDateWithTime, endDateWithTime,
						description, num - 1);
				uiObject.printGreen("Task number " + num + MSG_EDIT);
				sortObject.sortTasksChronologically();
				int index = crudObject.uncompletedTaskIndexWithBothDates(issue,
						startDateWithTime, endDateWithTime, input);
				crudObject.displayNearestFiveUncompleted(index);
				arraylistsHaveBeenModified = true;
			}
		}
	}

```
###### main\Logic\Core.java
``` java
	/**
	 * Function to delete all instances of a recurring task from the list of tasks.
	 */
	public void deleteAllRecurringTasks() {
		String s = parserObject.getDescription();
		try {
			String[] tmp = s.split(" ");
			if (s.contains("all")) {
				int num = Integer.parseInt(tmp[1]);
				boolean isDeleted = delAllRecurringTask(num - 1);
				if (isDeleted) {
					uiObject.printGreen("All instances of Task " + num + " have been deleted");
				} else {
					uiObject.printRed("Not a recurring tasks. Enter delete/d followed by index to delete this task");
				}
				arraylistsHaveBeenModified = isDeleted;
			} else {
				int num = Integer.parseInt(s);
				ArrayList<Task> list = localStorageObject.getUncompletedTasks();
				ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
				if (list.size() + list2.size() == 0) {
					uiObject.printRed(MSG_EMPTY);
				} else if ((list2.size() + list.size()) < num || num - 1 < 0) {
					uiObject.printRed(MSG_TASK_DES_NOT_EXIST);
				} else {
					if ((num - 1) < list.size()) {
						Task deleted = list.get(num - 1);
						issue = deleted.getIssue();

						crudObject.deleteTask(num - 1, 1);
						uiObject.printGreen("\"" + issue + "\" " + MSG_DELETE);
						crudObject.displayNearestFiveDeleteUncompleteTaskList(num - 1);
						arraylistsHaveBeenModified = true;
					} else {
						Task deleted = list2.get(num - list.size() - 1);
						issue = deleted.getIssue();
						crudObject.deleteTask(num - 1, 1);

						uiObject.printGreen("\"" + issue + "\" " + MSG_DELETE);
						crudObject.displayNearestFiveDeleteFloatingTask(num - 1);
						arraylistsHaveBeenModified = true;
					}
				}
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to convert an array to a string.
	 * 
	 * @param arr
	 * @return String
	 */
	public String arrayToString(String[] arr) {
		String temp = Arrays.toString(arr);
		temp = temp.substring(1, temp.length() - 1).replaceAll(", ", " ");
		return temp;
	}

	

	/**
	 * Function that returns the index of first keyword present in the String[] and
	 * return -1 if no keyword is present.
	 * 
	 * @param arr      the user input.
	 *   
	 * @return integer the index of the first keyword.
	 */
	public int getIndexOfKey(String[] arr) {
		int idx = -1;
		for (int j = 0; j < arr.length; j++) {
			for (int i = 0; i < KEY.length; i++) {
				if (arr[j].equals(KEY[i])) {
					idx = j;
				}
			}
		}
		return idx;
	}

	/**
	 * Function that returns the index of "from" from the input String[] and return
	 * -1 if no starting index is present.
	 * 
	 * @param arr      the user date input.
	 * 
	 * @return integer the index of the first occurrence of 'from' in the user date input.
	 */
	public int getStartingIndex(String[] arr) {
		int idx = -1;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals("from") || arr[i].equals("on")) {
				idx = i;
			}
		}
		return idx;
	}

	/**
	 * Function that returns a boolean value to indicate if input string[] contains starting time.
	 * 
	 * @param arr      the user date input.
	 * 
	 * @return boolean true if the input contains a starting time, false otherwise.
	 */
	public boolean hasStartTime(String[] arr) {
		boolean containTime = true;
		int start = getStartingIndex(arr);
		// if date is the last argument => no time.
		if (start + 2 >= arr.length) {
			containTime = false;
		} else {
			if (!checkDateObject.checkTimeformat(arr[start + 2])) {
				containTime = false;
			}
		}
		return containTime;
	}

	/**
	 * Function that returns a boolean value to indicate if input String[] contains ending time.
	 * 
	 * @param arr      the user date input.
	 * 
	 * @return boolean true if the input contains a starting time, false otherwise.
	 */
	public boolean hasEndTime(String[] arr) {
		boolean containTime = true;
		int end = getIndexOfKey(arr);
		// if date is the last argument => no time.
		if (end + 2 >= arr.length) {
			containTime = false;

		} else {
			if (!checkDateObject.checkTimeformat(arr[end + 2])) {
				containTime = false;
			}
		}
		return containTime;
	}


	/**
	 * Function that processes Date for recurring tasks based on the date and number
	 * of recurring tasks calculated.
	 * 
	 * @param s  the input date entered by the user.
	 * @param n  the number of recurring tasks.
	 * 
	 * @return   the processed date.
	 */
	public String processDate(String s, int n) {
		String[] temp = s.split("/");
		temp[0] = String.valueOf(Integer.parseInt(temp[0]) + n);
		YearMonth yearMonthObject;
		yearMonthObject = YearMonth.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[1]));
		int daysInMonth = yearMonthObject.lengthOfMonth();
		if (Integer.parseInt(temp[0]) > daysInMonth) {
			temp[0] = String.valueOf(Integer.parseInt(temp[0]) - daysInMonth);
			temp[1] = String.valueOf(Integer.parseInt(temp[1]) + 1);
		}
		if (temp[0].length() == 1) {
			temp[0] = "0" + temp[0];
		}
		if (temp[1].length() == 1) {
			temp[1] = "0" + temp[1];
		}

		String tmp = arrayToString(temp);
		tmp = tmp.replaceAll(" ", "/");

		return tmp;

	}

	/**
	 * Function that deletes all recurring task at index n from recurring tasks and
	 * uncompleted tasks in storage.
	 * 
	 * @param n                       the index of the recurring task to be deleted.
	 * 
	 * @return                        true if the edit is successful.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public boolean delAllRecurringTask(int n) throws ClassNotFoundException, IOException {
		ArrayList<Task> list = localStorageObject.getUncompletedTasks();
		Task deleted = list.get(n);

		String id = deleted.getId();
		if (id.equals("")) {// if the task is not recurring task.
			return false;
		} else {
			for (int i = 0; i < list.size(); i++) {// delete from uncompleted tasks.
				Task task = list.get(i);
				if (id.equals(task.getId())) {
					crudObject.deleteTask(i, 1);
					i = -1;
				}
			}
			return true;
		}
	}

	/**
	 * Function that supports the editing of recurring task at index n.
	 * 
	 * @param n                       the index of the recurring task to be edited.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void editRecurringTask(int n) throws ClassNotFoundException, IOException {
		Task replaced = localStorageObject.getUncompletedTask(n);
		if (replaced.getId().equals("")) { // if task is not a recurring task, stop & inform user.
			uiObject.printRed(MSG_EDIT_NOT_RECURRING_TASK_HEAD + (n + 1) + MSG_EDIT_NOT_RECURRING_TASK_TAIL);
			return;
		}
		
		crudObject.copyTask(replaced);
		uiObject.printRed("Enter new description and deadline of recurring tasks");
		String in = sc.nextLine();
		in = Natty.getInstance().parseEditString(in); // get index of key.
		String[] s2 = in.split("`");

		if (s2.length == 2) {
			String[] temp = s2[1].split(" ");
			int start = getStartingIndex(temp); // start has value of -1.
			int end = getIndexOfKey(temp);
			issue = s2[0];
			
			if (start == -1 && end != -1) {// no start date but has end date.
				startDate = "-";
				startTime = "-";
				endDate = temp[end + 1]; // read date & time.
				endDateWithTime = endDate;
				if (hasEndTime(temp)) {// check if contain end time.
					endTime = temp[end + 2];
					endTime = endTime.replaceAll(":", "/");
					endDateWithTime = endDateWithTime + "/" + endTime;
				} else {
					endTime = "-";
				}

				if (!checkDateObject.checkDateformat(endDate)) {
					uiObject.printRed(MSG_WRONG_DATE);
				} else {
					uiObject.printRed(PROMPT_RECURRING);
					try {
						String in2 = sc.nextLine();
						String[] tmp = in2.split(" ");
						String freq = tmp[0];
						String last = tmp[1];
						int frequency = Integer.parseInt(freq);

						Task task = new Task(issue, endDateWithTime, in, false, frequency, last);
						checkDateAndAdd(task);
						arraylistsHaveBeenModified = true;
						delAllRecurringTask(n);
						uiObject.printGreen("All instances of Task " + (n + 1) + " has been edited and saved");
					} catch (Exception e) {
						uiObject.printRed(MSG_INVALID);
						arraylistsHaveBeenModified = false;
					}
				}
			} else if (start != -1 && end == -1) {// has start date but no end date.
				endDate = "-";
				endTime = "-";
				startDate = temp[start + 1];
		
				startDateWithTime = startDate;

				if (hasStartTime(temp)) {
					startTime = temp[start + 2];
					startTime = startTime.replaceAll(":", "/");
					startDateWithTime = startDateWithTime + "/" + startTime;
				} else {
					startTime = "-";
				}
				if (!checkDateObject.checkDateformat(startDate)) {
					uiObject.printRed(MSG_WRONG_DATE);
				} else {
					uiObject.printRed(PROMPT_RECURRING);
					try {
						String in2 = sc.nextLine();
						String[] tmp = in2.split(" ");
						String freq = tmp[0];
						String last = tmp[1];
						int frequency = Integer.parseInt(freq);

						Task task = new Task(issue, endDateWithTime, in, true, frequency, last);
						checkDateAndAdd(task);
						arraylistsHaveBeenModified = true;
						delAllRecurringTask(n);
						uiObject.printGreen("All instances of Task " + (n + 1) + " has been edited and saved");
					} catch (Exception e) {
						uiObject.printRed(MSG_INVALID);
						arraylistsHaveBeenModified = false;
					}
				}
			} else { // has both start date and end date.
				startDate = temp[start + 1];
				endDate = temp[end + 1];
		
				endDateWithTime = endDate;
				startDateWithTime = startDate;
				if (hasStartTime(temp)) {
					startTime = temp[start + 2];
					startTime = startTime.replaceAll(":", "/");
					startDateWithTime = startDateWithTime + "/" + startTime;
				} else {
					startTime = "-";
				}
				if (hasEndTime(temp)) {
					endTime = temp[end + 2];
					endTime = endTime.replaceAll(":", "/");
					endDateWithTime = endDateWithTime + "/" + endTime;
				} else {
					endTime = "-";
				}
				if (!checkDateObject.checkDateformat(startDate) && !checkDateObject.checkDateformat(endDate)) {
					uiObject.printRed(MSG_WRONG_DATE);
				} else {
					uiObject.printRed(PROMPT_RECURRING);
					try {
						String in2 = sc.nextLine();
						String[] tmp = in2.split(" ");
						String freq = tmp[0];
						String last = tmp[1];
						int frequency = Integer.parseInt(freq);
						
						Task task = new Task(issue, startDateWithTime, endDateWithTime, in, frequency, last);
						checkDateAndAdd(task);
						arraylistsHaveBeenModified = true;
						delAllRecurringTask(n);
						uiObject.printGreen("All instances of Task " + (n + 1) + " has been edited and saved");
					} catch (Exception e) {
						uiObject.printRed(MSG_INVALID);
						arraylistsHaveBeenModified = false;
					}
				}
			}
		}
	}

	/**
	 * Function to check if date is valid.
	 * 
	 * @param task the task for which the date is checked.
	 */
	public void checkDateAndAdd(Task task) {
		try {
			String id = task.getId();
			String ed = task.getDateCompare();
			boolean expired = isExpired(ed, task.getLastDate());

			while (true) {
				if (expired) {// If task is not within display time frame or when task expired.
					break;
				}
				localStorageObject.addToUncompletedTasks(task);
				String newED = processDate(ed, task.getFrequency());
				if (task.getStartDate() == null) {// no start date.
					task = new Task(task.getIssue(), newED, task.getMsg(), false, task.getFrequency(),
							task.getLastDate());
					task.setID(id);
				} else if (task.getEndDate() == null) {
					task = new Task(task.getIssue(), newED, task.getMsg(), true, task.getFrequency(),
							task.getLastDate());
					task.setID(id);
				} else {// has start date and end date.
					task = new Task(task.getIssue(), task.getFixedStartDateString(), newED, task.getMsg(),
							task.getFrequency(), task.getLastDate());
					task.setID(id);
				}
				ed = task.getDateCompare();
				expired = (isExpired(ed, task.getLastDate()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to check if recurring task is expired.
	 * 
	 * @param currentRecurDate the date of the particular instance of the recurring task.
	 * @param recurDeadline    the date of the last instance of the recurring task.
	 * 
	 * @return                 true if date has expired, false otherwise.
	 */
	public boolean isExpired(String currentRecurDate, String recurDeadline) {
		String[] splitCurrentRecurDate = currentRecurDate.split("/");
		String[] splitRecurDeadline = recurDeadline.split("/");
		if (Integer.parseInt(splitRecurDeadline[2]) < Integer.parseInt(splitCurrentRecurDate[2])) { // end year < current year.
			return true;
		} else if (Integer.parseInt(splitRecurDeadline[2]) > Integer.parseInt(splitCurrentRecurDate[2])) { // end year>current year
			return false;
		} else { // end year == current year
			if (Integer.parseInt(splitRecurDeadline[1]) > Integer.parseInt(splitCurrentRecurDate[1])) { // end month> current month
				return false;
			}
			if (Integer.parseInt(splitRecurDeadline[1]) < Integer.parseInt(splitCurrentRecurDate[1])) { // end month < today.
				return true;
			} else { // same year & month.
				if (Integer.parseInt(splitRecurDeadline[0]) >= Integer.parseInt(splitCurrentRecurDate[0])) { // valid date.
					return false;
				} else {
					return true;
				}
			}
		}
	}
}
```
###### main\Parser\Parser.java
``` java
package Parser;

import java.util.Arrays;

import Logic.CheckDate;

public class Parser {
	
	private static Parser parser;
	
	private static final String[] KEY = { "by", "at", "during", "before", "to", "in" };
	
	private boolean rec, containDate;
	private CheckDate checkDateObject;
	private String command, sd, stime, sdWithTime, ed, etime, edWithTime, originalMsg, issueM;
	
	private Parser() {
		checkDateObject = new CheckDate();
		containDate = false;
		rec = false;
		
		command = "";
		ed = "";
		edWithTime = "";
		etime = "";
		issueM = "";
		originalMsg = "";
		sd = "";
		sdWithTime = "";
		stime = "";
	}
	
	public static Parser getInstance() {
		if (parser == null) {
			parser = new Parser();
		}
		return parser;
	}

	public void parse(String description) {
		String[] splitCommand = description.split(" ");
		command = splitCommand[0];
		if (splitCommand.length == 1) {
			originalMsg = "";
			issueM = originalMsg;
			sd = "-";
			stime = "-";
			ed = "-";
			etime = "-";
			sdWithTime = "";
			edWithTime = "";
			rec = false;
		} else {
			rec = false;
			containDate = false;
			if (splitCommand[splitCommand.length - 1].equals("r")) {
				rec = true;
			}
			int i = description.indexOf(" ");
			originalMsg = description.substring(i + 1);
			issueM = originalMsg;
			sd = "-";
			stime = "-";
			ed = "-";
			etime = "-";
			sdWithTime = "";
			edWithTime = "";
			if (originalMsg.contains(" ` ")) {
				containDate = true;
				String[] splitDate = originalMsg.split(" ` ");
				issueM = splitDate[0];
				String[] temp = splitDate[1].split(" ");
				int start = getStartingIndex(temp); // start has value of -1
				int end = getIndexOfKey(temp);
				// end has value of -1 if it
				if (end < start) {
					end = -1;
				}
				if (start != -1) {
					sd = temp[start + 1];
					if (hasStartTime(temp)) {
						stime = temp[start + 2];
						stime = stime.replaceAll(":", "/");
						sdWithTime = sd + "/" + stime;
					} else {
						sdWithTime = sd;
					}
				}

				if (end != -1) {
					ed = temp[end + 1];
					if (hasEndTime(temp)) {
						etime = temp[end + 2];
						etime = etime.replaceAll(":", "/");
						edWithTime = ed + "/" + etime;
					} else {
						edWithTime = ed;
					}
				}
			}
		}
	}

	public String getStartDateWithTime() {
		return sdWithTime;
	}

	public String getEndDateWithTime() {
		return edWithTime;
	}

	public boolean getContainDate() {
		return containDate;
	}

	public boolean getRecurrence() {
		return rec;
	}

	public String getCommand() {
		return command;
	}

	public String getStartDate() {
		return sd;
	}

	public String getEndDate() {
		return ed;
	}

	public String getStartTime() {
		return stime;
	}

	public String getEndTime() {
		return etime;
	}

	public String getDescription() {
		return originalMsg;
	}

	public String getIssueM() {
		return issueM;
	}

```
###### main\Parser\Parser.java
``` java
	/**
	 * method that convert String[] to String
	 * 
	 * @param arr
	 * @return String
	 */
	public String arrayToString(String[] arr) {
		String temp = Arrays.toString(arr);
		temp = temp.substring(1, temp.length() - 1).replaceAll(", ", " ");
		return temp;
	}

	/**
	 * method that return the index of first keyword present in the String[] and
	 * return -1 if no keyword is present
	 * 
	 * @param arr
	 * @return Integer
	 */
	public int getIndexOfKey(String[] arr) {
		int idx = -1;
		for (int j = 0; j < arr.length; j++) {
			for (int i = 0; i < KEY.length; i++) {
				if (arr[j].equals(KEY[i])) {
					idx = j;
				}
			}
		}
		return idx;
	}

	/**
	 * Method that return the index of "from" or "on" from the input String[]
	 * and return -1 if no starting index is present
	 * 
	 * @param arr
	 * @return Integer
	 */
	public int getStartingIndex(String[] arr) {
		int idx = -1;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals("from") || arr[i].equals("on")) {
				idx = i;
			}

		}
		return idx;
	}

	/**
	 * method that return a boolean value to indicate if input string[] contains
	 * starting time
	 * 
	 * @param arr
	 * @return
	 */
	public boolean hasStartTime(String[] arr) {
		boolean containTime = true;
		int start = getStartingIndex(arr);
		// if date is the last argument => no time
		if (start + 2 >= arr.length) {
			containTime = false;
		} else {
			if (!checkDateObject.checkTimeformat(arr[start + 2])) {
				containTime = false;
			}
		}
		return containTime;
	}

	/**
	 * method that return a boolean value to indicate if input String[] contains
	 * ending time
	 * 
	 * @param arr
	 * @return boolean
	 */
	public  boolean hasEndTime(String[] arr) {
		boolean containTime = true;
		int end = getIndexOfKey(arr);
		// if date is the last argument => no time
		if (end + 2 >= arr.length) {
			containTime = false;

		} else {
			if (!checkDateObject.checkTimeformat(arr[end + 2])) {
				containTime = false;
			}
		}
		return containTime;
	}
}
```
###### main\unitTest\ParserTest.java
``` java
package unitTest;

import static org.junit.Assert.*;
import Parser.Parser;
import Parser.Natty;
import Task.Task;
import java.util.ArrayList;
import org.junit.Test;

public class ParserTest {
	private static final String input1 = "add floating task";
	private static final String input2 = "add task ` from 11/04/2016";
	private static final String input3 = "add task ` from 11/04/2016 12am";
	private static final String input4 = "add task ` by today";
	private static final String input5 = "add task ` by tomorrow 23:59";
	private static final String input6 = "add task ` from today 1am to tuesday 23:59";
	private static final String input7 = "edit 1";
	private static final String input8 = "mark 1";
	private static final String input9 = "display all";
	private static final String input10 = "priority 1";
	private static final String input11 = "view 1";
	private static final String input12 = "delete 1";
	private static final String input13 = "add recurring task ` by 11/04/2016 r";
	private static final String input14 = "search task";
	private static final String input15 = "clear";
	private static final String input16 = "undo";

	private String issue, startDate, endDate, description, processed;
	private boolean isRecurring;
	protected Parser parse;
	protected Natty natty;

	/**
	 * To set up this test
	 */
	protected void initParser() {
		parse = Parser.getInstance();
	}

	protected void initNatty() {
		natty = Natty.getInstance();
	}

	@Test
	public void testAdd() {
		initParser();
		initNatty();
		issue = "floating task";
		processed = natty.parseString(input1);
		parse.parse(processed);
		assertEquals("add", parse.getCommand());
		assertEquals(issue,parse.getIssueM());

		issue = "task";
		startDate = "11/04/2016";
		processed = natty.parseString(input2);
		parse.parse(processed);
		assertEquals("add",parse.getCommand());
		assertEquals(issue,parse.getIssueM());
		assertEquals(startDate,parse.getStartDate());

		startDate = "11/04/2016/00/00";
		processed = natty.parseString(input3);
		parse.parse(processed);
		assertEquals("add",parse.getCommand());
		assertEquals(issue,parse.getIssueM());
		assertEquals(startDate,parse.getStartDateWithTime());

		endDate = "11/04/2016";
		processed = natty.parseString(input4);
		parse.parse(processed);
		assertEquals("add",parse.getCommand());
		assertEquals(issue,parse.getIssueM());
		assertEquals(endDate,parse.getEndDate());

		endDate = "12/04/2016/23/59";
		processed = natty.parseString(input5);
		parse.parse(processed);
		assertEquals("add",parse.getCommand());
		assertEquals(issue,parse.getIssueM());
		assertEquals(endDate,parse.getEndDateWithTime());

		startDate = "11/04/2016/01/00";
		processed = natty.parseString(input6);
		parse.parse(processed);
		assertEquals( "add",parse.getCommand());
		assertEquals(issue,parse.getIssueM());
		assertEquals(startDate,parse.getStartDateWithTime());
		assertEquals(endDate,parse.getEndDateWithTime());

		endDate = "11/04/2016";
		issue = "recurring task";
		processed = natty.parseString(input13);
		parse.parse(processed);
		assertEquals("add",parse.getCommand());
		assertEquals(issue,parse.getIssueM());
		assertEquals(endDate,parse.getEndDate());
		assertTrue(parse.getRecurrence());

	}

	@Test
	public void testCommandWithIndex() {
		initParser();
		description = "1";
		parse.parse(input7);
		assertEquals("edit",parse.getCommand());
		assertEquals(description,parse.getDescription());
		
		parse.parse(input8);
		assertEquals("mark",parse.getCommand());
		assertEquals(description,parse.getDescription());

		parse.parse(input10);
		assertEquals("priority",parse.getCommand());
		assertEquals(description,parse.getDescription());

		
		parse.parse(input11);
		assertEquals("view",parse.getCommand());
		assertEquals(description,parse.getDescription());

		parse.parse(input12);
		assertEquals("delete",parse.getCommand());
		assertEquals(description,parse.getDescription());
	}

	@Test
	public void testCommandWithNoIndex() {
		initParser();
		description = "all";
		parse.parse(input9);
		assertEquals("display",parse.getCommand());
		assertEquals(description,parse.getDescription());
		
		description = "task";
		parse.parse(input14);
		assertEquals("search",parse.getCommand());
		assertEquals(description,parse.getDescription());

		description = "";
		parse.parse(input15);
		assertEquals("clear",parse.getCommand());
		assertEquals(description,parse.getDescription());

		description = "";
		parse.parse(input16);
		assertEquals("undo",parse.getCommand());
		assertEquals(description,parse.getDescription());
	}

}
```
