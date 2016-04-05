package org.concordion.ext.excel;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;

import org.junit.Assert;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonListener;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DOMDifferenceEngine;
import org.xmlunit.diff.DifferenceEngine;


/**
 * All of these tests work on the basis of comparing what the Excel converter produced with
 * some expected version of the output.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractSpecTest {
	
	static Map<String, String> conversions = new HashMap<String, String>();
 	
	static {
		ExcelExtension.setConversionMap(conversions);
	}

	public static final String EXPECTED0 = "<html xmlns:concordion=\"http://www.concordion.org/2007/concordion\"><head><title>";
	public static final String EXPECTED1 = "</title></head><body><h1>";
	public static final String EXPECTED2 = "</h1><div class=\"example\"><h2>Sheet1</h2>";
	public static final String EXPECTED3 ="</div></body></html>";
	public static final String CHECK_CODE = "<p><span concordion:assertTrue=\"htmlMatchesExpected()\">Check the formatting</span></p>";
	
	public boolean htmlMatchesExpected() {
		String expectedString = ExcelSpecificationConverter.XML_PROLOG+EXPECTED0+getTestName()+EXPECTED1+getTestName()+EXPECTED2 + getBody() + CHECK_CODE+EXPECTED3;
		Source expected = Input.fromString(expectedString).build();
		
		String actualString = conversions.get(getTestName());
		Source actual = Input.fromString(actualString).build();
		
		DifferenceEngine diff = new DOMDifferenceEngine();
		diff.addDifferenceListener(new ComparisonListener() {
			
	        public void comparisonPerformed(Comparison comparison, ComparisonResult outcome) {
	            Assert.fail("found a difference: " + comparison);
	        }
	    });
		
		diff.compare(expected, actual);
		
		return true;
	}
	
	public String getTestName() {
		return this.getClass().getSimpleName()+".xlsx";
	}
	
	public abstract String getBody();
	
	
	
}
