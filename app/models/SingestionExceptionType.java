package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.annotation.CacheStrategy;

import play.db.ebean.Model;


@CacheStrategy(readOnly = true)
@Entity
@Table(name="singestionexceptiontype")
public class SingestionExceptionType extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static String BadRecordformat="BadRecordFormat";
	public static String BadYearformat="BadYearFormat";
	public static String BadPrintISSN="BadPrintISSN";
	public static String BadEISSN="BadEISSN";
	public static String BadHoldingFormat="BadHoldingFormat";
	public static String NoAuthorizedTitle="NoAuthorizedTitle";
	public static String NoAuthorizedVolume="NoAuthorizedVolume";
	public static String MissingAuthorizedVolume="MissingAuthorizedVolume";
	public static String NoAuthorizedPublicationRange="NoAuthorizedPublicationRange";
	
	@Id
	@GeneratedValue
	@Column(name="ingestionExceptionTypeID")
	public int ingestionExceptionTypeID;
	
	public String name;
    public String description;
    
 public SingestionExceptionType(String name, String description){
        
        this.name = name;
        this.description = description;
    }  
    
    public static Finder<Integer, SingestionExceptionType> find = new Finder<Integer, SingestionExceptionType>(
			Integer.class, SingestionExceptionType
			.class);
}
