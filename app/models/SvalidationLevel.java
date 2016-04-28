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
@Table(name = "svalidationLevel")
public class SvalidationLevel extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static public String None="None";
	
	@Id
	@GeneratedValue
	@Column(name="validationLevelID")
	public int validationLevelID;
	public String name;
	public String description;
	public int level;

	public SvalidationLevel(String name, String description,  int level) {

		this.name = name;
		this.description = description;
		this.level = level;
	}

	public static Finder<Integer, SvalidationLevel> find = new Finder<Integer, SvalidationLevel>(
			Integer.class, SvalidationLevel.class);
}
