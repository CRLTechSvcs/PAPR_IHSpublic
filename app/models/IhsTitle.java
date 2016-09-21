package models;

import java.util.ArrayList;
import java.util.List;

import play.db.ebean.Model;
import util.Helper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.joda.time.DateTime;

import json.TitleView;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

/**
 * Created by reza on 9/11/2014.
 */



@Entity
@Table(name = "ihstitle")
public class IhsTitle extends Model {

  /* AJE 2016-09-15 documentation about MATCH AGAINST, which I have never run into before:
    http://www.indepthinfo.com/sql-functions/match-against-function.php
    "MATCH()...AGAINST() syntax is used to search in FULLTEXT indexes [...]
    MATCH portion defines particular columns to be searched through their indexes,
    AGAINST() portion contains keyword(s) to be searched [...]
    IN BOOLEAN MODE allows specific items to be excluded from a search [with slight different syntax [...]
    Results of this natural language query are listed in order of relevance"
    more at link */
	static String titleSearchSql = "SELECT titleId, title,  MATCH (title) AGAINST (' param ' IN BOOLEAN MODE) as score "
			+ "FROM ihstitle "
			+ "WHERE MATCH (title) AGAINST (' param ' IN BOOLEAN MODE) " // preserve Travant original code:
//+ "WHERE title LIKE \"%' param '%\" " // AJE new TEST
			/* AJE 2016-09-15 we want the titles to be alphabetical ; preserve Travant original code:
			+ "ORDER BY score DESC "
			+ "limit  20;" // end Travant original */
			+ "ORDER BY title ASC, score DESC " // AJE 2016-09-15 : reversing these ORDER BY parts creates chaos
			+ "LIMIT  50;"; // if not included, results will often be too long for sidebar; some searches can be too long for the entire page
			//once results moved into center white space portion of screen, try removing LIMIT again

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue
	@Column(name = "titleID")
	public int titleID;

	public String title;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="titleTypeID")
    public StitleType stitleType;

	@Column(name="alphaTitle")
	public String alphaTitle;

	@Column(name="printISSN")
	public String printISSN;

	@Column(name="eISSN")
	public String eISSN;

	@Column(name="oclcNumber")
	public String oclcNumber;

	public String lccn;


	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="publisherID")
    public IhsPublisher ihsPublisher;

	public String description;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="titleStatusID")
    public StitleStatus stitleStatus;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ihsTitle")
	public List<IhsVolume> ihsVolume = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ihsTitle")
	public List<IhsPublicationRange> ihsPublicationRange = new ArrayList<>();

	@Column(name="changeDate")
    public DateTime changeDate;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	public IhsUser ihsUser;

	@Column(name="titleVersion")
	public int titleVersionID;

	@Column(name="imagePageRatio")
	public Integer imagePageRatio;

	@Column(name="language")
	public String language;

	@Column(name="country")
	public String country;

	@Column(name="volumeLevelFlag")
	public String volumeLevelFlag;


	public IhsTitle(){

	};

	public void setIhsUser(IhsUser ihsUser){

		this.ihsUser = ihsUser;
	}


	public IhsTitle(String title, StitleType stitleType, String alphaTitle, String printISSN,
			String eISSN,  String oclcNumber, String lccn,
			IhsPublisher ihsPublisher, String description, StitleStatus stitleStatus) {
		this.title = title;
		this.stitleType = stitleType;
		this.alphaTitle = alphaTitle;
		this.printISSN = printISSN;
		this.eISSN = eISSN;
		this.oclcNumber = oclcNumber;
		this.lccn = lccn;
		this.ihsPublisher = ihsPublisher;
		this.description = description;
		this.stitleStatus = stitleStatus;
	}

	public IhsTitle(String title, StitleType stitleType, String alphaTitle, String printISSN,
			String eISSN,  String oclcNumber, String lccn,
			IhsPublisher ihsPublisher, String description, StitleStatus stitleStatus, DateTime changeDate, IhsUser ihsUser,int titleVersionID ) {
		this.title = title;
		this.stitleType = stitleType;
		this.alphaTitle = alphaTitle;
		this.printISSN = printISSN;
		this.eISSN = eISSN;
		this.oclcNumber = oclcNumber;
		this.lccn = lccn;
		this.ihsPublisher = ihsPublisher;
		this.description = description;
		this.stitleStatus = stitleStatus;
		this.changeDate = changeDate;
		this.ihsUser = ihsUser;
		this.titleVersionID = titleVersionID;
	}

	public void setTitle(String title){

		this.title= title;

	}
	public static List <TitleView> getTitle(String search){

		 List <TitleView> titleViews= new ArrayList<TitleView>();

		 String tmpSql = titleSearchSql.replaceAll("param", search);

		List<SqlRow> sqlRows = Ebean.createSqlQuery(tmpSql)
				.findList();

		for(SqlRow sqlRow : sqlRows){

			titleViews.add(new TitleView( sqlRow.getInteger("titleId"), sqlRow.getString("title")));
		}

		return titleViews;

	}

	public static List <TitleView> getByISSN(String search){


		 List <TitleView> titleViews= new ArrayList<TitleView>();

		 IhsTitle ihsTitle = IhsTitle.find
				 			.where()
				 			.eq("printISSN", search)
				 			.findUnique();

		if(ihsTitle != null)
			titleViews.add(new TitleView( ihsTitle.titleID, ihsTitle.title));


		return titleViews;

	}

	public static List <TitleView> getByOCLC(String search){


		 List <TitleView> titleViews= new ArrayList<TitleView>();

		 IhsTitle ihsTitle = IhsTitle.find
				 			.where()
				 			.eq("oclcNumber", search)
				 			.findUnique();

		if(ihsTitle != null)
			titleViews.add(new TitleView( ihsTitle.titleID, ihsTitle.title));


		return titleViews;

	}

	public static TitleView getDetailById(int searchId){

		TitleView titleView= new TitleView();

		IhsTitle ihsTitle = IhsTitle.find.fetch("ihsPublisher").fetch("ihsPublicationRange").where().eq("titleID", searchId).findUnique();

		if(ihsTitle != null){

			titleView.titleId = ihsTitle.titleID;
			titleView.title = ihsTitle.title;
			titleView.publisher= ihsTitle.ihsPublisher.name.length() > 26 ? ihsTitle.ihsPublisher.name.substring(0,25): ihsTitle.ihsPublisher.name;


			titleView.printISSN = Helper.formatIssn(ihsTitle.printISSN);

			if( ihsTitle.eISSN != null){
				titleView.eISSN =  Helper.formatIssn(ihsTitle.eISSN);
			}

			if( ihsTitle.oclcNumber != null){
				titleView.oclcNumber = ihsTitle.oclcNumber;
			}



			titleView.publicationRange = Helper.getPublicationRange(ihsTitle.ihsPublicationRange);

			titleView.language = ihsTitle.language;
			titleView.country = ihsTitle.country;
			titleView.volumeLevelFlag = ihsTitle.volumeLevelFlag.equals("1") ? "Yes" : " No";

			try{
				ihsTitle.ihsVolume.get(0).ihsissues.get(0);
				titleView.editVolumeLevelFlag = "Yes";
			}catch (Exception e){

			}

		}

		return titleView;

	}

	public static Finder<Integer, IhsTitle> find = new Finder<Integer, IhsTitle>(
			Integer.class, IhsTitle.class);
}
