package com.tus.retailers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BiggestBazarRetailTest {
	
	private Inventory inventory;
	private PublicAddressSystem pas;
	private BiggestBazarRetail biggestBazar;
	private ArrayList<Item> items;
	
	private final static double DISCOUNT_RATE = 0.2;
	private final static double LEMONCAKE1_PRICE = 13.29;
	private final static double LEMONCAKE1_BASE_PRICE = 13.00;
	private final static double LEMONCAKE2_PRICE = 12.99;
	private final static double LEMONCAKE2_BASE_PRICE = 6.45;
	private final static double LEMONCAKE3_PRICE = 11.49;
	private final static double LEMONCAKE3_BASE_PRICE = 6.45;
	
	
	@BeforeEach
	public void setup() {
		inventory = mock(Inventory.class);
		pas = mock(PublicAddressSystem.class);
		biggestBazar = new BiggestBazarRetail(inventory, pas);
		items = new ArrayList<>();
	}
	
	@Test
	public void testDiscountIssuedOnOneItem() {
		Item lemonCake = new Item("131465", "Lemon Cake", LEMONCAKE2_PRICE, LEMONCAKE2_BASE_PRICE);
		items.add(lemonCake);
		
		when(inventory.getItemsExpireInAMonth()).thenReturn(items);
		when(inventory.itemsUpdated()).thenReturn(1);
		
		assertEquals(1, biggestBazar.issueDiscountForItemsExpireIn30Days(DISCOUNT_RATE));
		verify(inventory, times(1)).update(lemonCake, 10.392); // use actual object and values here
		
		//Offer lemonCakeOffer = new Offer(lemonCake, 10.392);  // this produces a new Offer object 
		verify(pas, times(1)).announce(isA(Offer.class)); // the new offer object is not going to be the same as the object created from issueDiscountForItemsExpireIn30Days

	}
	
	@Test
	public void testDiscountNotIssued() {
		Item lemonCake = new Item("131465", "Lemon Cake", LEMONCAKE1_PRICE, LEMONCAKE1_BASE_PRICE);
		items.add(lemonCake);
		
		when(inventory.getItemsExpireInAMonth()).thenReturn(items);
		when(inventory.itemsUpdated()).thenReturn(0);
		
		assertEquals(0, biggestBazar.issueDiscountForItemsExpireIn30Days(DISCOUNT_RATE));
		verify(inventory, times(0)).update(lemonCake, 10.392);
		
		verify(pas, times(0)).announce(isA(Offer.class));
	}
	
	@Test
	public void testThreeItemsDiscounted() {
		Item lemonCake1 = new Item("131465", "Lemon Cake Blond", LEMONCAKE1_PRICE, LEMONCAKE1_BASE_PRICE);
		Item lemonCake2 = new Item("131665", "Lemon Cake Brown", LEMONCAKE2_PRICE, LEMONCAKE2_BASE_PRICE);
		Item lemonCake3 = new Item("131556", "Lemon Cake Amber", LEMONCAKE3_PRICE, LEMONCAKE3_BASE_PRICE);
		items.add(lemonCake1);
		items.add(lemonCake2);
		items.add(lemonCake3);
		
		when(inventory.getItemsExpireInAMonth()).thenReturn(items);
		when(inventory.itemsUpdated()).thenReturn(2);
		
		assertEquals(2, biggestBazar.issueDiscountForItemsExpireIn30Days(0.2)); // this is supposed to be "2". 
																				//Question states "Assert that 0 is returned"
		verify(inventory, times(2)).update(isA(Item.class), anyDouble());
		verify(pas, times(2)).announce(isA(Offer.class));
	}


}
