package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.annotation.CacheStrategy;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@CacheStrategy(readOnly = true)
@Entity
@Table(name = "spublicationdatetype")
public class SpublicationDateType extends Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="publicationDateTypeId")
	public int publicationDateTypeId;
	
	@Column(name="publicationDateTypeVal")
	public String publicationDateTypeVal;
	
	
	public static Finder<Integer, SpublicationDateType> find = new Finder<Integer, SpublicationDateType>(
			Integer.class, SpublicationDateType.class);
}
