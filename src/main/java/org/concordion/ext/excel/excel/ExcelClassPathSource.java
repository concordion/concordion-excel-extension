package org.concordion.ext.excel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
        
        if (resource.getName().endsWith(ExcelLocator.EXCEL_EXTENSION)) {
        	 HTMLBuilder result = new HTMLBuilderImpl();
             workbookStrategy.process(new WorkbookHelper(inputStream, getTestTitle(resource)), result);
          	 inputStream.close();

             System.out.println("Created: "+result);
             return createInputStreamFromPage(result);
        } else {
        	return inputStream;
        }
    }

    protected InputStream createInputStreamFromPage(HTMLBuilder result) {
		String resultString = result.toString();
		return new ByteArrayInputStream(resultString.getBytes());
	}

	protected String getTestTitle(Resource resource) {
		return resource.getName().substring(0, resource.getName().length() - ExcelLocator.EXCEL_EXTENSION.length());
	}

	@Override
    public boolean canFind(Resource resource) {
        return decorated.canFind(resource);
    }
}
