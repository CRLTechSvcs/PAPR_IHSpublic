package controllers;

import static play.libs.Json.toJson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.AppUser;
import models.IhsPublicationRange;
import models.IhsPublicationRangeVer;
import models.IhsPublisher;
import models.IhsTitle;
import models.IhsTitleVersion;
import models.IhsUser;
import models.Ptitlelink;
import models.SperiodicityType;
import json.HoldingView;
import json.LinkView;
import json.LinkViewParentChild;
import json.PeriodicityTypeView;
import json.PublicationRangeView;
import json.TitleVersionView;
import json.TitleView;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import util.Helper;
import views.html.*;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

@Restrict(@Group("admin"))
public class AdvancedEditing extends Controller {

	static DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy ");

	static String NEW = "new";
	static String CHANGE = "change";

	public static Result advanced_editing_history() {
		return ok(advanced_editing_history.render());
	}

	public static Result advanced_editing_linking() {
		return ok(advanced_editing_linking.render());
	}

	public static List<TitleVersionView> BuildTitleVersionView (int titleid){

    /* AJE debugging 2016-11-28 */
Logger.info("AdvancedEditing.java: BuildTitleVersionView(" +Integer.toString(titleid)+").");


		IhsTitle ihsTitle = IhsTitle.find.fetch("ihsPublisher")
				.fetch("ihsPublicationRange").fetch("ihsUser")
				.fetch("ihsUser.ihsMember").where().eq("titleID", titleid)
				.findUnique();

Logger.info("BuildTitleVersionView: ihsTitle.toString(): " +ihsTitle.toString()+".");

		List<TitleVersionView> titleVersionViews = new ArrayList<TitleVersionView>();

		List<PublicationRangeView> publicationRangeViews = new ArrayList<PublicationRangeView>();

		List<IhsTitleVersion> ihsTitleVersions = IhsTitleVersion.find
				.fetch("ihsPublicationRangeVer").fetch("ihsUser")
				.fetch("ihsUser.ihsMember").where().eq("titleID", titleid)
				.orderBy("titleversionID desc").findList();

Logger.info("BTVV: ihsTitleVersions.toString(): " +ihsTitleVersions.toString()+".");
    //Logger.info("BTVV ... ihsTitleVersions.get(0).toString(): " +ihsTitleVersions.get(0).toString()+".");
    Logger.info("BTVV ... ihsTitleVersions has " +ihsTitleVersions.size()+" elements.");

		for (IhsPublicationRange ihsPublicationRange : ihsTitle.ihsPublicationRange) {

      Logger.info("BTVV: for (1) IhsPublicationRange : ihsTitle.ihsPublicationRange.");
      Logger.info("BTVV ... ihsTitle.ihsPublicationRange.toString(): " +ihsTitle.ihsPublicationRange.toString()+".");
      Logger.info("BTVV ... ihsTitle.ihsPublicationRange.get(0).toString(): " +ihsTitle.ihsPublicationRange.get(0).toString()+".");
      Logger.info("BTVV ... ihsTitle.ihsPublicationRange has " +ihsTitle.ihsPublicationRange.size()+" elements.");

			String endDate = ihsPublicationRange.endDate == null ? "" : dtf
					.print(ihsPublicationRange.endDate);

      Logger.info("BTVV for (1) endDate=" +endDate+" ; ihsPublicationRange.publicationRangeID=" +Integer.toString(ihsPublicationRange.publicationRangeID)+".");


			publicationRangeViews.add(new PublicationRangeView(
					ihsPublicationRange.publicationRangeID, dtf
							.print(ihsPublicationRange.startDate), endDate,
					ihsPublicationRange.speriodicityType.periodicityTypeID));

		} // end for IhsPublicationRange

      Logger.info("BTVV: for (1) publicationRangeViews.add completed.");


		String publicationRange = Helper
				.getPublicationRange(ihsTitle.ihsPublicationRange);

		String tmpPublisherName = ihsTitle.ihsPublisher.name.length() > 26 ? ihsTitle.ihsPublisher.name
				.substring(0, 25) : ihsTitle.ihsPublisher.name;

		String imagePageRatio = ihsTitle.imagePageRatio != null ? ihsTitle.imagePageRatio.toString(): "";

		titleVersionViews.add(new TitleVersionView(ihsTitle.titleVersionID,
				ihsTitle.titleID, ihsTitle.title, ihsTitle.alphaTitle,
				ihsTitle.ihsPublisher.publisherID, tmpPublisherName,
				ihsTitle.printISSN, ihsTitle.eISSN, ihsTitle.oclcNumber,
				ihsTitle.lccn == null ?"" : ihsTitle.lccn,
				ihsTitle.description, publicationRange,
				publicationRangeViews, "Y", dtf.print(ihsTitle.changeDate),
				ihsTitle.ihsUser.userName, ihsTitle.ihsUser.ihsMember.name, imagePageRatio,
				ihsTitle.language,ihsTitle.country));

    Logger.info("BTVV: for (1) titleVersionViews.add completed; titleVersionViews.toString(): " +titleVersionViews.toString()+".");
    Logger.info("BTVV ... titleVersionViews[0].get(0).toString(): " +titleVersionViews.get(0).toString()+".");
    Logger.info("BTVV ... titleVersionViews has " +titleVersionViews.size()+" elements.");

    Logger.info("BTVV: for 2) is next; titleVersionViews.add completed; titleVersionViews.toString(): " +titleVersionViews.toString()+".");

		for (IhsTitleVersion ihsTitleVersion : ihsTitleVersions) {

      Logger.info("BTVV: for 2) IhsTitleVersion : ihsTitleVersions begins.");

			List<PublicationRangeView> publicationRangeViewsVer = new ArrayList<PublicationRangeView>();

			for (IhsPublicationRangeVer ihsPublicationRangeVer : ihsTitleVersion.ihsPublicationRangeVer) {

      Logger.info("BTVV: for 3) IhsPublicationRangeVer : ihsTitleVersion.ihsPublicationRangeVer.");

				String endDate = ihsPublicationRangeVer.endDate == null ? ""
						: dtf.print(ihsPublicationRangeVer.endDate);

				publicationRangeViewsVer
						.add(new PublicationRangeView(
								ihsPublicationRangeVer.publicationRangeID,
								dtf.print(ihsPublicationRangeVer.startDate),
								endDate,
								ihsPublicationRangeVer.speriodicityType.periodicityTypeID));

			}

			String imagePageRatioVer = ihsTitleVersion.imagePageRatio != null ? ihsTitleVersion.imagePageRatio.toString(): "";

			if (ihsTitle.titleVersionID != ihsTitleVersion.titleversionID) {

        Logger.info("BTVV, IF (ihsTitle.titleVersionID ["+Integer.toString(ihsTitle.titleVersionID)+"] != ihsTitleVersion.titleversionID ["+Integer.toString(ihsTitleVersion.titleversionID)+"])");
        Logger.info("... will .add(new TitleVersionView...");

				titleVersionViews
						.add(new TitleVersionView(
								ihsTitleVersion.titleversionID,
								ihsTitleVersion.titleID,
								ihsTitleVersion.title,
								ihsTitleVersion.alphaTitle,
								ihsTitleVersion.ihsPublisher.publisherID,
								ihsTitle.ihsPublisher.name,
								ihsTitleVersion.printISSN,
								ihsTitleVersion.eISSN,
								ihsTitleVersion.oclcNumber,
								ihsTitleVersion.lccn,
								ihsTitleVersion.description,
								Helper.getPublicationRangeVer(ihsTitleVersion.ihsPublicationRangeVer),
								publicationRangeViewsVer, "N", dtf
										.print(ihsTitleVersion.changeDate),
								ihsTitleVersion.ihsUser.userName,
								ihsTitleVersion.ihsUser.ihsMember.name,
								imagePageRatioVer,
								ihsTitleVersion.language,ihsTitleVersion.country));
			} else { // AJE 2016-11-28 tis else block is just debugging
        Logger.info("BTVV: ELSE block of if (ihsTitle.titleVersionID ["+Integer.toString(ihsTitle.titleVersionID)+"] != ihsTitleVersion.titleversionID ["+Integer.toString(ihsTitleVersion.titleversionID)+"])");
        Logger.info("... no action will take place; this block exists only to remind you of that");
			}

		} // end for IhsTitleVersion

		Logger.info("BTVV at end, titleVersionViews="+titleVersionViews);
    Logger.info("BTVV ... titleVersionViews has " +titleVersionViews.size()+" elements.");

		return titleVersionViews;
	}

