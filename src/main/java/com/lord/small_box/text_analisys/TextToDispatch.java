package com.lord.small_box.text_analisys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lord.small_box.dtos.DispatchControlDto;
import com.lord.small_box.dtos.DispatchItemDto;

@Component
public class TextToDispatch {

	private final String patternDateBackSlash = "^(([0-9]{2})*(/){1}){2}([0-9]{4})";
	private final String patternDateNoDash = "^(([0-9]{2})*){2}([0-9]{4})";
	private final String patternType = "(MEMO|NOTA|EXP|HCD)";
	private final String patternDocCumber = "[0-9]{1,6}/[0-9]{2}";;
	private final String patternDesc = "^(([a-z])+(\s)*?)*";

	public List<DispatchControlDto> textToDispatch(List<String> pdfText) throws ParseException {
		// pdfText.forEach(e -> System.out.println(e));
		String[] lines = pdfText.stream().collect(Collectors.joining("")).split(" ");
		// Arrays.stream(lines).forEach(e -> System.out.println(e));

		List<DispatchItemDto> dispatchItemDtos = mapItems(pdfText);
		List<DispatchControlDto> dispatchs = new ArrayList<>();
		ListIterator<DispatchItemDto> it = dispatchItemDtos.listIterator();

		it.forEachRemaining(d -> {
			DispatchControlDto dispatchControlDto = new DispatchControlDto();
			dispatchControlDto.setDate(getTextDate(lines));
			dispatchControlDto.setToDependency(getToDependency(lines));
			dispatchControlDto.setType(d.getItemType());
			dispatchControlDto.setDocNumber(d.getItemNumber());
			dispatchControlDto.setDescription(d.getDescription());
			dispatchs.add(dispatchControlDto);
		});
		return dispatchs;
	}

	private List<DispatchItemDto> mapItems(List<String> pdfText) {
		// pdfText.forEach(e -> System.out.println(e + e.length()));
		ListIterator<String> it = pdfText.listIterator();
		List<DispatchItemDto> items = new ArrayList<DispatchItemDto>();
		it.forEachRemaining(t -> {
			DispatchItemDto dto = new DispatchItemDto();
			if (t.trim().startsWith("Tipo Expediente")) {
				dto.setItemNumber(getItemNumber(t));
				dto.setItemType(getItemType(t));
				String descResult = pdfText.get(it.nextIndex() + 4);
				dto.setDescription(getDescription(descResult));
				items.add(dto);
			}

		});
		return items;
	}

	private String getDescription(String line) {
		System.out.println("Description: " + line);
		String result = line.substring(line.indexOf(':') + 1, line.length()).trim();
		System.out.println("Description: " + result);
		return result;
	}
	
	private Calendar getTextDate(String[] lines) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		for (String s : lines) {
			s = s.replaceAll("[a-zA-Z]", "").strip();
			if (Pattern.matches(patternDateBackSlash, s)) {
				try {
					s = s.replace("/", "-");
					cal.setTime(sdf.parse(s));
					return cal;
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		}
		return null;
	}
	private String getToDependency(String[]lines) {
		for(String s:lines) {
			if(s.toLowerCase().contains("oficina origen")) {
				String result = s.substring(s.indexOf('n') + 1, s.length()).trim();
				return result;
			}
		}
		return null;
	}

	private String getItemType(String line) {
		String[] lines = line.split(" ");
		for (String s : lines) {
			s = s.replace(".", "").replace(":", "").strip().toUpperCase();
			if (s.matches(patternType)) {
				return s;
			}

		}
		return null;
	}

	private String getItemNumber(String line) {
		String[] lines = line.split(" ");
		for (String s : lines) {
			s = s.replace(".", "").replace(":", "").replaceAll("[a-zA-Z]", "").strip();
			if (s.matches(patternDocCumber)) {
				return s;
			}
		}
		return null;
	}
	

}
