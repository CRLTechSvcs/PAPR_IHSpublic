package util;

import play.Logger;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;

public class LogRequestAction extends Action<LogRequest> {

	@Override
	public Promise<Result> call(Context ctx) throws Throwable {
		
		Logger.info("Logging from LogRequest:" + ctx);
		
		return delegate.call(ctx);		
	}
}