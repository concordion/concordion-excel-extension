package org.concordion.ext.excel.conversion.sheet;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.concordion.ext.excel.conversion.ConversionStrategy;

public interface SheetConversionStrategy  extends ConversionStrategy<XSSFSheet>{

	/**
	 * Each sheet is rendered as a DIV in the HTML output. This allows you to specify the class of the div.
	 * @return css class you want to have on each sheet.
	 */
	public String getSheetCSSClass();

}
