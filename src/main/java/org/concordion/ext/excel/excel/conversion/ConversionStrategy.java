package org.concordion.ext.excel.conversion;

/**
 * Defines all or part of the approach of converting from X into a HTML String.
 * 
 * The contract of this class is that the methods must be called in order: start, process*, end.
 * They must also always be called with the same {@link StringBuilder} argument.
 * 
 * The converter is not thread-safe, but can be reused multiple times serially.
 * 
 * @author robmoffat
 *
 * @param <X>
 */
public interface ConversionStrategy<X> {

	public void start(HTMLBuilder out);
	
	public void process(X in, HTMLBuilder out);
	
	public void finish(HTMLBuilder out);
	
}
