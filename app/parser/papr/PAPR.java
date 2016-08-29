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

	public static String csvSplitBy = "\t"; // AJE: 2016-07-21 : fixed variable name, has nothing to do with the drugstore


	/* 
		AJE learning: WHY are there vars with similar values, named the same but with an underscore at the end?
			patternStringVdot1 and patternStringVdot1_   ?
			- base vars are used with Matcher matcher to find whether pattern exists in examined string at all
			- underscore vars are used with Matcher matcher1, to group() pieces, i.e. split them into substrings
			
		AJE 2016-07-21 reordered declarations in this file so that base var, then pattern compilation, then underscore var, then its pattern compilation are done in sections by format of holding before going on to the next (old version: declare all formats, then compile patterns, then declare format strings for underscore (group) vars, then compile those
		- 4 steps for each kind of holdings expression pattern
		
		AJE learning : Pattern.compile() means now can be used multiple times to match regex against multiple texts ; seems to be always better than Pattern.matches(text, pattern)
		
	*/	
	
	
	// v.10:no.7-v.14:no.91
	static String patternStringVdot1 = "(v.\\d+:no.\\d+-v.\\d+:no.\\d+)";
	static Pattern patternVdot1 = Pattern.compile(patternStringVdot1);
	static String patternStringVdot1_ = "(v.)(\\d+)(:no.)(\\d+)(-v.)(\\d+)(:no.)(\\d+)"; 	
	static Pattern patternVdot1_ = Pattern.compile(patternStringVdot1_);

	// v.10:no.7-v.14
	static String patternStringVdot2 = "(v.\\d+:no.\\d+-v.\\d)";
	static Pattern patternVdot2 = Pattern.compile(patternStringVdot2);
	static String patternStringVdot2_ = "(v.)(\\d+)(:no.)(\\d+)(-v.)(\\d+)";
	static Pattern patternVdot2_ = Pattern.compile(patternStringVdot2_);
	
	// v.10(1900)-v.14:no.91
	static String patternStringVdot3 = "(v.\\d+\\(.+\\)-v.\\d+:no.\\d+)";
	static Pattern patternVdot3 = Pattern.compile(patternStringVdot3);
	static String patternStringVdot3_ = "(v.)(\\d+)(\\(.+\\))(-v.)(\\d+)(:no.)(\\d+)";
	static Pattern patternVdot3_ = Pattern.compile(patternStringVdot3_);
	
	// v.10-v.14:no.91
	static String patternStringVdot4 = "(v.\\d+-v.\\d+:no.\\d+)";
	static Pattern patternVdot4 = Pattern.compile(patternStringVdot4);
	static String patternStringVdot4_ = "(v.)(\\d+)(-v.)(\\d+)(:no.)(\\d+)";
	static Pattern patternVdot4_ = Pattern.compile(patternStringVdot4_);

	// v.10(1900)-v.14
	static String patternStringVdot5 = "(v.\\d+\\(.+\\)-v.\\d+)";
	static Pattern patternVdot5 = Pattern.compile(patternStringVdot5);
	static String patternStringVdot5_ = "(v.)(\\d+)(\\(.+\\))(-v.)(\\d+)";
	static Pattern patternVdot5_ = Pattern.compile(patternStringVdot5_);

	// v.10-v.14
	static String patternStringVdot6 = "(v.\\d+-v.\\d+)";
	static Pattern patternVdot6 = Pattern.compile(patternStringVdot6);
	static String patternStringVdot6_ = "(v.)(\\d+)(-v.)(\\d+)";
	static Pattern patternVdot6_ = Pattern.compile(patternStringVdot6_);

	// v.10:no.7-9
	static String patternStringVdot7 = "(v.\\d+:no.\\d+-\\d+)";
	static Pattern patternVdot7 = Pattern.compile(patternStringVdot7);
	static String patternStringVdot7_ = "(v.)(\\d+)(:no.)(\\d+)(-)(\\d+)";	
	static Pattern patternVdot7_ = Pattern.compile(patternStringVdot7_);

	// v.10:no.7
	static String patternStringVdot8 = "(v.\\d+:no.\\d+)";
	static Pattern patternVdot8 = Pattern.compile(patternStringVdot8);
	static String patternStringVdot8_ = "(v.)(\\d+)(:no.)(\\d+)";
	static Pattern patternVdot8_ = Pattern.compile(patternStringVdot8_);

	// v.10
	static String patternStringVdot9 = "(v.\\d+)";
	static Pattern patternVdot9 = Pattern.compile(patternStringVdot9);
	static String patternStringVdot9_ = "(v.)(\\d+)";
	static Pattern patternVdot9_ = Pattern.compile(patternStringVdot9_);	
	
	
	// (Jan 2000-Feb 2012)
	static String patternStringMonYearMonYear = "(\\(\\w{3}\\s+\\d+-\\w{3}\\s+\\d+\\))";
	static Pattern patternMonYearMonYear = Pattern.compile(patternStringMonYearMonYear); 
	static String patternStringMonYearMonYear_ = "(\\()(\\w{3})(\\s+)(\\d+)(-)(\\w+)(\\s+)(\\d+)(\\))";	
	static Pattern patternMonYearMonYear_ = Pattern.compile(patternStringMonYearMonYear_);

	// (Jan 2000-2012)
	static String patternStringMonYearYear = "(\\(\\w{3}\\s+\\d+-\\d+\\))";
	static Pattern patternMonYearYear = Pattern.compile(patternStringMonYearYear);
	static String patternStringMonYearYear_ = "(\\()(\\w{3})(\\s+)(\\d+)(-)(\\d+)(\\))";	
	static Pattern patternMonYearYear_ = Pattern.compile(patternStringMonYearYear_);

	// (2000-Feb 2012)
	static String patternStringYearMonYear = "(\\(\\d+-\\w{3}\\s+\\d+\\))";
	static Pattern patternYearMonYear = Pattern.compile(patternStringYearMonYear);
	static String patternStringYearMonYear_ = "(\\()(\\d+)(-)(\\w{3})(\\s+)(\\d+)(\\))";	
	static Pattern patternYearMonYear_ = Pattern.compile(patternStringYearMonYear_);

	// (Feb 2012)
	static String patternStringMonYear = "(\\(\\w+\\s+\\d+)";
	static Pattern patternMonYear = Pattern.compile(patternStringMonYear);
	static String patternStringMonYear_ = "(\\()(\\w+)(\\s+)(\\d+)";
	static Pattern patternMonYear_ = Pattern.compile(patternStringMonYear_);
	
	// (2000-2012)
	static String patternStringYearYear = "(\\(\\d+-\\d+\\))";
	static Pattern patternYearYear = Pattern.compile(patternStringYearYear);
	static String patternStringYearYear_ = "(\\()(\\d+)(-)(\\d+)(\\))";
	static Pattern patternYearYear_ = Pattern.compile(patternStringYearYear_);

	// (2000)
	static String patternStringYear = "(\\(\\d+\\))";
	static Pattern patternYear = Pattern.compile(patternStringYear);
	static String patternStringYear_ = "(\\()(\\d+)(\\))";
	static Pattern patternYear_ = Pattern.compile(patternStringYear_);

	// no.5-no.6
	static String patternStringIssueToIssue = "(no.\\d+-no.\\d+)";
	static Pattern patternIssueToIssue = Pattern.compile(patternStringIssueToIssue);
	static String patternStringIssueToIssue_ = "(no.)(\\d+)(-no.)(\\d+)";
	static Pattern patternIssueToIssue_ = Pattern.compile(patternStringIssueToIssue_);

	// no.5
	static String patternStringIssue = "(no.\\d+)";
	static Pattern patternIssue = Pattern.compile(patternStringIssue);
	static String patternStringIssue_ = "(no.)(\\d+)";
	static Pattern patternIssue_ = Pattern.compile(patternStringIssue_);

	// static Pattern patternYear = Pattern.compile(patternStringYear); // AJE 2016-07-21 commented out in Travant original

	/*********************************************************************
	AJE 2016-07-21 any String / Pattern after this has been added by CRL 

	AJE 2016-07-21 end Travant original group pattern declarations; 
	begin CRL pattern declarations 
	
	helpful: 	http://tutorials.jenkov.com/java-regex/syntax.html
	****************/	

	/* after Reza example: 	
	// v.10-v.14
	static String patternStringVdot6 = "(v.\\d+-v.\\d+)";
	static Pattern patternVdot6 = Pattern.compile(patternStringVdot6);
	static String patternStringVdot6_ = "(v.)(\\d+)(-v.)(\\d+)";
	static Pattern patternVdot6_ = Pattern.compile(patternStringVdot6_);
	
	and 
	
	// (2000-2012)
	static String patternStringYearYear = "(\\(\\d+-\\d+\\))";
	static Pattern patternYearYear = Pattern.compile(patternStringYearYear);
	static String patternStringYearYear_ = "(\\()(\\d+)(-)(\\d+)(\\))";
	static Pattern patternYearYear_ = Pattern.compile(patternStringYearYear_);	

	CRL target data from 'nui_holdings_test.csv', modified by AJE as 'nui_AJE_1.csv'
	// v.1-v.21 (1966-1986)
	Social psychiatry	0037-7813	Springer-Verlag	1765687	*/
	static String patternString_CRL_test1 = "(v.\\d+-v.\\d+\\s+\\(\\d+-\\d+\\))";
	static Pattern pattern_CRL_test1 = Pattern.compile(patternString_CRL_test1);
	//static String patternString_CRL_test1_groups =  "(v.)(\\d+)(-v.)(\\d+)\\s+\\((\\d+)(-)((\\d+)\\))"; 
	static String patternString_CRL_test1_groups =  "(v.)(\\d+)(-v.)(\\d+)(\\s+\\()(\\d+)(-)(\\d+)\\)"; 
	// 8 grps
	static Pattern pattern_CRL_test1_groups = Pattern.compile(patternString_CRL_test1_groups);	
	
	/* CRL target data from 'ipl_AJE_1.csv'
	// v.41:no.3/4 (2007)
	Few-body systems	0177-7963	Springer-Verlag	13446320 	*/
	static String patternString_CRL_test2 = "(v.\\d+:no.\\d+/\\d+\\s+\\(\\d+\\))";
	static Pattern pattern_CRL_test2 = Pattern.compile(patternString_CRL_test2);
	static String patternString_CRL_test2_groups =  "(v.)(\\d+)(:no.)(\\d+)(/)(\\d+)(\\s+\\()(\\d+)(\\))"; 
	static Pattern pattern_CRL_test2_groups = Pattern.compile(patternString_CRL_test2_groups);
	/* 		
		AJE 2016-07-21 end CRL pattern declarations
	****************/	



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

			//Logger.error("processPortico() validation:" + SingestionRecordStatus.BadRecordError); // AJE 2016-07-21 Travant original
			//Logger.error("processPAPR() validation:" + SingestionRecordStatus.BadRecordError); // AJE 2016-07-21 I think this was meant
			Logger.info("processPAPR() validation: Exception is: " + e 
			+ "; and SingestionRecordStatus.BadRecordError is: " +SingestionRecordStatus.BadRecordError); // AJE 2016-08-01

			return;
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(paprData);

		} catch (JsonProcessingException e) {
			Logger.error("", e);
			Logger.info("processPAPR() ObjectMapper and jsonString block has JsonProcessingException: " +e);
			return;
		}

		/* AJE learning: dataValidateAndInsert() returns false if it gets to the end of its processing; 
				if it gets short-circuited, dataValidateAndInsert() returns the variable 'error' set to 'true'
		*/
		if (dataValidateAndInsert(paprData, ihsIngestionRecord, ihsUser, ihsMember, ihsLocation, errorString,
				scommitment)) {

			SingestionRecordStatus singestionRecordStatusAvailable = SingestionRecordStatus.find.where()
					.eq("name", SingestionRecordStatus.Available).findUnique();

			IhsIngestionRecord.updateStatusDetail(ihsIngestionRecord, singestionRecordStatusAvailable, jsonString,
					paprData.title,
					errorString.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", ""));

			Logger.debug("########FAILURE#############" + jsonString);
			
			Logger.info("########FAILURE#############" + jsonString); // AJE 2016-08-02

		} else {
			SingestionRecordStatus singestionRecordStatusComplete = SingestionRecordStatus.find.where()
					.eq("name", SingestionRecordStatus.Complete).findUnique();
			IhsIngestionRecord.updateStatusDetail(ihsIngestionRecord, singestionRecordStatusComplete, jsonString,
					paprData.title, "");
			Logger.debug("#################SUCCESS#### " + jsonString);

			Logger.info("#################SUCCESS#### " + jsonString); // AJE 2016-08-02
			
		}

	} // end processPAPR



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

	} // end processPapresolve


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
			Logger.info("dataValidateAndInsert() validation, BadPrintISSN:" + SingestionExceptionType.BadPrintISSN + ":"
					+ paprData.printISSN);
			errorString.add(singestionExceptionType.description + "|");
		}

		List<PaprVolume> volumes = new ArrayList<PaprVolume>();
		List<PaprIssue> issues = new ArrayList<PaprIssue>();
		List<PaprYear> years = new ArrayList<PaprYear>();
		List<PaprYear> yearsOverRide = new ArrayList<PaprYear>();

		String[] volumsissues = paprData.holding.split(",");
		
		
		// AJE 2016-07-27 examine input, and what is each piece of volumsissues ?
		//Logger.info("AJE 2016-07-27 what is paprData ? : '" + paprData + "'"); // just some ? object reference, like 'parser.BaseData@c554dbb'
		Logger.info("AJE 2016-07-27 what is paprData.holding ? : '" + paprData.holding + "'");
		Integer junkateria = 1;
		for (String volumsissue : volumsissues) {
			Logger.info("AJE 2016-07-27 junkateria=" +junkateria+ ") volumsissue = '" + volumsissue + "'");
			junkateria++;
		} 
		// end AJE 2016-07-27 what is each piece of volumsissues ?



		try {
			
			for (String volumsissue : volumsissues) {
				if (volumsissue.toLowerCase().contains("supplement")) {
					// TODO // AJE 2016-07-21 this is Travant original, 'TODO' what? [1 of N]
					Logger.info("dataValidateAndInsert() validation: '"+volumsissue+"' has 'supplement', \n which is unhandled in Travant original; appears to cause error 'BadHoldingFormat', as of 2016-08-02");
					continue;
				}

				if (volumsissue.toLowerCase().contains("index")) {
					// TODO // AJE 2016-07-21 this is Travant original, 'TODO' what? [2 of N]
					Logger.info("dataValidateAndInsert() validation: '"+volumsissue+"' has 'index', \n which is unhandled in Travant original; appears to cause error 'BadHoldingFormat', as of 2016-08-02");
					continue;
				}


				// v.10:no.7-v.14:no.91
				// static String patternStringVdot1_ =
				// "(v.)(\\d+)(:no.)(\\d+)(-v.)(\\d+)(:no.)(\\d+)";
				Matcher matcher = patternVdot1.matcher(volumsissue);

				if (matcher.find()) {

					Matcher matcher1 = patternVdot1_.matcher(matcher.group(1));

					matcher1.find(); // AJE learning: m1.find() can get substrings, but m1.match() would only match whole string ... ?

					PaprVolume paprVolume = new PaprVolume();
					paprVolume.startVolume = Integer.parseInt(matcher1.group(2)); // AJE learning: m1.group() extracts pieces of the regex
					paprVolume.startIssue = Integer.parseInt(matcher1.group(4));
					paprVolume.endVolume = Integer.parseInt(matcher1.group(6));
					paprVolume.endIssue = Integer.parseInt(matcher1.group(8));

					volumes.add(paprVolume);

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternVdot1 = '"+patternVdot1+"'.");

					continue;

				}


				
				/*****************************************************
				 AJE 2016-07-22 BEGIN CRL PATTERN MATCHING AND HANDLING 
				 AJE 2016-08-01 whatever I do sppears to be actually causing other errors ; 
					the file "PAPR.java_REINSTATE_CRL_PATTERNS" contains my pattern catching code, which has been removed from where it was here.

				 AJE 2016-07-22 END CRL PATTERN MATCHING AND HANDLING 
				*****************************************************/

				

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

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternVdot2 = '"+patternVdot2+"'.");

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

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternVdot3 = '"+patternVdot3+"'.");

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

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternVdot4 = '"+patternVdot4+"'.");

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

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternVdot5 = '"+patternVdot5+"'.");

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

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternVdot6 = '"+patternVdot6+"'.");

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

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternVdot7 = '"+patternVdot7+"'.");

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

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternVdot8 = '"+patternVdot8+"'.");

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

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternVdot9 = '"+patternVdot9+"'.");

					continue;
				}

				// (Jan 2000-Feb 2012)
				// String patternStringMonYearMonYear_ =
				// "(\\()(\\w+)(\\s+)(\\d+)(-)(\\w+)(\\s+)(\\d+)(\\))";
				matcher = patternMonYearMonYear.matcher(volumsissue);

				if (matcher.find()) {
					Matcher matcher1 = patternMonYearMonYear_.matcher(matcher.group(1));

					matcher1.find();

					PaprYear paprYear = new PaprYear();
					Helper.getMonthIndex(matcher1.group(2));  // AJE 2016-08-01 DEVNOTE: why is this here? whatever it returns is not saved into var?
					paprYear.beginMonth = Helper.getMonthIndex(matcher1.group(2));
					paprYear.beginYear = Integer.parseInt(matcher1.group(4));
					paprYear.endMonth = Helper.getMonthIndex(matcher1.group(6));
					paprYear.endYear = Integer.parseInt(matcher1.group(8));
					years.add(paprYear);

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternMonYearMonYear = '"+patternMonYearMonYear+"'.");

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

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternMonYearYear = '"+patternMonYearYear+"'.");

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

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternYearMonYear = '"+patternYearMonYear+"'.");

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

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternMonYear = '"+patternMonYear+"'.");

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
					
					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternYearYear = '"+patternYearYear+"'.");

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
					
					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternYear = '"+patternYear+"'.");

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

					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternIssueToIssue = '"+patternIssueToIssue+"'.");

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
					
					Logger.info("dataValidateAndInsert(): volumsissue '"+volumsissue+"' matched Travant patternIssue = '"+patternIssue+"'.");

					continue;
				}
			} // end for (String volumsissue : volumsissues)
		} catch (Exception e) { // giant try begins at about line 343
			
			Logger.info("dataValidateAndInsert() validation: there was an exception in the pattern-matching: "+e+"'.");
			
			error = true;

			SingestionExceptionType singestionExceptionType = SingestionExceptionType.find.where()
					.eq("name", SingestionExceptionType.BadHoldingFormat).findUnique();

			new IhsIngestionException(ihsIngestionRecord, singestionExceptionType, paprData.title, "",
					singestionExceptionStatus, ihsUser).save();

			/* AJE 2016-07-27 13:36 Travant original Logger message, expanded below: 
			Logger.info("dataValidateAndInsert() validation:" + SingestionExceptionType.BadHoldingFormat + ":"
					+ paprData.holding);			*/
			Logger.info("dataValidateAndInsert() finished validation, catch block:" + SingestionExceptionType.BadHoldingFormat 
					+ " ; paprData.holding: '" + paprData.holding 
					+"' ; siET.desc. = '" +singestionExceptionType.description 
					+"' ; the actual Exception is: " +e ); 
			// AJE 2016-07-27 13:37 expand the error message, just this line changed (1 of 2 versions)

			errorString.add(singestionExceptionType.description + "|");
		}

		if (volumes.size() == 0 && years.size() == 0 && yearsOverRide.size() == 0) {

			error = true;

			SingestionExceptionType singestionExceptionType = SingestionExceptionType.find.where()
					.eq("name", SingestionExceptionType.BadHoldingFormat).findUnique();

			new IhsIngestionException(ihsIngestionRecord, singestionExceptionType, paprData.title, "",
					singestionExceptionStatus, ihsUser).save();

			/* AJE 2016-07-27 13:47 Travant original Logger message: 
			Logger.info("dataValidateAndInsert() validation:" + SingestionExceptionType.BadHoldingFormat + ":"
					+ paprData.holding); */
			Logger.info("dataValidateAndInsert() finished validation, if block:" + SingestionExceptionType.BadHoldingFormat 
					+ " ; paprData.holding: '" + paprData.holding 
					+"' ; siET.desc.: '" +singestionExceptionType.description 
					+ " ; volumes.size(): '" + volumes.size() 
					+ " ; years.size(): '" + years.size() 
					+ " ; yearsOverRide.size(): '" + yearsOverRide.size() ); 
			// AJE 2016-07-27 13:47 expand the error message, just this line changed (2 of 2 versions)
		
			errorString.add(singestionExceptionType.description + "|");
		}

		if (error) {
			Logger.info("dataValidateAndInsert() will return 'error', which is '"+error+"'.");
			return error;
		}

			// AJE learning : find title by printISSN ?
		IhsTitle ihsTitle = IhsTitle.find.fetch("ihsPublicationRange").where().eq("printISSN", paprData.printISSN)
				.findUnique();

		if (ihsTitle == null) {

			Logger.info("dataValidateAndInsert() finished validation, ihsTitle is null when searched by printISSN; try find by oclcNumber.");

				// AJE learning : failed to find title by printISSN ; so look for it by OCLC?
			ihsTitle = IhsTitle.find.fetch("ihsPublicationRange").where().eq("oclcNumber", paprData.oclcNumber)
					.findUnique();

			if (ihsTitle == null) { 			// AJE learning : still failed (printISSN and OCLC all not found), create exception
				
				Logger.info("dataValidateAndInsert() finished validation, ihsTitle is null when searched by printISSN AND by oclcNumber.");
				
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

			//Logger.info("AJE 2016-07-28 11:00:42 for ihsVol, this one is " +ihsVol); // some kind of ? object pointer; useless

			//Logger.info("AJE 2016-07-28 11:10:45 for ihsVolume, ihsTitle.ihsVolume: " +ihsTitle.ihsVolume); // models.ihsVolume@some_hexID*
			/* compiler can't find these references 
			- ihsTitle.ihsVolume.volumeNumber
			- ihsTitle.ihsVolume.startDate
			- ihsTitle.ihsVolume.endDate) */ 
			
			 /* 
			 2016-07-28 seems to find the right vols but not helpful just now
Logger.info("AJE 2016-07-28 11:20:32 for ihsVolume, ihsVol.volumeNumber: " +ihsVol.volumeNumber
	+ "\t ihsVol.startDate to *endDate: " +ihsVol.startDate + " to " +ihsVol.endDate); 
			*/

			// AJE 2016-07-28 11:22:09 only the try/catch was originally in this block
			try {
				ihsIssuesYear.put(new Integer(ihsVol.startDate.getYear()).toString(), ihsVol.ihsissues);
			} catch (Exception e) {
				System.out.println(" not"); // AJE 2016-07-28 11:09:05 this is the original Travant message. Quite helpful.
				Logger.info("dataValidateAndInsert() improved error random id QPLS: have Exception: " + e + ".");
			}

		} // end for ihsVolume


// AJE 2016-07-29 14:42:59: testing first 2 patterns, they get caught; doesn't hit any of the other messages before this, although will fail with MissingAuthorizedVolume
/*
Logger.info("AJE 2016-07-27 17:23 dataValidateAndInsert() just before MissingAuthorizedVolume area"
+ " ; volumes: '" + volumes  
+ " ; volumes.size(): '" + volumes.size() 
+ " ; years: '" + years 
+ " ; years.size(): '" + years.size() 
+ " ; yearsOverRide: '" + yearsOverRide 
+ " ; yearsOverRide.size(): '" + yearsOverRide.size() 
+ " \n ... ihsIssuesYear = " + ihsIssuesYear); 

*/

/* Logger.info("dataValidateAndInsert() just before volumes.size() > 0 block; what does processVolumes return? :" + processVolumes(ihsIssues, volumes, ihsMember, ihsLocation, scommitment)); */

		// Check Holding
		if (volumes.size() > 0) {
			
			Logger.info("dataValidateAndInsert() if volumes.size() ["+volumes.size()+"] > 0 block; next call processVolumes()");
			
			if (processVolumes(ihsIssues, volumes, ihsMember, ihsLocation, scommitment)) {
				error = true;
				SingestionExceptionType singestionExceptionType = SingestionExceptionType.find.where()
						.eq("name", SingestionExceptionType.MissingAuthorizedVolume).findUnique();

				new IhsIngestionException(ihsIngestionRecord, singestionExceptionType, paprData.title, "",
						singestionExceptionStatus, ihsUser).save();

				Logger.info("dataValidateAndInsert() volumes.size() > 0 block; validation:" + SingestionExceptionType.MissingAuthorizedVolume);
				errorString.add(singestionExceptionType.description + "|");
				return error;
			} else { 
				// AJE 2016-08-02 there was no else block here
				Logger.info("dataValidateAndInsert() if volumes.size() ["+volumes.size()+"] > 0 block; empty ELSE: processVolumes() appears to have succeeded."); 
			}
		}

		if (years.size() > 0) {
			
			Logger.info("dataValidateAndInsert() if years.size() ["+years.size()+"] > 0 block; next call processYears()");
			
			if (processYears(ihsIssuesYear, years, ihsMember, ihsLocation, scommitment)) {
				error = true;
				SingestionExceptionType singestionExceptionType = SingestionExceptionType.find.where()
						.eq("name", SingestionExceptionType.MissingAuthorizedVolume).findUnique();

				new IhsIngestionException(ihsIngestionRecord, singestionExceptionType, paprData.title, "",
						singestionExceptionStatus, ihsUser).save();

				Logger.info("dataValidateAndInsert() years.size() > 0 block; validation:" + SingestionExceptionType.MissingAuthorizedVolume);
				errorString.add(singestionExceptionType.description + "|");
				return error;
			} else { 
				// AJE 2016-08-02 there was no else block here
				Logger.info("dataValidateAndInsert() if years.size() ["+years.size()+"] > 0 block; empty ELSE: processYears() appears to have succeeded."); 
			}
		}

		if(yearsOverRide.size() > 0){
			
			Logger.info("dataValidateAndInsert() if yearsOverRide.size() ["+yearsOverRide.size()+"] > 0 block; next call processYearsOverride()");
			
			if (processYearsOverride(ihsIssuesYear, yearsOverRide, ihsMember, ihsLocation, scommitment)) {
				error = true;
				SingestionExceptionType singestionExceptionType = SingestionExceptionType.find.where()
						.eq("name", SingestionExceptionType.MissingAuthorizedVolume).findUnique();

				new IhsIngestionException(ihsIngestionRecord, singestionExceptionType, paprData.title, "",
						singestionExceptionStatus, ihsUser).save();

				Logger.info("dataValidateAndInsert() yearsOverRide.size() > 0 block;  validation:" + SingestionExceptionType.MissingAuthorizedVolume);
				errorString.add(singestionExceptionType.description + "|");
				return error;
			} else { 
				// AJE 2016-08-02 there was no else block here
				Logger.info("dataValidateAndInsert() if yearsOverRide.size() ["+yearsOverRide.size()+"] > 0 block; empty ELSE: processYearsOverride() appears to have succeeded."); 
			}
		}

		// Check Holding

		Logger.info("dataValidateAndInsert() is about to return false for paprData.title '"+paprData.title+"', which, paradoxically, is good?"); 

		return false;
	} // end dataValidateAndInsert



	static boolean processVolumes(Hashtable<String, List<IhsIssue>> ihsIssues, List<PaprVolume> volumes,
			IhsMember ihsMember, IhsLocation ihsLocation, Scommitment scommitment) {

		boolean error = false;
		for (PaprVolume volume : volumes) {

// not helpful // Logger.info("AJE 2016-07-28 12:25:25 for PaprVolume volume : volumes, volume: " + volume + " ; volumes: " +volumes);
Logger.info("processVolumes(): for (PaprVolume volume : volumes); volume.startVolume: " + volume.startVolume + " ; volume.endIssue: " +volume.endIssue);

			// AJE 2016-07-28 there was originally nothing before the IF in this for loop 
			if (volume.startVolume == -1) {
				if (volume.endIssue < -1) {
					try {

						List<IhsIssue> issues = ihsIssues.get(new Integer(1).toString());

						for (IhsIssue ihsIssue : issues) {
// AJE 2016-08-01 this next IF condition is likely to cause ParseInteger error or whatever it's called: issueNumber is not always numeric or convertible
							if (new Integer(ihsIssue.issueNumber).intValue() >= volume.startIssue
									&& new Integer(ihsIssue.issueNumber).intValue() <= volume.endIssue) {

								saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);

							}
						}
					} catch (Exception e) {
						Logger.info("processVolumes(), volume.startVolume == -1 block; error e: " + e);
						error = true;
					}

				} else { // AJE: endIssue >= -1 
					try {
						List<IhsIssue> issues = ihsIssues.get(new Integer(1).toString());
						for (IhsIssue ihsIssue : issues) {
// AJE 2016-08-01 this next IF condition is likely to cause ParseInteger error or whatever it's called: issueNumber is not always numeric or convertible
							Logger.info(", ELSE (i.e, volume.startVolume != -1) block; ihsIssue.issueNumber == " +ihsIssue.issueNumber+".");
							if (new Integer(ihsIssue.issueNumber).intValue() == volume.startIssue) {
								saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);
							}
						}
					} catch (Exception e) {
						Logger.info("processVolumes(), ELSE (i.e, volume.startVolume != -1) block; error e: " + e);
						error = true;
					}
				}
				break;
			} // end if startVolume == -1

			int volCounter = 0;

			// if we have end volume then we have run, says Travant.  AJE has no idea what this means.  Finished? ; oh, more likely, "have complete run" of title
			if (volume.endVolume > -1) {
				for (;;) { // AJE 2016-07-28 begin Travant's infinite for loop; why would one even code this way?
					
					// handle first volume

					if (volCounter == 0) { // AJE 2016-07-28 apparently means, just run this block on the first pass; just first volume?
						volCounter = volume.startVolume;
						
						Logger.info("processVolumes(), volCounter first pass, set it = volume.startVolume: '" +volume.startVolume+ "'");
						
						try {
							List<IhsIssue> issues = ihsIssues.get(new Integer(volCounter).toString());

							for (IhsIssue ihsIssue : issues) {
								// AJE 2016-07-29 disabled_INT_CK_volCounter_Zero_block 
// AJE 2016-08-01 this next IF condition is likely to cause ParseInteger error or whatever it's called: issueNumber is not always numeric or convertible
								Logger.info("processVolumes(), volCounter == 0 block; ihsIssue.issueNumber == " +ihsIssue.issueNumber+"; \n ... if (new Integer(ihsIssue.issueNumber ["+ihsIssue.issueNumber+"]).intValue() >= volume.startIssue ["+volume.startIssue+"]) has been disabled [ihsIssue.issueNumber not always numeric].");
								//if (new Integer(ihsIssue.issueNumber).intValue() >= volume.startIssue) {
									Logger.info("processVolumes(), volCounter == 0 block; // TRY saveHolding on ihsIssue: "+ihsIssue.ihsTitle.title+",v."+ihsIssue.ihsVolume.volumeNumber+",no."+ihsIssue.issueNumber+" ("+ihsIssue.publicationDate+")");
									saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);
								//} 
								// AJE 2016-07-29 disabled_INT_CK_volCounter_Zero_block 
							}
						} catch (Exception e) {
							Logger.info("processVolumes() volCounter == 0 block; for ihsIssue; error: " + e + ".");
							error = true;
						}
					} // end first pass if block

					// handle end Volume
					else if (volCounter >= volume.endVolume) {

Logger.info("processVolumes(), for inf. loop; volCounter: "+volCounter+"; handle volume.endVolume: '" +volume.endVolume+ "'; is volume.endIssue ["+volume.endIssue+"] > -1 ?");
						
						if (volume.endIssue > -1) {
							
							Logger.info("processVolumes(); if (volume.endIssue ["+volume.endIssue+"] > -1) block...");
							
							try {
								List<IhsIssue> issues = ihsIssues.get(new Integer(volCounter).toString());

								for (IhsIssue ihsIssue : issues) {
// AJE 2016-08-01 this next IF condition is likely to cause ParseInteger error or whatever it's called: issueNumber is not always numeric or convertible
									Logger.info("processVolumes(), for inf. loop; end vol? // TRY saveHolding on ihsIssue: "+ihsIssue.ihsTitle.title+",v."+ihsIssue.ihsVolume.volumeNumber+",no."+ihsIssue.issueNumber+" ("+ihsIssue.publicationDate+") \n ...if (new Integer(ihsIssue.issueNumber ["+ihsIssue.issueNumber+"]).intValue() <= volume.endIssue ["+volume.endIssue+"]) has been disabled  [ihsIssue.issueNumber is not always numeric].");
									// AJE 2016-07-29 disabled_INT_CK_forinf_endVol 
									// if (new Integer(ihsIssue.issueNumber).intValue() <= volume.endIssue) {
										saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);
									// } 
									// AJE 2016-07-29 disabled_INT_CK_forinf_endVol 
								}
							} catch (Exception e) {
								Logger.info("processVolumes() for inf. loop; end vol?; error: " + e + ".");
								error = true;
							}
						}

						break;

					} else { // handle middle volumes
						
						//Logger.info("processVolumes(); else handle middle volumes...");						
						
						try {
							List<IhsIssue> issues = ihsIssues.get(new Integer(volCounter).toString());

							for (IhsIssue ihsIssue : issues) {
								try {
								//Logger.info("processVolumes(), for inf. loop; middle vols; for try WILL saveHolding on ihsIssue: "+ihsIssue.ihsTitle.title+",v."+ihsIssue.ihsVolume.volumeNumber+",no."+ihsIssue.issueNumber+" ("+ihsIssue.publicationDate+") \n...note no 'new Integer(ihsIssue.issueNumber:"+ihsIssue.issueNumber+")' block in middle vols");
									saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);
								} catch (Exception e) {
									// AJE 2016-07-21 (1 of X) this block was always empty : DEVNOTE : do we want to set error = true here?
									Logger.info("processVolumes(), for inf. loop; middle vols; AJE empty catch 1 of X; has Exception e="+e+".");									
								}
							}
						} catch (Exception e) {
							Logger.info("processVolumes(), for inf. loop; middle vols; AJE unlabeled catch that sets 'error' true; has Exception e="+e+".");									
							error = true;
						}
					}// end else handle middle vols
					volCounter++;
				} // AJE 2016-07-28 end Travant's infinite for loop
			} else {

//				Logger.info("processVolumes(), else block (volume.endVolume <= -1 ?): vol.endVol=" +volume.endVolume);

				List<IhsIssue> issues = ihsIssues.get(new Integer(volume.startVolume).toString());

				try {
					for (IhsIssue ihsIssue : issues) {
						saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);
					}
				} catch (Exception e) {
					Logger.info("processVolumes(), else block ; AJE unlabeled catch that sets 'error' true; has Exception e="+e+".");
					error = true;
				}
			}
		}

		return error;
	} // end processVolumes


	static boolean processYears(Hashtable<String, List<IhsIssue>> ihsIssuesYear, List<PaprYear> years,
			IhsMember ihsMember, IhsLocation ihsLocation, Scommitment scommitment) {

		boolean error = false;
		for (PaprYear year : years) {

			int yearCounter = 0;

			// "if we have end volume then we have run", says Travant; presumably means a complete run of the title
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
							// AJE 2016-07-21 (1point5 of X) this block was always empty : DEVNOTE : do we want to set error = true here?
							Logger.info("processYears(), for Travant infinite loop; if (yearCounter == 0) block; (AJE empty catch 1point5 of X)  error e: " + e);					
						}
					} // end if yearCounter == 0 [handle first volume]

					else if (yearCounter >= year.endYear) { // handle end Volume
						if (year.endMonth > -1) {
							try {
								List<IhsIssue> issues = ihsIssuesYear.get(new Integer(yearCounter).toString());

								if (issues == null) {
									Logger.info("processYears(), else if (yearCounter ["+yearCounter+"] >= year.endYear ["+year.endYear+"]) block; if (year.endMonth ["+year.endMonth+"] > -1) sub-block; issues is null in handling END volume.");
									Logger.info("processYears(), line 1166 ihsIssuesYear = '" +ihsIssuesYear+ "'.");
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
								// AJE 2016-07-21 (2 of X) this block was always empty : DEVNOTE : do we want to set error = true here?
								Logger.info("processYears(), for Travant infinite loop; else if (yearCounter "+yearCounter+" >= year.endYear "+year.endYear+" ) block; if (year.endMonth ["+year.endMonth+"] > -1) sub-block; (AJE 2 of X empty catch); error e: " + e);
							}
						} // end if year.endMonth > -1

						break;

					} else { // handle middle volumes
						try {
							List<IhsIssue> issues = ihsIssuesYear.get(new Integer(yearCounter).toString());

							if (issues == null) {
								Logger.info("processYears(), issues is null in handling MIDDLE volumes where yearCounter = "+yearCounter+".");
								error = true;
							}

							for (IhsIssue ihsIssue : issues) {
								try {
									saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);
								} catch (Exception e) {
									// AJE 2016-07-21 (3 of X) this block was always empty : DEVNOTE : do we want to set error = true here?
									Logger.info("processYears(), for Travant infinite loop; else handle middle volumes block, for issues block; (AJE 3 of X empty catch); error e: " + e);
								}
							} // end for ihsIssue
						} catch (Exception e) {
							// AJE 2016-07-21 (4 of X) this block was always empty : DEVNOTE : do we want to set error = true here?
							Logger.info("processYears(), for Travant infinite loop; else handle middle volumes block; (AJE 4 of X empty catch); error e: " + e);
						}
					} // end else middle volumes
					yearCounter++;
				}// end for Travant infinite loop
			} else { // year.endYear is not > -1

				if (yearCounter == 0) { // If Only on year // AJE 2016-07-21 that is Travant original comment : does it mean if only 1 year?
					yearCounter = year.beginYear;
				}

				List<IhsIssue> issues = ihsIssuesYear.get(new Integer(yearCounter).toString());

				if (issues == null) {
					Logger.info("processYears(), issues is null in else year.endYear is not > -1.");
					error = true;
				} else {
					for (IhsIssue ihsIssue : issues) {
						try {
							saveHolding(ihsIssue, ihsMember, ihsLocation, scommitment);
						} catch (Exception e) {
							// AJE 2016-07-21 (5 of X) this block was always empty : DEVNOTE : do we want to set error = true here?
							Logger.info("processYears(), for Travant infinite loop; else handle middle volumes block; (AJE 5 of X empty catch); error e: " + e);
						}
					}
				}
			}
		} // end for years

		return error;
	} // end processYears
	
	
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
	} // end processYearsOverride
	

	static void saveHolding(IhsIssue ihsIssue, IhsMember ihsMember, IhsLocation ihsLocation, Scommitment scommitment) {

		SconditionTypeOverall sconditionTypeOverall = SconditionTypeOverall.find.where()
				.eq("name", SconditionTypeOverall.Unknown).findUnique();

		SihsVarified sihsVarified = SihsVarified.find.where().eq("name", SihsVarified.NO).findUnique();

		SvalidationLevel svalidationLevel = SvalidationLevel.find.where().eq("name", SvalidationLevel.None)
				.findUnique();

		SholdingStatus sholdingStatus = SholdingStatus.find.where().eq("name", SholdingStatus.Default).findUnique();

		IhsHolding ihsHolding = IhsHolding.find.where().eq("issueID", ihsIssue.issueID)
				.eq("memberID", ihsMember.memberID).eq("locationID", ihsLocation.locationID).findUnique();


//		Logger.info("saveHolding() has ihsIssue: "+ihsIssue.ihsTitle.title+",v."+ihsIssue.ihsVolume.volumeNumber+",no."+ihsIssue.issueNumber+" ("+ihsIssue.publicationDate+")");

		if (ihsHolding == null) { // this appears to be the usual case: ihsHolding is null because there is no match, so we create new

			new IhsHolding(ihsIssue, ihsMember, ihsLocation, sholdingStatus, sconditionTypeOverall, sihsVarified,
					svalidationLevel, scommitment).save();
					
			//Logger.info("saveHolding() IF: ihsHolding was created INSIDE the null block for issue: "+ihsIssue.ihsTitle.title+",v."+ihsIssue.ihsVolume.volumeNumber+",no."+ihsIssue.issueNumber+" ("+ihsIssue.publicationDate+")");	
					
		} else { // AJE 2016-07-28 there was no else block here 
			//Logger.info("saveHolding() ELSE: ihsHolding was created OUTSIDE the null block for issue: "+ihsIssue.ihsTitle.title+",v."+ihsIssue.ihsVolume.volumeNumber+",no."+ihsIssue.issueNumber+" ("+ihsIssue.publicationDate+")");
		}	
		
		//Logger.info("\n");	
		
	} // end saveHolding
} // end class PAPR