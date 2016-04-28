package controllers;

import static play.libs.Json.toJson;
import json.IngestionDataParsingErrorDetailJson;
import json.IngestionDataParsingErrorJson;
import json.PageingJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import models.AppUser;
import models.IhsIngestionException;
import models.IhsIngestionJob;
import models.IhsIngestionRecord;
import models.IhsUser;
import models.Scommitment;
import models.SingestionDatatype;
import models.SingestionExceptionType;
import models.SingestionJobStatus;
import models.SingestionRecordStatus;

import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import parser.BaseData;
import parser.Portico.Portico;
import parser.papr.PAPR;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.BodyParser;
import util.Helper;
import views.html.*;

@Restrict(@Group("user"))
public class IngestionException extends Controller {

	static String INFO_OK = "OK";

	static DateTimeFormatter dtf = DateTimeFormat
			.forPattern("MMM dd, yyyy HH:mm aa");

	static PageingJson convertToJsonView(int offset,
			List<IhsIngestionRecord> ihsIngestionRecords) {

		PageingJson pageingJson = new PageingJson();
		List<IngestionDataParsingErrorJson> ingestionDataParsingErrorJsons = new ArrayList<IngestionDataParsingErrorJson>();

		pageingJson.maxrecord = IhsIngestionRecord.maxRecord;
		pageingJson.offset = offset;
		pageingJson.size = ihsIngestionRecords.size();

		String button = null;

		for (IhsIngestionRecord ihsIngestionRecord : ihsIngestionRecords) {

			try {
				if (SingestionRecordStatus.OnHold
						.equals(ihsIngestionRecord.singestionRecordStatus.name)) {

					String user = ihsIngestionRecord.locIshUser.getName();
					button = "<button id='" + "resolve"
							+ ihsIngestionRecord.ingestionRecordID
							+ "' onClick='getDataErrorDetail("
							+ ihsIngestionRecord.ingestionRecordID + ");' >"
							+ user + "</button>";

				} else {
					button = "<button id='" + "resolve"
							+ ihsIngestionRecord.ingestionRecordID
							+ "' onClick='getDataErrorDetail("
							+ ihsIngestionRecord.ingestionRecordID
							+ ");' >Resolve</button>";
				}
				ingestionDataParsingErrorJsons
						.add(new IngestionDataParsingErrorJson(
								// ihsIngestionRecord.ihsIngestionJob.creationDate
								// .toString(),
								dtf.print(new DateTime(
										ihsIngestionRecord.ihsIngestionJob.creationDate)),
								ihsIngestionRecord.ihsIngestionJob.name,
								ihsIngestionRecord.recordTitle,
								ihsIngestionRecord.issues.replace("|", "<br>"),
								ihsIngestionRecord.singestionRecordStatus.description,
								button));

			} catch (Exception e) {
				Logger.error("IngestionException.convertToJsonView()", e);
			}
		}

		pageingJson.items = ingestionDataParsingErrorJsons;

		return pageingJson;
	}

	/*
	 * Get the list of Ingestion records with data error exception
	 * 
	 * @param
	 * 
	 * @return
	 */

	@Restrict(@Group("admin"))
	public static Result getAllUserExceptions(int offset) {

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord
				.getAllUserExceptions(offset);

		// ingestionDataParsingErrorPageJson.items=convertToJsonView(ihsIngestionRecords);

		return ok(toJson(convertToJsonView(offset, ihsIngestionRecords)));
		// return ok(toJson(ingestionDataParsingErrorPageJson));
	}

	@Restrict(@Group("admin"))
	public static Result getAllUserAvailableExceptions(int offset) {

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord
				.getAllUserAvailableExceptions(offset);
		return ok(toJson(convertToJsonView(offset, ihsIngestionRecords)));

	}

	@Restrict(@Group("admin"))
	public static Result getAllUserOnholdExceptions(int offset) {

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord
				.getAllUserOnholdExceptions(offset);

		return ok(toJson(convertToJsonView(offset, ihsIngestionRecords)));

	}

	public static Result getAUserExceptions(int offset) {
		AppUser appuser = Helper.getAppUserFromCache(session().get(Login.User));
		IhsUser ihsUser = IhsUser.find.byId(appuser.userId);

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord
				.getAUserExceptions(offset, ihsUser);

		return ok(toJson(convertToJsonView(offset, ihsIngestionRecords)));
	}

