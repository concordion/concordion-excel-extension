package spec.concordion.ext.excel;

import java.util.HashMap;
import java.util.Map;

import org.concordion.api.extension.Extensions;
import org.concordion.ext.excel.ExcelExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(ExcelExtension.class)
public class ExcelConcordionTutorialTest {
	
	public String blah() {
		return "blah";
	}

	public Map<String, String> split(String name) {
		Map<String, String> out = new HashMap<String, String>();
		String firstName = name.substring(0, name.indexOf(" "));
		String lastName = name.substring(name.indexOf(" ")+1);
		out.put("firstName", firstName);
		out.put("lastName", lastName);
		return out;
	}
	
	public String surroundWithPTag(String text) {
		String tag = "p";
		return "<"+tag+">"+text+"</"+tag+">";
	}
	
	public double calculateResult(String a, String b, String c) {
		double aa = Double.parseDouble(a);
		double bb = Double.parseDouble(b);
		double cc = Double.parseDouble(c);
		return aa*bb+cc;
		
	}
}
