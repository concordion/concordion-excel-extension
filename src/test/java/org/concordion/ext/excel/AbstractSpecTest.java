package org.concordion.ext.excel;

import java.util.HashMap;
import java.util.Map;

import junit.framework.ComparisonFailure;


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
	
	public boolean htmlMatchesExpected() {
		String firstPart = EXPECTED0+getTestName()+EXPECTED1+getTestName()+EXPECTED2;
		String actual = conversions.get(getTestName());
		System.out.println(actual);

		if (actual.indexOf(firstPart) == -1) {
			throw new ComparisonFailure("Doesn't contain expected", firstPart, actual);
		}
		
		if (actual.indexOf(getBody()) == -1) {
			throw new ComparisonFailure("Doesn't contain body", getBody(), actual);

		}

		return true;
	}
	
	public String getTestName() {
		return this.getClass().getSimpleName()+".xlsx";
	}
	
	public abstract String getBody();
	
	
	
}
