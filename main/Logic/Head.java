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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import Storage.localStorage;
import Task.Task;
import org.fusesource.jansi.AnsiConsole;
public class Head {
	//@@author Cheng Gee
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
		 AnsiConsole.systemInstall();

//		 System.out.print(ansi().eraseScreen().fgBright(RED));
		 checkDateAndAdd();
		 UI.ui.eraseScreen();
		 UI.ui.printGreen(logo1);
		 UI.ui.printGreen(logo2);
		 UI.ui.printGreen(logo3);
		 UI.ui.printGreen(logo4);
		 UI.ui.printGreen(logo5);
		 UI.ui.printGreen(logo6);
		 UI.ui.printGreen(logo7);
		 UI.ui.printGreen(logo8);

		 UI.ui.printYellow(WELCOME_MSG_1 + WELCOME_MSG_2);

		 UI.ui.print("\n");
		 UI.ui.printYellow("Enter \"help\" for instructions.");
		 Logic.Notification.welcomeReminder();
		 runProgram();
	 }

	 public static void runProgram() throws IOException, ClassNotFoundException {
		 while (true) {
			 UI.ui.printRed(USER_PROMPT);
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
			 String lastArg = description;
			 if((lastCommand.equals("sort") && lastArg.equals("p"))!=true ||
					 (lastCommand.equals("sort")&& lastArg.equals("priority")) != true) {
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

					for (int i = 0; i < localStorage.getRecurringTasks().size(); i++) {// for
																						// loop
																						// to
																						// search
																						// storage
																						// for
						Task tmp = localStorage.getRecurringTasks().get(i);
						String ed = tmp.getEndDateString();
						
						ed = ed.substring(0, ed.length() - 1);
						int diff = compareTwoDate(today, ed);
						boolean expired = isExpired(ed, tmp.getLastDate());

						if (expired) { // if task has expired
							localStorage.delFromRecurringTasks(i);
							i = -1;// loop again
						} else {

							while (true) {
								if (diff > tmp.getDayBefore() || expired) {// if
																			// task
																			// is
																			// after
																			// the
																			// days
																			// before
																			// when
									// want to display the recurring tasks or when
									// the tasks had expired
									break;
								}
								Logic.crud.addByTask(tmp);
								String newED = processDate(ed, tmp.getFrequency());
								tmp = new Task(tmp.getIssue(), newED, tmp.getMsg(), false, tmp.getFrequency(),
										tmp.getDayBefore(), tmp.getLastDate());
								ed = tmp.getEndDateString();
								ed = ed.substring(0, ed.length() - 1);
								diff = compareTwoDate(today, ed);
								expired = (isExpired(ed, tmp.getLastDate()));

							}
							localStorage.setRecurringTask(i, tmp); // update storage
						}

						Logic.Sort.sortTasksChronologically();
					}
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
		/**
		 * method that process Date for recurring tasks based on the date and number
		 * of recurring tasks calculated
		 * 
		 * @param s
		 * @param n
		 * @return
		 */
		public static String processDate(String s, int n) {
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

		public static String arrayToString(String[] arr) {
			String temp = Arrays.toString(arr);
			temp = temp.substring(1, temp.length() - 1).replaceAll(", ", " ");
			return temp;
		}

		public static boolean isExpired(String date1, String date2) {
			boolean expired = true;
			String[] temp = date1.split("/");
			String[] temp2 = date2.split("/");
			if (Integer.parseInt(temp2[2]) > Integer.parseInt(temp[2])) {// year
				expired = false;
			} else if (Integer.parseInt(temp2[1]) > Integer.parseInt(temp[1])) {// month
				expired = false;
			} else {
				if (Integer.parseInt(temp2[0]) >= Integer.parseInt(temp[0])) {// day
					expired = false;
				}
			}
			return expired;
		}

	 //getter method
	 public static String getLastCommand() {
		 return lastCommand;
	 }
 }
