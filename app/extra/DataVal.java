package extra;

import java.util.ArrayList;

import models.AppRole;
import models.AppUser;

import org.apache.commons.lang3.SerializationUtils;

import java.util.List;

import parser.Portico.PorticoVolume;
import play.Logger;

import java.math.BigInteger;
import java.security.*;

public class DataVal {

	public static void main(String[] args) throws NoSuchAlgorithmException {

		String tilteSearchSql = "SELECT titleId, title, seacrh MATCH (title) AGAINST (' param ' IN BOOLEAN MODE) as score "
				+ "FROM ihstitle "
				+ "WHERE MATCH (title) AGAINST (' search ' IN BOOLEAN MODE) "
				+ "ORDER BY score DESC "
				+ "limit  10;";
		
		
		System.out.println(tilteSearchSql.replaceAll("param", "test"));
		/*
		String st = "sadf/sadf";
		System.out.println(st.replace("/", "~"));
		
		
		List<AppRole> appRoles = new ArrayList<AppRole>();
		appRoles.add(new AppRole("admin"));
		appRoles.add(new AppRole("admin"));
		appRoles.add(new AppRole("admin"));
		appRoles.add(new AppRole("admin"));
		
		AppUser  appuser = new AppUser(1, "user", appRoles, 1, "", 1,"");
		
		byte name[] = SerializationUtils.serialize(appuser);
		AppUser deserilazed = (AppUser) SerializationUtils.deserialize(name);
		System.out.println(deserilazed.userName);
		System.out.println(deserilazed.appRoles.get(0).getName());
		System.out.println(name.length);
		
		
		
		
		List<PorticoVolume> porticoVolumes = new ArrayList();
		// List <Volume> holdings= new ArrayList();
		//ce114e4501d2f4e2dcea3e17b546f339
		String s="password";
        MessageDigest m=MessageDigest.getInstance("MD5");
        m.update(s.getBytes(),0,s.length());
        String hashpassword = new BigInteger(1,m.digest()).toString(16);
        
        System.out.println("MD5: "+ hashpassword);
        
		/*
		System.out.println("");

		String sr = "1990-2345";
		if (sr.matches("^\\d+-\\d+")) {
			System.out.println("match");
		}

		int startYear = 1990;
		int endYear = 2000;
		
		if(startYear >= 1990
				&& startYear <= 2000
				&& endYear >= 1990
				&& endYear <= 9999
				){
			
			System.out.println("in range");
		}
		
		String st = "v.10(1,2-3), v.7(1-4), v.6(2-4), v.5(4), v.9(1,4,2003), v.8(1-4), v.11(1-2)";

		// VolumesIssue volumesIssue = this.new VolumesIssue();

		String[] volumsissues = st.substring(0, st.length() - 1).split("\\),");

		// Volume volume = null;

		for (String volumsissuePairs : volumsissues) {

			PorticoVolume porticoVolume = null;

			String[] volumsissuePair = volumsissuePairs.split("\\(");

			if (volumsissuePair[0].startsWith("v")
					&& volumsissuePair[0].contains(".")) {
				String[] volumes = volumsissuePair[0].split("\\.");

				System.out.print("Volume:" + volumes[1] + ":");
				porticoVolume = new PorticoVolume(Integer.parseInt(volumes[1]));
			} else {
				System.out.println("Data probelm");
				// throw new Exception();
			}

			String[] issues = volumsissuePair[1].split(",");
			for (String issuetmp : issues) {
				if (issuetmp.contains("-")) {
					String[] issueRun = issuetmp.split("-");
					int beginIssue = Integer.parseInt(issueRun[0]);
					int endIssue = Integer.parseInt(issueRun[1]);

					for (int i = beginIssue; i <= endIssue; i++) {
						System.out.print(i + ":");
						porticoVolume.issues.add(new Integer(i));
					}
				} else {
					porticoVolume.issues.add(new Integer(issuetmp));
					System.out.print(issuetmp + ":");
				}
			}

			System.out.println();
			porticoVolumes.add(porticoVolume);
		}
		
				
		System.out.println("Done");
		
		for(PorticoVolume porticoVolume : porticoVolumes){
			System.out.print(porticoVolume.volume + ":");
			for(Integer i : porticoVolume.issues){
				System.out.print(i + ":");
			}
			System.out.println();	
		}
		*/
	}
}
