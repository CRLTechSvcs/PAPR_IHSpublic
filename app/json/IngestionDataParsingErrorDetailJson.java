package json;

public class IngestionDataParsingErrorDetailJson {
	public String field;
	public String original;
	public String corrected;
	public String info;
	
	public IngestionDataParsingErrorDetailJson (String field, String original, String corrected, String info){
		this.field = field;
		this.original = original;
		this.corrected = corrected;
		this.info = info;
	}
}
