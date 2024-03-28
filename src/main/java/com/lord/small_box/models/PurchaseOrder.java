package com.lord.small_box.models;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "purchase_order")
public class PurchaseOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private int orderNumber;

	private String jurisdiction;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "executer_org_id", referencedColumnName = "id")
	private Organization executerUnit;

	private String financingSource;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "dependency_org_id", referencedColumnName = "id")
	private Organization dependency;

	private String provider;

	private String deliverTo;

	private Calendar date;

	private String exp;
	
	private BigDecimal purchaseOrderTotal;

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "purchase_order_item_junction", joinColumns = {
			@JoinColumn(name = "purchase_order_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "item_id", referencedColumnName = "id") })
	private List<PurchaseOrderItem> items;

}
