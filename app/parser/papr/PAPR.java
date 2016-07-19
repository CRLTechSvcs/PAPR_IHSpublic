package parser.papr;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import parser.BaseData;
import play.Logger;
import play.cache.Cache;
import util.Helper;
import models.AppUser;
import models.IhsHolding;
import models.IhsIngestionException;
import models.IhsIngestionRecord;
import models.IhsLocation;
import models.IhsMember;
import models.IhsTitle;
import models.IhsUser;
import models.IhsVolume;
import models.Scommitment;
import models.SconditionTypeOverall;
import models.SholdingStatus;
import models.SihsVarified;
import models.SingestionExceptionStatus;
import models.SingestionExceptionType;
import models.SingestionRecordStatus;
import models.IhsIssue;
import models.SvalidationLevel;

public class PAPR {

	public static String cvsSplitBy = "\t";

	// v.10:no.7-v.14:no.91
	static String patternStringVdot1 = "(v.\\d+:no.\\d+-v.\\d+:no.\\d+)";
	// v.10:no.7-v.14
	static String patternStringVdot2 = "(v.\\d+:no.\\d+-v.\\d)";
	// v.10(1900)-v.14:no.91
	static String patternStringVdot3 = "(v.\\d+\\(.+\\)-v.\\d+:no.\\d+)";
	// v.10-v.14:no.91
	static String patternStringVdot4 = "(v.\\d+-v.\\d+:no.\\d+)";
	// v.10(1900)-v.14
	static String patternStringVdot5 = "(v.\\d+\\(.+\\)-v.\\d+)";
	// v.10-v.14
	static String patternStringVdot6 = "(v.\\d+-v.\\d+)";
	// v.10:no.7-9
	static String patternStringVdot7 = "(v.\\d+:no.\\d+-\\d+)";
	// v.10:no.7
	static String patternStringVdot8 = "(v.\\d+:no.\\d+)";
	// v.10
	static String patternStringVdot9 = "(v.\\d+)";

	// (Jan 2000-Feb 2012)
	static String patternStringMonYearMonYear = "(\\(\\w{3}\\s+\\d+-\\w{3}\\s+\\d+\\))";
	// (Jan 2000-2012)
	static String patternStringMonYearYear = "(\\(\\w{3}\\s+\\d+-\\d+\\))";
	// (2000-Feb 2012)
	static String patternStringYearMonYear = "(\\(\\d+-\\w{3}\\s+\\d+\\))";
	// (Feb 2012)
	static String patternStringMonYear = "(\\(\\w+\\s+\\d+)";
	// (2000-2012)
	static String patternStringYearYear = "(\\(\\d+-\\d+\\))";
	// (2000)
	static String patternStringYear = "(\\(\\d+\\))";

	// no.5-no.6
	static String patternStringIssueToIssue = "(no.\\d+-no.\\d+)";
	// no.5
	static String patternStringIssue = "(no.\\d+)";

	// ---------
	// v.10:no.7-v.14:no.91
	static String patternStringVdot1_ = "(v.)(\\d+)(:no.)(\\d+)(-v.)(\\d+)(:no.)(\\d+)";
	// v.10:no.7-v.14
	static String patternStringVdot2_ = "(v.)(\\d+)(:no.)(\\d+)(-v.)(\\d+)";
	// v.10(1900)-v.14:no.91
	static String patternStringVdot3_ = "(v.)(\\d+)(\\(.+\\))(-v.)(\\d+)(:no.)(\\d+)";
	// v.10-v.14:no.91
	static String patternStringVdot4_ = "(v.)(\\d+)(-v.)(\\d+)(:no.)(\\d+)";
	// v.10(1900)-v.14
	static String patternStringVdot5_ = "(v.)(\\d+)(\\(.+\\))(-v.)(\\d+)";
	// v.10-v.14
	static String patternStringVdot6_ = "(v.)(\\d+)(-v.)(\\d+)";
	// v.10:no.7-9
	static String patternStringVdot7_ = "(v.)(\\d+)(:no.)(\\d+)(-)(\\d+)";
	// v.10:no.7
	static String patternStringVdot8_ = "(v.)(\\d+)(:no.)(\\d+)";
	// v.10
	static String patternStringVdot9_ = "(v.)(\\d+)";

