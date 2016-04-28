package extra;


import java.io.IOException;

import json.IngestionDataParsingErrorJson;
import json.PageingJson;
import parser.BaseData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonTest {

	public static void main(String[] args) throws IOException {
		/*
		try {
			System.out.print("");
			BaseoData porticoData = new BaseoData("\"\",\"dfd\"");
			
			System.out.print("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		/* ObjectMapper mapper = new ObjectMapper();
		String st = mapper.writeValueAsString(porticoData);
		
		System.out.println(st);
		JsonNode jsonNode = mapper.readTree(st);
		
		System.out.println(jsonNode.findPath("title").asText());
		
		ObjectMapper mapper = new ObjectMapper();
		MyValue myResultObject = new MyValue("q","q","q","q","q","q","q","q");
		
		
		String jsonString = mapper.writeValueAsString(porticoData);
		System.out.println(jsonString);
		
		MyValue value = mapper.readValue("{\"name\":\"Bob\", \"age\":13}", MyValue.class);
		
		PorticoData value = mapper.readValue(jsonString, PorticoData.class);
		
		*/
		//String st = "{\"recordId\":2,\"printISSN\":\"1553618\",\"eISSN\":\"15563626\",\"title\":\"Bulletin of the National Association of Student Anthropologists-1\",\"holding\":\"v.10(1,2-3),v.7(1-4),v.6(2-4),v.5(4),v.9(1,4,2003),v.8(1-4),v.11(1-2)\",\"years\":\"19901999\"}";
		
		ObjectMapper mapper = new ObjectMapper();
		IngestionDataParsingErrorJson ingestionDataParsingErrorJson1 = 
					new IngestionDataParsingErrorJson ("12","","","","","");
		IngestionDataParsingErrorJson ingestionDataParsingErrorJson2 = 
				new IngestionDataParsingErrorJson ("","","","","","");
		
		PageingJson ingestionDataParsingErrorPageJson = new PageingJson();
		
		
		ingestionDataParsingErrorPageJson.items.add(ingestionDataParsingErrorJson1);
		ingestionDataParsingErrorPageJson.items.add(ingestionDataParsingErrorJson2);
		String st = mapper.writeValueAsString(ingestionDataParsingErrorPageJson);
		
		System.out.println(st);
		//BaseData value = mapper.readValue(st, BaseData.class);
		
		//System.out.println(value.eISSN);
	}

}
