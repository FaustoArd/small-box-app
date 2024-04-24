package com.lord.small_box.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.ExcelItem;

public interface ExcelItemRepository extends JpaRepository<ExcelItem, Long> {

}
