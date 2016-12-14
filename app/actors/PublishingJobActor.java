/*
  AJE 2016-09-30 'Publishing' has been relabeled 'Export Data' in the public interface, but all code still uses the old term
*/


package actors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;


import play.Play;
import util.Helper;
import models.IhsIngestionRecord;
import models.IhsIssue;
import models.IhsPublishingJob;
import models.IhsTitle;
import models.IhsVolume;
import models.SingestionJobStatus;
import play.Logger;
import akka.actor.UntypedActor;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import play.db.ebean.Model;// AJE 2016-11-21
import com.avaje.ebean.Ebean;// AJE 2016-11-21
import com.avaje.ebean.SqlRow;// AJE 2016-11-21

import java.io.Writer.*; // AJE 2016-12-09 for UTF-8 in file output


public class PublishingJobActor extends UntypedActor {

	static Random rand = new Random();

	@Override
	public void onReceive(Object message) throws Exception {

    Logger.info("PublishingJobActor.onReceive begins with message=" +message+ "; (message instanceof Integer) == "+ (message instanceof Integer)+ ".");

		if (message instanceof Integer) {

			Integer ihsPublishingJobId = (Integer) message;

			List<IhsTitle> ihsTitles = null;

      Logger.info("... will try to ihsPublishingJob = IhsPublishingJob.find.byId(ihsPublishingJobId=" +Integer.toString(ihsPublishingJobId)+ ").");

			IhsPublishingJob ihsPublishingJob = IhsPublishingJob.find
					.byId(ihsPublishingJobId);

      //Logger.info("... found. ihsPublishingJob.size()=" +Integer.toString(ihsPublishingJob.size())+ ").");

			String link = "";

			Logger.info("PublishingJobActor.onReceive. 'Start Running Publishing job': "+
			  "ihsPublishingJob.startDate='" +ihsPublishingJob.startDate+"', ihsPublishingJob.endDate='"+ihsPublishingJob.endDate+"'.");

      // AJE 2016-11-22 next statement is true IF we have used Travant's version of setting startDate/endDate (in Publishing.java).
      //    Seems to have been resolved with combo of ihs_publishing.js:function submitPub and Publishing.java:postPublishingView
      //Logger.info("In the dates, year + day of month is probably right, but note month is always '01'?");

			if (ihsPublishingJob.startDate != null
					&& ihsPublishingJob.endDate != null) {
        Logger.info("PublishingJobActor.onReceive: enter if startDate AND endDate NOT null block");

        Logger.info("...Garbage collection -> System.gc() next for java.lang.OutOfMemoryError: Java heap space");
        System.gc(); // AJE 2016-11-30 to fix java.lang.OutOfMemoryError: Java heap space ; suggested by: http://stackoverflow.com/questions/37335/how-to-deal-with-java-lang-outofmemoryerror-java-heap-space-error-64mb-heap

				ihsTitles = IhsTitle.find.fetch("ihsPublisher")
						.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
						.fetch("ihsPublicationRange").where()
						.ge("changeDate", ihsPublishingJob.startDate)
						.le("changeDate", ihsPublishingJob.endDate).findList();
			} else if (ihsPublishingJob.startDate != null) {
  			Logger.info("PublishingJobActor.onReceive: enter if startDate NOT null block");

				ihsTitles = IhsTitle.find.fetch("ihsPublisher")
						.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
						.fetch("ihsPublicationRange").where()
						.ge("changeDate", ihsPublishingJob.startDate)
						.findList();
			} else if (ihsPublishingJob.endDate != null) {
			  Logger.info("PublishingJobActor.onReceive: enter if endDate NOT null block");
				ihsTitles = IhsTitle.find.fetch("ihsPublisher")
						.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
						.fetch("ihsPublicationRange").where()
						.le("changeDate", ihsPublishingJob.endDate).findList();
			} else {
			  Logger.info("PublishingJobActor.onReceive: enter ELSE block: one of startDate or endDate was null");
				ihsTitles = IhsTitle.find.fetch("ihsPublisher")
						.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
						.fetch("ihsPublicationRange").findList();
			}

        Logger.info("...ihsTitles.size() = "+Integer.toString(ihsTitles.size())+".");
        //Logger.info("...ihsTitles.get(0).alphaTitle = "+ihsTitles.get(0).alphaTitle+".");
        //Logger.info("...ihsTitles[0].title = "+ihsTitles[0].title+".");

      Logger.info("...ihsTitles is the parameter to "+
        //"buildMarcText/buildPortico/buildIhsXls, or buildIhsCsv, and it is '"+ihsTitles+"'.");
        "build* functions [is too long to do on screen].");

			try {
				if (ihsPublishingJob.fileformat == 1) {
					Logger.info("ihsPublishingJob.fileformat == " +ihsPublishingJob.fileformat+ ", thus will call buildMarcText. Value of 'link' is never set in this block.");
					link = buildMarcText(ihsTitles); // AJE 2016-09-30 buildMarcText was delivered as empty brackets by Travant
					Logger.info("after buildMarcText, link = " +link+ ".");
				} else if (ihsPublishingJob.fileformat == 2) {
					Logger.info("ihsPublishingJob.fileformat == " +ihsPublishingJob.fileformat+ ", thus will call buildPortico ; this Portico block is the only one where value of 'link' is set.");
          link = buildPortico(ihsTitles);
					Logger.info("after buildPortico, link = " +link+ ".");
					ihsPublishingJob.link = link;
					Logger.info("AJE explicitly set ihsPublishingJob.link = " +link+ ", = " + ihsPublishingJob.link);
				} else if (ihsPublishingJob.fileformat == 3) {
					Logger.info("ihsPublishingJob.fileformat == " +ihsPublishingJob.fileformat+ ", thus will call buildIhsXls. Value of 'link' is never set in this block.");
					buildIhsXls(ihsTitles); // AJE 2016-09-30 buildIhsXls was delivered as empty brackets by Travant
				} else if (ihsPublishingJob.fileformat == 4) {
					Logger.info("ihsPublishingJob.fileformat == " +ihsPublishingJob.fileformat+ ", thus will call buildIhsCsv. Value of 'link' is never set in this block.");
					// buildIhsCsv(ihsTitles); // AJE 2016-09-30 buildIhsCsv was delivered as empty brackets by Travant
					link = buildIhsCsv(ihsTitles); // AJE 2016-09-30 buildIhsCsv was delivered as empty brackets by Travant
					Logger.info("after buildIhsCsv, link = " +link+ ".");
				}

				SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
						.where().eq("name", SingestionJobStatus.Complete)
						.findUnique();

				ihsPublishingJob.setSingestionJobStatus(singestionJobStatus);

				/*
				 * 		/ingestion/getBadformatFile/ is route to controllers.IngestionJob.getBadformatFile
				 */
        /********************************************************
          AJE 2016-09-30 Travant's above comment is an artifact of a cut and paste job:
          Publishing is not a function related to Ingestion

          here we @##$@#$# go again: just like the Reporting module, Travant copy and pasted one piece of presumably working code and didn't even test it.
          This file has nothing to do with ingestion.
          See routes file, and ReportingActor.java for other examples.  Unforgiveable sloppiness.
        */

				/**********************************************************
				// AJE 2016-09-30 this block is the Travant original code.
				ihsPublishingJob.setLink("<a href=/ingestion/getBadformatFile/"
						+ link.replace(System.getProperty("file.separator"),
								IhsIngestionRecord.tilde) + ">" + "Download</a>");
				ihsPublishingJob.update();
				**********************************************************/

/*
AJE 2016-09-30
NOTE THAT UNLIKE THE SECTION 2 CALLING buildPortico,
CALLING THE OTHER build* FUNCTIONS DOES NOT SET link = THE RETURN VALUE OF THOSE FUNCTIONS
AJE 2016-12-06 buildIhsCsv now returns link as well
*/
/*
Logger.info("before AJE resets link, link = " +link+ ".");
String dataDir = Play.application().configuration().getString("application.publishing.process.data.Dir");
//ihsPublishingJob.setLink("<a href='/"+dataDir+"/"+fileName+"'>Download</a>");
Logger.info("after AJE resets link, link = " +link+ ".");
*/


Logger.info("PublishingJobActor.java, onReceive, BEFORE replace has ihsPublishingJob.link = " +ihsPublishingJob.link+ " ; but plain link = " +link+ ".");
Logger.info("... using AJE simplified link.replace next ; buildPortico DOES NOT ALWAYS RETURN A VALID LINK");
link = link.replace('\\','/');
ihsPublishingJob.setLink("<a href=" + link + ">Download</a>");
Logger.info("...AFTER replace, ihsPublishingJob.publishingJobId="+Integer.toString(ihsPublishingJob.publishingJobId)+", has ihsPublishingJob.link = " +ihsPublishingJob.link);

//Logger.info("PublishingJobActor.java: onReceive: AJE: (1) ihsPublishingJob.update() is not valid? created dummy in IhsPublishingJob.java");
ihsPublishingJob.update(); // Travant original ; appeared to cause error "javax.persistence.OptimisticLockException:Data has changed"
// AJE 2016-11-21 added fake "public void update()" method body in IhsPublishingJob.java ; rescinded 2016-12-06, I think it is using update() of the superclass?

//Logger.info("... NEW GET in routes for /public/reports ; new value in application.conf for application.PUBLISHING.process.data.Dir.  Yes, publishing is supposedly a different thing from reporting, and yes, there is an application.REPORTING.process.data.Dir");
//Logger.info("AJE 2016-09-30 why no 'href' in link: " +link+ " ?  It is present in database.ihsreportingjob.link field.");

				Logger.info("PublishingJobActor.java: onReceive: Done Running publishing job");

        // AJE 2016-09-30 resume Travant original code

			} catch (Exception e) {
				//Logger.info("PublishingJobActor.java: onReceive: in catch block, AJE disabled singestionJobStatus and ihsPublishingJob.update()");
				Logger.info("PublishingJobActor.java: onReceive: AJE: (EXCEPTION CATCH) ihsPublishingJob.update() is not valid? created dummy in IhsPublishingJob.java");

				SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
						.where().eq("name", SingestionJobStatus.InternalError)
						.findUnique();
				ihsPublishingJob.setSingestionJobStatus(singestionJobStatus);
				ihsPublishingJob.update();

				Logger.error("PublishingJobActor.java: onReceive: has Exception: ", e);
			}
		} // end if (message instanceof Integer)

	} // end onReceive



