package controllers;

import static play.libs.Json.toJson;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import json.CountryView;
import json.GroupView;
import json.MemberView;
import json.NewReportView;
import json.UserView;
import models.AppUser;
import models.IhsAddress;
import models.IhsLocation;
import models.IhsMember;
import models.IhsMemberGroup;
import models.IhsSecurityRole;
import models.IhsTitle;
import models.IhsUser;
import models.Scountry;
import models.SmemberStatus;
import models.SstateProvince;
import models.SuserStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import util.Helper;
import util.LogRequest;
import views.html.*;

@LogRequest 
@Restrict(@Group("admin"))
public class Administration extends Controller{
	
	public static Result administration_home(){
		return ok(administration_home.render());
	}
	
	public static Result getAdministrationGroup(){
		return ok(administration_group.render());
	}
	
	public static Result getAdministrationMember(){
		return ok(administration_member.render());
	}
	
	public static Result getAdministrationUser(){
		return ok(administration_user.render());
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result addAGroup(){
	
	GroupView groupView = null;
		
	try{
		String jsonString = Controller.request().body().asJson().toString();

		ObjectMapper mapper = new ObjectMapper();
		
		groupView  = mapper.readValue(jsonString, GroupView.class);

		String user = session().get(Login.User);

		List<IhsMemberGroup> ihsMemberGroups = IhsMemberGroup.find.where().ieq("name", groupView.groupName.trim()).findList();

		if(ihsMemberGroups.size() > 0){
			return ok(groupView.groupName + " group exists");
		
		}else{
			IhsMemberGroup ihsMemberGroup = new IhsMemberGroup();
			ihsMemberGroup.name= groupView.groupName;
			ihsMemberGroup.description = groupView.groupDesc;
			
			ihsMemberGroup.save();
			
		}
		
	} catch (Exception e) {
		Logger.error("ingestion_exception_resolve", e);
		return internalServerError("ingestion_exception_resolve");
	}
		return ok(groupView.groupName + " group added");
	}
	
	
	
	public static Result getAllGroup(){
		
		List <GroupView> groupViews = new ArrayList<GroupView>();
		
		List<IhsMemberGroup> ihsMemberGroups = IhsMemberGroup.find.where().orderBy("name").findList();
		
		for(IhsMemberGroup ihsMemberGroup : ihsMemberGroups){
			groupViews.add(new GroupView(ihsMemberGroup.membergroupID, ihsMemberGroup.name, ihsMemberGroup.description));
		}
		
		return ok(toJson(groupViews));
	}

	public static Result searchGroup(String name){
		
		List <GroupView> groupViews = new ArrayList<GroupView>();
		
		List<IhsMemberGroup> ihsMemberGroups = null;
		
		if("nbsp".equals(name)){
			ihsMemberGroups = IhsMemberGroup.find.where().orderBy("name").findList();
		}else{
			ihsMemberGroups = IhsMemberGroup.find.where().ilike("name", "%"+name+"%").orderBy("name").findList();
		}
		
		for(IhsMemberGroup ihsMemberGroup : ihsMemberGroups){
			groupViews.add(new GroupView(ihsMemberGroup.membergroupID, ihsMemberGroup.name, ihsMemberGroup.description));
		}
		
		return ok(toJson(groupViews));
	}
	
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result editGroup(){
	
	try{
		String jsonString = Controller.request().body().asJson().toString();

		ObjectMapper mapper = new ObjectMapper();
		
		GroupView groupView  = mapper.readValue(jsonString, GroupView.class);
		
		IhsMemberGroup ihsMemberGroups = IhsMemberGroup.find.byId(groupView.groupId);
		
		ihsMemberGroups.setName(groupView.groupName);
		ihsMemberGroups.setDescription(groupView.groupDesc);
		
		ihsMemberGroups.update();
		
	} catch (Exception e) {
		Logger.error("ingestion_exception_resolve", e);
		return internalServerError("ingestion_exception_resolve");
	}
		return ok();
	}
	
	/******************* Member **********************/
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result addAMember(){
	
	MemberView memberView = null;
	
	try{
		String jsonString = Controller.request().body().asJson().toString();

		ObjectMapper mapper = new ObjectMapper();
		
		memberView  = mapper.readValue(jsonString, MemberView.class);

		String user = session().get(Login.User);
		
		IhsMemberGroup ihsMemberGroups = IhsMemberGroup.find.byId(memberView.groupId);
		
		List<IhsMember> ihsMembers  = IhsMember.find.where().ieq("name", memberView.name.trim()).findList();
		
		if(ihsMembers.size() > 0){
			
			return ok(memberView.name.trim() + " member exists");
			
		}else{
			IhsAddress ihsAddress = new IhsAddress(
				memberView.address1, memberView.address2, memberView.city,
				memberView.stateOrProvence, memberView.postalCode,"", memberView.country, new DateTime());
		
			ihsAddress.save();
			SmemberStatus smemberStatus = SmemberStatus.find.where().eq("name", "Active").findUnique();
			
			IhsMember ihsMember = new IhsMember(memberView.name.trim(), memberView.description, smemberStatus,  memberView.name.replace(" ", ""),ihsAddress, ihsMemberGroups );
			
			ihsMember.save();
			
			IhsLocation ihsLocation = new IhsLocation("default","default",ihsMember, ihsAddress);
			
			ihsLocation.save();
		}	
		
		
 		
	} catch (Exception e) {
		Logger.error("ingestion_exception_resolve", e);
		return internalServerError("ingestion_exception_resolve");
	}
		return ok(memberView.name.trim() + " member added");
	}
	
	
	public static Result searchMember(String name){
		
		List <MemberView>  memberViews = new ArrayList<MemberView>();
		
		
		List<IhsMember> ihsMembers = null;
		
		if("nbsp".equals(name)){
			ihsMembers = IhsMember.find.fetch("ihsMemberGroup").where().orderBy("name").findList();
		}else{
			ihsMembers = IhsMember.find.fetch("ihsMemberGroup").where().ilike("name", "%"+name+"%").orderBy("name").findList();
		}

		try{
		for(IhsMember ihsMember : ihsMembers){
			
			System.out.println("---------------" + ihsMember.ihsAddress.country);
			
			String groupName = ihsMember.ihsMemberGroup.name;
			memberViews.add(new MemberView(ihsMember.memberID,
									ihsMember.name, 
									ihsMember.description,
									ihsMember.ihsAddress.address1 == null ? "":ihsMember.ihsAddress.address1,
									ihsMember.ihsAddress.address2 == null? "":ihsMember.ihsAddress.address2,
									ihsMember.ihsAddress.city == null ? "": ihsMember.ihsAddress.city,
									ihsMember.ihsAddress.stateOrProvence == null ? "" : ihsMember.ihsAddress.stateOrProvence,
									ihsMember.ihsAddress.postalCode == null ? "" : ihsMember.ihsAddress.postalCode,
									ihsMember.ihsAddress.country == null ? "" : ihsMember.ihsAddress.country,
									groupName
									));
		}
		}
		catch(Exception e){
			
		}
		
		return ok(toJson(memberViews));
	}
	

	@BodyParser.Of(BodyParser.Json.class)
	public static Result editAMember(){
	
	try{
		String jsonString = Controller.request().body().asJson().toString();

		ObjectMapper mapper = new ObjectMapper();
		
		MemberView memberView  = mapper.readValue(jsonString, MemberView.class);

		String user = session().get(Login.User);
		
		IhsMember ihsMember = IhsMember.find.byId(memberView.id);
		
		ihsMember.setName(memberView.name);
		
		ihsMember.setDescription(memberView.description);
		
 
		ihsMember.ihsAddress.address1=memberView.address1;
		ihsMember.ihsAddress.address2=memberView.address2;
		ihsMember.ihsAddress.city=memberView.city;
		ihsMember.ihsAddress.stateOrProvence = memberView.stateOrProvence;
		ihsMember.ihsAddress.postalCode = memberView.postalCode;
		ihsMember.ihsAddress.country = memberView.country;
	
		ihsMember.save();
		
		
		
	} catch (Exception e) {
		Logger.error("ingestion_exception_resolve", e);
		return internalServerError("ingestion_exception_resolve");
	}
		return ok();
	}
	
	public static Result getAllMember(){
		
		List <MemberView> memberViews = new ArrayList<MemberView>();
		
		List<IhsMember> ihsMembers = IhsMember.find.where().orderBy("name").findList();
		
		for(IhsMember ihsMember : ihsMembers){
			memberViews.add(new MemberView(ihsMember.memberID, ihsMember.name));
		}
		
		return ok(toJson(memberViews));
	}
	/***************************** User **********************************/
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result addAUser(){
		
		UserView userView = null;
		
		try{
			String jsonString = Controller.request().body().asJson().toString();

			ObjectMapper mapper = new ObjectMapper();
			
			userView  = mapper.readValue(jsonString, UserView.class);

			IhsMember ihsMember = IhsMember.find.byId(userView.memberId);
			
			SuserStatus suserStatus = SuserStatus.find.where().eq("name", "Active").findUnique();
		
			
			List<IhsUser> ihsUsers = IhsUser.find.where().ieq("userName", userView.userName.trim()).findList();
			
			if(ihsUsers.size() > 0){
				return ok(userView.userName.trim() + " User Exits");
			}
			
			IhsUser ihsUser = new IhsUser(userView.firstName, userView.lastName, ihsMember,
					suserStatus, userView.userName.trim(), userView.password);
			
			
			IhsSecurityRole ihsSecurityRoleUser = 	IhsSecurityRole.find.where().eq("name", "user").findUnique(); 
			IhsSecurityRole ihsSecurityRoleAdmin = 	IhsSecurityRole.find.where().eq("name", "admin").findUnique(); 

			ihsUser.ihsSecurityRoles.add(ihsSecurityRoleUser);
			
			if("admin".equals(userView.role)){
				ihsUser.ihsSecurityRoles.add(ihsSecurityRoleAdmin);
			}
			
			ihsUser.save();
			
			
			
		} catch (Exception e) {
			Logger.error("ingestion_exception_resolve", e);
			return internalServerError("ingestion_exception_resolve");
		}
		return ok(userView.userName.trim() + " User Added");
	}

	public static Result searchUser(String name){
		
		
		List <UserView> userViews = new ArrayList<UserView>();
		
		List<IhsUser> ihsUsers = null;
		
		
		if("nbsp".equals(name)){
		
			 ihsUsers = IhsUser.find.fetch("ihsMember").where().orderBy("userName").findList();
		}
		else{
			 ihsUsers = IhsUser.find.fetch("ihsMember").where().ilike("userName", "%"+name+"%").orderBy("userName").findList();
		}
		
		for(IhsUser ihsUser : ihsUsers){
			UserView userView = new UserView();
			userView.memberId = ihsUser.ihsMember.memberID;
			userView.userId = ihsUser.userID;
			userView.firstName = ihsUser.firstName;
			userView.lastName = ihsUser.lastName;
			userView.userName = ihsUser.userName;
			userView.role = ihsUser.ihsSecurityRoles.size()>1? "admin": "user";
			userView.groupName=ihsUser.ihsMember.name;
			userViews.add(userView);
		}
		
		return ok(toJson(userViews));
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result saveAUser(){
		
		UserView userView = null;
		
		try{
			String jsonString = Controller.request().body().asJson().toString();

			ObjectMapper mapper = new ObjectMapper();
			
			userView  = mapper.readValue(jsonString, UserView.class);
		
			IhsUser ihsUser = IhsUser.find.byId(userView.userId);
			
			IhsSecurityRole ihsSecurityRoleUser = 	IhsSecurityRole.find.where().eq("name", "user").findUnique(); 
			IhsSecurityRole ihsSecurityRoleAdmin = 	IhsSecurityRole.find.where().eq("name", "admin").findUnique(); 

			List<IhsSecurityRole>ihsSecurityRoles = new ArrayList <IhsSecurityRole>();
			
			ihsSecurityRoles.add(ihsSecurityRoleUser);
			
			if("admin".equals(userView.role)){
				ihsSecurityRoles.add(ihsSecurityRoleAdmin);
			}
			
			ihsUser.firstName = userView.firstName;
			ihsUser.lastName = userView.lastName;
			
			if(! userView.password.equals("") ){
				ihsUser.password = Helper.getMD5hash(userView.password);
			}
			
			ihsUser.setIhsSecurityRoles(ihsSecurityRoles); 
			
			
			ihsUser.save();
			
		} catch (Exception e) {
			Logger.error("ingestion_exception_resolve", e);
			return internalServerError("ingestion_exception_resolve");
		}
		return ok(userView.userName.trim() + " User Saved");
	}
	
	public static Result getCountryState(){
		
		List<CountryView> countryview = new ArrayList<CountryView>(); 
		
		List<Scountry> scounties = Scountry.find.fetch("stateProvinces").where().findList();
		
		for(Scountry scountry :scounties){
			
			CountryView sountryView = new CountryView();
			sountryView.id = scountry.countryId;
			sountryView.name = scountry.name;
			
			for(SstateProvince sstateProvince : scountry.stateProvinces){
				sountryView.stateProvinces.add(sstateProvince.name);
			}
			
			countryview.add(sountryView);
		}
		
		return ok(toJson(countryview));
	}
}