package models;

import java.util.ArrayList;
import java.util.List;

import play.db.ebean.Model;
import util.Helper;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by reza on 9/11/2014.
 */
@Entity
@Table(name = "ihsuser")
public class IhsUser extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "userID")
	public int userID;

	@Column(name = "firstName")
	public String firstName;

	@Column(name = "lastName")
	public String lastName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId")
	public IhsMember ihsMember;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userStatusID")
	public SuserStatus suserStatus;

	@Column(name = "userName")
	public String userName;

	@Column(name = "password")
	public  String password;

	@ManyToMany(cascade = CascadeType.REMOVE)
	public List<IhsSecurityRole> ihsSecurityRoles = new ArrayList<IhsSecurityRole>();

	public IhsUser(String firstName, String lastName, IhsMember ihsMember,
			SuserStatus suserStatus, String userName, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.ihsMember = ihsMember;
		this.suserStatus = suserStatus;
		this.userName = userName;
		this.password = Helper.getMD5hash(password);
	}

	public static Finder<Integer, IhsUser> find = new Finder<Integer, IhsUser>(
			Integer.class, IhsUser.class);

	public String getName() {
		return firstName + " " + lastName;
	}
	
	public static IhsUser findByName(String userName) {
		  return find.where().eq("userName", userName).findUnique();
	}
	
	public  void setIhsSecurityRoles(List<IhsSecurityRole> ihsSecurityRoles){
		this.ihsSecurityRoles = ihsSecurityRoles;
	}
}