package com.lord.small_box.models;

import java.math.BigDecimal;
import java.util.Calendar;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name="stock_control")
public class StockControl {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
	private Supply supply;
	
	private String itemName;
	
	private BigDecimal itemUnitPrice;
	
	private int itemOriginalQuantity;
	
	private int itemCurrentQuantity;
	
	private String provider;
	
}
