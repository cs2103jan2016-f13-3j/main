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
	
	/**
	 * Function to accept the command from the user
	 * @return returns the string entered by the user
	 */
	public static String acceptCommand() {
		String command = sc.nextLine();
		return command;
	}

}
