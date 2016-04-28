package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import play.Logger;
import play.db.ebean.Model;
import util.Helper;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import json.DeaccesionReportView;
import json.DeaccessionIssueView;
import json.DeaccessionIthacaView;
import json.DeaccessionNewView;
import json.DeaccessionTitleView;
import json.TitleView;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

/**
 * Created by reza on 9/24/2014.
 */
@Entity
@Table(name = "ihsholding")
public class IhsHolding extends Model {

	static DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM, yyyy");

	static public String Uncommitted = "Uncommitted";
	static public String Committed = "Committed";

	static String MinJournaltoDeaccess = " select titleId from ("
			+ "select titleID,  (part/total) * 100   as percen"
			+ " from "
			+ " ( "
			+ " select titleID, count(distinct iss1.issueID) as total, "
			+ " sum( CASE when hld1.issueID is not null and memberID = :memberID1 then 1 else 0 end) as part "
			+ " from ihsissue iss1 "
			+ " left join ihsholding hld1 on iss1.issueID = hld1.issueID "
			+ " where titleID in ( " + " select distinct titleID "
			+ " from ihsholding hld "
			+ " join ihsissue iss on iss.issueID = hld.issueID "
			+ " where memberID = :memberID2 ) " + " group by titleID "
			+ " ) as tmp1" + " ) as tmp2" + " where percen > :per";

	static String MinJournaltoDeaccessISSN = "select titleId from ( "
			+ " select titleID,  (part/total) * 100   as percen "
			+ " from "
			+ " ( "
			+ "  select titleID, count(distinct iss1.issueID) as total, "
			+ " sum( CASE when hld1.issueID is not null and memberID = :memberID1 then 1 else 0 end) as part "
			+ "  from ihsissue iss1 "
			+ " left join ihsholding hld1 on iss1.issueID = hld1.issueID "
			+ " where titleID in (    select distinct iss.titleID as titleID "
			+ " from ihsholding hld "
			+ " join ihsissue iss on iss.issueID = hld.issueID "
			+ " join ihstitle til on til.titleid =  iss.titleid "
			+ " where memberID = :memberID2 "
			+ " and printISSN = :issn)    group by titleID "
			+ " ) as tmp1   ) as tmp2   where percen > :per";

	static String MinJournaltoDeaccessOCLC = "select titleId from ( "
			+ " select titleID,  (part/total) * 100   as percen "
			+ " from "
			+ " ( "
			+ "  select titleID, count(distinct iss1.issueID) as total, "
			+ " sum( CASE when hld1.issueID is not null and memberID = :memberID1 then 1 else 0 end) as part "
			+ "  from ihsissue iss1 "
			+ " left join ihsholding hld1 on iss1.issueID = hld1.issueID "
			+ " where titleID in (    select distinct iss.titleID as titleID "
			+ " from ihsholding hld "
			+ " join ihsissue iss on iss.issueID = hld.issueID "
			+ " join ihstitle til on til.titleid =  iss.titleid "
			+ " where memberID = :memberID2 "
			+ " and oclcNumber = :oclc)    group by titleID "
			+ " ) as tmp1   ) as tmp2   where percen > :per";

	static String ithecaAllHolding = "select titleID,  FLOOR ( (part/total) * 100 )   as percen, vald "
			+ " from  "
			+ " ( "
			+ " select  titleID, count(distinct iss1.issueID) as total, "
			+ " sum( CASE when hld1.issueID is not null and memberID = :memberID1 then 1 else 0 end) as part, "
			+ " sum( CASE when sval.name = 'PageLevel' and  memberID = :memberID2  then 1 else 0 end) as vald "
			+ " from ihsissue iss1 "
			+ " left join ihsholding hld1 on iss1.issueID = hld1.issueID "
			+ " left join svalidationLevel sval on sval.validationLevelID = hld1.validationLevelID "
			+ " where titleid = :titleId " + " ) as tmp1";

	static String ithecaWithCommitment = "select titleID,  FLOOR ( (part/total) * 100 )   as percen, vald "
			+ " from  "
			+ " ( "
			+ " select  titleID, count(distinct iss1.issueID) as total, "
			+ " sum( CASE when hld1.issueID is not null and cmt.name = :committed and memberID = :memberID1 then 1 else 0 end) as part, "
			+ " sum( CASE when sval.name = 'PageLevel' and  memberID = :memberID2  then 1 else 0 end) as vald "
			+ " from ihsissue iss1 "
			+ " left join ihsholding hld1 on iss1.issueID = hld1.issueID "
			+ " left join svalidationLevel sval on sval.validationLevelID = hld1.validationLevelID "
			+ " left join scommitment cmt on cmt.commitmentID = hld1.commitmentID "
			+ " where titleid = :titleId " + " ) as tmp1";

