package models;

import play.db.ebean.Model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.DateTime;

/**
 * Created by reza on 9/11/2014.
 */
@Entity
@Table(name = "ihspublicationrange")
public class IhsPublicationRange extends Model {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    @Column(name="publicationRangeID")
    public int publicationRangeID;
   
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="titleID")
    public IhsTitle ihsTitle;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="periodicityTypeID")
    public SperiodicityType speriodicityType;
    
    @Column(name="pbrStartDate")
    public DateTime startDate;
    
    @Column(name="pbrEndDate")
    public DateTime endDate;

    public IhsPublicationRange (IhsTitle ihsTitle, SperiodicityType speriodicityType, DateTime startDate, DateTime endDate){
    	this.ihsTitle = ihsTitle;
    	this.speriodicityType = speriodicityType;
    	this.startDate = startDate;
    	this.endDate = endDate;
    }
   
    public void setStartDate(DateTime startDate){
    	
    	this.startDate = startDate;
    }
    
    public static Finder<Integer, IhsPublicationRange> find = new Finder<Integer, IhsPublicationRange>(
			Integer.class, IhsPublicationRange.class);
}