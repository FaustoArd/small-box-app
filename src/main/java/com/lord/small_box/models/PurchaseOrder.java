package com.lord.small_box.models;

import java.util.Calendar;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="purchase_order")
public class PurchaseOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private int orderNumber;
	
	private String jurisdiction;
	
	private Organization executerUnity;
	
	private String financingSource;
	
	private Organization dependency;
	
	private String provider;
	
	private String deliverTo;
	
	private Calendar date;
	
	private String exp;
	
	

}
