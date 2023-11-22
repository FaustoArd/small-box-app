package com.lord.small_box.models;

import java.math.BigDecimal;
import java.util.Calendar;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
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
@Table(name="small_box")
public class SmallBox {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="date")
	private Calendar date;
	
	@Column(name="ticket_number")
	private String ticketNumber;
	
	@Column(name="provider")
	private String provider;
	
	@ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
	@JoinColumn(name="input_id", referencedColumnName = "id")
	private Input input;
	
	@Column(name="ticket_total")
	private BigDecimal ticketTotal;
	
	@Column(name="view_order")
	private Integer viewOrder;
	
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name="container_id", referencedColumnName = "id")
	private Container container;
	
	@ManyToOne(cascade =  CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name="subtotal_id", referencedColumnName = "id")
	private SubTotal subtotal;
	
}
