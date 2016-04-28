package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.ebean.Model;

import com.avaje.ebean.annotation.CacheStrategy;

@CacheStrategy(readOnly = true)
@Entity
@Table(name = "sstateProvince")

public class SstateProvince extends Model {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static public String Default="Default";
	

	
	@Id
	@GeneratedValue
	@Column(name="stateProvinceId")
	public int stateProvinceId;
	
	@Column(name="name")
	public String name;
	
	@Column(name="countryId")
	public int countryId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "countryId")
	public Scountry scountry;
	
}
