
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
import json.TitleVersionView;
import play.Logger;
import play.Play;
//import util.DeaccessionReport.HeaderFooter;



import util.Helper;

import com.itextpdf.text.Chunk;
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
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

public class BibliographicHistory {

	static Random rand = new Random();
	static DateTimeFormatter dtfShort = DateTimeFormat
			.forPattern("MMM dd, yyyy");

	
	class HeaderFooter extends PdfPageEventHelper {

		int pagenumber;
		String title ="";
		int titleid = 0;
		String report = "";
		String date = "";
		
		void setTitle (String title){
			this.title =title;
		}

		void setTitleid (int titleid){
			this.titleid = titleid;
		}

		public HeaderFooter(String report, String date, String title) {
			this.report = report;
			this.date = date;
			this.title = title;
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
					new Phrase(title, FontFactory.getFont(
							FontFactory.HELVETICA, 10, Font.ITALIC)), ((rect
							.getLeft() + rect.getRight()) / 2) - 20, rect
							.getHeight() - 20, 0f);

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
									FontFactory.HELVETICA, 25, Font.NORMAL)),
							rect.getRight() - 480, rect.getHeight() - 70, 0f);

					table = new PdfPTable(1);
					table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("For: "  + title,
							FontFactory.getFont(FontFactory.HELVETICA, 15,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					table.addCell(cell);
							
					document.add(table);
					
					
				}
				
				

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

	public String createPdf(NewReportView newReportView,List<TitleVersionView> titleVersionViews, DateTime dateInitiated)
			throws FileNotFoundException, DocumentException {

		String destFileString = "";
		try {
			String dataDir = Play.application().configuration()
					.getString("application.publishing.process.data.Dir");

			String fileName = rand.nextInt(10000) + "-" + "-BibliographicHist.pdf";

			destFileString = dataDir + File.separator + fileName;

			Document document = new Document(PageSize.A4.rotate());

			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(destFileString));

			String report = "Bibliographic Data History Report";
			String date = dtfShort.print(new DateTime());


			HeaderFooter event = new HeaderFooter(report, date, titleVersionViews.get(0).title);

			
			writer.setPageEvent(event);
			

			document.open();
				
				//String issn = Helper.formatIssn(printISSN) ;
				
				
				
				PdfPTable table = new PdfPTable(1);
				
				table.setWidthPercentage(100);

				table.getDefaultCell().setBorder(4);

				PdfPCell cell = new PdfPCell(new Phrase(""));
				
				
				cell.setColspan(1);
				cell.setBorder(Rectangle.NO_BORDER);
				
				table.addCell(cell);
			
				
				cell = new PdfPCell(new Phrase("      ",
						FontFactory.getFont(FontFactory.HELVETICA, 12,
								Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setColspan(1);
				table.addCell(cell);
				
				document.add(table);

				int index = 0;
				
				for (TitleVersionView titleVersionView : titleVersionViews){
					
					table = new PdfPTable(1);
					
					table.setWidthPercentage(100);

					table.getDefaultCell().setBorder(4);
					
					if(index == 0){
						cell = new PdfPCell(new Phrase("Title: " + titleVersionView.title + "  (Current)",
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					}else{
						cell = new PdfPCell(new Phrase("Title: " +titleVersionView.title,
								FontFactory.getFont(FontFactory.HELVETICA, 12,
										Font.NORMAL)));
					}
					
					cell.setColspan(1);
					cell.setBorder(Rectangle.NO_BORDER);
					table.addCell(cell);
					
					//
					cell = new PdfPCell(new Phrase("Publisher: " + titleVersionView.publisherName ,
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					table.addCell(cell);
					
					//
					cell = new PdfPCell(new Phrase("Print ISSN: " + titleVersionView.printISSN ,
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					table.addCell(cell);
					
					//
					cell = new PdfPCell(new Phrase("Electronic ISSN: " + titleVersionView.eISSN ,
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					table.addCell(cell);
					
					
					//
					cell = new PdfPCell(new Phrase("OCLC Number: " + titleVersionView.oclcNumber ,
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					table.addCell(cell);
					
					//
					
					cell = new PdfPCell(new Phrase("LCCN: " + titleVersionView.lccn ,
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					table.addCell(cell);
					
					//
					cell = new PdfPCell(new Phrase("Image Page Ratio: " + titleVersionView.imagePageRatio ,
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					table.addCell(cell);
					
					
					cell = new PdfPCell(new Phrase("Publication Range: " + titleVersionView.publicationRange ,
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					table.addCell(cell);
					
					
					//
					cell = new PdfPCell(new Phrase("Publication Language: " + titleVersionView.language ,
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					table.addCell(cell);
					
					//
					cell = new PdfPCell(new Phrase("Publication Country: " + titleVersionView.country ,
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					table.addCell(cell);
					
					
					//
					cell = new PdfPCell(new Phrase("Date Changed: " + titleVersionView.changeDate ,
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					table.addCell(cell);
					
					//
					cell = new PdfPCell(new Phrase("Changed by User: " + titleVersionView.changeUser ,
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					table.addCell(cell);
					
					//
					cell = new PdfPCell(new Phrase("Changed by Member: " + titleVersionView.changeMember ,
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.NORMAL)));
					
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					table.addCell(cell);
					
					
					PdfPTable outertable1 = new PdfPTable(1);
					outertable1.addCell(table); 
					document.add(outertable1);
					
					 
				      
				    table = new PdfPTable(1);
					
					cell = new PdfPCell(new Phrase(" " ,
							FontFactory.getFont(FontFactory.HELVETICA, 15,
									Font.NORMAL)));
				    
				    cell.setColspan(1);
					cell.setBorder(Rectangle.NO_BORDER);
					table.addCell(cell);
					document.add(table);
					
					index++;
				}
			
				
				boolean shaded = true;

				int j = 1;
				
			document.close();
		} catch (Exception e) {
			Logger.error("", e);
		}

		return destFileString;

	}

}