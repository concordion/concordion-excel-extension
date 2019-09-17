package org.concordion.ext.excel;

import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(ExcelExtension.class)
public class CellConversions extends AbstractSpecTest {

	public static final String THAI_BAHT = "&#3647;";
	public static final String POUND = "&#163;";

	@Override
	public String getBody() {
		return "<p><span>Number Examples</span> <span>1.50</span> <span>3</span> <span>6,100.105</span> <span>1.7</span></p>"
				+ "<p><span>Date Examples</span> <span>03 July 2014</span> <span>15/01/2021</span> <span>3-Mar</span> <span>9-Apr-13</span> <span>1/3/1806</span></p>"
				+ "<p><span>Function</span> <span>6</span> <span>1/19/21</span> <span>AB</span></p>"
				+ "<p><span>Boolean</span> <span>true</span> <span>false</span></p>"
				+ "<p><span>Currency</span> <span>$6.78</span> <span>" + POUND + "50.00</span> <span>" + THAI_BAHT + "1,234.56</span></p>"
				+ "<p><span>Text with XML entities</span> <span>&lt; &amp; &gt;</span></p>";
	}
}