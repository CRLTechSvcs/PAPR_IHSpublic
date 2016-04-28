package models;

import org.joda.time.DateTime;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.sql.Date;

/**
 * Created by reza on 9/11/2014.
 */
@Entity
@Table(name = "ihsaddress")
public class IhsAddress extends Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="addressID")
	public int addressID;
	
	public String address1;
	public String address2;
	public String city;

	@Column(name="stateOrProvence")
	public String stateOrProvence;
	
	@Column(name="postalCode")
	public String postalCode;
	
	@Column(name="zipPlus")
	public String zipPlus;
	
	@Column(name="country")
	public String country;
	
	@Column(name="createdTS")
	public DateTime createdTS;

	public IhsAddress( String address1, String address2,
			String city, String stateOrProvence, String postalCode,
			String zipPlus, String country, DateTime createdTS) {
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.stateOrProvence = stateOrProvence;
		this.postalCode = postalCode;
		this.zipPlus = zipPlus;
		this.country = country;
		this.createdTS = createdTS;
	}

	public static Finder<Integer, IhsAddress> find = new Finder<Integer, IhsAddress>(
			Integer.class, IhsAddress.class);

}