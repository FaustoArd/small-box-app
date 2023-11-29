package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lord.small_box.dtos.SmallBoxDto;
import com.lord.small_box.dtos.SmallBoxUnifierDto;
import com.lord.small_box.mappers.SmallBoxMapper;
import com.lord.small_box.mappers.SmallBoxUnifierMapper;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SmallBoxUnifier;
import com.lord.small_box.services.InputService;
import com.lord.small_box.services.SmallBoxService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small_box/smallboxes")
@RequiredArgsConstructor
public class SmallBoxController {
	
	@Autowired
	private final SmallBoxService smallBoxService;

	@Autowired
	private final InputService inputService;
	
	@GetMapping("/all")
	ResponseEntity<List<SmallBoxDto>> findAll(){
		List<SmallBox> smallBoxes = smallBoxService.findAll();
		List<SmallBoxDto> smallBoxesDto = SmallBoxMapper.INSTANCE.toSmallBoxesDtos(smallBoxes);
		return new ResponseEntity<List<SmallBoxDto>>(smallBoxesDto,HttpStatus.OK);
	}
	
	@GetMapping("/all_by_input")
	ResponseEntity<List<SmallBoxDto>> findAllByInput(@RequestBody String inputNumber){
		List<SmallBox> smallBoxes = smallBoxService.findAllOrderByInputInputNumber(inputNumber);
		List<SmallBoxDto> smallBoxesDto = SmallBoxMapper.INSTANCE.toSmallBoxesDtos(smallBoxes);
		return new ResponseEntity<List<SmallBoxDto>>(smallBoxesDto,HttpStatus.OK);
	}
	
	@PostMapping("/new")
	ResponseEntity<SmallBoxDto> newSmallBox(@RequestBody SmallBoxDto smallBoxDto,
			@RequestParam("containerId")Integer containerId){
		SmallBox smallBox = SmallBoxMapper.INSTANCE.toSmallBox(smallBoxDto);
		SmallBox savedSmallBox = smallBoxService.save(smallBox, containerId);
		SmallBoxDto savedSmallBoxDto = SmallBoxMapper.INSTANCE.toSmallBoxDto(savedSmallBox);
		return new ResponseEntity<SmallBoxDto>(savedSmallBoxDto,HttpStatus.CREATED);
	}
	
	@PutMapping("/build")
	ResponseEntity<List<SmallBoxUnifierDto>> buildSmallBox(@RequestParam("containerId")Integer containerId){
		
		List<SmallBoxUnifier> sm = smallBoxService.insertSubtotalInColumn(containerId);
		List<SmallBoxUnifierDto> smDto = SmallBoxUnifierMapper.INSTANCE.toSmallBoxBuildersDto(sm);
		return new ResponseEntity<List<SmallBoxUnifierDto>>(smDto,HttpStatus.OK);
	}

	
	
}
