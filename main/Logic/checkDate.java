//@@author Cheng Gee
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
