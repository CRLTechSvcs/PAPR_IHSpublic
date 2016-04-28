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
import play.db.ebean.Model.Finder;

@Entity
@Table(name = "ihsreportingjob")
public final class IhsReportingJob  extends Model{

	@Id
	@Column(name = "reportingJobId")
	public int reportingJobId;
	
	@Column(name = "dateInitiated")
	public DateTime dateInitiated;

	@Column(name = "report")
	public String report;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	public IhsUser ihsUser;
	
	
	@Column(name = "jsonString")
	public String jsonString;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="jobStatusId")
    public SingestionJobStatus singestionJobStatus;

	@Column(name = "link")
	public String link;
	
	@Column(name = "fileformat")
	public String fileformat;
	
	@Column(name = "parameters")
	public String parameters;
	
	public IhsReportingJob(DateTime dateInitiated,  String report,
			IhsUser ihsUser,   String jsonString, 
			SingestionJobStatus singestionJobStatus, String fileformat, 
			String parameters) {

		this.dateInitiated = dateInitiated;
		this.report = report;
		this.ihsUser = ihsUser;
		this.jsonString = jsonString;
		this.singestionJobStatus = singestionJobStatus;
		this.fileformat = fileformat;
		this.parameters = parameters;
	}

	public void setSingestionJobStatus(SingestionJobStatus singestionJobStatus){
		this.singestionJobStatus = singestionJobStatus;	
	}
	
	public void setLink(String link){
		this.link= link;
	}
	
	public static Finder<Integer, IhsReportingJob> find = new Finder<Integer, IhsReportingJob>(
			Integer.class, IhsReportingJob.class);
}