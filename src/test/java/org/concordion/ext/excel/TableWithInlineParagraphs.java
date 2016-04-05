package org.concordion.ext.excel;

import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(ExcelExtension.class)
public class TableWithInlineParagraphs extends AbstractSpecTest {

	@Override
	public String getBody() {
		return "<table><tr><th>A</th><th>B</th></tr><tr><td>1</td><td>2</td></tr>"+
				"<tr><td>3</td><td>4</td></tr></table>"+
				"<p><span>Some text alongside, will appear below when rendered.</span></p>";
	}

}
