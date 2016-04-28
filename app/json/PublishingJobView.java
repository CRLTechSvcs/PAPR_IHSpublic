package json;

public class PublishingJobView {
	public String dateInitiated = "";
	public String jobName = "Test";
	public String formatType;
	public String organization;
	public String user;
	public String dateRange;
	public String jobStatus;
	public String link="";

	public PublishingJobView() {
	}

	public PublishingJobView(String dateInitiated, String jobName,
			String formatType, String organization, String user,
			String dateRange, String jobStatus, String link) {
		this.dateInitiated = dateInitiated;
		this.jobName = jobName;
		this.formatType = formatType;
		this.organization = organization;
		this.user = user;
		this.dateRange = dateRange;
		this.jobStatus = jobStatus;
		this.link = link;
	}
}