	public static Result getAUserAvailableExceptions(int offset) {
		AppUser appuser = Helper.getAppUserFromCache(session().get(Login.User));
		IhsUser ihsUser = IhsUser.find.byId(appuser.userId);

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord
				.getAUserAvailableExceptions(offset, ihsUser);

		return ok(toJson(convertToJsonView(offset, ihsIngestionRecords)));
	}

	/*
	 *
	 */

	public static Result getAUserOnholdExceptions(int offset) {
		AppUser appuser = Helper.getAppUserFromCache(session().get(Login.User));
		IhsUser ihsUser = IhsUser.find.byId(appuser.userId);

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord
				.getAUserOnholdExceptions(offset, ihsUser);

		return ok(toJson(convertToJsonView(offset, ihsIngestionRecords)));
	}

	public static Result getAllExceptionsByJob(int jobId, int offset) {

		IhsIngestionJob ihsIngestionJob = IhsIngestionJob.find.byId(jobId);

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord
				.getAllExceptionsByJob(offset, ihsIngestionJob);

		return ok(toJson(convertToJsonView(offset, ihsIngestionRecords)));
	}

	public static Result getMyMemberExceptions(int offset) {
		AppUser appuser = Helper.getAppUserFromCache(session().get(Login.User));
		IhsUser ihsUser = IhsUser.find.byId(appuser.userId);

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord
				.getMyMemberExceptions(offset, ihsUser);

		return ok(toJson(convertToJsonView(offset, ihsIngestionRecords)));
	}

	public static Result getMyMemberAvailableExceptions(int offset) {
		AppUser appuser = Helper.getAppUserFromCache(session().get(Login.User));
		IhsUser ihsUser = IhsUser.find.byId(appuser.userId);

		List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord
				.getMyMemberAvailableExceptions(offset, ihsUser);

		return ok(toJson(convertToJsonView(offset, ihsIngestionRecords)));
	}
	
	
	/*
	 * get the ingestion record details with exception for resolve
	 */
	public static Result getExceptionDetail(int index) {

		List<IngestionDataParsingErrorDetailJson> IngestionDataParsingErrorDetailJsons = new ArrayList<IngestionDataParsingErrorDetailJson>();

		ObjectMapper mapper = new ObjectMapper();
		BaseData baseData = null;

		IhsIngestionRecord ihsIngestionRecord = IhsIngestionRecord.find
				.byId(index);

		String printissnInfo = INFO_OK;
		String eISSNInfo = INFO_OK;
		String titleInfo = INFO_OK;
		String oclcNumber = INFO_OK;
		String holdingInfo = INFO_OK;
		String yearsInfo = INFO_OK;

		for (IhsIngestionException ihsIngestionException : ihsIngestionRecord.ihsIngestionExceptions) {

			if (SingestionExceptionType.BadPrintISSN
					.equals((ihsIngestionException.singestionExceptionType.name))) {
				printissnInfo = ihsIngestionException.singestionExceptionType.description;
			}
			if (SingestionExceptionType.BadEISSN
					.equals((ihsIngestionException.singestionExceptionType.name))) {
				eISSNInfo = ihsIngestionException.singestionExceptionType.description;
			}

			if (SingestionExceptionType.BadYearformat
					.equals((ihsIngestionException.singestionExceptionType.name))) {
				yearsInfo = ihsIngestionException.singestionExceptionType.description;
			}

			if (SingestionExceptionType.BadHoldingFormat
					.equals((ihsIngestionException.singestionExceptionType.name))) {
				holdingInfo = ihsIngestionException.singestionExceptionType.description;
			}
			
			if (SingestionExceptionType.MissingAuthorizedVolume
					.equals((ihsIngestionException.singestionExceptionType.name))) {
				holdingInfo = ihsIngestionException.singestionExceptionType.description;
			}

			if (SingestionExceptionType.NoAuthorizedTitle
					.equals((ihsIngestionException.singestionExceptionType.name))) {
				printissnInfo = ihsIngestionException.singestionExceptionType.description;
				oclcNumber = ihsIngestionException.singestionExceptionType.description;
			}

			if (SingestionExceptionType.NoAuthorizedPublicationRange
					.equals((ihsIngestionException.singestionExceptionType.name))) {
				yearsInfo = ihsIngestionException.singestionExceptionType.description;
			}
		}

		try {
			baseData = mapper.readValue(ihsIngestionRecord.jsonRecordData,
					BaseData.class);
		} catch (IOException e) {
			Logger.error("Ingestion Controller:", e);
		}

		// print issn
		IngestionDataParsingErrorDetailJson printissn = new IngestionDataParsingErrorDetailJson(
				"Print ISSN", baseData.printISSN, baseData.printISSN,
				printissnInfo);
		IngestionDataParsingErrorDetailJson oclcNNUMBE = new IngestionDataParsingErrorDetailJson(
				"oclc Number", baseData.oclcNumber, baseData.oclcNumber, oclcNumber);
		IngestionDataParsingErrorDetailJson title = new IngestionDataParsingErrorDetailJson(
				"Title", baseData.title, baseData.title, titleInfo);
		IngestionDataParsingErrorDetailJson holding = new IngestionDataParsingErrorDetailJson(
				"Holdings", baseData.holding, baseData.holding,
				holdingInfo);
		IngestionDataParsingErrorDetailJson years = new IngestionDataParsingErrorDetailJson(
				"Years", baseData.years, baseData.years, yearsInfo);

		IngestionDataParsingErrorDetailJsons.add(printissn);
		IngestionDataParsingErrorDetailJsons.add(oclcNNUMBE);
		IngestionDataParsingErrorDetailJsons.add(title);
		IngestionDataParsingErrorDetailJsons.add(holding);
		IngestionDataParsingErrorDetailJsons.add(years);

		return ok(toJson(IngestionDataParsingErrorDetailJsons));
	}

