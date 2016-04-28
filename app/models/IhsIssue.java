package models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.joda.time.DateTime;


/**
 * Created by reza on 9/24/2014.
 */
@Entity
@Table(name = "ihsissue")
public class IhsIssue extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="issueID")
	public int issueID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "titleID")
	public IhsTitle ihsTitle;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "volumeID")
	public IhsVolume ihsVolume;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "publicationRangeID")
	public IhsPublicationRange ihsPublicationRange;

	@Column(name="publicationDate")
	public DateTime publicationDate;
	
	@Column(name="issueNumber")
	public String issueNumber;
	
	public String name;
	public String description;
	
	@Column(name="numPages", nullable = true)
	public Integer numpages;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issueStatusID")
	public SissueStatus sissueStatus;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ihsIssue")
	public List<IhsHolding> ihsHoldings = new ArrayList<IhsHolding>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "publicationDateId")
	public SpublicationDate spublicationDate;
	
	public IhsIssue(IhsTitle ihsTitle, IhsVolume ihsVolume,
			IhsPublicationRange ihsPublicationRange, DateTime publicationDate,
			String issueNumber, String name, String description, int numPages,
			int imagePageRatio, SissueStatus sissueStatus) {

		this.ihsTitle = ihsTitle;
		this.ihsVolume = ihsVolume;
		this.ihsPublicationRange = ihsPublicationRange;
		this.publicationDate = publicationDate;
		this.issueNumber = issueNumber;
		this.name = name;
		this.description = description;
		this.numpages = numPages;
		this.sissueStatus = sissueStatus;
	}
	
	public IhsIssue(IhsTitle ihsTitle, IhsVolume ihsVolume,
			IhsPublicationRange ihsPublicationRange, DateTime publicationDate,
			String issueNumber, String name, String description, int numPages,
			int imagePageRatio, SissueStatus sissueStatus, SpublicationDate spublicationDate) {

		this.ihsTitle = ihsTitle;
		this.ihsVolume = ihsVolume;
		this.ihsPublicationRange = ihsPublicationRange;
		this.publicationDate = publicationDate;
		this.issueNumber = issueNumber;
		this.name = name;
		this.description = description;
		this.numpages = numPages;
		this.sissueStatus = sissueStatus;
		this.spublicationDate = spublicationDate;
	}

	public static Finder<Integer, IhsIssue> find = new Finder<Integer, IhsIssue>(
			Integer.class, IhsIssue.class);
}