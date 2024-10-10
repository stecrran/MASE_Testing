package com.ait.retailers;

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

import org.junit.jupiter.api.Test;

public class BiggestBazarRetailTest {
	
	private Inventory inventory = mock(Inventory.class);
	private PublicAddressSystem pas = mock(PublicAddressSystem.class);
	BiggestBazarRetail biggestBazar = new BiggestBazarRetail(inventory, pas);
	
	ArrayList<Item> items = new ArrayList<>();
	
	@Test
	void testDiscountIssuedOnOneItem() {
		Item lemonCake = new Item("131465", "Lemon Cake", 12.99, 6.45);
		items.add(lemonCake);
		
		when(inventory.getItemsExpireInAMonth()).thenReturn(items);
		when(inventory.itemsUpdated()).thenReturn(1);
		
		assertEquals(1, biggestBazar.issueDiscountForItemsExpireIn30Days(0.2));
		verify(inventory, times(1)).update(isA(Item.class), anyDouble());
		
		//Offer lemonCakeOffer = new Offer(lemonCake, 10.392);  // this produces a new Offer object 
		verify(pas, times(1)).announce(isA(Offer.class)); // the new offer object is not going to be the same as the object created from issueDiscountForItemsExpireIn30Days

	}
	
	@Test
	void testDiscountNotIssued() {
		Item lemonCake = new Item("131465", "Lemon Cake", 13.29, 13.00);
		items.add(lemonCake);
		
		when(inventory.getItemsExpireInAMonth()).thenReturn(items);
		when(inventory.itemsUpdated()).thenReturn(0);
		
		assertEquals(0, biggestBazar.issueDiscountForItemsExpireIn30Days(0.2));
		verify(inventory, times(0)).update(isA(Item.class), anyDouble());
		
		verify(pas, times(0)).announce(isA(Offer.class));
	}
	
	@Test
	void testThreeItemsDiscounted() {
		Item lemonCake1 = new Item("131465", "Lemon Cake Blond", 13.29, 13.00);
		Item lemonCake2 = new Item("131665", "Lemon Cake Brown", 12.99, 6.45);
		Item lemonCake3 = new Item("131556", "Lemon Cake Amber", 11.49, 6.45);
		items.add(lemonCake1);
		items.add(lemonCake2);
		items.add(lemonCake3);
		
		when(inventory.getItemsExpireInAMonth()).thenReturn(items);
		when(inventory.itemsUpdated()).thenReturn(2);
		
		assertEquals(2, biggestBazar.issueDiscountForItemsExpireIn30Days(0.2)); // I'm assuming this is supposed to be "2". 
																				//Question states "Assert that 0 is returned"
		verify(inventory, times(2)).update(isA(Item.class), anyDouble());
		verify(pas, times(2)).announce(isA(Offer.class));
	}


}
