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
@Table(name="singestiondatatype")
public class SingestionDatatype extends Model {

	
	private static final long serialVersionUID = 1L;

	public static final String Portico = "Portico";
	public static final String PAPR = "PAPR";
	
    @Id
    @GeneratedValue
    @Column(name="ingestionDataTypeID")
    public int ingestionDataTypeID;
    
    public String name;
    
    public String description;

    public SingestionDatatype(String name, String description){
        
        this.name = name;
        this.description = description;
    }  
    
    public static Finder<Integer, SingestionDatatype> find = new Finder<Integer, SingestionDatatype>(
			Integer.class, SingestionDatatype
			.class);
}