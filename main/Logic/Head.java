package Logic;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
import java.time.YearMonth;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import Storage.localStorage;
import Task.Task;

public class Head {

	 private static final String WELCOME_MSG_1 = "Welcome to Agendah. ";
	 private static final String WELCOME_MSG_2 = "Agendah is ready for use";
	 private static final String USER_PROMPT = "command: ";
	 private static Scanner sc = new Scanner(System.in);
	 private static String lastCommand = "";
	 private static String logo1="********   ********   ********  **       **   *****       ********   **    **";
	 private static String logo2="********   ********   ********  ***      **   **   **     ********   **    **";
	 private static String logo3="**    **   **         **        ****     **   **    **    **    **   **    **";
	 private static String logo4="**    **   **         ******    *** **   **   **     **   **    **   ********";
	 private static String logo5="********   **   ***   ******    ***  **  **   **     **   ********   ********";
	 private static String logo6="********   **    **   **        ***   ** **   **     **   ********   **    **";
	 private static String logo7="**    **   ********   ********  ***    ****   **    **    **    **   **    **";
	 private static String logo8="**    **   ********   ********  ***     ***   *******     **    **   **    **";
	 public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {		
		 Logic.ImportTasks.prepareAndImportFiles();
//		 System.out.print(ansi().eraseScreen().fgBright(RED));
		 checkDateAndAdd();
		 UI.ui.print(logo1);
		 UI.ui.print(logo2);
		 UI.ui.print(logo3);
		 UI.ui.print(logo4);
		 UI.ui.print(logo5);
		 UI.ui.print(logo6);
		 UI.ui.print(logo7);
		 UI.ui.print(logo8);

		 UI.ui.print(WELCOME_MSG_1 + WELCOME_MSG_2);
//		 System.out.print(ansi().reset());
		 UI.ui.print("\n");
		 UI.ui.print("Enter \"help\" for instructions.");
		 Logic.crud.displayUncompletedAndFloatingTasks();
		 runProgram();
	 }

	 public static void runProgram() throws IOException, ClassNotFoundException {
		 while (true) {
			 UI.ui.print(USER_PROMPT);
			 String input = UI.ui.acceptCommand();
			 String[] arr = input.split(" ");
			 // get command
			 String cmd = arr[0];
			 String description;
			 // handle lines with only the command
			 if (cmd.length() == input.length()) {
				 description = "";
			 } else {
				 // get description
				 description = input.substring(cmd.length() + 1, input.length());
			 }

			 Parser.Parser.run(cmd, description);
			 lastCommand = cmd;
			 if(lastCommand.equals("sortp") || lastCommand.equals("sp") != true) {
				 Logic.Sort.sortTasksChronologically();
			 }
			 // save all tasks into the actual file after command is done
			 Logic.Save.saveToFile();		
		 }
	 }
	 public static void checkDateAndAdd() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			String today = dateFormat.format(date);
			
			
			try {

				if (localStorage.getRecurringTasks().size() != 0) {
					for (int i = 0; i < localStorage.getRecurringTasks().size(); i++) {
						Task tmp = localStorage.getRecurringTasks().get(i);
						String dl = tmp.getEndDateString();
						 dl = dl.substring(0, dl.length()-1);
						 int diff = compareTwoDate(today,dl);
						if (diff <= 7) {
							Task temp = localStorage.delFromRecurringTasks(i);
							Logic.crud.addByTask(temp);
							
							i = 0;//loop again
						}
					}
					Logic.Sort.sortTasksChronologically();
					System.out.println("number of recurring task in storage: " + localStorage.getRecurringTasks().size());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static int compareTwoDate(String date1, String date2) {
			int ans;
			String[] temp = date1.split("/");
			String[] temp2 = date2.split("/");
			if (Integer.parseInt(temp[2]) == Integer.parseInt(temp2[2])) {// same year
				if (Integer.parseInt(temp[1])==Integer.parseInt(temp2[1])) {// same month
					ans = Integer.parseInt(temp2[0]) - Integer.parseInt(temp[0]);
				} else { // different month
					if (Integer.parseInt(temp2[1]) - Integer.parseInt(temp[1]) > 1) { // differ
																						// by
																						// >=2
																						// month
						ans = 30;
					} else { // differ by 1 month
						// check day in month.
						YearMonth yearMonthObject;
						yearMonthObject = YearMonth.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[1]));
						int daysInMonth = yearMonthObject.lengthOfMonth();
						int diff = daysInMonth - Integer.parseInt(temp[0]);
						ans = diff + Integer.parseInt(temp2[0]);
					}
				}
			} else {
				ans = 365;
			}
			return ans;
		}

	 //getter method
	 public static String getLastCommand() {
		 return lastCommand;
	 }
 }
