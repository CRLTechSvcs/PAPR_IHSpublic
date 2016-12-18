package models;

import java.util.ArrayList;
import java.util.List;

import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by reza on 9/11/2014.
 */
@Entity
@Table(name = "ihsmember")
public class IhsMember extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="memberID")
	public int memberID;


	public String name;

	public String description;
	
	public String ftpdirectory;


	@OneToOne(fetch = FetchType.LAZY, mappedBy = "ihsMember")
	public IhsLocation ihsLocation;
	
   @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
   @JoinColumn(name="addressId")
	public IhsAddress ihsAddress;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "ihsMember")
	public IhsAuthorizedSource ihsAuthorizedSource;

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "ihsMember")
	public List<IhsUser> ihsUsers = new ArrayList<>();
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="memberStatusID")
    public SmemberStatus smemberStatus;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="membergroupID")
    public IhsMemberGroup ihsMemberGroup;
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ihsMember")
	public List<IhsHolding> ihsHoldings = new ArrayList<>();
	
	
	
	public IhsMember(String name, String description, SmemberStatus smemberStatus, String ftpdirectory) {
		this.name = name;
		this.description = description;
		this.smemberStatus = smemberStatus;
		this.ftpdirectory = ftpdirectory;
	}

	public IhsMember(String name, String description, SmemberStatus smemberStatus, String ftpdirectory, IhsAddress ihsAddress, IhsMemberGroup ihsMemberGroup) {
		this.name = name;
		this.description = description;
		this.smemberStatus = smemberStatus;
		this.ftpdirectory = ftpdirectory;
		this.ihsAddress = ihsAddress;
		this.ihsMemberGroup = ihsMemberGroup;
	}
	
	public void setName( String name){
		this.name = name;
	}
	
	public void setDescription( String description){
		this.description = description;
	}
	
	public static Finder<Integer, IhsMember> find = new Finder<Integer, IhsMember>(
			Integer.class, IhsMember.class);

/*****************************************
AJE 2016-12-16 

	static String memberByNameSQL = "SELECT memberID, name FROM ihsmember WHERE name = 'param' ORDER BY name ASC;";
	
	public static List <MemberView> getMemberByName(String search){
    Logger.info("app/models/IhsMember.java : getMemberByName(search=|" +search+"|).");
    List <MemberView> memberViews = new ArrayList<MemberView>();
    String tmpSql = memberByNameSQL.replaceAll("param", search);
		Logger.info("... getMemberByName will use SQL: " +tmpSql);

    List<SqlRow> sqlRows = Ebean.createSqlQuery(tmpSql)
      .findList();

    if (sqlRows.size() > 0){
      for(SqlRow sqlRow : sqlRows){
        Logger.info(sqlRow.getInteger("memberID").toString()+ " ; "+ sqlRow.getString("title"));
        memberViews.add(
          new MemberView(
            sqlRow.getInteger("memberID"),
            sqlRow.getString("name"),
            sqlRow.getString("description")
        ));
        Logger.info(sqlRow.getInteger("memberID").toString()+ " ; "+ sqlRow.getString("name") + " : " + sqlRow.getString("description") );
      }
    } else {
      memberViews.add(
          new MemberView(
            0, // sqlRow.getInteger("memberID"),
            "No results for '" +search+ "'.", //sqlRow.getString("title"),
            " " // sqlRow.getString("publisher")
        ));
    }

    return memberViews;
	}
/*
AJE 2016-12-16 14:55:33
******************************************/

//AJE 2016-12-16 
	public static Finder<Integer, String> find_by_name = new Finder<Integer, String>(
			Integer.class, String.class);



}