package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

public class AppUser implements Subject, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int userId;
	public String userName = null;
	public List<AppRole> appRoles = new ArrayList<AppRole>();
	public int memberId;
	public String memberName;
	public int locatoinId;
	public String name;

	public AppUser(int userId, String userName, List<AppRole> appRoles,
			int memberId, String memberName, int locatoinId, String name) {
		this.userId = userId;
		this.userName = userName;
		this.appRoles = appRoles;
		this.memberId = memberId;
		this.memberName = memberName;
		this.locatoinId = locatoinId;
		this.name = name;
	}

	@Override
	public String getIdentifier() {
		return null;
	}

	@Override
	public List<? extends Permission> getPermissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends Role> getRoles() {
		return appRoles;
	}

}
