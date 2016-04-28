package models;

import play.db.ebean.Model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.annotation.CacheStrategy;

/**
 * Created by reza on 9/24/2014.
 */
@CacheStrategy(readOnly = true)
@Entity
@Table(name = "sissuestatus")
public class SissueStatus extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static public String Default="Default";
	
	@Id
	@GeneratedValue
	@Column(name="issueStatusID")
	int issueStatusID;
	public String name;
	public String description;

	public SissueStatus(String name, String description) {

		this.name = name;
		this.description = description;
	}

	public static Finder<Integer, SissueStatus> find = new Finder<Integer, SissueStatus>(
			Integer.class, SissueStatus.class);
}
