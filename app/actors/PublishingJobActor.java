package actors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import play.Logger;
import play.Play;
import util.Helper;
import models.IhsIngestionRecord;
import models.IhsIssue;
import models.IhsPublishingJob;
import models.IhsTitle;
import models.IhsVolume;
import models.SingestionJobStatus;
import akka.actor.UntypedActor;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class PublishingJobActor extends UntypedActor {

	static Random rand = new Random();

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Integer) {

			Integer ihsPublishingJobId = (Integer) message;

			List<IhsTitle> ihsTitles = null;

			IhsPublishingJob ihsPublishingJob = IhsPublishingJob.find
					.byId(ihsPublishingJobId);

			String link = "";

			Logger.info("Start Running Publishing job");

			if (ihsPublishingJob.startDate != null
					&& ihsPublishingJob.endDate != null) {
				ihsTitles = IhsTitle.find.fetch("ihsPublisher")
						.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
						.fetch("ihsPublicationRange").where()
						.ge("changeDate", ihsPublishingJob.startDate)
						.le("changeDate", ihsPublishingJob.endDate).findList();
			} else if (ihsPublishingJob.startDate != null) {
				ihsTitles = IhsTitle.find.fetch("ihsPublisher")
						.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
						.fetch("ihsPublicationRange").where()
						.ge("changeDate", ihsPublishingJob.startDate)
						.findList();
			} else if (ihsPublishingJob.endDate != null) {
				ihsTitles = IhsTitle.find.fetch("ihsPublisher")
						.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
						.fetch("ihsPublicationRange").where()
						.le("changeDate", ihsPublishingJob.endDate).findList();
			} else {
				ihsTitles = IhsTitle.find.fetch("ihsPublisher")
						.fetch("ihsVolume").fetch("ihsVolume.ihsissues")
						.fetch("ihsPublicationRange").findList();
			}
			try {
				if (ihsPublishingJob.fileformat == 1) {
					buildMArk(ihsTitles);
				} else if (ihsPublishingJob.fileformat == 2) {
					link = buildPortico(ihsTitles);
				} else if (ihsPublishingJob.fileformat == 3) {
					buildIhsXls(ihsTitles);
				} else if (ihsPublishingJob.fileformat == 4) {
					buildIhsCsv(ihsTitles);
				}
		
				SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
						.where().eq("name", SingestionJobStatus.Complete)
						.findUnique();

				ihsPublishingJob.setSingestionJobStatus(singestionJobStatus);
				
				/*
				 * 		/ingestion/getBadformatFile/ is route to controllers.IngestionJob.getBadformatFile		
				 */
				ihsPublishingJob.setLink("<a href=/ingestion/getBadformatFile/"
						+ link.replace(System.getProperty("file.separator"),
								IhsIngestionRecord.tilde) + ">" + "Download</a>");
				ihsPublishingJob.update();

				Logger.info("Done Running Publishing job");
				
			} catch (Exception e) {

				SingestionJobStatus singestionJobStatus = (SingestionJobStatus) SingestionJobStatus.find
						.where().eq("name", SingestionJobStatus.InternalError)
						.findUnique();

				ihsPublishingJob.setSingestionJobStatus(singestionJobStatus);
				ihsPublishingJob.update();
				Logger.error("PublishingJobActor",e);
			}
		}

	}

	void buildMArk(List<IhsTitle> ihsTitles) {

	}

	String buildPortico(List<IhsTitle> ihsTitles) throws IOException {

		String dataDir = Play.application().configuration()
				.getString("application.publishing.process.data.Dir");

		String fileName = rand.nextInt(10000) + "-" + "portico.xlsx";

		String destFileString = dataDir + File.separator + fileName;

		Workbook workbook = null;

		workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Portico");

		int rowIndex = 0;
		// Add header
		Row headedrow = sheet.createRow(rowIndex++);

		Cell headerpubcell = headedrow.createCell(0);
		headerpubcell.setCellValue("Publisher");
		sheet.setColumnWidth(0, 10000);
		
		
		Cell headertitlecell = headedrow.createCell(1);
		headertitlecell.setCellValue("Title");
		sheet.setColumnWidth(1, 10000);
		
		Cell headerSocietycell = headedrow.createCell(2);
		headerSocietycell.setCellValue("Society");

		Cell headerPrintISSNcell = headedrow.createCell(3);
		headerPrintISSNcell.setCellValue("Print ISSN");
		sheet.setColumnWidth(3, 2500);
		
		Cell headereISSNcell = headedrow.createCell(4);
		headereISSNcell.setCellValue("e-ISSN");
		sheet.setColumnWidth(4, 2500);
		
		Cell headerpcacell = headedrow.createCell(5);
		headerpcacell.setCellValue("PCA");

		Cell headerStatuscell = headedrow.createCell(6);
		headerStatuscell.setCellValue("Status");

		Cell headerHoldingscell = headedrow.createCell(7);
		headerHoldingscell.setCellValue("Holdings");
		sheet.setColumnWidth(7, 30000);
		
		Cell headerYearscell = headedrow.createCell(8);
		headerYearscell.setCellValue("Years");
		sheet.setColumnWidth(8, 2500);
		
		Cell headerContentSetIdcell = headedrow.createCell(9);
		headerContentSetIdcell.setCellValue("ContentSet Id");

		CellStyle style = workbook.createCellStyle(); 
		style.setWrapText(true);
		
		for (IhsTitle ihsTitle : ihsTitles) {
			Row childrow = sheet.createRow(rowIndex++);

			Cell pubcell = childrow.createCell(0);
			pubcell.setCellValue(ihsTitle.ihsPublisher.name);
			
			pubcell.setCellStyle(style);
			
			Cell titlecell = childrow.createCell(1);
			titlecell.setCellValue(ihsTitle.title);
			titlecell.setCellStyle(style);
			
			Cell societycell = childrow.createCell(2);
			societycell.setCellValue("");

			String pISSN = ihsTitle.printISSN != null ? Helper.formatIssn(ihsTitle.printISSN) :"";
			Cell printISSNcell = childrow.createCell(3);
			printISSNcell.setCellValue(pISSN);

			String eISSN = ihsTitle.eISSN != null ? Helper.formatIssn(ihsTitle.eISSN) :"";
			Cell eISSNcell = childrow.createCell(4);
			eISSNcell.setCellValue(eISSN);

			Cell pcacell = childrow.createCell(5);
			pcacell.setCellValue("");

			Cell statuscell = childrow.createCell(6);
			statuscell.setCellValue("");

			StringBuilder builderHolding = new StringBuilder();
			boolean startVolume = true;
			boolean startIssue = true;
			for(IhsVolume ihsVolume: ihsTitle.ihsVolume){
				
				if(!startVolume) builderHolding.append(",");
				startVolume=false;
				
				builderHolding.append("v.").append(ihsVolume.volumeNumber).append("(");
				
				for(IhsIssue ihsissue: ihsVolume.ihsissues){
					if(!startIssue) builderHolding.append(",");
					builderHolding.append(ihsissue.issueNumber);
					startIssue=false;
				}
				startIssue=true;
				builderHolding.append(")");
			}
			
			Cell holdingscell = childrow.createCell(7);
			holdingscell.setCellValue(builderHolding.toString());
			holdingscell.setCellStyle(style);

			
			String years = Helper.getPublicationRange(ihsTitle.ihsPublicationRange);
			
			Cell yearscell = childrow.createCell(8);
			yearscell.setCellValue(years);

			Cell contentSetIdcell = childrow.createCell(9);
			contentSetIdcell.setCellValue("");
		}

		FileOutputStream fos = new FileOutputStream(destFileString);
		workbook.write(fos);
		fos.close();

		/*
		 * File file = new File(destFileString);
		 * 
		 * try { BufferedWriter output = new BufferedWriter(new
		 * FileWriter(file));
		 * 
		 * for (IhsTitle ihsTitle : ihsTitles) { output.write(ihsTitle.title); }
		 * 
		 * output.close(); } catch (IOException e) { // TODO Auto-generated
		 * catch block throw e; }
		 */

		return destFileString;

	}

	void buildIhsXls(List<IhsTitle> ihsTitles) {

	}

	void buildIhsCsv(List<IhsTitle> ihsTitles) {

	}
}