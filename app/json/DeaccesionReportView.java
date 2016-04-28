package json;

import java.util.ArrayList;
import java.util.List;

public class DeaccesionReportView {

	public int jobId=0;
	public String jobName = "";
	public int NumberOfHolding=0;
	public int NumberOfDeaccession=0;
	public int NumberOfDonation=0;
	public int numberOfPreservation=0;
	public int total=0;
	public String initDate="";
	public String orgName="";
	public String userName="";
	public String standard="";
	
	
	public List<DeaccessionTitleView> deaccessionTitleView = new ArrayList<DeaccessionTitleView>();
	public DeaccesionReportView(){}
}