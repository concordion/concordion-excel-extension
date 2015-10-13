package org.concordion.ext.excel.conversion;

/**
 * Simple builder interface for managing the creation of compliant HTML, 
 * and ensuring that escaping is done correctly.
 * 
 * @author robmoffat
 *
 */
public interface HTMLBuilder {

	/**
	 * Pushes a new open tag onto the context
	 * @param tag xhtml tag to push.
	 */
	public void startTag(String tag);
	
	public void addAttribute(String attr, String value);
	
	public void addText(String text);
	
	public void addUnescapedText(String text);
	
	public void endTag();
	
	public String getCurrentOpenTag();
	
	public void setCurrentOpenTag(String tag);
	
	public String getCurrentOpenTagAttribute(String name);
	
	/**
	 * @return an HTML builder for the parent tag, allowing you to modify already created HTML elements.
	 */
	public HTMLBuilder withParentTag();
	
	/**
	 * @return the HTML builder for the root (i.e. &lt;html&gt;) tag at the start of the document.
	 */
	public HTMLBuilder withRootTag();

}
