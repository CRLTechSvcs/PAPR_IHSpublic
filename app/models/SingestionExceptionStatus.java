package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.annotation.CacheStrategy;

/**
 * Created by reza on 9/11/2014.
 */
@CacheStrategy(readOnly = true)
@Entity
@Table(name="singestionexceptionstatus")
public class SingestionExceptionStatus extends Model {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static String Available="Available";
	public static String Proccessing="Proccessing";
	public static String Resolved="Resolved";

	@Id
    @GeneratedValue
    @Column(name="ingestionExceptionStatusID")
    int ingestionExceptionStatusID;
    
	@Column(name="name")
    public String name;
	
	@Column(name="description")
    public String description;

    public SingestionExceptionStatus(String name, String description){
        
        this.name = name;
        this.description = description;
    }  
    
    public static Finder<Integer, SingestionExceptionStatus> find = new Finder<Integer, SingestionExceptionStatus>(
			Integer.class, SingestionExceptionStatus
			.class);
   
}