	String buildMarcText(List<IhsTitle> ihsTitles) { // Travant titled this 'buildMArk'
	  // AJE 2016-09-30 read the Logger: there was nothing here before today
    //Logger.info("buildMarcText was delivered as empty brackets by Travant: all it does it print this message"); // AJE 2016-09-30
    Logger.info("buildMarcText was delivered as empty brackets by Travant: all content now by CRL 2016-12-07."); // AJE 2016-12-02

		// String dataDir = Play.application().configuration().getString("application.publishing.process.data.Dir"); // Travant original
    String dataDir = Play.application().configuration().getString("application.exporting.process.data.Dir"); // AJE 2016-12-07
		String fileName = rand.nextInt(10000) + "_IHSexport.mrk";
		// String destFileString = dataDir + File.separator + fileName; // Travant original
		String destFileString = dataDir + System.getProperty("file.separator") + fileName; // AJE replaced 2016-11-02
		  destFileString = destFileString.replace('\\','/');
    Logger.info("buildMarcText has dataDir = " +dataDir+ " ; fileName = " +fileName+ " ; destFileString = " +destFileString);

		int titleIndex = 0;
    //Logger.info("buildMarcText: next is for IhsTitle; ihsTitles.size()=" +ihsTitles.size());
		for (IhsTitle ihsTitle : ihsTitles) {

      //Logger.info("buildMarcText: inside for (IhsTitle ihsTitle : ihsTitles); ihsTitles.size() = " +ihsTitles.size()+ "; titleIndex = " +titleIndex);

      Integer titleID = ihsTitle.titleID; // int(11) NOT NULL AUTO_INCREMENT,
      //Integer titleTypeID = ihsTitle.titleTypeID; // int(11) NOT NULL // NOT IN THE DATA
      Integer titleTypeID = -1;
      String title = ihsTitle.title != null ? ihsTitle.title : ""; // varchar(512) NOT NULL,
      String alphaTitle = ihsTitle.alphaTitle != null ? ihsTitle.alphaTitle : ""; // varchar(512) DEFAULT NULL,
      String printISSN = ihsTitle.printISSN != null ? Helper.formatIssn(ihsTitle.printISSN) : ""; // varchar(32) DEFAULT NULL,
      String eISSN = ihsTitle.eISSN != null ? Helper.formatIssn(ihsTitle.eISSN) : ""; // varchar(32) DEFAULT NULL,
      String oclcNumber = ihsTitle.oclcNumber != null ? ihsTitle.oclcNumber : ""; // varchar(32) DEFAULT NULL,
      String lccn = ihsTitle.lccn != null ? ihsTitle.lccn : ""; // varchar(32) DEFAULT NULL,
      //Integer publisherID = ihsTitle.publisherID; // int(11) NOT NULL // NOT IN THE DATA
      Integer publisherID = -1;
      String description = ihsTitle.description != null ? ihsTitle.description : ""; // varchar(512) DEFAULT NULL,
      //Integer titleStatusID = ihsTitle.titleStatusID; // int(11) NOT NULL // NOT IN THE DATA
      Integer titleStatusID = -1;
      String changeDate = ihsTitle.changeDate != null ? ihsTitle.changeDate.toString() : ""; // date NOT NULL,
      //Integer userID = ihsTitle.userId; // int(11) NOT NULL // NOT IN THE DATA
      Integer userID = -1;
      //Integer titleVersion = ihsTitle.titleVersion; // int(11) DEFAULT '0' // NOT IN THE DATA
      Integer titleVersion = -1;
      //Integer imagePageRatio = ihsTitle.imagePageRatio != null ? ihsTitle.imagePageRatio : -1; //int(11) DEFAULT '0' // NOT IN THE DATA
      Integer imagePageRatio = 0; // int(11) DEFAULT NULL,
      String language = ihsTitle.language != null ? ihsTitle.language : ""; // varchar(32) NOT NULL,
      String country = ihsTitle.country != null ? ihsTitle.country : ""; // varchar(32) NOT NULL,
      //Integer volumeLevelFlag = ihsTitle.volumeLevelFlag; // char(1) DEFAULT '0' // NOT IN THE DATA
      Integer volumeLevelFlag = -1;

      String titleMRKcontent = "=001  IHS-" +Integer.toString(titleID) + ":"+ Integer.toString(titleTypeID) + "\n";
        titleMRKcontent += "=010  \\\\$a" + lccn + "\n";
        titleMRKcontent += "=022  \\\\$a" + printISSN + "\n";
        titleMRKcontent += "=022  \\\\$a" + eISSN + "\n";
        titleMRKcontent += "=035  \\\\$a" + oclcNumber + "\n";
        titleMRKcontent += "=245  00$a" +title + "\n";
        titleMRKcontent += "=246  00$a"+ alphaTitle + "\n";
        titleMRKcontent += "=264  \1$b"+ Integer.toString(publisherID) + "\n";
        titleMRKcontent += "=500  \\\\$adescription: " + description + "\n";
        titleMRKcontent += "=500  \\\\$atitleID: " + Integer.toString(titleID) + "\n";
        titleMRKcontent += "=500  \\\\$atitleTypeID: " + Integer.toString(titleTypeID) + "\n";
        titleMRKcontent += "=500  \\\\$atitleStatusID: " + Integer.toString(titleStatusID) + "\n";
        titleMRKcontent += "=500  \\\\$achangeDate: " +changeDate.substring(0,10) + "\n";
        titleMRKcontent += "=500  \\\\$auserID: " +Integer.toString(userID) + "\n";
        titleMRKcontent += "=500  \\\\$atitleVersion: " +Integer.toString(titleVersion) + "\n";
        titleMRKcontent += "=500  \\\\$aIssue-level data in system? : ";
        if(volumeLevelFlag == -1) {
          titleMRKcontent += Integer.toString(volumeLevelFlag)+" = [no information]\n";
        } else if(volumeLevelFlag == 0) {
          titleMRKcontent += Integer.toString(volumeLevelFlag)+" = yes\n";
        } else if(volumeLevelFlag == 1) {
          titleMRKcontent += Integer.toString(volumeLevelFlag)+" = no\n";
        }
        titleMRKcontent += "=500  \\\\$aimagePageRatio: " +Integer.toString(imagePageRatio) + "\n" ;
        titleMRKcontent += "=522  \\\\$aFrom "+ country + "\n";
        titleMRKcontent += "=546  \\\\$aIn "+ language + "\n";

			StringBuilder builderHolding = new StringBuilder();
			boolean startVolume = true;
			boolean startIssue = true;

			for(IhsVolume ihsVolume: ihsTitle.ihsVolume){
        int volumeIndex = 0;

        if(startVolume && startIssue) {
          //Logger.info("buildMarcText: inside for (IhsVolume ihsVolume: ihsTitle.ihsVolume); ihsTitle.ihsVolume.size()=" +ihsTitle.ihsVolume.size()+ "; volumeIndex = " +volumeIndex);
        }

				if(!startVolume) {
				  builderHolding.append(",");
				}
				startVolume = false;
				builderHolding.append("v.").append(ihsVolume.volumeNumber).append("(");

        int issueIndex = 0;
				for(IhsIssue ihsissue: ihsVolume.ihsissues){

          if(startVolume && startIssue) {
            //Logger.info("buildMarcText: inside for (IhsIssue ihsissue: ihsVolume.ihsissues); ihsVolume.ihsissues.size()=" +ihsVolume.ihsissues.size()+ "; issueIndex = " +issueIndex);
          }

					if(!startIssue) {
					  builderHolding.append(",");
					}
					startIssue=false;
					builderHolding.append(ihsissue.issueNumber);

					issueIndex++;
				} // end for IhsIssue ihsissue

				builderHolding.append(")");
				startIssue = true;
				volumeIndex++;
			} // end for IhsVolume ihsVolume

        titleMRKcontent += "=945  \\\\$a"+ builderHolding + "\n";
      //Logger.info("titleMRKcontent now contains builderHolding: "+titleMRKcontent);

      try{
    		FileOutputStream fos = new FileOutputStream(destFileString, true); // 'true' for APPEND to file
        byte[] bytesArray = titleMRKcontent.toString().getBytes();
        fos.write(bytesArray);
        bytesArray = "\n".getBytes();
        fos.write(bytesArray);
    		fos.close();
        fos.flush();
        //Logger.info("buildMarcText: titleMRKcontent + newline written successfully at " +destFileString);
      } catch (IOException e) { // TODO Auto-generated
  		  Logger.info("buildMarcText error with writing titleMRKcontent: \n" +e);
      }
			titleIndex++;
		} // end for ihsTitles

		return destFileString;
	} // end buildMarcText



