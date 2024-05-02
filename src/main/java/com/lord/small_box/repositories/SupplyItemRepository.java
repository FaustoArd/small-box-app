package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Supply;
import com.lord.small_box.models.SupplyItem;

public interface SupplyItemRepository extends JpaRepository<SupplyItem, Long> {
	
	public List<SupplyItem> findAllBySupply(Supply supply);
	
	public List<SupplyItem> findAllBySupplyIn(List<Supply> supplies);

}
