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
	static String MARK ="MARC Text File (.txt)";
	static String PORTICO = "Portico Excel File (.xls, .xlsx)";
	static String IHS_XLS ="IHS Holdings Data Excel File (.xls, .xlsx)";
	static String IHS_CSV ="IHS Holdings Data CSV File (.csv)";

	static DateTimeFormatter dtf = DateTimeFormat
			.forPattern("MMM dd, yyyy HH:mm aa");

	static DateTimeFormatter shortdtf = DateTimeFormat.forPattern("mm/dd/yyyy");

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

      Logger.info("Publishing.java: (1) postPublishingView(); publishingView.startDate=" +publishingView.startDate+ "; publishingView.endDate=" +publishingView.endDate);
      /*
        AJE 2016-11-02 up to this point, startDate + endDate are valid,
          same values from <input> fields
        But Logger message "(2)" after these next DateTime declarations shows that these 2 lines
          reset the MONTH to always '01' (but YEAR and DAY are ok)

			DateTime startDate = "".equals(publishingView.startDate) ? null
					: shortdtf.parseDateTime(publishingView.startDate); // Travant original
			DateTime endDate = "".equals(publishingView.endDate) ? null
					: shortdtf.parseDateTime(publishingView.endDate); // Travant original
      */
      DateTime startDate = new DateTime(publishingView.startDate); // AJE 2016-11-02
      DateTime endDate   = new DateTime(publishingView.endDate); // AJE 2016-11-02

      Logger.info("Publishing.java: (2) postPublishingView(); has startDate=" +startDate+ "; endDate=" +endDate);

			SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
					.where().eq("name", SingestionJobStatus.Queued)
					.findUnique();

			IhsPublishingJob ihsPublishingJob = new IhsPublishingJob(
					new DateTime(), publishingView.jobName, ihsUser, startDate,
					endDate, jsonString, singestionJobStatus,
					publishingView.fileFormat);

			ihsPublishingJob.save();

			ActorSelection myActor = AppActorSystem.getInstance().getActorSystem()
					.actorSelection("user/PublishingJobActor");

			myActor.tell(new Integer(ihsPublishingJob.publishingJobId), null);

		} catch (Exception e) {
			Logger.error("ingestion_exception_resolve", e);
			return internalServerError("ingestion_exception_resolve");
		}
		return ok();
	}

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

			String foramType ="";

			if(ihsPublishingJob.fileformat == 1){
				foramType = MARK;
			}else if (ihsPublishingJob.fileformat == 2){
				foramType = PORTICO;
			}else if(ihsPublishingJob.fileformat == 3){
				foramType = IHS_XLS;
			}else if(ihsPublishingJob.fileformat == 4){
				foramType = IHS_CSV;
			}


			String startDate = ihsPublishingJob.startDate != null ? shortdtf.print( ihsPublishingJob.startDate) + " to ": "Begining to";
			String endDate = ihsPublishingJob.endDate != null ? shortdtf.print( ihsPublishingJob.endDate): " End";


			PublishingJobView publishingJobView = new PublishingJobView(
					dtf.print(ihsPublishingJob.dateInitiated),
					ihsPublishingJob.jobName,
					foramType,
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

			String foramType ="";

			if(ihsPublishingJob.fileformat == 1){
				foramType = MARK;
			}else if (ihsPublishingJob.fileformat == 2){
				foramType = PORTICO;
			}else if(ihsPublishingJob.fileformat == 3){
				foramType = IHS_XLS;
			}else if(ihsPublishingJob.fileformat == 4){
				foramType = IHS_CSV;
			}


			String startDate = ihsPublishingJob.startDate != null ? shortdtf.print( ihsPublishingJob.startDate) + " to ": "Begining to";
			String endDate = ihsPublishingJob.endDate != null ? shortdtf.print( ihsPublishingJob.endDate): " End";


			PublishingJobView publishingJobView = new PublishingJobView(
					dtf.print(ihsPublishingJob.dateInitiated),
					ihsPublishingJob.jobName,
					foramType,
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

			String foramType ="";

			if(ihsPublishingJob.fileformat == 1){
				foramType = MARK;
			}else if (ihsPublishingJob.fileformat == 2){
				foramType = PORTICO;
			}else if(ihsPublishingJob.fileformat == 3){
				foramType = IHS_XLS;
			}else if(ihsPublishingJob.fileformat == 4){
				foramType = IHS_CSV;
			}


			String startDate = ihsPublishingJob.startDate != null ? shortdtf.print( ihsPublishingJob.startDate) + " to ": "Begining to";
			String endDate = ihsPublishingJob.endDate != null ? shortdtf.print( ihsPublishingJob.endDate): " End";


			PublishingJobView publishingJobView = new PublishingJobView(
					dtf.print(ihsPublishingJob.dateInitiated),
					ihsPublishingJob.jobName,
					foramType,
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