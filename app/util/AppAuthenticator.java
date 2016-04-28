package util;

import play.mvc.Security.Authenticator;
import play.mvc.Http.Context;

public class AppAuthenticator extends Authenticator{

	@Override 
	public String getUsername(Context ctx){
		
		String authHeader=ctx.request().getHeader("AUTHORIZATION");
	
		
		return null;
	}
	
}
