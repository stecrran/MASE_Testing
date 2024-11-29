package com.tus.cart;

import com.tus.payment.PaymentStrategy;

public class Customer {
	private Cart cart;
	private long accountNumber;
	private String customerName;
	private String customerEmail;
	private String customerAddress;
	private String paymentType;
	private PaymentStrategy paymentStrategy;

	public Cart getCart() {
		return cart;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(final long number) {
		this.accountNumber = number;
	}

	public void setName(final String name) {
		this.customerName = name;
	}

	public void setEmail(final String email) {
		this.customerEmail = email;
	}


	public void setAddress(final String address) {
		this.customerAddress = address;
	}

	public void setCart(final Cart cart) {
		this.cart = cart;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(final String paymentType) {
		this.paymentType = paymentType;
	}

	public PaymentStrategy getPaymentStrategy() {
		return paymentStrategy;
	}

	public void setPaymentStrategy(final PaymentStrategy paymentStrategy) {
		this.paymentStrategy = paymentStrategy;
	}

}
