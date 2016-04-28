package reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import models.IhsIssue;
import models.IhsMember;
import models.IhsTitle;
import models.IhsVolume;
import json.DeaccesionReportView;
import json.NewReportView;
import play.Logger;
import play.Play;
//import util.DeaccessionReport.HeaderFooter;



import util.Helper;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class IssuesHeldReport {

	static Random rand = new Random();
	static DateTimeFormatter dtfShort = DateTimeFormat
			.forPattern("MMM dd, yyyy");

	private static DateTimeFormatter dateFormatmmyy = DateTimeFormat
			.forPattern("MMM-YYYY");
	
	class HeaderFooter extends PdfPageEventHelper {

		int pagenumber;
		String title ="";
		int titleid = 0;
		String report = "";
		String date = "";
		String org = "";
		
		void setTitle (String title){
			this.title =title;
		}

		void setTitleid (int titleid){
			this.titleid = titleid;
		}

		public HeaderFooter(String report, String date, String org) {
			this.report = report;
			this.date = date;
			this.org = org;
		}

		public void onStartPage(PdfWriter writer, Document document) {

			pagenumber++;
			PdfPTable table;
			PdfPCell cell;

			Rectangle rect = writer.getPageSize();

			ColumnText.showTextAligned(
					writer.getDirectContent(),
					Element.ALIGN_CENTER,
					new Phrase(report, FontFactory.getFont(
							FontFactory.HELVETICA, 10, Font.NORMAL)), rect
							.getLeft() + 88, rect.getHeight() - 20, 0f);

			ColumnText.showTextAligned(
					writer.getDirectContent(),
					Element.ALIGN_CENTER,
					new Phrase("Prepared " + date, FontFactory.getFont(
							FontFactory.HELVETICA, 10, Font.NORMAL)), rect
							.getRight() - 120, rect.getHeight() - 20, 0f);
			try {

				if (pagenumber == 1) {

					Image image1 = Image
							.getInstance("public/images/papr_logo.gif");

					document.add(image1);

					ColumnText.showTextAligned(
							writer.getDirectContent(),
							Element.ALIGN_LEFT,
							new Phrase(report, FontFactory.getFont(
									FontFactory.HELVETICA, 30, Font.NORMAL)),
							rect.getRight() - 480, rect.getHeight() - 70, 0f);

					table = new PdfPTable(1);
					table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("For " + org,
							FontFactory.getFont(FontFactory.HELVETICA, 25,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					table.addCell(cell);
							
					document.add(table);
					
					
				}
				
				table = new PdfPTable(5);
				table.setWidths(new float[] { 1.2f, 2, 3, 4, 5 });
				table.setWidthPercentage(100);

				cell = new PdfPCell(new Phrase("   "));
				cell.setColspan(5);
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Title", FontFactory.getFont(
						FontFactory.HELVETICA, 18, Font.BOLD)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Volume",
						FontFactory.getFont(FontFactory.HELVETICA, 18,
								Font.BOLD)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Issue",
						FontFactory.getFont(FontFactory.HELVETICA, 16,
								Font.BOLD)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Month-Year", FontFactory.getFont(
						FontFactory.HELVETICA, 18, Font.BOLD)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Location", FontFactory.getFont(
						FontFactory.HELVETICA, 18, Font.BOLD)));

				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("   "));
				cell.setColspan(5);
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);

				document.add(table);
				

			} catch (Exception e) {
				Logger.error("DeaccessionReport:", e);
			}

		}

		public void onEndPage(PdfWriter writer, Document document) {

			Rectangle rect = writer.getPageSize();

			ColumnText.showTextAligned(
					writer.getDirectContent(),
					Element.ALIGN_CENTER,
					new Phrase(String.format("%d", pagenumber), FontFactory
							.getFont(FontFactory.HELVETICA, 10, Font.BOLD)),
					rect.getRight() - 20, 10, 0f);

		}
	}

	public String createPdf(NewReportView newReportView,
			DateTime dateInitiated, List<IhsTitle> ihsTitles)
			throws FileNotFoundException, DocumentException {

		String destFileString = "";
		try {
			String dataDir = Play.application().configuration()
					.getString("application.publishing.process.data.Dir");

			String fileName = rand.nextInt(10000) + "-" + "-IssueHeldReport.pdf";

			destFileString = dataDir + File.separator + fileName;

			Document document = new Document(PageSize.A4.rotate());

			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(destFileString));

			String report = "Issue Held Report";
			String date = dtfShort.print(dateInitiated);

			IhsMember ishMember = IhsMember.find.byId(newReportView.memberId);

			HeaderFooter event = new HeaderFooter(report, date, ishMember.name);

			
			writer.setPageEvent(event);
			

			document.open();

			for(IhsTitle ihsTitle: ihsTitles){
				
				String issn = Helper.formatIssn(ihsTitle.printISSN) ;
				
				String title =  ihsTitle.title + " | " + issn;
				
				event.setTitle("      "+ title);
				PdfPTable table = new PdfPTable(1);
				table.setWidthPercentage(100);

				table.getDefaultCell().setBorder(4);

				PdfPCell cell = new PdfPCell(new Phrase(""));
				
				
				cell.setColspan(1);
				cell.setBorder(Rectangle.NO_BORDER);
				
				table.addCell(cell);
			
				
				cell = new PdfPCell(new Phrase("      "+ title,
						FontFactory.getFont(FontFactory.HELVETICA, 12,
								Font.BOLD)));
				cell.setColspan(1);
				table.addCell(cell);
				
					
				cell = new PdfPCell(new Phrase(""));
				cell.setColspan(1);
				cell.setBorder(Rectangle.NO_BORDER);		
				table.addCell(cell);
				document.add(table);

				
				boolean shaded = true;

				int j = 1;
				
				for(IhsVolume ihsVolume : ihsTitle.ihsVolume){
					
					for( IhsIssue ihsIssue : ihsVolume.ihsissues){
						event.setTitleid(j);
						
						table = new PdfPTable(5);
						table.setWidths(new float[] { 1.2f, 2, 3, 4, 5 });
						table.setWidthPercentage(100);
						
						cell = new PdfPCell(new Phrase(j+"",
								FontFactory.getFont(FontFactory.HELVETICA, 10,
										Font.NORMAL)));
						cell.setBorder(Rectangle.NO_BORDER);
						
						j++;
						
						if (shaded)
							cell.setGrayFill(0.9f);
						table.addCell(cell);
						
						cell = new PdfPCell(new Phrase(ihsVolume.volumeNumber,
								FontFactory.getFont(FontFactory.HELVETICA, 10,
										Font.NORMAL)));
						cell.setBorder(Rectangle.NO_BORDER);
						if (shaded)
							cell.setGrayFill(0.9f);
						table.addCell(cell);
						
						cell = new PdfPCell(new Phrase(ihsIssue.issueNumber,
								FontFactory.getFont(FontFactory.HELVETICA, 10,
										Font.NORMAL)));
						cell.setBorder(Rectangle.NO_BORDER);
						if (shaded)
							cell.setGrayFill(0.9f);
						table.addCell(cell);
						
						String datemmdyy = "";
						
						//Added the date override logic
						if(ihsIssue.spublicationDate !=  null){
							datemmdyy = ihsIssue.spublicationDate.publicationDateVal;
							datemmdyy += ihsIssue.publicationDate !=null ? "-" + ihsIssue.publicationDate.getYear() : "";
						}else {
							datemmdyy =  ihsIssue.publicationDate !=null ? dateFormatmmyy.print(ihsIssue.publicationDate) : "";
						}
						
						cell = new PdfPCell(new Phrase(datemmdyy,
								FontFactory.getFont(FontFactory.HELVETICA, 10,
										Font.NORMAL)));
						cell.setBorder(Rectangle.NO_BORDER);
						if (shaded)
							cell.setGrayFill(0.9f);
						table.addCell(cell);
						
						cell = new PdfPCell(new Phrase("",
								FontFactory.getFont(FontFactory.HELVETICA, 10,
										Font.NORMAL)));
						cell.setBorder(Rectangle.NO_BORDER);
						if (shaded)
							cell.setGrayFill(0.9f);
						
						table.addCell(cell);
						
						document.add(table);
						
						shaded = shaded ? false : true;
					}
				}
				
			}
				
			document.close();
		} catch (Exception e) {
			Logger.error("", e);
		}

		return destFileString;

	}

}