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
import com.lord.small_box.exceptions.ItemNotFoundException;

@Component
public class TextToDispatch {

	private final String patternDateBackSlash = "^(([0-9]{2})*(/){1}){2}([0-9]{4})";
	private final String patternType = "(MEMO|NOTA|EXP|HCD)";
	private final String patternDocCumber = "[0-9]{1,6}/[0-9]{2}";;
	private final String patternDesc = "^(([a-z])+(\s)*?)*";
	private final String patternVolumeNumber = "((Alcance:)[0-9])";

	public List<DispatchControlDto> textToDispatch(List<String> pdfText) throws ParseException {
		String[] lines = pdfText.stream().collect(Collectors.joining("")).split(" ");
		List<DispatchItemDto> dispatchItemDtos = mapItems(pdfText);
		List<DispatchControlDto> dispatchs = new ArrayList<>();
		ListIterator<DispatchItemDto> it = dispatchItemDtos.listIterator();
		it.forEachRemaining(d -> {
			DispatchControlDto dispatchControlDto = new DispatchControlDto();
			dispatchControlDto.setDate(getTextDate(lines));
			dispatchControlDto.setToDependency(getToDependency(pdfText));
			dispatchControlDto.setType(d.getItemType());
			dispatchControlDto.setDocNumber(d.getItemNumber());
			dispatchControlDto.setVolumeNumber(d.getVolumeNumber());
			dispatchControlDto.setDescription(d.getDescription());
			dispatchs.add(dispatchControlDto);
		});
		return dispatchs;
	}

	private List<DispatchItemDto> mapItems(List<String> pdfText) {
		ListIterator<String> it = pdfText.listIterator();
		List<DispatchItemDto> items = new ArrayList<DispatchItemDto>();
		it.forEachRemaining(t -> {
			DispatchItemDto dto = new DispatchItemDto();
			if (t.trim().startsWith("Tipo Expediente")) {
				dto.setItemNumber(getItemNumber(t));
				dto.setItemType(getItemType(t));
				dto.setVolumeNumber(getVolumeNumber(t));
				String descResult = pdfText.get(it.nextIndex() + 4);
				dto.setDescription(getDescription(descResult));
				items.add(dto);
			}

		});
		return items;
	}

	private String getDescription(String line) {
		String result = line.substring(line.indexOf(':') + 1, line.length()).trim();
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
	private String getToDependency(List<String> pdfText) {
		return pdfText.stream()
		.filter(f -> f.toLowerCase()
				.contains("oficina destino"))
		.map(m -> m.substring(m.indexOf('o')+1).trim())
		.findFirst().orElseThrow(()-> new ItemNotFoundException("No se encontro la dependencia"));
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
	private String getVolumeNumber(String line) {
		String[] lines = line.split(" ");
		for(String s:lines) {
			if(s.trim().matches(patternVolumeNumber)) {
				return Character.toString(s.charAt(s.indexOf(':') +1));
			}
		}
		return null;
	}
	

}
