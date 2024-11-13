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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	Customer customer;
	Order order;
	Product latte;
	Product tea;
	
	ProcessOrderImpl processOrderImplTest;
	private final Barista barista = mock(Barista.class);
	private final Invoicer invoicer = mock(Invoicer.class);
	private final OrderDAO orderDAO = mock(OrderDAO.class);
	private final ApplePayHandler applePayHandler = mock(ApplePayHandler.class);
	private final long ACCOUNT_NUMBER = 123456L; 
	

	@BeforeEach
	public void setUp() {
		customer = new Customer();
		order = new Order();
		latte = new Product("latte", 4, "2416");
		tea = new Product("tea", 3, "2417");
		customer.setAccountNumber(ACCOUNT_NUMBER);
		customer.setName("Jimmy");
		customer.setEmail("jimmy@hotmail.com");
		customer.setAddress("Athlone");
		processOrderImplTest = new ProcessOrderImpl(barista, invoicer, orderDAO, applePayHandler);
	}
//Test 1
	@Test
	void testCustomerNotFoundException() throws OrderException {
		Throwable exception = assertThrows(CustomerNotFoundException.class, () ->
		{processOrderImplTest.processOrder(ACCOUNT_NUMBER);}); 	// testing that Account Number received from OrderDAO is null. This Account Number is not received from OrderDAO. 
																// You need to use a final instance variable (instead of a directly inputed value as argument) as the 
																// ProcessOrderImpl constructor assigns the argument as the Customer Number
		assertEquals("Unknown Customer: "+ ACCOUNT_NUMBER, exception.getMessage());
		verify(applePayHandler, new Times(0)).pay(anyString(),anyString(), anyDouble());
		verify(invoicer, new Times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble());
		verify(barista, new Times(0)).prepareItem(anyString(), anyInt(), anyString());
		
	}
	
//Test 2
	@Test
	void testOrderDAOSQLException() throws OrderException, SQLException {
		when(orderDAO.findCustomerForId(ACCOUNT_NUMBER)).thenThrow(SQLException.class);
		Throwable exception = assertThrows(OrderDAOException.class, () ->
		{processOrderImplTest.processOrder(ACCOUNT_NUMBER);}); 	
																
		assertEquals("Error connecting to database: "+ ACCOUNT_NUMBER, exception.getMessage());
		verify(applePayHandler, new Times(0)).pay(anyString(),anyString(), anyDouble());
		verify(invoicer, new Times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble());
		verify(barista, new Times(0)).prepareItem(anyString(), anyInt(), anyString());
	}
	
	//Test 3
	@Test
	void testOrderNotFoundException() throws OrderException, SQLException {
		// this is used to return customer. If it wasn't here processOrder(customerAccountId) would only look at customerAccountID and return CustomerNotFoundException for assertThrows
		when(orderDAO.findCustomerForId(ACCOUNT_NUMBER)).thenReturn(customer); 	// orderDAO.findCustomerForId has a Customer return type so it will return a Customer
																				// even though it is an unimplemented Interface
		
		// processOrder() calls completeOrder() if customer is not null: customer = new Customer();
		// completeOrder throws OrderNotFoundException if order is null. order is null, no value has been assigned.
		Throwable exception = assertThrows(OrderNotFoundException.class, () ->
		{processOrderImplTest.processOrder(ACCOUNT_NUMBER);}); 	
																
		assertEquals("No order found: " + ACCOUNT_NUMBER, exception.getMessage());
		verify(applePayHandler, new Times(0)).pay(anyString(),anyString(), anyDouble());
		verify(invoicer, new Times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble());
		verify(barista, new Times(0)).prepareItem(anyString(), anyInt(), anyString());
	}
	
//Test 4
	@Test
	void testOrderEmptyException() throws OrderException, SQLException {
		customer.setOrder(order);
		when(orderDAO.findCustomerForId(ACCOUNT_NUMBER)).thenReturn(customer); 
		
		Throwable exception = assertThrows(OrderEmptyException.class, () ->
		{processOrderImplTest.processOrder(ACCOUNT_NUMBER);}); 
		
		assertEquals("Cart is empty: " + ACCOUNT_NUMBER, exception.getMessage());
		verify(applePayHandler, new Times(0)).pay(anyString(),anyString(), anyDouble());
		verify(invoicer, new Times(0)).invoiceCustomer(anyLong(), anyString(), anyDouble());
		verify(barista, new Times(0)).prepareItem(anyString(), anyInt(), anyString());
	}

	
