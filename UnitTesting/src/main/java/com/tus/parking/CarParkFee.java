package com.tus.parking;

public class CarParkFee {

	private static final int MIN = 0;
	private static final int MAX = 24;
	private static final int LOW_HOURS = 5;
	private static final int MED_HOURS = 10;
	private static final int HI_HOURS = 24;

	public double calculateDailyFee(int numHours) throws IllegalArgumentException {
		double rate = 0.0;
		if (numHours < MIN || numHours > MAX) {
			throw new IllegalArgumentException("Incorrect number of hours: " + numHours + " Number must be 0 to 24.");
		}

		if (numHours <= LOW_HOURS) {
			rate = numHours * 2.0;
		} else if (numHours <= MED_HOURS) {
			rate = (numHours * 2.0) * 0.8;
			if (rate < 10.0) {
				rate = 10.0;
			}
		} else if (numHours <= HI_HOURS) {
			rate = 20;
		}
		return rate;
	}

	private static final int LOW_HOURS_WEEK = 20;
	private static final int HI_HOURS_WEEK = 50;

	public double calculateWeeklyFee(int[] hoursPerDay) throws IllegalArgumentException {
		int totalHours = 0;
		double fee = 0.0;

		if (hoursPerDay.length != 5) {
			throw new IllegalArgumentException(
					"Incorrect number of days: " + hoursPerDay + " Use values for all 5 days.");
		}

		for (int hours : hoursPerDay) {
			if (hours < MIN || hours > MAX) {
				throw new IllegalArgumentException("Incorrect number of hours: " + hours + " Number must be 0 to 24.");
			} else {
				totalHours += hours;
			}
		}

		if (totalHours <= LOW_HOURS_WEEK) {
			fee = 10.0;
		}
		else if (totalHours <= HI_HOURS_WEEK) {
			fee = 20.0;
		}
		else if (totalHours > HI_HOURS_WEEK) {
			fee = 30.0;
		}

		return fee;
	}

}
