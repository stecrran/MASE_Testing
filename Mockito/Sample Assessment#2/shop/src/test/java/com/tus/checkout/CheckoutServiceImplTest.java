package com.tus.checkout;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.tus.cart.Cart;
import com.tus.cart.CartItemDTO;
import com.tus.cart.Customer;
import com.tus.payment.CreditCardStrategy;
import com.tus.payment.PayPalStrategy;
import com.tus.services.InventoryService;
import com.tus.services.NotificationService;
import com.tus.payment.PaymentGateway;
import com.tus.payment.PaymentStrategy;
import com.tus.exception.CartException;
import com.tus.exception.InventoryException;
import com.tus.exception.PaymentException;
import com.tus.exception.CartEmptyException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javax.management.Notification;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.anyDouble;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.any;

public class CheckoutServiceImplTest {
	CheckoutServiceImpl checkoutServImpl;
	InventoryService inventoryService;
	NotificationService notificationService;
	PaymentGateway paymentGateway;
	PaymentStrategy paymentStrategy;
	CartItemDTO cartItemDTO;
	Cart cart;
	Cart cart1;
	Customer customer;
	final long CUSTOMER_ID = 123456L;
		

	@BeforeEach
	public void setUp() {
		inventoryService = mock(InventoryService.class);
		notificationService = mock(NotificationService.class);
		paymentGateway = mock(PaymentGateway.class);
		paymentStrategy = mock(PaymentStrategy.class);
		cartItemDTO = mock(CartItemDTO.class);
		cart = mock(Cart.class);
		cart1 = new Cart();
		customer = new Customer();
		checkoutServImpl = new CheckoutServiceImpl(inventoryService, notificationService, paymentGateway);
	}
	
	//Test 1 - 
	// Write a test that checks that CartEmptyException is thrown when there are no items added to the cart. 
	@Test
	public void testEmptyCartException() throws CartException {
		customer.setCart(cart);
		customer.setAccountNumber(CUSTOMER_ID);
		Throwable exception = assertThrows(CartEmptyException.class, () -> {checkoutServImpl.checkout(customer);});
		assertEquals("Cart is Empty : "+ CUSTOMER_ID, exception.getMessage());
		verify(paymentGateway, times(0)).processPayPal(anyDouble(), anyString(), anyString());
		verify(inventoryService, times(0)).deductItem(anyLong(), anyInt());
		verify(notificationService, times(0)).notifyOrderProcessed(any(Cart.class));
	}
	
	//Test 2 - Write a test that checks that InventoryException is thrown when inventory is not available for the one item in the cart. 
	@Test
	public void testInventoryNotAvailableException() throws PaymentException {
		customer.setCart(cart1);
		CartItemDTO item = new CartItemDTO(12312465L, "lemon", 1.23, 1);
		cart1.addItem(item);
		Throwable exception = assertThrows(InventoryException.class, () -> {checkoutServImpl.checkout(customer);});
		assertEquals("Product " + item.getProductName() + " is out of stock or doesn't have sufficient quantity.", exception.getMessage());
		verify(paymentGateway, times(0)).processPayPal(anyDouble(), anyString(), anyString());
		verify(inventoryService, times(0)).deductItem(anyLong(), anyInt());
		verify(notificationService, times(0)).notifyOrderProcessed(any(Cart.class));
	}
	
	//Test 3 - Write a test that checks that PaymentException is re-thrown when processing a PayPal payment.
	@Test
	public void testPayPalProcessingException() throws CartException {
		customer.setCart(cart1);
		CartItemDTO item = new CartItemDTO(12312465L, "lemon", 1.23, 1);
		cart1.addItem(item);
		when(inventoryService.isAvailable(anyLong(), anyInt())).thenReturn(true);
		PayPalStrategy newPayPal = new PayPalStrategy("jimmy@hotmail.com", "test123");
		doThrow(new PaymentException("string")).when(paymentGateway).processPayPal(1.23, "jimmy@hotmail.com", "test123");
		customer.setPaymentStrategy(newPayPal);
		customer.setPaymentType("PayPal");
		Throwable exception = assertThrows(PaymentException.class, () -> {checkoutServImpl.checkout(customer);});
		assertEquals("Error processing payment for customer Id "+ customer.getAccountNumber(), exception.getMessage());
		verify(paymentGateway, times(1)).processPayPal(1.23, "jimmy@hotmail.com", "test123");
		verify(inventoryService, times(0)).deductItem(anyLong(), anyInt());
		verify(notificationService, times(0)).notifyOrderProcessed(any(Cart.class));
	}
	
