package extra;

import com.google.common.collect.ImmutableMap;

public class GuavaExample {
	public static void main(String[] args) {
		
		// Google Guava
		 ImmutableMap<String,String> OP_MAP =
		        new ImmutableMap.Builder<String,String>()
		                .put("+", "Plus")
		                .put("-", "Minus")
		                .put("*", "Times")
		                .put("/", "Divided By")
		                .put("%", "Modulo")
		                .put("^", "To the power of")
		                .build();
		 
		 
	}
}
