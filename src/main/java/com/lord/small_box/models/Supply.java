package com.lord.small_box.models;

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

	private Calendar date;

	private String jurisdiction;

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinTable(name = "supply_supply_item_junction", joinColumns = {
			@JoinColumn(name = "supply_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "supply_item_id", referencedColumnName = "id") })
	private List<SupplyItem> supplyItems;

}
