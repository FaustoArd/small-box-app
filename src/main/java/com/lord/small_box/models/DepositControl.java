package com.lord.small_box.models;

import java.math.BigDecimal;
import java.util.Calendar;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="deposit_control")
public class DepositControl {
	
	private Supply supply;
	
	private String place;
	
	private String itemName;
	
	private int quantity;
	
	private Calendar expirationDate;
	
	private String provider;
	
	private BigDecimal itemUnitPrice;
	
	private BigDecimal itemTotalPrice;

}
