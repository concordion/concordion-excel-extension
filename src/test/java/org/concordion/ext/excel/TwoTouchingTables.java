package org.concordion.ext.excel;

import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(ExcelExtension.class)
public class TwoTouchingTables extends AbstractSpecTest {

	@Override
	public String getBody() {
		return "<table><tr><th>A</th><th>B</th></tr><tr><td>3</td><td>4</td></tr><tr><td>1</td><td>2</td></tr></table>"+
				"<table><tr><th>C</th><th>D</th><th>E</th></tr><tr><td>1</td><td>2</td><td>3</td></tr></table>";
	}

}
