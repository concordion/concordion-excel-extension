package org.concordion.ext.excel;

import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(ExcelExtension.class)
public class TableCommand extends AbstractSpecTest {

	@Override
	public String getBody() {
		return "<table concordion:execute=\"atTableLevel()\"><tr>"+
				"<th>A</th><th>B</th></tr>"+
				"<tr><td>Someval</td><td>Some other</td></tr></table>";
	}
	
	int callCount = 0;
	
	public void atTableLevel() {	
		callCount++;
		if (callCount > 1) {
			throw new IllegalStateException("Not expecting table command to be called more than once");
		}
	}
	
}
