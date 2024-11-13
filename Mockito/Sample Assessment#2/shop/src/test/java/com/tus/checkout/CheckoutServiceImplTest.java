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

public class CheckoutServiceImplTest {
	

	@BeforeEach
	public void setUp() {
		
	}
	//Test 1
	@Test
	public void testEmptyCartException() throws CartException {
		
	}
/	/Test 2
	@Test
	public void testInventoryNotAvailableException() throws CartException {
		
	}
	
	//Test 3
	@Test
	public void testPayPalProcessingException() throws CartException {
		
	}
	
	//Test 4
	@Test
	public void testCreditCardProcessingException() throws CartException {
			
	}
		
	//Test 5
	@Test
	public void testPayPalOneCartItemSuccess() throws CartException {
					
	}
	//Test 6
	@Test
	public void testCreditCardOneCartItemSuccess() throws CartException {
					
	}
				
	@Test
	public void testPayPalTwoCartItemSuccess() throws CartException {
					
	}
}
