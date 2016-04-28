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
@Table(name = "speriodicitytype")
public class SperiodicityType extends Model {

	public static  String Default="Default";
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    @Column(name="periodicityTypeID")
    public int periodicityTypeID;
    
    public String name;
    public String description;
    
    @Column(name="intervalsPerYear")
    public int intervalsPerYear;

    public SperiodicityType( String name, String description, int intervalsPerYear){
    	this.name =name;
    	this.description = description;
    	this.intervalsPerYear = intervalsPerYear;
    }
    
    public static Finder<Integer, SperiodicityType> find = new Finder<Integer, SperiodicityType>(
			Integer.class, SperiodicityType.class);
}