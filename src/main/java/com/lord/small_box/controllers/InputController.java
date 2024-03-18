package com.lord.small_box.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lord.small_box.dtos.InputDto;
import com.lord.small_box.mappers.InputMapper;
import com.lord.small_box.models.Input;
import com.lord.small_box.services.InputService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/inputs")
@RequiredArgsConstructor
public class InputController {
	
	@Autowired
	private final InputService inputService;
	
	@GetMapping("/example")
	ResponseEntity<List<InputDto>> findByExample(@RequestBody InputDto inputDto){
		Input input = InputMapper.INSTNACE.toInput(inputDto);
		List<Input> inputs = inputService.findByExample(input);
		List<InputDto> inputsDto = InputMapper.INSTNACE.toInputsDto(inputs);
		return new ResponseEntity<List<InputDto>>(inputsDto,HttpStatus.OK);
	}
	@GetMapping("/all")
	ResponseEntity<List<InputDto>> findAllInputs(){
		List<Input> inputs = inputService.findAll();
		List<InputDto> inputsDto= InputMapper.INSTNACE.toInputsDto(inputs);
		return new ResponseEntity<List<InputDto>>(inputsDto,HttpStatus.OK);
	}

}
