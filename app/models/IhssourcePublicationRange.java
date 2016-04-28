package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * Created by reza on 9/24/2014.
 */

@Entity
@Table(name = "ihssourcepublicationrange")
public class IhssourcePublicationRange extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "sourcePublicationRangeID")
	public int sourcePublicationRangeID;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ingestionRecordID")
	public IhsIngestionRecord ihsIngestionRecord;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "titleID")
	public IhsTitle ihsTitle;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "periodicityTypeID")
	public SperiodicityType speriodicityType;

	//TODO Change them to date
	@Column(name = "sPbrStartDate")
	int startDate;

	@Column(name = "sPbrEndDate")
	int endDate;

	public IhssourcePublicationRange(IhsIngestionRecord ihsIngestionRecord,
			IhsTitle ihsTitle, SperiodicityType speriodicityType,
			int startDate, int endDate) {
		
		this.ihsIngestionRecord = ihsIngestionRecord;
		this.ihsTitle = ihsTitle;
		this.speriodicityType  = speriodicityType;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public static Finder<Integer, IhssourcePublicationRange> find = new Finder<Integer, IhssourcePublicationRange>(
			Integer.class, IhssourcePublicationRange.class);
}