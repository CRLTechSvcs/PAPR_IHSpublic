package json;

import java.util.ArrayList;
import java.util.List;

public class TitleView {
	public int titleVersionId = 0;
	public int titleId = 0;
	public String title = "";
	public String alphaTitle="";
	public String publisher = "";
	public String printISSN = "";
	public String eISSN = "";
	public String oclcNumber = "";
	public String lccn= "";
	public String publicationRange = "";
	public List<PublicationRangeView> publicationRangeViews = new ArrayList<PublicationRangeView>();
	public String language;
	public String country;
	public String volumeLevelFlag = "No";
	public String editVolumeLevelFlag = "No";

	public TitleView() {

	}

	public TitleView(int titleId, String title) {
		this.titleId = titleId;
		this.title = title;
	}

  /*************************************************************
  AJE 2016-10-25 add constructor for TitleView that includes setting the publisher: see IhsTitle.java : getTitle() + getTitleBrowse()
  */
	public TitleView(int titleId, String title, String publisher) {
		this.titleId = titleId;
		this.title = title;
		this.publisher = publisher;
	}



	public TitleView(int titleVersionId, int titleId, String title, String alphaTitle, String publisher,
			String printISSN, String eISSN, String oclcNumber, String lccn,
			String publicationRange,
			List<PublicationRangeView> publicationRangeViews,
			String language, String country) {
		this.titleVersionId =  titleVersionId;
		this.titleId = titleId;
		this.title = title;
		this.alphaTitle = alphaTitle;
		this.publisher= publisher;
		this.printISSN = printISSN;
		this.eISSN = eISSN;
		this.oclcNumber = oclcNumber;
		this.lccn= lccn;
		this.publicationRange=publicationRange;
		if( publicationRangeViews != null)
			this.publicationRangeViews =publicationRangeViews;
		this.language = language;
		this.country = country;
	}
}