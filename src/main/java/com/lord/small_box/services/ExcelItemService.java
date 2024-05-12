package com.lord.small_box.services;

import java.util.List;
import com.lord.small_box.dtos.ExcelItemContainerDto;
import com.lord.small_box.dtos.ExcelItemDto;
import com.lord.small_box.models.ExcelItemContainer;
import com.lord.small_box.models.Organization;


public interface ExcelItemService {
	
	public ExcelItemContainer saveExcelItemContainer(Organization organization);
	
	public List<ExcelItemDto> saveExcelItems(List<ExcelItemDto> excelItemDtos,ExcelItemContainer excelItemContainer);

}
