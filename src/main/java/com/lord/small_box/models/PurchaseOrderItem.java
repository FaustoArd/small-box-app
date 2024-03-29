package com.lord.small_box.models;

import java.math.BigDecimal;
import java.util.Calendar;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name="purchase_order_item")
public class PurchaseOrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String code;
	
	private String programaticCat;
	
	private int quantity;
	
	private String measureUnit;
	
	private String itemDetail;
	
	private BigDecimal unitCost;
	
	private BigDecimal  estimatedCost;
	
	private BigDecimal totalEstimatedCost;
	
	private Calendar expirationDate;
	
	@ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
	@JoinColumn(name="purchase_order_id", referencedColumnName = "id")
	private PurchaseOrder purchaseOrder;
	
	}
