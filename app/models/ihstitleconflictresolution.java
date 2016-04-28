package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * Created by reza on 9/24/2014.
 */
@Entity
public class ihstitleconflictresolution extends Model{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="titleConflictResolutionID")
    int titleConflictResolutionID;
    int titleID;
    int titleFieldID;
    int prevailingSourceTitleID;
    int overriddenSourceTitleID;

    public int getTitleConflictResolutionID() {
        return titleConflictResolutionID;
    }

    public void setTitleConflictResolutionID(int titleConflictResolutionID) {
        this.titleConflictResolutionID = titleConflictResolutionID;
    }

    public int getTitleID() {
        return titleID;
    }

    public void setTitleID(int titleID) {
        this.titleID = titleID;
    }

    public int getTitleFieldID() {
        return titleFieldID;
    }

    public void setTitleFieldID(int titleFieldID) {
        this.titleFieldID = titleFieldID;
    }

    public int getPrevailingSourceTitleID() {
        return prevailingSourceTitleID;
    }

    public void setPrevailingSourceTitleID(int prevailingSourceTitleID) {
        this.prevailingSourceTitleID = prevailingSourceTitleID;
    }

    public int getOverriddenSourceTitleID() {
        return overriddenSourceTitleID;
    }

    public void setOverriddenSourceTitleID(int overriddenSourceTitleID) {
        this.overriddenSourceTitleID = overriddenSourceTitleID;
    }
}
