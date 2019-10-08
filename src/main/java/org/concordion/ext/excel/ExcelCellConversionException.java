package org.concordion.ext.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellReference;

import java.text.MessageFormat;

public class ExcelCellConversionException extends ExcelConversionException {

	private static final long serialVersionUID = -8957983421526078295L;

	public ExcelCellConversionException(String arg0, Cell c) {
		super(message(arg0, c));
	}

	public ExcelCellConversionException(String arg0, Cell c, Throwable t) {
		super(message(arg0, c), t);
	}

	public ExcelCellConversionException(String arg0, CellReference c) {
	    super(message(arg0, c));
	}

	public ExcelCellConversionException(String arg0, CellReference c, Throwable t) {
		super(message(arg0, c), t);
	}

	private static String message(String msg, Cell cell) {
	    String sheetDesc= (cell!=null && cell.getSheet()!=null) ? cell.getSheet().getSheetName() : "(null)";
	    String cellDesc= (cell!=null && cell.getAddress()!=null) ? cell.getAddress().toString() : "(null)";
		return MessageFormat.format("{0} at {1}!{2}", msg, sheetDesc, cellDesc);
	}

	private static String message(String msg, CellReference cellReference) {
	    String locationDesc= cellReference!=null ? cellReference.formatAsString() : "(null)";
        return MessageFormat.format("{0} at {1}", msg, locationDesc);
	}


}
