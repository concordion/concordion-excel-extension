package spec.concordion.ext.excel;

public class BasicFareCalculator implements FareCalculator {

	double flatRate;
	double costPerMile1;
	double costPerMile1UpperLimit;
	double costPerMile2;
	
	public BasicFareCalculator(double flatRate,
			double costPerMile1,
			double costPerMile1UpperLimit, 
			double costPerMile2) {
		this.flatRate = flatRate;
		this.costPerMile1 = costPerMile1;
		this.costPerMile1UpperLimit = costPerMile1UpperLimit;
		this.costPerMile2 = costPerMile2;
	}

	@Override
	public double calculateFare(double distance) {
		return flatRate 
				+ Math.min(costPerMile1UpperLimit, distance) * costPerMile1
				+ Math.max(0, distance-costPerMile1UpperLimit) * costPerMile2;
	}

}
