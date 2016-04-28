package json;

public class CommitmentView {
	public int id=0;
	public String name="";
	public int checked=0;
	
	public CommitmentView(){}
	
	public CommitmentView(int id, String name, int checked){
		this.id = id;
		this.name=name;
		this.checked= checked;
	}
}
