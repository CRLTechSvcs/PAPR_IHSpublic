package json;

import java.util.ArrayList;
import java.util.List;

public class IssueView {

	public int issueId;
	public String issueNumber = "";
	public String issueMonth = "";
	public String issueStatus = "";
	public int issueCount = 0;
	public String issueCondition = "";
	public List<HoldingView> holdingViews = new ArrayList<HoldingView>() ;

  // AJE added these properties, used in ihsVolume.java
  public String name = "";
  public String description = "";
  // end AJE 2016-11-04

	public IssueView() {

	}

	public IssueView(int issueId, String issueNumber, String issueMonth, String issueStatus,
			int issueCount, String issueCondition, List<HoldingView> holdingViews) {
		this.issueId= issueId;
		this.issueNumber = issueNumber;
		this.issueMonth = issueMonth;
		this.issueStatus = issueStatus;
		this.issueCount = issueCount;
		this.issueCondition = issueCondition;
		this.holdingViews = holdingViews;
	}
}