package extra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import models.IhsIngestionJob;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaTimeExample {

    public static void main(String[] sm) throws IOException {
        DateTimeFormatter dateFormat = DateTimeFormat
                .forPattern("G,C,Y,x,w,e,E,Y,D,M,d,a,K,h,H,k,m,s,S,z,Z");
        
        DateTimeFormatter dateFormat1 = DateTimeFormat
                .forPattern("Y-M-d");

        String dob = "2002-01-15";
        LocalTime localTime = new LocalTime();
        LocalDate localDate = new LocalDate();
        DateTime dateTime = new DateTime();
        LocalDateTime localDateTime = new LocalDateTime();
        DateTimeZone dateTimeZone = DateTimeZone.getDefault();

        System.out
                .println("dateFormatr : " + dateFormat.print(localDateTime));
        

        System.out
                .println("dateFormatr : " + dateFormat1.print(localDateTime));
        
        System.out.println("LocalTime : " + localTime.toString());
        System.out.println("localDate : " + localDate.toString());
        System.out.println("dateTime : " + dateTime.toString());
        System.out.println("localDateTime : " + localDateTime.toString());
        System.out.println("DateTimeZone : " + dateTimeZone.toString());
        System.out.println("Year Difference : "
                + Years.yearsBetween(DateTime.parse(dob), dateTime).getYears());
        System.out.println("Month Difference : "
                + Months.monthsBetween(DateTime.parse(dob), dateTime)
                        .getMonths());
        
        //File file = new File("httpdata\\330-main.scala");
        //FileInputStream fis = new FileInputStream("httpdata\\330-main.scala");
        FileInputStream file = new FileInputStream("C:/install.log");
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        
		 
		
    }
}