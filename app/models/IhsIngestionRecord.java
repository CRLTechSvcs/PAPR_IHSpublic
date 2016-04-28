package models;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;


/**
 * Created by reza on 9/11/2014.
 */
@Entity
@Table(name = "ihsingestionrecord")
public class IhsIngestionRecord extends Model {

	public static int maxRecord = 5;
	public static String tilde = "~";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public static String upuateStatusDetailSql = "UPDATE ihsingestionrecord SET ingestionRecordStatusID = :ingestionrecordstatusid, "
			+ " jsonRecordData = :jsonrecorddata, recordTitle = :recordtitle, issues = :issue "
			+ " WHERE ingestionRecordID = :ingestionrecordid";


	public static String selectTotalAndErrorCount = " select sum(case when sts.name != 'Processing' then 1  else 0 end) as total_proc_rec "
			+ ", sum(case when sts.name = 'Available' then 1  else 0 end)  as exp_rec  "
			+ ", sum(case when sts.name = 'BadRecord' then 1  else 0 end)  as bad_rec  "
			+ " from ihsingestionrecord rec  "
			+ " join singestionrecordstatus sts  on rec.ingestionRecordStatusID = sts.ingestionRecordStatusID   "
			+ " where ingestionJobID = :ingestionjobid;";

	@Id
	@GeneratedValue
	@Column(name = "ingestionRecordID")
	public int ingestionRecordID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ingestionJobID")
	public IhsIngestionJob ihsIngestionJob;

	@Column(name = "rawRecordData", length = 1000)
	public String rawRecordData;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ingestionRecordStatusID")
	public SingestionRecordStatus singestionRecordStatus;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ihsIngestionRecord", cascade=CascadeType.ALL)
	public List<IhsIngestionException> ihsIngestionExceptions;

	//@OneToOne(fetch = FetchType.LAZY, mappedBy = "ihsIngestionRecord")
	//public IhsSourceTitle ihsSourceTitle;

	@Column(name = "recordTitle")
	public String recordTitle;

	@Column(name = "issues")
	public String issues;

	@Column(name = "jsonRecordData", length = 5000)
	public String jsonRecordData;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userID")
	public IhsUser ihsUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lockUserID")
	public IhsUser locIshUser;

	@Column(name = "lockDate")
	Date lockDate;

	/*
	 * Constructor
	 */
	public IhsIngestionRecord(IhsIngestionJob ihsIngestionJob,
			String rawRecordData,
			SingestionRecordStatus singestionRecordStatus, IhsUser ihsUser) {
		this.ihsIngestionJob = ihsIngestionJob;
		this.rawRecordData = rawRecordData;
		this.singestionRecordStatus = singestionRecordStatus;
		this.ihsUser = ihsUser;
	}

	/*
	 * 
	 */
	public static Finder<Integer, IhsIngestionRecord> find = new Finder<Integer, IhsIngestionRecord>(
			Integer.class, IhsIngestionRecord.class);

	/*
	 * 
	 */
	public static void postExceptionIgnore(int recordId) {

		SingestionRecordStatus singestionRecordStatus = SingestionRecordStatus.find
				.where().eq("name", SingestionRecordStatus.Ingnored)
				.findUnique();

		IhsIngestionRecord ihsIngestionRecord = IhsIngestionRecord.find
				.byId(recordId);

		ihsIngestionRecord.singestionRecordStatus = singestionRecordStatus;
	
		ihsIngestionRecord.update();

	}

	
	/*
	 * 
	 */

	public static void postExceptionOnhold(int recordId, int userId) {

		SingestionRecordStatus singestionRecordStatus = SingestionRecordStatus.find
				.where().eq("name", SingestionRecordStatus.OnHold).findUnique();

		IhsIngestionRecord ihsIngestionRecord = IhsIngestionRecord.find
				.byId(recordId);
		
		IhsUser ihsUser = IhsUser.find.byId(userId);
		
		ihsIngestionRecord.locIshUser = ihsUser;
		
		ihsIngestionRecord.singestionRecordStatus = singestionRecordStatus;
		
		ihsIngestionRecord.update();
		

	}

	/*
     * 
     */
	public static void updateStatusDetail(
			IhsIngestionRecord lihsIngestionRecord,
			SingestionRecordStatus lsingestionRecordStatus, String payload,
			String recordTitle, String issue) {
		
		SqlUpdate update = Ebean.createSqlUpdate(upuateStatusDetailSql);
		update.setParameter("ingestionrecordstatusid",
				lsingestionRecordStatus.ingestionRecordStatusID);
		update.setParameter("jsonrecorddata", payload);
		update.setParameter("recordtitle", recordTitle);
		update.setParameter("issue", issue);
		update.setParameter("ingestionrecordid",
				lihsIngestionRecord.ingestionRecordID);
		Ebean.execute(update);
	}