	//Test 4 - Write a test that checks that PaymentException is re-thrown when processing a Credit Card Payment.
	@Test
	public void testCreditCardProcessingException() throws CartException {
		customer.setCart(cart1);
		CartItemDTO item = new CartItemDTO(12312465L, "lemon", 1.23, 1);
		cart1.addItem(item);
		when(inventoryService.isAvailable(anyLong(), anyInt())).thenReturn(true);
		CreditCardStrategy newCreditCard = new CreditCardStrategy("131564", "01-Mar-2026", "555");
											//CreditCardStrategy(String cardNumber, String expiryDate, String cvv)
		
		doThrow(new PaymentException("string")).when(paymentGateway).processCreditCard(1.23, "131564", "01-Mar-2026", "555"); // this works
		// doThrow(new PaymentException("string")).when(paymentGateway).processCreditCard(1.23, "131564", "555", "01-Mar-2026"); // this doesn't work
																	// processCreditCard(double amount, String cardNumber, String cvv, String DateOfExpiry)
		customer.setPaymentStrategy(newCreditCard);
		customer.setPaymentType("CreditCard");
		Throwable exception = assertThrows(PaymentException.class, () -> {checkoutServImpl.checkout(customer);});
		assertEquals("Error processing payment for customer Id "+ customer.getAccountNumber(), exception.getMessage());
		// verify(paymentGateway, times(1)).processCreditCard(1.23, "131564", "555", "01-Mar-2026"); // doesn't work
		verify(paymentGateway, times(1)).processCreditCard(1.23, "131564", "01-Mar-2026", "555"); // does work
		verify(inventoryService, times(0)).deductItem(anyLong(), anyInt());
		verify(notificationService, times(0)).notifyOrderProcessed(any(Cart.class));
	}
		
	//Test 5
	/* Write a test that checks the correct interactions with the mocks for PaymentGateway 
	* (processPayPal), InventoryService (deductItem) and NotificationService (notifyOrderProcessed) 
	* when there is one item in the cart and payment type is paypal */
	@Test
	public void testPayPalOneCartItemSuccess() throws CartException {
		customer.setCart(cart1);
		CartItemDTO item = new CartItemDTO(12312465L, "lemon", 1.23, 1);
		cart1.addItem(item);	
		PayPalStrategy newPayPal = new PayPalStrategy("jimmy@hotmail.com", "test123");
		customer.setPaymentStrategy(newPayPal);
		customer.setPaymentType("PayPal");
		when(inventoryService.isAvailable(12312465L, 1)).thenReturn(true);
		checkoutServImpl.checkout(customer);
		verify(paymentGateway, times(1)).processPayPal(1.23, "jimmy@hotmail.com", "test123");
		verify(inventoryService, times(1)).deductItem(12312465L, 1);
		verify(notificationService, times(1)).notifyOrderProcessed(cart1);
	}
	
	//Test 6
	/* Write a test that checks the correct interactions with the mocks for PaymentGateway 
	* (processCrdeitCard), InventoryService (deductItem) and NotificationService (notifyOrderProcessed) 
	* when there is one item in the cart and payment type is credit card */
	@Test
	public void testCreditCardOneCartItemSuccess() throws CartException {
		customer.setCart(cart1);
		CartItemDTO item = new CartItemDTO(12312465L, "lemon", 1.23, 1);
		cart1.addItem(item);	
		CreditCardStrategy newCreditCard = new CreditCardStrategy("131564", "01-Mar-2026", "555");
		customer.setPaymentStrategy(newCreditCard);
		customer.setPaymentType("CreditCard");
		when(inventoryService.isAvailable(12312465L, 1)).thenReturn(true);
		checkoutServImpl.checkout(customer);
		verify(paymentGateway, times(1)).processCreditCard(1.23, "131564", "01-Mar-2026", "555"); 
		verify(inventoryService, times(1)).deductItem(12312465L, 1);
		verify(notificationService, times(1)).notifyOrderProcessed(cart1);	
	}
	
	// Test 7
	/* Write a test that checks the correct interactions with the mocks for PaymentGateway 
	* (processPayPal), InventoryService (deductItem) and NotificationService (notifyOrderProcessed) 
	* when there are two items in the cart and payment type is paypal */
	@Test 
	public void testPayPalTwoCartItemSuccess() throws CartException {
		customer.setCart(cart1);
		CartItemDTO item1 = new CartItemDTO(12312465L, "lemon", 1.23, 1);
		CartItemDTO item2 = new CartItemDTO(46545749L, "orange", 2.23, 1);
		cart1.addItem(item1);	
		cart1.addItem(item2);	
		PayPalStrategy newPayPal = new PayPalStrategy("jimmy@hotmail.com", "test123");
		customer.setPaymentStrategy(newPayPal);
		customer.setPaymentType("PayPal");
		when(inventoryService.isAvailable(12312465L, 1)).thenReturn(true);
		when(inventoryService.isAvailable(46545749L, 1)).thenReturn(true);
		checkoutServImpl.checkout(customer);
		verify(paymentGateway, times(1)).processPayPal(3.46, "jimmy@hotmail.com", "test123");
		verify(inventoryService, times(1)).deductItem(12312465L, 1);
		verify(inventoryService, times(1)).deductItem(46545749L, 1);
		verify(notificationService, times(1)).notifyOrderProcessed(cart1);		
	}
}
