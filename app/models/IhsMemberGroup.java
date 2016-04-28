package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(name = "ihsmembergroup")
public class IhsMemberGroup extends Model{

	@Id
	@GeneratedValue
	@Column(name="membergroupID")
	public int membergroupID;
	
	public String name;
	public String description;
	
	
	
	public int getMembergroupID() {
		return membergroupID;
	}



	public void setMembergroupID(int membergroupID) {
		this.membergroupID = membergroupID;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public static Finder<Integer, IhsMemberGroup> find = new Finder<Integer, IhsMemberGroup>(
			Integer.class, IhsMemberGroup.class);

}