//@@author Kowshik
package UI;

import java.util.Scanner;

import org.fusesource.jansi.AnsiConsole;

import Parser.Natty;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
public class ui {
	//@@author Cheng Gee
	public static final String	SANE				= "\u001B[0m";

	public static final String	HIGH_INTENSITY		= "\u001B[1m";
	public static final String	LOW_INTENSITY		= "\u001B[2m";

	public static final String	ITALIC				= "\u001B[3m";
	public static final String	UNDERLINE			= "\u001B[4m";
	public static final String	BLINK				= "\u001B[5m";
	public static final String	RAPID_BLINK			= "\u001B[6m";
	public static final String	REVERSE_VIDEO		= "\u001B[7m";
	public static final String	INVISIBLE_TEXT		= "\u001B[8m";

	public static final String	BLACK				= "\u001B[30m";
	public static final String	RED					= "\u001B[31m";
	public static final String	GREEN				= "\u001B[32m";
	public static final String	YELLOW				= "\u001B[33m";
	public static final String	BLUE				= "\u001B[34m";
	public static final String	MAGENTA				= "\u001B[35m";
	public static final String	CYAN				= "\u001B[36m";
	public static final String	WHITE				= "\u001B[37m";

	public static final String	BACKGROUND_BLACK	= "\u001B[40m";
	public static final String	BACKGROUND_RED		= "\u001B[41m";
	public static final String	BACKGROUND_GREEN	= "\u001B[42m";
	public static final String	BACKGROUND_YELLOW	= "\u001B[43m";
	public static final String	BACKGROUND_BLUE		= "\u001B[44m";
	public static final String	BACKGROUND_MAGENTA	= "\u001B[45m";
	public static final String	BACKGROUND_CYAN		= "\u001B[46m";
	public static final String	BACKGROUND_WHITE	= "\u001B[47m";
	public static final String ANSI_CLS = "\u001b[2J";
	  public static final String ANSI_HOME = "\u001b[H";
	  public static final String ANSI_BOLD = "\u001b[1m";
	  public static final String ANSI_AT55 = "\u001b[10;10H";
	  public static final String ANSI_REVERSEON = "\u001b[7m";
	  public static final String ANSI_NORMAL = "\u001b[0m";
	  public static final String ANSI_WHITEONBLUE = "\u001b[37;44m";
	private static Scanner sc = new Scanner(System.in);
	
	/**
	 * Function to print the incoming string
	 * @param temp the string to be printed
	 */
	public static void print(String temp) {
		//System.out.print( ansi().fg(GREEN).a(temp).fg(WHITE) );
		System.out.print(temp);
		if(temp.equals("command: ") != true) {
			System.out.println();
		}
	}

	public static void eraseScreen(){
		AnsiConsole.systemUninstall();
		System.out.print(ansi().eraseScreen());
		AnsiConsole.systemInstall();
		System.out.println(ANSI_CLS);
		System.out.println(ANSI_CLS);
		System.out.println(ANSI_CLS);
	}
	public static void printTask(int i,String sdate,String edate,String msg){
		i=i+1;
		System.out.println(HIGH_INTENSITY+YELLOW+i+".\t"+CYAN+sdate+edate+YELLOW+msg+ansi().reset());
	}
	public static void printTaskAdded(int i,String sdate,String edate,String msg){
		i=i+1;
		System.out.println(BACKGROUND_BLUE+HIGH_INTENSITY+YELLOW+i+".\t"+CYAN+sdate+edate+YELLOW+msg+ansi().reset());
	}
	public static void printRed(String temp){

		System.out.print(HIGH_INTENSITY+RED+temp+ansi().reset());
		if(temp.equals("command: ") != true) {
			System.out.println();
		}
	}
	public static void printBlue(String temp){
		System.out.println(HIGH_INTENSITY+BLUE+temp+ansi().reset());
	}
	public static void printGreen(String temp){
		System.out.println(HIGH_INTENSITY+GREEN+temp+ansi().reset());
	}
	public static void printYellow(String temp){
		System.out.println(HIGH_INTENSITY+YELLOW+temp+ansi().reset());
	}
	public static void printCyan(String temp){
		System.out.println(HIGH_INTENSITY+CYAN+temp+ansi().reset());
	}

	/**
	 * Function to accept the command from the user
	 * @return returns the string entered by the user
	 */
	public static String acceptCommand() {
		String command = sc.nextLine();
		String[] splitCommand = command.split(" ");
		if (splitCommand[0].equals("add")) { // only use natty if add command is detected
			command = Natty.getInstance().parseString(command);
		}
		return command;
	}
}
