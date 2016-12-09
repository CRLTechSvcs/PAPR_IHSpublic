/*
  AJE 2016-09-30 'Publishing' has been relabeled 'Export Data' in the public interface, but all code still uses the old term
*/

package controllers;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import models.AppUser;
import models.IhsDeaccessionJob;
import models.IhsPublishingJob;
import models.IhsUser;
import models.SingestionJobStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import json.PageingJson;
import json.PublishingJobView;
import json.PublishingView;
import actors.AppActorSystem;
import akka.actor.ActorSelection;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import util.Helper;
import views.html.*;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import static play.libs.Json.toJson;

@Restrict(@Group("user"))
public class Publishing extends Controller {

	static int MAX_ROW = 100;
	//static String MARK ="MARC Text File (.txt)"; // Travant original
	static String MARC ="MARC Text File (.mrk)";
	static String PORTICO = "Portico Excel File (.xls, .xlsx)";
	static String IHS_XLS ="IHS Holdings Data Excel File (.xls, .xlsx)";
	static String IHS_CSV ="IHS Holdings Data CSV File (.csv)";

	static DateTimeFormatter dtf = DateTimeFormat
			.forPattern("MMM dd, yyyy HH:mm aa");

	static DateTimeFormatter shortdtf = DateTimeFormat.forPattern("mm/dd/yyyy"); // Travant original
	//static DateTimeFormatter shortdtf = DateTimeFormat.forPattern("yyyy-mm-dd"); // AJE 2016-11-22

	public static Result publishing_published_data() {
		return ok(publishing_published_data.render());
	}

	public static Result publishing_publish_new_data() {
		return ok(publishing_publish_new_data.render());
	}

