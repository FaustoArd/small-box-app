package com.lord.small_box.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.lord.small_box.dtos.ExcelItemDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.ExcelItem;
import com.lord.small_box.models.ExcelItemContainer;
import com.lord.small_box.models.Organization;
import com.lord.small_box.repositories.ExcelItemContainerRepository;
import com.lord.small_box.repositories.ExcelItemRepository;
import com.lord.small_box.repositories.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExcelToListUtils {

	@Autowired
	private final ExcelItemRepository excelItemRepository;

	@Autowired
	private final ExcelItemContainerRepository excelItemContainerRepository;

	@Autowired
	private final OrganizationRepository organizationRepository;

	private final Logger log = LoggerFactory.getLogger(ExcelToListUtils.class);

	public List<ExcelItemDto> excelDataToDeposit(String fileLocation, long organizationId) throws IOException {
		log.info("Import xls to Database and create excelItemsDto");
		String filePath = "D:\\filetest\\" + fileLocation;
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);

		/* FileInputStream file = new FileInputStream(new File(fileLocation)); */
		Workbook workbook = new HSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0);
		List<ExcelItem> excelItems = new ArrayList<ExcelItem>();
		DataFormatter dataFormatter = new DataFormatter();
		for (int n = 1; n < sheet.getPhysicalNumberOfRows(); n++) {
			Row row = sheet.getRow(n);
			ExcelItem excelItem = new ExcelItem();
			int i = row.getFirstCellNum();
			excelItem.setItemDescription(dataFormatter.formatCellValue(row.getCell(i)).trim());
			excelItem.setItemMeasureUnit(dataFormatter.formatCellValue(row.getCell(++i)).trim());
			/*
			 * String unitPrice =
			 * dataFormatter.formatCellValue(row.getCell(++i)).replace(".", "").replace(",",
			 * ".").trim(); control.setItemUnitPrice(new BigDecimal(unitPrice));
			 */
			excelItem.setQuantity((int) row.getCell(++i).getNumericCellValue());
			excelItems.add(excelItem);
		}
		workbook.close();
		Calendar now = Calendar.getInstance();
		ExcelItemContainer excelItemContainer = new ExcelItemContainer();
		excelItemContainer.setContainerDate(now);
		excelItemContainer.setContainerName("EXCEL IMPORTADO " + now.getTime());
		ExcelItemContainer savedExcelItemContainer = excelItemContainerRepository.save(excelItemContainer);
		System.out.println(savedExcelItemContainer.getContainerName());
		Organization org = organizationRepository.findById(organizationId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro la organizacion"));
		excelItems = excelItems.stream().map(m -> {
			m.setOrganization(org);
			m.setExcelItemContainer(savedExcelItemContainer);
			return m;
		}).collect(Collectors.toList());
		List<ExcelItem> savedExcelItems = excelItemRepository.saveAll(excelItems);
		List<ExcelItemDto> itemDtos = savedExcelItems.stream().map(excelItem -> {
			ExcelItemDto excelItemDto = new ExcelItemDto();
			excelItemDto.setItemDescription(excelItem.getItemDescription());
			excelItemDto.setItemMeasureUnit(excelItem.getItemMeasureUnit());
			excelItemDto.setQuantity(excelItem.getQuantity());
			excelItemDto.setExcelItemId(excelItem.getId());
			excelItemDto.setExcelItemContainerId(excelItem.getExcelItemContainer().getId());
			return excelItemDto;
		}).toList();
		return itemDtos;
	}

}