	static String CRLPreCriteriaAllHolding = " select  titleID, "
			+ " SUM(case when all1.name = 'Good' or all1.name = 'Excellent' then 1 else 0 end) cond, "
			+ " SUM(case when sval.level >= :lvl  then 1 else 0 end) copy	"
			+

			" from ihsissue iss1 "
			+ " left join ihsholding hld1 on iss1.issueID = hld1.issueID "
			+ " left join sconditionTypeOverall all1 on all1.conditionTypeOverallID = hld1.conditionTypeOverallID "
			+ " left join svalidationLevel sval on sval.validationLevelID = hld1.validationLevelID "
			+ " left join scommitment cmt on cmt.commitmentID = hld1.commitmentID"
			+ " where titleid = :titleId  " + " and hld1.memberID = :memberID";
	// and cmt.name ='Uncommitted';

	static String CRLPreCriteriaAllCommit = " select  titleID, "
			+ " SUM(case when all1.name = 'Good' or all1.name = 'Excellent' then 1 else 0 end) cond, "
			+ " SUM(case when sval.level >= :lvl  then 1 else 0 end) copy	"
			+

			" from ihsissue iss1 "
			+ " left join ihsholding hld1 on iss1.issueID = hld1.issueID "
			+ " left join sconditionTypeOverall all1 on all1.conditionTypeOverallID = hld1.conditionTypeOverallID "
			+ " left join svalidationLevel sval on sval.validationLevelID = hld1.validationLevelID "
			+ " left join scommitment cmt on cmt.commitmentID = hld1.commitmentID"
			+ " where titleid = :titleId  " + " and hld1.memberID = :memberID "
			+ " and cmt.name = :committed ";

