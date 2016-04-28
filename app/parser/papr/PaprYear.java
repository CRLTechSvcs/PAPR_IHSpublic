package parser.papr;

public class PaprYear {
	public boolean isMonthly = true;
	public int beginYear = -1;
	public int beginMonth = -1;
	public int endYear = -1;
	public int endMonth = 13;
	public String dateVal = "";
	
	public PaprYear(){
		
	}
	
	public PaprYear(boolean isMonthly){
		this.isMonthly = isMonthly;
	}
}
