package actors;

import java.util.List;

import models.AppUser;
import models.IhsIngestionRecord;
import models.IhsLocation;
import models.IhsMember;
import models.IhsTitle;
import models.IhsUser;
import models.Scommitment;
import models.SingestionDatatype;
import parser.Portico.*;
import parser.papr.PAPR;
import play.Logger;
import play.cache.Cache;
import akka.actor.UntypedActor;

public class RecordProcessingActor extends UntypedActor{

	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof RecordProcessingMessage){
			
			RecordProcessingMessage recordProcessingMessage = (RecordProcessingMessage)message;
			
			IhsIngestionRecord thsIngestionRecord = IhsIngestionRecord.find.byId( recordProcessingMessage.recordId );
			
			
			try{
					
				Logger.debug(thsIngestionRecord.rawRecordData);
				
				IhsUser ishUser = IhsUser.find.byId(recordProcessingMessage.userId);
				IhsMember ihsMember = IhsMember.find.byId(recordProcessingMessage.memberId);
				IhsLocation ihsLocation = IhsLocation.find.byId(recordProcessingMessage.locationId);
				
				Scommitment scommitment = Scommitment.find.byId(recordProcessingMessage.commitmentID);
				
				//TODO : resolve the userid
				if(SingestionDatatype.Portico.equals(recordProcessingMessage.dataType)){
					
					Portico.processPortico(thsIngestionRecord, ishUser, ihsMember,ihsLocation, scommitment );
					
				}else if((SingestionDatatype.PAPR.equals(recordProcessingMessage.dataType))){
					
					PAPR.processPAPR(thsIngestionRecord, ishUser, ihsMember,ihsLocation, scommitment );
					//TODO
				}else if(("xyz".equals(thsIngestionRecord.ihsIngestionJob.authorizedSource.name))){
					
				}
				
			}
			catch  (Exception e){
				if(thsIngestionRecord != null){
					Logger.error("Error processing IhsIngestionRecord:" + thsIngestionRecord.ingestionRecordID,e);
				}
			}finally {
				getSender().tell(new StatusMessage(StatusMessage.SUCCESS), getSelf() );
			}
		}else{
			unhandled(message);
		}
	}
}