package org.concordion.ext.excel.conversion.workbook;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.concordion.ext.excel.conversion.AbstractConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;
import org.concordion.ext.excel.conversion.sheet.SheetConversionStrategy;
import org.concordion.internal.ConcordionBuilder;

public class BasicWorkbookConversionStrategy extends AbstractConversionStrategy<WorkbookHelper> implements WorkbookConversionStrategy {
	
	SheetConversionStrategy sheetStrategy;
	
	public BasicWorkbookConversionStrategy(SheetConversionStrategy sheetStrategy) {
		super();
		this.sheetStrategy = sheetStrategy;
	} 

	public void process(WorkbookHelper wb, HTMLBuilder result) {
    	XSSFWorkbook workbook = wb.getWorkbook();
    	String title = wb.getTitle();
    	result.startTag("html");
    	addRootElementAttributes(result);
    	result.startTag("head");
    	result.startTag("title");
    	result.addText(title);
    	result.endTag();
    	result.endTag();
    	result.startTag("body");
    	result.startTag("h1");
    	result.addText(title);
    	result.endTag();
    	
    	for (XSSFSheet xssfSheet : workbook) {
			sheetStrategy.process(xssfSheet, result);
		}
    	
    	result.endTag();
    	result.endTag();
	}

	protected void addRootElementAttributes(HTMLBuilder result) {
		result.addAttribute("xmlns:concordion", ConcordionBuilder.NAMESPACE_CONCORDION_2007);
	}
}
