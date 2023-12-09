package com.lord.small_box.models;

import java.util.Calendar;

import jakarta.persistence.Column;
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
@Table(name="container")
public class Container {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name="title")
	private String title;
	
	@Column(name="dependency")
	private String dependency;
	
	@Column(name="responsible")
	private String responsible; 
	
	@Column(name="small_box_total")
	private Double total;
	
	@Column(name="small_box_date")
	private Calendar smallBoxDate;
	
	@Column(name="created")
	private boolean smallBoxCreated;
	
	
}
