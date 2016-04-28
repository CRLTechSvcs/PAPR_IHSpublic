package extra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CsvfileHolding {
	public static void main(String[] args) throws IOException {
		
		 StringTokenizer st ;
		
		 File file = new File("C:\\travant\\data\\ihs\\CIC_data_for_IHS_holding_flatten.txt");
		 if (!file.exists()) {
				file.createNewFile();
		 }
		 
		 FileWriter fw = new FileWriter(file.getAbsoluteFile());
		 BufferedWriter bw = new BufferedWriter(fw);
			
		 BufferedReader TSVFile = 
			    new BufferedReader(new FileReader("C:\\travant\\data\\ihs\\CIC_data_for_IHS_holding.txt"));
		
		
		
		
		 List<String>dataArray = new ArrayList<String>() ;
	        String dataRow = TSVFile.readLine(); // Read first line.
		
	        while (dataRow != null){
	          
	            int count = 0;
	            String oclc = "";
	            for (String retval: dataRow.split("\\t")){
	            	
	            	
	            	
	            	StringBuffer newLine = new StringBuffer();
	            	
	                if(count == 0){
	                	oclc =  retval;
	                	count = 1699;
	                }else{
	                	if("1".equals(retval))
	                		newLine.append(oclc).append("\t").append(count).append("\t").append(retval).append(System.getProperty("line.separator"));
	                }
	                
	                //System.out.print(newLine.toString());
	                bw.write(newLine.toString());
	                count++;
	            	
	                
	            }    	
	            dataRow = TSVFile.readLine(); // Read next line of data.
	        }
		TSVFile.close();
		
		bw.close();
	}
}