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
import play.Logger; /* AJE 2016-09-30 */

@Entity
//@Table(name = "ihspublishingJob") // Travant original
@Table(name = "ihspublishingjob") // AJE 2016-11-21
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

  // AJE 2016-11-21 new constructor contains 'link'
	public IhsPublishingJob(DateTime dateInitiated,  String jobName,
			IhsUser ihsUser, DateTime startDate, DateTime endDate,  String jsonString,
			SingestionJobStatus singestionJobStatus,
			String link,
			int fileformat) {
Logger.info("IhsPublishingJob.java, AJE 2016-11-21 new IhsPublishingJob constructor contains 'link'");
		this.jobName = jobName;
		this.ihsUser = ihsUser;
		this.startDate = startDate;
		this.endDate = endDate;
		this.jsonString = jsonString;
		this.singestionJobStatus = singestionJobStatus;
		this.link = link;
		this.fileformat = fileformat;
Logger.info("IhsPublishingJob.java, AJE 2016-11-21 new IhsPublishingJob constructor complete: link=" +link +".");
	}



	public void setSingestionJobStatus(SingestionJobStatus singestionJobStatus){
		this.singestionJobStatus = singestionJobStatus;
	}

	public void setLink(String link){
	  Logger.info("IhsPublishingJob.java, public void setLink(has link = " +link+ ")");
		this.link= link;
	}

	public static Finder<Integer, IhsPublishingJob> find = new Finder<Integer, IhsPublishingJob>(
			Integer.class, IhsPublishingJob.class);

/*
	public void update(){
		Logger.info("IhsPublishingJob.java, public void update is a fake method added by AJE 2016-11-21.");
	}
*/

}