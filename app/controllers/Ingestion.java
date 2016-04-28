package controllers;


import java.util.List;

import models.IhsAuthorizedSource;
import models.Scommitment;
import models.SconditionType;
import models.SingestionDatatype;


import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

@Restrict(@Group("user"))
public class Ingestion extends Controller {

	

	public static Result ingestion_home() {

		return ok(ingestion_home.render());
	}

	
	//
	public static Result ingestion_exception() {

		return ok(ingestion_exception.render("getAUserExceptions/"));
	}

	public static Result ingestion_exception_jobid(int jobid) {

		return ok(ingestion_exception.render("getAllExceptionsByJob/" + jobid
				+ "/"));
	}

	@SuppressWarnings("finally")
	public static Result ingestion_new() {

		List<IhsAuthorizedSource> ihsAuthorizedSources = null;
		List<SingestionDatatype> singestionDatatypes = null;
		List<Scommitment> scommitments = null;

		try {
			ihsAuthorizedSources = IhsAuthorizedSource.find.findList();
			singestionDatatypes = SingestionDatatype.find.findList();
			scommitments = Scommitment.find.findList();

		} catch (Exception e) {
			Logger.error("Ingestion.ingestion_start()", e);

		}
		return ok(ingestion_new.render(ihsAuthorizedSources,
				singestionDatatypes, scommitments));
	}
}