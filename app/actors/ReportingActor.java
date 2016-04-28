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

public class ReportingActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {

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
				ihsReportingJob.setLink("<a href=/ingestion/getBadformatFile/"
						+ link.replace(System.getProperty("file.separator"),
								IhsIngestionRecord.tilde) + ">"
						+ "Download</a>");
				ihsReportingJob.update();

				Logger.info("Done Running publisng job");
			}
		} catch (Exception e) {
			Logger.error("",e) ;
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

			return new IssuesHeldReport().createPdf(newReportView, dateInitiated, ihsTitles);
			
		} catch (Exception e) {
			Logger.error("", e);
		}

		return "";
		 
	}

	String buildJournalFamily(NewReportView newReportView, DateTime dateInitiated) {

		return "";
	}

	String buildBibliographicHistory(NewReportView newReportView, DateTime dateInitiated) {

		try {
			
			
			List<TitleVersionView> titleVersionViews = AdvancedEditing.BuildTitleVersionView(newReportView.titleId);
			
			return new BibliographicHistory().createPdf(newReportView, titleVersionViews, dateInitiated);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.error("", e);
		}
		return "";
		
	}
}