package actors;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.IhsDeaccessionJob;
import models.IhsHolding;
import models.SingestionJobStatus;
import json.DeaccesionReportView;
import json.DeaccessionIssueView;
import json.DeaccessionTitleView;
import akka.actor.UntypedActor;

public class DeaccessionJobActor extends UntypedActor{

	@Override
	public void onReceive(Object message) throws Exception {
		
		if (message instanceof Integer) {
			
			Integer jobId = (Integer)message;
			IhsDeaccessionJob ihsDeaccessionJob = IhsDeaccessionJob.find.byId(jobId); 
			
			ObjectMapper mapper = new ObjectMapper();

			DeaccesionReportView deaccesionReportView = mapper.readValue(ihsDeaccessionJob.jsonString,
					DeaccesionReportView.class);
			
			
			for(DeaccessionTitleView deaccessionTitleView :deaccesionReportView.deaccessionTitleView){
				
				for (DeaccessionIssueView deaccessionIssueView : deaccessionTitleView.deaccessionIssueView){
					if(!deaccessionIssueView.action.equals("p")){
						IhsHolding ihsHolding = IhsHolding.find.byId(deaccessionIssueView.holdingId);
						ihsHolding.delete();
					}
				}
			}
			
			SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find.where().eq("name", SingestionJobStatus.Complete).findUnique();
			
			
			ihsDeaccessionJob.singestionJobStatus = singestionJobStatus;
			
			ihsDeaccessionJob.update();
			
		}	
	}
}