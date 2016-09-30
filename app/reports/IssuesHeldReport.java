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

			ColumnText.showTextAligned( // type of report, as shown in top left of report [Travant code]
					writer.getDirectContent(),
					Element.ALIGN_CENTER, // AJE 2016-01-25 this is original
					// Element.ALIGN_RIGHT, // 2016-01-25 AJE worse for cutting off the edge?
					new Phrase(report, FontFactory.getFont( // AJE test ; this line is original
					//new Phrase("Report Name AJE", FontFactory.getFont( // AJE: this line does affect the output
							FontFactory.HELVETICA, 10, Font.NORMAL)),
							//rect.getLeft() + 88, rect.getHeight() - 20, 0f); // AJE 2016-01-21 original values here
							rect.getLeft() + 180, rect.getHeight() - 20, 0f); // AJE 2016-01-22 long IhsMember.name means first part of 'report' (title of report generated) pushed off side of page
							// AJE 2016-01-22 tried inserting newline inside 'report' var

			ColumnText.showTextAligned( // date of report, as shown in top right of report [Travant code]
					writer.getDirectContent(),
					Element.ALIGN_CENTER,
					new Phrase("Prepared " + date, FontFactory.getFont(
							FontFactory.HELVETICA, 10, Font.NORMAL)),
							rect.getRight() - 120, rect.getHeight() - 20, 0f);
			try {

        // AJE: inserting a set of cells here and attaching to document will put those cells above the title / issn line

        // AJE 2016-01-20 16:43 here is the original location of the "if (pagenumber == 1)" block

        // AJE 2016-01-21 this is header table
				table = new PdfPTable(5);
				//table.setWidths(new float[] { 1.2f, 2, 3, 4, 5 }); // AJE 2016-01-21 original values here
				// AJE 2016-01-21 note there are 2 places where table.setWidths() is invoked: one for the header and one for the data
				//table.setWidths(new float[] { 9f, 2, 3, 4, 5 }); // AJE 2016-01-21 new values here : squeezes 'Volume' label
				//table.setWidths(new float[] { 8f, 3, 4, 5, 6 }); // AJE 2016-01-21 new values here
				table.setWidths(new float[] { 8f, 3, 4, 5, 6 }); // AJE 2016-01-21 new values here
				table.setWidthPercentage(100);

				cell = new PdfPCell(new Phrase("")); // AJE 2016-01-22 if filled in, this appears at top of every page under 'report' and 'Prepared [date'
				cell.setColspan(5);
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);

// AJE 2016-01-21 this is the original setup location for the column headings : saved pretty original code in IssuesHeldReport.java.column_headers.original //
  // BUT : now that the column headings appear under every title, we don't need this to show on the very first page

