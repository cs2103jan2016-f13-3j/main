package Logic;

public class help {

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
