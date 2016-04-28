
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
@Table(name="ihsauthorizedsource")
public class IhsAuthorizedSource extends Model {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue
    @Column(name="authorizedSourceID")
    public int authorizedSourceID;
    public String name;
    public String description;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="memberId")
    public IhsMember ihsMember;
    
    public IhsAuthorizedSource(){
    	
    }
    
    public IhsAuthorizedSource( String name, String description,IhsMember ihsMember ){
    	this.name = name;
    	this.description = description;
    	this.ihsMember = ihsMember;
    }

    public static Finder<Integer,IhsAuthorizedSource > find = new Finder<Integer,IhsAuthorizedSource >(Integer.class, IhsAuthorizedSource.class);
}