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
@Table(name="stitletype")
public class StitleType extends Model {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    @Column(name="titleTypeID")
    public int titleTypeID;
    
    public String name;
    public String description;

     public StitleType(String name, String description){
        
        this.name = name;
        this.description = description;
    }  

     public static Finder<Integer, StitleType> find = new Finder<Integer, StitleType>(
 			Integer.class, StitleType.class);
  
}