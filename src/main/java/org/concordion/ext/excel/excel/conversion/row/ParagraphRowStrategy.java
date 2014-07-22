package org.concordion.ext.excel.conversion.row;

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
		
		result.startTag("p");
		boolean firstCell = true;
		
		cellStrategy.start(result);
		for (Cell cell : r) {
			if (firstCell) {
				firstCell = false;
			} else {
				appendInterCellElements(r, result);
			}

			cellStrategy.process(cell, result);
		}
		cellStrategy.finish(result);
		
		result.endTag();
	}

	protected void appendInterCellElements(Row r, HTMLBuilder result) {
		result.addText(" ");
	}
}
