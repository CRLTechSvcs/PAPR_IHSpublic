package json;

import java.util.ArrayList;
import java.util.List;

public class VolumeView {
	
	public int volumeId;
	public String volumeNumber="";
	public String volumeYear="";
	public List<IssueView> issueView = new ArrayList<IssueView>();
	public List<Chart> chart = null;
	public String volumeLevelFlag = "0";
	
	public VolumeView(){};
	
	public VolumeView(int volumeId, String volumeNumber, String volumeYear, List<IssueView> issueView){
		this.volumeId = volumeId;
		this.volumeNumber=volumeNumber;
		this.volumeYear=volumeYear;	
		this.issueView = issueView;
		
	}
}