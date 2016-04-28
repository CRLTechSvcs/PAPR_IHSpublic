
package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;

import java.sql.Date;
import java.util.List;

/**
 * Created by reza on 9/11/2014.
 */
@Entity
@Table(name="ihsingestionexception")
public class IhsIngestionException extends Model {

	static String deleteallExcpetion =" DELETE FROM ihsingestionexception WHERE ingestionRecordID = ingestionRecordid ";
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    @Column(name="ingestionExceptionID")
    public int ingestionExceptionID;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ingestionRecordID")
    public IhsIngestionRecord ihsIngestionRecord;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ingestionExceptionTypeID")
    public SingestionExceptionType singestionExceptionType;
    
    
    @Column(name="recordTitle")
    public String recordTitle;
    
    @Column(name="issues")
    public String issues;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ingestionExceptionStatusID")
    public SingestionExceptionStatus singestionExceptionStatus;
 
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userID")
    public IhsUser ishUser;
    
    @Column(name="lockDate")
    Date lockDate;
    
    public IhsIngestionException(IhsIngestionRecord ihsIngestionRecord, SingestionExceptionType singestionExceptionType,
    		String recordTitle,String issues, SingestionExceptionStatus singestionExceptionStatus, IhsUser ishUser){
    	this.ihsIngestionRecord= ihsIngestionRecord;
    	this.singestionExceptionType = singestionExceptionType;
    	this.recordTitle = recordTitle;
    	this.issues = issues;
    	this.singestionExceptionStatus = singestionExceptionStatus;
    	this.ishUser = ishUser;
    	
    }
    public static Finder<Integer, IhsIngestionException> find = new Finder<Integer, IhsIngestionException>(
			Integer.class, IhsIngestionException
			.class);
  
    public static void deleteIhsIngestionExceptions( int ingestionRecordID){
    	
    	SqlUpdate update = Ebean.createSqlUpdate(deleteallExcpetion);
		update.setParameter("ingestionRecordid", ingestionRecordID);
		Ebean.execute(update);
    
    }
}