	// (Jan 2000-Feb 2012)
	static String patternStringMonYearMonYear_ = "(\\()(\\w{3})(\\s+)(\\d+)(-)(\\w+)(\\s+)(\\d+)(\\))";
	// (Jan 2000-2012)
	static String patternStringMonYearYear_ = "(\\()(\\w{3})(\\s+)(\\d+)(-)(\\d+)(\\))";
	// (2000-Feb 2012)
	static String patternStringYearMonYear_ = "(\\()(\\d+)(-)(\\w{3})(\\s+)(\\d+)(\\))";
	// (Feb 2012)
	static String patternStringMonYear_ = "(\\()(\\w+)(\\s+)(\\d+)";
	// (2000-2012)
	static String patternStringYearYear_ = "(\\()(\\d+)(-)(\\d+)(\\))";
	// (2000)
	static String patternStringYear_ = "(\\()(\\d+)(\\))";

	// no.5-no.6
	static String patternStringIssueToIssue_ = "(no.)(\\d+)(-no.)(\\d+)";
	// no.5
	static String patternStringIssue_ = "(no.)(\\d+)";

	// -----------
	static Pattern patternVdot1 = Pattern.compile(patternStringVdot1);
	static Pattern patternVdot2 = Pattern.compile(patternStringVdot2);
	static Pattern patternVdot3 = Pattern.compile(patternStringVdot3);
	static Pattern patternVdot4 = Pattern.compile(patternStringVdot4);
	static Pattern patternVdot5 = Pattern.compile(patternStringVdot5);
	static Pattern patternVdot6 = Pattern.compile(patternStringVdot6);
	static Pattern patternVdot7 = Pattern.compile(patternStringVdot7);
	static Pattern patternVdot8 = Pattern.compile(patternStringVdot8);
	static Pattern patternVdot9 = Pattern.compile(patternStringVdot9);

	static Pattern patternMonYaerMonYear = Pattern.compile(patternStringMonYearMonYear);
	static Pattern patternMonYearYear = Pattern.compile(patternStringMonYearYear);
	static Pattern patternYearMonYear = Pattern.compile(patternStringYearMonYear);
	static Pattern patternMonYear = Pattern.compile(patternStringMonYear);
	static Pattern patternYearYear = Pattern.compile(patternStringYearYear);
	static Pattern patternYear = Pattern.compile(patternStringYear);

	static Pattern patternIssueToIssue = Pattern.compile(patternStringIssueToIssue);
	static Pattern patternIssue = Pattern.compile(patternStringIssue);

	// ------------
	static Pattern patternVdot1_ = Pattern.compile(patternStringVdot1_);
	static Pattern patternVdot2_ = Pattern.compile(patternStringVdot2_);
	static Pattern patternVdot3_ = Pattern.compile(patternStringVdot3_);
	static Pattern patternVdot4_ = Pattern.compile(patternStringVdot4_);
	static Pattern patternVdot5_ = Pattern.compile(patternStringVdot5_);
	static Pattern patternVdot6_ = Pattern.compile(patternStringVdot6_);
	static Pattern patternVdot7_ = Pattern.compile(patternStringVdot7_);
	static Pattern patternVdot8_ = Pattern.compile(patternStringVdot8_);
	static Pattern patternVdot9_ = Pattern.compile(patternStringVdot9_);

	static Pattern patternMonYaerMonYear_ = Pattern.compile(patternStringMonYearMonYear_);
	static Pattern patternMonYearYear_ = Pattern.compile(patternStringMonYearYear_);
	static Pattern patternYearMonYear_ = Pattern.compile(patternStringYearMonYear_);
	static Pattern patternMonYear_ = Pattern.compile(patternStringMonYear_);
	static Pattern patternYearYear_ = Pattern.compile(patternStringYearYear_);
	static Pattern patternYear_ = Pattern.compile(patternStringYear_);

	static Pattern patternIssueToIssue_ = Pattern.compile(patternStringIssueToIssue_);
	static Pattern patternIssue_ = Pattern.compile(patternStringIssue_);

	// static Pattern patternYear = Pattern.compile(patternStringYear);

