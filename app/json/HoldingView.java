package json;

import java.util.ArrayList;
import java.util.List;

public class HoldingView {
	public int holdingId=0;
	public String member="";
	public String location="";
	public String location1="";
	public String location2="";
	
	public List<OverallView> overalls = new ArrayList<OverallView>();
	public List<ValidationLevel> validationLevels= new ArrayList<ValidationLevel>();
	public List<IhsVerifiedView> ihsVerified=new ArrayList<IhsVerifiedView>();
	public List<CommitmentView> commitmentView=new ArrayList<CommitmentView>();
	
	public String missingPages="";
	public List<HoldingConditionsView> holdingConditionsView = new ArrayList<HoldingConditionsView>();
	public String notes="";
	public String editable="0";
	public ArrayList <Integer> holdingIds;
	
	public HoldingView(){}
	
	public HoldingView(int holdingId, String member, String location, String location1, String location2, List<OverallView> overalls,   List<ValidationLevel> validationLevels,
			List<IhsVerifiedView> ihsVerified, String missingPages, List<HoldingConditionsView> holdingConditionsView , String notes, String editable){
		
		this.holdingId=holdingId;
		this.member=member;
		this.location=location;
		this.location1=location1;
		this.location2=location2;
		this.overalls=overalls;
		this.validationLevels=validationLevels;
		this.ihsVerified= ihsVerified;
		this.missingPages= missingPages;
		this.holdingConditionsView=holdingConditionsView;
		this.notes=notes;
		this.editable=editable;
	}
	
	public HoldingView(int holdingId, String member, String location, String location1, String location2, List<OverallView> overalls,   List<ValidationLevel> validationLevels,
			List<IhsVerifiedView> ihsVerified, String missingPages, List<HoldingConditionsView> holdingConditionsView , String notes, String editable, ArrayList <Integer> holdingIds){
		
		this.holdingId=holdingId;
		this.member=member;
		this.location=location;
		this.location1=location1;
		this.location2=location2;
		this.overalls=overalls;
		this.validationLevels=validationLevels;
		this.ihsVerified=ihsVerified;
		this.missingPages=missingPages;
		this.holdingConditionsView=holdingConditionsView;
		this.notes=notes;
		this.editable=editable;
		this.holdingIds=holdingIds;
	}
}