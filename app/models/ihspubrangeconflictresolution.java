package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

/**
 * Created by reza on 9/24/2014.
 */
@Entity
public class ihspubrangeconflictresolution extends Model {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="pubRangeConflictResolutionID")
    int pubRangeConflictResolutionID;
    int publicationRangeID;
    int prevailingSourcePubRangeID;
    int overriddenSourcePubRangeID;

    public int getPubRangeConflictResolutionID() {
        return pubRangeConflictResolutionID;
    }

    public void setPubRangeConflictResolutionID(int pubRangeConflictResolutionID) {
        this.pubRangeConflictResolutionID = pubRangeConflictResolutionID;
    }

    public int getPublicationRangeID() {
        return publicationRangeID;
    }

    public void setPublicationRangeID(int publicationRangeID) {
        this.publicationRangeID = publicationRangeID;
    }

    public int getPrevailingSourcePubRangeID() {
        return prevailingSourcePubRangeID;
    }

    public void setPrevailingSourcePubRangeID(int prevailingSourcePubRangeID) {
        this.prevailingSourcePubRangeID = prevailingSourcePubRangeID;
    }

    public int getOverriddenSourcePubRangeID() {
        return overriddenSourcePubRangeID;
    }

    public void setOverriddenSourcePubRangeID(int overriddenSourcePubRangeID) {
        this.overriddenSourcePubRangeID = overriddenSourcePubRangeID;
    }
}
