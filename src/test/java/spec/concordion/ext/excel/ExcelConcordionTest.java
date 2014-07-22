package spec.concordion.ext.excel;

import java.util.HashMap;
import java.util.Map;

import org.concordion.api.extension.Extensions;
import org.concordion.ext.excel.ExcelExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(ExcelExtension.class)
public class ExcelConcordionTest {

	public Map<String, String> split(String name) {
		Map<String, String> out = new HashMap<String, String>();
		String firstName = name.substring(0, name.indexOf(" "));
		String lastName = name.substring(name.indexOf(" ")+1);
		out.put("firstName", firstName);
		out.put("lastName", lastName);
		return out;
	}
}