//Test5
	@Test
	void testOneItemInOrder() throws OrderException, SQLException {
		order.addItem(latte, 1);
		customer.setOrder(order);
		when(orderDAO.findCustomerForId(ACCOUNT_NUMBER)).thenReturn(customer);
		when(invoicer.invoiceCustomer(ACCOUNT_NUMBER, "jimmy@hotmail.com", 4.0)).thenReturn("INV0123");
		processOrderImplTest.processOrder(ACCOUNT_NUMBER);
		verify(applePayHandler, new Times(1)).pay("INV0123", "jimmy@hotmail.com", 4.0);
		verify(invoicer, new Times(1)).invoiceCustomer(123456, "jimmy@hotmail.com", 4.0);
		verify(barista, new Times(1)).prepareItem("2416", 1, "Jimmy");
	}  
	
//Test6
	@Test
	void testTwoItemsInOrder() throws OrderException, SQLException {
		order.addItem(latte, 1);
		order.addItem(tea, 1);
		customer.setOrder(order);
		when(orderDAO.findCustomerForId(ACCOUNT_NUMBER)).thenReturn(customer);
		when(invoicer.invoiceCustomer(ACCOUNT_NUMBER, "jimmy@hotmail.com", 7.0)).thenReturn("INV0123");
		processOrderImplTest.processOrder(ACCOUNT_NUMBER);
		verify(applePayHandler, new Times(1)).pay("INV0123", "jimmy@hotmail.com", 7.0);
		verify(invoicer, new Times(1)).invoiceCustomer(123456, "jimmy@hotmail.com", 7.0);
		verify(barista, new Times(1)).prepareItem("2416", 1, "Jimmy");
		verify(barista, new Times(1)).prepareItem("2417", 1, "Jimmy");
	}
	
//Test 7
	@Test
	void testOneTeaTwoCoffeeItemsInOrder() throws OrderException, SQLException {
		order.addItem(latte, 2);
		order.addItem(tea, 1);
		customer.setOrder(order);
		when(orderDAO.findCustomerForId(ACCOUNT_NUMBER)).thenReturn(customer);
		when(invoicer.invoiceCustomer(ACCOUNT_NUMBER, "jimmy@hotmail.com", 11.0)).thenReturn("INV0123");
		processOrderImplTest.processOrder(ACCOUNT_NUMBER);
		verify(applePayHandler, new Times(1)).pay("INV0123", "jimmy@hotmail.com", 11.0);
		verify(invoicer, new Times(1)).invoiceCustomer(123456, "jimmy@hotmail.com", 11.0);
		verify(barista, new Times(1)).prepareItem("2416", 2, "Jimmy");
		verify(barista, new Times(1)).prepareItem("2417", 1, "Jimmy");
	}
	
//Test8
	@Test
	void testOrderPaymentException() throws OrderException, SQLException {
		order.addItem(tea, 1);
		customer.setOrder(order);
		when(orderDAO.findCustomerForId(ACCOUNT_NUMBER)).thenReturn(customer);
		when(invoicer.invoiceCustomer(ACCOUNT_NUMBER, "jimmy@hotmail.com", 3.0)).thenReturn("INV0123");
		doThrow(OrderPaymentException.class).when(applePayHandler).pay("INV0123", "jimmy@hotmail.com", 3.0);
		Throwable exception = assertThrows(OrderPaymentException.class, () -> 
		{processOrderImplTest.processOrder(ACCOUNT_NUMBER);});
		assertEquals("Problem with payment: " + ACCOUNT_NUMBER, exception.getMessage());
		verify(applePayHandler, new Times(1)).pay("INV0123", "jimmy@hotmail.com", 3.0);
		verify(invoicer, new Times(1)).invoiceCustomer(123456, "jimmy@hotmail.com", 3.0);
		verify(barista, new Times(0)).prepareItem(anyString(), anyInt(), anyString());

	}  

}
