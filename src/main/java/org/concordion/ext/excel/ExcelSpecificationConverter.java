package org.concordion.ext.excel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.concordion.api.SpecificationConverter;
import org.concordion.ext.excel.conversion.HTMLBuilder;
import org.concordion.ext.excel.conversion.HTMLBuilderImpl;
import org.concordion.ext.excel.conversion.workbook.WorkbookConversionStrategy;
import org.concordion.ext.excel.conversion.workbook.WorkbookHelper;

/**
 * Handles loading of Excel Spreadsheet from a given Resource and managing the conversion into 
 * an XHTML format that Concordion can handle.
 * 
 * @author robmoffat
 *
 */
public class ExcelSpecificationConverter implements SpecificationConverter {
	
	WorkbookConversionStrategy workbookStrategy;
	
	public ExcelSpecificationConverter(WorkbookConversionStrategy strategy) {
		this.workbookStrategy = strategy;
	}
	
	@Override
	public InputStream convert(InputStream inputStream, String testName) throws IOException {
		HTMLBuilder result = new HTMLBuilderImpl();
		workbookStrategy.process(new WorkbookHelper(inputStream, testName), result);
		inputStream.close();
		return createInputStreamFromPage(result, testName);
	}

	public static final String XML_PROLOG = "<?xml version=\"1.0\" encoding=\"UTF8\"?>";
	
    protected InputStream createInputStreamFromPage(HTMLBuilder result, String testName) throws UnsupportedEncodingException {
		String resultString = XML_PROLOG + result.toString();
		
		ExcelExtension.setConversion(testName, resultString);
		
		return new ByteArrayInputStream(resultString.getBytes("UTF-8"));
	}

}
