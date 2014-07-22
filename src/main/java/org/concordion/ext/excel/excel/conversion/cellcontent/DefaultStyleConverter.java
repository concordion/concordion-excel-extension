package org.concordion.ext.excel.conversion.cellcontent;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.concordion.ext.excel.conversion.AbstractConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;

/**
 * Converts font styles and cell colours by default.  Extend this class if you want to preseve other 
 * cell attributes such as size, word wrap etc.
 * 
 * @author robmoffat
 *
 */
public class DefaultStyleConverter extends AbstractConversionStrategy<Cell> {

	private int defaultFontPointSize = 12;
	private int defaultBoldWeight = 400;
	private int defaultBGColor = 64;
	private int defaultFGColor = 0;
	
	public DefaultStyleConverter(int defaultFontPointSize, int defaultBoldWeight, int defaultFGColor, int defaultBGColor) {
		super();
		this.defaultFontPointSize = defaultFontPointSize;
		this.defaultBoldWeight = defaultBoldWeight;
		this.defaultFGColor = defaultFGColor;
		this.defaultBGColor = defaultBGColor;
	}

	public DefaultStyleConverter() {
		super();
	}

	@Override
	public void process(Cell c, HTMLBuilder b) {
		StringBuilder out = new StringBuilder();
		extractColourStyle(c, out);
		extractFontStyle(c, out);
		
		if (out.length() > 0) {
			b.addAttribute("style", out.toString());
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
			// TODO: find HTML For this
		}
		
		if (f.getBoldweight() > defaultBoldWeight) {
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
