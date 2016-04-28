package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.annotation.CacheStrategy;


/**
 * Created by reza on 9/25/2014.
 */
@CacheStrategy(readOnly = true)
@Entity
@Table(name="suserstatus")
public class SuserStatus extends Model{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String Active="Active";
	public final static String Inactive="Inactive";
	
	@Id
    @GeneratedValue
    @Column(name="userStatusID")
    public int userStatusID;
    
    public String name;
    public String description;

    public SuserStatus(String name, String description){
        
        this.name = name;
        this.description = description;
    }  
    
    public static Finder<Integer, SuserStatus> find = new Finder<Integer, SuserStatus>(
			Integer.class, SuserStatus
			.class);
}
