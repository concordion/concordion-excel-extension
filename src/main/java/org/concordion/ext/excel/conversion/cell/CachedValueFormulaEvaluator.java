package org.concordion.ext.excel.conversion.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

/**
 * Just returns back the type of the pre-calculated value on the spreadsheet to the cell converter.
 * 
 * @author robmoffat
 *
 */
public class CachedValueFormulaEvaluator implements FormulaEvaluator{

	@Override
	public void clearAllCachedResultValues() {
	}

	@Override
	public void notifySetFormula(Cell cell) {
	}

	@Override
	public void notifyDeleteCell(Cell cell) {
	}

	@Override
	public void notifyUpdateCell(Cell cell) {
	}

	@Override
	public void evaluateAll() {
	}

	@Override
	public CellValue evaluate(Cell cell) {
		return null;
	}

	@Override
	public int evaluateFormulaCell(Cell cell) {
		return cell.getCachedFormulaResultType();
	}

	@Override
	public Cell evaluateInCell(Cell cell) {
		return null;
	}

	@Override
	public void setDebugEvaluationOutputForNextEval(boolean value) {
	}

}
