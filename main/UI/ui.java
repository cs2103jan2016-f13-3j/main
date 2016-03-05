package UI;

import java.util.Scanner;

public class ui {

	private static Scanner sc = new Scanner(System.in);
	
	public static void print(String temp) {
		System.out.print(temp);
	}
	
	public static void printEmptyLine() {
		System.out.println();
	}
	
	public static String acceptCommand() {
		String command = sc.nextLine();
		return command;
	}
	
}
