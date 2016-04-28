package actors;

public class RecordProcessingMessage {
	public int recordId;
	public String dataType;
	public int userId;
	public int memberId;
	public int locationId;
	public int commitmentID;
	
	public RecordProcessingMessage(int recordId, String dataType, int userId,
			int memberId, int locationId, int commitmentID) {
		this.recordId = recordId;
		this.dataType = dataType;
		this.userId = userId;
		this.memberId = memberId;
		this.locationId = locationId;
		this.commitmentID = commitmentID;
	}

}
