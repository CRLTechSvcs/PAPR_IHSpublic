package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by reza on 9/24/2014.
 */
@Entity
@Table(name="ihscategoryconflictresolution")
public class IhsCategoryConflictResolution extends Model{

    @Id
    @Column(name="categoryConflictResolutionID")
    public int categoryConflictResolutionID;
    public int titleCategoryID;
    public int prevailingSourceTitleCategoryID;
    public int overriddenSourceTitleCategoryID;

    public static Finder<Integer,IhsCategoryConflictResolution > find = new Finder<Integer, IhsCategoryConflictResolution>(Integer.class, IhsCategoryConflictResolution.class);


}
