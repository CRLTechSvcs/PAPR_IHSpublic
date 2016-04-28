package json;

public class ValidationLevel {
	public int id = 0;
	public String name="";
	public int checked=0;
	
	public ValidationLevel(){
		
	}
	
	public ValidationLevel (int id, String name, int checked){
		this.id= id;
		this.name=name;
		this.checked=checked;
	}

}