/* AJE 2016-01-20 16:46 : new location for "if (pagenumber == 1)" block - now AFTER the first header :
    this puts the Title-Volume-Issue-Date-Location headers on every page INCLUDING the first,
    which is new and was the original request by Amy for how it should look */
        if (pagenumber == 1) { // AJE 2016-01-13 tried removing IF braces, so do this on every page : FAILED: headers show up on no pages at all, revert to original
					//Image image1 = Image.getInstance("public/images/papr_logo.gif"); // old AJE 2016-09-30
					Image image1 = Image.getInstance("public/images/papr_ihs_logo.gif");
					document.add(image1);

          /* AJE 2016-09-30 FontFactory.HELVETICA, 30, can reduce to 20 and is nicer,
              but this output is redundant: see next below
					ColumnText.showTextAligned(
							writer.getDirectContent(),
							Element.ALIGN_LEFT,
							AJE 2016-09-30 testing : preserve the old
							new Phrase(report,
							  FontFactory.getFont( FontFactory.HELVETICA, 30, Font.NORMAL)),
							  rect.getRight() - 480, rect.getHeight() - 70, 0f); // AJE 2016-01-21 original values here
							*/
							// end AJE 2016-09-30



					table = new PdfPTable(1);
					table.setWidthPercentage(100);

					//cell = new PdfPCell(new Phrase("For " + org, // AJE 2016-09-30 Travant original
					cell = new PdfPCell(new Phrase(report + " for " + org,
							//FontFactory.getFont(FontFactory.HELVETICA, 25, Font.NORMAL))); // AJE 2016-01-21 original values here
							//FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL))); // AJE 2016-01-21 no way do we need a size 25 font ever
							FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD))); // AJE 2016-09-30 embiggen and embolden
					cell.setBorder(Rectangle.NO_BORDER);
					table.addCell(cell);

					document.add(table);

				} // AJE 2016-01-22 : next : whole else block is AJE, to put column headers on pages after #1
				else {
          table = new PdfPTable(5);
				  table.setWidths(new float[] { 8f, 3, 4, 5, 6 }); // AJE 2016-01-21 new values here
				  table.setWidthPercentage(100);

  				cell = new PdfPCell(new Phrase("Title (ISSN)",
  				    FontFactory.getFont( FontFactory.HELVETICA, 16, Font.BOLD)));
  				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
  				table.addCell(cell);

  				cell = new PdfPCell(new Phrase("Volume",
  						FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD)));
  				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
  				table.addCell(cell);

  				cell = new PdfPCell(new Phrase("Number",
  						FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD)));
  				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
  				table.addCell(cell);

  				cell = new PdfPCell(new Phrase("Date",
  				    FontFactory.getFont( FontFactory.HELVETICA, 16, Font.BOLD)));
  				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
  				table.addCell(cell);

  				cell = new PdfPCell(new Phrase("Location",
  				    FontFactory.getFont( FontFactory.HELVETICA, 16, Font.BOLD)));
  				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
  				table.addCell(cell);

  				cell = new PdfPCell(new Phrase("")); // AJE 2016-01-22 the blank line after column header on top of every page
  				cell.setColspan(5);
  				cell.setBorder(Rectangle.NO_BORDER);
  				table.addCell(cell);

  				document.add(table);
			  }// end if : the else part is AJE 2016-01-22
/* end AJE 2016-01-20 16:46 : new location for if block */

			} catch (Exception e) {
				// Logger.error("DeaccessionReport:", e); // AJE 2016-01-25 this line original, probably overlooked in copying a file?
				Logger.error("IssuesHeldReport:", e);
			}

		}

		public void onEndPage(PdfWriter writer, Document document) {

			Rectangle rect = writer.getPageSize();

			ColumnText.showTextAligned(
					writer.getDirectContent(),
					Element.ALIGN_CENTER,
					// new Phrase(String.format("%d", pagenumber), FontFactory // AJE 2016-01-25 this line original
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

			//String fileName = rand.nextInt(10000) + "-" + "-IssuesHeldReport.pdf"; // AJE 2016-01-20 was 'IssueHeldReport.pdf'
			String fileName = rand.nextInt(10000) + "_IssuesHeldReport.pdf"; // AJE 2016-08-23

			destFileString = dataDir + File.separator + fileName;

			Logger.info("IssueHeldReport.java, createPdf() will create the file at: " +destFileString);


			Document document = new Document(PageSize.A4.rotate());

			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(destFileString));

			String report = "Issues Held Report";  // AJE 2016-01-20 was 'Issue Held Report'
			String date = dtfShort.print(dateInitiated);

			IhsMember ishMember = IhsMember.find.byId(newReportView.memberId);
      //report = report + " : " + ishMember.name; // AJE 2016-01-20 new : WORKS but start of text is pushed off left margin
      //report = report + System.lineSeparator() + ishMember.name; // AJE 2016-01-21 : see comment 'make room for IhsMember.Name'
      report = report + " : " + ishMember.name; // AJE 2016-01-21 : see comment 'make room for IhsMember.Name'

			HeaderFooter event = new HeaderFooter(report, date, ishMember.name);

			writer.setPageEvent(event);

			document.open();

			for(IhsTitle ihsTitle: ihsTitles){

				String issn = Helper.formatIssn(ihsTitle.printISSN) ;

				//String title =  ihsTitle.title + " | " + issn; // AJE 2016-01-13 -- want first and all subsequent pages to show a header
				//String title =  ihsTitle.title + " | " + issn + " | held by: " +ishMember.name; // AJE 2016-01-20 : make it more awesome
				// above is awesome but if we put the title + issn into Title column and org_name into Location column, no need for this header at all
				String title = "";
				if(ihsTitle.title.endsWith(".")){ // AJE 2016-01-21 this if block added to remove trailing periods from titles
          title =  ihsTitle.title.substring(0, ihsTitle.title.length() - 1) + " (" +issn+ ") ";
        } else {
          title =  ihsTitle.title + " (" +issn+ ")";
        }// AJE 2016-01-21 end

				//event.setTitle("line 249      "+ title);
				event.setTitle("      "+ title); // AJE : original values here

				PdfPTable table = new PdfPTable(1);
				table.setWidthPercentage(100);
				table.getDefaultCell().setBorder(4);

				PdfPCell cell = new PdfPCell(new Phrase("")); // AJE 2016-01-22 the blank line after all the issues of a title
				cell.setColspan(1);
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);

/*
end AJE 2016-01-20 10:46
*****************************************/
				cell = new PdfPCell(new Phrase(title,
						FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD))); // AJE 2016-01-20 font size was 12
				cell.setColspan(1);
				table.addCell(cell);
