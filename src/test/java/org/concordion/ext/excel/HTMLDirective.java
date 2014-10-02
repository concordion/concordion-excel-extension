package org.concordion.ext.excel;

import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(ExcelExtension.class)
public class HTMLDirective extends AbstractSpecTest {

	@Override
	public String getBody() {
		return "<p><center html-tag=\"center\">This should be centered</center></p>";
	}

}
