package models;

import javax.persistence.*;

import com.avaje.ebean.annotation.CacheStrategy;

import play.db.ebean.*;

@CacheStrategy(readOnly = true)
@Entity
@Table(name = "ihssecurityrole")
public class IhsSecurityRole extends Model {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "securityRoleId")
	public int ihsSecurityRoleId;
	
    public String name;
    public String description;
    
    public IhsSecurityRole(String name, String description) {
      this.name = name;
      this.description = description;
    }

    public static Finder<Integer,IhsSecurityRole> find = new Finder<Integer,IhsSecurityRole>(
    		Integer.class, IhsSecurityRole.class
    ); 
}