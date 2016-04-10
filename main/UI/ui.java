//@@author Kowshik
package UI;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.Scanner;

import org.fusesource.jansi.AnsiConsole;

import Parser.Natty;

public class UI {
	//@@author Cheng Gee
	
	private static Scanner sc = new Scanner(System.in);
	
	private static final String LOGO_LINE_1 = "********   ********   ********  **       **   *****       ********   **    **";
	private static final String LOGO_LINE_2 = "********   ********   ********  ***      **   **   **     ********   **    **";
	private static final String LOGO_LINE_3 = "**    **   **         **        ****     **   **    **    **    **   **    **";
	private static final String LOGO_LINE_4 = "**    **   **         ******    *** **   **   **     **   **    **   ********";
	private static final String LOGO_LINE_5 = "********   **   ***   ******    ***  **  **   **     **   ********   ********";
	private static final String LOGO_LINE_6 = "********   **    **   **        ***   ** **   **     **   ********   **    **";
	private static final String LOGO_LINE_7 = "**    **   ********   ********  ***    ****   **    **    **    **   **    **";
	private static final String LOGO_LINE_8 = "**    **   ********   ********  ***     ***   *******     **    **   **    **";
	
	public static final String SANE = "\u001B[0m";

	public static final String HIGH_INTENSITY = "\u001B[1m";
	public static final String LOW_INTENSITY = "\u001B[2m";

	public static final String ITALIC = "\u001B[3m";
	public static final String UNDERLINE = "\u001B[4m";
	public static final String BLINK = "\u001B[5m";
	public static final String RAPID_BLINK = "\u001B[6m";
	public static final String REVERSE_VIDEO = "\u001B[7m";
	public static final String INVISIBLE_TEXT = "\u001B[8m";

