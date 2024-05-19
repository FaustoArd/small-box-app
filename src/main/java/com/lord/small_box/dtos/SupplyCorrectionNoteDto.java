package com.lord.small_box.dtos;

import java.util.Calendar;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SupplyCorrectionNoteDto {

	private Calendar supplyDate;
	
	private int supplyNumber;
	
	private int supplyExcerciseYear;
	
	private String from;
	
	private String to;
	
	private List<SupplyReportDto> supplyReport;
	
	private String depositName;
}
