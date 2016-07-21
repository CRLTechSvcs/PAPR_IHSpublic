package parser.Portico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;

import parser.BaseData;
import play.Logger;
import play.cache.Cache;
import play.mvc.Controller;
import models.AppUser;
import models.IhsHolding;
import models.IhsIngestionException;
import models.IhsIngestionRecord;
import models.IhsIssue;
import models.IhsLocation;
import models.IhsMember;
import models.IhsPublicationRange;
import models.IhsTitle;
import models.IhsUser;
import models.IhsVolume;
import models.IhssourcePublicationRange;
import models.Scommitment;
import models.SconditionTypeOverall;
import models.SholdingStatus;
import models.SihsVarified;
import models.SingestionExceptionStatus;
import models.SingestionExceptionType;
import models.SingestionRecordStatus;
import models.SissueStatus;
import models.SperiodicityType;
import models.SvalidationLevel;

public class Portico {

	static String DASH = "-";
	static String queued = "queued";
	static String EMPTY = "";
	public static String csvSplitBy = "\t";
	static String spaceDashspace =" - ";
	static String space =" ";
	static String nospace ="";
			
	public static void processPortico(IhsIngestionRecord ihsIngestionRecord,
			IhsUser ihsUser, IhsMember ihsMember, IhsLocation ihsLocation, Scommitment scommitment ) {

		BaseData porticoData = null;
		List<String> errorString = new ArrayList<String>();

		String jsonString = null;

		int index = 0;

		SingestionExceptionStatus singestionExceptionStatus = SingestionExceptionStatus.find
				.where().eq("name", SingestionExceptionStatus.Available)
				.findUnique();

		// Check for values
		try {
			porticoData = BaseData
					.buildPorticoData(ihsIngestionRecord.rawRecordData);
		} catch (Exception e) {

			SingestionRecordStatus singestionRecordStatusBad = SingestionRecordStatus.find
					.where().eq("name", SingestionRecordStatus.BadRecordError)
					.findUnique();
			IhsIngestionRecord.updateStatusDetail(ihsIngestionRecord,
					singestionRecordStatusBad, "", "", "");

			Logger.error("processPortico() validation:"
					+ SingestionRecordStatus.BadRecordError);
			return;
		}

		// Check the status
		if (queued.equals(porticoData.status)) {
			Logger.info("processPortico() validation:" + porticoData.status);
			return;
		}

		// update the json obj

		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(porticoData);

		} catch (JsonProcessingException e) {
			Logger.error("", e);
			return;
		}

