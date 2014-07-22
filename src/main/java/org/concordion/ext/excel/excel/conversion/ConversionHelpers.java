package org.concordion.ext.excel.conversion;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;

public class ConversionHelpers {

	public static CellRangeAddress getRangeForCell(Cell in) {
		if (in == null) {
			return null;
		}
		
		Sheet s = in.getSheet();
		
		if (s instanceof XSSFSheet) {
			XSSFSheet xssfSheet = (XSSFSheet) s;
			
			for (int i = 0; i < xssfSheet.getNumMergedRegions(); i++) {
				CellRangeAddress cra = xssfSheet.getMergedRegion(i);
				if (isPartOf(cra, in)) {
					return cra;
				}
			}
		}
		
		return null;
	}

	public static boolean isPartOf(CellRangeAddress cra, Cell in) {
		int inRow = in.getRowIndex();
		int inCol = in.getColumnIndex();
		return (cra.getFirstColumn() <= inCol) 
			&& (cra.getLastColumn() >= inCol)
			&& (cra.getFirstRow() <= inRow)
			&& (cra.getLastRow() >= inRow);
	}
	
	public static XSSFTable getTableForCell(Cell c) {
		XSSFSheet sheet = (XSSFSheet) c.getSheet();
		for (XSSFTable t : sheet.getTables()) {
			if (tableContainsCell(t, c)) {
				return t;
			}
		}
		
		return null;
 	}

	private static boolean tableContainsCell(XSSFTable t, Cell c) {
		return (t.getStartCellReference().getRow() <= c.getRowIndex()) &&
		 (t.getEndCellReference().getRow() >= c.getRowIndex()) && 
		 (t.getStartCellReference().getCol() <= c.getColumnIndex()) &&
		 (t.getEndCellReference().getCol() >= c.getColumnIndex());
	}

}
