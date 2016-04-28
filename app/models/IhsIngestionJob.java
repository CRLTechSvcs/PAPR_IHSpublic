package models;

import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reza on 9/11/2014.
 */
@Entity
@Table(name="ihsingestionjob")
public class IhsIngestionJob extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private static final int maxrecord = 50;
	 
    public static final String badExten = ".bad.txt";
	public static String  upDateSql = "UPDATE ihsingestionjob SET ingestionJobStatusID = :ingestionJobStatusID WHERE ingestionJobID = :ingestionJobID";

    @Id
    @GeneratedValue
    @Column(name="ingestionJobID")
    public int ingestionjobID;
    
    @Column(name="name")
    public String name;
    
    
    @Column(name="description")
    public String description;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="authorizedSourceID")
    public IhsAuthorizedSource authorizedSource;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ingestionDataTypeID")
    public SingestionDatatype singestionDatatype;
    
    @Column(name="creationDate")
    public Timestamp creationDate;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ingestedByUserID")
    public IhsUser ihsUser;
    
    @Column(name="sourceFileString")
    public String sourceFileString;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ingestionJobStatusID")
    public SingestionJobStatus singestionJobStatus;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ihsIngestionJob", cascade=CascadeType.ALL)
	public List<IhsIngestionRecord> ihsIngestionRecords = new ArrayList<>();
    
    @Column(name="statusDetail")
    public String statusDetail;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commitmentID")
	public Scommitment scommitment;
    
    public IhsIngestionJob(String name, String description,IhsAuthorizedSource authorizedSource, SingestionDatatype singestionDatatype,  
    		Timestamp creationDate, IhsUser ihsUser,String sourceFileString, SingestionJobStatus singestionJobStatus,String statusDetail )
    {
    	
    	this.name = name;
    	this.description = description;
    	this.authorizedSource = authorizedSource;
    	this.singestionDatatype = singestionDatatype;
		this.creationDate= creationDate;
		this.ihsUser = ihsUser;
		this.sourceFileString =  sourceFileString;
		this.singestionJobStatus = singestionJobStatus;
		this.statusDetail = statusDetail;
    }
    
    public IhsIngestionJob(String name, String description,IhsAuthorizedSource authorizedSource, SingestionDatatype singestionDatatype,  
    		Timestamp creationDate, IhsUser ihsUser,String sourceFileString, SingestionJobStatus singestionJobStatus,String statusDetail, Scommitment scommitment  )
    {
    	
    	this.name = name;
    	this.description = description;
    	this.authorizedSource = authorizedSource;
    	this.singestionDatatype = singestionDatatype;
		this.creationDate= creationDate;
		this.ihsUser = ihsUser;
		this.sourceFileString =  sourceFileString;
		this.singestionJobStatus = singestionJobStatus;
		this.statusDetail = statusDetail;
		this.scommitment = scommitment;
    }
    
    public static Finder<Integer, IhsIngestionJob> find = new Finder<Integer, IhsIngestionJob>(
			Integer.class, IhsIngestionJob
			.class);
       

    
	public static List<IhsIngestionJob> getAllUserJob() {

		List<IhsIngestionJob> ihsIngestionJobs = IhsIngestionJob.find
				.fetch("authorizedSource")
				.fetch("ihsUser")
				.fetch("singestionJobStatus")
				.orderBy("ingestionJobID desc").setMaxRows(maxrecord).findList();
		
		return ihsIngestionJobs;
	}
	
	public static List<IhsIngestionJob> getAUserJob(IhsUser ihsUser) {

		List<IhsIngestionJob> ihsIngestionJobs = IhsIngestionJob.find
				.fetch("authorizedSource")
				.fetch("ihsUser")
				.fetch("singestionJobStatus")
				.where()
				.eq("ihsUser", ihsUser)
				.orderBy("ingestionJobID desc")
				.setMaxRows(maxrecord)
				.findList();
		
		return ihsIngestionJobs;
	}
	
	public static List<IhsIngestionJob> getAMemberJob(IhsUser ihsUser) {

		
		List<IhsIngestionJob> ihsIngestionJobs = IhsIngestionJob.find
				.fetch("authorizedSource")
				.fetch("ihsUser")
				.fetch("singestionJobStatus")
				.where()
				.eq("ihsUser.ihsMember", ihsUser.ihsMember)
				.orderBy("ingestionJobID desc")
				.setMaxRows(maxrecord)
				.findList();
		
		return ihsIngestionJobs;
	}
	
	public static List<IhsIngestionJob> getAllQuedUserJob() {

		SingestionJobStatus singestionJobStatus = SingestionJobStatus.find.where().eq("name", SingestionJobStatus.Queued ).findUnique();
		
		List<IhsIngestionJob> ihsIngestionJobs = IhsIngestionJob.find
				.fetch("authorizedSource")
				.fetch("ihsUser")
				.fetch("singestionJobStatus")
				.where()
				.eq("singestionJobStatus", singestionJobStatus)
				.orderBy("ingestionJobID desc")
				.setMaxRows(maxrecord)
				.findList();
		
		return ihsIngestionJobs;
	}
	
	public static List<IhsIngestionJob> getAllArchiveUserJob() {

		SingestionJobStatus singestionJobStatus = SingestionJobStatus.find.where().eq("name", SingestionJobStatus.Processing ).findUnique();
		
		List<IhsIngestionJob> ihsIngestionJobs = IhsIngestionJob.find
				.fetch("authorizedSource")
				.fetch("ihsUser")
				.fetch("singestionJobStatus")
				.where()
				.ne("singestionJobStatus", singestionJobStatus)
				.orderBy("ingestionJobID desc")
				.setMaxRows(maxrecord)
				.findList();
		
		return ihsIngestionJobs;
	}
	
	public static void updateJobId(SingestionJobStatus singestionJobStatus, IhsIngestionJob ihsIngestionJob ){
		
		SqlUpdate update = Ebean.createSqlUpdate(upDateSql);  
		update.setParameter("ingestionJobStatusID", singestionJobStatus.ingestionJobStatusID);
		update.setParameter("ingestionJobID", ihsIngestionJob.ingestionjobID);
		Ebean.execute(update);
		
	}
	
}