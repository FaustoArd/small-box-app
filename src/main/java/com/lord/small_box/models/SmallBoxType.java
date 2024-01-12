package com.lord.small_box.models;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name="small_box_type")
public class SmallBoxType {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.AUTO)
	private Long id;
	
	private String smallBoxType;
	
	
	
	

}
