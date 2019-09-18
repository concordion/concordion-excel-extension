package org.concordion.ext.excel.conversion.cell;

import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
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
		
		try {
			if (hasContent(in)) {
				out.startTag(tag);
				out.addAttribute(ATTR_TRACE, in.getAddress().toString());			
				
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
		} catch(ExcelCellConversionException cellEx) {
			throw cellEx;
		} catch(Exception ex) {
			throw new ExcelCellConversionException("error processing", in, ex);
		}
	}

	protected String getCellContent(Cell in) {
		if (in == null) {
			return "";
		}

		return getCellStringContentsForType(in.getCellType(), in);
	}

	/*
	 * There are some notable differences here between what appears in your HTML
	 * file and what you have formatted on the spreadsheet.  It's pretty annoying,
	 * not sure there's much I can do, because I don't want to overcomplicate this.
	 * 
	 * Two examples:  booleans appear in lower case (true, false), dates in the default
	 * Excel format (in a UK locale) like 15/1/2021 get modified to 1/15/21. 
	 */
	protected String getCellStringContentsForType(CellType type, Cell in) {
		DataFormatter df = new DataFormatter(Locale.getDefault());
		return df.formatCellValue(in, EVALUATOR);
	}
	
	public static final FormulaEvaluator EVALUATOR = new CachedValueFormulaEvaluator();
	

	protected boolean hasContent(Cell in) {
		return (in != null)
				&& ((in.getCellType() != CellType.BLANK) || includeEmptyCells);
	}

}
