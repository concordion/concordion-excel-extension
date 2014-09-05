package org.concordion.ext.excel;

import org.apache.poi.ss.usermodel.Cell;

public class ExcelCellConversionException extends ExcelConversionException {

	private static final long serialVersionUID = -8957983421526078295L;

	public ExcelCellConversionException(String arg0, Cell c) {
		super(arg0+" at "+c.getRowIndex()+"x"+c.getColumnIndex());
	}

}