	/*
	 * 
	 */
	public static Result postExceptionIgnore(int recordId) {

		IhsIngestionRecord.postExceptionIgnore(recordId);

		return ok();
	}

	/*
	 * 
	 */
	@Transactional
	public static Result postExceptionOnhold(int recordId) {

		try {
			AppUser appuser = Helper.getAppUserFromCache(session().get(
					Login.User));

			IhsIngestionRecord.postExceptionOnhold(recordId, appuser.userId);

		} catch (Exception e) {
			Logger.error("ingestion_exception_onhold", e);
			return internalServerError("ingestion_exception_onhold");
		}
		return ok();
	}

	@Transactional
	@BodyParser.Of(BodyParser.Json.class)
	public static Result postExceptionResolve() {

		try {

			BaseData baseData = null;

			String user = session().get(Login.User);

			String jsonString = Controller.request().body().asJson().toString();

			ObjectMapper mapper = new ObjectMapper();

			try {
				baseData = mapper.readValue(jsonString, BaseData.class);
			} catch (IOException e) {
				Logger.error("processPorticoresolve", e);
			}

			SingestionRecordStatus singestionRecordStatus = SingestionRecordStatus.find
					.where().eq("name", SingestionRecordStatus.Processing)
					.findUnique();

			IhsIngestionRecord ihsIngestionRecord = IhsIngestionRecord.find
					.byId(Integer.parseInt(baseData.recordId));

			ihsIngestionRecord.singestionRecordStatus = singestionRecordStatus;

			ihsIngestionRecord.update();

			IhsIngestionException.deleteIhsIngestionExceptions(Integer
					.parseInt(baseData.recordId));

			
			if(SingestionDatatype.Portico.equals(ihsIngestionRecord.ihsIngestionJob.singestionDatatype.name)){
				Portico.processPorticoresolve(user, baseData, ihsIngestionRecord,
				jsonString, Scommitment.find.byId(1) );
			} else if(SingestionDatatype.PAPR.equals(ihsIngestionRecord.ihsIngestionJob.singestionDatatype.name)){
				PAPR.processPapresolve(user, baseData, ihsIngestionRecord,
						jsonString, ihsIngestionRecord.ihsIngestionJob.scommitment);
			}

		} catch (Exception e) {

			Logger.error("ingestion_exception_resolve", e);
			return internalServerError("ingestion_exception_resolve");
		}

		return ok();
	}	
}
