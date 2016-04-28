package actors;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import play.Logger;
import play.cache.Cache;
import scala.concurrent.duration.Duration;
import models.AppUser;
import models.IhsIngestionJob;
import models.IhsIngestionRecord;
import models.IhsUser;
import models.Scommitment;
import models.SingestionDatatype;
import models.SingestionRecordStatus;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;

public class FileReaderActor extends UntypedActor {

	int lineNumber = 0;
	int processedLine = 0;
	boolean done = false;
	boolean error = false;
	String timeOut = "300 seconds";

	private ActorRef recordProcessingActor = getContext().actorOf(
			new RoundRobinPool(5).props(Props
					.create(RecordProcessingActor.class)),
			"RecordProcessingActor");

	public FileReaderActor() {
		getContext().setReceiveTimeout(Duration.create(timeOut));
	}

	@Override
	public void onReceive(Object message) throws Exception {

		//
		BufferedReader br = null;
		FileInputStream fis = null;
		String dataType = "";

		if (message instanceof IhsIngestionJob) {

			Logger.debug("In FileReaderActor: ");

			IhsIngestionJob ihsIngestionJob = (IhsIngestionJob) message;

			
			Logger.debug(ihsIngestionJob.singestionDatatype.name);

			error = false;

			try {
				AppUser appUser= (AppUser) Cache.get(ihsIngestionJob.ihsUser.userName);
				int userId= appUser.userId;
				int memberId = appUser.memberId;
				int locationId = appUser.locatoinId;
				
				
				fis = new FileInputStream(ihsIngestionJob.sourceFileString);

				// Construct BufferedReader from InputStreamReader
				br = new BufferedReader(new InputStreamReader(fis));

				SingestionRecordStatus singestionRecordStatus = SingestionRecordStatus.find
						.where().eq("name", SingestionRecordStatus.Processing)
						.findUnique();

				// SingestionDatatype singestionDatatype =
				// SingestionDatatype.find.byId(singestionDatatype);

				
				String line = null;

				IhsUser ihsUser = IhsUser.find.byId(userId);
				
				while ((line = br.readLine()) != null) {

					IhsIngestionRecord ihsIngestionRecord = new IhsIngestionRecord(
							(IhsIngestionJob) message, line,
							singestionRecordStatus, ihsUser);

					ihsIngestionRecord.save();

					//TODO: need to provide real member and location
					recordProcessingActor
							.tell(new RecordProcessingMessage(
									ihsIngestionRecord.ingestionRecordID,
									ihsIngestionJob.singestionDatatype.name,
									userId, memberId, locationId, ihsIngestionJob.scommitment.commitmentID ),
									getSelf());

					lineNumber++;

				}
			} catch (Exception e) {
				error = true;
				Logger.error("FileReaderActor:", e);
			} finally {
				Logger.debug("In FileReaderActor: in finnaly");
				br.close();
				fis.close();
				done = true;
			}

		} else if (message instanceof StatusMessage) {

			Logger.debug("In FileReaderActor: StatusMessage ");
			getContext().setReceiveTimeout(Duration.create(timeOut));

			if (StatusMessage.SUCCESS.equals(((StatusMessage) message).SUCCESS)) {

				processedLine++;

				if (done && (lineNumber == processedLine)) {
					Logger.debug("FileReaderActor: done running");

					done = false;
					processedLine = 0;
					lineNumber = 0;

					if (error) {
						error = false;
						getContext().parent().tell(
								new StatusMessage(StatusMessage.ERROR),
								getSelf());
					} else
						getContext().parent().tell(
								new StatusMessage(StatusMessage.SUCCESS),
								getSelf());
				}
			}
		} else if (message instanceof ReceiveTimeout) {
			Logger.debug("FileReaderActor: In time out error:" + error);
			if (lineNumber > 0 || error) {
				error = false;

				Logger.error("FileReaderActor: Didn't get any messages from for RecordProcessingActor:"
						+ timeOut);
				getContext().parent().tell(
						new StatusMessage(StatusMessage.ERROR), getSelf());
			} else {
				getContext().setReceiveTimeout(Duration.create(timeOut));
			}
		} else {
			unhandled(message);
		}
	}
}