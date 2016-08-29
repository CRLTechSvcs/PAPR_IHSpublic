package actors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import json.NewReportView;
import json.TitleVersionView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;

import controllers.AdvancedEditing;
import controllers.Reporting;
import models.IhsIngestionRecord;
import models.IhsMember;
import models.IhsPublishingJob;
import models.IhsReportingJob;
import models.IhsTitle;
import models.IhsTitleVersion;
import models.SingestionJobStatus;
import play.Logger;
import reports.BibliographicHistory;
import reports.IssuesHeldReport;
import akka.actor.UntypedActor;


// AJE 2016-08-23 for test 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
// AJE 2016-08-23 for test 

public class ReportingActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
			
Logger.info("ReportingActor.java, begin onReceive(message='" +message+ "')");			
			
		try {
			if (message instanceof Integer) {

				Integer ihsReportingJobId = (Integer) message;

				IhsReportingJob ihsReportingJob = IhsReportingJob.find
						.byId(ihsReportingJobId);

				String link = "";

				Logger.debug("Start Running  job");

				ObjectMapper mapper = new ObjectMapper();
				
				
				NewReportView newReportView = mapper.readValue(
						ihsReportingJob.jsonString, NewReportView.class);

				SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
						.where().eq("name", SingestionJobStatus.Processing)
						.findUnique();
				
				ihsReportingJob.setSingestionJobStatus(singestionJobStatus);
				
				if (Reporting.IssuesHeld.equals(newReportView.report)) {
					link = buildIssuesHeld(newReportView,
							ihsReportingJob.dateInitiated);
				} else if (Reporting.JournalFamily.equals(newReportView.report)) {
					link = buildJournalFamily(newReportView, ihsReportingJob.dateInitiated );
				} else if (Reporting.BibliographicHistory
						.equals(newReportView.report)) {
					link = buildBibliographicHistory(newReportView, ihsReportingJob.dateInitiated);
				}

				singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
						.where().eq("name", SingestionJobStatus.Complete)
						.findUnique();

				ihsReportingJob.setSingestionJobStatus(singestionJobStatus);

				/*
				 * 		/ingestion/getBadformatFile/ is route to controllers.IngestionJob.getBadformatFile
				 * 		
				 */
				 /* 
				 		AJE 2016-08-09 Travant's above comment appears to be an artifact of a cut and paste job that has no reference to the actual location of where PDF reports are actually put once created: it matches one in Deaccession.java, and in any case Reporting is not a function related to Ingestion 
				 		- in /conf/application.conf : we have 'application.reporting.process.data.Dir="data/pubprocess"' 
				 		
						AJE 2016-08-25 
						 - in a truly egregious example of poor setup and copy-pasting, Travant not only reused 
						 		controllers.IngestionJob.getBadformatFile 
						 	in a section of the system that had nothing to do with ingestion, 
						 	it then configured (in application.conf)
						 		application.PUBLISHING.process.data.Dir with the path () for the report files, 
						 	(as of this writing I still don't know where that is relevant), 
						 	instead of the already existing 
						 		application.REPORTING.process.data.Dir

						The point here is that there is so much copy-pasting and reuse of code among the various 'modules' or 'actors' or 'controllers' 
							that even the very names of the configuration variables are suspect and 
							cannot be relied on to point the way to the actual portions of code that, 
							based on those names, one would assume to affect the part of the system you'd think they do.
						 	
				*/
				 		
				/* Travant original 2016-08-09
				ihsReportingJob.setLink("<a href=/ingestion/getBadformatFile/" 
						+ link.replace(System.getProperty("file.separator"),
								IhsIngestionRecord.tilde) + ">"
						+ "Download</a>");
				// end Travant original ; replaced 2016-08-09 */ 

Logger.info("AJE 2016-08-25 ReportingActor.java, onReceive has link = " +link);	
Logger.info("... NEW GET in routes for /public/reports ; new value in application.conf for application.PUBLISHING.process.data.Dir.  Yes, publishing is supposedly a different thing from reporting, and yes, there is an application.REPORTING.process.data.Dir");
/* 
Logger.info("... revert to Travant's system and see if still works");	
			ihsReportingJob.setLink("<a href=" 
						+ link.replace(System.getProperty("file.separator"),
								IhsIngestionRecord.tilde) + ">"
						+ "Download</a>");
// hint : it didn't and I'm sick of messing with this: use AJE version 
// WHY DO THEY EVEN REPLACE IT WITH TILDE IN THE FIRST PLACE ???!!??
*/

//AJE version of 'replace forward slash with back slash, and wrap it in a link tag
	// this stuff has always worked, but is dependent on the values in files: application.conf, and 'routes'
Logger.info("... using AJE simplified link.replace next");	
link = link.replace('\\','/'); 
ihsReportingJob.setLink("<a href=" + link + ">Download</a>");

Logger.info("AJE 2016-08-25 Link after: " +link);	
Logger.info("AJE 2016-08-24 why no 'href' in link: " +link+ " ?  It is present in database.ihsreportingjob.link field.");				

				// AJE: resume Travant original
				
				ihsReportingJob.update();

				Logger.info("ReportingActor.java: onReceive: Done Running publishing job");
			}
		} catch (Exception e) {
			Logger.error("",e) ;
			Logger.info("ReportingActor.java: onReceive has error: " +e); //AJE 2016-08-25 

		}

	}

	String buildIssuesHeld(NewReportView newReportView, DateTime dateInitiated) {

		try {
			IhsMember ihsMember = IhsMember.find.byId(newReportView.memberId);

			List<IhsTitle> ihsTitles = IhsTitle.find.fetch("ihsPublisher")
					.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
					.fetch("ihsVolume.ihsissues.ihsHoldings").where()
					.eq("ihsVolume.ihsissues.ihsHoldings.ihsMember", ihsMember)
					.order().asc("ihsVolume.startDate")
					.order().asc("ihsVolume.ihsissues.publicationDate")
					.findList();

			Logger.info("ReportingActor.java : buildIssuesHeld TRY block will call IssuesHeldReport() and return that."); // AJE 2016-08-25

			return new IssuesHeldReport().createPdf(newReportView, dateInitiated, ihsTitles);
			
		} catch (Exception e) { // AJE 2016-08-24 expanded error messaging
			Logger.error("ReportingActor.java : buildIssuesHeld has error: ", e);
			Logger.info("ReportingActor.java : buildIssuesHeld has error: ", e);

		}

		Logger.info("ReportingActor.java : buildIssuesHeld AFTER try block will return empty string."); // AJE 2016-08-25

		return "";
		 
	}

	String buildJournalFamily(NewReportView newReportView, DateTime dateInitiated) {

		Logger.info("AJE 2016-08-24 ReportingActor.java : buildJournalFamily was delivered with nothing but return empty String");

		return "";
	}

	String buildBibliographicHistory(NewReportView newReportView, DateTime dateInitiated) {

		try {
			
			
			List<TitleVersionView> titleVersionViews = AdvancedEditing.BuildTitleVersionView(newReportView.titleId);
			
			return new BibliographicHistory().createPdf(newReportView, titleVersionViews, dateInitiated);
		
		} catch (Exception e) {  // AJE 2016-08-24 expanded error messaging
			// TODO Auto-generated catch block
			Logger.error("ReportingActor.java : buildBibliographicHistory has error: ", e);
			Logger.info("ReportingActor.java : buildBibliographicHistory has error: ", e);

		}
		return "";
		
	}
	
	
}