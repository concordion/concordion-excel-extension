package org.concordion.ext.excel;

import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(ExcelExtension.class)
public class ParagraphLayout extends AbstractSpecTest {

	@Override
	public String getBody() {
		return 	"<p><span>A simple HTML Paragraph</span></p><p><span style=\"background-color: #4BACC6; \">"+
				"Background colours are respected</span></p><p><span style=\"background-color: #1F497D; \">"+
				"Cells on the same row </span> <span style=\"background-color: #9BBB59; \">are placed on a single line.</span></p>";

	}
	
	
}
