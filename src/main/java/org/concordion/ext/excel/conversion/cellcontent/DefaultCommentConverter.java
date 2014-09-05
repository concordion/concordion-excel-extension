package org.concordion.ext.excel.conversion.cellcontent;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.concordion.ext.excel.ExcelConversionException;
import org.concordion.ext.excel.conversion.AbstractConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;
import org.dom4j.DocumentException;

/**
 * Knows how to convert Excel cell comments into html attributes, generally for calling concordion commands.
 * 
 * This can add attributes to not only the current HTML element, but parent elements, by prefixing the name in the excel comment.
 * 
 * <ul>
 * <li><strong>name</strong>  Adds to the current element</li>
 * <li><strong>../name</strong>  Adds to the parent element</li>
 * <li><strong>../../name</strong>  Adds to the parent's parent element (and so on)</li>
 * <li><strong>(table)name</strong>  Adds to the first parent element which is a table tag.  (replace table with any tag)</li>
 * <li><strong>/name</strong> Adds the attribute to the root element of the HTML doc. </li>
 * </ul>
 * 
 * @author robmoffat
 *
 */
public class DefaultCommentConverter extends AbstractConversionStrategy<Cell> {

	/**
	 * We use a regular expression to extract name="value" pairs from the Excel Cell comments.
	 * This is because the comments are not always well-formed markup.
	 * 
	 * This is probably going to be a bit unreliable.
	 * @throws DocumentException 
	 */
	@Override
	public void process(Cell in, final HTMLBuilder out) {
		if (in == null) {
			return;
		}
		
		Comment comment = in.getCellComment();
		if (comment != null) {
			String commentString =comment.getString().toString();
			
			parseCommentString(commentString, new DefaultCommentConverter.Callback() {
			
				public void addAttribute(String name, String value) {
					setHtmlAttribute(name, value, out);
				}
				
			});
		}
	}
	
	static interface Callback {
		
		public void addAttribute(String name, String value);
		
		
	}
	
	enum Mode { READING_NAME, LOOKING_FOR_EQUALS, LOOKING_FOR_VALUE, READING_VALUE_UNQUOTED, READING_VALUE_SINGLE, READING_VALUE_DOUBLE }
	
	protected void parseCommentString(String in, Callback c) {
		Mode m = Mode.READING_NAME;	
		String name = null;
		StringBuilder elem = new StringBuilder();
		int i = 0;
		while (i < in.length()) {
			char ch = in.charAt(i);
			i++;
			switch (m) {
				case READING_NAME:
					if (Character.isWhitespace(ch))  {
						name = extract(elem);
						m = Mode.LOOKING_FOR_EQUALS;
					} else if ('=' == ch) {
						name = elem.toString();
						elem.setLength(0);
						m = Mode.LOOKING_FOR_VALUE;
					} else{
						elem.append(ch);
					}
					break;
				case LOOKING_FOR_EQUALS:
					if (Character.isWhitespace(ch))  {
						// do nothing
					} else if ('=' == ch) {
						m = Mode.LOOKING_FOR_VALUE;
					} else{
						// starting a new name
						elem.append(ch);
						m = Mode.READING_NAME;
					}
					break;
				case LOOKING_FOR_VALUE:
					if (Character.isWhitespace(ch)) {
						// do nothing
					} else if ('\'' == ch) {
						m = Mode.READING_VALUE_SINGLE;
					} else if ('\"' == ch) {
						m = Mode.READING_VALUE_DOUBLE;
					} else {
						m = Mode.READING_VALUE_UNQUOTED;
					}
					break;
				case READING_VALUE_UNQUOTED:
					if (Character.isWhitespace(ch))  {
						c.addAttribute(name, elem.toString());
						extract(elem);
						m = Mode.READING_NAME;
					} else {
						elem.append(ch);
					} 
					break;
				case READING_VALUE_DOUBLE:
					if ('"' == ch)  {
						c.addAttribute(name, elem.toString());
						extract(elem);
						m = Mode.READING_NAME;
					} else {
						elem.append(ch);
					} 
					break;
				case READING_VALUE_SINGLE:
					if ('\'' == ch)  {
						c.addAttribute(name, elem.toString());
						extract(elem);
						m = Mode.READING_NAME;
					} else {
						elem.append(ch);
					} 
					break;
			}
		}
	}
			
	
	private String extract(StringBuilder elem) {
		String out = elem.toString();
		elem.setLength(0);
		return out;
	}


	private void setHtmlAttribute(String name, String value, HTMLBuilder out) {
		if (name.startsWith("../")) {
			setHtmlAttribute(name.substring(3), value, out.withParentTag());
		} else if (name.startsWith("(")) {
			int rightParen = name.indexOf(")");
			String tag = name.substring(1, rightParen);
			name = name.substring(rightParen+1);
			out = findMatchingParentTag(out, tag);
			out.addAttribute(name, value);
		} else if (name.startsWith("/")) {
			name = name.substring(1);
			out = out.withRootTag();
		} else {
			out.addAttribute(name, value);
		}
	}

	private HTMLBuilder findMatchingParentTag(HTMLBuilder out, String tag) {
		try {
			if (tag.equals(out.getCurrentOpenTag())) {
				return out;
			} else {
				return findMatchingParentTag(out.withParentTag(), tag);
			}
		} catch (ExcelAttributeConversionException e) {
			throw e;
		} catch (ExcelConversionException ce) {
			throw new ExcelAttributeConversionException("Couldn't find matching tag: "+tag, ce);
		}
	}

}
