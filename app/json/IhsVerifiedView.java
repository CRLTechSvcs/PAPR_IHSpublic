package json;

public class IhsVerifiedView {

	public int id = 0;
	public String name="";
	public int level=1;
	public int checked=0;
	
	public IhsVerifiedView(){
		
	}
	
	public IhsVerifiedView (int id, String name, int checked){
		this.id= id;
		this.name=name;
		this.checked=checked;
	}
	
	public IhsVerifiedView (int id, String name, int checked, int level){
		this.id= id;
		this.name=name;
		this.level = level;
		this.checked=checked;
	}
}