	public static void processPAPR(IhsIngestionRecord ihsIngestionRecord, IhsUser ihsUser, IhsMember ihsMember,
			IhsLocation ihsLocation, Scommitment scommitment) {

		// System.out.println("" + ihsIngestionRecord.rawRecordData);
		List<String> errorString = new ArrayList<String>();

		String jsonString = null;
		BaseData paprData = null;

		SingestionExceptionStatus singestionExceptionStatus = SingestionExceptionStatus.find.where()
				.eq("name", SingestionExceptionStatus.Available).findUnique();

		try {
			paprData = BaseData.buildPAPRData(ihsIngestionRecord.rawRecordData);

		} catch (Exception e) {

			SingestionRecordStatus singestionRecordStatusBad = SingestionRecordStatus.find.where()
					.eq("name", SingestionRecordStatus.BadRecordError).findUnique();
			IhsIngestionRecord.updateStatusDetail(ihsIngestionRecord, singestionRecordStatusBad, "", "", "");

			Logger.error("processPortico() validation:" + SingestionRecordStatus.BadRecordError);
			return;
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(paprData);

		} catch (JsonProcessingException e) {
			Logger.error("", e);
			return;
		}

		if (dataValidateAndInsert(paprData, ihsIngestionRecord, ihsUser, ihsMember, ihsLocation, errorString,
				scommitment)) {

			SingestionRecordStatus singestionRecordStatusAvailable = SingestionRecordStatus.find.where()
					.eq("name", SingestionRecordStatus.Available).findUnique();

			IhsIngestionRecord.updateStatusDetail(ihsIngestionRecord, singestionRecordStatusAvailable, jsonString,
					paprData.title,
					errorString.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", ""));

			Logger.debug("########FAILURE#############" + jsonString);

		} else {
			SingestionRecordStatus singestionRecordStatusComplete = SingestionRecordStatus.find.where()
					.eq("name", SingestionRecordStatus.Complete).findUnique();
			IhsIngestionRecord.updateStatusDetail(ihsIngestionRecord, singestionRecordStatusComplete, jsonString,
					paprData.title, "");
			Logger.debug("#################SUCCESS#### " + jsonString);
		}

	}

	public static void processPapresolve(String username, BaseData porticoData, IhsIngestionRecord ihsIngestionRecord,
			String jsonString, Scommitment scommitment) {

		AppUser appUser = (AppUser) Cache.get(username);

		IhsUser ihsUser = IhsUser.find.byId(appUser.userId);
		IhsMember ihsMember = IhsMember.find.byId(appUser.memberId);
		IhsLocation ihsLocation = IhsLocation.find.byId(appUser.locatoinId);

		List<String> errorString = new ArrayList<String>();

		if (dataValidateAndInsert(porticoData, ihsIngestionRecord, ihsUser, ihsMember, ihsLocation, errorString,
				scommitment)) {

			SingestionRecordStatus singestionRecordStatusAvailable = SingestionRecordStatus.find.where()
					.eq("name", SingestionRecordStatus.Available).findUnique();

			IhsIngestionRecord.updateStatusDetail(ihsIngestionRecord, singestionRecordStatusAvailable, jsonString,
					porticoData.title,
					errorString.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", ""));

			Logger.debug("########FAILURE#############" + jsonString);

		} else {
			SingestionRecordStatus singestionRecordStatusComplete = SingestionRecordStatus.find.where()
					.eq("name", SingestionRecordStatus.Complete).findUnique();
			IhsIngestionRecord.updateStatusDetail(ihsIngestionRecord, singestionRecordStatusComplete, jsonString,
					porticoData.title, "");
			Logger.debug("#################SUCCESS#### " + jsonString);
		}

	}

	public static boolean dataValidateAndInsert(BaseData paprData, IhsIngestionRecord ihsIngestionRecord,
			IhsUser ihsUser, IhsMember ihsMember, IhsLocation ihsLocation, List<String> errorString,
			Scommitment scommitment) {

		boolean error = false;
		SingestionExceptionStatus singestionExceptionStatus = SingestionExceptionStatus.find.where()
				.eq("name", SingestionExceptionStatus.Available).findUnique();

		if (!paprData.printISSN.trim().equals("") && paprData.printISSN.replace("-", "").length() != 8) {

			error = true;
			SingestionExceptionType singestionExceptionType = SingestionExceptionType.find.where()
					.eq("name", SingestionExceptionType.BadPrintISSN).findUnique();
			new IhsIngestionException(ihsIngestionRecord, singestionExceptionType, paprData.title, "",
					singestionExceptionStatus, ihsUser).save();
			Logger.info("dataValidateAndInsert() validation:" + SingestionExceptionType.BadPrintISSN + ":"
					+ paprData.printISSN);
			errorString.add(singestionExceptionType.description + "|");
		}

		List<PaprVolume> volumes = new ArrayList<PaprVolume>();
		List<PaprIssue> issues = new ArrayList<PaprIssue>();
		List<PaprYear> years = new ArrayList<PaprYear>();
		List<PaprYear> yearsOverRide = new ArrayList<PaprYear>();

		String[] volumsissues = paprData.holding.split(",");

		try {
			for (String volumsissue : volumsissues) {

				if (volumsissue.toLowerCase().contains("supplement")) {

					// TODO
					continue;

				}

				if (volumsissue.toLowerCase().contains("index")) {

					// TODO
					continue;

				}

				// v.10:no.7-v.14:no.91
				// static String patternStringVdot1_ =
				// "(v.)(\\d+)(:no.)(\\d+)(-v.)(\\d+)(:no.)(\\d+)";
				Matcher matcher = patternVdot1.matcher(volumsissue);

				if (matcher.find()) {

					Matcher matcher1 = patternVdot1_.matcher(matcher.group(1));

					matcher1.find();

					PaprVolume paprVolume = new PaprVolume();
					paprVolume.startVolume = Integer.parseInt(matcher1.group(2));
					paprVolume.startIssue = Integer.parseInt(matcher1.group(4));
					paprVolume.endVolume = Integer.parseInt(matcher1.group(6));
					paprVolume.endIssue = Integer.parseInt(matcher1.group(8));

					volumes.add(paprVolume);

					continue;

				}

				// v.10:no.7-v.14
				// static String patternStringVdot2_ =
				// "(v.)(\\d+)(:no.)(\\d+)(-v.)(\\d+)";
				matcher = patternVdot2.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternVdot2_.matcher(matcher.group(1));

					matcher1.find();

					PaprVolume paprVolume = new PaprVolume();
					paprVolume.startVolume = Integer.parseInt(matcher1.group(2));
					paprVolume.startIssue = Integer.parseInt(matcher1.group(4));
					paprVolume.endVolume = Integer.parseInt(matcher1.group(6));

					volumes.add(paprVolume);

					continue;
				}

				// v.10(1900)-v.14:no.91
				// static String patternStringVdot3_ =
				// "(v.)(\\d+)(\\(.+\\))(-v.)(\\d+)(:no.)(\\d+)";
				matcher = patternVdot3.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternVdot3_.matcher(matcher.group(1));

					matcher1.find();

					PaprVolume paprVolume = new PaprVolume();
					paprVolume.startVolume = Integer.parseInt(matcher1.group(2));
					paprVolume.endVolume = Integer.parseInt(matcher1.group(5));
					paprVolume.endIssue = Integer.parseInt(matcher1.group(7));

					volumes.add(paprVolume);

					continue;
				}

				// v.10-v.14:no.91
				// static String patternStringVdot4_ =
				// "(v.)(\\d+)(-v.)(\\d+)(:no.)(\\d+)";
				matcher = patternVdot4.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternVdot4_.matcher(matcher.group(1));

					matcher1.find();

					PaprVolume paprVolume = new PaprVolume();
					paprVolume.startVolume = Integer.parseInt(matcher1.group(2));
					paprVolume.endVolume = Integer.parseInt(matcher1.group(4));
					paprVolume.endIssue = Integer.parseInt(matcher1.group(6));
					volumes.add(paprVolume);

					continue;
				}

				// v.10(1900)-v.14
				// static String patternStringVdot5_ =
				// "(v.)(\\d+)(\\(.+\\))(-v.)(\\d+)";

				matcher = patternVdot5.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternVdot5_.matcher(matcher.group(1));

					matcher1.find();

					PaprVolume paprVolume = new PaprVolume();
					paprVolume.startVolume = Integer.parseInt(matcher1.group(2));
					paprVolume.endVolume = Integer.parseInt(matcher1.group(5));
					volumes.add(paprVolume);

					continue;
				}

				// v.10-v.14
				// static String patternStringVdot6_ = "(v.)(\\d+)(-v.)(\\d+)";
				matcher = patternVdot6.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternVdot6_.matcher(matcher.group(1));

					matcher1.find();

					PaprVolume paprVolume = new PaprVolume();
					paprVolume.startVolume = Integer.parseInt(matcher1.group(2));
					paprVolume.endVolume = Integer.parseInt(matcher1.group(4));
					volumes.add(paprVolume);

					continue;
				}

