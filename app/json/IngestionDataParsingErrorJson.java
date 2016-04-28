package json;

public class IngestionDataParsingErrorJson {
	public String exceptionDate;
	public String jobName;
	public String recordTitle;
	public String issues;
	public String status;
	public String owner;

	public IngestionDataParsingErrorJson(String exceptionDate, String jobName,
			String recordTitle, String issues, String status, String owner) {
		this.exceptionDate = exceptionDate;
		this.jobName = jobName;
		this.recordTitle = recordTitle;
		this.issues = issues;
		this.status = status;
		this.owner = owner;
	}
}
