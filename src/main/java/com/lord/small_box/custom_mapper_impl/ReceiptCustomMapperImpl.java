package com.lord.small_box.custom_mapper_impl;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.google.common.collect.Multimap;
import com.lord.small_box.dtos.ReceiptDto;
import com.lord.small_box.mappers.ReceiptCustomMapper;

@Component
public class ReceiptCustomMapperImpl implements ReceiptCustomMapper {

	@Override
	public ReceiptDto entityListToDto(Multimap<String, String> enities) {
		ReceiptDto receiptDto = new ReceiptDto();
		
		ArrayList<String> lineItem = new ArrayList<>();
		lineItem.add(enities.get("line_item").toString());
		receiptDto.setLine_item(lineItem);
		
		ArrayList<String> receipt_code =  new ArrayList<>();
		receipt_code.add(enities.get("receipt_code").toString());
		receiptDto.setReceipt_code(receipt_code);
		
		receiptDto.setPayment_type(enities.get("payment_code").toString());
		
		ArrayList<String> supplies_city = new ArrayList<>();
		supplies_city.add(enities.get("supplier_city").toString());
		receiptDto.setSupplier_city(supplies_city);
		
		receiptDto.setSupplier_address(enities.get("supplier_address").toString());
		receiptDto.setReceipt_date(enities.get("receipt_date").toString());
		receiptDto.setTotal_price(enities.get("total_price").toString());
		
		ArrayList<String> supplier_name = new ArrayList<>();
		supplier_name.add(enities.get("supplier_name").toString());
		receiptDto.setSupplier_name(supplier_name);
		
		return receiptDto;
	}

	

}