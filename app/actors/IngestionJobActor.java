package actors;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;


import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;

import play.Logger;
import models.IhsIngestionJob;
import models.IhsIngestionRecord;
import models.SingestionJobStatus;
import models.SingestionRecordStatus;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import scala.concurrent.duration.Duration;
import akka.actor.ReceiveTimeout;

public class IngestionJobActor extends UntypedActor {

	boolean running = false;
	IhsIngestionJob ihsIngestionJob = null;
	String timeOut = "30 seconds";


	// LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private ActorRef fileReaderActor = getContext().actorOf(
			Props.create(FileReaderActor.class), "FileReaderActor");

	public IngestionJobActor() {
		getContext().setReceiveTimeout(Duration.create(timeOut));
	}

	public IhsIngestionJob checkForQueuedJob() {

		/*
		 * Get oldest queued job from the database
		 */

		// next is Travant original line: casting (SingestionJobStatus) causes an error every 30 seconds or so "models.SingestionJobStatus cannot be cast to models.SingestionJobStatus"
		//SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find.where().eq("name", SingestionJobStatus.Queued).findUnique();
		// AJE 2016-11-02 new test version : has no effect, still
		SingestionJobStatus singestionJobStatus = SingestionJobStatus.find.where().eq("name", SingestionJobStatus.Queued).findUnique();

		List<IhsIngestionJob> ihsIngestionJobs = IhsIngestionJob.find.where()
				.eq("singestionJobStatus.ingestionJobStatusID", singestionJobStatus.ingestionJobStatusID)
				.orderBy("ingestionJobID desc").setMaxRows(1).findList();

		Iterator<IhsIngestionJob> iterator = ihsIngestionJobs.iterator();

		while (iterator.hasNext()) {
			running = true;
			ihsIngestionJob = iterator.next();
		}
		return ihsIngestionJob;
	}

	@Override
	public void onReceive(Object message) throws Exception {

		try {
			if (message instanceof ReceiveTimeout) {

				if (running) {
					Logger.debug("IngestionJobActor: Job running");
					return;
				} else {

					Logger.debug("IngestionJobActor:Let's find a queued job");
					ihsIngestionJob = null;

					ihsIngestionJob = checkForQueuedJob();

					if (ihsIngestionJob != null) {
						Logger.debug("IngestionJobActor: "
								+ ihsIngestionJob.sourceFileString);


						SingestionJobStatus singestionJobStatus = SingestionJobStatus.find.where().eq("name", SingestionJobStatus.Processing).findUnique();

						SqlUpdate update = Ebean.createSqlUpdate(IhsIngestionJob.upDateSql);
						update.setParameter("ingestionJobStatusID", singestionJobStatus.ingestionJobStatusID);
						update.setParameter("ingestionJobID", ihsIngestionJob.ingestionjobID);
						Ebean.execute(update);


						fileReaderActor.tell(ihsIngestionJob, getSelf());

					} else {
						Logger.debug("IngestionJobActor: No queued job");
					}
				}
			} else if (message instanceof StatusMessage) {


					if (StatusMessage.SUCCESS
							.equals(((StatusMessage) message).code)) {

						Logger.debug("IngestionJobActor: Done running job: "
								+ ihsIngestionJob.name);

						SingestionRecordStatus singestionRecordStatus = SingestionRecordStatus.find
								.where().eq("name", SingestionRecordStatus.BadRecordError).findUnique();

						List<IhsIngestionRecord> ihsIngestionRecords = IhsIngestionRecord.find.where()
								.eq("ihsIngestionJob", ihsIngestionJob)
								.eq("singestionRecordStatus", singestionRecordStatus).findList();



						if ( ihsIngestionRecords.size() > 0){
						    //Get all the record with bad foramt and write to a static file
							String sourceFileString = ihsIngestionJob.sourceFileString + IhsIngestionJob.badExten;
							FileWriter outFile =  new FileWriter(sourceFileString);
							PrintWriter out = new PrintWriter(outFile);

							for(IhsIngestionRecord ihsIngestionRecord:  ihsIngestionRecords){
								out.println( ihsIngestionRecord.rawRecordData);
							}
							out.close();

						}

						SingestionJobStatus singestionJobStatus = SingestionJobStatus.find.where().eq("name", SingestionJobStatus.Complete).findUnique();

						SqlUpdate update = Ebean.createSqlUpdate(IhsIngestionJob.upDateSql);
						update.setParameter("ingestionJobStatusID", singestionJobStatus.ingestionJobStatusID);
						update.setParameter("ingestionJobID", ihsIngestionJob.ingestionjobID);
						Ebean.execute(update);

					} else if (StatusMessage.ERROR
							.equals(((StatusMessage) message).code)) {

						Logger.debug("IngestionJobActor: Error running job: "
								+ ihsIngestionJob.name);

						SingestionJobStatus singestionJobStatus = SingestionJobStatus.find.where().eq("name", SingestionJobStatus.FileProcessingError).findUnique();

						SqlUpdate update = Ebean.createSqlUpdate(IhsIngestionJob.upDateSql);
						update.setParameter("ingestionJobStatusID", singestionJobStatus.ingestionJobStatusID);
						update.setParameter("ingestionJobID", ihsIngestionJob.ingestionjobID);
						Ebean.execute(update);

					}
					running = false;

			} else {
				unhandled(message);
			}
		} catch (Exception e) {
			running = false;
			Logger.error("app/actors/IngestionJobActor.java has IhsIngestionJob Exception: ",e); // AJE 2016-08-25 this line was commented out
			Logger.error("app/actors/IngestionJobActor.java has IhsIngestionJob Exception: ",e); // AJE 2016-08-25 this line is new

			SingestionJobStatus singestionJobStatus = SingestionJobStatus.find.where().eq("name", SingestionJobStatus.InternalError).findUnique();

			SqlUpdate update = Ebean.createSqlUpdate(IhsIngestionJob.upDateSql);
			update.setParameter("ingestionJobStatusID", singestionJobStatus.ingestionJobStatusID);
			update.setParameter("ingestionJobID", ihsIngestionJob.ingestionjobID);
			Ebean.execute(update);

		} finally {

		}
		// getContext().stop(getSelf());
	}
}