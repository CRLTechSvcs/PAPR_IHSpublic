package json;

public class DeaccessionIssueView {
	public int holdingId =0;
	public String volissue="";
	public String date="";
	public String action ="d";
	public String location = "";
	
	public DeaccessionIssueView(  ){}
	
	public DeaccessionIssueView(int holdingId,String volissue, String date){
		this.holdingId = holdingId;
		this.volissue = volissue;
		this.date = date;
	}
}