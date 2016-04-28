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
@Table(name="singestionrecordstatus")
public class SingestionRecordStatus extends Model {
	
	
	private static final long serialVersionUID = 1L;
	
	static final public String Available = "Available";
	static final public String OnHold = "OnHold";
	static final public String Processing = "Processing";
	static final public String BadRecordError = "BadRecord";
	static final public String Ingnored = "Ingnored";
	static final public String Complete = "Complete";

    @Id
    @GeneratedValue
    @Column(name="ingestionRecordStatusID")
    int ingestionRecordStatusID;
   
    public String name;
    public String description;

    public SingestionRecordStatus(String name, String description){
        
        this.name = name;
        this.description = description;
    }  
    
    public static Finder<Integer, SingestionRecordStatus> find = new Finder<Integer, SingestionRecordStatus>(
			Integer.class, SingestionRecordStatus
			.class);
   
}