	public static Result getPublishingView() {

		PublishingView publishingView = new PublishingView();

		return ok(toJson(publishingView));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result postPublishingView() {
		PublishingView publishingView;

		try {
			String jsonString = Controller.request().body().asJson().toString();

			ObjectMapper mapper = new ObjectMapper();

			publishingView = mapper.readValue(jsonString, PublishingView.class);

			String user = session().get(Login.User);

			AppUser appUser = Helper.getAppUserFromCache(user);

			IhsUser ihsUser = IhsUser.find.byId(appUser.userId);

      Logger.info("Publishing.java: [1] postPublishingView(); publishingView.startDate=" +publishingView.startDate+ "; publishingView.endDate=" +publishingView.endDate);
      /*
        AJE 2016-11-02 up to this point, startDate + endDate are valid, same values from <input> fields
        But Logger message "[2]" after these next DateTime declarations shows that these 2 lines
          reset the MONTH to always '01' (but YEAR and DAY are ok)

        AJE 2016-11-22 after fixing the Javascript input to YYYY-MM-DD in ihs_publishing.js,
          Travant's setting of startDate/endDate causes 'Invalid format: "2016-11-01" is malformed at "16-11-01"'
          So about line 49 AJE changed shortdtf to take yyyy-mm-dd ; Travant's block below then did not fail, but proceeded to assign the wrong month.
      /*
        // Disable Travant:
			DateTime startDate = "".equals(publishingView.startDate) ? null
					: shortdtf.parseDateTime(publishingView.startDate); // Travant original
			DateTime endDate = "".equals(publishingView.endDate) ? null
					: shortdtf.parseDateTime(publishingView.endDate); // Travant original
Logger.info("Publishing.java: [2] date set by TRAVANT");
      */
      DateTime startDate = new DateTime(publishingView.startDate); // AJE 2016-11-02
      DateTime endDate   = new DateTime(publishingView.endDate); // AJE 2016-11-02
      Logger.info("Publishing.java: [2] date set by AJE ; postPublishingView(); has startDate=" +startDate+ "; endDate=" +endDate);

			SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
					.where().eq("name", SingestionJobStatus.Queued)
					.findUnique();

/* Travant original */
			IhsPublishingJob ihsPublishingJob = new IhsPublishingJob(
					new DateTime(), publishingView.jobName, ihsUser, startDate,
					endDate, jsonString, singestionJobStatus,
					publishingView.fileFormat);
/*
			IhsPublishingJob ihsPublishingJob = new IhsPublishingJob(
					new DateTime(), publishingView.jobName, ihsUser, startDate,
					endDate, jsonString, singestionJobStatus,
					link, // AJE 2016-11-21 new constructor accepts 'link'
					publishingView.fileFormat);
*/

			Logger.info("Publishing.java: next is ihsPublishingJob.save()");

			ihsPublishingJob.save();

      Logger.info("Publishing.java: back from ihsPublishingJob.save(); now get myActor");

			ActorSelection myActor = AppActorSystem.getInstance().getActorSystem()
					.actorSelection("user/PublishingJobActor");

      String junkable = Integer.toString(ihsPublishingJob.publishingJobId);
      Logger.info("Publishing.java: got myActor; now myActor.tell(" +junkable+ ", null)");

			myActor.tell(new Integer(ihsPublishingJob.publishingJobId), null);

      Logger.info("Publishing.java: myActor has told");

		} catch (Exception e) {
			Logger.error("ingestion_exception_resolve", e);
			return internalServerError("ingestion_exception_resolve");
		}
		return ok();
	} // end postPublishingView



	public static Result getAllPublishingJobs() {

		PageingJson pageingJson = new PageingJson();

		List<IhsDeaccessionJob> ihsDeaccessionJobs;

		String user = session().get(Login.User);

		AppUser appUser = Helper.getAppUserFromCache(user);

		IhsUser ihsUser = IhsUser.find.byId(appUser.userId);

		List<IhsPublishingJob> ihsPublishingJobs = IhsPublishingJob.find
				.where().orderBy("publishingJobId desc").setMaxRows(MAX_ROW)
				.findList();

		for (IhsPublishingJob ihsPublishingJob : ihsPublishingJobs) {

			String formatType ="";

			if(ihsPublishingJob.fileformat == 1){
				formatType = MARC;
			}else if (ihsPublishingJob.fileformat == 2){
				formatType = PORTICO;
			}else if(ihsPublishingJob.fileformat == 3){
				formatType = IHS_XLS;
			}else if(ihsPublishingJob.fileformat == 4){
				formatType = IHS_CSV;
			}

			String startDate = ihsPublishingJob.startDate != null ? shortdtf.print( ihsPublishingJob.startDate) + " to ": "Beginning to";
			String endDate = ihsPublishingJob.endDate != null ? shortdtf.print( ihsPublishingJob.endDate): " End";

			PublishingJobView publishingJobView = new PublishingJobView(
					dtf.print(ihsPublishingJob.dateInitiated),
					ihsPublishingJob.jobName,
					formatType,
					ihsPublishingJob.ihsUser.ihsMember.name,
					ihsPublishingJob.ihsUser.getName(),
					startDate + endDate,
					ihsPublishingJob.singestionJobStatus.name,
					ihsPublishingJob.link

			);
			pageingJson.items.add(publishingJobView);

		}

		return ok(toJson(pageingJson));
	}

	public static Result getAUserPublishingJobs() {

		PageingJson pageingJson = new PageingJson();

		List<IhsDeaccessionJob> ihsDeaccessionJobs;

		String user = session().get(Login.User);

		AppUser appUser = Helper.getAppUserFromCache(user);

		IhsUser ihsUser = IhsUser.find.byId(appUser.userId);

		List<IhsPublishingJob> ihsPublishingJobs = IhsPublishingJob.find
				.where().eq("ihsUser", ihsUser).orderBy("publishingJobId desc").setMaxRows(MAX_ROW)
				.findList();

		for (IhsPublishingJob ihsPublishingJob : ihsPublishingJobs) {

			String formatType ="";

			if(ihsPublishingJob.fileformat == 1){
				formatType = MARC;
			}else if (ihsPublishingJob.fileformat == 2){
				formatType = PORTICO;
			}else if(ihsPublishingJob.fileformat == 3){
				formatType = IHS_XLS;
			}else if(ihsPublishingJob.fileformat == 4){
				formatType = IHS_CSV;
			}


			String startDate = ihsPublishingJob.startDate != null ? shortdtf.print( ihsPublishingJob.startDate) + " to ": "Beginning to";
			String endDate = ihsPublishingJob.endDate != null ? shortdtf.print( ihsPublishingJob.endDate): " End";
/*
  		Logger.info("app.controllers.Publishing.java: getAUserPublishingJobs() has ihsPublishingJob.jobName=" +ihsPublishingJob.jobName+".");
  		Logger.info("...has user=" +user+" ; ihsPublishingJob.link=" +ihsPublishingJob.link+".");
  		Logger.info("...has ihsPublishingJob.fileformat="+Integer.toString(ihsPublishingJob.fileformat)+" ; formatType=" +formatType+".");
  		Logger.info("...has ihsPublishingJob.startDate="+ihsPublishingJob.startDate+", startDate=" +startDate+".");
  		Logger.info("...has ihsPublishingJob.endDate="+ihsPublishingJob.endDate+", endDate=" +endDate+".\n\n");
*/
			PublishingJobView publishingJobView = new PublishingJobView(
					dtf.print(ihsPublishingJob.dateInitiated),
					ihsPublishingJob.jobName,
					formatType,
					ihsPublishingJob.ihsUser.ihsMember.name,
					ihsPublishingJob.ihsUser.getName(),
					startDate + endDate,
					ihsPublishingJob.singestionJobStatus.name,
					ihsPublishingJob.link
			);
			pageingJson.items.add(publishingJobView);

		} // end for

		return ok(toJson(pageingJson));
	} // end getAUserPublishingJobs



	public static Result getAMemberPublishingJobs() {

		PageingJson pageingJson = new PageingJson();

		List<IhsDeaccessionJob> ihsDeaccessionJobs;

		String user = session().get(Login.User);

		AppUser appUser = Helper.getAppUserFromCache(user);

		IhsUser ihsUser = IhsUser.find.byId(appUser.userId);

		List<IhsPublishingJob> ihsPublishingJobs = IhsPublishingJob.find
				.where().eq("ihsUser.ihsMember", ihsUser.ihsMember).orderBy("publishingJobId desc").setMaxRows(MAX_ROW)
				.findList();

		for (IhsPublishingJob ihsPublishingJob : ihsPublishingJobs) {

			String formatType ="";

			if(ihsPublishingJob.fileformat == 1){
				formatType = MARC;
			}else if (ihsPublishingJob.fileformat == 2){
				formatType = PORTICO;
			}else if(ihsPublishingJob.fileformat == 3){
				formatType = IHS_XLS;
			}else if(ihsPublishingJob.fileformat == 4){
				formatType = IHS_CSV;
			}


			String startDate = ihsPublishingJob.startDate != null ? shortdtf.print( ihsPublishingJob.startDate) + " to ": "Beginning to";
			String endDate = ihsPublishingJob.endDate != null ? shortdtf.print( ihsPublishingJob.endDate): " End";


			PublishingJobView publishingJobView = new PublishingJobView(
					dtf.print(ihsPublishingJob.dateInitiated),
					ihsPublishingJob.jobName,
					formatType,
					ihsPublishingJob.ihsUser.ihsMember.name,
					ihsPublishingJob.ihsUser.getName(),
					startDate + endDate,
					ihsPublishingJob.singestionJobStatus.name,
					ihsPublishingJob.link
			);
			pageingJson.items.add(publishingJobView);

		}

		return ok(toJson(pageingJson));
	}
}