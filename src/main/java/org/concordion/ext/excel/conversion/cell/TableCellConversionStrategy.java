package org.concordion.ext.excel.conversion.cell;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.concordion.ext.excel.ExcelCellConversionException;
import org.concordion.ext.excel.conversion.ConversionHelpers;
import org.concordion.ext.excel.conversion.ConversionStrategy;


/**
 * Extends the Excel cell conversion to handle output into HTML Tables.  
 * 
 * Specifically, this means handling rowspan and colspan attributes.
 * 
 * @author robmoffat
 *
 */
public class TableCellConversionStrategy extends BasicCellConversionStrategy {

	public TableCellConversionStrategy(List<ConversionStrategy<Cell>> cellContents, String tag) {
		super(cellContents, tag, false);
	}

	enum CellType { TOP_LEFT_OF_RANGE, INDIVIDUAL, OTHER_CELL_IN_RANGE };

	protected CellType getCellType(Cell in) {
		CellRangeAddress cra  = ConversionHelpers.getRangeForCell(in);
				
		if (cra == null) {
			return CellType.INDIVIDUAL;
		} else if (isTopLeftOf(cra, in)) {
			return CellType.TOP_LEFT_OF_RANGE;
		} else if (ConversionHelpers.isPartOf(cra, in)) {
			return CellType.OTHER_CELL_IN_RANGE;
		} else {
			throw new ExcelCellConversionException("Couldn't determine range for cell", in);
		}
	}

	private boolean isTopLeftOf(CellRangeAddress cra, Cell in) {
		int inRow = in.getRowIndex();
		int inCol = in.getColumnIndex();
		return (cra.getFirstColumn() == inCol) 
			&& (cra.getFirstRow() == inRow);
	}

	@Override
	protected boolean hasContent(Cell in) {
		CellType t = getCellType(in);
		return (t==CellType.TOP_LEFT_OF_RANGE) || (t==CellType.INDIVIDUAL);
	}
	
	
}