	static String TitleRecord = "Select title, printISSN,  CONCAT('Vol.' , ihv.volumeNumber , '  Issue.', iss1.issueNumber) valissue, holdingID, publicationDate "
			+ " from ihstitle til "
			+ " join ihsvolume ihv on ihv.titleID = til.titleID "
			+ " join ihsissue iss1 on iss1.volumeID = ihv.volumeID "
			+ " join ihsholding hld1 on iss1.issueID = hld1.issueID"
			+ " where til.titleId = :titleId  "
			+ " and hld1.memberID = :memberID ";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "holdingID")
	public int holdingID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issueID")
	public IhsIssue ihsIssue;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "memberID")
	public IhsMember ihsMember;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "locationID")
	public IhsLocation ihsLocation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "holdingStatusID")
	public SholdingStatus sholdingStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "conditionTypeOverallID")
	public SconditionTypeOverall sconditionTypeOverall;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ihsVarifiedID")
	public SihsVarified sihsVarified;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "validationLevelID")
	public SvalidationLevel svalidationLevel;

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "ihsholding")
	public List<IhsHoldingNote> ihsHoldingNotes = new ArrayList<IhsHoldingNote>();

	@ManyToMany(cascade = CascadeType.REMOVE)
	public Set<SconditionType> sconditionType = new HashSet<SconditionType>();;

	@Column(name = "missingPages")
	public String missingPages;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commitmentID")
	public Scommitment scommitment;

	public IhsHolding(IhsIssue ihsIssue, IhsMember ihsMember,
			IhsLocation ihsLocation, SholdingStatus sholdingStatus,
			SconditionTypeOverall sconditionTypeOverall,
			SihsVarified sihsVarified, SvalidationLevel svalidationLevel,  Scommitment scommitment) {

		this.ihsIssue = ihsIssue;
		this.ihsMember = ihsMember;
		this.ihsLocation = ihsLocation;
		this.sholdingStatus = sholdingStatus;
		this.sconditionTypeOverall = sconditionTypeOverall;
		this.sihsVarified = sihsVarified;
		this.svalidationLevel = svalidationLevel;
		this.scommitment = scommitment;

	}

	public static Finder<Integer, IhsHolding> find = new Finder<Integer, IhsHolding>(
			Integer.class, IhsHolding.class);

	public static DeaccesionReportView searchDeaccession(IhsUser ihsUser,
			DeaccessionNewView deaccessionNewView) {

		//List<IhsTitle> deaccIhsTitles = new ArrayList<IhsTitle>();

		DeaccesionReportView deaccesionReportView = new DeaccesionReportView();

		List<IhsTitle> allTitles = new ArrayList<IhsTitle>();

		List<IhsMember> otherMembers = null;

		// Find the member to be searched
		if ("group".equals(deaccessionNewView.groupFlag)) {
			otherMembers = IhsMember.find.where()
					.eq("ihsMemberGroup", ihsUser.ihsMember.ihsMemberGroup)
					.findList();
		} else {
			otherMembers = IhsMember.find.where().findList();
		}

		// Find the title are eligbel for Deaccss
		if (!deaccessionNewView.fileContend.equals("")) {

			if (deaccessionNewView.fileType.equals("issn")) {

				String fileContend = deaccessionNewView.fileContend.replaceAll(
						"-", "");
				String[] issns = fileContend.split("\\r\\n");

				for (int i = 0; i < issns.length; i++) {

					List<SqlRow> sqlRows = Ebean
							.createSqlQuery(MinJournaltoDeaccessISSN)
							.setParameter("memberID1",
									ihsUser.ihsMember.memberID)
							.setParameter("memberID2",
									ihsUser.ihsMember.memberID)
							.setParameter("issn", issns[i])
							.setParameter("per", deaccessionNewView.minDeaccess)
							.findList();

					for (SqlRow sqlRow : sqlRows) {
						int titleId = sqlRow.getInteger("titleId");
						IhsTitle ihsTitle = IhsTitle.find.byId(titleId);
						allTitles.add(ihsTitle);
					}
				}

			} else {
				String[] oclcs = deaccessionNewView.fileContend.split("\\r\\n");
				for (int i = 0; i < oclcs.length; i++) {

					List<SqlRow> sqlRows = Ebean
							.createSqlQuery(MinJournaltoDeaccessOCLC)
							.setParameter("memberID1",
									ihsUser.ihsMember.memberID)
							.setParameter("memberID2",
									ihsUser.ihsMember.memberID)
							.setParameter("oclc", oclcs[i])
							.setParameter("per", deaccessionNewView.minDeaccess)
							.findList();

					for (SqlRow sqlRow : sqlRows) {
						int titleId = sqlRow.getInteger("titleId");
						IhsTitle ihsTitle = IhsTitle.find.byId(titleId);
						allTitles.add(ihsTitle);
					}
				}
			}

		} else {
			// Min % of Journal to Deaccess
			List<SqlRow> sqlRows = Ebean.createSqlQuery(MinJournaltoDeaccess)
					.setParameter("memberID1", ihsUser.ihsMember.memberID)
					.setParameter("memberID2", ihsUser.ihsMember.memberID)
					.setParameter("per", deaccessionNewView.minDeaccess)
					.findList();

			for (SqlRow sqlRow : sqlRows) {
				int titleId = sqlRow.getInteger("titleId");
				IhsTitle ihsTitle = IhsTitle.find.byId(titleId);
				
				
				allTitles.add(ihsTitle);
			}
		}

		// Loop over selecter title
		for (IhsTitle ihsTitle : allTitles) {

			
			
			if (ihsTitle.imagePageRatio == null
					|| ihsTitle.imagePageRatio >= deaccessionNewView.deaccessionIthacaView.ImagePageRatio) {

				Logger.debug("ihsTitle:" +  ihsTitle.printISSN);
				
				// loop for other memenr
				for (IhsMember otherMember : otherMembers) {

					if (otherMember.memberID == ihsUser.ihsMember.memberID)
						continue;

					SqlRow sqlRows1 = null;

					// Ithica processing
					if (deaccessionNewView.standard.equals("Ithica")) {
						if (deaccessionNewView.committed == 0) {
							sqlRows1 = Ebean
									.createSqlQuery(ithecaAllHolding)
									.setParameter("memberID1",
											otherMember.memberID)
									.setParameter("memberID2",
											otherMember.memberID)
									.setParameter("titleId", ihsTitle.titleID)
									.findUnique();
						} else {
							String comit = deaccessionNewView.committed == 1 ? "committed"
									: "Uncommitted";
							sqlRows1 = Ebean
									.createSqlQuery(ithecaWithCommitment)
									.setParameter("memberID1",
											otherMember.memberID)
									.setParameter("memberID2",
											otherMember.memberID)
									.setParameter("committed", comit)
									.setParameter("titleId", ihsTitle.titleID)
									.findUnique();
						}

						if (sqlRows1 != null) {
							Integer percen = sqlRows1.getInteger("percen");
							Integer vald = sqlRows1.getInteger("vald");

							Logger.debug("pecnet:" + percen  + ":" + deaccessionNewView.deaccessionIthacaView.HeldOfTitle );
							
							if (percen != null && vald != null) {
								if (percen.intValue() >= deaccessionNewView.deaccessionIthacaView.HeldOfTitle
										&& vald.intValue() >= deaccessionNewView.deaccessionIthacaView.PageVerifiedCopy) {
							
									Logger.debug("pecnet:" + percen  + ":" + deaccessionNewView.deaccessionIthacaView.HeldOfTitle );
									

									List<SqlRow> sqlRows2s = Ebean
											.createSqlQuery(TitleRecord)
											.setParameter("titleId",
													ihsTitle.titleID)
											.setParameter("memberID",
													ihsUser.ihsMember.memberID)
											.findList();

									List<DeaccessionIssueView> deaccessionIssueViews = new ArrayList<DeaccessionIssueView>();

									for (SqlRow sqlRow2 : sqlRows2s) {
										int holdingID = sqlRow2
												.getInteger("holdingID");
										String valissue = sqlRow2
												.getString("valissue");
										String publicationDate = sqlRow2
												.getString("publicationDate") != null ? dtf
												.print(new DateTime(
														sqlRow2.getString("publicationDate")))
												: "";

										deaccessionIssueViews
												.add(new DeaccessionIssueView(
														holdingID, valissue,
														publicationDate));
									}

									DeaccessionTitleView deaccessionTitleView = new DeaccessionTitleView(
											ihsTitle.titleID, ihsTitle.title,
											Helper.formatIssn(ihsTitle.printISSN),
											deaccessionIssueViews.size(),
											deaccessionIssueViews);

									deaccesionReportView.deaccessionTitleView
											.add(deaccessionTitleView);

									break;
								}
							}
						}

						// CRL pproccsing
					} else {
						if (deaccessionNewView.committed == 0) {
							sqlRows1 = Ebean
									.createSqlQuery(CRLPreCriteriaAllHolding)
									.setParameter("memberID",
											otherMember.memberID)
									.setParameter("titleId", ihsTitle.titleID)
									.setParameter(
											"lvl",
											deaccessionNewView.deaccessionCrlView.varificationLevel)
									.findUnique();
						} else {
							String comit = deaccessionNewView.committed == 1 ? "committed"
									: "Uncommitted";
							sqlRows1 = Ebean
									.createSqlQuery(CRLPreCriteriaAllCommit)
									.setParameter("memberID",
											otherMember.memberID)
									.setParameter("committed", comit)
									.setParameter(
											"lvl",
											deaccessionNewView.deaccessionCrlView.varificationLevel)

									.findUnique();
						}

						if (sqlRows1 != null) {
							Integer cond = sqlRows1.getInteger("cond");
							Integer copy = sqlRows1.getInteger("copy");
							if (cond != null && copy != null) {
								if (cond.intValue() >= deaccessionNewView.deaccessionCrlView.goodCondition
										&& copy.intValue() >= deaccessionNewView.deaccessionCrlView.verfiedCopy) {
									List<SqlRow> sqlRows2s = Ebean
											.createSqlQuery(TitleRecord)
											.setParameter("titleId",
													ihsTitle.titleID)
											.setParameter("memberID",
													ihsUser.ihsMember.memberID)
											.findList();

									List<DeaccessionIssueView> deaccessionIssueViews = new ArrayList<DeaccessionIssueView>();

									for (SqlRow sqlRow2 : sqlRows2s) {
										int holdingID = sqlRow2
												.getInteger("holdingID");
										String valissue = sqlRow2
												.getString("valissue");
										String publicationDate = sqlRow2
												.getString("publicationDate") != null ? dtf
												.print(new DateTime(
														sqlRow2.getString("publicationDate")))
												: "";

										deaccessionIssueViews
												.add(new DeaccessionIssueView(
														holdingID, valissue,
														publicationDate));
									}

									DeaccessionTitleView deaccessionTitleView = new DeaccessionTitleView(
											ihsTitle.titleID, ihsTitle.title,
											Helper.formatIssn(ihsTitle.printISSN),
											deaccessionIssueViews.size(),
											deaccessionIssueViews);

									deaccesionReportView.deaccessionTitleView
											.add(deaccessionTitleView);
									break;
								}
							}
						}

						// End Ithican and crl procssing
					}
					// Other member
				}
			}
			//
		}

		for (DeaccessionTitleView deaccessionTitleView : deaccesionReportView.deaccessionTitleView) {
			
			PwantTitleMember pwanttitlemember = PwantTitleMember.find.where()
					.eq("titleID", deaccessionTitleView.titleId)
					.setMaxRows(1).findUnique();

			if(pwanttitlemember != null){
				for ( DeaccessionIssueView deaccessionIssueView : deaccessionTitleView.deaccessionIssueView){
					deaccessionIssueView.action="n";
				}
				deaccesionReportView.NumberOfDonation += deaccessionTitleView.deaccessionIssueView.size();
			}else {
				deaccesionReportView.NumberOfDeaccession += deaccessionTitleView.deaccessionIssueView.size();
			}
			
			deaccesionReportView.NumberOfHolding += deaccessionTitleView.deaccessionIssueView.size();
			
			// deaccessionTitleView.
		}
		return deaccesionReportView;
	}
}