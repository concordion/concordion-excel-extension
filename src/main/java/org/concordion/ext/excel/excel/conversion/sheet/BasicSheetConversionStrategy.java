package org.concordion.ext.excel.conversion.sheet;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.concordion.ext.excel.conversion.AbstractConversionStrategy;
import org.concordion.ext.excel.conversion.ConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;

public class BasicSheetConversionStrategy extends AbstractConversionStrategy<XSSFSheet> implements SheetConversionStrategy {

	ConversionStrategy<Row> rowStrategy;
	
	public BasicSheetConversionStrategy(ConversionStrategy<Row> rowStrategy) {
		super();
		this.rowStrategy = rowStrategy;
	}

	@Override
	public String getSheetCSSClass() {
		return "example";
	}	

	public void process(XSSFSheet xssfSheet, HTMLBuilder result) {
		result.startTag("div");
		result.addAttribute("class", getSheetCSSClass());
		result.startTag("h2");
		result.addText(xssfSheet.getSheetName());
		result.endTag();
		
		rowStrategy.start(result);
		for (int i = xssfSheet.getFirstRowNum(); i <= xssfSheet.getLastRowNum(); i++) {
			Row row = xssfSheet.getRow(i);
			rowStrategy.process(row, result);
		}
		rowStrategy.finish(result);
		
		result.endTag();
	}
	
}
