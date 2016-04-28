package extra;

public class MyValue {
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
	
	public MyValue(){
		
	}
	public MyValue(String title, String printISSN, String eISSN,
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
	
	public MyValue(String rawdata) throws Exception {
		String[] records = rawdata.split(",");
		int index = 0;
		for (String col : records) {
			switch (index) {
			case 1:
				title = col;
			case 3:
				printISSN = col.replaceAll("-", "");
			case 4:
				eISSN = col.replaceAll("-", "");
			case 6:
				status = col;
			case 7:
				holding = col;
			case 8:
				years = col;
			}
			index++;
		}

		if (printISSN == null || eISSN == null || holding == null
				|| years == null) {
			throw new Exception();
		}

	}
}
