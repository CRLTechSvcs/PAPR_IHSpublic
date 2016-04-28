package reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import json.DeaccesionReportView;
import json.DeaccessionIssueView;
import json.DeaccessionTitleView;
import play.Logger;
import play.Play;

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



public class DeaccessionReport {

	private static DateTimeFormatter dateFormat = DateTimeFormat
			.forPattern("MMM-YYYY");
	static Random rand = new Random();
	
	class HeaderFooter extends PdfPageEventHelper {
		int pagenumber = 0;
		String jobname = "";
		String date = "";
		String org = "";
		String userName = "";
		String jobtype = "";
		int numberOfDeaccession = 0;
		int numberOfDonation = 0;
		int numberOfPreservation = 0;
		int totalIssue = 0;
		String title = "";
		int titleid = 0;

		public HeaderFooter(String jobname, String date, String org,
				String userName, String jobtype, int numberOfDeaccession,
				int numberOfDonation, int numberOfPreservation, int totalIssue) {
			this.jobname = jobname;
			this.date = date;
			this.org = org;
			this.userName = userName;
			this.jobtype = jobtype;
			this.numberOfDeaccession = numberOfDeaccession;
			this.numberOfDonation = numberOfDonation;
			this.numberOfPreservation = numberOfPreservation;
			this.totalIssue = totalIssue;
		}
		
		void setTitle (String title){
			this.title =title;
		}

		void setTitleid (int titleid){
			this.titleid = titleid;
		}
		
