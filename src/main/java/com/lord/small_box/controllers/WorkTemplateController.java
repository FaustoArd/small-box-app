package com.lord.small_box.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lord.small_box.services.WorkTemplateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/small-box/work-templates")
@RequiredArgsConstructor
public class WorkTemplateController {
	
	private final WorkTemplateService workTemplateService;
	
	

}
