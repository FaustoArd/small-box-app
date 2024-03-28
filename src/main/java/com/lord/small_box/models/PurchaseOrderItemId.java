package com.lord.small_box.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class PurchaseOrderItemId {
	
	String orderNumber;
	long orderId;
}
