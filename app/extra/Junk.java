package extra;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Junk {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*
		String st1 = ")v.10:no.7-v.14:no.91(";
		String st2 = "v.10:no.7-v.14";
		String st3 = "v.10-v.14:no.9";
		String st4 = "v.10-v.14";
		
		String st5 = "v.10";
		*/

		String st1 = "(Jan 2000-Feb 2012)";
		String st2 = "(Jan 2000-2012)";
		String st3 = "(2000-Feb 2012)";
		String stx = "(Feb 2012)";
		String st4 = "(2000-2012)";
		String st5 = "(2000)";
		
		String st10 = "no.5-no.6";
		String st11 = "no.5";
		
		String patternStringIssueToIssue = "(no.\\d+-no.\\d+)";
		String patternStringIssue = "(no.\\d+)";
		
		String patternStringIssueToIssue_ = "(no.)(\\d+)(-no.)(\\d+)";
		String patternStringIssue_ = "(no.)(\\d+)";
		 
		 //(Jan 2000-Feb 2012)
		 String patternStringMonYaerMonYear = "(\\(\\w+\\s+\\d+-\\w+\\s+\\d+\\))";
		 //(Jan 2000-2012)
		 String patternStringMonYaeYear = "(\\(\\w+\\s+\\d+-\\d+\\))";
		 //(2000-Feb 2012)
		 String patternStringYaerMonYear = "(\\(\\d+-\\w+\\s+\\d+\\))";
		 //(Feb 2012)
		 String patternStringMonYear = "(\\(\\w+\\s+\\d+)";
		 
		 //(2000-2012)
		 String patternStringYaerYear = "(\\(\\d+-\\d+\\))";
		 //2000
		 String patternStringYaer = "(\\(\\d+\\))";
		 
		
		 //(Jan 2000-Feb 2012)
		 String patternStringMonYaerMonYear_ = "(\\()(\\w+)(\\s+)(\\d+)(-)(\\w+)(\\s+)(\\d+)(\\))";
		 //(Jan 2000-2012)
		 String patternStringMonYaeYear_ = "(\\()(\\w+)(\\s+)(\\d+)(-)(\\d+)(\\))";
		 //(2000-Feb 2012)
		 String patternStringYaerMonYear_ ="(\\()(\\d+)(-)(\\w+)(\\s+)(\\d+)(\\))";
		 //(2000-2012)
		 String patternStringYaerYear_ = "(\\()(\\d+)(-)(\\d+)(\\))";
		 //2000
		 String patternStringYaer_ = "(\\()(\\d+)(\\))";
		 
		 String patternStringMonYear_ = "(\\()(\\w+)(\\s+\\d+)";
		 
	
       Pattern pattern = Pattern.compile(patternStringIssueToIssue);
       Matcher matcher = pattern.matcher(st10);

       if(matcher.find()) {
    	   
           System.out.println("found: " + matcher.group(1) ); 
           
           pattern = Pattern.compile(patternStringIssueToIssue_);
           Matcher matcher1 = pattern.matcher( matcher.group(1));
           
           matcher1.find();
           //System.out.println("found: " + matcher1.group(2)  +  matcher1.group(4)  +  matcher1.group(6)   +  matcher1.group(8)   );
           //System.out.println("found: " + matcher1.group(2)  +  matcher1.group(4)  +  matcher1.group(6)     );
          // System.out.println("found: " + matcher1.group(2)  +  matcher1.group(4) );
           System.out.println("found: " + matcher1.group(2)+matcher1.group(4));
       }
       
       /*
       String patternString1 = "(v.\\d+:no.\\d+-v.\\d+:no.\\d+)";
       String patternString2 = "(v.\\d+:no.\\d+-v.\\d+)";
       String patternString3 = "(v.\\d+.+-v.\\d+:no.\\d+)";
       String patternString4 = "(v.\\d+-v.\\d+)";
       String patternString5 = "(v.\\d+)";
       String patternString6 = "(\\(\\d+-\\d+\\))";
       String patternString7 = "(\\(\\d+\\))";
       String patternString8 = "(v.\\d+\\(.+\\)-v.\\d+)";

   
       String patternString1_ = "(v.)(\\d+)(:no.)(\\d+)(-v.)(\\d+)(:no.)(\\d+)";
       String patternString2_ = "(v.)(\\d+)(:no.)(\\d+)(-v.)(\\d+)";
       String patternString3_ = "(v.)(\\d+)(.+)(-v.)(\\d+)(:no.)(\\d+)";
       String patternString4_ = "(v.)(\\d+)(-v.)(\\d+)";
       String patternString5_ = "(v.)(\\d+)";
       String patternString6_ = "(\\()(\\d+)(\\))";
      
       */
	}

}
