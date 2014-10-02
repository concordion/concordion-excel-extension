package org.concordion.ext.excel;

import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(ExcelExtension.class)
public class TableLayout extends AbstractSpecTest {

	@Override
	public String getBody() {
		return "<table><tr><th style=\"background-color: #4F81BD; font-weight: bold; \">First Name</th><th style=\"background-color: #4F81BD; font-weight: bold; \">Last Name</th><th style=\"background-color: #4F81BD; font-weight: bold; \">Age</th></tr><tr><td>Jennifer</td><td>Aniston</td><td>42</td></tr><tr><td>Jeff</td><td>Bridges</td><td>66</td></tr><tr><td>John</td><td>Wayne</td><td>78</td></tr></table>";
	}
}
