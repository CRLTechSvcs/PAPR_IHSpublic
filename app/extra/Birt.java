package extra;

import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
/*
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
*/
public class Birt {

  public static void /*Result*/ testReport(){

	  /*
	  System.setSecurityManager(null);
        IReportEngine engine=null;
         EngineConfig config = null;
         try{
          config = new EngineConfig( );
          config.setLogConfig(null, Level.FINE);

          Platform.startup( config );
          IReportEngineFactory factory = (IReportEngineFactory) Platform
          .createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
          engine = factory.createReportEngine(config);
          engine.changeLogLevel( Level.WARNING );

         }catch( Exception ex){
          ex.printStackTrace();
         }
         IReportRunnable design = null;

         try {
            design = engine.openReportDesign("app/reports/hello_world.rptdesign");
        } catch (EngineException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } 
         IRunAndRenderTask task = engine.createRunAndRenderTask(design);
         PDFRenderOption options = new PDFRenderOption();
         options.setOutputFormat(IRenderOption.OUTPUT_FORMAT_PDF);
        
         ByteArrayOutputStream outs = new ByteArrayOutputStream();
         options.setOutputStream(outs);

         task.setRenderOption(options);

         try {
            task.run();
        } catch (EngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

         engine.destroy();
         Platform.shutdown();
    
         //return ok(outs.toByteArray()).as("application/pdf");
        */
    }
    
    
}
