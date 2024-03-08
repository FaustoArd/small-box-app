package com.lord.small_box.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lord.small_box.models.DispatchControl;
import com.lord.small_box.repositories.DispatchControlRepository;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.utils.FileReaderUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/csv_utils/")
@RequiredArgsConstructor
public class FileCsvUtilsController {
	
	@Autowired
	private final FileReaderUtils fileReaderUtils;
	
	@Autowired
	private final DispatchControlRepository dispatchControlRepository;

	@GetMapping("/")
	ResponseEntity<?> importTableToDB() throws IOException{
		List<String> data = fileReaderUtils.readFile();
		List<String> processedDAta = fileReaderUtils.replaceItemsMethod2(data);
		List<DispatchControl> dispatchs = fileReaderUtils.convertDataToDispatch(processedDAta);
		dispatchControlRepository.saveAll(dispatchs);
		return new ResponseEntity<List<DispatchControl>>(dispatchs,HttpStatus.OK);
	}
}
