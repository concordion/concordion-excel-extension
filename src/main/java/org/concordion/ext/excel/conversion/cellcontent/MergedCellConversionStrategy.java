package org.concordion.ext.excel.conversion.cellcontent;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.concordion.ext.excel.conversion.AbstractConversionStrategy;
import org.concordion.ext.excel.conversion.ConversionHelpers;
import org.concordion.ext.excel.conversion.HTMLBuilder;

/**
 * Handles rowspan and colspan attributes.
 * 
 * @author robmoffat
 *
 */
public class MergedCellConversionStrategy extends AbstractConversionStrategy<Cell> {

	public MergedCellConversionStrategy() {
	}

	@Override
	public void process(Cell in, HTMLBuilder out) {
		if (in == null) {
			return;
		}
		
		CellRangeAddress cra = ConversionHelpers.getRangeForCell(in);
		if ((cra != null) && (ConversionHelpers.isPartOf(cra, in))) {
			int rowCount = getRangeRowCount(cra);
			int colCount = getRangeColCount(cra);
			
			if (rowCount  > 1 ) {
				out.addAttribute("rowspan", ""+rowCount);
			}
			
			if (colCount > 1) {
				out.addAttribute("colspan",""+colCount);
			}
		}
	}

	private int getRangeColCount(CellRangeAddress cra) {
		return cra.getLastColumn() - cra.getFirstColumn() + 1;
	}

	private int getRangeRowCount(CellRangeAddress cra) {
		return cra.getLastRow() - cra.getFirstRow() + 1;
	}

}
