package com.lord.small_box.models;

import java.util.Calendar;

import jakarta.persistence.CascadeType;
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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="location_contracts")
public class LocationContract {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String homeDependency;
	
	private String homeResponsible;
	
	private String decree;
	
	private String homeDependencyAddress;
	
	private String contractedName;
	
	private String contractedCountry;
	
	private String contractedDni;
	
	private String contractedAfipRank;
	
	private String contractedCuit;
	
	private String contractedAddress;
	
	private String contractedAddressFloor;
	
	private String contractedAddressDptoNumber;
	
	private String contractedCity;
	
	private String contractedCounty;
	
	private String contractedLegalAddress;
	
	private String contractedTask;
	
	private Calendar vigencyPeriodStart;
	
	private Calendar vigencyPeriodEnd;
	
	private String training;
	
	private String trainingHour;
	
	private String contractedPrice;
	
	private Calendar contractDate;
	
	@ManyToOne(cascade =  CascadeType.MERGE,fetch = FetchType.LAZY)
	@JoinColumn(name="organization_id", referencedColumnName = "id")
	private Organization organization;

}
