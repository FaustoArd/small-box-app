package com.lord.small_box.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositControl;

@Component
public class ExcelToListUtils {
	
	public  List<DepositControl> excelDataToDeposit(String fileLocation,Deposit deposit)throws IOException{
		FileInputStream file = new FileInputStream(new File(fileLocation));
		Workbook workbook = new HSSFWorkbook(file);
		Sheet  sheet  = workbook.getSheetAt(0);
		List<DepositControl> controls = new ArrayList<DepositControl>();
		DataFormatter dataFormatter = new DataFormatter();
		for(int n = 1; n <sheet.getPhysicalNumberOfRows();n++) {
			Row row = sheet.getRow(n);
			DepositControl control = new DepositControl();
			int i = row.getFirstCellNum();
			control.setDeposit(deposit);
			control.setItemCode(dataFormatter.formatCellValue(row.getCell(i)).trim());
			control.setMeasureUnit(dataFormatter.formatCellValue(row.getCell(++i)).trim());
			String unitPrice = dataFormatter.formatCellValue(row.getCell(++i)).replace(".", "").replace(",", ".").trim();
			control.setItemUnitPrice(new BigDecimal(unitPrice));
			control.setQuantity((int)row.getCell(++i).getNumericCellValue());
			controls.add(control);
		}
		workbook.close();
		return controls;
		
	}

}