	public static Result GetTitles(int titleid) {
    Logger.info("Advanced Editing.java, GetTitles(int titleid) will just call BuildTitleVersionView(titleid)");
		return ok(toJson(BuildTitleVersionView(titleid)));
	}

	public static Result GetPublishers() {

		return ok(toJson(IhsPublisher.getAllPublisherOrder()));

	}

	public static Result GetPeriodicityType() {

		List<PeriodicityTypeView> periodicityTypeViews = new ArrayList<PeriodicityTypeView>();

		List<SperiodicityType> speriodicityTypes = SperiodicityType.find
				.findList();

		for (SperiodicityType speriodicityType : speriodicityTypes) {
			periodicityTypeViews.add(new PeriodicityTypeView(
					speriodicityType.periodicityTypeID, speriodicityType.name));
		}
		return ok(toJson(periodicityTypeViews));
	}


	public static void postTitleTran(TitleVersionView titleVersionView) {

		String user = session().get(Login.User);

		AppUser appUser = Helper.getAppUserFromCache(user);

		IhsUser ihsUser = IhsUser.find.byId(appUser.userId);

		/*-----------*/
		IhsTitle ihsTitle = IhsTitle.find.byId(titleVersionView.titleId);

		/*------------*/


		if (ihsTitle.titleVersionID == 0) {
			IhsTitleVersion ihsTitleVersion = new IhsTitleVersion(
					ihsTitle.titleID, ihsTitle.title, ihsTitle.alphaTitle,
					ihsTitle.printISSN, ihsTitle.eISSN, ihsTitle.oclcNumber,
					ihsTitle.lccn, ihsTitle.ihsPublisher, ihsTitle.description,
					ihsTitle.changeDate, ihsUser, ihsTitle.imagePageRatio, ihsTitle.language, ihsTitle.country);

			ihsTitleVersion.save();

			for (IhsPublicationRange ihsPublicationRange : ihsTitle.ihsPublicationRange) {
				(new IhsPublicationRangeVer(ihsTitleVersion,
						ihsPublicationRange.speriodicityType,
						ihsPublicationRange.startDate,
						ihsPublicationRange.endDate)).save();
			}

		}
		/*------------------*/

		ihsTitle.setTitle(titleVersionView.title);
		ihsTitle.alphaTitle = titleVersionView.alphaTitle;
		ihsTitle.printISSN = titleVersionView.printISSN;
		ihsTitle.eISSN = titleVersionView.eISSN;
		ihsTitle.oclcNumber = titleVersionView.oclcNumber;
		ihsTitle.lccn = titleVersionView.lccn;
		ihsTitle.description = titleVersionView.note;
		IhsPublisher ihsPublisher = IhsPublisher.find
				.byId(titleVersionView.publisher);

		ihsTitle.ihsPublisher = ihsPublisher;
		ihsTitle.imagePageRatio = titleVersionView.imagePageRatio.equals("") ? null : new Integer( titleVersionView.imagePageRatio );

		ihsTitle.titleVersionID = 0;

		ihsTitle.setIhsUser(ihsUser);
		ihsTitle.changeDate = new DateTime();
		ihsTitle.language = titleVersionView.language;
		ihsTitle.country = titleVersionView.country;

		ihsTitle.update();

		/*------------*/
		for (PublicationRangeView publicationRangeView : titleVersionView.publicationRangeViews) {

			if (NEW.equals(publicationRangeView.status)) {

				SperiodicityType speriodicityType = SperiodicityType.find
						.byId(publicationRangeView.pubRangeId);

				DateTime stdt = DateTime.parse(publicationRangeView.startDate,
						DateTimeFormat.forPattern("MM/dd/yyyy"));

				DateTime enddt = ("").equals(publicationRangeView.endDate) ? null
						: DateTime.parse(publicationRangeView.endDate,
								DateTimeFormat.forPattern("MM/dd/yyyy"));

				new IhsPublicationRange(ihsTitle, speriodicityType, stdt, enddt)
						.save();
				;

			}

			if (CHANGE.equals(publicationRangeView.status)) {

				IhsPublicationRange ihsPublicationRange = IhsPublicationRange.find
						.byId(publicationRangeView.publicationRangeID);
				ihsPublicationRange.setStartDate(DateTime.parse(
						publicationRangeView.startDate,
						DateTimeFormat.forPattern("MM/dd/yyyy")));
				ihsPublicationRange.endDate = ("")
						.equals(publicationRangeView.endDate) ? null : DateTime
						.parse(publicationRangeView.endDate,
								DateTimeFormat.forPattern("MM/dd/yyyy"));
				ihsPublicationRange.speriodicityType = SperiodicityType.find
						.byId(publicationRangeView.pubRangeId);

				ihsPublicationRange.update();

			}
		}

		/* ---------- */

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result postTitle() {

		try {

			TitleVersionView titleVersionView = null;
			String jsonString = Controller.request().body().asJson().toString();

			ObjectMapper mapper = new ObjectMapper();

			titleVersionView = mapper.readValue(jsonString,
					TitleVersionView.class);

			postTitleTran(titleVersionView);

		} catch (Exception e) {
			Logger.error("ingestion_exception_resolve", e);
			return internalServerError("ingestion_exception_resolve");
		}
		return ok();
	}


	public static Result restorePreviousTitle(int titleId, int titleVersionId) {

		String user = session().get(Login.User);

		AppUser appUser = Helper.getAppUserFromCache(user);

		IhsUser ihsUser = IhsUser.find.byId(appUser.userId);

		/*-----------*/
		IhsTitle ihsTitle = IhsTitle.find.byId(titleId);

		IhsTitleVersion ihsTitleVersion = IhsTitleVersion.find
				.byId(titleVersionId);

		/*------------*/


		if (ihsTitle.titleVersionID == 0) {
			IhsTitleVersion ihsTitleVersionNew = new IhsTitleVersion(
					ihsTitle.titleID, ihsTitle.title, ihsTitle.alphaTitle,
					ihsTitle.printISSN, ihsTitle.eISSN, ihsTitle.oclcNumber,
					ihsTitle.lccn, ihsTitle.ihsPublisher, ihsTitle.description,
					new DateTime(), ihsUser, ihsTitle.imagePageRatio, ihsTitle.language, ihsTitle.country);

			ihsTitleVersionNew.save();

			for (IhsPublicationRange ihsPublicationRange : ihsTitle.ihsPublicationRange) {
				(new IhsPublicationRangeVer(ihsTitleVersionNew,
						ihsPublicationRange.speriodicityType,
						ihsPublicationRange.startDate,
						ihsPublicationRange.endDate)).save();
			}
		}


		ihsTitle.setTitle(ihsTitleVersion.title);

		ihsTitle.alphaTitle = ihsTitleVersion.alphaTitle;
		ihsTitle.printISSN = ihsTitleVersion.printISSN;
		ihsTitle.eISSN = ihsTitleVersion.eISSN;
		ihsTitle.oclcNumber = ihsTitleVersion.oclcNumber;
		ihsTitle.lccn = ihsTitleVersion.lccn;

		ihsTitle.ihsPublisher = ihsTitleVersion.ihsPublisher;
		ihsTitle.description = ihsTitleVersion.description;

		ihsTitle.setIhsUser(ihsTitleVersion.ihsUser);

		ihsTitle.titleVersionID = ihsTitleVersion.titleversionID;

		ihsTitle.changeDate = ihsTitleVersion.changeDate;

		ihsTitle.imagePageRatio = ihsTitleVersion.imagePageRatio;

		ihsTitle.language = ihsTitleVersion.language;

		ihsTitle.country = ihsTitleVersion.country;


		ihsTitle.update();


		/*
		for (IhsPublicationRange ihsPublicationRange : ihsTitle.ihsPublicationRange) {
			ihsPublicationRange.delete();
		}

		for (IhsPublicationRangeVer ihsPublicationRangeVer : ihsTitleVersion.ihsPublicationRangeVer) {

			new IhsPublicationRange(ihsTitle,
					ihsPublicationRangeVer.speriodicityType,
					ihsPublicationRangeVer.startDate,
					ihsPublicationRangeVer.endDate).save();

		}
		*/

		return ok();
	}

	public static Result addParent(int titleid, int parentTitleId) {

		Ptitlelink ptitlelink = Ptitlelink.find.where()
				.eq("titleParentID", parentTitleId).eq("titleChildID", titleid)
				.findUnique();
		if (ptitlelink == null) {
			new Ptitlelink(titleid, parentTitleId).save();
		}
		return ok();
	}

	public static Result addChild(int titleid, int childTitleId) {

		Ptitlelink ptitlelink = Ptitlelink.find.where()
				.eq("titleChildID", childTitleId).eq("titleParentID", titleid)
				.findUnique();

		if (ptitlelink == null) {
			new Ptitlelink(childTitleId, titleid).save();
		}
		return ok();
	}

	public static Result removeParent(int titleid, int parentTitleId) {

		Ptitlelink ptitlelink = Ptitlelink.find.where()
				.eq("titleParentID", parentTitleId).eq("titleChildID", titleid)
				.findUnique();
		ptitlelink.delete();

		return ok();
	}

	public static Result removeChild(int titleid, int childTitleId) {

		Ptitlelink ptitlelink = Ptitlelink.find.where()
				.eq("titleChildID", childTitleId).eq("titleParentID", titleid)
				.findUnique();

		ptitlelink.delete();

		return ok();
	}

	public static Result getLinkView(int titleid) {

		IhsTitle ihsTitle = IhsTitle.find.fetch("ihsPublicationRange").where()
				.eq("titleID", titleid).findUnique();

		LinkView linkView = new LinkView(ihsTitle.titleID, ihsTitle.title,
				Helper.getPublicationRange(ihsTitle.ihsPublicationRange));

		/*-------------*/
		List<Integer> parentIds = new ArrayList<Integer>();
		List<Ptitlelink> ptitleinkParents = Ptitlelink.find.where()
				.eq("titleChildID", titleid).findList();
		for (Ptitlelink ptitlelink : ptitleinkParents) {
			parentIds.add(ptitlelink.titleParentID);
		}

		/*-------------*/
		List<Integer> childIds = new ArrayList<Integer>();
		List<Ptitlelink> ptitlelinChilds = Ptitlelink.find.where()
				.eq("titleParentID", titleid).findList();

		for (Ptitlelink ptitlelink : ptitlelinChilds) {
			childIds.add(ptitlelink.titleChildID);
		}

		/*-------------*/
		List<IhsTitle> ihsTitleParents = IhsTitle.find
				.fetch("ihsPublicationRange").where().in("titleID", parentIds)
				.findList();

		Logger.info("AdvancedEditing.java, getLinkView("+Integer.toString(titleid)+") ; next is for ihsTitleParents.");

		for (IhsTitle ihsTitlePar : ihsTitleParents) {

		Logger.info("...ihsTitlePar.title = "+ihsTitlePar.title);

			linkView.parents
					.add(new LinkViewParentChild(
							ihsTitlePar.titleID,
							ihsTitlePar.title,
							Helper.getPublicationRange(ihsTitlePar.ihsPublicationRange)));
		}

		/*-------------*/
		List<IhsTitle> ihsTitleChilds = IhsTitle.find
				.fetch("ihsPublicationRange").where().in("titleID", childIds)
				.findList();
		for (IhsTitle ihsTitleChl : ihsTitleChilds) {
			linkView.childs
					.add(new LinkViewParentChild(
							ihsTitleChl.titleID,
							ihsTitleChl.title,
							Helper.getPublicationRange(ihsTitleChl.ihsPublicationRange)));
		}

		return ok(toJson(linkView));
	}
}