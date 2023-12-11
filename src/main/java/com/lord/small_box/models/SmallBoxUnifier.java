package com.lord.small_box.models;

import java.math.BigDecimal;
import java.util.Calendar;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name="small_box_unifier")
public class SmallBoxUnifier {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="date")
	private Calendar date;
	
	@Column(name="ticket_number")
	private String ticketNumber;
	
	@Column(name="provider")
	private String provider;
	
	@Column(name="description")
	private String description;
	
	@Column(name="input_number")
	private String inputNumber;
	
	@Column(name="ticket_total")
	private BigDecimal ticketTotal;
	
	@ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
	@JoinColumn(name="container_id", referencedColumnName = "id")
	private Container container;
	
	@Column(name="str_subtotal")
	private String subtotalTitle;
	
	@Column(name="subtotal")
	private BigDecimal subtotal;
	
	@Column(name="total")
	private BigDecimal total;
	
	
}