				// v.10:no.7-9
				// static String patternStringVdot7_ =
				// "(v.)(\\d+)(:no.)(\\d+)(-)(\\d+)";
				matcher = patternVdot7.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternVdot7_.matcher(matcher.group(1));

					matcher1.find();

					PaprVolume paprVolume = new PaprVolume();
					paprVolume.startVolume = Integer.parseInt(matcher1.group(2));
					paprVolume.startIssue = Integer.parseInt(matcher1.group(4));
					paprVolume.endIssue = Integer.parseInt(matcher1.group(6));
					volumes.add(paprVolume);

					continue;
				}

				// v.10:no.7
				// static String patternStringVdot8_ = "(v.)(\\d+)(:no.)(\\d+)";

				matcher = patternVdot8.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternVdot8_.matcher(matcher.group(1));

					matcher1.find();

					PaprVolume paprVolume = new PaprVolume();
					paprVolume.startVolume = Integer.parseInt(matcher1.group(2));
					paprVolume.startIssue = Integer.parseInt(matcher1.group(4));
					volumes.add(paprVolume);

					continue;
				}

				// v.10
				// static String patternStringVdot9_ = "(v.)(\\d+)";
				matcher = patternVdot9.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternVdot9_.matcher(matcher.group(1));

					matcher1.find();

					PaprVolume paprVolume = new PaprVolume();
					paprVolume.startVolume = Integer.parseInt(matcher1.group(2));
					volumes.add(paprVolume);

					continue;
				}

				// (Jan 2000-Feb 2012)
				// String patternStringMonYearMonYear_ =
				// "(\\()(\\w+)(\\s+)(\\d+)(-)(\\w+)(\\s+)(\\d+)(\\))";
				matcher = patternMonYaerMonYear.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternMonYaerMonYear_.matcher(matcher.group(1));

					matcher1.find();

					PaprYear paprYear = new PaprYear();
					Helper.getMonthIndex(matcher1.group(2));
					paprYear.beginMonth = Helper.getMonthIndex(matcher1.group(2));
					paprYear.beginYear = Integer.parseInt(matcher1.group(4));
					paprYear.endMonth = Helper.getMonthIndex(matcher1.group(6));
					paprYear.endYear = Integer.parseInt(matcher1.group(8));
					years.add(paprYear);

					continue;
				}

				// (Jan 2000-2012)
				// static String patternStringMonYearYear_ =
				// "(\\()(\\w+)(\\s+)(\\d+)(-)(\\d+)(\\))";
				matcher = patternMonYearYear.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternMonYearYear_.matcher(matcher.group(1));

					matcher1.find();

					PaprYear paprYear = new PaprYear();
					paprYear.beginMonth = Helper.getMonthIndex(matcher1.group(2));
					paprYear.beginYear = Integer.parseInt(matcher1.group(4));
					paprYear.endYear = Integer.parseInt(matcher1.group(6));
					years.add(paprYear);

					continue;
				}
				// (2000-Feb 2012)
				// static String patternStringYearMonYear_
				// ="(\\()(\\d+)(-)(\\w+)(\\s+)(\\d+)(\\))";

				matcher = patternYearMonYear.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternYearMonYear_.matcher(matcher.group(1));

					matcher1.find();

					PaprYear paprYear = new PaprYear();
					paprYear.beginYear = Integer.parseInt(matcher1.group(2));
					paprYear.endMonth = Helper.getMonthIndex(matcher1.group(4));
					paprYear.endYear = Integer.parseInt(matcher1.group(6));
					years.add(paprYear);

					continue;
				}
				// (Feb 2012)
				// static String patternStringMonYear_ =
				// "(\\()(\\w+)(\\s+)(\\d+)";
				matcher = patternMonYear.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternMonYear_.matcher(matcher.group(1));

					matcher1.find();

					if (Helper.isMonth(matcher1.group(2))) {
						PaprYear paprYear = new PaprYear();
						paprYear.beginMonth = Helper.getMonthIndex(matcher1.group(2));
						paprYear.beginYear = Integer.parseInt(matcher1.group(4));
						years.add(paprYear);
					}else {

						PaprYear paprYear = new PaprYear();
						paprYear.dateVal = matcher1.group(2);
						paprYear.beginYear = Integer.parseInt(matcher1.group(4));

						yearsOverRide.add(paprYear);
					}
					continue;
				}

				// (2000-2012)
				// static String patternStringYearYear_ =
				// "(\\()(\\d+)(-)(\\d+)(\\))";

				matcher = patternYearYear.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternYearYear_.matcher(matcher.group(1));

					matcher1.find();

					PaprYear paprYear = new PaprYear();
					paprYear.beginYear = Integer.parseInt(matcher1.group(2));
					paprYear.endYear = Integer.parseInt(matcher1.group(4));

					years.add(paprYear);

					continue;
				}


				// (2000)
				// static String patternStringYear_ = "(\\()(\\d+)(\\))";

				matcher = patternYear.matcher(volumsissue);

				if (matcher.find() && volumsissue.length() == 6) {
					Matcher matcher1 = patternYear_.matcher(matcher.group(1));

					matcher1.find();

					PaprYear paprYear = new PaprYear();
					paprYear.beginYear = Integer.parseInt(matcher1.group(2));
					years.add(paprYear);

					continue;
				}

				// no.5-no.6
				// static String patternStringIssueToIssue_ =
				// "(no.)(\\d+)(-no.)(\\d+)";
				matcher = patternIssueToIssue.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternIssueToIssue_.matcher(matcher.group(1));

					matcher1.find();

					PaprIssue paprIssue = new PaprIssue();
					paprIssue.beginIssue = Integer.parseInt(matcher1.group(2));
					paprIssue.endIssue = Integer.parseInt(matcher1.group(4));
					issues.add(paprIssue);

					continue;
				}

				// no.5
				// static String patternStringIssue_ = "(no.)(\\d+)";
				matcher = patternIssue.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternIssue_.matcher(matcher.group(1));

					matcher1.find();

					PaprIssue paprIssue = new PaprIssue();
					paprIssue.beginIssue = Integer.parseInt(matcher1.group(2));
					issues.add(paprIssue);

					continue;
				}
			}
		} catch (Exception e) {
			error = true;

			SingestionExceptionType singestionExceptionType = SingestionExceptionType.find.where()
					.eq("name", SingestionExceptionType.BadHoldingFormat).findUnique();

			new IhsIngestionException(ihsIngestionRecord, singestionExceptionType, paprData.title, "",
					singestionExceptionStatus, ihsUser).save();

			Logger.info("dataValidateAndInsert() validation:" + SingestionExceptionType.BadHoldingFormat + ":"
					+ paprData.holding);
			errorString.add(singestionExceptionType.description + "|");
		}

		if (volumes.size() == 0 && years.size() == 0 && yearsOverRide.size() == 0) {

			error = true;

			SingestionExceptionType singestionExceptionType = SingestionExceptionType.find.where()
					.eq("name", SingestionExceptionType.BadHoldingFormat).findUnique();

			new IhsIngestionException(ihsIngestionRecord, singestionExceptionType, paprData.title, "",
					singestionExceptionStatus, ihsUser).save();

			Logger.info("dataValidateAndInsert() validation:" + SingestionExceptionType.BadHoldingFormat + ":"
					+ paprData.holding);
			errorString.add(singestionExceptionType.description + "|");
		}

		if (error) {
			return error;
		}

		IhsTitle ihsTitle = IhsTitle.find.fetch("ihsPublicationRange").where().eq("printISSN", paprData.printISSN)
				.findUnique();

		if (ihsTitle == null) {

			ihsTitle = IhsTitle.find.fetch("ihsPublicationRange").where().eq("oclcNumber", paprData.oclcNumber)
					.findUnique();

			if (ihsTitle == null) {
				error = true;
				SingestionExceptionType singestionExceptionType = SingestionExceptionType.find.where()
						.eq("name", SingestionExceptionType.NoAuthorizedTitle).findUnique();

				new IhsIngestionException(ihsIngestionRecord, singestionExceptionType, paprData.title, "",
						singestionExceptionStatus, ihsUser).save();

				Logger.info("dataValidateAndInsert() validation:" + SingestionExceptionType.NoAuthorizedTitle);
				errorString.add(singestionExceptionType.description + "|");
				return error;
			}
		}

		Hashtable<String, List<IhsIssue>> ihsIssues = new Hashtable<String, List<IhsIssue>>();

		for (IhsVolume ihsVol : ihsTitle.ihsVolume) {

			ihsIssues.put(ihsVol.volumeNumber, ihsVol.ihsissues);

		}

		Hashtable<String, List<IhsIssue>> ihsIssuesYear = new Hashtable<String, List<IhsIssue>>();

		for (IhsVolume ihsVol : ihsTitle.ihsVolume) {

			try {
				ihsIssuesYear.put(new Integer(ihsVol.startDate.getYear()).toString(), ihsVol.ihsissues);
			} catch (Exception e) {
				System.out.println(" not");
			}

		}

		// Check Holding
		if (volumes.size() > 0) {
			if (processVolumes(ihsIssues, volumes, ihsMember, ihsLocation, scommitment)) {
				error = true;
				SingestionExceptionType singestionExceptionType = SingestionExceptionType.find.where()
						.eq("name", SingestionExceptionType.MissingAuthorizedVolume).findUnique();

				new IhsIngestionException(ihsIngestionRecord, singestionExceptionType, paprData.title, "",
						singestionExceptionStatus, ihsUser).save();

				Logger.info("dataValidateAndInsert() validation:" + SingestionExceptionType.MissingAuthorizedVolume);
				errorString.add(singestionExceptionType.description + "|");
				return error;
			}
		}

		if (years.size() > 0) {
			if (processYears(ihsIssuesYear, years, ihsMember, ihsLocation, scommitment)) {
				error = true;
				SingestionExceptionType singestionExceptionType = SingestionExceptionType.find.where()
						.eq("name", SingestionExceptionType.MissingAuthorizedVolume).findUnique();

				new IhsIngestionException(ihsIngestionRecord, singestionExceptionType, paprData.title, "",
						singestionExceptionStatus, ihsUser).save();

				Logger.info("dataValidateAndInsert() validation:" + SingestionExceptionType.MissingAuthorizedVolume);
				errorString.add(singestionExceptionType.description + "|");
				return error;
			}
		}

		if(yearsOverRide.size() > 0){
			if (processYearsOverride(ihsIssuesYear, yearsOverRide, ihsMember, ihsLocation, scommitment)) {
				error = true;
				SingestionExceptionType singestionExceptionType = SingestionExceptionType.find.where()
						.eq("name", SingestionExceptionType.MissingAuthorizedVolume).findUnique();

				new IhsIngestionException(ihsIngestionRecord, singestionExceptionType, paprData.title, "",
						singestionExceptionStatus, ihsUser).save();

				Logger.info("dataValidateAndInsert() validation:" + SingestionExceptionType.MissingAuthorizedVolume);
				errorString.add(singestionExceptionType.description + "|");
				return error;
			}

		}

		// Check Holding

		return false;
	}

	static boolean processVolumes(Hashtable<String, List<IhsIssue>> ihsIssues, List<PaprVolume> volumes,
			IhsMember ihsMember, IhsLocation ihsLocation, Scommitment scommitment) {

		boolean error = false;
		for (PaprVolume volume : volumes) {

			if (volume.startVolume == -1) {

				if (volume.endIssue < -1) {

					try {
						List<IhsIssue> issues = ihsIssues.get(new Integer(1).toString());

						for (IhsIssue ihsIssue : issues) {

							if (new Integer(ihsIssue.issueNumber).intValue() >= volume.startIssue
									&& new Integer(ihsIssue.issueNumber).intValue() <= volume.endIssue) {

								saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);

							}
						}
					} catch (Exception e) {
						error = true;
					}

				} else {

					try {
						List<IhsIssue> issues = ihsIssues.get(new Integer(1).toString());

						for (IhsIssue ihsIssue : issues) {

							if (new Integer(ihsIssue.issueNumber).intValue() == volume.startIssue) {

								saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);

							}
						}
					} catch (Exception e) {
						error = true;
					}
				}

				break;
			}

			int volCounter = 0;

			// if we have end volume then we have run
			if (volume.endVolume > -1) {

				for (;;) {
					// handle first volume

					if (volCounter == 0) {

						volCounter = volume.startVolume;

						try {
							List<IhsIssue> issues = ihsIssues.get(new Integer(volCounter).toString());

							for (IhsIssue ihsIssue : issues) {

								if (new Integer(ihsIssue.issueNumber).intValue() >= volume.startIssue) {

									saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);

								}

							}
						} catch (Exception e) {
							error = true;
						}

					}

					// handle end Volume
					else if (volCounter >= volume.endVolume) {
						if (volume.endIssue > -1) {

							try {
								List<IhsIssue> issues = ihsIssues.get(new Integer(volCounter).toString());

								for (IhsIssue ihsIssue : issues) {

									if (new Integer(ihsIssue.issueNumber).intValue() <= volume.endIssue) {

										saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);
									}

								}
							} catch (Exception e) {
								error = true;
							}
						}

						break;
					} else {

						// handle middle volumes
						try {
							List<IhsIssue> issues = ihsIssues.get(new Integer(volCounter).toString());

							for (IhsIssue ihsIssue : issues) {

								try {
									saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);

								} catch (Exception e) {

								}

							}

						} catch (Exception e) {
							error = true;
						}
					}
					volCounter++;
				}
			} else {

				List<IhsIssue> issues = ihsIssues.get(new Integer(volume.startVolume).toString());

				try {
					for (IhsIssue ihsIssue : issues) {

						saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);

					}
				} catch (Exception e) {
					error = true;
				}

			}
		}

		return error;
	}

	static boolean processYears(Hashtable<String, List<IhsIssue>> ihsIssuesYear, List<PaprYear> years,
			IhsMember ihsMember, IhsLocation ihsLocation, Scommitment scommitment) {

		boolean error = false;
		for (PaprYear year : years) {

			int yearCounter = 0;

			// if we have end volume then we have run
			if (year.endYear > -1) {

				for (;;) {

					if (yearCounter == 0) { // handle first volume
						try {
							yearCounter = year.beginYear;

							List<IhsIssue> issues = ihsIssuesYear.get(new Integer(yearCounter).toString());

              // AJE : next line replaces Travant original 'for' block below: 2016-07-12 17:29:49
              saveHolding(issues.get(0), ihsMember, ihsLocation, scommitment); // AJE new 2016-07-12 16:43:41

							/* # AJE 2016-07-12 this 'for' block is Travant original: fails to ingest first Volume when holdings statement like "(1930-1940)"
							for (IhsIssue ihsIssue : issues) {
								if (ihsIssue.publicationDate.getMonthOfYear() >= year.beginMonth) {
									saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);
								}
							} // end for Travant original */

						} catch (Exception e) {

						}

					}

					else if (yearCounter >= year.endYear) { // handle end Volume
						if (year.endMonth > -1) {

							try {
								List<IhsIssue> issues = ihsIssuesYear.get(new Integer(yearCounter).toString());

								if (issues == null) {
									error = true;
								}

                // AJE : next line replaces Travant original 'for' block below: 2016-07-12 17:29:49
                saveHolding(issues.get( issues.size()-1 ), ihsMember, ihsLocation, scommitment); // AJE test 2016-07-12 16:53:02

							 /* # AJE 2016-07-12 this 'for' block is Travant original: fails to ingest last Volume when holdings statement like "(1930-1940)"
								for (IhsIssue ihsIssue : issues) {
									if (ihsIssue.publicationDate.getMonthOfYear() <= year.beginMonth) {
										saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);
									}
								} // end for Travant original */

							} catch (Exception e) {

							}
						}

						break;

					} else { // handle middle volumes
						try {
							List<IhsIssue> issues = ihsIssuesYear.get(new Integer(yearCounter).toString());

							if (issues == null) {
								error = true;
							}

							for (IhsIssue ihsIssue : issues) {
								try {
									saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);
								} catch (Exception e) {

								}

							}
						} catch (Exception e) {

						}
					}
					yearCounter++;
				}
			} else {

				if (yearCounter == 0) { // If Only on year
					yearCounter = year.beginYear;
				}

				List<IhsIssue> issues = ihsIssuesYear.get(new Integer(yearCounter).toString());

				if (issues == null) {
					error = true;
				} else {
					for (IhsIssue ihsIssue : issues) {
						try {
							saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);
						} catch (Exception e) {

						}
					}
				}
			}
		}

		return error;
	}
	static boolean processYearsOverride(Hashtable<String, List<IhsIssue>> ihsIssuesYear, List<PaprYear> years,
			IhsMember ihsMember, IhsLocation ihsLocation, Scommitment scommitment) {

		  boolean error = true;

		  for(PaprYear year :years){
			  List<IhsIssue> issues =  ihsIssuesYear.get(new Integer(year.beginYear).toString());
			  if(issues == null){
				  return error;
			  }
			  for(IhsIssue issue: issues){
				  if(issue.spublicationDate != null && year.dateVal.equals(issue.spublicationDate.publicationDateVal)){
					  error = false;
					  saveHolding(issue, ihsMember, ihsLocation, scommitment);
				  }
			  }
		  }
		  return error;
	}

	static void saveHolding(IhsIssue ihsIssue, IhsMember ihsMember, IhsLocation ihsLocation, Scommitment scommitment) {

		SconditionTypeOverall sconditionTypeOverall = SconditionTypeOverall.find.where()
				.eq("name", SconditionTypeOverall.Unknown).findUnique();

		SihsVarified sihsVarified = SihsVarified.find.where().eq("name", SihsVarified.NO).findUnique();

		SvalidationLevel svalidationLevel = SvalidationLevel.find.where().eq("name", SvalidationLevel.None)
				.findUnique();

		SholdingStatus sholdingStatus = SholdingStatus.find.where().eq("name", SholdingStatus.Default).findUnique();

		IhsHolding ihsHolding = IhsHolding.find.where().eq("issueID", ihsIssue.issueID)
				.eq("memberID", ihsMember.memberID).eq("locationID", ihsLocation.locationID).findUnique();

		if (ihsHolding == null) {

			new IhsHolding(ihsIssue, ihsMember, ihsLocation, sholdingStatus, sconditionTypeOverall, sihsVarified,
					svalidationLevel, scommitment).save();
		}
	}
}