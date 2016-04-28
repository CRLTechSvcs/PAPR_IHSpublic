
package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by reza on 9/11/2014.
 */
@Entity
@Table(name = "ihslocation")
public class IhsLocation extends Model {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue
    @Column(name="locationID")
    public int locationID;
    public String name;
    public String description;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="memberId")
    public IhsMember ihsMember;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="addressId")
    public IhsAddress ihsAddress;
    
    
    public IhsLocation ( String name, String description, IhsMember ihsMember, IhsAddress ihsAddress){
    	this.name = name;
    	this.description = description;
    	this.ihsMember = ihsMember;
    	this.ihsAddress = ihsAddress;
    };
    
    public static Finder<Integer, IhsLocation> find = new Finder<Integer, IhsLocation>(
			Integer.class, IhsLocation.class);
}