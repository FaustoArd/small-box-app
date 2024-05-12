package com.lord.small_box.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.lord.small_box.dtos.ExcelItemDto;

@Component
public class ExcelToListUtils {

	private final Logger log = LoggerFactory.getLogger(ExcelToListUtils.class);

	public List<ExcelItemDto> excelDataToDeposit(MultipartFile file) throws IOException {
		log.info("Import xls to Database and create excelItemsDto");
		File tempfile = new File("e:\\temp-files\\excelcomparator.tmp");
		file.transferTo(tempfile);
		FileInputStream fis = new FileInputStream(tempfile);
		Workbook workbook = new HSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0);
		List<ExcelItemDto> excelItemDtos = new ArrayList<ExcelItemDto>();
		DataFormatter dataFormatter = new DataFormatter();
		for (int n = 1; n < sheet.getPhysicalNumberOfRows(); n++) {
			Row row = sheet.getRow(n);
			ExcelItemDto excelItemDto = new ExcelItemDto();
			int i = row.getFirstCellNum();
			excelItemDto.setItemMeasureUnit(dataFormatter.formatCellValue(row.getCell(i)).trim());
			excelItemDto.setItemDescription(dataFormatter.formatCellValue(row.getCell(++i)).trim());
			excelItemDto.setItemQuantity((int) row.getCell(++i).getNumericCellValue());
			excelItemDtos.add(excelItemDto);
		}
		workbook.close();
		return excelItemDtos;
			
		
	}

}
