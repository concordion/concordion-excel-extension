package org.concordion.ext.excel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.concordion.api.Resource;
import org.concordion.api.Source;
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
public class ExcelClassPathSource implements Source {
	
	WorkbookConversionStrategy workbookStrategy;
	Source decorated;
	
	public ExcelClassPathSource(Source decorated, WorkbookConversionStrategy strategy) {
		this.workbookStrategy = strategy;
		this.decorated = decorated;
	}

	@Override
    public InputStream createInputStream(Resource resource) throws IOException {
        InputStream inputStream = decorated.createInputStream(resource);
        
        if (resource.getName().endsWith(ExcelExtension.EXCEL_FILE_EXTENSION)) {
        	 HTMLBuilder result = new HTMLBuilderImpl();
             workbookStrategy.process(new WorkbookHelper(inputStream, getTestTitle(resource)), result);
          	 inputStream.close();

             return createInputStreamFromPage(result);
        } else {
        	return inputStream;
        }
    }

	private static final String XML_PROLOG = "<?xml version=\"1.0\" encoding=\"UTF8\"?>";
	
    protected InputStream createInputStreamFromPage(HTMLBuilder result) throws UnsupportedEncodingException {
		String resultString = XML_PROLOG + result.toString();
		
		ExcelExtension.setLastConversion(resultString);
		
		return new ByteArrayInputStream(resultString.getBytes("UTF-8"));
	}

	protected String getTestTitle(Resource resource) {
		return resource.getName().substring(0, resource.getName().length() - ExcelExtension.EXCEL_FILE_EXTENSION.length());
	}

	@Override
    public boolean canFind(Resource resource) {
        return decorated.canFind(resource);
    }
}
