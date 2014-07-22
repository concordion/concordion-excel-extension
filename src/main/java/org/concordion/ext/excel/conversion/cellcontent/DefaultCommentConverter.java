package org.concordion.ext.excel.conversion.cellcontent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.concordion.ext.excel.ExcelConversionException;
import org.concordion.ext.excel.conversion.AbstractConversionStrategy;
import org.concordion.ext.excel.conversion.HTMLBuilder;

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
	 */
	@Override
	public void process(Cell in, HTMLBuilder out) {
		if (in == null) {
			return;
		}
		
		Comment comment = in.getCellComment();
		if (comment != null) {
			String commentString =comment.getString().toString();
			Matcher m = REGEX_PAT.matcher(commentString);
			while (m.find()) {
				String name = m.group(1);
				String value = m.group(2);
				setHtmlAttribute(name, value, out);
			}
			
		}
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

	/* From: http://stackoverflow.com/questions/317053/regular-expression-for-extracting-tag-attributes */
	public static final String REGEX_STR = "(\\S+)=[\"']?((?:.(?![\"']?\\s+(?:\\S+)=|[>\"']))+.)[\"']?";
	public static final Pattern REGEX_PAT = Pattern.compile(REGEX_STR);


}
