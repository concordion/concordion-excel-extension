package org.concordion.ext.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellReference;

public class ExcelCellConversionException extends ExcelConversionException {

	private static final long serialVersionUID = -8957983421526078295L;

	public ExcelCellConversionException(String arg0, Cell c) {
		super(arg0+" at cell "+c.getAddress());
	}
	
	public ExcelCellConversionException(String arg0, Cell c, Throwable t) {
		super(arg0+" at cell "+c.getAddress(), t);
	}

	public ExcelCellConversionException(String arg0, CellReference c) {
		super(arg0+" at cell "+c.formatAsString());
	}

	public ExcelCellConversionException(String arg0, CellReference c, Throwable t) {
		super(arg0+" at cell "+c.formatAsString(), t);
	}

}
