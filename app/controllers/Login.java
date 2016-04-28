package controllers;

import java.util.ArrayList;
import java.util.List;




import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SerializationUtils;

import models.AppRole;
import models.AppUser;
import models.IhsSecurityRole;
import models.IhsUser;
import forms.UserForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import util.Helper;
import views.html.ingestion_home;
import views.html.login;
import views.html.search_home;

public class Login extends Controller {

	static final String DefaultUserName = "User Name";
	public final static String User = "user";
	public final static String Member = "member";
	public final static String ROLE = "role";
	public final static String ORG = "org";

	public static Result login() {
		return ok(login.render(false));
	}

	public static Result doLogin() {

		UserForm userForm = Form.form(UserForm.class).bindFromRequest().get();

		if (DefaultUserName.equals(userForm.user)) {
			return ok(login.render(true));

		} else {

			IhsUser ihsUser = IhsUser.findByName(userForm.user.trim());

			if (ihsUser == null) {
				return ok(login.render(true));
			} else {
				if (ihsUser.password.equals(Helper
						.getMD5hash(userForm.password))) {
					
					session().put(User, ihsUser.userName);
					session().put(Member, ihsUser.ihsMember.name);
					
					Helper.AppUserEnCache(ihsUser);
					//TODO: change to search page
					return ok(search_home.render());
				}else{
					return ok(login.render(true));
				}
			}
		}
	}

	public static Result doLogout() {
		session().clear();
		return ok(login.render(false));
	}
}
