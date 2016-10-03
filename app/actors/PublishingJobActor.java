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

public class PublishingJobActor extends UntypedActor {

	static Random rand = new Random();

	@Override
	public void onReceive(Object message) throws Exception {

    Logger.info("PublishingJobActor.onReceive begins with message=" +message); // AJE 2016-09-30
    Logger.info("and (message instanceof Integer) == "+ (message instanceof Integer)+ ".");

		if (message instanceof Integer) {

			Integer ihsPublishingJobId = (Integer) message;

			List<IhsTitle> ihsTitles = null;

			IhsPublishingJob ihsPublishingJob = IhsPublishingJob.find
					.byId(ihsPublishingJobId);

			String link = "";

			Logger.info("PublishingJobActor.onReceive. 'Start Running Publishing job': "+
			  "ihsPublishingJob.startDate='" +ihsPublishingJob.startDate+"', "+
			  "ihsPublishingJob.endDate='"+ihsPublishingJob.endDate+"'.");

      Logger.info("In the dates , the year and day of month is probably right, but note that the month is always '01'?");

			if (ihsPublishingJob.startDate != null
					&& ihsPublishingJob.endDate != null) {
				ihsTitles = IhsTitle.find.fetch("ihsPublisher")
						.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
						.fetch("ihsPublicationRange").where()
						.ge("changeDate", ihsPublishingJob.startDate)
						.le("changeDate", ihsPublishingJob.endDate).findList();
			} else if (ihsPublishingJob.startDate != null) {
				ihsTitles = IhsTitle.find.fetch("ihsPublisher")
						.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
						.fetch("ihsPublicationRange").where()
						.ge("changeDate", ihsPublishingJob.startDate)
						.findList();
			} else if (ihsPublishingJob.endDate != null) {
				ihsTitles = IhsTitle.find.fetch("ihsPublisher")
						.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
						.fetch("ihsPublicationRange").where()
						.le("changeDate", ihsPublishingJob.endDate).findList();
			} else {
				ihsTitles = IhsTitle.find.fetch("ihsPublisher")
						.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
						.fetch("ihsPublicationRange").findList();
			}

      Logger.info("PublishingJobActor.onReceive: ihsTitles is the parameter to "+
        //"buildMArk/buildPortico/buildIhsXls, or buildIhsCsv, and it is '"+ihsTitles+"'.");
        "build* functions [is too long to do on screen].");

			try {
				if (ihsPublishingJob.fileformat == 1) {

					Logger.info("ihsPublishingJob.fileformat == "+ihsPublishingJob.fileformat+
					  ", thus will call buildMArk. The value of 'link' is never set in this block.");

          // AJE 2016-09-30 buildMArk was delivered as empty brackets by Travant
					buildMArk(ihsTitles);

				} else if (ihsPublishingJob.fileformat == 2) {

					Logger.info("ihsPublishingJob.fileformat == "+ihsPublishingJob.fileformat+
  					", thus will call buildPortico, which may cause "+
  					"'java.lang.OutOfMemoryError: Java heap space.'"+
            " This is the only block where the value of 'link' is set.");

          link = buildPortico(ihsTitles);

				} else if (ihsPublishingJob.fileformat == 3) {

					Logger.info("ihsPublishingJob.fileformat == "+ihsPublishingJob.fileformat+
					  ", thus will call buildIhsXls. The value of 'link' is never set in this block.");

          // AJE 2016-09-30 buildIhsXls was delivered as empty brackets by Travant
					buildIhsXls(ihsTitles);

				} else if (ihsPublishingJob.fileformat == 4) {

					Logger.info("ihsPublishingJob.fileformat == "+ihsPublishingJob.fileformat+
					  ", thus will call buildIhsCsv. The value of 'link' is never set in this block.");

          // AJE 2016-09-30 buildIhsCsv was delivered as empty brackets by Travant
					buildIhsCsv(ihsTitles);

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
      */

				ihsPublishingJob.setLink("<a href=/public/exported/AJE_dummy_export.txt>" + "Download</a>");
/*
AJE 2016-09-30
NOTE THAT UNLIKE THE SECTION 2 CALLING buildPortico,
CALLING THE OTHER build* FUNCTIONS DOES NOT SET link = THE RETURN VALUE OF THOSE FUNCTIONS
*/

Logger.info("PublishingJobActor.java, onReceive, BEFORE replace has ihsPublishingJob.link = " +ihsPublishingJob.link);
Logger.info("... using AJE simplified link.replace next");
link = link.replace('\\','/');
Logger.info("PublishingJobActor.java, onReceive, AFTER replace has ihsPublishingJob.link = " +ihsPublishingJob.link);
//ihsPublishingJob.setLink("<a href=" + link + ">Download</a>");
ihsPublishingJob.update();
//Logger.info("... NEW GET in routes for /public/reports ; new value in application.conf for application.PUBLISHING.process.data.Dir.  Yes, publishing is supposedly a different thing from reporting, and yes, there is an application.REPORTING.process.data.Dir");
//Logger.info("AJE 2016-09-30 why no 'href' in link: " +link+ " ?  It is present in database.ihsreportingjob.link field.");

				Logger.info("PublishingJobActor.java: onReceive: Done Running publishing job");

        // AJE 2016-09-30 resume Travant original code

			} catch (Exception e) {

				SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
						.where().eq("name", SingestionJobStatus.InternalError)
						.findUnique();

				ihsPublishingJob.setSingestionJobStatus(singestionJobStatus);
				ihsPublishingJob.update();
				Logger.error("PublishingJobActor has error: ", e);
			}
		} // end if (message instanceof Integer)

	} // end onReceive

	void buildMArk(List<IhsTitle> ihsTitles) {
	  // AJE 2016-09-30 read the Logger: there was nothing here before today
    Logger.info("buildMArk was delivered as empty brackets by Travant: all it does it print this message"); // AJE 2016-09-30
	}

	String buildPortico(List<IhsTitle> ihsTitles) throws IOException {

    Logger.info("PublishingJobActor.java: enter buildPortico: May cause 'java.lang.OutOfMemoryError: Java heap space.'"); // AJ 2016-09-30
    Logger.info("PublishingJobActor.java: enter buildPortico: did not crash when only a 5 day range chosen, but did for 30"); // AJ 2016-09-30


		String dataDir = Play.application().configuration()
				.getString("application.publishing.process.data.Dir");

    Logger.info("buildPortico has dataDir = " +dataDir);

		String fileName = rand.nextInt(10000) + "-" + "portico.xlsx";

    Logger.info("buildPortico has fileName = " +fileName);

		String destFileString = dataDir + File.separator + fileName;

    Logger.info("buildPortico has destFileString = " +destFileString);

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

		for (IhsTitle ihsTitle : ihsTitles) {


      Logger.info("buildPortico: for (IhsTitle ihsTitle : ihsTitles) rowIndex)" +rowIndex);

			Row childrow = sheet.createRow(rowIndex++);

			Cell pubcell = childrow.createCell(0);
			pubcell.setCellValue(ihsTitle.ihsPublisher.name);

			pubcell.setCellStyle(style);

			Cell titlecell = childrow.createCell(1);
			titlecell.setCellValue(ihsTitle.title);
			titlecell.setCellStyle(style);

			Cell societycell = childrow.createCell(2);
			societycell.setCellValue("");

			String pISSN = ihsTitle.printISSN != null ? Helper.formatIssn(ihsTitle.printISSN) :"";
			Cell printISSNcell = childrow.createCell(3);
			printISSNcell.setCellValue(pISSN);

			String eISSN = ihsTitle.eISSN != null ? Helper.formatIssn(ihsTitle.eISSN) :"";
			Cell eISSNcell = childrow.createCell(4);
			eISSNcell.setCellValue(eISSN);

			Cell pcacell = childrow.createCell(5);
			pcacell.setCellValue("");

			Cell statuscell = childrow.createCell(6);
			statuscell.setCellValue("");

			StringBuilder builderHolding = new StringBuilder();
			boolean startVolume = true;
			boolean startIssue = true;
			for(IhsVolume ihsVolume: ihsTitle.ihsVolume){

      Logger.info("buildPortico: for(IhsVolume ihsVolume: ihsTitle.ihsVolume) rowIndex)" +rowIndex);

				if(!startVolume) builderHolding.append(",");
				startVolume=false;

				builderHolding.append("v.").append(ihsVolume.volumeNumber).append("(");

				for(IhsIssue ihsissue: ihsVolume.ihsissues){

				  Logger.info("buildPortico: for(IhsIssue ihsissue: ihsVolume.ihsissues) rowIndex)" +rowIndex);

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

	void buildIhsCsv(List<IhsTitle> ihsTitles) {
	  // AJE 2016-09-30 read the Logger: there was nothing here before today
    Logger.info("buildIhsCsv was delivered as empty brackets by Travant: all it does it print this message"); // AJE 2016-09-30
	}
}
