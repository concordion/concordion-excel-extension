package org.concordion.ext.excel.conversion.row;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.concordion.ext.excel.conversion.AbstractConversionStrategy;
import org.concordion.ext.excel.conversion.ConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;
/**
 * Handles the conversion of Excel rows by turning each one into a "p" HTML tag.
 * 
 * @author robmoffat
 *
 */
public class ParagraphRowStrategy extends AbstractConversionStrategy<Row> {

	ConversionStrategy<Cell> cellStrategy;
	
	public ParagraphRowStrategy(ConversionStrategy<Cell> cellStrategy) {
		super();
		this.cellStrategy = cellStrategy;
	}

	@Override
	public void process(Row r, HTMLBuilder result) {
		if (r == null) {
			return;
		}
	
		boolean rowHasParagraph = rowNeedsParagraph(r);
		
		if (rowHasParagraph) {
			result.startTag("p");
		}
		
		boolean firstCell = true;
		
		for (Cell cell : r) {
			firstCell = processCell(result, cell, firstCell);
		}

		if (rowHasParagraph) {
			result.endTag();
		}
	}

	protected boolean processCell(HTMLBuilder result, Cell cell, boolean firstCell) {
		if (cellHasData(cell)) {
			if (firstCell) {
				firstCell = false;
			} else {
				appendInterCellElements(result);
			}

			cellStrategy.process(cell, result);
		}
		
		return firstCell;
	}

	protected void appendInterCellElements(HTMLBuilder result) {
		result.addText(" ");
	}
	
	protected boolean cellHasData(Cell in) {
		return (in != null) && (in.getCellType() != CELL_TYPE_BLANK);
	}
	
	protected boolean rowNeedsParagraph(Row r) {
		for (Cell cell : r) {
			if (cellHasData(cell)) {
				return true;
			}
		}
		
		return false;
	}
}
