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
import com.tus.exception.CartException;
import com.tus.exception.InventoryException;
import com.tus.exception.PaymentException;
import com.tus.exception.CartEmptyException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.anyDouble;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.any;

public class CheckoutServiceTest {
	
	private CheckoutServiceImpl checkoutServiceImpl;
	private Customer customer;
	private Cart cart;
	private InventoryService inventoryService;
	private PaymentGateway paymentGateway;
	private NotificationService notificationService;
	private final long CUSTOMER_ID = 123456L;

	@BeforeEach
	public void setUp() {
		customer = new Customer();
		cart = new Cart();
		inventoryService = mock(InventoryService.class);
		paymentGateway = mock(PaymentGateway.class);
		notificationService = mock(NotificationService.class);
		checkoutServiceImpl = new CheckoutServiceImpl(inventoryService, notificationService, paymentGateway);
		customer.setCart(cart);
		
		customer.setAccountNumber(CUSTOMER_ID);
	}
	
	//Test 1
	@Test
	public void testEmptyCartException() throws CartException {
		customer.setPaymentType("PayPal");
		Throwable exception = assertThrows(CartEmptyException.class, () -> {checkoutServiceImpl.checkout(customer);});
		assertEquals("Cart is Empty : " + CUSTOMER_ID, exception.getMessage());
		
		verify(paymentGateway, times(0)).processPayPal(anyLong(), anyString(), anyString());
		verify(inventoryService, times(0)).deductItem(anyLong(), anyInt());
		verify(notificationService, times(0)).notifyOrderProcessed(cart);
	}
	
	//Test 2
	@Test
	public void testInventoryNotAvailableException() throws CartException {
		CartItemDTO item1 = new CartItemDTO(55664L, "Lemons", 1.26, 1);
		cart.addItem(item1);
		Throwable exception = assertThrows(InventoryException.class, () -> {checkoutServiceImpl.checkout(customer);});
		assertEquals("Product " + item1.getProductName() + " is out of stock or doesn't have sufficient quantity.", exception.getMessage());
		
		verify(paymentGateway, times(0)).processPayPal(anyLong(), anyString(), anyString());
		verify(inventoryService, times(0)).deductItem(anyLong(), anyInt());
		verify(notificationService, times(0)).notifyOrderProcessed(cart);
	}
	
	//Test 3
	@Test
	public void testPayPalProcessingException() throws CartException {
		CartItemDTO item1 = new CartItemDTO(55664L, "Lemons", 1.26, 1);
		cart.addItem(item1);
		customer.setPaymentType("PayPal");
		when(inventoryService.isAvailable(55664L, 1)).thenReturn(true);
		PayPalStrategy paypalPayment = new PayPalStrategy("john@gmail.com", "test123");
		customer.setPaymentStrategy(paypalPayment);
		doThrow(PaymentException.class).when(paymentGateway).processPayPal(1.26, "john@gmail.com", "test123");
		Throwable exception = assertThrows(PaymentException.class, () -> {checkoutServiceImpl.checkout(customer);});
		assertEquals("Error processing payment for customer Id " + customer.getAccountNumber(), exception.getMessage());
		
		verify(paymentGateway, times(1)).processPayPal(1.26, "john@gmail.com", "test123");
		verify(inventoryService, times(0)).deductItem(anyLong(), anyInt());
		verify(notificationService, times(0)).notifyOrderProcessed(cart);
	}
	
	//Test 4
	@Test
	public void testCreditCardProcessingException() throws CartException {
		CartItemDTO item1 = new CartItemDTO(55664L, "Lemons", 1.26, 1);
		cart.addItem(item1);
		customer.setPaymentType("CreditCard");
		when(inventoryService.isAvailable(55664L, 1)).thenReturn(true);
		CreditCardStrategy ccardPayment = new CreditCardStrategy("5151512", "03-Jun-2027", "123");
		customer.setPaymentStrategy(ccardPayment);
		
		doThrow(PaymentException.class).when(paymentGateway).processCreditCard(1.26, "5151512", "03-Jun-2027", "123");
		Throwable exception = assertThrows(PaymentException.class, () -> {checkoutServiceImpl.checkout(customer);});
		assertEquals("Error processing payment for customer Id " + customer.getAccountNumber(), exception.getMessage());
		
		verify(paymentGateway, times(1)).processCreditCard(1.26, "5151512", "03-Jun-2027", "123");
		verify(inventoryService, times(0)).deductItem(anyLong(), anyInt());
		verify(notificationService, times(0)).notifyOrderProcessed(cart);
	}
		
	//Test 5
	@Test
	public void testPayPalOneCartItemSuccess() throws CartException {
		CartItemDTO item1 = new CartItemDTO(55664L, "Lemons", 1.26, 1);
		cart.addItem(item1);
		customer.setPaymentType("PayPal");
		PayPalStrategy paypalPayment = new PayPalStrategy("john@gmail.com", "test123");
		customer.setPaymentStrategy(paypalPayment);
		when(inventoryService.isAvailable(55664L, 1)).thenReturn(true);
//		paymentGateway.processPayPal(1.26, "john@gmail.com", "test123"); // don't do this. It's calling the methods 
//		inventoryService.deductItem(55664L, 1); // so that they can be verified later.
//		notificationService.notifyOrderProcessed(cart); // You need to call the actual checkout() method and verify that the method flow through

		checkoutServiceImpl.checkout(customer);
		verify(paymentGateway, times(1)).processPayPal(1.26, "john@gmail.com", "test123");
		verify(inventoryService, times(1)).deductItem(55664L, 1);
		verify(notificationService, times(1)).notifyOrderProcessed(cart);
	}
	//Test 6
	@Test
	public void testCreditCardOneCartItemSuccess() throws CartException {
		CartItemDTO item1 = new CartItemDTO(55664L, "Lemons", 1.26, 1);
		cart.addItem(item1);
		customer.setPaymentType("CreditCard");
		CreditCardStrategy ccardPayment = new CreditCardStrategy("5151512", "03-Jun-2027", "123");
		customer.setPaymentStrategy(ccardPayment);
		when(inventoryService.isAvailable(55664L, 1)).thenReturn(true);

		checkoutServiceImpl.checkout(customer);
		verify(paymentGateway, times(1)).processCreditCard(1.26, "5151512", "03-Jun-2027", "123");
		verify(inventoryService, times(1)).deductItem(55664L, 1);
		verify(notificationService, times(1)).notifyOrderProcessed(cart);	
	}
				
	@Test
	public void testPayPalTwoCartItemSuccess() throws CartException {
		CartItemDTO item1 = new CartItemDTO(55664L, "Lemons", 1.26, 1);
		CartItemDTO item2 = new CartItemDTO(99887L, "Oranges", 1.33, 1);
		cart.addItem(item1);
		cart.addItem(item2);
		customer.setPaymentType("PayPal");
		PayPalStrategy paypalPayment = new PayPalStrategy("john@gmail.com", "test123");
		customer.setPaymentStrategy(paypalPayment);
		when(inventoryService.isAvailable(55664L, 1)).thenReturn(true);
		when(inventoryService.isAvailable(99887L, 1)).thenReturn(true);

		checkoutServiceImpl.checkout(customer);
		verify(paymentGateway, times(1)).processPayPal(2.59, "john@gmail.com", "test123");
		verify(inventoryService, times(1)).deductItem(55664L, 1);
		verify(inventoryService, times(1)).deductItem(99887L, 1);
		verify(notificationService, times(1)).notifyOrderProcessed(cart);	
	}
}
