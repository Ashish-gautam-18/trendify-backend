package com.trendify.entity;

import com.trendify.user.domain.PaymentMethod;
import com.trendify.user.domain.PaymentStatus;
import lombok.Data;

// This class holds specific payment transaction details and Razorpay integration keys
@Data
public class PaymentDetails {
	
	// The method used for payment (e.g., CREDIT_CARD, CASH_ON_DELIVERY)
	private PaymentMethod paymentMethod;

	// The current status of the payment (e.g., PENDING, COMPLETED, FAILED)
	private PaymentStatus status;

	// Unique transaction ID generated for the payment
	private String paymentId;

	// Razorpay specific payment link identifier
	private String razorpayPaymentLinkId;

	// Razorpay internal reference ID for tracking the payment link
	private String razorpayPaymentLinkReferenceId;

	// The status of the Razorpay payment link (e.g., created, paid, expired)
	private String razorpayPaymentLinkStatus;

	// Razorpay final transaction payment identifier (Cleaned from hidden character)
	private String razorpayPaymentId;

}
