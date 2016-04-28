package json;

public class GroupView {
	
	public int groupId = 0;
	public String groupName = "";
	public String groupDesc = "";
	
	public GroupView(){};
	
	public GroupView(int groupId, String groupName, String groupDesc){
		
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupDesc = groupDesc;
		
	};
	
}
