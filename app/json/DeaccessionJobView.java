package json;

public class DeaccessionJobView {

	public String dateInitiated = "test";
	public String dateCompleted;
	public String jobName = "";
	public String organization = "";
	public String user = "";
	public int selected = 0;
	public int deaccessed = 0;
	public String finalized="";
	public String print = "";
	public String jobStatus = "";
	
	

	public DeaccessionJobView(){}
	
	public DeaccessionJobView(String dateInitiated, String dateCompleted,
			String jobName, String organization, String user, int selected, 
			String finalized, String print, int deaccessed, String jobStatus) {

		this.dateInitiated = dateInitiated;
		this.dateCompleted = dateCompleted;
		this.jobName = jobName;
		this.organization = organization;
		this.user = user;
		this.selected = selected;
		this.finalized = finalized;
		this.print = print;
		this.deaccessed = deaccessed;
		this.jobStatus = jobStatus;
	}
}