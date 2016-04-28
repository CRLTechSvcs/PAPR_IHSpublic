package json;

import java.util.ArrayList;
import java.util.List;

public class DeaccessionTitleView {
	public int titleId=0;
	public String title = "";
	public String issn ="";
	public String oclc = "";
	public int numberOfHolding=0;
	public List <DeaccessionIssueView> deaccessionIssueView = new ArrayList<DeaccessionIssueView>();
	public List <String> donnee = new ArrayList<String>();

	public DeaccessionTitleView(){}
	public DeaccessionTitleView(int titleId, String title, String issn, int numberOfHolding, List <DeaccessionIssueView> deaccessionIssueView){
		this.titleId = titleId;
		this.title = title;
		this.issn = issn;
		this.numberOfHolding = numberOfHolding;
		this.deaccessionIssueView = deaccessionIssueView;
		
	}
}