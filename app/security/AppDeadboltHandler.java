package security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import controllers.Login;
import controllers.routes;
import models.AppRole;
import models.AppUser;
import models.IhsUser;



import play.cache.Cache;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.Http.Session;
import play.mvc.Http;
import play.mvc.Result;
import util.Helper;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;

public class AppDeadboltHandler extends AbstractDeadboltHandler{

	@Override
	public Promise<Result> beforeAuthCheck(
			Context context) {
		if(Http.Context.current().session().get(Login.User) != null){
			return F.Promise.pure(null);
		}else {
			context.flash().put("error","error");
			return F.Promise.promise(new F.Function0<Result>()
		            {
		                @Override
		                public Result apply() throws Throwable
		                {
		                	//return forbidden("Not allowed");
		                	return redirect(routes.Login.login());
		                }
		            });
			
		}
		
	}
	
	@Override
	public Subject getSubject(final Http.Context context) {
	
		Session session = Http.Context.current().session();
		
		String user = session.get(Login.User);
		
		
		if(user != null){		
			
			AppUser appUser= (AppUser) Cache.get(user);
			
			if(appUser == null){
				IhsUser ihsUser = IhsUser.findByName(user);
				Helper.AppUserEnCache(ihsUser);
				appUser= (AppUser) Cache.get(user);
				return appUser;
			}else{
				return appUser;
			}
			
		}else {
			return null;	
		}
	}
}