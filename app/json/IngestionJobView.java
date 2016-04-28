package json;

public class IngestionJobView {
	public String creationDate;
	public String jobName;
	public String dataSource;
	public String member;
	public String User;
	public String jobStatus;
	public String statusDetail;

	public IngestionJobView(String creationDate, String jobName,
			String dataSource, String member, String User, String jobStatus,
			String statusDetail) {
		
		this.creationDate = creationDate;
		this.jobName = jobName;
		this.dataSource = dataSource;
		this.member=member;
		this.User=User;
		this.jobStatus = jobStatus;
		this.statusDetail = statusDetail;
	}
}