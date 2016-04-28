package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(name = "pwanttitlemember")
public class PwantTitleMember  extends Model{
	@Id
	@GeneratedValue
	@Column(name="pwantTitleMember")
	int pwantTitleMember;
	
	@Column(name="titleID")
	public int titleID;
	
	@Column(name="memberID")
	public int memberID;
	
	public PwantTitleMember (int titleID, int memberID){
		this.titleID = titleID;
		this.memberID = memberID;
	}
	
	public static Finder<Integer, PwantTitleMember> find = new Finder<Integer, PwantTitleMember>(
			Integer.class,PwantTitleMember
			.class);

}
