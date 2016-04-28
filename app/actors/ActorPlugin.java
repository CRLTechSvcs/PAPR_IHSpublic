package actors;

import play.Application;
import play.Configuration;
import play.Logger;
import play.Plugin;

public class ActorPlugin extends Plugin {

	private final Application application;

	public ActorPlugin(Application application) {
		this.application = application;
	}
	
	@Override
    public void onStart()
    {
        Configuration configuration = application.configuration();
        // you can now access the application.conf settings, including any custom ones you have added
        Logger.info("ActorPlugin has started"); 
        AppActorSystem.getInstance();
    }

    @Override
    public void onStop()
    {
        // you may want to tidy up resources here
        Logger.info("ActorPlugin has stopped");
    }
}
