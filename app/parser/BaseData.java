package parser;

import parser.Portico.Portico;
import parser.papr.PAPR;

public class BaseData {


	public String recordId = null;
	public String title = null;
	public String printISSN = null;
	public String eISSN = null;
	public String oclcNumber = null;
	public String lccn = null;
	public String publisher = null;
	public String status = null;
	public String years = null;
	public String holding = null;
	public int startYear = 0;
	public int endYear = 0;

	public BaseData(){
		
	}
	public BaseData(String title, String printISSN, String eISSN,
			String oclcNumber, String lccn, String publisher, String years,
			String holding) {
		this.title = title;
		this.printISSN = printISSN;
		this.eISSN = eISSN;
		this.oclcNumber = oclcNumber;
		this.lccn = lccn;
		this.publisher = publisher;
		this.status = publisher;
		this.years = years;
		this.holding = holding;
	}
	
	public static BaseData buildPorticoData(String rawdata) throws Exception {
		BaseData porticoData = new BaseData();
		String[] records = rawdata.split(Portico.cvsSplitBy);
		int index = 0;
		for (String col : records) {
			switch (index) {
			case 0:
				porticoData.publisher = col;
				break;
			case 1:
				porticoData.title = col;
				break;
			case 3:
				porticoData.printISSN = col.replaceAll("-", "");
				break;
			case 4:
				porticoData.eISSN = col.replaceAll("-", "");
				break;
			case 6:
				porticoData.status = col;
				break;
			case 7:
				porticoData.holding = col;
				break;
			case 8:
				porticoData.years = col;
				break;
			}
			index++;
		}

		if (porticoData.printISSN == null || porticoData.eISSN == null || porticoData.holding == null
				|| porticoData.title == null || porticoData.years == null || porticoData.years == null) {
			throw new Exception();
		}

		return porticoData;
	}

	public static BaseData buildPAPRData(String rawdata) throws Exception {
		BaseData paprData = new BaseData();
		String[] records = rawdata.split(PAPR.cvsSplitBy);
		int index = 0;
		for (String col : records) {
			switch (index) {
			case 0:
				paprData.title = col;
				break;
			case 1:
				paprData.printISSN = col.replaceAll("-", "");;
				break;
			case 2:
				paprData.publisher = col;
				break;
			case 3:
				paprData.oclcNumber = col;
				break;
			case 8:
				paprData.holding = col;
				break;
			}
			index++;
		}

		if (paprData.title == null ||  paprData.printISSN == null || paprData.oclcNumber == null || paprData.holding == null ) {
			throw new Exception();
		}

		return paprData;
	}
}