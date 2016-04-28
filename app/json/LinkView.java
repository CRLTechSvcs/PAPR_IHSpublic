package json;

import java.util.ArrayList;
import java.util.List;

public class LinkView {
	public int titleId = 0;
	public String title = "";
	public String publicationRange = "";
	public List<LinkViewParentChild> parents = new ArrayList<LinkViewParentChild>();
	public List<LinkViewParentChild> childs = new ArrayList<LinkViewParentChild>();

	public LinkView(int titleId, String title, String publicationRange ) {
		
		this.titleId = titleId;
		this.title = title;
		this.publicationRange = publicationRange;
	}
}