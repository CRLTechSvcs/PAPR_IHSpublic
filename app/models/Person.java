package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Person extends Model {

	@Id
	@GeneratedValue
	public String id;

	public String name;
	
	public Person( String name){
		//this.id = id;
		this.name = name;
	}
	
	public static Finder<String, Person> find = new Finder<String, Person>(String.class, Person.class);
}