package controllers;

import static play.libs.Json.toJson;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import json.FileViewJson;
import models.IhsAuthorizedSource;
import models.IhsIngestionJob;
import models.IhsUser;
import models.Scommitment;
import models.SingestionDatatype;
import models.SingestionJobStatus;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import play.Logger;
import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import com.google.common.io.Files;

import controllers.routes;
import forms.FileUploadForm;

public class IngestionNew  extends Controller {

	static Random rand = new Random();
	
	public static Result ingestionHttpStart() {

		try {

			String user = session().get(Login.User);

			String dataDir = Play.application().configuration()
					.getString("application.http.data.Dir");

			FileUploadForm fileUpload = Form.form(FileUploadForm.class)
					.bindFromRequest().get();

			/* Get the file */
			MultipartFormData body = request().body().asMultipartFormData();
			FilePart filePart1 = body.getFile("filePart1");

			String fileNameTmp = rand.nextInt(10000) + "-"
					+ filePart1.getFilename();

			String fileName = fileNameTmp.replace(" ", "");
			
			String sourceFileString = dataDir + File.separator + fileName;

			File newFile1 = new File(sourceFileString);
			File file1 = filePart1.getFile();
			InputStream isFile1 = new FileInputStream(file1);
			byte[] byteFile1 = IOUtils.toByteArray(isFile1);
			FileUtils.writeByteArrayToFile(newFile1, byteFile1);
			isFile1.close();

			SingestionJobStatus singestionJobStatus = SingestionJobStatus.find
					.where().eq("name", "Queued").findUnique();

			IhsIngestionJob ihsIngestionJob = new IhsIngestionJob(
					fileUpload.ingestionName, fileUpload.ingestionName,
					IhsAuthorizedSource.find.byId(fileUpload.authorizedSource),
					SingestionDatatype.find.byId(fileUpload.fileFormat),
					new Timestamp(System.currentTimeMillis()),
					IhsUser.findByName(user), sourceFileString,
					SingestionJobStatus.find
							.byId(singestionJobStatus.ingestionJobStatusID),
					"Job",
					Scommitment.find.byId(fileUpload.commitmentID));

			ihsIngestionJob.save();

		} catch (Exception e) {
			Logger.error("Ingestion.ingestion_start()", e);

		}

		return redirect(routes.Ingestion.ingestion_home());
	}

	public static Result ingestionFtpStart() {

		try {

			String user = session().get(Login.User);

			IhsUser ihsUser = IhsUser.findByName(user);

			String ftpdataDir = Play.application().configuration()
					.getString("application.ftp.data.Dir");

			String froProcssDir = Play.application().configuration()
					.getString("application.ftp.process.data.Dir");

			FileUploadForm fileUpload = Form.form(FileUploadForm.class)
					.bindFromRequest().get();

			String destinationFile = froProcssDir
					+ System.getProperty("file.separator")
					+ rand.nextInt(10000) + "-" + fileUpload.fileName;

			File sourcefile = new File(ftpdataDir
					+ System.getProperty("file.separator")
					+ ihsUser.ihsMember.ftpdirectory
					+ System.getProperty("file.separator")
					+ fileUpload.fileName);

			Files.move(sourcefile, new File(destinationFile));

			

			SingestionJobStatus singestionJobStatus = SingestionJobStatus.find
					.where().eq("name", "Queued").findUnique();

			IhsIngestionJob ihsIngestionJob = new IhsIngestionJob(
					fileUpload.ingestionName, fileUpload.ingestionName,
					IhsAuthorizedSource.find.byId(fileUpload.authorizedSource),
					SingestionDatatype.find.byId(fileUpload.fileFormat),
					new Timestamp(System.currentTimeMillis()),
					IhsUser.findByName(user), destinationFile,
					SingestionJobStatus.find
							.byId(singestionJobStatus.ingestionJobStatusID),
					"Job",
					Scommitment.find.byId(fileUpload.commitmentID));

			ihsIngestionJob.save();
			 
		} catch (Exception e) {
			Logger.error("ingestion_start_ftp()", e);

		}

		return redirect(routes.Ingestion.ingestion_home());
	}

	public static Result ingestionFtpFilelist() {

		List<FileViewJson> fileList = new ArrayList<FileViewJson>();

		String user = session().get(Login.User);

		IhsUser ihsUser = IhsUser.findByName(user);

		String memberDir = ihsUser.ihsMember.ftpdirectory != null ? ihsUser.ihsMember.ftpdirectory
				: "";

		String ftpdataDir = Play.application().configuration()
				.getString("application.ftp.data.Dir");

		String memberFileList = ftpdataDir
				+ System.getProperty("file.separator") + memberDir;

		try {
			File folder = new File(memberFileList);
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {

				if (listOfFiles[i].isFile()) {
					String file = listOfFiles[i].getName();
					if (file.endsWith(".csv") || file.endsWith(".CSV")
							|| file.endsWith(".xml") || file.endsWith(".XML")
							|| file.endsWith(".json") || file.endsWith(".JSON")) {
						fileList.add(new FileViewJson(file));
					}
				}
			}

		} catch (Exception e) {
			Logger.error("ingestion_ftp_filelist()", e);
		}
		return ok(toJson(fileList));
	}
}
