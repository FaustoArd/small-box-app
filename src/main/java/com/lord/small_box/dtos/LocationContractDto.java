package com.lord.small_box.dtos;

import java.util.Calendar;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationContractDto {

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

}
