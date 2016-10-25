package controllers;

import static play.libs.Json.toJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.AppUser;
import models.IhsHolding;
import models.IhsHoldingNote;
import models.IhsMember;
import models.IhsTitle;
import models.IhsUser;
import models.IhsVolume;
import models.PwantTitleMember;
import models.Scommitment;
import models.SconditionType;
import models.SconditionTypeOverall;
import models.SihsVarified;
import models.SvalidationLevel;
import json.CommitmentView;
import json.Data;
import json.HoldingConditionsView;
import json.HoldingView;
import json.IhsVerifiedView;
import json.IssueView;
import json.MemberView;
import json.OverallView;
import json.PageingJson;
import json.TitleView;
import json.ValidationLevel;
import json.VolumeView;
import json.WantStatus;
import util.Helper;
import views.html.*;
import parser.BaseData;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

@Restrict(@Group("user"))
public class SearchJournals extends Controller {

	public static Result search_home() {
		return ok(search_home.render());
	}

	public static Result searchJournalByTitle(String searchValue) {
		//String modifiedSearchValue = searchValue.trim().replaceAll(" ", "* "); // Travant original 2016-10-25
		// we don't want to expand on these searches any more than we do with browseJournalByTitle below
		String modifiedSearchValue = searchValue.trim();
    //Logger.info("app.controllers.SearchJournals.java searchJournalByTitle("+searchValue+"), modifiedSearchValue = "+modifiedSearchValue+".");

		//List<TitleView> titleViews = IhsTitle.getTitle(modifiedSearchValue + "*"); // Travant original
		List<TitleView> titleViews = IhsTitle.getTitle(modifiedSearchValue); // AJE 2016-10-25
    //Logger.info("...searchJournalByTitle("+searchValue+"), titleViews.size() = "+titleViews.size()+".");

		PageingJson pageingJson = new PageingJson();
		pageingJson.items = titleViews;

		return ok(toJson(pageingJson));
	}

/*****************************************************************
  AJE 2016-10-24 new function to use SQL LIKE instead of MATCH AGAINST; modeled after searchJournalByTitle
*/
	public static Result browseJournalByTitle(String searchValue) {
		String modifiedSearchValue = searchValue.trim();  // AJE 2016-10-24
    //Logger.info("app.controllers.SearchJournals.java browseJournalByTitle("+searchValue+"), modifiedSearchValue = "+modifiedSearchValue+".");

		List<TitleView> titleViews = IhsTitle.getTitleBrowse(modifiedSearchValue); // AJE 2016-10-24
    //Logger.info("...browseJournalByTitle("+searchValue+"), titleViews.size() = "+titleViews.size()+", titleViews[0].publisher = "+titleViews.get(0).publisher+".");

		PageingJson pageingJson = new PageingJson();
		pageingJson.items = titleViews;

		return ok(toJson(pageingJson));
	} /* end AJE 2016-10-24
*****************************************************************/


	public static Result searchJournalByISSN(String searchValue) {

		List<TitleView> titleViews = IhsTitle.getByISSN(searchValue);

		PageingJson pageingJson = new PageingJson();
		pageingJson.items = titleViews;

		return ok(toJson(pageingJson));

	}

	public static Result searchJournalByOCLC(String searchValue) {

		List<TitleView> titleViews = IhsTitle.getByOCLC(searchValue);

		PageingJson pageingJson = new PageingJson();
		pageingJson.items = titleViews;

		return ok(toJson(pageingJson));

	}
	public static Result getJournalDetail(int id) {

		TitleView titleView = IhsTitle.getDetailById(id);

		return ok(toJson(titleView));
	}

	public static Result getJournalVolumeDetail(int id, int memberid) {

		AppUser appuser = Helper.getAppUserFromCache(session().get(Login.User));

		List<VolumeView> volumeViews = IhsVolume.getTitleById(id, appuser,
				memberid);

		return ok(toJson(volumeViews));
	}

