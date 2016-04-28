package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.DateTime;

import play.db.ebean.Model;


@Entity
@Table(name = "ihspublishingJob")
public class IhsPublishingJob  extends Model{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "publishingJobId")
	public int publishingJobId;

	@Column(name = "dateInitiated")
	public DateTime dateInitiated;

	@Column(name = "jobName")
	public String jobName;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	public IhsUser ihsUser;

	@Column(name = "startDate")
	public DateTime startDate;

	@Column(name = "endDate")
	public DateTime endDate;
	
	@Column(name = "jsonString")
	public String jsonString;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="jobStatusId")
    public SingestionJobStatus singestionJobStatus;

	@Column(name = "link")
	public String link;
	
	@Column(name = "fileformat")
	public int fileformat;
	
	public IhsPublishingJob(DateTime dateInitiated,  String jobName,
			IhsUser ihsUser, DateTime startDate, DateTime endDate,  String jsonString, 
			SingestionJobStatus singestionJobStatus, int fileformat) {

		this.jobName = jobName;
		this.ihsUser = ihsUser;
		this.startDate = startDate;
		this.endDate = endDate;
		this.jsonString = jsonString;
		this.singestionJobStatus = singestionJobStatus;
		this.fileformat = fileformat;
	}

	public void setSingestionJobStatus(SingestionJobStatus singestionJobStatus){
		this.singestionJobStatus = singestionJobStatus;	
	}
	
	public void setLink(String link){
		this.link= link;
	}
	
	public static Finder<Integer, IhsPublishingJob> find = new Finder<Integer, IhsPublishingJob>(
			Integer.class, IhsPublishingJob.class);
}