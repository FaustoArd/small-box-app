package com.lord.small_box.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SupplyCorrectionNote {

	private String from;
	
	private String to;
	
	private List<SupplyReportDto> supplyReport;
}
