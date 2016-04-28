package actors;

import play.Logger;
import actors.IngestionJobActor;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Props;

public class AppActorSystem {

	private static AppActorSystem instance = null;
	private Cancellable scheduler;
	   
	   
	private ActorSystem actorSystem;
     
	private AppActorSystem() {
	      // Exists only to defeat instantiation.
	 }
   
	public static AppActorSystem getInstance() {
	      if(instance == null) {
	         instance = new AppActorSystem();
	         Logger.info("Starting Actor system");
	         instance.actorSystem = ActorSystem.create( "play" );
	         //TODO - update 
	         instance.actorSystem.actorOf( Props.create( IngestionJobActor.class ), "IngestionJobActor" );
	         instance.actorSystem.actorOf( Props.create( DeaccessionJobActor.class ), "DeaccessionJobActor" );
	         instance.actorSystem.actorOf( Props.create( PublishingJobActor.class ), "PublishingJobActor" );
	         instance.actorSystem.actorOf( Props.create( ReportingActor.class ), "ReportingActor" );
	         
	         
	      }
	      return instance;
	   }
	
	 public ActorSystem getActorSystem(){
		 return actorSystem;
	 }
}