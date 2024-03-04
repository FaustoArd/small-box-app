package com.lord.small_box.mappers;


import com.google.common.collect.Multimap;
import com.lord.small_box.dtos.ReceiptDto;

public interface ReceiptCustomMapper {
	
	public ReceiptDto entityListToDto(Multimap<String, String> enities);

}
