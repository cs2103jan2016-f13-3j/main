# Kowshik
###### main\Logic\Core.java
``` java
	/**
	 * Function that take in command and the body as the argument and process them to to meet the requests of the user.
	 * 
	 * @return true                   if storage has been modified, false otherwise.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean parseCommands() throws IOException, ClassNotFoundException {
		arraylistsHaveBeenModified = false;
		String option = parserObject.getCommand();

		if (option.equals("add") || option.equals("a") || option.equals("+")) {
			addCommand();
		} else if (option.equals("display") || option.equals("d")) {
			displayCommand();
		} else if (option.equals("edit") || option.equals("e")) {
			editCommand();
		} else if (option.equals("delete") || option.equals("-")) {
			deleteCommand();
		} else if (option.equals("view") || option.equals("v")) {
			viewCommand();
		} else if (option.equals("search") || option.equals("s")) {
			searchObject.searchTasksByKeyword(parserObject.getDescription());
		} else if (option.equals("mark") || option.equals("m")) {
			markCommand();
		} else if (option.equals("unmark") || option.equals("um")) {
			unmarkCommand();
		} else if (option.equals("priority") || option.equals("p")) {
			String s = parserObject.getIssueM();
			if (s.contains("all")) {
				setAllRecurringTasksPriorityCommand();
			} else {
				setPriorityCommand();
			}
		} else if (option.equals("history")) {
			String pastCommands = Undo.getInstance().viewPastCommands();
			uiObject.printYellow(pastCommands);
		} else if (option.equals("future")) {
			String possibleRedoCommands = Undo.getInstance().viewRedoCommands();
			uiObject.printYellow(possibleRedoCommands);
		} else if (option.equals("undo")) {
			undoCommand();
		} else if (option.equals("redo")) {
			redoCommand();
		} else if (option.equals("help")) {
			helpObject.printHelpMenu();
		} else if (option.equals("dir")) {
			changeDirectoryCommand();
		} else if (option.equals("label")) {
			setLabelCommand();
		} else if (option.equals("clear") || option.equals("c")) {
			clearCommand();
		} else if (option.equals("exit")) {
			uiObject.printGreen("Bye!");
			AnsiConsole.systemUninstall();
			crudObject.exit();
		} else {
			uiObject.printRed(MSG_INVALID);
		}
		return arraylistsHaveBeenModified;
	}

```
###### main\Logic\Core.java
``` java
	/**
	 * Function to add a label to a task.
	 */
	public void setLabelCommand() {
		String description = parserObject.getDescription();
		try {
			int num = Integer.parseInt(description);
			ArrayList<Task> list = localStorageObject.getUncompletedTasks();
			ArrayList<Task> list2 = localStorageObject.getFloatingTasks();

			if (list.size() + list2.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				uiObject.printRed(MSG_PRIORITY_FAIL);
			} else {
				uiObject.print("Enter label");
				String label = sc.nextLine();
				crudObject.addLabelToTask(num - 1, label);
				arraylistsHaveBeenModified = true;
				Task temp = localStorageObject.getUncompletedTask(num - 1);
				String issue = temp.getIssue();
				uiObject.printGreen("Task " + issue + " has been labelled " + label);
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}
	
	/**
	 * Function to edit a task.
	 */
	public void editCommand() {
		int num;
		String description = parserObject.getDescription();
		num = findTheLastDisplay(description);

```
###### main\Logic\Core.java
``` java
	/**
	 * Function to find the last display.
	 * 
	 * @param description the command entered by the user.
	 * 
	 * @return            the index of the task to be edited.
	 */
	public int findTheLastDisplay(String description) {
		int num;
		if (description.contains("all")) {
			String[] tmp = description.split(" ");
			num = Integer.parseInt(tmp[1]);
		} else {
			num = Integer.parseInt(description);
		}
		if (Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if (Logic.Head.getLastDisplayArg().equals("floating") || Logic.Head.getLastDisplayArg().equals("all")) {
				if (description.contains("all")) {
					String[] tmp = description.split(" ");
					num = Integer.parseInt(tmp[1]);
				} else {
					num = Integer.parseInt(description);
				}
			} else {
				num = getCorrectIndexFromDisplayAll(num);
			}
		} else if (Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s")) {
			num = getCorrectIndexFromSearchView(num);
		} else if (Logic.Head.getLastDisplay().equals("")) {
			num = getCorrectIndexWelcomeView(num - 1);
		}
		return num;
	}

	/**
	 * Function to get the index of the last from search "view".
	 * 
	 * @param num the current index of the task.
	 * 
	 * @return    the actual index of the task from the storage arraylist.
	 */
	public int getCorrectIndexFromSearchView(int num) {
		Task temp = searchObject.getSearchedTask(num - 1);
		ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
		ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();

		num = getCorrectIndexFromStorage(num, temp, tempUncompletedTasks, tempFloatingTasks);
		return num;
	}

	/**
	 * Function to get the actual index of the task from storage arraylist.
	 * 
	 * @param num                   the current index of the task.
	 * @param temp                  the task.
	 * @param tempUncompletedTasks  the arraylist containing the uncompleted tasks.
	 * @param tempFloatingTasks     the arraylist containing the floating tasks.
	 * 
	 * @return                      the actual index of the task.
	 */
	public int getCorrectIndexFromStorage(int num, Task temp, ArrayList<Task> tempUncompletedTasks,
			                              ArrayList<Task> tempFloatingTasks) {
		int counter = 1;
		for (Task t : tempUncompletedTasks) {
			if (t.getTaskString().equals(temp.getTaskString())) {
				num = counter;
				break;
			}
			counter++;
		}
		counter++;
		
		for (Task t : tempFloatingTasks) {
			if (t.getTaskString().equals(temp.getTaskString())) {
				num = counter;
				break;
			}
			counter++;
		}
		return num;
	}

	/**
	 * Function to get the index of the last from display all "view".
	 * 
	 * @param num the current index of the task.
	 * 
	 * @return    the actual index of the task from the storage arraylist.
	 */
	public int getCorrectIndexFromDisplayAll(int num) {
		try {
			Task temp = crudObject.getTempTask(num - 1);

			ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
			ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();

			num = getCorrectIndexFromStorage(num, temp, tempUncompletedTasks, tempFloatingTasks);
			return num;
			
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Function to get the index of the last from welcome "view".
	 * 
	 * @param num the current index of the task.
	 * 
	 * @return    the actual index of the task from the storage arraylist.
	 */
	public int getCorrectIndexWelcomeView(int num) {
		Task temp;
		try {
			temp = notificationObject.getSpecificTask(num);
		} catch (IndexOutOfBoundsException e) {
			return INVALID_TASK_INDEX;
		}

		ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
		ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();

		num = getCorrectIndexFromStorage(num, temp, tempUncompletedTasks, tempFloatingTasks);
		return num;
	}

	/**
	 * Function to determine which "view" to set priority from.
	 */
	public void setPriorityCommand() {
		if (Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if (Logic.Head.getLastDisplayArg().equals("all") || Logic.Head.getLastDisplayArg().equals("floating")) {
				setPriorityFromDisplayAllView();
			} else {
				setPriorityFromDisplayView();
			}
		} else if (Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s")) {
			setPriorityFromSearchTaskView();
		} else if (Logic.Head.getLastDisplay().equals("")) {
			setPriorityFromDisplayAllView();
		} else {
			setPriorityFromDisplayAllView();
		}
	}

	/**
	 * Function to set priority to task from display "view".
	 */
	public void setPriorityFromDisplayView() {
		ArrayList<Task> list = crudObject.getTemp();
		try {
			String s = parserObject.getDescription();
			int num = Integer.parseInt(s);
			int oldNum = num;
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_PRIORITY_FAIL);
			} else {
				String priority = acceptPriorityFromUser();
				uiObject.printGreen(crudObject.getTempTask(oldNum - 1).getIssue() + " has been set to " + priority 
						            + " priority");
				num = getCorrectIndexFromDisplayAll(num);
				markObject.setPriority(num - 1, priority);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to set priority from search "view".
	 */
	public void setPriorityFromSearchTaskView() {
		try {
			String s = parserObject.getDescription();
			int num = Integer.parseInt(s);
			int oldNum = num;
			ArrayList<Task> list = searchObject.getSearchedTasks();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_PRIORITY_FAIL);
			} else {
				String priority = acceptPriorityFromUser();
				uiObject.printGreen(searchObject.getSearchedTask(oldNum - 1).getIssue() + " has been set to " + priority
						            + " priority");
				num = getCorrectIndexFromSearchView(num);
				markObject.setPriority(num - 1, priority);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to set priority from the display all "view".
	 */
	public void setPriorityFromDisplayAllView() {
		try {
			String s = parserObject.getDescription();
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getUncompletedTasks();
			ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
			if (list.size() + list2.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				uiObject.printRed(MSG_PRIORITY_FAIL);
			} else {
				String priority = acceptPriorityFromUser();

				int sizeOfUncompletedTasksList = localStorageObject.getUncompletedTasks().size();
				if (num <= localStorageObject.getUncompletedTasks().size()) {
					uiObject.printGreen(localStorageObject.getUncompletedTask(num - 1).getIssue() + " has been set to "
							            + priority + " priority");
				} else {
					uiObject.printGreen(
							localStorageObject.getFloatingTask(num - sizeOfUncompletedTasksList - 1).getIssue()
									                           + " has been set to " + priority + " priority");
				}
				markObject.setPriority(num - 1, priority);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}
	
	/**
	 * Function to accept priority from user.
	 * 
	 * @return the priority entered by the user.
	 */
	public String acceptPriorityFromUser() {
		uiObject.printYellow("Enter priority");
		String priority = sc.nextLine();
		while ((priority.equals("high") != true) && (priority.equals("medium") != true)
				&& priority.equals("low") != true) {
			uiObject.printRed("Invalid priority entered. Please enter high, medium or low.");
			priority = sc.nextLine();
		}
		return priority;
	}

	/**
	 * Function to unmark a completed task.
	 */
	public void unmarkCommand() {
		try {
			String s = parserObject.getDescription();
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getCompletedTasks();
			
			if (list.size() == 0) {
				uiObject.printRed(MSG_NO_COMPLETED_TASKS);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_UNMARK_FAIL);
			} else {
				Task temp = crudObject.getCompletedTask(num - 1);
				markObject.markTaskAsUncompleted(num - 1);
				uiObject.printGreen("\"" + temp.getIssue() + "\"" + MSG_UNMARK);
				crudObject.displayNearestFiveUnmarkCompleteTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to determine which "view" to mark a task from.
	 */
	public void markCommand() {
		String s = parserObject.getDescription();
		if (Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if (Logic.Head.getLastDisplayArg().equals("all") || Logic.Head.getLastDisplayArg().equals("floating")) {
				markFromDisplayAllView(s);
			} else {
				markFromDisplayView();
			}
		} else if (Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s")) {
			markFromSearchView();
		} else if (Logic.Head.getLastDisplay().equals("")) {
			int num = getCorrectIndexWelcomeView(Integer.parseInt(s) - 1);
			String index = "" + num;
			markFromDisplayAllView(index);
		} else {
			markFromDisplayAllView(s);
		}
	}

	/**
	 * Function to mark a task from search "view".
	 */
	public void markFromSearchView() {
		String s = parserObject.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = searchObject.getSearchedTasks();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_MARK_FAIL);
			} else {
				Task temp = list.get(num - 1);
				ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
				ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();
				int counter = 0;
				for (Task t : tempUncompletedTasks) {
					if (t.getTaskString().equals(temp.getTaskString())) {
						markObject.markTaskAsCompleted(counter);
						break;
					}
					counter++;
				}
				for (Task t : tempFloatingTasks) {
					if (t.getTaskString().equals(temp.getTaskString())) {
						markObject.markTaskAsCompleted(counter);
						break;
					}
					counter++;
				}
				uiObject.printGreen("\"" + temp.getIssue() + "\"" + MSG_MARK);
				crudObject.displayNearestFiveCompletedTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to mark a task from display all "view".
	 * @param s
	 */
	public void markFromDisplayAllView(String s) {
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getUncompletedTasks();
			ArrayList<Task> list2 = localStorageObject.getFloatingTasks();
			if (list.size() + list2.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if ((list.size() + list2.size()) < num || num - 1 < 0) {
				uiObject.printRed(MSG_MARK_FAIL);
			} else {
				Task temp = crudObject.getUncompletedTask(num - 1);
				markObject.markTaskAsCompleted(num - 1);
				uiObject.printGreen("\"" + temp.getIssue() + "\"" + MSG_MARK);
				crudObject.displayNearestFiveCompletedTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to mark a task from welcome "view".
	 */
	public void markFromDisplayView() {
		String s = parserObject.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = crudObject.getTemp();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_MARK_FAIL);
			} else {
				Task temp = crudObject.getTempTask(num - 1);
				num = getCorrectIndexFromDisplayAll(num);
				markObject.markTaskAsCompleted(num - 1);
				uiObject.printGreen("\"" + temp.getIssue() + "\"" + MSG_MARK);
				crudObject.displayNearestFiveCompletedTaskList(temp);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to clear all the tasks in the storage.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void clearCommand() throws ClassNotFoundException, IOException {
		crudObject.clearTasks();
		uiObject.printGreen(MSG_CLEAR);
		arraylistsHaveBeenModified = true;
	}

	/**
	 * Function to determine which "view" should the details of the task be shown from.
	 */
	public void viewCommand() {
		String s = parserObject.getDescription();
		if (Logic.Head.getLastDisplay().equals("display") || Logic.Head.getLastDisplay().equals("d")) {
			if (Logic.Head.getLastDisplayArg().equals("all") || Logic.Head.getLastDisplayArg().equals("floating")) {
				int num = Integer.parseInt(s);
				crudObject.viewIndividualTask(num - 1);
			} else {
				viewFromDisplayView();
			}
		} else if (Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s")) {
			viewFromSearchView();
		} else if (Logic.Head.getLastDisplay().equals("")) {
			int num = getCorrectIndexWelcomeView(Integer.parseInt(s) - 1);
			crudObject.viewIndividualTask(num - 1);
		} else {
			try {
				int num = Integer.parseInt(s);
				crudObject.viewIndividualTask(num - 1);
			} catch (Exception e) {
				uiObject.printRed(MSG_INVALID);
			}
		}
	}

	/**
	 * Function to view a task from search "view".
	 */
	public void viewFromSearchView() {
		String s = parserObject.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = searchObject.getSearchedTasks();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed("Invalid index entered");
			} else {
				Task temp = list.get(num - 1);
				ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
				ArrayList<Task> tempFloatingTasks = localStorageObject.getFloatingTasks();
				int counter = 0;
				for (Task t : tempUncompletedTasks) {
					if (t.getTaskString().equals(temp.getTaskString())) {
						crudObject.viewIndividualTask(counter);
						arraylistsHaveBeenModified = true;
						break;
					}
					counter++;
				}
				for (Task t : tempFloatingTasks) {
					if (t.getTaskString().equals(temp.getTaskString())) {
						crudObject.viewIndividualTask(counter);
						arraylistsHaveBeenModified = true;
						break;
					}
					counter++;
				}
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to view a task from display "view".
	 */
	public void viewFromDisplayView() {
		String s = parserObject.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = crudObject.getTemp();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed("Wrong index entered");
			} else {
				num = getCorrectIndexFromDisplayAll(num);
				crudObject.viewIndividualTask(num - 1);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to read which display the user has entered.
	 */
	public void displayCommand() {
		String s = parserObject.getDescription();
		if (s.equals("completed") || s.equals("c")) {
			crudObject.displayCompletedTasks();
		} else if (s.equals("floating") || s.equals("f")) {
			crudObject.displayFloatingTasks();
		} else if (checkDateObject.checkDateformat(s)) {
			crudObject.displayScheduleForADay(s);
		} else if (s.equals("all")) {
			crudObject.displayUncompletedAndFloatingTasks();
		} else if (s.equals("")) {
			crudObject.displayUpcomingTasks();
		} else if (s.equals("today")) {
			Calendar today = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String todayString = df.format(today.getTime());
			crudObject.displayScheduleForADay(todayString);
		} else if (s.equals("tomorrow")) {
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.add(Calendar.DAY_OF_MONTH, 1);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String tomorrowString = df.format(tomorrow.getTime());
			crudObject.displayScheduleForADay(tomorrowString);
		} else if (s.equals("next week") || s.equals("w+1")) {
			crudObject.displayTasksForNextWeek();
		} else if (s.equals("two weeks later") || s.equals("w+2")) {
			crudObject.displayTaksForTwoWeeksLater();
		} else if (s.equals("last week") || s.equals("w -1")) {
			crudObject.displayTasksForLastWeek();
		} else {
			crudObject.displayByLabel(s);
		}
	}

	/**
	 * Function to determine from which "view" task should be deleted.
	 */
	public void deleteCommand() {
		String s = parserObject.getDescription();
		if ((Logic.Head.getLastDisplay().equals("d") == true || Logic.Head.getLastDisplay().equals("display")) == true) {
			if (Logic.Head.getLastDisplayArg().equals("all") || Logic.Head.getLastDisplayArg().equals("floating")) {
				if (s.contains("all") != true) {
					deleteFromDisplayAllView(s);
				} else {
					deleteAllRecurringTasks();
				}
			} else if (Logic.Head.getLastDisplayArg().equals("completed")|| Logic.Head.getLastDisplayArg().equals("c")) {
				if (s.contains("all") != true) {
					deleteFromDisplayCompletedView();
				} else {
					deleteAllRecurringTasks();
				}
			} else { // this is "display" only.
				if (s.contains("all") != true) {
					deleteFromDisplayView();
				} else {
					deleteAllRecurringTasks();
				}
			}
		} else if ((Logic.Head.getLastDisplay().equals("search") || Logic.Head.getLastDisplay().equals("s"))) {
			// delete from search results.
			if (s.contains("all") != true) {
				deleteFromSearchView();
			} else {
				deleteAllRecurringTasks();
			}
		} else if (Logic.Head.getLastDisplay().equals("")) {
			if (s.contains("all") != true) {
				int num = getCorrectIndexWelcomeView(Integer.parseInt(s) - 1);
				if (num == -1) { // -1 when storage is empty, and user tries to delete immediately after launch.
					uiObject.printRed(MSG_EMPTY);
					return;
				}

				String index = "" + num;
				deleteFromDisplayAllView(index);
			} else {
				deleteAllRecurringTasks();
			}
		} else {
			deleteFromDisplayAllView(s);
		}
	}

	/**
	 * Function to delete a task from display "view".
	 */
	public void deleteFromDisplayView() {
		String s = parserObject.getDescription();
		String[] splitInput = s.split(" ");
		try {
			int num;
			if (splitInput.length == 2) {
				num = Integer.parseInt(splitInput[1]);
			} else {
				num = Integer.parseInt(splitInput[0]);
			}

			ArrayList<Task> list = crudObject.getTemp();
			Task deleted = list.get(num - 1);
			issue = deleted.getIssue();
			try {
				crudObject.deleteTask(num - 1, 5);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			uiObject.printGreen("\"" + issue + "\" " + MSG_DELETE);
			crudObject.displayNearestFiveDeleteUncompleteTaskList(num - 1);
			arraylistsHaveBeenModified = true;
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to delete a task from display completed "view".
	 */
	public void deleteFromDisplayCompletedView() {
		String s = parserObject.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = localStorageObject.getCompletedTasks();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_TASK_DES_NOT_EXIST);
			} else {
				Task deleted = list.get(num - 1);
				issue = deleted.getIssue();
				crudObject.deleteTask(num - 1, 2);
				uiObject.printGreen("\"" + issue + "\" " + MSG_DELETE);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to delete a task from display completed "view".
	 */
	public void deleteFromSearchView() {
		String s = parserObject.getDescription();
		try {
			int num = Integer.parseInt(s);
			ArrayList<Task> list = searchObject.getSearchedTasks();
			if (list.size() == 0) {
				uiObject.printRed(MSG_EMPTY);
			} else if (list.size() < num || num - 1 < 0) {
				uiObject.printRed(MSG_TASK_DES_NOT_EXIST);
			} else {
				Task deleted = list.get(num - 1);
				issue = deleted.getIssue();
				crudObject.deleteTask(num - 1, 3);

				uiObject.printGreen("\"" + issue + "\" " + MSG_DELETE);
				arraylistsHaveBeenModified = true;
			}
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

	/**
	 * Function to delete a task from display all "view".
	 * 
	 * @param s the index of the task to be deleted.
	 */
	public void deleteFromDisplayAllView(String s) {
		try {
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
		} catch (Exception e) {
			uiObject.printRed(MSG_INVALID);
		}
	}

```
###### main\Logic\Crud.java
``` java
package Logic;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import Storage.LocalStorage;
import Task.Task;
import UI.UI;

public class Crud {
	private static Crud crud;

	private static ArrayList<Task> tempTasks = new ArrayList<Task>();
	private static boolean noDuplicate;
	private static Task tempTask;

	private static final String FLAG_COMPLETED = "completed";
	private static final String FLAG_FLOATING = "floating";
	private static final String FLAG_UNCOMPLETED = "uncompleted";
	private static final String MSG_NO_TASK_UNDER_THIS_LABEL = "There is no task under this label";
	private static final String MSG_NO_TASK = "There are no tasks to show.";
	private static final String MSG_INVALID = "Invalid inputs! Please try again";

	private LocalStorage localStorageObject;
	private Search searchObject;
	private Sort sortObject = new Sort();
	private UI uiObject;

	// Private constructor, following the singleton pattern.
	private Crud() {
		localStorageObject = LocalStorage.getInstance();
		searchObject = Search.getInstance();
		sortObject = new Sort();
		uiObject = new UI();
	}

	/**
	 * Method to access this class, following the singleton pattern. 
	 * Invokes constructor if Crud has not been initialised.
	 * 
	 * @return The Crud object.
	 */
	public static Crud getInstance() {
		if (crud == null) {
			crud = new Crud();
		}
		return crud;
	}

	// Getter methods.
	public ArrayList<Task> getTemp() {
		return tempTasks;
	}

	public Task getTempTask(int index) {
		return tempTasks.get(index);
	}

	/**
	 * Function to get a task from the list in Uncompleted Task List
	 * 
	 * @param index
	 * @return
	 */
	public Task getUncompletedTask(int index) {
		int size1 = localStorageObject.getUncompletedTasks().size();
		if (index < size1) {
			return localStorageObject.getUncompletedTask(index);
		} else {

			return localStorageObject.getFloatingTask(index - size1);
		}
	}

	public Task getCompletedTask(int index) {
		return localStorageObject.getCompletedTask(index);
	}
	/**
	 * Function to add task without time.
	 * 
	 * @param line                    the task to be added.
	 * 
	 * @return                        true if no duplicate found, false other.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean addTask(String line) throws IOException, ClassNotFoundException {
		Task task = new Task(line);
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();
		boolean noDuplicate = true;
		for (Task temp : tempTasks) {
			if (temp.getTaskString().equals(task.getTaskString())) {
				noDuplicate = false;
			}
		}
		if (noDuplicate) {
			localStorageObject.addToFloatingTasks(task);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Function to add a task with only start date.
	 * 
	 * @param line                   task description to be added.
	 * @param date                   start date of the task to be added.
	 * @param msg                    the task description and the start date to be added.
	 * 
	 * @return                       true if no duplicate is found, false otherwise.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean addTaskWithStartDate(String line, String date, String msg)
			throws IOException, ClassNotFoundException {
		Task task = new Task(line, date, msg, true);

		boolean noDuplicate = true;
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();
		for (Task temp : tempTasks) {
			if (temp.getTaskString().equals(task.getTaskString())) {
				uiObject.printRed(temp.getTaskString());
				noDuplicate = false;
			}
		}
		if (noDuplicate) {
			localStorageObject.addToUncompletedTasks(task);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Function to add a task with only end date.
	 * 
	 * @param line                   task description to be added.
	 * @param date                   end date of the task to be added.
	 * @param msg                    the task description and the end
           
	 * @return                       true if no duplicate if found, false otherwise.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean addTaskWithEndDate(String line, String date, String msg) throws IOException, ClassNotFoundException {
		Task task = new Task(line, date, msg, false);

		boolean noDuplicate = true;
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();
		for (Task temp : tempTasks) {
			if (temp.getTaskString().equals(task.getTaskString())) {
				uiObject.printRed(temp.getTaskString());
				noDuplicate = false;
			}
		}
		if (noDuplicate) {
			localStorageObject.addToUncompletedTasks(task);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Function to add a task with both start date and end date.
	 * 
	 * @param line                    task description to be added.
	 * @param startDate               start date of the task to be added.
	 * @param endDate                 end date of the task to be added.
	 * @param msg                     the task description and both the dates to be added.
	 * 
	 * @return                        true if no duplicate is found, false otherwise.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean addTaskWithBothDates(String line, String startDate, String endDate, String msg)
			throws IOException, ClassNotFoundException {
		Task task = new Task(line, startDate, endDate, msg);

		boolean noDuplicate = true;
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();
		for (Task temp : tempTasks) {
			if (temp.getTaskString().equals(task.getTaskString())) {
				uiObject.printRed(temp.getTaskString());
				noDuplicate = false;
			}

			if (temp.getStartDate() != null && temp.getEndDate() != null) {
				if (temp.getStartDateString().equals(task.getStartDateString())
						&& temp.getEndDateString().equals(task.getEndDateString())) {
					uiObject.printRed("CLASH IN TIMING DETECTED WITH - ");
					uiObject.printRed(temp.getTaskString());
				}
			}
		}
		if (noDuplicate) {
			localStorageObject.addToUncompletedTasks(task);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Function to add a label to a task.
	 * 
	 * @param index  index of the task for which label is be added.
	 * @param label  label to be added.
	 */
	public void addLabelToTask(int index, String label) {
		int sizeOfUncompletedTasks = localStorageObject.getUncompletedTasks().size();
		if (index < sizeOfUncompletedTasks) {
			Task temp = localStorageObject.getUncompletedTask(index);
			temp.setLabel(label);
			localStorageObject.setUncompletedTask(index, temp);
		} else {
			Task temp = localStorageObject.getFloatingTask(index);
			temp.setLabel(label);
			localStorageObject.setFloatingTask(index, temp);
		}
	}

```
###### main\Logic\Crud.java
``` java
	/**
	 * Function to edit a task (edited task has no date).
	 * 
	 * @param line                    the edited task description. 
	 * @param date                    the edited task date.
	 * @param message                 the edited task description with the edited task date.
	 * @param index                   index of the task to be edited.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void editTaskWithNoDate(String line, String message, int index) throws IOException, ClassNotFoundException {
		int uncompleteList = localStorageObject.getUncompletedTasks().size();

		if (index < uncompleteList) {
			deleteTask(index, 1);
			addTask(message);
		} else {
			Task temp = localStorageObject.getFloatingTask(index - uncompleteList);
			temp.setStartDate(null);
			temp.setEndDate(null);
			temp.setDescription(message);
			temp.setIssue(line);
			localStorageObject.setFloatingTask(index - uncompleteList, temp);
		}
	}

	/**
	 * Function to edit a task (edited task has start date).
	 * 
	 * @param line                    the edited task description. 
	 * @param date                    the edited task date.
	 * @param message                 the edited task description with the edited task date.
	 * @param index                   index of the task to be edited.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void editTaskWithStartDate(String line, String date, String message, int index)
			                          throws IOException, ClassNotFoundException {
		int uncompleteList = localStorageObject.getUncompletedTasks().size();

		if (index < uncompleteList) {
			Task temp = localStorageObject.getUncompletedTask(index);
			if (!temp.getIssue().equals(line)) {
				temp.resetID();
			}
			temp.setIssue(line);
			temp.setDescription(message);
			temp.setEndDate(null);
			temp.setStartDate(date);
			localStorageObject.setUncompletedTask(index, temp);
		} else {
			Task temp = localStorageObject.getFloatingTask(index - uncompleteList);
			temp.setIssue(line);
			temp.setDescription(message);
			temp.setEndDate(null);
			temp.setStartDate(date);
			localStorageObject.deleteFromFloatingTasks(index - uncompleteList);
			localStorageObject.addToUncompletedTasks(temp);
		}
	}

	/**
	 * Function to edit a task (edited task has end date).
	 * 
	 * @param line                    the edited task description. 
	 * @param date                    the edited task date.
	 * @param message                 the edited task description with the edited task date.
	 * @param index                   index of the task to be edited.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void editTaskWithEndDate(String line, String date, String message, int index)
			                        throws IOException, ClassNotFoundException {
		int uncompleteList = localStorageObject.getUncompletedTasks().size();

		if (index < uncompleteList) {
			Task temp = localStorageObject.getUncompletedTask(index);
			if (!temp.getIssue().equals(line)) {
				temp.resetID();
			}
			temp.setIssue(line);
			temp.setStartDate(null);
			temp.setEndDate(date);
			temp.setDescription(message);

			localStorageObject.setUncompletedTask(index, temp);
		} else {
			Task temp = localStorageObject.getFloatingTask(index - uncompleteList);
			deleteTask(index, 1);
			temp.setDescription(message);
			temp.setIssue(line);
			temp.setStartDate(null);
			temp.setEndDate(date);
			addTaskWithEndDate(line, date, message);
		}
	}

	/**
	 * Function to edit a task (edited task has start and end dates).
	 * 
	 * @param line                    the edited task description. 
	 * @param startDate               the edited task start date.
	 * @param startDate               the edited task end date.
	 * @param message                 the edited task description with the edited task date.
	 * @param index                   index of the task to be edited.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void editTaskWithBothDates(String line, String startDate, String endDate, String message, int index)
			                          throws IOException, ClassNotFoundException {
		int uncompleteList = localStorageObject.getUncompletedTasks().size();
		if (index < uncompleteList) {
			Task temp = localStorageObject.getUncompletedTask(index);
			if (!temp.getIssue().equals(line)) {
				temp.resetID();
			}
			temp.setIssue(line);
			temp.setDescription(message);
			temp.setStartDate(startDate);
			temp.setEndDate(endDate);
			localStorageObject.setUncompletedTask(index, temp);
		} else {
			Task temp = localStorageObject.getFloatingTask(index - uncompleteList);
			deleteTask(index, 1);
			temp.setDescription(message);
			temp.setIssue(line);
			temp.setStartDate(startDate);
			temp.setEndDate(endDate);
			addTaskWithBothDates(line, startDate, endDate, message);
		}
	}

	/**
	 * Function to display all the completed tasks in the storage.
	 */
	public void displayCompletedTasks() {
		tempTasks = localStorageObject.getCompletedTasks();

		if (tempTasks.isEmpty()) {
			uiObject.printGreen("There is no stored task to display");
		} else {
			printCompletedTask(tempTasks);
		}
	}

	/**
	 * Function to display all floating task in storage.
	 */
	public void displayFloatingTasks() {
		tempTasks = localStorageObject.getFloatingTasks();
		ArrayList<Task> getSize = localStorageObject.getUncompletedTasks();
		if (tempTasks.isEmpty()) {
			uiObject.printGreen("There are no floating tasks to show.");
		}else{
			uiObject.printGreen("FLOATING TASKS");
			uiObject.printGreen("Index\tTask");
			for (int i = 0; i < tempTasks.size(); i++) {
				Task temp = tempTasks.get(i);
				uiObject.printYellow((getSize.size() + i + 1) + ".\t" + temp.getShortPriority() + temp.getIssue());
			}
		}

	}

```
###### main\Logic\Crud.java
``` java
	/**
	 * Function to display all the uncompleted tasks and storage in the storage.
	 */
	public void displayUncompletedAndFloatingTasks() {
		boolean isEmptyUn = false;
		tempTasks = localStorageObject.getUncompletedTasks();
		if (tempTasks.isEmpty()) {
			isEmptyUn = true;
		} else {
			printUncompletedTask(tempTasks);
		}

		boolean isEmptyF = false;
		tempTasks = localStorageObject.getFloatingTasks();
		if (tempTasks.isEmpty()) {
			isEmptyF = true;
		} else {
			printFloatingTasks();
		}
		if (isEmptyUn && isEmptyF) {
			uiObject.printGreen(MSG_NO_TASK);
		}
	}

	/**
	 * Function to display all the uncompleted tasks and storage in the storage.
	 */
	public void displayUpcomingTasks() {
		ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();

		// 7 days in advance
		Calendar sevenDaysLaterCalendar = Calendar.getInstance();
		sevenDaysLaterCalendar.add(Calendar.DAY_OF_MONTH, 7);

		// today
		Calendar todayCalendar = Calendar.getInstance();

		tempTasks = new ArrayList<Task>();
		findUpcomingTasks(tempUncompletedTasks, sevenDaysLaterCalendar);
		printUpcomingTasks(todayCalendar);
	}

	/**
	 * Function to find the upcoming tasks.
	 * 
	 * @param tempUncompletedTasks   the arraylist containing the uncompleted tasks.
	 * @param sevenDaysLaterCalendar the Calendar object of seven days after current day.
	 */
	public void findUpcomingTasks(ArrayList<Task> tempUncompletedTasks, Calendar sevenDaysLaterCalendar) {
		for (Task temp : tempUncompletedTasks) {
			if (temp.getEndDate() != null) {
				if (temp.getEndDate().compareTo(sevenDaysLaterCalendar) <= 0) {
					tempTasks.add(temp);
					continue;
				}
			} else if (temp.getStartDate() != null) {
				if (temp.getStartDate().compareTo(sevenDaysLaterCalendar) <= 0) {
					tempTasks.add(temp);
				}
			}
		}
	}
	
	/**
	 * Function to print the upcoming tasks.
	 * 
	 * @param todayCalendar the Calendar object of current day.
	 */
	public void printUpcomingTasks(Calendar todayCalendar) {
		if (tempTasks.size() > 0) {
			uiObject.printGreen("UNCOMPLETED TASKS");
			uiObject.printGreen("Index\tStart Date\tEnd Date\tTask");

			for (int i = 0; i < tempTasks.size(); i++) {
				Task temp = tempTasks.get(i);
				if (temp.getEndDate() != null) {
					int result = temp.getEndDate().get(Calendar.DAY_OF_YEAR) - todayCalendar.get(Calendar.DAY_OF_YEAR);
					String message = "";
					if (result < 0) {
						message = "overdue by " + Math.abs(result) + " days";
					} else if (result == 0) {
						message = "deadline today";
					}
					uiObject.printTask2(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(),
							            temp.getEndDateLineOne(), temp.getEndDateLineTwo(),
							            temp.getShortPriority() + temp.getIssue(), message);
				} else if (temp.getStartDate() != null) {
					int result = temp.getStartDate().get(Calendar.DAY_OF_YEAR)
							     - todayCalendar.get(Calendar.DAY_OF_YEAR);
					String message = "";
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
		} else {
			uiObject.printGreen(MSG_NO_TASK);
		}
	}
	
	/**
	 * Function to display the uncompleted tasks starting from/due next week.
	 */
	public void displayTasksForNextWeek() {
		uiObject.printGreen("Upcoming tasks next week - ");
		tempTasks = new ArrayList<Task>();
		ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();

		Calendar date = Calendar.getInstance();
		date.add(Calendar.WEEK_OF_YEAR, 1);
		int nextWeek = date.get(Calendar.WEEK_OF_YEAR);

		// finding tasks which has dates in the next week
		findTasksInThatWeek(tempUncompletedTasks, nextWeek);
		if (tempTasks.size() > 0) {
			printUncompletedTask(tempTasks);
		} else {
			uiObject.printRed("No tasks next week");
		}
	}

	/**
	 * Function to find the uncompleted tasks starting from/due in the respective week.
	 * 
	 * @param tempUncompletedTasks the arraylist containing the uncompleted tasks.
	 * @param nextWeek             the week number entered by the user.
	 */
	public void findTasksInThatWeek(ArrayList<Task> tempUncompletedTasks, int nextWeek) {
		for (Task temp : tempUncompletedTasks) {
			if (temp.getEndDate() != null) {
				if (temp.getEndDate().get(Calendar.WEEK_OF_YEAR) == nextWeek) {
					tempTasks.add(temp);
					continue;
				}
			}
			if (temp.getStartDate() != null) {
				if (temp.getStartDate().get(Calendar.WEEK_OF_YEAR) == nextWeek) {
					tempTasks.add(temp);
				}
			}
		}
	}

	/**
	 * Function to display the uncompleted tasks starting from/due two weeks later.
	 */
	public void displayTaksForTwoWeeksLater() {
		uiObject.printGreen("Upcoming tasks for two weeks later - ");
		tempTasks = new ArrayList<Task>();
		ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();

		Calendar date = Calendar.getInstance();
		date.add(Calendar.WEEK_OF_YEAR, 2);
		int twoWeeksLater = date.get(Calendar.WEEK_OF_YEAR);

		findTasksInThatWeek(tempUncompletedTasks, twoWeeksLater);
		if (tempTasks.size() > 0) {
			printUncompletedTask(tempTasks);

		} else {
			uiObject.printRed("No tasks for two weeks later");
		}
	}

	/**
	 * Function to display the uncompleted tasks starting from/due last week.
	 */
	public void displayTasksForLastWeek() {
		uiObject.printGreen("Tasks uncompleted from last week - ");
		tempTasks = new ArrayList<Task>();
		ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();

		Calendar date = Calendar.getInstance();
		date.add(Calendar.WEEK_OF_YEAR, -1);
		int lastWeek = date.get(Calendar.WEEK_OF_YEAR);

		findTasksInThatWeek(tempUncompletedTasks, lastWeek);

		if (tempTasks.size() > 0) {
			printUncompletedTask(tempTasks);
		} else {
			uiObject.printRed("No tasks left from last week");
		}
	}

	/**
	 * Function to display tasks with a particular label.
	 * 
	 * @param description the label entered by the user.
	 */
	public void displayByLabel(String description) {
		boolean hasTaskUnderThisLabel = false;

		tempTasks = new ArrayList<Task>();
		ArrayList<Task> displayResults = localStorageObject.getUncompletedTasks();

		for (Task temp : displayResults) {
			if (temp.getLabel().contains(description)) {
				tempTasks.add(temp);
			}
		}

		if (tempTasks.size() > 0) {
			hasTaskUnderThisLabel = true;
			printUncompletedTask(tempTasks);
		}

		tempTasks = new ArrayList<Task>();
		displayResults = localStorageObject.getFloatingTasks();
		for (Task temp : displayResults) {
			if (temp.getLabel().contains(description)) {
				tempTasks.add(temp);
			}
		}

		if (tempTasks.size() > 0) {
			hasTaskUnderThisLabel = true;
			printFloatingTasks();
			/*uiObject.printGreen("FLOATING TASKS");
			uiObject.printGreen("Index \t Task");
			ArrayList<Task> getSize = localStorageObject.getUncompletedTasks();
			for (int i = 0; i < tempTasks.size(); i++) {
				Task temp = tempTasks.get(i);
				uiObject.printYellow((getSize.size() + i + 1) + ".\t" + temp.getShortPriority() + temp.getIssue());
			}
			uiObject.print("________________________________");*/
		}

		tempTasks = new ArrayList<Task>();
		displayResults = localStorageObject.getCompletedTasks();
		for (Task temp : displayResults) {
			if (temp.getLabel().contains(description)) {
				tempTasks.add(temp);
			}
		}

		if (tempTasks.size() > 0) {
			hasTaskUnderThisLabel = true;
			printCompletedTask(tempTasks);
		}

		if (!hasTaskUnderThisLabel) {
			uiObject.printRed(MSG_NO_TASK_UNDER_THIS_LABEL);
		}
	}

	/**
	 * Function to display the schedule for a specific date.
	 * 
	 * @param inputDate the date entered by the user.
	 */
	public void displayScheduleForADay(String inputDate) {
		inputDate = inputDate.replace("/0", "/");
		if (inputDate.startsWith("0")) {
			inputDate = inputDate.replaceFirst("0", "");
		}
		String[] splitDate = inputDate.split("/");
		// run through all the tasks and find which have same date
		tempTasks = new ArrayList<Task>();
		ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();

		for (Task temp : tempUncompletedTasks) {
			if (temp.getStartDate() != null) {
				String startDay = "" + temp.getStartDate().get(Calendar.DAY_OF_MONTH);
				String startMonth = "" + (temp.getStartDate().get(Calendar.MONTH) + 1);
				String startYear = "" + temp.getStartDate().get(Calendar.YEAR);
				if (checkIfDateIsContained(splitDate, startDay, startMonth, startYear)) {
					tempTasks.add(temp);
					continue;
				}
			}
			if (temp.getEndDate() != null) {
				String endDay = "" + temp.getEndDate().get(Calendar.DAY_OF_MONTH);
				String endMonth = "" + (temp.getEndDate().get(Calendar.MONTH) + 1);
				String endYear = "" + temp.getEndDate().get(Calendar.YEAR);
				if (checkIfDateIsContained(splitDate, endDay, endMonth, endYear)) {
					tempTasks.add(temp);
				}
			}
		}

		if (tempTasks.isEmpty()) {
			uiObject.printGreen("There is no stored task to display");
		} else {
			sortObject.sortTasksPriority();
			printUncompletedTask(tempTasks);
		}
	}

	/**
	 * Function to check if two dates match.
	 * 
	 * @param splitDate the date entered by the user.
	 * @param day       the day to be checked for.
	 * @param month     the month to be checked for.
	 * @param year      the year to be checked for.
	 * 
	 * @return          true if date is contained, false otherwise.
	 */
	public boolean checkIfDateIsContained(String[] splitDate, String day, String month, String year) {
		if (day.equals(splitDate[0]) && month.equals(splitDate[1]) && year.equals(splitDate[2])) {
			return true;
		}
		return false;
	}

	/**
	 * Function to delete a task from the list of tasks.
	 * 
	 * @param index                    the index of the task to be deleted.
	 * @param listOfTasks              to indicate from which list, the task should be deleted.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void deleteTask(int index, int listOfTasks) throws ClassNotFoundException, IOException {
		if (listOfTasks == 1) { // delete from "display all" view
			ArrayList<Task> getSize = localStorageObject.getUncompletedTasks();
			if (index < getSize.size()) {
				localStorageObject.deleteFromUncompletedTasks(index);
			} else {
				localStorageObject.deleteFromFloatingTasks(index - getSize.size());
			}
		} else if (listOfTasks == 2) { // delete from completed tasks.
			localStorageObject.deleteFromCompletedTasks(index);
		} else if (listOfTasks == 3) { // delete from search completed tasks view.
			ArrayList<Task> searchTemp = searchObject.getSearchedTasks();
			Task taskToBeDeleted = searchTemp.get(index);
			ArrayList<Task> uncompletedTemp = localStorageObject.getUncompletedTasks();
			for (int i = 0; i < uncompletedTemp.size(); i++) {
				if (uncompletedTemp.get(i).equals(taskToBeDeleted)) {
					uncompletedTemp.remove(i);
					break;
				}
			}
		} else if (listOfTasks == 4) { // delete from floating tasks view.
			localStorageObject.deleteFromFloatingTasks(index);
		} else if (listOfTasks == 5) { // delete from "display" view.
			Task temp = tempTasks.get(index);
			ArrayList<Task> tempUncompletedTasks = localStorageObject.getUncompletedTasks();
			for (int i = 0; i < tempUncompletedTasks.size(); i++) {
				if (tempUncompletedTasks.get(i).getTaskString().equals(temp.getTaskString())) {
					tempUncompletedTasks.remove(i);
					break;
				}
			}
			localStorageObject.setUncompletedTasks(tempUncompletedTasks);
		}
	}

```
###### main\Logic\Crud.java
``` java
	/**
	 * Function to print the list of uncompleted tasks.
	 * 
	 * @param tempTask the list of uncompleted Tasks.
	 */
	public void printUncompletedTask(ArrayList<Task> tempTask) {
		uiObject.printGreen("UNCOMPLETED TASKS");
		uiObject.printGreen("Index\tStart Date\tEnd Date\tTask");

		for (int i = 0; i < tempTask.size(); i++) {
			Task temp = tempTask.get(i);
			uiObject.printTask1(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(), temp.getEndDateLineOne(),
					temp.getEndDateLineTwo(), temp.getShortPriority() + temp.getIssue(), temp.getRecurFrequency());
		}
		uiObject.print("________________________________________________________________");

	}

	/**
	 * Function to print the list of floating tasks.
	 */
	public void printFloatingTasks() {
		uiObject.printGreen("FLOATING TASKS");
		uiObject.printGreen("Index\tTask");
		
		ArrayList<Task> getSize = localStorageObject.getUncompletedTasks();
		for (int i = 0; i < tempTasks.size(); i++) {
			Task temp = tempTasks.get(i);
			uiObject.printYellow((getSize.size() + i + 1) + ".\t" + temp.getShortPriority() + temp.getIssue());
		}
	}
	
	/**
	 * Function to print the list of completed tasks.
	 * 
	 * @param tempTask the arraylist of tasks to be printed.
	 */

	public void printCompletedTask(ArrayList<Task> tempTask) {
		uiObject.printGreen("COMPLETED TASKS");
		uiObject.printGreen("Index\tStart Date\tEnd Date\tTask");
		for (int i = 0; i < tempTasks.size(); i++) {
			Task temp = tempTasks.get(i);
			uiObject.printTask1(i, temp.getStartDateLineOne(), temp.getStartDateLineTwo(), temp.getEndDateLineOne(),
					temp.getEndDateLineTwo(), temp.getIssue(), temp.getRecurFrequency());
		}
	}
	
	/**
	 * Function to check for duplicate tasks.
	 * 
	 * @param task        the task to be checked for.
	 * @param destination the arraylist containing the tasks.
	 * 
	 * @return            true if no duplicate, false otherwise.
	 */
	private boolean checkForDuplicateTasks(Task task, ArrayList<Task> destination) {
		boolean noDuplicate = true;
		for (Task temp : destination) {
			if (temp.getTaskString().equals(task.getTaskString())) {
				noDuplicate = false;
				break;
			}
		}
		return noDuplicate;
	}

	/**
	 * Function to display the details of an individual task.
	 * 
	 * @param index the index of the task to be displayed.
	 */
	public void viewIndividualTask(int index) {
		ArrayList<Task> getSize = localStorageObject.getUncompletedTasks();
		if (index < getSize.size()) {
			tempTask = localStorageObject.getUncompletedTask(index);
		} else {
			tempTask = localStorageObject.getFloatingTask(index - getSize.size());
		}
		if (tempTask == null) {
			uiObject.printRed(MSG_INVALID);
			return;
		}
		
		boolean isCompleted = tempTask.getCompletedStatus();
		String completed = "Not completed";
		if (isCompleted) {
			completed = "Completed";
		}

		uiObject.printYellow(tempTask.getTaskString());
		uiObject.print("Status: " + completed);
		uiObject.print("Priority: " + tempTask.getPriority());
		uiObject.print("Labels:");
		for (String label : tempTask.getLabel()) {
			uiObject.print(label);
		}
	}

	/**
	 * Function to delete all the tasks.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void clearTasks() throws ClassNotFoundException, IOException {
		localStorageObject.clearAllTasks();
	}

	/**
	 * Function to exit the application when user enters exit.
	 */
	public void exit() {
		System.exit(0);
	}
}
```
###### main\Logic\Mark.java
``` java
package Logic;

import java.io.IOException;
import java.util.ArrayList;

import Storage.LocalStorage;
import Task.Task;

public class Mark {

	private LocalStorage localStorageObject;

	//Constructor.
	public Mark() {
		localStorageObject = LocalStorage.getInstance();
	}

	/**
	 * Function to mark tasks as completed.
	 * 
	 * @param index                  the index of the task to be marked as completed.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void markTaskAsCompleted(int index) throws IOException, ClassNotFoundException {
		ArrayList<Task> getSize = localStorageObject.getUncompletedTasks();

		if (index < getSize.size()) {
			Task temp = localStorageObject.getUncompletedTask(index);
			temp.setComplete();
			localStorageObject.deleteFromUncompletedTasks(index);
			localStorageObject.addToCompletedTasks(temp);
		} else {
			Task temp = localStorageObject.getFloatingTask(index - getSize.size());
			temp.setComplete();
			localStorageObject.deleteFromFloatingTasks(index - getSize.size());
			localStorageObject.addToCompletedTasks(temp);
		}
	}

	/**
	 * Function to mark a task as uncompleted.
	 * 
	 * @param index                   the index of the task to be marked as uncompleted.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void markTaskAsUncompleted(int index) throws IOException, ClassNotFoundException {
		Task temp = localStorageObject.getCompletedTask(index);

		if (temp.getEndDate() != null || temp.getStartDate() != null) {
			localStorageObject.addToUncompletedTasks(temp);
			localStorageObject.deleteFromCompletedTasks(index);
		} else {
			localStorageObject.addToFloatingTasks(temp);
			localStorageObject.deleteFromCompletedTasks(index);
		}
	}

	/**
	 * Function to set the priority for a task.
	 * 
	 * @param index                   the index of the task to be updated.
	 * @param priority                the priority to be set for the task.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void setPriority(int index, String priority) throws ClassNotFoundException, IOException {
		ArrayList<Task> getSize = localStorageObject.getUncompletedTasks();

		if (index < getSize.size()) {
			Task temp = localStorageObject.getUncompletedTask(index);
			temp.setPriority(priority);
			localStorageObject.setUncompletedTask(index, temp);
		} else {
			Task temp = localStorageObject.getFloatingTask(index - getSize.size());
			temp.setPriority(priority);
			localStorageObject.setFloatingTask(index - getSize.size(), temp);
		}
	}

	/**
	 * Function to set priority for all instances of a recurring task.
	 * 
	 * @param index    the index of the tasks.
	 * @param priority the priority to be set for the task.
	 */
	public void setRecurringTasksPriority(int index, String priority) {
		ArrayList<Task> tempTasks = localStorageObject.getUncompletedTasks();

		Task temp = localStorageObject.getUncompletedTask(index);
		String idOfTask = temp.getId();

		checkIfIdMatches(priority, tempTasks, idOfTask);
		
		try {
			localStorageObject.setUncompletedTasks(tempTasks);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to check if the id of the task matches recurring task id.
	 * 
	 * @param priority  the priority to be set for the task.
	 * @param tempTasks the arraylist of tasks.
	 * @param idOfTask  the id of the task.
	 */
	public void checkIfIdMatches(String priority, ArrayList<Task> tempTasks, String idOfTask) {
		if (!idOfTask.equals("")) {
			for (int i = 0; i < tempTasks.size(); i++) {
				if (!tempTasks.get(i).getId().equals("")) {
					if (tempTasks.get(i).getId().equals(idOfTask)) {
						tempTasks.get(i).setPriority(priority);
					}
				}
			}
		}
	}
}
```
###### main\Logic\Notification.java
``` java
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
```
###### main\Logic\Search.java
``` java
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
```
###### main\Logic\Sort.java
``` java
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
	 * Function to sort tasks according to priority.
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
	 * Function to sort given arraylist according to priority.
	 * 
	 * @param tempTasks  the arraylist to be sorted.
	 * @param isFloating to indicate if arraylist is floating task arraylist or not.
	 * 
	 * @return arraylist sorted according to priority .
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
	 * Function to sort tasks in chronological order.
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
	 * Function to compare the different dates of uncompleted tasks and sort them accordingly.
	 * 
	 * @param tempTasks  arraylist of tasks to be sorted.
	 * @param i          index of first Task.
	 * @param j          index of second Task.
	 * @param startDate1 start date of first Calendar object.
	 * @param startDate2 start date of second Calendar object.
	 * @param endDate1   end date of first Calendar object.
	 * @param endDate2   end date of second Calendar object.
	 */
	public void compareDates(ArrayList<Task> tempTasks, int i, int j, Calendar startDate1, Calendar startDate2,
			Calendar endDate1, Calendar endDate2) {
		if (endDate1 == null && endDate2 == null) { // both end dates are null.
			if (startDate1.compareTo(startDate2) > 0) {
				Task temp = tempTasks.get(i);
				tempTasks.set(i, tempTasks.get(j));
				tempTasks.set(j, temp);
			}
		} else if (endDate1 != null && endDate2 != null) { // both end dates are
															// not null.
			if (endDate1.compareTo(endDate2) > 0) { // endDate1 is greater than
													// endDate2.
				Task temp = tempTasks.get(i);
				tempTasks.set(i, tempTasks.get(j));
				tempTasks.set(j, temp);
			} else if (endDate1.compareTo(endDate2) == 0) { // end dates are equal.
				if (startDate1 != null) {
					if (startDate2 != null) { // both start dates are not null.
						if (startDate1.compareTo(startDate2) > 0) { // startDate1 is greater than startDate2.
							Task temp = tempTasks.get(i);
							tempTasks.set(i, tempTasks.get(j));
							tempTasks.set(j, temp);
						}
					}
				} else { // start date 1 is null.
					Task temp = tempTasks.get(i);
					tempTasks.set(i, tempTasks.get(j));
					tempTasks.set(j, temp);
				}
			}
		} else if (endDate1 == null && endDate2 != null) { // endDate1 is null.
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
			if (endDate1.compareTo(startDate2) > 0) { // endDate2 is null.
				Task temp = tempTasks.get(i);
				tempTasks.set(i, tempTasks.get(j));
				tempTasks.set(j, temp);
			}
		}
	}

	/**
	 * Function to sort a given arraylist of tasks in chronological order and return the sorted list.
	 * 
	 * @param tempTasks arraylist of tasks to be sorted in chronological order.
	 * 
	 * @return          the arraylist sortedChronologically.
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
```
###### main\Storage\LocalStorage.java
``` java
package Storage;

import java.io.IOException;
import java.util.ArrayList;

import Task.Task;

public class LocalStorage {

	private static LocalStorage localStorage;

	private ArrayList<Task> uncompletedTasks;
	private ArrayList<Task> floatingTasks;
	private ArrayList<Task> completedTasks;
	
	// Private constructor, following the singleton pattern.
	private LocalStorage() {
		uncompletedTasks = new ArrayList<Task>();
		floatingTasks = new ArrayList<Task>();
		completedTasks = new ArrayList<Task>();
	}

	/**
	 * Method to access this class, following the singleton pattern. 
	 * Invokes constructor if LocalStorage has not been initialised.
	 * 
	 * @return The LocalStorage object.
	 */
	public static LocalStorage getInstance() {
		if (localStorage == null) {
			localStorage = new LocalStorage();
		}
		return localStorage;
	}

	// Getter methods.
	public ArrayList<Task> getUncompletedTasks() {
		return uncompletedTasks;
	}

	public ArrayList<Task> getFloatingTasks() {
		return floatingTasks;
	}
	
	public ArrayList<Task> getCompletedTasks() {
		return completedTasks;
	}

	public Task getUncompletedTask(int index) {
		Task temp = null;
		for (int i = 0; i < uncompletedTasks.size(); i++) {
			if (i == index) {
				temp = uncompletedTasks.get(i);
			}
		}
		return temp;
	}

	public Task getCertainUncompletedTask(int index) {
		if (index >= 0 && index < uncompletedTasks.size())
			return uncompletedTasks.get(index);
		else
			return null;
	}
	
	public Task getFloatingTask(int index) {
		Task temp = null;
		for (int i = 0; i < floatingTasks.size(); i++) {
			if (i == index) {
				temp = floatingTasks.get(i);
			}
		}
		return temp;
	}
	
	public Task getCompletedTask(int index) {
		Task temp = null;
		for (int i = 0; i < completedTasks.size(); i++) {
			if (i == index) {
				temp = completedTasks.get(i);
			}
		}
		return temp;
	}


	// Setter methods.
	public void setUncompletedTasks(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		uncompletedTasks = changedDetails;
	}

	public void setFloatingTasks(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		floatingTasks = changedDetails;
	}

	public void setCompletedTasks(ArrayList<Task> changedDetails) throws ClassNotFoundException, IOException {
		completedTasks = changedDetails;
	}

	public void setUncompletedTask(int index, Task temp) {
		uncompletedTasks.set(index, temp);
	}

	public void setCompletedTask(int index, Task temp) {
		completedTasks.set(index, temp);
	}

	public void setFloatingTask(int index, Task temp) {
		floatingTasks.set(index, temp);
	}

	/**
	 * Function to add a task to the list of uncompleted tasks.
	 * 
	 * @param task contains the task to be added to list of uncompleted tasks.
	 */
	public void addToUncompletedTasks(Task task) {
		uncompletedTasks.add(task);
	}

	/**
	 * Function to add a task to the list of floating tasks.
	 * 
	 * @param task contains the task to be added to list of floating tasks.
	 */
	public void addToFloatingTasks(Task task) {
		floatingTasks.add(task);
	}

	/**
	 * Function to add a task to the list of completed tasks.
	 * 
	 * @param task contains the task to be added to list of completed tasks.
	 */
	public void addToCompletedTasks(Task task) throws IOException, ClassNotFoundException {
		completedTasks.add(task);
	}

	/**
	 * Function to delete a task from the list of uncompleted tasks.
	 * 
	 * @param index contains the index of the task to be deleted from list of uncompleted tasks.
	 */
	public Task deleteFromUncompletedTasks(int index) {
		Task temp = uncompletedTasks.remove(index);
		return temp;
	}

	/**
	 * Function to delete a task from the list of floating tasks.
	 * 
	 * @param index  contains the index of the task to be deleted from list of floating tasks.
	 */
	public Task deleteFromFloatingTasks(int index) {
		Task temp = floatingTasks.remove(index);
		return temp;
	}

	/**
	 * Function to delete a task from the list of completed tasks.
	 * 
	 * @param index contains the index of the task to be deleted from list of completed tasks.
	 */
	public Task deleteFromCompletedTasks(int index) {
		Task temp = completedTasks.remove(index);
		return temp;
	}

	/**
	 * Function to clear the contents of the file.
	 */
	public void clearAllTasks() {
		uncompletedTasks.clear();
		floatingTasks.clear();
		completedTasks.clear();
	}

```
###### main\UI\UI.java
``` java
package UI;

import static org.fusesource.jansi.Ansi.ansi;

import org.fusesource.jansi.AnsiConsole;

public class UI {
```
###### main\unitTest\markTest.java
``` java
package unitTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import Logic.Crud;
import Storage.LocalStorage;
import Task.Task;
import Logic.Mark;
public class MarkTest {
	
	private Crud crud = Crud.getInstance();
	private Mark mark = new Mark();
	private String issue;
	private ArrayList<Task> uncompleted,completed; 
	private LocalStorage storage = LocalStorage.getInstance();
	
	/**
	 * To test if mark works.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testMark() throws ClassNotFoundException, IOException {
		issue = "task";
		crud.clearTasks();
		crud.addTaskWithBothDates("task", "11/04/2016", "12/04/2016", "add task ` from 11/04/2016 to 12/04/2016");
		uncompleted = storage.getUncompletedTasks();
		completed = storage.getCompletedTasks();
		Task task = uncompleted.get(0);
		assertEquals(issue,task.getIssue());
		assertEquals(0,completed.size());
		mark.markTaskAsCompleted(0);
		assertEquals(0,uncompleted.size());
		task = completed.get(0);
		assertEquals(issue,task.getIssue());										
		
		
	}
	
	/**
	 * To test if unmark works.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testUnMark() throws ClassNotFoundException, IOException {
		issue = "task";
		crud.clearTasks();
		crud.addTaskWithBothDates("task", "11/04/2016", "12/04/2016", "add task ` from 11/04/2016 to 12/04/2016");
		uncompleted = storage.getUncompletedTasks();
		completed = storage.getCompletedTasks();
		Task task = uncompleted.get(0);
		assertEquals(issue,task.getIssue());
		assertEquals(0,completed.size());
		mark.markTaskAsCompleted(0);
		assertEquals(0,uncompleted.size());
		task = completed.get(0);
		assertEquals(issue,task.getIssue());
		mark.markTaskAsUncompleted(0);
		task = uncompleted.get(0);
		assertEquals(issue,task.getIssue());
		assertEquals(0,completed.size());		
		
	}
	
	@Test
	public void testPriority() throws ClassNotFoundException, IOException {
		issue = "task";
		crud.clearTasks();
		crud.addTaskWithBothDates("task", "11/04/2016", "12/04/2016", "add task ` from 11/04/2016 to 12/04/2016");
		uncompleted = storage.getUncompletedTasks();
		Task task = uncompleted.get(0);
		assertEquals(issue,task.getIssue());
		mark.setPriority(0,"high");
		task = uncompleted.get(0);
		assertEquals(issue,task.getIssue());
		assertEquals("high",task.getPriority());
	}

}
```
###### main\unitTest\SystemTest.java
``` java
package unitTest;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import Logic.Core;
import Logic.Search;
import Logic.Undo;
import Parser.Parser;
import Storage.LocalStorage;
import Task.Task;

public class SystemTest {

	/**
	 * To test if a task is added correctly.
	 */
	@Test
	public void testIfTaskIsAdded() {
		//Temporary task.
		Task temp = new Task("Testing", "31/03/2016", "01/04/2016", "Testing from 31/03/2016 to 01/04/2016");
		String testString = temp.getTaskString();
		Core test;
		Parser parse;
		LocalStorage storage = LocalStorage.getInstance();
		//Checking if add command works.
		try {
			test = Core.getInstance();
			parse = Parser.getInstance();
			parse.parse("add Testing ` from 31/03/2016 to 01/04/2016");
			test.parseCommands();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		

		Task taskAdded = storage.getUncompletedTask(0);
		String checkString = taskAdded.getTaskString();
		assertEquals(testString, checkString); //to check if the tast is added correctly.
		assertEquals(1, storage.getUncompletedTasks().size()); //to check size of array list.
	}

	/**
	 * To test if a task is deleted correctly.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testIfTaskIsDeleted() throws ClassNotFoundException, IOException {
		//adding task first.
		Task temp = new Task("Testing", "31/03/2016", "01/04/2016", "Testing ` from 31/03/2016 to 01/04/2016");
		Core test;
		Parser parse;
		LocalStorage storage = LocalStorage.getInstance();;
		storage.clearAllTasks();
		storage.addToUncompletedTasks(temp);
		assertEquals(1,storage.getUncompletedTasks().size());
		test = Core.getInstance();
		parse = Parser.getInstance();
		parse.parse("delete 1");
		test.parseCommands();
		assertEquals(1, storage.getUncompletedTasks().size());
	}

	/**
	 * To test if all tasks are cleared correctly.
	 */
	@Test
	public void testIfTasksAreCleared() {
		Core test = Core.getInstance();;
		Parser parse = Parser.getInstance();
		LocalStorage storage = LocalStorage.getInstance();
		storage.clearAllTasks();
		Task temp = new Task("Testing", "31/03/2016", "01/04/2016", "Testing ` from 31/03/2016 to 01/04/2016");
		storage.addToUncompletedTasks(temp);
		assertEquals(1, storage.getUncompletedTasks().size());
		try {
			parse.parse("clear");
			test.parseCommands();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		assertEquals(0, storage.getUncompletedTasks().size() + storage.getFloatingTasks().size());
	}
	
	/**
	 * To test if search results are correct.
	 */
	@Test
	public void testIfSearchResultsAreCorrect() {
		Task temp = new Task("Testing", "31/03/2016", "01/04/2016", "Testing ` from 31/03/2016 to 01/04/2016");
		Task temp1 = new Task("Testing", "02/04/2016", "03/04/2016", "Testing ` from 02/04/2016 to 03/04/2016");
		Task temp2 = new Task("Testing", "05/03/2016", "06/04/2016", "Testing ` from 05/03/2016 to 06/04/2016");
		
		Core test = Core.getInstance();;
		Parser parse = Parser.getInstance();
		LocalStorage storage = LocalStorage.getInstance();
		storage.clearAllTasks();
		storage.addToUncompletedTasks(temp);
		storage.addToUncompletedTasks(temp1);
		storage.addToUncompletedTasks(temp2);

		ArrayList<Task> searchResults = new ArrayList<Task>();
		Search search = Search.getInstance();
		try {
			parse.parse("search Testing");
			test.parseCommands();
			
			searchResults = search.getSearchedTasks();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		assertEquals(searchResults, storage.getUncompletedTasks());
	}
	
	/**
	 * To test if undo works correctly.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testIfUndoWorks() throws ClassNotFoundException, IOException {
		// check the initial amount off floating task.
		Core test = Core.getInstance();;
		Parser parse = Parser.getInstance();
		LocalStorage storage = LocalStorage.getInstance();
		storage.clearAllTasks();
		int initialFloatingNum = storage.getFloatingTasks().size();
		System.out.println("Current amount of floating tasks: " + initialFloatingNum);

		//Add a sample floating task.
		try {
			parse.parse("add Test Floating");
			test.parseCommands();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block.
			e.printStackTrace();
		}

		// Ensure that task has been added.
		System.out.println("Current amount of floating tasks (after adding): " + storage.getFloatingTasks().size());
		assertEquals(initialFloatingNum + 1, storage.getFloatingTasks().size());

		//Undo the addition of floating task.
		parse.parse("undo");
		test.parseCommands();

		// Ensure the added task has been removed (undone).
		System.out.println("Current amount of floating tasks (after undo): " + storage.getFloatingTasks().size());
		assertEquals(initialFloatingNum + 1, storage.getFloatingTasks().size());

		// Clear all tasks for clean testing state.
		storage.clearAllTasks(); 
		initialFloatingNum = storage.getFloatingTasks().size();

		//Add 2 sample floating tasks.
		try {
			parse.parse("add Test Floating 1");
			test.parseCommands();
			parse.parse("add Test Floating 2");
			test.parseCommands();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		// Ensure that task has been added.
		System.out.println("Current amount of floating tasks (after adding): " + storage.getFloatingTasks().size());
		assertEquals(initialFloatingNum + 2, storage.getFloatingTasks().size());

		// Remove all tasks.
		parse.parse("clear");
		test.parseCommands();

		// Ensure that all tasks has been deleted.
		System.out.println("Current amount of floating tasks (after clearing): " + storage.getFloatingTasks().size());
		assertEquals(0, storage.getFloatingTasks().size());

		//Undo the clear command.
		parse.parse("undo");
		test.parseCommands();

		// Ensure the removed tasks has been restored (undone).
		System.out.println("Current amount of floating tasks (after undo): " + storage.getFloatingTasks().size());
		assertEquals(initialFloatingNum , storage.getFloatingTasks().size());

	}
	private static Undo undoObject = Undo.getInstance();

	/**
	 * To test if undo and redo stack are empty initially.
	 */
	@Test
	public void testInitialEmptyUndoAndRedo() {
		// Wipe redo stack in case any other test methods were run before this.
		undoObject.clearRedoCommands();

		// Check for zero initial undo-able commands.
		assertEquals(0, undoObject.getHistoryCount());

		// Check for zero initial redo-able commands.
		System.out.println(undoObject.getRedoCount());
		assertEquals(0, undoObject.getRedoCount());
	}
	
	/**
	 * To test if redo works correctly.
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testRedo() throws ClassNotFoundException, IOException {
		// Helper method to simulate undo (and thus adding of command to redo stack).
		undoObject.testFunction();

		// Check for correct retrieval of the undo command name.
		String redoneCommand = undoObject.getRedoneCommand();
		assertEquals("Add Buy eggs ` on Friday", redoneCommand);

		// Check for one redo-able commands after pseudo-undo.
		undoObject.testFunction();
		assertEquals(1, undoObject.getRedoCount());
	}


	
}
```
