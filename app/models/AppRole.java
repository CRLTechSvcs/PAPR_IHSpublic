package models;

import java.io.Serializable;

import be.objectify.deadbolt.core.models.Role;

public class AppRole implements Role, Serializable{

	public String name= null;
	
	public AppRole(String name){
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}

}
