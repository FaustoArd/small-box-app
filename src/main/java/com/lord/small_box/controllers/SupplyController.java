package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lord.small_box.dao.SupplyDao;
import com.lord.small_box.dao.SupplyItemDao;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.mappers.SupplyItemMapper;
import com.lord.small_box.mappers.SupplyMapper;
import com.lord.small_box.models.Supply;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/supply")
@RequiredArgsConstructor
public class SupplyController {
	
	@Autowired
	private final SupplyDao supplyDao;
	
	@Autowired
	private final SupplyItemDao supplyItemDao;
	
	@GetMapping("/find-all-supplies")
	ResponseEntity<List<SupplyDto>> findAllSupplies(){
		List<Supply> supplies = supplyDao.findAllSupplies();
		
		return ResponseEntity.ok(SupplyMapper.INSTANCE.suppliesToDtos(supplies));
	}

}