	String buildPortico(List<IhsTitle> ihsTitles) throws IOException {
    Logger.info("PublishingJobActor.java: enter buildPortico: May cause 'java.lang.OutOfMemoryError: Java heap space.'"); // AJ 2016-09-30
    Logger.info("... buildPortico did not crash when only a 5 day range chosen, but did for 30"); // AJE 2016-09-30

		// String dataDir = Play.application().configuration().getString("application.publishing.process.data.Dir"); // Travant original
    String dataDir = Play.application().configuration().getString("application.exporting.process.data.Dir"); // AJE 2016-12-07
		String fileName = rand.nextInt(10000) + "-" + "portico.xlsx";
		// String destFileString = dataDir + File.separator + fileName; // Travant original
		String destFileString = dataDir + System.getProperty("file.separator") + fileName; // AJE replaced 2016-11-02
		  destFileString = destFileString.replace('\\','/');
    Logger.info("buildPortico has dataDir = " +dataDir+ "; fileName = " +fileName+ " ; destFileString = " +destFileString);

		Workbook workbook = null;

		workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Portico");

		int rowIndex = 0;
		// Add header
		Row headedrow = sheet.createRow(rowIndex++);

		Cell headerpubcell = headedrow.createCell(0);
		headerpubcell.setCellValue("Publisher");
		sheet.setColumnWidth(0, 10000);

		Cell headertitlecell = headedrow.createCell(1);
		headertitlecell.setCellValue("Title");
		sheet.setColumnWidth(1, 10000);

		Cell headerSocietycell = headedrow.createCell(2);
		headerSocietycell.setCellValue("Society");

		Cell headerPrintISSNcell = headedrow.createCell(3);
		headerPrintISSNcell.setCellValue("Print ISSN");
		sheet.setColumnWidth(3, 2500);

		Cell headereISSNcell = headedrow.createCell(4);
		headereISSNcell.setCellValue("e-ISSN");
		sheet.setColumnWidth(4, 2500);

		Cell headerpcacell = headedrow.createCell(5);
		headerpcacell.setCellValue("PCA");

		Cell headerStatuscell = headedrow.createCell(6);
		headerStatuscell.setCellValue("Status");

		Cell headerHoldingscell = headedrow.createCell(7);
		headerHoldingscell.setCellValue("Holdings");
		sheet.setColumnWidth(7, 30000);

		Cell headerYearscell = headedrow.createCell(8);
		headerYearscell.setCellValue("Years");
		sheet.setColumnWidth(8, 2500);

		Cell headerContentSetIdcell = headedrow.createCell(9);
		headerContentSetIdcell.setCellValue("ContentSet Id");

		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);

