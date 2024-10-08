package com.ait.cars;

public interface Car {
	boolean needsFuel();
	double getEngineTemperature();
	void driveTo(String destination);
}
