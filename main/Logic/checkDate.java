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
		if(msgArray.length != 3 && msg.matches("^\\d{2}/\\d{2}/\\d{4}")) {
			return false;
		} else {
			int date=Integer.parseInt(msgArray[0]);
			int month=Integer.parseInt(msgArray[1]);
			int year=Integer.parseInt(msgArray[2]);

			if((year % 4) == 0 && (month >= 1) && (month <= 12)) {
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
		}
	}
}
