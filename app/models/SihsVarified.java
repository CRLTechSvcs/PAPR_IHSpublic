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
@Table(name = "sihsVarified")
public class SihsVarified extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static public String NO="NO";
	static public String YES="YES";
	
	@Id
	@GeneratedValue
	@Column(name="ihsVarifiedID")
	int ihsVarifiedID;
	public String name;
	public String description;

	public SihsVarified(String name, String description) {

		this.name = name;
		this.description = description;
	}

	public static Finder<Integer, SihsVarified> find = new Finder<Integer, SihsVarified>(
			Integer.class, SihsVarified.class);
}