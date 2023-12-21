package org.concordion.ext.excel.conversion.row;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.concordion.ext.excel.ExcelCellConversionException;
import org.concordion.ext.excel.conversion.AbstractConversionStrategy;
import org.concordion.ext.excel.conversion.ConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;

/**
 * This strategy collects rows and outputs them as a table.
 * 
 * @author robmoffat
 *
 */
public class BasicTableStrategy extends AbstractConversionStrategy<XSSFTable> {

	protected ConversionStrategy<Cell> bodyCell;
	protected ConversionStrategy<Cell> headerCell = null;
	
	public BasicTableStrategy(ConversionStrategy<Cell> bodyCell) {
		super();
		this.bodyCell = bodyCell;
	}
	
	public BasicTableStrategy(ConversionStrategy<Cell> bodyCell, ConversionStrategy<Cell> headerCell) {
		this(bodyCell);
		this.headerCell = headerCell;
	}

	@Override
	public void process(XSSFTable table, HTMLBuilder result) {
		try { 
			boolean header = shouldOutputFirstRowAsHeader(table);
			int columnFrom = table.getStartCellReference().getCol();
			int columnTo = table.getEndCellReference().getCol();
			int rowFrom = table.getStartCellReference().getRow();
			int rowTo = table.getEndCellReference().getRow();
			XSSFSheet sheet = table.getXSSFSheet();
					
			result.startTag("table");
			for (int rowNum = rowFrom; rowNum <= rowTo; rowNum++) {
				Row r = sheet.getRow(rowNum);
				result.startTag("tr");
				for (int i = columnFrom; i <= columnTo; i++) {
					if (header) {
						headerCell.process(r.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK), result);
					} else {
						bodyCell.process(r.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK), result);
					}
				}
				
				header = false;
				result.endTag();
			}
			result.endTag();
		} catch(Exception e) {
			throw new ExcelCellConversionException("Error processing table ",
					table!=null ? table.getStartCellReference() : null, e);
		}
	}

	protected boolean shouldOutputFirstRowAsHeader(XSSFTable t) {
		return true;
	}

}
