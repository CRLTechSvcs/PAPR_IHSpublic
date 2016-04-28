package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.AppUser;
import models.IhsDeaccessionJob;
import models.IhsHolding;
import models.IhsIngestionRecord;
import models.IhsMember;
import models.IhsUser;
import models.PwantTitleMember;
import models.Scommitment;
import models.SingestionJobStatus;
import models.SvalidationLevel;
import json.CommitmentView;
import json.DeaccesionReportView;
import json.DeaccessionIssueView;
import json.DeaccessionJobView;
import json.DeaccessionNewView;
import json.DeaccessionTitleView;
import json.IhsVerifiedView;
import json.PageingJson;
import actors.AppActorSystem;
import akka.actor.ActorSelection;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import reports.DeaccessionReport;
import util.Helper;
import views.html.*;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

@Restrict(@Group("user"))
public class Deaccession extends Controller {

	static DateTimeFormatter dtf = DateTimeFormat
			.forPattern("MMM dd, yyyy HH:mm aa");

	static DateTimeFormatter dtfShort = DateTimeFormat
			.forPattern("MMM dd, yyyy");
	static int MAX_ROW = 100;

	public static Result deaccession_summary() {
		return ok(deaccession_summary.render());
	}

	public static Result deaccession_finalized_job() {
		return ok(deaccession_finalized_job.render());
	}

	public static Result deaccession_new_deaccession() {

		return ok(deaccession_new_deaccession.render());
	}

