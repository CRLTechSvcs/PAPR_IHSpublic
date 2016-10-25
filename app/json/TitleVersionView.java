package json;

import java.util.ArrayList;
import java.util.List;

public class TitleVersionView {
	public int titleVersionId = 0;
	public int titleId = 0;
	public String title = "";
	public String alphaTitle = "";
	public int publisher = 0;
	public String publisherName = "";
	public String printISSN = "";
	public String eISSN = "";
	public String oclcNumber = "";
	public String lccn = "";
	public String note = "";
	public String publicationRange = "";
	public List<PublicationRangeView> publicationRangeViews = new ArrayList<PublicationRangeView>();
	public String currentVersionFlag = "";
	public String changeDate = "";
	public String changeUser = "";
	public String changeMember = "";
	public String imagePageRatio="";
	public String language="";
	public String country="";

	public TitleVersionView() {

	}

	public TitleVersionView(int titleId, String title) {
		this.titleId = titleId;
		this.title = title;
	}

	public TitleVersionView(int titleVersionId, int titleId, String title,
			String alphaTitle, int publisher, String publisherName,
			String printISSN, String eISSN, String oclcNumber, String lccn,
			String note,
			String publicationRange,
			List<PublicationRangeView> publicationRangeViews,
			String currentVersionFlag, String changeDate, String changeUser,
			String changeMember, String imagePageRatio,
			String language,
			String country) {
		this.titleVersionId = titleVersionId;
		this.titleId = titleId;
		this.title = title;
		this.alphaTitle = alphaTitle;
		this.publisher = publisher;
		this.printISSN = printISSN;
		this.publisherName = publisherName;
		this.eISSN = eISSN;
		this.oclcNumber = oclcNumber;
		this.lccn = lccn;
		this.note = note;
		this.publicationRange = publicationRange;
		if (publicationRangeViews != null)
			this.publicationRangeViews = publicationRangeViews;
		this.currentVersionFlag = currentVersionFlag;
		this.changeDate = changeDate;
		this.changeUser = changeUser;
		this.changeMember = changeMember;
		this.imagePageRatio=imagePageRatio;
		this.language = language;
		this.country= country;
	}
}