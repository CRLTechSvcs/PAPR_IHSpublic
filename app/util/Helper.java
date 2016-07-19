package util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import models.AppRole;
import models.AppUser;
import models.IhsPublicationRange;
import models.IhsPublicationRangeVer;
import models.IhsSecurityRole;
import models.IhsUser;
import play.Logger;
import play.cache.Cache;

public class Helper {
	static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy ");
	static String MD5 = "MD5";

	public static String getMD5hash(String s) {

		String hashpassword = null;
		MessageDigest m;
		try {
			m = MessageDigest.getInstance(MD5);
			m.update(s.getBytes(), 0, s.length());
			hashpassword = new BigInteger(1, m.digest()).toString(16);
			return hashpassword;

		} catch (NoSuchAlgorithmException e) {
			Logger.error("MD5:", e);
		}
		return hashpassword;
	}

	public static void AppUserEnCache(IhsUser ihsUser) {

		List<AppRole> appRoles = new ArrayList<AppRole>();

		for (IhsSecurityRole ihsSecurityRole : ihsUser.ihsSecurityRoles) {
			appRoles.add(new AppRole(ihsSecurityRole.name));
		}


		AppUser appUser = new AppUser(ihsUser.userID, ihsUser.userName,
				appRoles, ihsUser.ihsMember.memberID, ihsUser.ihsMember.name,
				ihsUser.ihsMember.ihsLocation.locationID, ihsUser.getName());

		Cache.set(ihsUser.userName, appUser);
	}

	public static AppUser getAppUserFromCache(String user) {
		return (AppUser) Cache.get(user);
	}

	public static String getPublicationRange(

		List<IhsPublicationRange> ihsPublicationRanges) {

		if(ihsPublicationRanges == null){
			return "";
		}


		DateTime startDate = null;
		DateTime endDate = null;

		String st = "";

		try{

			for (IhsPublicationRange ihsPublicationRange : ihsPublicationRanges) {

				if (startDate == null) {
					startDate = ihsPublicationRange.startDate;
				}

				if (endDate == null) {
					endDate = ihsPublicationRange.endDate;
				}

				if (ihsPublicationRange.startDate.isBefore(startDate)) {
					startDate = ihsPublicationRange.startDate;
				}

				if (ihsPublicationRange.endDate != null && ihsPublicationRange.endDate.isAfter(endDate)) {
					endDate = ihsPublicationRange.endDate;
				}

				if (ihsPublicationRange.endDate == null)
					endDate = new DateTime(9999, 9, 9, 12, 0, 0, 0);
			}

			String endDateSt =endDate.isAfter(new DateTime(9999, 9, 8, 12, 0, 0, 0) ) ? " " : dtf.print(endDate);

			st = dtf.print(startDate) + "-" + endDateSt;
		}catch (Exception e){

		}

		return st;
	}

	public static String getPublicationRangeVer(

			List<IhsPublicationRangeVer> ihsPublicationRanges) {

			if(ihsPublicationRanges == null){
				return "";
			}


			DateTime startDate = null;
			DateTime endDate = null;

			String st = "";

			try{

				for (IhsPublicationRangeVer ihsPublicationRange : ihsPublicationRanges) {


					if (startDate == null) {
						startDate = ihsPublicationRange.startDate;
					}

					if (endDate == null) {
						endDate = ihsPublicationRange.endDate;
					}

					if (ihsPublicationRange.startDate.isBefore(startDate)) {
						startDate = ihsPublicationRange.startDate;
					}

					if (ihsPublicationRange.endDate != null && ihsPublicationRange.endDate.isAfter(endDate)) {
						endDate = ihsPublicationRange.endDate;
					}

					if (ihsPublicationRange.endDate == null)
						endDate = new DateTime(9999, 9, 9, 12, 0, 0, 0);
				}


				String endDateSt =endDate.isAfter(new DateTime(9999, 9, 8, 12, 0, 0, 0) ) ? " " : dtf.print(endDate);

				st = dtf.print(startDate) + "-" + endDateSt;
			}catch (Exception e){

			}

			return st;
		}

	public static String formatIssn(String issn){

		if(issn == null){
			return "";
		}else{
			if( issn.length() == 8){
				return new StringBuilder(issn).insert(issn.length()-4, "-").toString();
			}
			return "";
		}
	}


	public static boolean isMonth(String mon) {

		if("jan".equals(mon.toLowerCase())){
			return true;
		}
		if("feb".equals(mon.toLowerCase())){
			return true;
		}
		if("mar".equals(mon.toLowerCase())){
			return true;
		}
		if("apr".equals(mon.toLowerCase())){
			return true;
		}
		if("may".equals(mon.toLowerCase())){
			return true;
		}
		if("jun".equals(mon.toLowerCase())){
			return true;
		}
		if("jul".equals(mon.toLowerCase())){
			return true;
		}
		if("aug".equals(mon.toLowerCase())){
			return true;
		}
		if("sep".equals(mon.toLowerCase())){
			return true;
		}
		if("oct".equals(mon.toLowerCase())){
			return true;
		}
		if("nov".equals(mon.toLowerCase())){
			return true;
		}
		if("dec".equals(mon.toLowerCase())){
			return true;
		}

		return false;

	}
	public static int getMonthIndex(String mon) throws Exception{

		if("jan".equals(mon.toLowerCase())){
			return 1;
		}
		if("feb".equals(mon.toLowerCase())){
			return 2;
		}
		if("mar".equals(mon.toLowerCase())){
			return 3;
		}
		if("apr".equals(mon.toLowerCase())){
			return 4;
		}
		if("may".equals(mon.toLowerCase())){
			return 5;
		}
		if("jun".equals(mon.toLowerCase())){
			return 6;
		}
		if("jul".equals(mon.toLowerCase())){
			return 7;
		}
		if("aug".equals(mon.toLowerCase())){
			return 8;
		}
		if("sep".equals(mon.toLowerCase())){
			return 9;
		}
		if("oct".equals(mon.toLowerCase())){
			return 10;
		}
		if("nov".equals(mon.toLowerCase())){
			return 11;
		}
		if("dec".equals(mon.toLowerCase())){
			return 12;
		}
		if("spring".equals(mon.toLowerCase())){
			return 13;
		}

		if("fall".equals(mon.toLowerCase())){
			return 14;
		}
		if("summer".equals(mon.toLowerCase())){
			return 15;
		}
		if("winter".equals(mon.toLowerCase())){
			return 16;
		}
		throw new Exception();
	}
}