		if (dataValidateAndInsert(porticoData, ihsIngestionRecord, ihsUser,
				ihsMember, ihsLocation, errorString, scommitment )) {

			SingestionRecordStatus singestionRecordStatusAvailable = SingestionRecordStatus.find
					.where().eq("name", SingestionRecordStatus.Available)
					.findUnique();

			IhsIngestionRecord
					.updateStatusDetail(
							ihsIngestionRecord,
							singestionRecordStatusAvailable,
							jsonString,
							porticoData.title,
							errorString.toString().replace("[", "")
									.replace("]", "").replace(" ", "")
									.replace(",", ""));

			Logger.debug("########FAILURE#############" + jsonString);

		} else {
			SingestionRecordStatus singestionRecordStatusComplete = SingestionRecordStatus.find
					.where().eq("name", SingestionRecordStatus.Complete)
					.findUnique();
			IhsIngestionRecord.updateStatusDetail(ihsIngestionRecord,
					singestionRecordStatusComplete, jsonString,
					porticoData.title, "");
			Logger.debug("#################SUCCESS#### " + jsonString);
		}
	}

	public static void processPorticoresolve(String username,
			BaseData porticoData, IhsIngestionRecord ihsIngestionRecord,
			String jsonString, Scommitment scommitment ) {

		AppUser appUser = (AppUser) Cache.get(username);

		IhsUser ihsUser = IhsUser.find.byId(appUser.userId);
		IhsMember ihsMember = IhsMember.find.byId(appUser.memberId);
		IhsLocation ihsLocation = IhsLocation.find.byId(appUser.memberId);

		List<String> errorString = new ArrayList<String>();

		if (dataValidateAndInsert(porticoData, ihsIngestionRecord, ihsUser,
				ihsMember, ihsLocation, errorString,  scommitment )) {

			SingestionRecordStatus singestionRecordStatusAvailable = SingestionRecordStatus.find
					.where().eq("name", SingestionRecordStatus.Available)
					.findUnique();

			IhsIngestionRecord
					.updateStatusDetail(
							ihsIngestionRecord,
							singestionRecordStatusAvailable,
							jsonString,
							porticoData.title,
							errorString.toString().replace("[", "")
									.replace("]", "").replace(" ", "")
									.replace(",", ""));

			Logger.debug("########FAILURE#############" + jsonString);

		} else {
			SingestionRecordStatus singestionRecordStatusComplete = SingestionRecordStatus.find
					.where().eq("name", SingestionRecordStatus.Complete)
					.findUnique();
			IhsIngestionRecord.updateStatusDetail(ihsIngestionRecord,
					singestionRecordStatusComplete, jsonString,
					porticoData.title, "");
			Logger.debug("#################SUCCESS#### " + jsonString);
		}
	}

	public static boolean dataValidateAndInsert(BaseData porticoData,
			IhsIngestionRecord ihsIngestionRecord, IhsUser ihsUser,
			IhsMember ihsMember, IhsLocation ihsLocation,
			List<String> errorString, Scommitment scommitment ) {

		List<PorticoVolume> porticoVolumes = new ArrayList<PorticoVolume>();
		boolean error = false;

		SingestionExceptionStatus singestionExceptionStatus = SingestionExceptionStatus.find
				.where().eq("name", SingestionExceptionStatus.Available)
				.findUnique();

		SperiodicityType speriodicityType = SperiodicityType.find.where()
				.eq("name", SperiodicityType.Default).findUnique();

		// Check years
		if (porticoData.years.contains(DASH)) {
			String[] tmpYears = porticoData.years.split(DASH);
			porticoData.startYear = Integer.parseInt(tmpYears[0]);
			porticoData.endYear = Integer.parseInt(tmpYears[1]);
		} else {
			error = true;
			SingestionExceptionType singestionExceptionType = SingestionExceptionType.find
					.where().eq("name", SingestionExceptionType.BadYearformat)
					.findUnique();
			new IhsIngestionException(ihsIngestionRecord,
					singestionExceptionType, porticoData.title, "",
					singestionExceptionStatus, ihsUser).save();
			Logger.info("dataValidateAndInsert() validation:"
					+ SingestionExceptionType.BadYearformat + ":"
					+ porticoData.years);
			errorString.add(singestionExceptionType.description + "|");
		}

		if (porticoData.printISSN.replace("-", "").length() != 8) {

			error = true;
			SingestionExceptionType singestionExceptionType = SingestionExceptionType.find
					.where().eq("name", SingestionExceptionType.BadPrintISSN)
					.findUnique();
			new IhsIngestionException(ihsIngestionRecord,
					singestionExceptionType, porticoData.title, "",
					singestionExceptionStatus, ihsUser).save();
			Logger.info("dataValidateAndInsert() validation:"
					+ SingestionExceptionType.BadPrintISSN + ":"
					+ porticoData.printISSN);
			errorString.add(singestionExceptionType.description + "|");
		}

		// Check Holding
		try {

			
			
			
			String[] volumsissues = porticoData.holding.substring(0,
					porticoData.holding.length() - 1).split("\\),");

			PorticoVolume porticoVolume = null;
			
			for (String volumsissuePairs : volumsissues) {

				String[] volumsIssuesAll = volumsissuePairs.split(spaceDashspace);
				
				String volumsIssuesAllNospace= volumsIssuesAll[1].replace(space, nospace);
				
				String[] volumsissuePair = volumsIssuesAllNospace.split("\\(");

				if (volumsissuePair[0].startsWith("v")
						&& volumsissuePair[0].contains(".")) {
					String[] volumes = volumsissuePair[0].split("\\.");

					porticoVolume = new PorticoVolume(volumes[1].trim());

				} else {
					throw new Exception();
				}

				String[] issues = volumsissuePair[1].split(",");
				for (String issuetmp : issues) {
					if (issuetmp.contains("-")) {
						String[] issueRun = issuetmp.split("-");
						int beginIssue = Integer.parseInt(issueRun[0]);
						int endIssue = Integer.parseInt(issueRun[1]);

						for (int i = beginIssue; i <= endIssue; i++) {
							porticoVolume.issues
									.add(Integer.toString(i).trim());
						}
					} else {
						porticoVolume.issues.add(issuetmp.trim());
					}
				}

				porticoVolumes.add(porticoVolume);
			}

		} catch (Exception e) {
			error = true;

			SingestionExceptionType singestionExceptionType = SingestionExceptionType.find
					.where()
					.eq("name", SingestionExceptionType.BadHoldingFormat)
					.findUnique();

			new IhsIngestionException(ihsIngestionRecord,
					singestionExceptionType, porticoData.title, "",
					singestionExceptionStatus, ihsUser).save();

			Logger.info("dataValidateAndInsert() validation:"
					+ SingestionExceptionType.BadHoldingFormat + ":"
					+ porticoData.holding);
			errorString.add(singestionExceptionType.description + "|");
		}

		// Didn't pass the data validation test
		// Return
		if (error) {
			return error;
		}
		// check the title the issn and date range
		IhsTitle ihsTitle = IhsTitle.find.fetch("ihsPublicationRange").where()
				.eq("printISSN", porticoData.printISSN).findUnique();

		if (ihsTitle == null) {
			error = true;
			SingestionExceptionType singestionExceptionType = SingestionExceptionType.find
					.where()
					.eq("name", SingestionExceptionType.NoAuthorizedTitle)
					.findUnique();

			new IhsIngestionException(ihsIngestionRecord,
					singestionExceptionType, porticoData.title, "",
					singestionExceptionStatus, ihsUser).save();

			Logger.info("dataValidateAndInsert() validation:"
					+ SingestionExceptionType.NoAuthorizedTitle);
			errorString.add(singestionExceptionType.description + "|");
			return error;

		}

		SconditionTypeOverall sconditionTypeOverall = SconditionTypeOverall.find.where()
				.eq("name", SconditionTypeOverall.Unknown).findUnique();
		
		SihsVarified sihsVarified = SihsVarified.find.where()
				.eq("name", SihsVarified.NO).findUnique();
		
		
		SvalidationLevel svalidationLevel = SvalidationLevel.find.where()
				.eq("name", SvalidationLevel.None).findUnique();
		
		for (IhsPublicationRange IhsPublicationRange : ihsTitle.ihsPublicationRange) {

			int startYear = IhsPublicationRange.startDate.getYear();
			int endYear = IhsPublicationRange.endDate == null ? 9999
					: IhsPublicationRange.endDate.getYear();

			if (porticoData.startYear >= startYear
					&& porticoData.startYear <= endYear
					&& porticoData.endYear >= startYear
					&& porticoData.endYear <= endYear) {

				for (PorticoVolume porticoVolume : porticoVolumes) {

					IhsVolume ihsVolume = IhsVolume.find.where()
							.eq("titleID", ihsTitle.titleID)
							.eq("volumeNumber", porticoVolume.volume)
							.findUnique();
					SissueStatus sissueStatus = SissueStatus.find.where()
							.eq("name", SissueStatus.Default).findUnique();

					SholdingStatus sholdingStatus = SholdingStatus.find.where()
							.eq("name", SholdingStatus.Default).findUnique();

					if (ihsVolume == null) {
						ihsVolume = new IhsVolume(ihsTitle,
								porticoVolume.volume, null, null);
						ihsVolume.save();
					}
					
					for (String issue : porticoVolume.issues) {

						// Insert source title

						// Insert issue

						IhsIssue ihsIssue = IhsIssue.find.where()
								.eq("titleID", ihsTitle.titleID)
								.eq("volumeID", ihsVolume.volumeID)
								.eq("issueNumber", issue).findUnique();

						if (ihsIssue == null) {
							ihsIssue = new IhsIssue(ihsTitle, ihsVolume,
									IhsPublicationRange, null, issue, EMPTY,
									EMPTY, -1, -1, sissueStatus);
							ihsIssue.save();
						}
						
						// Insert holding
						IhsHolding ihsHolding = IhsHolding.find.where()
								.eq("issueID", ihsIssue.issueID)
								.eq("memberID", ihsMember.memberID)
								.eq("locationID", ihsLocation.locationID)
								.findUnique();

						if (ihsHolding == null) { 
							
							new IhsHolding(ihsIssue, ihsMember, ihsLocation,
									sholdingStatus, sconditionTypeOverall, sihsVarified, svalidationLevel, scommitment).save();
						}

					}
				}

			} else {
				error = true;
				SingestionExceptionType singestionExceptionType = SingestionExceptionType.find
						.where()
						.eq("name",
								SingestionExceptionType.NoAuthorizedPublicationRange)
						.findUnique();

				new IhsIngestionException(ihsIngestionRecord,
						singestionExceptionType, porticoData.title, "",
						singestionExceptionStatus, ihsUser).save();

				Logger.info("dataValidateAndInsert() validation:"
						+ SingestionExceptionType.NoAuthorizedPublicationRange);
				errorString.add(singestionExceptionType.description + "|");
				return error;
			}
		}

		return error;
	}
}
