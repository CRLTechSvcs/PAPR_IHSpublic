package actors;

public class StatusMessage {

	public static String EMPTY = "EMPTY";
	public static String SUCCESS = "SUCCESS";
	public static String ERROR = "ERROR";
	
	public String code = EMPTY; 
	
	StatusMessage(String str){
		code=str;
	}
	
}
