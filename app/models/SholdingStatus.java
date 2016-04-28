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
@Table(name = "sholdingstatus")
public class SholdingStatus extends Model {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String Default = "Default";
	
	@Id
    @GeneratedValue
    @Column(name="holdingStatusID")
    public int holdingStatusID;
    
    public String name;
    public String description;

    public SholdingStatus(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public static Finder<Integer, SholdingStatus> find = new Finder<Integer, SholdingStatus>(
			Integer.class, SholdingStatus.class);
}