package com.lord.small_box.models;

import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="organizations")
public class Organization {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="organization")
	private String organizationName;
	
	@Column(name="current_rotation")
	private int currentRotation;
	
	@Column(name="max_rotation")
	private int maxRotation;
	
	@Column(name="max_amount")
	private BigDecimal maxAmount;
	
	@Column(name="organization_number")
	private int organizationNumber;
	
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
	@JoinColumn(name="responsible_id", referencedColumnName = "id")
	private OrganizationResponsible responsible;
	
	@JsonIgnore
	@OneToMany(mappedBy = "organization")
	private List<Container> containers;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "organizations")
	private List<AppUser> users;
	
	
	

}
