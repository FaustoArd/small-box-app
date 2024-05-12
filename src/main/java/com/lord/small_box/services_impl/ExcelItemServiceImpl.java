package com.lord.small_box.services_impl;

import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lord.small_box.dtos.ExcelItemDto;
import com.lord.small_box.models.ExcelItem;
import com.lord.small_box.models.ExcelItemContainer;
import com.lord.small_box.models.Organization;
import com.lord.small_box.repositories.ExcelItemContainerRepository;
import com.lord.small_box.repositories.ExcelItemRepository;
import com.lord.small_box.services.ExcelItemService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelItemServiceImpl  implements ExcelItemService{
	
	@Autowired
	private final ExcelItemContainerRepository excelItemContainerRepository;
	
	@Autowired
	private final ExcelItemRepository excelItemRepository;
	
	@Override
	public ExcelItemContainer saveExcelItemContainer(Organization organization) {
		Calendar now = Calendar.getInstance();
		ExcelItemContainer excelItemContainer = new ExcelItemContainer();
		excelItemContainer.setContainerDate(now);
		excelItemContainer.setContainerName("EXCEL IMPORTADO " + now.getTime());
		excelItemContainer.setOrganization(organization);
		ExcelItemContainer savedExcelItemContainer = excelItemContainerRepository.save(excelItemContainer);
		return savedExcelItemContainer;
		
		
	}

	@Override
	public List<ExcelItemDto> saveExcelItems(List<ExcelItemDto> excelItemDtos,ExcelItemContainer excelItemContainer) {
		List<ExcelItem> excelItems = dtosToExcelItems(excelItemDtos);
		List<ExcelItem> updatedItems =  excelItems.stream().map(item -> {
			item.setExcelItemContainer(excelItemContainer);
			return item;
		}).toList();
		return excelItemsToDtos(excelItemRepository.saveAll(updatedItems));
	}
	
	private static List<ExcelItem> dtosToExcelItems(List<ExcelItemDto> excelItemDtos){
		if(excelItemDtos==null) {
			return null;
		}
		List<ExcelItem> items = excelItemDtos.stream().map(excelItemDto -> {
			ExcelItem excelItem = new ExcelItem();
			excelItem.setItemDescription(excelItemDto.getItemDescription());
			excelItem.setItemMeasureUnit(excelItemDto.getItemMeasureUnit());
			excelItem.setItemQuantity(excelItemDto.getItemQuantity());
			
			return excelItem;
		}).toList();
		return items;
	}
	
	private static List<ExcelItemDto> excelItemsToDtos(List<ExcelItem> excelItems){
		if(excelItems==null) {
			return null;
		}
		List<ExcelItemDto> itemDtos = excelItems.stream().map(excelItem -> {
			ExcelItemDto excelItemDto = new ExcelItemDto();
			excelItemDto.setItemDescription(excelItem.getItemDescription());
			excelItemDto.setItemMeasureUnit(excelItem.getItemMeasureUnit());
			excelItemDto.setItemQuantity(excelItem.getItemQuantity());
			excelItemDto.setExcelItemId(excelItem.getId());
			excelItemDto.setExcelItemContainerId(excelItem.getExcelItemContainer().getId());
			return excelItemDto;
		}).toList();
		return itemDtos;
	}
	

}
