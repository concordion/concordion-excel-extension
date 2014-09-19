package spec.concordion.ext.excel;

public interface FareCalculator {

	/**
	 * Returns price in GBP for a given distance in miles.
	 */
	double calculateFare(double parseDouble);

}
