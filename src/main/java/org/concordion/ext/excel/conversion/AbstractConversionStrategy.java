package org.concordion.ext.excel.conversion;


/**
 * Contains some helper methods used for converting cells to HTML.
 * 
 * @author robmoffat
 *
 * @param <X>
 */
public abstract class AbstractConversionStrategy<X> implements ConversionStrategy<X> {

	@Override
	public void start(HTMLBuilder hb) {
	}

	@Override
	public void finish(HTMLBuilder hb) {
	}

	
}
