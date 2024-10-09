package com.ait.stock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StockListenerTest {
	private StockBroker broker1 = mock(StockBroker.class);
	private Stock stock1 = new Stock("123", 15.38);

	@Before
	public void setup() {
		StockListener stockListener = new StockListener(broker1);
	}

//	@Test
//	public void sellStockWhenPriceGoesUp() {
//		when(stock1.boughtAt())
//	}

	@Test
	public void buyStocksWhenPriceGoesDown() {

	}
}
