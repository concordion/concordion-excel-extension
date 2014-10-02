package org.concordion.ext.excel.conversion.row;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.concordion.ext.excel.conversion.ConversionHelpers;
import org.concordion.ext.excel.conversion.ConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;

/**
 * This class takes notice of when cells are parts of tables, and calls the table strategy for those 
 * that are.
 * 
 * That is, it allows us to decide between 'table' and 'not table' cells.
 * 
 * @author robmoffat
 *
 */
public class ParagraphsAndTablesRowStrategy extends ParagraphRowStrategy {

	private ConversionStrategy<XSSFTable> tableStrategy;
	
	public ParagraphsAndTablesRowStrategy(ConversionStrategy<Cell> cellStrategy,
			ConversionStrategy<XSSFTable> tableStrategy) {
		super(cellStrategy);
		this.tableStrategy = tableStrategy;
	}

	private Set<XSSFTable> processedTables = new HashSet<XSSFTable>();

	
	@Override
	protected boolean processCell(HTMLBuilder result, Cell cell, boolean firstCell) {
		XSSFTable tableForCell = ConversionHelpers.getTableForCell(cell);
		
		if (tableForCell == null) {
			return super.processCell(result, cell, firstCell);			
		} else if (!processedTables.contains(tableForCell)) {
			tableStrategy.process(tableForCell, result);
			processedTables.add(tableForCell);
		}
		return firstCell;
	}

	@Override
	protected boolean cellHasData(Cell in) {
		return super.cellHasData(in) && (ConversionHelpers.getTableForCell(in) == null);
	}

	
}