	public static Result getMembers() {

		/* AJE 2016-09-14 make the select drop-downs show the members alphabetically by name
		List<IhsMember> ihsMembers = IhsMember.find.findList(); // Travant original code: */
		List<IhsMember> ihsMembers = IhsMember.find.orderBy("name ASC").findList();
    /* AJE 2016-09-14 appears to have worked; 'members' <select> in search_home + in Reporting > New Report both alphabetical now
    resume Travant original */

		List<MemberView> memberViews = new ArrayList<MemberView>();

		for (IhsMember ihsMember : ihsMembers) {
			memberViews.add(new MemberView(ihsMember.memberID, ihsMember.name));
			//Logger.info("AJE 2016-09-15: for ihsMember : ihsMembers...: ihsMember.memberID='"+ihsMember.memberID+"', ihsMember.name='"+ihsMember.name+".");
		}

    // AJE 2016-09-15 app/json/Data.java ; Data() is an ArrayList
		Data data = new Data();
		data.data = memberViews;

		return ok(toJson(data));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result postHoldingConditions() {

		try {

			HoldingView holdingView = null;

			String user = session().get(Login.User);

			String jsonString = Controller.request().body().asJson().toString();

			ObjectMapper mapper = new ObjectMapper();

			try {

				holdingView = mapper.readValue(jsonString, HoldingView.class);

				if (holdingView.holdingId != 0) {
					IhsHolding ihsHolding = IhsHolding.find
							.byId(holdingView.holdingId);

					for (CommitmentView sommitmentView : holdingView.commitmentView) {
						if (sommitmentView.checked == 1) {
							Scommitment scommitment = Scommitment.find
									.byId(sommitmentView.id);
							ihsHolding.scommitment = scommitment;
						}
					}

					for (OverallView overallView : holdingView.overalls) {
						if (overallView.checked == 1) {
							SconditionTypeOverall sconditionTypeOverall = SconditionTypeOverall.find
									.byId(overallView.id);
							ihsHolding.sconditionTypeOverall = sconditionTypeOverall;
						}
					}

					for (ValidationLevel validationLevel : holdingView.validationLevels) {
						if (validationLevel.checked == 1) {
							SvalidationLevel svalidationLevel = SvalidationLevel.find
									.byId(validationLevel.id);
							ihsHolding.svalidationLevel = svalidationLevel;
						}

					}

					for (IhsVerifiedView ihsVerified : holdingView.ihsVerified) {

						if (ihsVerified.checked == 1) {
							SihsVarified sihsVarified = SihsVarified.find
									.byId(ihsVerified.id);
							ihsHolding.sihsVarified = sihsVarified;
						}

					}

					ihsHolding.missingPages = holdingView.missingPages;

					Set<SconditionType> sconditionTypes = new HashSet<SconditionType>();

					for (HoldingConditionsView holdingConditionsView : holdingView.holdingConditionsView) {

						if (holdingConditionsView.checked == 1)
							sconditionTypes.add(SconditionType.find
									.byId(holdingConditionsView.conditionId));
					}

					ihsHolding.sconditionType = sconditionTypes;

					ihsHolding.update();

				} else {

					List<IhsHolding> ihsHoldings = IhsHolding.find.where()
							.in("holdingID", holdingView.holdingIds).findList();

					for (IhsHolding ihsHolding : ihsHoldings) {

						for (CommitmentView sommitmentView : holdingView.commitmentView) {
							if (sommitmentView.checked == 1) {
								Scommitment scommitment = Scommitment.find
										.byId(sommitmentView.id);
								ihsHolding.scommitment = scommitment;
							}
						}

						for (OverallView overallView : holdingView.overalls) {
							if (overallView.checked == 1 && overallView.id !=0) {
								SconditionTypeOverall sconditionTypeOverall = SconditionTypeOverall.find
										.byId(overallView.id);
								ihsHolding.sconditionTypeOverall = sconditionTypeOverall;
							}
						}

						for (ValidationLevel validationLevel : holdingView.validationLevels) {
							if (validationLevel.checked == 1 && validationLevel.id !=0) {
								SvalidationLevel svalidationLevel = SvalidationLevel.find
										.byId(validationLevel.id);
								ihsHolding.svalidationLevel = svalidationLevel;
							}

						}

						for (IhsVerifiedView ihsVerified : holdingView.ihsVerified) {

							if (ihsVerified.checked == 1 && ihsVerified.id !=0) {
								SihsVarified sihsVarified = SihsVarified.find
										.byId(ihsVerified.id);
								ihsHolding.sihsVarified = sihsVarified;
							}

						}


						for (HoldingConditionsView holdingConditionsView : holdingView.holdingConditionsView) {

							if (holdingConditionsView.checked == 1 ){

								ihsHolding.sconditionType.add(SconditionType.find
										.byId(holdingConditionsView.conditionId));
							}
						}

						ihsHolding.update();

					}

				}

			} catch (IOException e) {
				Logger.error("processPorticoresolve", e);
			}

		} catch (Exception e) {

			Logger.error("ingestion_exception_resolve", e);
			return internalServerError("ingestion_exception_resolve");
		}

		return ok();
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result postHoldingNotes() {

		try {

			HoldingView holdingView = null;

			String user = session().get(Login.User);

			String jsonString = Controller.request().body().asJson().toString();

			ObjectMapper mapper = new ObjectMapper();

			try {
				holdingView = mapper.readValue(jsonString, HoldingView.class);

				IhsHolding ihsHolding = IhsHolding.find
						.byId(holdingView.holdingId);

				new IhsHoldingNote(ihsHolding, holdingView.notes.trim()).save();

			} catch (IOException e) {
				Logger.error("processPorticoresolve", e);
			}

		} catch (Exception e) {

			Logger.error("ingestion_exception_resolve", e);
			return internalServerError("ingestion_exception_resolve");
		}

		return ok();
	}

	public static Result  getJournalWantStatus(int titleId)

	{
	    AppUser appuser = Helper.getAppUserFromCache(session().get(Login.User));
		int memberId = appuser.memberId;

		WantStatus wantStatus = new WantStatus();

		PwantTitleMember pwanttitlemember = PwantTitleMember.find.where().eq("titleID", titleId).eq("memberID", memberId).findUnique();

		wantStatus.status = pwanttitlemember != null ?  0 :  1;

		return ok(toJson(wantStatus));
	}

	public static Result  setJournalWantStatus(int titleId,  int status){

		AppUser appuser = Helper.getAppUserFromCache(session().get(Login.User));
		int memberId = appuser.memberId;

		WantStatus wantStatus = new WantStatus();

		if(status == 0){

			PwantTitleMember pwanttitlemember = PwantTitleMember.find.where().eq("titleID", titleId).eq("memberID", memberId).findUnique();

			pwanttitlemember.delete();

			wantStatus.status = 1;

		}else {

			PwantTitleMember pwanttitlemember = new PwantTitleMember(titleId,memberId);
			pwanttitlemember.save();
			wantStatus.status = 0;

		}

		return ok(toJson(wantStatus));

	}
}