package models;

import play.db.ebean.Model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Created by reza on 9/11/2014.
 */
@Entity
@Table(name = "ptitlelink")
public class Ptitlelink extends Model {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="titleLinkID")
    public int titleLinkID;
	
	@Column(name="titleParentID")
    public int titleParentID;
	
	@Column(name="titleChildID")
    public int titleChildID;

	public Ptitlelink(int titleChildID, int titleParentID) {
		
		this.titleParentID = titleParentID;
		this.titleChildID =  titleChildID;
		
	}
	
	public static Finder<Integer, Ptitlelink> find = new Finder<Integer, Ptitlelink>(
				Integer.class, Ptitlelink.class);
	    
}