package com.lord.small_box.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.StockControl;

public interface StockControlRepository extends JpaRepository<StockControl, Long> {

}
