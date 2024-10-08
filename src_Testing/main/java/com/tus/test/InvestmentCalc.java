package com.tus.test;

public class InvestmentCalc {

	public static double calculateInvestmentValue(int term, double startAmount) {
		double rate = 0;
		if (term >= 3 && term <= 10) {

			if (startAmount >= 1000 && startAmount < 3000) {
				rate = 0.02;
			} else if (startAmount >= 3000 && startAmount < 5000) {
				rate = 0.05;
			} else if (startAmount >= 5000 && startAmount <= 10000) {
				rate = 0.07;
			} else {
				throw new IllegalArgumentException("illegal investment amount: [" + startAmount + "]");
			}
			for (int i = 1; i <= term; i++) {
				startAmount += startAmount * rate;
			}
		} else {
			throw new IllegalArgumentException("illegal investment term: [" + term + "]");
		}
		return Math.round(startAmount * 100.0) / 100.0;

	}
	
	/* set rates as final and return to methods using getRate(double startAmount)
	private static final double RATE1=0.02;
	private static final double RATE2=0.05;
	private static final double RATE3=0.07;
	
	private static double getRate(double startAmount){
		double rate;
		if (startAmount<3000){
			rate=RATE1;
		}
		else if(startAmount<5000){
			rate=RATE2;
		}
		else{
			rate=RATE3;
		}
		return rate;
	}
	*/
}
