package com.lord.small_box.custom_mapper_impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

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
		lineItem.add(entities.get("line_item").toString().replace("[", "").replace("]", ""));
		receiptDto.setLine_item(lineItem);
		
		String result = "";
		if(entities.get("receipt_code").size()==2) {
			Iterator<String> it =  entities.get("receipt_code").iterator();
			
			while(it.hasNext()) {
				result =  it.next();
				result = it.next() + "-"  + result ;
				
			}
			receiptDto.setReceipt_code(result);
		}else {
			receiptDto.setReceipt_code(entities.get("receipt_code").toString().replace("[", "").replace("]", ""));
		}
		
		receiptDto.setPayment_type(entities.get("payment_type").toString());
		ArrayList<String> supplies_city = new ArrayList<>();
		supplies_city.add(entities.get("supplier_city").toString().replace("[", "").replace("]", ""));
		receiptDto.setSupplier_city(supplies_city);
		receiptDto.setSupplier_address(entities.get("supplier_address").toString().replace("[", "").replace("]", ""));
		receiptDto.setTotal_price(entities.get("total_price").toString().replace("[", "").replace("]", "").replace(",", "."));
		
		ArrayList<String> supplier_name = new ArrayList<>();
		supplier_name.add(entities.get("supplier_name").toString().replace("[", "").replace("]", ""));
		receiptDto.setSupplier_name(supplier_name);
		
		SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
		String dateResponse = entities.get("receipt_date").toString().replace("[", "").replace("]", "").replace("/", "-");
		try {
			Date formattedDate = dmyFormat.parse(dateResponse);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(formattedDate);
			receiptDto.setReceipt_date(calendar);
			
			return receiptDto;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return receiptDto;
	}

	

}