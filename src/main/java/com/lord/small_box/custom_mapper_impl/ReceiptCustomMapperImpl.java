package com.lord.small_box.custom_mapper_impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.stereotype.Component;

import com.google.common.collect.Multimap;
import com.lord.small_box.dtos.ReceiptDto;
import com.lord.small_box.mappers.ReceiptCustomMapper;

@Component
public class ReceiptCustomMapperImpl implements ReceiptCustomMapper {

	@Override
	public ReceiptDto entityListToDto(Multimap<String, String> entities) {
		ReceiptDto receiptDto = new ReceiptDto();
		
		ArrayList<String> lineItem = new ArrayList<>();
		lineItem.add(entities.get("line_item").toString());
		receiptDto.setLine_item(lineItem);
		
		/*ArrayList<String> receipt_code =  new ArrayList<>();
		receipt_code.add(entities.get("receipt_code").toString());
		receiptDto.setReceipt_code(receipt_code);*/
		String result = "";
		if(entities.get("receipt_code").size()==2) {
			Iterator<String> it =  entities.get("receipt_code").iterator();
			while(it.hasNext()) {
				result =  it.next();
				result = it.next() + result ;
				
			}
			receiptDto.setReceipt_code(result);
		}else {
			receiptDto.setReceipt_code(entities.get("receipt_code").toString());
		}
		
		receiptDto.setPayment_type(entities.get("payment_type").toString());
		
		ArrayList<String> supplies_city = new ArrayList<>();
		supplies_city.add(entities.get("supplier_city").toString());
		receiptDto.setSupplier_city(supplies_city);
		
		receiptDto.setSupplier_address(entities.get("supplier_address").toString());
		receiptDto.setReceipt_date(entities.get("receipt_date").toString());
		receiptDto.setTotal_price(entities.get("total_price").toString());
		
		ArrayList<String> supplier_name = new ArrayList<>();
		supplier_name.add(entities.get("supplier_name").toString());
		receiptDto.setSupplier_name(supplier_name);
		
		return receiptDto;
	}

	

}