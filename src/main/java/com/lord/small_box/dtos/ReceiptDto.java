package com.lord.small_box.dtos;

import java.util.ArrayList;
import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReceiptDto {
	
	private String payment_type;
	
	private ArrayList<String> supplier_city;
	
	private String supplier_address;
	
	private ArrayList<String> line_item;
	
	private String total_price;
	
	private Calendar receipt_date;
	
	private String receipt_code;
	
	private ArrayList<String> supplier_name;

}
