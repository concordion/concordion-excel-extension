package org.concordion.ext.excel.conversion.row;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.concordion.ext.excel.conversion.ConversionHelpers;
import org.concordion.ext.excel.conversion.ConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;

/**
 * This class exercises logic to decide when a table in an excel spreadsheet is beginning or ending.
 * 
 * That is, it allows us to decide between 'table' and 'not table' rows.
 * 
 * @author robmoffat
 *
 */
public class HeuristicTablesRowStrategy implements ConversionStrategy<Row> {

	private ConversionStrategy<Row> nonTableStrategy;
	private ConversionStrategy<Row> tableStrategy;
	
	enum Status { NOT_STARTED, IN_TABLE, NON_TABLE };
	Status status;
	
	public HeuristicTablesRowStrategy(ConversionStrategy<Row> nonTableStrategy,
			ConversionStrategy<Row> tableStrategy) {
		super();
		this.nonTableStrategy = nonTableStrategy;
		this.tableStrategy = tableStrategy;
		this.status = Status.NOT_STARTED;
	}

	@Override
	public void start(HTMLBuilder out) {
	}

	@Override
	public void process(Row in, HTMLBuilder out) {
		boolean isTable = isPartOfTable(in);
		if (status == Status.IN_TABLE) {
			if (isTable) {
				tableStrategy.process(in, out);
			} else {
				tableStrategy.finish(out);
				nonTableStrategy.start(out);
				nonTableStrategy.process(in, out);
				status = Status.NON_TABLE;
			}
		} else if (status == Status.NON_TABLE){
			if (isTable) {
				nonTableStrategy.finish(out);
				tableStrategy.start(out);
				tableStrategy.process(in, out);
				status = Status.IN_TABLE;
			} else {
				nonTableStrategy.process(in, out);
			}
		} else {
			if (isTable) {
				tableStrategy.start(out);
				tableStrategy.process(in, out);
				status = Status.IN_TABLE;
			} else {
				nonTableStrategy.start(out);
				nonTableStrategy.process(in, out);
				status = Status.NON_TABLE;
			}
		}
	}

	protected boolean isPartOfTable(Row in) {
		if (in == null) {
			return false;
		}
		
		return isPartOfExcelTable(in); 
		
//		|| 
//				hasBorderFormatting(in) || 
//				hasMergedCells(in);
	}

	protected boolean hasMergedCells(Row in) {
		for (Cell cell : in) {
			if (ConversionHelpers.getRangeForCell(cell) != null) {
				return true;
			}
		}
		
		return false;
	}

	protected boolean isPartOfExcelTable(Row in) {
		for (Cell cell : in) {
			if (ConversionHelpers.getTableForCell(cell) != null) {
				return true;
			}
		}
		
		return false;
	}

	protected boolean hasBorderFormatting(Row in) {
		for (Cell cell : in) {
			if (hasBorderFormatting(cell)) {
				return true;
			}
		}
		
		return false;
	}

	protected boolean hasBorderFormatting(Cell cell) {
		CellStyle style = cell.getCellStyle();
		return (style.getBorderBottom() != CellStyle.BORDER_NONE) ||
				(style.getBorderTop() != CellStyle.BORDER_NONE) ||
				(style.getBorderLeft() != CellStyle.BORDER_NONE) ||
				(style.getBorderRight() != CellStyle.BORDER_NONE);
	}

	@Override
	public void finish(HTMLBuilder out) {
		if (status == Status.IN_TABLE) {
			tableStrategy.finish(out);
		} else if (status == Status.NON_TABLE) {
			nonTableStrategy.finish(out);
		}
		
		status = Status.NOT_STARTED;
	}

}
