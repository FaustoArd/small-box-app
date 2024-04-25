package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.ExcelItem;
import com.lord.small_box.models.ExcelItemContainer;

public interface ExcelItemRepository extends JpaRepository<ExcelItem, Long> {
	
	public List<ExcelItem> findAllByExcelItemContainer(ExcelItemContainer excelItemContainer);

}
