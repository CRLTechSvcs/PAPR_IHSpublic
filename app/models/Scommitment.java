package models;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.annotation.CacheStrategy;

@CacheStrategy(readOnly = true)
@Entity
@Table(name = "scommitment")
public class Scommitment extends Model{

	public static String Uncommitted = "Uncommitted";
	public static String Committed = "Committed";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="commitmentID")
	public int commitmentID;
	public String name;
	public String description;
	

	 public static Finder<Integer, Scommitment> find = new Finder<Integer, Scommitment>(
				Integer.class, Scommitment.class);
}