		public void onStartPage(PdfWriter writer, Document document) {

			pagenumber++;
			PdfPTable table;
			PdfPCell cell;

			Rectangle rect = writer.getPageSize();

			ColumnText.showTextAligned(
					writer.getDirectContent(),
					Element.ALIGN_CENTER,
					new Phrase("Deaccession Report", FontFactory.getFont(
							FontFactory.HELVETICA, 10, Font.NORMAL)), rect
							.getLeft() + 88, rect.getHeight() - 20, 0f);

			ColumnText.showTextAligned(
					writer.getDirectContent(),
					Element.ALIGN_CENTER,
					new Phrase(jobname, FontFactory.getFont(
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
							new Phrase("Deaccession Report", FontFactory
									.getFont(FontFactory.HELVETICA, 30,
											Font.NORMAL)),
							rect.getRight() - 480, rect.getHeight() - 70, 0f);

					ColumnText.showTextAligned(
							writer.getDirectContent(),
							Element.ALIGN_LEFT,
							new Phrase("Summary of Planned Actions",
									FontFactory.getFont(FontFactory.HELVETICA,
											11, Font.BOLD)),
							rect.getRight() - 370, rect.getHeight() - 105, 0f);

					// --------------//
					table = new PdfPTable(1);
					table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("For " + org,
							FontFactory.getFont(FontFactory.HELVETICA, 25,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					table.addCell(cell);

					document.add(table);

					table = new PdfPTable(2);
					table.setWidthPercentage(100);

					// --------------//
					PdfPTable table1 = new PdfPTable(2);
					cell = new PdfPCell(new Phrase("Prepared By:",
							FontFactory.getFont(FontFactory.HELVETICA, 10,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);

					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(userName,
							FontFactory.getFont(FontFactory.HELVETICA, 10,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase("Prepared On:",
							FontFactory.getFont(FontFactory.HELVETICA, 10,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);

					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(date, FontFactory.getFont(
							FontFactory.HELVETICA, 10, Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase("Deaccession Job Name:",
							FontFactory.getFont(FontFactory.HELVETICA, 10,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);

					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(jobname,
							FontFactory.getFont(FontFactory.HELVETICA, 10,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					table1.addCell(cell);

					cell = new PdfPCell(new Phrase("Deaccession Standard:",
							FontFactory.getFont(FontFactory.HELVETICA, 10,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);

					table1.addCell(cell);

					cell = new PdfPCell(new Phrase(jobtype,
							FontFactory.getFont(FontFactory.HELVETICA, 10,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					table1.addCell(cell);

					cell = new PdfPCell(table1);
					cell.setBorder(Rectangle.NO_BORDER);
					table.addCell(cell);

					// --------------//
					PdfPTable table2 = new PdfPTable(2);

					cell = new PdfPCell(new Phrase("Issues for Deaccession",
							FontFactory.getFont(FontFactory.HELVETICA, 10,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					table2.addCell(cell);

					cell = new PdfPCell(new Phrase(String.format("%d",
							numberOfDeaccession), FontFactory.getFont(
							FontFactory.HELVETICA, 10, Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table2.addCell(cell);

					cell = new PdfPCell(new Phrase("Issues for Donation",
							FontFactory.getFont(FontFactory.HELVETICA, 10,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					table2.addCell(cell);

					cell = new PdfPCell(new Phrase(String.format("%d",
							numberOfDonation), FontFactory.getFont(
							FontFactory.HELVETICA, 10, Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table2.addCell(cell);

					cell = new PdfPCell(new Phrase("Issues for Preservation",
							FontFactory.getFont(FontFactory.HELVETICA, 10,
									Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					table2.addCell(cell);

					cell = new PdfPCell(new Phrase(String.format("%d",
							numberOfPreservation), FontFactory.getFont(
							FontFactory.HELVETICA, 10, Font.NORMAL)));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table2.addCell(cell);

					cell = new PdfPCell(new Phrase("Total Issues",
							FontFactory.getFont(FontFactory.HELVETICA, 10,
									Font.BOLD)));
					cell.setBorder(Rectangle.NO_BORDER);
					table2.addCell(cell);

					cell = new PdfPCell(new Phrase(String.format("%d",
							totalIssue), FontFactory.getFont(
							FontFactory.HELVETICA, 10, Font.BOLD)));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table2.addCell(cell);

					cell = new PdfPCell(table2);
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

				cell = new PdfPCell(new Phrase("Planned Action",
						FontFactory.getFont(FontFactory.HELVETICA, 18,
								Font.BOLD)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Action Taken Planned | Other",
						FontFactory.getFont(FontFactory.HELVETICA, 16,
								Font.BOLD)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Issue", FontFactory.getFont(
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
				
				if(titleid > 1) {
					table = new PdfPTable(1);
					table.setWidthPercentage(100);

					table.getDefaultCell().setBorder(4);

					cell = new PdfPCell(new Phrase(""));
					cell.setColspan(1);
					cell.setBorder(Rectangle.NO_BORDER);
					
					table.addCell(cell);
					
					
					cell = new PdfPCell(new Phrase("       " + title + "   (continued…)",
							FontFactory.getFont(FontFactory.HELVETICA, 12,
									Font.BOLD)));
					
					cell.setColspan(1);
						
					table.addCell(cell);
					
						
					cell = new PdfPCell(new Phrase(""));
					cell.setColspan(1);
					cell.setBorder(Rectangle.NO_BORDER);		
					table.addCell(cell);
					document.add(table);
				}

			} catch (DocumentException | IOException e) {
				Logger.error("DeaccessionReport:",e);
			}

		}

		public void onEndPage(PdfWriter writer, Document document) {

			Rectangle rect = writer.getPageSize();// writer.getBoxSize("art");

			String buttom = "Verify Actions - Printed Name: __________________ Signature: __________________ Date: __________________";

			ColumnText.showTextAligned(
					writer.getDirectContent(),
					Element.ALIGN_CENTER,
					new Phrase(buttom, FontFactory.getFont(
							FontFactory.HELVETICA, 10, Font.BOLD)), rect
							.getLeft() + 350, 10, 0f);

			ColumnText.showTextAligned(
					writer.getDirectContent(),	
					Element.ALIGN_CENTER,
					new Phrase(String.format("%d", pagenumber), FontFactory
							.getFont(FontFactory.HELVETICA, 10, Font.BOLD)),
					rect.getRight() - 20, 10, 0f);

		}
	}
	
	public String createPdf( DeaccesionReportView view) throws FileNotFoundException, DocumentException {
	
		
		String dataDir = Play.application().configuration()
				.getString("application.deaccesion.process.data.Dir");

		String fileName = rand.nextInt(10000) + "-" + "-deaccession.pdf";

		String destFileString = dataDir + File.separator + fileName;
		
		Document document = new Document(PageSize.A4.rotate());

		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(destFileString));

		String standard = view.standard.equals("Ithica")? "Ithaka What to Withdraw": "CRL preservation criteria";
		
		HeaderFooter event = new HeaderFooter(view.jobName, view.initDate,
					 view.orgName,view.userName, standard,
					 view.NumberOfDeaccession, view.NumberOfDonation,view.numberOfPreservation, view.total);

		writer.setPageEvent(event);

		
		document.open();
	
		for (DeaccessionTitleView adeaccessionTitleView : view.deaccessionTitleView){	
			
			String issn = adeaccessionTitleView.issn ;
			
			String title =  adeaccessionTitleView.title + " | " + issn;
			
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
			
			for(DeaccessionIssueView adeaccessionIssueView :adeaccessionTitleView.deaccessionIssueView){
				
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
				
				String planned  = null;
				
				if("p".equals(adeaccessionIssueView.action)){
					planned = "Preserve";
				}else if ("d".equals(adeaccessionIssueView.action)){
					planned = "Deaccess";
				}else if ("n".equals(adeaccessionIssueView.action)){
					planned = "Donate";
				}

				cell = new PdfPCell(new Phrase(planned,
						FontFactory.getFont(FontFactory.HELVETICA, 10,
								Font.NORMAL)));
				cell.setBorder(Rectangle.NO_BORDER);
				if (shaded)
					cell.setGrayFill(0.9f);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("  ___                   _____________",
						FontFactory.getFont(FontFactory.HELVETICA, 10,
								Font.NORMAL)));
				cell.setBorder(Rectangle.NO_BORDER);
				if (shaded)
					cell.setGrayFill(0.9f);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(adeaccessionIssueView.volissue,
						FontFactory.getFont(FontFactory.HELVETICA, 10,
								Font.NORMAL)));
				cell.setBorder(Rectangle.NO_BORDER);
				if (shaded)
					cell.setGrayFill(0.9f);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(adeaccessionIssueView.location,
						FontFactory.getFont(FontFactory.HELVETICA, 10,
								Font.NORMAL)));
				cell.setBorder(Rectangle.NO_BORDER);
				if (shaded)
					cell.setGrayFill(0.9f);
				table.addCell(cell);
				

				for(String adonnee : adeaccessionTitleView.donnee){
					cell = new PdfPCell(new Phrase("Recommended Donee: "+ adonnee,
							FontFactory.getFont(FontFactory.HELVETICA, 10,
									Font.ITALIC)));
					cell.setBorder(Rectangle.NO_BORDER);
					if (shaded)
						cell.setGrayFill(0.9f);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(5);
					table.addCell(cell);
				}
				
				document.add(table);
				
				shaded = shaded ? false : true;
				
			}

		}
		
		document.close();
		
		return destFileString;
	}

}
