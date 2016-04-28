import org.joda.time.DateTime;

import models.IhsAddress;
import models.IhsAuthorizedSource;
import models.IhsIngestionJob;
import models.IhsLocation;
import models.IhsMember;
import models.IhsPublicationRange;
import models.IhsPublisher;
import models.IhsSecurityRole;
import models.IhsTitle;
import models.IhsUser;
import models.SholdingStatus;
import models.SingestionDatatype;
import models.SingestionExceptionStatus;
import models.SingestionExceptionType;
import models.SingestionJobStatus;
import models.SingestionRecordStatus;
import models.SissueStatus;
import models.SmemberStatus;
import models.SperiodicityType;
import models.StitleStatus;
import models.StitleType;
import models.SuserStatus;
import actors.AppActorSystem;
import play.*;
import play.api.mvc.EssentialFilter;
import static play.mvc.Results.*;
import play.filters.gzip.GzipFilter;
import util.DailyCleanUpJob;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


//public class Global extends GlobalSettings{
public class Global extends GlobalSettings {

	Scheduler scheduler1;
	
	public <T extends EssentialFilter> Class<T>[] filters() {
		return new Class[] { GzipFilter.class };
	}

	private Logger.ALogger log = Logger.of(Global.class);

	public void onStart(Application app) {
		Logger.info("Application has started");

		// Chech

		//if (play.api.Play.isProd(play.api.Play.current())) {
			
			//System.out.println("prod");
			SingestionJobStatus singestionJobStatus = SingestionJobStatus.find
					.where().eq("name", SingestionJobStatus.Processing)
					.findUnique();
			IhsIngestionJob ihsIngestionJob = IhsIngestionJob.find.where()
					.eq("singestionJobStatus", singestionJobStatus)
					.findUnique();

			if (ihsIngestionJob != null) {
				SingestionJobStatus singestionJobStatusCom = SingestionJobStatus.find
						.where().eq("name", SingestionJobStatus.Incomplete)
						.findUnique();
				IhsIngestionJob.updateJobId(singestionJobStatusCom,
						ihsIngestionJob);
			}
			
			//set up a quartz cron job
			

			JobDetail job1 = JobBuilder.newJob(DailyCleanUpJob.class)
					.withIdentity("DailyCleanUpJob", "DailyCleanUpJobGroup").build();
			
			
			Trigger trigger1 = TriggerBuilder.newTrigger()
					.withIdentity("DailyCleanUpJobCronTrigger", "DailyCleanUpJobGroup")
					//.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")) //Every file second
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")) //Run every midnight
					.build();
			
			
			try {
				scheduler1 = new StdSchedulerFactory().getScheduler();
			
			scheduler1.start();
			scheduler1.scheduleJob(job1, trigger1);
			
			} catch (SchedulerException e) {
				Logger.error("Can't start Daily clean up job", e);
				System.exit(1);
			}
			
			// End cron job set up
			
		if (play.api.Play.isDev(play.api.Play.current())) {
			System.out.println("Dev");

			if (SmemberStatus.find.byId(1) == null) {
				SmemberStatus smemberStatus = new SmemberStatus("Active",
						"ActveDesc");
				smemberStatus.save();

				IhsMember ihsMember = new IhsMember("PAPR", "PARP",
						smemberStatus, "papr");
				ihsMember.save();

				IhsMember ihsMember1 = new IhsMember("TRAVANT", "TRAVANT",
						smemberStatus, "travant");
				ihsMember1.save();

				IhsAddress ihsAddress = new IhsAddress("address1", "address2",
						"ciry", "ST", "ZIP", "ZIP+", "CT", null);
				ihsAddress.save();

				IhsLocation ihsLocation = new IhsLocation("loc1", "locdesc",
						ihsMember, ihsAddress);
				ihsLocation.save();

				IhsLocation ihsLocation1 = new IhsLocation("loc1", "locdesc",
						ihsMember1, ihsAddress);
				ihsLocation1.save();

				IhsSecurityRole ihsSecurityRole1 = new IhsSecurityRole("admin",
						"admin");
				IhsSecurityRole ihsSecurityRole2 = new IhsSecurityRole("user",
						"user");

				ihsSecurityRole1.save();
				ihsSecurityRole2.save();

				SuserStatus suserStatus = new SuserStatus(SuserStatus.Active,
						SuserStatus.Active);
				suserStatus.save();

				IhsUser ihsUser = new IhsUser("Amy", "Wood", ihsMember,
						suserStatus, "awood", "password");
				IhsUser ihsUser1 = new IhsUser("Scot", "Sax", ihsMember1,
						suserStatus, "ssax", "password");
				IhsUser ihsUser2 = new IhsUser("Reza", "Rahim", ihsMember,
						suserStatus, "rrahim", "password");

				ihsUser.ihsSecurityRoles.add(ihsSecurityRole1);
				ihsUser.ihsSecurityRoles.add(ihsSecurityRole2);

				ihsUser1.ihsSecurityRoles.add(ihsSecurityRole1);

				ihsUser2.ihsSecurityRoles.add(ihsSecurityRole2);

				ihsUser.save();
				ihsUser1.save();
				ihsUser2.save();

				new IhsAuthorizedSource("PAPR", "AuthDesc", ihsMember).save();

				new SissueStatus(SissueStatus.Default, SissueStatus.Default)
						.save();

				new SholdingStatus(SholdingStatus.Default,
						SholdingStatus.Default).save();

				StitleStatus stitleStatus = new StitleStatus("Active", "Active");
				stitleStatus.save();

				StitleType stitleType = new StitleType("Default", "Default");
				stitleType.save();

				IhsPublisher ihsPublisher = new IhsPublisher("PubName-title",
						"PubDesc", null, null, ihsAddress);
				ihsPublisher.save();

				SperiodicityType speriodicityType = new SperiodicityType(
						SperiodicityType.Default, SperiodicityType.Default, 1);
				speriodicityType.save();

				IhsTitle title1 = new IhsTitle("Museum Anthropology",
						stitleType /* type */, "Museum Anthropology",
						"15563618", "15563626", "oclcNumber", "lccn",
						ihsPublisher/* pub */, "description", stitleStatus/* status */);
				title1.save();

				new IhsPublicationRange(title1, null, new DateTime() , new DateTime()).save();
				
				IhsTitle title2 = new IhsTitle("Africa Bibliography",
						stitleType /* type */, "Africa Bibliography",
						"15563619", "15563626", "oclcNumber", "lccn",
						ihsPublisher/* pub */, "description", stitleStatus/* status */);
				title2.save();

				new IhsPublicationRange(title2, null, new DateTime() , new DateTime()).save();
				
				IhsTitle title3 = new IhsTitle("Africa Education Review",
						stitleType /* type */, "Africa Education Review",
						"15563620", "15563626", "oclcNumber", "lccn",
						ihsPublisher/* pub */, "description", stitleStatus/* status */);
				title3.save();

				new IhsPublicationRange(title3, null, new DateTime() , new DateTime()).save();
				
				IhsTitle title4 = new IhsTitle("Africa Today",
						stitleType /* type */, "Africa Today",
						"15563621", "15563626", "oclcNumber", "lccn",
						ihsPublisher/* pub */, "description", stitleStatus/* status */);
				title4.save();

				new IhsPublicationRange(title4, null, new DateTime() , new DateTime()).save();
				
				IhsTitle title5 = new IhsTitle("African Arts",
						stitleType /* type */, "African Arts",
						"15563622", "15563626", "oclcNumber", "lccn",
						ihsPublisher/* pub */, "description", stitleStatus/* status */);
				title5.save();

				new IhsPublicationRange(title5, null, new DateTime() , new DateTime()).save();
				
				
				/*--------------------SingestionJobStatus  --------------*/

				new SingestionJobStatus(SingestionJobStatus.Queued,
						SingestionJobStatus.Queued).save();
				new SingestionJobStatus(SingestionJobStatus.Processing,
						SingestionJobStatus.Processing).save();
				new SingestionJobStatus(
						SingestionJobStatus.FileProcessingError,
						SingestionJobStatus.FileProcessingError).save();
				new SingestionJobStatus(SingestionJobStatus.Complete,
						SingestionJobStatus.Complete).save();
				new SingestionJobStatus(SingestionJobStatus.InternalError,
						SingestionJobStatus.InternalError).save();

				new SingestionDatatype("Portico", "Portico Excel File (.csv)")
						.save();

				new SingestionDatatype("CLOCKSS", "CLOCKSS").save();
				new SingestionDatatype("CIC_SPR", "CIC_SPR").save();

				new SingestionRecordStatus(SingestionRecordStatus.Available,
						SingestionRecordStatus.Available).save();
				new SingestionRecordStatus(SingestionRecordStatus.OnHold,
						SingestionRecordStatus.OnHold).save();
				new SingestionRecordStatus(SingestionRecordStatus.Processing,
						SingestionRecordStatus.Processing).save();
				new SingestionRecordStatus(
						SingestionRecordStatus.BadRecordError,
						SingestionRecordStatus.BadRecordError).save();
				new SingestionRecordStatus(SingestionRecordStatus.Ingnored,
						SingestionRecordStatus.Ingnored).save();

				new SingestionRecordStatus(SingestionRecordStatus.Complete,
						SingestionRecordStatus.Complete).save();

				new SingestionExceptionType(
						SingestionExceptionType.BadRecordformat,
						SingestionExceptionType.BadRecordformat).save();

				new SingestionExceptionType(
						SingestionExceptionType.BadYearformat,
						SingestionExceptionType.BadYearformat).save();
				new SingestionExceptionType(
						SingestionExceptionType.BadPrintISSN,
						SingestionExceptionType.BadPrintISSN).save();
				new SingestionExceptionType(
						SingestionExceptionType.BadHoldingFormat,
						SingestionExceptionType.BadHoldingFormat).save();

				new SingestionExceptionType(
						SingestionExceptionType.NoAuthorizedPublicationRange,
						SingestionExceptionType.NoAuthorizedPublicationRange)
						.save();
				new SingestionExceptionType(
						SingestionExceptionType.NoAuthorizedTitle,
						SingestionExceptionType.NoAuthorizedTitle).save();

				new SingestionExceptionStatus(
						SingestionExceptionStatus.Available, "Available")
						.save();

			}

		}

		// clean any proceeing job

	}

	public void onStop(Application app) {
		Logger.info("Application shutdown...");
		try {
			scheduler1.shutdown();
		} catch (SchedulerException e) {
			
			Logger.error("",e);
		}
	}

	public Status onBadRequest(String uri, String error) {
		return badRequest("Don't try to hack the URI!");
	}
}
