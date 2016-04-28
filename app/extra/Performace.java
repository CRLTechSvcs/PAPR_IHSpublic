package extra;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;

public class Performace {

	static void splitStrinAl(){
		final String numberList = "\"American Anthropological Association (through 2007)\",\"Bulletin of the National Association of Student Anthropologists\",\"\",\"1556-3618\",\"1556-3626\",\"Yes\",\"preserved\",\"v.10(1,2-3),v.7(1-4),v.6(2-4),v.5(4),v.9(1,4,2003),v.8(1-4),v.11(1-2)\",\"1990-1999\",\"ISSN_15563618\"";;
		long start = System.currentTimeMillis();  
		for(int i=0; i<1000000; i++) {
		  final String[] numbers = StringUtils.split(numberList, "\",\"");
		    for(String number : numbers) {
		      number.length();
		    }
		  }
		System.out.println(System.currentTimeMillis() - start);
		   
		Splitter splitter = Splitter.on("\",\"");
		start = System.currentTimeMillis();
		for(int i=0; i<1000000; i++) {
		  Iterable<String> numbers = splitter.split(numberList);
		    for(String number : numbers) {
		      number.length();
		    }
		  }
		
		System.out.println(System.currentTimeMillis() - start);
	}
	static void spiltString() {
		final String validRecod = "\"American Anthropological Association (through 2007)\",\"Bulletin of the National Association of Student Anthropologists\",\"\",\"1556-3618\",\"1556-3626\",\"Yes\",\"preserved\",\"v.10(1,2-3),v.7(1-4),v.6(2-4),v.5(4),v.9(1,4,2003),v.8(1-4),v.11(1-2)\",\"1990-1999\",\"ISSN_15563618\"";

		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
		String[] records = validRecod.split( "\",\"");
		}
		System.out.println(System.currentTimeMillis() - start);
		//With Apache Common
	   start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			StringUtils.split(validRecod, "\",\"");
		}
		System.out.println(System.currentTimeMillis() - start);

		// Google guava
		start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			Iterable<String> tokens1 = Splitter.on("\",\"").split(validRecod);
		}
		System.out.println(System.currentTimeMillis() - start);

		// With Google guava splitter object
		Splitter niceCommaSplitter = Splitter.on("\",\"").trimResults();

		start = System.currentTimeMillis();

		for (int i = 0; i < 1000000; i++) {
			Iterable<String> tokens2 = niceCommaSplitter.split(validRecod);
		}
		System.out.println(System.currentTimeMillis() - start);
	}

	public static void serialize(){
		byte name[] = SerializationUtils.serialize("Test");
		String deserilazed = (String) SerializationUtils.deserialize(name);
		System.out.println(deserilazed);
	}
	
	public static void main(String[] args) {
		spiltString();
		//serialize();
		//splitStrinAl();
		
	}
}
