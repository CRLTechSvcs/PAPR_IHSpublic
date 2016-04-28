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

import org.joda.time.DateTime;




import play.db.ebean.Model;


@Entity
@Table(name = "ihstitleversion")

public class IhsTitleVersion  extends Model {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "titleversionID")
	public int titleversionID;
	
	@Column(name = "titleID")
	public int titleID;

	public String title;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @Column(name="titleTypeID")
    public int titleTypeID;
	
	@Column(name="alphaTitle")
	public String alphaTitle;
	
	@Column(name="printISSN")
	public String printISSN;
	
	@Column(name="eISSN")
	public String eISSN;
	
	@Column(name="oclcNumber")
	public String oclcNumber;
	
	public String lccn;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="publisherID")
    public IhsPublisher ihsPublisher;
	
	public String description;

    @Column(name="titleStatusID")
    public int titleStatusID;
	
    
    @Column(name="changeDate")
    public DateTime changeDate;
    
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")
	public IhsUser ihsUser;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ihsTitleVersion")
	public List<IhsPublicationRangeVer> ihsPublicationRangeVer = new ArrayList<>();
    
    @Column(name="imagePageRatio")
	public Integer imagePageRatio;
	
    @Column(name="language")
	public String language;
    
    @Column(name="country")
	public String country;
	
    
	public IhsTitleVersion(){
		
	};
	
	
	public IhsTitleVersion(int titleID, String title, String alphaTitle, String printISSN,
			String eISSN,  String oclcNumber, String lccn,
			IhsPublisher ihsPublisher, String description,   DateTime changeDate,  IhsUser ihsUser, Integer imagePageRatio ,
			String language,
			String country) {
		
		this.titleID = titleID;
		this.title = title;
		this.alphaTitle = alphaTitle;
		this.printISSN = printISSN;
		this.eISSN = eISSN;
		this.oclcNumber = oclcNumber;
		this.lccn = lccn;
		this.ihsPublisher = ihsPublisher;
		this.description = description;
		this.ihsUser= ihsUser;
		this.changeDate= changeDate;
		this.imagePageRatio = imagePageRatio;
		this.language = language;
		this.country = country;
	}

	public static Finder<Integer, IhsTitleVersion> find = new Finder<Integer, IhsTitleVersion>(
			Integer.class, IhsTitleVersion.class);
}