package org.concordion.ext.excel;

import junit.framework.Assert;

/**
 * All of these tests work on the basis of comparing what the Excel converter produced with
 * some expected version of the output.
 * 
 * @author robmoffat
 *
 */
public abstract class AbstractSpecTest {

	public static final String EXPECTED0 = "<html xmlns:concordion=\"http://www.concordion.org/2007/concordion\"><head><title>";
	public static final String EXPECTED1 = ".</title></head><body><h1>";
	public static final String EXPECTED2 = ".</h1><div class=\"example\"><h2>Sheet1</h2>";
	
	public boolean htmlMatchesExpected() {
		String firstPart = EXPECTED0+getTestName()+EXPECTED1+getTestName()+EXPECTED2;
		String actual = ExcelExtension.getLastConversion();
		System.out.println(actual);

		if (actual.indexOf(firstPart) == -1) {
			return false;
		}
		
		if (actual.indexOf(getBody()) == -1) {
			return false;
		}

		return true;
	}
	
	public String getTestName() {
		return this.getClass().getSimpleName();
	}
	
	public abstract String getBody();
	
	
	
}
