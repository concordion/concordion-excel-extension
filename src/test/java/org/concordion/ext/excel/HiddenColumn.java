package org.concordion.ext.excel;

import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(ExcelExtension.class)
public class HiddenColumn extends AbstractSpecTest {

	@Override
	public String getBody() {
		return  // first row, has hidden middle column
				"<p><span>Column A</span>"+" <span style=\"display: none; \">Hidden Column B</span> "+
				"<span>Column C</span></p>"+
				
				// the hidden row
				"<p><span style=\"display: none; \">Some Hidden Row</span></p>"+
				
				// table header
				"<table concordion:execute=\"#result = calculateResult(#a, #b)\"><tr>"+
				"<th concordion:set=\"#a\" style=\"background-color: #4F81BD; font-weight: bold; \">Value 1</th>"+
				"<th concordion:set=\"#b\" style=\"background-color: #4F81BD; font-weight: bold; display: none; \">Intermediate</th>"+
				"<th concordion:assertEquals=\"#result\" style=\"background-color: #4F81BD; font-weight: bold; \">Result</th></tr>"+
				
				// first, visible row
				"<tr><td>7</td><td style=\"display: none; \">14</td><td>20</td></tr>"+
				
				// second, invisible row
				"<tr><td style=\"display: none; \">12</td><td style=\"display: none; \">24</td><td style=\"display: none; \">30</td></tr>"+
				
				// third, visible row
				"<tr><td>44</td><td style=\"display: none; \">88</td><td>94</td></tr></table>";
	}
	
	
	public int calculateResult(int a, int b) {
		return a*2+6;
	}
	
}
