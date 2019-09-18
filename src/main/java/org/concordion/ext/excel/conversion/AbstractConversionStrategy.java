package org.concordion.ext.excel.conversion;


/**
 * Base class for conversion strategies
 * 
 * @author robmoffat
 *
 * @param <X> Type of the object being converted.
 */
public abstract class AbstractConversionStrategy<X> implements ConversionStrategy<X> {

	public final static String ATTR_TRACE="excel-location"; 
	
}
