package org.concordion.ext.excel.conversion.cellcontent;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.helpers.ColumnHelper;
import org.concordion.ext.excel.conversion.AbstractConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;


/**
 * Converts font styles and cell colours by default.  Extend this class if you want to preseve other 
 * cell attributes such as size, word wrap etc.
 * 
 * @author robmoffat
 *
 */
public class DefaultStyleConverter extends AbstractConversionStrategy<Cell> {

	private String defaultFont = "Calibri";
	private int defaultFontPointSize = 12;
	private int defaultBoldWeight = 400;
	private int defaultBGColor = 64;
	private int defaultFGColor = 0;
	
	public DefaultStyleConverter(int defaultFontPointSize, int defaultBoldWeight, int defaultFGColor, int defaultBGColor, String defaultFont) {
		super();
		this.defaultFontPointSize = defaultFontPointSize;
		this.defaultBoldWeight = defaultBoldWeight;
		this.defaultFGColor = defaultFGColor;
		this.defaultBGColor = defaultBGColor;
		this.defaultFont = defaultFont;
	}

	public DefaultStyleConverter() {
		super();
	}

	@Override
	public void process(Cell c, HTMLBuilder b) {
		StringBuilder out = new StringBuilder();
		extractColourStyle(c, out);
		extractFontStyle(c, out);
		handleHiddenCells(c, out);
		
		if (out.length() > 0) {
			b.addAttribute("style", out.toString());
		}
	}
	
	private boolean isHiddenRow(Cell c) {
		Row r = c.getRow();
		CellStyle cs = r.getRowStyle();
		return ((cs != null) && cs.getHidden()) || ((r != null) && r.getZeroHeight());
	}
	
	private boolean isHiddenColumn(Cell c) {
		Sheet sheet = c.getSheet();
		ColumnHelper ch = ((org.apache.poi.xssf.usermodel.XSSFSheet)sheet).getColumnHelper();
		CTCol column = ch.getColumn(c.getColumnIndex(), true);
		return (column != null) && (column.getHidden());
	}
	
	protected void handleHiddenCells(Cell c, StringBuilder out) {
		if (c == null) {
			return;
		}
		if (isHiddenRow(c) || isHiddenColumn(c)) {
			out.append("display: none; ");
		}
	}


	protected void extractColourStyle(Cell c, StringBuilder out) {
		if (c == null) {
			return;
		}
				
		CellStyle style = c.getCellStyle();
		XSSFColor fgColor = ((XSSFFont) getFont(c)).getXSSFColor();
		
		if ((fgColor != null) && (fgColor.getIndexed() != defaultFGColor)) {
			convertColour(fgColor, "color", out);			
		}
		
		XSSFColor bgColor = (XSSFColor) style.getFillForegroundColorColor();
		if ((bgColor != null) && (bgColor.getIndexed() != defaultBGColor)) {
			convertColour(bgColor, "background-color", out);
		}
	}
	
	protected void convertColour(XSSFColor c, String attr,
		StringBuilder out) {
		String argbHex = c.getARGBHex();
		String rgbHex = "#"+argbHex.substring(2);
		if (argbHex != null) {
			out.append(attr+": "+rgbHex+"; ");
		} 
	}

	protected void extractFontStyle(Cell c, StringBuilder out) {
		if (c == null) {
			return;
		}
		
		Font f = getFont(c);
		int fontPointSize = f.getFontHeightInPoints();
		if (fontPointSize != defaultFontPointSize) {
			out.append("font-size: "+fontPointSize+"pt; ");
		}
		
		if (f.getItalic()) {
			out.append("font-style: italic; ");
		}
		
		if (f.getStrikeout()) {
			out.append("text-decoration: line-through;");
		}
		
		if (!f.getFontName().equals(defaultFont)) {
			out.append("font-family: "+f.getFontName()+"; ");
		}
		
		if (f.getBold()) {
			out.append("font-weight: bold; ");
		}
	}
	
	protected Font getFont(Cell c) {
		CellStyle style = c.getCellStyle();
		short fontIndex = style.getFontIndex();
		Sheet s = c.getSheet();
		Font f = s.getWorkbook().getFontAt(fontIndex);
		return f;
	}

}
