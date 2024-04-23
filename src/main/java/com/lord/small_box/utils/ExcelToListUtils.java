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
import org.springframework.stereotype.Component;
import com.lord.small_box.dtos.ExcelItemDto;
import com.lord.small_box.models.Deposit;

@Component
public class ExcelToListUtils {
	
	public  List<ExcelItemDto> excelDataToDeposit(String fileLocation,Deposit deposit)throws IOException{
		FileInputStream file = new FileInputStream(new File(fileLocation));
		Workbook workbook = new HSSFWorkbook(file);
		Sheet  sheet  = workbook.getSheetAt(0);
		List<ExcelItemDto> candidates = new ArrayList<ExcelItemDto>();
		DataFormatter dataFormatter = new DataFormatter();
		for(int n = 1; n <sheet.getPhysicalNumberOfRows();n++) {
			Row row = sheet.getRow(n);
			ExcelItemDto candidate = new ExcelItemDto();
			int i = row.getFirstCellNum();
			candidate.setExcelItemId(n);
			candidate.setItemDescription(dataFormatter.formatCellValue(row.getCell(i)).trim());
			candidate.setItemMeasureUnit(dataFormatter.formatCellValue(row.getCell(++i)).trim());
			/*String unitPrice = dataFormatter.formatCellValue(row.getCell(++i)).replace(".", "").replace(",", ".").trim();
			control.setItemUnitPrice(new BigDecimal(unitPrice));*/
			candidate.setQuantity((int)row.getCell(++i).getNumericCellValue());
			candidates.add(candidate);
		}
		workbook.close();
		return candidates;
		
	}

}
