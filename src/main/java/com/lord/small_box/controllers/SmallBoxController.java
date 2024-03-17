package com.lord.small_box.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.dtos.SmallBoxDto;
import com.lord.small_box.dtos.SmallBoxUnifierDto;
import com.lord.small_box.mappers.SmallBoxMapper;
import com.lord.small_box.mappers.SmallBoxUnifierMapper;
import com.lord.small_box.models.SmallBox;
import com.lord.small_box.models.SmallBoxUnifier;
import com.lord.small_box.services.InputService;
import com.lord.small_box.services.SmallBoxService;
import com.lord.small_box.services.SmallBoxUnifierService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/smallboxes")
@RequiredArgsConstructor
public class SmallBoxController {
	
	@Autowired
	private final SmallBoxService smallBoxService;

	@Autowired
	private final SmallBoxUnifierService smallBoxUnifierService;
	
	private static final Gson gson = new Gson();
	
	private static final Logger log = LoggerFactory.getLogger(SmallBoxController.class);
	
	@GetMapping("/smallbox/{id}")
	ResponseEntity<SmallBoxDto> findSmallBoxById(@PathVariable("id")Long id){
		SmallBox smallBox = smallBoxService.findById(id);
		SmallBoxDto smallBoxDto = SmallBoxMapper.INSTANCE.toSmallBoxDto(smallBox);
		return new ResponseEntity<SmallBoxDto>(smallBoxDto,HttpStatus.OK);
	}
	
	@GetMapping("/all")
	ResponseEntity<List<SmallBoxDto>> findAll(){
		List<SmallBox> smallBoxes = smallBoxService.findAll();
		List<SmallBoxDto> smallBoxesDto = SmallBoxMapper.INSTANCE.toSmallBoxesDtos(smallBoxes);
		return new ResponseEntity<List<SmallBoxDto>>(smallBoxesDto,HttpStatus.OK);
	}
	@GetMapping("/all-by-container")
	ResponseEntity<List<SmallBoxDto>> findAll(@RequestParam("containerId")Long containerId){
		List<SmallBox> smallBoxes = smallBoxService.findAllByContainerId(containerId);
		List<SmallBoxDto> smallBoxesDto = SmallBoxMapper.INSTANCE.toSmallBoxesDtos(smallBoxes);
		return new ResponseEntity<List<SmallBoxDto>>(smallBoxesDto,HttpStatus.OK);
	}
	
	@GetMapping("/all-by-input")
	ResponseEntity<List<SmallBoxDto>> findAllByInput(@RequestBody String inputNumber){
		List<SmallBox> smallBoxes = smallBoxService.findAllOrderByInputInputNumber(inputNumber);
		List<SmallBoxDto> smallBoxesDto = SmallBoxMapper.INSTANCE.toSmallBoxesDtos(smallBoxes);
		return new ResponseEntity<List<SmallBoxDto>>(smallBoxesDto,HttpStatus.OK);
	}
	
	@PostMapping("/new")
	ResponseEntity<SmallBoxDto> newSmallBox(@RequestBody SmallBoxDto smallBoxDto,
			@RequestParam("containerId")Long containerId){
		SmallBox smallBox = SmallBoxMapper.INSTANCE.toSmallBox(smallBoxDto);
		SmallBox savedSmallBox = smallBoxService.save(smallBox, containerId);
		SmallBoxDto savedSmallBoxDto = SmallBoxMapper.INSTANCE.toSmallBoxDto(savedSmallBox);
		return new ResponseEntity<SmallBoxDto>(savedSmallBoxDto,HttpStatus.CREATED);
	}
	
	 @PutMapping("/smallBox-update")
	 ResponseEntity<String> updateSmallBox(@RequestBody SmallBoxDto smallBoxDto){
		SmallBox smallBox = SmallBoxMapper.INSTANCE.toSmallBox(smallBoxDto);
		SmallBox updatedSmallBox =  smallBoxService.update(smallBox);
	
		return new ResponseEntity<String>(gson.toJson("Se actualizo el comprobante Numero: " + updatedSmallBox.getTicketNumber()),HttpStatus.OK);
	 }
	 
	 @DeleteMapping("/smallbox-delete/{id}")
	 ResponseEntity<String> deleteSmallBox(@PathVariable("id")Long id){
		 smallBoxService.delete(id);
		 return new ResponseEntity<String>(gson.toJson("Se elimino correctamente el ticket"),HttpStatus.OK);
	 }
	 
	
	@PutMapping("/complete")
	ResponseEntity<List<SmallBoxUnifierDto>> completeSmallBox(@RequestParam("containerId")Long containerId){
		
		List<SmallBoxUnifier> sm = smallBoxService.completeSmallBox(containerId);
		List<SmallBoxUnifierDto> smDto = SmallBoxUnifierMapper.INSTANCE.toSmallBoxBuildersDto(sm);
		return new ResponseEntity<List<SmallBoxUnifierDto>>(smDto,HttpStatus.OK);
	}
	
	
	
	@GetMapping("/unified/{containerId}")
	ResponseEntity<List<SmallBoxUnifierDto>> findSmallBoxCompletedByContainerId(@PathVariable("containerId")Long containerId){
		List<SmallBoxUnifier> completed = smallBoxUnifierService.findByContainerId(containerId);
		List<SmallBoxUnifierDto> completedDto = SmallBoxUnifierMapper.INSTANCE.toSmallBoxBuildersDto(completed);
		return new ResponseEntity<List<SmallBoxUnifierDto>>(completedDto,HttpStatus.OK);
	}
	
	@DeleteMapping("/unified-all-by-container/{containerId}")
	void deleteAllByContainerId(@PathVariable("containerId")Long containerId) {
		log.info("Deleting all UnifiedSmallBox by container id");
		smallBoxUnifierService.deleteAllByContainerId(containerId);
	}
	
	@GetMapping("/check-max-amount")
	ResponseEntity<String> checkMaxAmount(@RequestParam("containerId")Long containerId){
		String result = smallBoxService.checkMaxAmount(containerId);
		return new ResponseEntity<String>(gson.toJson(result),HttpStatus.OK); 
	}
	
	
	

	
	
}
