package com.tus.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

import com.tus.coffee.ProcessOrder;
import com.tus.coffee.ProcessOrderImpl;
import com.tus.dao.OrderDAO;
import com.tus.exception.CustomerNotFoundException;
import com.tus.exception.OrderDAOException;
import com.tus.exception.OrderEmptyException;
import com.tus.exception.OrderException;
import com.tus.exception.OrderNotFoundException;
import com.tus.exception.OrderPaymentException;
import com.tus.services.ApplePayHandler;
import com.tus.services.Barista;
import com.tus.services.Invoicer;

public class ProcessOrderImplTestEx {
	Customer customer;
	Order order;
	Product latte;
	Product tea;
	
	ProcessOrderImpl processOrderImpl;
	private final Barista barista = mock(Barista.class);
	private final Invoicer invoicer = mock(Invoicer.class);
	private final OrderDAO orderDAO = mock(OrderDAO.class);
	private final ApplePayHandler applePay = mock(ApplePayHandler.class);
	private final long CUSTOMER_ID = 123456L; 
	

	@BeforeEach
	public void setUp() {
		customer = new Customer();
		order = new Order();
		latte = new Product("latte", 4, "2416");
		tea = new Product("tea", 3, "2417");
		customer.setAccountNumber(CUSTOMER_ID);
		customer.setName("Jimmy");
		customer.setEmail("jimmy@hotmail.com");
		customer.setAddress("Athlone");

		// MAKE SURE YOU INSTANTIATE THE TESTING CLASS **AFTER** DECLARING ALL DEPENDENCIES
		processOrderImpl = new ProcessOrderImpl(barista, invoicer, orderDAO, applePay); 
	}

	
	@Test
	void testCustomerNotFoundException() throws OrderPaymentException {
		// Write a test that checks that CustomerNotFoundException is thrown when customer object received from OrderDAO is null. 
		Throwable exception = assertThrows(CustomerNotFoundException.class, ()
				-> {processOrderImpl.processOrder(CUSTOMER_ID);}); // it may be better to use a set value here
		
		// Test that the correct message is received in the exception.
		assertEquals("Unknown Customer: " + 123456, exception.getMessage()); // anyLong() would generates a '0' value for customer
		
		// Verify that there is no interaction with the mocks for ApplePayHandler, invoicer or Barista.
		verify(applePay, times(0)).pay(anyString(), anyString(), anyDouble()); // ensure method throws any exceptions
		verify(invoicer, times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble()); 
		verify(barista, times(0)).prepareItem(anyString(), anyInt(), anyString()); 
		
	}
	
	@Test
	void testOrderDAOSQLException() throws SQLException, OrderPaymentException {
		when(orderDAO.findCustomerForId(CUSTOMER_ID)).thenThrow(SQLException.class);
		Throwable exception = assertThrows(OrderDAOException.class, ()
				-> {processOrderImpl.processOrder(CUSTOMER_ID);});
		
		assertEquals("Error connecting to database: " + 123456, exception.getMessage()); // 
		verify(applePay, times(0)).pay(anyString(), anyString(), anyDouble()); 
		verify(invoicer, times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble()); 
		verify(barista, times(0)).prepareItem(anyString(), anyInt(), anyString()); 
	}
	
	@Test
	void testOrderNotFoundException() throws SQLException, OrderPaymentException {
		when(orderDAO.findCustomerForId(CUSTOMER_ID)).thenReturn(customer); // it's OrderDAO.class that returns a customer Object, not Customer.class
		Throwable exception = assertThrows(OrderNotFoundException.class, ()
				-> {processOrderImpl.processOrder(CUSTOMER_ID);});
		
		assertEquals("No order found: "+ CUSTOMER_ID, exception.getMessage()); // 
		verify(applePay, times(0)).pay(anyString(), anyString(), anyDouble()); 
		verify(invoicer, times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble()); 
		verify(barista, times(0)).prepareItem(anyString(), anyInt(), anyString()); 	
	}
	
	@Test
	void testOrderEmptyException() throws SQLException, OrderPaymentException {
		customer.setOrder(order); // set Order - nothing in "order"
		when(orderDAO.findCustomerForId(CUSTOMER_ID)).thenReturn(customer);
		
		Throwable exception = assertThrows(OrderEmptyException.class, ()
				-> {processOrderImpl.processOrder(CUSTOMER_ID);});
		assertEquals("Cart is empty: " + CUSTOMER_ID, exception.getMessage());
		verify(applePay, times(0)).pay(anyString(), anyString(), anyDouble()); 
		verify(invoicer, times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble()); 
		verify(barista, times(0)).prepareItem(anyString(), anyInt(), anyString()); 	
		
	}
	
