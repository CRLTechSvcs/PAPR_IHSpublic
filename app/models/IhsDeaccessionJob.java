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
@Table(name = "ihsdeaccessionJob")
public class IhsDeaccessionJob extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "deaccessionJobId")
	public int deaccessionJobId;

	@Column(name = "dateInitiated")
	public DateTime dateInitiated;

	@Column(name = "dateCompleted")
	public DateTime dateCompleted;

	@Column(name = "jbbName")
	public String jbbName;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	public IhsUser ihsUser;

	@Column(name = "selected")
	public int selected;

	@Column(name = "jsonString")
	public String jsonString;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="jobStatusId")
    public SingestionJobStatus singestionJobStatus;
	
	@Column(name = "link")
	public String link;
	
	public IhsDeaccessionJob(DateTime dateInitiated,  String jbbName,
			IhsUser ihsUser, int selected, String jsonString, SingestionJobStatus singestionJobStatus, String link) {

		this.dateInitiated = dateInitiated;
		this.jbbName = jbbName;
		this.ihsUser = ihsUser;
		this.selected = selected;
		this.jsonString = jsonString;
		this.singestionJobStatus = singestionJobStatus;
		this.link = link;
	}

	public static Finder<Integer, IhsDeaccessionJob> find = new Finder<Integer, IhsDeaccessionJob>(
			Integer.class, IhsDeaccessionJob.class);

}