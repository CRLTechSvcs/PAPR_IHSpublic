package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.annotation.CacheStrategy;

@CacheStrategy(readOnly = true)
@Entity
@Table(name = "scountry")

public class Scountry extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static public String Default="Default";
	
	@Id
	@GeneratedValue
	@Column(name="countryId")
	public int countryId;
	
	@Column(name="name")
	public String name;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "scountry")
	public List<SstateProvince> stateProvinces = new ArrayList<SstateProvince>();
	
	public static Finder<Integer, Scountry> find = new Finder<Integer, Scountry>(
			Integer.class, Scountry.class);
}
