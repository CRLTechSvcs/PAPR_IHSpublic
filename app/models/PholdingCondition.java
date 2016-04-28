package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by reza on 9/24/2014.
 */
@Entity
@Table(name = "PholdingCondition")

public class PholdingCondition extends Model {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    @Column(name="holdingConditionID")
    int holdingConditionID;

    @OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "holdingID")
	public IhsHolding ihsHolding;
    
    @ManyToOne(fetch = FetchType.LAZY)
   	@JoinColumn(name = "conditionTypeID")
   	public SconditionType sconditionType;
    
    public PholdingCondition (IhsHolding ihsHolding, SconditionType sconditionType){
    	this.ihsHolding = ihsHolding;
    	this.sconditionType = sconditionType;
    }    
    
    public static Finder<Integer, PholdingCondition> find = new Finder<Integer, PholdingCondition>(
			Integer.class, PholdingCondition.class);
}