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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "supply")
public class Supply {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(cascade =  CascadeType.MERGE,fetch = FetchType.LAZY)
	@JoinColumn(name="organization_id",referencedColumnName = "id")
	private Organization organization;

	private Calendar date;

	private String jurisdiction;
	
	private int supplyNumber;

	private BigDecimal estimatedTotalCost;
	
	
	private String dependencyApplicant;

}