    Logger.info("buildPortico: before for line 271; ihsTitles.size()=" +ihsTitles.size());

		for (IhsTitle ihsTitle : ihsTitles) {

      //Logger.info("buildPortico: for (IhsTitle ihsTitle : ihsTitles) rowIndex)" +rowIndex);

			Row childrow = sheet.createRow(rowIndex++);

			Cell pubcell = childrow.createCell(0);
			pubcell.setCellValue(ihsTitle.ihsPublisher.name);

			pubcell.setCellStyle(style);

			Cell titlecell = childrow.createCell(1);
			titlecell.setCellValue(ihsTitle.title);
			titlecell.setCellStyle(style);

			Cell societycell = childrow.createCell(2);
			societycell.setCellValue("");

			String pISSN = ihsTitle.printISSN != null ? Helper.formatIssn(ihsTitle.printISSN) : "";
			Cell printISSNcell = childrow.createCell(3);
			printISSNcell.setCellValue(pISSN);

			String eISSN = ihsTitle.eISSN != null ? Helper.formatIssn(ihsTitle.eISSN) : "";
			Cell eISSNcell = childrow.createCell(4);
			eISSNcell.setCellValue(eISSN);

			Cell pcacell = childrow.createCell(5);
			pcacell.setCellValue("");

			Cell statuscell = childrow.createCell(6);
			statuscell.setCellValue("");

			StringBuilder builderHolding = new StringBuilder();
			boolean startVolume = true;
			boolean startIssue = true;

			//Logger.info("buildPortico: line 309 before for(IhsVolume ihsVolume: ihsTitle.ihsVolume); ihsTitle.ihsVolume.size()=" +ihsTitle.ihsVolume.size());

			for(IhsVolume ihsVolume: ihsTitle.ihsVolume){

      //Logger.info("buildPortico: for(IhsVolume ihsVolume: ihsTitle.ihsVolume) rowIndex)" +rowIndex);

				if(!startVolume) builderHolding.append(",");
				startVolume=false;

				builderHolding.append("v.").append(ihsVolume.volumeNumber).append("(");

				for(IhsIssue ihsissue: ihsVolume.ihsissues){

				  //Logger.info("buildPortico: for(IhsIssue ihsissue: ihsVolume.ihsissues) rowIndex)" +rowIndex);

					if(!startIssue) builderHolding.append(",");
					builderHolding.append(ihsissue.issueNumber);
					startIssue=false;
				}
				startIssue=true;
				builderHolding.append(")");
			}

			Cell holdingscell = childrow.createCell(7);
			holdingscell.setCellValue(builderHolding.toString());
			holdingscell.setCellStyle(style);


			String years = Helper.getPublicationRange(ihsTitle.ihsPublicationRange);

			Cell yearscell = childrow.createCell(8);
			yearscell.setCellValue(years);

			Cell contentSetIdcell = childrow.createCell(9);
			contentSetIdcell.setCellValue("");
		}

