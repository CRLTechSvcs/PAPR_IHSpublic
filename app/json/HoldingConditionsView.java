package json;

public class HoldingConditionsView {
	
	public int conditionId=0;
	public String name="";
	public int checked=0;
	
	public HoldingConditionsView(){
		
	}
	
	public HoldingConditionsView(int conditionId, String name, int checked){
		this.conditionId = conditionId;
		this.name=name;
		this.checked= checked;
	}
}