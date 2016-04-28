package json;

public class PublicationRangeView {

	public int publicationRangeID=0;
	
	public String status="old";
	public String startDate = "";
	public String endDate="";
	public int pubRangeId=0;
	
	public PublicationRangeView(){
		
	}
	
	public PublicationRangeView(int publicationRangeID, String startDate, String endDate, int pubRangeId){
		this.publicationRangeID = publicationRangeID;
		this.startDate=startDate;
		this.endDate=endDate;
		this.pubRangeId = pubRangeId;
	}
}