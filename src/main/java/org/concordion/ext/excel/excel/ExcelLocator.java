package org.concordion.ext.excel;

import org.concordion.api.Resource;
import org.concordion.api.SpecificationLocator;
import org.concordion.internal.util.Check;

/**
 * Locates Excel files with the extension .xlsx.  
 * 
 * @author robmoffat
 *
 */
public class ExcelLocator implements SpecificationLocator {

	public static final String EXCEL_EXTENSION = ".xlsx";

	@Override
    public Resource locateSpecification(Object fixture) {
        Check.notNull(fixture, "Fixture is null");
        
        String dottedClassName = fixture.getClass().getName();
        String slashedClassName = dottedClassName.replaceAll("\\.", "/");
        String specificationName = slashedClassName.replaceAll("(Fixture|Test)$", "");
        String resourcePath = "/" + specificationName + EXCEL_EXTENSION;
        
        return new Resource(resourcePath);
    }
}
