package org.concordion.ext.excel.conversion.cell;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.concordion.ext.excel.ExcelCellConversionException;
import org.concordion.ext.excel.conversion.AbstractConversionStrategy;
import org.concordion.ext.excel.conversion.ConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;

/**
 * Generic approach to converting a single cell of a spreadsheet into HTML.
 * 
 * @author robmoffat
 * 
 */
public class BasicCellConversionStrategy extends
		AbstractConversionStrategy<Cell> {

	private String tag;
	private boolean includeEmptyCells;
	private List<ConversionStrategy<Cell>> contentConverters;

	public BasicCellConversionStrategy(List<ConversionStrategy<Cell>> contentConverters,
			String tag, boolean includeEmptyCells) {
		super();
		this.tag = tag;
		this.includeEmptyCells = includeEmptyCells;
		this.contentConverters = contentConverters;
	}

	@Override
	public void process(Cell in, HTMLBuilder out) {
		if (hasContent(in)) {
			out.startTag(tag);
			for (ConversionStrategy<Cell> cs : contentConverters) {
				cs.process(in, out);
			}
			
			String content = getCellContent(in);
			
			String tagOverride = out.getCurrentOpenTagAttribute("html-tag");
			if (tagOverride != null) {
				out.setCurrentOpenTag(tagOverride);
			}
			
			out.addText(content);
			out.endTag();
		}
	}

	protected String getCellContent(Cell in) {
		if (in == null) {
			return "";
		}

		return getCellStringContentsForType(in.getCellType(), in);
	}

	protected String getCellStringContentsForType(int type, Cell in) {
		switch (type) {
		case CELL_TYPE_BLANK:
			return "";
		case CELL_TYPE_BOOLEAN:
			return in.getBooleanCellValue() ? "true" : "false";
		case CELL_TYPE_ERROR:
			// not sure about this. Should this be allowed?
			return "";
		case CELL_TYPE_FORMULA:
			return getCellStringContentsForType(in.getCachedFormulaResultType(), in);
		case CELL_TYPE_STRING:
			return in.getStringCellValue();
		case CELL_TYPE_NUMERIC:
			// need to handle difference between numbers and dates

			// TODO: handle dates.

			return "" + in.getNumericCellValue();
		default:
			throw new ExcelCellConversionException("Unknown Excel Cell Type: "
					+ in.getCellType(), in);
		}
	}

	protected boolean hasContent(Cell in) {
		return (in != null)
				&& ((in.getCellType() != CELL_TYPE_BLANK) || includeEmptyCells);
	}

}
