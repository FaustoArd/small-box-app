package com.lord.small_box.dtos;

import java.util.ArrayList;

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
	
	private String receipt_date;
	
	private ArrayList<String> receipt_code;
	
	private ArrayList<String> supplier_name;

}
