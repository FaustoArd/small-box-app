package com.lord.small_box.text_analisys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lord.small_box.dtos.DispatchControlDto;

@Component
public class TextToDispatch {

	private final String patternDateBackSlash = "^(([0-9]{2})*(/){1}){2}([0-9]{4})";
	private final String patternDateNoDash = "^(([0-9]{2})*){2}([0-9]{4})";
	private final String patternType = "(MEMO|NOTA|EXP)";
	private final String patternDocCumber = "[0-9]{1,6}/[0-9]{2}";;
	private final String patternDesc = "^(([a-z])+(\s)*?)*";

	public DispatchControlDto textToDispatch(List<String> pdfText) throws ParseException {
		// pdfText.forEach(e -> System.out.println(e));
		String[] lines = pdfText.stream().collect(Collectors.joining("")).split(" ");
		//Arrays.stream(lines).forEach(e -> System.out.println(e));
		DispatchControlDto dispatchControlDto = new DispatchControlDto();
		dispatchControlDto.setDate(getTextDate(lines));
		dispatchControlDto.setType(getItemType(lines).stream().map(m -> m).collect(Collectors.joining("")));
		dispatchControlDto.setDocNumber(getItemNumber(lines));
		return dispatchControlDto;
	}

	private String getItemNumber(String[] lines) {
		//String[] lineSplitted = Arrays.stream(lines).map(m -> m.split("")).collect(Collectors.to)
		for(String s:lines) {
			if(s.replace(".", "").replace(":", "").strip().matches(patternDocCumber)) {
				System.out.println(s);
				return s.replace(".", "").replace(":", "").strip();
			}
		}
		return null;
	}

	private ArrayList<String> getItemType(String[] lines) {
		ArrayList<String> items = new ArrayList<>();
		for(String  s:lines) {
			if(s.replace(".", "").replace(":", "").strip().toUpperCase().matches(patternType)) {
				items.add( s.replace(".", "").replace(":", "").strip().toUpperCase());
				
				}
			}
		return items;
	}

	private Calendar getTextDate(String[] lines) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		for (String s : lines) {
			s = s.replaceAll("[a-zA-Z]","").strip();
			if (Pattern.matches(patternDateBackSlash, s)) {
				try {
					s= s.replace("/", "-");
					cal.setTime(sdf.parse(s));
					return cal;
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		}
		return null;
	}
	

}
