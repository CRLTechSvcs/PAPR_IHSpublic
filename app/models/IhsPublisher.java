package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;



import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import json.PublisherView;

/**
 * Created by reza on 9/11/2014.
 */
@Entity
@Table(name = "ihspublisher")
public class IhsPublisher extends Model {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="publisherID")
	public int publisherID;

	public String name;
	public String description;
	@Column(name="startDate")
	public Date startDate;
	
	@Column(name="endDate")
	public Date endDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "addressId")
	public IhsAddress ihsAddress;

	public IhsPublisher(String name, String description, Date startDate,
			Date endDate, IhsAddress ihsAddress) {

		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.ihsAddress = ihsAddress;
	}

	public static Finder<Integer, IhsPublisher> find = new Finder<Integer, IhsPublisher>(
			Integer.class, IhsPublisher.class);
	
	public static ArrayList<PublisherView>  getAllPublisherOrder(){
		
		ArrayList<PublisherView> publisherViews= new ArrayList<PublisherView>();
		
		List<IhsPublisher> ihsPublishers =IhsPublisher.find.where().orderBy("name asc").findList();
		
		for(IhsPublisher ihsPublisher: ihsPublishers){
			
			String tmpPublisherName = ihsPublisher.name.length() > 26 ? ihsPublisher.name.substring(0,25): ihsPublisher.name;

			publisherViews.add(new PublisherView(ihsPublisher.publisherID, tmpPublisherName ));
		}
		
		return publisherViews;
	}
}