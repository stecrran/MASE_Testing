package com.ait.stock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

class StockListenerTest {
	StockListener listener;
	Stock stock;
	private StockBroker stockBroker=mock(StockBroker.class);

	@BeforeEach
	public void setup() {
		listener = new StockListener(stockBroker);
		stock = new Stock("FDI", 100.0);
	}
	@Test
	void sellStocksWhenPriceGoesUp(){
		when(stockBroker.getQoute(stock)).thenReturn(150.00);
		listener.takeAction(stock);
		verify(stockBroker).sell(stock, 10);
	}
	@Test
	void buyStocksWhenPriceGoesDown() {
		//stock = new Stock("FDI", 100.0);
		when(stockBroker.getQoute(stock)).thenReturn(90.00);
		listener.takeAction(stock);
		verify(stockBroker, new Times(1)).buy(stock, 100);
	}

}
