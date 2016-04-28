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
@Table(name = "smemberstatus")
public class SmemberStatus extends Model{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	 @GeneratedValue
	 @Column(name="memberStatusID")
	 int memberStatusID;

	 public String name;
	 public String description;
	 
	 public SmemberStatus( String name, String description){
		 this.name = name;
		 this.description = description;
	 }
	 
	 public static Finder<Integer, SmemberStatus> find = new Finder<Integer, SmemberStatus>(
				Integer.class, SmemberStatus.class);

}
