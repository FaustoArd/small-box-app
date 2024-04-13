package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.mappers.SupplyMapper;
import com.lord.small_box.models.Supply;
import com.lord.small_box.repositories.SupplyItemRepository;
import com.lord.small_box.repositories.SupplyRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/supply")
@RequiredArgsConstructor
public class SupplyController {
	
	@Autowired
	private final SupplyRepository supplyDao;
	
	@Autowired
	private final SupplyItemRepository supplyItemDao;
	
	@GetMapping("/find-all-supplies")
	ResponseEntity<List<SupplyDto>> findAllSupplies(){
		List<Supply> supplies = supplyDao.findAll();
		
		return ResponseEntity.ok(SupplyMapper.INSTANCE.suppliesToDtos(supplies));
	}

}
