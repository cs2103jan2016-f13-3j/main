# Cheng Gee
###### main\Logic\checkDate.java
``` java
package Logic;
public class checkDate {

	private static int[] leapYearDate = new int[]{31,29,31,30,31,30,31,31,30,31,30,31};
	private static int[] commonYearDate= new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	
	/**
	 * Function to check the format of the string if it follows the date convention
	 * 
	 */
	public static boolean checkDateformat(String msg){
		String[] msgArray=msg.split("/");
		if(msgArray.length != 3 && !msg.matches("^\\d{2}/\\d{2}/\\d{4}")) {
			return false;
		} else {
			int date=Integer.parseInt(msgArray[0]);
			int month=Integer.parseInt(msgArray[1]);
			int year=Integer.parseInt(msgArray[2]);
			if(month>=1 && month<=12){
				if((year % 4) == 0 ) {
					if(date <= leapYearDate[month-1]) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				if(date <= commonYearDate[month-1]) {
					return true;
				}
				else {
					return false;
				}
			}
			}else{
				return false;
			}
		}
	}
	  public static boolean checkTimeformat(String msg){
		    String[] msgArray=msg.split(":");
		    if(msgArray.length != 2 && !msg.matches("^\\d{2}:\\d{2}")) {
		     return false;
		    } else if(msg.endsWith("am")||msg.endsWith("pm")){
		     msg=msg.substring(0, msg.length()-2);
		     int hour=Integer.parseInt(msgArray[0]);
		      int minute=Integer.parseInt(msgArray[1]);
		      if(minute>=0&&minute<60&&hour>=0&&hour<=23){
		        return true;
		     }return false;
		    }else {
		     int hour=Integer.parseInt(msgArray[0]);
		     int minute=Integer.parseInt(msgArray[1]);
		     if(minute>=0&&minute<60&&hour>=0&&hour<=23){
		       return true;
		    }return false;
		   }
		   }
}
```
###### main\Logic\crud.java
``` java
	 /**
	  * Function to find the index of Task with Start Date
	  * 
	  * @param line the updated task description
	  * @param index the index of the task to be edited
	  * @throws IOException
	  */
	 public static int uncompletedTaskIndexWithStartDate(String line,String date, String msg){
		 Task task = new Task(line, date, msg, true);
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 for(int i=0;i<tempTasks.size();i++){
			 if(tempTasks.get(i).getTaskString().equals(task.getTaskString())) {
				 return i;
			 }
		 }return -1;
	 }
	 /**
	  * Function to find the index of Task with End Date
	  * @param line
	  * @param date
	  * @param msg
	  * @return
	  */
	 public static int uncompletedTaskIndexWithEndDate(String line,String date, String msg){
		 Task task = new Task(line, date, msg, false);
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 for(int i=0;i<tempTasks.size();i++){
			 if(tempTasks.get(i).getTaskString().equals(task.getTaskString())) {
				 return i;
			 }
		 }return -1;
	 }
	 /**
	  * Function to find the index of Task with Both Dates
	  * @param line
	  * @param startDate
	  * @param endDate
	  * @param msg
	  * @return
	  */

	 public static int uncompletedTaskIndexWithBothDates(String line,String startDate, String endDate, String msg){
		 Task task = new Task(line, startDate, endDate, msg);
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 for(int i=0;i<tempTasks.size();i++){
			 if(tempTasks.get(i).getTaskString().equals(task.getTaskString())) {
				 return i;
			 }
		 }return -1;
	 }
	 
	 /**
	  * Function to find the index of Task with No Date
	  * @param line
	  * @return
	  */
	 public static int uncompletedTaskIndexWithNoDate(String line){
		 Task task = new Task(line);
		 ArrayList<Task> tempTasks = Storage.localStorage.getFloatingTasks();
		 for(int i=0;i<tempTasks.size();i++){
			 if(tempTasks.get(i).getTaskString().equals(task.getTaskString())) {
				 return i;
			 }
		 }return -1;
	 }
	 /**
	  * Function to display the Nearest 5 Completed Task with the newly 
	  * mark Completed Task
	  * @param t
	  */
	 public static void displayNearestFiveCompletedTaskList(Task t){
		 int index=-1;
		 ArrayList<Task> tempTasks = Storage.localStorage.getCompletedTasks();
		 int size=tempTasks.size();
		 for(int i=0;i<size;i++){
			 Task temp=tempTasks.get(i);
			 if(t.getMsg().equals(temp.getMsg())){
				 index=i;
				 break;
			 }
		 }
		 int head=index-2;
		 int tail = index+3;
		 if(head<0){
			 head=0;
		 }
		 if(tail>=size){
			 tail=size;
		 }
		 for(int i=head;i<tail;i++){
			 Task temp=tempTasks.get(i);
			 if(index==i){
				 UI.ui.printTaskAdded1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());

			 }else{
				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());
			 }

		 }

	 }
	 /**
	  * Function to display the nearest 5 task from uncompleted task list 
	  * or floating task list including the unmark task
	  * @param t
	  */
	 public static void displayNearestFiveUnmarkCompleteTaskList(Task t){
		 ArrayList<Task> tempTasks;
		 if(t.getEndDate() != null || t.getStartDate() != null){

			 tempTasks = Storage.localStorage.getUncompletedTasks();
		 }else{
			 tempTasks = Storage.localStorage.getFloatingTasks();
		 }
		 int size = tempTasks.size();
		 int index=-1;
		 for(int i=0;i<size;i++){
			 Task temp=tempTasks.get(i);
			 if(t.getMsg().equals(temp.getMsg())){
				 index=i;
				 break;
			 }
		 }
		 int head=index-2;
		 int tail = index+3;
		 if(head<0){
			 head=0;
		 }
		 if(tail>=size){
			 tail=size;
		 }
		 for(int i=head;i<tail;i++){
			 Task temp=tempTasks.get(i);
			 if(index==i){
				 UI.ui.printTaskAdded1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());

			 }else{
				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());

			 }

		 }
	 }

	 /**
	  * Function to Display the surrounding uncomplete task list of 
	  * the deleted task
	  * @param index
	  */
	 public static void displayNearestFiveDeleteUncompleteTaskList(int index){
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 int size = tempTasks.size();
		 if(size==0){
			 UI.ui.printGreen("Uncompleted Task List is empty");
		 }else if(index<=tempTasks.size()){

			 int head = index-2;
			 int tail = index+3;
			 if(head<0){
				 head=0;
			 }
			 if(tail>=size){
				 tail=size;
			 }

			 for(int i=head;i<tail;i++){
				 Task temp=tempTasks.get(i);
				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());


			 }
		 }
	 }
	 /**
	  * Function to display the surrounding floating task list of
	  * delete task 
	  * @param index
	  */
	 public static void displayNearestFiveDeleteFloatingTask(int index){
		 ArrayList<Task> tempTasks = Storage.localStorage.getFloatingTasks();
		 int size = Storage.localStorage.getUncompletedTasks().size();
		 int size2=tempTasks.size();
		 index-=size;
		 if(size2==0){
			 UI.ui.printGreen("Floating Task List is Empty");
		 }else if(index<size2){
			 int head = index-2;
			 int tail = index+3;
			 if(head<0){
				 head=0;
			 }
			 if(tail>size2){
				 tail=size2;
			 }
			 for(int i=head;i<tail;i++){
				 Task temp=tempTasks.get(i);

				 UI.ui.printTask(i,temp.getStartDateString(),temp.getEndDateString(),temp.getIssue());

			 }
		 }
	 }
	 /**
	  * Function to display the nearest 5 uncompleted task including the new
	  * added task
	  * @param index
	  */
	 public static void displayNearestFiveUncompleted(int index){
		 ArrayList<Task> tempTasks = Storage.localStorage.getUncompletedTasks();
		 int size = tempTasks.size();
		 int head = index-2;
		 int tail = index+3;
		 if(head<0){
			 head=0;
		 }
		 if(tail>=size){
			 tail=size;
		 }

		 for(int i=head;i<tail;i++){
			 Task temp=tempTasks.get(i);
			 if(i==index){
				 UI.ui.printTaskAdded1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());

			 }else{
				 UI.ui.printTask1(i,temp.getStartDateLineOne(),temp.getStartDateLineTwo(),temp.getEndDateLineOne(),temp.getEndDateLineTwo(),temp.getIssue(),temp.getRecurFrequency());

			 }
		 }

	 }
	 /**
	  * Function to display the nearest 5 floating tasks including the 
	  * newly added floating task
	  * @param index
	  */
	 public static void displayNearestFiveFloating(int index){
		 ArrayList<Task> tempTasks = Storage.localStorage.getFloatingTasks();
		 int unSize = Storage.localStorage.getUncompletedTasks().size();
		 int size = tempTasks.size();
		 int head = index-2;
		 int tail = index+3;
		 if(head<0){
			 head=0;
		 }
		 if(tail>=size){
			 tail=size;
		 }

		 for(int i=head;i<tail;i++){
			 Task temp=tempTasks.get(i);
			 if(i==index){
				 UI.ui.printFloatingBackground(unSize+i,temp.getIssue());


			 }else{
				 UI.ui.printFloating(unSize+i,temp.getIssue());


			 }
		 }

	 }
	 /**
	  * Function to copy the Recurring Task Description
	  * @param t
	  */
	 public static void copyRecurringTask(Task t){
		 if(t!=null){
			 String copy = t.getDescription();
			 StringSelection selec = new StringSelection(copy);
			 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			 clipboard.setContents(selec, selec);
		 }
	 }
	 /**
	  * Function to copy the uncompleted Task Description
	  * @param index
	  */
	 public static void copyTask(int index){
		 Task edit = Storage.localStorage.getUncompletedTask(index-1);
		 if(edit != null){
			 String copy = edit.getDescription();
			 StringSelection selec = new StringSelection(copy);
			 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			 clipboard.setContents(selec, selec);
		 }
	 }
	 /**
	  * Function to copy the editing Task Description
	  * @param index
	  */
	 public static void copyEditingTask(int index){
		 ArrayList<Task> task1=Storage.localStorage.getUncompletedTasks();

		 int size=task1.size();
		 if(index<=size){
			 Task edit = Storage.localStorage.getUncompletedTask(index-1);
			 if(edit != null){
				 String copy = edit.getDescription();
				 StringSelection selec = new StringSelection(copy);
				 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				 clipboard.setContents(selec, selec);
			 }
		 }else{
			 Task edit = Storage.localStorage.getFloatingTask(index-size-1);
			 if(edit != null){
				 String copy = edit.getIssue();
				 StringSelection selec = new StringSelection(copy);
				 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				 clipboard.setContents(selec, selec);
			 }
		 }
	 }

```
###### main\Logic\Head.java
``` java
	private static final String WELCOME_MSG_1 = "Welcome to Agendah. ";
	private static final String WELCOME_MSG_2 = "Agendah is ready for use";
	private static final String USER_PROMPT = "command: ";
	private static Scanner sc = new Scanner(System.in);
	private static String lastDisplay = "";
	private static String lastDisplayArg = "";
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
		//	checkDateAndAdd();
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
			if(input.contains("display") || input.contains("search")) {
				lastDisplay = cmd;
				lastDisplayArg = description;
			}
			/*if((lastCommand.equals("sort") && (lastArg.equals("c") || lastArg.equals("chrono")))!= true) {

			 }*/
			Logic.Sort.sortTasksPriority();
			// save all tasks into the actual file after command is done
			Logic.Save.saveToFile();		
		}
	}


	//getter method
	public static String getLastDisplay() {
		return lastDisplay;
	}

	public static String getLastDisplayArg() {
		return lastDisplayArg;
	}
}
```
###### main\UI\ui.java
``` java
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
		System.out.println(HIGH_INTENSITY+YELLOW+i+".\t"+CYAN+sdate + " " +edate+ " " +YELLOW+msg+ansi().reset());
	}
	public static void printTask1(int i,String sdate,String stime,String edate,String etime,String msg,String rec){
		i=i+1;
		if(sdate==null){
			sdate="\t";
		}else if(sdate.length()<8){
			sdate+="\t";
		}
		if(stime==null){
			stime="\t\t";
		}else if(stime.length()<8){
			stime+="\t";
		}
		if(edate==null){
			edate="\t\t";
		}else if(edate.length()<8){
			edate+="\t\t";
		}else if(edate.length()<12){
			edate+="\t";
		}
		if(etime==null){
			etime="\t\t";
		}else if(etime.length()<8){
			etime+="\t";
		}
		for(int j=rec.length();j<msg.length();j++){
			rec+=" ";
		}
		System.out.println(HIGH_INTENSITY+YELLOW+i+".\t"+CYAN+sdate+"\t"+edate+YELLOW+msg+ansi().reset());
		System.out.println(HIGH_INTENSITY+YELLOW+"\t"+CYAN+stime +etime+rec+ansi().reset());
	}
	public static void printTask2(int i,String sdate,String stime,String edate,String etime,String msg,String rec){
		i=i+1;
		if(sdate==null){
			sdate="\t";
		}else if(sdate.length()<8){
			sdate+="\t";
		}
		if(stime==null){
			stime="\t\t";
		}else if(stime.length()<8){
			stime+="\t";
		}
		if(edate==null){
			edate="\t\t";
		}else if(edate.length()<8){
			edate+="\t\t";
		}else if(edate.length()<12){
			edate+="\t";
		}
		if(etime==null){
			etime="\t\t";
		}else if(etime.length()<8){
			etime+="\t";
		}

		for(int j=rec.length();j<msg.length();j++){
			rec+=" ";
		}

		System.out.println(HIGH_INTENSITY+YELLOW+i+".\t"+CYAN+sdate+"\t"+edate+YELLOW+msg+ansi().reset());
		System.out.println(HIGH_INTENSITY+YELLOW+"\t"+CYAN+stime +etime+RED+rec+ansi().reset());
	}
	public static void printTaskAdded(int i,String sdate,String edate,String msg){
		i=i+1;
		System.out.println(BACKGROUND_BLUE+HIGH_INTENSITY+YELLOW+i+".\t"+CYAN+sdate+edate+YELLOW+msg+ansi().reset());
	}
	public static void printTaskAdded1(int i,String sdate,String stime,String edate,String etime,String msg,String rec){
		i=i+1;
		if(sdate==null){
			sdate="\t";
		}else if(sdate.length()<8){
			sdate+="\t";
		}
		if(stime==null){
			stime="\t\t";
		}else if(stime.length()<8){
			stime+="\t";
		}
		if(edate==null){
			edate="\t\t";
		}else if(edate.length()<8){
			edate+="\t\t";
		}else if(edate.length()<12){
			edate+="\t";
		}
		if(etime==null){
			etime="\t\t";
		}else if(etime.length()<8){
			etime+="\t";
		}
		for(int j=rec.length();j<msg.length();j++){
			rec+=" ";
			
		}
		System.out.println(BACKGROUND_BLUE+HIGH_INTENSITY+HIGH_INTENSITY+YELLOW+i+".\t"+CYAN+sdate+"\t"+edate+YELLOW+msg+ansi().reset());
		System.out.println(BACKGROUND_BLUE+HIGH_INTENSITY+HIGH_INTENSITY+YELLOW+"\t"+CYAN+stime +etime+rec+ansi().reset());
		
	}
	public static void printFloating(int i, String msg){
		i=i+1;
		System.out.println(HIGH_INTENSITY+YELLOW+i+".\t"+msg +ansi().reset());
		
		
	}
	public static void printFloatingBackground(int i, String msg){
		i=i+1;
		System.out.println(BACKGROUND_BLUE+HIGH_INTENSITY+YELLOW+i+".\t"+msg +ansi().reset());
		
		
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
		String firstWord = splitCommand[0];
		if (firstWord.equals("add") || firstWord.equals("+") || firstWord.equals("a")) { // only use natty if add command is detected
			command = Natty.getInstance().parseString(command);
		}
		return command;
	}

	public static void printTaskWithMessage(int i,String sdate,String edate,String issue, String message) {
		i=i+1;
		System.out.println(HIGH_INTENSITY+YELLOW+i+".\t"+CYAN+sdate + " " +edate+ " " +YELLOW+issue+ " " + RED + message +
				ansi().reset());	
	}
}
```
