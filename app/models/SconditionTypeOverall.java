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
@Table(name = "sconditionTypeOverall")
public class SconditionTypeOverall extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static public String Unknown ="Unknown";
	
	@Id
	@GeneratedValue
	@Column(name="conditionTypeOverallID")
	int conditionTypeOverallID;
	public String name;
	public String description;

	public SconditionTypeOverall(String name, String description) {

		this.name = name;
		this.description = description;
	}

	public static Finder<Integer, SconditionTypeOverall> find = new Finder<Integer, SconditionTypeOverall>(
			Integer.class, SconditionTypeOverall.class);
}