package org.concordion.ext.excel.conversion.workbook;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.concordion.ext.excel.ExcelConversionException;

public class WorkbookHelper {
	
	private InputStream is;
	private String title;
	
	public String getTitle() {
		return title;
	}

	public WorkbookHelper(InputStream is, String title) {
		super();
		this.is = is;
		this.title = title;
	}
	
	public XSSFWorkbook getWorkbook() {
		try {
			return new XSSFWorkbook(is);
		} catch (IOException e) {
			throw new ExcelConversionException(e);
		}
	}
	
	
}