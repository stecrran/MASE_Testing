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
import java.util.ArrayList;

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

public class ProcessOrderImpl_Test {
	
	private ProcessOrderImpl processOrderImpl;
	private OrderDAO orderDAO;
	private Customer customer;
	private Order order;
	ArrayList<OrderItem> orderItems;
	private Invoicer invoicer;
	private ApplePayHandler applePay;
	private Barista barista;
	private Product lemonTea = new Product("Lemon Tea", 4, "TT123");
	private final long CUSTOMER_ID = 123456L;

	@BeforeEach
	public void setUp() {
		orderDAO = mock(OrderDAO.class);
		customer = new Customer();
		order = new Order();
		orderItems = new ArrayList<OrderItem>();
		invoicer = mock(Invoicer.class);
		applePay = mock(ApplePayHandler.class);
		barista = mock(Barista.class);
		processOrderImpl = new ProcessOrderImpl(barista, invoicer, orderDAO, applePay);
	}
	
//Test 1
	@Test
	public void testCustomerNotFoundException() throws CustomerNotFoundException,OrderException {
		
		Throwable exception = assertThrows(CustomerNotFoundException.class, () -> {processOrderImpl.processOrder(CUSTOMER_ID);});
		assertEquals("Unknown Customer: "+ CUSTOMER_ID, exception.getMessage());
		verify(applePay, times(0)).pay(anyString(), anyString(), anyDouble());
		verify(invoicer, times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble());
		verify(barista, times(0)).prepareItem(anyString(), anyInt(), anyString());
	}
	
//Test 2
	@Test
	public void testOrderDAOSQLException() throws OrderException, SQLException {
		doThrow(SQLException.class).when(orderDAO).findCustomerForId(CUSTOMER_ID);
		Throwable exception = assertThrows(OrderDAOException.class, () -> {processOrderImpl.processOrder(CUSTOMER_ID);});
		assertEquals("Error connecting to database: "+ CUSTOMER_ID, exception.getMessage());
		verify(applePay, times(0)).pay(anyString(), anyString(), anyDouble());
		verify(invoicer, times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble());
		verify(barista, times(0)).prepareItem(anyString(), anyInt(), anyString());
	}
	
	//Test 3
	@Test
	public void testOrderNotFoundException() throws OrderException, SQLException {
		when(orderDAO.findCustomerForId(CUSTOMER_ID)).thenReturn(customer);
		//customer.setOrder(order); // this broke it, there should be null order to get to OrderNotFoundException
		customer.setAccountNumber(CUSTOMER_ID);
		
		Throwable exception = assertThrows(OrderNotFoundException.class, () -> {processOrderImpl.processOrder(CUSTOMER_ID);});
		assertEquals("No order found: "+ CUSTOMER_ID, exception.getMessage());
		verify(applePay, times(0)).pay(anyString(), anyString(), anyDouble());
		verify(invoicer, times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble());
		verify(barista, times(0)).prepareItem(anyString(), anyInt(), anyString());
	}
	
//Test 4
	@Test
	public void testOrderEmptyException() throws OrderException, SQLException {
		when(orderDAO.findCustomerForId(CUSTOMER_ID)).thenReturn(customer);
		customer.setOrder(order);
		customer.setAccountNumber(CUSTOMER_ID);
		
		Throwable exception = assertThrows(OrderEmptyException.class, () -> {processOrderImpl.processOrder(CUSTOMER_ID);});
		assertEquals("Cart is empty: "+ CUSTOMER_ID, exception.getMessage());
		verify(applePay, times(0)).pay(anyString(), anyString(), anyDouble());
		verify(invoicer, times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble());
		verify(barista, times(0)).prepareItem(anyString(), anyInt(), anyString());
	}

	
//Test5
	@Test
	public void testOneItemInOrder() throws OrderException, SQLException {
		
		order.addItem(lemonTea, 1);
		customer.setOrder(order);
		customer.setAccountNumber(CUSTOMER_ID);
		customer.setEmail("john@gmail.com"); // !!!!don't forget this stuff!!!
		
		OrderItem lemon = new OrderItem(lemonTea, 1);

		//orderItems.add(lemon);
		when(orderDAO.findCustomerForId(CUSTOMER_ID)).thenReturn(customer);
		when(invoicer.invoiceCustomer(CUSTOMER_ID, "john@gmail.com", 4.0)).thenReturn("INV0123");
		processOrderImpl.processOrder(CUSTOMER_ID);
		verify(applePay, times(1)).pay("INV0123", "john@gmail.com", 4.0);
		//verify(invoicer, times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble());
		//verify(barista, times(0)).prepareItem(anyString(), anyInt(), anyString());
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