	/*
	 * Get the data error exception
	 * 
	 * @param
	 * 
	 * @return List IhsIngestionRecord
	 */
	public static List<IhsIngestionRecord> getAllUserExceptions(int offset) {

		List<String> status = new ArrayList<String>();
		status.add(SingestionRecordStatus.Available);
		status.add(SingestionRecordStatus.OnHold);

		List<SingestionRecordStatus> singestionRecordStatus = SingestionRecordStatus.find
				.where().in("name", status).findList();

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord.find
				.fetch("ihsIngestionExceptions").where()
				.in("singestionRecordStatus", singestionRecordStatus)
				.setFirstRow(offset).setMaxRows(maxRecord)
				.orderBy("ingestionRecordID ASC").findList();
		return ihsIngestionRecords;
	}

	public static List<IhsIngestionRecord> getAUserExceptions(int offset,
			IhsUser ihsUser) {

		List<String> status = new ArrayList<String>();
		status.add(SingestionRecordStatus.Available);
		status.add(SingestionRecordStatus.OnHold);

		List<SingestionRecordStatus> singestionRecordStatus = SingestionRecordStatus.find
				.where().in("name", status).findList();

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord.find
				.fetch("ihsIngestionExceptions").where().eq("ihsUser", ihsUser)
				.in("singestionRecordStatus", singestionRecordStatus)
				.setFirstRow(offset).setMaxRows(maxRecord)
				.orderBy("ingestionRecordID ASC").findList();
		return ihsIngestionRecords;
	}

	public static List<IhsIngestionRecord> getAllUserAvailableExceptions(
			int offset) {

		List<SingestionRecordStatus> singestionRecordStatus = SingestionRecordStatus.find
				.where().eq("name", SingestionRecordStatus.Available)
				.findList();

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord.find
				.fetch("ihsIngestionExceptions").where()
				.in("singestionRecordStatus", singestionRecordStatus)
				.setFirstRow(offset).orderBy("ingestionRecordID ASC")
				.setMaxRows(maxRecord).findList();
		return ihsIngestionRecords;
	}

	public static List<IhsIngestionRecord> getAUserAvailableExceptions(
			int offset, IhsUser ihsUser) {

		List<SingestionRecordStatus> singestionRecordStatus = SingestionRecordStatus.find
				.where().eq("name", SingestionRecordStatus.Available)
				.findList();

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord.find
				.fetch("ihsIngestionExceptions").where().eq("ihsUser", ihsUser)
				.in("singestionRecordStatus", singestionRecordStatus)
				.setFirstRow(offset).orderBy("ingestionRecordID ASC")
				.setMaxRows(maxRecord).findList();
		return ihsIngestionRecords;
	}

	public static List<IhsIngestionRecord> getAllUserOnholdExceptions(int offset) {

		List<SingestionRecordStatus> singestionRecordStatus = SingestionRecordStatus.find
				.where().eq("name", SingestionRecordStatus.OnHold).findList();

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord.find
				.fetch("ihsIngestionExceptions").where()
				.in("singestionRecordStatus", singestionRecordStatus)
				.setFirstRow(offset).orderBy("ingestionRecordID ASC")
				.setMaxRows(maxRecord).findList();
		return ihsIngestionRecords;
	}

	public static List<IhsIngestionRecord> getAUserOnholdExceptions(int offset,
			IhsUser ihsUser) {

		List<SingestionRecordStatus> singestionRecordStatus = SingestionRecordStatus.find
				.where().eq("name", SingestionRecordStatus.OnHold).findList();

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord.find
				.fetch("ihsIngestionExceptions").where().eq("ihsUser", ihsUser)
				.in("singestionRecordStatus", singestionRecordStatus)
				.setFirstRow(offset).orderBy("ingestionRecordID ASC")
				.setMaxRows(maxRecord).findList();
		return ihsIngestionRecords;
	}