	// Write a test that checks the correct interactions with the mocks ApplePayHandler, Invoicer, or Barista 
	// (including actual values) when there is one item (with a quantity of one) in the list of orders.
	@Test
	void testOneItemInOrder() throws OrderException, SQLException {
		/*processOrder() => completeOrder() (if customer not null) => 
		 * final ArrayList<OrderItem> orderItems = (ArrayList<OrderItem>) order.getOrderItems(); in completeOrder() (if order not null) 
		 * => processPayment(invoiceNumber, customer.getEmail(), orderTotal) in completeOrder() => applePayHandler.pay(invoiceNumber, customerEmailAddress, orderTotal);
		 */
		order.addItem(latte, 1);
		customer.setOrder(order);
		// need to return customer
		when(orderDAO.findCustomerForId(CUSTOMER_ID)).thenReturn(customer);
		// need an InvoiceID
		when(invoicer.invoiceCustomer(CUSTOMER_ID, customer.getEmail(), 4.00)).thenReturn("INV00123"); // return an InvoiceID
		// MAKE SURE ALL VARIABLES PASSED IN - MATCH EXACTLY. CREATE GLOBAL VARIABLES AND PASS IN VARIABLE NAMES.
		processOrderImpl.processOrder(CUSTOMER_ID); // need to call this method to evaluate Order for Customer with real customer ID
		verify(applePay, times(1)).pay("INV00123", customer.getEmail(), 4.00);
		verify(invoicer, times(1)).invoiceCustomer(CUSTOMER_ID, customer.getEmail(), 4.00);
		verify(barista, times(1)).prepareItem("2416", 1, customer.getName()); 	
		}

	@Test
	void testTwoItemsInOrder() throws OrderException, SQLException {
		order.addItem(latte, 1);
		order.addItem(tea, 1);
		customer.setOrder(order);

		when(orderDAO.findCustomerForId(CUSTOMER_ID)).thenReturn(customer);
		when(invoicer.invoiceCustomer(CUSTOMER_ID, customer.getEmail(), 7.00)).thenReturn("INV00123"); 
		processOrderImpl.processOrder(CUSTOMER_ID); // 
		verify(applePay, times(1)).pay("INV00123", customer.getEmail(), 7.00);
		verify(invoicer, times(1)).invoiceCustomer(CUSTOMER_ID, customer.getEmail(), 7.00);
		verify(barista, times(1)).prepareItem("2416", 1, customer.getName()); 
		verify(barista, times(1)).prepareItem("2417", 1, customer.getName()); 
	}
	
	@Test
	void testOneTeaTwoCoffeeItemsInOrder() throws OrderException, SQLException {
		order.addItem(latte, 2);
		order.addItem(tea, 1);
		customer.setOrder(order);

		when(orderDAO.findCustomerForId(CUSTOMER_ID)).thenReturn(customer);
		when(invoicer.invoiceCustomer(CUSTOMER_ID, customer.getEmail(), 11.00)).thenReturn("INV00123"); 
		processOrderImpl.processOrder(CUSTOMER_ID); 
		verify(applePay, times(1)).pay("INV00123", customer.getEmail(), 11.00);
		verify(invoicer, times(1)).invoiceCustomer(CUSTOMER_ID, customer.getEmail(), 11.00);
		verify(barista, times(1)).prepareItem("2416", 2, customer.getName()); 
		verify(barista, times(1)).prepareItem("2417", 1, customer.getName()); 
	}
	
	// Write a test that checks the correct interactions with the mocks ApplePayHandler, Invoicer or Barista 
	// when ApplePayHandler throws OrderPaymentException. Check that OrderPaymentException is received with the correct values.
	@Test
	void testOrderPaymentException() throws SQLException, OrderException {
		order.addItem(tea, 1);
		customer.setOrder(order);
		when(orderDAO.findCustomerForId(CUSTOMER_ID)).thenReturn(customer);
		when(invoicer.invoiceCustomer(CUSTOMER_ID, customer.getEmail(), 3.00)).thenReturn("INV00123"); 
		
		doThrow(OrderPaymentException.class).when(applePay).pay(anyString(), anyString(), anyDouble());

		Throwable exception = assertThrows(OrderPaymentException.class, () -> {processOrderImpl.processOrder(CUSTOMER_ID);});
		assertEquals("Problem with payment: "+ CUSTOMER_ID, exception.getMessage());
		verify(applePay, times(1)).pay("INV00123", customer.getEmail(), 3.00);
		verify(invoicer, times(1)).invoiceCustomer(CUSTOMER_ID, customer.getEmail(), 3.00);
		verify(barista, times(0)).prepareItem(anyString(), anyInt(), anyString());
	}


}
