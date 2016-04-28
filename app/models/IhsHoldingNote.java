package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by reza on 9/24/2014.
 */
@Entity
@Table(name="ihsholdingnote")
public class IhsHoldingNote extends Model {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="holdingNoteID")
    public int holdingNoteID;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holdingID")
	public IhsHolding ihsholding;
    
    public String note;

    public IhsHoldingNote (IhsHolding ihsholding, String note){
    	this.ihsholding = ihsholding;
    	this.note = note;
    }
    
    public static Finder<Integer, IhsHoldingNote> find = new Finder<Integer,IhsHoldingNote >(Integer.class, IhsHoldingNote.class);   
}