package com.lord.small_box.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lord.small_box.SmallBoxApplication;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.DispatchControl;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.repositories.DispatchControlRepository;
import com.lord.small_box.repositories.OrganizationRepository;

@Service
public class FileReaderUtils {

	@Autowired
	private DispatchControlRepository dispatchControlRepository;
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	
	
	public void deleteFinalDotComma() throws IOException {
		String filePath = "\\\\canaima\\Usuarios\\Secretarias\\Desarrollo social\\SEC DSARROLLO SOCIAL\\ADMINISTRACION\\DESPACHO DIGITAL\\DESPACHO DIGITAL 2022 V4-cvs-v1-7.csv";	
		File file = new File(filePath);
		List<String> data = FileUtils.readLines(file, "UTF-8");
		
	}

	public List<String> readFile() throws IOException, URISyntaxException {
		String filePath = "\\\\canaima\\Usuarios\\Secretarias\\Desarrollo social\\SEC DSARROLLO SOCIAL\\ADMINISTRACION\\DESPACHO DIGITAL\\DESPACHO DIGITAL 2022 V4-cvs-v1-7.csv";
		File file = new File(filePath);
		List<String> data = FileUtils.readLines(file, "UTF-8");
		
		List<String> result = data.stream().filter(f -> !f.contains("id"))
				.map(m -> m.replace(",,,", ",\"0\",\"SIN DESCRIPCION\",")).map(m -> m.replace(",,", ",\"0\","))
				.toList();
		Organization org = organizationRepository.findById(2L).get();
		List<DispatchControl> dispatchTest = result.stream().map(m -> {
			DispatchControl dispatchResult;
			try {
				dispatchResult = mapToDispatchControl(m.split(","),org);
				return dispatchResult;
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}

		}).toList();
		dispatchTest.forEach(e -> System.out.println(e));

		/*
		 * String editedFilePath = "D:\\filetest\\despacho_edited_v1.csv"; File
		 * editedFile = new File(editedFilePath);
		 * FileUtils.writeLines(editedFile,result);
		 */
		return result;
	}

	public DispatchControl mapToDispatchControl(String[] arrayDispatch, Organization organization) throws ParseException {

		DispatchControl dispatchControl = new DispatchControl();
		dispatchControl.setId(Long.parseLong(arrayDispatch[0]));

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		if(arrayDispatch[1].length()==10) {
		if (arrayDispatch[1].contains("\"")) {
			arrayDispatch[1] = arrayDispatch[1].replace("\"", "");
			
		}
		arrayDispatch[1] = arrayDispatch[1].replace("/", "-");
		Date date = sdf.parse(arrayDispatch[1]);
			Calendar cal = sdf.getCalendar();
			cal.setTime(date);
			dispatchControl.setDate(cal);
		}
		
		dispatchControl.setType(arrayDispatch[2]);
		dispatchControl.setDocNumber(arrayDispatch[3]);
		dispatchControl.setVolumeNumber(arrayDispatch[4]);
		dispatchControl.setDescription(arrayDispatch[5]);
		dispatchControl.setToDependency(arrayDispatch[6]);
		dispatchControl.setOrganization(organization);
		return dispatchControl;
	}

}