	public static List<IhsIngestionRecord> getAllExceptionsByJob(int offset,
			IhsIngestionJob ihsIngestionJob) {

		SingestionRecordStatus singestionRecordStatus = SingestionRecordStatus.find
				.where().eq("name", SingestionRecordStatus.Available)
				.findUnique();

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord.find
				.fetch("ihsIngestionExceptions").where()
				.eq("ihsIngestionJob", ihsIngestionJob)
				.eq("singestionRecordStatus", singestionRecordStatus)
				.setFirstRow(offset).orderBy("ingestionRecordID ASC")
				.setMaxRows(maxRecord).findList();
		return ihsIngestionRecords;
	}

	public static List<IhsIngestionRecord> getMyMemberExceptions(int offset,
			 IhsUser ihsUser) {



		List<String> status = new ArrayList<String>();
		status.add(SingestionRecordStatus.Available);
		status.add(SingestionRecordStatus.OnHold);

		List<SingestionRecordStatus> singestionRecordStatus = SingestionRecordStatus.find
				.where().in("name", status).findList();
		
		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord.find
				.fetch("ihsIngestionExceptions")
				.fetch("ihsUser")
				.where()
				.in("singestionRecordStatus", singestionRecordStatus)
				.eq("ihsUser.ihsMember", ihsUser.ihsMember)
				.setFirstRow(offset).orderBy("ingestionRecordID ASC")
				.setMaxRows(maxRecord).findList();
		return ihsIngestionRecords;
	}

	
	public static List<IhsIngestionRecord> getMyMemberAvailableExceptions(int offset,
			 IhsUser ihsUser) {

		SingestionRecordStatus singestionRecordStatus = SingestionRecordStatus.find
				.where().eq("name", SingestionRecordStatus.Available)
				.findUnique();

		
		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord.find
				.fetch("ihsIngestionExceptions")
				.fetch("ihsUser")
				.where()
				.eq("singestionRecordStatus", singestionRecordStatus)
				.eq("ihsUser.ihsMember", ihsUser.ihsMember)
				.setFirstRow(offset).orderBy("ingestionRecordID ASC")
				.setMaxRows(maxRecord).findList();
		return ihsIngestionRecords;
	}

	public static List<IhsIngestionRecord> getMyMemberOnHoleExceptions(int offset,
			 IhsUser ihsUser) {

		SingestionRecordStatus singestionRecordStatus = SingestionRecordStatus.find
				.where().eq("name", SingestionRecordStatus.OnHold)
				.findUnique();

		
		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord.find
				.fetch("ihsIngestionExceptions")
				.fetch("ihsUser")
				.where()
				.eq("singestionRecordStatus", singestionRecordStatus)
				.eq("ihsUser.ihsMember", ihsUser.ihsMember)
				.setFirstRow(offset).orderBy("ingestionRecordID ASC")
				.setMaxRows(maxRecord).findList();
		return ihsIngestionRecords;
	}
	
	public static String getRecordTotalAndError(IhsIngestionJob ihsIngestionJob) {

		String st = "";

		SqlRow sqlRows = Ebean.createSqlQuery(selectTotalAndErrorCount)
				.setParameter("ingestionjobid", ihsIngestionJob.ingestionjobID)
				.findUnique();

		int total_rec =  sqlRows.getInteger("total_proc_rec") !=  null ? sqlRows.getInteger("total_proc_rec").intValue(): 0;

		int exp_rec =  sqlRows.getInteger("exp_rec") !=null ? sqlRows.getInteger("exp_rec").intValue() : 0;

		int bad_rec = sqlRows.getInteger("bad_rec") != null ? sqlRows.getInteger("bad_rec").intValue() : 0;

		String expRecString = "";
		String badRecString = "";

		// ingestion_exception_jobid/

		if (SingestionJobStatus.Complete
				.equals(ihsIngestionJob.singestionJobStatus.name)) {

			
			if (exp_rec > 0) {
				
				expRecString = "<a href=/ingestion_exception_jobid/" + ihsIngestionJob.ingestionjobID + ">"+ exp_rec +" Exceptions</a>";

			} else {
				expRecString = exp_rec + " Exceptions";
			}
			
			if (bad_rec > 0) {
				String filename = ihsIngestionJob.sourceFileString.replace(
						System.getProperty("file.separator"), tilde)
						+ ihsIngestionJob.badExten;

				badRecString = "<a href=/ingestion/getBadformatFile/" + filename
						+ ">" + +bad_rec + " Bad Records </a>";

			} else {
				badRecString = bad_rec + " Bad Records";
			}
			
		} else {
			badRecString = bad_rec + " Bad Records";
			expRecString = exp_rec + " Exceptions";
		}

		return total_rec + " Records Processed <br>" + expRecString
				+ " <br> " + badRecString;

	}

}