package controllers;

import static play.libs.Json.toJson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import json.IngestionJobView;
import models.AppUser;
import models.IhsIngestionJob;
import models.IhsIngestionRecord;
import models.IhsUser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import util.Helper;
import actors.AppActorSystem;
import akka.actor.ActorSelection;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

@Restrict(@Group("user"))
public class IngestionJob extends Controller {
	static String INFO_OK = "OK";

	static String CONTENT_TYPE = "Content-Type";
	static String CONTENT_DISPOSITION = "Content-Disposition";
	static String ATTACHMENT = "attachment";
	static String APP_FORCE_DWN = "application/force-download";

	static DateTimeFormatter dtf = DateTimeFormat
			.forPattern("MMM dd, yyyy HH:mm aa");

	static List<IngestionJobView> convertToJsonView(
			List<IhsIngestionJob> ihsIngestionJobs) {

		List<IngestionJobView> ingestionJobViews = new ArrayList<IngestionJobView>();

		for (IhsIngestionJob ihsIngestionJob : ihsIngestionJobs) {

			try {
				ingestionJobViews.add(new IngestionJobView(dtf
						.print(new DateTime(ihsIngestionJob.creationDate)),
						ihsIngestionJob.name,
						ihsIngestionJob.singestionDatatype.name,
						ihsIngestionJob.ihsUser.ihsMember.name,

						ihsIngestionJob.ihsUser.getName(),
						ihsIngestionJob.singestionJobStatus.name,
						IhsIngestionRecord
								.getRecordTotalAndError(ihsIngestionJob)));
			} catch (Exception e) {
				Logger.error("", e);
			}
		}

		return ingestionJobViews;
	}

	public static Result getBadformatFile(String fileName)
			throws FileNotFoundException {

		response().setHeader(CONTENT_TYPE, APP_FORCE_DWN);
		response().setHeader(CONTENT_DISPOSITION, ATTACHMENT);
		
Logger.info("ingestionJob.java, getBadformatFile() will send param to ok(): " +fileName.replace(IhsIngestionRecord.tilde, System.getProperty("file.separator")) +".");

		return ok(new FileInputStream(fileName.replace(
				// AJE 2016-08-10 18:55:18: preserve Travant original 
				IhsIngestionRecord.tilde, System.getProperty("file.separator"))));
	}

	/*
	 * Get the job list
	 */
	@Restrict(@Group("admin"))
	public static Result getAllIngestionJobs() {

		List<IhsIngestionJob> ihsIngestionJobs = null;

		try {

			ihsIngestionJobs = IhsIngestionJob.getAllUserJob();

		} catch (Exception e) {
			Logger.error("Ingestion.ingestion_home_job_list()", e);
		}

		return ok(toJson(convertToJsonView(ihsIngestionJobs)));
	}

	//
	public static Result getAUserIngestionJobs() {

		List<IhsIngestionJob> ihsIngestionJobs = null;

		try {

			AppUser appuser = Helper.getAppUserFromCache(session().get(
					Login.User));
			IhsUser ihsUser = IhsUser.find.byId(appuser.userId);

			ihsIngestionJobs = IhsIngestionJob.getAUserJob(ihsUser);

		} catch (Exception e) {
			Logger.error("Ingestion.ingestion_home_job_list()", e);
		}

		return ok(toJson(convertToJsonView(ihsIngestionJobs)));
	}

	//
	public static Result getAMemberIngestionJobs() {

		List<IhsIngestionJob> ihsIngestionJobs = null;

		try {

			AppUser appuser = Helper.getAppUserFromCache(session().get(
					Login.User));
			IhsUser ihsUser = IhsUser.find.byId(appuser.userId);

			ihsIngestionJobs = IhsIngestionJob.getAMemberJob(ihsUser);

		} catch (Exception e) {
			Logger.error("Ingestion.ingestion_home_job_list()", e);
		}

		return ok(toJson(convertToJsonView(ihsIngestionJobs)));
	}

	@Restrict(@Group("admin"))
	public static Result getAllQuedUserJobs() {

		List<IhsIngestionJob> ihsIngestionJobs = null;

		try {

			ihsIngestionJobs = IhsIngestionJob.getAllQuedUserJob();

		} catch (Exception e) {
			Logger.error("Ingestion.ingestion_home_job_list()", e);
		}

		return ok(toJson(convertToJsonView(ihsIngestionJobs)));
	}

	@Restrict(@Group("admin"))
	public static Result getAllArchiveUserJob() {

		List<IhsIngestionJob> ihsIngestionJobs = null;

		try {

			ihsIngestionJobs = IhsIngestionJob.getAllArchiveUserJob();

		} catch (Exception e) {
			Logger.error("Ingestion.ingestion_home_job_list()", e);
		}

		return ok(toJson(convertToJsonView(ihsIngestionJobs)));
	}

}