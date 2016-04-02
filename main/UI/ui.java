//@@author Kowshik
package UI;

import java.util.Scanner;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
public class ui {

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
	//@@author Cheng Gee
	public static void eraseScreen(){
		System.out.print(ansi().eraseScreen());
	}
	public static void printTask(int i,String sdate,String edate,String msg){
		System.out.println(ansi().fgBright(YELLOW).a((i+1)+".\t").fgBright(CYAN).a(sdate+edate).fgBright(YELLOW).a(msg).reset());
	}
	public static void printTaskAdded(int i,String sdate,String edate,String msg){
		System.out.println(ansi().bg(BLUE).fgBright(YELLOW).a((i+1)+".\t").fgBright(CYAN).a(sdate+edate).fgBright(YELLOW).a(msg).reset());
	}
	public static void printRed(String temp){
		
		System.out.print(ansi().fgBright(RED).a(temp).reset());
		if(temp.equals("command: ") != true) {
			System.out.println();
		}
	}
	public static void printBlue(String temp){
		System.out.println(ansi().fgBright(BLUE).a(temp).reset());
	}
	public static void printGreen(String temp){
		System.out.println(ansi().fgBright(GREEN).a(temp).reset());
	}
	public static void printYellow(String temp){
		System.out.println(ansi().fgBright(YELLOW).a(temp).reset());
	}
	public static void printCyan(String temp){
		System.out.println(ansi().fgBright(CYAN).a(temp).reset());
	}
	
	/**
	 * Function to accept the command from the user
	 * @return returns the string entered by the user
	 */
	public static String acceptCommand() {
		String command = sc.nextLine();
		return command;
	}

}