	public static Result getDeaccessionNewView() {

		DeaccessionNewView deaccessionNewView = new DeaccessionNewView();

		List<SvalidationLevel> svalidationLevels = SvalidationLevel.find
				.findList();
		for (SvalidationLevel svalidationLevel : svalidationLevels) {
			deaccessionNewView.deaccessionCrlView.ihsVerifiedView
					.add(new IhsVerifiedView(
							svalidationLevel.validationLevelID,
							svalidationLevel.name, 0, svalidationLevel.level));
		}

		List<Scommitment> scommitments = Scommitment.find.findList();

		for (Scommitment scommitmen : scommitments) {
			deaccessionNewView.commitmentView.add(new CommitmentView(
					scommitmen.commitmentID, scommitmen.name, 0));
		}

		deaccessionNewView.deaccessionCrlView.ihsVerifiedView.get(0).checked = 1;

		return ok(toJson(deaccessionNewView));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result postDeaccessionReport() {

		DeaccesionReportView deaccesionReportView = null;

		try {
			DeaccessionNewView deaccessionNewView = null;

			String jsonString = Controller.request().body().asJson().toString();

			ObjectMapper mapper = new ObjectMapper();

			deaccessionNewView = mapper.readValue(jsonString,
					DeaccessionNewView.class);

			String user = session().get(Login.User);

			AppUser appUser = Helper.getAppUserFromCache(user);

			IhsUser ihsUser = IhsUser.find.byId(appUser.userId);

			deaccesionReportView = IhsHolding.searchDeaccession(ihsUser,
					deaccessionNewView);

			deaccesionReportView.jobName = deaccessionNewView.jobName;
			deaccesionReportView.standard = deaccessionNewView.standard;

		} catch (Exception e) {
			Logger.error("ingestion_exception_resolve", e);
			return internalServerError("ingestion_exception_resolve");
		}

		return ok(toJson(deaccesionReportView));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result postDeaccessionJob() {

		// String jsonString = Controller.request().body().asJson().toString();

		DeaccesionReportView deaccesionReportView = null;

		try {

			String jsonString = Controller.request().body().asJson().toString();

			ObjectMapper mapper = new ObjectMapper();

			deaccesionReportView = mapper.readValue(jsonString,
					DeaccesionReportView.class);

			String user = session().get(Login.User);

			AppUser appUser = Helper.getAppUserFromCache(user);

			IhsUser ihsUser = IhsUser.find.byId(appUser.userId);

			//Create deaccession Report
			
			DateTime initTime = new DateTime();
			
			deaccesionReportView.initDate = dtfShort.print(initTime);
			deaccesionReportView.orgName=ihsUser.ihsMember.name;
			deaccesionReportView.userName=ihsUser.getName();
			
		
			deaccesionReportView.numberOfPreservation = 0;
			deaccesionReportView.NumberOfDeaccession=0;
			deaccesionReportView.NumberOfDonation=0;
			
			for(DeaccessionTitleView adeaccessionTitleView : deaccesionReportView.deaccessionTitleView){
				
				boolean flag = true;
				for(DeaccessionIssueView adeaccessionIssueView :adeaccessionTitleView.deaccessionIssueView){
					deaccesionReportView.total++;
					if("p".equals(adeaccessionIssueView.action)){
						deaccesionReportView.numberOfPreservation++;
					}else if ("d".equals(adeaccessionIssueView.action)){
						deaccesionReportView.NumberOfDeaccession++;
						
					}else if ("n".equals(adeaccessionIssueView.action)){
						deaccesionReportView.NumberOfDonation++;
					
						List<PwantTitleMember> pwanttitlemembers = 
								PwantTitleMember.find.where().eq("titleID", adeaccessionTitleView.titleId).findList();
						if(flag){
							
							for(PwantTitleMember pwantTitleMember : pwanttitlemembers){
							
								IhsMember ihsMember = IhsMember.find.byId(pwantTitleMember.memberID);
								adeaccessionTitleView.donnee.add(ihsMember.name);
							}
							flag = false;
						}
					}
				}
			}
			/*
			 * 		/ingestion/getBadformatFile/ is route to controllers.IngestionJob.getBadformatFile
			 * 		
			 */
			String link = "<a href=/ingestion/getBadformatFile/"
					+ new DeaccessionReport().createPdf(deaccesionReportView).replace(System.getProperty("file.separator"),
							IhsIngestionRecord.tilde) + ">" + "Download</a>";
			
			SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
					.where().eq("name", SingestionJobStatus.Queued)
					.findUnique();

			IhsDeaccessionJob ihsDeaccessionJob = new IhsDeaccessionJob(
					initTime, deaccesionReportView.jobName, ihsUser,
					deaccesionReportView.NumberOfDeaccession + deaccesionReportView.NumberOfDonation, jsonString,
					singestionJobStatus, link);

			ihsDeaccessionJob.save();

			deaccesionReportView.jobId = ihsDeaccessionJob.deaccessionJobId;

		} catch (Exception e) {
			Logger.error("ingestion_exception_resolve", e);
			return internalServerError("ingestion_exception_resolve");
		}

		return ok();
	}

	public static Result getAllDeaccessionJobs(int jobStattus) {

		PageingJson pageingJson = new PageingJson();

		List<IhsDeaccessionJob> ihsDeaccessionJobs;

		String user = session().get(Login.User);

		AppUser appUser = Helper.getAppUserFromCache(user);

		IhsUser ihsUser = IhsUser.find.byId(appUser.userId);

		if (jobStattus == 0) {
			SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
					.where().eq("name", SingestionJobStatus.Queued)
					.findUnique();
			
			ihsDeaccessionJobs = IhsDeaccessionJob.find.where()
					.eq("singestionJobStatus", singestionJobStatus)
					.orderBy("deaccessionJobId desc").setMaxRows(MAX_ROW)
					.findList();

		} else {
			SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
					.where().eq("name", SingestionJobStatus.Queued)
					.findUnique();
			
			ihsDeaccessionJobs = IhsDeaccessionJob.find.where()
					.ne("singestionJobStatus", singestionJobStatus)
					.orderBy("deaccessionJobId desc")
					.setMaxRows(MAX_ROW).findList();
		}

		for (IhsDeaccessionJob ihsDeaccessionJob : ihsDeaccessionJobs) {
			String dataCompleted = ihsDeaccessionJob.dateCompleted != null ? dtf
					.print(new DateTime(ihsDeaccessionJob.dateCompleted)) : "";

			if (!SingestionJobStatus.Complete
					.equals(ihsDeaccessionJob.singestionJobStatus.name)) {
				dataCompleted = "";
			}

			pageingJson.items.add(new DeaccessionJobView(

			dtf.print(new DateTime(ihsDeaccessionJob.dateInitiated)),
					dataCompleted, ihsDeaccessionJob.jbbName,
					ihsDeaccessionJob.ihsUser.ihsMember.name,
					ihsDeaccessionJob.ihsUser.getName(),
					ihsDeaccessionJob.selected, "<button onClick = 'submitJob("
							+ ihsDeaccessionJob.deaccessionJobId
							+ "'></button>", ihsDeaccessionJob.link, ihsDeaccessionJob.selected,
					ihsDeaccessionJob.singestionJobStatus.name));
		}

		return ok(toJson(pageingJson));
	}

	public static Result getAMemberDeaccessionJobs(int jobStattus) {

		PageingJson pageingJson = new PageingJson();

		List<IhsDeaccessionJob> ihsDeaccessionJobs;

		String user = session().get(Login.User);

		AppUser appUser = Helper.getAppUserFromCache(user);

		IhsUser ihsUser = IhsUser.find.byId(appUser.userId);

		if (jobStattus == 0) {
			SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
					.where().eq("name", SingestionJobStatus.Queued)
					.findUnique();
			ihsDeaccessionJobs = IhsDeaccessionJob.find.where()
					.eq("ihsUser.ihsMember", ihsUser.ihsMember)
					.eq("singestionJobStatus", singestionJobStatus)
					.orderBy("deaccessionJobId desc")
					.setMaxRows(MAX_ROW).findList();

		} else {
			SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
					.where().eq("name", SingestionJobStatus.Queued)
					.findUnique();
			ihsDeaccessionJobs = IhsDeaccessionJob.find.where()
					.eq("ihsUser.ihsMember", ihsUser.ihsMember)
					.ne("singestionJobStatus", singestionJobStatus)
					.orderBy("deaccessionJobId desc")
					.setMaxRows(MAX_ROW).findList();
		}

		for (IhsDeaccessionJob ihsDeaccessionJob : ihsDeaccessionJobs) {

			String dataCompleted = ihsDeaccessionJob.dateCompleted != null ? dtf
					.print(new DateTime(ihsDeaccessionJob.dateCompleted)) : "";

			if (!SingestionJobStatus.Complete
					.equals(ihsDeaccessionJob.singestionJobStatus.name)) {
				dataCompleted = "";
			}

			pageingJson.items.add(new DeaccessionJobView(

			dtf.print(new DateTime(ihsDeaccessionJob.dateInitiated)),
					dataCompleted, ihsDeaccessionJob.jbbName,
					ihsDeaccessionJob.ihsUser.ihsMember.name,
					ihsDeaccessionJob.ihsUser.getName(),
					ihsDeaccessionJob.selected, "<button onClick = 'submitJob("
							+ ihsDeaccessionJob.deaccessionJobId
							+ "'></button>", ihsDeaccessionJob.link, ihsDeaccessionJob.selected,
					ihsDeaccessionJob.singestionJobStatus.name));
		}

		return ok(toJson(pageingJson));

	}

	public static Result getAUserDeaccessionJobs(int jobStattus) {

		PageingJson pageingJson = new PageingJson();

		List<IhsDeaccessionJob> ihsDeaccessionJobs;

		String user = session().get(Login.User);

		AppUser appUser = Helper.getAppUserFromCache(user);

		IhsUser ihsUser = IhsUser.find.byId(appUser.userId);

		if (jobStattus == 0) {
			SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
					.where().eq("name", SingestionJobStatus.Queued)
					.findUnique();
			ihsDeaccessionJobs = IhsDeaccessionJob.find.where()
					.eq("ihsUser", ihsUser)
					.eq("singestionJobStatus", singestionJobStatus)
					.orderBy("deaccessionJobId desc").setMaxRows(MAX_ROW)
					.findList();

		} else {

			SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
					.where().eq("name", SingestionJobStatus.Queued)
					.findUnique();
			ihsDeaccessionJobs = IhsDeaccessionJob.find.where()
					.eq("ihsUser", ihsUser)
					.ne("singestionJobStatus", singestionJobStatus)
					.orderBy("deaccessionJobId desc").setMaxRows(MAX_ROW)
					.findList();
		}

		for (IhsDeaccessionJob ihsDeaccessionJob : ihsDeaccessionJobs) {

			String dataCompleted = ihsDeaccessionJob.dateCompleted != null ? dtf
					.print(new DateTime(ihsDeaccessionJob.dateCompleted)) : "";
			if (!SingestionJobStatus.Complete
					.equals(ihsDeaccessionJob.singestionJobStatus.name)) {
				dataCompleted = "";
			}

			pageingJson.items.add(new DeaccessionJobView(

			dtf.print(new DateTime(ihsDeaccessionJob.dateInitiated)),
					dataCompleted, ihsDeaccessionJob.jbbName,
					ihsDeaccessionJob.ihsUser.ihsMember.name,
					ihsDeaccessionJob.ihsUser.getName(),
					ihsDeaccessionJob.selected,
					"<a href='javascript:submitJob("
							+ ihsDeaccessionJob.deaccessionJobId
							+ ")'>Finalize this job</a>", 
							ihsDeaccessionJob.link,
					ihsDeaccessionJob.selected,
					ihsDeaccessionJob.singestionJobStatus.name));
		}

		return ok(toJson(pageingJson));
	}

	public static Result submitDeaccessionJob(int jobId) {

		ActorSelection myActor = AppActorSystem.getInstance().getActorSystem()
				.actorSelection("user/DeaccessionJobActor");

		SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
				.where().eq("name", SingestionJobStatus.Processing)
				.findUnique();

		IhsDeaccessionJob ihsDeaccessionJob = IhsDeaccessionJob.find
				.byId(jobId);

		ihsDeaccessionJob.singestionJobStatus = singestionJobStatus;

		ihsDeaccessionJob.update();

		myActor.tell(new Integer(jobId), null);

		return ok();
	}
}