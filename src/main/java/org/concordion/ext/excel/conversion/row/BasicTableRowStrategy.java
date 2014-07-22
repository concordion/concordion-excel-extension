package org.concordion.ext.excel.conversion.row;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.concordion.ext.excel.conversion.AbstractConversionStrategy;
import org.concordion.ext.excel.conversion.ConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;

/**
 * This strategy collects rows and outputs them as a table.
 * 
 * @author robmoffat
 *
 */
public class BasicTableRowStrategy extends AbstractConversionStrategy<Row> {

	protected ConversionStrategy<Cell> bodyCell;
	protected ConversionStrategy<Cell> headerCell = null;
	
	public BasicTableRowStrategy(ConversionStrategy<Cell> bodyCell) {
		super();
		this.bodyCell = bodyCell;
	}
	
	public BasicTableRowStrategy(ConversionStrategy<Cell> bodyCell, ConversionStrategy<Cell> headerCell) {
		this(bodyCell);
		this.headerCell = headerCell;
	}

	
	@Override
	public void process(Row in, HTMLBuilder out) {
		collectedRows.add(in);
	}
	
	protected List<Row> collectedRows = new LinkedList<Row>();

	@Override
	public void start(HTMLBuilder sb) {
		super.start(sb);
		collectedRows.clear();
	}

	@Override
	public void finish(HTMLBuilder result) {
		boolean header = shouldOutputFirstRowAsHeader();
		int columns = calculateTableColumnCount();
		int rows = calculateTableRowCount();
		
		bodyCell.start(result);
		
		result.startTag("table");
		int rowNum = 0;
		for (Row r : collectedRows) {
			if (rowNum>rows) {
				break;
			}
			
			rowNum ++;
			
			if (header) {
				headerCell.start(result);
			}
			
			result.startTag("tr");
			for (int i = 0; i < columns; i++) {
				if (header) {
					headerCell.process(r.getCell(i), result);
				} else {
					bodyCell.process(r.getCell(i), result);
				}
			}
			
			if (header) {
				headerCell.finish(result);
				header = false;
			}
			result.endTag();
		}
		result.endTag();
		
		bodyCell.finish(result);
		
		super.finish(result);
	}
	
	private int calculateTableRowCount() {
		int maxRows = 0;
		for (Row r : collectedRows) {
			for (int i = r.getFirstCellNum(); i < r.getLastCellNum(); i++) {
				if (cellHasData(r.getCell(i))) {
					maxRows++;
				}
			}

		}
		return maxRows;
	}

	protected int calculateTableColumnCount() {
		int maxColumns = 0;
		for (Row r : collectedRows) {
			for (int i = maxColumns; i < r.getLastCellNum(); i++) {
				if (cellHasData(r.getCell(i))) {
					maxColumns = Math.max(maxColumns, i+1);
				}
			}

		}
		return maxColumns;
	}

	protected boolean rowHasData(Row r) {
		for (Cell cell : r) {
			if (cellHasData(cell)) {
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean cellHasData(Cell in) {
		return (in != null) && (in.getCellType() != CELL_TYPE_BLANK);
	}

	protected boolean shouldOutputFirstRowAsHeader() {
		return headerCell != null;
	}

}
