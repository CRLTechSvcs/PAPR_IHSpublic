package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.annotation.CacheStrategy;

/**
 * Created by reza on 9/24/2014.
 */
@CacheStrategy(readOnly = true)
@Entity
@Table(name = "sconditiontype")
public class SconditionType extends Model {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue
    @Column(name="conditionTypeID")
    public int conditionTypeID;
    public String name;
    public String description;

    public SconditionType(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public static Finder<Integer, SconditionType> find = new Finder<Integer, SconditionType>(
			Integer.class, SconditionType.class);
	
	@Override
    public boolean equals(Object arg0) {
	
		SconditionType obj=(SconditionType)arg0;
		
		if (this.conditionTypeID ==obj.conditionTypeID){
	                return true;
	     }
	     return false;
	}
}