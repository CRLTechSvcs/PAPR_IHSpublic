package util;

import java.io.File;
import java.util.List;

import models.IhsDeaccessionJob;
import models.IhsIngestionJob;
import models.IhsPublishingJob;
import models.IhsReportingJob;

import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import play.Logger;

public class DailyCleanUpJob implements Job {

	static int days = 45;

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		Logger.info("Starting DailyCleanUpJob");

		List<IhsIngestionJob> ihsIngestionJobs = IhsIngestionJob.find.where()
				.le("creationDate", new DateTime().minusDays(days)).findList();

		for (IhsIngestionJob ihsIngestionJob : ihsIngestionJobs) {

			try {
				File file = new File(ihsIngestionJob.sourceFileString);
				if (file.delete()) {
					Logger.info(ihsIngestionJob.sourceFileString
							+ " file can't be deleted");
				} else {
					Logger.error(ihsIngestionJob.sourceFileString
							+ " file can't be deleted");
				}

				
			} catch (Exception e) {
				Logger.error( ihsIngestionJob.sourceFileString + " file can't be deleted", e);
			}
			finally{
				ihsIngestionJob.delete();
			}
		}

		List<IhsDeaccessionJob> ihsDeaccessionJobs = IhsDeaccessionJob.find
				.where().le("dateInitiated", new DateTime().minusDays(days))
				.findList();

		for (IhsDeaccessionJob ihsDeaccessionJob : ihsDeaccessionJobs) {
			try {
				File file = new File(ihsDeaccessionJob.link);
				if (file.delete()) {
					Logger.info(ihsDeaccessionJob.link
							+ " file can't be deleted");
				} else {
					Logger.error(ihsDeaccessionJob.link
							+ " file can't be deleted");
				}

				ihsDeaccessionJob.delete();
			} catch (Exception e) {
				Logger.error( ihsDeaccessionJob.link + " file can't be deleted", e);
			}
			finally{
				ihsDeaccessionJob.delete();
			}

		}

		
		List<IhsReportingJob> ihsReportingJobs = IhsReportingJob.find.where()
				.le("dateInitiated", new DateTime().minusDays(days)).findList();

		
		for (IhsReportingJob ihsReportingJob : ihsReportingJobs) {

			try {
				File file = new File(ihsReportingJob.link);
				if (file.delete()) {
					Logger.info(ihsReportingJob.link + " file can't be deleted");
				} else {
					Logger.error(ihsReportingJob.link
							+ " file can't be deleted");
				}

			} catch (Exception e) {
				Logger.error( ihsReportingJob.link + " file can't be deleted", e);
			}
			finally{
				ihsReportingJob.delete();
			}
		}
		

		List<IhsPublishingJob> ihsPublishingJobs = IhsPublishingJob.find
				.where().le("dateInitiated", new DateTime().minusDays(days))
				.findList();

		for (IhsPublishingJob ihsPublishingJob : ihsPublishingJobs) {

			try {
				File file = new File(ihsPublishingJob.link);
				if (file.delete()) {
					Logger.info(ihsPublishingJob.link
							+ " file can't be deleted");
				} else {
					Logger.error(ihsPublishingJob.link
							+ " file can't be deleted");
				}

				

			} catch (Exception e) {
				Logger.error( ihsPublishingJob.link + " file can't be deleted");
			}
			finally{
				ihsPublishingJob.delete();
			}
		}
		Logger.info("Done DailyCleanUpJob");

	}

}
