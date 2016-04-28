package json;

import java.util.ArrayList;
import java.util.List;

public class DeaccessionNewView {
	public String jobName="";
	public String standard="Ithica";
	public int committed=0;
	public String groupFlag="group";
	public String fileContend="";
	public String fileType= "issn";
	public int minDeaccess=0;
	
	public DeaccessionIthacaView deaccessionIthacaView = new DeaccessionIthacaView(); 
	public DeaccessionCrlView deaccessionCrlView = new DeaccessionCrlView();
	public List<CommitmentView> commitmentView = new ArrayList<CommitmentView>();
	
	public DeaccessionNewView(){
		
	}
}