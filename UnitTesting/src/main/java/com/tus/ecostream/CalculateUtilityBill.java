package com.tus.ecostream;

public class CalculateUtilityBill {

	private static final int ELECTRIC_TIER1 = 100;
	private static final int ELECTRIC_TIER3 = 500;
	private static final int WATER_TIER1 = 30;
	private static final int WATER_TIER3 = 80;
	private static final int GAS_TIER1 = 300;
	private static final int GAS_TIER3 = 500;

	private static final double ELECTRIC_RATE_TIER1 = 0.10;
	private static final double ELECTRIC_RATE_TIER2 = 0.12;
	private static final double ELECTRIC_RATE_TIER3 = 0.15;
	private static final double WATER_RATE_TIER1 = 1.50;
	private static final double WATER_RATE_TIER2 = 2.00;
	private static final double WATER_RATE_TIER3 = 2.50;
	private static final double GAS_RATE_TIER1 = 0.09;
	private static final double GAS_RATE_TIER2 = 0.11;
	private static final double GAS_RATE_TIER3 = 0.13;

	public Double calculateBill(int[] usages, boolean hasSolarPanels) {
		double total = 0;

		if (usages.length < 3 || usages.length > 3) {
			throw new IllegalArgumentException("Invalid number of usage values: expected 3, found " + usages.length);
		}

		if (usages[0] < 0) {
			throw new IllegalArgumentException("Usage cannot be negative: " + usages[0] + " KWh of electricity.");
		}

		else if (usages[1] < 0) {
			throw new IllegalArgumentException("Usage cannot be negative: " + usages[1] + " cubic meters of water.");
		}

		else if (usages[2] < 0) {
			throw new IllegalArgumentException("Usage cannot be negative: " + usages[2] + " cubic meters of gas.");
		}

		else {
			total = electricityCost(usages[0]) + waterCost(usages[1]) + gasCost(usages[2]);
		}

		if (hasSolarPanels) {
			total = total * 0.95;
		}

		return Math.round(total * 100.0) / 100.0;
	}

	private double electricityCost(int electricUnits) {
		double total = 0;

		if (electricUnits > ELECTRIC_TIER3) {
			total = (100 * ELECTRIC_RATE_TIER1) + (400 * ELECTRIC_RATE_TIER2)
					+ ((electricUnits - 500) * ELECTRIC_RATE_TIER3);
		}

		else if (electricUnits > ELECTRIC_TIER1) {
			total = (100 * ELECTRIC_RATE_TIER1) + ((electricUnits - 100) * ELECTRIC_RATE_TIER2);

		} else {
			total = electricUnits * ELECTRIC_RATE_TIER1;
		}

		return total;

	}

	private double waterCost(int waterUnits) {
		double total = 0;

		if (waterUnits > WATER_TIER3) {
			total = (30 * WATER_RATE_TIER1) + (50 * WATER_RATE_TIER2) + ((waterUnits - 80) * WATER_RATE_TIER3);
		}

		else if (waterUnits > WATER_TIER1) {
			total = (30 * WATER_RATE_TIER1) + ((waterUnits - 30) * WATER_RATE_TIER2);

		} else {
			total = waterUnits * WATER_RATE_TIER1;
		}

		return total;

	}

	private double gasCost(int gasUnits) {
		double total = 0;

		if (gasUnits > GAS_TIER3) {
			total = (300 * GAS_RATE_TIER1) + (200 * GAS_RATE_TIER2) + ((gasUnits - 500) * GAS_RATE_TIER3);
		}

		else if (gasUnits > GAS_TIER1) {
			total = (300 * GAS_RATE_TIER1) + ((gasUnits - 300) * GAS_RATE_TIER2);

		} else {
			total = gasUnits * GAS_RATE_TIER1;
		}

		return total;

	}

}
