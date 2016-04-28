package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.avaje.ebean.annotation.CacheStrategy;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@CacheStrategy(readOnly = true)
@Entity
@Table(name = "spublicationdate")
public class SpublicationDate extends Model {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="publicationDateId")
	public int publicationDateId;
	

	@Column(name="publicationDateVal")
	public String publicationDateVal;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "publicationDateTypeId")
	public SpublicationDateType publicationDateType;
	
	public static Finder<Integer, SpublicationDate> find = new Finder<Integer, SpublicationDate>(
			Integer.class, SpublicationDate.class);
	
}