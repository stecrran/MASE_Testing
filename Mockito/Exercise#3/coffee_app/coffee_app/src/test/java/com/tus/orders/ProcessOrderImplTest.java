package com.tus.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;

import java.sql.SQLException;
import com.tus.dao.OrderDAO;
import com.tus.services.ApplePayHandler;
import com.tus.services.Invoicer;
import com.tus.services.Barista;
import com.tus.coffee.ProcessOrderImpl;
import com.tus.exception.OrderDAOException;
import com.tus.exception.OrderException;
import com.tus.exception.OrderNotFoundException;
import com.tus.exception.OrderPaymentException;
import com.tus.exception.CustomerNotFoundException;
import com.tus.exception.OrderEmptyException;

public class ProcessOrderImplTest {
	 private ProcessOrderImpl processOrderImpl;
	 private final Invoicer invoicer = mock(Invoicer.class);
	 private final Barista barista = mock(Barista.class);
	 private OrderDAO orderDAO = mock(OrderDAO.class);
	 private final ApplePayHandler applePayHandler = mock(ApplePayHandler.class);
	 static final long ACCOUNT_NUMBER = 123456L;

	@BeforeEach
	public void setUp() {
		processOrderImpl = new ProcessOrderImpl(barista, invoicer, orderDAO, applePayHandler);
	}
	
//Test 1
	@Test
	void testCustomerNotFoundException() throws OrderException {
		Throwable exception = assertThrows(CustomerNotFoundException.class, () -> 
		{processOrderImpl.processOrder(ACCOUNT_NUMBER);});	
		assertEquals("Unknown Customer: " + ACCOUNT_NUMBER, exception.getMessage());
	}
	
//Test 2
	@Test
	public void testOrderDAOSQLException() throws OrderException, SQLException {
		
	}
	//Test 3
	@Test
	public void testOrderNotFoundException() throws OrderException, SQLException {
		
	}
//Test 4
	@Test
	public void testOrderEmptyException() throws OrderException, SQLException {
		
	}

	
//Test5
	@Test
	public void testOneItemInOrder() throws OrderException, SQLException {
		
	}  
//Test6
	@Test
	public void testTwoItemsInOrder() throws OrderException, SQLException {
		
	}
//Test 7
	@Test
	public void testOneTeaTwoCoffeeItemsInOrder() throws OrderException, SQLException {
		
	}
	
//Test8
	@Test
	public void testOrderPaymentException() throws OrderException, SQLException {
			
	}  

}
