package models;

import play.db.ebean.Model;
import util.Helper;

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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.avaje.ebean.FetchConfig;

import controllers.Login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import json.Chart;
import json.CommitmentView;
import json.HoldingConditionsView;
import json.HoldingView;
import json.IhsVerifiedView;
import json.IssueView;
import json.OverallView;
import json.ValidationLevel;
import json.VolumeView;

/**
 * Created by reza on 9/24/2014.
 */
@Entity
@Table(name = "ihsvolume")
public class IhsVolume extends Model {

	private static DateTimeFormatter dateFormat = DateTimeFormat
			.forPattern("MMM");
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "volumeID")
	public int volumeID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "titleID")
	public IhsTitle ihsTitle;

	@Column(name = "volumeNumber")
	public String volumeNumber;

	@Column(name = "vlmStartDate")
	public DateTime startDate;

	@Column(name = "vlmEndDate")
	public DateTime endDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ihsVolume")
	public List<IhsIssue> ihsissues = new ArrayList<IhsIssue>();

	public IhsVolume(IhsTitle ihsTitle, String volumeNumber,
			DateTime startDate, DateTime endDate) {
		this.ihsTitle = ihsTitle;
		this.volumeNumber = volumeNumber;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public static Finder<Integer, IhsVolume> find = new Finder<Integer, IhsVolume>(
			Integer.class, IhsVolume.class);

	public static List<VolumeView> getTitleById(int searchId, AppUser appuser,
			int memberid) {

		int held= 0;
		int missing = 0;

		List<VolumeView> volumeViews = new ArrayList<VolumeView>();

		IhsTitle ihsTitle = new IhsTitle().find.byId(searchId);

		List<IhsVolume> ihsVolumes = IhsVolume.find
				.fetch("ihsissues")
				.fetch("ihsissues.ihsHoldings", new FetchConfig().query())
				.fetch("ihsissues.ihsHoldings.ihsHoldingNotes")
				.fetch("ihsissues.ihsHoldings.sihsVarified")
				.fetch("ihsissues.ihsHoldings.sconditionTypeOverall")
				.fetch("ihsissues.ihsHoldings.svalidationLevel")
				.fetch("ihsissues.ihsHoldings.ihsMember")
				.fetch("ihsissues.ihsHoldings.ihsLocation")
				.fetch("ihsissues.ihsHoldings.sconditionType",
						new FetchConfig().query()).where()
				.eq("ihsTitle", ihsTitle)
				/*	// original Travant sorts before 2015-12-08 AJE
				.order().asc("startDate")
				.order().asc("ihsissues.publicationDate")
				// end original Travant */

				// AJE 2015-12-08 : seems to work on rational mechanics 0003-9527
				.order().asc("CAST(volumeNumber AS SIGNED)")
				/* removed publicationDate:
				.order().asc("ihsissues.publicationDate")
					- using all 3 levels means issue #2 would appear before issue #1
						- if issue #2 = blank publicationDate
						- if issue #2 = default YYYY-01-01 publicationDate
						- see Zeitschrift fu¨r anorganische und allgemeine Chemie = 0044-2313
					- but if using all 3 together, correct order is:
						- volumeNumber, publicationDate, issueNumber
				*/
				.order().asc("CAST(issueNumber AS SIGNED)")
				// resume original Travant
				.findList();

		for (IhsVolume ihsVolume : ihsVolumes) {
			VolumeView volumeView = new VolumeView();

			volumeView.volumeId = ihsVolume.volumeID;
			volumeView.volumeNumber = ihsVolume.volumeNumber;
			volumeView.volumeLevelFlag = ihsVolume.ihsTitle.volumeLevelFlag;

			if (ihsVolume.startDate != null) {
				volumeView.volumeYear = ihsVolume.startDate.getYear() + "";
			}

			int counter = 0;

			for (IhsIssue ihsissue : ihsVolume.ihsissues) {
				boolean heldFlag = false;
				int condtion = 0;

				if( "1".equals(volumeView.volumeLevelFlag) & counter > 0  )
					break;

				IssueView issueView = new IssueView();

				issueView.issueId = ihsissue.issueID;
				issueView.issueNumber = ihsissue.issueNumber;
				// AJE 2016-11-04 added next 2 properties
				issueView.name = ihsissue.name;
				issueView.description = ihsissue.description;

				for (IhsHolding ihsHolding : ihsissue.ihsHoldings) {

					if (memberid == 0 || (memberid == ihsHolding.ihsMember.memberID) ) {

						heldFlag = true;

						HoldingView holdingView = new HoldingView();

						holdingView.holdingId = ihsHolding.holdingID;

						holdingView.member = ihsHolding.ihsMember.name;
						holdingView.location = ihsHolding.ihsLocation.name;

						// SconditionTypeOverall
						List<SconditionTypeOverall> sconditionTypeOveralls = SconditionTypeOverall.find
								.findList();
						for (SconditionTypeOverall sconditionTypeOverall : sconditionTypeOveralls) {

							OverallView overallView = new OverallView(
									sconditionTypeOverall.conditionTypeOverallID,
									sconditionTypeOverall.name, 0);

							if (overallView.id == ihsHolding.sconditionTypeOverall.conditionTypeOverallID) {
								overallView.checked = 1;
							}

							holdingView.overalls.add(overallView);
						}

						// SvalidationLevel
						List<SvalidationLevel> svalidationLevels = SvalidationLevel.find
								.findList();
						for (SvalidationLevel svalidationLevel : svalidationLevels) {

							ValidationLevel validationLevel = new ValidationLevel(
									svalidationLevel.validationLevelID,
									svalidationLevel.name, 0);
							if (validationLevel.id == ihsHolding.svalidationLevel
									.hashCode()) {
								validationLevel.checked = 1;
							}
							holdingView.validationLevels.add(validationLevel);

						}

						// Verified
						List<SihsVarified> sihsVarifieds = SihsVarified.find
								.findList();
						for (SihsVarified sihsVarified : sihsVarifieds) {
							IhsVerifiedView ihsVerified = new IhsVerifiedView(
									sihsVarified.ihsVarifiedID,
									sihsVarified.name, 0);
							if (ihsVerified.id == ihsHolding.sihsVarified.ihsVarifiedID) {
								ihsVerified.checked = 1;
							}
							holdingView.ihsVerified.add(ihsVerified);
						}

						// Verified
						List<Scommitment> scommitments = Scommitment.find
								.findList();
						for (Scommitment scommitment : scommitments) {
							CommitmentView commitmentView = new CommitmentView(
									scommitment.commitmentID,
									scommitment.name, 0);

							if (commitmentView.id == ihsHolding.scommitment.commitmentID) {
								commitmentView.checked = 1;
							}
							holdingView.commitmentView.add(commitmentView);
						}

						// Missing Pages
						holdingView.missingPages = ihsHolding.missingPages != null ? ihsHolding.missingPages
								: "";

						// Notes
						if (ihsHolding.ihsHoldingNotes.size() > 0) {
							holdingView.notes = ihsHolding.ihsHoldingNotes
									.get(ihsHolding.ihsHoldingNotes.size() - 1).note;
						}

						//
						HashMap<String, String> hm = new HashMap<String, String>();
						for (SconditionType sconditionType : ihsHolding.sconditionType) {
							hm.put(sconditionType.name, "");
						}

						List<SconditionType> sconditionTypes = SconditionType.find
								.findList();

						for (SconditionType sconditionType : sconditionTypes) {
							HoldingConditionsView holdingConditionsView = new HoldingConditionsView(
									sconditionType.conditionTypeID,
									sconditionType.name, 0);

							if (hm.get(holdingConditionsView.name) != null) {
								holdingConditionsView.checked = 1;
							}

							holdingView.holdingConditionsView.add(holdingConditionsView);
						}

						if (ihsHolding.sconditionTypeOverall.conditionTypeOverallID > condtion) {
							condtion = ihsHolding.sconditionTypeOverall.conditionTypeOverallID;
							issueView.issueCondition = ihsHolding.sconditionTypeOverall.name;
						}

						if (appuser.memberId == ihsHolding.ihsMember.memberID)
							holdingView.editable = "1";

						issueView.holdingViews.add(holdingView);

					}
				}


				if(heldFlag){
					held++;
					issueView.issueStatus = "Held";
				} else {
					missing++;
					issueView.issueStatus = "Missing";
				}

				issueView.issueCount = ihsissue.ihsHoldings.size();
        System.out.println("IhsVolume.java, getTitleById, is (ihsissue.spublicationDate !=  null) ? " +(ihsissue.spublicationDate !=  null)+".");
				if(ihsissue.spublicationDate !=  null){
				  System.out.println("IhsVolume.java, getTitleById IF block, ihsissue.spublicationDate NOT  null.");
					issueView.issueMonth = ihsissue.spublicationDate.publicationDateVal; // Travant original
					//issueView.issueMonth += ihsissue.publicationDate != null ? "-" + ihsissue.publicationDate.getYear() : "";
				}
				else if(issueView.name != null){ // AJE added else if block 2016-11-04
				  System.out.println("IhsVolume.java, getTitleById ELSE IF NAME block, ihsissue.name=" +issueView.name+".");
					issueView.issueMonth = issueView.name +" ";
					issueView.issueMonth +=  ihsissue.publicationDate != null ? dateFormat.print(ihsissue.publicationDate) : "[no issue date found]";
				}
				else if(issueView.description != null){ // AJE added else if block 2016-11-04
				  System.out.println("IhsVolume.java, getTitleById ELSE IF DESC block, ihsissue.description=" +issueView.description+".");
					issueView.issueMonth = issueView.description +" ";
					issueView.issueMonth +=  ihsissue.publicationDate != null ? dateFormat.print(ihsissue.publicationDate) : "[no issue date found]";
				}
				else {
				  System.out.println("IhsVolume.java, getTitleById ELSE block, ihsissue.spublicationDate IS  null.");
          issueView.issueMonth += ihsissue.name != null ? ihsissue.name : "";
          issueView.issueMonth += ihsissue.description != null ? ihsissue.description : "";
          issueView.issueMonth += ihsissue.name != null ? ihsissue.name : dateFormat.print(ihsissue.publicationDate);
// AJE : it uses dateFormat.print(ihsissue.publicationDate) when ihsissue.name is null
				}

        // AJE no Logger available
        if(ihsissue.spublicationDate !=  null){
          System.out.println("IhsVolume.java, getTitleById: Travant uses: ihsissue.spublicationDate.publicationDateVal = " +ihsissue.spublicationDate.publicationDateVal+ ".");
        }
        System.out.println("IhsVolume.java, getTitleById: ihsissue.publicationDate = " +ihsissue.publicationDate+ ".");
        System.out.println("IhsVolume.java, getTitleById: dateFormat.print(ihsissue.publicationDate) = " +dateFormat.print(ihsissue.publicationDate)+ ".");
        System.out.println("IhsVolume.java, getTitleById, issueView.issueMonth = '" +issueView.issueMonth+ "'.");

				volumeView.issueView.add(issueView);
				counter++;
			} // end for ihsVolume.ihsissues
			volumeViews.add(volumeView);
		}

		if( volumeViews.size() > 0){
			volumeViews.get(0).chart = new ArrayList<Chart>();
			volumeViews.get(0).chart.add(new Chart("Held", held));
			volumeViews.get(0).chart.add(new Chart("Missing", missing));
		}

		return volumeViews;
	} // end getTitleById
}