/* AJE 2016-01-21 try adding header row after every title header cell */
        document.add(table); // AJE 2016-01-22 add the title cell before changing the table back to 5 columns

  			table = new PdfPTable(5);
  			table.setWidths(new float[] { 8f, 3, 4, 5, 6 }); // AJE 2016-01-21 new values here
  			table.setWidthPercentage(100);
  // end AJE 2016-01-22 new

				cell = new PdfPCell(new Phrase("Title (ISSN)",
				    FontFactory.getFont( FontFactory.HELVETICA, 16, Font.BOLD)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Volume",
						FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Number",
						FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Date",
				    FontFactory.getFont( FontFactory.HELVETICA, 16, Font.BOLD)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase("Location",
				    FontFactory.getFont( FontFactory.HELVETICA, 16, Font.BOLD)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

        // AJE 2016-01-20 removed blank line, reinstated next day ; 2016-01-22 removed again, added again
				cell = new PdfPCell(new Phrase("")); // AJE 2016-01-22 the blank line after first (or new) title and column headers
				cell.setColspan(5);
				cell.setBorder(Rectangle.NO_BORDER);
        // System.out.println("\n\n cell.getFixedHeight() = :" +cell.getFixedHeight()+ ":\n\n"); // AJE : no fixed height
        cell.setFixedHeight(3f); // AJE 2016-01-22 new, to make the height of the blank line lower
				table.addCell(cell);

				document.add(table);

				// AJE 2016-01-22 now reset table to prevent header row from showing twice after title
  			table = new PdfPTable(5);
  			table.setWidths(new float[] { 8f, 3, 4, 5, 6 }); // AJE 2016-01-21 new values here
  			table.setWidthPercentage(100);

/* end AJE 2016-01-21 try adding header row after every title header cell */


				cell = new PdfPCell(new Phrase("line 331")); // AJE 2016-01-22 the blank line after ... can't find this in output
				cell.setColspan(1);
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);
				document.add(table);


				boolean shaded = true;

				int j = 1;

				for(IhsVolume ihsVolume : ihsTitle.ihsVolume){

					for( IhsIssue ihsIssue : ihsVolume.ihsissues){
						event.setTitleid(j);

            // AJE 2016-01-21 this is data table
						table = new PdfPTable(5);
						//table.setWidths(new float[] { 1.2f, 2, 3, 4, 5 }); // AJE 2016-01-21 original values here
						// AJE 2016-01-21 note there are 2 places where table.setWidths() is invoked: one for the header and one for the data
						table.setWidths(new float[] { 8f, 3, 4, 5, 6 }); // AJE 2016-01-21 new values here
						table.setWidthPercentage(100);

						// cell = new PdfPCell(new Phrase(j+"", // AJE 2016-01-20 15:10 changed this Travant original to put meaningful data in first column
    				if(ihsTitle.title.endsWith(".")){ // AJE 2016-01-21 this if block added to remove trailing periods from titles
              title =  ihsTitle.title.substring(0, ihsTitle.title.length() - 1) + " (" +issn+ ")";
            } else {
              title =  ihsTitle.title + " (" +issn+ ")";
            }// AJE 2016-01-21 end
						cell = new PdfPCell(new Phrase( title, // AJE 2016-01-20 17:55 new inclusion of title + ISSN ; approved by Amy
								//FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL))); // AJE 2016-01-21 original values here
								FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));	// new AJE 2016-01-21 : this changes rarely, so smaller to fit and de-emphasize
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);	// new AJE 2016-01-20
						cell.setBorder(Rectangle.NO_BORDER);

						j++;

						if (shaded)
							cell.setGrayFill(0.9f);
						table.addCell(cell);

						//cell = new PdfPCell(new Phrase(ihsVolume.volumeNumber, // AJE added abbreviation before data: this line Travant original
						cell = new PdfPCell(new Phrase("v." + ihsVolume.volumeNumber,
								FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))); // AJE 2016-01-21 was Font.NORMAL
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);	// new AJE 2016-01-20
						cell.setBorder(Rectangle.NO_BORDER);
						if (shaded)
							cell.setGrayFill(0.9f);
						table.addCell(cell);

						//cell = new PdfPCell(new Phrase(ihsIssue.issueNumber,  // AJE added abbreviation before data: this line Travant original
						cell = new PdfPCell(new Phrase("no." + ihsIssue.issueNumber,
								FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))); // AJE 2016-01-21 was Font.NORMAL
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);	// new AJE 2016-01-20
						cell.setBorder(Rectangle.NO_BORDER);
						if (shaded)
							cell.setGrayFill(0.9f);
						table.addCell(cell);

						String datemmdyy =  ihsIssue.publicationDate !=null ? dateFormatmmyy.print(ihsIssue.publicationDate) : "";

						cell = new PdfPCell(new Phrase(datemmdyy,
								FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD))); // AJE 2016-01-21 was Font.NORMAL
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);	// new AJE 2016-01-20
						cell.setBorder(Rectangle.NO_BORDER);
						if (shaded)
							cell.setGrayFill(0.9f);
						table.addCell(cell);

						//cell = new PdfPCell(new Phrase("", // AJE 2016-01-13: this line Travant original
						// AJE 2016-01-20 DEVNOTE: empty string used above shows there is no Location data or field yet
						cell = new PdfPCell(new Phrase(ishMember.name, // AJE 2016-01-20 17:58 per Amy: show holding organization in this column
								//FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL))); // AJE 2016-01-21 original values here
								FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL)));	// new AJE 2016-01-21 : this is always same in this report, so smaller font to fit and de-emphasize
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);	// new AJE 2016-01-20
						cell.setBorder(Rectangle.NO_BORDER);
						if (shaded)
							cell.setGrayFill(0.9f);

						table.addCell(cell);

						document.add(table);

						shaded = shaded ? false : true;
					} // end for issue
				} // end for volume
			} // end for title

			document.close();
		} catch (Exception e) {
			Logger.error("", e);
		}

Logger.info("IssuesHeldreport.java, createPdf() will return destFileString = '" +destFileString+ "'.");
		return destFileString;

	}

}