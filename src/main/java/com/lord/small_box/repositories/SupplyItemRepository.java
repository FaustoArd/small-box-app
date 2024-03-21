package com.lord.small_box.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.SupplyItem;

public interface SupplyItemRepository extends JpaRepository<SupplyItem, Long> {

}