		FileOutputStream fos = new FileOutputStream(destFileString);
		workbook.write(fos);
		fos.close();

		/*
		 * File file = new File(destFileString);
		 *
		 * try { BufferedWriter output = new BufferedWriter(new
		 * FileWriter(file));
		 *
		 * for (IhsTitle ihsTitle : ihsTitles) { output.write(ihsTitle.title); }
		 *
		 * output.close(); } catch (IOException e) { // TODO Auto-generated
		 * catch block throw e; }
		 */

		return destFileString;

	}// end buildPortico



	void buildIhsXls(List<IhsTitle> ihsTitles) {
	  // AJE 2016-09-30 read the Logger: there was nothing here before today
    Logger.info("buildIhsXls was delivered as empty brackets by Travant: all it does it print this message"); // AJE 2016-09-30
	}



	String buildIhsCsv(List<IhsTitle> ihsTitles) {
	  // AJE 2016-09-30 read the Logger: there was nothing here before today
    //Logger.info("buildIhsCsv was delivered as empty brackets by Travant: all it does it print this message"); // AJE 2016-09-30
    Logger.info("buildIhsCsv was delivered as empty brackets by Travant: all content now by CRL 2016-12-02."); // AJE 2016-12-02

		// String dataDir = Play.application().configuration().getString("application.publishing.process.data.Dir"); // Travant original
    String dataDir = Play.application().configuration().getString("application.exporting.process.data.Dir"); // AJE 2016-12-07
		String fileName = rand.nextInt(10000) + "_IHSexport.csv";
		// String destFileString = dataDir + File.separator + fileName; // Travant original
		String destFileString = dataDir + System.getProperty("file.separator") + fileName; // AJE replaced 2016-11-02
		  destFileString = destFileString.replace('\\','/');
    Logger.info("buildIhsCsv has dataDir = " +dataDir+ " ;  fileName = " +fileName+ " ; destFileString = " +destFileString);

		// Add header: csvHeader taken from SHOW CREATE TABLE `ihstitle`
    String csvHeader = "titleID|titleTypeID|title|alphaTitle|printISSN|eISSN|oclcNumber|lccn|publisherID|description|";
           csvHeader += "titleStatusID|changeDate|userID|titleVersion|imagePageRatio|language|country|volumeLevelFlag|";
           csvHeader += "builderHolding"; // not in table
      try{
  		  FileOutputStream fos = new FileOutputStream(destFileString, true); // 'true' for APPEND
        byte[] bytesArray = csvHeader.toString().getBytes();
        fos.write(bytesArray);
        bytesArray = "\n".getBytes();
        fos.write(bytesArray);
    		fos.close();
        fos.flush();
        //Logger.info("buildIhsCsv: csvHeader written successfully at " +destFileString);
      } catch (IOException e) { // TODO Auto-generated
  		  Logger.info("buildIhsCsv error with writing csvHeader: \n" +e);
      }

		int titleIndex = 0;
    //Logger.info("buildIhsCsv: next is for IhsTitle; ihsTitles.size()=" +ihsTitles.size());
		for (IhsTitle ihsTitle : ihsTitles) {

      //Logger.info("buildIhsCsv: inside for (IhsTitle ihsTitle : ihsTitles); ihsTitles.size() = " +ihsTitles.size()+ "; titleIndex = " +titleIndex);

      Integer titleID = ihsTitle.titleID; // int(11) NOT NULL AUTO_INCREMENT,
      //Integer titleTypeID = ihsTitle.titleTypeID; // int(11) NOT NULL // NOT IN THE DATA
      Integer titleTypeID = -1;
      String title = ihsTitle.title != null ? ihsTitle.title : ""; // varchar(512) NOT NULL,
      //Logger.info("....ihsTitles.title = " +ihsTitle.title+ " ; local var 'title' = " +title+ ".");

      String alphaTitle = ihsTitle.alphaTitle != null ? ihsTitle.alphaTitle : ""; // varchar(512) DEFAULT NULL,
      String printISSN = ihsTitle.printISSN != null ? Helper.formatIssn(ihsTitle.printISSN) : ""; // varchar(32) DEFAULT NULL,
      String eISSN = ihsTitle.eISSN != null ? Helper.formatIssn(ihsTitle.eISSN) : ""; // varchar(32) DEFAULT NULL,
      String oclcNumber = ihsTitle.oclcNumber != null ? ihsTitle.oclcNumber : ""; // varchar(32) DEFAULT NULL,
      String lccn = ihsTitle.lccn != null ? ihsTitle.lccn : ""; // varchar(32) DEFAULT NULL,
      //Integer publisherID = ihsTitle.publisherID; // int(11) NOT NULL // NOT IN THE DATA
      Integer publisherID = -1;
      String description = ihsTitle.description != null ? ihsTitle.description : ""; // varchar(512) DEFAULT NULL,
      //Integer titleStatusID = ihsTitle.titleStatusID; // int(11) NOT NULL // NOT IN THE DATA
      Integer titleStatusID = -1;
      String changeDate = ihsTitle.changeDate != null ? ihsTitle.changeDate.toString() : ""; // date NOT NULL,
      //Integer userID = ihsTitle.userId; // int(11) NOT NULL // NOT IN THE DATA
      Integer userID = -1;
      //Integer titleVersion = ihsTitle.titleVersion; // int(11) DEFAULT '0' // NOT IN THE DATA
      Integer titleVersion = -1;
      //Integer imagePageRatio = ihsTitle.imagePageRatio != null ? ihsTitle.imagePageRatio : -1; //int(11) DEFAULT '0' // NOT IN THE DATA
      Integer imagePageRatio = 0; // int(11) DEFAULT NULL,
      String language = ihsTitle.language != null ? ihsTitle.language : ""; // varchar(32) NOT NULL,
      String country = ihsTitle.country != null ? ihsTitle.country : ""; // varchar(32) NOT NULL,
      //Integer volumeLevelFlag = ihsTitle.volumeLevelFlag; // char(1) DEFAULT '0' // NOT IN THE DATA
      Integer volumeLevelFlag = -1;

      String titleCSVcontent = Integer.toString(titleID) + "|" + Integer.toString(titleTypeID) + "|";
        titleCSVcontent += title + "|" + alphaTitle + "|" + printISSN + "|" + eISSN + "|" + oclcNumber + "|" + lccn + "|" ;
        titleCSVcontent += Integer.toString(publisherID) + "|" + description + "|" + Integer.toString(titleStatusID) + "|";
        titleCSVcontent += changeDate.substring(0,10) + "|";
        titleCSVcontent += Integer.toString(userID) + "|";
        titleCSVcontent += Integer.toString(titleVersion) + "|";
        titleCSVcontent += Integer.toString(imagePageRatio) + "|" ;
        titleCSVcontent += language + "|" + country + "|" +Integer.toString(volumeLevelFlag) + "|";

			StringBuilder builderHolding = new StringBuilder();
			boolean startVolume = true;
			boolean startIssue = true;

			for(IhsVolume ihsVolume: ihsTitle.ihsVolume){
        int volumeIndex = 0;

        if(startVolume && startIssue) {
          //Logger.info("titleMRKcontent: inside for (IhsVolume ihsVolume: ihsTitle.ihsVolume); ihsTitle.ihsVolume.size()=" +ihsTitle.ihsVolume.size()+ "; volumeIndex = " +volumeIndex);
        }

				if(!startVolume) {
				  builderHolding.append(",");
				}
				startVolume = false;
				builderHolding.append("v.").append(ihsVolume.volumeNumber).append("(");

        int issueIndex = 0;
				for(IhsIssue ihsissue: ihsVolume.ihsissues){

          if(startVolume && startIssue) {
            //Logger.info("titleCSVcontent: inside for (IhsIssue ihsissue: ihsVolume.ihsissues); ihsVolume.ihsissues.size()=" +ihsVolume.ihsissues.size()+ "; issueIndex = " +issueIndex);
          }
					if(!startIssue) {
					  builderHolding.append(",");
					}
					startIssue=false;
					builderHolding.append(ihsissue.issueNumber);

					issueIndex++;
				} // end for IhsIssue ihsissue

				builderHolding.append(")");
				startIssue = true;
				volumeIndex++;

			} // end for IhsVolume ihsVolume

      titleCSVcontent += builderHolding;
      //Logger.info("titleCSVcontent now contains builderHolding: "+titleCSVcontent);
      try{
        /*
        // AJE 2016-12-09 : this version does not produce UTF-8
        FileOutputStream fos = new FileOutputStream(destFileString, true); // 'true' for APPEND to file
        byte[] bytesArray = titleCSVcontent.toString().getBytes();
        fos.write(bytesArray);
        bytesArray = "\n".getBytes();
        fos.write(bytesArray);
        fos.close();
        fos.flush();
        //Logger.info("buildIhsCsv: titleCSVcontent + newline written successfully at " +destFileString);
        */
        // AJE 2016-12-09 : this version should produce UTF-8: http://stackoverflow.com/questions/1001540/how-to-write-a-utf-8-file-with-java?rq=1
        java.io.Writer bwfos = new java.io.BufferedWriter(new java.io.OutputStreamWriter(
          new FileOutputStream(destFileString, true), // 'true' for APPEND to file
          "UTF-8")
        );
        //or
        //Writer bwfos = new java.io.PrintWriter(new java.io.File(destFileString), "UTF-8");

        //byte[] bytesArray = titleCSVcontent.toString().getBytes();
        //fos.write(bytesArray);
        bwfos.write(titleCSVcontent);
        bwfos.write("\n");
       // bwfos.close();
        bwfos.flush();
Logger.info("buildIhsCsv: titleCSVcontent + newline UTF-8 written successfully with bwfos at " +destFileString);
      } catch (IOException e) { // TODO Auto-generated
      Logger.info("buildIhsCsv error with writing titleCSVcontent: \n" +e);
      }
			titleIndex++;
		} // end for ihsTitles

		return destFileString;

	} // end buildIhsCsv
}
