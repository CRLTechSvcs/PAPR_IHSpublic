package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import models.AppUser;
import models.IhsMember;
import models.IhsPublishingJob;
import models.IhsReportingJob;
import models.IhsTitle;
import models.IhsUser;
import models.SingestionJobStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import json.NewReportView;
import json.PageingJson;
import json.PublishingView;
import json.ReportJobView;
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




@Restrict(@Group("user"))
public class Reporting extends Controller {

	static int MAX_ROW = 50;
	public static String IssuesHeld ="IssuesHeld";
	public static String JournalFamily ="JournalFamily";
	public static String BibliographicHistory ="BibliographicHistory";

	static DateTimeFormatter dtf = DateTimeFormat
			.forPattern("MMM dd, yyyy HH:mm aa");


	public static Result reporting_previous_reports(){
		return ok(reporting_previous_reports.render());
	}

	public static Result reporting_new_report(){
		return ok(reporting_new_report.render());
	}

	public static Result getReportingView() {

		NewReportView newReportView = new NewReportView();

		return ok(toJson(newReportView));
	}

	public static Result getReportingJobs(){

		PageingJson pageingJson = new PageingJson();

		ReportJobView reportJobView;

		List<IhsReportingJob> ihsReportingJobs = IhsReportingJob.find
				.where().orderBy("reportingJobId desc").setMaxRows(MAX_ROW)
				.findList();


		for(IhsReportingJob ihsReportingJob : ihsReportingJobs){

			reportJobView = new ReportJobView(dtf.print(ihsReportingJob.dateInitiated),ihsReportingJob.report,ihsReportingJob.parameters,
					ihsReportingJob.fileformat, ihsReportingJob.ihsUser.ihsMember.name, ihsReportingJob.ihsUser.getName(),
					ihsReportingJob.singestionJobStatus.name, ihsReportingJob.link);
			pageingJson.items.add(reportJobView);
		}

		return ok(toJson(pageingJson));

	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result submitReport() {

		NewReportView newReportView;

		try {

			String jsonString = Controller.request().body().asJson().toString();

			ObjectMapper mapper = new ObjectMapper();

			newReportView = mapper.readValue(jsonString, NewReportView.class);

			String user = session().get(Login.User);

			AppUser appUser = Helper.getAppUserFromCache(user);

			IhsUser ihsUser = IhsUser.find.byId(appUser.userId);

			IhsMember ihsMember = IhsMember.find.byId(newReportView.memberId);

			IhsTitle ihsTitle = IhsTitle.find.byId(newReportView.titleId);

			String parameters = "";

			if(IssuesHeld.equals(newReportView.report)){
				// parameters = "Member Id: " + ihsMember.name; // Travant original
				parameters = "Member: <br/>" + ihsMember.name; // AJE 2016-12-14
			}else{
				parameters = "Title: " + ihsTitle.title + "<br> ISSN: " + ihsTitle.printISSN;

			}

			SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
					.where().eq("name", SingestionJobStatus.Queued)
					.findUnique();


			IhsReportingJob ihsReportingJob = new IhsReportingJob(
					new DateTime(), newReportView.report, ihsUser, jsonString, singestionJobStatus,
					"PDF", parameters);

			ihsReportingJob.save();

			ActorSelection myActor = AppActorSystem.getInstance().getActorSystem()
					.actorSelection("user/ReportingActor");

			myActor.tell(new Integer(ihsReportingJob.reportingJobId), null);


		} catch (Exception e) {
			Logger.error("ingestion_exception_resolve", e);
			return internalServerError("ingestion_exception_resolve");
		}
		return ok();
	}
}