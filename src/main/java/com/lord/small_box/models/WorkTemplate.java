package com.lord.small_box.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name="work_templates")
public class WorkTemplate {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private Long id;
	
	private Calendar date;
	
	private String correspond;
	
	private String correspondNumber;
	
	//private String producedBy;
	
	private ArrayList<String> beforeBy;
	
	private ArrayList<String> destinations;
	
	@Column(name="text", length = 800)
	private String text;
	
	 private ArrayList<String> refs;
	 
	 private ArrayList<String> items;
	
	@ManyToOne(cascade =  CascadeType.MERGE,fetch = FetchType.LAZY)
	@JoinColumn(name="organization_id", referencedColumnName = "id")
	private Organization organization;
	
	
	

}
