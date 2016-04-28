package json;

public class MemberView {

	public int groupId = 0;
	public int id = 0;
	public String name = "";
	public String description = "";
	public String address1 = "";
	public String address2 = "";
	public String city = "";
	public String stateOrProvence = "";
	public String postalCode = "";
	public String country = "";
	public String groupName="";

	public MemberView() {

	}

	public MemberView(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public MemberView(int id, String name, String description, String address1,
			String address2, String city, String stateOrProvence,
			String postalCode, String country, String groupName) {

		this.id = id;
		this.name = name;
		this.description = description;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.stateOrProvence = stateOrProvence;
		this.postalCode = postalCode;
		this.country = country;
		this.groupName = groupName;
	}
}