	public static final String BLACK = "\u001B[30m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String MAGENTA = "\u001B[35m";
	public static final String CYAN = "\u001B[36m";
	public static final String WHITE = "\u001B[37m";

	public static final String ANSI_CLS = "\u001b[2J";
	public static final String ANSI_HOME = "\u001b[H";
	public static final String ANSI_BOLD = "\u001b[1m";
	public static final String ANSI_AT55 = "\u001b[10;10H";
	public static final String ANSI_REVERSEON = "\u001b[7m";
	public static final String ANSI_NORMAL = "\u001b[0m";
	public static final String ANSI_WHITEONBLUE = "\u001b[37;44m";
	
	public static final String BACKGROUND_BLACK	= "\u001B[40m";
	public static final String BACKGROUND_RED = "\u001B[41m";
	public static final String BACKGROUND_GREEN	= "\u001B[42m";
	public static final String BACKGROUND_YELLOW = "\u001B[43m";
	public static final String BACKGROUND_BLUE = "\u001B[44m";
	public static final String BACKGROUND_MAGENTA = "\u001B[45m";
	public static final String BACKGROUND_CYAN = "\u001B[46m";
	public static final String BACKGROUND_WHITE	= "\u001B[47m";

	/**
	 * Method to print the welcome logo of Agendah.
	 */
	public static void printLogo() {
		eraseScreen();
		printGreen(LOGO_LINE_1);
		printGreen(LOGO_LINE_2);
		printGreen(LOGO_LINE_3);
		printGreen(LOGO_LINE_4);
		printGreen(LOGO_LINE_5);
		printGreen(LOGO_LINE_6);
		printGreen(LOGO_LINE_7);
		printGreen(LOGO_LINE_8);
	}
	
	/**
	 * Function to print the incoming string.
	 * 
	 * @param temp The string to be printed.
	 */
	public static void print(String temp) {
		System.out.print(temp);
		if(temp.equals("command: ") != true) {
			System.out.println();
		}
	}

	/**
	 * Method to clear the screen in CLI.
	 */
	public static void eraseScreen() {
		AnsiConsole.systemUninstall();
		System.out.print(ansi().eraseScreen());
		AnsiConsole.systemInstall();
		System.out.println(ANSI_CLS);
		System.out.println(ANSI_CLS);
		System.out.println(ANSI_CLS);
	}
	
	/**
	 * Method to print a task with its details, with tabs added for alignment.
	 * 
	 * @param i     A counter to print a numbered list.
	 * @param sdate Start date and time (if any) of the task.
	 * @param edate End date and time (if any) of the task.
	 * @param msg   Issue description of the task.
	 */
	public static void printTask(int i, String sdate, String edate, String msg) {
		i = i + 1;
		System.out.println(HIGH_INTENSITY + YELLOW + i + ".\t" + CYAN + sdate
				           + " " + edate + " " + YELLOW + msg + ansi().reset());
	}
	
	/**
	 * Method to a task with its details in two lines, with tabs added for alignment.
	 * 
	 * @param i     A counter to print a numbered list.
	 * @param sdate Start date (if any) of the task.
	 * @param stime Start day and time (if any) of the task.
	 * @param edate End date (if any) of the task.
	 * @param etime End day and time (if any) of the task.
	 * @param msg   Issue description of the task.
	 * @param rec   Recurrence interval of the task (if any).
	 */
	public static void printTask1(int i, String sdate, String stime,
			                      String edate,String etime,
			                      String msg, String rec) {
		i = i + 1;
		if (sdate == null) {
			sdate = "\t";
		} else if (sdate.length() < 8) {
			sdate += "\t";
		}
		if (stime == null) {
			stime = "\t\t";
		} else if (stime.length() < 8) {
			stime += "\t";
		}
		if (edate == null) {
			edate = "\t\t";
		} else if (edate.length() < 8) {
			edate += "\t\t";
		} else if (edate.length() < 12) {
			edate += "\t";
		}
		if (etime == null) {
			etime = "\t\t";
		} else if (etime.length() < 8) {
			etime += "\t";
		}
		for (int j = rec.length(); j < msg.length(); j++) {
			rec += " ";
		}
		System.out.println(HIGH_INTENSITY + YELLOW + i + ".\t" + CYAN + sdate
				           + "\t" + edate + YELLOW + msg + ansi().reset());
		System.out.println(HIGH_INTENSITY + YELLOW + "\t" + CYAN + stime
				           + etime + rec + ansi().reset());
	}

	/**
	 * Method to a task with its details in two lines during the welcome screen, with tabs added for alignment.
	 * Includes information on whether the task is expired or due today.
	 * 
	 * @param i     A counter to print a numbered list.
	 * @param sdate Start date (if any) of the task.
	 * @param stime Start day and time (if any) of the task.
	 * @param edate End date (if any) of the task.
	 * @param etime End day and time (if any) of the task.
	 * @param msg   Issue description of the task.
	 * @param rec   Information regarding expiry/deadline (if any) of task.
	 */
	public static void printTask2(int i, String sdate, String stime,
			                      String edate, String etime,
			                      String msg, String rec) {
		i = i + 1;
		if (sdate == null) {
			sdate = "\t";
		} else if (sdate.length() < 8) {
			sdate += "\t";
		}
		if (stime == null) {
			stime = "\t\t";
		} else if (stime.length() < 8) {
			stime += "\t";
		}
		if (edate == null) {
			edate = "\t\t";
		} else if (edate.length() < 8) {
			edate += "\t\t";
		} else if (edate.length() < 12) {
			edate += "\t";
		}
		if (etime == null) {
			etime = "\t\t";
		} else if (etime.length() < 8) {
			etime += "\t";
		}

		for (int j = rec.length(); j < msg.length(); j++) {
			rec += " ";
		}

		System.out.println(HIGH_INTENSITY + YELLOW + i + ".\t" + CYAN + sdate
				           + "\t" + edate + YELLOW + msg + ansi().reset());
		System.out.println(HIGH_INTENSITY + YELLOW + "\t" + CYAN + stime
				           + etime + RED + rec + ansi().reset());
	}

	/**
	 * Method to print feedback of the task details after it has been added.
	 * 
	 * @param i     A counter to print a numbered list.
	 * @param sdate Start date and time (if any) of the task.
	 * @param edate End date and time (if any) of the task.
	 * @param msg   Issue description of the task.
	 */
	public static void printTaskAdded(int i, String sdate, String edate, String msg) {
		i = i + 1;
		System.out.println(BACKGROUND_BLUE + HIGH_INTENSITY + YELLOW + i + ".\t"
		                   + CYAN + sdate + edate + YELLOW + msg + ansi().reset());
	}

	/**
	 * Method to print feedback of the task details in 2 lines after it has been added.
	 * 
	 * @param i     A counter to print a numbered list.
	 * @param sdate Start date (if any) of the task.
	 * @param stime Start day and time (if any) of the task.
	 * @param edate End date (if any) of the task.
	 * @param etime End day and time (if any) of the task.
	 * @param msg   Issue description of the task.
	 * @param rec   Information regarding expiry/deadline (if any) of task.
	 */
	public static void printTaskAdded1(int i, String sdate, String stime,
			                           String edate, String etime,
			                           String msg, String rec) {
		i = i + 1;
		if (sdate == null) {
			sdate = "\t";
		} else if (sdate.length() < 8) {
			sdate += "\t";
		}
		if (stime == null) {
			stime = "\t\t";
		} else if (stime.length() < 8) {
			stime += "\t";
		}
		if (edate == null) {
			edate = "\t\t";
		} else if (edate.length() < 8) {
			edate += "\t\t";
		} else if (edate.length() < 12) {
			edate += "\t";
		}
		if (etime == null) {
			etime = "\t\t";
		} else if (etime.length() < 8) {
			etime += "\t";
		}
		for (int j = rec.length(); j < msg.length(); j++) {
			rec += " ";

		}
		System.out.println(BACKGROUND_BLUE + HIGH_INTENSITY + HIGH_INTENSITY + YELLOW + i+ ".\t"
		                  + CYAN + sdate + "\t" + edate + YELLOW + msg + ansi().reset());
		System.out.println(BACKGROUND_BLUE + HIGH_INTENSITY + HIGH_INTENSITY + YELLOW + "\t"
		                  + CYAN + stime + etime + rec + ansi().reset());
	}

	/**
	 * Method to print a floating task.
	 * 
	 * @param i   A counter to print a numbered list.
	 * @param msg Issue description of the task.
	 */
	public static void printFloating(int i, String msg) {
		i = i + 1;
		System.out.println(HIGH_INTENSITY + YELLOW + i + ".\t" + msg + ansi().reset());

	}

	/**
	 * Method to print a floating task with blue background.
	 * 
	 * @param i   A counter to print a numbered list.
	 * @param msg Issue description of the task.
	 */
	public static void printFloatingBackground(int i, String msg) {
		i = i + 1;
		System.out.println(BACKGROUND_BLUE + HIGH_INTENSITY + YELLOW + i + ".\t" + msg + ansi().reset());

	}

	// The following methods print the given string in the stated colour.
	public static void printRed(String temp) {

		System.out.print(HIGH_INTENSITY + RED + temp + ansi().reset());
		if (temp.equals("command: ") != true) {
			System.out.println();
		}
	}

	public static void printBlue(String temp) {
		System.out.println(HIGH_INTENSITY + BLUE + temp + ansi().reset());
	}

	public static void printGreen(String temp) {
		System.out.println(HIGH_INTENSITY + GREEN + temp + ansi().reset());
	}

	public static void printYellow(String temp) {
		System.out.println(HIGH_INTENSITY + YELLOW + temp + ansi().reset());
	}

	public static void printCyan(String temp) {
		System.out.println(HIGH_INTENSITY + CYAN + temp + ansi().reset());
	}
}
