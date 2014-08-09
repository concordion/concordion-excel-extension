package org.concordion.ext.excel.conversion;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.StringEscapeUtils;
import org.concordion.ext.excel.ExcelConversionException;

public class HTMLBuilderImpl implements HTMLBuilder {

	static class Tag {
		
		String name;
		Map<String, String> attributes = new HashMap<String, String>();
		List<Object> content = new LinkedList<Object>();
		
		public Tag(String name) {
			this.name = name;
		}
		
		public void add(Object c) {
			content.add(c);
		}
		
		public void appendAttribute(String name, String value) {
			attributes.put(name, value);
		}
		
		public void output(StringBuilder sb) {
			boolean hasContent = content.size()!=0;
			
			sb.append("<");
			sb.append(name);
			for (Map.Entry<String, String> attr : attributes.entrySet()) {
				sb.append(" ");
				sb.append(attr.getKey());
				sb.append("=\"");
				sb.append(attr.getValue());
				sb.append("\"");
			}
			
			if (!hasContent) {
				sb.append("/");
			}
			
			sb.append(">");
			
			if (hasContent) {
				for (Object o : content) {
					if (o instanceof Tag) {
						((Tag)o).output(sb);
					} else if (o instanceof String) {
						sb.append((String) o);
					} else {
						throw new ExcelConversionException("Can't create HTML from tag contents type "+o.getClass());
					}
				}
				
				sb.append("</");
				sb.append(name);
				sb.append(">");
			}
		}
	}
	
	Tag top = null;
	Stack<Tag> document = new Stack<HTMLBuilderImpl.Tag>();
	
	public HTMLBuilderImpl() {
	}
	
	HTMLBuilderImpl(Stack<Tag> document, Tag top) {
		this.document = document;
		this.top = top;
	}
	
	@Override
	public void startTag(String tag) {
		Tag tag2 = new Tag(escape(tag));
		if (document.isEmpty()) {
			top = tag2;
		} else {
			Tag container = document.peek();
			container.add(tag2);
		}
		document.add(tag2);
	}

	@Override
	public void addAttribute(String name, String value) {
		if (isPresent(name) && isPresent(value)) {
			document.peek().appendAttribute(escape(name), escape(value));
		}
	}

	private boolean isPresent(String attrStyle) {
		return (attrStyle != null) && (attrStyle.trim().length() > 0);
	}
	
	@Override
	public void addText(String text) {
		document.peek().add(escape(text));
	}
	
	@Override
	public void addUnescapedText(String text) {
		document.peek().add(text);
	}

	@Override
	public void endTag() {
		document.pop();
	}
	
	protected String escape(String s) {
		return StringEscapeUtils.escapeHtml4(s);
	}

	public String toString() {
		StringBuilder out = new StringBuilder();
		top.output(out);
		return out.toString();
	}

	@Override
	public HTMLBuilder withParentTag() {
		if (document.size() <= 1) {
			throw new ExcelConversionException("Trying to add content above the HTML root element");
		}
		@SuppressWarnings("unchecked")
		Stack<Tag> higherUp = (Stack<Tag>) document.clone();
		higherUp.pop();
		return new HTMLBuilderImpl(higherUp, top);
		
	}

	@Override
	public HTMLBuilder withRootTag() {
		if (top == null) {
			throw new ExcelConversionException("Trying to add content above the HTML root element");
		}
		
		Stack<Tag> higherUp = new Stack<HTMLBuilderImpl.Tag>();
		higherUp.add(top);
		return new HTMLBuilderImpl(higherUp, top);
	}

	@Override
	public String getCurrentOpenTag() {
		return document.peek().name;
	}
	
	@Override
	public void setCurrentOpenTag(String tag) {
		document.peek().name = tag;
	}

	@Override
	public String getCurrentOpenTagAttribute(String name) {
		return document.peek().attributes.get(name);
	}
	
}
