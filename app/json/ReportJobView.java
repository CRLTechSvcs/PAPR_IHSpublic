package json;

public class ReportJobView {
	
	public String dateInitiated= "test1";
	public String reportType="";
	public String reportParameter="";
	public String formatType="";
	public String organization="";
	public String user="";
	public String jobStatus="";
	public String link="";

	public ReportJobView(){
		
	}
	
	public ReportJobView(String dateInitiated, String reportType, String reportParameter,  String formatType,
			String organization, String user, String jobStatus, String link){
		this.dateInitiated = dateInitiated;
		this.reportType = reportType;
		this.reportParameter = reportParameter;
		this.formatType = formatType;
		this.organization = organization;
		this.user= user;
		this.jobStatus = jobStatus;
		this.link = link;
	}
}
