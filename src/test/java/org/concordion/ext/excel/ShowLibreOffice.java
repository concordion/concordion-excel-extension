package org.concordion.ext.excel;

import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

/**
 * 
 * 	LibreOffice handles hidden cells differentl than Ms Office.
 * 	( cf. xl/worksheets/sheet1.xml )
 * 
 *  MS Office : visible is represented as missing hidden attribute
 *  LibreOffice: visible is represented as hidden="false"
 *  
 *  This test includes a trivial file created with LibreOffice to demonstrate this capability.
 * 
 *  @note Test suite assumes Default Style 
 *  	Font: Calibri
 *  	Size: 12
 *  
 *  This is required in the xlsx specification.    
 *
 */
@RunWith(ConcordionRunner.class)
@Extensions(ExcelExtension.class)
public class ShowLibreOffice extends AbstractSpecTest {

	@Override
	public String getBody() {
		return "<p><span>text</span></p>";
	}

}