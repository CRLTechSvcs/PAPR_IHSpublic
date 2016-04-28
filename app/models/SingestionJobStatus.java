
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
@Table(name="singestionjobstatus")
public class SingestionJobStatus extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3326776188642082142L;
	/**
	 * 
	 */
	
	
	static final public String Queued = "Queued";
	static final public String Processing = "Processing";
	static final public String FileProcessingError = "File Processing Error";
	static final public String Complete = "Complete";
	static final public String InternalError = "Internal Error";
	static final public String Incomplete = "Incomplete";
	
    @Id
    @GeneratedValue
    @Column(name="ingestionJobStatusID")
    public int ingestionJobStatusID;
    
    public String name;
    public String description;
    
   public SingestionJobStatus(String name, String description){
        
        this.name = name;
        this.description = description;
    }  
    
    public static Finder<Integer, SingestionJobStatus> find = new Finder<Integer, SingestionJobStatus>(
			Integer.class, SingestionJobStatus
			.class);
}